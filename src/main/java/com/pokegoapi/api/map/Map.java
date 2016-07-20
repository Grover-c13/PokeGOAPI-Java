package com.pokegoapi.api.map;

import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Map.Fort.FortTypeOuterClass;
import POGOProtos.Map.MapCellOuterClass;
import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.fort.FortDetails;
import com.pokegoapi.google.common.geometry.MutableInteger;
import com.pokegoapi.google.common.geometry.S2Cell;
import com.pokegoapi.google.common.geometry.S2CellId;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.ServerRequest;
import java8.util.function.Function;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import java.util.ArrayList;
import java.util.List;

import static com.pokegoapi.google.common.geometry.S2CellId.MAX_LEVEL;

public class Map {
	private PokemonGo api;
	private long lastMapUpdate;

	public Map(PokemonGo api) {
		this.api = api;
		lastMapUpdate = 0;
	}

    /**
     * Returns MapObjects around your current location
     * @return MapObjects at your current location
     */
    public MapObjects getMapObjects(){
        return getMapObjects(9);
    }

    /**
     * Returns MapObjects around your current location within a given width
     * @param width
     * @return MapObjects at your current location
     */
    public MapObjects getMapObjects(int width){
        return getMapObjects(getCellIds(api.getLatitude(), api.getLongitude(), width), api.getLatitude(), api.getLongitude(), api.getAltitude());
    }

    /**
     * Returns 9x9 cells with the requested lattitude/longitude in the center cell
     *
     * @param latitude
     * @param longitude
     * @return MapObjects in the given cells
     */
    public MapObjects getMapObjects(double latitude, double longitude) {
        return getMapObjects(latitude, longitude, 9);
    }

    /**
     * Returns the cells requested, you should send a latitude/longitude to fake a near location
     *
     * @param cellIds List<Long> of cellId
     * @param latitude
     * @param longitude
     * @return MapObjects in the given cells
     */
    public MapObjects getMapObjects(List<Long> cellIds, double latitude, double longitude) {
        return getMapObjects(cellIds, latitude, longitude, 0);
    }

    /**
     * Returns `width` * `width` cells with the requested latitude/longitude in the center
     *
     * @param latitude
     * @param longitude
     * @param width
     * @return MapObjects in the given cells
     */
    public MapObjects getMapObjects(double latitude, double longitude, int width) {
        return getMapObjects(getCellIds(latitude, longitude, width), latitude, longitude);
    }

	/**
	 * Returns the cells requested
	 *
	 * @param cellIds List<Long> of cellId
	 * @return MapObjects in the given cells
	 */
	public MapObjects getMapObjects(List<Long> cellIds, double latitude, double longitude, double altitude) {

		MapObjects result = null;
		try {
			GetMapObjectsMessageOuterClass.GetMapObjectsMessage.Builder builder = GetMapObjectsMessageOuterClass.GetMapObjectsMessage.newBuilder()
					.setLatitude(latitude)
					.setLongitude(longitude);

			int i = 0;
			for (Long cellId : cellIds) {
				builder.addCellId(cellId);
				builder.addSinceTimestampMs(lastMapUpdate);
				i++;
			}

			ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.GET_MAP_OBJECTS, builder.build());
			api.getRequestHandler().request(serverRequest);
			api.getRequestHandler().sendServerRequests();
			GetMapObjectsResponseOuterClass.GetMapObjectsResponse response = GetMapObjectsResponseOuterClass.GetMapObjectsResponse.parseFrom(serverRequest.getData());


			result = new MapObjects();
			for (MapCellOuterClass.MapCell mapCell : response.getMapCellsList()) {
				result.addNearbyPokemons(mapCell.getNearbyPokemonsList());
				result.addCatchablePokemons(mapCell.getCatchablePokemonsList());
				result.addWildPokemons(mapCell.getWildPokemonsList());
				result.addDecimatedSpawnPoints(mapCell.getDecimatedSpawnPointsList());
				result.addSpawnPoints(mapCell.getSpawnPointsList());

				java.util.Map<FortTypeOuterClass.FortType, List<FortDataOuterClass.FortData>> groupedForts
						= StreamSupport.stream(mapCell.getFortsList()).collect(Collectors.groupingBy(new Function<FortDataOuterClass.FortData, FortTypeOuterClass.FortType>() {
					@Override
					public FortTypeOuterClass.FortType apply(FortDataOuterClass.FortData t) {
						return t.getType();
					}
				}));
				result.addGyms(groupedForts.get(FortTypeOuterClass.FortType.GYM));
				result.addPokestops(groupedForts.get(FortTypeOuterClass.FortType.CHECKPOINT));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

    /**
     * Get a list of all the Cell Ids
     * @param latitude
     * @param longitude
     * @param width
     * @return List of Cells
     */
    public List<Long> getCellIds(double latitude, double longitude, int width){
        S2LatLng latLng = S2LatLng.fromDegrees(latitude, longitude);
        S2CellId cellId = S2CellId.fromLatLng(latLng).parent(15);

        MutableInteger i = new MutableInteger(0);
        MutableInteger j = new MutableInteger(0);

        int level = cellId.level();
        int size = 1 << (MAX_LEVEL - level);
        int face = cellId.toFaceIJOrientation(i, j, null);

        List<Long> cells = new ArrayList<Long>();

        int halfWidth = (int) Math.floor(width / 2);
        for (int x = -halfWidth; x <= halfWidth; x++) {
            for (int y = -halfWidth; y <= halfWidth; y++) {
                cells.add(cellId.fromFaceIJ(face, i.intValue() + x * size, j.intValue() + y * size).parent(15).id());
            }
        }
        return cells;
    }

    public FortDetails getFortDetails(String id, long lon, long lat) {
        // server request
        try {
            FortDetailsMessageOuterClass.FortDetailsMessage reqMsg = FortDetailsMessageOuterClass.FortDetailsMessage.newBuilder()
                    .setFortId(id)
                    .setLatitude(lat)
                    .setLongitude(lon)
                    .build();

            ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.FORT_DETAILS, reqMsg);
            api.getRequestHandler().request(serverRequest);
            api.getRequestHandler().sendServerRequests();
            FortDetailsResponseOuterClass.FortDetailsResponse response = FortDetailsResponseOuterClass.FortDetailsResponse.parseFrom(serverRequest.getData());
            return new FortDetails(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}