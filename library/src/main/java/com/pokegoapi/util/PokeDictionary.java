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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Offers methods to get information about pokémon as seen in the pokédex.
 */
public class PokeDictionary {
	private static final String POKE_NAMES_BUNDLE = "pokemon_names";
	private static final String POKE_DESCRIPTIONS_BUNDLE ="pokemon_descriptions";

	private static ResourceBundle getPokeBundle(String bundleBaseName, Locale locale)
			throws MissingResourceException{
		return ResourceBundle.getBundle(bundleBaseName, locale);
	}

	/**
	 * Returns the Pokédex Name for a Pokedex ID including known translations.
	 *
	 * @param pokedexId Pokemon index number
	 * @param locale taget name locale
	 * @return the Pokemon name in locale
	 * @throws MissingResourceException if can not find a matched Pokemon name for the given pokedex
	 */
	public static String getDisplayName(int pokedexId, Locale locale)
			throws MissingResourceException {
		return getPokeBundle(POKE_NAMES_BUNDLE, locale).getString(String.valueOf(pokedexId));
	}

	/**
	 * Returns the Pokédex Description for a Pokédex ID including known translations.
	 *
	 * @param pokedexId Pokemon index number
	 * @param locale target name locale
	 * @return the Pokemon description in locale
	 * @throws MissingResourceException if can not find a matched Pokemon description for the given pokedex
	 */
	public static String getDisplayDescription(int pokedexId, Locale locale)
			throws MissingResourceException {
		return getPokeBundle(POKE_DESCRIPTIONS_BUNDLE, locale).getString(String.valueOf(pokedexId));
	}

	/**
	 * Returns translated Pokemon name from ENGLISH locale.
	 *
	 * @param engName pokemon ENGLISH name
	 * @param newLocale the locale you want translate to
	 * @return translated pokemon name
	 * @throws MissingResourceException if can not find a matched Pokemon name for the given pokedex
	 */
	public static String translateName(String engName, Locale newLocale)
			throws MissingResourceException {
		return getDisplayName(getPokedexFromName(engName), newLocale);
	}

	/**
	 * Returns the Pokemon index from the Pokemon name list.
	 *
	 * @param pokeName pokemon name in locale
	 * @param locale the locale on this name
	 * @return pokedex Pokedex Id if a Pokemon with the given pokedex id exists, else -1.
	 * @throws MissingResourceException if can not find a matched Pokemon name for the given pokedex
	 */
	public static int getPokedexFromName(String pokeName, Locale locale)
			throws MissingResourceException {
		ResourceBundle nameList = getPokeBundle(pokeName, locale);
		for (String key : nameList.keySet()) {
			if (nameList.getString(key).equalsIgnoreCase(pokeName)) {
				return Integer.parseInt(key);
			}
		}
		return -1;
	}

	/**
	 * Returns the Pokemon index from the Pokemon name list in ENGLISH.
	 *
	 * @param pokeName the Pokemon ENGLISH name
	 * @return pokedex
	 * @throws MissingResourceException if can not find a matched Pokemon name for the given pokedex
	 */
	public static int getPokedexFromName(String pokeName)
			throws MissingResourceException {
		return getPokedexFromName(pokeName, Locale.ENGLISH);
	}
}
