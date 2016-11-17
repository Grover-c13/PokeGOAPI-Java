package com.pokegoapi.api.listener;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.player.PlayerAvatar;
import com.pokegoapi.api.pokemon.StarterPokemon;

/**
 * Listener for all tutorial and account setup events.
 */
public interface TutorialListener extends Listener {
	/**
	 * Called during the tutorial when you are asked to enter a name.
	 * @param api the current api
	 * @param lastFailure the last name used that was already taken; null for first try.
	 * @return a name for the current player, null to pick random
	 */
	String claimName(PokemonGo api, String lastFailure);

	/**
	 * Called when the user is required to select a starter pokemon.
	 * @param api the current api
	 * @return the desired starter pokemon; null to pick random
	 */
	StarterPokemon selectStarter(PokemonGo api);

	/**
	 * Called when the user is required to setup an avatar.
	 * @param api the current api
	 * @return the selected avatar; null to pick random
	 */
	PlayerAvatar selectAvatar(PokemonGo api);
}
