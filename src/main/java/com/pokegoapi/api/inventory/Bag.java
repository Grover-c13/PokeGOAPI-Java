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

import POGOProtos.Inventory.ItemIdOuterClass.ItemId;
import POGOProtos.Inventory.ItemOuterClass;
import POGOProtos.Networking.Requests.Messages.RecycleInventoryItemMessageOuterClass.RecycleInventoryItemMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.Result;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;

import java.util.HashMap;

/**
 * The type Bag.
 */
public class Bag {
	private PokemonGo pgo;
	private HashMap<ItemId, Item> items;

	public Bag(PokemonGo pgo) {
		this.pgo = pgo;
		items = new HashMap<>();
	}

	public void addItem(Item item) {
		items.put(item.getItemId(), item);
	}

	/**
	 * Remove item result.
	 *
	 * @param id       the id
	 * @param quantity the quantity
	 * @return the result
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	public Result removeItem(ItemId id, int quantity) throws RemoteServerException, LoginFailedException {
		Item item = getItem(id);
		if (item.getCount() < quantity) {
			throw new IllegalArgumentException("You cannont remove more quantity than you have");
		}

		RecycleInventoryItemMessage msg = RecycleInventoryItemMessage.newBuilder()
				.setItemId(id)
				.setCount(quantity)
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.RECYCLE_INVENTORY_ITEM, msg);
		pgo.getRequestHandler().request(serverRequest);
		pgo.getRequestHandler().sendServerRequests();

		RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse response = null;
		try {
			response = RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		if (response.getResult() == RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.Result.SUCCESS) {
			item.setCount(response.getNewCount());
		}
		return response.getResult();
	}


	/**
	 * Gets item.
	 *
	 * @param type the type
	 * @return the item
	 */
	public Item getItem(ItemId type) {
		if (type == ItemId.UNRECOGNIZED) {
			throw new IllegalArgumentException("You cannot get item for UNRECOGNIZED");
		}

		// prevent returning null
		if (!items.containsKey(type)) {
			return new Item(ItemOuterClass.Item.newBuilder().setCount(0).setItemId(type).build());
		}

		return items.get(type);
	}
}
