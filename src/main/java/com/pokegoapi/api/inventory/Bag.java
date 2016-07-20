package com.pokegoapi.api.inventory;

import com.pokegoapi.api.PokemonGo;
import POGOProtos.Inventory.ItemIdOuterClass.ItemId;

import com.pokegoapi.exceptions.NoSuchItemException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bag
{
	private PokemonGo pgo;
	private HashMap<ItemId, Item> items;

	public Bag(PokemonGo pgo) {
		this.pgo = pgo;
		items = new HashMap<ItemId, Item>();
	}

	public void addItem(Item item) {
		items.put(item.getItemId(), item);
	}

	public Item getItem(ItemId type) {
		if (type == ItemId.UNRECOGNIZED) {
			throw new IllegalArgumentException("You cannot get item for UNRECOGNIZED");
		}
		return items.get(type.getNumber());
	}
}
