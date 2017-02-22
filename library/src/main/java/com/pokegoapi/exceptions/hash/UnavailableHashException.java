package com.pokegoapi.exceptions.hash;

/**
 * Created by RebliNk17 on 1/25/2017.
 */
public class UnavailableHashException extends HashException {
	public UnavailableHashException() {
		super();
	}

	public UnavailableHashException(String reason) {
		super(reason);
	}

	public UnavailableHashException(Throwable exception) {
		super(exception);
	}
}
