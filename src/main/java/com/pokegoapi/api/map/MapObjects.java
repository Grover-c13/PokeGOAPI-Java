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

package com.pokegoapi.api.map;

import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass;
import POGOProtos.Map.SpawnPointOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.fort.Pokestop;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;

@ToString
public class MapObjects {

	@Getter
	Collection<NearbyPokemonOuterClass.NearbyPokemon> nearbyPokemons = new ArrayList<NearbyPokemonOuterClass.NearbyPokemon>();
	@Getter
	Collection<MapPokemonOuterClass.MapPokemon> catchablePokemons = new ArrayList<MapPokemonOuterClass.MapPokemon>();
	@Getter
	Collection<WildPokemonOuterClass.WildPokemon> wildPokemons = new ArrayList<WildPokemonOuterClass.WildPokemon>();
	@Getter
	Collection<SpawnPointOuterClass.SpawnPoint> decimatedSpawnPoints = new ArrayList<SpawnPointOuterClass.SpawnPoint>();
	@Getter
	Collection<SpawnPointOuterClass.SpawnPoint> spawnPoints = new ArrayList<SpawnPointOuterClass.SpawnPoint>();
	@Getter
	Collection<FortDataOuterClass.FortData> gyms = new ArrayList<FortDataOuterClass.FortData>();
	@Getter
	Collection<Pokestop> pokestops = new ArrayList<>();
	boolean complete = false;
	private PokemonGo api;

	public MapObjects(PokemonGo api) {
		this.api = api;
	}

	public void addNearbyPokemons(Collection<NearbyPokemonOuterClass.NearbyPokemon> nearbyPokemons) {
		if (nearbyPokemons == null || nearbyPokemons.isEmpty()) {
			return;
		}
		complete = true;
		this.nearbyPokemons.addAll(nearbyPokemons);
	}

	public void addCatchablePokemons(Collection<MapPokemonOuterClass.MapPokemon> catchablePokemons) {
		if (catchablePokemons == null || catchablePokemons.isEmpty()) {
			return;
		}
		complete = true;
		this.catchablePokemons.addAll(catchablePokemons);
	}

	public void addWildPokemons(Collection<WildPokemonOuterClass.WildPokemon> wildPokemons) {
		if (wildPokemons == null || wildPokemons.isEmpty()) {
			return;
		}
		complete = true;
		this.wildPokemons.addAll(wildPokemons);
	}

	public void addDecimatedSpawnPoints(Collection<SpawnPointOuterClass.SpawnPoint> decimatedSpawnPoints) {
		if (decimatedSpawnPoints == null || decimatedSpawnPoints.isEmpty()) {
			return;
		}
		complete = true;
		this.decimatedSpawnPoints.addAll(decimatedSpawnPoints);
	}

	public void addSpawnPoints(Collection<SpawnPointOuterClass.SpawnPoint> spawnPoints) {
		if (spawnPoints == null || spawnPoints.isEmpty()) {
			return;
		}
		complete = true;
		this.spawnPoints.addAll(spawnPoints);
	}

	public void addGyms(Collection<FortDataOuterClass.FortData> gyms) {
		if (gyms == null || gyms.isEmpty()) {
			return;
		}
		complete = true;
		this.gyms.addAll(gyms);
	}

	public void addPokestops(Collection<FortDataOuterClass.FortData> pokestops) {
		if (pokestops == null || pokestops.isEmpty()) {
			return;
		}
		complete = true;
		for (FortDataOuterClass.FortData pokestop : pokestops) {
			this.pokestops.add(new Pokestop(api, pokestop));
		}
	}

	/**
	 * Returns whether or not the return returned any data at all; when a user requests too many cells/wrong cell level/cells too far away from the users location,
	 * the server returns empty MapCells
	 */
	public boolean isComplete() {
		return complete;
	}
}