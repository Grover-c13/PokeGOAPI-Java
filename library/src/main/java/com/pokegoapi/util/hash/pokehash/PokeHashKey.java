package com.pokegoapi.util.hash.pokehash;

import lombok.Getter;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class PokeHashKey {
	private static final Map<String, PokeHashKey> KEYS = new HashMap<>();

	@Getter
	private final String key;

	private int rpm;
	@Getter
	private int maxRequests = 150;
	@Getter
	private int requestsRemaining = this.maxRequests;
	@Getter
	private long keyExpiration;
	@Getter
	private long ratePeriodEnd;

	private boolean tested;

	private PokeHashKey(String key) {
		this.key = key;
	}

	/**
	 * Creates a new, or returns an existing key for the given key string
	 *
	 * @param key the key string to use
	 * @return a PokeHashKey for the given input
	 */
	public static synchronized PokeHashKey from(String key) {
		if (key == null || key.length() == 0) {
			throw new IllegalArgumentException("Key cannot be null or empty!");
		}
		PokeHashKey from = KEYS.get(key);
		if (from == null) {
			from = new PokeHashKey(key);
			KEYS.put(key, from);
		}
		return from;
	}

	/**
	 * Sets the properties on this key from the headers received in the given connection
	 *
	 * @param connection the connection to check headers on
	 */
	synchronized void setProperties(HttpURLConnection connection) {
		this.checkPeriod();

		this.ratePeriodEnd = connection.getHeaderFieldLong("X-RatePeriodEnd", this.ratePeriodEnd);
		this.maxRequests = connection.getHeaderFieldInt("X-MaxRequestCount", this.maxRequests);
		this.requestsRemaining = connection.getHeaderFieldInt("X-RateRequestsRemaining", this.requestsRemaining);
		this.keyExpiration = connection.getHeaderFieldLong("X-AuthTokenExpiration", this.keyExpiration);
		this.tested = true;
	}

	/**
	 * Waits until the current rate period ends
	 *
	 * @throws InterruptedException if the thread is interrupted while awaiting the current period to end
	 */
	void await() throws InterruptedException {
		if (this.requestsRemaining <= 0) {
			long timeToPeriodEnd = System.currentTimeMillis() - this.getRatePeriodEnd();
			if (this.tested && timeToPeriodEnd > 0) {
				Thread.sleep(timeToPeriodEnd);
				this.checkPeriod();
			}
		}
	}

	/**
	 * Checks if this period is over yet, and if it is, set RPM
	 */
	private synchronized void checkPeriod() {
		if (System.currentTimeMillis() > this.ratePeriodEnd) {
			this.rpm = this.maxRequests - this.requestsRemaining;
			this.requestsRemaining = this.maxRequests;
		}
	}

	/**
	 * Returns the last RPM measurement
	 *
	 * @return the last RPM measurement
	 */
	public int getRPM() {
		return this.rpm;
	}

	/**
	 * Returns if this key has been used to send a request yet
	 *
	 * @return if this key has been used to send a request yet
	 */
	public boolean hasTested() {
		return this.tested;
	}
}
