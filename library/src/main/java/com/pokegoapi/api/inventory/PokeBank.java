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
import POGOProtos.Enums.PokemonIdOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class PokeBank {
	@Getter
	private final ConcurrentHashMap<Long, Pokemon> pokemon = new ConcurrentHashMap<Long, Pokemon>();

	public PokeBank() {
	}

	/**
	 * Add a pokemon to the pokebank inventory.  Will not add duplicates (pokemon with same id), but update them!!
	 *
	 * @param pokemon Pokemon to add to the inventory
	 */
	public void addPokemon(PokemonGo api, PokemonDataOuterClass.PokemonData pokemonData) {
		synchronized (pokemon) {
			Pokemon current = pokemon.get(pokemonData.getId());
			if (current == null) {
				current = new Pokemon(api, pokemonData);
				this.pokemon.put(current.getId(), current);
			} else {
				current.setProto(pokemonData);
			}
		}
	}

	/**
	 * Gets pokemon by pokemon id.
	 *
	 * @param id the id
	 * @return the pokemon by pokemon id
	 */
	public List<Pokemon> getPokemonByPokemonId(final PokemonIdOuterClass.PokemonId id) {
		List<Pokemon> ret = new ArrayList<>();
		for (Pokemon p : pokemon.values()) {
			if (p.getPokemonId() == id)
				ret.add(p);
		}
		return ret;
	}

	/**
	 * Remove pokemon.
	 *
	 * @param pokemon the pokemon
	 */
	public void removePokemon(final Pokemon pokemon) {
		this.pokemon.remove(pokemon.getId());
	}

	/**
	 * Get a pokemon by id.
	 *
	 * @param id the id
	 * @return the pokemon
	 */
	public Pokemon getPokemonById(final Long id) {
		return pokemon.get(id);
	}


}
