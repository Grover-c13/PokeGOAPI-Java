package com.pokegoapi.main;

import com.google.protobuf.ByteString;
import com.pokegoapi.main.Communication.Payload;
import com.pokegoapi.main.Communication.Request.Builder;
import lombok.Getter;

public abstract class Request 
{
	@Getter Builder builder;
	
	/**
	 * Get the RPC id for the call
	 * 
	 * @return Integer
	 */
	public abstract Communication.Method getRpcId();
	
	/**
	 * Handle the response of the call
	 * @param Payload to be parsed by protobuf
	 */
	public abstract void handleResponse(Payload payload);
	
	/**
	 * Get the input data that is needed to make the request
	 * 
	 * @return byte array
	 */
	public byte[] getInput() {
		return null;
	}
	
	/**
	 * Build a new request wrapper
	 */
	public Request() {
		builder = Communication.Request.newBuilder();
		builder.setType(getRpcId());
	}

	/**
	 * Get the protobuff request
	 * @return
	 */
	public Communication.Request getRequest()
	{
		if(getInput() != null) {
			builder.setData(ByteString.copyFrom(getInput())).build();
		}
		
		return builder.build();
	}
	
	public void handleExtensions() {  }

}
