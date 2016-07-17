package com.pokegoapi.requests;

import com.google.protobuf.ByteString;

import com.pokegoapi.main.Pokemon;
import com.pokegoapi.main.Pokemon.Payload;
import com.pokegoapi.main.Request;

public class SettingsRequest extends Request {
	private Pokemon.SettingsRequest.Builder settingsRequestBuilder;


	public SettingsRequest()
	{
		settingsRequestBuilder = Pokemon.SettingsRequest.newBuilder();
	}


	public void setUUID(String uuid)
	{
		settingsRequestBuilder.setUUID(uuid);
	}

	@Override
	public int getRpcId()
	{
		return 5;
	}

	@Override
	public void handleExtensions()
	{

	}


	@Override
	public void handleResponse(Payload payload) {


	}
	

	public byte[] getInput() 
	{
		return null;
	}


}
