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


import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.MapObjects;
import com.pokegoapi.api.map.Point;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.Encounter;
import com.pokegoapi.api.map.pokemon.NearbyPokemon;
import com.pokegoapi.api.map.pokemon.ThrowProperties;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.api.settings.PokeballSelector;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.request.RequestFailedException;
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

			MapObjects mapObjects = api.getMap().mapObjects;
			//Find all pokestops with pokemon nearby
			List<Pokestop> travelPokestops = new ArrayList<>();
			Set<NearbyPokemon> nearby = mapObjects.nearby;
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
					double lat = api.latitude;
					double lng = api.longitude;
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
					while (!path.complete) {
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
		} catch (NoSuchItemException | RequestFailedException e) {
			Log.e("Main", "An exception occurred while running example: ", e);
		}
	}

	private static void catchArea(PokemonGo api) throws RequestFailedException, NoSuchItemException {
		ItemBag bag = api.inventories.itemBag;
		try {
			//Wait until map is updated for the current location
			api.getMap().awaitUpdate();

			Set<CatchablePokemon> catchablePokemon = api.getMap().mapObjects.getPokemon();
			System.out.println("Pokemon in area: " + catchablePokemon.size());

			Random random = new Random();
			PokeBank pokebank = api.inventories.pokebank;

			for (CatchablePokemon cp : catchablePokemon) {
				// Encounter this pokemon
				Encounter encounter = cp.encounter();

				// If the encounter was successful, attempt to catch this pokemon
				if (encounter.isSuccessful()) {
					System.out.println("Encountered: " + cp.pokemonId);

					List<Pokeball> usablePokeballs = bag.getUsablePokeballs();

					if (usablePokeballs.size() > 0) {
						//Select pokeball with smart selector to print what pokeball is used
						double probability = encounter.getCaptureProbability();
						Pokeball pokeball = PokeballSelector.SMART.select(usablePokeballs, probability);
						System.out.println("Attempting to catch: " + cp.pokemonId + " with " + pokeball
								+ " (" + probability + ")");

						// Throw pokeballs until capture or flee
						while (encounter.isActive()) {
							// Wait between Pokeball throws
							Thread.sleep(500 + random.nextInt(1000));

							// If no item is active, use a razzberry
							int razzberryCount = bag.getItem(POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_RAZZ_BERRY).count;
							if (encounter.getActiveItem() == null && razzberryCount > 0) {
								encounter.useItem(ItemId.ITEM_RAZZ_BERRY);
							}

							// Throw pokeball with random properties
							encounter.throwPokeball(PokeballSelector.SMART, ThrowProperties.random());

							if (encounter.status == CatchStatus.CATCH_SUCCESS) {
								// Print pokemon stats
								Pokemon pokemon = pokebank.getPokemonById(encounter.capturedPokemon);
								if (pokemon != null) {
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
						}
					} else {
						System.out.println("Skipping Pokemon, we have no Pokeballs!");
					}

					// Wait for animation before catching next pokemon
					Thread.sleep(3000 + random.nextInt(1000));
				} else {
					System.out.println("Failed to encounter pokemon: " + encounter.encounterResult);
				}
			}
		} catch (InterruptedException e) {
			return;
		}
	}
}
