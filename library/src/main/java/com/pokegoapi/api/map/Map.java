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

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.exceptions.AsyncRemoteServerException;
import com.pokegoapi.google.common.geometry.MutableInteger;
import com.pokegoapi.google.common.geometry.S2CellId;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.main.CommonRequest;
import com.pokegoapi.util.PokeCallback;

import java.util.ArrayList;
import java.util.List;

import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Fort.FortTypeOuterClass.FortType;
import POGOProtos.Map.MapCellOuterClass.MapCell;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass.GetMapObjectsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass.GetMapObjectsResponse;
import rx.Observable;
import rx.functions.Func1;

public class Map {
	private final PokemonGo api;
	private MapObjects cachedMapObjects;
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
	 * Returns the cells requested.
	 *
	 * @param cellIds List of cellId
	 * @return MapObjects in the given cells
	 */
	private Observable<MapObjects> getMapObjectsAsync(List<Long> cellIds) {
		if (useCache()) {
			return Observable.just(cachedMapObjects);
		}

		lastMapUpdate = api.currentTimeMillis();
		GetMapObjectsMessage.Builder builder = GetMapObjectsMessageOuterClass.GetMapObjectsMessage.newBuilder()
				.setLatitude(api.getLatitude())
				.setLongitude(api.getLongitude());

		for (Long cellId : cellIds) {
			builder.addCellId(cellId);
			builder.addSinceTimestampMs(0);
		}

		final AsyncServerRequest asyncServerRequest = new AsyncServerRequest(
				RequestType.GET_MAP_OBJECTS, builder.build());
		asyncServerRequest.addCommonRequest(CommonRequest.getCommonRequests(api));
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

						return result;
					}
				});
	}

	/**
	 * Request a MapObjects around your current location.
	 */
	public void getMapObjects(final PokeCallback<MapObjects> callback) {
		getMapObjectsAsync(getDefaultCells()).subscribe(callback.getSubscriber());
	}

	/**
	 * Returns the cells requested.
	 *
	 * @param cellIds List of cellId
	 * @return MapObjects in the given cells
	 */
	public void getMapObjects(List<Long> cellIds, PokeCallback<MapObjects> callback) {
		getMapObjectsAsync(cellIds).subscribe(callback.getSubscriber());
	}

	/**
	 * Get a list of all the Cell Ids.
	 *
	 * @param latitude  latitude
	 * @param longitude longitude
	 * @param width     width
	 * @return List of Cells
	 */
	private List<Long> getCellIds(double latitude, double longitude, int width) {
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

	public List<Long> getDefaultCells() {
		return getCellIds(api.getLatitude(), api.getLongitude(), cellWidth);
	}
}