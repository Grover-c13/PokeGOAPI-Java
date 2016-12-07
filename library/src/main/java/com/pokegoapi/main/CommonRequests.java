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
import POGOProtos.Networking.Requests.Messages.CheckChallenge.CheckChallengeMessage;
import POGOProtos.Networking.Requests.Messages.DownloadRemoteConfigVersionMessageOuterClass.DownloadRemoteConfigVersionMessage;
import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass.DownloadSettingsMessage;
import POGOProtos.Networking.Requests.Messages.GetAssetDigestMessageOuterClass.GetAssetDigestMessage;
import POGOProtos.Networking.Requests.Messages.GetBuddyWalked.GetBuddyWalkedMessage;
import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass.GetHatchedEggsMessage;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckAwardedBadgesResponseOuterClass.CheckAwardedBadgesResponse;
import POGOProtos.Networking.Responses.CheckChallengeResponseOuterClass.CheckChallengeResponse;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import POGOProtos.Networking.Responses.GetBuddyWalkedResponseOuterClass.GetBuddyWalkedResponse;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.listener.PokemonListener;
import com.pokegoapi.util.Constant;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iGio90 on 27/08/16.
 */

public class CommonRequests {
	private static Map<RequestType, CommonRequest> COMMON_REQUESTS = new LinkedHashMap<>();

	static {
		COMMON_REQUESTS.put(RequestType.CHECK_CHALLENGE, new CommonRequest() {
			@Override
			public ServerRequest create(PokemonGo api, RequestType requestType) {
				return new ServerRequest(requestType, CheckChallengeMessage.getDefaultInstance());
			}

			@Override
			public void parse(PokemonGo api, ByteString data, RequestType requestType)
					throws InvalidProtocolBufferException {
				CheckChallengeResponse response = CheckChallengeResponse.parseFrom(data);
				api.updateChallenge(response.getChallengeUrl(), response.getShowChallenge());
			}
		});
		COMMON_REQUESTS.put(RequestType.GET_HATCHED_EGGS, new CommonRequest() {
			@Override
			public ServerRequest create(PokemonGo api, RequestType requestType) {
				return new ServerRequest(requestType, GetHatchedEggsMessage.getDefaultInstance());
			}

			@Override
			public void parse(PokemonGo api, ByteString data, RequestType requestType)
					throws InvalidProtocolBufferException {
				GetHatchedEggsResponse response = GetHatchedEggsResponse.parseFrom(data);
				api.getInventories().getHatchery().updateHatchedEggs(response);
			}
		});
		COMMON_REQUESTS.put(RequestType.GET_INVENTORY, new CommonRequest() {
			@Override
			public ServerRequest create(PokemonGo api, RequestType requestType) {
				return new ServerRequest(requestType, CommonRequests.getDefaultGetInventoryMessage(api));
			}

			@Override
			public void parse(PokemonGo api, ByteString data, RequestType requestType)
					throws InvalidProtocolBufferException {
				GetInventoryResponse response = GetInventoryResponse.parseFrom(data);
				api.getInventories().updateInventories(response);
			}
		});
		COMMON_REQUESTS.put(RequestType.CHECK_AWARDED_BADGES, new CommonRequest() {
			@Override
			public ServerRequest create(PokemonGo api, RequestType requestType) {
				return new ServerRequest(requestType, CheckAwardedBadgesMessage.getDefaultInstance());
			}

			@Override
			public void parse(PokemonGo api, ByteString data, RequestType requestType)
					throws InvalidProtocolBufferException {
				CheckAwardedBadgesResponse response = CheckAwardedBadgesResponse.parseFrom(data);
				try {
					api.getPlayerProfile().updateAwardedMedals(response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		COMMON_REQUESTS.put(RequestType.DOWNLOAD_SETTINGS, new CommonRequest() {
			@Override
			public ServerRequest create(PokemonGo api, RequestType requestType) {
				return new ServerRequest(requestType, CommonRequests.getDownloadSettingsMessageRequest(api));
			}

			@Override
			public void parse(PokemonGo api, ByteString data, RequestType requestType)
					throws InvalidProtocolBufferException {
				DownloadSettingsResponse response = DownloadSettingsResponse.parseFrom(data);
				api.getSettings().updateSettings(response);
			}
		});
		COMMON_REQUESTS.put(RequestType.GET_BUDDY_WALKED, new CommonRequest() {
			@Override
			public ServerRequest create(PokemonGo api, RequestType requestType) {
				return new ServerRequest(requestType, GetBuddyWalkedMessage.getDefaultInstance());
			}

			@Override
			public void parse(PokemonGo api, ByteString data, RequestType requestType)
					throws InvalidProtocolBufferException {
				GetBuddyWalkedResponse response = GetBuddyWalkedResponse.parseFrom(data);
				int candies = response.getCandyEarnedCount();
				if (response.getSuccess() && candies > 0) {
					List<PokemonListener> listeners = api.getListeners(PokemonListener.class);
					for (PokemonListener listener : listeners) {
						listener.onBuddyFindCandy(api, response.getFamilyCandyId(), candies);
					}
				}
			}
		});
	}

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
	 * Most of the requests from the official client are fired together with the following
	 * requests. We will append our request on top of the array and we will send it
	 * together with the others.
	 *
	 * @param request The main request we want to fire
	 * @param api The current instance of PokemonGO
	 * @return an array of ServerRequest
	 *
	 * @deprecated Use ServerRequest#withCommons
	 */
	@Deprecated
	public static ServerRequest[] fillRequest(ServerRequest request, PokemonGo api) {
		return Utils.appendRequests(new ServerRequest[]{request}, getCommonRequests(api));
	}

	/**
	 * Construct an array of common requests
	 *
	 * @param api The current instance of PokemonGO
	 * @return an array of ServerRequests for each CommonRequest
	 */
	public static ServerRequest[] getCommonRequests(PokemonGo api) {
		ServerRequest[] requests = new ServerRequest[COMMON_REQUESTS.size()];
		int index = 0;
		for (Map.Entry<RequestType, CommonRequest> entry : COMMON_REQUESTS.entrySet()) {
			requests[index++] = entry.getValue().create(api, entry.getKey());
		}
		return requests;
	}

	/**
	 * Parses the given common request
	 * @param api the current api
	 * @param type the request type
	 * @param data the response data
	 * @throws InvalidProtocolBufferException if the server returns an invalid response
	 */
	public static void parse(PokemonGo api, RequestType type, ByteString data) throws InvalidProtocolBufferException {
		CommonRequest commonRequest = COMMON_REQUESTS.get(type);
		if (commonRequest != null) {
			commonRequest.parse(api, data, type);
		}
	}
}