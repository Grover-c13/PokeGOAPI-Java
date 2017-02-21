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
import POGOProtos.Data.Gym.GymStateOuterClass.GymState;
import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Enums.TeamColorOuterClass;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Networking.Requests.Messages.FortDeployPokemonMessageOuterClass.FortDeployPokemonMessage;
import POGOProtos.Networking.Requests.Messages.GetGymDetailsMessageOuterClass.GetGymDetailsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.FortDeployPokemonResponseOuterClass.FortDeployPokemonResponse;
import POGOProtos.Networking.Responses.GetGymDetailsResponseOuterClass.GetGymDetailsResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.AsyncRemoteServerException;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.InsufficientLevelException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.exceptions.hash.HashException;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.main.PokemonMeta;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.MapPoint;
import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;

public class Gym implements MapPoint {
	private FortData proto;
	private GetGymDetailsResponse details;
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

	public boolean isAttackable() throws LoginFailedException, CaptchaActiveException, RemoteServerException,
			HashException {
		return this.getGymMembers().size() != 0;
	}

	/**
	 * Creates a battle for this gym
	 *
	 * @return the battle object
	 */
	public Battle battle() {
		int minimumPlayerLevel = PokemonMeta.battleSettings.getMinimumPlayerLevel();
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

	private GetGymDetailsResponse details() throws LoginFailedException, CaptchaActiveException,
			RemoteServerException, HashException {
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

	public String getName() throws LoginFailedException, CaptchaActiveException, RemoteServerException, HashException {
		return details().getName();
	}

	public ProtocolStringList getUrlsList() throws LoginFailedException, CaptchaActiveException,
			RemoteServerException, HashException {
		return details().getUrlsList();
	}

	public GetGymDetailsResponse.Result getResult()
			throws LoginFailedException, CaptchaActiveException, RemoteServerException, HashException {
		return details().getResult();
	}

	public boolean inRange() throws LoginFailedException, CaptchaActiveException, RemoteServerException,
			HashException {
		GetGymDetailsResponse.Result result = getResult();
		return (result != GetGymDetailsResponse.Result.ERROR_NOT_IN_RANGE);
	}

	public String getDescription() throws LoginFailedException, CaptchaActiveException, RemoteServerException,
			HashException {
		return details().getDescription();
	}


	public List<GymMembership> getGymMembers()
			throws LoginFailedException, CaptchaActiveException, RemoteServerException, HashException {
		return details().getGymState().getMembershipsList();
	}

	/**
	 * Get a list of pokemon defending this gym.
	 *
	 * @return List of pokemon
	 * @throws LoginFailedException if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 * @throws HashException if there is a problem with the Hash key / Service
	 */
	public List<PokemonData> getDefendingPokemon()
			throws LoginFailedException, CaptchaActiveException, RemoteServerException, HashException {
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
	 * @throws LoginFailedException if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 * @throws HashException if there is a problem with the Hash key / Service
	 */
	public FortDeployPokemonResponse.Result deployPokemon(Pokemon pokemon)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException, HashException {
		FortDeployPokemonMessage reqMsg = FortDeployPokemonMessage.newBuilder()
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
		}

	}

	/**
	 * Deploy pokemon
	 *
	 * @param pokemon The pokemon to deploy
	 * @return Result of attempt to deploy pokemon
	 * @throws LoginFailedException if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public Observable<FortDeployPokemonResponse.Result> deployPokemonAsync(Pokemon pokemon)
			throws RemoteServerException, CaptchaActiveException, LoginFailedException {
		FortDeployPokemonMessage reqMsg = FortDeployPokemonMessage.newBuilder()
				.setFortId(getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setPokemonId(pokemon.getId())
				.build();

		AsyncServerRequest asyncServerRequest = new AsyncServerRequest(RequestType.FORT_DEPLOY_POKEMON, reqMsg);
		return api.getRequestHandler()
				.sendAsyncServerRequests(asyncServerRequest)
				.map(new Func1<ByteString, FortDeployPokemonResponse.Result>() {

					@Override
					public FortDeployPokemonResponse.Result call(ByteString response) {

						try {
							return FortDeployPokemonResponse.parseFrom(response).getResult();
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
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
