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

/**
 * Created by Angelo on 18.08.2016.
 */
public class TranslatePokenameExample {
	/**
	 * Displays All 151 Pokemon Names for all Supported Locales
	 *
	 * @param args Not used
	 */
	public static void main(String[] args) {
		// Translate English Pokemon name to Simplified Chinese
		// Note:    You can use PokeDictionary.getDisplayName(int pokedexId, Locale locale)
		//          instead, if you already know the Pokedex Id.
		//          See DisplayPokenameExample for an example.
		Locale chs = new Locale("zh", "CN");
		for (int i = 1; i < 152; i++) {
			try {
				System.out.println(String.format(
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
