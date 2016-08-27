package com.pokegoapi.api.internal.networking;

import POGOProtos.Enums.PlatformOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass.ResponseEnvelope;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.DownloadItemTemplatesMessageOuterClass.DownloadItemTemplatesMessage;
import POGOProtos.Networking.Requests.Messages.DownloadRemoteConfigVersionMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetAssetDigestMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetDownloadUrlsMessageOuterClass.GetDownloadUrlsMessage;
import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass.GetMapObjectsMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.RequestOuterClass;
import POGOProtos.Networking.Requests.RequestOuterClass.Request.Builder;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.DownloadItemTemplatesResponseOuterClass.DownloadItemTemplatesResponse;
import POGOProtos.Networking.Responses.DownloadRemoteConfigVersionResponseOuterClass.DownloadRemoteConfigVersionResponse;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import POGOProtos.Networking.Responses.GetAssetDigestResponseOuterClass.GetAssetDigestResponse;
import POGOProtos.Networking.Responses.GetDownloadUrlsResponseOuterClass.GetDownloadUrlsResponse;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass.GetMapObjectsResponse;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import com.pokegoapi.api.device.ActivityStatus;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.functions.Func1;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;

/**
 * Created by paul on 21-8-2016.
 */
@Slf4j
public final class Networking {
	private static final int VERSION = 3500;
	private static final String HASH = "2788184af4004004d6ab0720f7612983332106f6";
	private static final Map<String, Networking> INSTANCES = new HashMap<>();
	private static final List<String> DOWNLOAD_URL_HASHES = Arrays.asList("d86c65f7-d521-4aa1-9324-d2690d8a61a0/1467337989725000",
			"d86c65f7-d521-4aa1-9324-d2690d8a61a0/1467337989725000", "5cbcb961-13dc-440c-a474-50212e6ff120/1467338119418000",
			"c918d441-8f37-4155-b824-0ed67f64cb5b/1467338237623000", "90f8eb5d-c398-4e9e-a53d-82c6367521db/1467338255908000",
			"5be4c90f-8b4b-49ce-92a1-6e1ffd591fda/1467338152232000");
	private final Random random = new Random();
	private final RequestScheduler requestScheduler;
	private final ExecutorService executorService;
	private final Location location;
	private final Callback callback;
	private final Signature signature;
	private final Locale locale;
	private Long lastInventoryCheck = null;

	public static Networking getInstance(URL initialServer, ExecutorService executorService, OkHttpClient client, Location location, DeviceInfo deviceInfo,
										 SensorInfo sensorInfo, ActivityStatus activityStatus, LocationFixes locationFixes, Callback callback, Locale locale) {
		String serverString = initialServer.toExternalForm();
		if (!INSTANCES.containsKey(serverString)) {
			synchronized (INSTANCES) {
				if (!INSTANCES.containsKey(serverString)) {
					INSTANCES.put(serverString, new Networking(initialServer, executorService, client, location, deviceInfo,
							sensorInfo, activityStatus, locationFixes, locale, callback));
				}
			}
		}
		return INSTANCES.get(serverString);
	}

	private Networking(URL initialServer, ExecutorService executorService, OkHttpClient client, Location location, DeviceInfo deviceInfo,
					   SensorInfo sensorInfo, ActivityStatus activityStatus, LocationFixes locationFixes, Locale locale, Callback callback) {
		this.executorService = executorService;
		this.location = location;
		this.callback = callback;
		this.signature = new Signature(location, deviceInfo, sensorInfo, activityStatus, locationFixes);
		this.locale = locale;
		this.requestScheduler = new RequestScheduler(executorService, client, initialServer);
	}

