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


/**
 * @deprecated Replaced by {@link PokeDictionary}
 */
@Deprecated
public class PokeNames {
	/**
	 * Returns the Name for a Pokedex ID including known translations.
	 * @deprecated Replaced by {@link PokeDictionary#getDisplayName(int, Locale)}
	 * @param pokedex Pokemon index number
	 * @param locale target name locale
	 * @return the Pokemon name in locale
	 * @throws MissingResourceException if can not find a matched Pokemon name for the given pokedex
	 */
	@Deprecated
	public static String getDisplayName(int pokedex, Locale locale) throws MissingResourceException {
		return PokeDictionary.getDisplayName(pokedex, locale);
	}

	/**
	 * Returns translated Pokemon name from ENGLISH locale.
	 *
	 * @deprecated Replaced by {@link PokeDictionary#translateName(String, Locale)}
	 * @param engName pokemon ENGLISH name
	 * @param newLocale the locale you want translate to
	 * @return translated pokemon name
	 * @throws MissingResourceException if can not find a matched Pokemon name for the given pokedex
	 */
	@Deprecated
	public static String translateName(String engName, Locale newLocale) throws MissingResourceException {
		return PokeDictionary.translateName(engName, newLocale);
	}

	/**
	 * Returns the Pokemon index from the Pokemon name list.
	 *
	 * @deprecated Replaced by {@link PokeDictionary#getPokedexFromName(String, Locale)}
	 * @param pokeName pokemon name in locale
	 * @param locale the locale on this name
	 * @return pokedex
	 * @throws MissingResourceException if can not find a matched Pokemon name for the given pokedex
	 */
	@Deprecated
	public static int getPokedexFromName(String pokeName, Locale locale) throws MissingResourceException {
		return PokeDictionary.getPokedexFromName(pokeName, locale);
	}

	/**
	 * Returns the Pokemon index from the Pokemon name list in ENGLISH.
	 *
	 * @deprecated Replaced by {@link PokeDictionary#getPokedexFromName(String)}
	 * @param pokeName the Pokemon ENGLISH name
	 * @return pokedex
	 * @throws MissingResourceException if can not find a matched Pokemon name for the given pokedex
	 */
	@Deprecated
	public static int getPokedexFromName(String pokeName) throws MissingResourceException {
		return PokeDictionary.getPokedexFromName(pokeName);
	}
}
