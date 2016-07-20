package com.pokegoapi.api.map;

import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass;
import lombok.Getter;
import lombok.ToString;
import POGOProtos.Map.SpawnPointOuterClass;

import java.util.ArrayList;
import java.util.Collection;

@ToString
public class MapObjects {
	@Getter Collection<NearbyPokemonOuterClass.NearbyPokemon> nearbyPokemons = new ArrayList<NearbyPokemonOuterClass.NearbyPokemon>();
	@Getter Collection<MapPokemonOuterClass.MapPokemon> catchablePokemons = new ArrayList<MapPokemonOuterClass.MapPokemon>();
	@Getter Collection<WildPokemonOuterClass.WildPokemon> wildPokemons = new ArrayList<WildPokemonOuterClass.WildPokemon>();
	@Getter Collection<SpawnPointOuterClass.SpawnPoint> decimatedSpawnPoints = new ArrayList<SpawnPointOuterClass.SpawnPoint>();
	@Getter Collection<SpawnPointOuterClass.SpawnPoint> spawnPoints = new ArrayList<SpawnPointOuterClass.SpawnPoint>();
	@Getter Collection<FortDataOuterClass.FortData> gyms = new ArrayList<FortDataOuterClass.FortData>();
	@Getter Collection<FortDataOuterClass.FortData> pokestops = new ArrayList<FortDataOuterClass.FortData>();
	boolean complete = false;
	
	public void addNearbyPokemons(Collection<NearbyPokemonOuterClass.NearbyPokemon> nearbyPokemons) {
		if (nearbyPokemons == null || nearbyPokemons.isEmpty()) {
			return;
		}
		complete = true;
		this.nearbyPokemons.addAll(nearbyPokemons);
	}

	public void addCatchablePokemons(Collection<MapPokemonOuterClass.MapPokemon> catchablePokemons) {
		if (catchablePokemons == null || catchablePokemons.isEmpty()) {
			return;
		}
		complete = true;
		this.catchablePokemons.addAll(catchablePokemons);
	}

	public void addWildPokemons(Collection<WildPokemonOuterClass.WildPokemon> wildPokemons) {
		if (wildPokemons == null || wildPokemons.isEmpty()) {
			return;
		}
		complete = true;
		this.wildPokemons.addAll(wildPokemons);
	}

	public void addDecimatedSpawnPoints(Collection<SpawnPointOuterClass.SpawnPoint> decimatedSpawnPoints) {
		if (decimatedSpawnPoints == null || decimatedSpawnPoints.isEmpty()) {
			return;
		}
		complete = true;
		this.decimatedSpawnPoints.addAll(decimatedSpawnPoints);
	}

	public void addSpawnPoints(Collection<SpawnPointOuterClass.SpawnPoint> spawnPoints) {
		if (spawnPoints == null || spawnPoints.isEmpty()) {
			return;
		}
		complete = true;
		this.spawnPoints.addAll(spawnPoints);
	}

	public void addGyms(Collection<FortDataOuterClass.FortData> gyms) {
		if (gyms == null || gyms.isEmpty()) {
			return;
		}
		complete = true;
		this.gyms.addAll(gyms);
	}

	public void addPokestops(Collection<FortDataOuterClass.FortData> pokestops) {
		if (pokestops == null || pokestops.isEmpty()) {
			return;
		}
		complete = true;
		this.pokestops.addAll(pokestops);
	}

	/**
	 * Returns whether or not the return returned any data at all; when a user requests too many cells/wrong cell level/cells too far away from the users location,
	 * the server returns empty MapCells
	 */
	public boolean isComplete() {
		return complete;
	}
}