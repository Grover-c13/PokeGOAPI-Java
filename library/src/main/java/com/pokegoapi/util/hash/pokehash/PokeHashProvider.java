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

package com.pokegoapi.util.hash.pokehash;

import com.pokegoapi.exceptions.hash.HashException;
import com.pokegoapi.exceptions.hash.HashLimitExceededException;
import com.pokegoapi.util.hash.Hash;
import com.pokegoapi.util.hash.HashProvider;
import com.pokegoapi.util.hash.crypto.Crypto;
import com.pokegoapi.util.hash.crypto.PokeHashCrypto;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Moshi.Builder;
import lombok.Getter;
import net.iharder.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Hash provider on latest version, using the PokeHash hashing service.
 * This requires a key and is not free like the legacy provider.
 */
public class PokeHashProvider implements HashProvider {
	private static final String DEFAULT_ENDPOINT = "https://pokehash.buddyauth.com/api/v123_1/hash";

	private String endpoint = DEFAULT_ENDPOINT;

	private static final int VERSION = 5301;
	private static final long UNK25 = -76506539888958491L;

	private static final Moshi MOSHI = new Builder().build();

	private final String key;

	/**
	 * Hold the total amounts of requests per minute.
	 */
	@Getter
	public static int totalRequests;
	/**
	 * Hold how many requests left per minute.
	 */
	@Getter
	public static int requestsLeft;
	/**
	 * Hold the total time for the service (Always 60).
	 */
	@Getter
	public static long rateLimitSeconds;
	/**
	 * When that api hash service key will end.
	 * Unix Milliseconds time.
	 */
	@Getter
	public static long expirationTimeStamp;
	/**
	 * When the current minute will end.
	 * Unix Milliseconds time.
	 */
	@Getter
	public static long endOfMinute;


	/**
	 * Creates a PokeHashProvider with the given key
	 *
	 * @param key the key for the PokeHash API
	 */
	public PokeHashProvider(String key) {
		this.key = key;
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null!");
		}
	}

	/**
	 * Sets the endpoint for this hash provider
	 * @param endpoint the endpoint to use
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

  /**
   * Provides a hash for the given arguments
	 * @param timestamp timestamp to hash
	 * @param latitude latitude to hash
	 * @param longitude longitude to hash
	 * @param altitude altitude to hash
	 * @param authTicket auth ticket to hash
	 * @param sessionData session data to hash
	 * @param requests request data to hash
	 * @return the hash provider
	 * @throws HashException - if can not login to the hash service
	 */
	@Override
	public Hash provide(long timestamp, double latitude, double longitude, double altitude, byte[] authTicket,
			byte[] sessionData, byte[][] requests) throws HashException {
		Request request = new Request(latitude, longitude, altitude, timestamp, authTicket, sessionData, requests);
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("X-AuthToken", key);
			connection.setRequestProperty("content-type", "application/json");
			connection.setRequestProperty("User-Agent", "PokeGOAPI-Java");
			connection.setDoOutput(true);

			String requestJSON = MOSHI.adapter(Request.class).toJson(request);
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(requestJSON);
			out.flush();
			out.close();

			int responseCode = connection.getResponseCode();

			String error = getError(connection);

			switch (responseCode) {
				case HttpURLConnection.HTTP_OK:
					// Get the total number of requests per minute
					totalRequests = Integer.parseInt(connection.getHeaderField("X-MaxRequestCount"));
					// End of the cycle of the current minute
					endOfMinute = Integer.parseInt(connection.getHeaderField("X-RatePeriodEnd"));
					// How many requests left for the current minute
					requestsLeft = Integer.parseInt(connection.getHeaderField("X-RateRequestsRemaining"));
					// 60 always, in seconds, the calculus cycle.
					rateLimitSeconds = Integer.parseInt(connection.getHeaderField("X-RateLimitSeconds"));
					// when the Hash key is expired - Unix timestamp
					expirationTimeStamp = Long.parseLong(connection.getHeaderField("X-AuthTokenExpiration"));
					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					StringBuilder builder = new StringBuilder();
					String line;
					while ((line = in.readLine()) != null) {
						builder.append(line);
					}
					in.close();
					Response response = MOSHI.adapter(Response.class).fromJson(builder.toString());
					long locationAuth = response.getLocationAuthHash();
					long location = response.getLocationHash();
					int locationAuthHash = (int) ((locationAuth & 0xFFFFFFFFL) ^ (locationAuth >>> 32));
					int locationHash = (int) ((location & 0xFFFFFFFFL) ^ (location >>> 32));
					return new Hash(locationAuthHash, locationHash, response.getRequestHashes());
				case HttpURLConnection.HTTP_BAD_REQUEST:
					if (error.length() > 0) {
						throw new HashException(error);
					}
					throw new HashException("Bad hash request!");
				case HttpURLConnection.HTTP_UNAUTHORIZED:
					if (error.length() > 0) {
						throw new HashException(error);
					}
					throw new HashException("Unauthorized hash request!");
				case 429:
					if (error.length() > 0) {
						throw new HashLimitExceededException(error);
					}
					throw new HashLimitExceededException("Exceeded hash limit!");
				default:
					if (error.length() > 0) {
						throw new HashException(error + " (" + responseCode + ")");
					}
					throw new HashException("Received unknown response code! (" + responseCode + ")");
			}
		} catch (IOException e) {
			throw new HashException("Failed to perform PokeHash request", e);
		}
	}

	private String getError(HttpURLConnection connection) throws IOException {
		if (connection.getErrorStream() != null) {
			BufferedReader error = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = error.readLine()) != null) {
				builder.append(line);
			}
			error.close();
			return builder.toString();
		}
		return "";
	}

	@Override
	public int getHashVersion() {
		return VERSION;
	}

	@Override
	public Crypto getCrypto() {
		return PokeHashCrypto.POKE_HASH;
	}

	@Override
	public long getUNK25() {
		return UNK25;
	}

	private static class Response {
		@Getter
		private long locationAuthHash;
		@Getter
		private long locationHash;
		@Getter
		private List<Long> requestHashes;
	}

	private static class Request {
		@Getter
		private double latitude;
		@Getter
		private double longitude;
		@Getter
		private double altitude;
		@Getter
		private long timestamp;
		@Getter
		private String authTicket;
		@Getter
		private String sessionData;
		@Getter
		private String[] requests;

		private Request(double latitude, double longitude, double altitude, long timestamp, byte[] authTicket,
				byte[] sessionData, byte[][] requests) {
			this.latitude = latitude;
			this.longitude = longitude;
			this.altitude = altitude;
			this.timestamp = timestamp;
			this.authTicket = Base64.encodeBytes(authTicket);
			this.sessionData = Base64.encodeBytes(sessionData);
			this.requests = new String[requests.length];
			for (int i = 0; i < requests.length; i++) {
				this.requests[i] = Base64.encodeBytes(requests[i]);
			}
		}
	}
}
