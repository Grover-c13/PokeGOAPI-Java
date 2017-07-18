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
import POGOProtos.Data.Battle.BattleActionTypeOuterClass.BattleActionType;
import POGOProtos.Data.Battle.BattleLogOuterClass.BattleLog;
import POGOProtos.Data.Battle.BattleParticipantOuterClass.BattleParticipant;
import POGOProtos.Data.Battle.BattlePokemonInfoOuterClass.BattlePokemonInfo;
import POGOProtos.Data.Battle.BattleResultsOuterClass.BattleResults;
import POGOProtos.Data.Battle.BattleStateOuterClass.BattleState;
import POGOProtos.Data.Battle.BattleTypeOuterClass.BattleType;
import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import POGOProtos.Networking.Requests.Messages.AttackGymMessageOuterClass.AttackGymMessage;
import POGOProtos.Networking.Requests.Messages.StartGymBattleMessageOuterClass.StartGymBattleMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.AttackGymResponseOuterClass.AttackGymResponse;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse;
import POGOProtos.Settings.Master.MoveSettingsOuterClass;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

public class Battle {
	private final PokemonGo api;

	@Getter
	private final Gym gym;

	@Getter
	private Pokemon[] team;

	@Getter
	private String battleId;
	@Getter
	private BattleParticipant attacker;
	@Getter
	private BattleParticipant defender;

	@Getter
	private BattleState battleState;

	@Getter
	private boolean active;

	@Getter
	private long serverTimeOffset;

	private Queue<ServerAction> serverActionQueue
			= new PriorityBlockingQueue<>(11, new Comparator<ServerAction>() {
				@Override
				public int compare(ServerAction o1, ServerAction o2) {
					return Long.compare(o1.getStart(), o2.getStart());
				}
			});

	private Set<ServerAction> activeActions = new HashSet<>();
	private Set<ServerAction> damagingActions = new HashSet<>();

	@Getter
	private Map<String, BattleParticipant> participants = new HashMap<>();
	private Map<Integer, BattleParticipant> participantIndices = new HashMap<>();

	private Map<BattleParticipant, BattlePokemon> activePokemon = new HashMap<>();

	private Queue<ClientAction> queuedActions = new LinkedBlockingDeque<>();

	@Getter
	private BattlePokemon activeDefender;
	@Getter
	private BattlePokemon activeAttacker;

	@Getter
	private long startTime;
	@Getter
	private long endTime;

	@Getter
	private BattleType battleType;

	private long lastSendTime;
	private long lastServerTime;

	private BattleAction lastRetrievedAction;

	private Set<Long> faintedPokemon = new HashSet<>();

	private boolean sentActions;

	@Getter
	private BattleResults results;

	private int defenderIndex = 0;
	private int defenderCount;

	private int gymPointsDelta;

	private Set<ServerAction> handledActions = new HashSet<>();

	public Battle(PokemonGo api, Gym gym) {
		this.api = api;
		this.gym = gym;
	}

