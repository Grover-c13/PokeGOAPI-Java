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
import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass.GetHatchedEggsMessage;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckChallengeResponseOuterClass.CheckChallengeResponse;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.util.Constant;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by iGio90 on 27/08/16.
 */

public class CommonRequests {
	private static final Map<RequestType, CommonRequest> COMMON_REQUESTS = new LinkedHashMap<>();

	static {
		COMMON_REQUESTS.put(RequestType.CHECK_CHALLENGE, new CommonRequest() {
			@Override
			public PokemonRequest create(final PokemonGo api, final RequestType requestType) {
				return new PokemonRequest(requestType, CheckChallengeMessage.getDefaultInstance())
						.withCallback(new RequestCallback() {
							@Override
							public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
								CheckChallengeResponse checkResponse =
										CheckChallengeResponse.parseFrom(response.getResponseData());
								api.updateChallenge(checkResponse.getChallengeUrl(), checkResponse.getShowChallenge());
							}
						});
			}
		});
		COMMON_REQUESTS.put(RequestType.GET_HATCHED_EGGS, new CommonRequest() {
			@Override
			public PokemonRequest create(final PokemonGo api, final RequestType requestType) {
				return new PokemonRequest(requestType, GetHatchedEggsMessage.getDefaultInstance())
						.withCallback(new RequestCallback() {
							@Override
							public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
								GetHatchedEggsResponse eggResponse
										= GetHatchedEggsResponse.parseFrom(response.getResponseData());
								api.getInventories().getHatchery().updateHatchedEggs(eggResponse);
							}
						});
			}
		});
		COMMON_REQUESTS.put(RequestType.GET_INVENTORY, new CommonRequest() {
			@Override
			public PokemonRequest create(final PokemonGo api, final RequestType requestType) {
				return new PokemonRequest(requestType, GetInventoryMessage.getDefaultInstance())
						.withCallback(new RequestCallback() {
							@Override
							public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
								GetInventoryResponse inventoryResponse
										= GetInventoryResponse.parseFrom(response.getResponseData());
								api.getInventories().updateInventories(inventoryResponse);
							}
						});
			}
		});
		COMMON_REQUESTS.put(RequestType.CHECK_AWARDED_BADGES, new CommonRequest() {
			@Override
			public PokemonRequest create(final PokemonGo api, final RequestType requestType) {
				return new PokemonRequest(requestType, CheckAwardedBadgesMessage.getDefaultInstance())
						.withCallback(new RequestCallback() {
							@Override
							public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
							}
						});
			}
		});
		COMMON_REQUESTS.put(RequestType.DOWNLOAD_SETTINGS, new CommonRequest() {
			@Override
			public PokemonRequest create(final PokemonGo api, final RequestType requestType) {
				return new PokemonRequest(requestType, DownloadSettingsMessage.getDefaultInstance())
						.withCallback(new RequestCallback() {
							@Override
							public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
								DownloadSettingsResponse settingsResponse
										= DownloadSettingsResponse.parseFrom(response.getResponseData());
								api.getSettings().updateSettings(settingsResponse);
							}
						});
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
	 * Append CheckChallenge request to the given ServerRequest
	 *
	 * @param requests The main requests we want to fire
	 * @return an array of PokemonRequests
	 */
	public static PokemonRequest[] appendCheckChallenge(PokemonGo api, PokemonRequest... requests) {
		RequestType type = RequestType.CHECK_CHALLENGE;
		return Utils.appendRequests(requests, COMMON_REQUESTS.get(type).create(api, type));
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
	public static PokemonRequest[] fillRequest(PokemonRequest request, PokemonGo api) {
		return Utils.appendRequests(new PokemonRequest[]{request}, getCommonRequests(api));
	}

	/**
	 * Construct an array of common requests
	 *
	 * @param api The current instance of PokemonGO
	 * @return an array of PokemonRequests for each CommonRequest
	 */
	public static PokemonRequest[] getCommonRequests(PokemonGo api) {
		PokemonRequest[] requests = new PokemonRequest[COMMON_REQUESTS.size()];
		int index = 0;
		for (Map.Entry<RequestType, CommonRequest> entry : COMMON_REQUESTS.entrySet()) {
			requests[index++] = entry.getValue().create(api, entry.getKey());
		}
		return requests;
	}
}
