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
import POGOProtos.Networking.Responses.AddFortModifierResponseOuterClass.AddFortModifierResponse;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass.FortDetailsResponse;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass.FortSearchResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.listener.PokestopListener;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonRequest;
import com.pokegoapi.main.PokemonResponse;
import com.pokegoapi.main.RequestCallback;
import com.pokegoapi.main.Utils;
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
	 * @param api the api
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
	 * @param result callback to return the loot result
	 */
	public void loot(final AsyncReturn<PokestopLootResult> result) {
		FortSearchMessage message = FortSearchMessage.newBuilder()
				.setFortId(getId())
				.setFortLatitude(getLatitude())
				.setFortLongitude(getLongitude())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.build();
		PokemonRequest request = new PokemonRequest(RequestTypeOuterClass.RequestType.FORT_SEARCH, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, null)) {
					return;
				}
				try {
					FortSearchResponse messageResponse = FortSearchResponse.parseFrom(response.getResponseData());
					cooldownCompleteTimestampMs = messageResponse.getCooldownCompleteTimestampMs();
					PokestopLootResult lootResult = new PokestopLootResult(messageResponse);
					List<PokestopListener> listeners = api.getListeners(PokestopListener.class);
					for (PokestopListener listener : listeners) {
						listener.onLoot(lootResult);
					}
					result.onReceive(lootResult, null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Adds a modifier to this pokestop. (i.e. add a lure module)
	 *
	 * @param item the modifier to add to this pokestop
	 * @param result callback to return the result of this action
	 */
	public void addModifier(ItemIdOuterClass.ItemId item, final AsyncReturn<AddFortModifierResponse.Result> result) {
		final AddFortModifierMessage message = AddFortModifierMessage.newBuilder()
				.setModifierType(item)
				.setFortId(getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.build();
		PokemonRequest request = new PokemonRequest(RequestTypeOuterClass.RequestType.ADD_FORT_MODIFIER, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				AddFortModifierResponse.Result error = AddFortModifierResponse.Result.NO_RESULT_SET;
				if (Utils.callbackException(response.getException(), result, error)) {
					return;
				}
				try {
					AddFortModifierResponse messageResponse =
							AddFortModifierResponse.parseFrom(response.getResponseData());
					result.onReceive(messageResponse.getResult(), null);
				} catch (Exception e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Get more detailed information about a pokestop.
	 *
	 * @param details callback to return details for this pokestop
	 */
	public void getDetails(final AsyncReturn<FortDetails> details) {
		FortDetailsMessage message = FortDetailsMessage.newBuilder()
				.setFortId(getId())
				.setLatitude(getLatitude())
				.setLongitude(getLongitude())
				.build();

		PokemonRequest request = new PokemonRequest(RequestTypeOuterClass.RequestType.FORT_DETAILS, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, details, null)) {
					return;
				}
				try {
					FortDetailsResponse messageResponse = FortDetailsResponse.parseFrom(response.getResponseData());
					details.onReceive(new FortDetails(messageResponse), null);
				} catch (Exception e) {
					details.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Returns whether this pokestop has an active lure.
	 *
	 * @return lure status
	 */
	@Deprecated
	public boolean hasLurePokemon() {
		return fortData.hasLureInfo() && fortData.getLureInfo().getLureExpiresTimestampMs() > api.currentTimeMillis();
	}

	/**
	 * Checks whether this pokestop has an active lure when detected on map.
	 *
	 * @param result callback for lure status
	 */
	public void checkLure(AsyncReturn<Boolean> result) {
		checkLure(false, result);
	}

	/**
	 * Returns whether this pokestop has an active lure.
	 *
	 * @param updateFortDetails to make a new request and get updated lured status
	 * @param result callback for lure status
	 */
	public void checkLure(boolean updateFortDetails, final AsyncReturn<Boolean> result) {
		final boolean cached = fortData.getActiveFortModifierList().contains(ItemIdOuterClass.ItemId.ITEM_TROY_DISK);
		if (updateFortDetails) {
			getDetails(new AsyncReturn<FortDetails>() {
				@Override
				public void onReceive(FortDetails details, Exception exception) {
					if (Utils.callbackException(exception, result, cached)) {
						return;
					}
					List<FortModifierOuterClass.FortModifier> modifiers = details.getModifier();
					for (FortModifierOuterClass.FortModifier modifier : modifiers) {
						if (modifier.getItemId() == ItemIdOuterClass.ItemId.ITEM_TROY_DISK) {
							result.onReceive(true, null);
							return;
						}
					}
					result.onReceive(false, null);
				}
			});
		} else {
			result.onReceive(cached, null);
		}
	}
}
