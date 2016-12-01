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

package com.pokegoapi.api.pokemon;

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Evolution {
	@Getter
	private PokemonId[] parents;
	@Getter
	private PokemonId pokemon;
	@Getter
	private List<PokemonId> evolutions = new ArrayList<>();
	@Getter
	private int stage;

	/**
	 * Constructor for this evolution class
	 * @param parents the parents of this evolution
	 * @param pokemon the pokmon being evolved
	 * @param stage the evolution stage, starting at 0
	 */
	public Evolution(PokemonId[] parents, PokemonId pokemon, int stage) {
		this.parents = parents;
		this.pokemon = pokemon;
		this.stage = stage;
	}

	/**
	 * Adds the given pokemon as an evolution
	 * @param pokemon the pokemon to add
	 */
	public void addEvolution(PokemonId pokemon) {
		this.evolutions.add(pokemon);
	}
}
