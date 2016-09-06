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
import POGOProtos.Map.Fort.FortTypeOuterClass.FortType;
import POGOProtos.Map.MapCellOuterClass;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass.WildPokemon;
import POGOProtos.Map.SpawnPointOuterClass.SpawnPoint;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass.GetMapObjectsResponse;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.NearbyPokemon;
import com.pokegoapi.api.settings.Settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MapObjects {
	private final Networking networking;
	private final Location location;
	private final Inventories inventories;
	private final Settings settings;
	private final MapOnSubscribe<NearbyPokemon> nearbyPokemonMapOnSubscribe = new MapOnSubscribe<>();
	private final Map<String, NearbyPokemon> nearbyPokemonMap = new ConcurrentHashMap<>();
	private final MapOnSubscribe<CatchablePokemon> catchablePokemonMapOnSubscribe = new MapOnSubscribe<>();
	private final Map<String, CatchablePokemon> catchablePokemonMap = new ConcurrentHashMap<>();
	private final MapOnSubscribe<Point> decimatedSpawnPointMapOnSubscribe = new MapOnSubscribe<>();
	private final Map<String, Point> decimatedSpawnPointMap = new ConcurrentHashMap<>();
	private final MapOnSubscribe<Point> spawnPointMapOnSubscribe = new MapOnSubscribe<>();
	private final Map<String, Point> spawnPointMap = new ConcurrentHashMap<>();
	private final MapOnSubscribe<Gym> gymMapOnSubscribe = new MapOnSubscribe<>();
	private final Map<String, Gym> gymMap = new ConcurrentHashMap<>();
	private final MapOnSubscribe<Pokestop> pokestopOnSubscribe = new MapOnSubscribe<>();
	private final Map<String, Pokestop> pokestopMap = new ConcurrentHashMap<>();

	private final GetKey<NearbyPokemon> nearbyPokemonGetKey = new GetKey<NearbyPokemon>() {
		@Override
		public String getKey(NearbyPokemon obj) {
			return Long.toString(obj.getEncounterId());
		}
	};
	private final GetKey<CatchablePokemon> catchablePokemonGetKey = new GetKey<CatchablePokemon>() {
		@Override
		public String getKey(CatchablePokemon obj) {
			return Long.toString(obj.getEncounterId());
		}
	};
	private final GetKey<Point> spawnPointGetKey = new GetKey<Point>() {
		@Override
		public String getKey(Point obj) {
			long latitidue = (long) obj.getLatitude() * 100000L;
			long longitude = (long) obj.getLatitude() * 10000000000L;
			return Long.toString(latitidue + longitude);
		}
	};
	private final GetKey<Gym> fortDataGetKey = new GetKey<Gym>() {
		@Override
		public String getKey(Gym obj) {
			return obj.getId();
		}
	};
	private final GetKey<Pokestop> pokestopGetKey = new GetKey<Pokestop>() {
		@Override
		public String getKey(Pokestop obj) {
			return obj.getId();
		}
	};

	/**
	 * Instantiates a new Map objects.
	 *
	 * @param networking            For doing network requests
	 * @param location              Holds the current position
	 * @param inventories           Inventories of the player
	 * @param settings              Settings
	 * @param getMapObjectsResponse Initial response object to fill the map objects
	 */
	MapObjects(Networking networking, Location location, Inventories inventories, Settings settings,
			GetMapObjectsResponse getMapObjectsResponse) {
		this.networking = networking;
		this.location = location;
		this.inventories = inventories;
		this.settings = settings;
		update(getMapObjectsResponse);
	}

	private static <T> void add(List<T> newList, Map<String, T> map, GetKey<T> getKey, MapOnSubscribe<T> subscribe) {
		List<String> existingKeys = new ArrayList<>(newList.size());
		List<T> newItems = new ArrayList<>(newList.size());
		for (T obj : newList) {
			String key = getKey.getKey(obj);
			map.put(key, obj);
			if (!existingKeys.contains(key)) {
				newItems.add(obj);
			}
			existingKeys.add(key);
		}
		map.keySet().retainAll(existingKeys);
		for (T newObj : newItems) {
			subscribe.onNext(newObj);
		}
	}

	final void update(GetMapObjectsResponse response) {
		List<NearbyPokemon> nearbyPokemons = new LinkedList<>();
		List<CatchablePokemon> catchablePokemons = new LinkedList<>();
		List<Point> decimatedSpawnPoints = new LinkedList<>();
		List<Point> spawnPoints = new LinkedList<>();
		List<Gym> gyms = new LinkedList<>();
		List<Pokestop> pokestops = new LinkedList<>();

		for (MapCellOuterClass.MapCell mapCell : response.getMapCellsList()) {
			for (NearbyPokemonOuterClass.NearbyPokemon nearbyPokemon : mapCell.getNearbyPokemonsList()) {
				nearbyPokemons.add(new NearbyPokemon(nearbyPokemon));
			}
			for (MapPokemonOuterClass.MapPokemon mapPokemon : mapCell.getCatchablePokemonsList()) {
				catchablePokemons.add(new CatchablePokemon(networking, location, inventories, mapPokemon));
			}
			for (WildPokemon mapPokemon : mapCell.getWildPokemonsList()) {
				catchablePokemons.add(new CatchablePokemon(networking, location, inventories, mapPokemon));
			}
			for (SpawnPoint spawnPoint : mapCell.getDecimatedSpawnPointsList()) {
				decimatedSpawnPoints.add(new Point(spawnPoint));
			}
			for (SpawnPoint spawnPoint : mapCell.getSpawnPointsList()) {
				spawnPoints.add(new Point(spawnPoint));
			}

			java.util.Map<FortType, List<FortData>> groupedForts = Stream.of(mapCell.getFortsList())
					.collect(Collectors.groupingBy(new Function<FortData, FortType>() {
						@Override
						public FortType apply(FortData fortData) {
							return fortData.getType();
						}
					}));
			if (groupedForts.containsKey(FortType.GYM)) {
				for (FortData fortData : groupedForts.get(FortType.GYM)) {
					gyms.add(new Gym(networking, location, fortData));
				}
			}
			if (groupedForts.containsKey(FortType.CHECKPOINT)) {
				for (FortData fortData : groupedForts.get(FortType.CHECKPOINT)) {
					pokestops.add(new Pokestop(networking, location, settings, fortData));
				}
			}
			for (Pokestop pokestop : pokestops) {
				if (pokestop.inRangeForLuredPokemon() && pokestop.getFortData().hasLureInfo()) {
					catchablePokemons
							.add(new CatchablePokemon(networking, location, inventories, pokestop.getFortData()));
				}
			}
		}
		add(nearbyPokemons, nearbyPokemonMap, nearbyPokemonGetKey, nearbyPokemonMapOnSubscribe);
		add(catchablePokemons, catchablePokemonMap, catchablePokemonGetKey, catchablePokemonMapOnSubscribe);
		add(decimatedSpawnPoints, decimatedSpawnPointMap, spawnPointGetKey, decimatedSpawnPointMapOnSubscribe);
		add(spawnPoints, spawnPointMap, spawnPointGetKey, spawnPointMapOnSubscribe);
		add(gyms, gymMap, fortDataGetKey, gymMapOnSubscribe);
		add(pokestops, pokestopMap, pokestopGetKey, pokestopOnSubscribe);
	}

	Collection<CatchablePokemon> getCatchablePokemons() {
		return catchablePokemonMap.values();
	}

	Collection<NearbyPokemon> getNearbyPokemons() {
		return nearbyPokemonMap.values();
	}

	Collection<Point> getDecimatedSpawnPoints() {
		return decimatedSpawnPointMap.values();
	}

	Collection<Point> getSpawnPoints() {
		return spawnPointMap.values();
	}

	Collection<Gym> getGyms() {
		return gymMap.values();
	}

	Collection<Pokestop> getPokestops() {
		return pokestopMap.values();
	}

	MapOnSubscribe<NearbyPokemon> getNearbyPokemonMapOnSubscribe() {
		return nearbyPokemonMapOnSubscribe;
	}

	MapOnSubscribe<CatchablePokemon> getCatchablePokemonMapOnSubscribe() {
		return catchablePokemonMapOnSubscribe;
	}

	MapOnSubscribe<Point> getDecimatedSpawnPointMapOnSubscribe() {
		return decimatedSpawnPointMapOnSubscribe;
	}

	MapOnSubscribe<Point> getSpawnPointMapOnSubscribe() {
		return spawnPointMapOnSubscribe;
	}

	MapOnSubscribe<Gym> getGymMapOnSubscribe() {
		return gymMapOnSubscribe;
	}

	MapOnSubscribe<Pokestop> getPokestopOnSubscribe() {
		return pokestopOnSubscribe;
	}

	private interface GetKey<T> {
		String getKey(T obj);
	}
}