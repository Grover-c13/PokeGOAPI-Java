package com.pokegoapi.requests;

import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Fort.FortTypeOuterClass;
import POGOProtos.Map.MapCellOuterClass;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass;
import POGOProtos.Map.SpawnPointOuterClass;
import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.main.Request;
import java8.util.function.Function;
import java8.util.function.Predicate;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import java.util.*;

import static POGOProtos.Map.Fort.FortTypeOuterClass.*;

public class GetMapObjectsRequest extends Request{
	private final List<Long> requestedCells;
	private GetMapObjectsMessageOuterClass.GetMapObjectsMessage.Builder builder;
	private GetMapObjectsResponseOuterClass.GetMapObjectsResponse output;

	public GetMapObjectsRequest(List<Long> cellIds, double latitude, double longitude) {
		this.requestedCells = cellIds;
		builder = GetMapObjectsMessageOuterClass.GetMapObjectsMessage.newBuilder();
		int i = 0;
		for (Long cellId : cellIds) {
			builder.addCellId(cellId);
			builder.addSinceTimestampMs(0);
			i++;
		}
		// Not necessary at all, location from RequestEnvelope is used
		/*builder.setLatitude(latitude);
		builder.setLongitude(longitude);*/
	}

	public RequestTypeOuterClass.RequestType getRpcId() {
		return RequestTypeOuterClass.RequestType.GET_MAP_OBJECTS;
	}

	public void handleResponse(ByteString payload) {
		try {
			GetMapObjectsResponseOuterClass.GetMapObjectsResponse response = GetMapObjectsResponseOuterClass.GetMapObjectsResponse.parseFrom(payload);
			output = response;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}

	public byte[] getInput() {
		return builder.build().toByteArray();
	}

	public GetMapObjectsReply getOutput() {
		GetMapObjectsReply result = new GetMapObjectsReply();
		for (MapCellOuterClass.MapCell mapCell : output.getMapCellsList()) {
			result.addNearbyPokemons(mapCell.getNearbyPokemonsList());
			result.addCatchablePokemons(mapCell.getCatchablePokemonsList());
			result.addWildPokemons(mapCell.getWildPokemonsList());
			result.addDecimatedSpawnPoints(mapCell.getDecimatedSpawnPointsList());
			result.addSpawnPoints(mapCell.getSpawnPointsList());
			Map<FortType, List<FortData>> groupedForts = StreamSupport.stream(mapCell.getFortsList())
					.collect(Collectors.groupingBy(new Function<FortData, FortType>() {
						@Override
						public FortType apply(FortData fortData) {
							return fortData.getType();
						}
					}));

			result.addGyms(groupedForts.get(FortType.GYM));

			result.addGyms(groupedForts.get(FortTypeOuterClass.FortType.GYM));
			result.addPokestops(groupedForts.get(FortTypeOuterClass.FortType.CHECKPOINT));
		}
		List<Long> missedCells = StreamSupport.stream(requestedCells).filter(new Predicate<Long>() {
			@Override
			public boolean test(Long cellId) {
				return !output.getMapCellsList().contains(cellId);
			}
		}).collect(Collectors.<Long>toList());
		result.setMissedCells(missedCells);
		return result;
	}

	public class GetMapObjectsReply {
		private Collection<NearbyPokemonOuterClass.NearbyPokemon> nearbyPokemons = new ArrayList<NearbyPokemonOuterClass.NearbyPokemon>();
		private Collection<MapPokemonOuterClass.MapPokemon> catchablePokemons = new ArrayList<MapPokemonOuterClass.MapPokemon>();
		private Collection<WildPokemonOuterClass.WildPokemon> wildPokemons = new ArrayList<WildPokemonOuterClass.WildPokemon>();
		private Collection<SpawnPointOuterClass.SpawnPoint> decimatedSpawnPoints = new ArrayList<SpawnPointOuterClass.SpawnPoint>();
		private Collection<SpawnPointOuterClass.SpawnPoint> spawnPoints = new ArrayList<SpawnPointOuterClass.SpawnPoint>();
		private Collection<FortData> gyms = new ArrayList<FortData>();
		private Collection<FortData> pokestops = new ArrayList<FortData>();
		private Collection<Long> missedCells;

		public Collection<NearbyPokemonOuterClass.NearbyPokemon> getNearbyPokemons() {
			return nearbyPokemons;
		}

		public Collection<MapPokemonOuterClass.MapPokemon> getCatchablePokemons() {
			return catchablePokemons;
		}

		public Collection<WildPokemonOuterClass.WildPokemon> getWildPokemons() {
			return wildPokemons;
		}

		public Collection<SpawnPointOuterClass.SpawnPoint> getDecimatedSpawnPoints() {
			return decimatedSpawnPoints;
		}

		public Collection<SpawnPointOuterClass.SpawnPoint> getSpawnPoints() {
			return spawnPoints;
		}

		public Collection<FortData> getGyms() {
			return gyms;
		}

		public Collection<FortData> getPokestops() {
			return pokestops;
		}

		@Override
		public String toString() {
			return "GetMapObjectsReply{" +
					"nearbyPokemons=" + nearbyPokemons +
					", catchablePokemons=" + catchablePokemons +
					", wildPokemons=" + wildPokemons +
					", decimatedSpawnPoints=" + decimatedSpawnPoints +
					", spawnPoints=" + spawnPoints +
					", gyms=" + gyms +
					", pokestops=" + pokestops +
					", isComplete=" + isComplete() +
					", missedCells=" + missedCells +
					'}';
		}

		public void addNearbyPokemons(Collection<NearbyPokemonOuterClass.NearbyPokemon> nearbyPokemons) {
			if (nearbyPokemons == null) {
				return;
			}
			this.nearbyPokemons.addAll(nearbyPokemons);
		}

		public void addCatchablePokemons(Collection<MapPokemonOuterClass.MapPokemon> catchablePokemons) {
			if (catchablePokemons == null) {
				return;
			}
			this.catchablePokemons.addAll(catchablePokemons);
		}

		public void addWildPokemons(Collection<WildPokemonOuterClass.WildPokemon> wildPokemons) {
			if (wildPokemons == null) {
				return;
			}
			this.wildPokemons.addAll(wildPokemons);
		}

		public void addDecimatedSpawnPoints(Collection<SpawnPointOuterClass.SpawnPoint> decimatedSpawnPoints) {
			if (decimatedSpawnPoints == null) {
				return;
			}
			this.decimatedSpawnPoints.addAll(decimatedSpawnPoints);
		}

		public void addSpawnPoints(Collection<SpawnPointOuterClass.SpawnPoint> spawnPoints) {
			if (spawnPoints == null) {
				return;
			}
			this.spawnPoints.addAll(spawnPoints);
		}

		public void addGyms(Collection<FortData> gyms) {
			if (gyms == null) {
				return;
			}
			this.gyms.addAll(gyms);
		}

		public void addPokestops(Collection<FortData> pokestops) {
			if (pokestops == null) {
				return;
			}
			this.pokestops.addAll(pokestops);
		}

		public boolean isComplete() {
			return missedCells.size() == 0;
		}

		public Collection<Long> getMissedCells() {
			return missedCells;
		}

		public void setMissedCells(List<Long> missedCells) {
			this.missedCells = missedCells;
		}
	}
}
