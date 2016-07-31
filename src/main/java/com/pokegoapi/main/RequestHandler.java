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
import rx.functions.Func1;
import rx.observables.BlockingObservable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RequestHandler {
	private static final String TAG = RequestHandler.class.getSimpleName();
	private final ThreadPoolExecutor threadPoolExecutor =
			new ThreadPoolExecutor(1,3,500, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3));

	private final PokemonGo api;
	private final OkHttpClient client;
	private String apiEndpoint = ApiSettings.API_ENDPOINT;
	private AuthTicket lastAuth = null;
	private Long requestId = new Random().nextLong();
	private Long lastRequestMs = 0L;


	public RequestHandler(final PokemonGo api, final OkHttpClient client) {
		this.api = api;
		this.client = client;
	}

	private boolean hasValidAuthTicket() {
		return (getLastAuth() != null
				&& getLastAuth().getExpireTimestampMs() > 0
				&& getLastAuth().getExpireTimestampMs() > getApi().currentTimeMillis());
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

	public synchronized AuthTicket getLastAuth() {
		return lastAuth;
	}

	public synchronized void setLastAuth(AuthTicket lastAuth) {
		this.lastAuth = lastAuth;
	}

	/**
	 * Implements a small delay between API requests to prevent throttling.
	 */
	private synchronized void holdUntilClear() {
		long current = System.currentTimeMillis();
		long diff = current - lastRequestMs;
		if (diff >= 300) {
			lastRequestMs = current;
		}

		try {
			Thread.sleep(300);
			lastRequestMs = System.currentTimeMillis();
		} catch (InterruptedException ex) {
			Log.e(TAG, "Request hold interrupted");
		}
	}

	private void resetBuilder(RequestEnvelopeOuterClass.RequestEnvelope.Builder builder,
							  AuthTicketOuterClass.AuthTicket authTicket)
			throws LoginFailedException, RemoteServerException {
		builder.setStatusCode(2);
		builder.setRequestId(getRequestId());

		if (hasValidAuthTicket()) {
			builder.setAuthTicket(authTicket);
		} else {
			Log.d(TAG, "Authenticated with static token");
			builder.setAuthInfo(getApi().getAuthInfo());
		}
		builder.setUnknown12(989);
		builder.setLatitude(getApi().getLatitude());
		builder.setLongitude(getApi().getLongitude());
		builder.setAltitude(getApi().getAltitude());
	}

	public ServerRequest[] sendServerRequests(final ServerRequest... serverRequests)
			throws RemoteServerException, LoginFailedException {
		return sendServerRequests(getLastAuth(), serverRequests);
	}

	/**
	 * Sends multiple ServerRequests synchrously.
	 *
	 * @param serverRequests list of ServerRequests to be sent
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	public ServerRequest[] sendServerRequests(
			AuthTicketOuterClass.AuthTicket authTicket, ServerRequest... serverRequests)
			throws RemoteServerException, LoginFailedException {

		AuthTicketOuterClass.AuthTicket newAuthTicket = authTicket;

		if (serverRequests.length == 0) {
			return serverRequests;
		}

		RequestEnvelopeOuterClass.RequestEnvelope.Builder builder =
				RequestEnvelopeOuterClass.RequestEnvelope.newBuilder();

		resetBuilder(builder, authTicket);

		for (ServerRequest serverRequest : serverRequests) { builder.addRequests(serverRequest.getRequest()); }

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		RequestEnvelopeOuterClass.RequestEnvelope request = builder.build();

		try {
			request.writeTo(stream);
		}
		catch(IOException ex)
		{
			throw new RemoteServerException("Could not build RequestEnvelope", ex);
		}

		RequestBody body = RequestBody.create(null, stream.toByteArray());
		okhttp3.Request httpRequest = new okhttp3.Request.Builder()
				.url(getApiEndpoint())
				.post(body)
				.build();

		holdUntilClear();

		try (Response response = getClient().newCall(httpRequest).execute()) {
			if (response.code() != 200) {
				throw new RemoteServerException("Got a unexpected http code : " + response.code());
			}

			ResponseEnvelopeOuterClass.ResponseEnvelope resEnvelope;
			try (InputStream content = response.body().byteStream()) {
				resEnvelope = ResponseEnvelopeOuterClass.ResponseEnvelope.parseFrom(content);
			} catch (IOException e) {
				throw new InvalidProtocolBufferException("Malformed response received: " + e);
			}

			if (resEnvelope.getApiUrl() != null && resEnvelope.getApiUrl().length() > 0) {
				String newApiEndpoint = "https://" + resEnvelope.getApiUrl() + "/rpc";
				if(!newApiEndpoint.equalsIgnoreCase(getApiEndpoint())) {
					setApiEndpoint(newApiEndpoint);
				}
			}

			if (resEnvelope.hasAuthTicket()) {
				newAuthTicket = resEnvelope.getAuthTicket();
				setLastAuth(newAuthTicket);
			}

			if (resEnvelope.getStatusCode() == 102) {
				throw new LoginFailedException(String.format("Error %s in API Url %s",
						resEnvelope.getApiUrl(), resEnvelope.getError()));
			} else if (resEnvelope.getStatusCode() == 53) {
				// 53 means that the api_endpoint was not correctly set,
				// should be at this point, though, so redo the request
				return sendServerRequests(newAuthTicket, serverRequests);
			}

			int count = 0;
			for (ByteString payload : resEnvelope.getReturnsList()) {
				ServerRequest serverReq = serverRequests[count++];
				if (payload != null && !payload.isEmpty()) {
					serverReq.handleData(payload);
				} else { throw new InvalidProtocolBufferException("ServerRequest payload is empty!"); }
			}

			return serverRequests;
		} catch(InvalidProtocolBufferException e) {
			Log.v(TAG, "Retrying requests due to InvalidProtocolBufferException");
			return sendServerRequests(newAuthTicket, serverRequests);
		} catch (IOException e) {
			throw new RemoteServerException(e);
		}catch (RemoteServerException e) {
			if(e.getCause() instanceof InvalidProtocolBufferException)
			{
				Log.v(TAG, "Retrying requests due to error: " + e.getMessage());
				return sendServerRequests(newAuthTicket, serverRequests);
			}

			throw e;
		}
	}

	/**
	 * Asynchronously sends the request batch.
	 *
	 * @param serverRequests The requests to to send to the remote server.
	 * @return An Observable event to react to.
	 */
	public BlockingObservable<ServerRequest[]> sendAsyncServerRequests(final ServerRequest... serverRequests)
	{
		return Observable.defer(new Func0<Observable<ServerRequest[]>>() {
			@Override
			public Observable<ServerRequest[]> call() {
				try {
					return Observable.just(sendServerRequests(getLastAuth(), serverRequests));
				}
				catch(Exception ex)
				{
					return Observable.error(ex);
				}
			}
		})
		.delay(100L, TimeUnit.MILLISECONDS)
		// .observeOn(Schedulers.from(threadPoolExecutor))
		.retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
			@Override
			public Observable<?> call(Observable<? extends Throwable> observable) {
				return observable.flatMap(new Func1<Throwable, Observable<?>>() {
					@Override
					public Observable<?> call(Throwable throwable) {
						System.err.println("Determining if a retry should happen...");
						if(throwable instanceof InvalidProtocolBufferException
							|| throwable.getCause() instanceof InvalidProtocolBufferException) {
							try {
								holdUntilClear();
								return Observable.just(sendServerRequests(serverRequests));
							} catch(Exception ex) {
								return Observable.error(ex);
							}
						}
						System.err.println("No retry");
						return Observable.error(throwable);
					}
				});
			}
		}).toBlocking();
	}
}
