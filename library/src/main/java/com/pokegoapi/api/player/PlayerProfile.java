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
import POGOProtos.Data.Player.PlayerStatsOuterClass;
import POGOProtos.Data.PlayerDataOuterClass.PlayerData;
import POGOProtos.Enums.GenderOuterClass.Gender;
import POGOProtos.Enums.TutorialStateOuterClass;
import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage;
import POGOProtos.Networking.Requests.Messages.ClaimCodenameMessageOuterClass.ClaimCodenameMessage;
import POGOProtos.Networking.Requests.Messages.EncounterTutorialCompleteMessageOuterClass.EncounterTutorialCompleteMessage;
import POGOProtos.Networking.Requests.Messages.EquipBadgeMessageOuterClass.EquipBadgeMessage;
import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass.GetPlayerMessage;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.Messages.MarkTutorialCompleteMessageOuterClass.MarkTutorialCompleteMessage;
import POGOProtos.Networking.Requests.Messages.SetAvatarMessageOuterClass.SetAvatarMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.ClaimCodenameResponseOuterClass.ClaimCodenameResponse;
import POGOProtos.Networking.Responses.EquipBadgeResponseOuterClass.EquipBadgeResponse;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import POGOProtos.Networking.Responses.SetAvatarResponseOuterClass.SetAvatarResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Stats;
import com.pokegoapi.api.listener.TutorialListener;
import com.pokegoapi.api.pokemon.StarterPokemon;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.CommonRequests;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonCallback;
import com.pokegoapi.main.PokemonRequest;
import com.pokegoapi.main.PokemonResponse;
import com.pokegoapi.main.RequestCallback;
import com.pokegoapi.main.Utils;
import com.pokegoapi.util.Log;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.EnumMap;
import java.util.List;
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
	 */
	public PlayerProfile(PokemonGo api, PokemonCallback updateCallback) {
		this.api = api;
		this.playerLocale = new PlayerLocale();

		if (playerData == null) {
			updateProfile(updateCallback);
		}
	}

	/**
	 * Updates the player profile with the latest data.
	 *
	 * @param callback callback for when this task completes
	 */
	public void updateProfile(final PokemonCallback callback) {
		GetPlayerMessage message = GetPlayerMessage.newBuilder()
				.setPlayerLocale(playerLocale.getPlayerLocale())
				.build();

		PokemonRequest request = new PokemonRequest(RequestType.GET_PLAYER, message)
				.withCallback(new RequestCallback() {
					@Override
					public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
						callback.onCompleted(response.getException());
						if (!response.hasErrored()) {
							updateProfile(GetPlayerResponse.parseFrom(response.getResponseData()));
						}
					}
				});
		api.getRequestHandler().sendRequests(
				CommonRequests.appendCheckChallenge(api, request));
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
	}

	/**
	 * Accept the rewards granted and the items unlocked by gaining a trainer level up. Rewards are retained by the
	 * server until a player actively accepts them.
	 * The rewarded items are automatically inserted into the players item bag.
	 *
	 * @param level the trainer level that you want to accept the rewards for
	 * @param rewards callback for returning the received items
	 * @see PlayerLevelUpRewards
	 */
	public void acceptLevelUpRewards(int level, final AsyncReturn<PlayerLevelUpRewards> rewards) {
		if (level > stats.getLevel()) {
			PlayerLevelUpRewards notUnlocked = new PlayerLevelUpRewards(PlayerLevelUpRewards.Status.NOT_UNLOCKED_YET);
			rewards.onReceive(notUnlocked, new IllegalArgumentException("Rewards not unlocked yet!"));
		}
		LevelUpRewardsMessage message = LevelUpRewardsMessage.newBuilder().setLevel(level).build();
		final PokemonRequest request = new PokemonRequest(RequestType.LEVEL_UP_REWARDS, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				PlayerLevelUpRewards error = new PlayerLevelUpRewards(PlayerLevelUpRewards.Status.ALREADY_ACCEPTED);
				if (Utils.callbackException(response, rewards, error)) {
					return;
				}
				LevelUpRewardsResponse messageResponse;
				try {
					messageResponse = LevelUpRewardsResponse.parseFrom(response.getResponseData());
				} catch (InvalidProtocolBufferException e) {
					rewards.onReceive(error, new RemoteServerException(e));
					return;
				}
				ItemBag bag = api.getInventories().getItemBag();
				for (ItemAward itemAward : messageResponse.getItemsAwardedList()) {
					Item item = bag.getItem(itemAward.getItemId());
					item.setCount(item.getCount() + itemAward.getItemCount());
				}
				rewards.onReceive(new PlayerLevelUpRewards(messageResponse), null);
			}
		});
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
			currencies.put(Currency.valueOf(name), amount);
		} catch (Exception e) {
			throw new InvalidCurrencyException();
		}
	}

	/**
	 * Checks for, and equips awarded badges if available.
	 *
	 * @param callback for when this task completes
	 */
	public void checkAndEquipBadges(final PokemonCallback callback) {
		CheckAwardedBadgesMessage message = CheckAwardedBadgesMessage.newBuilder().build();
		PokemonRequest request = new PokemonRequest(RequestType.CHECK_AWARDED_BADGES, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, callback)) {
					return;
				}
				CheckAwardedBadgesResponse messageResponse;
				try {
					messageResponse = CheckAwardedBadgesResponse.parseFrom(response.getResponseData());
				} catch (InvalidProtocolBufferException e) {
					callback.onCompleted(new RemoteServerException(e));
					return;
				}
				if (messageResponse.getSuccess()) {
					for (int i = 0; i < messageResponse.getAwardedBadgesCount(); i++) {
						EquipBadgeMessage badgeMessage = EquipBadgeMessage.newBuilder()
								.setBadgeType(messageResponse.getAwardedBadges(i))
								.setBadgeTypeValue(messageResponse.getAwardedBadgeLevels(i)).build();
						PokemonRequest badgeRequest = new PokemonRequest(RequestType.EQUIP_BADGE, badgeMessage);
						api.getRequestHandler().sendRequest(badgeRequest, new RequestCallback() {
							@Override
							public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
								if (Utils.callbackException(response, callback)) {
									return;
								}
								EquipBadgeResponse badgeResponse;
								try {
									badgeResponse = EquipBadgeResponse.parseFrom(response.getResponseData());
									PlayerProfile.this.badge = badgeResponse.getEquipped();
								} catch (InvalidProtocolBufferException e) {
									callback.onCompleted(new RemoteServerException(e));
								}
							}
						});
					}
				}
			}
		});
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
	 * @param callback callback for when this action has completed
	 */
	public void activateAccount(PokemonCallback callback) {
		markTutorial(TutorialStateOuterClass.TutorialState.LEGAL_SCREEN, callback);
	}

	/**
	 * Setup an avatar for the current account
	 *
	 * @param callback callback for when this action has completed
	 */
	public void setupAvatar(final PokemonCallback callback) {
		SecureRandom random = new SecureRandom();

		Gender gender = random.nextInt(100) % 2 == 0 ? Gender.FEMALE : Gender.MALE;
		PlayerAvatar avatar = PlayerAvatar.random(gender);

		List<TutorialListener> listeners = api.getListeners(TutorialListener.class);
		for (TutorialListener listener : listeners) {
			PlayerAvatar listenerAvatar = listener.selectAvatar(api);
			if (listenerAvatar != null) {
				avatar = listenerAvatar;
				break;
			}
		}

		final SetAvatarMessage message = SetAvatarMessage.newBuilder()
				.setPlayerAvatar(avatar.getAvatar())
				.build();

		PokemonRequest request = new PokemonRequest(RequestType.SET_AVATAR, message)
				.withCallback(new RequestCallback() {
					@Override
					public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
						if (Utils.callbackException(response, callback)) {
							return;
						}
						try {
							SetAvatarResponse messageResponse = SetAvatarResponse.parseFrom(response.getResponseData());
							playerData = messageResponse.getPlayerData();
							updateProfile(playerData);
							markTutorial(TutorialStateOuterClass.TutorialState.AVATAR_SELECTION, new PokemonCallback() {
								@Override
								public void onCompleted(Exception exception) {
									if (exception == null) {
										api.fireAssetRequestBlock(callback);
									} else {
										callback.onCompleted(exception);
									}
								}
							});
						} catch (InvalidProtocolBufferException e) {
							callback.onCompleted(new RemoteServerException(e));
						}
					}
				});
		api.getRequestHandler().sendRequests(CommonRequests.fillRequest(request, api));
	}

	/**
	 * Encounter tutorial complete. In other words, catch the first Pokémon
	 *
	 * @param callback for when this action has completed
	 */
	public void encounterTutorialComplete(final PokemonCallback callback) {
		StarterPokemon starter = StarterPokemon.random();

		List<TutorialListener> listeners = api.getListeners(TutorialListener.class);
		for (TutorialListener listener : listeners) {
			StarterPokemon pokemon = listener.selectStarter(api);
			if (pokemon != null) {
				starter = pokemon;
				break;
			}
		}

		final EncounterTutorialCompleteMessage.Builder messageBuilder = EncounterTutorialCompleteMessage.newBuilder()
				.setPokemonId(starter.getPokemon());

		PokemonRequest request = new PokemonRequest(RequestType.ENCOUNTER_TUTORIAL_COMPLETE, messageBuilder.build())
				.withCallback(new RequestCallback() {
					@Override
					public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
						if (Utils.callbackException(response, callback)) {
							return;
						}
						final GetPlayerMessage message = GetPlayerMessage.newBuilder()
								.setPlayerLocale(playerLocale.getPlayerLocale())
								.build();
						PokemonRequest getPlayerRequest = new PokemonRequest(RequestType.GET_PLAYER, message)
								.withCallback(new RequestCallback() {
									@Override
									public void handleResponse(PokemonResponse response)
											throws InvalidProtocolBufferException {
										callback.onCompleted(response.getException());
									}
								});
						api.getRequestHandler().sendRequests(CommonRequests.fillRequest(getPlayerRequest, api));
					}
				});

		api.getRequestHandler().sendRequests(CommonRequests.fillRequest(request, api));
	}

	/**
	 * Setup an user name for our account
	 *
	 * @param callback for when this action has completed
	 */
	public void claimCodeName(PokemonCallback callback) {
		claimCodeName(null, callback);
	}

	/**
	 * Setup an user name for our account
	 *
	 * @param lastFailure the last name used that was already taken; null for first try.
	 * @param callback for when this action has completed
	 */
	public void claimCodeName(String lastFailure, final PokemonCallback callback) {
		String name = generateRandomCodename();

		List<TutorialListener> listeners = api.getListeners(TutorialListener.class);
		for (TutorialListener listener : listeners) {
			String listenerName = listener.claimName(api, lastFailure);
			if (listenerName != null) {
				name = listenerName;
				break;
			}
		}

		final String finalName = name;

		ClaimCodenameMessage message = ClaimCodenameMessage.newBuilder().setCodename(name).build();

		PokemonRequest request = new PokemonRequest(RequestType.CLAIM_CODENAME, message)
				.withCallback(new RequestCallback() {
					@Override
					public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
						if (Utils.callbackException(response, callback)) {
							return;
						}
						String updatedCodename = null;
						try {
							ClaimCodenameResponse claimResponse =
									ClaimCodenameResponse.parseFrom(response.getResponseData());
							if (claimResponse.getStatus() != ClaimCodenameResponse.Status.SUCCESS) {
								if (claimResponse.getUpdatedPlayer().getRemainingCodenameClaims() > 0) {
									claimCodeName(finalName, callback);
								}
							} else {
								updatedCodename = claimResponse.getCodename();
								updateProfile(claimResponse.getUpdatedPlayer());
							}
						} catch (InvalidProtocolBufferException e) {
							callback.onCompleted(new RemoteServerException(e));
						}

						if (updatedCodename != null) {
							completeNameSelection(callback);
						}
					}
				});

		api.getRequestHandler().sendRequests(CommonRequests.fillRequest(request, api));
	}

	/**
	 * Complete the name selection tutorial.
	 *
	 * @param callback for when this action completes
	 */
	private void completeNameSelection(final PokemonCallback callback) {
		markTutorial(TutorialStateOuterClass.TutorialState.NAME_SELECTION, new PokemonCallback() {
			@Override
			public void onCompleted(Exception exception) {
				if (exception == null) {
					final GetPlayerMessage getPlayerMessage = GetPlayerMessage.newBuilder()
							.setPlayerLocale(playerLocale.getPlayerLocale()).build();
					PokemonRequest getPlayerRequest = new PokemonRequest(RequestType.GET_PLAYER, getPlayerMessage)
							.withCallback(new RequestCallback() {
								@Override
								public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
									updateProfile(GetPlayerResponse.parseFrom(response.getResponseData()));
									callback.onCompleted(null);
								}
							});

					PokemonRequest[] requests = CommonRequests.fillRequest(getPlayerRequest, api);
					api.getRequestHandler().sendRequests(requests);
				} else {
					callback.onCompleted(exception);
				}
			}
		});
	}

	/**
	 * The last step, mark the last tutorial state as completed
	 *
	 * @param callback callback for when this action has completed
	 */
	public void firstTimeExperienceComplete(PokemonCallback callback) {
		markTutorial(TutorialStateOuterClass.TutorialState.FIRST_TIME_EXPERIENCE_COMPLETE, callback);
	}

	/**
	 * Marks the given tutorial state complete.
	 *
	 * @param state the tutorial state to mark complete
	 * @param callback callback for when this action has completed
	 */
	private void markTutorial(TutorialStateOuterClass.TutorialState state, final PokemonCallback callback) {
		final MarkTutorialCompleteMessage tutorialMessage = MarkTutorialCompleteMessage.newBuilder()
				.addTutorialsCompleted(state)
				.setSendMarketingEmails(false)
				.setSendPushNotifications(false).build();

		PokemonRequest request = new PokemonRequest(RequestType.MARK_TUTORIAL_COMPLETE, tutorialMessage)
				.withCallback(new RequestCallback() {
					@Override
					public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
						callback.onCompleted(response.getException());
					}
				});
		PokemonRequest[] requests = CommonRequests.fillRequest(request, api);

		api.getRequestHandler().sendRequests(requests);
	}

	private static String generateRandomCodename() {
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