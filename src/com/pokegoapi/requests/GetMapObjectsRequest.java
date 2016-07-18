package com.pokegoapi.requests;

import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Map.Fort.FortTypeOuterClass;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetMapObjectsRequest extends Request {
	private GetMapObjectsMessageOuterClass.GetMapObjectsMessage.Builder builder;
	private GetMapObjectsResponseOuterClass.GetMapObjectsResponse output;

	public class GetMapObjectsReply {
		private Collection<NearbyPokemonOuterClass.NearbyPokemon> nearbyPokemons;
		private Collection<MapPokemonOuterClass.MapPokemon> catchablePokemons;
		private Collection<WildPokemonOuterClass.WildPokemon> wildPokemons;
		private Collection<SpawnPointOuterClass.SpawnPoint> decimatedSpawnPoints;
		private Collection<SpawnPointOuterClass.SpawnPoint> spawnPoints;
		private Collection<FortDataOuterClass.FortData> gyms;
		private Collection<FortDataOuterClass.FortData> pokestops;

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

		public Collection<FortDataOuterClass.FortData> getGyms() {
			return gyms;
		}

		public Collection<FortDataOuterClass.FortData> getPokestops() {
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
					'}';
		}

		GetMapObjectsReply() {
			nearbyPokemons = new ArrayList<>();
			catchablePokemons = new ArrayList<>();
			wildPokemons = new ArrayList<>();
			decimatedSpawnPoints = new ArrayList<>();
			spawnPoints = new ArrayList<>();
			gyms = new ArrayList<>();
			pokestops = new ArrayList<>();
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

		public void addGyms(Collection<FortDataOuterClass.FortData> gyms) {
			if (gyms == null) {
				return;
			}
			this.gyms.addAll(gyms);
		}

		public void addPokestops(Collection<FortDataOuterClass.FortData> pokestops) {
			if (pokestops == null) {
				return;
			}
			this.pokestops.addAll(pokestops);
		}
	}

	public GetMapObjectsRequest(List<Long> cellIds, double latitude, double longitude) {
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
		output.getMapCellsList().forEach(mapCell -> {
			result.addNearbyPokemons(mapCell.getNearbyPokemonsList());
			result.addCatchablePokemons(mapCell.getCatchablePokemonsList());
			result.addWildPokemons(mapCell.getWildPokemonsList());
			result.addDecimatedSpawnPoints(mapCell.getDecimatedSpawnPointsList());
			result.addSpawnPoints(mapCell.getSpawnPointsList());
			Map<FortTypeOuterClass.FortType, List<FortDataOuterClass.FortData>> groupedForts
					= mapCell.getFortsList().stream().collect(Collectors.groupingBy(FortDataOuterClass.FortData::getType));
			result.addGyms(groupedForts.get(FortTypeOuterClass.FortType.GYM));
			result.addPokestops(groupedForts.get(FortTypeOuterClass.FortType.CHECKPOINT));

		});
		return result;
	}
}
