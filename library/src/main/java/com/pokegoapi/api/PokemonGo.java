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

import com.pokegoapi.api.device.DeviceInfo;
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


public class PokemonGo {

	private static final java.lang.String TAG = PokemonGo.class.getSimpleName();
	private static final int sleepTime = 800;
	private final Time time;
	public final long startTime;
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
	@Setter
	private SensorInfo sensorInfo;

	/**
	 * Instantiates a new Pokemon go.
	 *
	 * @param credentialProvider the credential provider
	 * @param client             the http client
	 * @param time               a time implementation
	 * @param needSleep			 need sleep after creating settings
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException When server fails
	 * @throws InterruptedException When Thread.sleep fails
	 */

	public PokemonGo(CredentialProvider credentialProvider, OkHttpClient client, Time time, boolean needSleep)
			throws LoginFailedException, RemoteServerException, InterruptedException {

		if (credentialProvider == null) {
			throw new LoginFailedException("Credential Provider is null");
		} else {
			this.credentialProvider = credentialProvider;
		}
		this.time = time;

		sessionHash = new byte[32];
		new Random().nextBytes(sessionHash);

		requestHandler = new RequestHandler(this, client);
		playerProfile = new PlayerProfile(this);
		//Need to sleep after PlayerProfile's updateProfile()
		if (needSleep) Thread.sleep(sleepTime);
		
		settings = new Settings(this);
		//Need to sleep after Settings' updateSettings()
		if (needSleep) Thread.sleep(sleepTime);
		
		map = new Map(this);
		longitude = Double.NaN;
		latitude = Double.NaN;
		startTime = currentTimeMillis();
	}

	/**
	 * Instantiates a new Pokemon go asking which will sleep after settings.
	 *
	 * @param credentialProvider the credential provider
	 * @param client             the http client
	 * @param needSleep			 need sleep after creating settings
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException When server fails
	 * @throws InterruptedException When Thread.sleep fails
	 */
	public PokemonGo(CredentialProvider credentialProvider, OkHttpClient client, boolean needSleep)
			throws LoginFailedException, RemoteServerException, InterruptedException {
		this(credentialProvider, client, new SystemTimeImpl(), needSleep);
	}


	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param credentialProvider the credential provider
	 * @param client             the http client
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException When server fails
	 * @throws InterruptedException When Thread.sleep fails
	 */
	public PokemonGo(CredentialProvider credentialProvider, OkHttpClient client)
			throws LoginFailedException, RemoteServerException, InterruptedException {
		this(credentialProvider, client, new SystemTimeImpl(), false);
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
	 * @throws LoginFailedException  when login fails
	 * @throws RemoteServerException when server down/issue
	 */
	public Inventories getInventories() throws LoginFailedException, RemoteServerException {
		if (inventories == null) {
			inventories = new Inventories(this);
		}
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
			return null;
		}
		return deviceInfo.getDeviceInfo();
	}

	/**
	 * Gets the sensor info
	 *
	 * @return the sensor info
	 */
	public SignatureOuterClass.Signature.SensorInfo getSensorInfo() {
		if (sensorInfo == null) {
			return null;
		}
		return sensorInfo.getSensorInfo();
	}
}
