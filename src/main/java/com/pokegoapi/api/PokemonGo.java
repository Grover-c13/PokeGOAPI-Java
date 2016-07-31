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

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
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
import okhttp3.OkHttpClient;

public class PokemonGo {
	private static final String TAG = PokemonGo.class.getSimpleName();
	private final Time time;
	private RequestHandler requestHandler;
	private Map map;
	private PlayerProfile playerProfile;
	private Inventories inventories;
	private double latitude;
	private double longitude;
	private double altitude;
	private CredentialProvider credentialProvider;
	private Settings settings;

	/**
	 * Instantiates a new Pokemon go.
	 *
	 * @param credentialProvider the credential provider
	 * @param client             the http client
	 * @param time               a time implementation
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException When server fails
	 */
	public PokemonGo(CredentialProvider credentialProvider, OkHttpClient client, Time time)
			throws LoginFailedException, RemoteServerException {

		if (credentialProvider == null) {
			throw new LoginFailedException("Credential Provider is null");
		} else {
			this.credentialProvider = credentialProvider;
		}

		this.time = time;

		requestHandler = new RequestHandler(this, client);
		playerProfile = new PlayerProfile(this);
		inventories = new Inventories(this);

		// FIXME: These aren't converted yet
		settings = new Settings(this);
		map = new Map(this);

		// TODO: Make a data pull method instead of instantly firing requests in the constructor?
		playerProfile.refreshDataSync();
		inventories.refreshDataSync(true);
		settings.refreshDataSync();
	}

	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param credentialProvider the credential provider
	 * @param client             the http client
	 * @throws LoginFailedException  When login fails
	 * @throws RemoteServerException When server fails
	 */
	public PokemonGo(CredentialProvider credentialProvider, OkHttpClient client)
			throws LoginFailedException, RemoteServerException {
		this(credentialProvider, client, new SystemTimeImpl());
	}

	/**
	 * Fetches valid AuthInfo
	 *
	 * @return AuthInfo object
	 * @throws LoginFailedException when login fails
	 */
	public RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo getAuthInfo()
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

	public Time getTime() {
		return time;
	}

	public RequestHandler getRequestHandler() {
		return requestHandler;
	}

	public void setRequestHandler(RequestHandler requestHandler) {
		this.requestHandler = requestHandler;
	}

	// TODO: These will all need to be async

	public Map getMap() {
		return map;
	}

	private void setMap(Map map) {
		this.map = map;
	}

	public PlayerProfile getPlayerProfile() {
		return playerProfile;
	}

	private void setPlayerProfile(PlayerProfile playerProfile) {
		this.playerProfile = playerProfile;
	}

	public Inventories getInventories() {
		return inventories;
	}

	private void setInventories(Inventories inventories) {
		this.inventories = inventories;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public CredentialProvider getCredentialProvider() {
		return credentialProvider;
	}

	public void setCredentialProvider(CredentialProvider credentialProvider) {
		this.credentialProvider = credentialProvider;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
