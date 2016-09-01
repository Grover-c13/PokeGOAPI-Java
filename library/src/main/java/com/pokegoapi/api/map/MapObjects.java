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

import POGOProtos.Map.Pokemon.WildPokemonOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.fort.Pokestop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass.NearbyPokemon;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass.WildPokemon;
import POGOProtos.Map.SpawnPointOuterClass.SpawnPoint;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import lombok.Getter;
import lombok.ToString;

@ToString
public class MapObjects {

	@Getter
	private final Collection<NearbyPokemon> nearbyPokemons = Collections.synchronizedCollection(
			new ArrayList<NearbyPokemon>()
	);
	@Getter
	private final Collection<MapPokemon> catchablePokemons = Collections.synchronizedCollection(
			new ArrayList<MapPokemon>()
	);
	@Getter
	private final Collection<WildPokemon> wildPokemons = Collections.synchronizedCollection(
			new ArrayList<WildPokemon>()
	);
	@Getter
	private final Collection<SpawnPoint> decimatedSpawnPoints = Collections.synchronizedCollection(
			new ArrayList<SpawnPoint>()
	);
	@Getter
	private final Collection<SpawnPoint> spawnPoints = Collections.synchronizedCollection(
			new ArrayList<SpawnPoint>()
	);
	@Getter
	private final Collection<FortData> gyms = Collections.synchronizedCollection(
			new ArrayList<FortData>()
	);
	@Getter
	private final Collection<Pokestop> pokestops = Collections.synchronizedCollection(
			new ArrayList<Pokestop>()
	);
	boolean complete = false;
	private final PokemonGo api;

	/**
	 * Instantiates a new Map objects.
	 *
	 * @param api the api
	 */
	public MapObjects(PokemonGo api) {
		this.api = api;
	}

	/**
	 * Add nearby pokemons.
	 *
	 * @param nearbyPokemons the nearby pokemons
	 */
	public void addNearbyPokemons(Collection<NearbyPokemon> nearbyPokemons) {
		if (nearbyPokemons == null || nearbyPokemons.isEmpty()) {
			return;
		}
		complete = true;
		this.nearbyPokemons.addAll(nearbyPokemons);
	}

	/**
	 * Add catchable pokemons.
	 *
	 * @param catchablePokemons the catchable pokemons
	 */
	public void addCatchablePokemons(Collection<MapPokemon> catchablePokemons) {
		if (catchablePokemons == null || catchablePokemons.isEmpty()) {
			return;
		}
		complete = true;
		this.catchablePokemons.addAll(catchablePokemons);
	}

	/**
	 * Add wild pokemons.
	 *
	 * @param wildPokemons the wild pokemons
	 */
	public void addWildPokemons(Collection<WildPokemon> wildPokemons) {
		if (wildPokemons == null || wildPokemons.isEmpty()) {
			return;
		}
		complete = true;
		this.wildPokemons.addAll(wildPokemons);
	}

	/**
	 * Add decimated spawn points.
	 *
	 * @param decimatedSpawnPoints the decimated spawn points
	 */
	public void addDecimatedSpawnPoints(Collection<SpawnPoint> decimatedSpawnPoints) {
		if (decimatedSpawnPoints == null || decimatedSpawnPoints.isEmpty()) {
			return;
		}
		complete = true;
		this.decimatedSpawnPoints.addAll(decimatedSpawnPoints);
	}

	/**
	 * Add spawn points.
	 *
	 * @param spawnPoints the spawn points
	 */
	public void addSpawnPoints(Collection<SpawnPoint> spawnPoints) {
		if (spawnPoints == null || spawnPoints.isEmpty()) {
			return;
		}
		complete = true;
		this.spawnPoints.addAll(spawnPoints);
	}

	/**
	 * Add gyms.
	 *
	 * @param gyms the gyms
	 */
	public void addGyms(Collection<FortData> gyms) {
		if (gyms == null || gyms.isEmpty()) {
			return;
		}
		complete = true;
		this.gyms.addAll(gyms);
	}

	/**
	 * Add pokestops.
	 *
	 * @param pokestops the pokestops
	 */
	public void addPokestops(Collection<FortData> pokestops) {
		if (pokestops == null || pokestops.isEmpty()) {
			return;
		}
		complete = true;
		for (FortData pokestop : pokestops) {
			this.pokestops.add(new Pokestop(api, pokestop));
		}
	}

	/**
	 * Returns whether any data was returned. When a user requests too many cells/wrong cell level/cells too far away
	 * from the users location, the server returns empty MapCells.
	 *
	 * @return whether or not the return returned any data at all;
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Get all the catchable pokemon on the current map
	 *
	 * @return a set of catchable pokemon
     */
	public Set<CatchablePokemon> getAllCatchablePokemons() {
		Set<CatchablePokemon> catchablePokemons = new HashSet<>();
		for (MapPokemon mapPokemon : getCatchablePokemons()) {
			catchablePokemons.add(new CatchablePokemon(api, mapPokemon));
		}

		for (WildPokemonOuterClass.WildPokemon wildPokemon : getWildPokemons()) {
			catchablePokemons.add(new CatchablePokemon(api, wildPokemon));
		}

		for (Pokestop pokestop : getPokestops()) {
			if (pokestop.inRangeForLuredPokemon() && pokestop.getFortData().hasLureInfo()) {
				catchablePokemons.add(new CatchablePokemon(api, pokestop.getFortData()));
			}
		}
		return catchablePokemons;
	}
}