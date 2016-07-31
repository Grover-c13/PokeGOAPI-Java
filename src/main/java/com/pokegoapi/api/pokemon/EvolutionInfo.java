package com.pokegoapi.api.pokemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import POGOProtos.Enums.PokemonIdOuterClass;

import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ABRA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.AERODACTYL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ALAKAZAM;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ARBOK;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ARCANINE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ARTICUNO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.BEEDRILL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.BELLSPROUT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.BLASTOISE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.BULBASAUR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.BUTTERFREE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CATERPIE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CHANSEY;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CHARIZARD;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CHARMANDER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CHARMELEON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CLEFABLE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CLEFAIRY;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CLOYSTER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.CUBONE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DEWGONG;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DIGLETT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DITTO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DODRIO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DODUO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DRAGONAIR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DRAGONITE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DRATINI;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DROWZEE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.DUGTRIO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.EEVEE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.EKANS;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ELECTABUZZ;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ELECTRODE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.EXEGGCUTE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.EXEGGUTOR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.FARFETCHD;
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
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.HITMONCHAN;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.HITMONLEE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.HORSEA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.HYPNO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.IVYSAUR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.JIGGLYPUFF;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.JOLTEON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.JYNX;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KABUTO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KABUTOPS;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KADABRA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KAKUNA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KANGASKHAN;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KINGLER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KOFFING;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.KRABBY;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.LAPRAS;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.LICKITUNG;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MACHAMP;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MACHOKE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MACHOP;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MAGIKARP;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MAGMAR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MAGNEMITE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MAGNETON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MANKEY;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MAROWAK;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MEOWTH;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.METAPOD;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MEW;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MEWTWO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MOLTRES;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.MR_MIME;
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
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ONIX;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PARAS;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PARASECT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PERSIAN;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PIDGEOT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PIDGEOTTO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PIDGEY;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PIKACHU;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PINSIR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.POLIWAG;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.POLIWHIRL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.POLIWRATH;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PONYTA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.PORYGON;
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
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SCYTHER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SEADRA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SEAKING;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SEEL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SHELLDER;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SLOWBRO;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SLOWPOKE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SNORLAX;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SPEAROW;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.SQUIRTLE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.STARMIE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.STARYU;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.TANGELA;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.TAUROS;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.TENTACOOL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.TENTACRUEL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VAPOREON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VENOMOTH;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VENONAT;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VENUSAUR;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VICTREEBEL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VILEPLUME;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VOLTORB;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VULPIX;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.WARTORTLE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.WEEDLE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.WEEPINBELL;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.WEEZING;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.WIGGLYTUFF;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ZAPDOS;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.ZUBAT;
import static java.util.Arrays.asList;

