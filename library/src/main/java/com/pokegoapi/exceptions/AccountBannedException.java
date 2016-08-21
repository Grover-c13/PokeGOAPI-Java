package com.pokegoapi.exceptions;

/**
 * Created by paul on 21-8-2016.
 */
public class AccountBannedException extends RemoteServerException {
	public AccountBannedException() {
		super("Your account may be banned! please try from the official client.");
	}
}
