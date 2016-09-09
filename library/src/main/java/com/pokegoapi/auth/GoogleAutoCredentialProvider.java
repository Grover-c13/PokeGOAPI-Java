package com.pokegoapi.auth;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import com.pokegoapi.exceptions.request.InvalidCredentialsException;
import com.pokegoapi.exceptions.request.LoginFailedException;
import com.pokegoapi.util.SystemTimeImpl;
import com.pokegoapi.util.Time;
import lombok.Getter;
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

	private final Gpsoauth gpsoauth;
	private final String username;
	private Time time;

	@Getter
	private TokenInfo tokenInfo;

	/**
	 * Constructs credential provider using username and password
	 *
	 * @param httpClient OkHttp client
	 * @param username google username
	 * @param password google password
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public GoogleAutoCredentialProvider(OkHttpClient httpClient, String username, String password)
			throws LoginFailedException, InvalidCredentialsException {
		this.gpsoauth = new Gpsoauth(httpClient);
		this.username = username;
		this.tokenInfo = login(username, password);
		this.time = new SystemTimeImpl();
	}

	/**
	 * @param httpClient the client that will make http call
	 * @param username google username
	 * @param password google pwd
	 * @param time time instance used to refresh token
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public GoogleAutoCredentialProvider(OkHttpClient httpClient, String username, String password, Time time)
			throws LoginFailedException, InvalidCredentialsException {
		this.gpsoauth = new Gpsoauth(httpClient);
		this.username = username;
		this.tokenInfo = login(username, password);
		this.time = time;
	}

	private TokenInfo login(String username, String password)
			throws LoginFailedException, InvalidCredentialsException {
		try {
			String masterToken = gpsoauth.performMasterLoginForToken(username, password, GOOGLE_LOGIN_ANDROID_ID);
			AuthToken authToken = gpsoauth.performOAuthForToken(username, masterToken, GOOGLE_LOGIN_ANDROID_ID,
					GOOGLE_LOGIN_SERVICE, GOOGLE_LOGIN_APP, GOOGLE_LOGIN_CLIENT_SIG);
			return new TokenInfo(authToken, masterToken);
		} catch (IOException | Gpsoauth.TokenRequestFailed e) {
			throw new LoginFailedException(e);
		}
	}

	/**
	 * @param username user name
	 * @param refreshToken refresh token
	 * @return the token info
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	private TokenInfo refreshToken(String username, String refreshToken) throws LoginFailedException,
			InvalidCredentialsException {
		try {
			AuthToken authToken = gpsoauth.performOAuthForToken(username, refreshToken, GOOGLE_LOGIN_ANDROID_ID,
					GOOGLE_LOGIN_SERVICE, GOOGLE_LOGIN_APP, GOOGLE_LOGIN_CLIENT_SIG);
			return new TokenInfo(authToken, refreshToken);
		} catch (IOException | Gpsoauth.TokenRequestFailed e) {
			throw new LoginFailedException(e);
		}
	}

	/**
	 * @param refresh if this AuthInfo should be refreshed
	 * @return token id
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	@Override
	public String getTokenId(boolean refresh) throws LoginFailedException, InvalidCredentialsException {
		if (refresh || isTokenIdInvalid()) {
			this.tokenInfo = refreshToken(username, tokenInfo.refreshToken);
		}
		return tokenInfo.authToken.getToken();
	}

	/**
	 * @param refresh if this AuthInfo should be refreshed
	 * @return auth info
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	@Override
	public AuthInfo getAuthInfo(boolean refresh) throws LoginFailedException, InvalidCredentialsException {
		AuthInfo.Builder builder = AuthInfo.newBuilder();
		builder.setProvider("google");
		builder.setToken(AuthInfo.JWT.newBuilder().setContents(getTokenId(refresh)).setUnknown2(0).build());
		return builder.build();
	}

	@Override
	public boolean isTokenIdInvalid() {
		return tokenInfo == null || tokenInfo.authToken.getExpiry() < time.currentTimeMillis() / 1000;
	}

	@Override
	public void reset() {
		tokenInfo = null;
	}

	private static class TokenInfo {
		final AuthToken authToken;
		final String refreshToken;

		TokenInfo(AuthToken authToken, String refreshToken) {
			this.authToken = authToken;
			this.refreshToken = refreshToken;
		}
	}
}
