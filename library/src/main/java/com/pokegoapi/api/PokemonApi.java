package com.pokegoapi.api;

import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.pokegoapi.api.device.ActivityStatus;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.api.internal.networking.BootstrapResult;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.settings.Settings;
import com.pokegoapi.auth.CredentialProvider;
import lombok.Getter;
import okhttp3.OkHttpClient;

import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

/**
 * Pokemon API
 */
public class PokemonApi implements Networking.Callback {
	private final Location location;
	@Getter
	private final Networking networking;
	@Getter
	private final PlayerProfile playerProfile;
	@Getter
	private final Inventories inventories;
	@Getter
	private final Map map;
	@Getter
	private final Settings settings;

	PokemonApi(ExecutorService executorService, CredentialProvider credentialProvider, OkHttpClient client, URL server,
			Location location, DeviceInfo deviceInfo, SensorInfo sensorInfo, ActivityStatus activityStatus,
			LocationFixes locationFixes, Locale locale) {
		this.location = location;

		networking = new Networking(server, executorService, client, location, deviceInfo, sensorInfo,
				activityStatus, locationFixes, locale, this);
		BootstrapResult bootstrapResult = networking.bootstrap(credentialProvider.getAuthInfo());
		playerProfile = new PlayerProfile(bootstrapResult.getPlayerResponse(), networking);
		inventories = new Inventories(executorService, bootstrapResult.getInventoryResponse(),
				bootstrapResult.getHatchedEggsResponse(), networking, playerProfile);
		settings = new Settings(bootstrapResult.getDownloadSettingsResponse());
		map = new Map(executorService, settings, networking,
				location, inventories, bootstrapResult.getGetMapObjectsResponse());
	}

	/**
	 * @return Pokemon api builder
	 */
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

	public double getLatitude() {
		return location.getLatitude();
	}

	/**
	 * Validates and sets a given latitude value
	 *
	 * @param value the latitude
	 * @throws IllegalArgumentException if value exceeds +-90
	 */
	private void setLatitude(double value) {
		if (value > 90 || value < -90) {
			throw new IllegalArgumentException("latittude can not exceed +/- 90");
		}
		location.setLatitude(value);
	}

	public double getLongitude() {
		return location.getLongitude();
	}

	/**
	 * Validates and sets a given longitude value
	 *
	 * @param value the longitude
	 * @throws IllegalArgumentException if value exceeds +-180
	 */
	private void setLongitude(double value) {
		if (value > 180 || value < -180) {
			throw new IllegalArgumentException("longitude can not exceed +/- 180");
		}
		location.setLongitude(value);
	}

	public double getAltitude() {
		return location.getAccuracy();
	}

	private void setAccuracy(double value) {
		location.setAccuracy(value);
	}

	/**
	 * Set location and accuracy
	 *
	 * @param latitude  Latitude
	 * @param longitude Longitude
	 * @param accuracy  Accuracy of the location
	 */
	public void setLocation(double latitude, double longitude, double accuracy) {
		setLatitude(latitude);
		setLongitude(longitude);
		setAccuracy(accuracy);
	}
}
