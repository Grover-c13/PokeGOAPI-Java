package com.pokegoapi.go.settings.spec;

public interface MapSettingsSpec {
    /**
     * @return the Google Maps key used to display maps in the official app
     */
    String getGoogleMapsKey();

    /**
     * @return the minimum distance moved to force request a map update in meters
     */
    float getMinMapObjectDistance();

    /**
     * @return the minimum time between map updates in seconds
     */
    float getMinimumRefreshTime();

    /**
     * @return the maximum time between map updates in seconds
     */
    float getMaximumRefreshTime();

    /**
     * @return the range in meters in which you can encounter a pokemon
     */
    double getPokemonEncounterRange();

    /**
     * @return the range in meters in which pokemon are visible on the map
     */
    double getPokemonVisibilityRange();

    /**
     * @return the range in meters of the pokemon tracker
     */
    double getPokeNavRange();
}
