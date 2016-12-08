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
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.path.Path;
import okhttp3.OkHttpClient;

import java.util.Collection;

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
			api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD));
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);

			Collection<Pokestop> pokestops = api.getMap().getMapObjects().getPokestops();
			System.out.println("Found " + pokestops.size() + " pokestops in the current area.");

			Pokestop destinationPokestop = null;
			for (Pokestop pokestop : pokestops) {
				if (!pokestop.inRange()) {
					destinationPokestop = pokestop;
				}
			}

			if (destinationPokestop != null) {
				Point destination = new Point(destinationPokestop.getLatitude(), destinationPokestop.getLongitude());
				//Use the current player position as the source and the pokestop position as the destination
				//Travel to Pokestop at 15KMPH
				Path path = new Path(api.getPoint(), destination, 15.0);
				System.out.println("Traveling to " + destination + " at 15KMPH!");
				path.start(api);
				try {
					while (!path.isComplete()) {
						//Calculate the desired intermediate point for the current time
						Point point = path.calculateIntermediate(api);
						//Set the API location to that point
						api.setLatitude(point.getLatitude());
						api.setLongitude(point.getLongitude());
						System.out.println("Move to " + point);
						//Sleep for 2 seconds before setting the location again
						Thread.sleep(2000);
					}
				} catch (InterruptedException e) {
					//Do nothing
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
		} catch (LoginFailedException | RemoteServerException | CaptchaActiveException e) {
			Log.e("Main", "Failed to login, captcha or server issue: ", e);
		}
	}
}
