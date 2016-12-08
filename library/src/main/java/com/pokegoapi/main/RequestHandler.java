/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.main;

import POGOProtos.Networking.Envelopes.AuthTicketOuterClass.AuthTicket;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass.ResponseEnvelope;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.AsyncCaptchaActiveException;
import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.AsyncHelper;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.Signature;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

public class RequestHandler implements Runnable {
	private static final String TAG = RequestHandler.class.getSimpleName();
	private final PokemonGo api;
	private final Thread asyncHttpThread;
	private final BlockingQueue<AsyncServerRequest> workQueue = new LinkedBlockingQueue<>();
	private final Map<Long, ResultOrException> resultMap = new ConcurrentHashMap<>();
	private String apiEndpoint;
	private OkHttpClient client;
	private AtomicLong requestId = new AtomicLong(System.currentTimeMillis());
	private Random random;

	/**
	 * Instantiates a new Request handler.
	 *
	 * @param api the api
	 * @param client the client
	 */
	public RequestHandler(PokemonGo api, OkHttpClient client) {
		this.api = api;
		this.client = client;
		apiEndpoint = ApiSettings.API_ENDPOINT;
		asyncHttpThread = new Thread(this, "Async HTTP Thread");
		asyncHttpThread.setDaemon(true);
		asyncHttpThread.start();
		random = new Random();
	}

