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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.fort.Raid;
import com.pokegoapi.auth.GoogleAutoCredentialProvider;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.hash.HashProvider;
import okhttp3.OkHttpClient;

public class ScanRaidExample {
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();
		final PokemonGo api = new PokemonGo(http);
		try {
			HashProvider hasher = ExampleConstants.getHashProvider();
			/*
			 * you can using Ptc or Google
			 */
			api.login(new GoogleAutoCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD), hasher);
			//api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD), hasher);
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);
			
			/*
			 * Loop until timeout or get the raid
			 */
			int count = 0;
			do {
				if (count > 60) {
					throw new Exception("Get Map Timeout !");
				}
				if (api.getMap().getMapObjects().getRaids().size() != 0) {
					count++;
					Thread.sleep(1000);
					break;
				}
			} while (true);
			
			/*
			 * assign Raids to Set list
			 */
			Set<Raid> raids = api.getMap().getMapObjects().getRaids();
			for (Raid raid : raids) {
				/*
				 * output the raid data
				 */
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				System.out.println("Raid : " + raid.getGym().getName());
				System.out.println("Boss : " + raid.getRaidPokemon().getPokemonId());
				System.out.println("Team : " + raid.getGym().getOwnedByTeam());
				System.out.println("Level : " + raid.getRaidLevel().getNumber());
				System.out.println("Start time : " + format.format(new Date(raid.getRaidBattleMs())));
				System.out.println("End time : " + format.format(new Date(raid.getRaidEndMs())));
				System.out.println("Location : " + raid.getLatitude() + "," + raid.getLongitude() );
				System.out.println("Raid ID : " + raid.getRaidSeed());
			}
			
		} catch (Exception e) {
			Log.e("Main", "An exception occurred while running example: ", e);
		}
	}
}