	/**
	 * Starts this battle
	 *
	 * @param handler to handle this battle
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void start(final BattleHandler handler) throws RequestFailedException {
		battleId = null;
		participantIndices.clear();
		participants.clear();
		activePokemon.clear();
		serverActionQueue.clear();
		activeActions.clear();
		serverTimeOffset = 0;
		active = false;
		team = handler.createTeam(api, this);
		faintedPokemon.clear();
		defenderIndex = 0;
		defenderCount = gym.getDefendingPokemon().size();
		gymPointsDelta = 0;
		handledActions.clear();

		Thread updateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (active || battleId == null) {
					if (battleId != null) {
						updateBattle(handler);
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						active = false;
					}
				}
			}
		});
		updateThread.setDaemon(true);
		updateThread.setName("Gym Battle Update Thread");
		updateThread.start();

		beginDefenderBattle(handler);
	}

	/**
	 * Starts this battle with an individual defender
	 *
	 * @param handler to handle this battle
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	private void beginDefenderBattle(final BattleHandler handler)
			throws RequestFailedException {
		lastRetrievedAction = null;
		queuedActions.clear();
		battleState = BattleState.STATE_UNSET;
		lastServerTime = api.currentTimeMillis();
		lastSendTime = lastServerTime;
		sentActions = false;

		List<Pokemon> attackers = new ArrayList<>();

		for (Pokemon pokemon : team) {
			if (!faintedPokemon.contains(pokemon.getId())) {
				attackers.add(pokemon);
			}
		}

		if (attackers.size() > 0 && defenderIndex < defenderCount) {
			StartGymBattleMessage.Builder builder = StartGymBattleMessage.newBuilder()
					.setPlayerLatitude(api.getLatitude())
					.setPlayerLongitude(api.getLongitude())
					.setGymId(gym.getId())
					.setDefendingPokemonId(gym.getDefendingPokemon().get(defenderIndex).getPokemon().getId());
			for (Pokemon pokemon : attackers) {
				builder.addAttackingPokemonIds(pokemon.getId());
				if (pokemon.getStamina() < pokemon.getMaxStamina()) {
					throw new IllegalArgumentException("Pokemon must have full stamina to battle in a gym!");
				} else {
					String deployedFortId = pokemon.getDeployedFortId();
					if (pokemon.getFromFort() && deployedFortId != null && deployedFortId.length() > 0) {
						throw new IllegalArgumentException("Cannot deploy Pokemon that is already in a gym!");
					}
				}
			}
			try {
				StartGymBattleMessage message = builder.build();
				ServerRequest request = new ServerRequest(RequestType.START_GYM_BATTLE, message);

				api.getRequestHandler().sendServerRequests(request, true);
				StartGymBattleResponse response = StartGymBattleResponse.parseFrom(request.getData());

				if (response.getResult() == StartGymBattleResponse.Result.SUCCESS) {
					battleId = response.getBattleId();
					attacker = response.getAttacker();
					defender = response.getDefender();

					activeDefender = new BattlePokemon(defender.getActivePokemon());
					activeAttacker = new BattlePokemon(attacker.getActivePokemon());

					updateLog(handler, response.getBattleLog());
				}

				sendActions(handler);

				handler.onStart(api, this, response.getResult());
			} catch (InvalidProtocolBufferException e) {
				battleId = "";
				throw new RequestFailedException(e);
			}
		} else {
			active = false;
		}
	}

	/**
	 * Performs a tick for this battle
	 *
	 * @param handler to handle this battle
	 */
	private void updateBattle(BattleHandler handler) {
		long time = api.currentTimeMillis();
		while (serverActionQueue.size() > 0) {
			ServerAction action = serverActionQueue.element();
			if (time >= action.getStart()) {
				handler.onActionStart(api, this, action);
				activeActions.add(serverActionQueue.remove());
				handleAction(handler, action);
			} else {
				break;
			}
		}
		Set<ServerAction> completedActions = new HashSet<>();
		for (ServerAction action : activeActions) {
			if (time >= action.getEnd()) {
				handler.onActionEnd(api, this, action);
				completedActions.add(action);
			} else {
				if (damagingActions.contains(action)) {
					if (time > action.getDamageWindowEnd()) {
						handler.onDamageEnd(api, this, action);
						damagingActions.remove(action);
					}
				} else {
					if (time > action.getDamageWindowStart()) {
						damagingActions.add(action);
						handler.onDamageStart(api, this, action);
					}
				}
			}
		}
		activeActions.removeAll(completedActions);
		boolean nextDefender = false;
		if (active && !queuedActions.isEmpty()) {
			try {
				nextDefender = sendActions(handler);
			} catch (Exception e) {
				handler.onException(api, this, e);
			}
			lastSendTime = time;
		}
		if (nextDefender) {
			defenderIndex++;
			try {
				beginDefenderBattle(handler);
				Thread.sleep(1500);
			} catch (Exception e) {
				handler.onException(api, this, e);
			}
		}
	}

