package com.pokegoapi.api.settings;

import POGOProtos.Settings.MapSettingsOuterClass;
import lombok.Getter;

/**
 * Created by rama on 27/07/16.
 */
public class MapSettings {

	@Getter
	/**
	 * Google api key used for display map
	 *
	 * @return String.
	 */
	private String googleApiKey;

	@Getter
	/**
	 * Minimum distance between getMapObjects requests
	 *
	 * @return distance in meters.
	 */
	private float minMapObjectDistance;

	@Getter
	/**
	 * Max refresh betweewn getMapObjecs requests
	 *
	 * @return value in milliseconds.
	 */
	private float maxRefresh;

	@Getter
	/**
	 * Min refresh betweewn getMapObjecs requests
	 *
	 * @return value in milliseconds.
	 */
	private float minRefresh;

	@Getter
	/**
	 * NOT SURE: the max distance for encounter pokemon?
	 *
	 * @return distance in meters.
	 */
	private double encounterRange;

	@Getter
	/**
	 * NOT SURE: the max distance before show pokemon on map?
	 *
	 * @return distance in meters.
	 */
	private double pokemonVisibilityRange;

	@Getter
	/**
	 * NO IDEA
	 *
	 * @return distance in meters.
	 */
	private double pokeNavRange;

	protected void update(MapSettingsOuterClass.MapSettings mapSettings) {
		googleApiKey = mapSettings.getGoogleMapsApiKey();
		minMapObjectDistance = mapSettings.getGetMapObjectsMinDistanceMeters();
		maxRefresh = mapSettings.getGetMapObjectsMaxRefreshSeconds() * 1000;
		minRefresh = mapSettings.getGetMapObjectsMinRefreshSeconds() * 1000;
		encounterRange = mapSettings.getEncounterRangeMeters();
		pokemonVisibilityRange = mapSettings.getPokemonVisibleRange();
		pokeNavRange = mapSettings.getPokeNavRangeMeters();
	}
}
