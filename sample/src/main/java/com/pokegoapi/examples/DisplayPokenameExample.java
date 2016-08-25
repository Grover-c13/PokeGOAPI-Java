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
import com.pokegoapi.util.PokeDictionary;

import java.util.Locale;
import java.util.MissingResourceException;

import static com.pokegoapi.util.PokeDictionary.supportedLocales;

public class DisplayPokenameExample {

	/**
	 * Displays All 151 Pokemon Names for all Supported Locales
	 *
	 * @param args Not used
	 */
	public static void main(String[] args) {
		for (int i = 1; i < 152; i++) {
			//Showcase for Supported Languages
			for (Locale l : supportedLocales) {
				try {
					System.out.println(String.format(
							l,
							"%s: Pokedex #%d is %s\n    %s",
							l.getDisplayName(l),
							i,
							PokeDictionary.getDisplayName(i, l),
							PokeDictionary.getDisplayDescription(i, l)));
				} catch (MissingResourceException e) {
					Log.e("Main", "Unable to find Pokemon name with given Pokedex: " + i, e);
				}
			}
			//Showcase for Fallback Behaviour
			try {
				System.out.println(String.format(
						"%s: Pokedex# %d is %s\n    %s",
						"Fallback",
						i,
						PokeDictionary.getDisplayName(i, new Locale("xx")),
						PokeDictionary.getDisplayDescription(i, new Locale("xx"))));
			} catch (MissingResourceException e) {
				Log.e("Main", "Unable to find Pokemon name with given Pokedex: ", e);
			}
			System.out.println();
		}

	}
}
