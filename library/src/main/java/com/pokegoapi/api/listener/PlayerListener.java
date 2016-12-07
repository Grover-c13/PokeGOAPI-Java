package com.pokegoapi.api.listener;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.player.Medal;
import com.pokegoapi.api.player.PlayerProfile;

/**
 * Listener for all player related events.
 */
public interface PlayerListener extends Listener {
	/**
	 * Called when the player levels up
	 * @param api the current api instance
	 * @param oldLevel the old player level
	 * @param newLevel the new player level
	 * @return true if you want to accept level up rewards
	 */
	boolean onLevelUp(PokemonGo api, int oldLevel, int newLevel);

	/**
	 * Called when a new medal is awarded or leveled up for the current player
	 * @param api the current api
	 * @param profile the player receiving this medal
	 * @param medal the medal awarded
	 */
	void onMedalAwarded(PokemonGo api, PlayerProfile profile, Medal medal);
}
