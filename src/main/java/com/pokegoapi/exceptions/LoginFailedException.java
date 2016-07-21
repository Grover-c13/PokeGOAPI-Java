package com.pokegoapi.exceptions;

public class LoginFailedException extends Exception {

    public LoginFailedException() {
    }

    public LoginFailedException(Throwable t)  {
        super(t);
    }
}
