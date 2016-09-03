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

package com.pokegoapi.api.map.fort;

import POGOProtos.Inventory.Item.ItemIdOuterClass;
import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Map.Fort.FortModifierOuterClass;
import POGOProtos.Networking.Requests.Messages.AddFortModifierMessageOuterClass.AddFortModifierMessage;
import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass.FortDetailsMessage;
import POGOProtos.Networking.Requests.Messages.FortSearchMessageOuterClass.FortSearchMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.AddFortModifierResponseOuterClass;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.util.PokeAFunc;
import com.pokegoapi.util.PokeCallback;
import lombok.Getter;

import java.util.List;


/**
 * Created by mjmfighter on 7/20/2016.
 */
public class Pokestop {

	private final PokemonGo api;
	@Getter
	private final FortDataOuterClass.FortData fortData;
	@Getter
	private long cooldownCompleteTimestampMs;

	/**
	 * Instantiates a new Pokestop.
	 *
	 * @param api      the api
	 * @param fortData the fort data
	 */
	public Pokestop(PokemonGo api, FortDataOuterClass.FortData fortData) {
		this.api = api;
		this.fortData = fortData;
		this.cooldownCompleteTimestampMs = fortData.getCooldownCompleteTimestampMs();
	}

	/**
	 * Returns the distance to a pokestop.
	 *
	 * @return the calculated distance
	 */
	public double getDistance() {
		S2LatLng pokestop = S2LatLng.fromDegrees(getLatitude(), getLongitude());
		S2LatLng player = S2LatLng.fromDegrees(api.getLatitude(), api.getLongitude());
		return pokestop.getEarthDistance(player);
	}

	/**
	 * Returns whether or not a pokestop is in range.
	 *
	 * @return true when in range of player
	 */
	public boolean inRange() {
		return getDistance() <= api.getSettings().getFortSettings().getInteractionRangeInMeters();
	}

	/**
	 * Returns whether or not the lured pokemon is in range.
	 *
	 * @return true when the lured pokemon is in range of player
	 */
	public boolean inRangeForLuredPokemon() {
		return getDistance() <= api.getSettings().getMapSettings().getPokemonVisibilityRange();
	}

	/**
	 * can user loot this from current position.
	 *
	 * @return true when lootable
	 */
	public boolean canLoot() {
		return canLoot(false);
	}

	/**
	 * Can loot boolean.
	 *
	 * @param ignoreDistance the ignore distance
	 * @return the boolean
	 */
	public boolean canLoot(boolean ignoreDistance) {
		boolean active = cooldownCompleteTimestampMs < api.currentTimeMillis();
		if (!ignoreDistance) {
			return active && inRange();
		}
		return active;
	}

	public String getId() {
		return fortData.getId();
	}

	public double getLatitude() {
		return fortData.getLatitude();
	}

	public double getLongitude() {
		return fortData.getLongitude();
	}

	/**
	 * Loots a pokestop for pokeballs and other items.
	 *
	 * @param callback an optional callback to handle results
	 *
	 * @return callback passed as argument
	 */
	public PokeCallback<PokestopLootResult>  loot(PokeCallback<PokestopLootResult> callback) {
		FortSearchMessage searchMessage = FortSearchMessage.newBuilder()
				.setFortId(getId())
				.setFortLatitude(getLatitude())
				.setFortLongitude(getLongitude())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.build();

		new AsyncServerRequest(RequestTypeOuterClass.RequestType.FORT_SEARCH, searchMessage,
				new PokeAFunc<FortSearchResponseOuterClass.FortSearchResponse, PokestopLootResult>() {
					@Override
					public PokestopLootResult exec(FortSearchResponseOuterClass.FortSearchResponse response) {
						cooldownCompleteTimestampMs = response.getCooldownCompleteTimestampMs();
						return new PokestopLootResult(response);
					}
				}, callback, api);
		return callback;
	}

	/**
	 * Adds a modifier to this pokestop. (i.e. add a lure module)
	 *
	 * @param item the modifier to add to this pokestop
	 * @param callback an optional callback to handle results
	 *
	 * @return callback passed as argument
	 */
	public PokeCallback<Boolean> addModifier(ItemIdOuterClass.ItemId item, PokeCallback<Boolean> callback) {
		AddFortModifierMessage msg = AddFortModifierMessage.newBuilder()
				.setModifierType(item)
				.setFortId(getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.build();

		new AsyncServerRequest(RequestTypeOuterClass.RequestType.ADD_FORT_MODIFIER, msg,
				new PokeAFunc<AddFortModifierResponseOuterClass.AddFortModifierResponse, Boolean>() {
					@Override
					public Boolean exec(AddFortModifierResponseOuterClass.AddFortModifierResponse response) {
						return Boolean.TRUE;
					}
				}, callback, api);
		return callback;
	}

	/**
	 * Get more detailed information about a pokestop.
	 *
	 * @param callback an optional callback to handle results
	 *
	 * @return callback passed as argument
	 */
	public PokeCallback<FortDetails> getDetails(PokeCallback<FortDetails> callback) {
		FortDetailsMessage reqMsg = FortDetailsMessage.newBuilder()
				.setFortId(getId())
				.setLatitude(getLatitude())
				.setLongitude(getLongitude())
				.build();

		new AsyncServerRequest(RequestTypeOuterClass.RequestType.FORT_DETAILS, reqMsg,
				new PokeAFunc<FortDetailsResponseOuterClass.FortDetailsResponse, FortDetails>() {
					@Override
					public FortDetails exec(FortDetailsResponseOuterClass.FortDetailsResponse response) {
						return new FortDetails(response);
					}
				}, callback, api);
		return callback;
	}

	/**
	 * Returns whether this pokestop has an active lure.
	 *
	 * @return lure status
	 */
	public boolean hasLurePokemon() {
		return fortData.hasLureInfo() && fortData.getLureInfo().getLureExpiresTimestampMs() > api.currentTimeMillis();
	}

	/**
	 * Returns whether this pokestop has an active lure.
	 *
	 * @return lure status
	 */
	public boolean hasLure() {
		return fortData.getActiveFortModifierList().contains(ItemIdOuterClass.ItemId.ITEM_TROY_DISK);
	}
}
