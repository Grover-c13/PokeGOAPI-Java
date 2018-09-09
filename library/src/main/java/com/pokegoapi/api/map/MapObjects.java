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

import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.MapCellOuterClass.MapCell;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass.WildPokemon;
import POGOProtos.Map.SpawnPointOuterClass.SpawnPoint;
import POGOProtos.Networking.Responses.GetIncensePokemonResponseOuterClass.GetIncensePokemonResponse;
import POGOProtos.Networking.Responses.GetIncensePokemonResponseOuterClass.GetIncensePokemonResponse.Result;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.fort.Raid;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.NearbyPokemon;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapObjects {
	private PokemonGo api;

	/**
	 * Creates a MapObjects object
	 *
	 * @param api the api for these MapObjects
	 */
	public MapObjects(PokemonGo api) {
		this.api = api;
	}

	@Getter
	public Set<NearbyPokemon> nearby = new HashSet<>();
	private Set<CatchablePokemon> pokemon = new HashSet<>();
	@Getter
	private Set<Point> spawnpoints = new HashSet<>();
	@Getter
	private Set<Point> decimatedSpawnPoints = new HashSet<>();
	@Getter
	public Set<Pokestop> pokestops = new HashSet<>();
	@Getter
	public Set<Gym> gyms = new HashSet<>();
	@Getter
	private Set<Raid> raids = new HashSet<>();

	/**
	 * Adds the given nearby pokemon to this object
	 *
	 * @param nearby the nearby protos
	 */
	public void addNearby(List<NearbyPokemonOuterClass.NearbyPokemon> nearby) {
		for (NearbyPokemonOuterClass.NearbyPokemon nearbyPokemon : nearby) {
			this.nearby.add(new NearbyPokemon(nearbyPokemon));
		}
	}

	/**
	 * Adds the given pokemon to this object
	 *
	 * @param mapPokemon the map pokemon protos
	 */
	public void addMapPokemon(List<MapPokemon> mapPokemon) {
		for (MapPokemon pokemon : mapPokemon) {
			this.pokemon.add(new CatchablePokemon(api, pokemon));
		}
	}

	/**
	 * Adds the given pokemon to this object
	 *
	 * @param wildPokemon the wild pokemon protos
	 */
	public void addWildPokemon(List<WildPokemon> wildPokemon) {
		for (WildPokemon pokemon : wildPokemon) {
			this.pokemon.add(new CatchablePokemon(api, pokemon));
		}
	}

	/**
	 * Adds the given spawnpoints to this object
	 *
	 * @param spawnPoints the spawnpoint protos
	 */
	public void addSpawnpoints(List<SpawnPoint> spawnPoints) {
		for (SpawnPoint spawnPoint : spawnPoints) {
			this.spawnpoints.add(new Point(spawnPoint));
		}
	}

	/**
	 * Adds the given decimated spawnpoints to this object
	 *
	 * @param spawnPoints the spawnpoint protos
	 */
	public void addDecimatedSpawnpoints(List<SpawnPoint> spawnPoints) {
		for (SpawnPoint spawnPoint : spawnPoints) {
			this.decimatedSpawnPoints.add(new Point(spawnPoint));
		}
	}

	/**
	 * Adds the given forts to this object
	 *
	 * @param forts the fort protos
	 */
	public void addForts(List<FortData> forts) {
		for (FortData fortData : forts) {
			switch (fortData.getType()) {
				case CHECKPOINT:
					this.pokestops.add(new Pokestop(api, fortData));
					break;
				case GYM:
					Gym gym = new Gym(api, fortData);
					if (fortData.hasRaidInfo()) {
						this.raids.add(new Raid(api, gym, fortData.getRaidInfo()));
					}
					this.gyms.add(gym);
					break;
				default:
					break;
			}
			if (fortData.hasLureInfo()) {
				this.pokemon.add(new CatchablePokemon(api, fortData));
			}
		}
	}

	/**
	 * Adds an incense pokemon from the given GetIncensePokemon response
	 *
	 * @param response the response containing the incense pokemon, if any
	 */
	public void addIncensePokemon(GetIncensePokemonResponse response) {
		if (response.getResult() == Result.INCENSE_ENCOUNTER_AVAILABLE) {
			this.pokemon.add(new CatchablePokemon(api, response));
		}
	}

	/**
	 * Adds all the MapObjects from the given MapCell to this object
	 *
	 * @param cell the cell to add
	 */
	public void addCell(MapCell cell) {
		this.addNearby(cell.getNearbyPokemonsList());
		this.addMapPokemon(cell.getCatchablePokemonsList());
		this.addWildPokemon(cell.getWildPokemonsList());
		this.addSpawnpoints(cell.getSpawnPointsList());
		this.addDecimatedSpawnpoints(cell.getDecimatedSpawnPointsList());
		this.addForts(cell.getFortsList());
	}

	/**
	 * @return a set of all visible pokemon on the map
	 */
	public Set<CatchablePokemon> getPokemon() {
		Set<CatchablePokemon> pokemon = new HashSet<>();
		for (CatchablePokemon catchable : this.pokemon) {
			long expirationTime = catchable.expirationTimestampMs;
			if ((expirationTime == -1 || api.currentTimeMillis() < expirationTime) && !catchable.despawned) {
				pokemon.add(catchable);
			}
		}
		return pokemon;
	}

	/**
	 * Gets the pokestop with the requested ID
	 *
	 * @param id the id to search for
	 * @return the pokestop with the requested ID, null if none with that ID are visible
	 */
	public Pokestop getPokestop(String id) {
		for (Pokestop pokestop : pokestops) {
			if (pokestop.getId().equals(id)) {
				return pokestop;
			}
		}
		return null;
	}

	/**
	 * Gets the gym with the requested ID
	 *
	 * @param id the id to search for
	 * @return the gym with the requested ID, null if none with that ID are visible
	 */
	public Gym getGym(String id) {
		for (Gym gym : gyms) {
			if (gym.getId().equals(id)) {
				return gym;
			}
		}
		return null;
	}
}
