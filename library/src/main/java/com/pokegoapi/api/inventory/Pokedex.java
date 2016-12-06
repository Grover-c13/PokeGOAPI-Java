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

import POGOProtos.Data.PokedexEntryOuterClass.PokedexEntry;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class Pokedex {
	private final Map<PokemonId, PokedexEntry> pokedexMap =
			Collections.synchronizedMap(new EnumMap<PokemonId, PokedexEntry>(PokemonId.class));
	private final Object lock = new Object();

	public void reset() {
		synchronized (this.lock) {
			pokedexMap.clear();
		}
	}

	/**
	 * Add/Update a PokdexEntry.
	 *
	 * @param entry The entry to add or update
	 */
	public void add(PokedexEntry entry) {
		PokemonId id = PokemonId.forNumber(entry.getPokemonId().getNumber());
		synchronized (this.lock) {
			pokedexMap.put(id, entry);
		}
	}

	/**
	 * Get a pokedex entry value.
	 *
	 * @param pokemonId the ID of the pokemon to get
	 * @return Entry if in pokedex or null if it doesn't
	 */
	public PokedexEntry getPokedexEntry(PokemonId pokemonId) {
		synchronized (this.lock) {
			return pokedexMap.get(pokemonId);
		}
	}
}
