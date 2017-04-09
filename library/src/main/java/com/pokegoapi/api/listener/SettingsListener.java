package com.pokegoapi.api.listener;

import com.pokegoapi.api.PokemonGo;

/**
 * Created by RebliNk17 on 09/04/2017.
 */
public interface SettingsListener extends Listener {

	 void onNewVersionForced(PokemonGo go, String currentVersion, String forcedVersion);

}
