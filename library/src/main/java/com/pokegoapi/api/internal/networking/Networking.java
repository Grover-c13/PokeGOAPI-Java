package com.pokegoapi.api.internal.networking;

import POGOProtos.Enums.PlatformOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Networking.Envelopes.AuthTicketOuterClass.AuthTicket;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass.ResponseEnvelope;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.DownloadRemoteConfigVersionMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetAssetDigestMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass.GetMapObjectsMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.RequestOuterClass;
import POGOProtos.Networking.Requests.RequestOuterClass.Request.Builder;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.DownloadRemoteConfigVersionResponseOuterClass.DownloadRemoteConfigVersionResponse;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import POGOProtos.Networking.Responses.GetAssetDigestResponseOuterClass.GetAssetDigestResponse;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass.GetMapObjectsResponse;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.exceptions.AccountBannedException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;

/**
 * Created by paul on 21-8-2016.
 */
public final class Networking implements Runnable {
	private long lastRequest = System.currentTimeMillis();
	private static final String HASH = "2788184af4004004d6ab0720f7612983332106f6";
	private static final Map<String, Networking> INSTANCES = new HashMap<>();
	private final Random random = new Random();
	private final ExecutorService executorService;
	private final OkHttpClient client;
	private final Location location;
	private final Callback callback;
	private final Signature signature;
	private URL currentServer;
	private AuthTicket authTicket;
	private Long lastInventoryCheck = null;

	public static Networking getInstance(URL initialServer, ExecutorService executorService, OkHttpClient client, Location location, DeviceInfo deviceInfo,
										 SensorInfo sensorInfo, Callback callback) {
		String serverString = initialServer.toExternalForm();
		if (!INSTANCES.containsKey(serverString)) {
			synchronized (INSTANCES) {
				if (!INSTANCES.containsKey(serverString)) {
					INSTANCES.put(serverString, new Networking(initialServer, executorService, client, location, deviceInfo,
							sensorInfo, callback));
				}
			}
		}
		return INSTANCES.get(serverString);
	}

	private Networking(URL initialServer, ExecutorService executorService, OkHttpClient client, Location location, DeviceInfo deviceInfo,
					   SensorInfo sensorInfo, Callback callback) {
		this.executorService = executorService;
		this.client = client;
		this.location = location;
		this.callback = callback;
		this.signature = new Signature(location, deviceInfo, sensorInfo);
		this.currentServer = initialServer;
	}

