package com.pokegoapi.auth;

public interface GoogleAuthCallback {
	void codeRequired(String userCode, String verificationUrl);
}
