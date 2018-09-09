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
import com.squareup.moshi.Moshi;
import lombok.Getter;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @deprecated use {@link GoogleAutoCredentialProvider}
 */
@Deprecated
public class GoogleCredentialProvider extends CredentialProvider {

	public static final String SECRET = "NCjF1TLi2CcY6t5mt0ZveuL7";
	public static final String CLIENT_ID = "848232511240-73ri3t7plvk96pj4f85uj8otdat2alem.apps.googleusercontent.com";
	public static final String OAUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/device/code";
	public static final String OAUTH_TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token";
	private static final String TAG = GoogleCredentialProvider.class.getSimpleName();
	//We try and refresh token 5 minutes before it actually expires
	private static final long REFRESH_TOKEN_BUFFER_TIME = 5 * 60 * 1000;
	private final OkHttpClient client;

	private final OnGoogleLoginOAuthCompleteListener onGoogleLoginOAuthCompleteListener;

	private long expiresTimestamp;

	private String tokenId;

	@Getter
	private String refreshToken;

	private AuthInfo.Builder authbuilder;

	/**
	 * Used for logging in when one has a persisted refreshToken.
	 *
	 * @param client OkHttp client
	 * @param refreshToken Refresh Token Persisted by user
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public GoogleCredentialProvider(OkHttpClient client, String refreshToken)
			throws LoginFailedException, InvalidCredentialsException {
		this.client = client;
		this.refreshToken = refreshToken;
		onGoogleLoginOAuthCompleteListener = null;
		refreshToken(refreshToken);
		authbuilder = AuthInfo.newBuilder();
	}

	/**
	 * Used for logging in when you dont have a persisted refresh token.
	 *
	 * @param client OkHttp client
	 * @param onGoogleLoginOAuthCompleteListener Callback to know verification url and also persist refresh token
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public GoogleCredentialProvider(OkHttpClient client,
			OnGoogleLoginOAuthCompleteListener onGoogleLoginOAuthCompleteListener)
			throws LoginFailedException, InvalidCredentialsException {
		this.client = client;
		if (onGoogleLoginOAuthCompleteListener != null) {
			this.onGoogleLoginOAuthCompleteListener = onGoogleLoginOAuthCompleteListener;
		} else {
			throw new LoginFailedException("You need to implement OnGoogleLoginOAuthCompleteListener");
		}
		login();
		authbuilder = AuthInfo.newBuilder();
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
			throw new InvalidCredentialsException(googleAuthTokenJson.error);
		} else {
			Log.d(TAG, "Refreshed Token " + googleAuthTokenJson.idToken);
			expiresTimestamp = System.currentTimeMillis()
					+ (googleAuthTokenJson.expiresIn * 1000 - REFRESH_TOKEN_BUFFER_TIME);
			tokenId = googleAuthTokenJson.idToken;
		}
	}

	/**
	 * Starts a login flow for google using googles device oauth endpoint.
	 *
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public void login() throws LoginFailedException, InvalidCredentialsException {
		HttpUrl url = HttpUrl.parse(OAUTH_ENDPOINT).newBuilder()
				.addQueryParameter("client_id", CLIENT_ID)
				.addQueryParameter("scope", "openid email https://www.googleapis.com/auth/userinfo.email")
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

		GoogleAuthJson googleAuth = null;
		try {
			googleAuth = moshi.adapter(GoogleAuthJson.class).fromJson(response.body().string());
			Log.d(TAG, "" + googleAuth.expiresIn);
		} catch (IOException e) {
			throw new LoginFailedException("Failed to unmarshell the Json response to fetch tokenId", e);
		}
		Log.d(TAG, "Get user to go to:"
				+ googleAuth.verificationUrl
				+ " and enter code:" + googleAuth.userCode);
		onGoogleLoginOAuthCompleteListener.onInitialOAuthComplete(googleAuth);

		GoogleAuthTokenJson googleAuthTokenJson;
		try {
			while ((googleAuthTokenJson = poll(googleAuth)) == null) {
				Thread.sleep(googleAuth.interval * 1000);
			}
		} catch (InterruptedException e) {
			throw new LoginFailedException("Sleeping was interrupted", e);
		} catch (IOException e) {
			throw new LoginFailedException(e);
		} catch (URISyntaxException e) {
			throw new LoginFailedException(e);
		}

		Log.d(TAG, "Got token: " + googleAuthTokenJson.idToken);
		onGoogleLoginOAuthCompleteListener.onTokenIdReceived(googleAuthTokenJson);
		expiresTimestamp = System.currentTimeMillis()
				+ (googleAuthTokenJson.expiresIn * 1000 - REFRESH_TOKEN_BUFFER_TIME);
		tokenId = googleAuthTokenJson.idToken;
		refreshToken = googleAuthTokenJson.refreshToken;
	}

	/**
	 * Get the google auth token json from the google auth json
	 *
	 * @param json google auth json
	 * @return google auth token json
	 * @throws URISyntaxException syntax exception
	 * @throws IOException io exception
	 * @throws LoginFailedException If we fail to get tokenId
	 */
	private GoogleAuthTokenJson poll(GoogleAuthJson json) throws URISyntaxException, IOException,
			LoginFailedException {
		HttpUrl url = HttpUrl.parse(OAUTH_TOKEN_ENDPOINT).newBuilder()
				.addQueryParameter("client_id", CLIENT_ID)
				.addQueryParameter("client_secret", SECRET)
				.addQueryParameter("code", json.deviceCode)
				.addQueryParameter("grant_type", "http://oauth.net/grant_type/device/1.0")
				.addQueryParameter("scope", "openid email https://www.googleapis.com/auth/userinfo.email")
				.build();

		//Empty request body
		RequestBody reqBody = RequestBody.create(null, new byte[0]);
		Request request = new Request.Builder()
				.url(url)
				.method("POST", reqBody)
				.build();

		Response response = client.newCall(request).execute();

		Moshi moshi = new Moshi.Builder().build();
		GoogleAuthTokenJson token = moshi.adapter(GoogleAuthTokenJson.class).fromJson(response.body().string());

		if (token.error == null) {
			return token;
		} else {
			return null;
		}
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
	public AuthInfo getAuthInfo(boolean refresh) throws LoginFailedException, InvalidCredentialsException {
		if (refresh || isTokenIdInvalid()) {
			refreshToken(refreshToken);
		}
		authbuilder.setProvider("google");
		authbuilder.setToken(AuthInfo.JWT.newBuilder().setContents(tokenId).setUnknown2(0).build());
		return authbuilder.build();
	}

	@Override
	public boolean isTokenIdInvalid() {
		return tokenId == null || System.currentTimeMillis() > expiresTimestamp;
	}

	@Override
	public void reset() {
		tokenId = null;
		refreshToken = null;
	}

	/**
	 * This callback will be called when we get the
	 * verification url and device code.
	 * This will allow applications to
	 * programmatically redirect user to redirect user.
	 */
	public interface OnGoogleLoginOAuthCompleteListener {
		/**
		 * Good Hook to provide custom redirects to verification url page.
		 *
		 * @param googleAuthJson GoogleAuth object
		 */
		void onInitialOAuthComplete(GoogleAuthJson googleAuthJson);

		/**
		 * Good Idea to persist the refresh token recieved here
		 *
		 * @param googleAuthTokenJson GoogleAuthToken object
		 */
		void onTokenIdReceived(GoogleAuthTokenJson googleAuthTokenJson);
	}
}
