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

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Inventory.AppliedItemOuterClass.AppliedItem;
import POGOProtos.Inventory.AppliedItemsOuterClass.AppliedItems;
import POGOProtos.Inventory.EggIncubatorOuterClass;
import POGOProtos.Inventory.EggIncubatorsOuterClass.EggIncubators;
import POGOProtos.Inventory.InventoryItemDataOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Inventory.Item.ItemDataOuterClass.ItemData;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Inventories {

	private final PokemonGo api;
	@Getter
	private ItemBag itemBag;
	@Getter
	private PokeBank pokebank;
	@Getter
	private CandyJar candyjar;
	@Getter
	private Pokedex pokedex;
	@Getter
	private final List<EggIncubator> incubators = Collections.synchronizedList(new ArrayList<EggIncubator>());
	@Getter
	private Hatchery hatchery;
	@Getter
	private long lastInventoryUpdate = 0;

	private Map<ItemId, AppliedItem> appliedItems = new HashMap<>();

	@Getter
	private final Object lock = new Object();

	/**
	 * Creates Inventories and initializes content.
	 *
	 * @param api PokemonGo api
	 */
	public Inventories(PokemonGo api) {
		this.api = api;
		itemBag = new ItemBag(api);
		pokebank = new PokeBank(api);
		candyjar = new CandyJar(api);
		pokedex = new Pokedex();
		hatchery = new Hatchery(api);
	}

	/**
	 * Updates the inventories with latest data.
	 *
	 * @return the response to the update message
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public GetInventoryResponse updateInventories() throws RequestFailedException {
		return updateInventories(false);
	}

	/**
	 * Updates the inventories with the latest data.
	 *
	 * @param forceUpdate For a full update if true
	 * @return the response to the update message
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public GetInventoryResponse updateInventories(boolean forceUpdate)
			throws RequestFailedException {
		if (forceUpdate) {
			lastInventoryUpdate = 0;
			itemBag.reset();
			pokebank.reset();
			candyjar.reset();
			pokedex.reset();
			synchronized (this.lock) {
				incubators.clear();
			}
			hatchery.reset();
		}
		GetInventoryMessage invReqMsg = GetInventoryMessage.newBuilder()
				.setLastTimestampMs(lastInventoryUpdate)
				.build();
		ServerRequest inventoryRequest = new ServerRequest(RequestTypeOuterClass.RequestType.GET_INVENTORY, invReqMsg);
		api.getRequestHandler().sendServerRequests(inventoryRequest);

		GetInventoryResponse response;
		try {
			response = GetInventoryResponse.parseFrom(inventoryRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}

		return response;
	}

	/**
	 * Updates the inventories with the latest data.
	 *
	 * @param response the get inventory response
	 */
	public void updateInventories(GetInventoryResponse response) {
		lastInventoryUpdate = api.currentTimeMillis();

		for (InventoryItemOuterClass.InventoryItem inventoryItem
				: response.getInventoryDelta().getInventoryItemsList()) {
			InventoryItemDataOuterClass.InventoryItemData itemData = inventoryItem.getInventoryItemData();

			// hatchery
			PokemonData pokemonData = itemData.getPokemonData();
			if (pokemonData.getPokemonId() == PokemonId.MISSINGNO && pokemonData.getIsEgg()) {
				hatchery.addEgg(new EggPokemon(pokemonData));
			}

			// pokebank
			if (pokemonData.getPokemonId() != PokemonId.MISSINGNO) {
				pokebank.addPokemon(new Pokemon(api, inventoryItem.getInventoryItemData().getPokemonData()));
			}

			// items
			if (itemData.getItem().getItemId() != ItemId.UNRECOGNIZED
					&& itemData.getItem().getItemId() != ItemId.ITEM_UNKNOWN) {
				ItemData item = itemData.getItem();
				if (item.getCount() > 0) {
					itemBag.addItem(new Item(api, item, itemBag));
				}
			}

			// candyjar
			if (itemData.getCandy().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.UNRECOGNIZED
					&& itemData.getCandy().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.FAMILY_UNSET) {
				candyjar.setCandy(
						itemData.getCandy().getFamilyId(),
						itemData.getCandy().getCandy()
				);
			}
			// player stats
			if (itemData.hasPlayerStats()) {
				api.getPlayerProfile().setStats(new Stats(itemData.getPlayerStats()));
			}

			// pokedex
			if (itemData.hasPokedexEntry()) {
				pokedex.add(itemData.getPokedexEntry());
			}

			if (itemData.hasEggIncubators()) {
				EggIncubators eggIncubators = itemData.getEggIncubators();
				for (EggIncubatorOuterClass.EggIncubator incubator : eggIncubators.getEggIncubatorList()) {
					EggIncubator eggIncubator = new EggIncubator(api, incubator);
					synchronized (this.lock) {
						incubators.remove(eggIncubator);
						incubators.add(eggIncubator);
					}
				}
			}

			if (itemData.hasAppliedItems()) {
				AppliedItems appliedItems = itemData.getAppliedItems();
				for (AppliedItem appliedItem : appliedItems.getItemList()) {
					this.appliedItems.put(appliedItem.getItemId(), appliedItem);
				}
			}

			Set<ItemId> stale = new HashSet<>();
			for (Map.Entry<ItemId, AppliedItem> entry : appliedItems.entrySet()) {
				ItemId itemId = entry.getKey();
				AppliedItem applied = entry.getValue();
				if (api.currentTimeMillis() >= applied.getExpireMs()) {
					stale.add(itemId);
				} else {
					Item item = itemBag.getItem(itemId);
					item.setApplied(applied);
					itemBag.addItem(item);
				}
			}

			for (ItemId item : stale) {
				appliedItems.remove(item);
				itemBag.getItem(item).removeApplied();
			}
		}
	}
}
