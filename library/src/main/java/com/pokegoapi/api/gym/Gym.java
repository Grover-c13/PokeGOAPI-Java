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

package com.pokegoapi.api.gym;

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Enums.TeamColorOuterClass;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Networking.Requests.Messages.GetGymDetailsMessageOuterClass.GetGymDetailsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetGymDetailsResponseOuterClass.GetGymDetailsResponse;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.api.map.MapPoint;
import rx.Observable;
import rx.functions.Func1;

public class Gym implements MapPoint {
	private final Networking networking;
	private final Location location;
	private final FortData proto;
	private GymDetails gymDetails = null;

	/**
	 * Gym object.
	 *
	 * @param proto The FortData to populate the Gym with.
	 */
	public Gym(Networking networking, Location location, FortData proto) {
		this.networking = networking;
		this.location = location;
		this.proto = proto;
	}

	public String getId() {
		return proto.getId();
	}

	public double getLatitude() {
		return proto.getLatitude();
	}

	public double getLongitude() {
		return proto.getLongitude();
	}

	public boolean getEnabled() {
		return proto.getEnabled();
	}

	public TeamColorOuterClass.TeamColor getOwnedByTeam() {
		return proto.getOwnedByTeam();
	}

	public PokemonIdOuterClass.PokemonId getGuardPokemonId() {
		return proto.getGuardPokemonId();
	}

	public int getGuardPokemonCp() {
		return proto.getGuardPokemonCp();
	}

	public long getPoints() {
		return proto.getGymPoints();
	}

	public boolean getIsInBattle() {
		return proto.getIsInBattle();
	}


	public Observable<Battle> battle(final Pokemon[] team) {
		final Gym gym = this;
		return getGymDetails().map(new Func1<GymDetails, Battle>() {
			@Override
			public Battle call(GymDetails gymDetails) {
				return new Battle(networking, location, team, gym, gymDetails);
			}
		});
	}


	public Observable<GymDetails> getGymDetails() {
		if (gymDetails != null) {
			return Observable.just(gymDetails);
		}
		return networking.queueRequest(RequestType.GET_GYM_DETAILS, GetGymDetailsMessage
					.newBuilder()
					.setGymId(this.getId())
					.setGymLatitude(this.getLatitude())
					.setGymLongitude(this.getLongitude())
					.setPlayerLatitude(location.getLatitude())
					.setPlayerLongitude(location.getLongitude())
					.build(), GetGymDetailsResponse.class).map(new Func1<GetGymDetailsResponse, GymDetails>() {
			@Override
			public GymDetails call(GetGymDetailsResponse getGymDetailsResponse) {
				gymDetails = new GymDetails(getGymDetailsResponse);
				return gymDetails;
			}
		});
	}
}
