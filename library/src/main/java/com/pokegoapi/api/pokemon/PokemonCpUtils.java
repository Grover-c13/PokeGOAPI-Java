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

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Settings.Master.Pokemon.StatsAttributesOuterClass.StatsAttributes;
import POGOProtos.Settings.Master.PokemonSettingsOuterClass.PokemonSettings;
import POGOProtos.Settings.Master.PokemonUpgradeSettingsOuterClass.PokemonUpgradeSettings;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.settings.templates.ItemTemplates;
import com.pokegoapi.exceptions.NoSuchItemException;

/**
 * Information in this class is based on:
 * http://pokemongo.gamepress.gg/cp-multiplier
 * and
 * http://pokemongo.gamepress.gg/pokemon-stats-advanced
 */
public class PokemonCpUtils {
	private static float getLevel(double combinedCpMultiplier) {
		double level;
		if (combinedCpMultiplier < 0.734f) {
			// compute polynomial approximation obtained by regression
			level = 58.35178527 * combinedCpMultiplier * combinedCpMultiplier
					- 2.838007664 * combinedCpMultiplier + 0.8539209906;
		} else {
			// compute linear approximation obtained by regression
			level = 171.0112688 * combinedCpMultiplier - 95.20425243;
		}
		// round to nearest .5 value and return
		return (float) (Math.round((level) * 2) / 2.0);
	}

	/**
	 * Get the level from the cp multiplier
	 *
	 * @param combinedCpMultiplier All CP multiplier values combined
	 * @return Level
	 */
	public static float getLevelFromCpMultiplier(double combinedCpMultiplier) {
		return getLevel(combinedCpMultiplier);
	}

	/**
	 * Calculate CP based on raw values
	 *
	 * @param attack All attack values combined
	 * @param defense All defense values combined
	 * @param stamina All stamina values combined
	 * @param combinedCpMultiplier All CP multiplier values combined
	 * @return CP
	 */
	public static int getCp(int attack, int defense, int stamina, double combinedCpMultiplier) {
		return (int) Math.round(attack * Math.pow(defense, 0.5) * Math.pow(stamina, 0.5)
				* Math.pow(combinedCpMultiplier, 2) / 10f);
	}

	/**
	 * Get the maximum CP from the values
	 *
	 * @param api the current api
	 * @param attack All attack values combined
	 * @param defense All defense values combined
	 * @param stamina All stamina values combined
	 * @return Maximum CP for these levels
	 */
	public static int getMaxCp(PokemonGo api, int attack, int defense, int stamina) {
		return getMaxCpForPlayer(api, attack, defense, stamina, 40);
	}

	/**
	 * Get the absolute maximum CP for pokemons with their PokemonId.
	 *
	 * @param api the current api
	 * @param id The {@link PokemonId} of the Pokemon to get CP for.
	 * @return The absolute maximum CP
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link ItemTemplates}.
	 */
	public static int getAbsoluteMaxCp(PokemonGo api, PokemonId id) throws NoSuchItemException {
		PokemonSettings settings = api.itemTemplates.getPokemonSettings(id);
		if (settings == null) {
			throw new NoSuchItemException("Cannot find meta data for " + id);
		}
		StatsAttributes stats = settings.getStats();
		int attack = 15 + stats.getBaseAttack();
		int defense = 15 + stats.getBaseDefense();
		int stamina = 15 + stats.getBaseStamina();
		return getMaxCpForPlayer(api, attack, defense, stamina, 40);
	}

	/**
	 * Get the maximum CP from the values
	 *
	 * @param api the current api
	 * @param attack All attack values combined
	 * @param defense All defense values combined
	 * @param stamina All stamina values combined
	 * @param playerLevel The player level
	 * @return Maximum CP for these levels
	 */
	public static int getMaxCpForPlayer(PokemonGo api, int attack, int defense, int stamina, int playerLevel) {
		float maxLevel = Math.min(playerLevel + 1.5F, 40.0F);
		double maxCpMultplier = api.itemTemplates.getLevelCpMultiplier(maxLevel);
		return getCp(attack, defense, stamina, maxCpMultplier);
	}

	/**
	 * Get the CP after powerup
	 *
	 * @param cp Current CP level
	 * @param combinedCpMultiplier All CP multiplier values combined
	 * @return New CP
	 */
	public static int getCpAfterPowerup(int cp, double combinedCpMultiplier) {
		// Based on http://pokemongo.gamepress.gg/power-up-costs
		double level = getLevelFromCpMultiplier(combinedCpMultiplier);
		if (level <= 10) {
			return cp + (int) Math.round((cp * 0.009426125469) / Math.pow(combinedCpMultiplier, 2));
		}
		if (level <= 20) {
			return cp + (int) Math.round((cp * 0.008919025675) / Math.pow(combinedCpMultiplier, 2));
		}
		if (level <= 30) {
			return cp + (int) Math.round((cp * 0.008924905903) / Math.pow(combinedCpMultiplier, 2));
		}
		return cp + (int) Math.round((cp * 0.00445946079) / Math.pow(combinedCpMultiplier, 2));
	}

	/**
	 * Get the new additional multiplier after powerup
	 *
	 * @param api the current api
	 * @param cpMultiplier Multiplier
	 * @param additionalCpMultiplier Additional multiplier
	 * @return Additional CP multiplier after upgrade
	 */
	public static double getAdditionalCpMultiplierAfterPowerup(PokemonGo api,
			double cpMultiplier, double additionalCpMultiplier) {
		float nextLevel = getLevelFromCpMultiplier(cpMultiplier + additionalCpMultiplier) + .5f;
		return api.itemTemplates.getLevelCpMultiplier(nextLevel) - cpMultiplier;
	}

	/**
	 * Get the amount of stardust required to do a powerup
	 *
	 * @param api the current api
	 * @param combinedCpMultiplier All CP multiplier values combined
	 * @return Amount of stardust
	 */
	public static int getStartdustCostsForPowerup(PokemonGo api, double combinedCpMultiplier) {
		PokemonUpgradeSettings upgradeSettings = api.itemTemplates.upgradeSettings;
		int level = (int) getLevelFromCpMultiplier(combinedCpMultiplier);
		if (level < upgradeSettings.getStardustCostCount()) {
			return upgradeSettings.getStardustCost(level);
		}
		return 0;
	}

	/**
	 * Get the amount of candy required to do a powerup
	 *
	 * @param api the current api
	 * @param combinedCpMultiplier All CP multiplier values combined
	 * @return Amount of candy
	 */
	public static int getCandyCostsForPowerup(PokemonGo api, double combinedCpMultiplier) {
		int level = (int) getLevelFromCpMultiplier(combinedCpMultiplier);
		return api.itemTemplates.upgradeSettings.getCandyCost(level);
	}
}
