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

import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Inventory.Item.ItemDataOuterClass.ItemData;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.RecycleInventoryItemMessageOuterClass.RecycleInventoryItemMessage;
import POGOProtos.Networking.Requests.Messages.UseIncenseMessageOuterClass.UseIncenseMessage;
import POGOProtos.Networking.Requests.Messages.UseItemXpBoostMessageOuterClass.UseItemXpBoostMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.Result;
import POGOProtos.Networking.Responses.UseIncenseResponseOuterClass.UseIncenseResponse;
import POGOProtos.Networking.Responses.UseItemXpBoostResponseOuterClass.UseItemXpBoostResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The type Bag.
 */
public class ItemBag {
	private final PokemonGo api;
	private final Map<ItemId, Item> items = Collections.synchronizedMap(new HashMap<ItemId, Item>());
	private final Object lock = new Object();

	public ItemBag(PokemonGo api) {
		this.api = api;
	}

	/**
	 * Resets this item bag and removes all items
	 */
	public void reset() {
		synchronized (this.lock) {
			items.clear();
		}
	}

	/**
	 * Adds the given item to this bag
	 *
	 * @param item the item to add
	 */
	public void addItem(Item item) {
		synchronized (this.lock) {
			items.put(item.getItemId(), item);
		}
	}

	/**
	 * Discards the given item.
	 *
	 * @param id the id
	 * @param quantity the quantity
	 * @return the result
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public Result removeItem(ItemId id, int quantity) throws RequestFailedException {
		Item item = getItem(id);
		if (item.getCount() < quantity) {
			throw new IllegalArgumentException("You cannot remove more quantity than you have");
		}

		RecycleInventoryItemMessage msg = RecycleInventoryItemMessage.newBuilder().setItemId(id).setCount(quantity)
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.RECYCLE_INVENTORY_ITEM, msg);
		api.getRequestHandler().sendServerRequests(serverRequest);

		RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse response;
		try {
			response = RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse
					.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}

		if (response
				.getResult() == RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.Result.SUCCESS) {
			item.setCount(response.getNewCount());
			if (item.getCount() <= 0) {
				removeItem(item.getItemId());
			}
		}
		return response.getResult();
	}

	/**
	 * Removes the given item ID from the bag item map.
	 *
	 * @param id the item to remove
	 * @return The item removed, if any
	 */
	public Item removeItem(ItemId id) {
		synchronized (this.lock) {
			return items.remove(id);
		}
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

		synchronized (this.lock) {
			// prevent returning null
			if (!items.containsKey(type)) {
				return new Item(api, ItemData.newBuilder().setCount(0).setItemId(type).build(), this);
			}

			return items.get(type);
		}
	}

	/**
	 * @return all the items in this bag
	 */
	public Collection<Item> getItems() {
		synchronized (this.lock) {
			return items.values();
		}
	}

	/**
	 * Get used space inside of player inventory.
	 *
	 * @return used space
	 */
	public int getItemsCount() {
		synchronized (this.lock) {
			int ct = 0;
			for (Item item : items.values()) {
				ct += item.getCount();
			}
			return ct;
		}
	}

	/**
	 * use an item with itemID
	 *
	 * @param type type of item
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void useItem(ItemId type) throws RequestFailedException {
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
	 *
	 * @param type type of item
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void useIncense(ItemId type) throws RequestFailedException {
		UseIncenseMessage useIncenseMessage =
				UseIncenseMessage.newBuilder()
						.setIncenseType(type)
						.setIncenseTypeValue(type.getNumber())
						.build();

		ServerRequest useIncenseRequest = new ServerRequest(RequestType.USE_INCENSE, useIncenseMessage);
		api.getRequestHandler().sendServerRequests(useIncenseRequest, true);

		try {
			UseIncenseResponse response = UseIncenseResponse.parseFrom(useIncenseRequest.getData());
			Log.i("Main", "Use incense result: " + response.getResult());
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}


	/**
	 * use an item with itemID
	 *
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void useIncense() throws RequestFailedException {
		useIncense(ItemId.ITEM_INCENSE_ORDINARY);
	}

	/**
	 * use a lucky egg
	 *
	 * @return the xp boost response
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public UseItemXpBoostResponse useLuckyEgg()
			throws RequestFailedException {
		UseItemXpBoostMessage xpMsg = UseItemXpBoostMessage
				.newBuilder()
				.setItemId(ItemId.ITEM_LUCKY_EGG)
				.build();

		ServerRequest req = new ServerRequest(RequestType.USE_ITEM_XP_BOOST, xpMsg);
		api.getRequestHandler().sendServerRequests(req, true);

		try {
			UseItemXpBoostResponse response = UseItemXpBoostResponse.parseFrom(req.getData());
			Log.i("Main", "Use incense result: " + response.getResult());
			return response;
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * @deprecated use getUsablePokeballs
	 * @return a list of usable pokeballs that are in the inventory
	 */
	@Deprecated
	public List<Pokeball> getUseablePokeballs() {
		return getUsablePokeballs();
	}

	/**
	 * @return a list of usable pokeballs that are in the inventory
	 */
	public List<Pokeball> getUsablePokeballs() {
		List<Pokeball> pokeballs = new ArrayList<>();
		for (Pokeball pokeball : Pokeball.values()) {
			if (getItem(pokeball.getBallType()).getCount() > 0) {
				pokeballs.add(pokeball);
			}
		}
		return pokeballs;
	}

	/**
	 * @return true if the current player has incense active
	 */
	public boolean isIncenseActive() {
		synchronized (lock) {
			for (Map.Entry<ItemId, Item> entry : items.entrySet()) {
				Item item = entry.getValue();
				if (item.isApplied() && item.isIncense()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return true if the current player has a lucky egg active
	 */
	public boolean isLuckyEggActive() {
		synchronized (lock) {
			for (Map.Entry<ItemId, Item> entry : items.entrySet()) {
				Item item = entry.getValue();
				if (item.isApplied() && item.isLuckyEgg()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Adds the awarded items contained in the level up response
	 *
	 * @param levelUpResponse the response to add items from
	 */
	public void addAwardedItems(LevelUpRewardsResponse levelUpResponse) {
		for (ItemAward itemAward : levelUpResponse.getItemsAwardedList()) {
			Item item = getItem(itemAward.getItemId());
			item.setCount(item.getCount() + itemAward.getItemCount());
		}
	}

	/**
	 * @return the maximum amount of items this item bag can store
	 */
	public int getMaxStorage() {
		return api.getPlayerProfile().getPlayerData().getMaxItemStorage();
	}
}
