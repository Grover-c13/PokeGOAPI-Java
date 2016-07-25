package com.pokegoapi.exceptions;

public class PokemonGoApiException extends RuntimeException {
	public PokemonGoApiException() {
	}

	public PokemonGoApiException(String message) {
		super(message);
	}

	public PokemonGoApiException(Throwable cause) {
		super(cause);
	}
}
