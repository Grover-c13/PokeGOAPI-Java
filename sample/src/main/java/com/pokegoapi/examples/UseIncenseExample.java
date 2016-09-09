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
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.SystemTimeImpl;
import com.pokegoapi.util.hash.HashProvider;
import okhttp3.OkHttpClient;

public class UseIncenseExample {
	/**
	 * Catches a pokemon at an area.
	 */
	public static void main(String[] args) {
		OkHttpClient http = new OkHttpClient();

		PokemonGo api = new PokemonGo(http, new SystemTimeImpl());

		try {
			HashProvider hasher = ExampleConstants.getHashProvider();
			api.login(new PtcCredentialProvider(http, ExampleConstants.LOGIN, ExampleConstants.PASSWORD), hasher);
			api.setLocation(ExampleConstants.LATITUDE, ExampleConstants.LONGITUDE, ExampleConstants.ALTITUDE);
			api.inventories.itemBag.useIncense();
		} catch (RequestFailedException e) {
			// failed to login, invalid credentials, auth issue or server issue.
			Log.e("Main", "Failed to login, captcha or server issue: ", e);
		}
	}
}
