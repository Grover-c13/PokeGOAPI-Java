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

package com.pokegoapi.api.inventory;

import POGOProtos.Inventory.EggIncubatorOuterClass;
import POGOProtos.Inventory.EggIncubatorTypeOuterClass.EggIncubatorType;
import POGOProtos.Networking.Requests.Messages.UseItemEggIncubatorMessageOuterClass.UseItemEggIncubatorMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.UseItemEggIncubatorResponseOuterClass.UseItemEggIncubatorResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonCallback;
import com.pokegoapi.main.PokemonRequest;
import com.pokegoapi.main.PokemonResponse;
import com.pokegoapi.main.RequestCallback;
import com.pokegoapi.main.Utils;

public class EggIncubator {
	private final EggIncubatorOuterClass.EggIncubator proto;
	private final PokemonGo api;

	/**
	 * Create new EggIncubator with given proto.
	 *
	 * @param api the api
	 * @param proto the proto
	 */
	public EggIncubator(PokemonGo api, EggIncubatorOuterClass.EggIncubator proto) {
		this.api = api;
		this.proto = proto;
	}

	/**
	 * Returns the remaining uses.
	 *
	 * @return uses remaining, always 0 for infinite egg incubator
	 */
	public int getUsesRemaining() {
		return proto.getUsesRemaining();
	}

	/**
	 * Hatch an egg.
	 *
	 * @param egg the egg
	 * @param result callback for status of putting egg in incubator
	 */
	public void hatchEgg(EggPokemon egg, final AsyncReturn<UseItemEggIncubatorResponse.Result> result) {
		final UseItemEggIncubatorMessage message = UseItemEggIncubatorMessage.newBuilder()
				.setItemId(proto.getId())
				.setPokemonId(egg.getId())
				.build();

		PokemonRequest request = new PokemonRequest(RequestType.USE_ITEM_EGG_INCUBATOR, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				UseItemEggIncubatorResponse.Result error
						= UseItemEggIncubatorResponse.Result.ERROR_POKEMON_EGG_NOT_FOUND;
				if (Utils.callbackException(response, result, error)) {
					return;
				}
				try {
					UseItemEggIncubatorResponse messageResponse
							= UseItemEggIncubatorResponse.parseFrom(response.getResponseData());
					api.getInventories().updateInventories(true, PokemonCallback.NULL_CALLBACK);
					result.onReceive(messageResponse.getResult(), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(error, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Get incubator id.
	 *
	 * @return the id
	 */
	public String getId() {
		return proto.getId();
	}

	/**
	 * Get incubator type.
	 *
	 * @return EggIncubatorType
	 */
	public EggIncubatorType getType() {
		return proto.getIncubatorType();
	}

	/**
	 * Get the total distance you need to walk to hatch the current egg.
	 *
	 * @return total distance to walk to hatch the egg (km)
	 */
	public double getKmTarget() {
		return proto.getTargetKmWalked();
	}

	/**
	 * Get the distance walked before the current egg was incubated.
	 *
	 * @return distance to walked before incubating egg
	 * @deprecated Wrong method name, use {@link #getKmStart()}
	 */
	public double getKmWalked() {
		return getKmStart();
	}

	/**
	 * Get the distance walked before the current egg was incubated.
	 *
	 * @return distance walked before incubating egg (km)
	 */
	public double getKmStart() {
		return proto.getStartKmWalked();
	}

	/**
	 * Gets the total distance to walk with the current egg before hatching.
	 *
	 * @return total km between incubation and hatching
	 */
	public double getHatchDistance() {
		return getKmTarget() - getKmStart();
	}

	/**
	 * Get the distance walked with the current incubated egg.
	 *
	 * @return distance walked with the current incubated egg (km)
	 */
	public double getKmCurrentlyWalked() {
		return api.getPlayerProfile().getStats().getKmWalked() - getKmStart();
	}

	/**
	 * Get the distance left to walk before this incubated egg will hatch.
	 *
	 * @return distance to walk before hatch (km)
	 */
	public double getKmLeftToWalk() {
		return getKmTarget() - api.getPlayerProfile().getStats().getKmWalked();
	}

	/**
	 * Is the incubator currently being used
	 *
	 * @return currently used or not
	 */
	public boolean isInUse() {
		return getKmTarget() > api.getPlayerProfile().getStats().getKmWalked();
	}
}
