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

import POGOProtos.Data.Player.PlayerStatsOuterClass;
import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.InventoryItemDataOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Inventory.ItemIdOuterClass;
import POGOProtos.Inventory.ItemOuterClass;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;


public class Inventories {

	private final PokemonGo api;
	@Getter
	private ItemBag itemBag;
	@Getter
	private PokeBank pokebank;
	@Getter
	private CandyJar candyjar;
	@Getter
	private PlayerStatsOuterClass.PlayerStats stats;

	private long lastInventoryUpdate = 0;

	/**
	 * Creates Inventories and initializes content.
	 * @param api PokemonGo api
	 * @throws LoginFailedException the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public Inventories(PokemonGo api) throws LoginFailedException, RemoteServerException {
		this.api = api;
		itemBag = new ItemBag(api);
		pokebank = new PokeBank(api);
		candyjar = new CandyJar(api);
		updateInventories();
	}

	/**
	 * Updates the inventories with latest data.
	 * @throws LoginFailedException the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void updateInventories() throws LoginFailedException, RemoteServerException {
		updateInventories(false);
	}

	/**
	 * Updates the inventories with the latest data.
	 * @param forceUpdate For a full update if true
	 * @throws LoginFailedException the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void updateInventories(boolean forceUpdate) throws LoginFailedException, RemoteServerException {
		if (forceUpdate) {
			lastInventoryUpdate = 0;
			itemBag = new ItemBag(api);
			pokebank = new PokeBank(api);
			candyjar = new CandyJar(api);
		}
		GetInventoryMessage invReqMsg = GetInventoryMessage.newBuilder()
				.setLastTimestampMs(lastInventoryUpdate)
				.build();
		ServerRequest inventoryRequest = new ServerRequest(RequestTypeOuterClass.RequestType.GET_INVENTORY, invReqMsg);
		api.getRequestHandler().request(inventoryRequest);
		api.getRequestHandler().sendServerRequests();

		GetInventoryResponse response = null;
		try {
			response = GetInventoryResponse.parseFrom(inventoryRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		for (InventoryItemOuterClass.InventoryItem inventoryItem
				: response.getInventoryDelta().getInventoryItemsList()) {
			InventoryItemDataOuterClass.InventoryItemData itemData = inventoryItem.getInventoryItemData();
			if (inventoryItem.getDeletedItemKey() > 0) {
				if (itemData.getPokemonData().getPokemonId() != PokemonIdOuterClass.PokemonId.MISSINGNO) {
					pokebank.removePokemon(new Pokemon(inventoryItem.getInventoryItemData().getPokemonData()));
				}
				if (itemData.getItem().getItemId() != ItemIdOuterClass.ItemId.UNRECOGNIZED) {
					ItemOuterClass.Item item = inventoryItem.getInventoryItemData().getItem();
					itemBag.removeItem(inventoryItem.getInventoryItemData().getItem().getItemId(), item.getCount());
				}
				if (itemData.getPokemonFamily().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.UNRECOGNIZED) {
					candyjar.removeCandy(
							inventoryItem.getInventoryItemData().getPokemonFamily().getFamilyId(),
							inventoryItem.getInventoryItemData().getPokemonFamily().getCandy()
					);
				}
			} else {
				if (itemData.getPokemonData().getPokemonId() != PokemonIdOuterClass.PokemonId.MISSINGNO) {
					pokebank.addPokemon(new Pokemon(inventoryItem.getInventoryItemData().getPokemonData()));
				}
				if (itemData.getItem().getItemId() != ItemIdOuterClass.ItemId.UNRECOGNIZED) {
					ItemOuterClass.Item item = inventoryItem.getInventoryItemData().getItem();
					itemBag.addItem(new Item(item));
				}
				if (itemData.getPokemonFamily().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.UNRECOGNIZED) {
					candyjar.addCandy(
							inventoryItem.getInventoryItemData().getPokemonFamily().getFamilyId(),
							inventoryItem.getInventoryItemData().getPokemonFamily().getCandy()
					);
				}
				if (itemData.hasPlayerStats()) {
					stats = inventoryItem.getInventoryItemData().getPlayerStats();
				}
			}

			lastInventoryUpdate = System.currentTimeMillis();
		}
	}
}
