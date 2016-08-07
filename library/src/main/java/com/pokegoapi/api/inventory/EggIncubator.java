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
	 * @throws LoginFailedException if there is an error with the token during retrieval of player stats
	 * @throws RemoteServerException if the server responds badly during retrieval of player stats
	 */
	public double getKmCurrentlyWalked() throws LoginFailedException, RemoteServerException {
		return pgo.getPlayerProfile().getStats().getKmWalked() - getKmStart();
	}
	
	/**
	 * Get the distance left to walk before this incubated egg will hatch.
	 * 
	 * @return distance to walk before hatch (km)
	 * @throws LoginFailedException if there is an error with the token during retrieval of player stats
	 * @throws RemoteServerException if the server responds badly during retrieval of player stats
	 */
	public double getKmLeftToWalk() throws LoginFailedException, RemoteServerException {
		return getKmTarget() - pgo.getPlayerProfile().getStats().getKmWalked();
	}
	
	/**
	 * Is the incubator currently being used
	 * 
	 * @return currently used or not
	 */
	public boolean isInUse() throws LoginFailedException, RemoteServerException {
		return getKmTarget() > pgo.getPlayerProfile().getStats().getKmWalked();
	}
}
