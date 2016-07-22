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

import POGOProtos.Data.Player.CurrencyOuterClass;
import POGOProtos.Data.Player.PlayerStatsOuterClass;
import POGOProtos.Data.PlayerDataOuterClass;
import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Inventory.ItemIdOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.inventory.*;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.player.ContactSettings;
import com.pokegoapi.api.player.DailyBonus;
import com.pokegoapi.api.player.PlayerAvatar;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.player.Team;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.RequestHandler;
import com.pokegoapi.main.ServerRequest;
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
	public PokemonGo(RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth, OkHttpClient client) throws LoginFailedException, RemoteServerException {
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
			} catch (Exception e){
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