	/**
	 * Make an async server request. The answer will be provided in the future
	 *
	 * @param asyncServerRequest Request to make
	 * @return ByteString response to be processed in the future
	 */
	public Observable<ByteString> sendAsyncServerRequests(final AsyncServerRequest asyncServerRequest) {
		workQueue.offer(asyncServerRequest);
		return Observable.from(new Future<ByteString>() {
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				return false;
			}

			@Override
			public boolean isCancelled() {
				return false;
			}

			@Override
			public boolean isDone() {
				return resultMap.containsKey(asyncServerRequest.getId());
			}

			@Override
			public ByteString get() throws InterruptedException, ExecutionException {
				ResultOrException resultOrException = getResult(1, TimeUnit.MINUTES);
				while (resultOrException == null) {
					resultOrException = getResult(1, TimeUnit.MINUTES);
				}
				if (resultOrException.getException() != null) {
					throw new ExecutionException(resultOrException.getException());
				}
				return resultOrException.getResult();
			}

			@Override
			public ByteString get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				ResultOrException resultOrException = getResult(timeout, unit);
				if (resultOrException == null) {
					throw new TimeoutException("No result found");
				}
				if (resultOrException.getException() != null) {
					throw new ExecutionException(resultOrException.getException());
				}
				return resultOrException.getResult();

			}

			private ResultOrException getResult(long timeout, TimeUnit timeUnit) throws InterruptedException {
				long wait = api.currentTimeMillis() + timeUnit.toMillis(timeout);
				while (!isDone()) {
					Thread.sleep(10);
					if (wait < api.currentTimeMillis()) {
						return null;
					}
				}
				return resultMap.remove(asyncServerRequest.getId());
			}
		});
	}

	/**
	 * Sends multiple ServerRequests in a thread safe manner.
	 *
	 * @param serverRequests list of ServerRequests to be sent
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException the login failed exception
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void sendServerRequests(ServerRequest... serverRequests)
			throws RemoteServerException, LoginFailedException, CaptchaActiveException {
		List<Observable<ByteString>> observables = new ArrayList<>(serverRequests.length);
		for (ServerRequest request : serverRequests) {
			AsyncServerRequest asyncServerRequest = new AsyncServerRequest(request.getType(), request.getRequest())
					.withCommons(request.isRequireCommon());
			observables.add(sendAsyncServerRequests(asyncServerRequest));
		}
		for (int i = 0; i != serverRequests.length; i++) {
			serverRequests[i].handleData(AsyncHelper.toBlocking(observables.get(i)));
		}
	}

	/**
	 * Sends multiple ServerRequests in a thread safe manner.
	 *
	 * @param serverRequests list of ServerRequests to be sent
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException the login failed exception
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	private AuthTicket internalSendServerRequests(AuthTicket authTicket, ServerRequest... serverRequests)
			throws RemoteServerException, CaptchaActiveException, LoginFailedException {
		AuthTicket newAuthTicket = authTicket;
		if (serverRequests.length == 0) {
			return authTicket;
		}
		RequestEnvelope.Builder builder = RequestEnvelope.newBuilder();
		resetBuilder(builder, authTicket);

		for (ServerRequest serverRequest : serverRequests) {
			builder.addRequests(serverRequest.getRequest());
		}

		Signature.setSignature(api, builder);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		RequestEnvelope request = builder.build();
		try {
			request.writeTo(stream);
		} catch (IOException e) {
			Log.wtf(TAG, "Failed to write request to bytearray ouput stream. This should never happen", e);
		}

		RequestBody body = RequestBody.create(null, stream.toByteArray());
		okhttp3.Request httpRequest = new okhttp3.Request.Builder()
				.url(apiEndpoint)
				.post(body)
				.build();

		try (Response response = client.newCall(httpRequest).execute()) {
			if (response.code() != 200) {
				throw new RemoteServerException("Got a unexpected http code : " + response.code());
			}

			ResponseEnvelope responseEnvelop;
			try (InputStream content = response.body().byteStream()) {
				responseEnvelop = ResponseEnvelope.parseFrom(content);
			} catch (IOException e) {
				// retrieved garbage from the server
				throw new RemoteServerException("Received malformed response : " + e);
			}

			if (responseEnvelop.getApiUrl() != null && responseEnvelop.getApiUrl().length() > 0) {
				apiEndpoint = "https://" + responseEnvelop.getApiUrl() + "/rpc";
			}

			if (responseEnvelop.hasAuthTicket()) {
				newAuthTicket = responseEnvelop.getAuthTicket();
			}

			if (responseEnvelop.getStatusCode() == ResponseEnvelope.StatusCode.INVALID_AUTH_TOKEN) {
				throw new LoginFailedException(String.format("Invalid Auth status code recieved, token not refreshed? %s %s",
						responseEnvelop.getApiUrl(), responseEnvelop.getError()));
			} else if (responseEnvelop.getStatusCode() == ResponseEnvelope.StatusCode.REDIRECT) {
				// 53 means that the api_endpoint was not correctly set, should be at this point, though, so redo the request
				return internalSendServerRequests(newAuthTicket, serverRequests);
			} else if (responseEnvelop.getStatusCode() == ResponseEnvelope.StatusCode.BAD_REQUEST) {
				throw new RemoteServerException("Your account may be banned! please try from the official client.");
			}


			/**
			 * map each reply to the numeric response,
			 * ie first response = first request and send back to the requests to toBlocking.
			 * */
			int count = 0;
			for (ByteString payload : responseEnvelop.getReturnsList()) {
				ServerRequest serverReq = serverRequests[count];
				/**
				 * TODO: Probably all other payloads are garbage as well in this case,
				 * so might as well throw an exception and leave this loop */
				if (payload != null) {
					serverReq.handleData(payload);
				}
				count++;
			}
		} catch (IOException e) {
			throw new RemoteServerException(e);
		} catch (RemoteServerException e) {
			// catch it, so the auto-close of resources triggers, but don't wrap it in yet another RemoteServer Exception
			throw e;
		}
		return newAuthTicket;
	}

	private void resetBuilder(RequestEnvelope.Builder builder, AuthTicket authTicket)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		builder.setStatusCode(2);
		builder.setRequestId(getRequestId());
		//builder.setAuthInfo(api.getAuthInfo());
		if (authTicket != null
				&& authTicket.getExpireTimestampMs() > 0
				&& authTicket.getExpireTimestampMs() > api.currentTimeMillis()) {
			builder.setAuthTicket(authTicket);
		} else {
			Log.d(TAG, "Authenticated with static token");
			builder.setAuthInfo(api.getAuthInfo());
		}
		builder.setMsSinceLastLocationfix(random.nextInt(1651) + 149);
		builder.setLatitude(api.getLatitude());
		builder.setLongitude(api.getLongitude());
		builder.setAccuracy(api.getAccuracy());
	}

	private Long getRequestId() {
		return requestId.getAndIncrement();
	}

	@Override
	public void run() {
		List<AsyncServerRequest> requests = new LinkedList<>();
		AuthTicket authTicket = null;
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new AsyncPokemonGoException("System shutdown", e);
			}
			if (workQueue.isEmpty()) {
				continue;
			}

			workQueue.drainTo(requests);

			boolean addCommon = false;

			ArrayList<ServerRequest> serverRequests = new ArrayList<>();
			Map<ServerRequest, AsyncServerRequest> requestMap = new HashMap<>();

			if (api.hasChallenge() & !api.isLoggingIn()) {
				for (AsyncServerRequest request : requests) {
					RequestTypeOuterClass.RequestType type = request.getType();
					if (type == RequestTypeOuterClass.RequestType.VERIFY_CHALLENGE) {
						ServerRequest serverRequest = new ServerRequest(type, request.getRequest());
						serverRequests.add(serverRequest);
						requestMap.put(serverRequest, request);
					} else {
						AsyncCaptchaActiveException exception = new AsyncCaptchaActiveException(api.getChallengeURL());
						ResultOrException error = ResultOrException.getError(exception);
						resultMap.put(request.getId(), error);
					}
				}
			} else {
				for (AsyncServerRequest request : requests) {
					ServerRequest serverRequest = new ServerRequest(request.getType(), request.getRequest());
					serverRequests.add(serverRequest);
					requestMap.put(serverRequest, request);
					if (request.isRequireCommonRequest()) {
						addCommon = true;
					}
				}
			}

			if (addCommon) {
				ServerRequest[] commonRequests = CommonRequests.getCommonRequests(api);
				Collections.addAll(serverRequests, commonRequests);
			}

			ServerRequest[] arrayServerRequests = serverRequests.toArray(new ServerRequest[serverRequests.size()]);

			try {
				authTicket = internalSendServerRequests(authTicket, arrayServerRequests);

				for (ServerRequest request : serverRequests) {
					AsyncServerRequest asyncRequest = requestMap.get(request);
					try {
						ByteString data = request.getData();
						if (asyncRequest != null) {
							resultMap.put(asyncRequest.getId(), ResultOrException.getResult(data));
						}
						CommonRequests.parse(api, request.getType(), data);
					} catch (InvalidProtocolBufferException e) {
						if (asyncRequest != null) {
							resultMap.put(asyncRequest.getId(), ResultOrException.getError(e));
						}
					}
				}
				continue;
			} catch (RemoteServerException | LoginFailedException | CaptchaActiveException e) {
				for (AsyncServerRequest request : requests) {
					resultMap.put(request.getId(), ResultOrException.getError(e));
				}
				continue;
			} finally {
				requests.clear();
			}
		}
	}


}
