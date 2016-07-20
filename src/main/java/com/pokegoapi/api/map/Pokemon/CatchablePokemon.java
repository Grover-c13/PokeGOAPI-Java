package com.pokegoapi.api.map.Pokemon;

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.ItemIdOuterClass;
import POGOProtos.Inventory.ItemIdOuterClass.ItemId;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.ToString;

@ToString
public class CatchablePokemon {
	private MapPokemon proto;
	private Map map;

	public CatchablePokemon(MapPokemon proto, Map map) {
		this.proto = proto;
	}

	public String getSpawnpointId() {
		return proto.getSpawnpointId();
	}

	public long getEncounterId() {
		return proto.getEncounterId();
	}

	public PokemonIdOuterClass.PokemonId getPokemonId() {
		return proto.getPokemonId();
	}

	public long getExpirationTimestampMs() {
		return proto.getExpirationTimestampMs();
	}

	public double getLatitude() {
		return proto.getLatitude();
	}

	public double getLongitude() {
		return proto.getLongitude();
	}


	public void catchPokemon() throws LoginFailedException, RemoteServerException {
		catchPokemon(Pokeball.POKEBALL);
	}

	public void catchPokemon(Pokeball balltype) throws LoginFailedException, RemoteServerException {
		map.catchPokemon(proto, 1.0, 1.95 + Math.random() * 0.05, 0.85 + Math.random() * 0.15, balltype.ordinal());
	}


}