class EvolutionInfo {
	private static final PokemonIdOuterClass.PokemonId[] BULBASAUR_EVOLUTION = {BULBASAUR, IVYSAUR, VENUSAUR};
	private static final PokemonIdOuterClass.PokemonId[] CHARMANDER_EVOLUTION = {CHARMANDER, CHARMELEON, CHARIZARD};
	private static final PokemonIdOuterClass.PokemonId[] SQUIRTLE_EVOLUTION = {SQUIRTLE, WARTORTLE, BLASTOISE};
	private static final PokemonIdOuterClass.PokemonId[] CATERPIE_EVOLUTION = {CATERPIE, METAPOD, BUTTERFREE};
	private static final PokemonIdOuterClass.PokemonId[] WEEDLE_EVOLUTION = {WEEDLE, KAKUNA, BEEDRILL};
	private static final PokemonIdOuterClass.PokemonId[] PIDGEY_EVOLUTION = {PIDGEY, PIDGEOTTO, PIDGEOT};
	private static final PokemonIdOuterClass.PokemonId[] RATTATA_EVOLUTION = {RATTATA, RATICATE};
	private static final PokemonIdOuterClass.PokemonId[] SPEAROW_EVOLUTION = {SPEAROW, FEAROW};
	private static final PokemonIdOuterClass.PokemonId[] EKANS_EVOLUTION = {EKANS, ARBOK};
	private static final PokemonIdOuterClass.PokemonId[] PIKACHU_EVOLUTION = {PIKACHU, RAICHU};
	private static final PokemonIdOuterClass.PokemonId[] SANDSHREW_EVOLUTION = {SANDSHREW, SANDSLASH};
	private static final PokemonIdOuterClass.PokemonId[] NIDORAN_FEMALE_EVOLUTION = {NIDORAN_FEMALE, NIDORINA, NIDOQUEEN};
	private static final PokemonIdOuterClass.PokemonId[] NIDORAN_MALE_EVOLUTION = {NIDORAN_MALE, NIDORINO, NIDOKING};
	private static final PokemonIdOuterClass.PokemonId[] CLEFAIRY_EVOLUTION = {CLEFAIRY, CLEFABLE};
	private static final PokemonIdOuterClass.PokemonId[] VULPIX_EVOLUTION = {VULPIX, NINETALES};
	private static final PokemonIdOuterClass.PokemonId[] JIGGLYPUFF_EVOLUTION = {JIGGLYPUFF, WIGGLYTUFF};
	private static final PokemonIdOuterClass.PokemonId[] ZUBAT_EVOLUTION = {ZUBAT, GOLBAT};
	private static final PokemonIdOuterClass.PokemonId[] ODDISH_EVOLUTION = {ODDISH, GLOOM, VILEPLUME};
	private static final PokemonIdOuterClass.PokemonId[] PARAS_EVOLUTION = {PARAS, PARASECT};
	private static final PokemonIdOuterClass.PokemonId[] VENONAT_EVOLUTION = {VENONAT, VENOMOTH};
	private static final PokemonIdOuterClass.PokemonId[] DIGLETT_EVOLUTION = {DIGLETT, DUGTRIO};
	private static final PokemonIdOuterClass.PokemonId[] MEOWTH_EVOLUTION = {MEOWTH, PERSIAN};
	private static final PokemonIdOuterClass.PokemonId[] PSYDUCK_EVOLUTION = {PSYDUCK, GOLDUCK};
	private static final PokemonIdOuterClass.PokemonId[] MANKEY_EVOLUTION = {MANKEY, PRIMEAPE};
	private static final PokemonIdOuterClass.PokemonId[] GROWLITHE_EVOLUTION = {GROWLITHE, ARCANINE};
	private static final PokemonIdOuterClass.PokemonId[] POLIWAG_EVOLUTION = {POLIWAG, POLIWHIRL, POLIWRATH};
	private static final PokemonIdOuterClass.PokemonId[] ABRA_EVOLUTION = {ABRA, KADABRA, ALAKAZAM};
	private static final PokemonIdOuterClass.PokemonId[] MACHOP_EVOLUTION = {MACHOP, MACHOKE, MACHAMP};
	private static final PokemonIdOuterClass.PokemonId[] BELLSPROUT_EVOLUTION = {BELLSPROUT, WEEPINBELL, VICTREEBEL};
	private static final PokemonIdOuterClass.PokemonId[] TENTACOOL_EVOLUTION = {TENTACOOL, TENTACRUEL};
	private static final PokemonIdOuterClass.PokemonId[] GEODUDE_EVOLUTION = {GEODUDE, GRAVELER, GOLEM};
	private static final PokemonIdOuterClass.PokemonId[] PONYTA_EVOLUTION = {PONYTA, RAPIDASH};
	private static final PokemonIdOuterClass.PokemonId[] SLOWPOKE_EVOLUTION = {SLOWPOKE, SLOWBRO};
	private static final PokemonIdOuterClass.PokemonId[] MAGNEMITE_EVOLUTION = {MAGNEMITE, MAGNETON};
	private static final PokemonIdOuterClass.PokemonId[] FARFETCHD_EVOLUTION = {FARFETCHD};
	private static final PokemonIdOuterClass.PokemonId[] DODUO_EVOLUTION = {DODUO, DODRIO};
	private static final PokemonIdOuterClass.PokemonId[] SEEL_EVOLUTION = {SEEL, DEWGONG};
	private static final PokemonIdOuterClass.PokemonId[] GRIMER_EVOLUTION = {GRIMER, MUK};
	private static final PokemonIdOuterClass.PokemonId[] SHELLDER_EVOLUTION = {SHELLDER, CLOYSTER};
	private static final PokemonIdOuterClass.PokemonId[] GASTLY_EVOLUTION = {GASTLY, HAUNTER, GENGAR};
	private static final PokemonIdOuterClass.PokemonId[] ONIX_EVOLUTION = {ONIX};
	private static final PokemonIdOuterClass.PokemonId[] DROWZEE_EVOLUTION = {DROWZEE, HYPNO};
	private static final PokemonIdOuterClass.PokemonId[] KRABBY_EVOLUTION = {KRABBY, KINGLER};
	private static final PokemonIdOuterClass.PokemonId[] VOLTORB_EVOLUTION = {VOLTORB, ELECTRODE};
	private static final PokemonIdOuterClass.PokemonId[] EXEGGCUTE_EVOLUTION = {EXEGGCUTE, EXEGGUTOR};
	private static final PokemonIdOuterClass.PokemonId[] CUBONE_EVOLUTION = {CUBONE, MAROWAK};
	private static final PokemonIdOuterClass.PokemonId[] HITMONLEE_EVOLUTION = {HITMONLEE, HITMONCHAN};
	private static final PokemonIdOuterClass.PokemonId[] LICKITUNG_EVOLUTION = {LICKITUNG};
	private static final PokemonIdOuterClass.PokemonId[] KOFFING_EVOLUTION = {KOFFING, WEEZING};
	private static final PokemonIdOuterClass.PokemonId[] RHYHORN_EVOLUTION = {RHYHORN, RHYDON};
	private static final PokemonIdOuterClass.PokemonId[] CHANSEY_EVOLUTION = {CHANSEY};
	private static final PokemonIdOuterClass.PokemonId[] TANGELA_EVOLUTION = {TANGELA};
	private static final PokemonIdOuterClass.PokemonId[] KANGASKHAN_EVOLUTION = {KANGASKHAN};
	private static final PokemonIdOuterClass.PokemonId[] HORSEA_EVOLUTION = {HORSEA, SEADRA};
	private static final PokemonIdOuterClass.PokemonId[] GOLDEEN_EVOLUTION = {GOLDEEN, SEAKING};
	private static final PokemonIdOuterClass.PokemonId[] STARYU_EVOLUTION = {STARYU, STARMIE};
	private static final PokemonIdOuterClass.PokemonId[] MR_MIME_EVOLUTION = {MR_MIME};
	private static final PokemonIdOuterClass.PokemonId[] SCYTHER_EVOLUTION = {SCYTHER};
	private static final PokemonIdOuterClass.PokemonId[] JYNX_EVOLUTION = {JYNX};
	private static final PokemonIdOuterClass.PokemonId[] ELECTABUZZ_EVOLUTION = {ELECTABUZZ};
	private static final PokemonIdOuterClass.PokemonId[] MAGMAR_EVOLUTION = {MAGMAR};
	private static final PokemonIdOuterClass.PokemonId[] PINSIR_EVOLUTION = {PINSIR};
	private static final PokemonIdOuterClass.PokemonId[] TAUROS_EVOLUTION = {TAUROS};
	private static final PokemonIdOuterClass.PokemonId[] MAGIKARP_EVOLUTION = {MAGIKARP, GYARADOS};
	private static final PokemonIdOuterClass.PokemonId[] LAPRAS_EVOLUTION = {LAPRAS};
	private static final PokemonIdOuterClass.PokemonId[] DITTO_EVOLUTION = {DITTO};

