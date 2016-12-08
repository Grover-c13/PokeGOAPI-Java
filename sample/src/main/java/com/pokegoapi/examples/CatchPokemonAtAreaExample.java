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


import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.api.settings.CatchOptions;
import com.pokegoapi.api.settings.PokeballSelector;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import okhttp3.OkHttpClient;

import java.util.List;

public class CatchPokemonAtAreaExample {

	/**
	 * Catches a pokemon at an area.
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		PokemonGo go = new PokemonGo(http);
		try {
			go.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN,
					ExampleConstants.PASSWORD));
			//or google
			//new PokemonGo(GoogleCredentialProvider(http,listner));
			//Subsiquently
			//new PokemonGo(GoogleCredentialProvider(http,refreshtoken));
			// set location
			go.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);

			List<CatchablePokemon> catchablePokemon = go.getMap().getCatchablePokemon();
			System.out.println("Pokemon in area: " + catchablePokemon.size());

			for (CatchablePokemon cp : catchablePokemon) {
				// You need to Encounter first.
				EncounterResult encResult = cp.encounterPokemon();
				// if encounter was succesful, catch
				if (encResult.wasSuccessful()) {
					System.out.println("Encountered: " + cp.getPokemonId());
					CatchOptions options = new CatchOptions(go)
							.useRazzberry(true)
							.withPokeballSelector(PokeballSelector.SMART);
					List<Pokeball> useablePokeballs = go.getInventories().getItemBag().getUseablePokeballs();
					double probability = cp.getCaptureProbability();
					if (useablePokeballs.size() > 0) {
						Pokeball pokeball = PokeballSelector.SMART.select(useablePokeballs, probability);
						System.out.println("Attempting to catch: " + cp.getPokemonId() + " with " + pokeball
								+ " (" + probability + ")");
						CatchResult result = cp.catchPokemon(options);
						System.out.println("Result:" + result.getStatus());
					} else {
						System.out.println("Skipping Pokemon, we have no Pokeballs!");
					}
				}

			}

		} catch (LoginFailedException | NoSuchItemException | RemoteServerException | CaptchaActiveException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login, captcha or server issue: ", e);

		}
	}
}
