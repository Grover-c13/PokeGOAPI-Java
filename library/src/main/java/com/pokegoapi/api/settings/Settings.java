package com.pokegoapi.api.settings;

import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import lombok.Getter;

/**
 * Created by rama on 27/07/16.
 */
public class Settings {

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


	/**
	 * Settings object that hold different configuration aspect of the game.
	 * Can be used to simulate the real app behaviour.
	 *
	 */
	public Settings(DownloadSettingsResponse downloadSettingsResponse) {
		this.mapSettings = new MapSettings();
		this.levelUpSettings = new LevelUpSettings();
		this.fortSettings = new FortSettings();
		this.inventorySettings = new InventorySettings();
		this.gpsSettings = new GpsSettings();
		update(downloadSettingsResponse);
	}

	/**
	 * Updates settings latest data.
	 *
	 */
	public final void update(DownloadSettingsResponse response) {
		mapSettings.update(response.getSettings().getMapSettings());
		levelUpSettings.update(response.getSettings().getInventorySettings());
		fortSettings.update(response.getSettings().getFortSettings());
		inventorySettings.update(response.getSettings().getInventorySettings());
		gpsSettings.update(response.getSettings().getGpsSettings());
	}


}
