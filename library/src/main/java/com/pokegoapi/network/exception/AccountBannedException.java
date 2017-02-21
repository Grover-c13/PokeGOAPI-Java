package com.pokegoapi.network.exception;

/**
 * Created by chris on 2/3/2017.
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
