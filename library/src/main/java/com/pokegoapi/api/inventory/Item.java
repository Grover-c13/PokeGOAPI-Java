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

import POGOProtos.Inventory.Item.ItemDataOuterClass;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import lombok.Getter;

public class Item {
	private ItemDataOuterClass.ItemData proto;
	@Getter
	private int count;

	@Getter
	private ItemBag itemBag;

	/**
	 * Constructs a new item.
	 *
	 * @param proto the protocol to construct this item from
	 * @param itemBag the item bag containing this item
	 */
	public Item(ItemDataOuterClass.ItemData proto, ItemBag itemBag) {
		this.proto = proto;
		this.count = proto.getCount();
		this.itemBag = itemBag;
	}

	public ItemId getItemId() {
		return proto.getItemId();
	}

	public boolean isUnseen() {
		return proto.getUnseen();
	}

	/**
	 * Check if the item it's a potion
	 *
	 * @return true if the item it's a potion
	 */
	public boolean isPotion() {
		return getItemId() == ItemId.ITEM_POTION
				|| getItemId() == ItemId.ITEM_SUPER_POTION
				|| getItemId() == ItemId.ITEM_HYPER_POTION
				|| getItemId() == ItemId.ITEM_MAX_POTION
				;
	}

	/**
	 * Check if the item it's a revive
	 *
	 * @return true if the item it's a revive
	 */
	public boolean isRevive() {
		return getItemId() == ItemId.ITEM_REVIVE
				|| getItemId() == ItemId.ITEM_MAX_REVIVE
				;
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
}
