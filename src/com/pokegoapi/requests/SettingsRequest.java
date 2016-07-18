package com.pokegoapi.requests;

import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import com.google.protobuf.ByteString;

import com.pokegoapi.main.Request;


public class SettingsRequest extends Request {
	private DownloadSettingsMessageOuterClass.DownloadSettingsMessage.Builder settingsRequestBuilder;


	public SettingsRequest()
	{
		settingsRequestBuilder = DownloadSettingsMessageOuterClass.DownloadSettingsMessage.newBuilder();
	}


	public void setUUID(String uuid)
	{
		settingsRequestBuilder.setHash(uuid);
	}

	@Override
	public RequestTypeOuterClass.RequestType getRpcId()
	{
		return RequestTypeOuterClass.RequestType.DOWNLOAD_SETTINGS;
	}

	@Override
	public void handleExtensions()
	{

	}


	@Override
	public void handleResponse(ByteString payload) {


	}
	

	public byte[] getInput() 
	{
		return null;
	}


}
