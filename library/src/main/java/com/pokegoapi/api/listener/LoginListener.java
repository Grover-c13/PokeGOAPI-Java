package com.pokegoapi.api.listener;

import com.pokegoapi.api.PokemonGo;

/**
 * Listener for all login related events.
 */
public interface LoginListener extends Listener {
	/**
	 * Called when this api performs a successful login via PokemonGo#login
	 * @param api the api that has logged in
	 */
	void onLogin(PokemonGo api);

	/**
	 * Called when a challenge is requested.
	 * @param api the current api
	 * @param challengeURL the challenge url
	 */
	void onChallenge(PokemonGo api, String challengeURL);
}
