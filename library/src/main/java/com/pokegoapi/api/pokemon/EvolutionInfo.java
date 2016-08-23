package com.pokegoapi.api.pokemon;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.*;
import static java.util.Arrays.asList;

class EvolutionInfo {
	private static final PokemonId[] BULBASAUR_EVOLUTION = {BULBASAUR, IVYSAUR, VENUSAUR};
	private static final PokemonId[] CHARMANDER_EVOLUTION = {CHARMANDER, CHARMELEON, CHARIZARD};
	private static final PokemonId[] SQUIRTLE_EVOLUTION = {SQUIRTLE, WARTORTLE, BLASTOISE};
	private static final PokemonId[] CATERPIE_EVOLUTION = {CATERPIE, METAPOD, BUTTERFREE};
	private static final PokemonId[] WEEDLE_EVOLUTION = {WEEDLE, KAKUNA, BEEDRILL};
	private static final PokemonId[] PIDGEY_EVOLUTION = {PIDGEY, PIDGEOTTO, PIDGEOT};
	private static final PokemonId[] RATTATA_EVOLUTION = {RATTATA, RATICATE};
	private static final PokemonId[] SPEAROW_EVOLUTION = {SPEAROW, FEAROW};
	private static final PokemonId[] EKANS_EVOLUTION = {EKANS, ARBOK};
	private static final PokemonId[] PIKACHU_EVOLUTION = {PIKACHU, RAICHU};
	private static final PokemonId[] SANDSHREW_EVOLUTION = {SANDSHREW, SANDSLASH};
	private static final PokemonId[] NIDORAN_FEMALE_EVOLUTION = {NIDORAN_FEMALE, NIDORINA, NIDOQUEEN};
	private static final PokemonId[] NIDORAN_MALE_EVOLUTION = {NIDORAN_MALE, NIDORINO, NIDOKING};
	private static final PokemonId[] CLEFAIRY_EVOLUTION = {CLEFAIRY, CLEFABLE};
	private static final PokemonId[] VULPIX_EVOLUTION = {VULPIX, NINETALES};
	private static final PokemonId[] JIGGLYPUFF_EVOLUTION = {JIGGLYPUFF, WIGGLYTUFF};
	private static final PokemonId[] ZUBAT_EVOLUTION = {ZUBAT, GOLBAT};
	private static final PokemonId[] ODDISH_EVOLUTION = {ODDISH, GLOOM, VILEPLUME};
	private static final PokemonId[] PARAS_EVOLUTION = {PARAS, PARASECT};
	private static final PokemonId[] VENONAT_EVOLUTION = {VENONAT, VENOMOTH};
	private static final PokemonId[] DIGLETT_EVOLUTION = {DIGLETT, DUGTRIO};
	private static final PokemonId[] MEOWTH_EVOLUTION = {MEOWTH, PERSIAN};
	private static final PokemonId[] PSYDUCK_EVOLUTION = {PSYDUCK, GOLDUCK};
	private static final PokemonId[] MANKEY_EVOLUTION = {MANKEY, PRIMEAPE};
	private static final PokemonId[] GROWLITHE_EVOLUTION = {GROWLITHE, ARCANINE};
	private static final PokemonId[] POLIWAG_EVOLUTION = {POLIWAG, POLIWHIRL, POLIWRATH};
	private static final PokemonId[] ABRA_EVOLUTION = {ABRA, KADABRA, ALAKAZAM};
	private static final PokemonId[] MACHOP_EVOLUTION = {MACHOP, MACHOKE, MACHAMP};
	private static final PokemonId[] BELLSPROUT_EVOLUTION = {BELLSPROUT, WEEPINBELL, VICTREEBEL};
	private static final PokemonId[] TENTACOOL_EVOLUTION = {TENTACOOL, TENTACRUEL};
	private static final PokemonId[] GEODUDE_EVOLUTION = {GEODUDE, GRAVELER, GOLEM};
	private static final PokemonId[] PONYTA_EVOLUTION = {PONYTA, RAPIDASH};
	private static final PokemonId[] SLOWPOKE_EVOLUTION = {SLOWPOKE, SLOWBRO};
	private static final PokemonId[] MAGNEMITE_EVOLUTION = {MAGNEMITE, MAGNETON};
	private static final PokemonId[] FARFETCHD_EVOLUTION = {FARFETCHD};
	private static final PokemonId[] DODUO_EVOLUTION = {DODUO, DODRIO};
	private static final PokemonId[] SEEL_EVOLUTION = {SEEL, DEWGONG};
	private static final PokemonId[] GRIMER_EVOLUTION = {GRIMER, MUK};
	private static final PokemonId[] SHELLDER_EVOLUTION = {SHELLDER, CLOYSTER};
	private static final PokemonId[] GASTLY_EVOLUTION = {GASTLY, HAUNTER, GENGAR};
	private static final PokemonId[] ONIX_EVOLUTION = {ONIX};
	private static final PokemonId[] DROWZEE_EVOLUTION = {DROWZEE, HYPNO};
	private static final PokemonId[] KRABBY_EVOLUTION = {KRABBY, KINGLER};
	private static final PokemonId[] VOLTORB_EVOLUTION = {VOLTORB, ELECTRODE};
	private static final PokemonId[] EXEGGCUTE_EVOLUTION = {EXEGGCUTE, EXEGGUTOR};
	private static final PokemonId[] CUBONE_EVOLUTION = {CUBONE, MAROWAK};
	private static final PokemonId[] HITMONLEE_EVOLUTION = {HITMONLEE, HITMONCHAN};
	private static final PokemonId[] LICKITUNG_EVOLUTION = {LICKITUNG};
	private static final PokemonId[] KOFFING_EVOLUTION = {KOFFING, WEEZING};
	private static final PokemonId[] RHYHORN_EVOLUTION = {RHYHORN, RHYDON};
	private static final PokemonId[] CHANSEY_EVOLUTION = {CHANSEY};
	private static final PokemonId[] TANGELA_EVOLUTION = {TANGELA};
	private static final PokemonId[] KANGASKHAN_EVOLUTION = {KANGASKHAN};
	private static final PokemonId[] HORSEA_EVOLUTION = {HORSEA, SEADRA};
	private static final PokemonId[] GOLDEEN_EVOLUTION = {GOLDEEN, SEAKING};
	private static final PokemonId[] STARYU_EVOLUTION = {STARYU, STARMIE};
	private static final PokemonId[] MR_MIME_EVOLUTION = {MR_MIME};
	private static final PokemonId[] SCYTHER_EVOLUTION = {SCYTHER};
	private static final PokemonId[] JYNX_EVOLUTION = {JYNX};
	private static final PokemonId[] ELECTABUZZ_EVOLUTION = {ELECTABUZZ};
	private static final PokemonId[] MAGMAR_EVOLUTION = {MAGMAR};
	private static final PokemonId[] PINSIR_EVOLUTION = {PINSIR};
	private static final PokemonId[] TAUROS_EVOLUTION = {TAUROS};
	private static final PokemonId[] MAGIKARP_EVOLUTION = {MAGIKARP, GYARADOS};
	private static final PokemonId[] LAPRAS_EVOLUTION = {LAPRAS};
	private static final PokemonId[] DITTO_EVOLUTION = {DITTO};

