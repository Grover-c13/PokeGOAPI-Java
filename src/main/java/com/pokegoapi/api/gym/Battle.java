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
import POGOProtos.Data.Battle.BattleParticipantOuterClass.BattleParticipant;
import POGOProtos.Data.Battle.BattlePokemonInfoOuterClass.BattlePokemonInfo;
import POGOProtos.Data.PokemonDataOuterClass;
import POGOProtos.Networking.Requests.Messages.AttackGymMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.StartGymBattleMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.StartGymBattleMessageOuterClass.StartGymBattleMessage.Builder;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.AttackGymResponseOuterClass.AttackGymResponse;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse.Result;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;

import java.util.ArrayList;
import java.util.List;

public class Battle {
	private Gym gym;
	private Pokemon[] team;
	private List<BattlePokemonInfo> bteam;
	private StartGymBattleResponse battleResponse;
	private PokemonGo api;
	private int active_index;
	private Pokemon active;
	private int defeated;

	public Battle(PokemonGo api, Pokemon[] team, Gym gym) {
		this.team = team;
		this.gym = gym;
		this.api = api;
		this.active = team[0];
		this.defeated = 0;
		this.active_index = 0;

		this.bteam = new ArrayList<BattlePokemonInfo>();
		for (int i = 0; i < team.length; i++) {
			bteam.add(this.createBattlePokemon(team[i]));
		}

	}

	public Result start() throws LoginFailedException, RemoteServerException {

		Builder builder = StartGymBattleMessageOuterClass.StartGymBattleMessage.newBuilder();

		for (int i = 0; i < team.length; i++) {
			builder.addAttackingPokemonIds(team[i].getId());
		}


		List<PokemonDataOuterClass.PokemonData> defenders = gym.getDefendingPokemon();
		builder.setGymId(gym.getId());
		builder.setPlayerLongitude(api.getLongitude());
		builder.setPlayerLatitude(api.getLatitude());
		builder.setDefendingPokemonId(defenders.get(0).getId()); // may need to be sorted

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.START_GYM_BATTLE, builder.build());
		api.getRequestHandler().sendServerRequests(serverRequest);


		try {
			battleResponse = StartGymBattleResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException();
		}

		return battleResponse.getResult();
	}


	public AttackGymResponse.Result join() throws LoginFailedException, RemoteServerException {
		BattleParticipant participant = BattleParticipant
										.newBuilder()
										.setActivePokemon(bteam.get(0))
										.addAllReversePokemon(bteam)
										.build();

		BattleAction action = BattleAction
								.newBuilder()
								.setType(BattleActionTypeOuterClass.BattleActionType.ACTION_PLAYER_JOIN)
								.setActionStartMs(System.currentTimeMillis())
								.setAttackerIndex(0)
								.setActivePokemonId(active.getId())
								.setPlayerJoined(participant)
								.build();

		AttackGymResponse response = doAction(action);
		System.out.println(response);
		return response.getResult();

	}

	public void attack() throws LoginFailedException, RemoteServerException {
		// TODO: need meta information about attack durations, can be extracted from game master dump
		BattleAction action = BattleAction
								.newBuilder()
								.setType(BattleActionTypeOuterClass.BattleActionType.ACTION_ATTACK)
								.setActionStartMs(System.currentTimeMillis())
								.setDurationMs(3)
								.setEnergyDelta(0)
								.setAttackerIndex(active_index)
								.setTargetIndex(defeated)
								.setActivePokemonId(active.getId())
								.setTargetPokemonId(getDefender(defeated).getId())
								.setDamageWindowsStartTimestampMss(System.currentTimeMillis()+1000)
								.setDamageWindowsEndTimestampMss(System.currentTimeMillis()+2000)
								.build();
		doAction(action);
	}

	public void dodge() {

	}

	public void swap(Pokemon pokemon) {

	}

	private BattlePokemonInfo createBattlePokemon(Pokemon pokemon) {
		BattlePokemonInfo info = BattlePokemonInfo
									.newBuilder()
									.setCurrentEnergy(0)
									.setCurrentHealth(100)
									.setPokemonData(pokemon.getDefaultInstanceForType())
									.build();
		return info;
	}

	private PokemonDataOuterClass.PokemonData getDefender(int index) throws LoginFailedException, RemoteServerException {
		return gym.getGymMembers().get(0).getPokemonData();
	}

	private BattleAction getLastActionFromServer() {
		BattleAction action;
		int actionCount = battleResponse.getBattleLog().getBattleActionsCount();
		action = battleResponse.getBattleLog().getBattleActions(actionCount-1);
		return action;
	}

	private AttackGymResponse doAction(BattleAction action) throws LoginFailedException, RemoteServerException {
		AttackGymMessageOuterClass.AttackGymMessage message = AttackGymMessageOuterClass.AttackGymMessage
									.newBuilder()
									.addAttackActions(action)
									.setGymId(gym.getId())
									.setPlayerLatitude(api.getLatitude())
									.setPlayerLongitude(api.getLongitude())
									.setLastRetrievedActions(action)
									.setBattleId(battleResponse.getBattleId())
									.build();

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.ATTACK_GYM, message);
		api.getRequestHandler().sendServerRequests(serverRequest);


		try {
			return AttackGymResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException();
		}

	}

}