	/**
	 * Updates this battle with the given log
	 *
	 * @param handler to handle this battle
	 * @param log the log to update with
	 * @return if this battle should move on to the next defender
	 */
	private boolean updateLog(BattleHandler handler, BattleLog log) {
		serverTimeOffset = log.getServerMs() - api.currentTimeMillis();
		battleType = log.getBattleType();
		startTime = log.getBattleStartTimestampMs();
		endTime = log.getBattleEndTimestampMs();
		if (log.getBattleActionsCount() > 0) {
			long latestTime = Long.MIN_VALUE;
			for (BattleAction action : log.getBattleActionsList()) {
				if (action.getActionStartMs() > latestTime) {
					lastRetrievedAction = action;
					latestTime = action.getActionStartMs();
				}
			}
		}
		results = null;
		for (BattleAction action : log.getBattleActionsList()) {
			BattleResults results = action.getBattleResults();
			if (results.hasGymState()) {
				this.results = action.getBattleResults();
			}
		}
		if (results != null) {
			gym.updatePoints(results.getGymPointsDelta());
			gymPointsDelta += results.getGymPointsDelta();
		}
		BattleState state = log.getState();
		active = defenderIndex < defenderCount && !(state == BattleState.TIMED_OUT || state == BattleState
				.STATE_UNSET);
		if (state != battleState) {
			switch (state) {
				case TIMED_OUT:
					gym.clearDetails();
					handler.onTimedOut(api, this);
					break;
				case DEFEATED:
					gym.clearDetails();
					handler.onDefeated(api, this);
					break;
				case VICTORY:
					if (!active) {
						gym.updateState(results.getGymState());
						handler.onVictory(api, this, gymPointsDelta, gym.getPoints());
					}
					break;
				default:
					break;
			}
			if (!active) {
				try {
					api.getInventories().updateInventories(true);
				} catch (Exception e) {
					handler.onException(api, this, e);
				}
			}
			battleState = state;
		}
		for (BattleAction action : log.getBattleActionsList()) {
			ServerAction serverAction = new ServerAction(action);
			if (!handledActions.contains(serverAction)) {
				serverActionQueue.add(serverAction);
				handledActions.add(serverAction);
			}
		}
		lastServerTime = log.getServerMs();
		return battleState != BattleState.ACTIVE && battleState != BattleState.STATE_UNSET
				&& battleState != BattleState.TIMED_OUT;
	}

	/**
	 * Handles an action from the server
	 *
	 * @param handler to handle this battle
	 * @param action the action being handled
	 */
	private void handleAction(BattleHandler handler, ServerAction action) {
		switch (action.getType()) {
			case ACTION_PLAYER_JOIN:
				onPlayerJoin(handler, action);
				break;
			case ACTION_PLAYER_QUIT:
				onPlayerQuit(handler, action);
				break;
			case ACTION_ATTACK:
				handleAttack(handler, action);
				break;
			case ACTION_DODGE:
				handleDodge(handler, action);
				break;
			case ACTION_FAINT:
				handleFaint(handler, action);
				break;
			case ACTION_SPECIAL_ATTACK:
				handleSpecialAttack(handler, action);
				break;
			default:
				break;
		}
	}

	/**
	 * Handles a player join action
	 *
	 * @param handler to handle this battle
	 * @param action the join action
	 */
	private void onPlayerJoin(BattleHandler handler, ServerAction action) {
		BattleParticipant joined = action.getJoined();
		String name = joined.getTrainerPublicProfile().getName();
		participants.put(name, joined);
		participantIndices.put(action.getTargetIndex(), joined);
		activePokemon.put(joined, new BattlePokemon(joined.getActivePokemon()));
		handler.onPlayerJoin(api, this, joined, action);
	}

