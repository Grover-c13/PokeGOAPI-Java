package com.pokegoapi.provider;

/**
 * Created by chris on 1/22/2017.
 */
public class NoSuchTypeException extends Exception {

    public NoSuchTypeException() {
    }

    public NoSuchTypeException(String message) {
        super(message);
    }

    public NoSuchTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchTypeException(Throwable cause) {
        super(cause);
    }
}
