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
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonCallback;
import com.pokegoapi.main.PokemonRequest;
import com.pokegoapi.main.PokemonResponse;
import com.pokegoapi.main.RequestCallback;
import com.pokegoapi.main.Utils;
import com.pokegoapi.util.Log;

import java.util.Collection;
import java.util.HashMap;

/**
 * The type Bag.
 */
public class ItemBag {
	private final PokemonGo api;
	private final HashMap<ItemId, Item> items = new HashMap<>();

	public ItemBag(PokemonGo api) {
		this.api = api;
	}

	public void reset() {
		items.clear();
	}

	public void addItem(Item item) {
		items.put(item.getItemId(), item);
	}

	/**
	 * Discards the given item.
	 *
	 * @param id the id
	 * @param quantity the quantity
	 * @param result result callback
	 */
	public void removeItem(ItemId id, int quantity, final AsyncReturn<Result> result) {
		final Item item = getItem(id);
		if (item.getCount() < quantity) {
			throw new IllegalArgumentException("You cannot remove more quantity than you have");
		}

		RecycleInventoryItemMessage message = RecycleInventoryItemMessage.newBuilder()
				.setItemId(id)
				.setCount(quantity)
				.build();

		PokemonRequest request = new PokemonRequest(RequestType.RECYCLE_INVENTORY_ITEM, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, Result.UNRECOGNIZED)) {
					return;
				}
				try {
					RecycleInventoryItemResponse messageResponse
							= RecycleInventoryItemResponse.parseFrom(response.getResponseData());
					if (messageResponse.getResult() == Result.SUCCESS) {
						item.setCount(messageResponse.getNewCount());
						if (item.getCount() <= 0) {
							removeItem(item.getItemId());
						}
					}
					result.onReceive(messageResponse.getResult(), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(Result.UNRECOGNIZED, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Removes the given item ID from the bag item map.
	 *
	 * @param id the item to remove
	 * @return The item removed, if any
	 */
	public Item removeItem(ItemId id) {
		return items.remove(id);
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
			return new Item(ItemData.newBuilder().setCount(0).setItemId(type).build(), this);
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
	 * @param callback for when this task completes
	 */
	public void useItem(ItemId type, PokemonCallback callback) {
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
	 * Use an incense
	 *
	 * @param type type of item
	 * @param callback for when this task completes
	 */
	public void useIncense(ItemId type, final PokemonCallback callback) {
		UseIncenseMessage message =
				UseIncenseMessage.newBuilder()
						.setIncenseType(type)
						.setIncenseTypeValue(type.getNumber())
						.build();

		PokemonRequest request = new PokemonRequest(RequestType.USE_INCENSE, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, callback)) {
					return;
				}
				try {
					UseIncenseResponse messageResponse = UseIncenseResponse.parseFrom(response.getResponseData());
					Log.i("Main", "Use incense result: " + messageResponse.getResult());
					callback.onCompleted(null);
				} catch (InvalidProtocolBufferException e) {
					callback.onCompleted(new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Use ordinary
	 *
	 * @param callback for when this task completes
	 */
	public void useIncense(PokemonCallback callback) {
		useIncense(ItemId.ITEM_INCENSE_ORDINARY, callback);
	}

	/**
	 * Use a lucky egg
	 *
	 * @param callback for when this task completes
	 */
	public void useLuckyEgg(final AsyncReturn<UseItemXpBoostResponse> callback) {
		UseItemXpBoostMessage message =
				UseItemXpBoostMessage.newBuilder()
						.setItemId(ItemId.ITEM_LUCKY_EGG)
						.build();

		PokemonRequest request = new PokemonRequest(RequestType.USE_ITEM_XP_BOOST, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, callback, null)) {
					return;
				}
				try {
					UseItemXpBoostResponse messageResponse
							= UseItemXpBoostResponse.parseFrom(response.getResponseData());
					Log.i("Main", "Use incense result: " + messageResponse.getResult());
					callback.onReceive(messageResponse, null);
				} catch (InvalidProtocolBufferException e) {
					callback.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}
}
