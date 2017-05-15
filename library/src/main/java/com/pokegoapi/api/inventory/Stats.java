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

import POGOProtos.Data.Player.PlayerStatsOuterClass;
import lombok.Getter;

public class Stats {
	@Getter
	private PlayerStatsOuterClass.PlayerStats proto;

	public Stats(PlayerStatsOuterClass.PlayerStats proto) {
		this.proto = proto;
	}

	/**
	 * Get the current level of the player.
	 *
	 * @return Current level of the player.
	 */
	public int getLevel() {
		return proto.getLevel();
	}

	/**
	 * Get the current total amount of experience earned.
	 *
	 * @return Current total amount of experience earned.
	 */
	public long getExperience() {
		return proto.getExperience();
	}

	/**
	 * Get the total experience required to reach the previous level.
	 *
	 * @deprecated use {@link #getTotExpToReachPreviousLevel()} instead.
	 * @return Total experience required to reach the previous level.
	 */
	@Deprecated
	public long getPrevLevelXp() {
		return proto.getPrevLevelXp();
	}

	/**
	 * Get the total experience required to reach the previous level.
	 *
	 * @return Total experience required to reach the previous level.
	 */
	public int getTotExpToReachPreviousLevel() {
		return LevelToExpUtils.getTotExpToReachPrevLevelOf(proto.getLevel());
	}

	/**
	 * Get the total experience required to reach the current level.
	 *
	 * @return Total experience required to reach the current level.
	 */
	public int getTotExpToReachCurrentLevel() {
		return LevelToExpUtils.getTotExpToReachLevel(proto.getLevel());
	}

	/**
	 * Get the total experience required to reach the next level.
	 *
	 * @deprecated use {@link #getTotExpToReachNextLevel()} instead.
	 * @return Total experience required to reach the next level.
	 */
	@Deprecated
	public long getNextLevelXp() {
		return proto.getNextLevelXp();
	}

	/**
	 * Get the total experience required to reach the next level.
	 *
	 * @return Total experience required to reach the next level.
	 */
	public int getTotExpToReachNextLevel() {
		return LevelToExpUtils.getTotExpToReachNextLevelOf(proto.getLevel());
	}

	/**
	 * Get the experience interval from current level to next level.
	 *
	 * @return Experience interval from current level to next.
	 */
	public int getExpIntervalFromCurrentLevelToNext() {
		return LevelToExpUtils.getExpIntervalFromLevelToNext(getLevel());
	}

	/**
	 * Get the experience earned in current level.
	 *
	 * @return Experience earned in current level.
	 */
	public int getExpEarnedInCurrentLevel() {
		return LevelToExpUtils.getExpEarnedInCurrentLevel(getLevel(), (int) getExperience());
	}

	/**
	 * Get the experience earned in current level in percentage (usefull for
	 * ProgressBars).
	 *
	 * @return Experience earned in current level in percentage.
	 */
	public int getExpEarnedInCurrentLevelInPercentage() {
		return LevelToExpUtils.getExpEarnedInCurrentLevelInPercentage(getLevel(), (int) getExperience());
	}

	/**
	 * Get the missing experience to level up.
	 *
	 * @return Missing experience to level up.
	 */
	public int getMissingExpToLevelUp() {
		return LevelToExpUtils.getMissingExpToLevelUp(getLevel(), (int) getExperience());
	}

	/**
	 * Get the missing experience to level up in percentage (usefull for
	 * ProgressBars).
	 *
	 * @return Missing experience to level up in percentage.
	 */
	public int getMissingExpToLevelUpInPercentage() {
		return LevelToExpUtils.getMissingExpToLevelUpInPercentage(getLevel(), (int) getExperience());
	}

	/**
	 * Get the amount of kilometers walked.
	 *
	 * @return Amount of kilometers walked.
	 */
	public float getKmWalked() {
		return proto.getKmWalked();
	}

	/**
	 * Get the amount of pokemons encountered.
	 *
	 * @return Amount of pokemons encountered.
	 */
	public int getPokemonsEncountered() {
		return proto.getPokemonsEncountered();
	}

	/**
	 * Get the amount of pokemons registered in pokedex.
	 *
	 * @return Amount of pokemons registered in pokedex.
	 */
	public int getUniquePokedexEntries() {
		return proto.getUniquePokedexEntries();
	}

	/**
	 * Get the amount of pokemons captured.
	 *
	 * @return Amount of pokemons captured.
	 */
	public int getPokemonsCaptured() {
		return proto.getPokemonsCaptured();
	}

	/**
	 * Get the amount of pokemons evolved.
	 *
	 * @return Amount of pokemons evolved.
	 */
	public int getEvolutions() {
		return proto.getEvolutions();
	}

	/**
	 * Get the amount of pokestops visited.
	 *
	 * @return Amount of pokestops visited.
	 */
	public int getPokeStopVisits() {
		return proto.getPokeStopVisits();
	}

	/**
	 * Get the amount of pokeballs thrown.
	 *
	 * @return Amount of pokeballs thrown.
	 */
	public int getPokeballsThrown() {
		return proto.getPokeballsThrown();
	}

	/**
	 * Get the amount of eggs hatched.
	 *
	 * @return Amount of eggs hatched.
	 */
	public int getEggsHatched() {
		return proto.getEggsHatched();
	}

	/**
	 * Get the amount of big magikarps caught.
	 *
	 * @return Amount of big magikarps caught.
	 */
	public int getBigMagikarpCaught() {
		return proto.getBigMagikarpCaught();
	}

	/**
	 * Get the amount of battles won versus an opponent team's gym.
	 *
	 * @return Amount of battles won versus an opponent team's gym.
	 */
	public int getBattleAttackWon() {
		return proto.getBattleAttackWon();
	}

	/**
	 * Get the total amount of battles undertaken versus an opponent team's gym.
	 *
	 * @return Total amount of battles undertaken versus an opponent team's gym.
	 */
	public int getBattleAttackTotal() {
		return proto.getBattleAttackTotal();
	}

	/**
	 * Get the amount of battles won as defender of a gym.
	 *
	 * @return Amount of battles won as defender of a gym.
	 */
	public int getBattleDefendedWon() {
		return proto.getBattleDefendedWon();
	}

	/**
	 * Get the amount of battles won versus your team's gym.
	 *
	 * @return Amount of battles won versus your team's gym.
	 */
	public int getBattleTrainingWon() {
		return proto.getBattleTrainingWon();
	}

	/**
	 * Get the total amount of battles undertaken versus your team's gym.
	 *
	 * @return Total amount of battles undertaken versus your team's gym.
	 */
	public int getBattleTrainingTotal() {
		return proto.getBattleTrainingTotal();
	}

	/**
	 * Get the total prestige raised in gyms.
	 *
	 * @return Total prestige raised in gyms.
	 */
	public int getPrestigeRaisedTotal() {
		return proto.getPrestigeRaisedTotal();
	}

	/**
	 * Get the total prestige dropped in gyms.
	 *
	 * @return Total prestige dropped in gyms.
	 */
	public int getPrestigeDroppedTotal() {
		return proto.getPrestigeDroppedTotal();
	}

	/**
	 * Get the amount of pokemons deployed in gyms.
	 *
	 * @return Amount of pokemons deployed in gyms.
	 */
	public int getPokemonDeployed() {
		return proto.getPokemonDeployed();
	}

	/**
	 * Get the amount of small rattatas caught.
	 *
	 * @return Amount of small rattatas caught.
	 */
	public int getSmallRattataCaught() {
		return proto.getSmallRattataCaught();
	}
}
