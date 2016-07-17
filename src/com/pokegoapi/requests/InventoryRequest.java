package com.pokegoapi.requests;

import java.util.LinkedList;
import java.util.List;


import com.google.protobuf.InvalidProtocolBufferException;


import com.pokegoapi.api.inventory.Pokemon;
import com.pokegoapi.main.Communication;
import com.pokegoapi.main.Inventory.InventoryRequestProto;
import com.pokegoapi.main.Inventory.InventoryRequestProto.Builder;
import com.pokegoapi.main.Inventory.InventoryResponseProto;
import com.pokegoapi.main.Inventory.InventoryResponseProto.InventoryItemResponseProto;
import com.pokegoapi.main.Communication.Payload;
import com.pokegoapi.main.Request;

import lombok.Getter;

public class InventoryRequest extends Request {

	private Builder builder;
	@Getter List<Pokemon> pokemon = new LinkedList<Pokemon>();
	
	public Communication.Method getRpcId()
	{
		return Communication.Method.GET_INVENTORY;
	}

	public InventoryRequest() {
		builder = InventoryRequestProto.newBuilder();
	}

	public void setTimestamp(long timestamp)
	{
		builder.setTimestamp(timestamp);
	}

	public void handleResponse(Payload payload)
	{
		try
		{
			InventoryResponseProto response = InventoryResponseProto.parseFrom(payload.getData());
			for(InventoryItemResponseProto item : response.getItemsList())
			{
				if(item.getItem().hasPokemon())
				{
					pokemon.add(new Pokemon(item.getItem().getPokemon()));
				}
			}
		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
	}


	public byte[] getInput() 
	{
		return builder.build().toByteArray();
	}
}
