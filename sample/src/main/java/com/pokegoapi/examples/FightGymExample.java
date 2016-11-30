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

import POGOProtos.Networking.Responses.AttackGymResponseOuterClass.AttackGymResponse;
import POGOProtos.Networking.Responses.GetGymDetailsResponseOuterClass.GetGymDetailsResponse;
import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.gym.Battle;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.CredentialProvider;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonCallback;
import com.pokegoapi.main.SyncedReturn;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

import java.util.List;

public class FightGymExample {
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		CredentialProvider credentials;
		final PokemonGo api = new PokemonGo(http);
		try {
			credentials = new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD);
			api.login(credentials, new PokemonCallback() {
				@Override
				public void onCompleted(Exception exception) {
					if (exception != null) {
						Log.e("Main", "Failed to login or server issue: ", exception);
					}
					onLogin(api);
				}
			});

		} catch (LoginFailedException | RemoteServerException e) {
			Log.e("Main", "Failed to login to PTC: ", e);
		}
	}

	private static void onLogin(final PokemonGo api) {
		api.setLocation(-32.011011, 115.932831, 0);

		if (api.getPlayerProfile().getStats().getLevel() >= 5) {
			final List<Pokemon> pokemon = api.getInventories().getPokebank().getPokemons();
			final Pokemon[] attackers = new Pokemon[6];

			//Select 6 pokemon to attack the gym with
			for (int i = 0; i < 6; i++) {
				attackers[i] = pokemon.get(i);
			}

			api.getMap().getGyms(new AsyncReturn<List<Gym>>() {
				@Override
				public void onReceive(final List<Gym> gyms, Exception exception) {
					if (exception != null) {
						Log.e("Main", "Failed to load gyms in area: ", exception);
					} else {
						api.queueTask(new Runnable() {
							@Override
							public void run() {
								for (Gym gym : gyms) {
									attackGym(attackers, gym);
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
								}
								System.exit(0);
							}
						});
					}
				}
			});
		} else {
			System.out.println("Not yet level 5! Can't attack gyms!");
		}
	}

	private static void attackGym(final Pokemon[] pokemon, final Gym gym) {
		SyncedReturn<GetGymDetailsResponse> detailsReturn = new SyncedReturn<>();
		gym.getDetails(detailsReturn);
		try {
			GetGymDetailsResponse details = detailsReturn.get();
			//Ensure this gym is attackable
			if (details.getGymState().getMembershipsCount() > 0) {
				final Battle battle = gym.battle(pokemon);
				SyncedReturn<StartGymBattleResponse.Result> startReturn = new SyncedReturn<>();
				battle.start(startReturn);
				StartGymBattleResponse.Result result = startReturn.get();
				if (result == StartGymBattleResponse.Result.SUCCESS) {
					while (!battle.isConcluded()) {
						SyncedReturn<AttackGymResponse> attackReturn = new SyncedReturn<>();
						battle.attack(5, attackReturn);
						AttackGymResponse attackResult = attackReturn.get();
						System.out.println("Attack: " + attackResult.getResult());
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					System.out.println("Battle result: " + battle.getOutcome());
				} else {
					System.out.println("Failed to start gym battle: " + result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
