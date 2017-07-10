/*
 *	 This program is free software: you can redistribute it and/or modify
 *	 it under the terms of the GNU General Public License as published by
 *	 the Free Software Foundation, either version 3 of the License, or
 *	 (at your option) any later version.
 *
 *	 This program is distributed in the hope that it will be useful,
 *	 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	 GNU General Public License for more details.
 *
 *	 You should have received a copy of the GNU General Public License
 *	 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.map.fort;

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Data.PokemonDataOuterClass.PokemonDataOrBuilder;
import POGOProtos.Data.Raid.RaidInfoOuterClass.RaidInfo;
import POGOProtos.Enums.RaidLevelOuterClass.RaidLevel;
import POGOProtos.Enums.TeamColorOuterClass;
import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass.FortDetailsMessage;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.google.common.geometry.S2LatLng;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.AsyncHelper;

import lombok.Getter;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

public class Raid {

	private final PokemonGo api;
	@Getter
	private final FortDataOuterClass.FortData fortData;
	@Getter
	private long cooldownCompleteTimestampMs;
	@Getter
	private final RaidInfo raidInfo;
	
	public Raid(PokemonGo api, FortDataOuterClass.FortData fortData) {
		this.api = api;
		this.fortData = fortData;
		this.raidInfo = fortData.getRaidInfo();
	}
	
	
	public double getDistance() {
		S2LatLng pokestop = S2LatLng.fromDegrees(getLatitude(), getLongitude());
		S2LatLng player = S2LatLng.fromDegrees(api.getLatitude(), api.getLongitude());
		return pokestop.getEarthDistance(player);
	}
	
	/**
	 * Get more detailed information about a raid.
	 *
	 * @return FortDetails
	 */
	private Observable<FortDetails> getDetailsAsync() {
		FortDetailsMessage reqMsg = FortDetailsMessage.newBuilder()
				.setFortId(getId())
				.setLatitude(getLatitude())
				.setLongitude(getLongitude())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.FORT_DETAILS,
				reqMsg);
		return api.getRequestHandler().sendAsyncServerRequests(serverRequest, true).map(
				new Func1<ByteString, FortDetails>() {
					@Override
					public FortDetails call(ByteString result) {
						FortDetailsResponseOuterClass.FortDetailsResponse response = null;
						try {
							response = FortDetailsResponseOuterClass.FortDetailsResponse.parseFrom(result);
						} catch (InvalidProtocolBufferException e) {
							throw Exceptions.propagate(e);
						}
						return new FortDetails(response);
					}
				});
	}
	
	private FortDetails getDetails() throws RequestFailedException {
		return AsyncHelper.toBlocking(getDetailsAsync());
	}
	
	public String getName() throws RequestFailedException {
		return getDetails().getName();
	}
	
	public String getDescription() throws RequestFailedException {
		return getDetails().getDescription();
	}
	
	public TeamColorOuterClass.TeamColor getTeam() throws RequestFailedException {
		return fortData.getOwnedByTeam();
	}
	
	public long getRaidSeed() {
		return raidInfo.getRaidSeed();
	}
	
	public long getRaidSpawnMs() {
		return raidInfo.getRaidSpawnMs();
	}

	public long getRaidBattleMs() {
		return raidInfo.getRaidBattleMs();
	}

	public long getRaidEndMs() {
		return raidInfo.getRaidEndMs();
	}

	public boolean hasRaidPokemon() {
		return raidInfo.hasRaidPokemon();
	}

	public PokemonData getRaidPokemon() {
		return raidInfo.getRaidPokemon();
	}

	public PokemonDataOrBuilder getRaidPokemonOrBuilder() {
		return raidInfo.getRaidPokemonOrBuilder();
	}

	public int getRaidLevelValue() {
		return raidInfo.getRaidLevelValue();
	}

	public RaidLevel getRaidLevel() {
		return raidInfo.getRaidLevel();
	}

	public boolean getComplete() {
		return raidInfo.getComplete();
	}

	public boolean getIsExclusive() {
		return raidInfo.getIsExclusive();
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

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Raid && ((Raid) obj).getId().equals(getId());
	}
}
