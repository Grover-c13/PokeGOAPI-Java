/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.map.pokemon;

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.ItemIdOuterClass;
import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Fort.FortLureInfoOuterClass;
import POGOProtos.Map.Fort.FortLureInfoOuterClass.FortLureInfo;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass.WildPokemon;
import POGOProtos.Networking.Requests.Messages.CatchPokemonMessageOuterClass.CatchPokemonMessage;
import POGOProtos.Networking.Requests.Messages.EncounterMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;
import lombok.Getter;
import lombok.ToString;

/**
 * The type Catchable pokemon.
 */
@ToString
public class CatchablePokemon {
	private static final String TAG = CatchablePokemon.class.getSimpleName();
	private final PokemonGo api;

	@Getter
	private final String spawnPointId;
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

	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api   the api
	 * @param proto the proto
	 */
	public CatchablePokemon(PokemonGo api, MapPokemon proto) {
		this.api = api;

		this.spawnPointId = proto.getSpawnPointId();
		this.encounterId = proto.getEncounterId();
		this.pokemonId = proto.getPokemonId();
		this.expirationTimestampMs = proto.getExpirationTimestampMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
	}


	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api   the api
	 * @param proto the proto
	 */
	public CatchablePokemon(PokemonGo api, WildPokemon proto) {
		this.api = api;
		this.spawnPointId = proto.getSpawnPointId();
		this.encounterId = proto.getEncounterId();
		this.pokemonId = proto.getPokemonData().getPokemonId();
		this.expirationTimestampMs = proto.getTimeTillHiddenMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
	}

	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api   the api
	 * @param proto the proto
	 */
	public CatchablePokemon(PokemonGo api, FortData proto) {
		if (!proto.hasLureInfo()) {
			throw new IllegalArgumentException("Fort does not have lure");
		}
		this.api = api;
		// TODO: does this work?
		this.spawnPointId = null;
		this.encounterId = proto.getLureInfo().getEncounterId();
		this.pokemonId = proto.getLureInfo().getActivePokemonId();
		this.expirationTimestampMs = proto.getLureInfo().getLureExpiresTimestampMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
	}

	/**
	 * Encounter pokemon encounter result.
	 *
	 * @return the encounter result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public EncounterResult encounterPokemon() throws LoginFailedException, RemoteServerException {
		EncounterMessageOuterClass.EncounterMessage reqMsg = EncounterMessageOuterClass.EncounterMessage.newBuilder()
				.setEncounterId(getEncounterId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setSpawnPointId(getSpawnPointId())
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
	 * Tries to catch a pokemon (will attempt to use a pokeball, if you have none will use greatball etc).
	 *
	 * @return CatchResult
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemon() throws LoginFailedException, RemoteServerException {
		Pokeball pokeball;

		ItemBag bag = api.getInventories().getItemBag();
		if (bag.getItem(ItemIdOuterClass.ItemId.ITEM_POKE_BALL).getCount() > 0) {
			pokeball = Pokeball.POKEBALL;
		} else if (bag.getItem(ItemIdOuterClass.ItemId.ITEM_GREAT_BALL).getCount() > 0) {
			pokeball = Pokeball.GREATBALL;
		} else if (bag.getItem(ItemIdOuterClass.ItemId.ITEM_ULTRA_BALL).getCount() > 0) {
			pokeball = Pokeball.ULTRABALL;
		} else {
			pokeball = Pokeball.MASTERBALL;
		}

		return catchPokemon(pokeball);
	}


	/**
	 * Tries to catch a pokeball with the given type.
	 *
	 * @param pokeball Type of pokeball
	 * @return CatchResult
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemon(Pokeball pokeball) throws LoginFailedException, RemoteServerException {
		return catchPokemon(pokeball, -1);
	}

	/**
	 * Tried to catch a pokemon with given pokeball and max number of pokeballs.
	 *
	 * @param pokeball Type of pokeball
	 * @param amount   Max number of pokeballs to use
	 * @return CatchResult
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemon(Pokeball pokeball, int amount) throws LoginFailedException, RemoteServerException {
		return catchPokemon(1.0, 1.95 + Math.random() * 0.05, 0.85 + Math.random() * 0.15, pokeball, amount);
	}

	/**
	 * Tries to catch a pokemon.
	 *
	 * @param normalizedHitPosition the normalized hit position
	 * @param normalizedReticleSize the normalized hit reticle
	 * @param spinModifier          the spin modifier
	 * @param type                  Type of pokeball to throw
	 * @param amount                Max number of Pokeballs to throw, negative number for unlimited
	 * @return CatchResult of resulted try to catch pokemon
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemon(
			double normalizedHitPosition, double normalizedReticleSize, double spinModifier, Pokeball type, int amount)
			throws LoginFailedException, RemoteServerException {
		if (!isEncountered()) {
			return new CatchResult();
		}

		int numThrows = 0;
		CatchPokemonResponse response = null;
		do {
			CatchPokemonMessage reqMsg = CatchPokemonMessage.newBuilder()
					.setEncounterId(getEncounterId())
					.setHitPokemon(true)
					.setNormalizedHitPosition(normalizedHitPosition)
					.setNormalizedReticleSize(normalizedReticleSize)
					.setSpawnPointGuid(getSpawnPointId())
					.setSpinModifier(spinModifier)
					.setPokeball(type.getBalltype())
					.build();
			ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.CATCH_POKEMON, reqMsg);
			api.getRequestHandler().sendServerRequests(serverRequest);

			try {
				response = CatchPokemonResponse.parseFrom(serverRequest.getData());
			} catch (InvalidProtocolBufferException e) {
				throw new RemoteServerException(e);
			}

			if (response.getStatus() != CatchPokemonResponse.CatchStatus.CATCH_ESCAPE
					&& response.getStatus() != CatchPokemonResponse.CatchStatus.CATCH_MISSED) {
				break;
			}
			numThrows++;
		}
		while (amount < 0 || numThrows < amount);

		api.getInventories().updateInventories();

		return new CatchResult(response);
	}

}
