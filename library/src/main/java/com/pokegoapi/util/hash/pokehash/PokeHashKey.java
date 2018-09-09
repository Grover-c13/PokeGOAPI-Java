package com.pokegoapi.util.hash.pokehash;

import lombok.Getter;

import java.net.HttpURLConnection;
import java.util.Map;
import java.util.WeakHashMap;

public class PokeHashKey {
	private static final Map<String, PokeHashKey> KEYS = new WeakHashMap<>();

	@Getter
	public final String key;

	private int rpm;
	@Getter
	private int maxRequests = 150;
	@Getter
	public int requestsRemaining = this.maxRequests;
	@Getter
	private long keyExpiration;
	@Getter
	public long ratePeriodEnd;

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

		this.ratePeriodEnd = this.getHeaderLong(connection, "X-RatePeriodEnd", this.ratePeriodEnd);
		this.maxRequests = this.getHeaderInteger(connection, "X-MaxRequestCount", this.maxRequests);
		this.requestsRemaining = this.getHeaderInteger(connection, "X-RateRequestsRemaining", this.requestsRemaining);
		this.keyExpiration = this.getHeaderLong(connection, "X-AuthTokenExpiration", this.keyExpiration);
		this.tested = true;
	}

	/**
	 * Parses a long header
	 * @param connection the connection to load the header from
	 * @param name the header name
	 * @param defaultValue the default value to use, if parsing fails
	 * @return the parsed long
	 */
	private long getHeaderLong(HttpURLConnection connection, String name, long defaultValue) {
		try {
			return Long.parseLong(connection.getHeaderField(name));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Parses an integer header
	 * @param connection the connection to load the header from
	 * @param name the header name
	 * @param defaultValue the default value to use, if parsing fails
	 * @return the parsed integer
	 */
	private int getHeaderInteger(HttpURLConnection connection, String name, int defaultValue) {
		try {
			return Integer.parseInt(connection.getHeaderField(name));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Waits until the current rate period ends
	 *
	 * @throws InterruptedException if the thread is interrupted while awaiting the current period to end
	 */
	void await() throws InterruptedException {
		if (this.requestsRemaining <= 0) {
			long timeToPeriodEnd = System.currentTimeMillis() - this.ratePeriodEnd;
			if (this.tested && timeToPeriodEnd > 0) {
				Thread.sleep(Math.min(timeToPeriodEnd, 3600000));
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
