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
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.pokemon.Pokemon;

/**
 * Result of an evolution, wrapped within the new pokemon received
 */
public class EvolutionResult {
	private final EvolvePokemonResponseOuterClass.EvolvePokemonResponse proto;
	private final Pokemon pokemon;

	/**
	 * The evolution result.
	 *
	 * @param networking Networking
	 * @param inventories Inventories are needed for the pokemon actions
	 * @param playerProfile Player profile is needed for calulations of max cp and such
	 * @param proto      Pokemon proto
	 */
	public EvolutionResult(Networking networking, Inventories inventories, PlayerProfile playerProfile,
						   EvolvePokemonResponseOuterClass.EvolvePokemonResponse proto) {
		this.proto = proto;
		this.pokemon = new Pokemon(networking, inventories, playerProfile, proto.getEvolvedPokemonData());
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
