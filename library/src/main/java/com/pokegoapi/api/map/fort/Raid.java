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

package com.pokegoapi.api.map.fort;

import POGOProtos.Data.Raid.RaidInfoOuterClass.RaidInfo;
import POGOProtos.Map.Fort.FortDataOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.google.common.geometry.S2LatLng;
import lombok.Getter;

public class Raid {

	private final PokemonGo api;
	@Getter
	private final FortDataOuterClass.FortData fortData;
	@Getter
	private long cooldownCompleteTimestampMs;
	@Getter
	private final RaidInfo raidInfo;

	
	public Raid(PokemonGo api, FortDataOuterClass.FortData fortData) {
		this.api = api;
		this.fortData = fortData;
		this.raidInfo = fortData.getRaidInfo();
	}

	
	public double getDistance() {
		S2LatLng pokestop = S2LatLng.fromDegrees(getLatitude(), getLongitude());
		S2LatLng player = S2LatLng.fromDegrees(api.getLatitude(), api.getLongitude());
		return pokestop.getEarthDistance(player);
	}
	
	public long getRaidSeed(){
    	return raidInfo.getRaidSeed();
    }
    public long getRaidSpawnMs(){
    	return raidInfo.getRaidSpawnMs();
    }

    public long getRaidBattleMs(){
    	return raidInfo.getRaidBattleMs();
    }

    public long getRaidEndMs(){
    	return raidInfo.getRaidEndMs();
    }

    public boolean hasRaidPokemon(){
    	return raidInfo.hasRaidPokemon();
    }
    
    public POGOProtos.Data.PokemonDataOuterClass.PokemonData getRaidPokemon(){
    	return raidInfo.getRaidPokemon();
    }

    public POGOProtos.Data.PokemonDataOuterClass.PokemonDataOrBuilder getRaidPokemonOrBuilder(){
    	return raidInfo.getRaidPokemonOrBuilder();
    }

    public int getRaidLevelValue(){
    	return raidInfo.getRaidLevelValue();
    }
    
    public POGOProtos.Enums.RaidLevelOuterClass.RaidLevel getRaidLevel(){
    	return raidInfo.getRaidLevel();
    }

    public boolean getComplete(){
    	return raidInfo.getComplete();
    }

    public boolean getIsExclusive(){
    	return raidInfo.getIsExclusive();
    }

    public String getId() {
		return fortData.getId();
	}

	public double getLatitude() {
		return fortData.getLatitude();
	}

	public double getLongitude() {
		return fortData.getLongitude();
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
