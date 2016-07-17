package com.pokegoapi.requests;

import com.pokegoapi.main.Inventory.EvolvePokemonProto.Builder;
import com.pokegoapi.main.Inventory.EvolvePokemonProto;
import com.pokegoapi.main.Inventory.EvolvePokemonOutProto;
import com.pokegoapi.main.Communication.Payload;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.main.Request;

public class PokemonEvolveRequest extends Request {
	private Builder builder;
	private EvolvePokemonOutProto out;
	
	public PokemonEvolveRequest(long entid)
	{
		builder = EvolvePokemonProto.newBuilder();
		builder.setPokemonId(entid);
	}
	
	@Override
	public int getRpcId()
	{
		return 125;
	}
	

	public EvolvePokemonOutProto getOutput()
	{
		return out;
	}
	
	@Override
	public void handleResponse(Payload payload)
	{
		try
		{
			out = EvolvePokemonOutProto.parseFrom(payload.toByteArray());

		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public byte[] getInput()
	{
		EvolvePokemonProto in = builder.build();
		return in.toByteArray();
	}

}
