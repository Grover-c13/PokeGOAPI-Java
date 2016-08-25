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
 * This class can help you retrieving informations about experience.
 * 
 * @author gionata-bisciari
 */
public abstract class TotalExperienceUtil {

	/**
	 * Max experience
	 */
	public static final int MAX_EXP = 20000000;

	/**
	 * Checks if the value of total experience is valid.
	 * 
	 * @param totExp
	 *            The total experience.
	 * @return Validity of the value.
	 */
	public static boolean isValidTotalExperience(int totExp) {
		return totExp >= 1 && totExp <= MAX_EXP;
	}
}
