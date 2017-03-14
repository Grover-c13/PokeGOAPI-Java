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
import POGOProtos.Settings.Master.Pokemon.EvolutionBranchOuterClass.EvolutionBranch;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pokegoapi.main.PokemonMeta;

public class Evolution {
	@Getter
	private PokemonId parent;
	@Getter
	private PokemonId pokemon;
	@Getter
	private List<PokemonId> evolutions = new ArrayList<>();
	@Getter
	private List<EvolutionBranch> evolutionBranch;

	/**
	 * Constructor for this evolution class
	 *
	 * @param parent the parent of this evolution
	 * @param pokemon the pokemon being evolved
	 */
	public Evolution(PokemonId parent, PokemonId pokemon) {
		this.parent = parent;
		this.pokemon = pokemon;
		this.evolutionBranch = PokemonMeta.getPokemonSettings(pokemon).getEvolutionBranchList();
		for (EvolutionBranch evolutionBranch : this.evolutionBranch) {
			this.evolutions.add(evolutionBranch.getEvolution());
		}
		
	}
}
