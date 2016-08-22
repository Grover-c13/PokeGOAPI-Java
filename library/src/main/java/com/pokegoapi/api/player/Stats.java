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

import com.pokegoapi.exceptions.InvalidExperienceException;
import com.pokegoapi.exceptions.InvalidLevelException;
import com.pokegoapi.util.LevelToExpUtil;

import POGOProtos.Data.Player.PlayerStatsOuterClass;

/**
 * This class can help you retrieving informations about player stats.
 * 
 * @author gionata-bisciari
 */
public class Stats {

	private PlayerStatsOuterClass.PlayerStats proto;

	public Stats(PlayerStatsOuterClass.PlayerStats proto) {
		this.proto = proto;
	}

	/**
	 * Get the current level of the player.
	 *
	 * @return Current level of the player.
	 */
	public int getCurrentPlayerLevel() {
		return proto.getLevel();
	}

	/**
	 * Get the current total amount of experience earned.
	 *
	 * @return Current total amount of experience earned.
	 */
	public int getTotalExperienceEarned() {
		return (int) proto.getExperience();
	}

	/**
	 * Get the total experience required to reach the previous level.
	 *
	 * @return Total experience required to reach the previous level.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getTotExpToReachPrevLevelOf(int)}.
	 */
	public int getTotExpToReachPreviousLevel() throws InvalidLevelException {
		return LevelToExpUtil.getTotExpToReachPrevLevelOf(getCurrentPlayerLevel());
	}

	/**
	 * Get the total experience required to reach the current level.
	 *
	 * @return Total experience required to reach the current level.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getTotExpToReachLevel(int)}.
	 */
	public int getTotExpToReachCurrentLevel() throws InvalidLevelException {
		return LevelToExpUtil.getTotExpToReachLevel(getCurrentPlayerLevel());
	}

	/**
	 * Get the total experience required to reach the next level.
	 *
	 * @return Total experience required to reach the next level.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getTotExpToReachNextLevelOf(int)}.
	 */
	public int getTotExpToReachNextLevel() throws InvalidLevelException {
		return LevelToExpUtil.getTotExpToReachNextLevelOf(getCurrentPlayerLevel());
	}

	/**
	 * Get the experience interval from current level to next level.
	 *
	 * @return Experience interval from current level to next.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getExpIntervalFromLevelToNext(int)}.
	 */
	public int getExpIntervalFromCurrentLevelToNext() throws InvalidLevelException {
		return LevelToExpUtil.getExpIntervalFromLevelToNext(getCurrentPlayerLevel());
	}

	/**
	 * Get the experience earned in current level.
	 *
	 * @return Experience earned in current level.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getExpEarnedInCurrentLevel(int, int)}.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getExpEarnedInCurrentLevel(int, int)}.
	 */
	public int getExpEarnedInCurrentLevel() throws InvalidLevelException, InvalidExperienceException {
		return LevelToExpUtil.getExpEarnedInCurrentLevel(getCurrentPlayerLevel(), getTotalExperienceEarned());
	}

	/**
	 * Get the experience earned in current level in percentage (useful for
	 * ProgressBars).
	 *
	 * @return Experience earned in current level in percentage.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getExpEarnedInCurrentLevelInPercentage(int, int)}.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getExpEarnedInCurrentLevelInPercentage(int, int)}.
	 */
	public int getExpEarnedInCurrentLevelInPercentage() throws InvalidLevelException, InvalidExperienceException {
		return LevelToExpUtil.getExpEarnedInCurrentLevelInPercentage(getCurrentPlayerLevel(),
				getTotalExperienceEarned());
	}

	/**
	 * Get the missing experience to level up.
	 *
	 * @return Missing experience to level up.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getMissingExpToLevelUp(int, int)}.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getMissingExpToLevelUp(int, int)}.
	 */
	public int getMissingExpToLevelUp() throws InvalidLevelException, InvalidExperienceException {
		return LevelToExpUtil.getMissingExpToLevelUp(getCurrentPlayerLevel(), getTotalExperienceEarned());
	}

	/**
	 * Get the missing experience to level up in percentage (useful for
	 * ProgressBars).
	 *
	 * @return Missing experience to level up in percentage.
	 * @throws InvalidExperienceException
	 *             Thrown if the value of experience isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getMissingExpToLevelUpInPercentage(int, int)}.
	 * @throws InvalidLevelException
	 *             Thrown if the value of level isn't valid, threw from
	 *             {@link com.pokegoapi.util.LevelToExpUtil#getMissingExpToLevelUpInPercentage(int, int)}.
	 */
	public int getMissingExpToLevelUpInPercentage() throws InvalidLevelException, InvalidExperienceException {
		return LevelToExpUtil.getMissingExpToLevelUpInPercentage(getCurrentPlayerLevel(), getTotalExperienceEarned());
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
	public int getPokemonsEvolved() {
		return proto.getEvolutions();
	}

	/**
	 * Get the amount of pokestops visited.
	 *
	 * @return Amount of pokestops visited.
	 */
	public int getVisitedPokestops() {
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
	public int getBattlesVSOpponentTeamWon() {
		return proto.getBattleAttackWon();
	}

	/**
	 * Get the total amount of battles undertaken versus an opponent team's gym.
	 *
	 * @return Total amount of battles undertaken versus an opponent team's gym.
	 */
	public int getBattlesVSOpponentTeamTot() {
		return proto.getBattleAttackTotal();
	}

	/**
	 * Get the amount of battles won as defender of a gym.
	 *
	 * @return Amount of battles won as defender of a gym.
	 */
	public int getBattlesDefendedWon() {
		return proto.getBattleDefendedWon();
	}

	/**
	 * Get the amount of battles won versus your team's gym.
	 *
	 * @return Amount of battles won versus your team's gym.
	 */
	public int getBattlesVSSameTeamWon() {
		return proto.getBattleTrainingWon();
	}

	/**
	 * Get the total amount of battles undertaken versus your team's gym.
	 *
	 * @return Total amount of battles undertaken versus your team's gym.
	 */
	public int getBattlesVSSameTeamTot() {
		return proto.getBattleTrainingTotal();
	}

	/**
	 * Get the total prestige raised in gyms.
	 *
	 * @return Total prestige raised in gyms.
	 */
	public int getGymPrestigeRaisedTotal() {
		return proto.getPrestigeRaisedTotal();
	}

	/**
	 * Get the total prestige dropped in gyms.
	 *
	 * @return Total prestige dropped in gyms.
	 */
	public int getGymPrestigeDroppedTotal() {
		return proto.getPrestigeDroppedTotal();
	}

	/**
	 * Get the amount of pokemons deployed in gyms.
	 *
	 * @return Amount of pokemons deployed in gyms.
	 */
	public int getPokemonsDeployedInGyms() {
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

	/**
	 * Get the proto
	 * 
	 * @return proto The proto
	 */
	public PlayerStatsOuterClass.PlayerStats getProto() {
		return proto;
	}
}