package com.pokegoapi.api;

import java.util.LinkedList;
import java.util.List;

import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.inventory.PokemonDetails;
import com.pokegoapi.main.Communication.RequestEnvelop.AuthInfo;
import com.pokegoapi.main.RequestHandler;
import com.pokegoapi.requests.FortDetailsRequest;
import com.pokegoapi.requests.InventoryRequest;
import com.pokegoapi.requests.ProfileRequest;

public class PokemonGo 
{
	private AuthInfo auth;
	private RequestHandler requestHandler;
	private PlayerProfile playerProfile;
	private PokeBank pokebank;

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
		
		pokebank = new PokeBank(this);

		lastInventoryUpdate = 0;
		getInventory();
	}
	
	
	public PlayerProfile getPlayerProfile()
	{

			ProfileRequest pr = new ProfileRequest();
			requestHandler.addRequest(pr);
			requestHandler.sendRequests();
			
			return pr.getProfile();

		

	}
	

	
	
	public PokeBank getPokeBank()
	{
		return this.pokebank;
	}
	
	
	public RequestHandler getRequestHandler()
	{
		return this.requestHandler;
	}
	
	private void getInventory()
	{
		InventoryRequest invRequest = new InventoryRequest();
		invRequest.setTimestamp(lastInventoryUpdate);
		requestHandler.addRequest(invRequest);
		requestHandler.sendRequests();
		for (PokemonDetails newPokemon : invRequest.getPokemon())
		{
			this.pokebank.addPokemon(newPokemon);
		}
		
	}
	
	
	public FortDetails getFortDetails(String id, long lon, long lat)
	{
		FortDetailsRequest request = new FortDetailsRequest(id);
		request.setLatitude(lat);
		request.setLongitude(lon);
		requestHandler.addRequest(request);
		requestHandler.sendRequests();
		return new FortDetails(request.getOutput());
	}
	
}
