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
import com.pokegoapi.util.SystemTimeImpl;
import com.pokegoapi.util.Time;
import com.squareup.moshi.Moshi;
import lombok.Setter;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PtcCredentialProvider extends CredentialProvider {
	public static final String CLIENT_SECRET = "w8ScCUXJQc6kXKw8FiOhd8Fixzht18Dq3PEVkUCP5ZPxtgyWsbTvWHFLm2wNY0JR";
	public static final String REDIRECT_URI = "https://www.nianticlabs.com/pokemongo/error";
	public static final String CLIENT_ID = "mobile-app_pokemon-go";
	public static final String SERVICE_URL = "https://sso.pokemon.com/sso/oauth2.0/callbackAuthorize";
	public static final String LOGIN_URL = "https://sso.pokemon.com/sso/login";
	public static final String LOGIN_OAUTH = "https://sso.pokemon.com/sso/oauth2.0/accessToken";
	public static final String USER_AGENT = "pokemongo/1 CFNetwork/897.1 Darwin/17.5.0"; //Lasted iOS 11.3.0
	//We try and refresh token 5 minutes before it actually expires
	protected static final long REFRESH_TOKEN_BUFFER_TIME = 5 * 60 * 1000;
	protected static final int MAXIMUM_RETRIES = 5;
	protected static final int[] UK2_VALUES = new int[]{2, 8, 21, 24, 28, 37, 56, 58, 59};

	protected final OkHttpClient client;
	protected final String username;
	protected final String password;
	protected final Time time;
	protected String tokenId;
	protected long expiresTimestamp;
	protected AuthInfo.Builder authbuilder;

	protected SecureRandom random = new SecureRandom();

	@Setter
	protected boolean shouldRetry = true;

	/**
	 * Instantiates a new Ptc login.
	 *
	 * @param client the client
	 * @param username Username
	 * @param password password
	 * @param time a Time implementation
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public PtcCredentialProvider(OkHttpClient client, String username, String password, Time time)
			throws LoginFailedException, InvalidCredentialsException {
		this.time = time;
		this.username = username;
		this.password = password;
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
						return chain.proceed(chain.request()
								.newBuilder()
								.removeHeader("User-Agent")
								.addHeader("User-Agent", USER_AGENT)
								.build());
					}
				})
				.build();

		authbuilder = AuthInfo.newBuilder();
		login(username, password, 0);
	}

	/**
	 * Instantiates a new Ptc login.
	 * Deprecated: specify a Time implementation
	 *
	 * @param client the client
	 * @param username Username
	 * @param password password
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	public PtcCredentialProvider(OkHttpClient client, String username, String password)
			throws LoginFailedException, InvalidCredentialsException {
		this(client, username, password, new SystemTimeImpl());
	}

	/**
	 * Starts a login flow for pokemon.com (PTC) using a username and password,
	 * this uses pokemon.com's oauth endpoint and returns a usable AuthInfo without user interaction
	 *
	 * @param username PTC username
	 * @param password PTC password
	 * @param attempt the current attempt index
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	private void login(String username, String password, int attempt)
			throws LoginFailedException, InvalidCredentialsException {
		try {
			//TODO: stop creating an okhttp client per request

			Response getResponse;
			try {
				getResponse = client.newCall(new Request.Builder()
						.url(HttpUrl.parse("https://sso.pokemon.com/sso/oauth2.0/authorize").newBuilder()
								.addQueryParameter("client_id", CLIENT_ID)
								.addQueryParameter("redirect_uri", REDIRECT_URI)
								.addQueryParameter("locale", "en")
								.build())
						.get()
						.build())
						.execute();
			} catch (IOException e) {
				throw new LoginFailedException("Failed to receive contents from server", e);
			}

			Moshi moshi = new Moshi.Builder().build();

			PtcAuthJson ptcAuth;
			try {
				ptcAuth = moshi.adapter(PtcAuthJson.class).fromJson(getResponse.body().string());
			} catch (IOException e) {
				throw new LoginFailedException("Looks like the servers are down", e);
			}

			Response postResponse;
			try {
				FormBody postForm = new Builder()
						.add("lt", ptcAuth.getLt())
						.add("execution", ptcAuth.getExecution())
						.add("_eventId", "submit")
						.add("username", username)
						.add("password", password)
						.add("locale", "en_US")
						.build();
				HttpUrl loginPostUrl = HttpUrl.parse(LOGIN_URL).newBuilder()
						.addQueryParameter("service", SERVICE_URL)
						.build();
				Request postRequest = new Request.Builder()
						.url(loginPostUrl)
						.post(postForm)
						.build();

				// Need a new client for this to not follow redirects
				postResponse = client.newBuilder()
						.followRedirects(false)
						.followSslRedirects(false)
						.build()
						.newCall(postRequest)
						.execute();
			} catch (IOException e) {
				throw new LoginFailedException("Network failure", e);
			}

			String postBody;
			try {
				postBody = postResponse.body().string();
			} catch (IOException e) {
				throw new LoginFailedException("Response body fetching failed", e);
			}

			List<Cookie> cookies = client.cookieJar().loadForRequest(HttpUrl.parse(LOGIN_URL));
			for (Cookie cookie : cookies) {
				if (cookie.name().startsWith("CASTGC")) {
					this.tokenId = cookie.value();
					expiresTimestamp = time.currentTimeMillis() + 7140000L;
					return;
				}
			}

			if (postBody.length() > 0) {
				try {
					String[] errors = moshi.adapter(PtcAuthError.class).fromJson(postBody).getErrors();
					if (errors != null && errors.length > 0) {
						throw new InvalidCredentialsException(errors[0]);
					}
				} catch (IOException e) {
					throw new LoginFailedException("Failed to parse ptc error json");
				}
			}
		} catch (LoginFailedException e) {
			if (shouldRetry && attempt < MAXIMUM_RETRIES) {
				login(username, password, ++attempt);
			} else {
				throw new LoginFailedException("Exceeded maximum login retries", e);
			}
		}
	}

	@Override
	public String getTokenId(boolean refresh) throws LoginFailedException, InvalidCredentialsException {
		if (refresh || isTokenIdInvalid()) {
			login(username, password, 0);
		}
		return tokenId;
	}

	/**
	 * Valid auth info object	 *
	 *
	 * @param refresh if this AuthInfo should be refreshed
	 * @return AuthInfo a AuthInfo proto structure to be encapsulated in server requests
	 * @throws LoginFailedException if an exception occurs while attempting to log in
	 * @throws InvalidCredentialsException if invalid credentials are used
	 */
	@Override
	public AuthInfo getAuthInfo(boolean refresh) throws LoginFailedException, InvalidCredentialsException {
		if (refresh || isTokenIdInvalid()) {
			login(username, password, 0);
		}

		authbuilder.setProvider("ptc");
		authbuilder.setToken(AuthInfo.JWT.newBuilder().setContents(tokenId).setUnknown2(59).build());

		return authbuilder.build();
	}

	@Override
	public boolean isTokenIdExpired() {
		return isTokenIdInvalid();
	}

	@Override
	public boolean isTokenIdInvalid() {
		return tokenId == null || time.currentTimeMillis() > expiresTimestamp;
	}

	@Override
	public void reset() {
		tokenId = null;
		expiresTimestamp = 0;
	}
}
