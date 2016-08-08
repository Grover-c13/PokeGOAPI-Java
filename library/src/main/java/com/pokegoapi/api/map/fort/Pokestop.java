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

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.AsyncRemoteServerException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.util.AsyncHelper;

import java.util.List;

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
import lombok.Getter;
import rx.Observable;
import rx.functions.Func1;

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
	 * @return PokestopLootResult
	 */
	public Observable<PokestopLootResult> lootAsync() {
		FortSearchMessage searchMessage = FortSearchMessage.newBuilder()
				.setFortId(getId())
				.setFortLatitude(getLatitude())
				.setFortLongitude(getLongitude())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestTypeOuterClass.RequestType.FORT_SEARCH,
				searchMessage);
		return api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(
				new Func1<ByteString, PokestopLootResult>() {
					@Override
					public PokestopLootResult call(ByteString result) {
						FortSearchResponseOuterClass.FortSearchResponse response;
						try {
							response = FortSearchResponseOuterClass.FortSearchResponse.parseFrom(result);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}
						cooldownCompleteTimestampMs = response.getCooldownCompleteTimestampMs();
						return new PokestopLootResult(response);
					}
				});
	}

	/**
	 * Loots a pokestop for pokeballs and other items.
	 *
	 * @return PokestopLootResult
	 * @throws LoginFailedException  if login failed
	 * @throws RemoteServerException if the server failed to respond
	 */
	public PokestopLootResult loot() throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(lootAsync());
	}

	/**
	 * Adds a modifier to this pokestop. (i.e. add a lure module)
	 *
	 * @param item the modifier to add to this pokestop
	 */
	public Observable<Boolean> addModifierAsync(ItemIdOuterClass.ItemId item) {
		AddFortModifierMessage msg = AddFortModifierMessage.newBuilder()
				.setModifierType(item)
				.setFortId(getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.build();
		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestTypeOuterClass.RequestType.ADD_FORT_MODIFIER, msg);
		return api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, Boolean>() {
			@Override
			public Boolean call(ByteString result) {
				try {
					//sadly the server response does not contain any information to verify if the request was successful
					AddFortModifierResponseOuterClass.AddFortModifierResponse.parseFrom(result);
				} catch (InvalidProtocolBufferException e) {
					throw new AsyncRemoteServerException(e);
				}
				return Boolean.TRUE;
			}
		});
	}

	/**
	 * Adds a modifier to this pokestop. (i.e. add a lure module)
	 *
	 * @param item the modifier to add to this pokestop
	 * @throws LoginFailedException  if login failed
	 * @throws RemoteServerException if the server failed to respond or the modifier could not be added to this pokestop
	 */
	public void addModifier(ItemIdOuterClass.ItemId item) throws LoginFailedException, RemoteServerException {
		AsyncHelper.toBlocking(addModifierAsync(item));
	}

	/**
	 * Get more detailed information about a pokestop.
	 *
	 * @return FortDetails
	 */
	public Observable<FortDetails> getDetailsAsync() {
		FortDetailsMessage reqMsg = FortDetailsMessage.newBuilder()
				.setFortId(getId())
				.setLatitude(getLatitude())
				.setLongitude(getLongitude())
				.build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestTypeOuterClass.RequestType.FORT_DETAILS, reqMsg);
		return api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, FortDetails>() {
			@Override
			public FortDetails call(ByteString result) {
				FortDetailsResponseOuterClass.FortDetailsResponse response = null;
				try {
					response = FortDetailsResponseOuterClass.FortDetailsResponse.parseFrom(result);
				} catch (InvalidProtocolBufferException e) {
					throw new AsyncRemoteServerException(e);
				}
				return new FortDetails(response);
			}
		});
	}


	/**
	 * Get more detailed information about a pokestop.
	 *
	 * @return FortDetails
	 * @throws LoginFailedException  if login failed
	 * @throws RemoteServerException if the server failed to respond
	 */
	public FortDetails getDetails() throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(getDetailsAsync());
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
	 * Returns whether this pokestop has an active lure.
	 *
	 * @return lure status
	 * @throws LoginFailedException  If login failed.
	 * @throws RemoteServerException If server communications failed.
	 */
	public boolean hasLure() throws LoginFailedException, RemoteServerException {
		List<FortModifierOuterClass.FortModifier> modifiers = getDetails().getModifier();
		for (FortModifierOuterClass.FortModifier mod : modifiers) {
			if (mod.getItemId() == ItemIdOuterClass.ItemId.ITEM_TROY_DISK) {
				return true;
			}
		}

		return false;
	}
}
