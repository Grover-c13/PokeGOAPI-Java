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

package com.pokegoapi.api.map.fort;

import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.FortSearchMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

/**
 * Created by mjmfighter on 7/20/2016.
 */
public class Pokestop {

	private final PokemonGo api;
	private final FortDataOuterClass.FortData fortData;
	@Getter
	private long cooldownCompleteTimestampMs;


	public Pokestop(PokemonGo api, FortDataOuterClass.FortData fortData) {
		this.api = api;
		this.fortData = fortData;
		this.cooldownCompleteTimestampMs = fortData.getCooldownCompleteTimestampMs();
	}

	public boolean canLoot() {
		return canLoot(false);
	}

	public boolean canLoot(boolean ignoreDistance) {
		S2LatLng pokestop = S2LatLng.fromDegrees(getLatitude(), getLongitude());
		S2LatLng player = S2LatLng.fromDegrees(api.getLatitude(), api.getLongitude());
		double distance = pokestop.getEarthDistance(player);
		return (ignoreDistance || distance < 30) && cooldownCompleteTimestampMs < System.currentTimeMillis();
	}

	public String getId() {
		return fortData.getId();
	}

	public double getLatitude() {
		return fortData.getLatitude();
	}

	public double getLongitude() {
		return fortData.getLongitude();
	}

	/**
	 * Loots a pokestop for pokeballs and other items
	 *
	 * @return PokestopLootResult
	 * @throws LoginFailedException
	 * @throws RemoteServerException
	 */
	public PokestopLootResult loot() throws LoginFailedException, RemoteServerException {
		FortSearchMessageOuterClass.FortSearchMessage searchMessage = FortSearchMessageOuterClass.FortSearchMessage.newBuilder()
				.setFortId(getId())
				.setFortLatitude(getLatitude())
				.setFortLongitude(getLongitude())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.FORT_SEARCH, searchMessage);
		api.getRequestHandler().request(serverRequest);
		api.getRequestHandler().sendServerRequests();
		FortSearchResponseOuterClass.FortSearchResponse response;
		try {
			response = FortSearchResponseOuterClass.FortSearchResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		cooldownCompleteTimestampMs = response.getCooldownCompleteTimestampMs();
		return new PokestopLootResult(response);
	}

	/**
	 * Get more detailed information about a pokestop
	 *
	 * @return FortDetails
	 * @throws LoginFailedException
	 * @throws RemoteServerException
	 */
	public FortDetails getDetails() throws LoginFailedException, RemoteServerException {
		FortDetailsMessageOuterClass.FortDetailsMessage reqMsg = FortDetailsMessageOuterClass.FortDetailsMessage.newBuilder()
				.setFortId(getId())
				.setLatitude(getLatitude())
				.setLongitude(getLongitude())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.FORT_DETAILS, reqMsg);
		api.getRequestHandler().request(serverRequest);
		api.getRequestHandler().sendServerRequests();
		FortDetailsResponseOuterClass.FortDetailsResponse response = null;
		try {
			response = FortDetailsResponseOuterClass.FortDetailsResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		return new FortDetails(response);
	}


}
