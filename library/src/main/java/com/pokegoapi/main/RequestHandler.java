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
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.AsyncPokemonGoException;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class RequestHandler implements Runnable {
	private static final String TAG = RequestHandler.class.getSimpleName();
	private final PokemonGo api;
	private final Thread asyncHttpThread;
	private final BlockingQueue<PokemonRequest> requestQueue = new LinkedBlockingQueue<>();
	private String apiEndpoint;
	private OkHttpClient client;
	private AtomicLong nextRequestID = new AtomicLong(System.currentTimeMillis());
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
	 * Sends an array or requests. Callbacks must be set on the PokemonRequest object with PokemonRequest#withCallback
	 *
	 * @param requests the requests to send
	 */
	public void sendRequests(final PokemonRequest... requests) {
		for (PokemonRequest request : requests) {
			sendRequest(request);
		}
	}

	/**
	 * Sends the given request.
	 *
	 * @param request the request to send
	 */
	public void sendRequest(final PokemonRequest request) {
		sendRequest(request, null);
	}

	/**
	 * Sends the given request, with a callback, if not already set on the PokemonRequest object.
	 *
	 * @param request the request to send
	 * @param callback the callback to use
	 */
	public void sendRequest(final PokemonRequest request, final RequestCallback callback) {
		if (callback != null && request.getCallback() == null) {
			request.withCallback(callback);
		}
		requestQueue.offer(request);
	}

	/**
	 * Sends multiple PokemonRequests in a thread-safe manner.
	 *
	 * @param pokemonRequests requests to be sent
	 * @throws RemoteServerException if the server fails
	 * @throws LoginFailedException if the login fails
	 */
	private AuthTicket internalSendRequests(AuthTicket authTicket, PokemonRequest... pokemonRequests)
			throws RemoteServerException, LoginFailedException {
		AuthTicket newAuthTicket = authTicket;
		if (pokemonRequests.length == 0) {
			return authTicket;
		}
		RequestEnvelope.Builder builder = RequestEnvelope.newBuilder();
		resetBuilder(builder, authTicket);

		for (PokemonRequest pokemonRequest : pokemonRequests) {
			builder.addRequests(pokemonRequest.getRequest());
		}

		Signature.setSignature(api, builder);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		RequestEnvelope request = builder.build();
		try {
			request.writeTo(stream);
		} catch (IOException e) {
			Log.wtf(TAG, "Failed to write request to byte array output stream. This should never happen", e);
		}

		RequestBody body = RequestBody.create(null, stream.toByteArray());
		okhttp3.Request httpRequest = new okhttp3.Request.Builder()
				.url(apiEndpoint)
				.post(body)
				.build();

		try (Response response = client.newCall(httpRequest).execute()) {
			if (response.code() != 200) {
				throw new RemoteServerException("Received an unexpected http code : " + response.code());
			}

			ResponseEnvelope responseEnvelop;
			try (InputStream content = response.body().byteStream()) {
				responseEnvelop = ResponseEnvelope.parseFrom(content);
			} catch (IOException e) {
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
				return internalSendRequests(newAuthTicket, pokemonRequests);
			} else if (responseEnvelop.getStatusCode() == ResponseEnvelope.StatusCode.BAD_REQUEST) {
				throw new RemoteServerException("Your account may be banned! please try from the official client.");
			}

			int index = 0;
			for (ByteString responseData : responseEnvelop.getReturnsList()) {
				PokemonRequest pokemonRequest = pokemonRequests[index];
				/*
				 * TODO: Probably all other payloads are garbage as well in this case,
				 * so might as well throw an exception and leave this loop
				*/
				final RequestCallback callback = pokemonRequest.getCallback();
				if (callback != null) {
					final PokemonResponse pokemonResponse =
							responseData != null ?
									PokemonResponse.getResult(responseData) :
									PokemonResponse.getError(new InvalidProtocolBufferException("Null response"));
					callback.handleResponse(pokemonResponse);
				}
				index++;
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
		builder.setRequestId(getNextRequestID());
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

	private Long getNextRequestID() {
		return nextRequestID.getAndIncrement();
	}

	@Override
	public void run() {
		List<PokemonRequest> pokemonRequests = new LinkedList<>();
		AuthTicket authTicket = null;
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new AsyncPokemonGoException("System shutdown", e);
			}
			if (requestQueue.isEmpty() || api.hasChallenge()) {
				continue;
			}

			requestQueue.drainTo(pokemonRequests);

			boolean requiresCommon = false;
			for (PokemonRequest request : pokemonRequests) {
				if (request.isRequireCommonRequest()) {
					requiresCommon = true;
				}
			}

			PokemonRequest[] commonRequests;

			if (requiresCommon) {
				commonRequests = CommonRequests.getCommonRequests(api);
				Collections.addAll(pokemonRequests, commonRequests);
			}

			PokemonRequest[] requestArray = pokemonRequests.toArray(new PokemonRequest[pokemonRequests.size()]);

			try {
				authTicket = internalSendRequests(authTicket, requestArray);
				continue;
			} catch (RemoteServerException | LoginFailedException e) {
			} finally {
				pokemonRequests.clear();
			}
		}
	}


}
