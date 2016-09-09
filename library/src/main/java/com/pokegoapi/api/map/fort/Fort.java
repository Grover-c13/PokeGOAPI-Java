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

import POGOProtos.Enums.TutorialStateOuterClass.TutorialState;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Networking.Requests.Messages.AddFortModifierMessageOuterClass.AddFortModifierMessage;
import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass.FortDetailsMessage;
import POGOProtos.Networking.Requests.Messages.FortSearchMessageOuterClass.FortSearchMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.AddFortModifierResponseOuterClass.AddFortModifierResponse;
import POGOProtos.Networking.Responses.AddFortModifierResponseOuterClass.AddFortModifierResponse.Result;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.listener.PokestopListener;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.AsyncHelper;
import lombok.Getter;
import lombok.Setter;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

import java.util.List;

/**
 * Created by mjmfighter on 7/20/2016.
 */
public class Fort {
	@Getter
	public final PokemonGo api;
	@Getter
	@Setter
	public FortDataOuterClass.FortData fortData;
	@Getter
	private long cooldownCompleteTimestampMs;

	/**
	 * Instantiates a new Pokestop.
	 *
	 * @param api the api
	 * @param fortData the fort data
	 */
	public Fort(PokemonGo api, FortDataOuterClass.FortData fortData) {
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
		S2LatLng player = S2LatLng.fromDegrees(api.latitude, api.longitude);
		return pokestop.getEarthDistance(player);
	}

	/**
	 * Returns whether or not a pokestop is in range.
	 *
	 * @return true when in range of player
	 */
	public boolean inRange() {
		return getDistance() <= api.settings.fortSettings.interactionRangeInMeters;
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
				.setPlayerLatitude(api.latitude)
				.setPlayerLongitude(api.longitude)
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.FORT_SEARCH,
				searchMessage);
		return api.requestHandler.sendAsyncServerRequests(serverRequest, true).map(
				new Func1<ByteString, PokestopLootResult>() {
					@Override
					public PokestopLootResult call(ByteString result) {
						FortSearchResponseOuterClass.FortSearchResponse response;
						try {
							response = FortSearchResponseOuterClass.FortSearchResponse.parseFrom(result);
						} catch (InvalidProtocolBufferException e) {
							throw Exceptions.propagate(e);
						}
						cooldownCompleteTimestampMs = response.getCooldownCompleteTimestampMs();
						PokestopLootResult lootResult = new PokestopLootResult(response);
						List<PokestopListener> listeners = api.getListeners(PokestopListener.class);
						for (PokestopListener listener : listeners) {
							// listener.onLoot(lootResult);
							// return the pokestop, also change in listener
							listener.onLoot(lootResult, Fort.this);
						}
						return lootResult;
					}
				});
	}

	/**
	 * Loots a pokestop for pokeballs and other items.
	 *
	 * @return PokestopLootResult
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public PokestopLootResult loot() throws RequestFailedException {
		return AsyncHelper.toBlocking(lootAsync());
	}

	/**
	 * Adds a modifier to this pokestop. (i.e. add a lure module)
	 *
	 * @param item the modifier to add to this pokestop
	 * @return true if success
	 */
	public Observable<Boolean> addModifierAsync(ItemId item) {
		AddFortModifierMessage msg = AddFortModifierMessage.newBuilder()
				.setModifierType(item)
				.setFortId(getId())
				.setPlayerLatitude(api.latitude)
				.setPlayerLongitude(api.longitude)
				.build();
		ServerRequest serverRequest = new ServerRequest(RequestType.ADD_FORT_MODIFIER, msg);
		return api.requestHandler.sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, Boolean>() {
			@Override
			public Boolean call(ByteString result) {
				try {
					AddFortModifierResponse response = AddFortModifierResponse.parseFrom(result);
					return response.getResult() == Result.SUCCESS;
				} catch (InvalidProtocolBufferException e) {
					throw Exceptions.propagate(e);
				}
			}
		});
	}

	/**
	 * Adds a modifier to this pokestop. (i.e. add a lure module)
	 *
	 * @param item the modifier to add to this pokestop
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void addModifier(ItemId item) throws RequestFailedException {
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

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.FORT_DETAILS,
				reqMsg);
		return api.requestHandler.sendAsyncServerRequests(serverRequest, true).map(
				new Func1<ByteString, FortDetails>() {
					@Override
					public FortDetails call(ByteString result) {
						FortDetailsResponseOuterClass.FortDetailsResponse response = null;
						try {
							response = FortDetailsResponseOuterClass.FortDetailsResponse.parseFrom(result);
						} catch (InvalidProtocolBufferException e) {
							throw Exceptions.propagate(e);
						}
						return new FortDetails(response);
					}
				});
	}


	/**
	 * Get more detailed information about a pokestop.
	 *
	 * @return FortDetails for this Pokestop
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public FortDetails getDetails() throws RequestFailedException {
		List<TutorialState> tutorialStates = api.playerProfile.getTutorialState().getTutorialStates();
		if (!tutorialStates.contains(TutorialState.POKESTOP_TUTORIAL)) {
			api.playerProfile.visitPokestopComplete();
		}

		return AsyncHelper.toBlocking(getDetailsAsync());
	}
	
	public ProtocolStringList getUrl() throws RequestFailedException {
		return getDetails().getImageUrl();
	}

	public String getDescription() throws RequestFailedException {
		return getDetails().getDescription();
	}
	
	public String getName() throws RequestFailedException {
		return getDetails().getName();
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Fort && ((Fort) obj).getId().equals(getId());
	}
}
