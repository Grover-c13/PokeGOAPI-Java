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

package com.pokegoapi.api.player;

import java.util.ArrayList;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

/**
 * This class can help you retriving informations about experience from level.
 *
 * @author gionata.bisciari
 */
public final class LevelToExpUtils {

	private static final ArrayList<Integer> LEVEL_TOT_EXP_TO_REACH = new ArrayList<Integer>();

	static {
		LEVEL_TOT_EXP_TO_REACH.add(0); // LVL 1
		LEVEL_TOT_EXP_TO_REACH.add(1000); // LVL 2
		LEVEL_TOT_EXP_TO_REACH.add(3000); // LVL 3
		LEVEL_TOT_EXP_TO_REACH.add(6000); // LVL 4
		LEVEL_TOT_EXP_TO_REACH.add(10000); // LVL 5
		LEVEL_TOT_EXP_TO_REACH.add(15000); // LVL 6
		LEVEL_TOT_EXP_TO_REACH.add(21000); // LVL 7
		LEVEL_TOT_EXP_TO_REACH.add(28000); // LVL 8
		LEVEL_TOT_EXP_TO_REACH.add(36000); // LVL 9
		LEVEL_TOT_EXP_TO_REACH.add(45000); // LVL 10
		LEVEL_TOT_EXP_TO_REACH.add(55000); // LVL 11
		LEVEL_TOT_EXP_TO_REACH.add(65000); // LVL 12
		LEVEL_TOT_EXP_TO_REACH.add(75000); // LVL 13
		LEVEL_TOT_EXP_TO_REACH.add(85000); // LVL 14
		LEVEL_TOT_EXP_TO_REACH.add(100000); // LVL 15
		LEVEL_TOT_EXP_TO_REACH.add(120000); // LVL 16
		LEVEL_TOT_EXP_TO_REACH.add(140000); // LVL 17
		LEVEL_TOT_EXP_TO_REACH.add(160000); // LVL 18
		LEVEL_TOT_EXP_TO_REACH.add(185000); // LVL 19
		LEVEL_TOT_EXP_TO_REACH.add(210000); // LVL 20
		LEVEL_TOT_EXP_TO_REACH.add(260000); // LVL 21
		LEVEL_TOT_EXP_TO_REACH.add(335000); // LVL 22
		LEVEL_TOT_EXP_TO_REACH.add(435000); // LVL 23
		LEVEL_TOT_EXP_TO_REACH.add(560000); // LVL 24
		LEVEL_TOT_EXP_TO_REACH.add(710000); // LVL 25
		LEVEL_TOT_EXP_TO_REACH.add(900000); // LVL 26
		LEVEL_TOT_EXP_TO_REACH.add(1100000); // LVL 27
		LEVEL_TOT_EXP_TO_REACH.add(1350000); // LVL 28
		LEVEL_TOT_EXP_TO_REACH.add(1650000); // LVL 29
		LEVEL_TOT_EXP_TO_REACH.add(2000000); // LVL 30
		LEVEL_TOT_EXP_TO_REACH.add(2500000); // LVL 31
		LEVEL_TOT_EXP_TO_REACH.add(3000000); // LVL 32
		LEVEL_TOT_EXP_TO_REACH.add(3750000); // LVL 33
		LEVEL_TOT_EXP_TO_REACH.add(4750000); // LVL 34
		LEVEL_TOT_EXP_TO_REACH.add(6000000); // LVL 35
		LEVEL_TOT_EXP_TO_REACH.add(7500000); // LVL 36
		LEVEL_TOT_EXP_TO_REACH.add(9500000); // LVL 37
		LEVEL_TOT_EXP_TO_REACH.add(12000000); // LVL 38
		LEVEL_TOT_EXP_TO_REACH.add(15000000); // LVL 39
		LEVEL_TOT_EXP_TO_REACH.add(20000000); // LVL 40
	}

	/**
	 * Get the total experience required to reach the previous level of the
	 * level passed as parameter.
	 *
	 * @param lvl
	 *            The level.
	 * @return Total experience required to reach the previous level.
	 */
	public static int getTotExpToReachPrevLevelOf(final int lvl) {
		return LEVEL_TOT_EXP_TO_REACH.get(lvl - 2);
	}

	/**
	 * Get the total experience required to reach the level passed as parameter.
	 *
	 * @param lvl
	 *            The level.
	 * @return Total experience required to reach the level.
	 */
	public static int getTotExpToReachLevel(final int lvl) {
		return LEVEL_TOT_EXP_TO_REACH.get(lvl - 1);
	}

	/**
	 * Get the total experience required to reach the next level of the level
	 * passed as parameter.
	 *
	 * @param lvl
	 *            The level.
	 * @return Total experience required to reach the next level.
	 */
	public static int getTotExpToReachNextLevelOf(final int lvl) {
		return LEVEL_TOT_EXP_TO_REACH.get(lvl);
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
	 *             When the auth is invalid. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws RemoteServerException
	 *             When the server is down/having issues or no internet
	 *             connection. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 */
	public static int getExpEarnedInCurrentLevel(final PokemonGo api)
			throws LoginFailedException, RemoteServerException {
		return getExpEarnedInCurrentLevel(api.getPlayerProfile().getStats().getCurrentPlayerLevel(),
				api.getPlayerProfile().getStats().getTotalExperienceEarned());
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
	 *             When the auth is invalid. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws RemoteServerException
	 *             When the server is down/having issues or no internet
	 *             connection. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 */
	public static int getExpEarnedInCurrentLevelInPercentage(final PokemonGo api)
			throws LoginFailedException, RemoteServerException {
		return getExpEarnedInCurrentLevelInPercentage(api.getPlayerProfile().getStats().getCurrentPlayerLevel(),
				api.getPlayerProfile().getStats().getTotalExperienceEarned());
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
	 *             When the auth is invalid. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws RemoteServerException
	 *             When the server is down/having issues or no internet
	 *             connection. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 */
	public static int getMissingExpToLevelUp(final PokemonGo api) throws LoginFailedException, RemoteServerException {
		return getMissingExpToLevelUp(api.getPlayerProfile().getStats().getCurrentPlayerLevel(),
				api.getPlayerProfile().getStats().getTotalExperienceEarned());
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
	 *             When the auth is invalid. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 * @throws RemoteServerException
	 *             When the server is down/having issues or no internet
	 *             connection. Threw from
	 *             {@link com.pokegoapi.api.player.PlayerProfile#getStats()}.
	 */
	public static int getMissingExpToLevelUpInPercentage(final PokemonGo api)
			throws LoginFailedException, RemoteServerException {
		return getMissingExpToLevelUpInPercentage(api.getPlayerProfile().getStats().getCurrentPlayerLevel(),
				api.getPlayerProfile().getStats().getTotalExperienceEarned());
	}
}
