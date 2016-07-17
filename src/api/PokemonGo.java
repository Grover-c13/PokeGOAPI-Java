package api;

import main.Pokemon.RequestEnvelop.AuthInfo;
import main.RequestHandler;
import requests.InventoryRequest;
import requests.ProfileRequest;

public class PokemonGo 
{
	private AuthInfo auth;
	private RequestHandler requestHandler;
	private PlayerProfile playerProfile;
	
	public PokemonGo(AuthInfo auth)
	{
		this.auth = auth;
		playerProfile = null;
		// send profile request to get the ball rolling
		requestHandler = new RequestHandler(auth);
		ProfileRequest pr = new ProfileRequest();
		requestHandler.addRequest(pr);
		requestHandler.sendRequests();
		// should have proper end point now.
		
	}
	
	
	public PlayerProfile getPlayerProfile()
	{

			ProfileRequest pr = new ProfileRequest();
			requestHandler.addRequest(pr);
			requestHandler.sendRequests();
			
			return pr.getProfile();

		

	}
	
	
	public void getInventory()
	{
		requestHandler.addRequest(new InventoryRequest());
		requestHandler.sendRequests();
	}
	
}
