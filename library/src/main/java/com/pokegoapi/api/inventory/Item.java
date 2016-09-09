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

import POGOProtos.Enums.ItemCategoryOuterClass.ItemCategory;
import POGOProtos.Inventory.AppliedItemOuterClass.AppliedItem;
import POGOProtos.Inventory.Item.ItemDataOuterClass;
import POGOProtos.Inventory.Item.ItemDataOuterClass.ItemData;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Settings.Master.ItemSettingsOuterClass.ItemSettings;
import com.pokegoapi.api.PokemonGo;
import lombok.Getter;

public class Item {
	private ItemData proto;

	private PokemonGo api;

	@Getter
	private final ItemSettings settings;

	@Getter
	public int count;

	@Getter
	private ItemBag itemBag;

	private boolean applied;

	@Getter
	private long appliedTime;
	@Getter
	private long appliedExpiration;

	/**
	 * Constructs a new item.
	 *
	 * @param api the current api
	 * @param proto the protocol to construct this item from
	 * @param itemBag the item bag containing this item
	 */
	public Item(PokemonGo api, ItemDataOuterClass.ItemData proto, ItemBag itemBag) {
		this.api = api;
		this.proto = proto;
		this.count = proto.getCount();
		this.itemBag = itemBag;
		this.settings = api.itemTemplates.getItemSettings(getItemId());
	}

	public ItemId getItemId() {
		return proto.getItemId();
	}

	public boolean isUnseen() {
		return proto.getUnseen();
	}

	/**
	 * Check if the item is a potion
	 *
	 * @return true if the item is a potion
	 */
	public boolean isPotion() {
		return settings.hasPotion();
	}

	/**
	 * Check if the item is a revive
	 *
	 * @return true if the item is a revive
	 */
	public boolean isRevive() {
		return settings.hasRevive();
	}

	/**
	 * Check if the item is a lucky egg
	 *
	 * @return true if the item is a lucky egg
	 */
	public boolean isLuckyEgg() {
		return settings.hasXpBoost();
	}

	/**
	 * Check if the item is incense
	 *
	 * @return true if the item is incense
	 */
	public boolean isIncense() {
		return settings.hasIncense();
	}

	/**
	 * Sets the item count. If the count reaches 0, this item is removed from the containing item bag.
	 *
	 * @param count the new item count
	 */
	public void setCount(int count) {
		this.count = count;
		if (count <= 0) {
			itemBag.removeItem(getItemId());
		} else {
			itemBag.addItem(this);
		}
	}

	/**
	 * @return the category this item is in
	 */
	public ItemCategory getCategory() {
		return settings.getCategory();
	}

	/**
	 * Sets this item to applied with the given AppliedItem proto
	 *
	 * @param item the proto to import from
	 */
	public void setApplied(AppliedItem item) {
		this.applied = true;
		this.appliedTime = item.getAppliedMs();
		this.appliedExpiration = item.getExpireMs();
	}

	/**
	 * Checks if this item is applied
	 *
	 * @return if this item is applied / active
	 */
	public boolean isApplied() {
		return api.currentTimeMillis() <= appliedExpiration && applied;
	}

	/**
	 * Sets this item as not applied
	 */
	public void removeApplied() {
		applied = false;
	}
}
