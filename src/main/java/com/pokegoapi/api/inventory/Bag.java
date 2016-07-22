package com.pokegoapi.api.inventory;

import POGOProtos.Inventory.ItemIdOuterClass.ItemId;
import POGOProtos.Inventory.ItemOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Requests.Messages.RecycleInventoryItemMessageOuterClass;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass;
import POGOProtos.Networking.Responses.RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.Result;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;

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
	
	public Result removeItem(ItemId id, int quantity) throws RemoteServerException, LoginFailedException {
		Item item = getItem(id);
		if (item.getCount() < quantity)
			throw new IllegalArgumentException("You cannont remove more quantity than you have");
		
		RecycleInventoryItemMessageOuterClass.RecycleInventoryItemMessage msg = RecycleInventoryItemMessageOuterClass.RecycleInventoryItemMessage.newBuilder()
					.setItemId(id).setCount(quantity).build();
		
		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.RECYCLE_INVENTORY_ITEM, msg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);
		
		RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse response = null;
		try {
			response = RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		
		if (response.getResult() == RecycleInventoryItemResponseOuterClass.RecycleInventoryItemResponse.Result.SUCCESS) 
			item.setCount(response.getNewCount());
		return response.getResult();
	}

	public Item getItem(ItemId type) {
		if (type == ItemId.UNRECOGNIZED) 
			throw new IllegalArgumentException("You cannot get item for UNRECOGNIZED");

		// prevent returning null
		if (!items.containsKey(type)) 
			return new Item(ItemOuterClass.Item.newBuilder().setCount(0).setItemId(type).build());
		
		return items.get(type);
	}
}
