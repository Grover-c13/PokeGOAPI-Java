package com.pokegoapi.auth;

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.main.Communication.RequestEnvelop.AuthInfo;


public abstract class Login {

  public abstract AuthInfo login(String username, String password) throws LoginFailedException;

  public abstract AuthInfo login(String token) throws LoginFailedException;

}
