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

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.AsyncRemoteServerException;
import com.pokegoapi.util.Constant;

import POGOProtos.Enums.PlatformOuterClass.Platform;
import POGOProtos.Networking.Requests.Messages.CheckAwardedBadgesMessageOuterClass.CheckAwardedBadgesMessage;
import POGOProtos.Networking.Requests.Messages.CheckChallenge.CheckChallengeMessage;
import POGOProtos.Networking.Requests.Messages.DownloadRemoteConfigVersionMessageOuterClass.DownloadRemoteConfigVersionMessage;
import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass.DownloadSettingsMessage;
import POGOProtos.Networking.Requests.Messages.GetAssetDigestMessageOuterClass.GetAssetDigestMessage;
import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass.GetHatchedEggsMessage;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass.GetInventoryMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;

/**
 * Created by iGio90 on 27/08/16.
 */

public class CommonRequest {

	/**
	 * Constant for repetitive usage of DownloadRemoteConfigVersionMessage request
	 *
	 * @return DownloadRemoteConfigVersionMessage
	 */
	public static DownloadRemoteConfigVersionMessage getDefaultDownloadRemoteConfigVersionRequest() {
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
	public static GetAssetDigestMessage getDefaultGetAssetDigestMessageRequest() {
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
	public static DownloadSettingsMessage getDefaultDownloadSettingsMessageRequest(PokemonGo api) {
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
		GetInventoryMessage.Builder builder = GetInventoryMessage.newBuilder();
		if (api.getInventories().getLastInventoryUpdate() != 0) {
			builder.setLastTimestampMs(api.getInventories().getLastInventoryUpdate());
		}
		return builder.build();
	}

	/**
	 * Build an internal server request for the checkchallenge message
	 *
	 * @return GetInventoryMessage
	 */
	public static InternalServerRequest getDefaultCheckChallenge() {
		return new InternalServerRequest(RequestType.CHECK_CHALLENGE,
				CheckChallengeMessage.getDefaultInstance());
	}

	/**
	 * Construct a List of common requests
	 *
	 * @param api The current instance of PokemonGO
	 * @return a List of AsyncServerRequests
	 */
	public static InternalServerRequest[] getCommonRequests(PokemonGo api) {
		return new InternalServerRequest[] {
				getDefaultCheckChallenge(),
				new InternalServerRequest(RequestType.GET_HATCHED_EGGS,
						GetHatchedEggsMessage.getDefaultInstance()),
				new InternalServerRequest(RequestType.GET_INVENTORY,
						CommonRequest.getDefaultGetInventoryMessage(api)),
				new InternalServerRequest(RequestType.CHECK_AWARDED_BADGES,
						CheckAwardedBadgesMessage.getDefaultInstance()),
				new InternalServerRequest(RequestType.DOWNLOAD_SETTINGS,
						CommonRequest.getDefaultDownloadSettingsMessageRequest(api))
		};
	}

	/**
	 * parse the response received during commonRequest
	 *
	 * @param api The current instance of PokemonGO
	 * @param requestType The requestType of the current common request
	 * @param data The data received from server
	 */
	public static void parse(PokemonGo api, RequestType requestType, ByteString data) {
		try {
			switch (requestType) {
				case GET_INVENTORY:
					api.getInventories().updateInventories(GetInventoryResponse.parseFrom(data));
					break;
				case DOWNLOAD_SETTINGS:
					api.getSettings().updateSettings(DownloadSettingsResponse.parseFrom(data));
					break;
				case CHECK_AWARDED_BADGES:
				//	api.getPlayerProfile().equipBadge(CheckAwardedBadgesResponse.parseFrom(data));
				default:
					break;
			}
		} catch (InvalidProtocolBufferException e) {
			throw new AsyncRemoteServerException(e);
		}
	}
}
