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

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.*;

public class Evolutions {
	private static final Map<PokemonId, Evolution> EVOLUTIONS = new HashMap<>();

	static {
		registerEvolution(BULBASAUR, IVYSAUR, VENUSAUR);
		registerEvolution(CHARMANDER, CHARMELEON, CHARIZARD);
		registerEvolution(SQUIRTLE, WARTORTLE, BLASTOISE);
		registerEvolution(CATERPIE, METAPOD, BUTTERFREE);
		registerEvolution(WEEDLE, KAKUNA, BEEDRILL);
		registerEvolution(PIDGEY, PIDGEOTTO, PIDGEOT);
		registerEvolution(RATTATA, RATICATE);
		registerEvolution(SPEAROW, FEAROW);
		registerEvolution(EKANS, ARBOK);
		registerEvolution(PIKACHU, RAICHU);
		registerEvolution(SANDSHREW, SANDSLASH);
		registerEvolution(NIDORAN_FEMALE, NIDORINA, NIDOQUEEN);
		registerEvolution(NIDORAN_MALE, NIDORINO, NIDOKING);
		registerEvolution(CLEFAIRY, CLEFABLE);
		registerEvolution(VULPIX, NINETALES);
		registerEvolution(JIGGLYPUFF, WIGGLYTUFF);
		registerEvolution(ZUBAT, GOLBAT);
		registerEvolution(ODDISH, GLOOM);
		registerEvolution(PARAS, PARASECT);
		registerEvolution(VENONAT, VENOMOTH);
		registerEvolution(DIGLETT, DUGTRIO);
		registerEvolution(MEOWTH, PERSIAN);
		registerEvolution(PSYDUCK, GOLDUCK);
		registerEvolution(MANKEY, PRIMEAPE);
		registerEvolution(GROWLITHE, ARCANINE);
		registerEvolution(POLIWAG, POLIWHIRL, POLIWRATH);
		registerEvolution(ABRA, KADABRA, ALAKAZAM);
		registerEvolution(MACHOP, MACHOKE, MACHAMP);
		registerEvolution(BELLSPROUT, WEEPINBELL, VICTREEBEL);
		registerEvolution(TENTACOOL, TENTACRUEL);
		registerEvolution(GEODUDE, GRAVELER, GOLEM);
		registerEvolution(PONYTA, RAPIDASH);
		registerEvolution(SLOWPOKE, SLOWBRO);
		registerEvolution(MAGNEMITE, MAGNETON);
		registerEvolution(DODUO, DODRIO);
		registerEvolution(SEEL, DEWGONG);
		registerEvolution(GRIMER, MUK);
		registerEvolution(SHELLDER, CLOYSTER);
		registerEvolution(GASTLY, HAUNTER, GENGAR);
		registerEvolution(DROWZEE, HYPNO);
		registerEvolution(KRABBY, KINGLER);
		registerEvolution(VOLTORB, ELECTRODE);
		registerEvolution(EXEGGCUTE, EXEGGUTOR);
		registerEvolution(CUBONE, MAROWAK);
		registerEvolution(KOFFING, WEEZING);
		registerEvolution(RHYHORN, RHYDON);
		registerEvolution(HORSEA, SEADRA);
		registerEvolution(GOLDEEN, SEAKING);
		registerEvolution(STARYU, STARMIE);
		registerEvolution(MAGIKARP, GYARADOS);
		registerEvolution(EEVEE, new PokemonId[]{VAPOREON, JOLTEON, FLAREON});
		registerEvolution(OMANYTE, OMASTAR);
		registerEvolution(KABUTO, KABUTOPS);
		registerEvolution(DRATINI, DRAGONAIR, DRAGONITE);
	}

	/**
	 * Registers the given evolution chain.
	 *
	 * @param evolutionChain the evolution chain, made up of arrays or individual pokemon ids
	 */
	private static void registerEvolution(Object... evolutionChain) {
		PokemonId[] parents = null;
		for (int stage = 0; stage < evolutionChain.length; stage++) {
			PokemonId[] pokemons = get(evolutionChain[stage]);
			if (pokemons != null) {
				for (PokemonId pokemon : pokemons) {
					EVOLUTIONS.put(pokemon, new Evolution(parents, pokemon, stage));
					if (parents != null) {
						for (PokemonId parent : parents) {
							Evolution parentEvolution = EVOLUTIONS.get(parent);
							parentEvolution.addEvolution(pokemon);
						}
					}
				}
				parents = pokemons;
			}
		}
	}

	/**
	 * Gets a PokemonId array from the given object
	 *
	 * @param object the object to cast
	 * @return a PokemonId array
	 */
	private static PokemonId[] get(Object object) {
		if (object instanceof PokemonId[]) {
			return (PokemonId[]) object;
		} else if (object instanceof PokemonId) {
			return new PokemonId[]{(PokemonId) object};
		}
		return null;
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
			if (evolution.getParents() != null) {
				for (PokemonId parent : evolution.getParents()) {
					basic.addAll(getBasic(parent));
				}
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
