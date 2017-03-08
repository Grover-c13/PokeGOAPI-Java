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
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.PlatformRequest;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.PlatformRequest.Builder;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass.ResponseEnvelope;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass.ResponseEnvelope.PlatformResponse;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass.ResponseEnvelope.StatusCode;
import POGOProtos.Networking.Requests.RequestOuterClass.Request;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.request.BadRequestException;
import com.pokegoapi.exceptions.request.BannedException;
import com.pokegoapi.exceptions.request.InvalidCredentialsException;
import com.pokegoapi.exceptions.request.LoginFailedException;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.util.AsyncHelper;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.Signature;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestHandler implements Runnable {
	private static final int THROTTLE = 350;
	private static final String TAG = RequestHandler.class.getSimpleName();
	private final PokemonGo api;
	private final Thread asyncHttpThread;
	private final BlockingQueue<ServerRequestEnvelope> workQueue = new LinkedBlockingQueue<>();
	private String apiEndpoint;
	private OkHttpClient client;
	private Random random;
	private AuthTicket authTicket;

	private boolean active = true;

	private RequestIdGenerator requestIdGenerator = new RequestIdGenerator(16807);

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
	 * @param envelope the envelope to send
	 * @return ServerResponse response to be processed in the future
	 */
	public Observable<ServerResponse> sendAsyncServerRequests(ServerRequestEnvelope envelope) {
		workQueue.offer(envelope);
		return envelope.observable();
	}

	/**
	 * Sends a single ServerRequest asynchronously
	 *
	 * @param request the request to send
	 * @return the result from this request
	 */
	public Observable<ByteString> sendAsyncServerRequests(final ServerRequest request) {
		return sendAsyncServerRequests(request, false);
	}

	/**
	 * Sends a single ServerRequest asynchronously
	 *
	 * @param request the request to send
	 * @param commons whether this request should include commons
	 * @param commonExclusions the common requests to exclude from this request
	 * @return the result from this request
	 */
	public Observable<ByteString> sendAsyncServerRequests(final ServerRequest request, boolean commons,
			RequestType... commonExclusions) {
		ServerRequestEnvelope envelope = ServerRequestEnvelope.create();
		envelope.add(request);
		envelope.setCommons(commons);
		envelope.excludeCommons(commonExclusions);
		return sendAsyncServerRequests(envelope).map(new Func1<ServerResponse, ByteString>() {
			@Override
			public ByteString call(ServerResponse serverResponse) {
				try {
					return request.getData();
				} catch (InvalidProtocolBufferException e) {
					return null;
				}
			}
		});
	}

	/**
	 * Sends ServerRequests in a thread safe manner.
	 *
	 * @param envelope list of ServerRequests to be sent
	 * @return the server response
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public ServerResponse sendServerRequests(ServerRequestEnvelope envelope)
			throws RequestFailedException {
		return AsyncHelper.toBlocking(sendAsyncServerRequests(envelope));
	}

	/**
	 * Sends a single ServerRequest without commons
	 *
	 * @param request the request to send
	 * @return the result from this request
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public ByteString sendServerRequests(ServerRequest request)
			throws RequestFailedException {
		return sendServerRequests(request, false);
	}


	/**
	 * Sends a single ServerRequest
	 *
	 * @param request the request to send
	 * @param commons whether this request should include commons
	 * @param commonExclusions the common requests to exclude from this request
	 * @return the result from this request
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public ByteString sendServerRequests(ServerRequest request, boolean commons, RequestType... commonExclusions)
			throws RequestFailedException {
		ServerRequestEnvelope envelope = ServerRequestEnvelope.create();
		envelope.add(request);
		envelope.setCommons(commons);
		envelope.excludeCommons(commonExclusions);
		AsyncHelper.toBlocking(sendAsyncServerRequests(envelope));
		try {
			return request.getData();
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * Builds and sends a request envelope
	 *
	 * @param serverResponse the response to append to
	 * @param requests list of ServerRequests to be sent
	 * @param platformRequests list of ServerPlatformRequests to be sent
	 * @throws RequestFailedException if this request fails to send
	 */
	private ServerResponse sendInternal(ServerResponse serverResponse, ServerRequest[] requests,
			ServerPlatformRequest[] platformRequests)
			throws RequestFailedException {
		RequestEnvelope.Builder builder = buildRequest(requests, platformRequests);

		return sendInternal(serverResponse, requests, platformRequests, builder);
	}

	/**
	 * Sends an already built request envelope
	 *
	 * @param serverResponse the response to append to
	 * @param requests list of ServerRequests to be sent
	 * @param platformRequests list of ServerPlatformRequests to be sent
	 * @param builder the request envelope builder
	 * @throws RequestFailedException if this message fails to send
	 */
	private ServerResponse sendInternal(ServerResponse serverResponse, ServerRequest[] requests,
			ServerPlatformRequest[] platformRequests, RequestEnvelope.Builder builder)
			throws RequestFailedException {
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
				throw new RequestFailedException("Got a unexpected http code : " + response.code());
			}

			ResponseEnvelope responseEnvelop;
			try (InputStream content = response.body().byteStream()) {
				responseEnvelop = ResponseEnvelope.parseFrom(content);
			} catch (IOException e) {
				// retrieved garbage from the server
				throw new RequestFailedException("Received malformed response : " + e);
			}

			if (responseEnvelop.getApiUrl() != null && responseEnvelop.getApiUrl().length() > 0) {
				apiEndpoint = "https://" + responseEnvelop.getApiUrl() + "/rpc";
			}

			if (responseEnvelop.hasAuthTicket()) {
				this.authTicket = responseEnvelop.getAuthTicket();
			}

			boolean empty = false;

			StatusCode statusCode = responseEnvelop.getStatusCode();

			if (statusCode != StatusCode.REDIRECT && statusCode != StatusCode.INVALID_AUTH_TOKEN) {
				for (int i = 0; i < responseEnvelop.getReturnsCount(); i++) {
					ByteString returned = responseEnvelop.getReturns(i);
					ServerRequest serverRequest = requests[i];
					if (returned != null) {
						serverResponse.addResponse(serverRequest.getType(), returned);
						if (serverRequest.getType() == RequestType.GET_PLAYER) {
							if (GetPlayerResponse.parseFrom(returned).getBanned()) {
								throw new BannedException("Cannot send request, your account has been banned!");
							}
						}
					} else {
						empty = true;
					}
				}
			}

			for (int i = 0; i < responseEnvelop.getPlatformReturnsCount(); i++) {
				PlatformResponse platformResponse = responseEnvelop.getPlatformReturns(i);
				ByteString returned = platformResponse.getResponse();
				if (returned != null) {
					serverResponse.addResponse(platformResponse.getType(), returned);
				}
			}

			if (statusCode != StatusCode.OK && statusCode != StatusCode.OK_RPC_URL_IN_RESPONSE) {
				if (statusCode == StatusCode.INVALID_AUTH_TOKEN) {
					try {
						authTicket = null;
						api.getAuthInfo(true);
						return sendInternal(serverResponse, requests, platformRequests);
					} catch (LoginFailedException | InvalidCredentialsException e) {
						throw new RequestFailedException("Failed to refresh auth token!", e);
					} catch (RequestFailedException e) {
						throw new RequestFailedException("Failed to send request with refreshed auth token!", e);
					}
				} else if (statusCode == StatusCode.REDIRECT) {
					// API_ENDPOINT was not correctly set, should be at this point, though, so redo the request
					return sendInternal(serverResponse, requests, platformRequests, builder);
				} else if (statusCode == StatusCode.BAD_REQUEST) {
					if (api.getPlayerProfile().isBanned()) {
						throw new BannedException("Cannot send request, your account has been banned!");
					} else {
						throw new BadRequestException("A bad request was sent!");
					}
				} else {
					throw new RequestFailedException("Failed to send request: " + statusCode);
				}
			}

			if (empty) {
				throw new RequestFailedException("Received empty response from server!");
			}
		} catch (IOException e) {
			throw new RequestFailedException(e);
		} catch (RequestFailedException e) {
			throw e;
		}

		return serverResponse;
	}

	private RequestEnvelope.Builder buildRequest(ServerRequest[] requests, ServerPlatformRequest[] platformRequests)
			throws RequestFailedException {
		RequestEnvelope.Builder builder = RequestEnvelope.newBuilder();
		resetBuilder(builder);

		for (ServerRequest serverRequest : requests) {
			ByteString data = serverRequest.getRequest().toByteString();
			Request request = Request.newBuilder()
					.setRequestMessage(data)
					.setRequestType(serverRequest.getType())
					.build();
			builder.addRequests(request);
		}

		Signature.setSignature(api, builder);

		for (ServerPlatformRequest platformRequest : platformRequests) {
			ByteString data = platformRequest.getRequest();
			Builder request = PlatformRequest.newBuilder()
					.setType(platformRequest.getType())
					.setRequestMessage(data);
			builder.addPlatformRequests(request);
		}
		return builder;
	}

	private void resetBuilder(RequestEnvelope.Builder builder)
			throws RequestFailedException {
		builder.setStatusCode(2);
		builder.setRequestId(requestIdGenerator.next());
		//builder.setAuthInfo(api.getAuthInfo());
		boolean refresh = authTicket != null && api.currentTimeMillis() >= authTicket.getExpireTimestampMs();
		if (authTicket != null && !refresh) {
			builder.setAuthTicket(authTicket);
		} else {
			Log.d(TAG, "Authenticated with static token");
			builder.setAuthInfo(api.getAuthInfo(refresh));
		}
		builder.setMsSinceLastLocationfix(random.nextInt(1651) + 149);
		builder.setLatitude(api.getLatitude());
		builder.setLongitude(api.getLongitude());
		builder.setAccuracy(api.getAccuracy());
	}

	@Override
	public void run() {
		long lastRequest = System.currentTimeMillis();

		while (active) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new AsyncPokemonGoException("System shutdown", e);
			}

			if (!workQueue.isEmpty()) {
				long time = System.currentTimeMillis();
				long timeSinceLastRequest = time - lastRequest;

				if (timeSinceLastRequest < THROTTLE) {
					try {
						Thread.sleep(THROTTLE - timeSinceLastRequest);
					} catch (InterruptedException e) {
						throw new AsyncPokemonGoException("System shutdown", e);
					}
				}

				ServerRequestEnvelope envelope = workQueue.poll();

				List<ServerRequest> requests = new ArrayList<>();

				Set<RequestType> exclusions = envelope.getCommonExclusions();

				ServerResponse response = new ServerResponse();

				if (api.hasChallenge() && !api.isLoggingIn()) {
					for (ServerRequest request : envelope.getRequests()) {
						RequestType type = request.getType();
						if (!exclusions.contains(type)) {
							if (type == RequestType.VERIFY_CHALLENGE || type == RequestType.CHECK_CHALLENGE) {
								requests.add(request);
							}
						}
					}
				} else {
					for (ServerRequest request : envelope.getRequests()) {
						RequestType type = request.getType();
						if (!exclusions.contains(type)) {
							requests.add(request);
						}
					}
				}

				if (envelope.isCommons()) {
					ServerRequest[] commonRequests = CommonRequests.getCommonRequests(api);
					for (ServerRequest request : commonRequests) {
						RequestType type = request.getType();
						if (CommonRequests.shouldAdd(api, type, envelope.getRequests()) && !exclusions.contains(type)) {
							requests.add(request);
							envelope.add(request);
						}
					}
				}

				ServerRequest[] arrayRequests = requests.toArray(new ServerRequest[requests.size()]);
				List<ServerPlatformRequest> platformRequests = envelope.getPlatformRequests();
				ServerPlatformRequest[] arrayPlatformRequests
						= platformRequests.toArray(new ServerPlatformRequest[platformRequests.size()]);

				try {
					response = sendInternal(response, arrayRequests, arrayPlatformRequests);
				} catch (RequestFailedException e) {
					response.setException(e);
				}

				envelope.handleResponse(response);

				try {
					for (ServerRequest request : requests) {
						ByteString result = request.getData();
						try {
							CommonRequests.queue(request.getType(), result);
						} catch (InvalidProtocolBufferException e) {
							break;
						}
					}

					CommonRequests.handleQueue(api);
				} catch (InvalidProtocolBufferException | RequestFailedException e) {
					continue;
				}

				lastRequest = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Stops this RequestHandler
	 */
	public void exit() {
		active = false;
	}
}
