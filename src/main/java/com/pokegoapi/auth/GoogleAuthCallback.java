package com.pokegoapi.auth;

/**
 * @author Paul van Assen
 */
public interface GoogleAuthCallback {
	void codeRequired(String userCode, String verificationUrl);
}
