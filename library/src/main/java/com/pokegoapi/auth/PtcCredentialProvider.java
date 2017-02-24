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
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.SystemTimeImpl;
import com.pokegoapi.util.Time;
import com.squareup.moshi.Moshi;
import lombok.Setter;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PtcCredentialProvider extends CredentialProvider {
	public static final String CLIENT_SECRET = "w8ScCUXJQc6kXKw8FiOhd8Fixzht18Dq3PEVkUCP5ZPxtgyWsbTvWHFLm2wNY0JR";
	public static final String REDIRECT_URI = "https://www.nianticlabs.com/pokemongo/error";
	public static final String CLIENT_ID = "mobile-app_pokemon-go";
	public static final String API_URL = "https://pgorelease.nianticlabs.com/plfe/rpc";
	public static final String LOGIN_URL = "https://sso.pokemon.com/sso/login?service=https%3A%2F%2Fsso.pokemon"
			+ ".com%2Fsso%2Foauth2.0%2FcallbackAuthorize";
	public static final String LOGIN_OAUTH = "https://sso.pokemon.com/sso/oauth2.0/accessToken";
	public static final String USER_AGENT = "niantic";
	private static final String TAG = PtcCredentialProvider.class.getSimpleName();
	//We try and refresh token 5 minutes before it actually expires
	protected static final long REFRESH_TOKEN_BUFFER_TIME = 5 * 60 * 1000;
	protected static final int MAXIMUM_RETRIES = 5;

	protected final OkHttpClient client;
	protected final String username;
	protected final String password;
	protected final Time time;
	protected String tokenId;
	protected long expiresTimestamp;
	protected AuthInfo.Builder authbuilder;

	@Setter
	protected boolean shouldRetry = true;

	/**
	 * Instantiates a new Ptc login.
	 *
	 * @param client the client
	 * @param username Username
	 * @param password password
	 * @param time a Time implementation
	 * @throws LoginFailedException When login fails
	 * @throws RemoteServerException When server fails
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public PtcCredentialProvider(OkHttpClient client, String username, String password, Time time)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
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
						Request req = chain.request();
						req = req.newBuilder().header("User-Agent", USER_AGENT).build();
						return chain.proceed(req);
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
	 * @throws LoginFailedException if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public PtcCredentialProvider(OkHttpClient client, String username, String password)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		this(client, username, password, new SystemTimeImpl());
	}

	/**
	 * Starts a login flow for pokemon.com (PTC) using a username and password,
	 * this uses pokemon.com's oauth endpoint and returns a usable AuthInfo without user interaction
	 *
	 * @param username PTC username
	 * @param password PTC password
	 * @param attempt the current attempt index
	 * @throws LoginFailedException if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	private void login(String username, String password, int attempt)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		boolean hasPTCError = false;

		try {
			//TODO: stop creating an okhttp client per request
			Request get = new Request.Builder()
					.url(LOGIN_URL)
					.get()
					.build();

			Response getResponse;
			try {
				getResponse = client.newCall(get).execute();
			} catch (IOException e) {
				throw new RemoteServerException("Failed to receive contents from server", e);
			}

			Moshi moshi = new Moshi.Builder().build();

			PtcAuthJson ptcAuth;
			try {
				String response = getResponse.body().string();
				ptcAuth = moshi.adapter(PtcAuthJson.class).fromJson(response);
			} catch (IOException e) {
				throw new RemoteServerException("Looks like the servers are down", e);
			}

			HttpUrl url = HttpUrl.parse(LOGIN_URL).newBuilder()
					.addQueryParameter("lt", ptcAuth.getLt())
					.addQueryParameter("execution", ptcAuth.getExecution())
					.addQueryParameter("_eventId", "submit")
					.addQueryParameter("username", username)
					.addQueryParameter("password", password)
					.build();

			RequestBody reqBody = RequestBody.create(null, new byte[0]);

			Request postRequest = new Request.Builder()
					.url(url)
					.method("POST", reqBody)
					.build();

			// Need a new client for this to not follow redirects
			Response response;
			try {
				response = client.newBuilder()
						.followRedirects(false)
						.followSslRedirects(false)
						.build()
						.newCall(postRequest)
						.execute();
			} catch (IOException e) {
				throw new RemoteServerException("Network failure", e);
			}

			String body;
			try {
				body = response.body().string();
			} catch (IOException e) {
				throw new RemoteServerException("Response body fetching failed", e);
			}

			if (body.length() > 0) {
				PtcError ptcError;
				try {
					ptcError = moshi.adapter(PtcError.class).fromJson(body);
					if (ptcError != null) {
						hasPTCError = true;
					}
				} catch (IOException e) {
					throw new RemoteServerException("Unmarshalling failure", e);
				}
				if (ptcError.getError() != null && ptcError.getError().length() > 0) {
					throw new LoginFailedException(ptcError.getError());
				} else if (ptcError.getErrors().length > 0) {
					StringBuilder builder = new StringBuilder();
					String[] errors = ptcError.getErrors();
					for (int i = 0; i < errors.length - 1; i++) {
						String error = errors[i];
						builder.append("\"").append(error).append("\", ");
					}
					builder.append("\"").append(errors[errors.length - 1]).append("\"");
					throw new LoginFailedException(builder.toString());
				}
			}

			String ticket = null;
			for (String location : response.headers("location")) {
				String[] ticketArray = location.split("ticket=");
				if (ticketArray.length > 1) {
					ticket = ticketArray[1];
				}
			}

			if (ticket == null) {
				throw new LoginFailedException("Failed to fetch token, body:" + body);
			}

			url = HttpUrl.parse(LOGIN_OAUTH).newBuilder()
					.addQueryParameter("client_id", CLIENT_ID)
					.addQueryParameter("redirect_uri", REDIRECT_URI)
					.addQueryParameter("client_secret", CLIENT_SECRET)
					.addQueryParameter("grant_type", "refreshToken")
					.addQueryParameter("code", ticket)
					.build();

			postRequest = new Request.Builder()
					.url(url)
					.method("POST", reqBody)
					.build();

			try {
				response = client.newCall(postRequest).execute();
			} catch (IOException e) {
				throw new RemoteServerException("Network Failure ", e);
			}

			try {
				body = response.body().string();
			} catch (IOException e) {
				throw new RemoteServerException("Network failure", e);
			}

			String[] params;
			try {
				params = body.split("&");
				this.tokenId = params[0].split("=")[1];
				this.expiresTimestamp = time.currentTimeMillis()
						+ (Integer.valueOf(params[1].split("=")[1]) * 1000 - REFRESH_TOKEN_BUFFER_TIME);
			} catch (Exception e) {
				throw new LoginFailedException("Failed to fetch token, body:" + body);
			}
		} catch (Exception e) {
			if (!hasPTCError && shouldRetry && attempt < MAXIMUM_RETRIES) {
				login(username, password, ++attempt);
			} else {
				throw e;
			}
		}
	}

	@Override
	public String getTokenId(boolean refresh) throws LoginFailedException, CaptchaActiveException,
			RemoteServerException {
		if (refresh || isTokenIdExpired()) {
			login(username, password, 0);
		}
		return tokenId;
	}

	/**
	 * Valid auth info object	 *
	 *
	 * @param refresh if this AuthInfo should be refreshed
	 * @return AuthInfo a AuthInfo proto structure to be encapsulated in server requests
	 * @throws LoginFailedException if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	@Override
	public AuthInfo getAuthInfo(boolean refresh) throws LoginFailedException, CaptchaActiveException,
			RemoteServerException {
		if (refresh || isTokenIdExpired()) {
			login(username, password, 0);
		}

		authbuilder.setProvider("ptc");
		authbuilder.setToken(AuthInfo.JWT.newBuilder().setContents(tokenId).setUnknown2(59).build());

		return authbuilder.build();
	}

	@Override
	public boolean isTokenIdExpired() {
		return time.currentTimeMillis() > expiresTimestamp;
	}
}
