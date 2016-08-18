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
import com.pokegoapi.util.PokeNames;

import java.util.Locale;
import java.util.MissingResourceException;

public class DisplayPokenameExample {

	/**
	 * Displays All 151 Pokemon Names for all Supported Locales
	 *
	 * @param args Not used
	 */
	public static void main(String[] args) {
		Locale[] supportedLocales = {
				Locale.GERMAN,
				Locale.ENGLISH,
				new Locale("es"),
				Locale.FRENCH,
				Locale.ITALIAN,
				Locale.JAPANESE,
				Locale.KOREAN,
				new Locale("ru"),
				new Locale("zh", "CN"),
				new Locale("zh", "HK"),
				new Locale("zh", "TW"),
		};

		for (int i = 1; i < 152; i++) {
			//Showcase for Supported Languages
			for (Locale l : supportedLocales) {
				try {
					Log.d("Names-Example", String.format(
							l,
							"Pokedex# %d is %s in %s",
							i,
							PokeDictionary.getDisplayName(i, l),
							l.getDisplayName(l)));
				} catch (MissingResourceException e) {
					Log.e("Main", "Unable to find Pokemon name with given Pokedex: ", e);
				}
			}
			//Showcase for Fallback Behaviourn
			try {
				Log.d("Names-Example", String.format(
						"Pokedex# %d is %s in %s",
						i,
						PokeDictionary.getDisplayName(i, new Locale("xx")), "Fallback"));
			} catch (MissingResourceException e) {
				Log.e("Main", "Unable to find Pokemon name with given Pokedex: ", e);
			}
		}

		for (int i = 1; i < 152; i++) {
			//Translate English Pokemon name to Simplified Chinese
			Locale chs = new Locale("zh", "CN");
			try {
				Log.d("Translate English Names to Simplified Chinese - Example", String.format(
						chs,
						"Pokedex# %d is %s in %s",
						i,
						PokeDictionary.translateName(PokeDictionary.getDisplayName(i, Locale.ENGLISH), chs),
						chs.getDisplayName(chs)));
			} catch (MissingResourceException e) {
				Log.e("Main", "Unable to find Pokemon name with given Pokedex: ", e);
			}
		}
	}
}
