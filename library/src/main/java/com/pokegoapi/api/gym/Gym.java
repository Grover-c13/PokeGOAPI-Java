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
import POGOProtos.Networking.Requests.Messages.FortDeployPokemonMessageOuterClass.FortDeployPokemonMessage;
import POGOProtos.Networking.Requests.Messages.GetGymDetailsMessageOuterClass.GetGymDetailsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.FortDeployPokemonResponseOuterClass.FortDeployPokemonResponse;
import POGOProtos.Networking.Responses.GetGymDetailsResponseOuterClass.GetGymDetailsResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonRequest;
import com.pokegoapi.main.PokemonResponse;
import com.pokegoapi.main.RequestCallback;
import com.pokegoapi.main.Utils;
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
	 * @param api The api object to use for requests.
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

	public boolean isInBattle() {
		return proto.getIsInBattle();
	}

	/**
	 * Returns whether or not this gym is attackable or not.
	 * @param attackable async callback to return the result
	 */
	public void isAttackable(final AsyncReturn<Boolean> attackable) {
		getGymMembers(new AsyncReturn<List<GymMembership>>() {
			@Override
			public void onReceive(List<GymMembership> members, Exception exception) {
				if (Utils.callbackException(exception, attackable, false)) {
					return;
				}
				attackable.onReceive(members.size() > 0, null);
			}
		});
	}

	public Battle battle(Pokemon[] team) {
		return new Battle(api, team, this);
	}

	/**
	 * Gets the details of this gym.
	 * @param result async callback to return the result from this method
	 */
	public void getDetails(final AsyncReturn<GetGymDetailsResponse> result) {
		if (details == null) {
			final GetGymDetailsMessage message = GetGymDetailsMessage
					.newBuilder()
					.setGymId(this.getId())
					.setGymLatitude(this.getLatitude())
					.setGymLongitude(this.getLongitude())
					.setPlayerLatitude(api.getLatitude())
					.setPlayerLongitude(api.getLongitude())
					.build();
			PokemonRequest request = new PokemonRequest(RequestType.GET_GYM_DETAILS, message);
			api.getRequestHandler().sendRequest(request, new RequestCallback() {
				@Override
				public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
					if (Utils.callbackException(response, result, details)) {
						return;
					}
					try {
						GetGymDetailsResponse messageResponse
								= GetGymDetailsResponse.parseFrom(response.getResponseData());
						details = messageResponse;
						result.onReceive(messageResponse, null);
					} catch (InvalidProtocolBufferException e) {
						result.onReceive(null, new RemoteServerException(e));
					}
				}
			});
		} else {
			result.onReceive(details, null);
		}
	}

	/**
	 * Gets the name of this gym.
	 *
	 * @param name async callback to return the result of this method
	 */
	public void getName(final AsyncReturn<String> name) {
		getDetails(new AsyncReturn<GetGymDetailsResponse>() {
			@Override
			public void onReceive(GetGymDetailsResponse details, Exception exception) {
				if (Utils.callbackException(exception, name, null)) {
					return;
				}
				if (details != null) {
					name.onReceive(details.getName(), null);
				} else {
					name.onReceive(null, null);
				}
			}
		});
	}

	/**
	 * Gets the URLs for this gym
	 * @param urls async callback to return the result of this method
	 */
	public void getURLsList(final AsyncReturn<ProtocolStringList> urls) {
		getDetails(new AsyncReturn<GetGymDetailsResponse>() {
			@Override
			public void onReceive(GetGymDetailsResponse details, Exception exception) {
				if (Utils.callbackException(exception, urls, null)) {
					return;
				}
				if (details != null) {
					urls.onReceive(details.getUrlsList(), null);
				} else {
					urls.onReceive(null, null);
				}
			}
		});
	}

	/**
	 * Gets the details response result.
	 * @param result async callback to return the result of this method
	 */
	public void getResult(final AsyncReturn<GetGymDetailsResponse.Result> result) {
		getDetails(new AsyncReturn<GetGymDetailsResponse>() {
			@Override
			public void onReceive(GetGymDetailsResponse details, Exception exception) {
				if (Utils.callbackException(exception, result, null)) {
					return;
				}
				if (details != null) {
					result.onReceive(details.getResult(), null);
				} else {
					result.onReceive(null, null);
				}
			}
		});
	}

	/**
	 * Returns if this gym is in range or not.
	 * @param inRange async callback to return the result of this method
	 */
	public void inRange(final AsyncReturn<Boolean> inRange) {
		getResult(new AsyncReturn<GetGymDetailsResponse.Result>() {
			@Override
			public void onReceive(GetGymDetailsResponse.Result result, Exception exception) {
				if (Utils.callbackException(exception, inRange, false)) {
					return;
				}
				if (result != null) {
					inRange.onReceive(result != GetGymDetailsResponse.Result.ERROR_NOT_IN_RANGE, null);
				} else {
					inRange.onReceive(false, null);
				}
			}
		});
	}

	/**
	 * Gets the description of this gym
	 * @param description async callback to return the result of this method
	 */
	public void getDescription(final AsyncReturn<String> description) {
		getDetails(new AsyncReturn<GetGymDetailsResponse>() {
			@Override
			public void onReceive(GetGymDetailsResponse details, Exception exception) {
				if (Utils.callbackException(exception, description, null)) {
					return;
				}
				if (details != null) {
					description.onReceive(details.getDescription(), null);
				} else {
					description.onReceive(null, null);
				}
			}
		});
	}

	/**
	 * Gets the members of this gym
	 * @param members async callback to return the result of this method
	 */
	public void getGymMembers(final AsyncReturn<List<GymMembership>> members) {
		getDetails(new AsyncReturn<GetGymDetailsResponse>() {
			@Override
			public void onReceive(GetGymDetailsResponse details, Exception exception) {
				if (Utils.callbackException(exception, members, null)) {
					return;
				}
				if (details != null) {
					members.onReceive(details.getGymState().getMembershipsList(), null);
				} else {
					members.onReceive(null, null);
				}
			}
		});
	}

	/**
	 * Get a list of pokemon defending this gym.
	 *
	 * @param result callback to return list of defending pokemon
	 */
	public void getDefendingPokemon(final AsyncReturn<List<PokemonData>> result) {
		getGymMembers(new AsyncReturn<List<GymMembership>>() {
			@Override
			public void onReceive(List<GymMembership> members, Exception exception) {
				if (Utils.callbackException(exception, result, null)) {
					return;
				}
				if (members != null) {
					List<PokemonData> data = new ArrayList<>();

					for (GymMembership member : members) {
						data.add(member.getPokemonData());
					}

					result.onReceive(data, null);
				} else {
					result.onReceive(new ArrayList<PokemonData>(), null);
				}
			}
		});
	}

	/**
	 * Deploys the given pokemon to this gym.
	 *
	 * @param pokemon the pokemon to deploy
	 * @param result callback to return the result from this action
	 */
	public void deployPokemon(Pokemon pokemon, final AsyncReturn<FortDeployPokemonResponse.Result> result) {
		FortDeployPokemonMessage message = FortDeployPokemonMessage.newBuilder()
				.setFortId(getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setPokemonId(pokemon.getId())
				.build();
		PokemonRequest request = new PokemonRequest(RequestType.FORT_DEPLOY_POKEMON, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, FortDeployPokemonResponse.Result.UNRECOGNIZED)) {
					return;
				}
				try {
					FortDeployPokemonResponse messageResponse
							= FortDeployPokemonResponse.parseFrom(response.getResponseData());
					result.onReceive(messageResponse.getResult(), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	protected PokemonGo getApi() {
		return api;
	}

}
