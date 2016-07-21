package com.pokegoapi.exceptions;

public class LoginFailedException extends Exception {
	public LoginFailedException() {
		super();
	}

	public LoginFailedException(String reason) {
		super(reason);
	}

	public LoginFailedException(Exception e) {
		super(e);
	}
}
