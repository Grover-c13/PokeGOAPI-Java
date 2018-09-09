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

package com.pokegoapi.main;

import POGOProtos.Enums.PlatformOuterClass.Platform;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage;
import POGOProtos.Networking.Requests.Messages.CheckChallengeMessageOuterClass.CheckChallengeMessage;
import POGOProtos.Networking.Requests.Messages.DownloadItemTemplatesMessageOuterClass.DownloadItemTemplatesMessage;
import POGOProtos.Networking.Requests.Messages.DownloadRemoteConfigVersionMessageOuterClass.DownloadRemoteConfigVersionMessage;
import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass.DownloadSettingsMessage;
import POGOProtos.Networking.Requests.Messages.GetAssetDigestMessageOuterClass.GetAssetDigestMessage;
import POGOProtos.Networking.Requests.Messages.GetBuddyWalkedMessageOuterClass.GetBuddyWalkedMessage;
import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass.GetHatchedEggsMessage;
import POGOProtos.Networking.Requests.Messages.GetHoloInventoryMessageOuterClass.GetHoloInventoryMessage;
import POGOProtos.Networking.Requests.Messages.GetIncensePokemonMessageOuterClass.GetIncensePokemonMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.CheckChallengeResponseOuterClass.CheckChallengeResponse;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import POGOProtos.Networking.Responses.GetBuddyWalkedResponseOuterClass.GetBuddyWalkedResponse;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetHoloInventoryResponseOuterClass.GetHoloInventoryResponse;
import POGOProtos.Networking.Responses.GetIncensePokemonResponseOuterClass.GetIncensePokemonResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.listener.PokemonListener;
import com.pokegoapi.exceptions.request.RequestFailedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all the common requests, handled by the API
 */
public class CommonRequests {
	/**
	 * Constant for repetitive usage of DownloadRemoteConfigVersionMessage request
	 *
	 * @param api the current API instance
	 * @return DownloadRemoteConfigVersionMessage
	 */
	public static DownloadRemoteConfigVersionMessage getDownloadRemoteConfigVersionMessageRequest(PokemonGo api) {
		return DownloadRemoteConfigVersionMessage
				.newBuilder()
				.setPlatform(Platform.IOS)
				.setAppVersion(api.getVersion())
				.build();
	}

	/**
	 * Constant for repetitive usage of GetAssetDigestMessage request
	 *
	 * @param api the current API instance
	 * @return GetAssetDigestMessage
	 */
	public static GetAssetDigestMessage getGetAssetDigestMessageRequest(PokemonGo api) {
		return GetAssetDigestMessage.newBuilder()
				.setPlatform(Platform.IOS)
				.setAppVersion(api.getVersion())
				.build();
	}

	/**
	 * Constant for repetitive usage of DownloadItemTemplatesMessage request
	 *
	 * @return the DownloadItemTemplatesMessage
	 */
	public static DownloadItemTemplatesMessage getDownloadItemTemplatesRequest() {
		return DownloadItemTemplatesMessage.newBuilder().build();
	}

	/**
	 * Returns a list of all default commons to be included in an envelope
	 *
	 * @param api the current api
	 * @param request the request in this envelope
	 * @return a list of all default commons to be included
	 */
	public static List<ServerRequest> getDefaultCommons(PokemonGo api, RequestType request) {
		List<ServerRequest> defaultCommons = new ArrayList<>();
		if (!api.hasChallenge() || request == RequestType.VERIFY_CHALLENGE) {
			defaultCommons.add(CommonRequests.checkChallenge());
		}
		defaultCommons.add(CommonRequests.getHatchedEggs());
		defaultCommons.add(CommonRequests.getInventory(api));
		defaultCommons.add(CommonRequests.checkAwardedBadges());
		if (api.loggingIn) {
			defaultCommons.add(CommonRequests.downloadSettings(api));
		}
		if (api.inventories.itemBag.isIncenseActive()) {
			defaultCommons.add(CommonRequests.getIncensePokemon(api));
		}
		if (api.hasTemplates()) {
			defaultCommons.add(CommonRequests.getBuddyWalked());
		}
		return defaultCommons;
	}

