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
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.Getter;
import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;

/**
 * Battle instance
 * TODO: Update this class with upstream version
 */
public class Battle {
	private final Networking networking;
	private final Location location;
	private final Gym gym;
	private final GymDetails gymDetails;
	private final Pokemon[] team;
	private final List<BattlePokemonInfo> bteam;
	private final List<Integer> gymIndex;
	@Getter
	private String battleId;
	@Getter
	private boolean concluded;
	@Getter
	private BattleState outcome;

	/**
	 * New battle to track the state of a battle.
	 *
	 * @param networking Networking, for all actions on pokemon
	 * @param location   Current location of the user
	 * @param gym        The Gym to fight at.
	 * @param gymDetails The Gym to fight at, detailed information
	 * @param team       The Pokemon to use for attacking in the battle.
	 */
	public Battle(Networking networking, Location location, Gym gym, GymDetails gymDetails, Pokemon... team) {
		this.networking = networking;
		this.location = location;
		this.team = team;
		this.gym = gym;
		this.gymDetails = gymDetails;

		this.bteam = new ArrayList<>();
		this.gymIndex = new ArrayList<>();

		for (Pokemon aTeam : team) {
			bteam.add(this.createBattlePokemon(aTeam));
		}
	}

	/**
	 * Start a battle.
	 *
	 * @return Result of the attempt to start
	 */
	public Observable<Result> start() {

		Builder builder = StartGymBattleMessageOuterClass.StartGymBattleMessage.newBuilder();

		for (Pokemon aTeam : team) {
			builder.addAttackingPokemonIds(aTeam.getId());
		}

		List<PokemonDataOuterClass.PokemonData> defenders = gymDetails.getDefendingPokemon();
		builder.setGymId(gym.getId());
		builder.setPlayerLongitude(location.getLongitude());
		builder.setPlayerLatitude(location.getLatitude());
		builder.setDefendingPokemonId(defenders.get(0).getId()); // may need to be sorted
		return networking.queueRequest(RequestType.START_GYM_BATTLE, builder.build(), StartGymBattleResponse.class)
				.map(new Func1<StartGymBattleResponse, Result>() {
					@Override
					public Result call(StartGymBattleResponse battleResponse) {
						battleId = battleResponse.getBattleId();
						// need to send blank action
						sendBlankAction();
						for (BattleAction action : battleResponse.getBattleLog().getBattleActionsList()) {
							gymIndex.add(action.getTargetIndex());
						}
						return battleResponse.getResult();
					}
				});
	}


	/**
	 * Attack a gym.
	 *
	 * @param times the amount of times to attack
	 * @return Battle
	 */
	public Observable<AttackGymResponse> attack(int times) {
		List<BattleAction> actions = new ArrayList<>();
		for (int i = 0; i < times; i++) {
			BattleAction action = BattleAction
					.newBuilder()
					.setType(BattleActionTypeOuterClass.BattleActionType.ACTION_ATTACK)
					.setActionStartMs(System.currentTimeMillis() + (100 * times))
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
	 * @param pokemon Pokemon to battle with
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
	 * Send blank action, used for polling the state of the battle. (i think).
	 *
	 * @return AttackGymResponse
	 */
	private Observable<AttackGymResponse> sendBlankAction() {
		return networking.queueRequest(RequestType.ATTACK_GYM, AttackGymMessage
				.newBuilder()
				.setGymId(gym.getId())
				.setPlayerLatitude(location.getLatitude())
				.setPlayerLongitude(location.getLongitude())
				.setBattleId(battleId)
				.build(), AttackGymResponse.class);
	}


	/**
	 * Do Actions in battle.
	 *
	 * @param actions list of actions to send in this request
	 * @return AttackGymResponse
	 */
	private Observable<AttackGymResponse> doActions(List<BattleAction> actions)
			throws LoginFailedException, RemoteServerException {
		AttackGymMessage.Builder message = AttackGymMessage
				.newBuilder()
				.setGymId(gym.getId())
				.setPlayerLatitude(location.getLatitude())
				.setPlayerLongitude(location.getLongitude())
				.setBattleId(battleId);

		for (BattleAction action : actions) {
			message.addAttackActions(action);
		}
		return networking.queueRequest(RequestType.ATTACK_GYM, message.build(), AttackGymResponse.class)
				.map(new Func1<AttackGymResponse, AttackGymResponse>() {
					@Override
					public AttackGymResponse call(AttackGymResponse response) {
						if (response.getBattleLog().getState() == BattleState.DEFEATED
								|| response.getBattleLog().getState() == BattleState.VICTORY
								|| response.getBattleLog().getState() == BattleState.TIMED_OUT) {
							concluded = true;
						}

						outcome = response.getBattleLog().getState();
						return response;
					}
				});
	}

}
