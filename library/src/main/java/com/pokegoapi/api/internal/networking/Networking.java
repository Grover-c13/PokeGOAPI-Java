package com.pokegoapi.api.internal.networking;

import POGOProtos.Networking.Envelopes.AuthTicketOuterClass.AuthTicket;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass.ResponseEnvelope;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass;
import POGOProtos.Networking.Requests.RequestOuterClass;
import POGOProtos.Networking.Requests.RequestOuterClass.Request.Builder;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.exceptions.AccountBannedException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.*;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by paul on 21-8-2016.
 */
public final class Networking {
	private static final String HASH = "2788184af4004004d6ab0720f7612983332106f6";
	private static final Map<String, Networking> INSTANCES = new HashMap<>();
	private final Random random = new Random();
	private final URL initialServer;
	private final OkHttpClient client;
	private final Location location;
	private final Callback callback;
	private final Signature signature;
	private URL currentServer;
	private AuthTicket authTicket;
	private Long lastInventoryCheck = null;

	public static Networking getInstance(URL initialServer, OkHttpClient client, Location location, DeviceInfo deviceInfo,
										 SensorInfo sensorInfo, Callback callback) {
		String serverString = initialServer.toExternalForm();
		if (!INSTANCES.containsKey(serverString)) {
			synchronized (INSTANCES) {
				if (!INSTANCES.containsKey(serverString)) {
					INSTANCES.put(serverString, new Networking(initialServer, client, location, deviceInfo,
							sensorInfo, callback));
				}
			}
		}
		return INSTANCES.get(serverString);
	}

	private Networking(URL initialServer, OkHttpClient client, Location location, DeviceInfo deviceInfo,
					   SensorInfo sensorInfo, Callback callback) {
		this.initialServer = initialServer;
		this.client = client;
		this.location = location;
		this.callback = callback;
		this.signature = new Signature(location, deviceInfo, sensorInfo);
	}

	public GetPlayerResponse initialize(AuthInfo authInfo) {
		long requestId = Math.abs(random.nextLong());
		// First call to niantic is GET_PLAYER. This call will return the URL to use for future requests
		// Also the auth ticket is returned
		GetPlayerMessageOuterClass.GetPlayerMessage getPlayerMessage = GetPlayerMessageOuterClass.GetPlayerMessage.newBuilder().build();
		Builder reqBuilder = RequestOuterClass.Request.newBuilder();
		reqBuilder.setRequestMessage(getPlayerMessage.toByteString());
		reqBuilder.setRequestType(RequestType.GET_PLAYER);
		RequestEnvelopeOuterClass.RequestEnvelope.Builder request = RequestEnvelopeOuterClass.RequestEnvelope.newBuilder()
				.setStatusCode(2)
				.setRequestId(requestId)
				.setRequests(0, reqBuilder)
				.setLatitude(location.getLatitude())
				.setLongitude(location.getLongitude())
				.setAltitude(location.getAltitude())
				.setAuthInfo(authInfo)
				.setUnknown12(getUnknown12());
		signature.setSignature(request);
		ResponseEnvelope response = doRequest(request.build());
		try {
			currentServer = new URL("https://" + response.getApiUrl() + "/rpc");
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Received invalid URL from server. Giving up", e);
		}
		authTicket = response.getAuthTicket();
		response = handleRequest(request.build());
		try {
			return GetPlayerResponse.parseFrom(response.getReturns(0));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}
	}

	private ResponseEnvelope doRequest(RequestEnvelopeOuterClass.RequestEnvelope request) throws RemoteServerException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			request.writeTo(stream);
		}
		catch (IOException e) {
			// If this happens we can just stop
			throw new RuntimeException(e);
		}
		RequestBody body = RequestBody.create(null, stream.toByteArray());
		okhttp3.Request httpRequest = new okhttp3.Request.Builder()
				.url(initialServer)
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
			return responseEnvelop;
		} catch (IOException e) {
			throw new RemoteServerException(e);
		}
	}

	private synchronized ResponseEnvelope handleRequest(RequestEnvelopeOuterClass.RequestEnvelope request) {
		ResponseEnvelope responseEnvelop = doRequest(request);
		if (responseEnvelop.hasAuthTicket()) {
			authTicket = responseEnvelop.getAuthTicket();
		}

		if (responseEnvelop.getStatusCode() == 102) {
			throw new LoginFailedException(String.format("Invalid Auth status code recieved, token not refreshed? %s %s",
					responseEnvelop.getApiUrl(), responseEnvelop.getError()));
		} else if (responseEnvelop.getStatusCode() == 3) {
			throw new AccountBannedException();
		}
		return responseEnvelop;
	}

	private long getUnknown12() {
		return random.nextInt(6000);
	}

	public interface Callback {
		void update(GetHatchedEggsResponse getHatchedEggsResponse,
					GetInventoryResponse getInventoryResponse,
					CheckAwardedBadgesResponse checkAwardedBadgesResponse,
					DownloadSettingsResponse downloadSettingsResponse);
	}

	public <T extends GeneratedMessage> Observable<T> queueRequest(RequestType requestType,
																   GeneratedMessage message, Class<T> responseType) {
		long requestId = Math.abs(random.nextLong());
		// After networking has been setup with get player, every network request will be appended with, except for one which sets unknown6 request type to 5
		// - 126: GET_HATCHED_EGGS
		// - 4: GET_INVENTORY
		// - 129: CHECK_AWARDED_BADGES
		// - 5: DOWNLOAD_SETTINGS
		RequestEnvelopeOuterClass.RequestEnvelope.Builder request = RequestEnvelopeOuterClass.RequestEnvelope.newBuilder()
				.setStatusCode(2)
				.setRequestId(requestId)
				.addRequests(wrap(requestType, message))
				.addRequests(getHatchedEggs())
				.addRequests(getInventory())
				.addRequests(getCheckAwardedBAtches())
				.addRequests(getDownloadSettings())
				.setLatitude(location.getLatitude())
				.setLongitude(location.getLongitude())
				.setAltitude(location.getAltitude())
				.setAuthTicket(authTicket)
				.setUnknown12(getUnknown12());
		signature.setSignature(request);


		return Observable.empty();
	}

	private static Builder wrap(RequestType requestType,
														  GeneratedMessage message) {
		Builder reqBuilder = RequestOuterClass.Request.newBuilder();
		reqBuilder.setRequestMessage(message.toByteString());
		reqBuilder.setRequestType(requestType);
		return reqBuilder;
	}

	private static Builder getHatchedEggs() {
		return wrap(RequestType.GET_HATCHED_EGGS, GetHatchedEggsMessageOuterClass.GetHatchedEggsMessage.newBuilder().build());
	}

	private Builder getInventory() {
		Builder inventory;
		if (lastInventoryCheck == null) {
			inventory = wrap(RequestType.GET_INVENTORY, GetInventoryMessageOuterClass.GetInventoryMessage.newBuilder().build());
		}
		else {
			inventory = wrap(RequestType.GET_INVENTORY, GetInventoryMessageOuterClass.GetInventoryMessage.newBuilder().setLastTimestampMs(lastInventoryCheck).build());
		}
		lastInventoryCheck = System.currentTimeMillis();
		return inventory;
	}

	private static Builder getCheckAwardedBAtches() {
		return wrap(RequestType.CHECK_AWARDED_BADGES, CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage.newBuilder().build());
	}

	private static Builder getDownloadSettings() {
		return wrap(RequestType.DOWNLOAD_SETTINGS, DownloadSettingsMessageOuterClass.DownloadSettingsMessage.newBuilder().setHash(HASH).build());
	}
}
