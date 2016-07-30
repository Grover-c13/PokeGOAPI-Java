package com.pokegoapi.auth;

/**
 * @author Paul van Assen
 */
public interface TokenCallback {
	void tokenRequired(String token);
}
