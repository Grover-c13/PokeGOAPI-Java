package com.pokegoapi.api.map.Pokemon;

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse;
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


	public CatchResult catchPokemon() throws LoginFailedException, RemoteServerException {
		return catchPokemon(Pokeball.POKEBALL);
	}

	public CatchResult catchPokemon(Pokeball balltype) throws LoginFailedException, RemoteServerException {
		CatchPokemonResponse result = map.catchPokemon(proto, 1.0, 1.95 + Math.random() * 0.05, 0.85 + Math.random() * 0.15, balltype.ordinal());
		return new CatchResult(result);
	}


}
