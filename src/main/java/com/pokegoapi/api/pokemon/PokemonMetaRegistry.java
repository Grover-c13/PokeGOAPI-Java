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

public class PokemonMetaRegistry {

	private static EnumMap<PokemonId, PokemonFamilyId> familys = new EnumMap<>(PokemonId.class);
	private static EnumMap<PokemonFamilyId, PokemonId> highestForFamily = new EnumMap<>(PokemonFamilyId.class);
	private static EnumMap<PokemonId, PokemonMeta> meta = new EnumMap<>(PokemonId.class);

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

		meta.put(PokemonId.BULBASAUR, new PokemonMeta(90,0.16,25,0.1,0.7,null));
		meta.put(PokemonId.IVYSAUR, new PokemonMeta(120,0.08,100,0.07,1,PokemonId.BULBASAUR));
		meta.put(PokemonId.VENUSAUR, new PokemonMeta(160,0.04,0,0.05,2,PokemonId.IVYSAUR));
		meta.put(PokemonId.CHARMANDER, new PokemonMeta(78,0.16,25,0.1,0.6,null));
		meta.put(PokemonId.CHARMELEON, new PokemonMeta(116,0.08,100,0.07,1.1,PokemonId.CHARMANDER));
		meta.put(PokemonId.CHARIZARD, new PokemonMeta(156,0.04,0,0.05,1.7,PokemonId.CHARMELEON));
		meta.put(PokemonId.SQUIRTLE, new PokemonMeta(88,0.16,25,0.1,0.5,null));
		meta.put(PokemonId.WARTORTLE, new PokemonMeta(118,0.08,100,0.07,1,PokemonId.SQUIRTLE));
		meta.put(PokemonId.BLASTOISE, new PokemonMeta(158,0.04,0,0.05,1.6,PokemonId.WARTORTLE));
		meta.put(PokemonId.CATERPIE, new PokemonMeta(90,0.4,12,0.2,0.3,null));
		meta.put(PokemonId.METAPOD, new PokemonMeta(100,0.2,50,0.09,0.7,PokemonId.CATERPIE));
		meta.put(PokemonId.BUTTERFREE, new PokemonMeta(120,0.1,0,0.06,1.1,PokemonId.METAPOD));
		meta.put(PokemonId.WEEDLE, new PokemonMeta(80,0.4,12,0.2,0.3,null));
		meta.put(PokemonId.KAKUNA, new PokemonMeta(90,0.2,50,0.09,0.6,PokemonId.WEEDLE));
		meta.put(PokemonId.BEEDRILL, new PokemonMeta(130,0.1,0,0.06,1,PokemonId.KAKUNA));
		meta.put(PokemonId.PIDGEY, new PokemonMeta(80,0.4,12,0.2,0.3,null));
		meta.put(PokemonId.PIDGEOTTO, new PokemonMeta(126,0.2,50,0.09,1.1,PokemonId.PIDGEY));
		meta.put(PokemonId.PIDGEOT, new PokemonMeta(166,0.1,0,0.06,1.5,PokemonId.PIDGEOTTO));
		meta.put(PokemonId.RATTATA, new PokemonMeta(60,0.4,25,0.2,0.3,null));
		meta.put(PokemonId.RATICATE, new PokemonMeta(110,0.16,0,0.07,0.7,PokemonId.RATTATA));
		meta.put(PokemonId.SPEAROW, new PokemonMeta(80,0.4,50,0.15,0.3,null));
		meta.put(PokemonId.FEAROW, new PokemonMeta(130,0.16,0,0.07,1.2,PokemonId.SPEAROW));
		meta.put(PokemonId.EKANS, new PokemonMeta(70,0.4,50,0.15,2,null));
		meta.put(PokemonId.ARBOK, new PokemonMeta(120,0.16,0,0.07,3.5,PokemonId.EKANS));
		meta.put(PokemonId.PIKACHU, new PokemonMeta(70,0.16,50,0.1,0.4,null));
		meta.put(PokemonId.SANDSHREW, new PokemonMeta(100,0.4,50,0.1,0.6,null));
		meta.put(PokemonId.SANDSLASH, new PokemonMeta(150,0.16,0,0.06,1,PokemonId.SANDSHREW));
		meta.put(PokemonId.NIDORAN_FEMALE, new PokemonMeta(110,0.4,25,0.15,0.4,null));
		meta.put(PokemonId.NIDORINA, new PokemonMeta(140,0.2,100,0.07,0.8,PokemonId.NIDORAN_FEMALE));
		meta.put(PokemonId.NIDOQUEEN, new PokemonMeta(180,0.1,0,0.05,1.3,PokemonId.NIDORINA));
		meta.put(PokemonId.NIDORAN_MALE, new PokemonMeta(92,0.4,25,0.15,0.5,null));
		meta.put(PokemonId.NIDORINO, new PokemonMeta(122,0.2,100,0.07,0.9,PokemonId.NIDORAN_MALE));
		meta.put(PokemonId.NIDOKING, new PokemonMeta(162,0.1,0,0.05,1.4,PokemonId.NIDORINO));
		meta.put(PokemonId.CLEFAIRY, new PokemonMeta(140,0.24,50,0.1,0.6,null));
		meta.put(PokemonId.CLEFABLE, new PokemonMeta(190,0.08,0,0.06,1.3,PokemonId.CLEFAIRY));
		meta.put(PokemonId.VULPIX, new PokemonMeta(76,0.24,50,0.1,0.6,null));
		meta.put(PokemonId.NINETALES, new PokemonMeta(146,0.08,0,0.06,1.1,PokemonId.VULPIX));
		meta.put(PokemonId.JIGGLYPUFF, new PokemonMeta(230,0.4,50,0.1,0.5,null));
		meta.put(PokemonId.WIGGLYTUFF, new PokemonMeta(280,0.16,0,0.06,1,PokemonId.JIGGLYPUFF));
		meta.put(PokemonId.ZUBAT, new PokemonMeta(80,0.4,50,0.2,0.8,null));
		meta.put(PokemonId.GOLBAT, new PokemonMeta(150,0.16,0,0.07,1.6,PokemonId.ZUBAT));
		meta.put(PokemonId.GLOOM, new PokemonMeta(120,0.24,100,0.07,0.8,PokemonId.ODDISH));
		meta.put(PokemonId.VILEPLUME, new PokemonMeta(150,0.12,0,0.05,1.2,PokemonId.GLOOM));
		meta.put(PokemonId.PARAS, new PokemonMeta(70,0.32,50,0.15,0.3,null));
		meta.put(PokemonId.PARASECT, new PokemonMeta(120,0.16,0,0.07,1,PokemonId.PARAS));
		meta.put(PokemonId.VENONAT, new PokemonMeta(120,0.4,50,0.15,1,null));
		meta.put(PokemonId.VENOMOTH, new PokemonMeta(140,0.16,0,0.07,1.5,PokemonId.VENONAT));
		meta.put(PokemonId.DIGLETT, new PokemonMeta(20,0.4,50,0.1,0.2,null));
		meta.put(PokemonId.DUGTRIO, new PokemonMeta(70,0.16,0,0.06,0.7,PokemonId.DIGLETT));
		meta.put(PokemonId.MEOWTH, new PokemonMeta(80,0.4,50,0.15,0.4,null));
		meta.put(PokemonId.PERSIAN, new PokemonMeta(130,0.16,0,0.07,1,PokemonId.MEOWTH));
		meta.put(PokemonId.PSYDUCK, new PokemonMeta(100,0.4,50,0.1,0.8,null));
		meta.put(PokemonId.GOLDUCK, new PokemonMeta(160,0.16,0,0.06,1.7,PokemonId.PSYDUCK));
		meta.put(PokemonId.PRIMEAPE, new PokemonMeta(130,0.16,0,0.06,1,null));
		meta.put(PokemonId.GROWLITHE, new PokemonMeta(110,0.24,50,0.1,0.7,null));
		meta.put(PokemonId.ARCANINE, new PokemonMeta(180,0.08,0,0.06,1.9,PokemonId.GROWLITHE));
		meta.put(PokemonId.POLIWAG, new PokemonMeta(80,0.4,25,0.15,0.6,null));
		meta.put(PokemonId.POLIWHIRL, new PokemonMeta(130,0.2,100,0.07,1,PokemonId.POLIWAG));
		meta.put(PokemonId.POLIWRATH, new PokemonMeta(180,0.1,0,0.05,1.3,PokemonId.POLIWHIRL));
		meta.put(PokemonId.ABRA, new PokemonMeta(50,0.4,25,0.99,0.9,null));
		meta.put(PokemonId.KADABRA, new PokemonMeta(80,0.2,100,0.07,1.3,PokemonId.ABRA));
		meta.put(PokemonId.ALAKAZAM, new PokemonMeta(110,0.1,0,0.05,1.5,PokemonId.KADABRA));
		meta.put(PokemonId.MACHAMP, new PokemonMeta(180,0.1,0,0.05,1.6,PokemonId.MACHOKE));
		meta.put(PokemonId.BELLSPROUT, new PokemonMeta(100,0.4,25,0.15,0.7,null));
		meta.put(PokemonId.WEEPINBELL, new PokemonMeta(130,0.2,100,0.07,1,PokemonId.BELLSPROUT));
		meta.put(PokemonId.VICTREEBEL, new PokemonMeta(160,0.1,0,0.05,1.7,PokemonId.WEEPINBELL));
		meta.put(PokemonId.TENTACOOL, new PokemonMeta(80,0.4,50,0.15,0.9,null));
		meta.put(PokemonId.TENTACRUEL, new PokemonMeta(160,0.16,0,0.07,1.6,PokemonId.TENTACOOL));
		meta.put(PokemonId.GEODUDE, new PokemonMeta(80,0.4,25,0.1,0.4,null));
		meta.put(PokemonId.GRAVELER, new PokemonMeta(110,0.2,100,0.07,1,PokemonId.GEODUDE));
		meta.put(PokemonId.GOLEM, new PokemonMeta(160,0.1,0,0.05,1.4,PokemonId.GRAVELER));
		meta.put(PokemonId.PONYTA, new PokemonMeta(100,0.32,50,0.1,1,null));
		meta.put(PokemonId.RAPIDASH, new PokemonMeta(130,0.12,0,0.06,1.7,PokemonId.PONYTA));
		meta.put(PokemonId.SLOWPOKE, new PokemonMeta(180,0.4,50,0.1,1.2,null));
		meta.put(PokemonId.SLOWBRO, new PokemonMeta(190,0.16,0,0.06,1.6,PokemonId.SLOWPOKE));
		meta.put(PokemonId.MAGNEMITE, new PokemonMeta(50,0.4,50,0.1,0.3,null));
		meta.put(PokemonId.MAGNETON, new PokemonMeta(100,0.16,0,0.06,1,PokemonId.MAGNEMITE));
		meta.put(PokemonId.FARFETCHD, new PokemonMeta(104,0.24,0,0.09,0.8,null));
		meta.put(PokemonId.DODUO, new PokemonMeta(70,0.4,50,0.1,1.4,null));
		meta.put(PokemonId.DODRIO, new PokemonMeta(120,0.16,0,0.06,1.8,PokemonId.DODUO));
		meta.put(PokemonId.SEEL, new PokemonMeta(130,0.4,50,0.09,1.1,null));
		meta.put(PokemonId.DEWGONG, new PokemonMeta(180,0.16,0,0.06,1.7,PokemonId.SEEL));
		meta.put(PokemonId.GRIMER, new PokemonMeta(160,0.4,50,0.1,0.9,null));
		meta.put(PokemonId.MUK, new PokemonMeta(210,0.16,0,0.06,1.2,PokemonId.GRIMER));
		meta.put(PokemonId.SHELLDER, new PokemonMeta(60,0.4,50,0.1,0.3,null));
		meta.put(PokemonId.CLOYSTER, new PokemonMeta(100,0.16,0,0.06,1.5,PokemonId.SHELLDER));
		meta.put(PokemonId.GASTLY, new PokemonMeta(60,0.32,25,0.1,1.3,null));
		meta.put(PokemonId.HAUNTER, new PokemonMeta(90,0.16,100,0.07,1.6,PokemonId.GASTLY));
		meta.put(PokemonId.GENGAR, new PokemonMeta(120,0.08,0,0.05,1.5,PokemonId.HAUNTER));
		meta.put(PokemonId.ONIX, new PokemonMeta(70,0.16,0,0.09,8.8,null));
		meta.put(PokemonId.DROWZEE, new PokemonMeta(120,0.4,50,0.1,1,null));
		meta.put(PokemonId.HYPNO, new PokemonMeta(170,0.16,0,0.06,1.6,PokemonId.DROWZEE));
		meta.put(PokemonId.KRABBY, new PokemonMeta(60,0.4,50,0.15,0.4,null));
		meta.put(PokemonId.KINGLER, new PokemonMeta(110,0.16,0,0.07,1.3,PokemonId.KRABBY));
		meta.put(PokemonId.VOLTORB, new PokemonMeta(80,0.4,50,0.1,0.5,null));
		meta.put(PokemonId.ELECTRODE, new PokemonMeta(120,0.16,0,0.06,1.2,PokemonId.VOLTORB));
		meta.put(PokemonId.EXEGGCUTE, new PokemonMeta(120,0.4,50,0.1,0.4,null));
		meta.put(PokemonId.EXEGGUTOR, new PokemonMeta(190,0.16,0,0.06,2,PokemonId.EXEGGCUTE));
		meta.put(PokemonId.CUBONE, new PokemonMeta(100,0.32,50,0.1,0.4,null));
		meta.put(PokemonId.MAROWAK, new PokemonMeta(120,0.12,0,0.06,1,PokemonId.CUBONE));
		meta.put(PokemonId.HITMONLEE, new PokemonMeta(100,0.16,0,0.09,1.5,null));
		meta.put(PokemonId.LICKITUNG, new PokemonMeta(180,0.16,0,0.09,1.2,null));
		meta.put(PokemonId.KOFFING, new PokemonMeta(80,0.4,50,0.1,0.6,null));
		meta.put(PokemonId.WEEZING, new PokemonMeta(130,0.16,0,0.06,1.2,PokemonId.KOFFING));
		meta.put(PokemonId.RHYHORN, new PokemonMeta(160,0.4,50,0.1,1,null));
		meta.put(PokemonId.RHYDON, new PokemonMeta(210,0.16,0,0.06,1.9,PokemonId.RHYHORN));
		meta.put(PokemonId.CHANSEY, new PokemonMeta(500,0.16,0,0.09,1.1,null));
		meta.put(PokemonId.TANGELA, new PokemonMeta(130,0.32,0,0.09,1,null));
		meta.put(PokemonId.HORSEA, new PokemonMeta(60,0.4,50,0.1,0.4,null));
		meta.put(PokemonId.SEADRA, new PokemonMeta(110,0.16,0,0.06,1.2,PokemonId.HORSEA));
		meta.put(PokemonId.GOLDEEN, new PokemonMeta(90,0.4,50,0.15,0.6,null));
		meta.put(PokemonId.SEAKING, new PokemonMeta(160,0.16,0,0.07,1.3,PokemonId.GOLDEEN));
		meta.put(PokemonId.STARYU, new PokemonMeta(60,0.4,50,0.15,0.8,null));
		meta.put(PokemonId.STARMIE, new PokemonMeta(120,0.16,0,0.06,1.1,PokemonId.STARYU));
		meta.put(PokemonId.MR_MIME, new PokemonMeta(80,0.24,0,0.09,1.3,null));
		meta.put(PokemonId.SCYTHER, new PokemonMeta(140,0.24,0,0.09,1.5,null));
		meta.put(PokemonId.JYNX, new PokemonMeta(130,0.24,0,0.09,1.4,null));
		meta.put(PokemonId.ELECTABUZZ, new PokemonMeta(130,0.24,0,0.09,1.1,null));
		meta.put(PokemonId.MAGMAR, new PokemonMeta(130,0.24,0,0.09,1.3,null));
		meta.put(PokemonId.PINSIR, new PokemonMeta(130,0.24,0,0.09,1.5,null));
		meta.put(PokemonId.TAUROS, new PokemonMeta(150,0.24,0,0.09,1.4,null));
		meta.put(PokemonId.MAGIKARP, new PokemonMeta(40,0.56,400,0.15,0.9,null));
		meta.put(PokemonId.GYARADOS, new PokemonMeta(190,0.08,0,0.07,6.5,PokemonId.MAGIKARP));
		meta.put(PokemonId.LAPRAS, new PokemonMeta(260,0.16,0,0.09,2.5,null));
		meta.put(PokemonId.DITTO, new PokemonMeta(96,0.16,0,0.1,0.3,null));
		meta.put(PokemonId.EEVEE, new PokemonMeta(110,0.32,25,0.1,0.3,null));
		meta.put(PokemonId.VAPOREON, new PokemonMeta(260,0.12,0,0.06,1,PokemonId.EEVEE));
		meta.put(PokemonId.JOLTEON, new PokemonMeta(130,0.12,0,0.06,0.8,PokemonId.EEVEE));
		meta.put(PokemonId.FLAREON, new PokemonMeta(130,0.12,0,0.06,0.9,PokemonId.EEVEE));
		meta.put(PokemonId.PORYGON, new PokemonMeta(130,0.32,0,0.09,0.8,null));
		meta.put(PokemonId.OMANYTE, new PokemonMeta(70,0.32,50,0.09,0.4,null));
		meta.put(PokemonId.OMASTAR, new PokemonMeta(140,0.12,0,0.05,1,PokemonId.OMANYTE));
		meta.put(PokemonId.KABUTO, new PokemonMeta(60,0.32,50,0.09,0.5,null));
		meta.put(PokemonId.KABUTOPS, new PokemonMeta(120,0.12,0,0.05,1.3,PokemonId.KABUTO));
		meta.put(PokemonId.AERODACTYL, new PokemonMeta(160,0.16,0,0.09,1.8,null));
		meta.put(PokemonId.SNORLAX, new PokemonMeta(320,0.16,0,0.09,2.1,null));
		meta.put(PokemonId.ARTICUNO, new PokemonMeta(180,0,0,0.1,1.7,null));
		meta.put(PokemonId.ZAPDOS, new PokemonMeta(180,0,0,0.1,1.6,null));
		meta.put(PokemonId.MOLTRES, new PokemonMeta(180,0,0,0.1,2,null));
		meta.put(PokemonId.DRATINI, new PokemonMeta(82,0.32,25,0.09,1.8,null));
		meta.put(PokemonId.DRAGONAIR, new PokemonMeta(122,0.08,100,0.06,4,PokemonId.DRATINI));
		meta.put(PokemonId.DRAGONITE, new PokemonMeta(182,0.04,0,0.05,2.2,PokemonId.DRAGONAIR));
		meta.put(PokemonId.MEWTWO, new PokemonMeta(212,0,0,0.1,2,null));
		meta.put(PokemonId.MEW, new PokemonMeta(200,0,0,0.1,0.4,null));


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
	 * Return PokemonMeta object containing meta info about a pokemon.
	 *
	 * @param id the id of the pokemon
	 * @return PokemonMeta
	 */
	public static PokemonMeta getMeta(PokemonId id) {
		return meta.get(id);
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