	public BootstrapResult bootstrap(AuthInfo authInfo) {
		long requestId = Math.abs(random.nextLong());
		// First call to niantic is GET_PLAYER. This call will return the URL to use for future requests
		// Also the auth ticket is returned
		log.info("Initial request, do a GET_PLAYER to get correct URL and auth ticket");
		GetPlayerMessageOuterClass.GetPlayerMessage getPlayerMessage = GetPlayerMessageOuterClass.GetPlayerMessage.newBuilder()
				.setPlayerLocale(GetPlayerMessageOuterClass.GetPlayerMessage.PlayerLocale.newBuilder().setCountry(locale.getCountry()).setLanguage(locale.getLanguage()))
				.build();

		Builder reqBuilder = RequestOuterClass.Request.newBuilder();
		reqBuilder.setRequestMessage(getPlayerMessage.toByteString());
		reqBuilder.setRequestType(RequestType.GET_PLAYER);

		RequestEnvelope.Builder getPlayerRequest = RequestEnvelope.newBuilder()
				.setStatusCode(2)
				.setRequestId(requestId)
				.addRequests(reqBuilder)
				.addRequests(RequestOuterClass.Request.newBuilder().setRequestTypeValue(600).build())
				.setLatitude(location.getLatitude())
				.setLongitude(location.getLongitude())
				.setAltitude(location.getAltitude())
				.setAuthInfo(authInfo)
				.setMsSinceLastLocationfix(getUnknown12());
		signature.setSignature(getPlayerRequest);
		ResponseEnvelope response = requestScheduler.queueRequest(getPlayerRequest.build()).toBlocking().first();
		try {
			requestScheduler.setCurrentServer(new URL("https://" + response.getApiUrl() + "/rpc"));
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Received invalid URL from server. Giving up", e);
		}
		sleep(300);
		response = requestScheduler.queueRequest(getPlayerRequest.build()).toBlocking().first();
		// Retry with new
		log.info("Do a GET_PLAYER to the new URL, and get player info");
		GetPlayerResponse playerResponse;
		try {
			playerResponse = GetPlayerResponse.parseFrom(response.getReturns(0));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}
		sleep(400);

		DownloadRemoteConfigVersionResponse downloadRemoteConfigVersionResponse;
		GetInventoryResponse inventoryResponse;
		GetHatchedEggsResponse hatchedEggsResponse;
		CheckAwardedBadgesResponse checkAwardedBadgesResponse;
		DownloadSettingsResponse downloadSettingsResponse;
		GetAssetDigestResponse assetDigestResponse;
		DownloadItemTemplatesResponse downloadItemTemplatesResponse;
		LevelUpRewardsResponse levelUpRewardsResponse;
		GetMapObjectsResponse getMapObjectsResponse;
		List<GetDownloadUrlsResponse> downloadUrlsResponses = new LinkedList<>();

		// Do a 7, 600, 126, 4, 129 and 5
		log.info("Do a DOWNLOAD_REMOTE_CONFIG_VERSION, 600, GET_HATCHED_EGGS, GET_INVENTORY, CHECK_AWARDED_BADGES and DOWNLOAD_SETTINGS");
		RequestEnvelope.Builder remoteConfigRequest = buildRequestEnvelope(RequestType.DOWNLOAD_REMOTE_CONFIG_VERSION, DownloadRemoteConfigVersionMessageOuterClass.DownloadRemoteConfigVersionMessage
				.newBuilder()
				.setPlatform(PlatformOuterClass.Platform.ANDROID)
				.setAppVersion(3300)
				.build());
		response = requestScheduler.queueRequest(remoteConfigRequest.build()).toBlocking().first();
		try {
			downloadRemoteConfigVersionResponse = DownloadRemoteConfigVersionResponse.parseFrom(response.getReturns(0));
			hatchedEggsResponse = GetHatchedEggsResponse.parseFrom(response.getReturns(2));
			inventoryResponse = GetInventoryResponse.parseFrom(response.getReturns(3));
			checkAwardedBadgesResponse = CheckAwardedBadgesResponse.parseFrom(response.getReturns(4));
			downloadSettingsResponse = DownloadSettingsResponse.parseFrom(response.getReturns(5));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}
		sleep(3000);
		log.info("Do a GET_ASSET_DIGEST, 600, GET_HATCHED_EGGS, GET_INVENTORY, CHECK_AWARDED_BADGES, DOWNLOAD_SETTINGS");
		RequestEnvelope.Builder assetsRequest = buildRequestEnvelope(RequestType.GET_ASSET_DIGEST, GetAssetDigestMessageOuterClass.GetAssetDigestMessage
				.newBuilder()
				.setPlatform(PlatformOuterClass.Platform.ANDROID)
				.setAppVersion(VERSION)
				.build());
		requestScheduler.queueRequest(assetsRequest.build()).toBlocking().first();

		int playerLevel = 1;
		for (InventoryItemOuterClass.InventoryItem inventoryItem : inventoryResponse.getInventoryDelta().getInventoryItemsList()) {
			if (inventoryItem.getInventoryItemData().hasPlayerStats()) {
				playerLevel = inventoryItem.getInventoryItemData().getPlayerStats().getLevel();
				break;
			}
		}
		sleep(3000);
		log.info("Do a DOWNLOAD_ITEM_TEMPLATES, GET_HATCHED_EGGS, GET_INVENTORY, CHECK_AWARDED_BADGES, DOWNLOAD_SETTINGS");
		RequestEnvelope.Builder downloadItemTemplatesRequest = buildRequestEnvelope(RequestType.DOWNLOAD_ITEM_TEMPLATES, DownloadItemTemplatesMessage
				.newBuilder()
				.build());
		response = requestScheduler.queueRequest(downloadItemTemplatesRequest.build()).toBlocking().first();
		try {
			downloadItemTemplatesResponse = DownloadItemTemplatesResponse.parseFrom(response.getReturns(0));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}

		sleep(3000);
		log.info("Do a LEVEL_UP_REWARDS, GET_HATCHED_EGGS, GET_INVENTORY, CHECK_AWARDED_BADGES, DOWNLOAD_SETTINGS");
		RequestEnvelope.Builder levelUpRequest = buildRequestEnvelope(RequestType.LEVEL_UP_REWARDS, LevelUpRewardsMessage
				.newBuilder()
				.setLevel(playerLevel)
				.build());
		response = requestScheduler.queueRequest(levelUpRequest.build()).toBlocking().first();
		try {
			levelUpRewardsResponse = LevelUpRewardsResponse.parseFrom(response.getReturns(0));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}

		log.info("Do an unpacked GET_MAP_OBJECTS, GET_HATCHED_EGGS, GET_INVENTORY, CHECK_AWARDED_BADGES, DOWNLOAD_SETTINGS");

		sleep(300);
		// Initial map request
		List<Long> cellIds = com.pokegoapi.api.map.Map.getCellIds(location.getLatitude(), location.getLongitude(), com.pokegoapi.api.map.Map.CELL_WIDTH);
		GetMapObjectsMessage.Builder builder = GetMapObjectsMessage.newBuilder();
		builder.addAllCellId(cellIds);
		builder.addAllSinceTimestampMs(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L));
		builder.setLatitude(location.getLatitude()).setLongitude(location.getLongitude());
		RequestEnvelope.Builder initialMapRequest = buildRequestEnvelope(RequestType.GET_MAP_OBJECTS, builder.setLatitude(location.getLatitude()).setLongitude(location.getLongitude())
				.build());
		response = requestScheduler.queueRequest(initialMapRequest.build()).toBlocking().first();
		try {
			getMapObjectsResponse = GetMapObjectsResponse.parseFrom(response.getReturns(0));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}
		sleep(300);
		log.info("Do a GET_ASSET_DIGEST, 600, GET_HATCHED_EGGS, GET_INVENTORY, CHECK_AWARDED_BADGES, DOWNLOAD_SETTINGS");
		assetsRequest = buildRequestEnvelope(RequestType.GET_ASSET_DIGEST, GetAssetDigestMessageOuterClass.GetAssetDigestMessage
				.newBuilder()
				.setPlatform(PlatformOuterClass.Platform.ANDROID)
				.setAppVersion(VERSION)
				.build());
		response = requestScheduler.queueRequest(assetsRequest.build()).toBlocking().first();
		try {
			assetDigestResponse = GetAssetDigestResponse.parseFrom(response.getReturns(0));
		}
		catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
		}
		for (String hash : DOWNLOAD_URL_HASHES) {
			RequestEnvelope.Builder downloadUrls = buildRequestEnvelope(RequestType.GET_DOWNLOAD_URLS, GetDownloadUrlsMessage.newBuilder().addAssetId(hash).build());
			response = requestScheduler.queueRequest(downloadUrls.build()).toBlocking().first();
			try {
				downloadUrlsResponses.add(GetDownloadUrlsResponse.parseFrom(response.toByteString()));
			}
			catch (InvalidProtocolBufferException e) {
				throw new RemoteServerException("Initial setup of request handler failed. Can't parse player response: " + e);
			}
		}
		return new BootstrapResult(playerResponse,
				downloadRemoteConfigVersionResponse,
				inventoryResponse,
				hatchedEggsResponse,
				checkAwardedBadgesResponse,
				downloadSettingsResponse,
				assetDigestResponse,
				levelUpRewardsResponse,
				getMapObjectsResponse,
				downloadItemTemplatesResponse,
				downloadUrlsResponses);
	}
	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		}
		catch (InterruptedException e) {
			throw new RuntimeException("Can't wait. Shutting down?", e);
		}
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

	public <T extends GeneratedMessage> Observable<T> queueRequest(final RequestType requestType,
																   final GeneratedMessage message,
																   final Class<T> responseType) {
		RequestEnvelope.Builder request = buildRequestEnvelope(requestType, message);
		return requestScheduler.queueRequest(request.build()).flatMap(new Func1<ResponseEnvelope, Observable<T>>() {
			@Override
			public Observable<T> call(ResponseEnvelope responseEnvelope) {
				try {
					T responseMessage = getParser(responseType).parseFrom(responseEnvelope.getReturns(0));
					final GetHatchedEggsResponse getHatchedEggsResponse = GetHatchedEggsResponse.parseFrom(responseEnvelope.getReturns(2));
					final GetInventoryResponse getInventoryResponse = GetInventoryResponse.parseFrom(responseEnvelope.getReturns(3));
					final CheckAwardedBadgesResponse checkAwardedBadgesResponse = CheckAwardedBadgesResponse.parseFrom(responseEnvelope.getReturns(4));
					final DownloadSettingsResponse downloadSettingsResponse = DownloadSettingsResponse.parseFrom(responseEnvelope.getReturns(5));
					executorService.submit(new Runnable() {
						@Override
						public void run() {
							callback.update(getHatchedEggsResponse, getInventoryResponse, checkAwardedBadgesResponse, downloadSettingsResponse);
						}
					});
					return Observable.just(responseMessage);
				} catch (IllegalAccessException | InvalidProtocolBufferException | NoSuchMethodException | InvocationTargetException e) {
					return Observable.error(e);
				}
			}
		});
	}

	static <T> Parser<T> getParser(Class<T> clz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		return (Parser<T>) clz.getDeclaredMethod("parser").invoke(null);
	}

	private RequestEnvelope.Builder buildRequestEnvelope(RequestType requestType,
														 GeneratedMessage message) {
		long requestId = Math.abs(random.nextLong());
		RequestEnvelope.Builder request = RequestEnvelope.newBuilder()
				.setStatusCode(2)
				.setRequestId(requestId)
				.addRequests(wrap(requestType, message))
				.addRequests(RequestOuterClass.Request.newBuilder().setRequestTypeValue(600).build())
				.addRequests(getHatchedEggs())
				.addRequests(getInventory())
				.addRequests(getCheckAwardedBAtches())
				.addRequests(getDownloadSettings())
				.setLatitude(location.getLatitude())
				.setLongitude(location.getLongitude())
				.setAltitude(location.getAltitude())
				.setAuthTicket(requestScheduler.getAuthTicket())
				.setMsSinceLastLocationfix(getUnknown12());
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
