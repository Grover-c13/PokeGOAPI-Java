package com.pokegoapi.api;

import POGOProtos.Enums.PlatformOuterClass;
import POGOProtos.Enums.PlatformOuterClass.Platform;
import POGOProtos.Networking.Requests.Messages.DownloadRemoteConfigVersionMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.DownloadRemoteConfigVersionMessageOuterClass.DownloadRemoteConfigVersionMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.DownloadRemoteConfigVersionResponseOuterClass;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.auth.CredentialProvider;
import lombok.Data;
import okhttp3.OkHttpClient;
import rx.functions.Func1;

import java.net.URL;
import java.util.concurrent.ExecutorService;

/**
 * Created by paul on 20-8-2016.
 */
@Data
public class PokemonApi implements Networking.Callback {
	private final ExecutorService executorService;
	private final CredentialProvider credentialProvider;
	private final OkHttpClient client;
	private final URL server;
	private final Location location;
	private final Networking networking;
	private PlayerProfile playerProfile;

	PokemonApi(ExecutorService executorService, CredentialProvider credentialProvider, OkHttpClient client, URL server,
			   Location location) {
		this.executorService = executorService;
		this.credentialProvider = credentialProvider;
		this.client = client;
		this.server = server;
		this.location = location;
		this.networking = Networking.getInstance(server, client, location, this);
		this.playerProfile = new PlayerProfile(this.networking.initialize(credentialProvider.getAuthInfo()));
		// First call made is 7: DOWNLOAD_REMOTE_CONFIG_VERSION
		networking.queueRequest(RequestType.DOWNLOAD_REMOTE_CONFIG_VERSION,
				DownloadRemoteConfigVersionMessage
						.newBuilder()
						.setPlatform(Platform.ANDROID)
						.setAppVersion(3300)
						.build(),
				DownloadRemoteConfigVersionResponseOuterClass.DownloadRemoteConfigVersionResponse.class)
				.toBlocking()
				.first();
	}

	@Override
	public void update(GetHatchedEggsResponse getHatchedEggsResponse,
					   GetInventoryResponse getInventoryResponse,
					   CheckAwardedBadgesResponse checkAwardedBadgesResponse,
					   DownloadSettingsResponse downloadSettingsResponse) {

	}
}
