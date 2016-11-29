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
import POGOProtos.Map.MapCellOuterClass.MapCell;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass.WildPokemon;
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
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.map.fort.FortDetails;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.NearbyPokemon;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.google.common.geometry.MutableInteger;
import com.pokegoapi.google.common.geometry.S2CellId;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonRequest;
import com.pokegoapi.main.PokemonResponse;
import com.pokegoapi.main.RequestCallback;
import com.pokegoapi.main.Utils;
import com.pokegoapi.util.MapUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Map {
	private final PokemonGo api;
	private MapObjects cachedMapObjects;
	private final List<CatchablePokemon> cachedCatchable = Collections.synchronizedList(
			new CopyOnWriteArrayList<CatchablePokemon>()
	);
	private int cellWidth = 3;
	private long lastMapUpdate;

	/**
	 * Instantiates a new Map.
	 *
	 * @param api the api
	 */
	public Map(PokemonGo api) {
		this.api = api;
		cachedMapObjects = new MapObjects(api);
		lastMapUpdate = 0;
	}

	/**
	 * Returns a list of catchable pokemon around the current location.
	 *
	 * @param catchablePokemon callback returning a list of CatchablePokemon at your current location
	 */
	public void getCatchablePokemon(final AsyncReturn<List<CatchablePokemon>> catchablePokemon) {
		if (useCache() && cachedCatchable.size() > 0) {
			catchablePokemon.onReceive(cachedCatchable, null);
		}

		List<Long> defaultCells = getDefaultCells();
		getMapObjects(defaultCells, new AsyncReturn<MapObjects>() {
			@Override
			public void onReceive(MapObjects objects, Exception exception) {
				if (Utils.callbackException(exception, catchablePokemon, new ArrayList<CatchablePokemon>())) {
					return;
				}
				Set<CatchablePokemon> catchablePokemons = new HashSet<>();
				for (MapPokemon mapPokemon : objects.getCatchablePokemons()) {
					catchablePokemons.add(new CatchablePokemon(api, mapPokemon));
				}

				for (WildPokemon wildPokemon : objects.getWildPokemons()) {
					catchablePokemons.add(new CatchablePokemon(api, wildPokemon));
				}

				for (Pokestop pokestop : objects.getPokestops()) {
					if (pokestop.inRangeForLuredPokemon() && pokestop.getFortData().hasLureInfo()) {
						catchablePokemons.add(new CatchablePokemon(api, pokestop.getFortData()));
					}
				}

				cachedCatchable.clear();
				cachedCatchable.addAll(catchablePokemons);

				catchablePokemon.onReceive(cachedCatchable, null);
			}
		});
	}

	/**
	 * Remove a catchable pokemon from the cache
	 *
	 * @param pokemon the catchable pokemon
	 */
	public void removeCatchable(CatchablePokemon pokemon) {
		if (cachedCatchable.size() > 0) {
			cachedCatchable.remove(pokemon);
		}
	}

	/**
	 * Returns a list of catchable pokemon around the current location, sorted by distance.
	 *
	 * @param catchablePokemon callback returning a list of CatchablePokemon at your current location
	 */
	public void getSortedCatchablePokemon(final AsyncReturn<java.util.Map<Double, CatchablePokemon>> catchablePokemon) {
		getCatchablePokemon(new AsyncReturn<List<CatchablePokemon>>() {
			@Override
			public void onReceive(List<CatchablePokemon> pokemon, Exception exception) {
				if (Utils.callbackException(exception, catchablePokemon, new TreeMap<Double, CatchablePokemon>())) {
					return;
				}
				MapUtil<CatchablePokemon> util = new MapUtil<>();
				catchablePokemon.onReceive(util.sortItems(pokemon, api), null);
			}
		});
	}

	/**
	 * Gets a list of nearby pokemon (non-catchable/sightings).
	 * @param nearby callback to return list of nearby pokemon
	 */
	public void getNearbyPokemon(final AsyncReturn<List<NearbyPokemon>> nearby) {
		getMapObjects(getDefaultCells(), new AsyncReturn<MapObjects>() {
			@Override
			public void onReceive(MapObjects objects, Exception exception) {
				if (Utils.callbackException(exception, nearby, new ArrayList<NearbyPokemon>())) {
					return;
				}
				List<NearbyPokemon> pokemons = new ArrayList<>();
				for (NearbyPokemonOuterClass.NearbyPokemon pokemon : objects.getNearbyPokemons()) {
					pokemons.add(new NearbyPokemon(pokemon));
				}
				nearby.onReceive(pokemons, null);
			}
		});
	}

	/**
	 * Gets a list of spawn points.
	 *
	 * @param spawnPoints callback to return all spawn points
	 */
	public void getSpawnPoints(final AsyncReturn<List<Point>> spawnPoints) {
		getMapObjects(new AsyncReturn<MapObjects>() {
			@Override
			public void onReceive(MapObjects objects, Exception exception) {
				if (Utils.callbackException(exception, spawnPoints, new ArrayList<Point>())) {
					return;
				}
				List<Point> points = new ArrayList<>();
				for (SpawnPointOuterClass.SpawnPoint point : objects.getSpawnPoints()) {
					points.add(new Point(point));
				}
				spawnPoints.onReceive(points, null);
			}
		});
	}

	/**
	 * Gets a list of gyms.
	 *
	 * @param gymCallback callback to return all gyms
	 */
	public void getGyms(final AsyncReturn<List<Gym>> gymCallback) {
		getMapObjects(new AsyncReturn<MapObjects>() {
			@Override
			public void onReceive(MapObjects objects, Exception exception) {
				if (Utils.callbackException(exception, gymCallback, new ArrayList<Gym>())) {
					return;
				}
				List<Gym> gyms = new ArrayList<>();
				for (FortData fortdata : objects.getGyms()) {
					gyms.add(new Gym(api, fortdata));
				}
				gymCallback.onReceive(gyms, null);
			}
		});
	}

	/**
	 * Returns a list of gyms around the current location, sorted by distance.
	 *
	 * @param gyms callback returning a list of Gyms at your current location
	 */
	public void getSortedGyms(final AsyncReturn<java.util.Map<Double, Gym>> gyms) {
		getGyms(new AsyncReturn<List<Gym>>() {
			@Override
			public void onReceive(List<Gym> gymList, Exception exception) {
				if (Utils.callbackException(exception, gyms, new TreeMap<Double, Gym>())) {
					return;
				}
				MapUtil<Gym> util = new MapUtil<>();
				gyms.onReceive(util.sortItems(gymList, api), null);
			}
		});
	}

	/**
	 * Gets a list of decimated spawn points at current location.
	 *
	 * @param pointsCallback callback for the returned decimated spawn points
	 */
	public void getDecimatedSpawnPoints(final AsyncReturn<List<Point>> pointsCallback) {
		getMapObjects(getDefaultCells(), new AsyncReturn<MapObjects>() {
			@Override
			public void onReceive(MapObjects objects, Exception exception) {
				if (Utils.callbackException(exception, pointsCallback, new ArrayList<Point>())) {
					return;
				}
				List<Point> points = new ArrayList<>();
				for (SpawnPointOuterClass.SpawnPoint point : objects.getDecimatedSpawnPoints()) {
					points.add(new Point(point));
				}
				pointsCallback.onReceive(points, null);
			}
		});
	}

	/**
	 * Gets a list of decimated spawn points at current location, sorted by distance.
	 *
	 * @param pointsCallback callback for the returned decimated spawn points
	 */
	public void getSortedDecimatedSpawnPoints(final AsyncReturn<java.util.Map<Double, Point>> pointsCallback) {
		getDecimatedSpawnPoints(new AsyncReturn<List<Point>>() {
			@Override
			public void onReceive(List<Point> points, Exception exception) {
				if (Utils.callbackException(exception, pointsCallback, new TreeMap<Double, Point>())) {
					return;
				}
				MapUtil<Point> util = new MapUtil<>();
				pointsCallback.onReceive(util.sortItems(points, api), null);
			}
		});
	}

	/**
	 * Gets map objects at your current location.
	 *
	 * @param mapObjects callback for the returned map objects
	 */
	public void getMapObjects(AsyncReturn<MapObjects> mapObjects) {
		getMapObjects(getDefaultCells(), mapObjects);
	}

	/**
	 * Gets map objects at your current location with a custom width.
	 *
	 * @param mapObjects callback for the returned map objects
	 * @param width width to find map objects in
	 */
	public void getMapObjects(int width, AsyncReturn<MapObjects> mapObjects) {
		getMapObjects(getCellIds(api.getLatitude(), api.getLongitude(), width), mapObjects);
	}

	/**
	 * Gets map objects for the given cells
	 *
	 * @param cellIds List of cellId
	 * @param mapObjects callback for the returned map objects
	 */
	public void getMapObjects(List<Long> cellIds, final AsyncReturn<MapObjects> mapObjects) {
		if ((useCache() && (cachedMapObjects.getNearbyPokemons().size() > 0
				|| cachedMapObjects.getCatchablePokemons().size() > 0
				|| cachedMapObjects.getWildPokemons().size() > 0
				|| cachedMapObjects.getDecimatedSpawnPoints().size() > 0
				|| cachedMapObjects.getSpawnPoints().size() > 0)) || api.hasChallenge()) {
			mapObjects.onReceive(cachedMapObjects, null);
		}

		lastMapUpdate = api.currentTimeMillis();
		GetMapObjectsMessage.Builder builder = GetMapObjectsMessageOuterClass.GetMapObjectsMessage.newBuilder()
				.setLatitude(api.getLatitude())
				.setLongitude(api.getLongitude());

		for (Long cellId : cellIds) {
			builder.addCellId(cellId);
			builder.addSinceTimestampMs(0);
		}

		final PokemonRequest request = new PokemonRequest(
				RequestType.GET_MAP_OBJECTS, builder.build(), true)
				.withCallback(new RequestCallback() {
					@Override
					public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
						if (Utils.callbackException(response, mapObjects, cachedMapObjects)) {
							return;
						}
						try {
							GetMapObjectsResponse messageResponse = GetMapObjectsResponse.parseFrom(response.getResponseData());
							MapObjects result = new MapObjects(api);
							cachedMapObjects = result;
							for (MapCell cell : messageResponse.getMapCellsList()) {
								result.addNearbyPokemons(cell.getNearbyPokemonsList());
								result.addCatchablePokemons(cell.getCatchablePokemonsList());
								result.addWildPokemons(cell.getWildPokemonsList());
								result.addDecimatedSpawnPoints(cell.getDecimatedSpawnPointsList());
								result.addSpawnPoints(cell.getSpawnPointsList());

								java.util.Map<FortType, List<FortData>> groupedForts = Stream.of(cell.getFortsList())
										.collect(Collectors.groupingBy(new Function<FortData, FortType>() {
											@Override
											public FortType apply(FortData fortData) {
												return fortData.getType();
											}
										}));
								result.addGyms(groupedForts.get(FortType.GYM));
								result.addPokestops(groupedForts.get(FortType.CHECKPOINT));
							}

							cachedCatchable.clear();
							mapObjects.onReceive(result, null);
						} catch (InvalidProtocolBufferException e) {
							mapObjects.onReceive(cachedMapObjects, new RemoteServerException(e));
						}
					}
				});
		api.getRequestHandler().sendRequest(request);
	}


	/**
	 * Get a list of all the Cell Ids.
	 *
	 * @param latitude latitude
	 * @param longitude longitude
	 * @param width width
	 * @return List of Cells
	 */
	public List<Long> getCellIds(double latitude, double longitude, int width) {
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
	 * @param id the id
	 * @param lon the lon
	 * @param lat the lat
	 * @param details callback to return fort details
	 */
	public void getFortDetails(String id, double lon, double lat, final AsyncReturn<FortDetails> details) {
		FortDetailsMessage message = FortDetailsMessage.newBuilder()
				.setFortId(id)
				.setLatitude(lat)
				.setLongitude(lon)
				.build();

		PokemonRequest request = new PokemonRequest(RequestType.FORT_DETAILS, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, details, null)) {
					return;
				}
				try {
					FortDetailsResponse messageResponse = FortDetailsResponse.parseFrom(response.getResponseData());
					details.onReceive(new FortDetails(messageResponse), null);
				} catch (InvalidProtocolBufferException e) {
					details.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Sets the default cell width.
	 *
	 * @param width the new default cell width
	 */
	public void setDefaultCellWidth(int width) {
		cellWidth = width;
	}

	/**
	 * Whether or not to get a fresh copy or use cache;
	 *
	 * @return false if enough time has elapsed since the last request, true otherwise
	 */
	private boolean useCache() {
		return (api.currentTimeMillis() - lastMapUpdate) < api.getSettings().getMapSettings().getMinRefresh();
	}

	/**
	 * Clear map objects cache
	 */
	public void clearCache() {
		cachedCatchable.clear();
		cachedMapObjects.getNearbyPokemons().clear();
		cachedMapObjects.getCatchablePokemons().clear();
		cachedMapObjects.getWildPokemons().clear();
		cachedMapObjects.getDecimatedSpawnPoints().clear();
		cachedMapObjects.getSpawnPoints().clear();
	}

	private List<Long> getDefaultCells() {
		return getCellIds(api.getLatitude(), api.getLongitude(), cellWidth);
	}
}