	// needs to be handled exceptionally
	private static final PokemonId[] EEVEE_EVOLUTION = {EEVEE, VAPOREON, JOLTEON, FLAREON};
	private static final List<PokemonId> EEVEE_FINAL_EVOLUTIONS = asList(VAPOREON, JOLTEON, FLAREON);

	private static final PokemonId[] PORYGON_EVOLUTION = {PORYGON};
	private static final PokemonId[] OMANYTE_EVOLUTION = {OMANYTE, OMASTAR};
	private static final PokemonId[] KABUTO_EVOLUTION = {KABUTO, KABUTOPS};
	private static final PokemonId[] AERODACTYL_EVOLUTION = {AERODACTYL};
	private static final PokemonId[] SNORLAX_EVOLUTION = {SNORLAX};
	private static final PokemonId[] ARTICUNO_EVOLUTION = {ARTICUNO};
	private static final PokemonId[] ZAPDOS_EVOLUTION = {ZAPDOS};
	private static final PokemonId[] MOLTRES_EVOLUTION = {MOLTRES};
	private static final PokemonId[] DRATINI_EVOLUTION = {DRATINI, DRAGONAIR, DRAGONITE};
	private static final PokemonId[] MEWTWO_EVOLUTION = {MEWTWO};
	private static final PokemonId[] MEW_EVOLUTION = {MEW};

