package com.pokegoapi.api;


import POGOProtos.Networking.EnvelopesOuterClass;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.inventory.Pokemon;
import com.pokegoapi.main.RequestHandler;
import com.pokegoapi.requests.FortDetailsRequest;
import com.pokegoapi.requests.GetMapObjectsRequest;
import com.pokegoapi.requests.InventoryRequest;
import com.pokegoapi.requests.ProfileRequest;

import lombok.Getter;

import java.util.List;

public class PokemonGo {

  @Getter RequestHandler requestHandler;
  private PlayerProfile playerProfile;
  @Getter PokeBank pokebank;

  private long lastInventoryUpdate;

  public PokemonGo(EnvelopesOuterClass.Envelopes.RequestEnvelope.AuthInfo auth) {
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


  public PlayerProfile getPlayerProfile() {
    ProfileRequest pr = new ProfileRequest();
    requestHandler.addRequest(pr);
    requestHandler.sendRequests();

    return pr.getProfile();
  }


  private void getInventory() {
    InventoryRequest invRequest = new InventoryRequest();
    invRequest.setTimestamp(lastInventoryUpdate);
    requestHandler.addRequest(invRequest);
    requestHandler.sendRequests();
    for (Pokemon newPokemon : invRequest.getPokemon()) {
      this.pokebank.addPokemon(newPokemon);
    }
  }


  public FortDetails getFortDetails(String id, long lon, long lat) {
    FortDetailsRequest request = new FortDetailsRequest(id, lon, lat);
    requestHandler.addRequest(request);
    requestHandler.sendRequests();
    return new FortDetails(request.getOutput());
  }

  
  public void getMapObjects(List<Long> cellIds, double latitude, double longitude) {
    requestHandler.setLatitude(latitude);
    requestHandler.setLongitude(longitude);
    requestHandler.setAltitude(0);
    GetMapObjectsRequest request = new GetMapObjectsRequest(cellIds, latitude, longitude);
    requestHandler.addRequest(request);
    requestHandler.sendRequests();
  }
}
