package com.pokegoapi.api.inventory;

import POGOProtos.Inventory.ItemIdOuterClass.ItemId;


public class Item {
	private POGOProtos.Inventory.ItemOuterClass.Item proto;
	private int	count;

	public Item(POGOProtos.Inventory.ItemOuterClass.Item proto) {
		this.proto = proto;
		this.count = proto.getCount();
	}

	public ItemId getItemId() {
		return proto.getItemId();
	}

	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public boolean isUnseen() {
		return proto.getUnseen();
	}
}
