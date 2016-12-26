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
import com.pokegoapi.api.map.MapObjects;
import com.pokegoapi.api.map.Point;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.NearbyPokemon;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.api.settings.CatchOptions;
import com.pokegoapi.api.settings.PokeballSelector;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.MapUtil;
import com.pokegoapi.util.path.Path;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class CatchPokemonAtAreaExample {

	/**
	 * Catches a pokemon at an area.
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		final PokemonGo api = new PokemonGo(http);
		try {
			api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD));
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);

			// Catch all pokemon in the current area
			catchArea(api);

			MapObjects mapObjects = api.getMap().getMapObjects();
			//Find all pokestops with pokemon nearby
			List<Pokestop> travelPokestops = new ArrayList<>();
			Set<NearbyPokemon> nearby = mapObjects.getNearby();
			for (NearbyPokemon nearbyPokemon : nearby) {
				String fortId = nearbyPokemon.getFortId();
				//Check if nearby pokemon is near a pokestop
				if (fortId != null && fortId.length() > 0) {
					//Find the pokestop with the fort id of the nearby pokemon
					Pokestop pokestop = mapObjects.getPokestop(fortId);
					if (pokestop != null && !travelPokestops.contains(pokestop)) {
						travelPokestops.add(pokestop);
					}
				}
			}

			//Sort from closest to farthest
			Collections.sort(travelPokestops, new Comparator<Pokestop>() {
				@Override
				public int compare(Pokestop primary, Pokestop secondary) {
					double lat = api.getLatitude();
					double lng = api.getLongitude();
					double distance1 = MapUtil.distFrom(primary.getLatitude(), primary.getLongitude(), lat, lng);
					double distance2 = MapUtil.distFrom(secondary.getLatitude(), secondary.getLongitude(), lat, lng);
					return Double.compare(distance1, distance2);
				}
			});

			for (Pokestop pokestop : travelPokestops) {
				Point destination = new Point(pokestop.getLatitude(), pokestop.getLongitude());
				//Use the current player position as the source and the pokestop position as the destination
				//Travel to Pokestop at 15KMPH
				Path path = new Path(api.getPoint(), destination, 20.0);
				System.out.println("Traveling to " + destination + " at 20KMPH!");
				path.start(api);
				try {
					while (!path.isComplete()) {
						//Calculate the desired intermediate point for the current time
						Point point = path.calculateIntermediate(api);
						//Set the API location to that point
						api.setLatitude(point.getLatitude());
						api.setLongitude(point.getLongitude());
						//Sleep for 2 seconds before setting the location again
						Thread.sleep(2000);
					}
				} catch (InterruptedException e) {
					break;
				}
				System.out.println("Finished traveling to pokestop, catching pokemon.");
				catchArea(api);
			}
		} catch (LoginFailedException | NoSuchItemException | RemoteServerException | CaptchaActiveException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login, captcha or server issue: ", e);
		}
	}

	private static void catchArea(PokemonGo api)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException, NoSuchItemException {
		try {
			//Wait until map is updated for the current location
			api.getMap().awaitUpdate();
		} catch (InterruptedException e) {
			return;
		}

		Set<CatchablePokemon> catchablePokemon = api.getMap().getMapObjects().getPokemon();
		System.out.println("Pokemon in area: " + catchablePokemon.size());

		for (CatchablePokemon cp : catchablePokemon) {
			// You need to Encounter first.
			EncounterResult encResult = cp.encounterPokemon();
			// if encounter was successful, catch
			if (encResult.wasSuccessful()) {
				System.out.println("Encountered: " + cp.getPokemonId());
				CatchOptions options = new CatchOptions(api)
						.useRazzberry(true)
						.withPokeballSelector(PokeballSelector.SMART);
				List<Pokeball> useablePokeballs = api.getInventories().getItemBag().getUseablePokeballs();
				double probability = cp.getCaptureProbability();
				if (useablePokeballs.size() > 0) {
					//No need to get pokeball like this, this is just for debug logs
					Pokeball pokeball = PokeballSelector.SMART.select(useablePokeballs, probability);
					System.out.println("Attempting to catch: " + cp.getPokemonId() + " with " + pokeball
							+ " (" + probability + ")");
					CatchResult result = cp.catchPokemon(options);
					System.out.println("Result: " + result.getStatus());
				} else {
					System.out.println("Skipping Pokemon, we have no Pokeballs!");
				}
			} else {
				System.out.println("Encounter failed. " + encResult.getStatus());
			}
		}
	}
}
