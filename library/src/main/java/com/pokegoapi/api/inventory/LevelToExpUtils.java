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

import java.util.HashMap;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

public final class LevelToExpUtils {

	private static final HashMap<Integer, Integer> LEVEL_TOT_EXP_TO_REACH = new HashMap<Integer, Integer>();

	static {
		LEVEL_TOT_EXP_TO_REACH.put(1, 0);
		LEVEL_TOT_EXP_TO_REACH.put(2, 1000);
		LEVEL_TOT_EXP_TO_REACH.put(3, 3000);
		LEVEL_TOT_EXP_TO_REACH.put(4, 6000);
		LEVEL_TOT_EXP_TO_REACH.put(5, 10000);
		LEVEL_TOT_EXP_TO_REACH.put(6, 15000);
		LEVEL_TOT_EXP_TO_REACH.put(7, 21000);
		LEVEL_TOT_EXP_TO_REACH.put(8, 28000);
		LEVEL_TOT_EXP_TO_REACH.put(9, 36000);
		LEVEL_TOT_EXP_TO_REACH.put(10, 45000);
		LEVEL_TOT_EXP_TO_REACH.put(11, 55000);
		LEVEL_TOT_EXP_TO_REACH.put(12, 65000);
		LEVEL_TOT_EXP_TO_REACH.put(13, 75000);
		LEVEL_TOT_EXP_TO_REACH.put(14, 85000);
		LEVEL_TOT_EXP_TO_REACH.put(15, 100000);
		LEVEL_TOT_EXP_TO_REACH.put(16, 120000);
		LEVEL_TOT_EXP_TO_REACH.put(17, 140000);
		LEVEL_TOT_EXP_TO_REACH.put(18, 160000);
		LEVEL_TOT_EXP_TO_REACH.put(19, 185000);
		LEVEL_TOT_EXP_TO_REACH.put(20, 210000);
		LEVEL_TOT_EXP_TO_REACH.put(21, 260000);
		LEVEL_TOT_EXP_TO_REACH.put(22, 335000);
		LEVEL_TOT_EXP_TO_REACH.put(23, 435000);
		LEVEL_TOT_EXP_TO_REACH.put(24, 560000);
		LEVEL_TOT_EXP_TO_REACH.put(25, 710000);
		LEVEL_TOT_EXP_TO_REACH.put(26, 900000);
		LEVEL_TOT_EXP_TO_REACH.put(27, 1100000);
		LEVEL_TOT_EXP_TO_REACH.put(28, 1350000);
		LEVEL_TOT_EXP_TO_REACH.put(29, 1650000);
		LEVEL_TOT_EXP_TO_REACH.put(30, 2000000);
		LEVEL_TOT_EXP_TO_REACH.put(31, 2500000);
		LEVEL_TOT_EXP_TO_REACH.put(32, 3000000);
		LEVEL_TOT_EXP_TO_REACH.put(33, 3750000);
		LEVEL_TOT_EXP_TO_REACH.put(34, 4750000);
		LEVEL_TOT_EXP_TO_REACH.put(35, 6000000);
		LEVEL_TOT_EXP_TO_REACH.put(36, 7500000);
		LEVEL_TOT_EXP_TO_REACH.put(37, 9500000);
		LEVEL_TOT_EXP_TO_REACH.put(38, 12000000);
		LEVEL_TOT_EXP_TO_REACH.put(39, 15000000);
		LEVEL_TOT_EXP_TO_REACH.put(40, 20000000);
	}

	/**
	 * Get the total experience required to reach the previous level of the one
	 * passed as parameter.
	 *
	 * @param lvl
	 *            The level.
	 * @return Total experience required to reach the previous level.
	 */
	public static int getTotExpToReachPrevLevelOf(final int lvl) {
		return LEVEL_TOT_EXP_TO_REACH.get(lvl - 1);
	}

	/**
	 * Get the total experience required to reach the level passed as parameter.
	 *
	 * @param lvl
	 *            The level.
	 * @return Total experience required to reach the level.
	 */
	public static int getTotExpToReachLevel(final int lvl) {
		return LEVEL_TOT_EXP_TO_REACH.get(lvl);
	}

	/**
	 * Get the total experience required to reach the next level of the one
	 * passed as parameter.
	 *
	 * @param lvl
	 *            The level.
	 * @return Total experience required to reach the next level.
	 */
	public static int getTotExpToReachNextLevelOf(final int lvl) {
		return LEVEL_TOT_EXP_TO_REACH.get(lvl + 1);
	}

	/**
	 * Get the experience required to level up from the level passed as
	 * parameter to the next.
	 *
	 * @param lvl
	 *            The level.
	 * @return Experience required in level to reach the next level.
	 */
	public static int getExpIntervalFromLevelToNext(final int lvl) {
		return (getTotExpToReachNextLevelOf(lvl) - getTotExpToReachLevel(lvl));
	}

