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

import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Inventory.EggIncubatorOuterClass;
import POGOProtos.Inventory.InventoryItemDataOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Inventory.Item.ItemDataOuterClass.ItemData;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class Inventories {
	private final Networking networking;
	@Getter
	private final ItemBag itemBag;
	@Getter
	private final PokeBank pokebank;
	@Getter
	private final CandyJar candyjar;
	@Getter
	private final Pokedex pokedex;
	private final Map<String, EggIncubator> incubators = new ConcurrentHashMap<>();
	@Getter
	private final Hatchery hatchery;
	@Getter
	private final Stats stats;

	/**
	 * Creates Inventories and initializes content.
	 *
	 */
	public Inventories(GetInventoryResponse getInventoryResponse, Networking networking, PlayerProfile playerProfile,
					   ExecutorService executorService) {
		this.networking = networking;
		itemBag = new ItemBag(getInventoryResponse, networking);
		pokebank = new PokeBank(getInventoryResponse, this, networking, playerProfile);
		candyjar = new CandyJar(getInventoryResponse);
		pokedex = new Pokedex(getInventoryResponse);
		hatchery = new Hatchery(getInventoryResponse, this, executorService);
		stats = new Stats(getInventoryResponse);
		updateInventories(getInventoryResponse);
	}

	public final void update(GetInventoryResponse getInventoryResponse) {
		itemBag.update(getInventoryResponse);
		pokebank.update(getInventoryResponse);
		candyjar.update(getInventoryResponse);
		pokebank.update(getInventoryResponse);
		hatchery.update(getInventoryResponse);
		stats.update(getInventoryResponse);
	}

	public final void update(GetHatchedEggsResponse response) {
		hatchery.update(response);
	}

	private void updateInventories(GetInventoryResponse response) {
		for (InventoryItemOuterClass.InventoryItem inventoryItem
				: response.getInventoryDelta().getInventoryItemsList()) {
			InventoryItemDataOuterClass.InventoryItemData itemData = inventoryItem.getInventoryItemData();

			if (itemData.hasEggIncubators()) {
				List<String> currentIncubators = new ArrayList<>(itemData.getEggIncubators().getEggIncubatorCount());
				for (EggIncubatorOuterClass.EggIncubator incubator : itemData.getEggIncubators().getEggIncubatorList()) {
					currentIncubators.add(incubator.getId());
					incubators.put(incubator.getId(), new EggIncubator(networking, this, incubator));
				}
				incubators.keySet().retainAll(currentIncubators);
			}
		}
	}

	public Collection<EggIncubator> getIncubators() {
		return incubators.values();
	}
}
