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
import POGOProtos.Networking.Responses.GetHoloInventoryResponseOuterClass.GetHoloInventoryResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.main.ServerRequestEnvelope;
import com.pokegoapi.main.ServerResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokeBank {
	@Getter
	public final List<Pokemon> pokemons = Collections.synchronizedList(new ArrayList<Pokemon>());
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
	 * Remove pokemon by ID.
	 *
	 * @param pokemonID the pokemon id to remove.
	 */
	public void removePokemon(final long pokemonID) {
		synchronized (this.lock) {
			List<Pokemon> previous = new ArrayList<>();
			previous.addAll(pokemons);

			pokemons.clear();
			pokemons.addAll(Stream.of(previous).filter(new Predicate<Pokemon>() {
				@Override
				public boolean test(Pokemon pokemon) {
					return pokemon.getId() != pokemonID;
				}
			}).collect(Collectors.<Pokemon>toList()));
		}
	}

	/**
	 * Remove pokemon.
	 *
	 * @param pokemon the pokemon to remove.
	 */
	public void removePokemon(final Pokemon pokemon) {
		removePokemon(pokemon.getId());
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
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public Map<PokemonFamilyId, Integer> releasePokemon(Pokemon... releasePokemon) throws RequestFailedException {
		ReleasePokemonMessage.Builder releaseBuilder = ReleasePokemonMessage.newBuilder();
		for (Pokemon pokemon : releasePokemon) {
			if (!pokemon.isDeployed() && !pokemon.isFavorite()) {
				releaseBuilder.addPokemonIds(pokemon.getId());
			}
		}
		ServerRequest releaseRequest = new ServerRequest(RequestType.RELEASE_POKEMON, releaseBuilder.build());
		ServerRequestEnvelope envelope = ServerRequestEnvelope.createCommons(releaseRequest, api);
		Map<PokemonFamilyId, Integer> lastCandies = new HashMap<>(api.inventories.candyjar.getCandies());
		ServerResponse response = api.requestHandler.sendServerRequests(envelope);
		try {
			ByteString inventoryData = response.get(RequestType.GET_HOLOHOLO_INVENTORY);
			GetHoloInventoryResponse inventoryResponse = GetHoloInventoryResponse.parseFrom(inventoryData);
			ReleasePokemonResponse releaseResponse = ReleasePokemonResponse.parseFrom(releaseRequest.getData());
			Map<PokemonFamilyId, Integer> candyCount = new HashMap<>();
			if (releaseResponse.getResult() == Result.SUCCESS && inventoryResponse.getSuccess()) {
				synchronized (this.lock) {
					this.pokemons.removeAll(Arrays.asList(releasePokemon));
				}
				for (Pokemon pokemon : releasePokemon) {
					api.inventories.pokebank.removePokemon(pokemon);
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
				api.inventories.updateInventories(inventoryResponse);
			}
			return candyCount;
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * Gets the amount of pokemon in the PokeBank, including the egg count
	 *
	 * @return the amount of pokemon in the PokeBank
	 */
	public int size() {
		return pokemons.size() + api.inventories.hatchery.eggs.size();
	}

	/**
	 * @return the maximum amount of pokemon this pokebank can store
	 */
	public int getMaxStorage() {
		return api.playerProfile.getPlayerData().getMaxPokemonStorage();
	}
}
