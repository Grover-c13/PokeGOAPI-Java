package mx.may.courtney.pgo.requests;

import mx.may.courtney.pgo.Pgo;
import mx.may.courtney.pgo.Pgo.Payload;
import mx.may.courtney.pgo.Request;

public class SettingsRequest extends Request
{
	private mx.may.courtney.pgo.Pgo.SettingsRequest.Builder settingsRequestBuilder;
	

	public SettingsRequest()
	{
		settingsRequestBuilder = Pgo.SettingsRequest.newBuilder();
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
	public void handleResponse(Payload payload)
	{

		
	}
	
}
