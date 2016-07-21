package com.pokegoapi.auth;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pokegoapi.exceptions.LoginFailedException;
import lombok.Getter;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PTCLogin extends Login {
	public static final String CLIENT_SECRET = "w8ScCUXJQc6kXKw8FiOhd8Fixzht18Dq3PEVkUCP5ZPxtgyWsbTvWHFLm2wNY0JR";
	public static final String REDIRECT_URI = "https://www.nianticlabs.com/pokemongo/error";
	public static final String CLIENT_ID = "mobile-app_pokemon-go";

	public static final String API_URL = "https://pgorelease.nianticlabs.com/plfe/rpc";
	public static final String LOGIN_URL = "https://sso.pokemon.com/sso/login?service=https%3A%2F%2Fsso.pokemon.com%2Fsso%2Foauth2.0%2FcallbackAuthorize";
	public static final String LOGIN_OAUTH = "https://sso.pokemon.com/sso/oauth2.0/accessToken";

	private static final RequestBody emptyRequestBody = RequestBody.create(null, new byte[0]);

	public static final String USER_AGENT = "niantic";

	private final OkHttpClient client;

	@Getter
	String token;

	public PTCLogin(OkHttpClient client) {
		  /*
			This is a temporary, in-memory cookie jar.
			We don't require any persistence outside of the scope of the login,
			so it being discarded is completely fine
		   */
		CookieJar tempJar = new CookieJar() {
			private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

			@Override
			public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
				cookieStore.put(url.host(), cookies);
			}

			@Override
			public List<Cookie> loadForRequest(HttpUrl url) {
				List<Cookie> cookies = cookieStore.get(url.host());
				return cookies != null ? cookies : new ArrayList<Cookie>();
			}
		};

		this.client = client.newBuilder()
				.cookieJar(tempJar)
				.addInterceptor(new Interceptor() {
					@Override
					public Response intercept(Chain chain) throws IOException {
						//Makes sure the User-Agent is always set
						Request req = chain.request();
						req = req.newBuilder().header("User-Agent", USER_AGENT).build();
						return chain.proceed(req);
					}
				})
				.build();
	}

	/**
	 * Returns an AuthInfo object given a token, this should not be an access token but rather an id_token
	 *
	 * @param token the id_token stored from a previous oauth attempt.
	 * @return AuthInfo a AuthInfo proto structure to be encapsulated in server requests
	 */
	public AuthInfo login(String token) {
		return buildAuthInfo(token);
	}

	/**
	 * Starts a login flow for pokemon.com (PTC) using a username and password, this uses pokemon.com's oauth endpoint and returns a usable AuthInfo without user interaction
	 *
	 * @param username PTC username
	 * @param password PTC password
	 * @return AuthInfo a AuthInfo proto structure to be encapsulated in server requests
	 */
	public AuthInfo login(String username, String password) throws LoginFailedException {

		try {
			String ticket = requestAuthenticationTicket(username, password);
			String token = generateOauthToken(ticket);

			return buildAuthInfo(token);
		} catch (Exception e) {
			throw new LoginFailedException(e);
		}
	}

	private AuthInfo buildAuthInfo(String token) {
		AuthInfo.Builder authbuilder = AuthInfo.newBuilder();
		authbuilder.setProvider("ptc");
		authbuilder.setToken(AuthInfo.JWT.newBuilder().setContents(token).setUnknown2(59).build());

		return authbuilder.build();
	}

	private String requestAuthenticationTicket(String username, String password) throws IOException, LoginFailedException {
		Request get = new Request.Builder()
				.url(LOGIN_URL)
				.get()
				.build();

		Response getResponse = client.newCall(get).execute();

		Gson gson = new GsonBuilder().create();

		PTCAuthJson ptcAuth = gson.fromJson(getResponse.body().string(), PTCAuthJson.class);

		HttpUrl url = HttpUrl.parse(LOGIN_URL).newBuilder()
                .addQueryParameter("lt", ptcAuth.getLt())
                .addQueryParameter("execution", ptcAuth.getExecution())
                .addQueryParameter("_eventId", "submit")
                .addQueryParameter("username", username)
                .addQueryParameter("password", password)
                .build();

		Request postRequest = new Request.Builder()
                .url(url)
                .method("POST", emptyRequestBody)
                .build();

		// Need a new client for this to not follow redirects
		Response response = client.newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build()
                .newCall(postRequest)
                .execute();

		String body = response.body().string();

		if (body.length() > 0) {
            PTCError ptcError = gson.fromJson(body, PTCError.class);
            if (ptcError.getError() != null && ptcError.getError().length() > 0) {
                throw new LoginFailedException();
            }
        }

		String ticket = null;
		for (String location : response.headers("location")) {
            ticket = location.split("ticket=")[1];
        }
		return ticket;
	}

	private String generateOauthToken(String ticket) throws IOException, LoginFailedException {
		HttpUrl url;
		Request postRequest;
		Response response;
		String body;
		url = HttpUrl.parse(LOGIN_OAUTH).newBuilder()
				.addQueryParameter("client_id", CLIENT_ID)
				.addQueryParameter("redirect_uri", REDIRECT_URI)
				.addQueryParameter("client_secret", CLIENT_SECRET)
				.addQueryParameter("grant_type", "refresh_token")
				.addQueryParameter("code", ticket)
				.build();

		postRequest = new Request.Builder()
				.url(url)
				.method("POST", emptyRequestBody)
				.build();

		response = client.newCall(postRequest).execute();

		body = response.body().string();

		try {
			String token = body.split("token=")[1];
			token = token.split("&")[0];
			return token;
		} catch (Exception e) {
			throw new LoginFailedException(e);
		}
	}

}
