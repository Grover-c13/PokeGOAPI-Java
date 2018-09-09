package com.pokegoapi.api.settings;

import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import com.pokegoapi.api.PokemonGo;
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
	public final MapSettings mapSettings;

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
	public final FortSettings fortSettings;


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
	public String hash;

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
		this.hash = "";
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
