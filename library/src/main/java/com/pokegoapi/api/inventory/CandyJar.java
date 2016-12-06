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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ToString
public class CandyJar {
	private final PokemonGo api;
	private final Map<PokemonFamilyId, Integer> candies =
			Collections.synchronizedMap(new HashMap<PokemonFamilyId, Integer>());
	private final Object lock = new Object();

	public CandyJar(PokemonGo api) {
		this.api = api;
	}

	/**
	 * Resets this candy jar and removes all candies
	 */
	public void reset() {
		synchronized (this.lock) {
			candies.clear();
		}
	}

	/**
	 * Sets the number of candies in the jar.
	 *
	 * @param family  Pokemon family id
	 * @param candies Amount to set it to
	 */
	public void setCandy(PokemonFamilyId family, int candies) {
		synchronized (this.lock) {
			this.candies.put(family, candies);
		}
	}

	/**
	 * Adds a candy to the candy jar.
	 *
	 * @param family Pokemon family id
	 * @param amount Amount of candies to add
	 */
	public void addCandy(PokemonFamilyId family, int amount) {
		synchronized (this.lock) {
			if (candies.containsKey(family)) {
				candies.put(family, candies.get(family) + amount);
			} else {
				candies.put(family, amount);
			}
		}
	}

	/**
	 * Remove a candy from the candy jar.
	 *
	 * @param family Pokemon family id
	 * @param amount Amount of candies to remove
	 */
	public void removeCandy(PokemonFamilyId family, int amount) {
		synchronized (this.lock) {
			if (candies.containsKey(family)) {
				if (candies.get(family) - amount < 0) {
					candies.put(family, 0);
				} else {
					candies.put(family, candies.get(family) - amount);
				}
			} else {
				candies.put(family, 0);
			}
		}
	}

	/**
	 * Get number of candies from the candyjar.
	 *
	 * @param family Pokemon family id
	 * @return number of candies in jar
	 */
	public int getCandies(PokemonFamilyId family) {
		synchronized (this.lock) {
			if (candies.containsKey(family)) {
				return this.candies.get(family);
			} else {
				return 0;
			}
		}
	}
}
