package com.pokegoapi.requests;

import java.util.LinkedList;
import java.util.List;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import com.pokegoapi.api.ContactSettings;
import com.pokegoapi.api.DailyBonus;
import com.pokegoapi.api.PlayerAvatar;
import com.pokegoapi.api.PlayerProfile;
import com.pokegoapi.api.Team;
import com.pokegoapi.api.inventory.PokemonDetails;
import com.pokegoapi.main.Pokemon.ClientPlayerDetails;
import com.pokegoapi.main.Pokemon.InventoryRequestProto.Builder;
import com.pokegoapi.main.Pokemon.InventoryResponseProto;
import com.pokegoapi.main.Pokemon.InventoryResponseProto.InventoryItemResponseProto;
import com.pokegoapi.main.Pokemon.Payload;
import com.pokegoapi.main.Pokemon;
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
		builder = Pokemon.InventoryRequestProto.newBuilder();
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
