package com.pokegoapi.api.listener;

import com.pokegoapi.api.PokemonGo;

/**
 * Created by RebliNk17 on 09/04/2017.
 */
public interface SettingsListener extends Listener {

	/**
	 * Called when a new API is forced
	 *
	 * @param go the current api
	 * @param currentVersion the current supported api version
	 * @param forcedVersion the new forced api.
	 */
	 void onNewVersionForced(PokemonGo go, String currentVersion, String forcedVersion);

}
