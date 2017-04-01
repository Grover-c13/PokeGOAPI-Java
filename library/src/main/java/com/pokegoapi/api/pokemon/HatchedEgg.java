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

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import com.pokegoapi.api.PokemonGo;
import lombok.Getter;
import lombok.Setter;

public class HatchedEgg {
	@Setter
	@Getter
	private Pokemon pokemon;
	@Setter
	@Getter
	private Long id;
	@Setter
	@Getter
	private int experience;
	@Setter
	@Getter
	private int candy;
	@Setter
	@Getter
	private int stardust;

	/**
	 * Creates a hatched egg
	 *
	 * @param pokemonId the hatched pokemon id
	 * @param experienceAwarded the experience awarded from this hatch
	 * @param candyAwarded the candy awarded from this hatch
	 * @param stardustAwarded the stardust awarded from this hatch
	 * @param hatchedPokemon the pokemon hatched
	 * @param api the current API
	 */
	public HatchedEgg(long pokemonId, int experienceAwarded, int candyAwarded, int stardustAwarded,
			PokemonData hatchedPokemon, PokemonGo api) {
		this.pokemon = new Pokemon(api, hatchedPokemon);
		this.id = pokemonId;
		this.experience = experienceAwarded;
		this.candy = candyAwarded;
		this.stardust = stardustAwarded;
	}

	@Override
	public int hashCode() {
		return id.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof HatchedEgg && ((HatchedEgg) obj).id.equals(id);
	}
}
