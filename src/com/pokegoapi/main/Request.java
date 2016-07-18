package com.pokegoapi.main;

import POGOProtos.Networking.Requests.RequestOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import com.google.protobuf.ByteString;
import lombok.Getter;

public abstract class Request 
{
	@Getter
	RequestOuterClass.Request.Builder builder;
	
	/**
	 * Get the RPC id for the call
	 * 
	 * @return Integer
	 */
	public abstract RequestTypeOuterClass.RequestType getRpcId();
	
	/**
	 * Handle the response of the call
	 * @param Payload to be parsed by protobuf
	 * @param payload
	 */
	public abstract void handleResponse(ByteString payload);
	
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
		builder = RequestOuterClass.Request.newBuilder();
		builder.setRequestType(getRpcId());
	}

	/**
	 * Get the protobuff request
	 * @return
	 */
	public RequestOuterClass.Request getRequest()
	{
		if(getInput() != null) {
			builder.setRequestMessage(ByteString.copyFrom(getInput())).build();
		}
		
		return builder.build();
	}
	
	public void handleExtensions() {  }

}
