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
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Func0;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class RequestHandler {
	private static final String TAG = RequestHandler.class.getSimpleName();
	private final PokemonGo api;
	private final OkHttpClient client;
	private String apiEndpoint = ApiSettings.API_ENDPOINT;
	private AuthTicketOuterClass.AuthTicket lastAuth;
	private Long requestId = new Random().nextLong();
	private Long lastRequestMs = 0L;

	public RequestHandler(final PokemonGo api, final OkHttpClient client) {
		this.api = api;
		this.client = client;
	}

	private boolean hasValidAuthTicket() {
		return !(getLastAuth() != null && getLastAuth().getExpireTimestampMs() > 0
				&& getLastAuth().getExpireTimestampMs() > getApi().currentTimeMillis());
	}

	private void resetBuilder(RequestEnvelopeOuterClass.RequestEnvelope.Builder builder)
			throws LoginFailedException, RemoteServerException {

		builder.setStatusCode(2);
		builder.setRequestId(getRequestId());

		if (!hasValidAuthTicket()) {
			builder.setAuthTicket(getLastAuth());
		} else {
			Log.d(TAG, "Authenticated with static token");
			builder.setAuthInfo(getApi().getAuthInfo());
		}

		builder.setUnknown12(989);
		builder.setLatitude(getApi().getLatitude());
		builder.setLongitude(getApi().getLongitude());
		builder.setAltitude(getApi().getAltitude());
	}

	private RequestEnvelopeOuterClass.RequestEnvelope.Builder newBuilder() {
		try {
			RequestEnvelopeOuterClass.RequestEnvelope.Builder builder =
					RequestEnvelopeOuterClass.RequestEnvelope.newBuilder();

			resetBuilder(builder);

			return builder;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Sends all the provided requests to the server.
	 *
	 * @param requests The requests to send.
	 * @return An Obvservable response object.
	 */
	public Observable<Response> sendServerRequests(final ServerRequest... requests) {

		if (requests.length == 0) {
			throw new IllegalArgumentException("No server requests were supplied!");
		}

		return Observable.defer(new Func0<Observable<Response>>() {
			@Override
			public Observable<Response> call() {
				holdUntilClear();

				final RequestEnvelopeOuterClass.RequestEnvelope.Builder builder = newBuilder();

				if (builder == null) {
					return Observable.error(new Exception("Could not create RequestEnvelope Builder"));
				}

				for (ServerRequest serverRequest : requests) {
					builder.addRequests(serverRequest.getRequest());
				}

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				final RequestEnvelopeOuterClass.RequestEnvelope request = builder.build();

				try {
					request.writeTo(stream);
				} catch (IOException e) {
					Log.wtf(TAG, "Failed to write request to bytearray ouput stream. This should never happen", e);
				}

				final RequestBody body = RequestBody.create(null, stream.toByteArray());
				final okhttp3.Request httpRequest =
						new okhttp3.Request.Builder().url(getApiEndpoint()).post(body).build();

				try (Response response = getClient().newCall(httpRequest).execute()) {
					if (response.code() != 200) {
						throw new RemoteServerException("Got a unexpected http code : " + response.code());
					}

					ResponseEnvelopeOuterClass.ResponseEnvelope resEnvelope;
					try (InputStream content = response.body().byteStream()) {
						resEnvelope = ResponseEnvelopeOuterClass.ResponseEnvelope.parseFrom(content);
					} catch (Exception e) {
						return Observable.error(new RemoteServerException("Received malformed response : " + e));
					}

					if (resEnvelope.getApiUrl() != null && resEnvelope.getApiUrl().length() > 0) {
						setApiEndpoint("https://" + resEnvelope.getApiUrl() + "/rpc");
					}

					if (resEnvelope.hasAuthTicket()) {
						setLastAuth(resEnvelope.getAuthTicket());
					}

					if (resEnvelope.getStatusCode() == 102) {
						throw new LoginFailedException(String.format("Error %s in API Url %s", resEnvelope.getApiUrl(),
								resEnvelope.getError()));
					} else if (resEnvelope.getStatusCode() == 53) {
						return sendServerRequests(requests);
					}

					int count = 0;
					for (ByteString payload : resEnvelope.getReturnsList()) {
						if (payload == null || payload.size() == 0) {
							throw new InvalidProtocolBufferException("Payload was empty!");
						}

						ServerRequest serverReq = requests[count++];
						serverReq.handleData(payload);
					}

					return Observable.just(response);
				} catch (InvalidProtocolBufferException | RemoteServerException ex) {
					if (ex instanceof InvalidProtocolBufferException
							|| ex.getCause() instanceof InvalidProtocolBufferException) {
						return sendServerRequests(requests);
					}

					return Observable.error(ex);
				} catch (LoginFailedException ex) {
					return Observable.error(ex);
				} catch (Exception ex) {
					return Observable.error(new RemoteServerException(ex));
				}
			}
		});
	}

	/**
	 * @return Increment and return the new request ID number.
	 */
	private synchronized Long getRequestId() {
		return ++requestId;
	}

	private synchronized void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public synchronized PokemonGo getApi() {
		return api;
	}

	public synchronized OkHttpClient getClient() {
		return client;
	}

	public synchronized String getApiEndpoint() {
		return apiEndpoint;
	}

	public synchronized void setApiEndpoint(String apiEndpoint) {
		this.apiEndpoint = apiEndpoint;
	}

	public synchronized AuthTicketOuterClass.AuthTicket getLastAuth() {
		return lastAuth;
	}

	public synchronized void setLastAuth(AuthTicketOuterClass.AuthTicket lastAuth) {
		this.lastAuth = lastAuth;
	}

	/**
	 * Implements a small delay between API requests to prevent throttling.
	 */
	private synchronized void holdUntilClear() {
		long current = System.currentTimeMillis();
		if ((current - lastRequestMs) >= 350) {
			lastRequestMs = current;
		}

		try {
			Thread.sleep(current - lastRequestMs);
			lastRequestMs = System.currentTimeMillis();
		} catch (InterruptedException ex) {
			Log.e(TAG, "Request hold interrupted");
		}
	}
}
