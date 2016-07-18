package com.pokegoapi.requests;

import POGOProtos.Networking.Requests.Messages.EvolvePokemonMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.main.Request;

public class PokemonEvolveRequest extends Request {
	private EvolvePokemonMessageOuterClass.EvolvePokemonMessage.Builder builder;
	private EvolvePokemonResponseOuterClass.EvolvePokemonResponse out;
	
	public PokemonEvolveRequest(long entid)
	{
		builder = EvolvePokemonMessageOuterClass.EvolvePokemonMessage.newBuilder();
		builder.setPokemonId(entid);
	}
	
	@Override
	public RequestTypeOuterClass.RequestType getRpcId()
	{
		return RequestTypeOuterClass.RequestType.EVOLVE_POKEMON;
	}
	

	public EvolvePokemonResponseOuterClass.EvolvePokemonResponse getOutput()
	{
		return out;
	}
	
	@Override
	public void handleResponse(ByteString payload)
	{
		try
		{
			out = EvolvePokemonResponseOuterClass.EvolvePokemonResponse.parseFrom(payload);

		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public byte[] getInput()
	{
		return builder.build().toByteArray();
	}

}
