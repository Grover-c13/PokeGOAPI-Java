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
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Inventory.ItemIdOuterClass;
import POGOProtos.Inventory.ItemOuterClass;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass;
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

	private long lastInventoryUpdate = 0;

    public Inventories(PokemonGo api) throws LoginFailedException, RemoteServerException {
        this.api = api;
		itemBag = new ItemBag(api);
		pokebank = new PokeBank(api);
		candyjar = new CandyJar(api);
		updateInventories();
    }

	public void updateInventories() throws LoginFailedException, RemoteServerException {
		updateInventories(false);
	}

	public void updateInventories(boolean forceUpdate) throws LoginFailedException, RemoteServerException {
		if(forceUpdate){
			lastInventoryUpdate = 0;
			itemBag = new ItemBag(api);
			pokebank = new PokeBank(api);
			candyjar = new CandyJar(api);
		}
		GetInventoryMessageOuterClass.GetInventoryMessage invReqMsg = GetInventoryMessageOuterClass.GetInventoryMessage.newBuilder()
				.setLastTimestampMs(lastInventoryUpdate)
				.build();
		ServerRequest inventoryRequest = new ServerRequest(RequestTypeOuterClass.RequestType.GET_INVENTORY, invReqMsg);
		api.getRequestHandler().request(inventoryRequest);
		api.getRequestHandler().sendServerRequests();

		GetInventoryResponseOuterClass.GetInventoryResponse response = null;
		try{
			response = GetInventoryResponseOuterClass.GetInventoryResponse.parseFrom(inventoryRequest.getData());
		} catch (InvalidProtocolBufferException e){
			e.printStackTrace();
		}

		for(InventoryItemOuterClass.InventoryItem inventoryItem : response.getInventoryDelta().getInventoryItemsList()){
			if(inventoryItem.getDeletedItemKey() > 0){
				if(inventoryItem.getInventoryItemData().getPokemonData().getPokemonId() != PokemonIdOuterClass.PokemonId.MISSINGNO){
					pokebank.removePokemon(new Pokemon(inventoryItem.getInventoryItemData().getPokemonData()));
				}
				if(inventoryItem.getInventoryItemData().getItem().getItemId() != ItemIdOuterClass.ItemId.UNRECOGNIZED){
					ItemOuterClass.Item item = inventoryItem.getInventoryItemData().getItem();
					itemBag.removeItem(inventoryItem.getInventoryItemData().getItem().getItemId(), item.getCount());
				}
				if(inventoryItem.getInventoryItemData().getPokemonFamily().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.UNRECOGNIZED){
					candyjar.removeCandy(
							inventoryItem.getInventoryItemData().getPokemonFamily().getFamilyId(),
							inventoryItem.getInventoryItemData().getPokemonFamily().getCandy()
					);
				}
			} else {
				if(inventoryItem.getInventoryItemData().getPokemonData().getPokemonId() != PokemonIdOuterClass.PokemonId.MISSINGNO){
					pokebank.addPokemon(new Pokemon(inventoryItem.getInventoryItemData().getPokemonData()));
				}
				if(inventoryItem.getInventoryItemData().getItem().getItemId() != ItemIdOuterClass.ItemId.UNRECOGNIZED){
					ItemOuterClass.Item item = inventoryItem.getInventoryItemData().getItem();
					itemBag.addItem(new Item(item));
				}
				if(inventoryItem.getInventoryItemData().getPokemonFamily().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.UNRECOGNIZED){
					candyjar.addCandy(
							inventoryItem.getInventoryItemData().getPokemonFamily().getFamilyId(),
							inventoryItem.getInventoryItemData().getPokemonFamily().getCandy()
					);
				}
			}

			lastInventoryUpdate = System.currentTimeMillis();
		}
	}
}