	/**
	 * Get the experience earned in current level.
	 *
	 * @param onlyCurrentLvl
	 *            Current player level.
	 * @param onlyCurrentTotalExperience
	 *            Total experience earned from the player.
	 * @return Experience earned in current level.
	 */
	public static int getExpEarnedInCurrentLevel(final int onlyCurrentLvl, final int onlyCurrentTotalExperience) {
		return (getExpIntervalFromLevelToNext(onlyCurrentLvl)
				- (getTotExpToReachNextLevelOf(onlyCurrentLvl) - onlyCurrentTotalExperience));
	}

	/**
	 * Get the experience earned in current level.
	 *
	 * @param api
	 *            PokemonGo instance
	 * @return Experience earned in current level.
	 * @throws LoginFailedException
	 *             when the auth is invalid.
	 * @throws RemoteServerException
	 *             when the server is down/having issues or no internet
	 *             connection.
	 */
	public static int getExpEarnedInCurrentLevel(final PokemonGo api)
			throws LoginFailedException, RemoteServerException {
		return getExpEarnedInCurrentLevel(api.getPlayerProfile().getStats().getLevel(),
				(int) api.getPlayerProfile().getStats().getExperience());
	}

	/**
	 * Get the experience earned in current level in percentage.
	 *
	 * @param onlyCurrentLvl
	 *            Current player level.
	 * @param onlyCurrentTotalExperience
	 *            Total experience earned from the player.
	 * @return Experience earned in current level in percentage.
	 */
	public static int getExpEarnedInCurrentLevelInPercentage(final int onlyCurrentLvl,
			final int onlyCurrentTotalExperience) {
		return ((getExpEarnedInCurrentLevel(onlyCurrentLvl, onlyCurrentTotalExperience) * 100)
				/ getExpIntervalFromLevelToNext(onlyCurrentLvl));
	}

	/**
	 * Get the experience earned in current level in percentage.
	 *
	 * @param api
	 *            PokemonGo instance
	 * @return Experience earned in current level in percentage.
	 * @throws LoginFailedException
	 *             when the auth is invalid.
	 * @throws RemoteServerException
	 *             when the server is down/having issues or no internet
	 *             connection.
	 */
	public static int getExpEarnedInCurrentLevelInPercentage(final PokemonGo api)
			throws LoginFailedException, RemoteServerException {
		return getExpEarnedInCurrentLevelInPercentage(api.getPlayerProfile().getStats().getLevel(),
				(int) api.getPlayerProfile().getStats().getExperience());
	}

	/**
	 * Get the missing experience to level up.
	 *
	 * @param onlyCurrentLevel
	 *            Current player level.
	 * @param onlyCurrentTotalExperience
	 *            Total experience earned from the player.
	 * @return Missing experience to level up.
	 */
	public static int getMissingExpToLevelUp(final int onlyCurrentLevel, final int onlyCurrentTotalExperience) {
		return (getTotExpToReachNextLevelOf(onlyCurrentLevel) - onlyCurrentTotalExperience);
	}

	/**
	 * Get the experience missing to level up.
	 *
	 * @param api
	 *            PokemonGo instance
	 * @return Experience missing to level up.
	 * @throws LoginFailedException
	 *             when the auth is invalid.
	 * @throws RemoteServerException
	 *             when the server is down/having issues or no internet
	 *             connection.
	 */
	public static int getMissingExpToLevelUp(final PokemonGo api) throws LoginFailedException, RemoteServerException {
		return getMissingExpToLevelUp(api.getPlayerProfile().getStats().getLevel(),
				(int) api.getPlayerProfile().getStats().getExperience());
	}

	/**
	 * Get the missing experience to level up in percentage.
	 *
	 * @param onlyCurrentLvl
	 *            Current player level.
	 * @param onlyCurrentTotalExperience
	 *            Total experience earned from the player.
	 * @return Missing experience to level up in percentage.
	 */
	public static int getMissingExpToLevelUpInPercentage(final int onlyCurrentLvl,
			final int onlyCurrentTotalExperience) {
		return (100 - getExpEarnedInCurrentLevelInPercentage(onlyCurrentLvl, onlyCurrentTotalExperience));
	}

	/**
	 * Get the missing experience to level up in percentage.
	 *
	 * @param api
	 *            PokemonGo instance
	 * @return Missing experience to level up in percentage.
	 * @throws LoginFailedException
	 *             when the auth is invalid.
	 * @throws RemoteServerException
	 *             when the server is down/having issues or no internet
	 *             connection.
	 */
	public static int getMissingExpToLevelUpInPercentage(final PokemonGo api)
			throws LoginFailedException, RemoteServerException {
		return getMissingExpToLevelUpInPercentage(api.getPlayerProfile().getStats().getLevel(),
				(int) api.getPlayerProfile().getStats().getExperience());
	}
}
