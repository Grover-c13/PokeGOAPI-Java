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
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


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
	private List<EggIncubator> incubators;
	@Getter
	private Hatchery hatchery;

	private long lastInventoryUpdate = 0;

	/**
	 * Creates Inventories and initializes content.
	 *
	 * @param api PokemonGo api
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public Inventories(PokemonGo api) throws LoginFailedException, RemoteServerException {
		this.api = api;
		itemBag = new ItemBag(api);
		pokebank = new PokeBank(api);
		candyjar = new CandyJar(api);
		pokedex = new Pokedex(api);
		incubators = new ArrayList<>();
		hatchery = new Hatchery(api);
		updateInventories();
	}

	/**
	 * Updates the inventories with latest data.
	 *
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void updateInventories() throws LoginFailedException, RemoteServerException {
		updateInventories(false);
	}

	/**
	 * Updates the inventories with the latest data.
	 *
	 * @param forceUpdate For a full update if true
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void updateInventories(boolean forceUpdate) throws LoginFailedException, RemoteServerException {
		if (forceUpdate) {
			lastInventoryUpdate = 0;
			itemBag = new ItemBag(api);
			pokebank = new PokeBank(api);
			candyjar = new CandyJar(api);
			pokedex = new Pokedex(api);
			incubators = new ArrayList<>();
			hatchery = new Hatchery(api);
		}
		GetInventoryMessage invReqMsg = GetInventoryMessage.newBuilder()
				.setLastTimestampMs(lastInventoryUpdate)
				.build();
		ServerRequest inventoryRequest = new ServerRequest(RequestTypeOuterClass.RequestType.GET_INVENTORY, invReqMsg);
		api.getRequestHandler().sendServerRequests(inventoryRequest);

		GetInventoryResponse response = null;
		try {
			response = GetInventoryResponse.parseFrom(inventoryRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

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
				ItemData item = itemData.getItem();
				itemBag.addItem(new Item(item));
			}

			// candyjar
			if (itemData.getPokemonFamily().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.UNRECOGNIZED
					&& itemData.getPokemonFamily().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.FAMILY_UNSET) {
				candyjar.setCandy(
						itemData.getPokemonFamily().getFamilyId(),
						itemData.getPokemonFamily().getCandy()
				);
			}
			// player stats
			if (itemData.hasPlayerStats()) {
				api.getPlayerProfile().setStats(itemData.getPlayerStats());
			}

			// pokedex
			if (itemData.hasPokedexEntry()) {
				pokedex.add(itemData.getPokedexEntry());
			}

			if (itemData.hasEggIncubators()) {
				for (EggIncubatorOuterClass.EggIncubator incubator : itemData.getEggIncubators().getEggIncubatorList()) {
					incubators.add(new EggIncubator(api, incubator));
				}
			}

			lastInventoryUpdate = System.currentTimeMillis();
		}
	}
}
