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

import com.pokegoapi.util.hash.HashProvider;
import com.pokegoapi.util.hash.legacy.LegacyHashProvider;
import com.pokegoapi.util.hash.pokehash.PokeHashKey;
import com.pokegoapi.util.hash.pokehash.PokeHashProvider;

/**
 * Created by court on 19/07/2016.
 */
public class ExampleConstants {

	public static final String LOGIN = "";
	public static final String PASSWORD = "";
	public static final double LATITUDE = -32.058087;
	public static final double LONGITUDE = 115.744325;
	public static final double ALTITUDE = Math.random() * 15.0;
	public static final String POKEHASH_KEY = "";

	/**
	 * Creates the appropriate hash provider, based on if the POKEHASH_KEY property is set or not
	 * @return a hash provider
	 */
	public static HashProvider getHashProvider() {
		boolean hasKey = POKEHASH_KEY != null && POKEHASH_KEY.length() > 0;
		if (hasKey) {
			return new PokeHashProvider(PokeHashKey.from(POKEHASH_KEY), true);
		} else {
			return new LegacyHashProvider();
		}
	}
}
