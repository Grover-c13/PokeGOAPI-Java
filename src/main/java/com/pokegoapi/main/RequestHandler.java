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

import POGOProtos.Networking.Envelopes.AuthTicketOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.extern.slf4j.Slf4j;
import com.pokegoapi.util.FutureWrapper;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class RequestHandler implements Runnable {
	private final PokemonGo api;
	private String apiEndpoint;
	private OkHttpClient client;
	private Long requestId = new Random().nextLong();

	private final ExecutorService executorService = Executors.newFixedThreadPool(1);
	private final BlockingQueue<AsyncServerRequest> workQueue = new LinkedBlockingQueue<>();
	private final Map<Long, ResultOrException> resultMap = new HashMap<>();

	/**
	 * Instantiates a new Request handler.
	 *
	 * @param api    the api
	 * @param client the client
     * @throws LoginFailedException When login fails
     * @throws RemoteServerException If request errors occur
	 */
	public RequestHandler(PokemonGo api, OkHttpClient client) throws LoginFailedException, RemoteServerException {
		this.api = api;
		this.client = client;
		apiEndpoint = ApiSettings.API_ENDPOINT;
		executorService.submit(this);
	}

	/**
	 * Make an async server request. The answer will be provided in the future
	 *
	 * @param serverRequest Request to make
	 * @return ByteString response to be processed in the future
	 */
	public Future<ByteString> sendAsyncServerRequests(final AsyncServerRequest serverRequest) {
		workQueue.offer(serverRequest);
		return new Future<ByteString>() {
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
				return resultMap.containsKey(serverRequest.getId());
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

			private ResultOrException getResult(long timeouut, TimeUnit timeUnit) throws InterruptedException {
				long wait = System.currentTimeMillis() + timeUnit.toMillis(timeouut);
				while (!isDone()) {
					Thread.sleep(10);
					if (wait < System.currentTimeMillis()) {
						return null;
					}
				}
				return resultMap.remove(serverRequest.getId());
			}
		};
	}

	/**
	 * Sends multiple ServerRequests in a thread safe manner.
	 *
	 * @param serverRequests list of ServerRequests to be sent
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	public void sendServerRequests(ServerRequest... serverRequests) throws RemoteServerException, LoginFailedException {
		List<Future<ByteString>> futures = new ArrayList<>(serverRequests.length);
		for (ServerRequest request : serverRequests) {
			AsyncServerRequest asyncServerRequest = new AsyncServerRequest(request.getType(), request.getRequest());
			futures.add(sendAsyncServerRequests(asyncServerRequest));
		}
		for (int i = 0; i != serverRequests.length; i++) {
			serverRequests[i].handleData(FutureWrapper.toBlocking(futures.get(i)));
		}
	}

	/**
	 * Sends multiple ServerRequests in a thread safe manner.
	 *
	 * @param serverRequests list of ServerRequests to be sent
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	private AuthTicketOuterClass.AuthTicket internalSendServerRequests(AuthTicketOuterClass.AuthTicket authTicket,
			ServerRequest... serverRequests)
			throws RemoteServerException, LoginFailedException {
		AuthTicketOuterClass.AuthTicket newAuthTicket = authTicket;
		if (serverRequests.length == 0) {
			return authTicket;
		}
		RequestEnvelopeOuterClass.RequestEnvelope.Builder builder = RequestEnvelopeOuterClass.RequestEnvelope
				.newBuilder();
		resetBuilder(builder, authTicket);

		for (ServerRequest serverRequest : serverRequests) {
			builder.addRequests(serverRequest.getRequest());
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		RequestEnvelopeOuterClass.RequestEnvelope request = builder.build();
		try {
			request.writeTo(stream);
		} catch (IOException e) {
			log.error("Failed to write request to bytearray ouput stream. This should never happen", e);
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

			ResponseEnvelopeOuterClass.ResponseEnvelope responseEnvelop;
			try (InputStream content = response.body().byteStream()) {
				responseEnvelop = ResponseEnvelopeOuterClass.ResponseEnvelope.parseFrom(content);
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

			if (responseEnvelop.getStatusCode() == 102) {
				throw new LoginFailedException(String.format("Error %s in API Url %s",
						responseEnvelop.getApiUrl(), responseEnvelop.getError()));
			} else if (responseEnvelop.getStatusCode() == 53) {
				// 53 means that the api_endpoint was not correctly set, should be at this point, though, so redo the request
				return internalSendServerRequests(newAuthTicket, serverRequests);
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

	private void resetBuilder(RequestEnvelopeOuterClass.RequestEnvelope.Builder builder,
								AuthTicketOuterClass.AuthTicket authTicket)
			throws LoginFailedException, RemoteServerException {
		builder.setStatusCode(2);
		builder.setRequestId(getRequestId());
		if (authTicket != null
				&& authTicket.getExpireTimestampMs() > 0
				&& authTicket.getExpireTimestampMs() > api.currentTimeMillis()) {
			builder.setAuthTicket(authTicket);
		} else {
			log.debug("Authenticated with static token");
			builder.setAuthInfo(api.getAuthInfo());
		}
		builder.setUnknown12(989);
		builder.setLatitude(api.getLatitude());
		builder.setLongitude(api.getLongitude());
		builder.setAltitude(api.getAltitude());
	}

	private Long getRequestId() {
		return ++requestId;
	}

	@Override
	public void run() {
		List<AsyncServerRequest> requests = new LinkedList<>();
		AuthTicketOuterClass.AuthTicket authTicket = null;
		while (true) {
			try {
				Thread.sleep(350);
			} catch (InterruptedException e) {
				throw new AsyncPokemonGoException("System shutdown", e);
			}
			if (workQueue.isEmpty()) {
				continue;
			}
			workQueue.drainTo(requests);
			ServerRequest[] serverRequests = new ServerRequest[requests.size()];
			for (int i = 0; i != requests.size(); i++) {
				serverRequests[i] = new ServerRequest(requests.get(i).getType(), requests.get(i).getRequest());
			}
			try {
				authTicket = internalSendServerRequests(authTicket, serverRequests);
				for (int i = 0; i != requests.size(); i++) {
					try {
						resultMap.put(requests.get(i).getId(), ResultOrException.getResult(serverRequests[i].getData()));
					} catch (InvalidProtocolBufferException e) {
						resultMap.put(requests.get(i).getId(), ResultOrException.getError(e));
					}
				}
				continue;
			} catch (RemoteServerException | LoginFailedException e) {
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
