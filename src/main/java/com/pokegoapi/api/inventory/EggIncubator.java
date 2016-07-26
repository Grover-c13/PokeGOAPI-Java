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
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.UseItemEggIncubatorResponseOuterClass.UseItemEggIncubatorResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

public class EggIncubator {
	private final EggIncubatorOuterClass.EggIncubator proto;
	private final PokemonGo pgo;

	/**
	 * Create new EggIncubator with given proto.
	 *
	 * @param pgo   the api
	 * @param proto the proto
	 */
	public EggIncubator(PokemonGo pgo, EggIncubatorOuterClass.EggIncubator proto) {
		this.pgo = pgo;
		this.proto = proto;
	}

	/**
	 * Returns the remaining uses.
	 *
	 * @return uses remaining
	 */
	public int getUsesRemaining() {
		return proto.getUsesRemaining();
	}

	/**
	 * Hatch an egg.
	 *
	 * @param egg the egg
	 * @return status of putting egg in incubator
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	public UseItemEggIncubatorResponse.Result hatchEgg(EggPokemon egg)
			throws LoginFailedException, RemoteServerException {
		
		UseItemEggIncubatorMessage reqMsg = UseItemEggIncubatorMessage.newBuilder()
				.setItemId(proto.getId())
				.setPokemonId(egg.getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.USE_ITEM_EGG_INCUBATOR, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		UseItemEggIncubatorResponse response;
		try {
			response = UseItemEggIncubatorResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		pgo.getInventories().updateInventories(true);

		return response.getResult();
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
	 * Get the target distance for egg to hatch.
	 * 
	 * @return km distance to hatch the egg
	 */
	public double getKmTarget() {
		return proto.getTargetKmWalked();
	}
	
	/**
	 * Get the current distance walked with this incubator.
	 * 
	 * @return km walked with an egg
	 */
	public double getKmWalked() {
		return proto.getStartKmWalked();
	}
	
	/**
	 * Returns whether this incubator is hatching an egg.
	 * 
	 * @return true if it is currently hatching an egg
	 */
	public boolean isInUse() {
		return getKmTarget() > pgo.getPlayerProfile().getStats().getKmWalked();
	}
}
