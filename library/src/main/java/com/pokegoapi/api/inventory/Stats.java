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

	public int getLevel() {
		return proto.getLevel();
	}

	public long getExperience() {
		return proto.getExperience();
	}

	public long getPrevLevelXp() {
		return proto.getPrevLevelXp();
	}

	public long getNextLevelXp() {
		return proto.getNextLevelXp();
	}

	public float getKmWalked() {
		return proto.getKmWalked();
	}

	public int getPokemonsEncountered() {
		return proto.getPokemonsEncountered();
	}

	public int getUniquePokedexEntries() {
		return proto.getUniquePokedexEntries();
	}

	public int getPokemonsCaptured() {
		return proto.getPokemonsCaptured();
	}

	public int getEvolutions() {
		return proto.getEvolutions();
	}

	public int getPokeStopVisits() {
		return proto.getPokeStopVisits();
	}

	public int getPokeballsThrown() {
		return proto.getPokeballsThrown();
	}

	public int getEggsHatched() {
		return proto.getEggsHatched();
	}

	public int getBigMagikarpCaught() {
		return proto.getBigMagikarpCaught();
	}

	public int getBattleAttackWon() {
		return proto.getBattleAttackWon();
	}

	public int getBattleAttackTotal() {
		return proto.getBattleAttackTotal();
	}

	public int getBattleDefendedWon() {
		return proto.getBattleDefendedWon();
	}

	public int getBattleTrainingWon() {
		return proto.getBattleTrainingWon();
	}

	public int getBattleTrainingTotal() {
		return proto.getBattleTrainingTotal();
	}

	public int getPrestigeRaisedTotal() {
		return proto.getPrestigeRaisedTotal();
	}

	public int getPrestigeDroppedTotal() {
		return proto.getPrestigeDroppedTotal();
	}

	public int getPokemonDeployed() {
		return proto.getPokemonDeployed();
	}

	public int getSmallRattataCaught() {
		return proto.getSmallRattataCaught();
	}
}
