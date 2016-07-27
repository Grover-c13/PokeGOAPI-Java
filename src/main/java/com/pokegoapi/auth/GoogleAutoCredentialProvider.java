package com.pokegoapi.auth;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import okhttp3.OkHttpClient;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.IOException;

/**
 * Use to login with google username and password
 */
public class GoogleAutoCredentialProvider extends CredentialProvider {

	// from https://github.com/tejado/pgoapi/blob/master/pgoapi/auth_google.py
	private static String GOOGLE_LOGIN_ANDROID_ID = "9774d56d682e549c";
	private static String GOOGLE_LOGIN_SERVICE =
			"audience:server:client_id:848232511240-7so421jotr2609rmqakceuu1luuq0ptb.apps.googleusercontent.com";
	private static String GOOGLE_LOGIN_APP = "com.nianticlabs.pokemongo";
	private static String GOOGLE_LOGIN_CLIENT_SIG = "321187995bc7cdc2b5fc91b11a96e2baa8602c62";

	private final String username;
	private final String password;

	private AuthToken authToken;
	private AuthInfo authInfo;

	/**
	 * Constructs credential provider using username and password
	 *
	 * @param username - google username
	 * @param password - google password
	 * @throws LoginFailedException - login failed possibly due to invalid credentials
	 * @throws RemoteServerException - some server/network failure
	 */
	public GoogleAutoCredentialProvider(String username, String password)
			throws LoginFailedException, RemoteServerException {
		this.username = username;
		this.password = password;
		login(username, password);
	}

	private void login(String username, String password) throws RemoteServerException, LoginFailedException {
		try {
			OkHttpClient httpClient = new OkHttpClient();
			Gpsoauth gpsoauth = new Gpsoauth(httpClient);
			this.authToken = gpsoauth.login(
					username,
					password,
					GOOGLE_LOGIN_ANDROID_ID,
					GOOGLE_LOGIN_SERVICE,
					GOOGLE_LOGIN_APP,
					GOOGLE_LOGIN_CLIENT_SIG
			);
		} catch (IOException e) {
			throw new RemoteServerException(e);
		} catch (Gpsoauth.TokenRequestFailed e) {
			throw new LoginFailedException(e);
		}
	}


	@Override
	public String getTokenId() throws LoginFailedException, RemoteServerException {
		if (isTokenIdExpired()) login(username, password);
		return authToken.getToken();
	}

	@Override
	public AuthInfo getAuthInfo() throws LoginFailedException, RemoteServerException {
		AuthInfo.Builder builder = AuthInfo.newBuilder();
		builder.setProvider("google");
		builder.setToken(AuthInfo.JWT.newBuilder().setContents(authToken.getToken()).setUnknown2(59).build());
		return builder.build();
	}

	@Override
	public boolean isTokenIdExpired() {
		return authToken.getExpiry() > System.currentTimeMillis() / 1000;
	}
}
