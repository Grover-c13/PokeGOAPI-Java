package com.pokegoapi.api.pokemon;


import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;

import java.util.EnumMap;

public class PokemonFamilyMap {
	private static EnumMap<PokemonId, PokemonFamilyId> map;


	private static void registerValues()
	{
		map = new EnumMap<PokemonId, PokemonFamilyId>(PokemonId.class);
		map.put(PokemonId.BULBASAUR, PokemonFamilyId.FAMILY_BULBASAUR);
		map.put(PokemonId.IVYSAUR, PokemonFamilyId.FAMILY_BULBASAUR);
		map.put(PokemonId.VENUSAUR, PokemonFamilyId.FAMILY_BULBASAUR);
		map.put(PokemonId.CHARMENDER, PokemonFamilyId.FAMILY_CHARMANDER);
		map.put(PokemonId.CHARMELEON, PokemonFamilyId.FAMILY_CHARMANDER);
		map.put(PokemonId.CHARIZARD, PokemonFamilyId.FAMILY_CHARMANDER);
		map.put(PokemonId.SQUIRTLE, PokemonFamilyId.FAMILY_SQUIRTLE);
		map.put(PokemonId.WARTORTLE, PokemonFamilyId.FAMILY_SQUIRTLE);
		map.put(PokemonId.BLASTOISE, PokemonFamilyId.FAMILY_SQUIRTLE);
		map.put(PokemonId.CATERPIE, PokemonFamilyId.FAMILY_CATERPIE);
		map.put(PokemonId.METAPOD, PokemonFamilyId.FAMILY_SQUIRTLE);
		map.put(PokemonId.BUTTERFREE, PokemonFamilyId.FAMILY_SQUIRTLE);
		map.put(PokemonId.WEEDLE, PokemonFamilyId.FAMILY_WEEDLE);
		map.put(PokemonId.KAKUNA, PokemonFamilyId.FAMILY_WEEDLE);
		map.put(PokemonId.BEEDRILL, PokemonFamilyId.FAMILY_WEEDLE);
		map.put(PokemonId.PIDGEY, PokemonFamilyId.FAMILY_PIDGEY);
		map.put(PokemonId.PIDGEOTTO, PokemonFamilyId.FAMILY_PIDGEY);
		map.put(PokemonId.PIDGEOT, PokemonFamilyId.FAMILY_PIDGEY);
		map.put(PokemonId.RATTATA, PokemonFamilyId.FAMILY_RATTATA);
		map.put(PokemonId.RATICATE, PokemonFamilyId.FAMILY_RATTATA);
		map.put(PokemonId.SPEAROW, PokemonFamilyId.FAMILY_SPEAROW);
		map.put(PokemonId.FEAROW, PokemonFamilyId.FAMILY_SPEAROW);
		map.put(PokemonId.EKANS, PokemonFamilyId.FAMILY_EKANS);
		map.put(PokemonId.ARBOK, PokemonFamilyId.FAMILY_EKANS);
		map.put(PokemonId.PIKACHU, PokemonFamilyId.FAMILY_PIKACHU);
		map.put(PokemonId.RAICHU, PokemonFamilyId.FAMILY_PIKACHU);
		map.put(PokemonId.SANDSHREW, PokemonFamilyId.FAMILY_SANDSHREW);
		map.put(PokemonId.SANDLASH, PokemonFamilyId.FAMILY_SANDSHREW);
		map.put(PokemonId.NIDORAN_FEMALE, PokemonFamilyId.FAMILY_NIDORAN);
		map.put(PokemonId.NIDORINA, PokemonFamilyId.FAMILY_NIDORAN);
		map.put(PokemonId.NIDOQUEEN, PokemonFamilyId.FAMILY_NIDORAN);
		map.put(PokemonId.NIDORAN_MALE, PokemonFamilyId.FAMILY_NIDORAN2);
		map.put(PokemonId.NIDORINO, PokemonFamilyId.FAMILY_NIDORAN2);
		map.put(PokemonId.NIDOKING, PokemonFamilyId.FAMILY_NIDORAN2);
		map.put(PokemonId.CLEFARY, PokemonFamilyId.FAMILY_CLEFAIRY);
		map.put(PokemonId.CLEFABLE, PokemonFamilyId.FAMILY_CLEFAIRY);
		map.put(PokemonId.VULPIX, PokemonFamilyId.FAMILY_VULPIX);
		map.put(PokemonId.NINETALES, PokemonFamilyId.FAMILY_VULPIX);
		map.put(PokemonId.JIGGLYPUFF, PokemonFamilyId.FAMILY_JIGGLYPUFF);
		map.put(PokemonId.WIGGLYTUFF, PokemonFamilyId.FAMILY_JIGGLYPUFF);
		map.put(PokemonId.ZUBAT, PokemonFamilyId.FAMILY_ZUBAT);
		map.put(PokemonId.GOLBAT, PokemonFamilyId.FAMILY_ZUBAT);
		map.put(PokemonId.ODDISH, PokemonFamilyId.FAMILY_ODDISH);
		map.put(PokemonId.GLOOM, PokemonFamilyId.FAMILY_ODDISH);
		map.put(PokemonId.VILEPLUME, PokemonFamilyId.FAMILY_ODDISH);
		map.put(PokemonId.PARAS, PokemonFamilyId.FAMILY_PARAS);
		map.put(PokemonId.PARASECT, PokemonFamilyId.FAMILY_PARAS);
		map.put(PokemonId.VENONAT, PokemonFamilyId.FAMILY_VENONAT);
		map.put(PokemonId.VENOMOTH, PokemonFamilyId.FAMILY_VENONAT);
		map.put(PokemonId.DIGLETT, PokemonFamilyId.FAMILY_DIGLETT);
		map.put(PokemonId.DUGTRIO, PokemonFamilyId.FAMILY_DIGLETT);
		map.put(PokemonId.MEOWTH, PokemonFamilyId.FAMILY_MEOWTH);
		map.put(PokemonId.PERSIAN, PokemonFamilyId.FAMILY_MEOWTH);
		map.put(PokemonId.PSYDUCK, PokemonFamilyId.FAMILY_PSYDUCK);
		map.put(PokemonId.GOLDUCK, PokemonFamilyId.FAMILY_PSYDUCK);
		map.put(PokemonId.MANKEY, PokemonFamilyId.FAMILY_MANKEY);
		map.put(PokemonId.PRIMEAPE, PokemonFamilyId.FAMILY_MANKEY);
		map.put(PokemonId.GROWLITHE, PokemonFamilyId.FAMILY_GROWLITHE);
		map.put(PokemonId.ARCANINE, PokemonFamilyId.FAMILY_GROWLITHE);
		map.put(PokemonId.POLIWAG, PokemonFamilyId.FAMILY_POLIWAG);
		map.put(PokemonId.POLIWHIRL, PokemonFamilyId.FAMILY_POLIWAG);
		map.put(PokemonId.POLIWRATH, PokemonFamilyId.FAMILY_POLIWAG);
		map.put(PokemonId.ABRA, PokemonFamilyId.FAMILY_ABRA);
		map.put(PokemonId.KADABRA, PokemonFamilyId.FAMILY_ABRA);
		map.put(PokemonId.ALAKHAZAM, PokemonFamilyId.FAMILY_ABRA);
		map.put(PokemonId.MACHOP, PokemonFamilyId.FAMILY_MACHOP);
		map.put(PokemonId.MACHOKE, PokemonFamilyId.FAMILY_MACHOP);
		map.put(PokemonId.MACHAMP, PokemonFamilyId.FAMILY_MACHOP);
		map.put(PokemonId.BELLSPROUT, PokemonFamilyId.FAMILY_BELLSPROUT);
		map.put(PokemonId.WEEPINBELL, PokemonFamilyId.FAMILY_BELLSPROUT);
		map.put(PokemonId.VICTREEBELL, PokemonFamilyId.FAMILY_BELLSPROUT);
		map.put(PokemonId.TENTACOOL, PokemonFamilyId.FAMILY_TENTACOOL);
		map.put(PokemonId.TENTACRUEL, PokemonFamilyId.FAMILY_TENTACOOL);
		map.put(PokemonId.GEODUGE, PokemonFamilyId.FAMILY_GEODUDE);
		map.put(PokemonId.GRAVELER, PokemonFamilyId.FAMILY_GEODUDE);
		map.put(PokemonId.GOLEM, PokemonFamilyId.FAMILY_GEODUDE);
		map.put(PokemonId.PONYTA, PokemonFamilyId.FAMILY_PONYTA);
		map.put(PokemonId.RAPIDASH, PokemonFamilyId.FAMILY_PONYTA);
		map.put(PokemonId.SLOWPOKE, PokemonFamilyId.FAMILY_SLOWPOKE);
		map.put(PokemonId.SLOWBRO, PokemonFamilyId.FAMILY_SLOWPOKE);
		map.put(PokemonId.MAGNEMITE, PokemonFamilyId.FAMILY_MAGNEMITE);
		map.put(PokemonId.MAGNETON, PokemonFamilyId.FAMILY_MAGNEMITE);
		map.put(PokemonId.FARFETCHD, PokemonFamilyId.FAMILY_FARFETCHD);
		map.put(PokemonId.DODUO, PokemonFamilyId.FAMILY_DODUO);
		map.put(PokemonId.DODRIO, PokemonFamilyId.FAMILY_DODUO);
		map.put(PokemonId.SEEL, PokemonFamilyId.FAMILY_SEEL);
		map.put(PokemonId.DEWGONG, PokemonFamilyId.FAMILY_SEEL);
		map.put(PokemonId.GRIMER, PokemonFamilyId.FAMILY_GRIMER);
		map.put(PokemonId.MUK, PokemonFamilyId.FAMILY_GRIMER);
		map.put(PokemonId.SHELLDER, PokemonFamilyId.FAMILY_SHELLDER);
		map.put(PokemonId.CLOYSTER, PokemonFamilyId.FAMILY_SHELLDER);
		map.put(PokemonId.GASTLY, PokemonFamilyId.FAMILY_GASTLY);
		map.put(PokemonId.HAUNTER, PokemonFamilyId.FAMILY_GASTLY);
		map.put(PokemonId.GENGAR, PokemonFamilyId.FAMILY_GASTLY);
		map.put(PokemonId.ONIX, PokemonFamilyId.FAMILY_ONIX);
		map.put(PokemonId.DROWZEE, PokemonFamilyId.FAMILY_DROWZEE);
		// MISSING ENUM IN PROTO
		//map.put(PokemonId.HYPNO,PokemonFamilyId.FAMILY_HYPNO);
		map.put(PokemonId.KRABBY, PokemonFamilyId.FAMILY_KRABBY);
		map.put(PokemonId.KINGLER, PokemonFamilyId.FAMILY_KRABBY);
		map.put(PokemonId.VOLTORB, PokemonFamilyId.FAMILY_VOLTORB);
		map.put(PokemonId.ELECTRODE, PokemonFamilyId.FAMILY_VOLTORB);
		map.put(PokemonId.EXEGGCUTE, PokemonFamilyId.FAMILY_EXEGGCUTE);
		map.put(PokemonId.EXEGGUTOR, PokemonFamilyId.FAMILY_EXEGGCUTE);
		map.put(PokemonId.CUBONE, PokemonFamilyId.FAMILY_CUBONE);
		map.put(PokemonId.MAROWAK, PokemonFamilyId.FAMILY_CUBONE);
		map.put(PokemonId.HITMONLEE, PokemonFamilyId.FAMILY_HITMONLEE);
		map.put(PokemonId.HITMONCHAN, PokemonFamilyId.FAMILY_HITMONCHAN);
		map.put(PokemonId.LICKITUNG, PokemonFamilyId.FAMILY_LICKITUNG);
		map.put(PokemonId.KOFFING, PokemonFamilyId.FAMILY_KOFFING);
		map.put(PokemonId.WEEZING, PokemonFamilyId.FAMILY_KOFFING);
		map.put(PokemonId.RHYHORN, PokemonFamilyId.FAMILY_RHYHORN);
		map.put(PokemonId.RHYDON, PokemonFamilyId.FAMILY_RHYHORN);
		map.put(PokemonId.CHANSEY, PokemonFamilyId.FAMILY_CHANSEY);
		map.put(PokemonId.TANGELA, PokemonFamilyId.FAMILY_TANGELA);
		map.put(PokemonId.KANGASKHAN, PokemonFamilyId.FAMILY_KANGASKHAN);
		map.put(PokemonId.HORSEA, PokemonFamilyId.FAMILY_HORSEA);
		map.put(PokemonId.SEADRA, PokemonFamilyId.FAMILY_HORSEA);
		map.put(PokemonId.GOLDEEN, PokemonFamilyId.FAMILY_GOLDEEN);
		map.put(PokemonId.SEAKING, PokemonFamilyId.FAMILY_GOLDEEN);
		map.put(PokemonId.STARYU, PokemonFamilyId.FAMILY_STARYU);
		map.put(PokemonId.STARMIE, PokemonFamilyId.FAMILY_STARYU);
		map.put(PokemonId.MR_MIME, PokemonFamilyId.FAMILY_MR_MIME);
		map.put(PokemonId.SCYTHER, PokemonFamilyId.FAMILY_SCYTHER);
		map.put(PokemonId.JYNX, PokemonFamilyId.FAMILY_JYNX);
		map.put(PokemonId.ELECTABUZZ, PokemonFamilyId.FAMILY_ELECTABUZZ);
		map.put(PokemonId.MAGMAR, PokemonFamilyId.FAMILY_MAGMAR);
		map.put(PokemonId.PINSIR, PokemonFamilyId.FAMILY_PINSIR);
		map.put(PokemonId.TAUROS, PokemonFamilyId.FAMILY_TAUROS);
		map.put(PokemonId.MAGIKARP, PokemonFamilyId.FAMILY_MAGIKARP);
		map.put(PokemonId.GYARADOS, PokemonFamilyId.FAMILY_MAGIKARP);
		map.put(PokemonId.LAPRAS, PokemonFamilyId.FAMILY_LAPRAS);
		map.put(PokemonId.DITTO, PokemonFamilyId.FAMILY_DITTO);
		map.put(PokemonId.EEVEE, PokemonFamilyId.FAMILY_EEVEE);
		map.put(PokemonId.JOLTEON, PokemonFamilyId.FAMILY_EEVEE);
		map.put(PokemonId.VAPOREON, PokemonFamilyId.FAMILY_EEVEE);
		map.put(PokemonId.FLAREON, PokemonFamilyId.FAMILY_EEVEE);
		map.put(PokemonId.PORYGON, PokemonFamilyId.FAMILY_PORYGON);
		map.put(PokemonId.OMANYTE, PokemonFamilyId.FAMILY_OMANYTE);
		map.put(PokemonId.OMASTAR, PokemonFamilyId.FAMILY_OMANYTE);
		map.put(PokemonId.KABUTO, PokemonFamilyId.FAMILY_KABUTO);
		map.put(PokemonId.KABUTOPS, PokemonFamilyId.FAMILY_KABUTO);
		map.put(PokemonId.AERODACTYL, PokemonFamilyId.FAMILY_AERODACTYL);
		map.put(PokemonId.SNORLAX, PokemonFamilyId.FAMILY_SNORLAX);
		map.put(PokemonId.ARTICUNO, PokemonFamilyId.FAMILY_ARTICUNO);
		map.put(PokemonId.ZAPDOS, PokemonFamilyId.FAMILY_ZAPDOS);
		map.put(PokemonId.MOLTRES, PokemonFamilyId.FAMILY_MOLTRES);
		map.put(PokemonId.DRATINI, PokemonFamilyId.FAMILY_DRATINI);
		map.put(PokemonId.DRAGONAIR, PokemonFamilyId.FAMILY_DRATINI);
		map.put(PokemonId.DRAGONITE, PokemonFamilyId.FAMILY_DRATINI);
		map.put(PokemonId.MEWTWO, PokemonFamilyId.FAMILY_MEWTWO);
		map.put(PokemonId.MEW, PokemonFamilyId.FAMILY_MEW);

	}

	public static PokemonFamilyId getFamily(PokemonId id) {
		if (map == null)
		{
			registerValues();
		}

		return map.get(id);
	}


}
