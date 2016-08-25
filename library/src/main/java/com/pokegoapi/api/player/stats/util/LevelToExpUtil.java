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

import java.util.ArrayList;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.player.stats.exceptions.InvalidExperienceException;
import com.pokegoapi.api.player.stats.exceptions.InvalidLevelException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

/**
 * This class can help you retrieving informations about experience from level.
 * 
 * @author gionata.bisciari
 */
public abstract class LevelToExpUtil {

	private static final ArrayList<Integer> LEVEL_TOT_EXP_TO_REACH = new ArrayList<Integer>();

	static {
		LEVEL_TOT_EXP_TO_REACH.add(0); // Level 1
		LEVEL_TOT_EXP_TO_REACH.add(1000); // Level 2
		LEVEL_TOT_EXP_TO_REACH.add(3000); // Level 3
		LEVEL_TOT_EXP_TO_REACH.add(6000); // Level 4
		LEVEL_TOT_EXP_TO_REACH.add(10000); // Level 5
		LEVEL_TOT_EXP_TO_REACH.add(15000); // Level 6
		LEVEL_TOT_EXP_TO_REACH.add(21000); // Level 7
		LEVEL_TOT_EXP_TO_REACH.add(28000); // Level 8
		LEVEL_TOT_EXP_TO_REACH.add(36000); // Level 9
		LEVEL_TOT_EXP_TO_REACH.add(45000); // Level 10
		LEVEL_TOT_EXP_TO_REACH.add(55000); // Level 11
		LEVEL_TOT_EXP_TO_REACH.add(65000); // Level 12
		LEVEL_TOT_EXP_TO_REACH.add(75000); // Level 13
		LEVEL_TOT_EXP_TO_REACH.add(85000); // Level 14
		LEVEL_TOT_EXP_TO_REACH.add(100000); // Level 15
		LEVEL_TOT_EXP_TO_REACH.add(120000); // Level 16
		LEVEL_TOT_EXP_TO_REACH.add(140000); // Level 17
		LEVEL_TOT_EXP_TO_REACH.add(160000); // Level 18
		LEVEL_TOT_EXP_TO_REACH.add(185000); // Level 19
		LEVEL_TOT_EXP_TO_REACH.add(210000); // Level 20
		LEVEL_TOT_EXP_TO_REACH.add(260000); // Level 21
		LEVEL_TOT_EXP_TO_REACH.add(335000); // Level 22
		LEVEL_TOT_EXP_TO_REACH.add(435000); // Level 23
		LEVEL_TOT_EXP_TO_REACH.add(560000); // Level 24
		LEVEL_TOT_EXP_TO_REACH.add(710000); // Level 25
		LEVEL_TOT_EXP_TO_REACH.add(900000); // Level 26
		LEVEL_TOT_EXP_TO_REACH.add(1100000); // Level 27
		LEVEL_TOT_EXP_TO_REACH.add(1350000); // Level 28
		LEVEL_TOT_EXP_TO_REACH.add(1650000); // Level 29
		LEVEL_TOT_EXP_TO_REACH.add(2000000); // Level 30
		LEVEL_TOT_EXP_TO_REACH.add(2500000); // Level 31
		LEVEL_TOT_EXP_TO_REACH.add(3000000); // Level 32
		LEVEL_TOT_EXP_TO_REACH.add(3750000); // Level 33
		LEVEL_TOT_EXP_TO_REACH.add(4750000); // Level 34
		LEVEL_TOT_EXP_TO_REACH.add(6000000); // Level 35
		LEVEL_TOT_EXP_TO_REACH.add(7500000); // Level 36
		LEVEL_TOT_EXP_TO_REACH.add(9500000); // Level 37
		LEVEL_TOT_EXP_TO_REACH.add(12000000); // Level 38
		LEVEL_TOT_EXP_TO_REACH.add(15000000); // Level 39
		LEVEL_TOT_EXP_TO_REACH.add(20000000); // Level 40
	}

	/**
	 * Get the total experience required to reach the previous level of the
	 * level passed as parameter.
	 *
	 * @param lvl
	 *            The level.
	 * @return Total experience required to reach the previous level.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 */
	public static int getTotExpToReachPrevLevelOf(final int lvl) throws InvalidLevelException {
		return getTotExpToReachLevel(lvl - 1);
	}

	/**
	 * Get the total experience required to reach the level passed as parameter.
	 *
	 * @param lvl
	 *            The level.
	 * @return Total experience required to reach the level.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 */
	public static int getTotExpToReachLevel(final int lvl) throws InvalidLevelException {
		checkLevel(lvl);
		return LEVEL_TOT_EXP_TO_REACH.get(lvl - 1);
	}

	/**
	 * Get the total experience required to reach the next level of the level
	 * passed as parameter.
	 *
	 * @param lvl
	 *            The level.
	 * @return Total experience required to reach the next level.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 */
	public static int getTotExpToReachNextLevelOf(final int lvl) throws InvalidLevelException {
		checkLevel(lvl);
		return getTotExpToReachLevel(lvl + 1);
	}

	/**
	 * Get the experience required to level up from the level passed as
	 * parameter to the next.
	 *
	 * @param lvl
	 *            The level.
	 * @return Experience required in level to reach the next level.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 */
	public static int getExpIntervalFromLevelToNext(final int lvl) throws InvalidLevelException {
		return getTotExpToReachNextLevelOf(lvl) - getTotExpToReachLevel(lvl);
	}

