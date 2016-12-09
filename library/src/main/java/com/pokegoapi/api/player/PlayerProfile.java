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
import POGOProtos.Enums.GenderOuterClass.Gender;
import POGOProtos.Enums.TutorialStateOuterClass;
import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage;
import POGOProtos.Networking.Requests.Messages.ClaimCodenameMessageOuterClass.ClaimCodenameMessage;
import POGOProtos.Networking.Requests.Messages.EncounterTutorialCompleteMessageOuterClass.EncounterTutorialCompleteMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerProfileMessageOuterClass.GetPlayerProfileMessage;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.Messages.MarkTutorialCompleteMessageOuterClass.MarkTutorialCompleteMessage;
import POGOProtos.Networking.Requests.Messages.SetAvatarMessageOuterClass.SetAvatarMessage;
import POGOProtos.Networking.Requests.Messages.SetBuddyPokemon;
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
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Stats;
import com.pokegoapi.api.listener.PlayerListener;
import com.pokegoapi.api.listener.TutorialListener;
import com.pokegoapi.api.pokemon.Buddy;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.api.pokemon.StarterPokemon;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
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
	private Buddy buddy;

	private Stats stats;
	private TutorialState tutorialState;

	@Getter
	private final Object lock = new Object();

	@Getter
	private int level = 1;

	/**
	 * @param api the api
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public PlayerProfile(PokemonGo api) throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		this.api = api;
		this.playerLocale = new PlayerLocale();
	}

	/**
	 * Updates the player profile with the latest data.
	 *
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void updateProfile() throws RemoteServerException, CaptchaActiveException, LoginFailedException {
		GetPlayerMessage message = GetPlayerMessage.newBuilder()
				.setPlayerLocale(playerLocale.getPlayerLocale())
				.build();

		ServerRequest request = new ServerRequest(RequestType.GET_PLAYER, message);
		api.getRequestHandler().sendServerRequests(request);

		try {
			updateProfile(GetPlayerResponse.parseFrom(request.getData()));

			GetPlayerProfileMessage profileMessage = GetPlayerProfileMessage.newBuilder()
					.setPlayerName(playerData.getUsername())
					.build();

			ServerRequest profileRequest = new ServerRequest(RequestType.GET_PLAYER_PROFILE, profileMessage);
			api.getRequestHandler().sendServerRequests(profileRequest.withCommons());

			GetPlayerProfileResponse response = GetPlayerProfileResponse.parseFrom(profileRequest.getData());
			if (response.getResult() == GetPlayerProfileResponse.Result.SUCCESS) {
				medals.clear();
				List<PlayerBadge> badges = response.getBadgesList();
				for (PlayerBadge badge : badges) {
					medals.put(badge.getBadgeType(), new Medal(badge));
				}
				this.startTime = response.getStartTime();
			}
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	/**
	 * Update the profile with the given response
	 *
	 * @param playerResponse the response
	 */
	public void updateProfile(GetPlayerResponse playerResponse) {
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
		for (CurrencyOuterClass.Currency currency : playerData.getCurrenciesList()) {
			try {
				addCurrency(currency.getName(), currency.getAmount());
			} catch (InvalidCurrencyException e) {
				Log.w(TAG, "Error adding currency. You can probably ignore this.", e);
			}
		}

		// Tutorial state
		tutorialState = new TutorialState(playerData.getTutorialStateList());

		if (playerData.hasBuddyPokemon()) {
			buddy = new Buddy(api, playerData.getBuddyPokemon());
		} else {
			buddy = null;
		}
	}

	/**
	 * Accept the rewards granted and the items unlocked by gaining a trainer level up. Rewards are retained by the
	 * server until a player actively accepts them.
	 * The rewarded items are automatically inserted into the players item bag.
	 *
	 * @param level the trainer level that you want to accept the rewards for
	 * @return a PlayerLevelUpRewards object containing information about the items rewarded and unlocked for this level
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 * @see PlayerLevelUpRewards
	 */
	public PlayerLevelUpRewards acceptLevelUpRewards(int level)
			throws RemoteServerException, CaptchaActiveException, LoginFailedException {
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
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException When a buffer exception is thrown
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 * @deprecated use getMedals, which uses common requests to check for badges
	 */
	@Deprecated
	public void checkAndEquipBadges() throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		CheckAwardedBadgesMessage msg = CheckAwardedBadgesMessage.newBuilder().build();
		ServerRequest serverRequest = new ServerRequest(RequestType.CHECK_AWARDED_BADGES, msg);
		api.getRequestHandler().sendServerRequests(serverRequest);
		CheckAwardedBadgesResponse response;
		try {
			response = CheckAwardedBadgesResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
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
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 * @throws LoginFailedException if login fails
	 * @throws RemoteServerException if the server has an issue
	 */
	public void updateAwardedMedals(CheckAwardedBadgesResponse response)
			throws CaptchaActiveException, LoginFailedException, RemoteServerException {
		if (response.getSuccess()) {
			List<PlayerListener> listeners = api.getListeners(PlayerListener.class);
			for (int i = 0; i < response.getAwardedBadgesCount(); i++) {
				BadgeType type = response.getAwardedBadges(i);
				int level = response.getAwardedBadgeLevels(i);
				Medal medal = medals.get(type);
				if (medal != null) {
					medal.setRank(level);
					for (PlayerListener listener : listeners) {
						listener.onMedalAwarded(api, this, medal);
					}
				}
			}
		}
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
	 * @param stats the statistics to apply
	 */
	public void setStats(Stats stats) {
		int newLevel = stats.getLevel();
		if (this.stats != null) {
			if (newLevel > this.level) {
				boolean acceptRewards = false;
				List<PlayerListener> listeners = api.getListeners(PlayerListener.class);
				for (PlayerListener listener : listeners) {
					acceptRewards |= listener.onLevelUp(api, level, newLevel);
				}
				if (acceptRewards) {
					try {
						this.acceptLevelUpRewards(newLevel);
					} catch (Exception e) {
						//Ignore
					}
				}
			}
		}
		this.stats = stats;
		this.level = newLevel;
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
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public boolean setBuddy(Pokemon pokemon) throws CaptchaActiveException, LoginFailedException, RemoteServerException {
		SetBuddyPokemon.SetBuddyPokemonMessage message = SetBuddyPokemon.SetBuddyPokemonMessage.newBuilder()
				.setPokemonId(pokemon.getId())
				.build();
		ServerRequest request = new ServerRequest(RequestType.SET_BUDDY_POKEMON, message);
		api.getRequestHandler().sendServerRequests(request);
		try {
			SetBuddyPokemonResponse response = SetBuddyPokemonResponse.parseFrom(request.getData());
			buddy = new Buddy(api, response.getUpdatedBuddy());
			return response.hasUpdatedBuddy();
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	/**
	 * Set the account to legal screen in order to receive valid response
	 *
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void activateAccount() throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		markTutorial(TutorialStateOuterClass.TutorialState.LEGAL_SCREEN);
	}

	/**
	 * Setup an avatar for the current account
	 *
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void setupAvatar() throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		SecureRandom random = new SecureRandom();

		Gender gender = random.nextInt(100) % 2 == 0 ? Gender.FEMALE : Gender.MALE;
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
				.setPlayerAvatar(avatar.getAvatar())
				.build();

		ServerRequest request = new ServerRequest(RequestType.SET_AVATAR, setAvatarMessage);

		api.getRequestHandler().sendServerRequests(request.withCommons());

		try {
			SetAvatarResponse setAvatarResponse = SetAvatarResponse.parseFrom(request.getData());
			playerData = setAvatarResponse.getPlayerData();

			updateProfile(playerData);
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		markTutorial(TutorialStateOuterClass.TutorialState.AVATAR_SELECTION);

		api.fireRequestBlockTwo();
	}

	/**
	 * Encounter tutorial complete. In other words, catch the first Pokémon
	 *
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void encounterTutorialComplete() throws LoginFailedException, CaptchaActiveException, RemoteServerException {
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
						.setPokemonId(starter.getPokemon());

		ServerRequest request = new ServerRequest(RequestType.ENCOUNTER_TUTORIAL_COMPLETE, builder.build());

		api.getRequestHandler().sendServerRequests(request.withCommons());

		final GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder()
				.setPlayerLocale(playerLocale.getPlayerLocale())
				.build();
		request = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg);

		api.getRequestHandler().sendServerRequests(request.withCommons());

		try {
			updateProfile(GetPlayerResponse.parseFrom(request.getData()));
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	/**
	 * Setup an user name for our account
	 *
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void claimCodeName() throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		claimCodeName(null);
	}

	/**
	 * Setup an user name for our account
	 *
	 * @param lastFailure the last name used that was already taken; null for first try.
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void claimCodeName(String lastFailure)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
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

		api.getRequestHandler().sendServerRequests(request.withCommons());

		String updatedCodename = null;
		try {
			ClaimCodenameResponse claimCodenameResponse = ClaimCodenameResponse.parseFrom(request.getData());
			if (claimCodenameResponse.getStatus() != ClaimCodenameResponse.Status.SUCCESS) {
				if (claimCodenameResponse.getUpdatedPlayer().getRemainingCodenameClaims() > 0) {
					claimCodeName(name);
				}
			} else {
				updatedCodename = claimCodenameResponse.getCodename();
				updateProfile(claimCodenameResponse.getUpdatedPlayer());
			}
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		if (updatedCodename != null) {
			markTutorial(TutorialStateOuterClass.TutorialState.NAME_SELECTION);

			final GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder()
					.setPlayerLocale(playerLocale.getPlayerLocale())
					.build();
			request = new ServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg);

			api.getRequestHandler().sendServerRequests(request.withCommons());

			try {
				updateProfile(GetPlayerResponse.parseFrom(request.getData()));
			} catch (InvalidProtocolBufferException e) {
				throw new RemoteServerException(e);
			}
		}
	}

	/**
	 * The last step, mark the last tutorial state as completed
	 *
	 * @throws LoginFailedException when the auth is invalid
	 * @throws RemoteServerException when the server is down/having issues
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public void firstTimeExperienceComplete()
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		markTutorial(TutorialStateOuterClass.TutorialState.FIRST_TIME_EXPERIENCE_COMPLETE);
	}

	private void markTutorial(TutorialStateOuterClass.TutorialState state)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		final MarkTutorialCompleteMessage tutorialMessage = MarkTutorialCompleteMessage.newBuilder()
				.addTutorialsCompleted(state)
				.setSendMarketingEmails(false)
				.setSendPushNotifications(false).build();

		ServerRequest request = new ServerRequest(RequestType.MARK_TUTORIAL_COMPLETE, tutorialMessage);

		api.getRequestHandler().sendServerRequests(request);

		try {
			playerData = MarkTutorialCompleteResponse.parseFrom(request.getData()).getPlayerData();

			updateProfile(playerData);
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
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