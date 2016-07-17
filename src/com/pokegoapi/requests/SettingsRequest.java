package com.pokegoapi.requests;

import com.google.protobuf.ByteString;

import com.pokegoapi.main.Communication.Payload;
import com.pokegoapi.main.Player;
import com.pokegoapi.main.Request;
import com.pokegoapi.main.Player.SettingsRequest.Builder;


public class SettingsRequest extends Request {
	private Builder settingsRequestBuilder;


	public SettingsRequest()
	{
		settingsRequestBuilder = Player.SettingsRequest.newBuilder();
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
