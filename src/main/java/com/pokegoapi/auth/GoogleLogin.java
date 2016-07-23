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
import com.pokegoapi.util.Log;
import com.squareup.moshi.Moshi;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.URISyntaxException;

public class GoogleLogin extends Login {

	private static final String TAG = GoogleLogin.class.getSimpleName();

	private final OkHttpClient client;

	public GoogleLogin(OkHttpClient client) {
		this.client = client;
	}

	/**
	 * Given the refresh token fetches a new access token and returns AuthInfo.
	 * @param refreshToken Refresh token returned during initial login
	 * @return Refreshed AuthInfo object
	 * @throws IOException If the network call fails
	 */
	public AuthInfo refreshToken(String refreshToken) throws IOException {
		HttpUrl url = HttpUrl.parse(GoogleLoginSecrets.OAUTH_TOKEN_ENDPOINT).newBuilder()
				.addQueryParameter("client_id", GoogleLoginSecrets.CLIENT_ID)
				.addQueryParameter("client_secret", GoogleLoginSecrets.SECRET)
				.addQueryParameter("refreshToken", refreshToken)
				.addQueryParameter("grant_type", "refreshToken")
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
		if (token.getError() == null) {
			return null;
		} else {
			AuthInfo.Builder builder = AuthInfo.newBuilder();
			builder.setProvider("google");
			builder.setToken(AuthInfo.JWT.newBuilder().setContents(token.getIdToken()).setUnknown2(59).build());
			return builder.build();
		}
	}

	/**
	 * Returns an AuthInfo object given a token, this should not be an access token but rather an id_token
	 *
	 * @param token the id_token stored from a previous oauth attempt.
	 * @return AuthInfo a AuthInfo proto structure to be encapsulated in server requests
	 */
	public AuthInfo login(String token) {
		AuthInfo.Builder builder = AuthInfo.newBuilder();
		builder.setProvider("google");
		builder.setToken(AuthInfo.JWT.newBuilder().setContents(token).setUnknown2(59).build());
		return builder.build();
	}

	/**
	 * Starts a login flow for google using a username and password, this uses googles device oauth endpoint,
	 * a URL and code is displayed, not really ideal right now.
	 *
	 * @param username Google username
	 * @param password Google password
	 * @return AuthInfo a AuthInfo proto structure to be encapsulated in server requests
	 */
	public AuthInfo login(String username, String password) throws LoginFailedException {
		try {
			HttpUrl url = HttpUrl.parse(GoogleLoginSecrets.OAUTH_ENDPOINT).newBuilder()
					.addQueryParameter("client_id", GoogleLoginSecrets.CLIENT_ID)
					.addQueryParameter("scope", "openid email https://www.googleapis.com/auth/userinfo.email")
					.build();

			//Create empty body
			RequestBody reqBody = RequestBody.create(null, new byte[0]);

			Request request = new Request.Builder()
					.url(url)
					.method("POST", reqBody)
					.build();

			Response response = client.newCall(request).execute();

			Moshi moshi = new Moshi.Builder().build();

			GoogleAuthJson googleAuth = moshi.adapter(GoogleAuthJson.class).fromJson(response.body().string());
			Log.d(TAG, "Get user to go to:"
					+ googleAuth.getVerificationUrl()
					+ " and enter code:" + googleAuth.getUserCode());

			GoogleAuthTokenJson token;
			while ((token = poll(googleAuth)) == null) {
				Thread.sleep(googleAuth.getInterval() * 1000);
			}

			Log.d(TAG, "Got token: " + token.getIdToken());
			GoogleLoginSecrets.refresh_token = token.getRefreshToken();
			AuthInfo.Builder authbuilder = AuthInfo.newBuilder();
			authbuilder.setProvider("google");
			authbuilder.setToken(AuthInfo.JWT.newBuilder().setContents(token.getIdToken()).setUnknown2(59).build());

			return authbuilder.build();
		} catch (Exception e) {
			throw new LoginFailedException();
		}

	}


	private GoogleAuthTokenJson poll(GoogleAuthJson json) throws URISyntaxException, IOException {
		HttpUrl url = HttpUrl.parse(GoogleLoginSecrets.OAUTH_TOKEN_ENDPOINT).newBuilder()
				.addQueryParameter("client_id", GoogleLoginSecrets.CLIENT_ID)
				.addQueryParameter("client_secret", GoogleLoginSecrets.SECRET)
				.addQueryParameter("code", json.getDeviceCode())
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

		if (token.getError() == null) {
			return token;
		} else {
			return null;
		}

	}

}
