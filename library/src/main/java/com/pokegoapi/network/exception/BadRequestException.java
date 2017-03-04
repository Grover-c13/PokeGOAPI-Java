package com.pokegoapi.network.exception;

/**
 * Thrown when the server responds to a request with the BAD_REQUEST status code
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