	public BootstrapResult bootstrap(AuthInfo authInfo) {
		long requestId = Math.abs(random.nextLong());
		// First call to niantic is GET_PLAYER. This call will return the URL to use for future requests
		// Also the auth ticket is returned
		GetPlayerMessageOuterClass.GetPlayerMessage getPlayerMessage = GetPlayerMessageOuterClass.GetPlayerMessage.newBuilder().build();
		Builder reqBuilder = RequestOuterClass.Request.newBuilder();
		reqBuilder.setRequestMessage(getPlayerMessage.toByteString());
		reqBuilder.setRequestType(RequestType.GET_PLAYER);
		RequestEnvelope.Builder getPlayerRequest = RequestEnvelope.newBuilder()
				.setStatusCode(2)
				.setRequestId(requestId)
				.setRequests(0, reqBuilder)
				.setLatitude(location.getLatitude())
				.setLongitude(location.getLongitude())
				.setAltitude(location.getAltitude())
				.setAuthInfo(authInfo)
				.setUnknown12(getUnknown12());
		signature.setSignature(getPlayerRequest);
		ResponseEnvelope response = doRequest(getPlayerRequest.build());
		try {
			currentServer = new URL("https://" + response.getApiUrl() + "/rpc");
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Received invalid URL from server. Giving up", e);
		}
		response = handleRequest(getPlayerRequest.build());
		GetPlayerResponse playerResponse;
		try {
			playerResponse = GetPlayerResponse.parseFrom(response.getReturns(0));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}
		sleep(300);
		RequestEnvelope.Builder remoteCondigRequest = buildRequestEnvalope(RequestType.DOWNLOAD_REMOTE_CONFIG_VERSION, DownloadRemoteConfigVersionMessageOuterClass.DownloadRemoteConfigVersionMessage
				.newBuilder()
				.setPlatform(PlatformOuterClass.Platform.ANDROID)
				.setAppVersion(3300)
				.build());
		response = handleRequest(remoteCondigRequest.build());
		DownloadRemoteConfigVersionResponse downloadRemoteConfigVersionResponse;
		GetInventoryResponse inventoryResponse;
		GetHatchedEggsResponse hatchedEggsResponse;
		CheckAwardedBadgesResponse checkAwardedBadgesResponse;
		DownloadSettingsResponse downloadSettingsResponse;
		GetAssetDigestResponse assetDigestResponse;
		LevelUpRewardsResponse levelUpRewardsResponse;
		GetMapObjectsResponse getMapObjectsResponse;
		try {
			downloadRemoteConfigVersionResponse = DownloadRemoteConfigVersionResponse.parseFrom(response.getReturns(0));
			hatchedEggsResponse = GetHatchedEggsResponse.parseFrom(response.getReturns(1));
			inventoryResponse = GetInventoryResponse.parseFrom(response.getReturns(2));
			checkAwardedBadgesResponse = CheckAwardedBadgesResponse.parseFrom(response.getReturns(3));
			downloadSettingsResponse = DownloadSettingsResponse.parseFrom(response.getReturns(4));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}
		sleep(3000);
		RequestEnvelope.Builder assetsRequest = buildRequestEnvalope(RequestType.GET_ASSET_DIGEST, GetAssetDigestMessageOuterClass.GetAssetDigestMessage
				.newBuilder()
				.setPlatform(PlatformOuterClass.Platform.ANDROID)
				.setAppVersion(3300)
				.build());
		response = handleRequest(assetsRequest.build());
		try {
			assetDigestResponse = GetAssetDigestResponse.parseFrom(response.getReturns(0));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}
		int playerLevel = 1;
		for (InventoryItemOuterClass.InventoryItem inventoryItem : inventoryResponse.getInventoryDelta().getInventoryItemsList()) {
			if (inventoryItem.getInventoryItemData().hasPlayerStats()) {
				playerLevel = inventoryItem.getInventoryItemData().getPlayerStats().getLevel();
			}
		}
		sleep(3000);
		RequestEnvelope.Builder levelUpRequest = buildRequestEnvalope(RequestType.LEVEL_UP_REWARDS, LevelUpRewardsMessage
				.newBuilder()
				.setLevel(playerLevel)
				.build());
		response = handleRequest(levelUpRequest.build());
		try {
			levelUpRewardsResponse = LevelUpRewardsResponse.parseFrom(response.getReturns(0));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}

		// Initial map request
		List<Long> cellIds = com.pokegoapi.api.map.Map.getCellIds(location.getLatitude(), location.getLongitude(), com.pokegoapi.api.map.Map.CELL_WIDTH);

		sleep(300);
		GetMapObjectsMessage.Builder builder = GetMapObjectsMessage.newBuilder();
		for (int i=0;i!=9;i++) {
			builder.setCellId(i, cellIds.get(i));
			builder.setSinceTimestampMs(i, 0);
		}
		RequestEnvelope.Builder initialMapRequest = buildRequestEnvalope(RequestType.GET_MAP_OBJECTS, builder.setLatitude(location.getLatitude()).setLongitude(location.getLongitude())
				.build());
		response = handleRequest(initialMapRequest.build());
		try {
			getMapObjectsResponse = GetMapObjectsResponse.parseFrom(response.getReturns(0));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}

		return new BootstrapResult(playerResponse, downloadRemoteConfigVersionResponse, inventoryResponse, hatchedEggsResponse, checkAwardedBadgesResponse, downloadSettingsResponse, assetDigestResponse, levelUpRewardsResponse, getMapObjectsResponse);
	}
	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		}
		catch (InterruptedException e) {
			throw new RuntimeException("Can't wait. Shutting down?", e);
		}
	}

	@Override
	public void run() {

	}

	private ResponseEnvelope doRequest(RequestEnvelope request) throws RemoteServerException {
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
				.url(currentServer)
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
			authTicket = responseEnvelop.getAuthTicket();
			return responseEnvelop;
		} catch (IOException e) {
			throw new RemoteServerException(e);
		}
	}

	private synchronized ResponseEnvelope handleRequest(RequestEnvelope request) {
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

		// TODO: Fix this, put in networking thread
		long wait = Math.max(0, System.currentTimeMillis() - (lastRequest + 300));
		try {
			Thread.sleep(wait);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		lastRequest = System.currentTimeMillis();

		RequestEnvelope.Builder request = buildRequestEnvalope(requestType, message);
		ResponseEnvelope responseEnvelope = doRequest(request.build());
		try {
			T responseMessage = (T)responseType.newInstance().getParserForType().parseFrom(responseEnvelope.getReturns(0));
			final GetHatchedEggsResponse getHatchedEggsResponse = GetHatchedEggsResponse.parseFrom(responseEnvelope.getReturns(1));
			final GetInventoryResponse getInventoryResponse = GetInventoryResponse.parseFrom(responseEnvelope.getReturns(2));
			final CheckAwardedBadgesResponse checkAwardedBadgesResponse = CheckAwardedBadgesResponse.parseFrom(responseEnvelope.getReturns(3));
			final DownloadSettingsResponse downloadSettingsResponse = DownloadSettingsResponse.parseFrom(responseEnvelope.getReturns(4));
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					callback.update(getHatchedEggsResponse, getInventoryResponse, checkAwardedBadgesResponse, downloadSettingsResponse);
				}
			});
			return Observable.just(responseMessage);
		} catch (IllegalAccessException | InstantiationException e) {
			throw new RuntimeException("All hope is lost");
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	private RequestEnvelope.Builder buildRequestEnvalope(RequestType requestType,
									  GeneratedMessage message) {
		long requestId = Math.abs(random.nextLong());
		RequestEnvelope.Builder request = RequestEnvelope.newBuilder()
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
		return request;
	}


	private static Builder wrap(RequestType requestType, GeneratedMessage message) {
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
