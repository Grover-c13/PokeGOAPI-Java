package com.pokegoapi.exceptions;

public class NoSuchItemException extends Exception {
	public NoSuchItemException() {
		super();
	}

	public NoSuchItemException(String reason) {
		super(reason);
	}

	public NoSuchItemException(Exception e) {
		super(e);
	}
}