	/**
	 * Handles a player quit action
	 *
	 * @param handler to handle this battle
	 * @param action the quit action
	 */
	private void onPlayerQuit(BattleHandler handler, ServerAction action) {
		BattleParticipant left = action.getLeft();
		String name = left.getTrainerPublicProfile().getName();
		BattleParticipant remove = participants.remove(name);
		participantIndices.remove(action.getTargetIndex());
		activePokemon.remove(remove);
		handler.onPlayerLeave(api, this, left, action);
	}

	/**
	 * Handles an attack action
	 *
	 * @param handler to handle this battle
	 * @param action the attack action
	 */
	private void handleAttack(BattleHandler handler, ServerAction action) {
		BattlePokemon attacked = getActivePokemon(action.getTargetIndex(), true);
		BattlePokemon attacker = getActivePokemon(action.getAttackerIndex(), false);
		if (action.getAttackerIndex() == 0) {
			attacker = activeAttacker;
		}

		long damageWindowStart = action.getDamageWindowStart();
		long damageWindowEnd = action.getDamageWindowEnd();
		int duration = action.getDuration();

		handler.onAttacked(api, this, attacked, attacker, duration, damageWindowStart, damageWindowEnd, action);
	}

	/**
	 * Handles a special attack action
	 *
	 * @param handler to handle this battle
	 * @param action the attack action
	 */
	private void handleSpecialAttack(BattleHandler handler, ServerAction action) {
		BattlePokemon attacked = getActivePokemon(action.getTargetIndex(), false);
		BattlePokemon attacker = getActivePokemon(action.getAttackerIndex(), true);
		if (action.getAttackerIndex() == 0) {
			attacker = activeAttacker;
		}

		long damageWindowStart = action.getDamageWindowStart();
		long damageWindowEnd = action.getDamageWindowEnd();
		int duration = action.getDuration();

		handler.onAttackedSpecial(api, this, attacked, attacker, duration, damageWindowStart, damageWindowEnd, action);
	}

	/**
	 * Handles a faint action
	 *
	 * @param handler to handle this battle
	 * @param action the faint action
	 */
	private void handleFaint(BattleHandler handler, ServerAction action) {
		BattlePokemon pokemon = getActivePokemon(action.getAttackerIndex(), true);
		if (action.getAttackerIndex() == 0) {
			pokemon = activeAttacker;
		}

		int duration = action.getDuration();
		handler.onFaint(api, this, pokemon, duration, action);

		faintedPokemon.add(pokemon.getPokemon().getId());
	}

	/**
	 * Handles a dodge action
	 *
	 * @param handler to handle this battle
	 * @param action the dodge action
	 */
	private void handleDodge(BattleHandler handler, ServerAction action) {
		BattlePokemon pokemon = getActivePokemon(action.getAttackerIndex(), true);
		if (action.getAttackerIndex() == 0) {
			pokemon = activeAttacker;
		}

		int duration = action.getDuration();
		handler.onDodge(api, this, pokemon, duration, action);
	}

	/**
	 * Converts the client time to the server time based on serverTimeOffset
	 *
	 * @param clientTime the client time to convert
	 * @return the converted time
	 */
	public long toServerTime(long clientTime) {
		return clientTime + serverTimeOffset;
	}

	/**
	 * Converts the server time to the client time based on serverTimeOffset
	 *
	 * @param serverTime the server time to convert
	 * @return the converted time
	 */
	public long toClientTime(long serverTime) {
		return serverTime - serverTimeOffset;
	}

