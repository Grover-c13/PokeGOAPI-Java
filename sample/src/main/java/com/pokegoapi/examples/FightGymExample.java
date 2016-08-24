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


import POGOProtos.Networking.Responses.StartGymBattleResponseOuterClass.StartGymBattleResponse.Result;
import com.pokegoapi.api.PokemonApi;
import com.pokegoapi.api.gym.Battle;
import com.pokegoapi.api.gym.Gym;
import com.pokegoapi.api.gym.GymDetails;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.CredentialProvider;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

import java.util.List;

public class FightGymExample {

	/**
	 * Catches a pokemon at an area.
	 * @param args Not used
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		CredentialProvider auth = null;
		try {
			auth = new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD);
			// or google
			//auth = new GoogleCredentialProvider(http, token); // currently uses oauth flow so no user or pass needed
			PokemonApi pokemonApi = PokemonApi.newBuilder().credentialProvider(auth)
					.withHttpClient(http)
					.latitude(-32.011011)
					.longitude(115.932831)
					.altitude(0d)
					.build();

			// set location

			List<Pokemon> pokemons = pokemonApi.getInventories().getPokebank().getPokemons();
			Pokemon[] attackers = new Pokemon[6];

			for (int i = 0; i < 6; i++) {
				attackers[i] = pokemons.get(i);
			}


			for (Gym gym : pokemonApi.getMap().getGyms()) {
				GymDetails gymDetails = gym.getGymDetails().toBlocking().first();
				if (gymDetails.isAttackable()) {
					Battle battle = gym.battle(attackers).toBlocking().first();
					// start the battle
					Result result = battle.start().toBlocking().first();

					if (result == Result.SUCCESS) {
						// started battle successfully

						// loop while battle is not finished
						while (!battle.isConcluded()) {
							System.out.println("attack:" + battle.attack(5));
							Thread.sleep(500);
						}

						System.out.println("Battle result:" + battle.getOutcome());

					} else {
						System.out.println("FAILED:" + result);
					}
				}

			}

		} catch (LoginFailedException | RemoteServerException | InterruptedException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login or server issue: ", e);

		}
	}
}
