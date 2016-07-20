package com.pokegoapi.api.inventory;

import POGOProtos.Inventory.ItemIdOuterClass.ItemId;
import POGOProtos.Inventory.ItemOuterClass;
import com.pokegoapi.api.PokemonGo;

import java.util.HashMap;

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

	public Item getItem(ItemId type) {
		if (type == ItemId.UNRECOGNIZED) {
			throw new IllegalArgumentException("You cannot get item for UNRECOGNIZED");
		}
		if (!items.containsKey(type)) {
			// prevent returning null
			return new Item(ItemOuterClass.Item.newBuilder().setCount(0).setItemId(type).build());
		}
		return items.get(type);
	}
}
