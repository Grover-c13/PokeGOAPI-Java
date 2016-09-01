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
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.api.pokemon.Pokemon;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


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
	private final ConcurrentHashMap<String, EggIncubator> incubators = new ConcurrentHashMap<>();
	@Getter
	private Hatchery hatchery;
	@Getter
	private long lastInventoryUpdate = 0;

	/**
	 * Creates Inventories and initializes content.
	 *
	 * @param api PokemonGo api
	 */
	public Inventories(PokemonGo api) {
		this.api = api;
		itemBag = new ItemBag(api);
		pokebank = new PokeBank();
		candyjar = new CandyJar(api);
		pokedex = new Pokedex();
		hatchery = new Hatchery(api);
	}

	/**
	 * Updates the inventories with the latest data.
	 *
	 * @param response the get inventory response
	 */
	public void updateInventories(GetInventoryResponse response) {
		for (InventoryItemOuterClass.InventoryItem inventoryItem
				: response.getInventoryDelta().getInventoryItemsList()) {
			InventoryItemDataOuterClass.InventoryItemData itemData = inventoryItem.getInventoryItemData();

			// hatchery
			if (itemData.getPokemonData().getPokemonId() == PokemonId.MISSINGNO && itemData.getPokemonData().getIsEgg()) {
				hatchery.addEgg(new EggPokemon(itemData.getPokemonData()));
			}

			// pokebank
			if (itemData.getPokemonData().getPokemonId() != PokemonId.MISSINGNO) {
				pokebank.addPokemon(new Pokemon(api, inventoryItem.getInventoryItemData().getPokemonData()));
			}

			// items
			if (itemData.getItem().getItemId() != ItemId.UNRECOGNIZED
					&& itemData.getItem().getItemId() != ItemId.ITEM_UNKNOWN) {
				itemBag.addItem(new Item(itemData.getItem()));
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
				for (EggIncubatorOuterClass.EggIncubator incubator : itemData.getEggIncubators().getEggIncubatorList()) {
					incubators.put(incubator.getId(),new EggIncubator(api, incubator));
				}
			}
		}

		lastInventoryUpdate = response.getInventoryDelta().getNewTimestampMs();
	}
}
