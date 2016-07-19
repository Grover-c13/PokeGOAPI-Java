package com.pokegoapi.api;


import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Inventory.ItemIdOuterClass;
import POGOProtos.LocalPlayerOuterClass;
import POGOProtos.Networking.EnvelopesOuterClass;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import POGOProtos.Player.CurrencyOuterClass.Currency;
import POGOProtos.Inventory.ItemIdOuterClass.ItemId;
import com.pokegoapi.api.inventory.Bag;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.inventory.Pokemon;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.player.*;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.main.RequestHandler;
import com.pokegoapi.main.ServerRequest;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import okhttp3.OkHttpClient;

import java.util.List;

public class PokemonGo {

	private RequestHandler requestHandler;
	private PlayerProfile playerProfile;
	private PokeBank pokebank;
	private Bag bag;
	private Map map;
	private double latitude;
	private double longitude;

	private long lastInventoryUpdate;

	public PokemonGo(EnvelopesOuterClass.Envelopes.RequestEnvelope.AuthInfo auth, OkHttpClient client) {
		playerProfile = null;

		// send profile request to get the ball rolling
		requestHandler = new RequestHandler(auth, client);
		getPlayerProfile();
		// should have proper end point now.

		pokebank = new PokeBank(this);
		bag = new Bag(this);
		map = new Map(this);
		lastInventoryUpdate = 0;
		getInventory(); // data will be loaded on constructor and then kept, all future requests will be updates after this call
	}

	private LocalPlayerOuterClass.LocalPlayer getLocalPlayer() {
		LocalPlayerOuterClass.LocalPlayer localPlayer = null;

		// server request
		try {
			GetPlayerMessage reqMsg = GetPlayerMessage.newBuilder().build();
			ServerRequest serverRequest = new ServerRequest(RequestType.GET_PLAYER, reqMsg);
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

	public Map getMap() {
		return map;
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
		for (Currency currency : localPlayer.getCurrenciesList()) {
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
			GetInventoryMessage reqMsg = GetInventoryMessage.newBuilder()
					.setLastTimestampMs(this.lastInventoryUpdate)
					.build();
			ServerRequest serverRequest = new ServerRequest(RequestType.GET_INVENTORY, reqMsg);
			getRequestHandler().request(serverRequest);
			getRequestHandler().sendServerRequests();
			GetInventoryResponse response = GetInventoryResponse.parseFrom(serverRequest.getData());

			for (InventoryItemOuterClass.InventoryItem item : response.getInventoryDelta().getInventoryItemsList()) {

				if (item.getInventoryItemData().getPokemonData().getPokemonId() != PokemonIdOuterClass.PokemonId.MISSINGNO) {
					this.pokebank.addPokemon( new Pokemon( item.getInventoryItemData().getPokemonData() ) );
				}

				if (item.getInventoryItemData().getItem().getItemId() != ItemIdOuterClass.ItemId.ITEM_UNKNOWN) {
					bag.addItem( new Item( item.getInventoryItemData().getItem() ) );
				}

			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}




	}

	public void setLatitude(double latitude) { this.latitude = latitude; }

	public void setLongitude(double latitude) { this.longitude = longitude; }

	public double getLatitude() { return latitude; }

	public double getLongitude() { return longitude; }

	public RequestHandler getRequestHandler() {
		return requestHandler;
	}

	public PokeBank getPokebank() {
		return pokebank;
	}
}
