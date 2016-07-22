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
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.RequestHandler;
import com.pokegoapi.util.Log;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;


public class PokemonGo {

	private static final java.lang.String TAG = PokemonGo.class.getSimpleName();
	@Getter
	RequestHandler requestHandler;
	@Getter
	Map map;
	@Getter
	private PlayerProfile playerProfile;
	@Getter
	private Inventories inventories;
	@Getter
	@Setter
	private double latitude;
	@Getter
	@Setter
	private double longitude;
	@Getter
	@Setter
	private double altitude;


	/**
	 * Instantiates a new Pokemon go.
	 *
	 * @param auth   the auth
	 * @param client the client
	 */
	public PokemonGo(RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth, OkHttpClient client)
			throws LoginFailedException, RemoteServerException {
		playerProfile = null;

		// send profile request to get the ball rolling
		requestHandler = new RequestHandler(this, auth, client);

		playerProfile = new PlayerProfile(this);
		inventories = new Inventories(this);

		playerProfile.updateProfile();
		inventories.updateInventories();

		// should have proper end point now.

		map = new Map(this);
	}

	/**
	 * Gets player profile.
	 *
	 * @param forceUpdate the force update
	 * @return the player profile
	 */
	@Deprecated
	public PlayerProfile getPlayerProfile(boolean forceUpdate) {
		if (!forceUpdate && playerProfile != null) {
			try {
				playerProfile.updateProfile();
			} catch (Exception e) {
				Log.e(TAG, "Error updating Player Profile", e);
			}
		}
		return playerProfile;
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
}
