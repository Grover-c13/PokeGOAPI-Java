package requests;

import main.Pokemon;
import main.Pokemon.Payload;
import main.Request;

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

}
