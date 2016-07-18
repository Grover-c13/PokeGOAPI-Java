package com.pokegoapi.auth;

import com.pokegoapi.exceptions.LoginFailedException;
import POGOProtos.Networking.EnvelopesOuterClass.Envelopes.RequestEnvelope.AuthInfo;


public abstract class Login {

  public abstract AuthInfo login(String username, String password) throws LoginFailedException;

  public abstract AuthInfo login(String token) throws LoginFailedException;

}
