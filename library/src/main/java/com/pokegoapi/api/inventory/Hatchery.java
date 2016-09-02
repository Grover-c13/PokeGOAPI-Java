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

import POGOProtos.Data.PokemonDataOuterClass;
import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass.GetHatchedEggsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.api.pokemon.HatchedEgg;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.util.PokeAFunc;
import com.pokegoapi.util.PokeCallback;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Hatchery {
	@Getter

	private final ConcurrentMap<Long, EggPokemon> eggs = new ConcurrentHashMap<>();

	@Getter
	private PokemonGo api;

	public Hatchery(PokemonGo api) {
		this.api = api;
	}

	public void addEgg(PokemonDataOuterClass.PokemonData egg) {
		EggPokemon current =  eggs.putIfAbsent(egg.getId(), new EggPokemon(api, egg));
		if (current != null) {
			current.setProto(egg);
		}
	}

	/**
	 * Get if eggs has hatched.
	 *
	 * @param callback an optional callback to handle results
	 */
	public void queryHatchedEggs(PokeCallback<List<HatchedEgg>> callback) {
		GetHatchedEggsMessage msg = GetHatchedEggsMessage.newBuilder().build();

		new AsyncServerRequest(RequestType.GET_HATCHED_EGGS, msg,
				new PokeAFunc<GetHatchedEggsResponse, List<HatchedEgg>>() {
					@Override
					public List<HatchedEgg> exec(GetHatchedEggsResponse response) {
						List<HatchedEgg> eggs = new ArrayList<HatchedEgg>();
						for (int i = 0; i < response.getPokemonIdCount(); i++) {
							eggs.add(new HatchedEgg(response.getPokemonId(i),
									response.getExperienceAwarded(i),
									response.getCandyAwarded(i),
									response.getStardustAwarded(i)));
						}
						return eggs;
					}
				}, callback, api);
	}
}
