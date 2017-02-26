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

package com.pokegoapi.api.pokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Networking.Responses.DownloadItemTemplatesResponseOuterClass.DownloadItemTemplatesResponse.ItemTemplate;
import POGOProtos.Settings.Master.PokemonSettingsOuterClass.PokemonSettings;

public class Evolutions {
	private static final Map<PokemonId, Evolution> EVOLUTIONS = new HashMap<>();

	/**
	 * Initializes these evolutions from PokemonSettings
	 *
	 * @param templates the templates to initialize from
	 */
	public static void initialize(List<ItemTemplate> templates) {
		EVOLUTIONS.clear();
		for (ItemTemplate template : templates) {
			if (template.hasPokemonSettings()) {
				PokemonSettings settings = template.getPokemonSettings();
				PokemonId pokemon = settings.getPokemonId();
				if (!EVOLUTIONS.containsKey(pokemon)) {
					addEvolution(null, pokemon);
				}
			}
		}
	}
	
	/**
	 * Auxiliar method to add the evolution by recursion in the EVOLUTIONS Map
	 *
	 * @param parent the parent of this pokemon
	 * @param pokemon the pokemon that evolution will be added
	 */
	private static void addEvolution(PokemonId parent, PokemonId pokemon) {
		Evolution evolution = new Evolution(parent, pokemon);
		EVOLUTIONS.put(pokemon, evolution);
		for (PokemonId poke : evolution.getEvolutions()) {
			addEvolution(pokemon, poke);
		}
	}

	/**
	 * Returns the evolution data for the given pokemon
	 *
	 * @param pokemon the pokemon to get data for
	 * @return the evolution data
	 */
	public static Evolution getEvolution(PokemonId pokemon) {
		return EVOLUTIONS.get(pokemon);
	}

	/**
	 * Returns the possible evolutions for the given pokemon.
	 *
	 * @param pokemon the pokemon to get data for
	 * @return the evolutions from this pokemon
	 */
	public static List<PokemonId> getEvolutions(PokemonId pokemon) {
		Evolution evolution = getEvolution(pokemon);
		if (evolution != null) {
			return evolution.getEvolutions();
		}
		return new ArrayList<>();
	}

	/**
	 * Gets the most basic (lowest evolution stage) pokemon in the given evolution chain.
	 *
	 * @param pokemon the pokemon to find the lowest evolution for
	 * @return the lowest evolution for the given pokemon
	 */
	public static List<PokemonId> getBasic(PokemonId pokemon) {
		List<PokemonId> basic = new ArrayList<>();
		Evolution evolution = getEvolution(pokemon);
		if (evolution != null) {
			if (evolution.getParent() != null) {
				basic.add(evolution.getParent());
			} else {
				basic.add(pokemon);
			}
			return basic;
		} else {
			basic.add(pokemon);
			return basic;
		}
	}

	/**
	 * Gets the highest evolution pokemon in the given evolution chain.
	 *
	 * @param pokemon the pokemon to find the highest evolution for
	 * @return the highest evolution for the given pokemon
	 */
	public static List<PokemonId> getHighest(PokemonId pokemon) {
		List<PokemonId> highest = new ArrayList<>();
		Evolution evolution = getEvolution(pokemon);
		if (evolution != null) {
			if (evolution.getEvolutions() != null && evolution.getEvolutions().size() > 0) {
				for (PokemonId child : evolution.getEvolutions()) {
					highest.addAll(getHighest(child));
				}
			} else {
				highest.add(pokemon);
			}
			return highest;
		} else {
			highest.add(pokemon);
			return highest;
		}
	}

	/**
	 * Returns if this pokemon can be evolved any more than it already is
	 *
	 * @param pokemon the pokemon
	 * @return if this pokemon can be evolved
	 */
	public static boolean canEvolve(PokemonId pokemon) {
		Evolution evolution = getEvolution(pokemon);
		return evolution != null && evolution.getEvolutions() != null && evolution.getEvolutions().size() > 0;
	}
}
