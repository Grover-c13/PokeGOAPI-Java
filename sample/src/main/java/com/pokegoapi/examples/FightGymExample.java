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

package com.pokegoapi.examples;

import POGOProtos.Data.Battle.BattleActionTypeOuterClass.BattleActionType;
import POGOProtos.Data.Battle.BattleParticipantOuterClass.BattleParticipant;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse.Result;
import POGOProtos.Networking.Responses.UseItemPotionResponseOuterClass.UseItemPotionResponse;
import POGOProtos.Networking.Responses.UseItemReviveResponseOuterClass.UseItemReviveResponse;
import POGOProtos.Settings.Master.MoveSettingsOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.gym.Battle;
import com.pokegoapi.api.gym.Battle.ServerAction;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.map.MapObjects;
import com.pokegoapi.api.map.Point;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.request.CaptchaActiveException;
import com.pokegoapi.exceptions.request.LoginFailedException;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.exceptions.request.HashException;
import com.pokegoapi.main.PokemonMeta;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.MapUtil;
import com.pokegoapi.util.hash.HashProvider;
import com.pokegoapi.util.path.Path;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FightGymExample {
	/**
	 * Fights gyms in the nearby area.
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		final PokemonGo api = new PokemonGo(http);
		try {
			//Login and set location
			HashProvider hasher = ExampleConstants.getHashProvider();
			api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD), hasher);
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);

			List<Pokemon> pokemons = api.getInventories().getPokebank().getPokemons();

			//List to put all pokemon that can be used in a gym battle
			List<Pokemon> possiblePokemon = new ArrayList<>();

			for (Pokemon pokemon : pokemons) {
				//Check if pokemon has full health and is not deployed in a gym
				if (pokemon.getDeployedFortId().length() == 0) {
					if (pokemon.getStamina() < pokemon.getMaxStamina()) {
						healPokemonFull(api, pokemon);
						if (!(pokemon.isInjured() || pokemon.isFainted())) {
							possiblePokemon.add(pokemon);
						}
						Thread.sleep(1000);
					} else {
						possiblePokemon.add(pokemon);
					}
				} else {
					System.out.println(pokemon.getPokemonId() + " already deployed.");
				}
			}

			//Sort by highest CP
			Collections.sort(possiblePokemon, new Comparator<Pokemon>() {
				@Override
				public int compare(Pokemon primary, Pokemon secondary) {
					return Integer.compare(secondary.getCp(), primary.getCp());
				}
			});

			//Pick the top 6 pokemon from the possible list
			final Pokemon[] attackers = new Pokemon[6];

			for (int i = 0; i < 6; i++) {
				attackers[i] = possiblePokemon.get(i);
			}

			//Sort from closest to farthest
			MapObjects mapObjects = api.getMap().getMapObjects();
			List<Gym> gyms = new ArrayList<>(mapObjects.getGyms());
			Collections.sort(gyms, new Comparator<Gym>() {
				@Override
				public int compare(Gym primary, Gym secondary) {
					double lat = api.getLatitude();
					double lng = api.getLongitude();
					double distance1 = MapUtil.distFrom(primary.getLatitude(), primary.getLongitude(), lat, lng);
					double distance2 = MapUtil.distFrom(secondary.getLatitude(), secondary.getLongitude(), lat, lng);
					return Double.compare(distance1, distance2);
				}
			});

			for (Gym gym : gyms) {
				//Check if gym is attackable, and check if it is not owned by your team
				if (gym.isAttackable() && gym.getOwnedByTeam() != api.getPlayerProfile().getPlayerData().getTeam()) {
					//Walk to gym; Documented pathing in TravelToPokestopExample
					Point destination = new Point(gym.getLatitude(), gym.getLongitude());
					Path path = new Path(api.getPoint(), destination, 50.0);
					System.out.println("Traveling to " + destination + " at 50KMPH!");
					path.start(api);
					try {
						while (!path.isComplete()) {
							Point point = path.calculateIntermediate(api);
							api.setLatitude(point.getLatitude());
							api.setLongitude(point.getLongitude());
							System.out.println("Time left: " + (int) (path.getTimeLeft(api) / 1000) + " seconds.");
							Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					System.out.println("Beginning battle with gym.");

					//Create battle object
					Battle battle = gym.battle();

					//Start battle
					battle.start(new FightHandler(attackers));
					while (battle.isActive()) {
						handleAttack(battle);
					}

					//Heal all pokemon after battle
					for (Pokemon pokemon : possiblePokemon) {
						if (pokemon.getStamina() < pokemon.getMaxStamina()) {
							healPokemonFull(api, pokemon);
							Thread.sleep(1000);
						}
					}

					//If prestige reaches 0, deploy your pokemon
					if (battle.getGym().getPoints() <= 0) {
						Pokemon best = possiblePokemon.get(0);
						System.out.println("Deploying " + best.getPokemonId() + " to gym.");
						battle.getGym().deployPokemon(best);
					}
				}
			}
		} catch (RequestFailedException | InterruptedException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login, captcha or server issue: ", e);
		}
	}

	private static void handleAttack(Battle battle) throws InterruptedException {
		int duration;
		PokemonMove specialMove = battle.getActiveAttacker().getPokemon().getMove2();
		MoveSettingsOuterClass.MoveSettings moveSettings = PokemonMeta.getMoveSettings(specialMove);
		//Check if we have sufficient energy to perform a special attack
		int energy = battle.getActiveAttacker().getEnergy();
		int desiredEnergy = -moveSettings.getEnergyDelta();
		if (energy <= desiredEnergy) {
			duration = battle.attack();
		} else {
			duration = battle.attackSpecial();
		}
		//Attack and sleep for the duration of the attack + some extra time
		Thread.sleep(duration + (long) (Math.random() * 10));
	}

	private static void healPokemonFull(PokemonGo api, Pokemon pokemon)
			throws LoginFailedException, CaptchaActiveException, RequestFailedException, HashException {
		System.out.println("Healing " + pokemon.getPokemonId());
		//Continue healing the pokemon until fully healed
		while (pokemon.isInjured() || pokemon.isFainted()) {
			if (pokemon.isFainted()) {
				if (pokemon.revive() == UseItemReviveResponse.Result.ERROR_CANNOT_USE) {
					System.out.println("We have no revives! Cannot revive pokemon.");
					break;
				}
			} else {
				if (pokemon.heal() == UseItemPotionResponse.Result.ERROR_CANNOT_USE) {
					System.out.println("We have no potions! Cannot heal pokemon.");
					break;
				}
			}
		}
	}

	private static class FightHandler implements Battle.BattleHandler {
		private Pokemon[] team;

		FightHandler(Pokemon[] team) {
			this.team = team;
		}

		@Override
		public Pokemon[] createTeam(PokemonGo api, Battle battle) {
			return team;
		}

		@Override
		public void onStart(PokemonGo api, Battle battle, Result result) {
			System.out.println("Battle started with result: " + result);
			try {
				System.out.println("Defender count: " + battle.getGym().getDefendingPokemon().size());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onVictory(PokemonGo api, Battle battle, int deltaPoints, long newPoints) {
			System.out.println("Gym ended with result: Victory!");
			System.out.println("Delta points: " + deltaPoints + ", New points: " + newPoints);
		}

		@Override
		public void onDefeated(PokemonGo api, Battle battle) {
			System.out.println("Gym ended with result: Defeated");
		}

		@Override
		public void onTimedOut(PokemonGo api, Battle battle) {
			System.out.println("Gym battle timed out!");
		}

		@Override
		public void onActionStart(PokemonGo api, Battle battle, Battle.ServerAction action) {
			//Dodge all special attacks
			if (action.getType() == BattleActionType.ACTION_SPECIAL_ATTACK) {
				System.out.println("Dodging special attack!");
				battle.dodge();
			}
			System.out.println(toIndexName(action) + " performed " + action.getType());
		}

		@Override
		public void onActionEnd(PokemonGo api, Battle battle, Battle.ServerAction action) {
		}

		@Override
		public void onDamageStart(PokemonGo api, Battle battle, ServerAction action) {
		}

		@Override
		public void onDamageEnd(PokemonGo api, Battle battle, ServerAction action) {
		}

		@Override
		public void onPlayerJoin(PokemonGo api, Battle battle, BattleParticipant joined, Battle.ServerAction action) {
			System.out.println(joined.getTrainerPublicProfile().getName() + " joined this battle!");
		}

		@Override
		public void onPlayerLeave(PokemonGo api, Battle battle, BattleParticipant left, Battle.ServerAction action) {
			System.out.println(left.getTrainerPublicProfile().getName() + " left this battle!");
		}

		@Override
		public void onAttacked(PokemonGo api, Battle battle, Battle.BattlePokemon attacked,
							   Battle.BattlePokemon attacker, int duration,
							   long damageWindowStart, long damageWindowEnd, Battle.ServerAction action) {
			PokemonId attackedPokemon = attacked.getPokemon().getPokemonId();
			PokemonId attackerPokemon = attacker.getPokemon().getPokemonId();
			System.out.println(attackedPokemon + " attacked by " + attackerPokemon + " (" + toIndexName(action) + ")");
		}

		@Override
		public void onAttackedSpecial(PokemonGo api, Battle battle, Battle.BattlePokemon attacked,
									  Battle.BattlePokemon attacker, int duration,
									  long damageWindowStart, long damageWindowEnd, Battle.ServerAction action) {
			PokemonId attackedPokemon = attacked.getPokemon().getPokemonId();
			PokemonId attackerPokemon = attacker.getPokemon().getPokemonId();
			System.out.println(attackedPokemon
					+ " attacked with special attack by " + attackerPokemon + " (" + toIndexName(action) + ")");
		}

		@Override
		public void onException(PokemonGo api, Battle battle, Exception exception) {
			System.err.println("Exception while performing battle:");
			exception.printStackTrace();
		}

		@Override
		public void onInvalidActions(PokemonGo api, Battle battle) {
			System.err.println("Sent invalid actions!");
		}

		@Override
		public void onAttackerHealthUpdate(PokemonGo api, Battle battle, int lastHealth, int health, int maxHealth) {
			System.out.println("Attacker: " + health + " / " + maxHealth);
		}

		@Override
		public void onDefenderHealthUpdate(PokemonGo api, Battle battle, int lastHealth, int health, int maxHealth) {
			System.out.println("Defender: " + health + " / " + maxHealth);
		}

		@Override
		public void onAttackerSwap(PokemonGo api, Battle battle, Battle.BattlePokemon newAttacker) {
			System.out.println("Attacker change: " + newAttacker.getPokemon().getPokemonId());
		}

		@Override
		public void onDefenderSwap(PokemonGo api, Battle battle, Battle.BattlePokemon newDefender) {
			System.out.println("Defender change: " + newDefender.getPokemon().getPokemonId());
		}

		@Override
		public void onFaint(PokemonGo api, Battle battle, Battle.BattlePokemon pokemon, int duration,
							Battle.ServerAction action) {
			System.out.println(toIndexName(action) + " fainted!");
		}

		@Override
		public void onDodge(PokemonGo api, Battle battle, Battle.BattlePokemon pokemon, int duration,
							Battle.ServerAction action) {
			System.out.println(toIndexName(action) + " dodged!");
		}

		/**
		 * Converts the attacker index to a readable name
		 *
		 * @param action the action containing an index
		 * @return a readable name for the attacker
		 */
		private String toIndexName(Battle.ServerAction action) {
			String name = "Attacker";
			if (action.getAttackerIndex() == -1) {
				name = "Defender";
			}
			return name;
		}
	}
}
