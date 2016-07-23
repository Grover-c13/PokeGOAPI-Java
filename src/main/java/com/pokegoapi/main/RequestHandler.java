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
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass;
import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler {
	private static final String TAG = RequestHandler.class.getSimpleName();
	private final PokemonGo api;
	private RequestEnvelopeOuterClass.RequestEnvelope.Builder builder;
	private boolean hasRequests;
	private RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth;
	private List<ServerRequest> serverRequests;
	private String apiEndpoint;
	private OkHttpClient client;

	private AuthTicketOuterClass.AuthTicket lastAuth;

	/**
	 * Instantiates a new Request handler.
	 *
	 * @param api    the api
	 * @param auth   the auth
	 * @param client the client
	 */
	public RequestHandler(PokemonGo api, RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth, OkHttpClient client) {
		this.api = api;
		this.client = client;
		apiEndpoint = ApiSettings.API_ENDPOINT;
		this.auth = auth;
		serverRequests = new ArrayList<>();
		/* TODO: somehow fix it so people using the deprecated functions will still work,
		   while not calling this deprecated stuff ourselves */
		resetBuilder();
	}

	/**
	 * Request.
	 *
	 * @param requestIn the request in
	 */
	@Deprecated
	public void request(ServerRequest requestIn) {
		hasRequests = true;
		serverRequests.add(requestIn);
		builder.addRequests(requestIn.getRequest());
	}

	/**
	 * Sends multiple ServerRequests in a thread safe manner.
	 *
	 * @param serverRequests list of ServerRequests to be sent
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	public void sendServerRequests(ServerRequest... serverRequests) throws RemoteServerException, LoginFailedException {
		if (serverRequests.length == 0) {
			return;
		}
		RequestEnvelopeOuterClass.RequestEnvelope.Builder builder = RequestEnvelopeOuterClass.RequestEnvelope.newBuilder();
		resetBuilder(builder);

		for (ServerRequest serverRequest : serverRequests) {
			builder.addRequests(serverRequest.getRequest());
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		RequestEnvelopeOuterClass.RequestEnvelope request = builder.build();
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
				lastAuth = responseEnvelop.getAuthTicket();
			}

			if (responseEnvelop.getStatusCode() == 102) {
				throw new LoginFailedException();
			} else if (responseEnvelop.getStatusCode() == 53) {
				// 53 means that the api_endpoint was not correctly set, should be at this point, though, so redo the request
				sendServerRequests(serverRequests);
				return;
			}

			/**
			 * map each reply to the numeric response,
			 * ie first response = first request and send back to the requests to handle.
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
	}

	/**
	 * Send server requests.
	 *
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	@Deprecated
	public void sendServerRequests() throws RemoteServerException, LoginFailedException {
		setLatitude(api.getLatitude());
		setLongitude(api.getLongitude());
		setAltitude(api.getAltitude());

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		RequestEnvelopeOuterClass.RequestEnvelope request = builder.build();
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
		Response response;
		try {
			response = client.newCall(httpRequest).execute();
		} catch (IOException e) {
			throw new RemoteServerException(e);
		}

		if (response.code() != 200) {
			throw new RemoteServerException("Got a unexcepted http code : " + response.code());
		}

		ResponseEnvelopeOuterClass.ResponseEnvelope responseEnvelop = null;
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
			lastAuth = responseEnvelop.getAuthTicket();
		}

		if (responseEnvelop.getStatusCode() == 102) {
			throw new LoginFailedException();
		} else if (responseEnvelop.getStatusCode() == 53) {
			// 53 means that the apiEndpoint was not correctly set, should be at this point, though, so redo the request
			sendServerRequests();
			return;
		}

		// map each reply to the numeric response,
		// ie first response = first request and send back to the requests to handle.
		int count = 0;
		for (ByteString payload : responseEnvelop.getReturnsList()) {
			ServerRequest serverReq = serverRequests.get(count);
			// TODO: Probably all other payloads are garbage as well in this case, so might as well throw an exception
			if (payload != null) {
				serverReq.handleData(payload);
			}
			count++;
		}

		resetBuilder();
	}

	@Deprecated
	private void resetBuilder() {
		builder = RequestEnvelopeOuterClass.RequestEnvelope.newBuilder();
		resetBuilder(builder);
		hasRequests = false;
		serverRequests.clear();
	}

	private void resetBuilder(RequestEnvelopeOuterClass.RequestEnvelope.Builder builder) {
		builder.setStatusCode(2);
		builder.setRequestId(8145806132888207460L);
		if (lastAuth != null
				&& lastAuth.getExpireTimestampMs() > 0
				&& lastAuth.getExpireTimestampMs() > System.currentTimeMillis()) {
			builder.setAuthTicket(lastAuth);
		} else {
			Log.d(TAG, "Authenticated with static token");
			builder.setAuthInfo(auth);
		}
		builder.setUnknown12(989);
		builder.setLatitude(api.getLatitude());
		builder.setLongitude(api.getLongitude());
		builder.setAltitude(api.getAltitude());
	}


	/**
	 * Build request envelope outer class . request envelope.
	 *
	 * @return the request envelope outer class . request envelope
	 */
	public RequestEnvelopeOuterClass.RequestEnvelope build() {
		if (!hasRequests) {
			throw new IllegalStateException("Attempting to send request envelop with no requests");
		}
		return builder.build();
	}

	public void setAuthInfo(AuthInfo auth) {
		this.auth = auth;
		this.lastAuth = null;
	}

	public void setLatitude(double latitude) {
		builder.setLatitude(latitude);
	}

	public void setLongitude(double longitude) {
		builder.setLongitude(longitude);
	}

	public void setAltitude(double altitude) {
		builder.setAltitude(altitude);
	}

}
