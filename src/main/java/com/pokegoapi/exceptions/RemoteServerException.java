package com.pokegoapi.exceptions;

public class RemoteServerException extends Exception {
	public RemoteServerException() {
		super();
	}

	public RemoteServerException(String reason) {
		super(reason);
	}

	public RemoteServerException(Exception e) {
		super(e);
	}
}
