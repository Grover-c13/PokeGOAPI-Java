package com.pokegoapi.api.map.Pokemon;

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass.WildPokemon;
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

	@Getter
	private final String spawnpointId;
	@Getter
	private final long encounterId;
	@Getter
	private final PokemonIdOuterClass.PokemonId pokemonId;
	@Getter
	private final long expirationTimestampMs;
	@Getter
	private final double latitude;
	@Getter
	private final double longitude;

	@Getter
	private boolean encountered = false;

	public CatchablePokemon(PokemonGo api, MapPokemon proto) {
		this.api = api;

		this.spawnpointId = proto.getSpawnpointId();
		this.encounterId = proto.getEncounterId();
		this.pokemonId = proto.getPokemonId();
		this.expirationTimestampMs = proto.getExpirationTimestampMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
	}

	public CatchablePokemon(PokemonGo api, WildPokemon proto) {
		this.api = api;

		this.spawnpointId = proto.getSpawnpointId();
		this.encounterId = proto.getEncounterId();
		this.pokemonId = proto.getPokemonData().getPokemonId();
		this.expirationTimestampMs = proto.getTimeTillHiddenMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
	}


	public EncounterResult encounterPokemon() throws LoginFailedException, RemoteServerException {
		EncounterMessageOuterClass.EncounterMessage reqMsg = EncounterMessageOuterClass.EncounterMessage.newBuilder()
				.setEncounterId(getEncounterId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setSpawnpointId(getSpawnpointId())
				.build();
		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.ENCOUNTER, reqMsg);
		api.getRequestHandler().sendServerRequests(serverRequest);
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
			api.getRequestHandler().sendServerRequests(serverRequest);

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
