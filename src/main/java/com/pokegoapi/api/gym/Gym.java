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

import POGOProtos.Data.Gym.GymMembershipOuterClass.GymMembership;
import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Enums.TeamColorOuterClass;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Networking.Requests.Messages.GetGymDetailsMessageOuterClass.GetGymDetailsMessage;
import POGOProtos.Networking.Requests.Messages.StartGymBattleMessageOuterClass.StartGymBattleMessage;
import POGOProtos.Networking.Requests.Messages.StartGymBattleMessageOuterClass.StartGymBattleMessage.Builder;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetGymDetailsResponseOuterClass.GetGymDetailsResponse;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;

import java.util.ArrayList;
import java.util.List;

public class Gym {
	private FortData proto;
	private GetGymDetailsResponse details;
	private PokemonGo api;

	/**
	 * .
	 *
	 * @return
	 */
	public Gym(PokemonGo api, FortData proto) {
		this.api = api;
		this.proto = proto;
		this.details = null;
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

	public boolean getIsInBattle() {
		return proto.getIsInBattle();
	}



	private GetGymDetailsResponse details() throws LoginFailedException, RemoteServerException {
		if (details == null) {
			GetGymDetailsMessage reqMsg = GetGymDetailsMessage
											.newBuilder()
											.setGymId(this.getId())
											.setGymLatitude(this.getLatitude())
											.setGymLongitude(this.getLongitude())
											.setPlayerLatitude(api.getLatitude())
											.setPlayerLongitude(api.getLongitude())
											.build();


			ServerRequest serverRequest = new ServerRequest(RequestType.GET_GYM_DETAILS, reqMsg);
			api.getRequestHandler().sendServerRequests(serverRequest);

			try {
				details = GetGymDetailsResponse.parseFrom(serverRequest.getData());
			} catch (InvalidProtocolBufferException e) {
				throw new RemoteServerException();
			}

		}

		return details;
	}

	public String getName() throws LoginFailedException, RemoteServerException {
		return details().getName();
	}

	public ProtocolStringList getUrlsList() throws LoginFailedException, RemoteServerException {
		return details().getUrlsList();
	}

	public GetGymDetailsResponse.Result getResult() throws LoginFailedException, RemoteServerException {
		return details().getResult();
	}

	public boolean inRange() throws LoginFailedException, RemoteServerException {
		GetGymDetailsResponse.Result result = getResult();
		System.out.println(result + " != " + GetGymDetailsResponse.Result.ERROR_NOT_IN_RANGE);
		System.out.println(result != GetGymDetailsResponse.Result.ERROR_NOT_IN_RANGE);
		return ( result != GetGymDetailsResponse.Result.ERROR_NOT_IN_RANGE);
	}

	public String getDescription() throws LoginFailedException, RemoteServerException {
		return details().getDescription();
	}


	public List<GymMembership> getGymMembers() throws LoginFailedException, RemoteServerException {
		return details().getGymState().getMembershipsList();
	}

	public List<PokemonData> getDefendingPokemon() throws LoginFailedException, RemoteServerException {
		List<PokemonData> data = new ArrayList<PokemonData>();

		for (GymMembership gymMember : getGymMembers()) {
			data.add(gymMember.getPokemonData());
		}

		return data;
	}

	protected PokemonGo getApi() {
		return api;
	}


}
