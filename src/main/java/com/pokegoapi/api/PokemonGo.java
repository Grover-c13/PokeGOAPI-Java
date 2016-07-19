package com.pokegoapi.api;


import POGOProtos.Data.Player.CurrencyOuterClass;
import POGOProtos.Data.PlayerDataOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Inventory.ItemIdOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.inventory.Bag;
import com.pokegoapi.api.inventory.CandyJar;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.player.*;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.RequestHandler;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

public class PokemonGo {

	@Getter
	RequestHandler requestHandler;
	@Getter
	PokeBank pokebank;
	@Getter
	Bag bag;
	@Getter
	Map map;
	@Getter
	CandyJar candyjar;
	private PlayerProfile playerProfile;
	@Getter
	@Setter
	private double latitude;
	@Getter
	@Setter
	private double longitude;
	@Getter
	@Setter
	private double altitude;

	private long lastInventoryUpdate;

	public PokemonGo(RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth, OkHttpClient client) {
		playerProfile = null;

		// send profile request to get the ball rolling
		requestHandler = new RequestHandler(this, auth, client);
		getPlayerProfile();
		// should have proper end point now.

		pokebank = new PokeBank(this);
		bag = new Bag(this);
		map = new Map(this);
		candyjar = new CandyJar(this);
		lastInventoryUpdate = 0;
	}

	private PlayerDataOuterClass.PlayerData getLocalPlayerAndInventory() throws LoginFailedException, RemoteServerException {
		PlayerDataOuterClass.PlayerData localPlayer = null;

		GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder().build();
		ServerRequest getPlayerServerRequest = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg);
		getRequestHandler().request(getPlayerServerRequest);

		GetInventoryMessage invReqMsg = GetInventoryMessage.newBuilder()
				.setLastTimestampMs(this.lastInventoryUpdate)
				.build();
		ServerRequest getInventoryServerRequest = new ServerRequest(RequestType.GET_INVENTORY, invReqMsg);
		getRequestHandler().request(getInventoryServerRequest);

		getRequestHandler().sendServerRequests();

		GetPlayerResponse getPlayerResponse = null;
		try {
			getPlayerResponse = GetPlayerResponse.parseFrom(getPlayerServerRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			System.err.println("Should never happen");
		}
		localPlayer = getPlayerResponse.getPlayerData();

		GetInventoryResponse getInventoryResponse = null;
		try {
			GetPlayerMessage reqMsg = GetPlayerMessage.newBuilder().build();
			ServerRequest serverRequest = new ServerRequest(RequestType.GET_PLAYER, reqMsg);
			getRequestHandler().request(serverRequest);
			getRequestHandler().sendServerRequests();
			GetPlayerResponse response = GetPlayerResponse.parseFrom(serverRequest.getData());
			localPlayer = response.getPlayerData();
			getInventoryResponse = GetInventoryResponse.parseFrom(getInventoryServerRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			System.err.println("Should never happen");
		}

		for (InventoryItemOuterClass.InventoryItem item : getInventoryResponse.getInventoryDelta().getInventoryItemsList()) {

			if (item.getInventoryItemData().getPokemonData().getPokemonId() != PokemonIdOuterClass.PokemonId.MISSINGNO) {
				pokebank.addPokemon(new Pokemon(item.getInventoryItemData().getPokemonData()));
			}

			if (item.getInventoryItemData().getItem().getItemId() != ItemIdOuterClass.ItemId.ITEM_UNKNOWN) {
				bag.addItem(new Item(item.getInventoryItemData().getItem()));
			}

		}

		return localPlayer;
	}

	public PlayerProfile getPlayerProfile() {
		if (playerProfile != null) {
			return playerProfile;
		}

		PlayerDataOuterClass.PlayerData localPlayer = null;
		try {
			localPlayer = getLocalPlayerAndInventory();
		} catch (LoginFailedException | RemoteServerException e) {
		}

		if (localPlayer == null) {
			return null;
		}
		playerProfile = new PlayerProfile();

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

		avatarAPI.setGender(localPlayer.getAvatar().getGender());
		avatarAPI.setBackpack(localPlayer.getAvatar().getBackpack());
		avatarAPI.setEyes(localPlayer.getAvatar().getEyes());
		avatarAPI.setHair(localPlayer.getAvatar().getHair());
		avatarAPI.setHat(localPlayer.getAvatar().getHat());
		avatarAPI.setPants(localPlayer.getAvatar().getPants());
		avatarAPI.setShirt(localPlayer.getAvatar().getShirt());
		avatarAPI.setShoes(localPlayer.getAvatar().getShoes());
		avatarAPI.setSkin(localPlayer.getAvatar().getSkin());

		bonusAPI.setNextCollectionTimestamp(localPlayer.getDailyBonus().getNextCollectedTimestampMs());
		bonusAPI.setNextDefenderBonusCollectTimestamp(localPlayer.getDailyBonus().getNextDefenderBonusCollectTimestampMs());

		playerProfile.setAvatar(avatarAPI);
		playerProfile.setDailyBonus(bonusAPI);

		return playerProfile;
	}

	public void setLocation(double latitude, double longitude, double altitude) {
		setLatitude(latitude);
		setLongitude(longitude);
		setAltitude(altitude);
	}
}
