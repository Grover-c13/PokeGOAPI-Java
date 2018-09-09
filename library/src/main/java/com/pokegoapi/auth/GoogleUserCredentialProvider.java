/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.auth;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import com.pokegoapi.exceptions.request.InvalidCredentialsException;
import com.pokegoapi.exceptions.request.LoginFailedException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.SystemTimeImpl;
import com.pokegoapi.util.Time;
import com.squareup.moshi.Moshi;
import lombok.Getter;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 * @deprecated Use {@link GoogleAutoCredentialProvider}
 */
@Deprecated
public class GoogleUserCredentialProvider extends CredentialProvider {

	public static final String SECRET = "NCjF1TLi2CcY6t5mt0ZveuL7";
	public static final String CLIENT_ID = "848232511240-73ri3t7plvk96pj4f85uj8otdat2alem.apps.googleusercontent.com";
	public static final String OAUTH_TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token";
	public static final String LOGIN_URL = "https://accounts.google"
			+ ".com/o/oauth2/auth?client_id=848232511240-73ri3t7plvk96pj4f85uj8otdat2alem.apps.googleusercontent"
			+ ".com&redirect_uri=urn%3Aietf%3Awg%3Aoauth%3A2.0%3Aoob&response_type=code&scope=openid%20email%20https"
			+ "%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email";
	private static final String TAG = GoogleUserCredentialProvider.class.getSimpleName();
	//We try and refresh token 5 minutes before it actually expires
	protected static final long REFRESH_TOKEN_BUFFER_TIME = 5 * 60 * 1000;
	protected final OkHttpClient client;

	protected final Time time;

	protected long expiresTimestamp;

	protected String tokenId;

	@Getter
	public String refreshToken;

	protected AuthInfo.Builder authbuilder;

