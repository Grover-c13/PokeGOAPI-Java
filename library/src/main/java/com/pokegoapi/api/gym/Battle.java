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
import POGOProtos.Data.Gym.GymMembershipOuterClass;
import POGOProtos.Data.PokemonDataOuterClass;
import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Networking.Requests.Messages.AttackGymMessageOuterClass.AttackGymMessage;
import POGOProtos.Networking.Requests.Messages.StartGymBattleMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.StartGymBattleMessageOuterClass.StartGymBattleMessage.Builder;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.AttackGymResponseOuterClass.AttackGymResponse;
import POGOProtos.Networking.Responses.GetGymDetailsResponseOuterClass;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse.Result;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonRequest;
import com.pokegoapi.main.PokemonResponse;
import com.pokegoapi.main.RequestCallback;
import com.pokegoapi.main.Utils;
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
	private List<GymMembershipOuterClass.GymMembership> memberships;

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
	 * @param result callback to return result of the attempt to start
	 */
	public void start(final AsyncReturn<Result> result) {
		final Builder builder = StartGymBattleMessageOuterClass.StartGymBattleMessage.newBuilder();

		for (Pokemon team : teams) {
			builder.addAttackingPokemonIds(team.getId());
		}

		gym.getDetails(new AsyncReturn<GetGymDetailsResponseOuterClass.GetGymDetailsResponse>() {
			@Override
			public void onReceive(GetGymDetailsResponseOuterClass.GetGymDetailsResponse details, Exception exception) {
				gym.getGymMembers(new AsyncReturn<List<GymMembershipOuterClass.GymMembership>>() {
					@Override
					public void onReceive(List<GymMembershipOuterClass.GymMembership> members, Exception exception) {
						if (Utils.callbackException(exception, result, Result.UNRECOGNIZED)) {
							return;
						}
						memberships = members;
						gym.getDefendingPokemon(new AsyncReturn<List<PokemonData>>() {
							@Override
							public void onReceive(List<PokemonData> defenders, Exception exception) {
								if (Utils.callbackException(exception, result, Result.UNRECOGNIZED)) {
									return;
								}
								builder.setGymId(gym.getId());
								builder.setPlayerLongitude(api.getLongitude());
								builder.setPlayerLatitude(api.getLatitude());
								builder.setDefendingPokemonId(defenders.get(0).getId()); // may need to be sorted

								PokemonRequest request = new PokemonRequest(RequestType.START_GYM_BATTLE, builder.build());
								api.getRequestHandler().sendRequest(request, new RequestCallback() {
									@Override
									public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
										if (Utils.callbackException(response, result, Result.UNRECOGNIZED)) {
											return;
										}
										try {
											battleResponse = StartGymBattleResponse.parseFrom(response.getResponseData());
											sendBlankAction(new AsyncReturn<AttackGymResponse>() {
												@Override
												public void onReceive(AttackGymResponse attackResponse, Exception exception) {
													if (Utils.callbackException(exception, result, Result.UNRECOGNIZED)) {
														result.onReceive(Result.UNRECOGNIZED, null);
														return;
													}
													for (BattleAction action : battleResponse.getBattleLog().getBattleActionsList()) {
														gymIndex.add(action.getTargetIndex());
													}
													result.onReceive(battleResponse.getResult(), null);
												}
											});
										} catch (InvalidProtocolBufferException e) {
											result.onReceive(null, new RemoteServerException(e));
										}
									}
								});
							}
						});
					}
				});
			}
		});
	}

	/**
	 * Attacks the given amount of times
	 * @param times the times to attack this gym
	 * @param result the result from the attack
	 */
	public void attack(int times, AsyncReturn<AttackGymResponse> result) {
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

		performActions(actions, result);
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
	private PokemonDataOuterClass.PokemonData getDefender(int index) {
		return memberships.get(index).getPokemonData();
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
	 * @param result callback to return result
	 */
	private void sendBlankAction(final AsyncReturn<AttackGymResponse> result) {
		AttackGymMessage message = AttackGymMessage
				.newBuilder()
				.setGymId(gym.getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setBattleId(battleResponse.getBattleId())
				.build();

		PokemonRequest request = new PokemonRequest(RequestType.ATTACK_GYM, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, null)) {
					return;
				}
				try {
					result.onReceive(AttackGymResponse.parseFrom(response.getResponseData()), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Perfoms the given actions
	 * @param actions the actions to perform
	 * @param result async callback for this result
	 */
	private void performActions(List<BattleAction> actions, final AsyncReturn<AttackGymResponse> result) {
		AttackGymMessage.Builder message = AttackGymMessage
				.newBuilder()
				.setGymId(gym.getId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setBattleId(battleResponse.getBattleId());

		for (BattleAction action : actions) {
			message.addAttackActions(action);
		}

		PokemonRequest request = new PokemonRequest(RequestType.ATTACK_GYM, message.build());
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, null)) {
					return;
				}
				try {
					AttackGymResponse messageResponse = AttackGymResponse.parseFrom(response.getResponseData());

					if (messageResponse.getBattleLog().getState() == BattleState.DEFEATED
							|| messageResponse.getBattleLog().getState() == BattleState.VICTORY
							|| messageResponse.getBattleLog().getState() == BattleState.TIMED_OUT) {
						concluded = true;
					}

					outcome = messageResponse.getBattleLog().getState();

					result.onReceive(messageResponse, null);
				} catch (Exception e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}
}