	/**
	 * Sends all currently queued actions to the server
	 *
	 * @param handler to handle this battle
	 * @return if this battle should switch to the next defender
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	private boolean sendActions(BattleHandler handler)
			throws RequestFailedException {
		AttackGymMessage.Builder builder = AttackGymMessage.newBuilder()
				.setGymId(gym.getId())
				.setBattleId(battleId)
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude());
		while (queuedActions.size() > 0) {
			ClientAction action = queuedActions.element();
			if (action.getEndTime() < lastSendTime) {
				queuedActions.remove();
				long activePokemon = activeAttacker.getPokemon().getId();
				if (action.getPokemon() != null) {
					activePokemon = action.getPokemon().getId();
				}
				long start = action.getStartTime();
				BattleAction.Builder actionBuilder = BattleAction.newBuilder()
						.setActionStartMs(start)
						.setDurationMs(action.getDuration())
						.setTargetIndex(-1)
						.setActivePokemonId(activePokemon)
						.setType(action.getType());
				if (action.isHasDamageWindow()) {
					long damageWindowsStart = start + action.getDamageWindowStart();
					long damageWindowEnd = start + action.getDamageWindowEnd();
					actionBuilder.setDamageWindowsStartTimestampMs(damageWindowsStart);
					actionBuilder.setDamageWindowsEndTimestampMs(damageWindowEnd);
				}
				builder.addAttackActions(actionBuilder.build());
			} else {
				break;
			}
		}
		if (lastRetrievedAction != null && sentActions) {
			builder.setLastRetrievedAction(lastRetrievedAction);
		}
		if (builder.getAttackActionsCount() > 0) {
			AttackGymMessage message = builder.build();
			ServerRequest request = new ServerRequest(RequestType.ATTACK_GYM, message);
			api.getRequestHandler().sendServerRequests(request, true);
			boolean nextDefender;
			try {
				AttackGymResponse response = AttackGymResponse.parseFrom(request.getData());
				nextDefender = handleAttackResponse(handler, response);
			} catch (InvalidProtocolBufferException e) {
				throw new RequestFailedException(e);
			}
			sentActions = true;
			return nextDefender;
		}
		return false;
	}

	/**
	 * Handles the response from an AttackGymMessage
	 *
	 * @param handler to handle this battle
	 * @param response the response to handle
	 * @return if this battle should move on to the next defender
	 */
	private boolean handleAttackResponse(BattleHandler handler, AttackGymResponse response) {
		if (response.getResult() == AttackGymResponse.Result.SUCCESS) {
			final BattlePokemon lastDefender = activeDefender;
			final BattlePokemon lastAttacker = activeAttacker;

			activeAttacker = new BattlePokemon(response.getActiveAttacker());
			activeDefender = new BattlePokemon(response.getActiveDefender());

			if (lastAttacker == null || lastAttacker.getPokemon().getId() != activeAttacker.getPokemon().getId()) {
				handler.onAttackerSwap(api, this, activeAttacker);
			}

			if (lastDefender == null || lastDefender.getPokemon().getId() != activeDefender.getPokemon().getId()) {
				handler.onDefenderSwap(api, this, activeDefender);
			}

			int lastAttackerHealth = lastAttacker.getHealth();
			int lastDefenderHealth = lastDefender.getHealth();
			int attackerHealth = activeAttacker.getHealth();
			int defenderHealth = activeDefender.getHealth();
			int attackerMaxHealth = activeAttacker.getMaxHealth();
			int defenderMaxHealth = activeDefender.getMaxHealth();
			handler.onAttackerHealthUpdate(api, this, lastAttackerHealth, attackerHealth, attackerMaxHealth);
			handler.onDefenderHealthUpdate(api, this, lastDefenderHealth, defenderHealth, defenderMaxHealth);

			BattleLog log = response.getBattleLog();
			return updateLog(handler, log);
		} else if (response.getResult() == AttackGymResponse.Result.ERROR_INVALID_ATTACK_ACTIONS) {
			handler.onInvalidActions(api, this);
		}
		return false;
	}

	/**
	 * Gets the currently active pokemon for the given BattleParticipant
	 *
	 * @param participant the participant
	 * @return the active pokemon
	 */
	public BattlePokemon getActivePokemon(BattleParticipant participant) {
		return activePokemon.get(participant);
	}

