package com.pokegoapi.examples;

import com.pokegoapi.util.Log;
import com.pokegoapi.util.PokeNames;

import java.util.Locale;

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
