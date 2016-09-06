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

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.InventoryItemDataOuterClass.InventoryItemData;
import POGOProtos.Inventory.InventoryItemOuterClass.InventoryItem;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pokebank contains all pokemon
 */
public class PokeBank {
	private final Networking networking;
	private final Inventories inventories;
	private final PlayerProfile playerProfile;
	private final Map<Long, Pokemon> pokemons = new ConcurrentHashMap<>();

	/**
	 * Constructor for internal use
	 *
	 * @param getInventoryResponse Inventory response, for inital loading
	 * @param inventories          Inventories is used to pass to pokemon objects, so they can be caught with pokeballs
	 * @param networking           Networking is used to pass to pokemon objects, so they can be caught
	 * @param playerProfile        Player profile is used for the pokemon objects to do CP calculations
	 */
	public PokeBank(GetInventoryResponse getInventoryResponse, Inventories inventories, Networking networking,
					PlayerProfile playerProfile) {
		this.networking = networking;
		this.inventories = inventories;
		this.playerProfile = playerProfile;
		update(getInventoryResponse);
	}

	/**
	 * Update object
	 *
	 * @param getInventoryResponse for finding pokemons in the inventory
	 */
	final void update(GetInventoryResponse getInventoryResponse) {
		List<Long> currentItems = new LinkedList<>();
		for (InventoryItem inventoryItem : getInventoryResponse.getInventoryDelta().getInventoryItemsList()) {
			InventoryItemData itemData = inventoryItem.getInventoryItemData();
			if (itemData.getPokemonData().getPokemonId() != PokemonIdOuterClass.PokemonId.MISSINGNO) {
				Pokemon pokemon = new Pokemon(networking, inventories, playerProfile,
						inventoryItem.getInventoryItemData().getPokemonData());
				pokemons.put(pokemon.getId(), pokemon);
				currentItems.add(pokemon.getId());
			}
		}
		pokemons.keySet().retainAll(currentItems);
	}

	/**
	 * Gets pokemon by pokemon id.
	 *
	 * @param id the id
	 * @return the pokemon by pokemon id
	 */
	public List<Pokemon> getPokemonByPokemonId(final PokemonIdOuterClass.PokemonId id) {
		return Stream.of(pokemons.values()).filter(new Predicate<Pokemon>() {
			@Override
			public boolean test(Pokemon pokemon) {
				return pokemon.getPokemonId().equals(id);
			}
		}).collect(Collectors.<Pokemon>toList());
	}

	/**
	 * Get a pokemon by id.
	 *
	 * @param id the id
	 * @return the pokemon
	 */
	public Pokemon getPokemonById(final long id) {
		return pokemons.get(id);
	}

	public List<Pokemon> getPokemons() {
		// For compatibility
		return new ArrayList<>(pokemons.values());
	}
}
