package com.pokegoapi.api.listener;

import com.pokegoapi.api.map.fort.Fort;
import com.pokegoapi.api.map.fort.PokestopLootResult;

/**
 * Listener for all pokestop related events.
 */
public interface PokestopListener extends Listener {
	/**
	 * Called when a Pokestop is looted
	 *
	 * @param result the loot result from this pokestop
	 * @param pokestop the pokestop being looted
	 */
	void onLoot(PokestopLootResult result, Fort pokestop);
}
