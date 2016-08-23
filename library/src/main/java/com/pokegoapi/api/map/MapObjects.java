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
import POGOProtos.Map.Fort.FortTypeOuterClass;
import POGOProtos.Map.MapCellOuterClass;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass.NearbyPokemon;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass.WildPokemon;
import POGOProtos.Map.SpawnPointOuterClass.SpawnPoint;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass.GetMapObjectsResponse;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.settings.Settings;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MapObjects {
	private final Networking networking;
	private final Location location;
	private final Settings settings;
	private final Map<Long, NearbyPokemon> nearbyPokemonMap = new ConcurrentHashMap<>();
	private final Map<Long, MapPokemon> catchablePokemonMap = new ConcurrentHashMap<>();
	private final Map<Long, WildPokemon> wildPokemonMap = new ConcurrentHashMap<>();
	private final Map<Long, SpawnPoint> decimatedSpawnPointMap = new ConcurrentHashMap<>();
	private final Map<Long, SpawnPoint> spawnPointMap = new ConcurrentHashMap<>();
	private final Map<String, FortData> gymMap = new ConcurrentHashMap<>();
	private final Map<String, Pokestop> pokestopMap = new ConcurrentHashMap<>();

	/**
	 * Instantiates a new Map objects.
	 *
	 */
	MapObjects(Networking networking, Location location, Settings settings, GetMapObjectsResponse getMapObjectsResponse) {
		this.networking = networking;
		this.location = location;
		this.settings = settings;
		update(getMapObjectsResponse);
	}

	final void update(GetMapObjectsResponse response) {
		List<NearbyPokemon> nearbyPokemons = new LinkedList<>();
		List<MapPokemon> catchablePokemons = new LinkedList<>();
		List<WildPokemon> wildPokemons = new LinkedList<>();
		List<SpawnPoint> decimatedSpawnPoints = new LinkedList<>();
		List<SpawnPoint> spawnPoints = new LinkedList<>();
		List<FortData> gyms = new LinkedList<>();
		List<FortData> pokestops = new LinkedList<>();

		for (MapCellOuterClass.MapCell mapCell : response.getMapCellsList()) {
			nearbyPokemons.addAll(mapCell.getNearbyPokemonsList());
			catchablePokemons.addAll(mapCell.getCatchablePokemonsList());
			wildPokemons.addAll(mapCell.getWildPokemonsList());
			decimatedSpawnPoints.addAll(mapCell.getDecimatedSpawnPointsList());
			spawnPoints.addAll(mapCell.getSpawnPointsList());

			java.util.Map<FortTypeOuterClass.FortType, List<FortData>> groupedForts = Stream.of(mapCell.getFortsList())
					.collect(Collectors.groupingBy(new Function<FortData, FortTypeOuterClass.FortType>() {
						@Override
						public FortTypeOuterClass.FortType apply(FortData fortData) {
							return fortData.getType();
						}
					}));
			gyms.addAll(groupedForts.get(FortTypeOuterClass.FortType.GYM));
			pokestops.addAll(groupedForts.get(FortTypeOuterClass.FortType.CHECKPOINT));
		}
		List<Long> ids = new LinkedList<>();
		for (NearbyPokemon pokemon : nearbyPokemons) {
			nearbyPokemonMap.put(pokemon.getEncounterId(), pokemon);
			ids.add(pokemon.getEncounterId());
		}
		nearbyPokemonMap.keySet().retainAll(ids);
		ids.clear();
		for (MapPokemon pokemon : catchablePokemons) {
			catchablePokemonMap.put(pokemon.getEncounterId(), pokemon);
			ids.add(pokemon.getEncounterId());
		}
		catchablePokemonMap.keySet().retainAll(ids);
		ids.clear();
		for (WildPokemon pokemon : wildPokemons) {
			wildPokemonMap.put(pokemon.getEncounterId(), pokemon);
			ids.add(pokemon.getEncounterId());
		}
		wildPokemonMap.keySet().retainAll(ids);
		ids.clear();
		for (SpawnPoint spawnPoint : decimatedSpawnPoints) {
			decimatedSpawnPointMap.put(getId(spawnPoint), spawnPoint);
			ids.add(getId(spawnPoint));
		}
		decimatedSpawnPointMap.keySet().retainAll(ids);
		ids.clear();
		for (SpawnPoint spawnPoint : spawnPoints) {
			spawnPointMap.put(getId(spawnPoint), spawnPoint);
			ids.add(getId(spawnPoint));
		}
		spawnPointMap.keySet().retainAll(ids);
		List<String> gymIds = new LinkedList<>();
		for (FortData gym : gyms) {
			gymMap.put(gym.getId(), gym);
			gymIds.add(gym.getId());
		}
		gymMap.keySet().retainAll(gymIds);
		gymIds.clear();
		for (FortData gym : pokestops) {
			pokestopMap.put(gym.getId(), new Pokestop(networking, location, settings, gym));
			gymIds.add(gym.getId());
		}
		pokestopMap.keySet().retainAll(gymIds);
		gymIds.clear();
	}

	private static long getId(SpawnPoint spawnPoint) {
		long latitidue = (long)spawnPoint.getLatitude() * 100000L;
		long longitude = (long)spawnPoint.getLatitude() * 10000000000L;
		return latitidue + longitude;
	}

	Collection<NearbyPokemon> getNearbyPokemons() {
		return nearbyPokemonMap.values();
	}

	Collection<MapPokemon> getCatchablePokemons() {
		return catchablePokemonMap.values();
	}

	Collection<WildPokemon> getWildPokemons() {
		return wildPokemonMap.values();
	}

	Collection<SpawnPoint> getDecimatedSpawnPoints() {
		return decimatedSpawnPointMap.values();
	}

	Collection<SpawnPoint> getSpawnPoints() {
		return spawnPointMap.values();
	}

	Collection<FortData> getGyms() {
		return gymMap.values();
	}

	Collection<Pokestop> getPokestops() {
		return pokestopMap.values();
	}
}