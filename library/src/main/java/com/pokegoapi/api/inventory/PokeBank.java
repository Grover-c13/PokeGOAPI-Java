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
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.pokegoapi.api.pokemon.Pokemon;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PokeBank {
	@Getter
	private final List<Pokemon> pokemons = Collections.synchronizedList(new ArrayList<Pokemon>());
	@Getter
	private final Object lock = new Object();

	public PokeBank() {
	}

	public void reset() {
		synchronized (this.lock) {
			pokemons.clear();
		}
	}

	/**
	 * Add a pokemon to the pokebank inventory.  Will not add duplicates (pokemon with same id).
	 *
	 * @param pokemon Pokemon to add to the inventory
	 */
	public void addPokemon(final Pokemon pokemon) {
		synchronized (this.lock) {
			List<Pokemon> alreadyAdded = Stream.of(pokemons).filter(new Predicate<Pokemon>() {
				@Override
				public boolean test(Pokemon testPokemon) {
					return pokemon.getId() == testPokemon.getId();
				}
			}).collect(Collectors.<Pokemon>toList());
			if (alreadyAdded.size() < 1) {
				pokemons.add(pokemon);
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
		synchronized (this.lock) {
			return Stream.of(pokemons).filter(new Predicate<Pokemon>() {
				@Override
				public boolean test(Pokemon pokemon) {
					return pokemon.getPokemonId().equals(id);
				}
			}).collect(Collectors.<Pokemon>toList());
		}
	}

	/**
	 * Remove pokemon.
	 *
	 * @param pokemon the pokemon to remove.
	 */
	public void removePokemon(final Pokemon pokemon) {
		synchronized (this.lock) {
			List<Pokemon> previous = new ArrayList<>();
			previous.addAll(pokemons);

			pokemons.clear();
			pokemons.addAll(Stream.of(previous).filter(new Predicate<Pokemon>() {
				@Override
				public boolean test(Pokemon pokemn) {
					return pokemn.getId() != pokemon.getId();
				}
			}).collect(Collectors.<Pokemon>toList()));
		}
	}

	/**
	 * Get a pokemon by id.
	 *
	 * @param id the id
	 * @return the pokemon
	 */
	public Pokemon getPokemonById(final Long id) {
		synchronized (this.lock) {
			for (Pokemon pokemon : pokemons) {
				if (pokemon.getId() == id) {
					return pokemon;
				}
			}
		}
		return null;
	}
}
