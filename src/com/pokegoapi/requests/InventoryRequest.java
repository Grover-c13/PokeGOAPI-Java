package com.pokegoapi.requests;

import java.util.LinkedList;
import java.util.List;


import com.google.protobuf.InvalidProtocolBufferException;


import com.pokegoapi.api.inventory.PokemonDetails;
import com.pokegoapi.main.Inventory.InventoryRequestProto;
import com.pokegoapi.main.Inventory.InventoryRequestProto.Builder;
import com.pokegoapi.main.Inventory.InventoryResponseProto;
import com.pokegoapi.main.Inventory.InventoryResponseProto.InventoryItemResponseProto;
import com.pokegoapi.main.Communication.Payload;
import com.pokegoapi.main.Request;

public class InventoryRequest extends Request {

	private Builder builder;
	private List<PokemonDetails> pokemon;
	

	
	public int getRpcId()
	{
		return 4;
	}

	public InventoryRequest()
	{
		builder = InventoryRequestProto.newBuilder();
		pokemon = new LinkedList<PokemonDetails>();
	}

	public void setTimestamp(long timestamp)
	{
		builder.setTimestamp(timestamp);
	}
	
	public List<PokemonDetails> getPokemon()
	{
		return pokemon;
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
					pokemon.add(new PokemonDetails(item.getItem().getPokemon()));
				}
			}
			System.out.println(response);
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
