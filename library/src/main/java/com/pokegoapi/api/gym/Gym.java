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

import POGOProtos.Data.Gym.GymDefenderOuterClass.GymDefender;
import POGOProtos.Data.Gym.GymStateOuterClass.GymState;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Enums.TeamColorOuterClass;
import POGOProtos.Enums.TutorialStateOuterClass.TutorialState;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Pokemon.MotivatedPokemonOuterClass.MotivatedPokemon;
import POGOProtos.Networking.Requests.Messages.FortDeployPokemonMessageOuterClass.FortDeployPokemonMessage;
import POGOProtos.Networking.Requests.Messages.GymGetInfoMessageOuterClass.GymGetInfoMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.FortDeployPokemonResponseOuterClass.FortDeployPokemonResponse;
import POGOProtos.Networking.Responses.GymGetInfoResponseOuterClass.GymGetInfoResponse;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.InsufficientLevelException;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.MapPoint;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;

public class Gym implements MapPoint {
	private FortData proto;
	private GymGetInfoResponse details;
	private PokemonGo api;
	private long points;

	/**
	 * Gym object.
	 *
	 * @param api The api object to use for requests.
	 * @param proto The FortData to populate the Gym with.
	 */
	public Gym(PokemonGo api, FortData proto) {
		this.api = api;
		this.proto = proto;
		this.points = proto.getGymPoints();
	}

	public String getId() {
		return proto.getId();
	}

	@Override
	public double getLatitude() {
		return proto.getLatitude();
	}

	@Override
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
		return points;
	}

	public boolean getIsInBattle() {
		return proto.getIsInBattle();
	}

	public boolean isAttackable() throws RequestFailedException {
		return this.getGymMembers().size() != 0;
	}

	/**
	 * Creates a battle for this gym
	 *
	 * @return the battle object
	 */
	public Battle battle() {
		int minimumPlayerLevel = api.getItemTemplates().getBattleSettings().getMinimumPlayerLevel();
		if (api.getPlayerProfile().getLevel() < minimumPlayerLevel) {
			throw new InsufficientLevelException("You must be at least " + minimumPlayerLevel + " to battle a gym!");
		}
		return new Battle(api, this);
	}

	/**
	 * Clears the details cache for this gym, and when requested again will send a request to the server instead of
	 * using the cached values.
	 */
	public void clearDetails() {
		details = null;
	}

	private GymGetInfoResponse details() throws RequestFailedException {
		List<TutorialState> tutorialStates = api.getPlayerProfile().getTutorialState().getTutorialStates();
		if (!tutorialStates.contains(TutorialState.GYM_TUTORIAL)) {
			api.getPlayerProfile().visitGymComplete();
		}

		if (details == null) {
			GymGetInfoMessage reqMsg = GymGetInfoMessage
					.newBuilder()
					.setGymId(this.getId())
					.setGymLatDegrees(this.getLatitude())
					.setGymLngDegrees(this.getLongitude())
					.setPlayerLatDegrees(api.getLatitude())
					.setPlayerLngDegrees(api.getLongitude())
					.build();

			ServerRequest serverRequest = new ServerRequest(RequestType.GYM_GET_INFO, reqMsg);
			api.getRequestHandler().sendServerRequests(serverRequest, true);

			try {
				details = GymGetInfoResponse.parseFrom(serverRequest.getData());
			} catch (InvalidProtocolBufferException e) {
				throw new RequestFailedException();
			}
		}

		return details;
	}

	public String getName() throws RequestFailedException {
		return details().getName();
	}	

	public String getDescription() throws RequestFailedException {
		return details().getDescription();
	}

	public List<GymDefender> getGymMembers()
			throws RequestFailedException {
		return details().getGymStatusAndDefenders().getGymDefenderList();
	}

	/**
	 * Get a list of pokemon defending this gym.
	 *
	 * @return List of pokemon
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public List<MotivatedPokemon> getDefendingPokemon() throws RequestFailedException {
		List<MotivatedPokemon> data = new ArrayList<MotivatedPokemon>();

		for (GymDefender gymMember : getGymMembers()) {
			data.add(gymMember.getMotivatedPokemon());
		}

		return data;
	}

	/**
	 * Deploy pokemon
	 *
	 * @param pokemon The pokemon to deploy
	 * @return Result of attempt to deploy pokemon
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public FortDeployPokemonResponse.Result deployPokemon(Pokemon pokemon) throws RequestFailedException {
		FortDeployPokemonMessage reqMsg = FortDeployPokemonMessage.newBuilder()
				.setFortId(getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setPokemonId(pokemon.getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.FORT_DEPLOY_POKEMON, reqMsg);
		api.getRequestHandler().sendServerRequests(serverRequest, true);

		try {
			return FortDeployPokemonResponse.parseFrom(serverRequest.getData()).getResult();
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException();
		}

	}

	/**
	 * Deploy pokemon
	 *
	 * @param pokemon The pokemon to deploy
	 * @return Result of attempt to deploy pokemon
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public Observable<FortDeployPokemonResponse.Result> deployPokemonAsync(Pokemon pokemon)
			throws RequestFailedException {
		FortDeployPokemonMessage reqMsg = FortDeployPokemonMessage.newBuilder()
				.setFortId(getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setPokemonId(pokemon.getId())
				.build();

		ServerRequest asyncServerRequest = new ServerRequest(RequestType.FORT_DEPLOY_POKEMON, reqMsg);
		return api.getRequestHandler()
				.sendAsyncServerRequests(asyncServerRequest)
				.map(new Func1<ByteString, FortDeployPokemonResponse.Result>() {

					@Override
					public FortDeployPokemonResponse.Result call(ByteString response) {

						try {
							return FortDeployPokemonResponse.parseFrom(response).getResult();
						} catch (InvalidProtocolBufferException e) {
							throw Exceptions.propagate(e);
						}

					}

				});

	}

	protected PokemonGo getApi() {
		return api;
	}

	/**
	 * Updates this gym's point count by the given delta
	 *
	 * @param delta the amount to change the points by
	 */
	public void updatePoints(int delta) {
		this.points += delta;
	}

	/**
	 * Updates this gym with the given gym state
	 *
	 * @param state the state to update from
	 */
	public void updateState(GymState state) {
		proto = state.getFortData();
		clearDetails();
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Gym && ((Gym) obj).getId().equals(getId());
	}
}
