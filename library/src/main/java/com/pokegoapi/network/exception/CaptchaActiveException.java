package com.pokegoapi.network.exception;

/**
 * Created by chris on 2/3/2017.
 */
public class CaptchaActiveException extends RequestFailedException {

    public CaptchaActiveException() {
        super();
    }

    public CaptchaActiveException(String message) {
        super(message);
    }

    public CaptchaActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaActiveException(Throwable cause) {
        super(cause);
    }
}
