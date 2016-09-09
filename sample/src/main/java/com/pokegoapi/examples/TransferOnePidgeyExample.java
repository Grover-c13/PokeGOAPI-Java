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

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.hash.HashProvider;
import okhttp3.OkHttpClient;

import java.util.List;

public class TransferOnePidgeyExample {
	/**
	 * Transfers one pidgey from the player's inventory.
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();

		PokemonGo api = new PokemonGo(http);
		try {
			HashProvider hasher = ExampleConstants.getHashProvider();
			api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD), hasher);
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);

			List<Pokemon> pidgeys =
					api.inventories.pokebank.getPokemonByPokemonId(PokemonIdOuterClass.PokemonId.PIDGEY);

			if (pidgeys.size() > 0) {
				Pokemon pest = pidgeys.get(0);
				// print the pokemon data
				pest.debug();
				ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result result = pest.transferPokemon();

				Log.i("Main", "Transfered Pidgey result:" + result);
			} else {
				Log.i("Main", "You have no pidgeys :O");
			}
		} catch (RequestFailedException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login. Invalid credentials, captcha or server issue: ", e);
		}
	}
}
