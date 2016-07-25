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


import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.EncounterResult;
import com.pokegoapi.auth.GoogleLogin;
import com.pokegoapi.auth.PtcLogin;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

import java.util.List;

public class CatchPokemonAtAreaExample {

	/**
	 * Catches a pokemon at an area.
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth = null;
		try {
			auth = new PtcLogin(http).login(ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD);
			// or google
			//auth = new GoogleLogin(http).login("", ""); // currently uses oauth flow so no user or pass needed
			PokemonGo go = new PokemonGo(auth, http);
			// set location
			go.setLocation(-32.058087, 115.744325, 0);

			List<CatchablePokemon> catchablePokemon = go.getMap().getCatchablePokemon();
			System.out.println("Pokemon in area:" + catchablePokemon.size());

			for (CatchablePokemon cp : catchablePokemon) {
				// You need to Encounter first.
				EncounterResult encResult = cp.encounterPokemon();
				// if encounter was succesful, catch
				if (encResult.wasSuccessful()) {
					System.out.println("Encounted:" + cp.getPokemonId());
					CatchResult result = cp.catchPokemonWithRazzBerry();
					System.out.println("Attempt to catch:" + cp.getPokemonId() + " " + result.getStatus());
				}

			}

		} catch (LoginFailedException | RemoteServerException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login or server issue: ", e);

		}
	}
}
