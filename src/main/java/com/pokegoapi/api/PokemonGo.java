package com.pokegoapi.api;


import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.LocalPlayerOuterClass;
import POGOProtos.Networking.EnvelopesOuterClass;
import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import POGOProtos.Player.CurrencyOuterClass;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.inventory.Pokemon;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.player.*;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.main.RequestHandler;
import com.pokegoapi.main.ServerRequest;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass;
import okhttp3.OkHttpClient;

import java.util.List;

public class PokemonGo {

	RequestHandler requestHandler;
	private PlayerProfile playerProfile;
	PokeBank pokebank;
	Map map;
	private long lastInventoryUpdate;

	public PokemonGo(EnvelopesOuterClass.Envelopes.RequestEnvelope.AuthInfo auth, OkHttpClient client) {
		playerProfile = null;

		// send profile request to get the ball rolling
		requestHandler = new RequestHandler(auth, client);
		getPlayerProfile();
		// should have proper end point now.

		pokebank = new PokeBank(this);

		lastInventoryUpdate = 0;
		getInventory(); // data will be loaded on constructor and then kept, all future requests will be updates after this call
	}

	private LocalPlayerOuterClass.LocalPlayer getLocalPlayer() {
		LocalPlayerOuterClass.LocalPlayer localPlayer = null;

		// server request
		try {
			GetPlayerMessageOuterClass.GetPlayerMessage reqMsg = GetPlayerMessageOuterClass.GetPlayerMessage.newBuilder().build();
			ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.GET_PLAYER, reqMsg);
			getRequestHandler().request(serverRequest);
			getRequestHandler().sendServerRequests();
			GetPlayerResponse response = GetPlayerResponse.parseFrom(serverRequest.getData());
			localPlayer = response.getLocalPlayer();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return localPlayer;
	}

	public PlayerProfile getPlayerProfile() {
		if (playerProfile != null)
		{
			return playerProfile;
		}

		playerProfile = new PlayerProfile();

		LocalPlayerOuterClass.LocalPlayer localPlayer = getLocalPlayer();

		if (localPlayer == null)
		{
			return null;
		}



		playerProfile.setBadge(localPlayer.getEquippedBadge());
		playerProfile.setCreationTime(localPlayer.getCreationTimestampMs());
		playerProfile.setItemStorage(localPlayer.getMaxItemStorage());
		playerProfile.setPokemonStorage(localPlayer.getMaxPokemonStorage());
		playerProfile.setTeam(Team.values()[localPlayer.getTeam()]);
		playerProfile.setUsername(localPlayer.getUsername());

		PlayerAvatar avatarAPI = new PlayerAvatar();
		DailyBonus bonusAPI = new DailyBonus();
		ContactSettings contactAPI = new ContactSettings();

		// maybe something more graceful?
		for (CurrencyOuterClass.Currency currency : localPlayer.getCurrenciesList()) {
			try {
				playerProfile.addCurrency(currency.getName(), currency.getAmount());
			} catch (InvalidCurrencyException e) {
				e.printStackTrace();
			}
		}

		avatarAPI.setGender(localPlayer.getAvatarDetails().getGender());
		avatarAPI.setBackpack(localPlayer.getAvatarDetails().getBackpack());
		avatarAPI.setEyes(localPlayer.getAvatarDetails().getEyes());
		avatarAPI.setHair(localPlayer.getAvatarDetails().getHair());
		avatarAPI.setHat(localPlayer.getAvatarDetails().getHat());
		avatarAPI.setPants(localPlayer.getAvatarDetails().getPants());
		avatarAPI.setShirt(localPlayer.getAvatarDetails().getShirt());
		avatarAPI.setShoes(localPlayer.getAvatarDetails().getShoes());
		avatarAPI.setSkin(localPlayer.getAvatarDetails().getSkin());

		bonusAPI.setNextCollectionTimestamp(localPlayer.getDailyBonus().getNextCollectedTimestampMs());
		bonusAPI.setNextDefenderBonusCollectTimestamp(localPlayer.getDailyBonus().getNextDefenderBonusCollectTimestampMs());

		playerProfile.setAvatar(avatarAPI);
		playerProfile.setDailyBonus(bonusAPI);

		return playerProfile;
	}




	private void getInventory() {

		// server request
		try {
			GetInventoryMessageOuterClass.GetInventoryMessage reqMsg = GetInventoryMessageOuterClass.GetInventoryMessage.newBuilder()
					.setLastTimestampMs(this.lastInventoryUpdate)
					.build();
			ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.GET_INVENTORY, reqMsg);
			getRequestHandler().request(serverRequest);
			getRequestHandler().sendServerRequests();
			GetInventoryResponseOuterClass.GetInventoryResponse response = GetInventoryResponseOuterClass.GetInventoryResponse.parseFrom(serverRequest.getData());

			for (InventoryItemOuterClass.InventoryItem item : response.getInventoryDelta().getInventoryItemsList()) {

				if (item.getInventoryItemData().getPokemonData().getPokemonId() != PokemonIdOuterClass.PokemonId.MISSINGNO) {
					Pokemon p = new Pokemon(item.getInventoryItemData().getPokemonData());
					this.pokebank.addPokemon(p);
					System.out.println(p.getPokemonId());
				}

			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}




	}






	public RequestHandler getRequestHandler() {
		return requestHandler;
	}

	public PokeBank getPokebank() {
		return pokebank;
	}
}