	/**
	 * Used for logging in when one has a persisted refreshToken.
	 *
	 * @param client OkHttp client
	 * @param refreshToken Refresh Token Persisted by user
	 * @param time a Time implementation
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public GoogleUserCredentialProvider(OkHttpClient client, String refreshToken, Time time)
			throws LoginFailedException, InvalidCredentialsException {
		this.time = time;
		this.client = client;
		this.refreshToken = refreshToken;

		refreshToken(refreshToken);
		authbuilder = AuthInfo.newBuilder();
	}

	/**
	 * Used for logging in when one has a persisted refreshToken.
	 *
	 * @param client OkHttp client
	 * @param refreshToken Refresh Token Persisted by user
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public GoogleUserCredentialProvider(OkHttpClient client, String refreshToken)
			throws LoginFailedException, InvalidCredentialsException {
		this.time = new SystemTimeImpl();
		this.client = client;
		this.refreshToken = refreshToken;

		refreshToken(refreshToken);
		authbuilder = AuthInfo.newBuilder();
	}

	/**
	 * Used for logging in when you dont have a persisted refresh token.
	 *
	 * @param client OkHttp client
	 * @param time a Time implementation
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public GoogleUserCredentialProvider(OkHttpClient client, Time time)
			throws LoginFailedException, InvalidCredentialsException {
		this.time = time;
		this.client = client;
	}

	/**
	 * Used for logging in when you dont have a persisted refresh token.
	 *
	 * @param client OkHttp client
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public GoogleUserCredentialProvider(OkHttpClient client)
			throws LoginFailedException, InvalidCredentialsException {
		this.time = new SystemTimeImpl();
		this.client = client;
	}


	/**
	 * Given the refresh token fetches a new access token and returns AuthInfo.
	 *
	 * @param refreshToken Refresh token persisted by the user after initial login
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public void refreshToken(String refreshToken)
			throws LoginFailedException, InvalidCredentialsException {
		HttpUrl url = HttpUrl.parse(OAUTH_TOKEN_ENDPOINT).newBuilder()
				.addQueryParameter("client_id", CLIENT_ID)
				.addQueryParameter("client_secret", SECRET)
				.addQueryParameter("refresh_token", refreshToken)
				.addQueryParameter("grant_type", "refresh_token")
				.build();
		//Empty request body
		RequestBody reqBody = RequestBody.create(null, new byte[0]);
		Request request = new Request.Builder()
				.url(url)
				.method("POST", reqBody)
				.build();

		Response response = null;
		try {
			response = client.newCall(request).execute();

		} catch (IOException e) {
			throw new LoginFailedException("Network Request failed to fetch refreshed tokenId", e);
		}
		Moshi moshi = new Moshi.Builder().build();
		GoogleAuthTokenJson googleAuthTokenJson = null;
		try {
			googleAuthTokenJson = moshi.adapter(GoogleAuthTokenJson.class).fromJson(response.body().string());
			Log.d(TAG, "" + googleAuthTokenJson.expiresIn);
		} catch (IOException e) {
			throw new LoginFailedException("Failed to unmarshal the Json response to fetch refreshed tokenId", e);
		}
		if (googleAuthTokenJson.error != null) {
			throw new LoginFailedException(googleAuthTokenJson.error);
		} else {
			Log.d(TAG, "Refreshed Token " + googleAuthTokenJson.idToken);
			expiresTimestamp = time.currentTimeMillis()
					+ (googleAuthTokenJson.expiresIn * 1000 - REFRESH_TOKEN_BUFFER_TIME);
			tokenId = googleAuthTokenJson.idToken;
		}
	}


	/**
	 * Uses an access code to login and get tokens
	 *
	 * @param authCode auth code to authenticate
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public void login(String authCode) throws LoginFailedException, InvalidCredentialsException {

		HttpUrl url = HttpUrl.parse(OAUTH_TOKEN_ENDPOINT).newBuilder()
				.addQueryParameter("code", authCode)
				.addQueryParameter("client_id", CLIENT_ID)
				.addQueryParameter("client_secret", SECRET)
				.addQueryParameter("grant_type", "authorization_code")
				.addQueryParameter("scope", "openid email https://www.googleapis.com/auth/userinfo.email")
				.addQueryParameter("redirect_uri", "urn:ietf:wg:oauth:2.0:oob")
				.build();

		//Create empty body
		RequestBody reqBody = RequestBody.create(null, new byte[0]);

		Request request = new Request.Builder()
				.url(url)
				.method("POST", reqBody)
				.build();
		Response response = null;
		try {
			response = client.newCall(request).execute();
		} catch (IOException e) {
			throw new LoginFailedException("Network Request failed to fetch tokenId", e);
		}

		Moshi moshi = new Moshi.Builder().build();

		GoogleAuthTokenJson googleAuth = null;
		try {
			googleAuth = moshi.adapter(GoogleAuthTokenJson.class).fromJson(response.body().string());
			Log.d(TAG, "" + googleAuth.expiresIn);
		} catch (IOException e) {
			throw new LoginFailedException("Failed to unmarshell the Json response to fetch tokenId", e);
		}

		Log.d(TAG, "Got token: " + googleAuth.accessToken);

		expiresTimestamp = time.currentTimeMillis()
				+ (googleAuth.expiresIn * 1000 - REFRESH_TOKEN_BUFFER_TIME);
		tokenId = googleAuth.idToken;
		refreshToken = googleAuth.refreshToken;

		authbuilder = AuthInfo.newBuilder();
	}

	@Override
	public String getTokenId(boolean refresh) throws LoginFailedException, InvalidCredentialsException {
		if (refresh || isTokenIdInvalid()) {
			refreshToken(refreshToken);
		}
		return tokenId;
	}

	/**
	 * Refreshes tokenId if it has expired
	 *
	 * @param refresh if this AuthInfo should be refreshed
	 * @return AuthInfo object
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	@Override
	public AuthInfo getAuthInfo(boolean refresh)
			throws LoginFailedException, InvalidCredentialsException {
		if (refresh || isTokenIdInvalid()) {
			refreshToken(refreshToken);
		}
		authbuilder.setProvider("google");
		authbuilder.setToken(AuthInfo.JWT.newBuilder().setContents(tokenId).setUnknown2(0).build());
		return authbuilder.build();
	}

	@Override
	public boolean isTokenIdInvalid() {
		return tokenId == null || time.currentTimeMillis() > expiresTimestamp;
	}

	@Override
	public void reset() {
		tokenId = null;
		refreshToken = null;
	}
}
