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
import com.pokegoapi.auth.GoogleAutoCredentialProvider;
import com.pokegoapi.main.BlockingCallback;
import com.pokegoapi.main.PokemonCallback;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.SystemTimeImpl;
import okhttp3.OkHttpClient;

public class UseIncenseExample {

	/**
	 * Catches a pokemon at an area.
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();

		PokemonGo go = new PokemonGo(http, new SystemTimeImpl());

		try {
			GoogleAutoCredentialProvider authProvider =
					new GoogleAutoCredentialProvider(http, ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD);
			//new PtcLogin(http).login(ExampleLoginDetails.LOGIN, ExampleLoginDetails.PASSWORD);

			BlockingCallback callback = new BlockingCallback();
			go.login(authProvider, callback);
			callback.block();

			go.setLocation(-32.058087, 115.744325, 0);
			go.getInventories().getItemBag().useIncense(new PokemonCallback() {
				@Override
				public void onCompleted(Exception e) {
					System.out.println("Incense was used!");
					if (e != null) {
						Log.e("Main", "Error while placing incense", e);
					}
				}
			});

		} catch (Exception e) {
			Log.e("Main", "Failed to login or server issue: ", e);
		}
	}
}
