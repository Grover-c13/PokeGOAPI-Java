package com.pokegoapi.exceptions;

/**
 * Created by paul on 30-7-2016.
 */
public class AsyncPokemonGoException extends RuntimeException {

    public AsyncPokemonGoException(String message) {
        super(message);
    }

    public AsyncPokemonGoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsyncPokemonGoException(Throwable cause) {
        super(cause);
    }
}
