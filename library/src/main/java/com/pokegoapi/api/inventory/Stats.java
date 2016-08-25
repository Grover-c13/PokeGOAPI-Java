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
import POGOProtos.Data.Player.PlayerStatsOuterClass.PlayerStats;

/**
 * Stats class
 *
 * @deprecated Please use {@link com.pokegoapi.api.player.stats.Stats} instead.
 */
@Deprecated
public class Stats {

	@Deprecated
	private PlayerStatsOuterClass.PlayerStats proto;

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#Stats(PlayerStatsOuterClass.PlayerStats)}
	 *             class instead.
	 * @param proto
	 *            Proto
	 */
	@Deprecated
	public Stats(PlayerStatsOuterClass.PlayerStats proto) {
		this.proto = proto;
	}

	/**
	 * Get the current level of the player.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getCurrentPlayerLevel()}
	 *             instead.
	 * @return Current level of the player.
	 */
	@Deprecated
	public int getLevel() {
		return proto.getLevel();
	}

	/**
	 * Get the current total amount of experience earned.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getTotalExperienceEarned()}
	 *             instead.
	 * @return Current total amount of experience earned.
	 */
	@Deprecated
	public long getExperience() {
		return proto.getExperience();
	}

	/**
	 * Get the total experience required to reach the previous level.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getTotExpToReachPreviousLevel()}
	 *             instead.
	 * @return Total experience required to reach the previous level.
	 */
	@Deprecated
	public long getPrevLevelXp() {
		return proto.getPrevLevelXp();
	}

	/**
	 * Get the total experience required to reach the next level.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getTotExpToReachNextLevel()}
	 *             instead.
	 * @return Total experience required to reach the next level.
	 */
	@Deprecated
	public long getNextLevelXp() {
		return proto.getNextLevelXp();
	}

	/**
	 * Get the amount of kilometers walked.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getKmWalked()}
	 *             instead.
	 * @return Amount of kilometers walked.
	 */
	@Deprecated
	public float getKmWalked() {
		return proto.getKmWalked();
	}

	/**
	 * Get the amount of pokemons encountered.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getPokemonsEncountered()}
	 *             instead.
	 * @return Amount of pokemons encountered.
	 */
	@Deprecated
	public int getPokemonsEncountered() {
		return proto.getPokemonsEncountered();
	}

	/**
	 * Get the amount of pokemons registered in pokedex.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getPokemonsRegisteredInPokedex()}
	 *             instead.
	 * @return Amount of pokemons registered in pokedex.
	 */
	@Deprecated
	public int getUniquePokedexEntries() {
		return proto.getUniquePokedexEntries();
	}

	/**
	 * Get the amount of pokemons captured.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getPokemonsCaptured()}
	 *             instead.
	 * @return Amount of pokemons captured.
	 */
	@Deprecated
	public int getPokemonsCaptured() {
		return proto.getPokemonsCaptured();
	}

	/**
	 * Get the amount of pokemons evolved.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getPokemonsEvolved()}
	 *             instead.
	 * @return Amount of pokemons evolved.
	 */
	@Deprecated
	public int getEvolutions() {
		return proto.getEvolutions();
	}

	/**
	 * Get the amount of pokestops visited.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getVisitedPokestops()}
	 *             instead.
	 * @return Amount of pokestops visited.
	 */
	@Deprecated
	public int getPokeStopVisits() {
		return proto.getPokeStopVisits();
	}

	/**
	 * Get the amount of pokeballs thrown.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getPokeballsThrown()}
	 *             instead.
	 * @return Amount of pokeballs thrown.
	 */
	@Deprecated
	public int getPokeballsThrown() {
		return proto.getPokeballsThrown();
	}

	/**
	 * Get the amount of eggs hatched.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getEggsHatched()}
	 *             instead.
	 * @return Amount of eggs hatched.
	 */
	@Deprecated
	public int getEggsHatched() {
		return proto.getEggsHatched();
	}

	/**
	 * Get the amount of big magikarps caught.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getBigMagikarpCaught()}
	 *             instead.
	 * @return Amount of big magikarps caught.
	 */
	@Deprecated
	public int getBigMagikarpCaught() {
		return proto.getBigMagikarpCaught();
	}

	/**
	 * Get the amount of battles won versus an opponent team's gym.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getBattlesVSOpponentTeamWon()}
	 *             instead.
	 * @return Amount of battles won versus an opponent team's gym.
	 */
	@Deprecated
	public int getBattleAttackWon() {
		return proto.getBattleAttackWon();
	}

	/**
	 * Get the total amount of battles undertaken versus an opponent team's gym.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getBattlesVSOpponentTeamTot()}
	 *             instead.
	 * @return Total amount of battles undertaken versus an opponent team's gym.
	 */
	@Deprecated
	public int getBattleAttackTotal() {
		return proto.getBattleAttackTotal();
	}

	/**
	 * Get the amount of battles won as defender of a gym.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getBattlesDefendedWon()}
	 *             instead.
	 * @return Amount of battles won as defender of a gym.
	 */
	@Deprecated
	public int getBattleDefendedWon() {
		return proto.getBattleDefendedWon();
	}

	/**
	 * Get the amount of battles won versus your team's gym.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getBattlesVSSameTeamWon()}
	 *             instead.
	 * @return Amount of battles won versus your team's gym.
	 */
	@Deprecated
	public int getBattleTrainingWon() {
		return proto.getBattleTrainingWon();
	}

	/**
	 * Get the total amount of battles undertaken versus your team's gym.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getBattlesVSSameTeamTot()}
	 *             instead.
	 * @return Total amount of battles undertaken versus your team's gym.
	 */
	@Deprecated
	public int getBattleTrainingTotal() {
		return proto.getBattleTrainingTotal();
	}

	/**
	 * Get the total prestige raised in gyms.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getGymPrestigeRaisedTotal()}
	 *             instead.
	 * @return Total prestige raised in gyms.
	 */
	@Deprecated
	public int getPrestigeRaisedTotal() {
		return proto.getPrestigeRaisedTotal();
	}

	/**
	 * Get the total prestige dropped in gyms.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getGymPrestigeDroppedTotal()}
	 *             instead.
	 * @return Total prestige dropped in gyms.
	 */
	@Deprecated
	public int getPrestigeDroppedTotal() {
		return proto.getPrestigeDroppedTotal();
	}

	/**
	 * Get the amount of pokemons deployed in gyms.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getPokemonsDeployedInGyms()}
	 *             instead.
	 * @return Amount of pokemons deployed in gyms.
	 */
	@Deprecated
	public int getPokemonDeployed() {
		return proto.getPokemonDeployed();
	}

	/**
	 * Get the amount of small rattatas caught.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getSmallRattataCaught()}
	 *             instead.
	 * @return Amount of small rattatas caught.
	 */
	@Deprecated
	public int getSmallRattataCaught() {
		return proto.getSmallRattataCaught();
	}

	/**
	 * Get the proto.
	 * 
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.stats.Stats#getProto()}
	 *             instead.
	 * @return The proto.
	 */
	@Deprecated
	public PlayerStats getProto() {
		return this.proto;
	}
}