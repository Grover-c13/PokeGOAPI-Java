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

import POGOProtos.Enums.PokemonIdOuterClass;

import java.util.List;

/**
 * Evolution forms
 */
public class EvolutionForm {
	private final PokemonIdOuterClass.PokemonId pokemonId;

	EvolutionForm(PokemonIdOuterClass.PokemonId pokemonId) {
		this.pokemonId = pokemonId;
	}

	/**
	 * @return True if type is fully upgraded
	 */
	public boolean isFullyEvolved() {
		return EvolutionInfo.isFullyEvolved(pokemonId);
	}

	/**
	 * @return List of evolution forms
	 */
	public List<EvolutionForm> getEvolutionForms() {
		return EvolutionInfo.getEvolutionForms(pokemonId);
	}

	/**
	 * @return Evolution stage
	 */
	public int getEvolutionStage() {
		return EvolutionInfo.getEvolutionStage(pokemonId);
	}

}
