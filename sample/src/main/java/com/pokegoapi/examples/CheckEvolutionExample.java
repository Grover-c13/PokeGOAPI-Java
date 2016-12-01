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

package com.pokegoapi.examples;

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import com.pokegoapi.api.pokemon.Evolution;
import com.pokegoapi.api.pokemon.Evolutions;

import java.util.List;

public class CheckEvolutionExample {

	/**
	 * Displays pokemon evolutions
	 *
	 * @param args Not used
	 */
	public static void main(String[] args) {
		System.out.println("Evolutions: ");
		for (PokemonId pokemon : PokemonId.values()) {
			Evolution evolution = Evolutions.getEvolution(pokemon);
			List<PokemonId> evolutions = Evolutions.getEvolutions(pokemon);
			if (evolutions.size() > 0) {
				System.out.println(pokemon + " -> " + evolutions + " (Stage: " + evolution.getStage() + ")");
			}
		}
		System.out.println();
		System.out.println("Most basic: ");
		for (PokemonId pokemon : PokemonId.values()) {
			List<PokemonId> basic = Evolutions.getBasic(pokemon);
			if (basic.size() > 0) {
				//Check this is not the most basic pokemon
				if (!(basic.size() == 1 && basic.contains(pokemon))) {
					System.out.println(pokemon + " -> " + basic);
				}
			}
		}
		System.out.println();
		System.out.println("Highest: ");
		for (PokemonId pokemon : PokemonId.values()) {
			List<PokemonId> highest = Evolutions.getHighest(pokemon);
			if (highest.size() > 0) {
				//Check this is not the highest pokemon
				if (!(highest.size() == 1 && highest.contains(pokemon))) {
					System.out.println(pokemon + " -> " + highest);
				}
			}
		}
	}
}
