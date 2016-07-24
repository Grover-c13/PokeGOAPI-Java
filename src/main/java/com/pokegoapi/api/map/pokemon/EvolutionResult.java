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

package com.pokegoapi.api.map.pokemon;

import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;

public class EvolutionResult {

	private EvolvePokemonResponseOuterClass.EvolvePokemonResponse proto;
	private Pokemon pokemon;

	/**
	 * The evolution result.
	 * @param api PokemonGo api
	 * @param proto Pokemon proto
	 */
	public EvolutionResult(PokemonGo api, EvolvePokemonResponseOuterClass.EvolvePokemonResponse proto) {
		this.proto = proto;
		this.pokemon = new Pokemon(api, proto.getEvolvedPokemonData());
	}

	public EvolvePokemonResponseOuterClass.EvolvePokemonResponse.Result getResult() {
		return proto.getResult();
	}

	public Pokemon getEvolvedPokemon() {
		return pokemon;
	}

	public int getExpAwarded() {
		return proto.getExperienceAwarded();
	}

	public int getCandyAwarded() {
		return proto.getCandyAwarded();
	}

	public boolean isSuccessful() {
		return (getResult().equals(EvolvePokemonResponseOuterClass.EvolvePokemonResponse.Result.SUCCESS));
	}
}
