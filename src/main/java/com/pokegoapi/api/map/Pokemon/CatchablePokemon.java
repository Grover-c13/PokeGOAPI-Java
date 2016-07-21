package com.pokegoapi.api.map.Pokemon;

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Networking.Requests.Messages.CatchPokemonMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.EncounterMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;
import lombok.ToString;

@ToString
public class CatchablePokemon {
	private static final String TAG = CatchablePokemon.class.getSimpleName();
	private final PokemonGo api;
	private final MapPokemon proto;

	@Getter
	private boolean encountered = false;

	public CatchablePokemon(PokemonGo api, MapPokemon proto) {
		this.api = api;
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

	public EncounterResult encounterPokemon() throws LoginFailedException, RemoteServerException {
		EncounterMessageOuterClass.EncounterMessage reqMsg = EncounterMessageOuterClass.EncounterMessage.newBuilder()
				.setEncounterId(getEncounterId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setSpawnpointId(getSpawnpointId())
				.build();
		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.ENCOUNTER, reqMsg);
		api.getRequestHandler().request(serverRequest);
		api.getRequestHandler().sendServerRequests();
		EncounterResponseOuterClass.EncounterResponse response = null;
		try {
			response = EncounterResponseOuterClass.EncounterResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		encountered = response.getStatus() == EncounterResponse.Status.ENCOUNTER_SUCCESS;
		return new EncounterResult(response);
	}

	/**
	 * Tries to catch a pokemon with a pokeball
	 *
	 * @return CatchResult
	 * @throws LoginFailedException
	 * @throws RemoteServerException
	 */
	public CatchResult catchPokemon() throws LoginFailedException, RemoteServerException {
		return catchPokemon(Pokeball.POKEBALL);
	}


	/**
	 * Tries to catch a pokeball with the given type
	 *
	 * @param pokeball Type of pokeball
	 * @return CatchResult
	 * @throws LoginFailedException
	 * @throws RemoteServerException
	 */
	public CatchResult catchPokemon(Pokeball pokeball) throws LoginFailedException, RemoteServerException {
		return catchPokemon(pokeball, -1);
	}

	/**
	 * Tried to catch a pokemon with given pokeball and max number of pokeballs
	 *
	 * @param pokeball Type of pokeball
	 * @param amount   Max number of pokeballs to use
	 * @return CatchResult
	 * @throws LoginFailedException
	 * @throws RemoteServerException
	 */
	public CatchResult catchPokemon(Pokeball pokeball, int amount) throws LoginFailedException, RemoteServerException {
		return catchPokemon(1.0, 1.95 + Math.random() * 0.05, 0.85 + Math.random() * 0.15, pokeball, amount);
	}

	/**
	 * Tries to catch a pokemon.
	 *
	 * @param normalizedHitPosition
	 * @param normalizedReticleSize
	 * @param spinModifier
	 * @param type                  Type of pokeball to throw
	 * @param amount                Max number of Pokeballs to throw, negative number for unlimited
	 * @return CatchResult of resulted try to catch pokemon
	 * @throws LoginFailedException
	 * @throws RemoteServerException
	 */
	public CatchResult catchPokemon(double normalizedHitPosition, double normalizedReticleSize, double spinModifier, Pokeball type, int amount) throws LoginFailedException, RemoteServerException {
		if (!isEncountered())
			return new CatchResult();

		int numThrows = 0;
		CatchPokemonResponseOuterClass.CatchPokemonResponse response = null;
		do {
			CatchPokemonMessageOuterClass.CatchPokemonMessage reqMsg = CatchPokemonMessageOuterClass.CatchPokemonMessage.newBuilder()
					.setEncounterId(getEncounterId())
					.setHitPokemon(true)
					.setNormalizedHitPosition(normalizedHitPosition)
					.setNormalizedReticleSize(normalizedReticleSize)
					.setSpawnPointGuid(getSpawnpointId())
					.setSpinModifier(spinModifier)
					.setPokeball(type.getBalltype())
					.build();
			ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.CATCH_POKEMON, reqMsg);
			api.getRequestHandler().request(serverRequest);
			api.getRequestHandler().sendServerRequests();

			try {
				response = CatchPokemonResponseOuterClass.CatchPokemonResponse.parseFrom(serverRequest.getData());
			} catch (InvalidProtocolBufferException e) {
				throw new RemoteServerException(e);
			}

			if (response.getStatus() != CatchPokemonResponse.CatchStatus.CATCH_ESCAPE && response.getStatus() != CatchPokemonResponse.CatchStatus.CATCH_MISSED) {
				break;
			}
			numThrows++;
		} while (amount < 0 || numThrows < amount);

		return new CatchResult(response);
	}


}
