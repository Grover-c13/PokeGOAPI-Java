package com.pokegoapi.exceptions;

public class InvalidCurrencyException extends Exception {
	public InvalidCurrencyException() {
		super();
	}

	public InvalidCurrencyException(String reason) {
		super(reason);
	}

	public InvalidCurrencyException(Exception e) {
		super(e);
	}
}
