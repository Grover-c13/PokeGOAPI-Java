/*
 *	 This program is free software: you can redistribute it and/or modify
 *	 it under the terms of the GNU General Public License as published by
 *	 the Free Software Foundation, either version 3 of the License, or
 *	 (at your option) any later version.
 *
 *	 This program is distributed in the hope that it will be useful,
 *	 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	 GNU General Public License for more details.
 *
 *	 You should have received a copy of the GNU General Public License
 *	 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.map.fort;

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Data.Raid.RaidInfoOuterClass.RaidInfo;
import POGOProtos.Enums.RaidLevelOuterClass.RaidLevel;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.gym.Gym;
import lombok.Getter;

public class Raid {
	private final PokemonGo api;
	@Getter
	private final Gym gym;
	@Getter
	private final RaidInfo raidInfo;

	/**
	 * Raid Constructor.
	 *
	 * @param api set api
	 * @param gym set gym
	 * @param raidInfo set raidInfo
	 */
	public Raid(PokemonGo api, Gym gym, RaidInfo raidInfo) {
		this.api = api;
		this.gym = gym;
		this.raidInfo = raidInfo;
	}

	public long getRaidSeed() {
		return raidInfo.getRaidSeed();
	}

	public long getRaidSpawnMs() {
		return raidInfo.getRaidSpawnMs();
	}

	public long getRaidBattleMs() {
		return raidInfo.getRaidBattleMs();
	}

	public long getRaidEndMs() {
		return raidInfo.getRaidEndMs();
	}

	public boolean hasRaidPokemon() {
		return raidInfo.hasRaidPokemon();
	}

	public PokemonData getRaidPokemon() {
		return raidInfo.getRaidPokemon();
	}

	public RaidLevel getRaidLevel() {
		return raidInfo.getRaidLevel();
	}

	public boolean getComplete() {
		return raidInfo.getComplete();
	}

	public boolean getIsExclusive() {
		return raidInfo.getIsExclusive();
	}

	public String getId() {
		return gym.getId();
	}

	public double getLatitude() {
		return gym.getLatitude();
	}

	public double getLongitude() {
		return gym.getLongitude();
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Raid && ((Raid) obj).getId().equals(getId());
	}
}