	/**
	 * Handles all commons in a ServerResponse
	 * @param api the current api
	 * @param response the response to handle
	 * @throws InvalidProtocolBufferException if an invalid response is parsed
	 * @throws RequestFailedException if a request fails while sending a request
	 */
	public static void handleCommons(PokemonGo api, ServerResponse response)
			throws InvalidProtocolBufferException, RequestFailedException {
		if (response.has(RequestType.DOWNLOAD_SETTINGS)) {
			ByteString data = response.get(RequestType.DOWNLOAD_SETTINGS);
			DownloadSettingsResponse settings = DownloadSettingsResponse.parseFrom(data);
			api.settings.updateSettings(settings);
		}
		if (response.has(RequestType.CHECK_CHALLENGE)) {
			ByteString data = response.get(RequestType.CHECK_CHALLENGE);
			CheckChallengeResponse checkChallenge = CheckChallengeResponse.parseFrom(data);
			api.updateChallenge(checkChallenge.getChallengeUrl(), checkChallenge.getShowChallenge());
		}
		if (response.has(RequestType.GET_HOLOHOLO_INVENTORY)) {
			ByteString data = response.get(RequestType.GET_HOLOHOLO_INVENTORY);
			GetHoloInventoryResponse inventory = GetHoloInventoryResponse.parseFrom(data);
			api.inventories.updateInventories(inventory);
		}
		if (response.has(RequestType.CHECK_AWARDED_BADGES)) {
			ByteString data = response.get(RequestType.CHECK_AWARDED_BADGES);
			CheckAwardedBadgesResponse awardedBadges = CheckAwardedBadgesResponse.parseFrom(data);
			api.playerProfile.updateAwardedMedals(awardedBadges);
		}
		if (response.has(RequestType.GET_HATCHED_EGGS)) {
			ByteString data = response.get(RequestType.GET_HATCHED_EGGS);
			GetHatchedEggsResponse hatchedEggs = GetHatchedEggsResponse.parseFrom(data);
			api.inventories.hatchery.updateHatchedEggs(hatchedEggs);
		}
		if (response.has(RequestType.GET_BUDDY_WALKED)) {
			ByteString data = response.get(RequestType.GET_BUDDY_WALKED);
			GetBuddyWalkedResponse buddyWalked = GetBuddyWalkedResponse.parseFrom(data);
			int candies = buddyWalked.getCandyEarnedCount();
			if (buddyWalked.getSuccess() && candies > 0) {
				List<PokemonListener> listeners = api.getListeners(PokemonListener.class);
				for (PokemonListener listener : listeners) {
					listener.onBuddyFindCandy(api, buddyWalked.getFamilyCandyId(), candies);
				}
			}
		}
		if (response.has(RequestType.GET_INCENSE_POKEMON)) {
			ByteString data = response.get(RequestType.GET_INCENSE_POKEMON);
			GetIncensePokemonResponse incense = GetIncensePokemonResponse.parseFrom(data);
			api.getMap().mapObjects.addIncensePokemon(incense);
		}
	}

	/**
	 * Creates a default CHECK_CHALLENGE request
	 *
	 * @return the constructed request
	 */
	public static ServerRequest checkChallenge() {
		return new ServerRequest(RequestType.CHECK_CHALLENGE, CheckChallengeMessage.getDefaultInstance());
	}

	/**
	 * Creates a default GET_HATCHED_EGGS request
	 *
	 * @return the constructed request
	 */
	public static ServerRequest getHatchedEggs() {
		return new ServerRequest(RequestType.GET_HATCHED_EGGS, GetHatchedEggsMessage.getDefaultInstance());
	}

	/**
	 * Creates a default GET_INVENTORY request
	 *
	 * @param api the current api
	 * @return the constructed request
	 */
	public static ServerRequest getInventory(PokemonGo api) {
		long lastUpdate = api.inventories.lastInventoryUpdate;
		GetHoloInventoryMessage message = GetHoloInventoryMessage.newBuilder().setLastTimestampMs(lastUpdate).build();
		return new ServerRequest(RequestType.GET_HOLOHOLO_INVENTORY, message);
	}

	/**
	 * Creates a default CHECK_AWARDED_BADGES request
	 *
	 * @return the constructed request
	 */
	public static ServerRequest checkAwardedBadges() {
		return new ServerRequest(RequestType.CHECK_AWARDED_BADGES, CheckAwardedBadgesMessage.getDefaultInstance());
	}

	/**
	 * Creates a default DOWNLOAD_SETTINGS request
	 *
	 * @param api the current api
	 * @return the constructed request
	 */
	public static ServerRequest downloadSettings(PokemonGo api) {
		String hash = api.settings.hash;
		DownloadSettingsMessage message = DownloadSettingsMessage.newBuilder().setHash(hash).build();
		return new ServerRequest(RequestType.DOWNLOAD_SETTINGS, message);
	}

	/**
	 * Creates a default GET_INCENSE_POKEMON request
	 *
	 * @param api the current api
	 * @return the constructed request
	 */
	public static ServerRequest getIncensePokemon(PokemonGo api) {
		GetIncensePokemonMessage message = GetIncensePokemonMessage.newBuilder()
				.setPlayerLatitude(api.latitude)
				.setPlayerLongitude(api.longitude)
				.build();
		return new ServerRequest(RequestType.GET_INCENSE_POKEMON, message);
	}

	/**
	 * Creates a default GET_BUDDY_WALKED request
	 *
	 * @return the constructed request
	 */
	public static ServerRequest getBuddyWalked() {
		return new ServerRequest(RequestType.GET_BUDDY_WALKED, GetBuddyWalkedMessage.getDefaultInstance());
	}
}