	/**
	 * Get the experience earned in current level.
	 *
	 * @param lvl
	 *            Current player level.
	 * @param totExp
	 *            Total experience earned from the player.
	 * @return Experience earned in current level.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link #checkTotalExperience(int)}.
	 */
	public static int getExpEarnedInCurrentLevel(final int lvl, final int totExp)
			throws InvalidLevelException, InvalidExperienceException {
		return getExpIntervalFromLevelToNext(lvl) - getMissingExpToLevelUp(lvl, totExp);
	}

	/**
	 * Get the experience earned in current level.
	 *
	 * @param api
	 *            PokemonGo instance
	 * @return Experience earned in current level.
	 * @throws LoginFailedException
	 *             When the auth is invalid. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws RemoteServerException
	 *             When the server is down/having issues or no internet
	 *             connection. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link #checkTotalExperience(int)}.
	 */
	public static int getExpEarnedInCurrentLevel(final PokemonGo api)
			throws LoginFailedException, RemoteServerException, InvalidLevelException, InvalidExperienceException {
		return getExpEarnedInCurrentLevel(api.getPlayerProfile().getStats().getCurrentPlayerLevel(),
				api.getPlayerProfile().getStats().getTotalExperienceEarned());
	}

	/**
	 * Get the experience earned in current level in percentage.
	 *
	 * @param lvl
	 *            Current player level.
	 * @param totExp
	 *            Total experience earned from the player.
	 * @return Experience earned in current level in percentage.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link #checkTotalExperience(int)}.
	 */
	public static int getExpEarnedInCurrentLevelInPercentage(final int lvl, final int totExp)
			throws InvalidLevelException, InvalidExperienceException {
		return getExpEarnedInCurrentLevel(lvl, totExp) * 100 / getExpIntervalFromLevelToNext(lvl);
	}

	/**
	 * Get the experience earned in current level in percentage.
	 *
	 * @param api
	 *            PokemonGo instance
	 * @return Experience earned in current level in percentage.
	 * @throws LoginFailedException
	 *             When the auth is invalid. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws RemoteServerException
	 *             When the server is down/having issues or no internet
	 *             connection. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link #checkTotalExperience(int)}.
	 */
	public static int getExpEarnedInCurrentLevelInPercentage(final PokemonGo api)
			throws InvalidLevelException, LoginFailedException, RemoteServerException, InvalidExperienceException {
		return getExpEarnedInCurrentLevelInPercentage(api.getPlayerProfile().getStats().getCurrentPlayerLevel(),
				api.getPlayerProfile().getStats().getTotalExperienceEarned());
	}

	/**
	 * Get the missing experience to level up.
	 *
	 * @param lvl
	 *            Current player level.
	 * @param totExp
	 *            Total experience earned from the player.
	 * @return Missing experience to level up.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link #checkTotalExperience(int)}.
	 */
	public static int getMissingExpToLevelUp(final int lvl, final int totExp)
			throws InvalidLevelException, InvalidExperienceException {
		checkTotalExperience(totExp);
		return getTotExpToReachNextLevelOf(lvl) - totExp;
	}

	/**
	 * Get the experience missing to level up.
	 *
	 * @param api
	 *            PokemonGo instance
	 * @return Experience missing to level up.
	 * @throws LoginFailedException
	 *             When the auth is invalid. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws RemoteServerException
	 *             When the server is down/having issues or no internet
	 *             connection. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link #checkTotalExperience(int)}.
	 */
	public static int getMissingExpToLevelUp(final PokemonGo api)
			throws LoginFailedException, RemoteServerException, InvalidLevelException, InvalidExperienceException {
		return getMissingExpToLevelUp(api.getPlayerProfile().getStats().getCurrentPlayerLevel(),
				api.getPlayerProfile().getStats().getTotalExperienceEarned());
	}

	/**
	 * Get the missing experience to level up in percentage.
	 *
	 * @param lvl
	 *            Current player level.
	 * @param totExp
	 *            Total experience earned from the player.
	 * @return Missing experience to level up in percentage.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link #checkTotalExperience(int)}.
	 */
	public static int getMissingExpToLevelUpInPercentage(final int lvl, final int totExp)
			throws InvalidLevelException, InvalidExperienceException {
		return 100 - getExpEarnedInCurrentLevelInPercentage(lvl, totExp);
	}

	/**
	 * Get the missing experience to level up in percentage.
	 *
	 * @param api
	 *            PokemonGo instance
	 * @return Missing experience to level up in percentage.
	 * @throws LoginFailedException
	 *             When the auth is invalid. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws RemoteServerException
	 *             When the server is down/having issues or no internet
	 *             connection. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link #checkLevel(int)}.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link #checkTotalExperience(int)}.
	 */
	public static int getMissingExpToLevelUpInPercentage(final PokemonGo api)
			throws LoginFailedException, RemoteServerException, InvalidLevelException, InvalidExperienceException {
		return getMissingExpToLevelUpInPercentage(api.getPlayerProfile().getStats().getCurrentPlayerLevel(),
				api.getPlayerProfile().getStats().getTotalExperienceEarned());
	}

	/**
	 * Check if the value of level is correct.
	 *
	 * @param lvl
	 *            The level.
	 * @throws InvalidLevelException
	 *             Thrown if the value of lvl is incorrect.
	 */
	private static void checkLevel(final int lvl) throws InvalidLevelException {
		if (!PlayerLevelUtil.existsLevel(lvl)) {
			throw new InvalidLevelException("Level " + lvl + " doesn't exists.");
		}
	}

	/**
	 * Checks if the value of total experience is valid.
	 * 
	 * @param totExp
	 *            The total experience.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid.
	 */
	public static void checkTotalExperience(final int totExp) throws InvalidExperienceException {
		if (!TotalExperienceUtil.isValidTotalExperience(totExp)) {
			throw new InvalidExperienceException(totExp + "isn't a valid value for experience");
		}
	}
}
