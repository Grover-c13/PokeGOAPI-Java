package com.pokegoapi.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PokeDescriptions {
	/**
	 * Returns the Pokédex Description for a Pokédex ID including known translations.
	 *
	 * @param pokedex Pokemon index number
	 * @param locale target name locale
	 * @return the Pokemon name in locale
	 * @throws MissingResourceException if can not find a matched Pokemon description for the given pokedex
	 */
	public static String getDisplayDescription(int pokedex, Locale locale) throws MissingResourceException {
		ResourceBundle names = ResourceBundle.getBundle("pokemon_descriptions", locale);
		return names.getString(String.valueOf(pokedex));
	}
}
