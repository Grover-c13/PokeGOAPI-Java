package com.pokegoapi.api;

import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.api.internal.networking.BootstrapResult;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.settings.Settings;
import com.pokegoapi.auth.CredentialProvider;
import lombok.Data;
import okhttp3.OkHttpClient;

import java.net.URL;
import java.util.Locale;
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
	private final PlayerProfile playerProfile;
	private final Inventories inventories;
	private final Map map;
	private final Settings settings;

	PokemonApi(ExecutorService executorService, CredentialProvider credentialProvider, OkHttpClient client, URL server,
			   Location location, DeviceInfo deviceInfo, SensorInfo sensorInfo, Locale locale) {
		this.executorService = executorService;
		this.credentialProvider = credentialProvider;
		this.client = client;
		this.server = server;
		this.location = location;

		networking = Networking.getInstance(server, executorService, client, location, deviceInfo, sensorInfo, this, locale);
		BootstrapResult bootstrapResult = networking.bootstrap(credentialProvider.getAuthInfo());
		playerProfile = new PlayerProfile(bootstrapResult.getPlayerResponse());
		inventories = new Inventories(executorService, bootstrapResult.getInventoryResponse(),
				bootstrapResult.getHatchedEggsResponse(), networking, playerProfile);
		settings = new Settings(bootstrapResult.getDownloadSettingsResponse());
		map = new Map(executorService, settings, networking,
				location, inventories, bootstrapResult.getGetMapObjectsResponse());
	}

	public static PokemonApiBuilder newBuilder() {
		return new PokemonApiBuilder();
	}

	@Override
	public void update(GetHatchedEggsResponse getHatchedEggsResponse,
					   GetInventoryResponse getInventoryResponse,
					   CheckAwardedBadgesResponse checkAwardedBadgesResponse,
					   DownloadSettingsResponse downloadSettingsResponse) {
		inventories.update(getHatchedEggsResponse);
		inventories.update(getInventoryResponse);
		settings.update(downloadSettingsResponse);
	}


	/**
	 * Validates and sets a given latitude value
	 *
	 * @param value the latitude
	 * @throws IllegalArgumentException if value exceeds +-90
	 */
	public void setLatitude(double value) {
		if (value > 90 || value < -90) {
			throw new IllegalArgumentException("latittude can not exceed +/- 90");
		}
		location.setLatitude(value);
	}

	/**
	 * Validates and sets a given longitude value
	 *
	 * @param value the longitude
	 * @throws IllegalArgumentException if value exceeds +-180
	 */
	public void setLongitude(double value) {
		if (value > 180 || value < -180) {
			throw new IllegalArgumentException("longitude can not exceed +/- 180");
		}
		location.setLongitude(value);
	}
}
