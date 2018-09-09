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
import com.pokegoapi.api.settings.templates.ItemTemplates;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Evolution {
	@Getter
	public PokemonId parent;
	@Getter
	private PokemonId pokemon;
	@Getter
	public List<PokemonId> evolutions = new ArrayList<>();
	@Getter
	public List<EvolutionBranch> evolutionBranch;

	/**
	 * Constructor for this evolution class
	 *
	 * @param templates the item templates received from the server
	 * @param parent the parent of this evolution
	 * @param pokemon the pokemon being evolved
	 */
	public Evolution(ItemTemplates templates, PokemonId parent, PokemonId pokemon) {
		this.parent = parent;
		this.pokemon = pokemon;
		this.evolutionBranch = templates.getPokemonSettings(pokemon).getEvolutionBranchList();
		for (EvolutionBranch evolutionBranch : evolutionBranch) {
			evolutions.add(evolutionBranch.getEvolution());
		}
	}
}
