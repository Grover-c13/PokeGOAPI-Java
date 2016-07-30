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
import POGOProtos.Inventory.Item.ItemAwardOuterClass;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.EquipBadgeMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass;
import POGOProtos.Networking.Responses.EquipBadgeResponseOuterClass;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.main.Task;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.TaskUtil;
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
	}

	public void updateProfile(final Task<Void> task) {

		Task<ServerRequest> stask = new Task<ServerRequest>() {

			@Override
			public void onComplete(ServerRequest req) throws RemoteServerException, InvalidProtocolBufferException {

				GetPlayerResponseOuterClass.GetPlayerResponse playerResponse = null;
				playerResponse = GetPlayerResponseOuterClass.GetPlayerResponse.parseFrom(req.getData());


				badge = playerResponse.getPlayerData().getEquippedBadge();
				creationTime = playerResponse.getPlayerData().getCreationTimestampMs();
				itemStorage = playerResponse.getPlayerData().getMaxItemStorage();
				pokemonStorage = playerResponse.getPlayerData().getMaxPokemonStorage();
				team = Team.values()[playerResponse.getPlayerData().getTeamValue()];
				username = playerResponse.getPlayerData().getUsername();

				final PlayerAvatar avatarApi = new PlayerAvatar(playerResponse.getPlayerData().getAvatar());
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



				bonusApi.setNextCollectionTimestamp(
						playerResponse.getPlayerData().getDailyBonus().getNextCollectedTimestampMs()
				);
				bonusApi.setNextDefenderBonusCollectTimestamp(
						playerResponse.getPlayerData().getDailyBonus().getNextDefenderBonusCollectTimestampMs()
				);

				avatar = avatarApi;
				dailyBonus = bonusApi;
				task.onComplete(null);
				task.setDone(true);
				System.out.println("TASK COMPLETE");

			}
		};

		GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder().build();
		ServerRequest getPlayerServerRequest = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg, stask);

		api.getRequestHandler().request(getPlayerServerRequest);

	}



	/**
	 * Updates the player profile with the latest data.
	 *
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void updateProfile() throws RemoteServerException, LoginFailedException {
		Task task = new Task<Void>() {

			public void onComplete(Void v) throws RemoteServerException, InvalidProtocolBufferException {
				return;
			}
		};

		updateProfile(task);
		TaskUtil.waitForTask(task);

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
	public void acceptLevelUpRewards(final int level, final Task<PlayerLevelUpRewards> task) throws RemoteServerException, LoginFailedException {
		Task<ServerRequest> stask = new Task<ServerRequest>()
		{
			@Override
			public void onComplete(ServerRequest input) throws RemoteServerException, InvalidProtocolBufferException {
				PlayerLevelUpRewards out;
				// Check if we even have achieved this level yet
				if (level > stats.getLevel()) {
					out = new PlayerLevelUpRewards(PlayerLevelUpRewards.Status.NOT_UNLOCKED_YET);
				} else {
					LevelUpRewardsResponse response = LevelUpRewardsResponse.parseFrom(input.getData());

					// Add the awarded items to our bag
					ItemBag bag = api.getInventories().getItemBag();
					for (ItemAwardOuterClass.ItemAward itemAward : response.getItemsAwardedList()) {
						Item item = bag.getItem(itemAward.getItemId());
						item.setCount(item.getCount() + itemAward.getItemCount());
					}
					// Build a new rewards object and return it
					out = new PlayerLevelUpRewards(response);
				}

				task.onComplete(out);
				task.setDone(true);

			}
		};

		LevelUpRewardsMessage msg = LevelUpRewardsMessage.newBuilder()
				.setLevel(level)
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.LEVEL_UP_REWARDS, msg, stask);
		api.getRequestHandler().request(serverRequest);

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
	public PlayerLevelUpRewards acceptLevelUpRewards(final int level) throws RemoteServerException, LoginFailedException {
		final PlayerLevelUpRewards[] out = new PlayerLevelUpRewards[1];
		Task<PlayerLevelUpRewards> task = new Task<PlayerLevelUpRewards>()
		{
			@Override
			public void onComplete(PlayerLevelUpRewards input) throws RemoteServerException, InvalidProtocolBufferException {
				out[0] = input;
			}
		};

		acceptLevelUpRewards(level, task);
		TaskUtil.waitForTask(task);

		return out[0];
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
		CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage msg =
				CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage.newBuilder().build();
		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.CHECK_AWARDED_BADGES, msg);
		api.getRequestHandler().sendServerRequests(serverRequest);
		CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse response;
		try {
			response = CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		if (response.getSuccess()) {
			for (int i = 0; i < response.getAwardedBadgesCount(); i++) {
				EquipBadgeMessageOuterClass.EquipBadgeMessage msg1 = EquipBadgeMessageOuterClass.EquipBadgeMessage.newBuilder()
						.setBadgeType(response.getAwardedBadges(i))
						.setBadgeTypeValue(response.getAwardedBadgeLevels(i)).build();
				ServerRequest serverRequest1 = new ServerRequest(RequestTypeOuterClass.RequestType.EQUIP_BADGE, msg1);
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
