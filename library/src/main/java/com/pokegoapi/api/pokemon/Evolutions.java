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

import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ABRA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ALAKAZAM;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ARBOK;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ARCANINE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.BEEDRILL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.BELLSPROUT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.BLASTOISE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.BULBASAUR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.BUTTERFREE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CATERPIE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CHARIZARD;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CHARMANDER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CHARMELEON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CLEFABLE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CLEFAIRY;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CLOYSTER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CUBONE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DEWGONG;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DIGLETT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DODRIO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DODUO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DRAGONAIR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DRAGONITE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DRATINI;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DROWZEE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DUGTRIO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.EEVEE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.EKANS;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ELECTRODE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.EXEGGCUTE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.EXEGGUTOR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.FEAROW;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.FLAREON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GASTLY;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GENGAR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GEODUDE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GLOOM;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GOLBAT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GOLDEEN;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GOLDUCK;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GOLEM;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GRAVELER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GRIMER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GROWLITHE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.GYARADOS;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.HAUNTER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.HORSEA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.HYPNO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.IVYSAUR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.JIGGLYPUFF;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.JOLTEON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KABUTO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KABUTOPS;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KADABRA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KAKUNA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KINGLER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KOFFING;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KRABBY;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MACHAMP;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MACHOKE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MACHOP;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MAGIKARP;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MAGNEMITE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MAGNETON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MANKEY;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MAROWAK;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MEOWTH;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.METAPOD;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MUK;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.NIDOKING;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.NIDOQUEEN;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.NIDORAN_FEMALE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.NIDORAN_MALE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.NIDORINA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.NIDORINO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.NINETALES;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ODDISH;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.OMANYTE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.OMASTAR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PARAS;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PARASECT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PERSIAN;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PIDGEOT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PIDGEOTTO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PIDGEY;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PIKACHU;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.POLIWAG;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.POLIWHIRL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.POLIWRATH;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PONYTA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PRIMEAPE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PSYDUCK;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.RAICHU;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.RAPIDASH;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.RATICATE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.RATTATA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.RHYDON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.RHYHORN;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SANDSHREW;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SANDSLASH;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SEADRA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SEAKING;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SEEL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SHELLDER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SLOWBRO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SLOWPOKE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SPEAROW;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SQUIRTLE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.STARMIE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.STARYU;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.TENTACOOL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.TENTACRUEL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VAPOREON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VENOMOTH;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VENONAT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VENUSAUR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VICTREEBEL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VOLTORB;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VULPIX;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.WARTORTLE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.WEEDLE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.WEEPINBELL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.WEEZING;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.WIGGLYTUFF;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ZUBAT;

public class Evolutions {
	private static final Map<PokemonId, Evolution> EVOLUTIONS = new HashMap<>();

	static {
		registerEvolution(BULBASAUR, IVYSAUR, VENUSAUR);
		registerEvolution(CHARMANDER, CHARMELEON, CHARIZARD);
		registerEvolution(CATERPIE, METAPOD, BUTTERFREE);
		registerEvolution(SQUIRTLE, WARTORTLE, BLASTOISE);
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
