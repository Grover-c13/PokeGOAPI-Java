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

import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;

import java.util.EnumMap;

public class PokemonFamilyMap {

	private static EnumMap<PokemonId, PokemonFamilyId> familys = new EnumMap<>(PokemonId.class);
	private static EnumMap<PokemonFamilyId, PokemonId> highestForFamily = new EnumMap<>(PokemonFamilyId.class);

	static {
		familys.put(PokemonId.BULBASAUR, PokemonFamilyId.FAMILY_BULBASAUR);
		familys.put(PokemonId.IVYSAUR, PokemonFamilyId.FAMILY_BULBASAUR);
		familys.put(PokemonId.VENUSAUR, PokemonFamilyId.FAMILY_BULBASAUR);
		highestForFamily.put(PokemonFamilyId.FAMILY_BULBASAUR, PokemonId.VENUSAUR);
		familys.put(PokemonId.CHARMANDER, PokemonFamilyId.FAMILY_CHARMANDER);
		familys.put(PokemonId.CHARMELEON, PokemonFamilyId.FAMILY_CHARMANDER);
		familys.put(PokemonId.CHARIZARD, PokemonFamilyId.FAMILY_CHARMANDER);
		highestForFamily.put(PokemonFamilyId.FAMILY_CHARMANDER, PokemonId.CHARIZARD);
		familys.put(PokemonId.SQUIRTLE, PokemonFamilyId.FAMILY_SQUIRTLE);
		familys.put(PokemonId.WARTORTLE, PokemonFamilyId.FAMILY_SQUIRTLE);
		familys.put(PokemonId.BLASTOISE, PokemonFamilyId.FAMILY_SQUIRTLE);
		highestForFamily.put(PokemonFamilyId.FAMILY_SQUIRTLE, PokemonId.BLASTOISE);
		familys.put(PokemonId.CATERPIE, PokemonFamilyId.FAMILY_CATERPIE);
		familys.put(PokemonId.METAPOD, PokemonFamilyId.FAMILY_CATERPIE);
		familys.put(PokemonId.BUTTERFREE, PokemonFamilyId.FAMILY_CATERPIE);
		highestForFamily.put(PokemonFamilyId.FAMILY_CATERPIE, PokemonId.BUTTERFREE);
		familys.put(PokemonId.WEEDLE, PokemonFamilyId.FAMILY_WEEDLE);
		familys.put(PokemonId.KAKUNA, PokemonFamilyId.FAMILY_WEEDLE);
		familys.put(PokemonId.BEEDRILL, PokemonFamilyId.FAMILY_WEEDLE);
		highestForFamily.put(PokemonFamilyId.FAMILY_WEEDLE, PokemonId.BEEDRILL);
		familys.put(PokemonId.PIDGEY, PokemonFamilyId.FAMILY_PIDGEY);
		familys.put(PokemonId.PIDGEOTTO, PokemonFamilyId.FAMILY_PIDGEY);
		familys.put(PokemonId.PIDGEOT, PokemonFamilyId.FAMILY_PIDGEY);
		highestForFamily.put(PokemonFamilyId.FAMILY_PIDGEY, PokemonId.PIDGEOT);
		familys.put(PokemonId.RATTATA, PokemonFamilyId.FAMILY_RATTATA);
		familys.put(PokemonId.RATICATE, PokemonFamilyId.FAMILY_RATTATA);
		highestForFamily.put(PokemonFamilyId.FAMILY_RATTATA, PokemonId.RATICATE);
		familys.put(PokemonId.SPEAROW, PokemonFamilyId.FAMILY_SPEAROW);
		familys.put(PokemonId.FEAROW, PokemonFamilyId.FAMILY_SPEAROW);
		highestForFamily.put(PokemonFamilyId.FAMILY_SPEAROW, PokemonId.FEAROW);
		familys.put(PokemonId.EKANS, PokemonFamilyId.FAMILY_EKANS);
		familys.put(PokemonId.ARBOK, PokemonFamilyId.FAMILY_EKANS);
		highestForFamily.put(PokemonFamilyId.FAMILY_EKANS, PokemonId.ARBOK);
		familys.put(PokemonId.PIKACHU, PokemonFamilyId.FAMILY_PIKACHU);
		familys.put(PokemonId.RAICHU, PokemonFamilyId.FAMILY_PIKACHU);
		highestForFamily.put(PokemonFamilyId.FAMILY_PIKACHU, PokemonId.RAICHU);
		familys.put(PokemonId.SANDSHREW, PokemonFamilyId.FAMILY_SANDSHREW);
		familys.put(PokemonId.SANDSLASH, PokemonFamilyId.FAMILY_SANDSHREW);
		highestForFamily.put(PokemonFamilyId.FAMILY_SANDSHREW, PokemonId.SANDSLASH);
		familys.put(PokemonId.NIDORAN_FEMALE, PokemonFamilyId.FAMILY_NIDORAN_FEMALE);
		familys.put(PokemonId.NIDORINA, PokemonFamilyId.FAMILY_NIDORAN_FEMALE);
		familys.put(PokemonId.NIDOQUEEN, PokemonFamilyId.FAMILY_NIDORAN_FEMALE);
		highestForFamily.put(PokemonFamilyId.FAMILY_NIDORAN_FEMALE, PokemonId.NIDOQUEEN);
		familys.put(PokemonId.NIDORAN_MALE, PokemonFamilyId.FAMILY_NIDORAN_MALE);
		familys.put(PokemonId.NIDORINO, PokemonFamilyId.FAMILY_NIDORAN_MALE);
		familys.put(PokemonId.NIDOKING, PokemonFamilyId.FAMILY_NIDORAN_MALE);
		highestForFamily.put(PokemonFamilyId.FAMILY_NIDORAN_MALE, PokemonId.NIDOKING);
		familys.put(PokemonId.CLEFAIRY, PokemonFamilyId.FAMILY_CLEFAIRY);
		familys.put(PokemonId.CLEFABLE, PokemonFamilyId.FAMILY_CLEFAIRY);
		highestForFamily.put(PokemonFamilyId.FAMILY_CLEFAIRY, PokemonId.CLEFABLE);
		familys.put(PokemonId.VULPIX, PokemonFamilyId.FAMILY_VULPIX);
		familys.put(PokemonId.NINETALES, PokemonFamilyId.FAMILY_VULPIX);
		highestForFamily.put(PokemonFamilyId.FAMILY_VULPIX, PokemonId.NINETALES);
		familys.put(PokemonId.JIGGLYPUFF, PokemonFamilyId.FAMILY_JIGGLYPUFF);
		familys.put(PokemonId.WIGGLYTUFF, PokemonFamilyId.FAMILY_JIGGLYPUFF);
		highestForFamily.put(PokemonFamilyId.FAMILY_JIGGLYPUFF, PokemonId.WIGGLYTUFF);
		familys.put(PokemonId.ZUBAT, PokemonFamilyId.FAMILY_ZUBAT);
		familys.put(PokemonId.GOLBAT, PokemonFamilyId.FAMILY_ZUBAT);
		highestForFamily.put(PokemonFamilyId.FAMILY_ZUBAT, PokemonId.GOLBAT);
		familys.put(PokemonId.ODDISH, PokemonFamilyId.FAMILY_ODDISH);
		familys.put(PokemonId.GLOOM, PokemonFamilyId.FAMILY_ODDISH);
		familys.put(PokemonId.VILEPLUME, PokemonFamilyId.FAMILY_ODDISH);
		highestForFamily.put(PokemonFamilyId.FAMILY_ODDISH, PokemonId.VILEPLUME);
		familys.put(PokemonId.PARAS, PokemonFamilyId.FAMILY_PARAS);
		familys.put(PokemonId.PARASECT, PokemonFamilyId.FAMILY_PARAS);
		highestForFamily.put(PokemonFamilyId.FAMILY_PARAS, PokemonId.PARASECT);
		familys.put(PokemonId.VENONAT, PokemonFamilyId.FAMILY_VENONAT);
		familys.put(PokemonId.VENOMOTH, PokemonFamilyId.FAMILY_VENONAT);
		highestForFamily.put(PokemonFamilyId.FAMILY_VENONAT, PokemonId.VENOMOTH);
		familys.put(PokemonId.DIGLETT, PokemonFamilyId.FAMILY_DIGLETT);
		familys.put(PokemonId.DUGTRIO, PokemonFamilyId.FAMILY_DIGLETT);
		highestForFamily.put(PokemonFamilyId.FAMILY_DIGLETT, PokemonId.DUGTRIO);
		familys.put(PokemonId.MEOWTH, PokemonFamilyId.FAMILY_MEOWTH);
		familys.put(PokemonId.PERSIAN, PokemonFamilyId.FAMILY_MEOWTH);
		highestForFamily.put(PokemonFamilyId.FAMILY_MEOWTH, PokemonId.PERSIAN);
		familys.put(PokemonId.PSYDUCK, PokemonFamilyId.FAMILY_PSYDUCK);
		familys.put(PokemonId.GOLDUCK, PokemonFamilyId.FAMILY_PSYDUCK);
		highestForFamily.put(PokemonFamilyId.FAMILY_PSYDUCK, PokemonId.GOLDUCK);
		familys.put(PokemonId.MANKEY, PokemonFamilyId.FAMILY_MANKEY);
		familys.put(PokemonId.PRIMEAPE, PokemonFamilyId.FAMILY_MANKEY);
		highestForFamily.put(PokemonFamilyId.FAMILY_MANKEY, PokemonId.PRIMEAPE);
		familys.put(PokemonId.GROWLITHE, PokemonFamilyId.FAMILY_GROWLITHE);
		familys.put(PokemonId.ARCANINE, PokemonFamilyId.FAMILY_GROWLITHE);
		highestForFamily.put(PokemonFamilyId.FAMILY_GROWLITHE, PokemonId.ARCANINE);
		familys.put(PokemonId.POLIWAG, PokemonFamilyId.FAMILY_POLIWAG);
		familys.put(PokemonId.POLIWHIRL, PokemonFamilyId.FAMILY_POLIWAG);
		familys.put(PokemonId.POLIWRATH, PokemonFamilyId.FAMILY_POLIWAG);
		highestForFamily.put(PokemonFamilyId.FAMILY_POLIWAG, PokemonId.POLIWRATH);
		familys.put(PokemonId.ABRA, PokemonFamilyId.FAMILY_ABRA);
		familys.put(PokemonId.KADABRA, PokemonFamilyId.FAMILY_ABRA);
		familys.put(PokemonId.ALAKAZAM, PokemonFamilyId.FAMILY_ABRA);
		highestForFamily.put(PokemonFamilyId.FAMILY_ABRA, PokemonId.ALAKAZAM);
		familys.put(PokemonId.MACHOP, PokemonFamilyId.FAMILY_MACHOP);
		familys.put(PokemonId.MACHOKE, PokemonFamilyId.FAMILY_MACHOP);
		familys.put(PokemonId.MACHAMP, PokemonFamilyId.FAMILY_MACHOP);
		highestForFamily.put(PokemonFamilyId.FAMILY_MACHOP, PokemonId.MACHAMP);
		familys.put(PokemonId.BELLSPROUT, PokemonFamilyId.FAMILY_BELLSPROUT);
		familys.put(PokemonId.WEEPINBELL, PokemonFamilyId.FAMILY_BELLSPROUT);
		familys.put(PokemonId.VICTREEBEL, PokemonFamilyId.FAMILY_BELLSPROUT);
		highestForFamily.put(PokemonFamilyId.FAMILY_BELLSPROUT, PokemonId.VICTREEBEL);
		familys.put(PokemonId.TENTACOOL, PokemonFamilyId.FAMILY_TENTACOOL);
		familys.put(PokemonId.TENTACRUEL, PokemonFamilyId.FAMILY_TENTACOOL);
		highestForFamily.put(PokemonFamilyId.FAMILY_TENTACOOL, PokemonId.TENTACRUEL);
		familys.put(PokemonId.GEODUDE, PokemonFamilyId.FAMILY_GEODUDE);
		familys.put(PokemonId.GRAVELER, PokemonFamilyId.FAMILY_GEODUDE);
		familys.put(PokemonId.GOLEM, PokemonFamilyId.FAMILY_GEODUDE);
		highestForFamily.put(PokemonFamilyId.FAMILY_GEODUDE, PokemonId.GOLEM);
		familys.put(PokemonId.PONYTA, PokemonFamilyId.FAMILY_PONYTA);
		familys.put(PokemonId.RAPIDASH, PokemonFamilyId.FAMILY_PONYTA);
		highestForFamily.put(PokemonFamilyId.FAMILY_PONYTA, PokemonId.RAPIDASH);
		familys.put(PokemonId.SLOWPOKE, PokemonFamilyId.FAMILY_SLOWPOKE);
		familys.put(PokemonId.SLOWBRO, PokemonFamilyId.FAMILY_SLOWPOKE);
		highestForFamily.put(PokemonFamilyId.FAMILY_SLOWPOKE, PokemonId.SLOWBRO);
		familys.put(PokemonId.MAGNEMITE, PokemonFamilyId.FAMILY_MAGNEMITE);
		familys.put(PokemonId.MAGNETON, PokemonFamilyId.FAMILY_MAGNEMITE);
		highestForFamily.put(PokemonFamilyId.FAMILY_MAGNEMITE, PokemonId.MAGNETON);
		familys.put(PokemonId.FARFETCHD, PokemonFamilyId.FAMILY_FARFETCHD);
		highestForFamily.put(PokemonFamilyId.FAMILY_FARFETCHD, PokemonId.FARFETCHD);
		familys.put(PokemonId.DODUO, PokemonFamilyId.FAMILY_DODUO);
		familys.put(PokemonId.DODRIO, PokemonFamilyId.FAMILY_DODUO);
		highestForFamily.put(PokemonFamilyId.FAMILY_DODUO, PokemonId.DODRIO);
		familys.put(PokemonId.SEEL, PokemonFamilyId.FAMILY_SEEL);
		familys.put(PokemonId.DEWGONG, PokemonFamilyId.FAMILY_SEEL);
		highestForFamily.put(PokemonFamilyId.FAMILY_SEEL, PokemonId.DEWGONG);
		familys.put(PokemonId.GRIMER, PokemonFamilyId.FAMILY_GRIMER);
		familys.put(PokemonId.MUK, PokemonFamilyId.FAMILY_GRIMER);
		highestForFamily.put(PokemonFamilyId.FAMILY_GRIMER, PokemonId.MUK);
		familys.put(PokemonId.SHELLDER, PokemonFamilyId.FAMILY_SHELLDER);
		familys.put(PokemonId.CLOYSTER, PokemonFamilyId.FAMILY_SHELLDER);
		highestForFamily.put(PokemonFamilyId.FAMILY_SHELLDER, PokemonId.CLOYSTER);
		familys.put(PokemonId.GASTLY, PokemonFamilyId.FAMILY_GASTLY);
		familys.put(PokemonId.HAUNTER, PokemonFamilyId.FAMILY_GASTLY);
		familys.put(PokemonId.GENGAR, PokemonFamilyId.FAMILY_GASTLY);
		highestForFamily.put(PokemonFamilyId.FAMILY_GASTLY, PokemonId.GENGAR);
		familys.put(PokemonId.ONIX, PokemonFamilyId.FAMILY_ONIX);
		highestForFamily.put(PokemonFamilyId.FAMILY_ONIX, PokemonId.ONIX);
		familys.put(PokemonId.DROWZEE, PokemonFamilyId.FAMILY_DROWZEE);
		highestForFamily.put(PokemonFamilyId.FAMILY_DROWZEE, PokemonId.DROWZEE);
		// MISSING ENUM IN PROTO
		//familys.put(PokemonId.HYPNO,PokemonFamilyId.FAMILY_HYPNO);
		familys.put(PokemonId.KRABBY, PokemonFamilyId.FAMILY_KRABBY);
		familys.put(PokemonId.KINGLER, PokemonFamilyId.FAMILY_KRABBY);
		highestForFamily.put(PokemonFamilyId.FAMILY_KRABBY, PokemonId.KINGLER);
		familys.put(PokemonId.VOLTORB, PokemonFamilyId.FAMILY_VOLTORB);
		familys.put(PokemonId.ELECTRODE, PokemonFamilyId.FAMILY_VOLTORB);
		highestForFamily.put(PokemonFamilyId.FAMILY_VOLTORB, PokemonId.ELECTRODE);
		familys.put(PokemonId.EXEGGCUTE, PokemonFamilyId.FAMILY_EXEGGCUTE);
		familys.put(PokemonId.EXEGGUTOR, PokemonFamilyId.FAMILY_EXEGGCUTE);
		highestForFamily.put(PokemonFamilyId.FAMILY_EXEGGCUTE, PokemonId.EXEGGUTOR);
		familys.put(PokemonId.CUBONE, PokemonFamilyId.FAMILY_CUBONE);
		familys.put(PokemonId.MAROWAK, PokemonFamilyId.FAMILY_CUBONE);
		highestForFamily.put(PokemonFamilyId.FAMILY_CUBONE, PokemonId.MAROWAK);
		familys.put(PokemonId.HITMONLEE, PokemonFamilyId.FAMILY_HITMONLEE);
		highestForFamily.put(PokemonFamilyId.FAMILY_HITMONLEE, PokemonId.HITMONLEE);
		familys.put(PokemonId.HITMONCHAN, PokemonFamilyId.FAMILY_HITMONCHAN);
		highestForFamily.put(PokemonFamilyId.FAMILY_HITMONCHAN, PokemonId.HITMONCHAN);
		familys.put(PokemonId.LICKITUNG, PokemonFamilyId.FAMILY_LICKITUNG);
		highestForFamily.put(PokemonFamilyId.FAMILY_LICKITUNG, PokemonId.LICKITUNG);
		familys.put(PokemonId.KOFFING, PokemonFamilyId.FAMILY_KOFFING);
		familys.put(PokemonId.WEEZING, PokemonFamilyId.FAMILY_KOFFING);
		highestForFamily.put(PokemonFamilyId.FAMILY_KOFFING, PokemonId.WEEZING);
		familys.put(PokemonId.RHYHORN, PokemonFamilyId.FAMILY_RHYHORN);
		familys.put(PokemonId.RHYDON, PokemonFamilyId.FAMILY_RHYHORN);
		highestForFamily.put(PokemonFamilyId.FAMILY_RHYHORN, PokemonId.RHYDON);
		familys.put(PokemonId.CHANSEY, PokemonFamilyId.FAMILY_CHANSEY);
		highestForFamily.put(PokemonFamilyId.FAMILY_CHANSEY, PokemonId.CHANSEY);
		familys.put(PokemonId.TANGELA, PokemonFamilyId.FAMILY_TANGELA);
		highestForFamily.put(PokemonFamilyId.FAMILY_TANGELA, PokemonId.TANGELA);
		familys.put(PokemonId.KANGASKHAN, PokemonFamilyId.FAMILY_KANGASKHAN);
		highestForFamily.put(PokemonFamilyId.FAMILY_KANGASKHAN, PokemonId.KANGASKHAN);
		familys.put(PokemonId.HORSEA, PokemonFamilyId.FAMILY_HORSEA);
		familys.put(PokemonId.SEADRA, PokemonFamilyId.FAMILY_HORSEA);
		highestForFamily.put(PokemonFamilyId.FAMILY_HORSEA, PokemonId.SEADRA);
		familys.put(PokemonId.GOLDEEN, PokemonFamilyId.FAMILY_GOLDEEN);
		familys.put(PokemonId.SEAKING, PokemonFamilyId.FAMILY_GOLDEEN);
		highestForFamily.put(PokemonFamilyId.FAMILY_GOLDEEN, PokemonId.SEAKING);
		familys.put(PokemonId.STARYU, PokemonFamilyId.FAMILY_STARYU);
		familys.put(PokemonId.STARMIE, PokemonFamilyId.FAMILY_STARYU);
		highestForFamily.put(PokemonFamilyId.FAMILY_STARYU, PokemonId.STARMIE);
		familys.put(PokemonId.MR_MIME, PokemonFamilyId.FAMILY_MR_MIME);
		highestForFamily.put(PokemonFamilyId.FAMILY_MR_MIME, PokemonId.MR_MIME);
		familys.put(PokemonId.SCYTHER, PokemonFamilyId.FAMILY_SCYTHER);
		highestForFamily.put(PokemonFamilyId.FAMILY_SCYTHER, PokemonId.SCYTHER);
		familys.put(PokemonId.JYNX, PokemonFamilyId.FAMILY_JYNX);
		highestForFamily.put(PokemonFamilyId.FAMILY_JYNX, PokemonId.JYNX);
		familys.put(PokemonId.ELECTABUZZ, PokemonFamilyId.FAMILY_ELECTABUZZ);
		highestForFamily.put(PokemonFamilyId.FAMILY_ELECTABUZZ, PokemonId.ELECTABUZZ);
		familys.put(PokemonId.MAGMAR, PokemonFamilyId.FAMILY_MAGMAR);
		highestForFamily.put(PokemonFamilyId.FAMILY_MAGMAR, PokemonId.MAGMAR);
		familys.put(PokemonId.PINSIR, PokemonFamilyId.FAMILY_PINSIR);
		highestForFamily.put(PokemonFamilyId.FAMILY_PINSIR, PokemonId.PINSIR);
		familys.put(PokemonId.TAUROS, PokemonFamilyId.FAMILY_TAUROS);
		highestForFamily.put(PokemonFamilyId.FAMILY_TAUROS, PokemonId.TAUROS);
		familys.put(PokemonId.MAGIKARP, PokemonFamilyId.FAMILY_MAGIKARP);
		familys.put(PokemonId.GYARADOS, PokemonFamilyId.FAMILY_MAGIKARP);
		highestForFamily.put(PokemonFamilyId.FAMILY_MAGIKARP, PokemonId.GYARADOS);
		familys.put(PokemonId.LAPRAS, PokemonFamilyId.FAMILY_LAPRAS);
		highestForFamily.put(PokemonFamilyId.FAMILY_LAPRAS, PokemonId.LAPRAS);
		familys.put(PokemonId.DITTO, PokemonFamilyId.FAMILY_DITTO);
		highestForFamily.put(PokemonFamilyId.FAMILY_DITTO, PokemonId.DITTO);
		familys.put(PokemonId.EEVEE, PokemonFamilyId.FAMILY_EEVEE);
		familys.put(PokemonId.JOLTEON, PokemonFamilyId.FAMILY_EEVEE);
		familys.put(PokemonId.VAPOREON, PokemonFamilyId.FAMILY_EEVEE);
		familys.put(PokemonId.FLAREON, PokemonFamilyId.FAMILY_EEVEE);
		highestForFamily.put(PokemonFamilyId.FAMILY_EEVEE, PokemonId.EEVEE);
		familys.put(PokemonId.PORYGON, PokemonFamilyId.FAMILY_PORYGON);
		highestForFamily.put(PokemonFamilyId.FAMILY_PORYGON, PokemonId.PORYGON);
		familys.put(PokemonId.OMANYTE, PokemonFamilyId.FAMILY_OMANYTE);
		familys.put(PokemonId.OMASTAR, PokemonFamilyId.FAMILY_OMANYTE);
		highestForFamily.put(PokemonFamilyId.FAMILY_OMANYTE, PokemonId.OMASTAR);
		familys.put(PokemonId.KABUTO, PokemonFamilyId.FAMILY_KABUTO);
		familys.put(PokemonId.KABUTOPS, PokemonFamilyId.FAMILY_KABUTO);
		highestForFamily.put(PokemonFamilyId.FAMILY_KABUTO, PokemonId.KABUTOPS);
		familys.put(PokemonId.AERODACTYL, PokemonFamilyId.FAMILY_AERODACTYL);
		highestForFamily.put(PokemonFamilyId.FAMILY_AERODACTYL, PokemonId.AERODACTYL);
		familys.put(PokemonId.SNORLAX, PokemonFamilyId.FAMILY_SNORLAX);
		highestForFamily.put(PokemonFamilyId.FAMILY_SNORLAX, PokemonId.SNORLAX);
		familys.put(PokemonId.ARTICUNO, PokemonFamilyId.FAMILY_ARTICUNO);
		highestForFamily.put(PokemonFamilyId.FAMILY_ARTICUNO, PokemonId.ARTICUNO);
		familys.put(PokemonId.ZAPDOS, PokemonFamilyId.FAMILY_ZAPDOS);
		highestForFamily.put(PokemonFamilyId.FAMILY_ZAPDOS, PokemonId.ZAPDOS);
		familys.put(PokemonId.MOLTRES, PokemonFamilyId.FAMILY_MOLTRES);
		highestForFamily.put(PokemonFamilyId.FAMILY_MOLTRES, PokemonId.MOLTRES);
		familys.put(PokemonId.DRATINI, PokemonFamilyId.FAMILY_DRATINI);
		familys.put(PokemonId.DRAGONAIR, PokemonFamilyId.FAMILY_DRATINI);
		familys.put(PokemonId.DRAGONITE, PokemonFamilyId.FAMILY_DRATINI);
		highestForFamily.put(PokemonFamilyId.FAMILY_DRATINI, PokemonId.DRAGONITE);
		familys.put(PokemonId.MEWTWO, PokemonFamilyId.FAMILY_MEWTWO);
		highestForFamily.put(PokemonFamilyId.FAMILY_MEWTWO, PokemonId.MEWTWO);
		familys.put(PokemonId.MEW, PokemonFamilyId.FAMILY_MEW);
		highestForFamily.put(PokemonFamilyId.FAMILY_MEW, PokemonId.MEW);
	}

	/**
	 * Return the FamilyId for the given PokemonId.
	 *
	 * @param id the id of the pokemon
	 * @return PokemonFamilyId
	 */
	public static PokemonFamilyId getFamily(PokemonId id) {
		return familys.get(id);
	}

	/**
	 * Return the highest evolution for given family ID.
	 * !!! CARE TO EVEE THAT DOESNT HAVE BETTER EVOLUTION !!!
	 *
	 * @param family the id of the pokemon family
	 * @return PokemonId
	 */
	public static PokemonId getHightestForFamily(PokemonFamilyId family) {
		return highestForFamily.get(family);
	}


}