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

import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass.FortDetailsMessage;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass.GetMapObjectsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass.FortDetailsResponse;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass.GetMapObjectsResponse;
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
import com.pokegoapi.google.common.geometry.MutableInteger;
import com.pokegoapi.google.common.geometry.S2CellId;
import com.pokegoapi.google.common.geometry.S2LatLng;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Map implements Runnable {
	public static final int CELL_WIDTH = 4;
	private final ExecutorService executorService;
	private final MapSettings mapSettings;
	private final MapObjects mapObjects;
	private final Networking networking;
	private final Location location;


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
		mapObjects = new MapObjects(networking, location, inventories, settings, getMapObjectsResponse);
		executorService.execute(this);
	}

	/**
	 * Returns a list of catchable pokemon around the current location.
	 *
	 * @return a List of CatchablePokemon at your current location
	 */
	public List<CatchablePokemon> getCatchablePokemon() {
		return new ArrayList<>(mapObjects.getCatchablePokemons());
	}

	public Observable<CatchablePokemon> createCatchablePokemonObservable() {
		return mapObjects.getCatchablePokemonMapOnSubscribe().create();
	}

	/**
	 * Returns a list of nearby pokemon (non-catchable).
	 *
	 * @return a List of NearbyPokemon at your current location
	 */
	public List<NearbyPokemon> getNearbyPokemon() {
		return new ArrayList<>(mapObjects.getNearbyPokemons());
	}

	public Observable<NearbyPokemon> createNearbyPokemonObservable() {
		return mapObjects.getNearbyPokemonMapOnSubscribe().create();
	}

	/**
	 * Returns a list of spawn points.
	 *
	 * @return list of spawn points
	 */
	public List<Point> getSpawnPoints() {
		return new ArrayList<>(mapObjects.getSpawnPoints());
	}

	public Observable<Point> createSpawnPointObservable() {
		return mapObjects.getSpawnPointMapOnSubscribe().create();
	}

	/**
	 * Get a list of gyms near the current location.
	 *
	 * @return List of gyms
	 */
	public List<Gym> getGyms() {
		return new ArrayList<>(mapObjects.getGyms());
	}

	public Observable<Gym> createGymObservable() {
		return mapObjects.getGymMapOnSubscribe().create();
	}

	/**
	 * Returns a list of decimated spawn points at current location.
	 *
	 * @return list of spawn points
	 */
	public List<Point> getDecimatedSpawnPoints() {
		return new ArrayList<>(mapObjects.getDecimatedSpawnPoints());
	}

	public Observable<Point> createDecimatedSpawnPointsObservable() {
		return mapObjects.getDecimatedSpawnPointMapOnSubscribe().create();
	}

	public List<Pokestop> getPokestops() {
		return new ArrayList<>(mapObjects.getPokestops());
	}

	public Observable<Pokestop> createPokestopObservable() {
		return mapObjects.getPokestopOnSubscribe().create();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(Math.round(mapSettings.getMinRefresh()));
			GetMapObjectsMessage.Builder builder = GetMapObjectsMessageOuterClass.GetMapObjectsMessage.newBuilder()
					.setLatitude(location.getLatitude())
					.setLongitude(location.getLongitude());

			for (long cellId : getCellIds(location.getLatitude(), location.getLongitude())) {
				builder.addCellId(cellId);
				builder.addSinceTimestampMs(0L);
			}

			networking.queueRequest(RequestType.GET_MAP_OBJECTS, builder.build(), GetMapObjectsResponse.class).forEach(new Action1<GetMapObjectsResponse>() {
				@Override
				public void call(GetMapObjectsResponse mapObjectsResponse) {
					update(mapObjectsResponse);
				}
			});
		}
		catch (InterruptedException e) {
			// Shutdown detected
			return;
		}
		// Prevent pool exhaustion by rescheduling
		executorService.execute(this);
	}

	private void update(GetMapObjectsResponse mapObjectsResponse) {
		mapObjects.update(mapObjectsResponse);
	}

	public static List<Long> getCellIds(double latitude, double longitude) {
		final int width = 3;
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
	 * Get a list of all the Cell Ids.
	 *
	 * @param latitude  latitude
	 * @param longitude longitude
	 * @return List of Cells
	 */
	public static List<Long> getNewCellIds(double latitude, double longitude) {
		S2LatLng latLng = S2LatLng.fromDegrees(latitude, longitude);
		S2CellId cellId = S2CellId.fromLatLng(latLng).parent(15);

		MutableInteger index = new MutableInteger(0);
		MutableInteger jindex = new MutableInteger(0);


		int level = cellId.level();
		int size = 1 << (S2CellId.MAX_LEVEL - level);
		int face = cellId.toFaceIJOrientation(index, jindex, null);

		List<Long> cells = new ArrayList<>();

		int halfHeight = 2;
		int halfWidth = 2;
		for (int x = -halfWidth; x <= halfWidth; x++) {
			for (int y = -halfHeight; y <= halfHeight; y++) {
				cells.add(S2CellId.fromFaceIJ(face, index.intValue() + x * size, jindex.intValue() + y * size).parent(15).id());
			}
		}
		List<Long> cellIdsToSort = cells.subList(1, Math.min(20, cells.size()));
		Collections.sort(cellIdsToSort);
		return cellIdsToSort;
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
