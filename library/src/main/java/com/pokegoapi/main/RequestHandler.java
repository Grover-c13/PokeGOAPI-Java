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
import POGOProtos.Networking.Envelopes.AuthTicketOuterClass.AuthTicket;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass.ResponseEnvelope;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Envelopes.Unknown6OuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.AsyncHelper;
import com.pokegoapi.util.Log;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;

public class RequestHandler implements Runnable {
	private static final String TAG = RequestHandler.class.getSimpleName();
	private final PokemonGo api;
	private final Thread asyncHttpThread;
	private final BlockingQueue<AsyncServerRequest> workQueue = new LinkedBlockingQueue<>();
	private final Map<Long, ResultOrException> resultMap = new HashMap<>();
	private String apiEndpoint;
	private OkHttpClient client;
	private Long requestId = new Random().nextLong();

	/**
	 * Instantiates a new Request handler.
	 *
	 * @param api    the api
	 * @param client the client
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException If request errors occur
	 */
	public RequestHandler(PokemonGo api, OkHttpClient client) throws LoginFailedException, RemoteServerException {
		this.api = api;
		this.client = client;
		apiEndpoint = ApiSettings.API_ENDPOINT;
		asyncHttpThread = new Thread(this, "Async HTTP Thread");
		asyncHttpThread.setDaemon(true);
		asyncHttpThread.start();
	}

