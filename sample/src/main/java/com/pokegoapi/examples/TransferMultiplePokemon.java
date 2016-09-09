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

import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.hash.HashProvider;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferMultiplePokemon {
	/**
	 * Transfers all bad pokemon from the player's inventory.
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();

		PokemonGo api = new PokemonGo(http);
		try {
			HashProvider hasher = ExampleConstants.getHashProvider();
			api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD), hasher);
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);

			PokeBank pokebank = api.inventories.pokebank;
			List<Pokemon> pokemons = pokebank.pokemons;
			List<Pokemon> transferPokemons = new ArrayList<>();
			//Find all pokemon of bad types or with IV less than 25%
			for (Pokemon pokemon : pokemons) {
				PokemonId id = pokemon.getPokemonId();
				double iv = pokemon.getIvInPercentage();
				if (iv < 90) {
					if (id == PokemonId.RATTATA || id == PokemonId.PIDGEY
							|| id == PokemonId.CATERPIE || id == PokemonId.WEEDLE
							|| id == PokemonId.MAGIKARP || id == PokemonId.ZUBAT
							|| iv < 25) {
						transferPokemons.add(pokemon);
					}
				}
			}
			System.out.println("Releasing " + transferPokemons.size() + " pokemon.");
			Pokemon[] transferArray = transferPokemons.toArray(new Pokemon[transferPokemons.size()]);
			Map<PokemonFamilyId, Integer> responses = pokebank.releasePokemon(transferArray);

			//Loop through all responses and find the total amount of candies earned for each family
			Map<PokemonFamilyId, Integer> candies = new HashMap<>();
			for (Map.Entry<PokemonFamilyId, Integer> entry : responses.entrySet()) {
				int candyAwarded = entry.getValue();
				PokemonFamilyId family = entry.getKey();
				Integer candy = candies.get(family);
				if (candy == null) {
					//candies map does not yet contain the amount if null, so set it to 0
					candy = 0;
				}
				//Add the awarded candies from this request
				candy += candyAwarded;
				candies.put(family, candy);
			}
			for (Map.Entry<PokemonFamilyId, Integer> entry : candies.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue() + " candies awarded");
			}
		} catch (RequestFailedException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login. Invalid credentials, captcha or server issue: ", e);
		}
	}
}
