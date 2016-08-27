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

import POGOProtos.Enums.PlatformOuterClass;
import POGOProtos.Networking.Requests.Messages.DownloadRemoteConfigVersionMessageOuterClass.DownloadRemoteConfigVersionMessage;
import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass.DownloadSettingsMessage;
import POGOProtos.Networking.Requests.Messages.GetAssetDigestMessageOuterClass.GetAssetDigestMessage;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass;

/**
 * Created by iGio90 on 27/08/16.
 */

public class CommonRequest {

	public static DownloadRemoteConfigVersionMessage getDownloadRemoteConfigVersionMessageRequest() {
		return DownloadRemoteConfigVersionMessage
				.newBuilder()
				.setPlatform(PlatformOuterClass.Platform.IOS)
				.setAppVersion(Constant.APP_VERSION)
				.build();
	}

	public static GetAssetDigestMessage getGetAssetDigestMessageRequest() {
		return GetAssetDigestMessage.newBuilder()
				.setPlatform(PlatformOuterClass.Platform.IOS)
				.setAppVersion(Constant.APP_VERSION)
				.build();
	}

	public static DownloadSettingsMessage getDownloadSettingsMessageRequest(PokemonGo api) {
		return DownloadSettingsMessage.newBuilder()
				.setHash(api.getSettings().getHash())
				.build();
	}

	public static GetInventoryMessageOuterClass.GetInventoryMessage getDefaultGetInventoryMessage(PokemonGo api) {
		return GetInventoryMessageOuterClass.GetInventoryMessage.newBuilder()
				.setLastTimestampMs(api.getInventories().getLastInventoryUpdate())
				.build();
	}
}
