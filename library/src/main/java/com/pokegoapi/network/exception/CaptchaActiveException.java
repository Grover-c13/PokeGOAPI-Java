package com.pokegoapi.network.exception;

/**
 * Thrown when the API fails to send a request because a captcha is currently active
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
