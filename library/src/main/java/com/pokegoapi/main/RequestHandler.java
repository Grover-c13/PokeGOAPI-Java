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
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.Signature;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestHandler implements Runnable {
	private static final String TAG = RequestHandler.class.getSimpleName();
	private final PokemonGo api;
	private final Thread asyncHttpThread;
	private final BlockingQueue<AsyncServerRequest> workQueue = new LinkedBlockingQueue<>();
	private String apiEndpoint;
	private OkHttpClient client;
	private Long requestId = new Random().nextLong();

	/**
	 * Instantiates a new Request handler.
	 *
	 * @param api    the api
	 * @param client the client
	 */
	public RequestHandler(PokemonGo api, OkHttpClient client) {
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
	 * @param asyncServerRequest Request to make
	 * @return ByteString response to be processed in the future
	 */
	public void sendAsyncServerRequests(final AsyncServerRequest asyncServerRequest) {
		workQueue.offer(asyncServerRequest);
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

			if (responseEnvelop.getStatusCode() == 102) {
				throw new LoginFailedException(String.format("Invalid Auth status code recieved, token not refreshed? %s %s",
						responseEnvelop.getApiUrl(), responseEnvelop.getError()));
			} else if (responseEnvelop.getStatusCode() == 53) {
				// 53 means that the api_endpoint was not correctly set, should be at this point, though, so redo the request
				return internalSendServerRequests(newAuthTicket, serverRequests);
			} else if (responseEnvelop.getStatusCode() == 3) {
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
			throws LoginFailedException, RemoteServerException {
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
		builder.setMsSinceLastLocationfix(989);
		builder.setLatitude(api.getLatitude());
		builder.setLongitude(api.getLongitude());
		builder.setAltitude(api.getAltitude());
	}

	private Long getRequestId() {
		return ++requestId;
	}

	@Override
	public void run() {
		AsyncServerRequest<GeneratedMessage,Object> request = null;
		AuthTicket authTicket = null;
		while (true) {
			try {
				request = workQueue.take();
			} catch (Throwable e) {

			}

			if (request == null)
				continue;

			ArrayList<ServerRequest> serverRequests = new ArrayList<>();

			serverRequests.add(new ServerRequest(request.getType(), request.getRequest()));

			for (ServerRequest extra : request.getCommonRequests()) {
				serverRequests.add(extra);
			}

			ServerRequest[] arrayServerRequests = serverRequests.toArray(new ServerRequest[serverRequests.size()]);

			try {
				authTicket = internalSendServerRequests(authTicket, arrayServerRequests);

				try {
					request.fire(arrayServerRequests[0].getData());
				} catch (InvalidProtocolBufferException e) {
					request.fire(e);
				}

				// Assuming all the bunded requests are commons
				if (arrayServerRequests.length > 1) {
					for (int i = 1; i != arrayServerRequests.length; i++) {
						try {
							CommonRequest.parse(api, arrayServerRequests[i].getType(),
									arrayServerRequests[i].getData());
						} catch (InvalidProtocolBufferException e) {
							//TODO: notify error even in case of common requests?
						}
					}
				}
				continue;
			} catch (RemoteServerException | LoginFailedException e) {
				request.fire(e);
				continue;
			} finally {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {

				}
			}
		}
	}
}
