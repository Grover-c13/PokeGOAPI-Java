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

import java.util.HashMap;
import java.util.Map;

/**
 * Information in this class is based on:
 * http://pokemongo.gamepress.gg/cp-multiplier
 * and
 * http://pokemongo.gamepress.gg/pokemon-stats-advanced
 */
class PokemonCpUtils {
	private static final Map<Float,Float> CPMULTIPLIER_LEVEL = new HashMap<>();
	private static final Map<Float,Float> LEVEL_CPMULTIPLIER = new HashMap<>();

	static {
		CPMULTIPLIER_LEVEL.put(0.094f, 1f);
		CPMULTIPLIER_LEVEL.put(0.135137432f, 1.5f);
		CPMULTIPLIER_LEVEL.put(0.16639787f, 2f);
		CPMULTIPLIER_LEVEL.put(0.192650919f, 2.5f);
		CPMULTIPLIER_LEVEL.put(0.21573247f, 3f);
		CPMULTIPLIER_LEVEL.put(0.236572661f, 3.5f);
		CPMULTIPLIER_LEVEL.put(0.25572005f, 4f);
		CPMULTIPLIER_LEVEL.put(0.273530381f, 4.5f);
		CPMULTIPLIER_LEVEL.put(0.29024988f, 5f);
		CPMULTIPLIER_LEVEL.put(0.306057377f, 5.5f);
		CPMULTIPLIER_LEVEL.put(0.3210876f, 6f);
		CPMULTIPLIER_LEVEL.put(0.335445036f, 6.5f);
		CPMULTIPLIER_LEVEL.put(0.34921268f, 7f);
		CPMULTIPLIER_LEVEL.put(0.362457751f, 7.5f);
		CPMULTIPLIER_LEVEL.put(0.37523559f, 8f);
		CPMULTIPLIER_LEVEL.put(0.387592406f, 8.5f);
		CPMULTIPLIER_LEVEL.put(0.39956728f, 9f);
		CPMULTIPLIER_LEVEL.put(0.411193551f, 9.5f);
		CPMULTIPLIER_LEVEL.put(0.42250001f, 10f);
		CPMULTIPLIER_LEVEL.put(0.432926419f, 10.5f);
		CPMULTIPLIER_LEVEL.put(0.44310755f, 11f);
		CPMULTIPLIER_LEVEL.put(0.453059958f, 11.5f);
		CPMULTIPLIER_LEVEL.put(0.46279839f, 12f);
		CPMULTIPLIER_LEVEL.put(0.472336083f, 12.5f);
		CPMULTIPLIER_LEVEL.put(0.48168495f, 13f);
		CPMULTIPLIER_LEVEL.put(0.4908558f, 13.5f);
		CPMULTIPLIER_LEVEL.put(0.49985844f, 14f);
		CPMULTIPLIER_LEVEL.put(0.508701765f, 14.5f);
		CPMULTIPLIER_LEVEL.put(0.51739395f, 15f);
		CPMULTIPLIER_LEVEL.put(0.525942511f, 15.5f);
		CPMULTIPLIER_LEVEL.put(0.53435433f, 16f);
		CPMULTIPLIER_LEVEL.put(0.542635767f, 16.5f);
		CPMULTIPLIER_LEVEL.put(0.55079269f, 17f);
		CPMULTIPLIER_LEVEL.put(0.558830576f, 17.5f);
		CPMULTIPLIER_LEVEL.put(0.56675452f, 18f);
		CPMULTIPLIER_LEVEL.put(0.574569153f, 18.5f);
		CPMULTIPLIER_LEVEL.put(0.58227891f, 19f);
		CPMULTIPLIER_LEVEL.put(0.589887917f, 19.5f);
		CPMULTIPLIER_LEVEL.put(0.59740001f, 20f);
		CPMULTIPLIER_LEVEL.put(0.604818814f, 20.5f);
		CPMULTIPLIER_LEVEL.put(0.61215729f, 21f);
		CPMULTIPLIER_LEVEL.put(0.619399365f, 21.5f);
		CPMULTIPLIER_LEVEL.put(0.62656713f, 22f);
		CPMULTIPLIER_LEVEL.put(0.633644533f, 22.5f);
		CPMULTIPLIER_LEVEL.put(0.64065295f, 23f);
		CPMULTIPLIER_LEVEL.put(0.647576426f, 23.5f);
		CPMULTIPLIER_LEVEL.put(0.65443563f, 24f);
		CPMULTIPLIER_LEVEL.put(0.661214806f, 24.5f);
		CPMULTIPLIER_LEVEL.put(0.667934f, 25f);
		CPMULTIPLIER_LEVEL.put(0.674577537f, 25.5f);
		CPMULTIPLIER_LEVEL.put(0.68116492f, 26f);
		CPMULTIPLIER_LEVEL.put(0.687680648f, 26.5f);
		CPMULTIPLIER_LEVEL.put(0.69414365f, 27f);
		CPMULTIPLIER_LEVEL.put(0.700538673f, 27.5f);
		CPMULTIPLIER_LEVEL.put(0.70688421f, 28f);
		CPMULTIPLIER_LEVEL.put(0.713164996f, 28.5f);
		CPMULTIPLIER_LEVEL.put(0.71939909f, 29f);
		CPMULTIPLIER_LEVEL.put(0.725571552f, 29.5f);
		CPMULTIPLIER_LEVEL.put(0.7317f, 30f);
		CPMULTIPLIER_LEVEL.put(0.734741009f, 30.5f);
		CPMULTIPLIER_LEVEL.put(0.73776948f, 31f);
		CPMULTIPLIER_LEVEL.put(0.740785574f, 31.5f);
		CPMULTIPLIER_LEVEL.put(0.74378943f, 32f);
		CPMULTIPLIER_LEVEL.put(0.746781211f, 32.5f);
		CPMULTIPLIER_LEVEL.put(0.74976104f, 33f);
		CPMULTIPLIER_LEVEL.put(0.752729087f, 33.5f);
		CPMULTIPLIER_LEVEL.put(0.75568551f, 34f);
		CPMULTIPLIER_LEVEL.put(0.758630378f, 34.5f);
		CPMULTIPLIER_LEVEL.put(0.76156384f, 35f);
		CPMULTIPLIER_LEVEL.put(0.764486065f, 35.5f);
		CPMULTIPLIER_LEVEL.put(0.76739717f, 36f);
		CPMULTIPLIER_LEVEL.put(0.770297266f, 36.5f);
		CPMULTIPLIER_LEVEL.put(0.7731865f, 37f);
		CPMULTIPLIER_LEVEL.put(0.776064962f, 37.5f);
		CPMULTIPLIER_LEVEL.put(0.77893275f, 38f);
		CPMULTIPLIER_LEVEL.put(0.781790055f, 38.5f);
		CPMULTIPLIER_LEVEL.put(0.78463697f, 39f);
		CPMULTIPLIER_LEVEL.put(0.787473578f, 39.5f);
		CPMULTIPLIER_LEVEL.put(0.79030001f, 40f);
		for (Map.Entry<Float,Float> entry : CPMULTIPLIER_LEVEL.entrySet()) {
			LEVEL_CPMULTIPLIER.put(entry.getValue(), entry.getKey());
		}
	}