	/**
	 * Gets the currently active pokemon for the given participant name
	 *
	 * @param participantName the participant's name
	 * @return the active pokemon
	 */
	public BattlePokemon getActivePokemon(String participantName) {
		BattleParticipant participant = participants.get(participantName);
		if (participant != null) {
			return activePokemon.get(participant);
		}
		return null;
	}

	/**
	 * Gets the currently active pokemon for the given participant index
	 *
	 * @param index the participant index
	 * @param attacker if the index is that of the attacker
	 * @return the active pokemon
	 */
	public BattlePokemon getActivePokemon(int index, boolean attacker) {
		if (attacker || index != -1) {
			BattleParticipant participant = getParticipant(index);
			if (participant != null) {
				return activePokemon.get(participant);
			}
		} else {
			return activeDefender;
		}
		return null;
	}

	/**
	 * Gets the participant for the given index
	 *
	 * @param index the index to get a participant at
	 * @return the participant for
	 */
	public BattleParticipant getParticipant(int index) {
		return participantIndices.get(index);
	}

	/**
	 * Performs an action with the given duration
	 *
	 * @param type the action to perform
	 * @param duration the duration of this action
	 * @return the action performed
	 */
	public ClientAction performAction(BattleActionType type, int duration) {
		ClientAction action = new ClientAction(type, api.currentTimeMillis(), duration);
		queuedActions.add(action);
		return action;
	}

	/**
	 * Performs an attack action
	 *
	 * @return the duration of this attack
	 */
	public int attack() {
		PokemonData pokemon = activeAttacker.getPokemon();
		PokemonMove move = pokemon.getMove1();
		MoveSettingsOuterClass.MoveSettings moveSettings = api.getItemTemplates().getMoveSettings(move);
		int duration = moveSettings.getDurationMs();
		long time = api.currentTimeMillis();
		ClientAction action = new ClientAction(BattleActionType.ACTION_ATTACK, time, duration);
		action.setDamageWindow(moveSettings.getDamageWindowStartMs(), moveSettings.getDamageWindowEndMs());
		queuedActions.add(action);
		return duration;
	}

	/**
	 * Performs a special attack action
	 *
	 * @return the duration of this attack
	 */
	public int attackSpecial() {
		PokemonData pokemon = activeAttacker.getPokemon();
		PokemonMove move = pokemon.getMove2();
		MoveSettingsOuterClass.MoveSettings moveSettings = api.getItemTemplates().getMoveSettings(move);
		int duration = moveSettings.getDurationMs();
		if (activeAttacker.getEnergy() >= -moveSettings.getEnergyDelta()) {
			long time = api.currentTimeMillis();
			ClientAction action = new ClientAction(BattleActionType.ACTION_SPECIAL_ATTACK, time, duration);
			action.setDamageWindow(moveSettings.getDamageWindowStartMs(), moveSettings.getDamageWindowEndMs());
			queuedActions.add(action);
			return duration;
		} else {
			throw new RuntimeException("Not enough energy to use special attack!");
		}
	}

	/**
	 * Performs a dodge action
	 *
	 * @return the duration of this action
	 */
	public int dodge() {
		int duration = api.getItemTemplates().getBattleSettings().getDodgeDurationMs();
		performAction(BattleActionType.ACTION_DODGE, duration);
		return duration;
	}

	/**
	 * Swaps your current attacking Pokemon
	 *
	 * @param pokemon the pokemon to swap to
	 * @return the duration of this action
	 */
	public int swap(Pokemon pokemon) {
		int duration = api.getItemTemplates().getBattleSettings().getSwapDurationMs();
		ClientAction action = new ClientAction(BattleActionType.ACTION_SWAP_POKEMON, api.currentTimeMillis(),
				duration);
		action.setPokemon(pokemon);
		queuedActions.add(action);
		return duration;
	}

	/**
	 * @return the time left for this battle before it times out
	 */
	public long getTimeLeft() {
		return endTime - api.currentTimeMillis();
	}

