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
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.PtcLogin;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

import java.util.List;

public class TransferOnePidgeyExample {


	/**
	 * Transfers one pidgey from the player's inventory.
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth = null;
		try {
			auth = new PtcLogin(http).login(ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD);
			// or google
			//auth = new GoogleLogin(http).login("", ""); // currently uses oauth flow so no user or pass needed
			PokemonGo go = new PokemonGo(auth, http);

			List<Pokemon> pidgeys =
					go.getInventories().getPokebank().getPokemonByPokemonId(PokemonIdOuterClass.PokemonId.PIDGEY);

			if (pidgeys.size() > 0) {
				Pokemon pest = pidgeys.get(0);
				pest.debug();
				ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result result = pest.transferPokemon();

				Log.i("Main", "Transfered Pidgey result:" + result);
			} else {
				Log.i("Main", "You have no pidgeys :O");
			}


		} catch (LoginFailedException | RemoteServerException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login. Invalid credentials or server issue: ", e);
		}
	}
}
