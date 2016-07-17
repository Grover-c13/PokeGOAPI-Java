package com.pokegoapi.requests;

import com.pokegoapi.main.Inventory.TransferPokemonProto.Builder;
import com.pokegoapi.main.Communication.Payload;
import com.pokegoapi.main.Inventory.TransferPokemonOutProto;
import com.pokegoapi.main.Inventory.TransferPokemonProto;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.main.Request;

public class PokemonTransferRequest extends Request {
	private Builder builder;
	private int candies;
	private int status;
	
	public PokemonTransferRequest(long entid)
	{
		candies = 0;
		status = 0;
		builder = TransferPokemonProto.newBuilder();
		builder.setPokemonId(entid);
	}
	
	@Override
	public int getRpcId()
	{
		return 112;
	}
	
	public int getCandies()
	{
		return candies;
	}

	public int getStatus()
	{
		return status;
	}
	
	
	@Override
	public void handleResponse(Payload payload)
	{
		try
		{
			TransferPokemonOutProto out = TransferPokemonOutProto.parseFrom(payload.toByteArray());
			candies = out.getCandyAwarded();
			status = out.getStatus();
		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public byte[] getInput()
	{
		TransferPokemonProto in = builder.build();
		System.out.println(in);
		return in.toByteArray();
	}

}
