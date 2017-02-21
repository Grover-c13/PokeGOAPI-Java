package com.pokegoapi.api.listener;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.Point;

public interface LocationListener extends Listener {
	/**
	 * Called when the player location updates
	 * @param api the current api
	 * @param point the point moved to
	 */
	void onLocationUpdate(PokemonGo api, Point point);
}
