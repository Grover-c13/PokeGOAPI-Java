package com.pokegoapi.auth;

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.main.Pokemon;

public abstract class Login {

  public abstract Pokemon.RequestEnvelop.AuthInfo login(String username, String password) throws LoginFailedException;

  public abstract Pokemon.RequestEnvelop.AuthInfo login(String token) throws LoginFailedException;

}
