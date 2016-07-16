package mx.may.courtney.pgo.auth;

import mx.may.courtney.pgo.Pgo;
import mx.may.courtney.pgo.Pgo.RequestEnvelop;
import mx.may.courtney.pgo.Pgo.RequestEnvelop.AuthInfo;
import mx.may.courtney.pgo.exceptions.LoginFailedException;

public abstract class Login 
{
	
	public abstract Pgo.RequestEnvelop.AuthInfo login(String username, String password) throws LoginFailedException;
	
	public abstract  Pgo.RequestEnvelop.AuthInfo login(String token) throws LoginFailedException;
	
}
