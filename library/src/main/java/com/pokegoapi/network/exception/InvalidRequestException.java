package com.pokegoapi.network.exception;

/**
 * Thrown when an invalid request ID is sent to the server
 */
public class InvalidRequestException extends RequestFailedException {

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }
}
