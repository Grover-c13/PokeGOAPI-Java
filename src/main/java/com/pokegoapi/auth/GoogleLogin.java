package com.pokegoapi.auth;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pokegoapi.exceptions.LoginFailedException;
import okhttp3.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class GoogleLogin extends Login {
	public static final String SECRET = "NCjF1TLi2CcY6t5mt0ZveuL7";
	public static final String CLIENT_ID = "848232511240-73ri3t7plvk96pj4f85uj8otdat2alem.apps.googleusercontent.com";
	public static final String OAUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/device/code";
	public static final String OAUTH_TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token";

	private final OkHttpClient client;

	public GoogleLogin(OkHttpClient client) {
		this.client = client;
	}

	/**
	 * Returns an AuthInfo object given a token, this should not be an access token but rather an id_token
	 *
	 * @param String the id_token stored from a previous oauth attempt.
	 * @return AuthInfo a AuthInfo proto structure to be encapsulated in server requests
	 */
	public AuthInfo login(String token) {
		AuthInfo.Builder builder = AuthInfo.newBuilder();
		builder.setProvider("google");
		builder.setToken(AuthInfo.JWT.newBuilder().setContents(token).setUnknown2(59).build());
		return builder.build();
	}

	/**
	 * Starts a login flow for google using a username and password, this uses googles device oauth endpoint, a URL and code is display
	 * not really ideal right now.
	 *
	 * @param String Google username
	 * @param String Google password
	 * @return AuthInfo a AuthInfo proto structure to be encapsulated in server requests
	 */
	public AuthInfo login(String username, String password) throws LoginFailedException {
		try {


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

			Response response = client.newCall(request).execute();

			Gson gson = new GsonBuilder().create();

			GoogleAuthJson googleAuth = gson.fromJson(response.body().string(), GoogleAuthJson.class);
			System.out.println("Get user to go to:" + googleAuth.getVerification_url() + " and enter code:" + googleAuth.getUser_code());

			GoogleAuthTokenJson token;
			while ((token = poll(googleAuth)) == null) {
				Thread.sleep(googleAuth.getInterval() * 1000);
			}

			System.out.println("Got token:" + token.getId_token());

			AuthInfo.Builder authbuilder = AuthInfo.newBuilder();
			authbuilder.setProvider("google");
			authbuilder.setToken(AuthInfo.JWT.newBuilder().setContents(token.getId_token()).setUnknown2(59).build());

			return authbuilder.build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LoginFailedException();
		}

	}


	private GoogleAuthTokenJson poll(GoogleAuthJson json) throws URISyntaxException, IOException {
		HttpUrl url = HttpUrl.parse(OAUTH_TOKEN_ENDPOINT).newBuilder()
				.addQueryParameter("client_id", CLIENT_ID)
				.addQueryParameter("client_secret", SECRET)
				.addQueryParameter("code", json.getDevice_code())
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

		Gson gson = new GsonBuilder().create();
		GoogleAuthTokenJson token = gson.fromJson(response.body().string(), GoogleAuthTokenJson.class);

		if (token.getError() == null) {
			return token;
		} else {
			return null;
		}

	}

}
