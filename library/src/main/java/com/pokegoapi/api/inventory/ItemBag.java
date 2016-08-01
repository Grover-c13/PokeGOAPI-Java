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

import POGOProtos.Inventory.Item.ItemDataOuterClass.ItemData;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.RecycleInventoryItemMessageOuterClass.RecycleInventoryItemMessage;
import POGOProtos.Networking.Requests.Messages.UseIncenseMessageOuterClass.UseIncenseMessage;
import POGOProtos.Networking.Requests.Messages.UseItemXpBoostMessageOuterClass.UseItemXpBoostMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.Result;
import POGOProtos.Networking.Responses.UseIncenseResponseOuterClass.UseIncenseResponse;
import POGOProtos.Networking.Responses.UseItemXpBoostResponseOuterClass.UseItemXpBoostResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;

import java.util.Collection;
import java.util.HashMap;


/**
 * The type Bag.
 */
public class ItemBag {
	private PokemonGo pgo;
	private HashMap<ItemId, Item> items;

	public ItemBag(PokemonGo pgo) {
		reset(pgo);
	}

	public void reset(PokemonGo pgo) {
		this.pgo = pgo;
		items = new HashMap<>();
	}

	public void addItem(Item item) {
		items.put(item.getItemId(), item);
	}

	/**
	 * Remove item result.
	 *
	 * @param id
	 *            the id
	 * @param quantity
	 *            the quantity
	 * @return the result
	 * @throws RemoteServerException
	 *             the remote server exception
	 * @throws LoginFailedException
	 *             the login failed exception
	 */
	public Result removeItem(ItemId id, int quantity) throws RemoteServerException, LoginFailedException {
		Item item = getItem(id);
		if (item.getCount() < quantity) {
			throw new IllegalArgumentException("You cannont remove more quantity than you have");
		}

		RecycleInventoryItemMessage msg = RecycleInventoryItemMessage.newBuilder().setItemId(id).setCount(quantity)
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.RECYCLE_INVENTORY_ITEM, msg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse response;
		try {
			response = RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse
					.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		if (response
				.getResult() == RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.Result.SUCCESS) {
			item.setCount(response.getNewCount());
		}
		return response.getResult();
	}

	/**
	 * Gets item.
	 *
	 * @param type
	 *            the type
	 * @return the item
	 */
	public Item getItem(ItemId type) {
		if (type == ItemId.UNRECOGNIZED) {
			throw new IllegalArgumentException("You cannot get item for UNRECOGNIZED");
		}

		// prevent returning null
		if (!items.containsKey(type)) {
			return new Item(ItemData.newBuilder().setCount(0).setItemId(type).build());
		}

		return items.get(type);
	}

	public Collection<Item> getItems() {
		return items.values();
	}

	/**
	 * Get used space inside of player inventory.
	 *
	 * @return used space
	 * @throws RemoteServerException
	 *             the remote server exception
	 * @throws LoginFailedException
	 *             the login failed exception
	 */
	public int getItemsCount() {
		int ct = 0;
		for (Item item : items.values()) {
			ct += item.getCount();
		}
		return ct;
	}

	/**
	 * use an item with itemID
	 * @param type type of item
	 * @throws RemoteServerException
	 *             the remote server exception
	 * @throws LoginFailedException
	 *             the login failed exception
	 */
	public void useItem(ItemId type) throws RemoteServerException, LoginFailedException {
		if (type == ItemId.UNRECOGNIZED) {
			throw new IllegalArgumentException("You cannot use item for UNRECOGNIZED");
		}

		switch (type) {
			case ITEM_INCENSE_ORDINARY:
			case ITEM_INCENSE_SPICY:
			case ITEM_INCENSE_COOL:
			case ITEM_INCENSE_FLORAL:
				useIncense(type);
				break;
			default:
				break;
		}
	}

	/**
	 * use an incense
	 * @param type type of item
	 */
	public void useIncense(ItemId type) throws RemoteServerException, LoginFailedException {
		UseIncenseMessage useIncenseMessage = 
				UseIncenseMessage.newBuilder()
				.setIncenseType(type)
				.setIncenseTypeValue(type.getNumber())
				.build();
		
		ServerRequest useIncenseRequest = new ServerRequest(RequestType.USE_INCENSE,
				useIncenseMessage);
		pgo.getRequestHandler().sendServerRequests(useIncenseRequest);

		UseIncenseResponse response = null;
		try {
			response = UseIncenseResponse.parseFrom(useIncenseRequest.getData());
			Log.i("Main", "Use incense result: " + response.getResult());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}


	/**
	 * use an item with itemID
	 */
	public void useIncense() throws RemoteServerException, LoginFailedException {	
		useIncense(ItemId.ITEM_INCENSE_ORDINARY);
	}

	/**
	 * use a lucky egg
	 * @returns lucky egg response
	 */
	public UseItemXpBoostResponse useLuckyEgg() throws RemoteServerException, LoginFailedException {
		UseItemXpBoostMessage xpMsg = UseItemXpBoostMessage
				.newBuilder()
				.setItemId(ItemId.ITEM_LUCKY_EGG)
				.build();

		ServerRequest req = new ServerRequest(RequestType.USE_ITEM_XP_BOOST,
				xpMsg);
		pgo.getRequestHandler().sendServerRequests(req);

		UseItemXpBoostResponse response = null;
		try {
			response = UseItemXpBoostResponse.parseFrom(req.getData());
			Log.i("Main", "Use incense result: " + response.getResult());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		return response;
	}

}
