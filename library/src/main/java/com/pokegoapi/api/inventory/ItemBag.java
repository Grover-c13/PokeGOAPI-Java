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

import POGOProtos.Inventory.InventoryItemDataOuterClass.InventoryItemData;
import POGOProtos.Inventory.InventoryItemOuterClass.InventoryItem;
import POGOProtos.Inventory.Item.ItemDataOuterClass.ItemData;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.RecycleInventoryItemMessageOuterClass.RecycleInventoryItemMessage;
import POGOProtos.Networking.Requests.Messages.UseIncenseMessageOuterClass.UseIncenseMessage;
import POGOProtos.Networking.Requests.Messages.UseItemXpBoostMessageOuterClass.UseItemXpBoostMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.Result;
import POGOProtos.Networking.Responses.UseIncenseResponseOuterClass.UseIncenseResponse;
import POGOProtos.Networking.Responses.UseItemXpBoostResponseOuterClass.UseItemXpBoostResponse;
import com.pokegoapi.api.internal.networking.Networking;
import rx.Observable;
import rx.functions.Func1;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The type Bag.
 */
public class ItemBag {
	private final Networking networking;
	private final Map<ItemId, Item> items = new ConcurrentHashMap<>();

	/**
	 * Constructor for internal use
	 *
	 * @param getInventoryResponse Initial inventory response
	 * @param networking           Networking for requests
	 */
	public ItemBag(GetInventoryResponse getInventoryResponse, Networking networking) {
		this.networking = networking;
		update(getInventoryResponse);
	}

	final void update(GetInventoryResponse getInventoryResponse) {
		List<ItemId> currentItems = new LinkedList<>();
		for (InventoryItem inventoryItem
				: getInventoryResponse.getInventoryDelta().getInventoryItemsList()) {
			InventoryItemData itemData = inventoryItem.getInventoryItemData();
			// items
			if (itemData.getItem().getItemId() != ItemId.UNRECOGNIZED
					&& itemData.getItem().getItemId() != ItemId.ITEM_UNKNOWN) {
				ItemData item = itemData.getItem();
				items.put(item.getItemId(), new Item(item));
				currentItems.add(item.getItemId());
			}
		}
		// Fill all non-received item its with 0
		for (ItemId itemId : ItemId.values()) {
			if (currentItems.contains(itemId)) {
				continue;
			}
			if (itemId == ItemId.UNRECOGNIZED) {
				continue;
			}
			items.put(itemId, new Item(ItemData.newBuilder().setCount(0).setItemId(itemId).build()));
		}
	}

	/**
	 * Remove item result.
	 *
	 * @param id       the id
	 * @param quantity the quantity
	 * @return the result
	 */
	public Observable<Result> removeItem(ItemId id, int quantity) {
		final Item item = getItem(id);
		if (item.getCount() < quantity) {
			throw new IllegalArgumentException("You cannont remove more quantity than you have");
		}

		return networking.queueRequest(RequestType.RECYCLE_INVENTORY_ITEM, RecycleInventoryItemMessage
				.newBuilder()
				.setItemId(id)
				.setCount(quantity)
				.build(), RecycleInventoryItemResponse.class)
				.map(new Func1<RecycleInventoryItemResponse, Result>() {
					@Override
					public Result call(RecycleInventoryItemResponse response) {
						if (response.getResult() == RecycleInventoryItemResponse.Result.SUCCESS) {
							item.setCount(response.getNewCount());
						}
						return response.getResult();
					}
				});
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
	 * use an incense
	 *
	 * @param type type of item
	 * @return Respionse observable
	 */
	public Observable<UseIncenseResponse> useIncense(ItemId type) {
		final Item item = items.get(type);
		if (item.getCount() == 0) {
			return Observable
					.just(UseIncenseResponse.newBuilder().setResult(UseIncenseResponse.Result.NONE_IN_INVENTORY)
							.build());
		}
		return networking
				.queueRequest(RequestType.USE_INCENSE,
						UseIncenseMessage.newBuilder()
								.setIncenseType(type)
								.setIncenseTypeValue(type.getNumber())
								.build(), UseIncenseResponse.class)
				.map(new Func1<UseIncenseResponse, UseIncenseResponse>() {
					@Override
					public UseIncenseResponse call(UseIncenseResponse response) {
						if (response.getResult() == UseIncenseResponse.Result.SUCCESS) {
							item.decrease();
						}
						return response;
					}
				});
	}


	/**
	 * use an item with itemID
	 *
	 * @return Incense response
	 */
	public Observable<UseIncenseResponse> useIncense() {
		return useIncense(ItemId.ITEM_INCENSE_ORDINARY);
	}

	/**
	 * use a lucky egg
	 *
	 * @return the xp boost response
	 */
	public Observable<UseItemXpBoostResponse> useLuckyEgg() {
		return networking.queueRequest(RequestType.USE_ITEM_XP_BOOST,
				UseItemXpBoostMessage
						.newBuilder()
						.setItemId(ItemId.ITEM_LUCKY_EGG)
						.build(),
				UseItemXpBoostResponse.class);
	}

}
