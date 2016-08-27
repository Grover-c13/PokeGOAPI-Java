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

package com.pokegoapi.api;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;

import com.pokegoapi.api.device.ActivityStatus;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.settings.Settings;
import com.pokegoapi.auth.CredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.RequestHandler;
import com.pokegoapi.util.SystemTimeImpl;
import com.pokegoapi.util.Time;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

import java.util.Random;
import java.util.UUID;


public class PokemonGo {

	private static final java.lang.String TAG = PokemonGo.class.getSimpleName();
	private final Time time;
	@Getter
	private long startTime;
	@Getter
	private final byte[] sessionHash;
	@Getter
	RequestHandler requestHandler;
	@Getter
	private PlayerProfile playerProfile;
	private Inventories inventories;
	@Getter
	private double latitude;
	@Getter
	private double longitude;
	@Getter
	@Setter
	private double altitude;
	private CredentialProvider credentialProvider;
	@Getter
	private Settings settings;
	private Map map;
	@Setter
	private DeviceInfo deviceInfo;
	@Getter
	@Setter
	public SensorInfo sensorInfo;
	@Getter
	@Setter
	public ActivityStatus activityStatus;
	@Setter
	@Getter
	private long seed;
	@Getter
	@Setter
	public LocationFixes locationFixes;

	/**
	 * Instantiates a new Pokemon go.
	 *
	 * @param client the http client
	 * @param time   a time implementation
	 * @param seed   the seed to generate same device
	 */
	public PokemonGo(OkHttpClient client, Time time, long seed) {
		this.time = time;
		this.seed = seed;
		sessionHash = new byte[32];
		new Random().nextBytes(sessionHash);
		requestHandler = new RequestHandler(this, client);
		map = new Map(this);
		longitude = Double.NaN;
		latitude = Double.NaN;
	}

	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param client the http client
	 * @param seed   the seed to generate same device
	 */
	public PokemonGo(OkHttpClient client, long seed) {
		this(client, new SystemTimeImpl(), seed);
	}

	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param client the http client
	 * @param time   a time implementation
	 */
	public PokemonGo(OkHttpClient client, Time time) {
		this(client, time, hash(UUID.randomUUID().toString()));
	}

	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param client the http client
	 */
	public PokemonGo(OkHttpClient client) {
		this(client, new SystemTimeImpl(), hash(UUID.randomUUID().toString()));
	}

	/**
	 * Login user with the provided provider
	 *
	 * @param credentialProvider the credential provider
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException When server fails
	 */
	public void login(CredentialProvider credentialProvider) throws LoginFailedException, RemoteServerException {
		if (credentialProvider == null) {
			throw new NullPointerException("Credential Provider is null");
		}
		this.credentialProvider = credentialProvider;
		startTime = currentTimeMillis();
		playerProfile = new PlayerProfile(this);
		settings = new Settings(this);
		inventories = new Inventories(this);
	}

	/**
	 * Hash the given string
	 *
	 * @param string string to hash
	 * @return the hashed long
	 */
	private static long hash(String string) {
		long upper = ((long) string.hashCode()) << 32;
		int len = string.length();
		StringBuilder dest = new StringBuilder(len);

		for (int index = (len - 1); index >= 0; index--) {
			dest.append(string.charAt(index));
		}
		long lower = ((long) dest.toString().hashCode()) - ((long) Integer.MIN_VALUE);
		return upper + lower;
	}

	/**
	 * Fetches valid AuthInfo
	 *
	 * @return AuthInfo object
	 * @throws LoginFailedException  when login fails
	 * @throws RemoteServerException When server fails
	 */
	public AuthInfo getAuthInfo()
			throws LoginFailedException, RemoteServerException {
		return credentialProvider.getAuthInfo();
	}

	/**
	 * Sets location.
	 *
	 * @param latitude  the latitude
	 * @param longitude the longitude
	 * @param altitude  the altitude
	 */
	public void setLocation(double latitude, double longitude, double altitude) {
		if (latitude != this.latitude
				|| longitude != this.longitude
				|| altitude != this.altitude) {
			getMap().clearCache();
		}
		setLatitude(latitude);
		setLongitude(longitude);
		setAltitude(altitude);
	}

	public long currentTimeMillis() {
		return time.currentTimeMillis();
	}

	/**
	 * Get the inventories API
	 *
	 * @return Inventories
	 */
	public Inventories getInventories() {
		return inventories;
	}

	/**
	 * Validates and sets a given latitude value
	 *
	 * @param value the latitude
	 * @throws IllegalArgumentException if value exceeds +-90
	 */
	public void setLatitude(double value) {
		if (value > 90 || value < -90) {
			throw new IllegalArgumentException("latittude can not exceed +/- 90");
		}
		latitude = value;
	}

	/**
	 * Validates and sets a given longitude value
	 *
	 * @param value the longitude
	 * @throws IllegalArgumentException if value exceeds +-180
	 */
	public void setLongitude(double value) {
		if (value > 180 || value < -180) {
			throw new IllegalArgumentException("longitude can not exceed +/- 180");
		}
		longitude = value;
	}

	/**
	 * Gets the map API
	 *
	 * @return the map
	 * @throws IllegalStateException if location has not been set
	 */
	public Map getMap() {
		if (this.latitude == Double.NaN || this.longitude == Double.NaN) {
			throw new IllegalStateException("Attempt to get map without setting location first");
		}
		return map;
	}

	/**
	 * Gets the device info
	 *
	 * @return the device info
	 */
	public SignatureOuterClass.Signature.DeviceInfo getDeviceInfo() {
		if (deviceInfo == null) {
			deviceInfo = DeviceInfo.getDefault(this);
		}
		return deviceInfo.getDeviceInfo();
	}
}
