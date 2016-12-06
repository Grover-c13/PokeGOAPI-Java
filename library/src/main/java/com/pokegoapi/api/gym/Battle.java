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

import POGOProtos.Data.Battle.BattleActionOuterClass.BattleAction;
import POGOProtos.Data.Battle.BattleActionTypeOuterClass;
import POGOProtos.Data.Battle.BattlePokemonInfoOuterClass.BattlePokemonInfo;
import POGOProtos.Data.Battle.BattleStateOuterClass.BattleState;
import POGOProtos.Data.PokemonDataOuterClass;
import POGOProtos.Networking.Requests.Messages.AttackGymMessageOuterClass.AttackGymMessage;
import POGOProtos.Networking.Requests.Messages.StartGymBattleMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.StartGymBattleMessageOuterClass.StartGymBattleMessage.Builder;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.AttackGymResponseOuterClass.AttackGymResponse;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse.Result;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Battle {
	private final Gym gym;
	private final Pokemon[] teams;
	private final List<BattlePokemonInfo> bteam = new ArrayList<>();
	private StartGymBattleResponse battleResponse;
	private final PokemonGo api;
	private final List<Integer> gymIndex = new ArrayList<>();
	@Getter
	private boolean concluded;
	@Getter
	private BattleState outcome;

	/**
	 * New battle to track the state of a battle.
	 *
	 * @param api The api instance to submit requests with.
	 * @param teams The Pokemon to use for attacking in the battle.
	 * @param gym The Gym to fight at.
	 */
	public Battle(PokemonGo api, Pokemon[] teams, Gym gym) {
		this.teams = teams;
		this.gym = gym;
		this.api = api;

		for (Pokemon team : teams) {
			bteam.add(this.createBattlePokemon(team));
		}
	}

	/**
	 * Start a battle.
	 *
	 * @return Result of the attempt to start
	 * @throws LoginFailedException if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public Result start() throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		Builder builder = StartGymBattleMessageOuterClass.StartGymBattleMessage.newBuilder();

		for (Pokemon team : teams) {
			builder.addAttackingPokemonIds(team.getId());
		}


		List<PokemonDataOuterClass.PokemonData> defenders = gym.getDefendingPokemon();
		builder.setGymId(gym.getId());
		builder.setPlayerLongitude(api.getLongitude());
		builder.setPlayerLatitude(api.getLatitude());
		builder.setDefendingPokemonId(defenders.get(0).getId()); // may need to be sorted

		ServerRequest serverRequest = new ServerRequest(RequestType.START_GYM_BATTLE, builder.build());
		api.getRequestHandler().sendServerRequests(serverRequest);


		try {
			battleResponse = StartGymBattleResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException();
		}

		// need to send blank action
		this.sendBlankAction();

		for (BattleAction action : battleResponse.getBattleLog().getBattleActionsList()) {
			gymIndex.add(action.getTargetIndex());
		}

		return battleResponse.getResult();
	}


	/**
	 * Attack a gym.
	 *
	 * @param times the amount of times to attack
	 * @return Battle
	 * @throws LoginFailedException if the login failed
	 * @throws RemoteServerException When a buffer exception is thrown
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 */
	public AttackGymResponse attack(int times)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {

		ArrayList<BattleAction> actions = new ArrayList<>();

		for (int i = 0; i < times; i++) {
			BattleAction action = BattleAction
					.newBuilder()
					.setType(BattleActionTypeOuterClass.BattleActionType.ACTION_ATTACK)
					.setActionStartMs(api.currentTimeMillis() + (100 * times))
					.setDurationMs(500)
					.setTargetIndex(-1)
					.build();
			actions.add(action);
		}

		return doActions(actions);
	}


	/**
	 * Creates a battle pokemon object to send with the request.
	 *
	 * @param pokemon the battle pokemon
	 * @return BattlePokemonInfo
	 */
	private BattlePokemonInfo createBattlePokemon(Pokemon pokemon) {
		return BattlePokemonInfo
				.newBuilder()
				.setCurrentEnergy(0)
				.setCurrentHealth(100)
				.setPokemonData(pokemon.getDefaultInstanceForType())
				.build();
	}

	/**
	 * Get the Pokemondata for the defenders.
	 *
	 * @param index of defender(0 to gym lever)
	 * @return Battle
	 */
	private PokemonDataOuterClass.PokemonData getDefender(int index)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		return gym.getGymMembers().get(0).getPokemonData();
	}

	/**
	 * Get the last action from server.
	 *
	 * @return BattleAction
	 */
	private BattleAction getLastActionFromServer() {
		BattleAction action;
		int actionCount = battleResponse.getBattleLog().getBattleActionsCount();
		action = battleResponse.getBattleLog().getBattleActions(actionCount - 1);
		return action;
	}

	/**
	 * Send blank action, used for polling the state of the battle. (i think).
	 *
	 * @return AttackGymResponse
	 */
	private AttackGymResponse sendBlankAction()
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		AttackGymMessage message = AttackGymMessage
				.newBuilder()
				.setGymId(gym.getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setBattleId(battleResponse.getBattleId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.ATTACK_GYM, message);
		api.getRequestHandler().sendServerRequests(serverRequest);


		try {
			return AttackGymResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException();
		}
	}


	/**
	 * Do Actions in battle.
	 *
	 * @param actions list of actions to send in this request
	 * @return AttackGymResponse
	 */
	private AttackGymResponse doActions(List<BattleAction> actions)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {


		AttackGymMessage.Builder message = AttackGymMessage
				.newBuilder()
				.setGymId(gym.getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setBattleId(battleResponse.getBattleId());

		for (BattleAction action : actions) {
			message.addAttackActions(action);
		}


		ServerRequest serverRequest = new ServerRequest(RequestType.ATTACK_GYM, message.build());
		api.getRequestHandler().sendServerRequests(serverRequest);


		try {
			AttackGymResponse response = AttackGymResponse.parseFrom(serverRequest.getData());

			if (response.getBattleLog().getState() == BattleState.DEFEATED
					|| response.getBattleLog().getState() == BattleState.VICTORY
					|| response.getBattleLog().getState() == BattleState.TIMED_OUT) {
				concluded = true;
			}

			outcome = response.getBattleLog().getState();


			return response;
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException();
		}

	}

}
