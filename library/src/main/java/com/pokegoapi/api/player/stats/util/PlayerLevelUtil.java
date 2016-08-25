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

package com.pokegoapi.api.player.stats.util;

/**
 * This class can help you retrieving informations about player level.
 * 
 * @author gionata-bisciari
 */
public abstract class PlayerLevelUtil {

	/**
	 * Max player level
	 */
	public static final int MAX_PLAYER_LEVEL = 40;

	/**
	 * Checks if the level exists.
	 * 
	 * @param lvl
	 *            The level.
	 * @return The level existence.
	 */
	public static boolean existsLevel(int lvl) {
		return lvl >= 1 && lvl <= MAX_PLAYER_LEVEL;
	}

	/**
	 * Checks if the level has a previous level.
	 * 
	 * @param lvl
	 *            The level.
	 * @return The previous level existence.
	 */
	public static boolean hasPrevLevel(int lvl) {
		return existsLevel(lvl - 1);
	}

	/**
	 * Checks if the level has a next level.
	 * 
	 * @param lvl
	 *            The level.
	 * @return The next level existence.
	 */
	public static boolean hasNextLevel(int lvl) {
		return existsLevel(lvl + 1);
	}
}