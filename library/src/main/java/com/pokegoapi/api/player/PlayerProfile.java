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
import POGOProtos.Data.Player.EquippedBadgeOuterClass.EquippedBadge;
import POGOProtos.Data.PlayerDataOuterClass.PlayerData;
import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage;
import POGOProtos.Networking.Requests.Messages.EquipBadgeMessageOuterClass.EquipBadgeMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.EquipBadgeResponseOuterClass;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Stats;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;


public class PlayerProfile {
	private static final String TAG = PlayerProfile.class.getSimpleName();
	private final PokemonGo api;
	private PlayerData playerData;
	private EquippedBadge badge;
	private PlayerAvatar avatar;
	private DailyBonus dailyBonus;
	private ContactSettings contactSettings;
	private Map<Currency, Integer> currencies = new EnumMap<Currency, Integer>(Currency.class);
	@Setter
	private Stats stats;

	private boolean init;

	public PlayerProfile(PokemonGo api) throws LoginFailedException, RemoteServerException {
		this.api = api;
		init = false;
	}

	/**
	 * Updates the player profile with the latest data.
	 *
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void updateProfile() throws RemoteServerException, LoginFailedException {

		GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder().build();
		ServerRequest getPlayerServerRequest = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg);
		api.getRequestHandler().sendServerRequests(getPlayerServerRequest);

		GetPlayerResponse playerResponse = null;
		try {
			playerResponse = GetPlayerResponse.parseFrom(getPlayerServerRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		playerData = playerResponse.getPlayerData();

		avatar = new PlayerAvatar(playerData.getAvatar());
		dailyBonus = new DailyBonus(playerData.getDailyBonus());
		contactSettings = new ContactSettings(playerData.getContactSettings());

		// maybe something more graceful?
		for (CurrencyOuterClass.Currency currency : playerResponse.getPlayerData().getCurrenciesList()) {
			try {
				addCurrency(currency.getName(), currency.getAmount());
			} catch (InvalidCurrencyException e) {
				Log.w(TAG, "Error adding currency. You can probably ignore this.", e);
			}
		}

		init = true;
	}

	/**
	 * Accept the rewards granted and the items unlocked by gaining a trainer level up. Rewards are retained by the
	 * server until a player actively accepts them.
	 * The rewarded items are automatically inserted into the players item bag.
	 *
	 * @see PlayerLevelUpRewards
	 * @param level the trainer level that you want to accept the rewards for
	 * @return a PlayerLevelUpRewards object containing information about the items rewarded and unlocked for this level
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException if the server failed to respond
	 */
	public PlayerLevelUpRewards acceptLevelUpRewards(int level) throws RemoteServerException, LoginFailedException {
		// Check if we even have achieved this level yet
		if (level > stats.getLevel()) {
			return new PlayerLevelUpRewards(PlayerLevelUpRewards.Status.NOT_UNLOCKED_YET);
		}
		LevelUpRewardsMessage msg = LevelUpRewardsMessage.newBuilder()
				.setLevel(level)
				.build();
		ServerRequest serverRequest = new ServerRequest(RequestType.LEVEL_UP_REWARDS, msg);
		api.getRequestHandler().sendServerRequests(serverRequest);
		LevelUpRewardsResponse response;
		try {
			response = LevelUpRewardsResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		// Add the awarded items to our bag
		ItemBag bag = api.getInventories().getItemBag();
		for (ItemAward itemAward : response.getItemsAwardedList()) {
			Item item = bag.getItem(itemAward.getItemId());
			item.setCount(item.getCount() + itemAward.getItemCount());
		}
		// Build a new rewards object and return it
		return new PlayerLevelUpRewards(response);
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
	 * Check and equip badges.
	 *
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 */

	public void checkAndEquipBadges() throws LoginFailedException, RemoteServerException {
		CheckAwardedBadgesMessage msg =
				CheckAwardedBadgesMessage.newBuilder().build();
		ServerRequest serverRequest = new ServerRequest(RequestType.CHECK_AWARDED_BADGES, msg);
		api.getRequestHandler().sendServerRequests(serverRequest);
		CheckAwardedBadgesResponse response;
		try {
			response = CheckAwardedBadgesResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		if (response.getSuccess()) {
			for (int i = 0; i < response.getAwardedBadgesCount(); i++) {
				EquipBadgeMessage msg1 = EquipBadgeMessage.newBuilder()
						.setBadgeType(response.getAwardedBadges(i))
						.setBadgeTypeValue(response.getAwardedBadgeLevels(i)).build();
				ServerRequest serverRequest1 = new ServerRequest(RequestType.EQUIP_BADGE, msg1);
				api.getRequestHandler().sendServerRequests(serverRequest1);
				EquipBadgeResponseOuterClass.EquipBadgeResponse response1;
				try {
					response1 = EquipBadgeResponseOuterClass.EquipBadgeResponse.parseFrom(serverRequest1.getData());
					badge = response1.getEquipped();
				} catch (InvalidProtocolBufferException e) {
					throw new RemoteServerException(e);
				}
			}
		}
	}

	/**
	 * Gets currency.
	 *
	 * @param currency the currency
	 * @return the currency
	 * @throws InvalidCurrencyException the invalid currency exception
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 */
	public int getCurrency(Currency currency)
			throws InvalidCurrencyException, LoginFailedException, RemoteServerException {
		if (!init) {
			updateProfile();
		}
		if (currencies.containsKey(currency)) {
			return currencies.get(currency);
		} else {
			throw new InvalidCurrencyException();
		}
	}

	public enum Currency {
		STARDUST, POKECOIN;
	}

	/**
	 * Gets raw player data proto
	 *
	 * @return Player data
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 */
	public PlayerData getPlayerData()
			throws LoginFailedException, RemoteServerException {
		if (!init) {
			updateProfile();
		}
		return playerData;
	}

	/**
	 * Gets avatar
	 *
	 * @return Player Avatar object
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 */
	public PlayerAvatar getAvatar()
			throws LoginFailedException, RemoteServerException {
		if (!init) {
			updateProfile();
		}
		return avatar;
	}

	/**
	 * Gets daily bonus
	 *
	 * @return DailyBonus object
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 */
	public DailyBonus getDailyBonus()
			throws LoginFailedException, RemoteServerException {
		if (!init) {
			updateProfile();
		}
		return dailyBonus;
	}

	/**
	 * Gets contact settings
	 *
	 * @return ContactSettings object
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 */
	public ContactSettings getContactSettings()
			throws LoginFailedException, RemoteServerException {
		if (!init) {
			updateProfile();
		}
		return contactSettings;
	}

	/**
	 * Gets a map of all currencies
	 *
	 * @return map of currencies
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 */
	public Map<Currency, Integer> getCurrencies()
			throws LoginFailedException, RemoteServerException {
		if (!init) {
			updateProfile();
		}
		return currencies;
	}



	/**
	 * Gets player stats
	 *
	 * @return stats API objet
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 */
	public Stats getStats()
			throws LoginFailedException, RemoteServerException {
		if (stats == null) {
			api.getInventories().updateInventories();
		}
		return stats;
	}
}
