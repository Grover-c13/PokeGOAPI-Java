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

import com.pokegoapi.util.Log;
import com.pokegoapi.util.PokeNames;

import java.util.Locale;

public class DisplayPokenameExample {

	/**
	 * Displays All 151 Pokemon Names for all Supported Locales
	 * @param args Not used
	 */
	public static void main(String[] args) {
		Locale[] supportedLocales = {
				Locale.FRENCH,
				Locale.GERMAN,
				Locale.ENGLISH,
				Locale.JAPANESE,
				new Locale("zh", "CN"),
				new Locale("zh", "HK"),
				new Locale("ru"),
		};
		for (int i = 1; i < 152; i++) {
			//Showcase for Supported Languages
			for (Locale l : supportedLocales) {
				Log.d("Names-Example", String.format(
						l,
						"Pokedex Nr# %d is %s in %s",
						i,
						PokeNames.getDisplayName(i, l),
						l.getDisplayName(l)));
			}
			//Showcase for Fallback Behaviour
			Log.d("Names-Example", String.format(
					"Pokedex Nr# %d is %s in %s",
					i,
					PokeNames.getDisplayName(i, new Locale("xx")), "Fallback"));
		}
	}
}
