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

import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass.GetHatchedEggsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class Hatchery {
	@Getter
	Set<EggPokemon> eggs = new HashSet<EggPokemon>();
	@Getter
	PokemonGo instance;

	public Hatchery(PokemonGo instance) {
		this.instance = instance;
	}

	public void addEgg(EggPokemon egg) {
		eggs.add(egg);
	}

	/**
	 * Find new hatch results.
	 *
	 * @return the hatch results
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public GetHatchedEggsResponse getHatchResults() throws LoginFailedException, RemoteServerException {
		GetHatchedEggsMessage reqMsg = GetHatchedEggsMessage.newBuilder().build();

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.GET_HATCHED_EGGS, reqMsg);
		instance.getRequestHandler().sendServerRequests(serverRequest);

		GetHatchedEggsResponse response;
		try {
			response = GetHatchedEggsResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		if (response.getSuccess()) {
			instance.getInventories().updateInventories();
		}

		return response;
	}

}
