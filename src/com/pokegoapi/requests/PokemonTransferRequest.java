package com.pokegoapi.requests;

import POGOProtos.Networking.Requests.Messages.ReleasePokemonMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.main.Request;

public class PokemonTransferRequest extends Request {
	private ReleasePokemonMessageOuterClass.ReleasePokemonMessage.Builder builder;
	private int candies;
	private int status;
	
	public PokemonTransferRequest(long entid)
	{
		candies = 0;
		status = 0;
		builder = ReleasePokemonMessageOuterClass.ReleasePokemonMessage.newBuilder();
		builder.setPokemonId(entid);
	}
	
	@Override
	public RequestTypeOuterClass.RequestType getRpcId()
	{
		return RequestTypeOuterClass.RequestType.RELEASE_POKEMON;
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
	public void handleResponse(ByteString payload)
	{
		try
		{
			ReleasePokemonResponseOuterClass.ReleasePokemonResponse out = ReleasePokemonResponseOuterClass.ReleasePokemonResponse.parseFrom(payload);
			candies = out.getCandyAwarded();
			status = out.getStatus();
		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public byte[] getInput() {
		return builder.build().toByteArray();
	}

}
