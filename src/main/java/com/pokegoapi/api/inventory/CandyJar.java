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

import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import com.pokegoapi.api.PokemonGo;
import lombok.ToString;

import java.util.HashMap;

@ToString
public class CandyJar {
	private PokemonGo pgo;
	private HashMap<PokemonFamilyId, Integer> candies;

	public CandyJar(PokemonGo pgo) {
		this.pgo = pgo;
		candies = new HashMap<>();
	}

	public void setCandy(PokemonFamilyId family, int candies) {
		this.candies.put(family, candies);
	}

	public int getCandies(PokemonFamilyId family) {
		return this.candies.get(family);
	}
}
