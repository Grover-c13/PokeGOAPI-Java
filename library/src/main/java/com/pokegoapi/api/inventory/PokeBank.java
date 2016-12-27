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

package com.pokegoapi.api.inventory;

import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Networking.Requests.Messages.ReleasePokemonMessageOuterClass.ReleasePokemonMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokeBank {
	@Getter
	private final List<Pokemon> pokemons = Collections.synchronizedList(new ArrayList<Pokemon>());
	@Getter
	private final Object lock = new Object();
	@Getter
	private final PokemonGo api;

	public PokeBank(PokemonGo api) {
		this.api = api;
	}

	/**
	 * Resets the Pokebank and removes all pokemon
	 */
	public void reset() {
		synchronized (this.lock) {
			pokemons.clear();
		}
	}

	/**
	 * Add a pokemon to the pokebank inventory.  Will not add duplicates (pokemon with same id).
	 *
	 * @param pokemon Pokemon to add to the inventory
	 */
	public void addPokemon(final Pokemon pokemon) {
		synchronized (this.lock) {
			List<Pokemon> alreadyAdded = Stream.of(pokemons).filter(new Predicate<Pokemon>() {
				@Override
				public boolean test(Pokemon testPokemon) {
					return pokemon.getId() == testPokemon.getId();
				}
			}).collect(Collectors.<Pokemon>toList());
			if (alreadyAdded.size() < 1) {
				pokemons.add(pokemon);
			}
		}
	}

	/**
	 * Gets pokemon by pokemon id.
	 *
	 * @param id the id
	 * @return the pokemon by pokemon id
	 */
	public List<Pokemon> getPokemonByPokemonId(final PokemonIdOuterClass.PokemonId id) {
		synchronized (this.lock) {
			return Stream.of(pokemons).filter(new Predicate<Pokemon>() {
				@Override
				public boolean test(Pokemon pokemon) {
					return pokemon.getPokemonId().equals(id);
				}
			}).collect(Collectors.<Pokemon>toList());
		}
	}

	/**
	 * Remove pokemon.
	 *
	 * @param pokemon the pokemon to remove.
	 */
	public void removePokemon(final Pokemon pokemon) {
		synchronized (this.lock) {
			List<Pokemon> previous = new ArrayList<>();
			previous.addAll(pokemons);

			pokemons.clear();
			pokemons.addAll(Stream.of(previous).filter(new Predicate<Pokemon>() {
				@Override
				public boolean test(Pokemon pokemn) {
					return pokemn.getId() != pokemon.getId();
				}
			}).collect(Collectors.<Pokemon>toList()));
		}
	}

	/**
	 * Get a pokemon by id.
	 *
	 * @param id the id
	 * @return the pokemon
	 */
	public Pokemon getPokemonById(final Long id) {
		synchronized (this.lock) {
			for (Pokemon pokemon : pokemons) {
				if (pokemon.getId() == id) {
					return pokemon;
				}
			}
		}
		return null;
	}

	/**
	 * Releases multiple pokemon in a single request
	 * @param pokemons the pokemon to release
	 * @return the responses for all the requests
	 * @throws CaptchaActiveException if a captcha is active and a message cannot be sent
	 * @throws LoginFailedException the login fails
	 * @throws RemoteServerException if the server errors
	 */
	public Map<PokemonFamilyId, ReleasePokemonResponse> releasePokemon(Pokemon... pokemons)
			throws CaptchaActiveException, LoginFailedException, RemoteServerException {
		Map<PokemonFamilyId, List<Long>> familyPokemon = new HashMap<>();
		for (Pokemon pokemon : pokemons) {
			List<Long> ids = familyPokemon.get(pokemon.getPokemonFamily());
			if (ids == null) {
				ids = new ArrayList<>();
				familyPokemon.put(pokemon.getPokemonFamily(), ids);
			}
			ids.add(pokemon.getId());
		}
		Map<PokemonFamilyId, ServerRequest> requests = new HashMap<>();
		ServerRequest[] requestArray = new ServerRequest[familyPokemon.size()];
		int index = 0;
		for (Map.Entry<PokemonFamilyId, List<Long>> entry : familyPokemon.entrySet()) {
			ReleasePokemonMessage message = ReleasePokemonMessage.newBuilder()
					.addAllPokemonIds(entry.getValue())
					.build();
			ServerRequest request = new ServerRequest(RequestType.RELEASE_POKEMON, message);
			requests.put(entry.getKey(), request);
			requestArray[index++] = request;
		}
		api.getRequestHandler().sendServerRequests(requestArray);
		try {
			Map<PokemonFamilyId, ReleasePokemonResponse> responses = new HashMap<>();
			for (Map.Entry<PokemonFamilyId, ServerRequest> entry : requests.entrySet()) {
				ServerRequest request = entry.getValue();
				ByteString data = request.getData();
				ReleasePokemonResponse response = ReleasePokemonResponse.parseFrom(data);
				responses.put(entry.getKey(), response);
			}
			return responses;
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}
}
