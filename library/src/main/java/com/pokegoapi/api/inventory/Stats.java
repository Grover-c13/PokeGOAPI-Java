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

/**
 * Stats class
 *
 * @deprecated Please use {@link com.pokegoapi.api.player.Stats} instead.
 */
@Deprecated
public class Stats {

	@Deprecated
	private PlayerStatsOuterClass.PlayerStats proto;

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#Stats(POGOProtos.Data.Player.PlayerStatsOuterClass.PlayerStats)}
	 *             instead.
	 */
	@Deprecated
	public Stats(PlayerStatsOuterClass.PlayerStats proto) {
		this.proto = proto;
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getCurrentPlayerLevel()}
	 *             instead.
	 */
	@Deprecated
	public int getLevel() {
		return proto.getLevel();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getTotalExperienceEarned()}
	 *             instead.
	 */
	@Deprecated
	public long getExperience() {
		return proto.getExperience();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getTotExpToReachPreviousLevel()}
	 *             instead.
	 */
	@Deprecated
	public long getPrevLevelXp() {
		return proto.getPrevLevelXp();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getTotExpToReachNextLevel()}
	 *             instead.
	 */
	@Deprecated
	public long getNextLevelXp() {
		return proto.getNextLevelXp();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getKmWalked()} instead.
	 */
	@Deprecated
	public float getKmWalked() {
		return proto.getKmWalked();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getPokemonsEncountered()}
	 *             instead.
	 */
	@Deprecated
	public int getPokemonsEncountered() {
		return proto.getPokemonsEncountered();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getUniquePokedexEntries()}
	 *             instead.
	 */
	@Deprecated
	public int getUniquePokedexEntries() {
		return proto.getUniquePokedexEntries();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getPokemonsCaptured()}
	 *             instead.
	 */
	@Deprecated
	public int getPokemonsCaptured() {
		return proto.getPokemonsCaptured();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getPokemonsEvolved()}
	 *             instead.
	 */
	@Deprecated
	public int getEvolutions() {
		return proto.getEvolutions();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getVisitedPokestops()}
	 *             instead.
	 */
	@Deprecated
	public int getPokeStopVisits() {
		return proto.getPokeStopVisits();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getPokeballsThrown()}
	 *             instead.
	 */
	@Deprecated
	public int getPokeballsThrown() {
		return proto.getPokeballsThrown();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getEggsHatched()}
	 *             instead.
	 */
	@Deprecated
	public int getEggsHatched() {
		return proto.getEggsHatched();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getBigMagikarpCaught()}
	 *             instead.
	 */
	@Deprecated
	public int getBigMagikarpCaught() {
		return proto.getBigMagikarpCaught();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getBattlesVSOpponentTeamWon()}
	 *             instead.
	 */
	@Deprecated
	public int getBattleAttackWon() {
		return proto.getBattleAttackWon();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getBattlesVSOpponentTeam()}
	 *             instead.
	 */
	@Deprecated
	public int getBattleAttackTotal() {
		return proto.getBattleAttackTotal();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getBattlesDefendedWon()}
	 *             instead.
	 */
	@Deprecated
	public int getBattleDefendedWon() {
		return proto.getBattleDefendedWon();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getBattlesVSSameTeamWon()}
	 *             instead.
	 */
	@Deprecated
	public int getBattleTrainingWon() {
		return proto.getBattleTrainingWon();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getBattlesVSSameTeam()}
	 *             instead.
	 */
	@Deprecated
	public int getBattleTrainingTotal() {
		return proto.getBattleTrainingTotal();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getGymPrestigeRaisedTotal()}
	 *             instead.
	 */
	@Deprecated
	public int getPrestigeRaisedTotal() {
		return proto.getPrestigeRaisedTotal();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getGymPrestigeDroppedTotal()}
	 *             instead.
	 */
	@Deprecated
	public int getPrestigeDroppedTotal() {
		return proto.getPrestigeDroppedTotal();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getPokemonsDeployedInGyms()}
	 *             instead.
	 */
	@Deprecated
	public int getPokemonDeployed() {
		return proto.getPokemonDeployed();
	}

	/**
	 * @deprecated Please use
	 *             {@link com.pokegoapi.api.player.Stats#getSmallRattataCaught()}
	 *             instead.
	 */
	@Deprecated
	public int getSmallRattataCaught() {
		return proto.getSmallRattataCaught();
	}
}