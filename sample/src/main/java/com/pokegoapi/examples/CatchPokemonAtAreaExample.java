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


import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.MapObjects;
import com.pokegoapi.api.map.Point;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.NearbyPokemon;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.api.settings.CatchOptions;
import com.pokegoapi.api.settings.PokeballSelector;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.exceptions.hash.HashException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.MapUtil;
import com.pokegoapi.util.PokeDictionary;
import com.pokegoapi.util.hash.HashProvider;
import com.pokegoapi.util.path.Path;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
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
			HashProvider hasher = ExampleConstants.getHashProvider();
			api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD), hasher);
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
				//Travel to Pokestop at 20KMPH
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
		} catch (HashException e) {
			Log.e("Main ", "Failed to login to the Hash Service: ", e);
		}
	}

	private static void catchArea(PokemonGo api)
			throws LoginFailedException, CaptchaActiveException, RemoteServerException, NoSuchItemException {
		try {
			//Wait until map is updated for the current location
			api.getMap().awaitUpdate();

			Set<CatchablePokemon> catchablePokemon = api.getMap().getMapObjects().getPokemon();
			System.out.println("Pokemon in area: " + catchablePokemon.size());

			Random random = new Random();
			PokeBank pokebank = api.getInventories().getPokebank();

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
						//Select pokeball with smart selector to print what pokeball is used
						Pokeball pokeball = PokeballSelector.SMART.select(useablePokeballs, probability);
						System.out.println("Attempting to catch: " + cp.getPokemonId() + " with " + pokeball
								+ " (" + probability + ")");
						//Throw pokeballs until capture or flee
						while (!cp.isDespawned()) {
							//Wait between Pokeball throws
							Thread.sleep(500 + random.nextInt(1000));
							CatchResult result = cp.catchPokemon(options);
							System.out.println("Threw ball: " + result.getStatus());
							if (result.getStatus() == CatchStatus.CATCH_SUCCESS) {
								//Print pokemon stats
								Pokemon pokemon = pokebank.getPokemonById(result.getCapturedPokemonId());
								double iv = pokemon.getIvInPercentage();
								int number = pokemon.getPokemonId().getNumber();
								String name = PokeDictionary.getDisplayName(number, Locale.ENGLISH);
								System.out.println("====" + name + "====");
								System.out.println("CP: " + pokemon.getCp());
								System.out.println("IV: " + iv + "%");
								System.out.println("Height: " + pokemon.getHeightM() + "m");
								System.out.println("Weight: " + pokemon.getWeightKg() + "kg");
								System.out.println("Move 1: " + pokemon.getMove1());
								System.out.println("Move 2: " + pokemon.getMove2());
								//Rename the pokemon to <Name> IV%
								pokemon.renamePokemon(name + " " + iv + "%");
								//Set pokemon with IV above 90% as favorite
								if (iv > 90) {
									pokemon.setFavoritePokemon(true);
								}
							}
						}
						//Wait for animation before catching next pokemon
						Thread.sleep(3000 + random.nextInt(1000));
					} else {
						System.out.println("Skipping Pokemon, we have no Pokeballs!");
					}
				} else {
					System.out.println("Encounter failed. " + encResult.getStatus());
				}
			}
		} catch (InterruptedException e) {
			return;
		} catch (HashException e) {
			Log.e("Main ", "Failed to login to the Hash Service: ", e);
		}
	}
}
