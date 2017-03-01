package com.pokegoapi.network.exception;

/**
 * Exception thrown when this API fails to send a request because this current account is banned
 */
public class AccountBannedException extends RequestFailedException {

    public AccountBannedException() {
        super();
    }

    public AccountBannedException(String message) {
        super(message);
    }

    public AccountBannedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountBannedException(Throwable cause) {
        super(cause);
    }
}
