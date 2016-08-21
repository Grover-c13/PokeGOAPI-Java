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

import POGOProtos.Data.Player.PlayerStatsOuterClass;
import lombok.Getter;

/**
 * Stats info
 * 
 * @author gionata-bisciari
 *
 */
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
	 */
	public int getTotExpToReachPreviousLevel() {
		return LevelToExpUtils.getTotExpToReachPrevLevelOf(getCurrentPlayerLevel());
	}

	/**
	 * Get the total experience required to reach the current level.
	 *
	 * @return Total experience required to reach the current level.
	 */
	public int getTotExpToReachCurrentLevel() {
		return LevelToExpUtils.getTotExpToReachLevel(getCurrentPlayerLevel());
	}

	/**
	 * Get the total experience required to reach the next level.
	 *
	 * @return Total experience required to reach the next level.
	 */
	public int getTotExpToReachNextLevel() {
		return LevelToExpUtils.getTotExpToReachNextLevelOf(getCurrentPlayerLevel());
	}

	/**
	 * Get the experience interval from current level to next level.
	 *
	 * @return Experience interval from current level to next.
	 */
	public int getExpIntervalFromCurrentLevelToNext() {
		return LevelToExpUtils.getExpIntervalFromLevelToNext(getCurrentPlayerLevel());
	}

	/**
	 * Get the experience earned in current level.
	 *
	 * @return Experience earned in current level.
	 */
	public int getExpEarnedInCurrentLevel() {
		return LevelToExpUtils.getExpEarnedInCurrentLevel(getCurrentPlayerLevel(), getTotalExperienceEarned());
	}

	/**
	 * Get the experience earned in current level in percentage (usefull for
	 * ProgressBars).
	 *
	 * @return Experience earned in current level in percentage.
	 */
	public int getExpEarnedInCurrentLevelInPercentage() {
		return LevelToExpUtils.getExpEarnedInCurrentLevelInPercentage(getCurrentPlayerLevel(),
				getTotalExperienceEarned());
	}

	/**
	 * Get the missing experience to level up.
	 *
	 * @return Missing experience to level up.
	 */
	public int getMissingExpToLevelUp() {
		return LevelToExpUtils.getMissingExpToLevelUp(getCurrentPlayerLevel(), getTotalExperienceEarned());
	}

	/**
	 * Get the missing experience to level up in percentage (usefull for
	 * ProgressBars).
	 *
	 * @return Missing experience to level up in percentage.
	 */
	public int getMissingExpToLevelUpInPercentage() {
		return LevelToExpUtils.getMissingExpToLevelUpInPercentage(getCurrentPlayerLevel(), getTotalExperienceEarned());
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
	public int getBattlesVSOpponentTeam() {
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
	public int getBattlesVSSameTeam() {
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
}
