package com.pokegoapi.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * +        o     o       +        o
 * -_-_-_-_-_-_-_,------,      o
 * _-_-_-_-_-_-_-|   /\_/\
 * -_-_-_-_-_-_-~|__( ^ .^)  +     +
 * _-_-_-_-_-_-_-"  ""
 * +      o         o   +       o
 *
 * @author Angelo Rüggeberg
 */

public class PokeNames {
	/**
	 * Returns the Name for a Pokedex ID including known translations.
	 * @param pokedexNr
	 * @param locale
	 * @return
	 */
	public static String getDisplayName(int pokedexNr, Locale locale) {
		ResourceBundle names = ResourceBundle.getBundle("pokemon_names", locale);
		return names.getString(String.valueOf(pokedexNr));
	}
}