	private static final Map<PokemonId, PokemonId[]> EVOLUTION_INFO = new EnumMap<>(PokemonId.class);

	static {
		EVOLUTION_INFO.put(BULBASAUR, BULBASAUR_EVOLUTION);
		EVOLUTION_INFO.put(IVYSAUR, BULBASAUR_EVOLUTION);
		EVOLUTION_INFO.put(VENUSAUR, BULBASAUR_EVOLUTION);
		EVOLUTION_INFO.put(CHARMANDER, CHARMANDER_EVOLUTION);
		EVOLUTION_INFO.put(CHARMELEON, CHARMANDER_EVOLUTION);
		EVOLUTION_INFO.put(CHARIZARD, CHARMANDER_EVOLUTION);
		EVOLUTION_INFO.put(SQUIRTLE, SQUIRTLE_EVOLUTION);
		EVOLUTION_INFO.put(WARTORTLE, SQUIRTLE_EVOLUTION);
		EVOLUTION_INFO.put(BLASTOISE, SQUIRTLE_EVOLUTION);
		EVOLUTION_INFO.put(CATERPIE, CATERPIE_EVOLUTION);
		EVOLUTION_INFO.put(METAPOD, CATERPIE_EVOLUTION);
		EVOLUTION_INFO.put(BUTTERFREE, CATERPIE_EVOLUTION);
		EVOLUTION_INFO.put(WEEDLE, WEEDLE_EVOLUTION);
		EVOLUTION_INFO.put(KAKUNA, WEEDLE_EVOLUTION);
		EVOLUTION_INFO.put(BEEDRILL, WEEDLE_EVOLUTION);
		EVOLUTION_INFO.put(PIDGEY, PIDGEY_EVOLUTION);
		EVOLUTION_INFO.put(PIDGEOTTO, PIDGEY_EVOLUTION);
		EVOLUTION_INFO.put(PIDGEOT, PIDGEY_EVOLUTION);
		EVOLUTION_INFO.put(RATTATA, RATTATA_EVOLUTION);
		EVOLUTION_INFO.put(RATICATE, RATTATA_EVOLUTION);
		EVOLUTION_INFO.put(SPEAROW, SPEAROW_EVOLUTION);
		EVOLUTION_INFO.put(FEAROW, SPEAROW_EVOLUTION);
		EVOLUTION_INFO.put(EKANS, EKANS_EVOLUTION);
		EVOLUTION_INFO.put(ARBOK, EKANS_EVOLUTION);
		EVOLUTION_INFO.put(PIKACHU, PIKACHU_EVOLUTION);
		EVOLUTION_INFO.put(RAICHU, PIKACHU_EVOLUTION);
		EVOLUTION_INFO.put(SANDSHREW, SANDSHREW_EVOLUTION);
		EVOLUTION_INFO.put(SANDSLASH, SANDSHREW_EVOLUTION);
		EVOLUTION_INFO.put(NIDORAN_FEMALE, NIDORAN_FEMALE_EVOLUTION);
		EVOLUTION_INFO.put(NIDORINA, NIDORAN_FEMALE_EVOLUTION);
		EVOLUTION_INFO.put(NIDOQUEEN, NIDORAN_FEMALE_EVOLUTION);
		EVOLUTION_INFO.put(NIDORAN_MALE, NIDORAN_MALE_EVOLUTION);
		EVOLUTION_INFO.put(NIDORINO, NIDORAN_MALE_EVOLUTION);
		EVOLUTION_INFO.put(NIDOKING, NIDORAN_MALE_EVOLUTION);
		EVOLUTION_INFO.put(CLEFAIRY, CLEFAIRY_EVOLUTION);
		EVOLUTION_INFO.put(CLEFABLE, CLEFAIRY_EVOLUTION);
		EVOLUTION_INFO.put(VULPIX, VULPIX_EVOLUTION);
		EVOLUTION_INFO.put(NINETALES, VULPIX_EVOLUTION);
		EVOLUTION_INFO.put(JIGGLYPUFF, JIGGLYPUFF_EVOLUTION);
		EVOLUTION_INFO.put(WIGGLYTUFF, JIGGLYPUFF_EVOLUTION);
		EVOLUTION_INFO.put(ZUBAT, ZUBAT_EVOLUTION);
		EVOLUTION_INFO.put(GOLBAT, ZUBAT_EVOLUTION);
		EVOLUTION_INFO.put(ODDISH, ODDISH_EVOLUTION);
		EVOLUTION_INFO.put(GLOOM, ODDISH_EVOLUTION);
		EVOLUTION_INFO.put(VILEPLUME, ODDISH_EVOLUTION);
		EVOLUTION_INFO.put(PARAS, PARAS_EVOLUTION);
		EVOLUTION_INFO.put(PARASECT, PARAS_EVOLUTION);
		EVOLUTION_INFO.put(VENONAT, VENONAT_EVOLUTION);
		EVOLUTION_INFO.put(VENOMOTH, VENONAT_EVOLUTION);
		EVOLUTION_INFO.put(DIGLETT, DIGLETT_EVOLUTION);
		EVOLUTION_INFO.put(DUGTRIO, DIGLETT_EVOLUTION);
		EVOLUTION_INFO.put(MEOWTH, MEOWTH_EVOLUTION);
		EVOLUTION_INFO.put(PERSIAN, MEOWTH_EVOLUTION);
		EVOLUTION_INFO.put(PSYDUCK, PSYDUCK_EVOLUTION);
		EVOLUTION_INFO.put(GOLDUCK, PSYDUCK_EVOLUTION);
		EVOLUTION_INFO.put(MANKEY, MANKEY_EVOLUTION);
		EVOLUTION_INFO.put(PRIMEAPE, MANKEY_EVOLUTION);
		EVOLUTION_INFO.put(GROWLITHE, GROWLITHE_EVOLUTION);
		EVOLUTION_INFO.put(ARCANINE, GROWLITHE_EVOLUTION);
		EVOLUTION_INFO.put(POLIWAG, POLIWAG_EVOLUTION);
		EVOLUTION_INFO.put(POLIWHIRL, POLIWAG_EVOLUTION);
		EVOLUTION_INFO.put(POLIWRATH, POLIWAG_EVOLUTION);
		EVOLUTION_INFO.put(ABRA, ABRA_EVOLUTION);
		EVOLUTION_INFO.put(KADABRA, ABRA_EVOLUTION);
		EVOLUTION_INFO.put(ALAKAZAM, ABRA_EVOLUTION);
		EVOLUTION_INFO.put(MACHOP, MACHOP_EVOLUTION);
		EVOLUTION_INFO.put(MACHOKE, MACHOP_EVOLUTION);
		EVOLUTION_INFO.put(MACHAMP, MACHOP_EVOLUTION);
		EVOLUTION_INFO.put(BELLSPROUT, BELLSPROUT_EVOLUTION);
		EVOLUTION_INFO.put(WEEPINBELL, BELLSPROUT_EVOLUTION);
		EVOLUTION_INFO.put(VICTREEBEL, BELLSPROUT_EVOLUTION);
		EVOLUTION_INFO.put(TENTACOOL, TENTACOOL_EVOLUTION);
		EVOLUTION_INFO.put(TENTACRUEL, TENTACOOL_EVOLUTION);
		EVOLUTION_INFO.put(GEODUDE, GEODUDE_EVOLUTION);
		EVOLUTION_INFO.put(GRAVELER, GEODUDE_EVOLUTION);
		EVOLUTION_INFO.put(GOLEM, GEODUDE_EVOLUTION);
		EVOLUTION_INFO.put(PONYTA, PONYTA_EVOLUTION);
		EVOLUTION_INFO.put(RAPIDASH, PONYTA_EVOLUTION);
		EVOLUTION_INFO.put(SLOWPOKE, SLOWPOKE_EVOLUTION);
		EVOLUTION_INFO.put(SLOWBRO, SLOWPOKE_EVOLUTION);
		EVOLUTION_INFO.put(MAGNEMITE, MAGNEMITE_EVOLUTION);
		EVOLUTION_INFO.put(MAGNETON, MAGNEMITE_EVOLUTION);
		EVOLUTION_INFO.put(FARFETCHD, FARFETCHD_EVOLUTION);
		EVOLUTION_INFO.put(DODUO, DODUO_EVOLUTION);
		EVOLUTION_INFO.put(DODRIO, DODUO_EVOLUTION);
		EVOLUTION_INFO.put(SEEL, SEEL_EVOLUTION);
		EVOLUTION_INFO.put(DEWGONG, SEEL_EVOLUTION);
		EVOLUTION_INFO.put(GRIMER, GRIMER_EVOLUTION);
		EVOLUTION_INFO.put(MUK, GRIMER_EVOLUTION);
		EVOLUTION_INFO.put(SHELLDER, SHELLDER_EVOLUTION);
		EVOLUTION_INFO.put(CLOYSTER, SHELLDER_EVOLUTION);
		EVOLUTION_INFO.put(GASTLY, GASTLY_EVOLUTION);
		EVOLUTION_INFO.put(HAUNTER, GASTLY_EVOLUTION);
		EVOLUTION_INFO.put(GENGAR, GASTLY_EVOLUTION);
		EVOLUTION_INFO.put(ONIX, ONIX_EVOLUTION);
		EVOLUTION_INFO.put(DROWZEE, DROWZEE_EVOLUTION);
		EVOLUTION_INFO.put(HYPNO, DROWZEE_EVOLUTION);
		EVOLUTION_INFO.put(KRABBY, KRABBY_EVOLUTION);
		EVOLUTION_INFO.put(KINGLER, KRABBY_EVOLUTION);
		EVOLUTION_INFO.put(VOLTORB, VOLTORB_EVOLUTION);
		EVOLUTION_INFO.put(ELECTRODE, VOLTORB_EVOLUTION);
		EVOLUTION_INFO.put(EXEGGCUTE, EXEGGCUTE_EVOLUTION);
		EVOLUTION_INFO.put(EXEGGUTOR, EXEGGCUTE_EVOLUTION);
		EVOLUTION_INFO.put(CUBONE, CUBONE_EVOLUTION);
		EVOLUTION_INFO.put(MAROWAK, CUBONE_EVOLUTION);
		EVOLUTION_INFO.put(HITMONLEE, HITMONLEE_EVOLUTION);
		EVOLUTION_INFO.put(HITMONCHAN, HITMONLEE_EVOLUTION);
		EVOLUTION_INFO.put(LICKITUNG, LICKITUNG_EVOLUTION);
		EVOLUTION_INFO.put(KOFFING, KOFFING_EVOLUTION);
		EVOLUTION_INFO.put(WEEZING, KOFFING_EVOLUTION);
		EVOLUTION_INFO.put(RHYHORN, RHYHORN_EVOLUTION);
		EVOLUTION_INFO.put(RHYDON, RHYHORN_EVOLUTION);
		EVOLUTION_INFO.put(CHANSEY, CHANSEY_EVOLUTION);
		EVOLUTION_INFO.put(TANGELA, TANGELA_EVOLUTION);
		EVOLUTION_INFO.put(KANGASKHAN, KANGASKHAN_EVOLUTION);
		EVOLUTION_INFO.put(HORSEA, HORSEA_EVOLUTION);
		EVOLUTION_INFO.put(SEADRA, HORSEA_EVOLUTION);
		EVOLUTION_INFO.put(GOLDEEN, GOLDEEN_EVOLUTION);
		EVOLUTION_INFO.put(SEAKING, GOLDEEN_EVOLUTION);
		EVOLUTION_INFO.put(STARYU, STARYU_EVOLUTION);
		EVOLUTION_INFO.put(STARMIE, STARYU_EVOLUTION);
		EVOLUTION_INFO.put(MR_MIME, MR_MIME_EVOLUTION);
		EVOLUTION_INFO.put(SCYTHER, SCYTHER_EVOLUTION);
		EVOLUTION_INFO.put(JYNX, JYNX_EVOLUTION);
		EVOLUTION_INFO.put(ELECTABUZZ, ELECTABUZZ_EVOLUTION);
		EVOLUTION_INFO.put(MAGMAR, MAGMAR_EVOLUTION);
		EVOLUTION_INFO.put(PINSIR, PINSIR_EVOLUTION);
		EVOLUTION_INFO.put(TAUROS, TAUROS_EVOLUTION);
		EVOLUTION_INFO.put(MAGIKARP, MAGIKARP_EVOLUTION);
		EVOLUTION_INFO.put(GYARADOS, MAGIKARP_EVOLUTION);
		EVOLUTION_INFO.put(LAPRAS, LAPRAS_EVOLUTION);
		EVOLUTION_INFO.put(DITTO, DITTO_EVOLUTION);

		// needs to be handled exceptionally
		EVOLUTION_INFO.put(EEVEE, EEVEE_EVOLUTION);
		EVOLUTION_INFO.put(VAPOREON, EEVEE_EVOLUTION);
		EVOLUTION_INFO.put(JOLTEON, EEVEE_EVOLUTION);
		EVOLUTION_INFO.put(FLAREON, EEVEE_EVOLUTION);

		EVOLUTION_INFO.put(PORYGON, PORYGON_EVOLUTION);
		EVOLUTION_INFO.put(OMANYTE, OMANYTE_EVOLUTION);
		EVOLUTION_INFO.put(OMASTAR, OMANYTE_EVOLUTION);
		EVOLUTION_INFO.put(KABUTO, KABUTO_EVOLUTION);
		EVOLUTION_INFO.put(KABUTOPS, KABUTO_EVOLUTION);
		EVOLUTION_INFO.put(AERODACTYL, AERODACTYL_EVOLUTION);
		EVOLUTION_INFO.put(SNORLAX, SNORLAX_EVOLUTION);
		EVOLUTION_INFO.put(ARTICUNO, ARTICUNO_EVOLUTION);
		EVOLUTION_INFO.put(ZAPDOS, ZAPDOS_EVOLUTION);
		EVOLUTION_INFO.put(MOLTRES, MOLTRES_EVOLUTION);
		EVOLUTION_INFO.put(DRATINI, DRATINI_EVOLUTION);
		EVOLUTION_INFO.put(DRAGONAIR, DRATINI_EVOLUTION);
		EVOLUTION_INFO.put(DRAGONITE, DRATINI_EVOLUTION);
		EVOLUTION_INFO.put(MEWTWO, MEWTWO_EVOLUTION);
		EVOLUTION_INFO.put(MEW, MEW_EVOLUTION);
	}

