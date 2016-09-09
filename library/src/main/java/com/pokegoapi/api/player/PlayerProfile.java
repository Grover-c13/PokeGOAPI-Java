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
import POGOProtos.Data.Player.PlayerStatsOuterClass;
import POGOProtos.Data.PlayerBadgeOuterClass.PlayerBadge;
import POGOProtos.Data.PlayerDataOuterClass.PlayerData;
import POGOProtos.Enums.BadgeTypeOuterClass.BadgeType;
import POGOProtos.Enums.TutorialStateOuterClass;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage;
import POGOProtos.Networking.Requests.Messages.ClaimCodenameMessageOuterClass.ClaimCodenameMessage;
import POGOProtos.Networking.Requests.Messages.EncounterTutorialCompleteMessageOuterClass.EncounterTutorialCompleteMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerProfileMessageOuterClass.GetPlayerProfileMessage;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.Messages.MarkTutorialCompleteMessageOuterClass.MarkTutorialCompleteMessage;
import POGOProtos.Networking.Requests.Messages.SetAvatarMessageOuterClass.SetAvatarMessage;
import POGOProtos.Networking.Requests.Messages.SetBuddyPokemonMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.ClaimCodenameResponseOuterClass.ClaimCodenameResponse;
import POGOProtos.Networking.Responses.GetPlayerProfileResponseOuterClass.GetPlayerProfileResponse;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import POGOProtos.Networking.Responses.MarkTutorialCompleteResponseOuterClass.MarkTutorialCompleteResponse;
import POGOProtos.Networking.Responses.SetAvatarResponseOuterClass.SetAvatarResponse;
import POGOProtos.Networking.Responses.SetBuddyPokemonResponseOuterClass.SetBuddyPokemonResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Stats;
import com.pokegoapi.api.listener.PlayerListener;
import com.pokegoapi.api.listener.TutorialListener;
import com.pokegoapi.api.pokemon.Buddy;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.api.pokemon.StarterPokemon;
import com.pokegoapi.exceptions.InsufficientLevelException;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;
import lombok.Getter;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayerProfile {
	private static final String TAG = PlayerProfile.class.getSimpleName();
	private final PokemonGo api;
	private final PlayerLocale playerLocale;
	private PlayerData playerData;
	@Getter
	private Map<BadgeType, Medal> medals = Collections.synchronizedMap(new HashMap<BadgeType, Medal>());
	private PlayerAvatar avatar;
	private DailyBonus dailyBonus;
	private ContactSettings contactSettings;
	private Map<Currency, Integer> currencies =
			Collections.synchronizedMap(new EnumMap<Currency, Integer>(Currency.class));

	@Getter
	private long startTime;

	@Getter
	public Buddy buddy;

	private Stats stats;
	private TutorialState tutorialState;

	@Getter
	private final Object lock = new Object();

	@Getter
	private int level = 1;

	@Getter
	public boolean banned;

	@Getter
	private boolean warned = false;

	/**
	 * @param api the api
	 */
	public PlayerProfile(PokemonGo api) {
		this.api = api;
		this.playerLocale = new PlayerLocale();
	}

	/**
	 * Updates the player profile with the latest data.
	 *
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void updateProfile() throws RequestFailedException {
		GetPlayerMessage message = GetPlayerMessage.newBuilder()
				.setPlayerLocale(playerLocale.getPlayerLocale())
				.build();

		ServerRequest request = new ServerRequest(RequestType.GET_PLAYER, message);
		api.requestHandler.sendServerRequests(request, false);

		try {
			updateProfile(GetPlayerResponse.parseFrom(request.getData()));
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * Update the profile with the given response
	 *
	 * @param playerResponse the response
	 */
	public void updateProfile(GetPlayerResponse playerResponse) {
		banned = playerResponse.getBanned();
		if (playerResponse.getWarn() && !warned) {
			warned = true;
			List<PlayerListener> listeners = api.getListeners(PlayerListener.class);
			for (PlayerListener listener : listeners) {
				listener.onWarningReceived(api);
			}
		}
		updateProfile(playerResponse.getPlayerData());
	}

	/**
	 * Update the profile with the given player data
	 *
	 * @param playerData the data for update
	 */
	public void updateProfile(PlayerData playerData) {
		this.playerData = playerData;

		avatar = new PlayerAvatar(playerData.getAvatar());
		dailyBonus = new DailyBonus(playerData.getDailyBonus());
		contactSettings = new ContactSettings(playerData.getContactSettings());

		// maybe something more graceful?
		for (CurrencyOuterClass.Currency currency : playerData.getCurrencyBalanceList()) {
			try {
				addCurrency(currency.getCurrencyType(), currency.getQuantity());
			} catch (InvalidCurrencyException e) {
				Log.w(TAG, "Error adding currency. You can probably ignore this.", e);
			}
		}

		// Tutorial state
		tutorialState = new TutorialState(playerData.getTutorialStateList());

		if (playerData.hasBuddyPokemon() && playerData.getBuddyPokemon().getId() != 0) {
			buddy = new Buddy(api, playerData.getBuddyPokemon());
		} else {
			buddy = null;
		}
	}

	/**
	 * Performs a GET_PLAYER_PROFILE request.
	 *
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void getProfile() throws RequestFailedException {
		GetPlayerProfileMessage profileMessage = GetPlayerProfileMessage.newBuilder().setPlayerName("").build();

		ServerRequest profileRequest = new ServerRequest(RequestType.GET_PLAYER_PROFILE, profileMessage);
		api.requestHandler.sendServerRequests(profileRequest, true);

		try {
			GetPlayerProfileResponse response = GetPlayerProfileResponse.parseFrom(profileRequest.getData());
			if (response.getResult() == GetPlayerProfileResponse.Result.SUCCESS) {
				medals.clear();
				List<PlayerBadge> badges = response.getBadgesList();
				for (PlayerBadge badge : badges) {
					medals.put(badge.getBadgeType(), new Medal(api, badge));
				}
				this.startTime = response.getStartTime();
			}
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * Accept the rewards granted and the items unlocked by gaining a trainer level up. Rewards are retained by the
	 * server until a player actively accepts them.
	 * The rewarded items are automatically inserted into the players item bag.
	 *
	 * @param level the trainer level that you want to accept the rewards for
	 * @return a PlayerLevelUpRewards object containing information about the items rewarded and unlocked for this level
	 * @throws RequestFailedException if an exception occurred while sending requests
	 * @throws InsufficientLevelException if you have not yet reached the desired level
	 * @see PlayerLevelUpRewards
	 */
	public PlayerLevelUpRewards acceptLevelUpRewards(int level)
			throws RequestFailedException {
		// Check if we even have achieved this level yet
		if (level > stats.getLevel()) {
			throw new InsufficientLevelException();
		}
		LevelUpRewardsMessage msg = LevelUpRewardsMessage.newBuilder()
				.setLevel(level)
				.build();
		ServerRequest serverRequest = new ServerRequest(RequestType.LEVEL_UP_REWARDS, msg);
		api.requestHandler.sendServerRequests(serverRequest, true);
		LevelUpRewardsResponse response;
		try {
			response = LevelUpRewardsResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
		// Add the awarded items to our bag
		ItemBag bag = api.inventories.itemBag;
		bag.addAwardedItems(response);
		// Build a new rewards object and return it
		return new PlayerLevelUpRewards(response);
	}

	/**
	 * Add currency.
	 *
	 * @param name the name
	 * @param amount the amount
	 * @throws InvalidCurrencyException the invalid currency exception
	 */
	public void addCurrency(String name, int amount) throws InvalidCurrencyException {
		try {
			synchronized (this.lock) {
				currencies.put(Currency.valueOf(name), amount);
			}
		} catch (Exception e) {
			throw new InvalidCurrencyException();
		}
	}

	/**
	 * Check and equip badges.
	 *
	 * @throws RequestFailedException if an exception occurred while sending requests
	 * @deprecated use getMedals, which uses common requests to check for badges
	 */
	@Deprecated
	public void checkAndEquipBadges() throws RequestFailedException {
		CheckAwardedBadgesMessage msg = CheckAwardedBadgesMessage.newBuilder().build();
		ServerRequest serverRequest = new ServerRequest(RequestType.CHECK_AWARDED_BADGES, msg);
		api.requestHandler.sendServerRequests(serverRequest, false);
		CheckAwardedBadgesResponse response;
		try {
			response = CheckAwardedBadgesResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
		this.updateAwardedMedals(response);
	}

	/**
	 * Gets currency.
	 *
	 * @param currency the currency
	 * @return the currency
	 */
	public int getCurrency(Currency currency) {
		synchronized (this.lock) {
			if (!currencies.containsKey(currency)) {
				return 0;
			}
			return currencies.get(currency);
		}
	}

	/**
	 * Equips the badges contained in the given response
	 *
	 * @param response the response to get badges from
	 */
	public void updateAwardedMedals(CheckAwardedBadgesResponse response) {
		if (response.getSuccess()) {
			List<PlayerListener> listeners = api.getListeners(PlayerListener.class);
			for (int i = 0; i < response.getAwardedBadgesCount(); i++) {
				BadgeType type = response.getAwardedBadges(i);
				int level = response.getAwardedBadgeLevels(i);
				Medal medal = medals.get(type);
				if (medal != null) {
					medal.rank = level;
					for (PlayerListener listener : listeners) {
						listener.onMedalAwarded(api, this, medal);
					}
				}
			}
		}
	}

	public enum Currency {
		STARDUST, POKECOIN
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
	 * @return stats API object
	 */
	public Stats getStats() {
		if (stats == null) {
			return new Stats(PlayerStatsOuterClass.PlayerStats.newBuilder().build());
		}
		return stats;
	}

	/**
	 * Sets the player statistics
	 *
	 * @param stats the statistics to apply
	 * @throws RequestFailedException if a request fails while sending a request
	 */
	public void setStats(Stats stats) throws RequestFailedException {
		int oldLevel = level;
		level = stats.getLevel();
		if (this.stats != null) {
			if (level > oldLevel) {
				List<PlayerListener> listeners = api.getListeners(PlayerListener.class);
				for (PlayerListener listener : listeners) {
					listener.onLevelUp(api, oldLevel, level);
				}
				acceptLevelUpRewards(level);
			}
		}
		this.stats = stats;
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
	 * @return whether this player has a buddy active
	 */
	public boolean hasBuddy() {
		return buddy != null;
	}

	/**
	 * Sets the current buddy
	 *
	 * @param pokemon the pokemon to set as your buddy
	 * @return if this task was successfull
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public boolean setBuddy(Pokemon pokemon) throws
			RequestFailedException {
		SetBuddyPokemonMessageOuterClass.SetBuddyPokemonMessage message = SetBuddyPokemonMessageOuterClass
				.SetBuddyPokemonMessage.newBuilder()
				.setPokemonId(pokemon.getId())
				.build();
		ServerRequest request = new ServerRequest(RequestType.SET_BUDDY_POKEMON, message);
		api.requestHandler.sendServerRequests(request, true);
		try {
			SetBuddyPokemonResponse response = SetBuddyPokemonResponse.parseFrom(request.getData());
			buddy = new Buddy(api, response.getUpdatedBuddy());
			return response.hasUpdatedBuddy();
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * Set the account to legal screen in order to receive valid response
	 *
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void activateAccount() throws RequestFailedException {
		markTutorial(TutorialStateOuterClass.TutorialState.LEGAL_SCREEN);
	}

	/**
	 * Setup an avatar for the current account
	 *
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void setupAvatar() throws RequestFailedException {
		SecureRandom random = new SecureRandom();

		PlayerGender gender = random.nextInt(100) % 2 == 0 ? PlayerGender.FEMALE : PlayerGender.MALE;
		PlayerAvatar avatar = new PlayerAvatar(gender,
				random.nextInt(PlayerAvatar.getAvailableSkins()),
				random.nextInt(PlayerAvatar.getAvailableHair()),
				random.nextInt(PlayerAvatar.getAvailableShirts(gender)),
				random.nextInt(PlayerAvatar.getAvailablePants(gender)),
				random.nextInt(PlayerAvatar.getAvailableHats()),
				random.nextInt(PlayerAvatar.getAvailableShoes()),
				random.nextInt(PlayerAvatar.getAvailableEyes()),
				random.nextInt(PlayerAvatar.getAvailableBags(gender)));

		List<TutorialListener> listeners = api.getListeners(TutorialListener.class);
		for (TutorialListener listener : listeners) {
			PlayerAvatar listenerAvatar = listener.selectAvatar(api);
			if (listenerAvatar != null) {
				avatar = listenerAvatar;
				break;
			}
		}

		final SetAvatarMessage setAvatarMessage = SetAvatarMessage.newBuilder()
				.setPlayerAvatar(avatar.avatar)
				.build();

		ServerRequest request = new ServerRequest(RequestType.SET_AVATAR, setAvatarMessage);

		api.requestHandler.sendServerRequests(request, true);

		try {
			SetAvatarResponse setAvatarResponse = SetAvatarResponse.parseFrom(request.getData());
			playerData = setAvatarResponse.getPlayerData();

			updateProfile(playerData);
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}

		markTutorial(TutorialStateOuterClass.TutorialState.AVATAR_SELECTION);

		api.getAssetDigest();
	}

	/**
	 * Encounter tutorial complete. In other words, catch the first Pokémon
	 *
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void encounterTutorialComplete() throws
			RequestFailedException {
		StarterPokemon starter = StarterPokemon.random();

		List<TutorialListener> listeners = api.getListeners(TutorialListener.class);
		for (TutorialListener listener : listeners) {
			StarterPokemon pokemon = listener.selectStarter(api);
			if (pokemon != null) {
				starter = pokemon;
				break;
			}
		}

		final EncounterTutorialCompleteMessage.Builder builder =
				EncounterTutorialCompleteMessage.newBuilder()
						.setPokemonId(starter.pokemon);

		ServerRequest request = new ServerRequest(RequestType.ENCOUNTER_TUTORIAL_COMPLETE, builder.build());

		api.requestHandler.sendServerRequests(request, true);

		final GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder()
				.setPlayerLocale(playerLocale.getPlayerLocale())
				.build();
		request = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg);

		api.requestHandler.sendServerRequests(request, true);

		try {
			updateProfile(GetPlayerResponse.parseFrom(request.getData()));
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * Setup an user name for our account
	 *
	 * @return the claimed codename
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public String claimCodeName() throws RequestFailedException {
		return claimCodeName(null);
	}

	/**
	 * Setup an user name for our account
	 *
	 * @param lastFailure the last name used that was already taken; null for first try.
	 * @return the claimed codename
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public String claimCodeName(String lastFailure)
			throws RequestFailedException {
		if (getPlayerData().getRemainingCodenameClaims() <= 0) {
			throw new RuntimeException("You have no remaining codename claims!");
		}

		String name = randomCodenameGenerator();

		List<TutorialListener> listeners = api.getListeners(TutorialListener.class);
		for (TutorialListener listener : listeners) {
			String listenerName = listener.claimName(api, lastFailure);
			if (listenerName != null) {
				name = listenerName;
				break;
			}
		}

		ClaimCodenameMessage claimCodenameMessage = ClaimCodenameMessage.newBuilder()
				.setCodename(name)
				.build();

		ServerRequest request = new ServerRequest(RequestType.CLAIM_CODENAME, claimCodenameMessage);

		api.requestHandler.sendServerRequests(request, true);

		String updatedCodename;
		try {
			ClaimCodenameResponse claimCodenameResponse = ClaimCodenameResponse.parseFrom(request.getData());
			if (claimCodenameResponse.getStatus() != ClaimCodenameResponse.Status.SUCCESS) {
				return claimCodeName(name);
			}
			updatedCodename = claimCodenameResponse.getCodename();

			if (claimCodenameResponse.hasUpdatedPlayer()) {
				updateProfile(claimCodenameResponse.getUpdatedPlayer());
			}

			if (updatedCodename != null) {
				markTutorial(TutorialStateOuterClass.TutorialState.NAME_SELECTION);

				final GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder()
						.setPlayerLocale(playerLocale.getPlayerLocale())
						.build();
				request = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg);

				api.requestHandler.sendServerRequests(request, true);

				updateProfile(GetPlayerResponse.parseFrom(request.getData()));
			}
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
		return updatedCodename;
	}

	/**
	 * The last step, mark the last tutorial state as completed
	 *
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void firstTimeExperienceComplete() throws RequestFailedException {
		markTutorial(TutorialStateOuterClass.TutorialState.FIRST_TIME_EXPERIENCE_COMPLETE);
	}

	/**
	 * Completes the visit gym tutorial
	 * @throws RequestFailedException if an exception occurred while sending this request
	 */
	public void visitGymComplete() throws RequestFailedException {
		markTutorial(TutorialStateOuterClass.TutorialState.GYM_TUTORIAL);
	}

	/**
	 * Completes the visit pokestop tutorial
	 * @throws RequestFailedException if an exception occurred while sending this request
	 */
	public void visitPokestopComplete() throws RequestFailedException {
		markTutorial(TutorialStateOuterClass.TutorialState.POKESTOP_TUTORIAL);
	}

	private void markTutorial(TutorialStateOuterClass.TutorialState state)
			throws RequestFailedException {
		final MarkTutorialCompleteMessage tutorialMessage = MarkTutorialCompleteMessage.newBuilder()
				.addTutorialsCompleted(state)
				.setSendMarketingEmails(false)
				.setSendPushNotifications(false).build();

		ServerRequest request = new ServerRequest(RequestType.MARK_TUTORIAL_COMPLETE, tutorialMessage);

		api.requestHandler.sendServerRequests(request, true);

		try {
			playerData = MarkTutorialCompleteResponse.parseFrom(request.getData()).getPlayerData();

			updateProfile(playerData);
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	private static String randomCodenameGenerator() {
		final String a = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		final SecureRandom r = new SecureRandom();
		final int l = new Random().nextInt(15 - 10) + 10;
		StringBuilder sb = new StringBuilder(l);
		for (int i = 0; i < l; i++) {
			sb.append(a.charAt(r.nextInt(a.length())));
		}
		return sb.toString();
	}
}
