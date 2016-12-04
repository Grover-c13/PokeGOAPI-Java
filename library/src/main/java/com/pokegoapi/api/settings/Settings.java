package com.pokegoapi.api.settings;

import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

/**
 * Created by rama on 27/07/16.
 */
public class Settings {

	private final PokemonGo api;


	@Getter
	/**
	 * Settings for various parameters on map
	 *
	 * @return MapSettings instance.
	 */
	private final MapSettings mapSettings;

	@Getter
	/**
	 * Settings for various parameters during levelup
	 *
	 * @return LevelUpSettings instance.
	 */
	private final LevelUpSettings levelUpSettings;

	@Getter
	/**
	 * Settings for various parameters during levelup
	 *
	 * @return LevelUpSettings instance.
	 */
	private final FortSettings fortSettings;


	@Getter
	/**
	 * Settings for various parameters during levelup
	 *
	 * @return LevelUpSettings instance.
	 */
	private final InventorySettings inventorySettings;

	@Getter
	/**
	 * Settings for showing speed warnings
	 *
	 * @return GpsSettings instance.
	 */
	private final GpsSettings gpsSettings;
	@Getter
	/**
     * Settings for hash
     *
     * @return String hash.
     */
	private String hash;

	/**
	 * Settings object that hold different configuration aspect of the game.
	 * Can be used to simulate the real app behaviour.
	 *
	 * @param api api instance
	 */
	public Settings(PokemonGo api) {
		this.api = api;
		this.mapSettings = new MapSettings();
		this.levelUpSettings = new LevelUpSettings();
		this.fortSettings = new FortSettings();
		this.inventorySettings = new InventorySettings();
		this.gpsSettings = new GpsSettings();
		this.hash = new String();
	}

	/**
	 * Updates settings latest data.
	 *
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void updateSettings() throws RemoteServerException, CaptchaActiveException, LoginFailedException {
		DownloadSettingsMessageOuterClass.DownloadSettingsMessage msg =
				DownloadSettingsMessageOuterClass.DownloadSettingsMessage.newBuilder().build();
		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.DOWNLOAD_SETTINGS, msg);
		api.getRequestHandler().sendServerRequests(serverRequest); //here you marked everything as read
		DownloadSettingsResponseOuterClass.DownloadSettingsResponse response;
		try {
			response = DownloadSettingsResponseOuterClass.DownloadSettingsResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		updateSettings(response);
	}

	/**
	 * Updates settings latest data.
	 *
	 * @param response the settings download response
	 */
	public void updateSettings(DownloadSettingsResponse response) {
		if (response.getSettings().hasMapSettings()) {
			mapSettings.update(response.getSettings().getMapSettings());
		}
		if (response.getSettings().hasLevelSettings()) {
			levelUpSettings.update(response.getSettings().getInventorySettings());
		}
		if (response.getSettings().hasFortSettings()) {
			fortSettings.update(response.getSettings().getFortSettings());
		}
		if (response.getSettings().hasInventorySettings()) {
			inventorySettings.update(response.getSettings().getInventorySettings());
		}
		if (response.getSettings().hasGpsSettings()) {
			gpsSettings.update(response.getSettings().getGpsSettings());
		}
		this.hash = response.getHash();
	}
}
