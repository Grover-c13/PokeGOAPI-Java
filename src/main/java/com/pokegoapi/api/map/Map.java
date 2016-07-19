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
import com.pokegoapi.api.player.FortDetails;
import com.pokegoapi.main.ServerRequest;
import java8.util.function.Function;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import java.util.List;

public class Map {
	private PokemonGo api;
	private long lastMapUpdate;

	public Map(PokemonGo api) {
		this.api = api;
		lastMapUpdate = 0;
	}

	public MapObjects getMapObjects(List<Long> cellIds, double latitude, double longitude) {
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
		}

		return null;
	}
}