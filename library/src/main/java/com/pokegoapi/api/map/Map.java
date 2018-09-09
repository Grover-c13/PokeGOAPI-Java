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

import POGOProtos.Map.MapCellOuterClass.MapCell;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass.GetMapObjectsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass.GetMapObjectsResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.google.common.geometry.MutableInteger;
import com.pokegoapi.google.common.geometry.S2CellId;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Map {
	private final PokemonGo api;
	private int defaultCellWidth = 3;

	@Getter
	public MapObjects mapObjects;

	@Getter
	private long mapUpdateTime;

	private final Object updateLock = new Object();

	/**
	 * Instantiates a new Map.
	 *
	 * @param api the api
	 */
	public Map(PokemonGo api) {
		this.api = api;
		this.mapObjects = new MapObjects(api);
	}

	/**
	 * Updates the map. Only API should be calling this.
	 *
	 * @return if the map was updated
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public boolean update() throws RequestFailedException {
		boolean updated = false;
		if (!(Double.isNaN(api.latitude) || Double.isNaN(api.longitude))) {
			this.mapObjects = requestMapObjects();
			updated = true;
		}
		synchronized (this.updateLock) {
			this.updateLock.notifyAll();
		}
		return updated;
	}

	/**
	 * Requests and returns MapObjects from the server.
	 *
	 * @return the returned MapObjects
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	protected MapObjects requestMapObjects()
			throws RequestFailedException {
		List<Long> cells = getDefaultCells();
		GetMapObjectsMessage.Builder builder = GetMapObjectsMessage.newBuilder();
		builder.setLatitude(api.latitude);
		builder.setLongitude(api.longitude);
		for (Long cell : cells) {
			builder.addCellId(cell);
			builder.addSinceTimestampMs(0);
		}
		ServerRequest request = new ServerRequest(RequestType.GET_MAP_OBJECTS, builder.build());
		api.requestHandler.sendServerRequests(request, true);
		try {
			long updateTime = mapUpdateTime;
			GetMapObjectsResponse response = GetMapObjectsResponse.parseFrom(request.getData());
			MapObjects mapObjects = new MapObjects(api);
			for (MapCell cell : response.getMapCellsList()) {
				mapObjects.addCell(cell);
				updateTime = Math.max(updateTime, cell.getCurrentTimestampMs());
			}
			mapUpdateTime = updateTime;
			return mapObjects;
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * @return a list of all default cells
	 */
	private List<Long> getDefaultCells() {
		return getCellIds(api.latitude, api.longitude, defaultCellWidth);
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
				cells.add(S2CellId.fromFaceIJ(face, index.intValue() + x * size,
						jindex.intValue() + y * size).parent(15).id());
			}
		}
		return cells;
	}

	/**
	 * Blocks this thread until MapObjects are updates
	 * @throws InterruptedException if this thread is interrupted while awaiting map update
	 */
	public void awaitUpdate() throws InterruptedException {
		synchronized (this.updateLock) {
			this.updateLock.wait();
		}
	}
}