	/**
	 * Make an async server request. The answer will be provided in the future
	 *
	 * @param serverRequest Request to make
	 * @return ByteString response to be processed in the future
	 */
	public Observable<ByteString> sendAsyncServerRequests(final AsyncServerRequest serverRequest) {
		workQueue.offer(serverRequest);
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
				long wait = api.currentTimeMillis() + timeUnit.toMillis(timeouut);
				while (!isDone()) {
					Thread.sleep(10);
					if (wait < api.currentTimeMillis()) {
						return null;
					}
				}
				return resultMap.remove(serverRequest.getId());
			}
		});
	}

	/**
	 * Sends multiple ServerRequests in a thread safe manner.
	 *
	 * @param serverRequests list of ServerRequests to be sent
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	public void sendServerRequests(ServerRequest... serverRequests) throws RemoteServerException, LoginFailedException {
		List<Observable<ByteString>> observables = new ArrayList<>(serverRequests.length);
		for (ServerRequest request : serverRequests) {
			AsyncServerRequest asyncServerRequest = new AsyncServerRequest(request.getType(), request.getRequest());
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
	 * @throws LoginFailedException  the login failed exception
	 */
	private AuthTicket internalSendServerRequests(AuthTicket authTicket, ServerRequest... serverRequests)
			throws RemoteServerException, LoginFailedException {
		AuthTicket newAuthTicket = authTicket;
		if (serverRequests.length == 0) {
			return authTicket;
		}
		RequestEnvelope.Builder builder = RequestEnvelope.newBuilder();
		resetBuilder(builder, authTicket);

		for (ServerRequest serverRequest : serverRequests) {
			builder.addRequests(serverRequest.getRequest());
		}

		setSignature(authTicket, builder, serverRequests);

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

			if (responseEnvelop.getStatusCode() == 102) {
				throw new LoginFailedException(String.format("Invalid Auth status code recieved, token not refreshed?",
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

	private void resetBuilder(RequestEnvelope.Builder builder,
							  AuthTicket authTicket)
			throws LoginFailedException, RemoteServerException {
		builder.setStatusCode(2);
		builder.setRequestId(getRequestId());
		builder.setAuthInfo(api.getAuthInfo());
		/*if (authTicket != null
				&& authTicket.getExpireTimestampMs() > 0
				&& authTicket.getExpireTimestampMs() > api.currentTimeMillis()) {
			builder.setAuthTicket(authTicket);
		} else {
			Log.d(TAG, "Authenticated with static token");
			builder.setAuthInfo(api.getAuthInfo());
		}*/
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
		AuthTicket authTicket = null;
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

	private void setSignature(AuthTicket authTicket, RequestEnvelope.Builder builder, ServerRequest[] serverRequests) {
		if (authTicket == null) {
			System.out.println("Ticket == null");
			return;
		}
		byte[] uk22 = new byte[32];
		new Random().nextBytes(uk22);

		long curTime = api.currentTimeMillis();

		byte[] authTicketBA = authTicket.toByteArray();

		SignatureOuterClass.Signature.Builder sigBuilder = SignatureOuterClass.Signature.newBuilder()
				.setLocationHash1(getLocationHash1(authTicketBA, builder))
				.setLocationHash2(getLocationHash2(builder))
				.setUnk22(ByteString.copyFrom(uk22))
				.setTimestamp(api.currentTimeMillis())
				.setTimestampSinceStart(curTime - api.startTime);


		for (ServerRequest serverRequest : serverRequests) {
			byte[] request = serverRequest.getRequest().toByteArray();
			sigBuilder.addRequestHash(getRequestHash(authTicketBA, request));
		}

		// TODO: Call encrypt function on this
		byte[] uk2 = sigBuilder.build().toByteArray();
		byte[] encrypted = encrypt(uk2);
		System.out.println(uk2);
		System.out.println(encrypted);
		Unknown6OuterClass.Unknown6.newBuilder()
				.setRequestType(6)
				.setUnknown2(Unknown6OuterClass.Unknown6.Unknown2.newBuilder().setUnknown1(ByteString.copyFrom(encrypted)));
	}

	private int getLocationHash1(byte[] authTicket, RequestEnvelope.Builder builder) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		StreamingXXHash32 xx32 = factory.newStreamingHash32(0x1B845238);
		xx32.update(authTicket, 0, authTicket.length);
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(builder.getLatitude());

		xx32.update(bytes, 0, 8);
		bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(builder.getLongitude());

		xx32.update(bytes, 0, 8);
		bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(builder.getAltitude());

		xx32.update(bytes, 0, 8);
		return xx32.getValue();
	}

	private int getLocationHash2(RequestEnvelope.Builder builder) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		StreamingXXHash32 xx32 = factory.newStreamingHash32(0x1B845238);
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(builder.getLatitude());

		xx32.update(bytes, 0, 8);
		bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(builder.getLongitude());

		xx32.update(bytes, 0, 8);
		bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(builder.getAltitude());

		xx32.update(bytes, 0, 8);
		return xx32.getValue();
	}

	private long getRequestHash(byte[] authTicket, byte[] request) {
		XXHashFactory factory = XXHashFactory.fastestInstance();
		StreamingXXHash64 xx64 = factory.newStreamingHash64(0x1B845238);
		xx64.update(authTicket, 0, authTicket.length);
		xx64.update(request, 0, request.length);
		return xx64.getValue();
	}

	private interface Encrypt extends Library {
		int encrypt(byte[] input, int input_size,
					byte[] iv, int iv_size,
					byte[] output, WinDef.ULONGByReference output_size);

	}

	private static byte[] encrypt(byte[] input) {
		Encrypt encrypt =
				(Encrypt) Native.loadLibrary("encrypt", Encrypt.class);
		byte[] iv = new byte[32];
		new Random().nextBytes(iv);
		WinDef.ULONGByReference outputLength = new WinDef.ULONGByReference();
		int rv = encrypt.encrypt(input, input.length, iv, iv.length, null, outputLength);
		if (rv != 0) {
			throw new RuntimeException("Encrypt failed: " + rv);
		}

		byte[] output = new byte[outputLength.getValue().intValue()];
		rv = encrypt.encrypt(input, input.length, iv, iv.length, output, outputLength);
		if (rv != 0) {
			throw new RuntimeException("Encrypt failed: " + rv);
		}
		return output;
	}
}
