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

package com.pokegoapi.api.settings;

import com.pokegoapi.api.inventory.Pokeball;

import java.util.List;

public interface PokeballSelector {
	/**
	 * Selects the lowest possible pokeball
	 */
	PokeballSelector LOWEST = new PokeballSelector() {
		@Override
		public Pokeball select(List<Pokeball> pokeballs, double captureProbability) {
			return pokeballs.get(0);
		}
	};
	/**
	 * Selects the highest possible pokeball
	 */
	PokeballSelector HIGHEST = new PokeballSelector() {
		@Override
		public Pokeball select(List<Pokeball> pokeballs, double captureProbability) {
			return pokeballs.get(pokeballs.size() - 1);
		}
	};
	/**
	 * Selects a pokeball to use based on the capture probability of the current pokemon
	 */
	PokeballSelector SMART = new PokeballSelector() {
		@Override
		public Pokeball select(List<Pokeball> pokeballs, double captureProbability) {
			Pokeball desired = pokeballs.get(0);
			for (Pokeball pokeball : pokeballs) {
				if (captureProbability <= pokeball.getCaptureProbability()) {
					desired = pokeball;
				}
			}
			return desired;
		}
	};

	Pokeball select(List<Pokeball> pokeballs, double captureProbability);
}