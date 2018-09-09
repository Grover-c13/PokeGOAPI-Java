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
import com.pokegoapi.api.map.Point;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.fort.PokestopLootResult;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.hash.HashProvider;
import com.pokegoapi.util.path.Path;
import okhttp3.OkHttpClient;

import java.util.Set;

public class TravelToPokestopExample {

	/**
	 * Travels to a Pokestop and loots it
	 *
	 * @param args args
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		PokemonGo api = new PokemonGo(http);
		try {
			HashProvider hasher = ExampleConstants.getHashProvider();
			api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD), hasher);
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);

			Set<Pokestop> pokestops = api.getMap().mapObjects.pokestops;
			System.out.println("Found " + pokestops.size() + " pokestops in the current area.");

			Pokestop destinationPokestop = null;
			for (Pokestop pokestop : pokestops) {
				//Check if not in range and if it is not on cooldown
				if (!pokestop.inRange() && pokestop.canLoot(true)) {
					destinationPokestop = pokestop;
					break;
				}
			}

			if (destinationPokestop != null) {
				Point destination = new Point(destinationPokestop.getLatitude(), destinationPokestop.getLongitude());
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
						System.out.println("Time left: " + (int) (path.getTimeLeft(api) / 1000) + " seconds.");
						//Sleep for 2 seconds before setting the location again
						Thread.sleep(2000);
					}
				} catch (InterruptedException e) {
					return;
				}
				System.out.println("Finished traveling to pokestop!");
				if (destinationPokestop.inRange()) {
					System.out.println("Looting pokestop...");
					PokestopLootResult result = destinationPokestop.loot();
					System.out.println("Pokestop loot returned result: " + result.getResult());
				} else {
					System.out.println("Something went wrong! We're still not in range of the destination pokestop!");
				}
			} else {
				System.out.println("Couldn't find out of range pokestop to travel to!");
			}
		} catch (RequestFailedException e) {
			Log.e("Main", "Failed to login, captcha or server issue: ", e);
		}
	}
}
