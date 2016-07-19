package com.pokegoapi.api.inventory;

import com.pokegoapi.api.PokemonGo;
import POGOProtos.Inventory.ItemIdOuterClass.ItemId;

import com.pokegoapi.exceptions.NoSuchItemException;

import java.util.HashMap;

public class Bag
{
	private PokemonGo pgo;
	private HashMap<ItemId, Item> items;

	public Bag(PokemonGo pgo) {
		this.pgo = pgo;
		items = new HashMap<ItemId, Item>();
	}

	public void addItem(Item item)
	{
		items.put(item.getItemId(), item);
	}

	public Item getItem(ItemId type) throws NoSuchItemException {
		if (items.containsKey(type)) {
			return items.get(type);
		}

		throw new NoSuchItemException();
	}
}
