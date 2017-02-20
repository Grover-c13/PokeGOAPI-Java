package com.pokegoapi.network.exception;

/**
 * Created by chris on 2/3/2017.
 */
public class BadRequestException extends RequestFailedException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
