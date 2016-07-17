package auth;

import exceptions.LoginFailedException;
import main.Pokemon;

public abstract class Login {

  public abstract Pokemon.RequestEnvelop.AuthInfo login(String username, String password) throws LoginFailedException;

  public abstract Pokemon.RequestEnvelop.AuthInfo login(String token) throws LoginFailedException;

}