	/**
	 * Get evolution forms
	 *
	 * @param pokemonId pokemon id
	 * @return ordered evolution forms
	 */
	public static List<EvolutionForm> getEvolutionForms(PokemonId pokemonId) {
		List<EvolutionForm> evolutionForms = new ArrayList<>();
		for (PokemonId id : EVOLUTION_INFO.get(pokemonId)) {
			evolutionForms.add(new EvolutionForm(id));
		}
		return evolutionForms;
	}

	/**
	 * Tell if a pokemon is fully evolved
	 *
	 * @param pokemonId pokemon id
	 * @return true if a pokemon is fully evolved, false otherwise
	 */
	public static boolean isFullyEvolved(PokemonId pokemonId) {
		if (EEVEE_FINAL_EVOLUTIONS.contains(pokemonId)) {
			return true;
		} else {
			PokemonId[] info = EVOLUTION_INFO.get(pokemonId);
			return info[info.length - 1] == pokemonId;
		}
	}

	/**
	 * Get evolution stage number
	 *
	 * @param pokemonId pokemon id
	 * @return 0 based evolution stage number
	 */
	public static int getEvolutionStage(PokemonId pokemonId) {
		return EEVEE_FINAL_EVOLUTIONS.contains(pokemonId)
				? 1
				: asList(EVOLUTION_INFO.get(pokemonId)).indexOf(pokemonId);
	}
}