	// needs to be handled exceptionally
	private static final PokemonIdOuterClass.PokemonId[] EEVEE_EVOLUTION = {EEVEE, VAPOREON, JOLTEON, FLAREON};

	private static final PokemonIdOuterClass.PokemonId[] PORYGON_EVOLUTION = {PORYGON};
	private static final PokemonIdOuterClass.PokemonId[] OMANYTE_EVOLUTION = {OMANYTE, OMASTAR};
	private static final PokemonIdOuterClass.PokemonId[] KABUTO_EVOLUTION = {KABUTO, KABUTOPS};
	private static final PokemonIdOuterClass.PokemonId[] AERODACTYL_EVOLUTION = {AERODACTYL};
	private static final PokemonIdOuterClass.PokemonId[] SNORLAX_EVOLUTION = {SNORLAX};
	private static final PokemonIdOuterClass.PokemonId[] ARTICUNO_EVOLUTION = {ARTICUNO};
	private static final PokemonIdOuterClass.PokemonId[] ZAPDOS_EVOLUTION = {ZAPDOS};
	private static final PokemonIdOuterClass.PokemonId[] MOLTRES_EVOLUTION = {MOLTRES};
	private static final PokemonIdOuterClass.PokemonId[] DRATINI_EVOLUTION = {DRATINI, DRAGONAIR, DRAGONITE};
	private static final PokemonIdOuterClass.PokemonId[] MEWTWO_EVOLUTION = {MEWTWO};
	private static final PokemonIdOuterClass.PokemonId[] MEW_EVOLUTION = {MEW};

	private static final Map<PokemonIdOuterClass.PokemonId, PokemonIdOuterClass.PokemonId[]> EVOLUTION_INFO = new HashMap<>();

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
	public static List<EvolutionForm> getEvolutionForms(PokemonIdOuterClass.PokemonId pokemonId) {
		List<EvolutionForm> evolutionForms = new ArrayList<>();
		for (PokemonIdOuterClass.PokemonId id : EVOLUTION_INFO.get(pokemonId)) {
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
	public static boolean isFullyEvolved(PokemonIdOuterClass.PokemonId pokemonId) {
		PokemonIdOuterClass.PokemonId[] info = EVOLUTION_INFO.get(pokemonId);
		return info[info.length] == pokemonId;
	}

	/**
	 * Get evolution stage number
	 *
	 * @param pokemonId pokemon id
	 * @return 0 based evolution stage number
	 */
	public static int getEvolutionStage(PokemonIdOuterClass.PokemonId pokemonId) {
		return asList(VAPOREON, JOLTEON, FLAREON).contains(pokemonId)
				? 1
				: asList(EVOLUTION_INFO.get(pokemonId)).indexOf(pokemonId);
	}
}