	public class ServerAction {
		@Getter
		private final BattleActionType type;
		@Getter
		private final long start;
		@Getter
		private final long end;
		@Getter
		private final int duration;
		@Getter
		private final int energyDelta;
		@Getter
		private final int attackerIndex;
		@Getter
		private final int targetIndex;
		@Getter
		private final long damageWindowStart;
		@Getter
		private final long damageWindowEnd;
		@Getter
		private final BattleParticipant joined;
		@Getter
		private final BattleParticipant left;

		ServerAction(BattleAction action) {
			type = action.getType();
			start = toClientTime(action.getActionStartMs());
			duration = action.getDurationMs();
			end = start + duration;
			energyDelta = action.getEnergyDelta();
			attackerIndex = action.getAttackerIndex();
			targetIndex = action.getTargetIndex();
			damageWindowStart = toClientTime(action.getDamageWindowsStartTimestampMs());
			damageWindowEnd = toClientTime(action.getDamageWindowsEndTimestampMs());
			joined = action.getPlayerJoined();
			left = action.getPlayerLeft();
		}

		@Override
		public int hashCode() {
			return (int) start;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ServerAction) {
				ServerAction action = (ServerAction) obj;
				return action.getType() == type && action.getStart() == start && action.getDuration() == duration
						&& action.getAttackerIndex() == attackerIndex && action.getTargetIndex() == targetIndex;
			}
			return false;
		}
	}

	public class ClientAction {
		@Getter
		private final BattleActionType type;
		@Getter
		private final long startTime;
		@Getter
		private final long endTime;
		@Getter
		private final int duration;
		@Getter
		@Setter
		private Pokemon pokemon;
		@Getter
		private int damageWindowStart;
		@Getter
		private int damageWindowEnd;
		@Getter
		private boolean hasDamageWindow;

		ClientAction(BattleActionType type, long startTime, int duration) {
			this.type = type;
			this.startTime = toServerTime(startTime);
			this.endTime = this.startTime + duration;
			this.duration = duration;
		}

		/**
		 * Sets the damage window for this action
		 *
		 * @param start the start offset
		 * @param end the end offset
		 */
		public void setDamageWindow(int start, int end) {
			this.damageWindowStart = start;
			this.damageWindowEnd = end;
			this.hasDamageWindow = true;
		}
	}

	public class BattlePokemon {
		@Getter
		private final PokemonData pokemon;
		@Setter
		@Getter
		private int health;
		@Getter
		private int maxHealth;
		@Setter
		@Getter
		private int energy;

		BattlePokemon(BattlePokemonInfo activePokemon) {
			this.health = activePokemon.getCurrentHealth();
			this.energy = activePokemon.getCurrentEnergy();
			this.pokemon = activePokemon.getPokemonData();
			this.maxHealth = pokemon.getStaminaMax();
		}
	}

	public interface BattleHandler {
		/**
		 * Called to create a team of Pokemon to use in the battle
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @return the team to use in this battle
		 */
		Pokemon[] createTeam(PokemonGo api, Battle battle);

		/**
		 * Called when this battle begins
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param result the result from the start message
		 */
		void onStart(PokemonGo api, Battle battle, StartGymBattleResponse.Result result);

		/**
		 * Called when this battle end, and you won
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param deltaPoints the amount of points (prestige) added or removed after completing this battle
		 * @param newPoints the new amount of points on this gym
		 */
		void onVictory(PokemonGo api, Battle battle, int deltaPoints, long newPoints);

		/**
		 * Called when this battle ends, and you were defeated
		 *
		 * @param api the current API
		 * @param battle the current battle
		 */
		void onDefeated(PokemonGo api, Battle battle);

		/**
		 * Called when this battle times out
		 *
		 * @param api the current API
		 * @param battle the current battle
		 */
		void onTimedOut(PokemonGo api, Battle battle);

		/**
		 * Called when an action is started
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param action the action started
		 */
		void onActionStart(PokemonGo api, Battle battle, ServerAction action);

		/**
		 * Called when an action is completed
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param action the action completed
		 */
		void onActionEnd(PokemonGo api, Battle battle, ServerAction action);

		/**
		 * Called when an action's damage window opens
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param action the action
		 */
		void onDamageStart(PokemonGo api, Battle battle, ServerAction action);

		/**
		 * Called when an action's damage window closes
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param action the action
		 */
		void onDamageEnd(PokemonGo api, Battle battle, ServerAction action);

		/**
		 * Called when a player joins this battle
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param joined the player that joined
		 * @param action the action for the joining player
		 */
		void onPlayerJoin(PokemonGo api, Battle battle, BattleParticipant joined, ServerAction action);

		/**
		 * Called when a player leaves this battle
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param left player that left
		 * @param action the action for the leaving player
		 */
		void onPlayerLeave(PokemonGo api, Battle battle, BattleParticipant left, ServerAction action);

		/**
		 * Called when a Pokemon is attacked in this battle
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param attacked the attacked pokemon
		 * @param attacker the pokemon attacking the attacked pokemon
		 * @param duration the duration of the attack
		 * @param damageWindowStart the start of the damage window
		 * @param damageWindowEnd the end of the damage window
		 * @param action the attack action
		 */
		void onAttacked(PokemonGo api, Battle battle, BattlePokemon attacked, BattlePokemon attacker, int duration,
				long damageWindowStart, long damageWindowEnd, ServerAction action);

		/**
		 * Called when a Pokemon is attacked with the special move in this battle
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param attacked the attacked pokemon
		 * @param attacker the pokemon attacking the attacked pokemon
		 * @param duration the duration of the attack
		 * @param damageWindowStart the start of the damage window
		 * @param damageWindowEnd the end of the damage window
		 * @param action the attack action
		 */
		void onAttackedSpecial(PokemonGo api, Battle battle, BattlePokemon attacked, BattlePokemon attacker,
				int duration, long damageWindowStart, long damageWindowEnd, ServerAction action);

		/**
		 * Called when an exception occurs during this battle
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param exception the exception that occurred
		 */
		void onException(PokemonGo api, Battle battle, Exception exception);

		/**
		 * Called when invalid actions are sent to the server
		 *
		 * @param api the current API
		 * @param battle the current battle
		 */
		void onInvalidActions(PokemonGo api, Battle battle);

		/**
		 * Called when the attacker's health is updated
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param lastHealth the attacker's last health
		 * @param health the attacker's new health
		 * @param maxHealth the maximum health for the attacker
		 */
		void onAttackerHealthUpdate(PokemonGo api, Battle battle, int lastHealth, int health, int maxHealth);

		/**
		 * Called when the defender's health is updated
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param lastHealth the defender's last health
		 * @param health the defender's new health
		 * @param maxHealth the maximum health for the defender
		 */
		void onDefenderHealthUpdate(PokemonGo api, Battle battle, int lastHealth, int health, int maxHealth);

		/**
		 * Called when the attacker Pokemon changes
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param newAttacker the new attacker pokemon
		 */
		void onAttackerSwap(PokemonGo api, Battle battle, BattlePokemon newAttacker);

		/**
		 * Called when the defender Pokemon changes
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param newDefender the new defender pokemon
		 */
		void onDefenderSwap(PokemonGo api, Battle battle, BattlePokemon newDefender);

		/**
		 * Called when the given Pokemon faints.
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param pokemon the fainted pokemon
		 * @param duration the duration of this action
		 * @param action the faint action
		 */
		void onFaint(PokemonGo api, Battle battle, BattlePokemon pokemon, int duration, ServerAction action);

		/**
		 * Called when the given Pokemon dodges.
		 *
		 * @param api the current API
		 * @param battle the current battle
		 * @param pokemon the dodging pokemon
		 * @param duration the duration of this action
		 * @param action the dodge action
		 */
		void onDodge(PokemonGo api, Battle battle, BattlePokemon pokemon, int duration, ServerAction action);
	}
}
