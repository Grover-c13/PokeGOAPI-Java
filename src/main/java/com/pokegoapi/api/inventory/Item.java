package com.pokegoapi.api.inventory;

import POGOProtos.Inventory.ItemIdOuterClass.ItemId;


public class Item
{
	private POGOProtos.Inventory.ItemOuterClass.Item proto;

	public Item(POGOProtos.Inventory.ItemOuterClass.Item  proto)
	{
		this.proto = proto;
	}

	public ItemId getItemId() {
		return proto.getItemId();
	}

	public int getCount() {
		return proto.getCount();
	}

	public boolean isUnseen() {
		return proto.getUnseen();
	}
}
