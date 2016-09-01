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
import POGOProtos.Networking.Responses.GetGymDetailsResponseOuterClass.GetGymDetailsResponse;
import POGOProtos.Networking.Responses.FortDeployPokemonResponseOuterClass.FortDeployPokemonResponse;

import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.MapPoint;

import java.util.ArrayList;
import java.util.List;

public class Gym implements MapPoint {
	private FortData proto;
	private GetGymDetailsResponse details;
	private PokemonGo api;

	/**
	 * Gym object.
	 *
	 * @param api   The api object to use for requests.
	 * @param proto The FortData to populate the Gym with.
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

	public long getPoints() {
		return proto.getGymPoints();
	}

	public boolean getIsInBattle() {
		return proto.getIsInBattle();
	}

	public boolean isAttackable() throws LoginFailedException, RemoteServerException {
		return this.getGymMembers().size() != 0;
	}

	public Battle battle(Pokemon[] team) {
		return new Battle(api, team, this);
	}

	private GetGymDetailsResponse details() {
		/*if (details == null) {
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

		return details;*/
		return null;
	}

	public String getName() {
		return details().getName();
	}

	public ProtocolStringList getUrlsList() {
		return details().getUrlsList();
	}

	public GetGymDetailsResponse.Result getResult() {
		return details().getResult();
	}

	public boolean inRange() {
		GetGymDetailsResponse.Result result = getResult();
		return (result != GetGymDetailsResponse.Result.ERROR_NOT_IN_RANGE);
	}

	public String getDescription() {
		return details().getDescription();
	}


	public List<GymMembership> getGymMembers() {
		return details().getGymState().getMembershipsList();
	}

	/**
	 * Get a list of pokemon defending this gym.
	 *
	 * @return List of pokemon
	 */
	public List<PokemonData> getDefendingPokemon() {
		List<PokemonData> data = new ArrayList<PokemonData>();

		for (GymMembership gymMember : getGymMembers()) {
			data.add(gymMember.getPokemonData());
		}

		return data;
	}

	/**
	 * Deploy pokemon
	 *
	 * @param pokemon The pokemon to deploy
	 * @return Result of attempt to deploy pokemon
	 */
	public FortDeployPokemonResponse.Result deployPokemon(Pokemon pokemon) {
		/*FortDeployPokemonMessage reqMsg = FortDeployPokemonMessage.newBuilder()
				.setFortId(getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setPokemonId(pokemon.getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.FORT_DEPLOY_POKEMON, reqMsg);
		api.getRequestHandler().sendServerRequests(serverRequest);

		try {
			return FortDeployPokemonResponse.parseFrom(serverRequest.getData()).getResult();
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException();
		}*/
		return null;

	}

	protected PokemonGo getApi() {
		return api;
	}

}
