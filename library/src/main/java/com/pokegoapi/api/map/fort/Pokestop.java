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
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Fort.FortModifierOuterClass;
import POGOProtos.Networking.Requests.Messages.AddFortModifierMessageOuterClass.AddFortModifierMessage;
import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass.FortDetailsMessage;
import POGOProtos.Networking.Requests.Messages.FortSearchMessageOuterClass.FortSearchMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.AddFortModifierResponseOuterClass.AddFortModifierResponse;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass.FortDetailsResponse;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass.FortSearchResponse;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.settings.Settings;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.Getter;
import repack.com.google.common.geometry.S2LatLng;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by mjmfighter on 7/20/2016.
 */
public class Pokestop {
	private final Networking networking;
	private final Location location;
	private final Settings settings;
	@Getter
	private final FortData fortData;
	@Getter
	private long cooldownCompleteTimestampMs;

	/**
	 * Instantiate new pokestop
	 *
	 * @param networking Networking needed for all operations on this pokestop
	 * @param location   Current location of the user
	 * @param settings   Settings from the remote server, needed for checking if stop is in reach
	 * @param fortData   Response from server
	 */
	public Pokestop(Networking networking, Location location, Settings settings, FortData fortData) {
		this.networking = networking;
		this.location = location;
		this.settings = settings;
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
		S2LatLng player = S2LatLng.fromDegrees(location.getLatitude(), location.getLongitude());
		return pokestop.getEarthDistance(player);
	}

	/**
	 * Returns whether or not a pokestop is in range.
	 *
	 * @return true when in range of player
	 */
	public boolean inRange() {
		return getDistance() <= settings.getFortSettings().getInteractionRangeInMeters();
	}

	/**
	 * Returns whether or not the lured pokemon is in range.
	 *
	 * @return true when the lured pokemon is in range of player
	 */
	public boolean inRangeForLuredPokemon() {
		return getDistance() <= settings.getMapSettings().getPokemonVisibilityRange();
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
		boolean active = cooldownCompleteTimestampMs < System.currentTimeMillis();
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
	 * @return PokestopLootResult
	 */
	public Observable<PokestopLootResult> loot() {
		return networking.queueRequest(RequestType.FORT_SEARCH, FortSearchMessage.newBuilder()
				.setFortId(getId())
				.setFortLatitude(getLatitude())
				.setFortLongitude(getLongitude())
				.setPlayerLatitude(location.getLatitude())
				.setPlayerLongitude(location.getLongitude())
				.build(), FortSearchResponse.class)
				.map(new Func1<FortSearchResponse, PokestopLootResult>() {
					@Override
					public PokestopLootResult call(FortSearchResponse response) {
						cooldownCompleteTimestampMs = response.getCooldownCompleteTimestampMs();
						return new PokestopLootResult(response);
					}
				});
	}

	/**
	 * Adds a modifier to this pokestop. (i.e. add a lure module)
	 *
	 * @param item the modifier to add to this pokestop
	 * @return true if success
	 */
	public Observable<Boolean> addModifier(ItemIdOuterClass.ItemId item) {
		return networking.queueRequest(RequestType.ADD_FORT_MODIFIER, AddFortModifierMessage.newBuilder()
				.setModifierType(item)
				.setFortId(getId())
				.setPlayerLatitude(location.getLatitude())
				.setPlayerLongitude(location.getLongitude())
				.build(), AddFortModifierResponse.class)
				.map(new Func1<AddFortModifierResponse, Boolean>() {
					@Override
					public Boolean call(AddFortModifierResponse addFortModifierResponse) {
						return addFortModifierResponse.getResult() == AddFortModifierResponse.Result.SUCCESS;
					}
				});
	}

	/**
	 * Get more detailed information about a pokestop.
	 *
	 * @return FortDetails
	 */
	public Observable<FortDetails> getDetails() {
		return networking.queueRequest(RequestType.FORT_DETAILS, FortDetailsMessage.newBuilder()
				.setFortId(getId())
				.setLatitude(getLatitude())
				.setLongitude(getLongitude())
				.build(), FortDetailsResponse.class)
				.map(new Func1<FortDetailsResponse, FortDetails>() {
					@Override
					public FortDetails call(FortDetailsResponse response) {
						return new FortDetails(response);
					}
				});
	}

	/**
	 * Returns whether this pokestop has an active lure.
	 *
	 * @return lure status
	 * @throws LoginFailedException  If login failed.
	 * @throws RemoteServerException If server communications failed.
	 */
	public Observable<Boolean> hasLure() throws LoginFailedException, RemoteServerException {
		return getDetails().map(new Func1<FortDetails, Boolean>() {
			@Override
			public Boolean call(FortDetails fortDetails) {
				for (FortModifierOuterClass.FortModifier mod : fortDetails.getModifier()) {
					if (mod.getItemId() == ItemIdOuterClass.ItemId.ITEM_TROY_DISK) {
						return true;
					}
				}
				return false;
			}
		});
	}
}
