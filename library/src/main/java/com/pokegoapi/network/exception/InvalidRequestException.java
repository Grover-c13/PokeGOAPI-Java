package com.pokegoapi.network.exception;

/**
 * Created by chris on 2/3/2017.
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
