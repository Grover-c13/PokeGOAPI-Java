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
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.BlockingCallback;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

import java.util.List;

public class TransferOnePidgeyExample {
	/**
	 * Transfers one pidgey from the player's inventory.
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();

		PokemonGo go = new PokemonGo(http);
		try {
			// check readme for other example
			BlockingCallback callback = new BlockingCallback();
			go.login(new PtcCredentialProvider(http, ExampleLoginDetails.LOGIN,
					ExampleLoginDetails.PASSWORD), callback);
			callback.block();

			List<Pokemon> pidgeys =
					go.getInventories().getPokebank().getPokemonByPokemonId(PokemonIdOuterClass.PokemonId.PIDGEY);

			if (pidgeys.size() > 0) {
				Pokemon pest = pidgeys.get(0);
				// print the pokemon data
				pest.debug();
				pest.transferPokemon(new AsyncReturn<ReleasePokemonResponse.Result>() {
					@Override
					public void onReceive(ReleasePokemonResponse.Result result, Exception exception) {
						Log.i("Main", "Transferred Pidgey result:" + result);
					}
				});
			} else {
				Log.i("Main", "You have no pidgeys :O");
			}
		} catch (Exception e) {
			Log.e("Main", "Failed to login. Invalid credentials or server issue: ", e);
		}
	}
}
