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

import POGOProtos.Enums.PokemonIdOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import java8.util.function.Predicate;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class PokeBank {
	@Getter
	List<Pokemon> pokemons = new ArrayList<Pokemon>();
	@Getter
	PokemonGo instance;

	public PokeBank(PokemonGo instance) {
		this.instance = instance;
	}

	/**
	 * Add a pokemon to the pokebank inventory.  Will not add duplicates (pokemon with same id).
	 * @param pokemon Pokemon to add to the inventory
	 */
	public void addPokemon(final Pokemon pokemon) {
		List<Pokemon> alreadyAdded = StreamSupport.stream(pokemons).filter(new Predicate<Pokemon>() {
			@Override
			public boolean test(Pokemon testPokemon) {
				return pokemon.getId() == testPokemon.getId();
			}
		}).collect(Collectors.<Pokemon>toList());
		if (alreadyAdded.size() < 1) {
			pokemons.add(pokemon);
		}
	}



	/**
	 * Gets pokemon by pokemon id.
	 *
	 * @param id the id
	 * @return the pokemon by pokemon id
	 */
	public List<Pokemon> getPokemonByPokemonId(final PokemonIdOuterClass.PokemonId id) {
		return StreamSupport.stream(pokemons).filter(new Predicate<Pokemon>() {
			@Override
			public boolean test(Pokemon pokemon) {
				return pokemon.getPokemonId().equals(id);
			}
		}).collect(Collectors.<Pokemon>toList());
	}

	/**
	 * Remove pokemon.
	 *
	 * @param pokemon the pokemon
	 */
	public void removePokemon(final Pokemon pokemon) {
		pokemons = StreamSupport.stream(pokemons).filter(new Predicate<Pokemon>() {
			@Override
			public boolean test(Pokemon pokemn) {
				return pokemn.getId() != pokemon.getId();
			}
		}).collect(Collectors.<Pokemon>toList());
	}
}
