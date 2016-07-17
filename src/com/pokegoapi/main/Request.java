package com.pokegoapi.main;
import com.google.protobuf.ByteString;

import com.pokegoapi.main.Communication.Payload;
import com.pokegoapi.main.Communication.Request.Builder;

public abstract class Request 
{
	private Builder builder;
	
	
	public abstract int getRpcId();
	public abstract void handleResponse(Payload payload);
	public abstract byte[] getInput();
	
	public Request()
	{
		builder = Communication.Request.newBuilder();
		builder.setType(getRpcId());
	}
	
	public Builder getBuilder()
	{
		return this.builder;
	}
	

	
	public Communication.Request getRequest()
	{
		if(getInput() != null)
		{
			builder.setData(ByteString.copyFrom(this.getInput())).build();
		}
		
		return builder.build();
	}
	
	
	public void handleExtensions()
	{
		
	}
	

}
