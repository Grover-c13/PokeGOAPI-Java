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
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass;
import POGOProtos.Map.SpawnPointOuterClass;
import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass.FortDetailsMessage;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass.GetMapObjectsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass.FortDetailsResponse;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass.GetMapObjectsResponse;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.map.fort.FortDetails;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.NearbyPokemon;
import com.pokegoapi.api.settings.MapSettings;
import com.pokegoapi.api.settings.Settings;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.google.common.geometry.MutableInteger;
import com.pokegoapi.google.common.geometry.S2CellId;
import com.pokegoapi.google.common.geometry.S2LatLng;
import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class Map implements Runnable {
	public static final int CELL_WIDTH = 3;
	private final ExecutorService executorService;
	private final MapSettings mapSettings;
	private final MapObjects mapObjects;
	private final Networking networking;
	private final Location location;
	private final Inventories inventories;

	/**
	 * Instantiates a new Map.
	 *
	 */
	public Map(ExecutorService executorService, Settings settings, Networking networking, Location location,
			   Inventories inventories, GetMapObjectsResponse getMapObjectsResponse)  {
		this.executorService = executorService;
		this.mapSettings = settings.getMapSettings();
		this.networking = networking;
		this.location = location;
		this.inventories = inventories;
		mapObjects = new MapObjects(networking, location, settings, getMapObjectsResponse);
		executorService.execute(this);
	}

	/**
	 * Returns a list of catchable pokemon around the current location.
	 *
	 * @return a List of CatchablePokemon at your current location
	 */
	public List<CatchablePokemon> getCatchablePokemon() {
		Set<CatchablePokemon> catchablePokemons = new HashSet<>();
		for (MapPokemon mapPokemon : mapObjects.getCatchablePokemons()) {
			catchablePokemons.add(new CatchablePokemon(networking, location, inventories, mapPokemon));
		}
		for (WildPokemonOuterClass.WildPokemon wildPokemon : mapObjects.getWildPokemons()) {
			catchablePokemons.add(new CatchablePokemon(networking, location, inventories, wildPokemon));
		}
		for (Pokestop pokestop : mapObjects.getPokestops()) {
			if (pokestop.inRangeForLuredPokemon() && pokestop.getFortData().hasLureInfo()) {
				catchablePokemons.add(new CatchablePokemon(networking, location, inventories, pokestop.getFortData()));
			}
		}
		return new ArrayList<>(catchablePokemons);
	}

	/**
	 * Gets catchable pokemon sort by distance.
	 *
	 * @return the catchable pokemon sort
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public java.util.Map<Double, CatchablePokemon> getCatchablePokemonSort()
			throws LoginFailedException, RemoteServerException {
		return MapUtil.sortItems(getCatchablePokemon(), location);
	}

	/**
	 * Returns a list of nearby pokemon (non-catchable).
	 *
	 * @return a List of NearbyPokemon at your current location
	 */
	public List<NearbyPokemon> getNearbyPokemon() {
		return Stream.of(mapObjects.getNearbyPokemons()).map(new Function<NearbyPokemonOuterClass.NearbyPokemon, NearbyPokemon>() {
			@Override
			public NearbyPokemon apply(NearbyPokemonOuterClass.NearbyPokemon nearbyPokemon) {
				return new NearbyPokemon(nearbyPokemon);
			}
		}).collect(Collectors.<NearbyPokemon>toList());
	}

	/**
	 * Returns a list of spawn points.
	 *
	 * @return list of spawn points
	 */
	public List<Point> getSpawnPoints() {
		return Stream.of(mapObjects.getSpawnPoints()).map(new Function<SpawnPointOuterClass.SpawnPoint, Point>() {
			@Override
			public Point apply(SpawnPointOuterClass.SpawnPoint spawnPoint) {
				return new Point(spawnPoint);
			}
		}).collect(Collectors.<Point>toList());
	}

	/**
	 * Get a list of gyms near the current location.
	 *
	 * @return List of gyms
	 */
	public List<Gym> getGyms() {
		return Stream.of(mapObjects.getGyms()).map(new Function<FortData, Gym>() {
			@Override
			public Gym apply(FortData fortData) {
				return new Gym(networking, location, fortData);
			}
		}).collect(Collectors.<Gym>toList());
	}


	/**
	 * Gets gym sort by distance.
	 *
	 * @return the gym sort
	 */
	public java.util.Map<Double, Gym> getGymSort() {
		return MapUtil.sortItems(getGyms(), location);
	}

	/**
	 * Returns a list of decimated spawn points at current location.
	 *
	 * @return list of spawn points
	 */
	public List<Point> getDecimatedSpawnPoints() {
		return Stream.of(mapObjects.getDecimatedSpawnPoints()).map(new Function<SpawnPointOuterClass.SpawnPoint, Point>() {
			@Override
			public Point apply(SpawnPointOuterClass.SpawnPoint spawnPoint) {
				return new Point(spawnPoint);
			}
		}).collect(Collectors.<Point>toList());
	}

	@Override
	public void run() {
		try {
			Thread.sleep(Math.round(mapSettings.getMinRefresh()));
			GetMapObjectsMessage.Builder builder = GetMapObjectsMessageOuterClass.GetMapObjectsMessage.newBuilder()
					.setLatitude(location.getLatitude())
					.setLongitude(location.getLongitude());

			List<Long> cellIds = getCellIds(location.getLatitude(), location.getLongitude(), CELL_WIDTH);
			for (int i=0;i!=9;i++) {
				builder.setCellId(i, cellIds.get(0));
				builder.setSinceTimestampMs(i, 0);
			}
			mapObjects.update(networking.queueRequest(RequestType.GET_MAP_OBJECTS, builder.build(), GetMapObjectsResponse.class).toBlocking().first());
		}
		catch (InterruptedException e) {
			// Shutdown detected
			return;
		}
		// Prevent pool exhaustion by rescheduling
		executorService.execute(this);
	}

	/**
	 * Returns MapObjects around your current location.
	 *
	 * @return MapObjects at your current location
	 */
	public MapObjects getMapObjects()  {
		return mapObjects;
	}

	/**
	 * Get a list of all the Cell Ids.
	 *
	 * @param latitude  latitude
	 * @param longitude longitude
	 * @param width     width
	 * @return List of Cells
	 */
	public static List<Long> getCellIds(double latitude, double longitude, int width) {
		S2LatLng latLng = S2LatLng.fromDegrees(latitude, longitude);
		S2CellId cellId = S2CellId.fromLatLng(latLng).parent(15);

		MutableInteger index = new MutableInteger(0);
		MutableInteger jindex = new MutableInteger(0);


		int level = cellId.level();
		int size = 1 << (S2CellId.MAX_LEVEL - level);
		int face = cellId.toFaceIJOrientation(index, jindex, null);

		List<Long> cells = new ArrayList<>();

		int halfWidth = (int) Math.floor(width / 2);
		for (int x = -halfWidth; x <= halfWidth; x++) {
			for (int y = -halfWidth; y <= halfWidth; y++) {
				cells.add(S2CellId.fromFaceIJ(face, index.intValue() + x * size, jindex.intValue() + y * size).parent(15).id());
			}
		}
		return cells;
	}

	/**
	 * Gets fort details.
	 *
	 * @param id  the id
	 * @param lon the lon
	 * @param lat the lat
	 * @return the fort details
	 */
	public Observable<FortDetails> getFortDetailsAsync(String id, double lon, double lat) {
		return networking.queueRequest(RequestType.FORT_DETAILS, FortDetailsMessage.newBuilder()
				.setFortId(id)
				.setLatitude(lat)
				.setLongitude(lon)
				.build(), FortDetailsResponse.class).map(new Func1<FortDetailsResponse, FortDetails>() {
			@Override
			public FortDetails call(FortDetailsResponse response) {
				return new FortDetails(response);
			}
		});
	}

}
