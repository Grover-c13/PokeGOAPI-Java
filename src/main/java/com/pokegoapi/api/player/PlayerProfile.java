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
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass;
import POGOProtos.Networking.Responses.EquipBadgeResponseOuterClass;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;
import lombok.Getter;
import lombok.Setter;
import rx.Observable;
import rx.functions.Func1;
import rx.observables.BlockingObservable;

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

	public PlayerProfile(PokemonGo api) {
		this.api = api;
	}

	private synchronized PlayerProfile getInstance(){ return this; }

	/**
	 * Refresh PlayerProfile data asynchronously.
	 *
	 * @return An Observable PlayerProfile object.
	 */
	public PlayerProfile refreshData() {

		final GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder().build();
		final ServerRequest getPlayerServerRequest = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg);

		ServerRequest[] res = api.getRequestHandler().sendAsyncServerRequests(getPlayerServerRequest).first();
		try {
			return updateInstanceData(res[0]);
		}
		catch(Exception ex) {
			return null;
		}
		/*
		return api.getRequestHandler().sendAsyncServerRequests(getPlayerServerRequest)
				.flatMap(new Func1<ServerRequest[], Observable<?>>() {
					@Override
					public Observable<?> call(ServerRequest[] requests) {
						if(requests == null || requests.length == 0){ return Observable.empty(); }
						try {
							return Observable.just(updateInstanceData(requests[0]));
						}
						catch (Exception e) {
							return Observable.error(e);
						}
					}
				}).cast(PlayerProfile.class);*/
	}

	/**
	 * Updates the player profile with the latest data synchronously.
	 *
	 * @return The latest data from the servers.
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public PlayerProfile refreshDataSync() throws LoginFailedException, RemoteServerException {
		GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder().build();
		final ServerRequest getPlayerServerRequest = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg);

		api.getRequestHandler().sendServerRequests(getPlayerServerRequest);
		updateInstanceData(getPlayerServerRequest);

		return this;
	}

	/**
	 * Updates this instance data with the request/response data.
	 *
	 * @param request  The server request.
	 * @throws RemoteServerException If the server response was empty or invalid.
	 */
	private synchronized PlayerProfile updateInstanceData(final ServerRequest request)
			throws LoginFailedException, RemoteServerException {

		GetPlayerResponseOuterClass.GetPlayerResponse playerResponse = null;
		try {
			playerResponse = GetPlayerResponseOuterClass.GetPlayerResponse.parseFrom(request.getData());
		} catch (InvalidProtocolBufferException e) {
			Log.e(TAG, "Could not parse GetPlayerResponse data");
			return refreshDataSync();
		}

		badge = playerResponse.getPlayerData().getEquippedBadge();
		creationTime = playerResponse.getPlayerData().getCreationTimestampMs();
		itemStorage = playerResponse.getPlayerData().getMaxItemStorage();
		pokemonStorage = playerResponse.getPlayerData().getMaxPokemonStorage();
		team = Team.values()[playerResponse.getPlayerData().getTeamValue()];
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
				playerResponse.getPlayerData().getDailyBonus().getNextCollectedTimestampMs());

		bonusApi.setNextDefenderBonusCollectTimestamp(
				playerResponse.getPlayerData().getDailyBonus().getNextDefenderBonusCollectTimestampMs());

		avatar = avatarApi;
		dailyBonus = bonusApi;

		return this;
	}

	/**
	 * Accept the rewards granted and the items unlocked by gaining a trainer level up. Rewards are retained by the
	 * server until a player actively accepts them.
	 * The rewarded items are automatically inserted into the players item bag.
	 *
	 * @param level the trainer level that you want to accept the rewards for
	 * @return a PlayerLevelUpRewards object containing information about the items rewarded and unlocked for this level
	 * @throws LoginFailedException  if the login failed
	 * @throws RemoteServerException if the server failed to respond
	 * @see PlayerLevelUpRewards
	 */
	public PlayerLevelUpRewards acceptLevelUpRewards(int level) throws RemoteServerException, LoginFailedException {
		// Check if we even have achieved this level yet
		if (level > stats.getLevel()) {
			return new PlayerLevelUpRewards(PlayerLevelUpRewards.Status.NOT_UNLOCKED_YET);
		}
		LevelUpRewardsMessage msg = LevelUpRewardsMessage.newBuilder().setLevel(level).build();
		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.LEVEL_UP_REWARDS, msg);
		api.getRequestHandler().sendServerRequests(serverRequest);
		LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse response;
		try {
			response = LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		// Add the awarded items to our bag
		ItemBag bag = api.getInventories().getItemBag();
		for (ItemAwardOuterClass.ItemAward itemAward : response.getItemsAwardedList()) {
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
		CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage msg =
				CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage.newBuilder().build();
		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.CHECK_AWARDED_BADGES, msg);
		api.getRequestHandler().sendServerRequests(serverRequest);
		CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse response;
		try {
			response =
					CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		if (response.getSuccess()) {
			for (int i = 0; i < response.getAwardedBadgesCount(); i++) {
				EquipBadgeMessageOuterClass.EquipBadgeMessage msg1 =
						EquipBadgeMessageOuterClass.EquipBadgeMessage.newBuilder()
								.setBadgeType(response.getAwardedBadges(i))
								.setBadgeTypeValue(response.getAwardedBadgeLevels(i))
								.build();
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
