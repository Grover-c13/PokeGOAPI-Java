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
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
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

public class GoogleUserCredentialProvider extends CredentialProvider {

	public static final String SECRET = "NCjF1TLi2CcY6t5mt0ZveuL7";
	public static final String CLIENT_ID = "848232511240-73ri3t7plvk96pj4f85uj8otdat2alem.apps.googleusercontent.com";
	public static final String OAUTH_TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token";
	public static final String LOGIN_URL = "https://accounts.google.com/o/oauth2/auth?client_id=848232511240-73ri3t7plvk96pj4f85uj8otdat2alem.apps.googleusercontent.com&redirect_uri=urn%3Aietf%3Awg%3Aoauth%3A2.0%3Aoob&response_type=code&scope=openid%20email%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email";
	private static final String TAG = GoogleUserCredentialProvider.class.getSimpleName();
	//We try and refresh token 5 minutes before it actually expires
	protected static final long REFRESH_TOKEN_BUFFER_TIME = 5 * 60 * 1000;
	protected final OkHttpClient client;

	protected final Time time;

	protected long expiresTimestamp;

	protected String tokenId;

	@Getter
	protected String refreshToken;

	protected AuthInfo.Builder authbuilder;

	/**
	 * Used for logging in when one has a persisted refreshToken.
	 *
	 * @param client       OkHttp client
	 * @param refreshToken Refresh Token Persisted by user
	 * @param time         a Time implementation
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException When server fails
	 */
	public GoogleUserCredentialProvider(OkHttpClient client, String refreshToken, Time time)
			throws LoginFailedException, RemoteServerException {
		this.time = time;
		this.client = client;
		this.refreshToken = refreshToken;

		refreshToken(refreshToken);
		authbuilder = AuthInfo.newBuilder();
	}
	
	/**
	 * Used for logging in when one has a persisted refreshToken.
	 *
	 * @param client       OkHttp client
	 * @param refreshToken Refresh Token Persisted by user
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException When server fails
	 */
	public GoogleUserCredentialProvider(OkHttpClient client, String refreshToken)
			throws LoginFailedException, RemoteServerException {
		this.time = new SystemTimeImpl();
		this.client = client;
		this.refreshToken = refreshToken;

		refreshToken(refreshToken);
		authbuilder = AuthInfo.newBuilder();
	}
	
	/**
	 * Used for logging in when you dont have a persisted refresh token.
	 *
	 * @param client                             OkHttp client
	 * @param time                               a Time implementation
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException When server fails
	 */
	public GoogleUserCredentialProvider(OkHttpClient client, Time time)
			throws LoginFailedException, RemoteServerException {
		this.time = time;
		this.client = client;

		authbuilder = AuthInfo.newBuilder();
	}

	/**
	 * Used for logging in when you dont have a persisted refresh token.
	 *
	 * @param client                             OkHttp client
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException When server fails
	 */
	public GoogleUserCredentialProvider(OkHttpClient client)
			throws LoginFailedException, RemoteServerException {
		this.time = new SystemTimeImpl();
		this.client = client;

		authbuilder = AuthInfo.newBuilder();
	}


	/**
	 * Given the refresh token fetches a new access token and returns AuthInfo.
	 *
	 * @param refreshToken Refresh token persisted by the user after initial login
	 * @throws LoginFailedException If we fail to get tokenId
	 */
	public void refreshToken(String refreshToken) throws LoginFailedException, RemoteServerException {
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
			throw new RemoteServerException("Network Request failed to fetch refreshed tokenId", e);
		}
		Moshi moshi = new Moshi.Builder().build();
		GoogleAuthTokenJson googleAuthTokenJson = null;
		try {
			googleAuthTokenJson = moshi.adapter(GoogleAuthTokenJson.class).fromJson(response.body().string());
			Log.d(TAG, "" + googleAuthTokenJson.getExpiresIn());
		} catch (IOException e) {
			throw new RemoteServerException("Failed to unmarshal the Json response to fetch refreshed tokenId", e);
		}
		if (googleAuthTokenJson.getError() != null) {
			throw new LoginFailedException(googleAuthTokenJson.getError());
		} else {
			Log.d(TAG, "Refreshed Token " + googleAuthTokenJson.getIdToken());
			expiresTimestamp = time.currentTimeMillis()
					+ (googleAuthTokenJson.getExpiresIn() * 1000 - REFRESH_TOKEN_BUFFER_TIME);
			tokenId = googleAuthTokenJson.getIdToken();
		}
	}


	/**
	 * Uses an access code to login and get tokens
	 */
	public void login(String authcode) throws LoginFailedException, RemoteServerException {

		HttpUrl url = HttpUrl.parse(OAUTH_TOKEN_ENDPOINT).newBuilder()
				.addQueryParameter("code", authcode)
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
			throw new RemoteServerException("Network Request failed to fetch tokenId", e);
		}

		Moshi moshi = new Moshi.Builder().build();

		GoogleAuthTokenJson googleAuth = null;
		try {
			googleAuth = moshi.adapter(GoogleAuthTokenJson.class).fromJson(response.body().string());
			Log.d(TAG, "" + googleAuth.getExpiresIn());
		} catch (IOException e) {
			throw new RemoteServerException("Failed to unmarshell the Json response to fetch tokenId", e);
		}

		Log.d(TAG, "Got token: " + googleAuth.getAccessToken());

		expiresTimestamp = time.currentTimeMillis()
				+ (googleAuth.getExpiresIn() * 1000 - REFRESH_TOKEN_BUFFER_TIME);
		tokenId = googleAuth.getIdToken();
		refreshToken = googleAuth.getRefreshToken();
	}
	
	@Override
	public String getTokenId() throws LoginFailedException, RemoteServerException {
		if (isTokenIdExpired()) {
			refreshToken(refreshToken);
		}
		return tokenId;
	}

	/**
	 * Refreshes tokenId if it has expired
	 *
	 * @return AuthInfo object
	 * @throws LoginFailedException When login fails
	 */
	@Override
	public AuthInfo getAuthInfo() throws LoginFailedException, RemoteServerException {
		if (isTokenIdExpired()) {
			refreshToken(refreshToken);
		}
		authbuilder.setProvider("google");
		authbuilder.setToken(AuthInfo.JWT.newBuilder().setContents(tokenId).setUnknown2(59).build());
		return authbuilder.build();
	}

	@Override
	public boolean isTokenIdExpired() {
		if (time.currentTimeMillis() > expiresTimestamp) {
			return true;
		} else {
			return false;
		}
	}
}
