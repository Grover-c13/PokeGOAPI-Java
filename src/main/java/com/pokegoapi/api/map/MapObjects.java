package com.pokegoapi.api.map;

import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Map.MapCellOuterClass;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass;
import POGOProtos.Map.SpawnPointOuterClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class MapObjects {
	private Collection<NearbyPokemonOuterClass.NearbyPokemon> nearbyPokemons = new ArrayList<NearbyPokemonOuterClass.NearbyPokemon>();
	private Collection<MapPokemonOuterClass.MapPokemon> catchablePokemons = new ArrayList<MapPokemonOuterClass.MapPokemon>();
	private Collection<WildPokemonOuterClass.WildPokemon> wildPokemons = new ArrayList<WildPokemonOuterClass.WildPokemon>();
	private Collection<SpawnPointOuterClass.SpawnPoint> decimatedSpawnPoints = new ArrayList<SpawnPointOuterClass.SpawnPoint>();
	private Collection<SpawnPointOuterClass.SpawnPoint> spawnPoints = new ArrayList<SpawnPointOuterClass.SpawnPoint>();
	private Collection<FortDataOuterClass.FortData> gyms = new ArrayList<FortDataOuterClass.FortData>();
	private Collection<FortDataOuterClass.FortData> pokestops = new ArrayList<FortDataOuterClass.FortData>();
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