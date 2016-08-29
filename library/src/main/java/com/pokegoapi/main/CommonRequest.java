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

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.util.Constant;

import POGOProtos.Enums.PlatformOuterClass.Platform;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage;
import POGOProtos.Networking.Requests.Messages.DownloadRemoteConfigVersionMessageOuterClass.DownloadRemoteConfigVersionMessage;
import POGOProtos.Networking.Requests.Messages.CheckChallenge;
import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass.DownloadSettingsMessage;
import POGOProtos.Networking.Requests.Messages.GetAssetDigestMessageOuterClass.GetAssetDigestMessage;
import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass.GetHatchedEggsMessage;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;

/**
 * Created by iGio90 on 27/08/16.
 */

public class CommonRequest {

	/**
	 * Constant for repetitive usage of DownloadRemoteConfigVersionMessage request
	 *
	 * @return DownloadRemoteConfigVersionMessage
	 */
	public static DownloadRemoteConfigVersionMessage getDownloadRemoteConfigVersionMessageRequest() {
		return DownloadRemoteConfigVersionMessage
				.newBuilder()
				.setPlatform(Platform.IOS)
				.setAppVersion(Constant.APP_VERSION)
				.build();
	}

	/**
	 * Constant for repetitive usage of GetAssetDigestMessage request
	 *
	 * @return GetAssetDigestMessage
	 */
	public static GetAssetDigestMessage getGetAssetDigestMessageRequest() {
		return GetAssetDigestMessage.newBuilder()
				.setPlatform(Platform.IOS)
				.setAppVersion(Constant.APP_VERSION)
				.build();
	}

	/**
	 * Constant for repetitive usage of DownloadSettingsMessage request
	 *
	 * @param api The current instance of PokemonGO
	 * @return DownloadSettingsMessage
	 */
	public static DownloadSettingsMessage getDownloadSettingsMessageRequest(PokemonGo api) {
		return DownloadSettingsMessage.newBuilder()
				.setHash(api.getSettings().getHash())
				.build();
	}

	/**
	 * Constant for repetitive usage of GetInventoryMessage request
	 *
	 * @param api The current instance of PokemonGO
	 * @return GetInventoryMessage
	 */
	public static GetInventoryMessage getDefaultGetInventoryMessage(PokemonGo api) {
		return GetInventoryMessage.newBuilder()
				.setLastTimestampMs(api.getInventories().getLastInventoryUpdate())
				.build();
	}

	/**
	 * Constant for repetitive usage of GetInventoryMessage request
	 *
	 * @param api The current instance of PokemonGO
	 * @return GetInventoryMessage
	 */
	public static CheckChallenge.CheckChallengeMessage getCheckChallengeMessage(PokemonGo api) {
		return CheckChallenge.CheckChallengeMessage.newBuilder().build();
	}

	/**
	 * Most of the requests from the official client are fired together with the following
	 * requests. We will append our request on top of the array and we will send it
	 * together with the others.
	 *
	 * @param request The main request we want to fire
	 * @param api The current instance of PokemonGO
	 * @return an array of ServerRequest
	 */
	public static ServerRequest[] fillRequest(ServerRequest request, PokemonGo api) {
		ServerRequest[] serverRequests = new ServerRequest[6];
		serverRequests[0] = request;
		serverRequests[1] = new ServerRequest(RequestType.CHECK_CHALLENGE,
				CheckChallenge.CheckChallengeMessage.getDefaultInstance());
		serverRequests[2] = new ServerRequest(RequestType.GET_HATCHED_EGGS,
				GetHatchedEggsMessage.getDefaultInstance());
		serverRequests[3] = new ServerRequest(RequestType.GET_INVENTORY,
				CommonRequest.getDefaultGetInventoryMessage(api));
		serverRequests[4] = new ServerRequest(RequestType.CHECK_AWARDED_BADGES,
				CheckAwardedBadgesMessage.getDefaultInstance());
		serverRequests[5] = new ServerRequest(RequestType.DOWNLOAD_SETTINGS,
				CommonRequest.getDownloadSettingsMessageRequest(api));
		return serverRequests;
	}
}
