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

import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Fort.FortTypeOuterClass.FortType;
import POGOProtos.Map.MapCellOuterClass.MapCell;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass;
import POGOProtos.Map.SpawnPointOuterClass;
import POGOProtos.Networking.Requests.Messages.CatchPokemonMessageOuterClass.CatchPokemonMessage;
import POGOProtos.Networking.Requests.Messages.EncounterMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass.FortDetailsMessage;
import POGOProtos.Networking.Requests.Messages.FortSearchMessageOuterClass.FortSearchMessage;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass.GetMapObjectsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass.FortSearchResponse;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass.GetMapObjectsResponse;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.map.fort.FortDetails;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.NearbyPokemon;
import com.pokegoapi.exceptions.AsyncRemoteServerException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.google.common.geometry.MutableInteger;
import com.pokegoapi.google.common.geometry.S2CellId;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.AsyncHelper;
import com.pokegoapi.util.MapUtil;
import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class Map {
	private final PokemonGo api;
	private MapObjects cachedMapObjects;
	private List<CatchablePokemon> cachedCatchable;
	private int cellWidth = 3;
	private long lastMapUpdate;

	/**
	 * Instantiates a new Map.
	 *
	 * @param api the api
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	public Map(PokemonGo api) throws LoginFailedException, RemoteServerException {
		this.api = api;
		cachedMapObjects = new MapObjects(api);
		lastMapUpdate = 0;
	}

	/**
	 * Returns a list of catchable pokemon around the current location.
	 *
	 * @return a List of CatchablePokemon at your current location
	 */
	public Observable<List<CatchablePokemon>> getCatchablePokemonAsync() {

		if (!useCache()) {
			// getMapObjects wont be called unless this is null
			// so need to force it if due for a refresh
			cachedCatchable = null;
		}

		if (cachedCatchable != null) {
			return Observable.just(cachedCatchable);
		}


		List<Long> cellIds = getDefaultCells();
		return getMapObjectsAsync(cellIds).map(new Func1<MapObjects, List<CatchablePokemon>>() {
			@Override
			public List<CatchablePokemon> call(MapObjects mapObjects) {
				Set<CatchablePokemon> catchablePokemons = new HashSet<>();
				for (MapPokemon mapPokemon : mapObjects.getCatchablePokemons()) {
					catchablePokemons.add(new CatchablePokemon(api, mapPokemon));
				}

				for (WildPokemonOuterClass.WildPokemon wildPokemon : mapObjects.getWildPokemons()) {
					catchablePokemons.add(new CatchablePokemon(api, wildPokemon));
				}

				for (Pokestop pokestop : mapObjects.getPokestops()) {
					if (pokestop.inRangeForLuredPokemon() && pokestop.getFortData().hasLureInfo()) {
						catchablePokemons.add(new CatchablePokemon(api, pokestop.getFortData()));
					}
				}

				cachedCatchable = Collections.synchronizedList(new CopyOnWriteArrayList<>(catchablePokemons));
				return cachedCatchable;
			}
		});
	}

	/**
	 * Remove a catchable pokemon from the cache
	 *
	 */
	public void removeCatchable(CatchablePokemon pokemon) {
		if (cachedCatchable != null) {
			cachedCatchable.remove(pokemon);
		}
	}

	/**
	 * Returns a list of catchable pokemon around the current location.
	 *
	 * @return a List of CatchablePokemon at your current location
	 */
	public List<CatchablePokemon> getCatchablePokemon() throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(getCatchablePokemonAsync());
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
		MapUtil<CatchablePokemon> util = new MapUtil<>();
		return util.sortItems(getCatchablePokemon(), api);
	}

	/**
	 * Returns a list of nearby pokemon (non-catchable).
	 *
	 * @return a List of NearbyPokemon at your current location
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	public Observable<List<NearbyPokemon>> getNearbyPokemonAsync() {
		return getMapObjectsAsync(getDefaultCells()).map(new Func1<MapObjects, List<NearbyPokemon>>() {
			@Override
			public List<NearbyPokemon> call(MapObjects result) {
				List<NearbyPokemon> pokemons = new ArrayList<>();
				for (NearbyPokemonOuterClass.NearbyPokemon pokemon : result.getNearbyPokemons()) {
					pokemons.add(new NearbyPokemon(pokemon));
				}

				return pokemons;
			}
		});
	}

	/**
	 * Returns a list of nearby pokemon (non-catchable).
	 *
	 * @return a List of NearbyPokemon at your current location
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	public List<NearbyPokemon> getNearbyPokemon() throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(getNearbyPokemonAsync());
	}

	/**
	 * Returns a list of spawn points.
	 *
	 * @return list of spawn points
	 */
	public Observable<List<Point>> getSpawnPointsAsync() {
		return getMapObjectsAsync(getDefaultCells()).map(new Func1<MapObjects, List<Point>>() {
			@Override
			public List<Point> call(MapObjects result) {
				List<Point> points = new ArrayList<>();

				for (SpawnPointOuterClass.SpawnPoint point : result.getSpawnPoints()) {
					points.add(new Point(point));
				}

				return points;
			}
		});
	}

	/**
	 * Returns a list of spawn points.
	 *
	 * @return list of spawn points
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	public List<Point> getSpawnPoints() throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(getSpawnPointsAsync());
	}

	/**
	 * Get a list of gyms near the current location.
	 *
	 * @return List of gyms
	 */
	public Observable<List<Gym>> getGymsAsync() {
		return getMapObjectsAsync(getDefaultCells()).map(new Func1<MapObjects, List<Gym>>() {
			@Override
			public List<Gym> call(MapObjects result) {
				List<Gym> gyms = new ArrayList<>();

				for (FortData fortdata : result.getGyms()) {
					gyms.add(new Gym(api, fortdata));
				}

				return gyms;
			}
		});
	}

	/**
	 * Get a list of gyms near the current location.
	 *
	 * @return List of gyms
	 */
	public List<Gym> getGyms() throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(getGymsAsync());
	}

	/**
	 * Gets gym sort by distance.
	 *
	 * @return the gym sort
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public java.util.Map<Double, Gym> getGymSort() throws LoginFailedException, RemoteServerException {
		MapUtil<Gym> util = new MapUtil<>();
		return util.sortItems(getGyms(), api);
	}

	/**
	 * Returns a list of decimated spawn points at current location.
	 *
	 * @return list of spawn points
	 */
	public Observable<List<Point>> getDecimatedSpawnPointsAsync() {
		return getMapObjectsAsync(getDefaultCells()).map(new Func1<MapObjects, List<Point>>() {
			public List<Point> call(MapObjects result) {
				List<Point> points = new ArrayList<>();
				for (SpawnPointOuterClass.SpawnPoint point : result.getDecimatedSpawnPoints()) {
					points.add(new Point(point));
				}

				return points;
			}
		});
	}

	/**
	 * Returns a list of decimated spawn points at current location.
	 *
	 * @return list of spawn points
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	public List<Point> getDecimatedSpawnPoints() throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(getDecimatedSpawnPointsAsync());
	}


	/**
	 * Gets decimated spawn points sort by distance.
	 *
	 * @return the decimated spawn points sort
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public java.util.Map<Double, Point> getDecimatedSpawnPointsSort() throws LoginFailedException, RemoteServerException {
		MapUtil<Point> util = new MapUtil<>();
		return util.sortItems(getDecimatedSpawnPoints(), api);
	}

	/**
	 * Returns MapObjects around your current location.
	 *
	 * @return MapObjects at your current location
	 */
	public Observable<MapObjects> getMapObjectsAsync() {
		return getMapObjectsAsync(getDefaultCells());
	}

	/**
	 * Returns MapObjects around your current location within a given width.
	 *
	 * @param width width
	 * @return MapObjects at your current location
	 */
	public Observable<MapObjects> getMapObjectsAsync(int width) {
		return getMapObjectsAsync(getCellIds(api.getLatitude(), api.getLongitude(), width));
	}

	/**
	 * Returns the cells requested.
	 *
	 * @param cellIds List of cellId
	 * @return MapObjects in the given cells
	 */
	public Observable<MapObjects> getMapObjectsAsync(List<Long> cellIds) {

		if (useCache()) {
			return Observable.just(cachedMapObjects);
		}

		lastMapUpdate = api.currentTimeMillis();
		GetMapObjectsMessage.Builder builder = GetMapObjectsMessageOuterClass.GetMapObjectsMessage.newBuilder()
				.setLatitude(api.getLatitude())
				.setLongitude(api.getLongitude());

		int index = 0;
		for (Long cellId : cellIds) {
			builder.addCellId(cellId);
			builder.addSinceTimestampMs(0);
			index++;
		}

		final AsyncServerRequest asyncServerRequest = new AsyncServerRequest(
				RequestType.GET_MAP_OBJECTS, builder.build());
		return api.getRequestHandler()
				.sendAsyncServerRequests(asyncServerRequest).map(new Func1<ByteString, MapObjects>() {
					@Override
					public MapObjects call(ByteString byteString) {
						GetMapObjectsResponse response;
						try {
							response = GetMapObjectsResponse.parseFrom(byteString);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}

						MapObjects result = new MapObjects(api);
						cachedMapObjects = result;
						for (MapCell mapCell : response.getMapCellsList()) {
							result.addNearbyPokemons(mapCell.getNearbyPokemonsList());
							result.addCatchablePokemons(mapCell.getCatchablePokemonsList());
							result.addWildPokemons(mapCell.getWildPokemonsList());
							result.addDecimatedSpawnPoints(mapCell.getDecimatedSpawnPointsList());
							result.addSpawnPoints(mapCell.getSpawnPointsList());

							java.util.Map<FortType, List<FortData>> groupedForts = Stream.of(mapCell.getFortsList())
									.collect(Collectors.groupingBy(new Function<FortData, FortType>() {
										@Override
										public FortType apply(FortData fortData) {
											return fortData.getType();
										}
									}));
							result.addGyms(groupedForts.get(FortType.GYM));
							result.addPokestops(groupedForts.get(FortType.CHECKPOINT));
						}

						cachedCatchable = null;
						return result;
					}
				});
	}

	/**
	 * Returns MapObjects around your current location.
	 *
	 * @return MapObjects at your current location
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	public MapObjects getMapObjects() throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(getMapObjectsAsync());
	}

	/**
	 * Returns MapObjects around your current location within a given width.
	 *
	 * @param width width
	 * @return MapObjects at your current location
	 * @throws LoginFailedException  If login fails.
	 * @throws RemoteServerException If request errors occurred.
	 */
	public MapObjects getMapObjects(int width) throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(getMapObjectsAsync(width));
	}

	/**
	 * Returns 5x5 cells with the requested lattitude/longitude in the center cell.
	 *
	 * @param latitude  latitude
	 * @param longitude longitude
	 * @return MapObjects in the given cells
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	@Deprecated
	public MapObjects getMapObjects(double latitude, double longitude)
			throws LoginFailedException, RemoteServerException {
		return getMapObjects(latitude, longitude, cellWidth);
	}

	/**
	 * Returns the cells requested, you should send a latitude/longitude to fake a near location.
	 *
	 * @param cellIds   List of cellIds
	 * @param latitude  latitude
	 * @param longitude longitude
	 * @return MapObjects in the given cells
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	@Deprecated
	public MapObjects getMapObjects(List<Long> cellIds, double latitude, double longitude)
			throws LoginFailedException, RemoteServerException {
		return getMapObjects(cellIds, latitude, longitude, 0);
	}

	/**
	 * Returns `width` * `width` cells with the requested latitude/longitude in the center.
	 *
	 * @param latitude  latitude
	 * @param longitude longitude
	 * @param width     width
	 * @return MapObjects in the given cells
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	@Deprecated
	public MapObjects getMapObjects(double latitude, double longitude, int width)
			throws LoginFailedException, RemoteServerException {
		return getMapObjects(getCellIds(latitude, longitude, width), latitude, longitude);
	}

	/**
	 * Returns the cells requested.
	 *
	 * @param cellIds   cellIds
	 * @param latitude  latitude
	 * @param longitude longitude
	 * @param altitude  altitude
	 * @return MapObjects in the given cells
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	@Deprecated
	public MapObjects getMapObjects(List<Long> cellIds, double latitude, double longitude, double altitude)
			throws LoginFailedException, RemoteServerException {
		api.setLatitude(latitude);
		api.setLongitude(longitude);
		api.setAltitude(altitude);
		return getMapObjects(cellIds);
	}

	/**
	 * Returns the cells requested.
	 *
	 * @param cellIds List of cellId
	 * @return MapObjects in the given cells
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */
	public MapObjects getMapObjects(List<Long> cellIds) throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(getMapObjectsAsync(cellIds));
	}

	/**
	 * Get a list of all the Cell Ids.
	 *
	 * @param latitude  latitude
	 * @param longitude longitude
	 * @param width     width
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

		List<Long> cells = new ArrayList<Long>();

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
		FortDetailsMessage reqMsg = FortDetailsMessage.newBuilder()
				.setFortId(id)
				.setLatitude(lat)
				.setLongitude(lon)
				.build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.FORT_DETAILS,
				reqMsg);
		return api.getRequestHandler()
				.sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, FortDetails>() {
					@Override
					public FortDetails call(ByteString byteString) {
						FortDetailsResponseOuterClass.FortDetailsResponse response;
						try {
							response = FortDetailsResponseOuterClass.FortDetailsResponse.parseFrom(byteString);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}
						return new FortDetails(response);
					}
				});
	}

	/**
	 * Gets fort details.
	 *
	 * @param id  the id
	 * @param lon the lon
	 * @param lat the lat
	 * @return the fort details
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public FortDetails getFortDetails(String id, double lon, double lat)
			throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(getFortDetailsAsync(id, lon, lat));
	}

	/**
	 * Search fort fort search response.
	 *
	 * @param fortData the fort data
	 * @return the fort search response
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	@Deprecated
	public FortSearchResponse searchFort(FortData fortData) throws LoginFailedException, RemoteServerException {
		FortSearchMessage reqMsg = FortSearchMessage.newBuilder()
				.setFortId(fortData.getId())
				.setFortLatitude(fortData.getLatitude())
				.setFortLongitude(fortData.getLongitude())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.build();
		ServerRequest serverRequest = new ServerRequest(RequestType.FORT_SEARCH, reqMsg);

		api.getRequestHandler().sendServerRequests(serverRequest);

		FortSearchResponse response;
		try {
			response = FortSearchResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		return response;
	}

	/**
	 * Encounter pokemon encounter response.
	 *
	 * @param catchablePokemon the catchable pokemon
	 * @return the encounter response
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	@Deprecated
	public EncounterResponse encounterPokemon(MapPokemon catchablePokemon)
			throws LoginFailedException, RemoteServerException {

		EncounterMessageOuterClass.EncounterMessage reqMsg = EncounterMessageOuterClass.EncounterMessage.newBuilder()
				.setEncounterId(catchablePokemon.getEncounterId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setSpawnPointId(catchablePokemon.getSpawnPointId())
				.build();
		ServerRequest serverRequest = new ServerRequest(RequestType.ENCOUNTER, reqMsg);
		api.getRequestHandler().sendServerRequests(serverRequest);

		EncounterResponse response;
		try {
			response = EncounterResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		return response;
	}

	/**
	 * Catch pokemon catch pokemon response.
	 *
	 * @param catchablePokemon      the catchable pokemon
	 * @param normalizedHitPosition the normalized hit position
	 * @param normalizedReticleSize the normalized reticle size
	 * @param spinModifier          the spin modifier
	 * @param pokeball              the pokeball
	 * @return the catch pokemon response
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	@Deprecated
	public CatchPokemonResponse catchPokemon(
			MapPokemon catchablePokemon,
			double normalizedHitPosition,
			double normalizedReticleSize,
			double spinModifier,
			ItemId pokeball)
			throws LoginFailedException, RemoteServerException {

		CatchPokemonMessage reqMsg = CatchPokemonMessage.newBuilder()
				.setEncounterId(catchablePokemon.getEncounterId())
				.setHitPokemon(true)
				.setNormalizedHitPosition(normalizedHitPosition)
				.setNormalizedReticleSize(normalizedReticleSize)
				.setSpawnPointId(catchablePokemon.getSpawnPointId())
				.setSpinModifier(spinModifier)
				.setPokeball(pokeball)
				.build();
		ServerRequest serverRequest = new ServerRequest(RequestType.CATCH_POKEMON, reqMsg);
		api.getRequestHandler().sendServerRequests(serverRequest);

		CatchPokemonResponse response;
		try {
			response = CatchPokemonResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		return response;
	}

	public void setDefaultWidth(int width) {
		cellWidth = width;
	}

	/**
	 * Wether or not to get a fresh copy or use cache;
	 *
	 * @return true if enough time has elapsed since the last request, false otherwise
	 */
	private boolean useCache() {
		return (api.currentTimeMillis() - lastMapUpdate) < api.getSettings().getMapSettings().getMinRefresh();
	}

	private List<Long> getDefaultCells() {
		return getCellIds(api.getLatitude(), api.getLongitude(), cellWidth);
	}
}
