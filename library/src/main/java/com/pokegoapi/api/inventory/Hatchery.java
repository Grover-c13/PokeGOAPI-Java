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
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.api.pokemon.HatchedEgg;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hatchery {
	@Getter
	Set<EggPokemon> eggs = new HashSet<EggPokemon>();
	@Getter
	PokemonGo api;

	public Hatchery(PokemonGo pgo) {
		reset(pgo);
	}

	public void reset(PokemonGo api) {
		this.api = api;
		eggs = new HashSet<>();
	}

	public void addEgg(EggPokemon egg) {
		egg.setApi(api);
		eggs.add(egg);
	}
	
	
	/**
	 * Get if eggs has hatched.
	 * 
	 * @return list of hatched eggs
	 * @throws RemoteServerException e
	 * @throws LoginFailedException e
	 */
	public List<HatchedEgg> queryHatchedEggs() throws RemoteServerException, LoginFailedException {
		GetHatchedEggsMessage msg = GetHatchedEggsMessage.newBuilder().build(); 
		ServerRequest serverRequest = new ServerRequest(RequestType.GET_HATCHED_EGGS, msg);
		api.getRequestHandler().sendServerRequests(serverRequest);
		
		GetHatchedEggsResponse response = null;
		try {
			response = GetHatchedEggsResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		api.getInventories().updateInventories();
		List<HatchedEgg> eggs = new ArrayList<HatchedEgg>();
		for (int i = 0; i < response.getPokemonIdCount(); i++) {
			eggs.add(new HatchedEgg(response.getPokemonId(i), 
					response.getExperienceAwarded(i), 
					response.getCandyAwarded(i),
					response.getStardustAwarded(i)));
		}
		return eggs;
	}

}