	/**
	 * Get the level from the cp multiplier
	 * @param cpMultiplier All CP multiplier values combined
	 * @return Level
	 */
	static float getLevelFromCpMultiplier(float cpMultiplier) {
		return CPMULTIPLIER_LEVEL.get(cpMultiplier);
	}

	/**
	 * Get the maximum CP from the values
	 * @param attack All attack values combined
	 * @param defense All defense values combined
	 * @param stamina All stamina values combined
	 * @return Maximum CP for these levels
	 */
	static int getMaxCp(int attack, int defense, int stamina) {
		float maxCpMultplier = LEVEL_CPMULTIPLIER.get(40f);
		return (int)(attack * Math.pow(defense, 0.5) * Math.pow(stamina, 0.5) * Math.pow(maxCpMultplier,2) / 10f);
	}

	/**
	 * Get the CP after powerup
	 * @param cp Current CP level
	 * @param cpMultiplier All CP multiplier values combined
	 * @return New CP level
	 */
	static int getCpAfterPowerup(float cp, float cpMultiplier) {
		// Based on http://pokemongo.gamepress.gg/power-up-costs
		float level = getLevelFromCpMultiplier(cpMultiplier);
		if (level <= 10) {
			return (int)((cp * 0.009426125469) / Math.pow(cpMultiplier, 2));
		}
		if (level <= 20) {
			return (int)((cp * 0.008919025675) / Math.pow(cpMultiplier, 2));
		}
		if (level <= 30) {
			return (int)((cp * 0.008924905903) / Math.pow(cpMultiplier, 2));
		}
		return (int)((cp * 0.00445946079) / Math.pow(cpMultiplier, 2));
	}

	/**
	 * Get the amount of stardust required to do a powerup
	 * @param cpMultiplier All CP multiplier values combined
	 * @param powerups Number of previous powerups
	 * @return Amount of stardust
	 */
	static int getStartdustCostsForPowerup(float cpMultiplier, int powerups) {
		// Based on http://pokemongo.gamepress.gg/power-up-costs
		float level = getLevelFromCpMultiplier(cpMultiplier);
		if (level <= 3 && powerups <= 4) {
			return 200;
		}
		if (level <= 4 && powerups <= 8) {
			return 400;
		}
		if (level <= 7 && powerups <= 12) {
			return 600;
		}
		if (level <= 8 && powerups <= 16) {
			return 800;
		}
		if (level <= 11 && powerups <= 20) {
			return 1000;
		}
		if (level <= 13 && powerups <= 24) {
			return 1300;
		}
		if (level <= 15 && powerups <= 28) {
			return 1600;
		}
		if (level <= 17 && powerups <= 32) {
			return 1900;
		}
		if (level <= 19 && powerups <= 36) {
			return 2200;
		}
		if (level <= 21 && powerups <= 40) {
			return 2500;
		}
		if (level <= 23 && powerups <= 44) {
			return 3000;
		}
		if (level <= 25 && powerups <= 48) {
			return 3500;
		}
		if (level <= 27 && powerups <= 52) {
			return 4000;
		}
		if (level <= 29 && powerups <= 56) {
			return 4500;
		}
		if (level <= 31 && powerups <= 60) {
			return 5000;
		}
		if (level <= 33 && powerups <= 64) {
			return 6000;
		}
		if (level <= 35 && powerups <= 68) {
			return 7000;
		}
		if (level <= 37 && powerups <= 72) {
			return 8000;
		}
		if (level <= 39 && powerups <= 76) {
			return 9000;
		}
		return 10000;
	}

	/**
	 * Get the amount of candy required to do a powerup
	 * @param cpMultiplier All CP multiplier values combined
	 * @param powerups Number of previous powerups
	 * @return Amount of candy
	 */
	static int getCandyCostsForPowerup(float cpMultiplier, int powerups) {
		// Based on http://pokemongo.gamepress.gg/power-up-costs
		float level = getLevelFromCpMultiplier(cpMultiplier);
		if (level <= 13 && powerups <= 20 ) {
			return 1;
		}
		if (level <= 21 && powerups <= 36 ) {
			return 2;
		}
		if (level <= 31 && powerups <= 60 ) {
			return 3;
		}
		return 4;
	}
}
