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

import java.util.EnumMap;
import java.util.Map;

import POGOProtos.Data.Player.CurrencyOuterClass;
import POGOProtos.Data.Player.EquippedBadgeOuterClass.EquippedBadge;
import POGOProtos.Data.Player.PlayerStatsOuterClass;
import POGOProtos.Data.PlayerDataOuterClass.PlayerData;
import POGOProtos.Enums.TutorialStateOuterClass;
import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage;
import POGOProtos.Networking.Requests.Messages.EquipBadgeMessageOuterClass.EquipBadgeMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.Messages.MarkTutorialCompleteMessageOuterClass.MarkTutorialCompleteMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.EquipBadgeResponseOuterClass;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import POGOProtos.Networking.Responses.MarkTutorialCompleteResponseOuterClass.MarkTutorialCompleteResponse;
import lombok.Setter;

public class PlayerProfile {
	private static final String TAG = PlayerProfile.class.getSimpleName();
	private final PokemonGo api;
	private final PlayerLocale playerLocale;
	private PlayerData playerData;
	private EquippedBadge badge;
	private PlayerAvatar avatar;
	private DailyBonus dailyBonus;
	private ContactSettings contactSettings;
	private Map<Currency, Integer> currencies = new EnumMap<>(Currency.class);
	@Setter
	private Stats stats;
	private TutorialState tutorialState;

	/**
	 * @param api the api
	 * @throws LoginFailedException  when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 */
	public PlayerProfile(PokemonGo api) throws LoginFailedException, RemoteServerException {
		this.api = api;
		this.playerLocale = new PlayerLocale();

		if (playerData == null) {
			updateProfile();
		}
	}

	/**
	 * Updates the player profile with the latest data.
	 *
	 * @throws LoginFailedException  when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 */
	public void updateProfile() throws RemoteServerException, LoginFailedException {
		GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder()
				.setPlayerLocale(playerLocale.getPlayerLocale())
				.build();
		ServerRequest getPlayerServerRequest = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg);
		api.getRequestHandler().sendServerRequests(getPlayerServerRequest);

		GetPlayerResponse playerResponse;
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

		// Tutorial state
		tutorialState = new TutorialState(playerData.getTutorialStateList());

		// Check if we are allowed to receive valid responses
		if (tutorialState.getTutorialStates().isEmpty()) {
			enableAccount();
		}
	}

	/**
	 * Accept the rewards granted and the items unlocked by gaining a trainer level up. Rewards are retained by the
	 * server until a player actively accepts them.
	 * The rewarded items are automatically inserted into the players item bag.
	 *
	 * @param level the trainer level that you want to accept the rewards for
	 * @return a PlayerLevelUpRewards object containing information about the items rewarded and unlocked for this level
	 * @throws LoginFailedException  when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @see PlayerLevelUpRewards
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
	 * @throws LoginFailedException  when the auth is invalid
	 * @throws RemoteServerException When a buffer exception is thrown
	 */

	public void checkAndEquipBadges() throws LoginFailedException, RemoteServerException {
		CheckAwardedBadgesMessage msg = CheckAwardedBadgesMessage.newBuilder().build();
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
	 */
	public int getCurrency(Currency currency) {
		return currencies.get(currency);
	}

	public enum Currency {
		STARDUST, POKECOIN;
	}

	/**
	 * Gets raw player data proto
	 *
	 * @return Player data
	 */
	public PlayerData getPlayerData() {
		return playerData;
	}

	/**
	 * Gets avatar
	 *
	 * @return Player Avatar object
	 */
	public PlayerAvatar getAvatar() {
		return avatar;
	}

	/**
	 * Gets daily bonus
	 *
	 * @return DailyBonus object
	 */
	public DailyBonus getDailyBonus() {
		return dailyBonus;
	}

	/**
	 * Gets contact settings
	 *
	 * @return ContactSettings object
	 */
	public ContactSettings getContactSettings() {
		return contactSettings;
	}

	/**
	 * Gets a map of all currencies
	 *
	 * @return map of currencies
	 */
	public Map<Currency, Integer> getCurrencies() {
		return currencies;
	}

	/**
	 * Gets player stats
	 *
	 * @return stats API objet
	 */
	public Stats getStats() {
		if (stats == null) {
			return new Stats(PlayerStatsOuterClass.PlayerStats.newBuilder().build());
		}
		return stats;
	}

	/**
	 * Gets tutorial states
	 *
	 * @return TutorialState object
	 */
	public TutorialState getTutorialState() {
		return tutorialState;
	}

	/**
	 * Set the account to legal screen in order to receive valid response
	 *
	 * @throws LoginFailedException  when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 */
	public void enableAccount() throws LoginFailedException, RemoteServerException {
		MarkTutorialCompleteMessage.Builder tutorialBuilder = MarkTutorialCompleteMessage.newBuilder();
		tutorialBuilder.addTutorialsCompleted(TutorialStateOuterClass.TutorialState.LEGAL_SCREEN)
				.setSendMarketingEmails(false)
				.setSendPushNotifications(false);

		ServerRequest serverRequest = new ServerRequest(RequestType.MARK_TUTORIAL_COMPLETE, tutorialBuilder.build());
		api.getRequestHandler().sendServerRequests(serverRequest);

		MarkTutorialCompleteResponse response;
		try {
			response = MarkTutorialCompleteResponse.parseFrom(serverRequest.getData());
			playerData = response.getPlayerData();
			tutorialState.addTutorialStates(playerData.getTutorialStateList());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}
}
