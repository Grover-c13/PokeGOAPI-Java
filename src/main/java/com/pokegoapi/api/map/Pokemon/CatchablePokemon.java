package com.pokegoapi.api.map.Pokemon;

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import lombok.ToString;

@ToString
public class CatchablePokemon {
	private static final String TAG = CatchablePokemon.class.getSimpleName();
	private MapPokemon proto;
	private Map map;

	public CatchablePokemon(MapPokemon proto, Map map) {
		this.map = map;
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

		// encounter
		EncounterResponse encounterResponse = map.encounterPokemon(proto);
		Log.i(TAG, "Encounter Response: " + encounterResponse.toString());
		CatchResult cresult;
		if (encounterResponse.getStatus() == EncounterResponse.Status.ENCOUNTER_SUCCESS)
		{
			CatchPokemonResponse result = map.catchPokemon(proto, 1.0, 1.95 + Math.random() * 0.05, 0.85 + Math.random() * 0.15, balltype.ordinal());
			cresult = new CatchResult(result);
		}
		cresult = new CatchResult();
		cresult.setFailed(true);
		return cresult;
	}


}
