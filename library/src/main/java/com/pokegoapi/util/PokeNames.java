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

package com.pokegoapi.util;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Angelo Rüggeberg
 */

public class PokeNames {
	/**
	 * Returns the Name for a Pokedex ID including known translations.
	 * @param pokedexNr pokedex number
	 * @param locale locale
	 * @return the pokemon name locale
	 */
	public static String getDisplayName(int pokedexNr, Locale locale) {
		ResourceBundle names = ResourceBundle.getBundle("pokemon_names", locale);
		String s = names.getString(String.valueOf(pokedexNr));
		if (locale == Locale.FRENCH)
			try {
				return new String(s.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		return s;
	}
}
