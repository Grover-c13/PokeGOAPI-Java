package com.pokegoapi.old.api.listener;

import com.pokegoapi.old.api.map.fort.PokestopLootResult;
import com.pokegoapi.old.api.map.fort.Pokestop;

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
	void onLoot(PokestopLootResult result, Pokestop pokestop);
}
