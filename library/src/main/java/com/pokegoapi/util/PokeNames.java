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
import java.util.MissingResourceException;

/**
 * @author Angelo Rüggeberg
 */

public class PokeNames {
	/**
	 * Returns the Name for a Pokedex ID including known translations.
	 *
	 * @param pokedexNr pokedex number
	 * @param locale    locale
	 * @return the pokemon name locale
	 */
	public static String getDisplayName(int pokedexNr, Locale locale) {
		ResourceBundle names = ResourceBundle.getBundle("pokemon_names", locale);
		return names.getString(String.valueOf(pokedexNr));
	}

	/**
	 * Returns translated Pokemon name from english locale.
	 * @param engName pokemon english name
	 * @param newLocale target locale
	 * @return translated pokemon name
	 */
	public static String translateName(String engName, Locale newLocale) {
		ResourceBundle engNameList = ResourceBundle.getBundle("pokemon_names");
		ResourceBundle translatedNameList = ResourceBundle.getBundle("pokemon_names", newLocale);

		String nameKey = "", translatedName = "", engNameUTF8 = "";

		String compareName = engName;
		if (engName.indexOf("_FEMALE") >= 0)
			compareName = compareName.replace("_FEMALE", "♀");
		if (engName.indexOf("_MALE") >= 0)
			compareName = compareName.replace("_MALE", "♂");

		for (String key : engNameList.keySet()) {
			try {
				engNameUTF8 = new String(engNameList.getString(key).getBytes("ISO-8859-1"), "UTF-8");
				if (engNameUTF8.equalsIgnoreCase(compareName)) {
					nameKey = key;
					break;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("Error on coverting name string " + engNameList.getString(key) + " to UTF-8");
			}
		}

		try {
			translatedName = translatedNameList.getString(nameKey);
		} catch (MissingResourceException e) {
			e.printStackTrace();
			System.out.println("Error on finding Pokemon name: " + engNameUTF8);
		}

		if (newLocale == Locale.FRENCH) {
			try {
				return new String(translatedName.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return translatedName;
	}
}
