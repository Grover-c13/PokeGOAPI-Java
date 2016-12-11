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

import POGOProtos.Data.Battle.BattleParticipantOuterClass.BattleParticipant;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse.Result;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.gym.Battle;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.map.Point;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.api.pokemon.PokemonMoveMeta;
import com.pokegoapi.api.pokemon.PokemonMoveMetaRegistry;
import com.pokegoapi.auth.CredentialProvider;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.MapUtil;
import com.pokegoapi.util.path.Path;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FightGymExample {

	/**
	 * Catches a pokemon at an area.
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		CredentialProvider auth;
		final PokemonGo api = new PokemonGo(http);
		try {
			auth = new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD);
			api.login(auth);
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);

			List<Pokemon> pokemons = api.getInventories().getPokebank().getPokemons();
			List<Pokemon> possible = new ArrayList<>();

			for (Pokemon pokemon : pokemons) {
				//Check if pokemon has full health and is not deployed in a gym
				if (pokemon.getDeployedFortId().length() == 0) {
					possible.add(pokemon);
					if (pokemon.getStamina() < pokemon.getMaxStamina()) {
						healPokemonFull(pokemon);
						Thread.sleep(1000);
					}
				} else {
					System.out.println(pokemon.getPokemonId() + " already deployed.");
				}
			}

			//Sort by highest CP
			Collections.sort(possible, new Comparator<Pokemon>() {
				@Override
				public int compare(Pokemon primary, Pokemon secondary) {
					return Integer.compare(secondary.getCp(), primary.getCp());
				}
			});

			final Pokemon[] attackers = new Pokemon[6];

			for (int i = 0; i < 6; i++) {
				attackers[i] = possible.get(i);
			}

			//Sort from closest to farthest
			List<Gym> gyms = api.getMap().getGyms();
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
				if (gym.isAttackable() && gym.getOwnedByTeam() != api.getPlayerProfile().getPlayerData().getTeam()) {
					//Documented pathing in TravelToPokestopExample
					Point destination = new Point(gym.getLatitude(), gym.getLongitude());
					Path path = new Path(api.getPoint(), destination, 25.0);
					System.out.println("Traveling to " + destination + " at 25KMPH!");
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

					//Start battle (method blocks until battle is complete)
					battle.start(new FightHandler(attackers));
					while (battle.isActive()) {
						handleAttack(battle);
					}
					//Heal all pokemon after battle
					for (Pokemon pokemon : pokemons) {
						if (pokemon.getStamina() < pokemon.getMaxStamina()) {
							healPokemonFull(pokemon);
							Thread.sleep(1000);
						}
					}
					if (battle.getGym().getPoints() <= 0) {
						Pokemon best = possible.get(0);
						System.out.println("Deploying " + best.getPokemonId() + " to gym.");
						battle.getGym().deployPokemon(best);
					}
				}
			}

		} catch (LoginFailedException | RemoteServerException | InterruptedException | CaptchaActiveException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login, captcha or server issue: ", e);
		}
	}

	private static void handleAttack(Battle battle) throws InterruptedException {
		int duration;
		PokemonMove specialMove = battle.getActiveAttacker().getPokemon().getMove2();
		PokemonMoveMeta specialMeta = PokemonMoveMetaRegistry.getMeta(specialMove);
		//Check if we have sufficient energy to perform a special attack
		int energy = battle.getActiveAttacker().getEnergy();
		int desiredEnergy = -specialMeta.getEnergy();
		if (energy >= desiredEnergy) {
			duration = battle.attack();
		} else {
			duration = battle.attackSpecial();
		}
		//Attack and sleep for the duration of the attack + some extra time
		System.out.println("Attack " + duration + ", energy: " + energy + " / " + desiredEnergy);
		Thread.sleep(duration + 10);
	}

	private static void healPokemonFull(Pokemon pokemon)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException {
		System.out.println("Healing " + pokemon.getPokemonId());
		if (pokemon.isFainted()) {
			pokemon.revive();
		}
		while (pokemon.getStamina() < pokemon.getMaxStamina()) {
			pokemon.heal();
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
		}

		@Override
		public void onActionEnd(PokemonGo api, Battle battle, Battle.ServerAction action) {
		}

		private String toIndexName(Battle.ServerAction action) {
			String name = "Me";
			if (action.getAttackerIndex() == -1) {
				name = "Defender";
			}
			return name;
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
							   Battle.BattlePokemon attacker, long duration,
							   long damageWindowStart, long damageWindowEnd, Battle.ServerAction action) {
			PokemonId attackedPokemon = attacked.getPokemon().getPokemonId();
			PokemonId attackerPokemon = attacker.getPokemon().getPokemonId();
			System.out.println(attackedPokemon + " attacked by " + attackerPokemon + " (" + toIndexName(action) + ")");
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
	}
}
