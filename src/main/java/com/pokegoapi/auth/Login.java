package com.pokegoapi.auth;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import com.pokegoapi.exceptions.LoginFailedException;


public abstract class Login {

	public abstract AuthInfo login(String username, String password) throws LoginFailedException;

	public abstract AuthInfo login(String token) throws LoginFailedException;

}
