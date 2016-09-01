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
import POGOProtos.Data.Player.PlayerAvatarOuterClass;
import POGOProtos.Data.Player.PlayerStatsOuterClass;
import POGOProtos.Data.PlayerDataOuterClass.PlayerData;
import POGOProtos.Enums.GenderOuterClass.Gender;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Enums.TutorialStateOuterClass;
import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Networking.Requests.Messages.CheckChallenge;
import POGOProtos.Networking.Requests.Messages.ClaimCodenameMessageOuterClass.ClaimCodenameMessage;
import POGOProtos.Networking.Requests.Messages.EncounterTutorialCompleteMessageOuterClass.EncounterTutorialCompleteMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.Messages.MarkTutorialCompleteMessageOuterClass.MarkTutorialCompleteMessage;
import POGOProtos.Networking.Requests.Messages.SetAvatarMessageOuterClass.SetAvatarMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.ClaimCodenameResponseOuterClass.ClaimCodenameResponse;
import POGOProtos.Networking.Responses.EncounterTutorialCompleteResponseOuterClass.EncounterTutorialCompleteResponse;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;

import com.google.protobuf.GeneratedMessage;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Stats;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.main.CommonRequest;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.PokeAFunc;
import com.pokegoapi.util.PokeCallback;

import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import POGOProtos.Networking.Responses.MarkTutorialCompleteResponseOuterClass.MarkTutorialCompleteResponse;
import POGOProtos.Networking.Responses.SetAvatarResponseOuterClass.SetAvatarResponse;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

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
			initProfile();
		}
	}

	/**
	 * Init the player profile.
	 */
	private void initProfile() {
		GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder()
				.setPlayerLocale(playerLocale.getPlayerLocale())
				.build();

		AsyncServerRequest getPlayerServerRequest = new AsyncServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg,
				new PokeAFunc<GetPlayerResponse, Void>() {
					@Override
					public Void exec(GetPlayerResponse response) {
						ArrayList<TutorialStateOuterClass.TutorialState> tutorialStates =
								getTutorialState().getTutorialStates();
						if (tutorialStates.isEmpty()) {
							activateAccount();
						}

						if (!tutorialStates.contains(TutorialStateOuterClass.TutorialState.AVATAR_SELECTION)) {
							setupAvatar();
						}

						if (!tutorialStates.contains(TutorialStateOuterClass.TutorialState.POKEMON_CAPTURE)) {
							encounterTutorialComplete();
						}

						if (!tutorialStates.contains(TutorialStateOuterClass.TutorialState.NAME_SELECTION)) {
							claimCodeName();
						}

						if (!tutorialStates.contains(TutorialStateOuterClass.TutorialState.FIRST_TIME_EXPERIENCE_COMPLETE)) {
							firstTimeExperienceComplete();
						}
						return null;
					}
				}, null).addCommonRequest(new ServerRequest(RequestType.CHECK_CHALLENGE,
				CheckChallenge.CheckChallengeMessage.getDefaultInstance()));
		api.getRequestHandler().sendAsyncServerRequests(getPlayerServerRequest);
	}

	/**
	 * Update profile
	 *
	 * @param callback an optional callback to handle results
	 */
	private void updateProfile(PokeCallback<PlayerProfile> callback) {
		GetPlayerMessage getPlayerReqMsg = GetPlayerMessage.newBuilder()
				.setPlayerLocale(playerLocale.getPlayerLocale())
				.build();

		AsyncServerRequest getPlayerServerRequest = new AsyncServerRequest(RequestType.GET_PLAYER, getPlayerReqMsg,
				new PokeAFunc<GetPlayerResponse, PlayerProfile>() {
					@Override
					public PlayerProfile exec(GetPlayerResponse response) {
						parseData(response.getPlayerData());
						return PlayerProfile.this;
					}
				}, callback, api);
		api.getRequestHandler().sendAsyncServerRequests(getPlayerServerRequest);
	}

	/**
	 * Update the profile with the given player data
	 *
	 * @param playerData the data for update
	 */
	private void parseData(PlayerData playerData) {
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
	}

	/**
	 * Accept the rewards granted and the items unlocked by gaining a trainer level up. Rewards are retained by the
	 * server until a player actively accepts them.
	 * The rewarded items are automatically inserted into the players item bag.
	 *
	 * @param level the trainer level that you want to accept the rewards for
	 * @param callback an optional callback to handle results
	 * @see PlayerLevelUpRewards
	 */
	public void acceptLevelUpRewards(int level, PokeCallback<PlayerLevelUpRewards> callback) {
		// Check if we even have achieved this level yet
		if (level > stats.getLevel()) {
			callback.onResponse(new PlayerLevelUpRewards(PlayerLevelUpRewards.Status.NOT_UNLOCKED_YET));
			return;
		}

		LevelUpRewardsMessage msg = LevelUpRewardsMessage.newBuilder()
				.setLevel(level)
				.build();
		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.LEVEL_UP_REWARDS, msg,
				new PokeAFunc<LevelUpRewardsResponse, PlayerLevelUpRewards>() {
					@Override
					public PlayerLevelUpRewards exec(LevelUpRewardsResponse response) {
						// Add the awarded items to our bag
						ItemBag bag = api.getInventories().getItemBag();
						for (ItemAward itemAward : response.getItemsAwardedList()) {
							Item item = bag.getItem(itemAward.getItemId());
							item.setCount(item.getCount() + itemAward.getItemCount());
						}

						// Build a new rewards object and return it
						return new PlayerLevelUpRewards(response);
					}
				}, callback, api);
		api.getRequestHandler().sendAsyncServerRequests(serverRequest);
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
	 * @param callback an optional callback to handle results
	 */
	public void activateAccount(PokeCallback<PlayerProfile> callback) {
		markTutorial(TutorialStateOuterClass.TutorialState.LEGAL_SCREEN, callback);
	}

	/**
	 * Setup an avatar for the current account

	 * @param callback an optional callback to handle results
	 */
	public void setupAvatar(PokeCallback<SetAvatarResponse.Status> callback) {
		Random random = new Random();

		final PlayerAvatarOuterClass.PlayerAvatar.Builder playerAvatarBuilder =
				PlayerAvatarOuterClass.PlayerAvatar.newBuilder();
		final boolean female = random.nextInt(100) % 2 == 0;
		if (female) {
			playerAvatarBuilder.setGender(Gender.FEMALE);
		}

		playerAvatarBuilder.setSkin(random.nextInt(PlayerAvatar.getAvailableSkins()))
				.setHair(random.nextInt(PlayerAvatar.getAvailableHair()))
				.setEyes(random.nextInt(PlayerAvatar.getAvailableEyes()))
				.setHat(random.nextInt(PlayerAvatar.getAvailableHats()))
				.setShirt(random.nextInt(PlayerAvatar.getAvailableShirts(female ? Gender.FEMALE : Gender.MALE)))
				.setPants(random.nextInt(PlayerAvatar.getAvailablePants(female ? Gender.FEMALE : Gender.MALE)))
				.setShoes(random.nextInt(PlayerAvatar.getAvailableShoes()))
				.setBackpack(random.nextInt(PlayerAvatar.getAvailableShoes()));

		final SetAvatarMessage setAvatarMessage = SetAvatarMessage.newBuilder()
				.setPlayerAvatar(playerAvatarBuilder.build())
				.build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.SET_AVATAR, setAvatarMessage,
				new PokeAFunc<SetAvatarResponse, SetAvatarResponse.Status>() {
					@Override
					public SetAvatarResponse.Status exec(SetAvatarResponse response) {
						parseData(response.getPlayerData());

						if (response.getStatus() == SetAvatarResponse.Status.SUCCESS) {
							markTutorial(TutorialStateOuterClass.TutorialState.AVATAR_SELECTION, null);
						}

						api.fireRequestBlockTwo();

						return response.getStatus();
					}
				}, callback, api);
		api.getRequestHandler().sendAsyncServerRequests(serverRequest);
	}

	/**
	 * Encounter tutorial complete. In other words, catch the first Pokémon

	 * @param callback an optional callback to handle results
	 */
	public void encounterTutorialComplete(PokeCallback<EncounterTutorialCompleteResponse.Result> callback) {
		Random random = new Random();
		int pokemonId = random.nextInt(4);

		final EncounterTutorialCompleteMessage.Builder encounterTutorialCompleteBuilder =
				EncounterTutorialCompleteMessage.newBuilder()
						.setPokemonId(pokemonId == 1 ? PokemonId.BULBASAUR :
								pokemonId == 2 ? PokemonId.CHARMANDER : PokemonId.SQUIRTLE);

		AsyncServerRequest serverRequest = new AsyncServerRequest(
				RequestType.ENCOUNTER_TUTORIAL_COMPLETE, encounterTutorialCompleteBuilder.build(),
				new PokeAFunc<EncounterTutorialCompleteResponse, EncounterTutorialCompleteResponse.Result>() {
					@Override
					public EncounterTutorialCompleteResponse.Result exec(EncounterTutorialCompleteResponse response) {
						if (response.getResult() == EncounterTutorialCompleteResponse.Result.SUCCESS) {
							updateProfile(null);
						}

						return response.getResult();
					}
				}, callback)
				.addCommonRequest(new ServerRequest(RequestType.CHECK_CHALLENGE,
						CheckChallenge.CheckChallengeMessage.getDefaultInstance()));
		api.getRequestHandler().sendAsyncServerRequests(serverRequest);
	}

	/**
	 * Setup an user name for our account
	 *
	 * @param callback an optional callback to handle results
	 */
	public void claimCodeName(final PokeCallback<ClaimCodenameResponse.Status> callback) {
		ClaimCodenameMessage claimCodenameMessage = ClaimCodenameMessage.newBuilder()
				.setCodename(randomCodenameGenerator())
				.build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(
				RequestType.CLAIM_CODENAME, claimCodenameMessage,
				new PokeAFunc<ClaimCodenameResponse, ClaimCodenameResponse.Status>() {
					@Override
					public ClaimCodenameResponse.Status exec(ClaimCodenameResponse response) {
						String updatedCodename = null;
						if (response.getStatus() != ClaimCodenameResponse.Status.SUCCESS) {
							if (response.getUpdatedPlayer().getRemainingCodenameClaims() > 0) {
								claimCodeName(callback);
							}
						} else {
							updatedCodename = response.getCodename();
							parseData(response.getUpdatedPlayer());
						}

						if (updatedCodename != null) {
							markTutorial(TutorialStateOuterClass.TutorialState.NAME_SELECTION, null);
							updateProfile(null);
						}
						return response.getStatus();
					}
				}, callback, api);
		api.getRequestHandler().sendAsyncServerRequests(serverRequest);
	}

	/**
	 * The last step, mark the last tutorial state as completed
	 *
	 * @param callback an optional callback to handle results
	 */
	public void firstTimeExperienceComplete(PokeCallback<PlayerProfile> callback) {
		markTutorial(TutorialStateOuterClass.TutorialState.FIRST_TIME_EXPERIENCE_COMPLETE, callback);
	}

	/**
	 * Mark the tutorial state as complete
	 *
	 * @param state
	 * @param callback an optional callback to handle results
	 */
	private void markTutorial(TutorialStateOuterClass.TutorialState state, PokeCallback<PlayerProfile> callback) {
		final MarkTutorialCompleteMessage tutorialMessage = MarkTutorialCompleteMessage.newBuilder()
				.addTutorialsCompleted(state)
				.setSendMarketingEmails(false)
				.setSendPushNotifications(false).build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.MARK_TUTORIAL_COMPLETE, tutorialMessage,
				new PokeAFunc<MarkTutorialCompleteResponse, PlayerProfile>() {
					@Override
					public PlayerProfile exec(MarkTutorialCompleteResponse response) {
						parseData(response.getPlayerData());
						return PlayerProfile.this;
					}
				}, callback, api);
		api.getRequestHandler().sendAsyncServerRequests(serverRequest);
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