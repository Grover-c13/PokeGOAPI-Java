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
import POGOProtos.Inventory.CandyOuterClass.Candy;
import POGOProtos.Inventory.InventoryItemDataOuterClass.InventoryItemData;
import POGOProtos.Inventory.InventoryItemOuterClass.InventoryItem;
import POGOProtos.Networking.Requests.Messages.ReleasePokemonMessageOuterClass.ReleasePokemonMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.exceptions.hash.HashException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.main.ServerRequestEnvelope;
import com.pokegoapi.main.ServerResponse;
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
	 *
	 * @param releasePokemon the pokemon to release
	 * @return the amount of candies for each pokemon family
	 * @throws CaptchaActiveException if a captcha is active and a message cannot be sent
	 * @throws LoginFailedException the login fails
	 * @throws RemoteServerException if the server errors
	 * @throws HashException if an exception occurred while requesting hash
	 */
	public Map<PokemonFamilyId, Integer> releasePokemon(Pokemon... releasePokemon)
			throws CaptchaActiveException, LoginFailedException, RemoteServerException, HashException {
		ReleasePokemonMessage.Builder releaseBuilder = ReleasePokemonMessage.newBuilder();
		for (Pokemon pokemon : releasePokemon) {
			if (!pokemon.isDeployed() && !pokemon.isFavorite()) {
				releaseBuilder.addPokemonIds(pokemon.getId());
			}
		}
		ServerRequestEnvelope envelope = ServerRequestEnvelope.createCommons();
		ServerRequest releaseRequest = envelope.add(RequestType.RELEASE_POKEMON, releaseBuilder.build());
		Map<PokemonFamilyId, Integer> lastCandies = new HashMap<>(api.getInventories().getCandyjar().getCandies());
		ServerResponse response = api.getRequestHandler().sendServerRequests(envelope);
		try {
			GetInventoryResponse inventoryResponse = GetInventoryResponse.parseFrom(response.get(RequestType.GET_INVENTORY));
			ReleasePokemonResponse releaseResponse = ReleasePokemonResponse.parseFrom(releaseRequest.getData());
			Map<PokemonFamilyId, Integer> candyCount = new HashMap<>();
			if (releaseResponse.getResult() == Result.SUCCESS && inventoryResponse.getSuccess()) {
				synchronized (this.lock) {
					for (Pokemon pokemon : releasePokemon) {
						this.pokemons.remove(pokemon);
					}
				}
				for (Pokemon pokemon : releasePokemon) {
					api.getInventories().getPokebank().removePokemon(pokemon);
				}
				List<InventoryItem> items = inventoryResponse.getInventoryDelta().getInventoryItemsList();
				for (InventoryItem item : items) {
					InventoryItemData data = item.getInventoryItemData();
					if (data != null && data.hasCandy()) {
						Candy candy = data.getCandy();
						PokemonFamilyId family = candy.getFamilyId();
						Integer lastCandy = lastCandies.get(family);
						if (lastCandy == null) {
							lastCandy = 0;
						}
						candyCount.put(family, candy.getCandy() - lastCandy);
					}
				}
				api.getInventories().updateInventories(inventoryResponse);
			}
			return candyCount;
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	/**
	 * @return the maximum amount of pokemon this pokebank can store
	 */
	public int getMaxStorage() {
		return api.getPlayerProfile().getPlayerData().getMaxPokemonStorage();
	}
}
