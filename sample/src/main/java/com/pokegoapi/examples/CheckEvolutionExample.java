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

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Evolutions;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.hash.HashProvider;
import okhttp3.OkHttpClient;

import java.util.List;

public class CheckEvolutionExample {

	/**
	 * Displays pokemon evolutions
	 *
	 * @param args Not used
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		final PokemonGo api = new PokemonGo(http);
		try {
			//Login and set location
			HashProvider hasher = ExampleConstants.getHashProvider();
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);
			api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD), hasher);

			//Get the evolution meta from the item templates received from the game server
			Evolutions evolutionMeta = api.itemTemplates.evolutions;

			System.out.println("Evolutions: ");
			for (PokemonId pokemon : PokemonId.values()) {
				List<PokemonId> evolutions = evolutionMeta.getEvolutions(pokemon);
				if (evolutions.size() > 0) {
					System.out.println(pokemon + " -> " + evolutions);
				}
			}
			System.out.println();
			System.out.println("Most basic: ");
			for (PokemonId pokemon : PokemonId.values()) {
				List<PokemonId> basic = evolutionMeta.getBasic(pokemon);
				if (basic.size() > 0) {
					//Check this is not the most basic pokemon
					if (!(basic.size() == 1 && basic.contains(pokemon))) {
						System.out.println(pokemon + " -> " + basic);
					}
				}
			}
			System.out.println();
			System.out.println("Highest: ");
			for (PokemonId pokemon : PokemonId.values()) {
				List<PokemonId> highest = evolutionMeta.getHighest(pokemon);
				if (highest.size() > 0) {
					//Check this is not the highest pokemon
					if (!(highest.size() == 1 && highest.contains(pokemon))) {
						System.out.println(pokemon + " -> " + highest);
					}
				}
			}
		} catch (RequestFailedException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login, captcha or server issue: ", e);
		}
	}
}
