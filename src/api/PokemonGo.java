package api;

import java.util.LinkedList;
import java.util.List;

import main.Pokemon.RequestEnvelop.AuthInfo;
import main.RequestHandler;
import requests.InventoryRequest;
import requests.ProfileRequest;

public class PokemonGo 
{
	private AuthInfo auth;
	private RequestHandler requestHandler;
	private PlayerProfile playerProfile;
	private List<PokemonDetails> pokemon;
	private long lastInventoryUpdate;
	
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
		
		
		pokemon = new LinkedList<PokemonDetails>();
		lastInventoryUpdate = 0;
	}
	
	
	public PlayerProfile getPlayerProfile()
	{

			ProfileRequest pr = new ProfileRequest();
			requestHandler.addRequest(pr);
			requestHandler.sendRequests();
			
			return pr.getProfile();

		

	}
	
	public List<PokemonDetails> getPokemon()
	{
		getInventory();
		return this.pokemon;
	}
	
	
	private void getInventory()
	{
		InventoryRequest invRequest = new InventoryRequest();
		invRequest.setTimestamp(lastInventoryUpdate);
		requestHandler.addRequest(invRequest);
		requestHandler.sendRequests();
		for (PokemonDetails newPokemon : invRequest.getPokemon())
		{
			this.pokemon.add(newPokemon);
		}
		
	}
	
}
