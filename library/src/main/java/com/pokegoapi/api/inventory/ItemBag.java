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
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.Result;
import POGOProtos.Networking.Responses.UseIncenseResponseOuterClass.UseIncenseResponse;
import POGOProtos.Networking.Responses.UseItemXpBoostResponseOuterClass.UseItemXpBoostResponse;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.PokeAFunc;
import com.pokegoapi.util.PokeCallback;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Bag.
 */
public class ItemBag {
	private final PokemonGo api;
	private final ConcurrentHashMap<ItemId, Item> items = new ConcurrentHashMap<>();

	public ItemBag(PokemonGo api) {
		this.api = api;
	}

	public void addItem(ItemData item) {
		synchronized (item) {
			Item current = items.get(item.getItemId());
			if (current == null) {
				items.put(item.getItemId(), new Item(item));
			}
			current.setCount(item.getCount());
			current.setProto(item);
		}
	}

	/**
	 * Remove item result.
	 *
	 * @param id       the id
	 * @param quantity the quantity
	 * @param callback an optional callback to handle results
	 */
	public void removeItem(ItemId id, int quantity, PokeCallback<Result> callback) {
		final Item item = getItem(id);
		if (item.getCount() < quantity) {
			throw new IllegalArgumentException("You cannont remove more quantity than you have");
		}

		RecycleInventoryItemMessage msg = RecycleInventoryItemMessage.newBuilder().setItemId(id).setCount(quantity)
				.build();

		new AsyncServerRequest(RequestType.RECYCLE_INVENTORY_ITEM, msg,
				new PokeAFunc<RecycleInventoryItemResponse, Result>() {
					@Override
					public Result exec(RecycleInventoryItemResponse response) {
						if (response.getResult() == RecycleInventoryItemResponse.Result.SUCCESS) {
							item.setCount(response.getNewCount());
						}
						return response.getResult();
					}
				}, callback, api);
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
	 *
	 * @param type type of item
	 * @param callback an optional callback to handle results
	 */
	public void useItem(ItemId type, PokeCallback<UseIncenseResponse.Result> callback) {
		if (type == ItemId.UNRECOGNIZED) {
			throw new IllegalArgumentException("You cannot use item for UNRECOGNIZED");
		}

		switch (type) {
			case ITEM_INCENSE_ORDINARY:
			case ITEM_INCENSE_SPICY:
			case ITEM_INCENSE_COOL:
			case ITEM_INCENSE_FLORAL:
				useIncense(type, callback);
				break;
			default:
				break;
		}
	}

	/**
	 * use an incense
	 *
	 * @param type type of item
	 * @param callback an optional callback to handle results
	 */
	public void useIncense(ItemId type, PokeCallback<UseIncenseResponse.Result> callback) {
		UseIncenseMessage useIncenseMessage =
				UseIncenseMessage.newBuilder()
						.setIncenseType(type)
						.setIncenseTypeValue(type.getNumber())
						.build();

		new AsyncServerRequest(RequestType.USE_INCENSE, useIncenseMessage,
				new PokeAFunc<UseIncenseResponse, UseIncenseResponse.Result>() {
					@Override
					public UseIncenseResponse.Result exec(UseIncenseResponse response) {
						return response.getResult();
					}
				}, callback, api);
	}

	/**
	 * use an item with itemID
	 *
	 * @param callback an optional callback to handle results
	 */
	public void useIncense(PokeCallback<UseIncenseResponse.Result> callback) {
		useIncense(ItemId.ITEM_INCENSE_ORDINARY, callback);
	}

	/**
	 * use a lucky egg
	 *
	 * @param callback an optional callback to handle results
	 */
	public void useLuckyEgg(PokeCallback<UseItemXpBoostResponse> callback) {
		UseItemXpBoostMessage xpMsg = UseItemXpBoostMessage
				.newBuilder()
				.setItemId(ItemId.ITEM_LUCKY_EGG)
				.build();

		new AsyncServerRequest(RequestType.USE_ITEM_XP_BOOST, xpMsg,
				new PokeAFunc<UseItemXpBoostResponse, UseItemXpBoostResponse>() {
					@Override
					public UseItemXpBoostResponse exec(UseItemXpBoostResponse response) {
						Log.i("Main", "Use incense result: " + response.getResult());
						return response;
					}
				}, callback, api);
	}

}
