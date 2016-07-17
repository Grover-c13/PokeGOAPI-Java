package com.pokegoapi.requests;

import com.pokegoapi.main.Pokemon.TransferPokemonProto.Builder;
import com.pokegoapi.main.Pokemon.Payload;
import com.pokegoapi.main.Pokemon.TransferPokemonOutProto;
import com.pokegoapi.main.Pokemon.TransferPokemonProto;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.main.Pokemon;
import com.pokegoapi.main.Request;

public class PokemonTransferRequest extends Request {
	private Builder builder;
	private int candies;
	private int status;
	
	public PokemonTransferRequest(long entid)
	{
		candies = 0;
		status = 0;
		builder = Pokemon.TransferPokemonProto.newBuilder();
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
