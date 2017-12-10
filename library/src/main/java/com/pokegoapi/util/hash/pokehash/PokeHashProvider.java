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

import com.pokegoapi.exceptions.request.HashException;
import com.pokegoapi.exceptions.request.HashLimitExceededException;
import com.pokegoapi.exceptions.request.HashUnauthorizedException;
import com.pokegoapi.util.hash.Hash;
import com.pokegoapi.util.hash.HashProvider;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Moshi.Builder;
import lombok.Getter;
import lombok.Setter;
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
 * @see <a href="https://hashing.pogodev.org/">https://hashing.pogodev.org/</a>
 */
public class PokeHashProvider implements HashProvider {
	private static final String DEFAULT_ENDPOINT = "https://pokehash.buddyauth.com/api/v147_1/hash";

	@Getter
	@Setter
	private String endpoint = DEFAULT_ENDPOINT;

	private static final int VERSION = 8500;
	private static final long UNK25 = 3081064678568720862L;

	private static final Moshi MOSHI = new Builder().build();

	@Getter
	private final PokeHashKey key;
	@Getter
	private final boolean awaitRequests;

	/**
	 * Creates a PokeHashProvider with the given key
	 *
	 * @param key the key for the PokeHash API
	 * @param awaitRequest true if the API should, when the rate limit has been exceeded, wait until the current
	 *     period ends, or false to throw a HashLimitExceededException
	 */
	public PokeHashProvider(PokeHashKey key, boolean awaitRequest) {
		this.key = key;
		this.awaitRequests = awaitRequest;
		if (key == null || key.getKey() == null) {
			throw new IllegalArgumentException("Key cannot be null!");
		}
	}

	/**
	 * Provides a hash for the given arguments
	 *
	 * @param timestamp timestamp to hash
	 * @param latitude latitude to hash
	 * @param longitude longitude to hash
	 * @param altitude altitude to hash
	 * @param authTicket auth ticket to hash
	 * @param sessionData session data to hash
	 * @param requests request data to hash
	 * @return the hash provider
	 * @throws HashException if an exception occurs while providing this hash
	 */
	@Override
	public Hash provide(long timestamp, double latitude, double longitude, double altitude, byte[] authTicket,
			byte[] sessionData, byte[][] requests) throws HashException {
		if (key.hasTested()) {
			if (awaitRequests) {
				try {
					key.await();
				} catch (InterruptedException e) {
					throw new HashException(e);
				}
			} else {
				long time = System.currentTimeMillis();
				long timeLeft = time - key.getRatePeriodEnd();
				if (key.getRequestsRemaining() <= 0 && timeLeft > 0) {
					throw new HashLimitExceededException(
							"Exceeded hash request limit! Period ends in " + timeLeft + "ms");
				}
			}
		}

		Request request = new Request(latitude, longitude, altitude, timestamp, authTicket, sessionData, requests);
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("X-AuthToken", key.getKey());
			connection.setRequestProperty("content-type", "application/json");
			connection.setRequestProperty("User-Agent", "PokeGOAPI-Java");
			connection.setDoOutput(true);

			String requestJSON = MOSHI.adapter(Request.class).toJson(request);
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(requestJSON);
			out.flush();
			out.close();

			int responseCode = connection.getResponseCode();

			this.key.setProperties(connection);

			String error = getError(connection);

			switch (responseCode) {
				case HttpURLConnection.HTTP_OK:
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
						throw new HashUnauthorizedException(error);
					}
					throw new HashUnauthorizedException("Unauthorized hash request!");
				case 429:
					if (awaitRequests) {
						try {
							key.await();
							return provide(timestamp, latitude, longitude, altitude, authTicket, sessionData, requests);
						} catch (InterruptedException e) {
							throw new HashException("Interrupted while awaining request", e);
						}
					} else {
						if (error.length() > 0) {
							throw new HashLimitExceededException(error);
						}
						throw new HashLimitExceededException("Exceeded hash limit!");
					}
				case HttpURLConnection.HTTP_NOT_FOUND:
					throw new HashException("Unknown hashing endpoint! \"" + this.endpoint + "\"");
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
		private long latitude64;
		@Getter
		private long longitude64;
		@Getter
		private long accuracy64;
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
			this.latitude64 = Double.doubleToLongBits(latitude);
			this.longitude64 = Double.doubleToLongBits(longitude);
			this.accuracy64 = Double.doubleToLongBits(altitude);
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
