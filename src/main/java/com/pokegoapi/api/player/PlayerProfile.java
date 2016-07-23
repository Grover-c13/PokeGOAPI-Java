/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.player;

import POGOProtos.Data.Player.CurrencyOuterClass;
import POGOProtos.Data.Player.EquippedBadgeOuterClass;
import POGOProtos.Data.Player.PlayerStatsOuterClass;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class PlayerProfile {
	private static final String TAG = PlayerProfile.class.getSimpleName();
	private final PokemonGo api;
	@Getter
	private long creationTime;
	@Getter
	private String username;
	@Getter
	private Team team;
	@Getter
	private int pokemonStorage;
	@Getter
	private int itemStorage;
	@Getter
	private EquippedBadgeOuterClass.EquippedBadge badge;

	@Getter
	private PlayerAvatar avatar;
	@Getter
	private DailyBonus dailyBonus;
	@Getter
	private ContactSettings contactSettings;
	@Getter
	private Map<Currency, Integer> currencies = new HashMap<Currency, Integer>();
	@Getter
	@Setter
	private PlayerStatsOuterClass.PlayerStats stats;

	public PlayerProfile(PokemonGo api) throws LoginFailedException, RemoteServerException {
		this.api = api;
		updateProfile();
	}

	/**
	 * Updates the player profile with the latest data.
	 *
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void updateProfile() throws LoginFailedException, RemoteServerException {
		GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder().build();
		ServerRequest getPlayerServerRequest = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg);
		api.getRequestHandler().sendServerRequests(getPlayerServerRequest);

		GetPlayerResponseOuterClass.GetPlayerResponse playerResponse = null;
		try {
			playerResponse = GetPlayerResponseOuterClass.GetPlayerResponse.parseFrom(getPlayerServerRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		badge = playerResponse.getPlayerData().getEquippedBadge();
		creationTime = playerResponse.getPlayerData().getCreationTimestampMs();
		itemStorage = playerResponse.getPlayerData().getMaxItemStorage();
		pokemonStorage = playerResponse.getPlayerData().getMaxPokemonStorage();
		team = Team.values()[playerResponse.getPlayerData().getTeam()];
		username = playerResponse.getPlayerData().getUsername();

		final PlayerAvatar avatarApi = new PlayerAvatar();
		final DailyBonus bonusApi = new DailyBonus();
		final ContactSettings contactApi = new ContactSettings();

		// maybe something more graceful?
		for (CurrencyOuterClass.Currency currency : playerResponse.getPlayerData().getCurrenciesList()) {
			try {
				addCurrency(currency.getName(), currency.getAmount());
			} catch (InvalidCurrencyException e) {
				Log.w(TAG, "Error adding currency. You can probably ignore this.", e);
			}
		}

		avatarApi.setGender(playerResponse.getPlayerData().getAvatar().getGender());
		avatarApi.setBackpack(playerResponse.getPlayerData().getAvatar().getBackpack());
		avatarApi.setEyes(playerResponse.getPlayerData().getAvatar().getEyes());
		avatarApi.setHair(playerResponse.getPlayerData().getAvatar().getHair());
		avatarApi.setHat(playerResponse.getPlayerData().getAvatar().getHat());
		avatarApi.setPants(playerResponse.getPlayerData().getAvatar().getPants());
		avatarApi.setShirt(playerResponse.getPlayerData().getAvatar().getShirt());
		avatarApi.setShoes(playerResponse.getPlayerData().getAvatar().getShoes());
		avatarApi.setSkin(playerResponse.getPlayerData().getAvatar().getSkin());

		bonusApi.setNextCollectionTimestamp(
				playerResponse.getPlayerData().getDailyBonus().getNextCollectedTimestampMs()
		);
		bonusApi.setNextDefenderBonusCollectTimestamp(
				playerResponse.getPlayerData().getDailyBonus().getNextDefenderBonusCollectTimestampMs()
		);

		avatar = avatarApi;
		dailyBonus = bonusApi;


	}

	/**
	 * Add currency.
	 *
	 * @param name   the name
	 * @param amount the amount
	 * @throws InvalidCurrencyException the invalid currency exception
	 */
	public void addCurrency(String name, int amount) throws InvalidCurrencyException {
		try {
			currencies.put(Currency.valueOf(name), amount);
		} catch (Exception e) {
			throw new InvalidCurrencyException();
		}
	}

	/**
	 * Gets currency.
	 *
	 * @param currency the currency
	 * @return the currency
	 * @throws InvalidCurrencyException the invalid currency exception
	 */
	public int getCurrency(Currency currency) throws InvalidCurrencyException {
		if (currencies.containsKey(currency)) {
			return currencies.get(currency);
		} else {
			throw new InvalidCurrencyException();
		}
	}

	public enum Currency {
		STARDUST, POKECOIN;
	}
}
