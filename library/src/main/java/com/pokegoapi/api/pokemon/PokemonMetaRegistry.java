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
import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import lombok.Getter;

import java.util.EnumMap;

public class PokemonMetaRegistry {

	@Getter
	private static EnumMap<PokemonFamilyId, PokemonId> highestForFamily = new EnumMap<>(PokemonFamilyId.class);
	@Getter
	private static EnumMap<PokemonId, PokemonMeta> meta = new EnumMap<>(PokemonId.class);

	static {
		highestForFamily.put(PokemonFamilyId.FAMILY_BULBASAUR, PokemonId.VENUSAUR);
		highestForFamily.put(PokemonFamilyId.FAMILY_CHARMANDER, PokemonId.CHARIZARD);
		highestForFamily.put(PokemonFamilyId.FAMILY_SQUIRTLE, PokemonId.BLASTOISE);
		highestForFamily.put(PokemonFamilyId.FAMILY_CATERPIE, PokemonId.BUTTERFREE);
		highestForFamily.put(PokemonFamilyId.FAMILY_WEEDLE, PokemonId.BEEDRILL);
		highestForFamily.put(PokemonFamilyId.FAMILY_PIDGEY, PokemonId.PIDGEOT);
		highestForFamily.put(PokemonFamilyId.FAMILY_RATTATA, PokemonId.RATICATE);
		highestForFamily.put(PokemonFamilyId.FAMILY_SPEAROW, PokemonId.FEAROW);
		highestForFamily.put(PokemonFamilyId.FAMILY_EKANS, PokemonId.ARBOK);
		highestForFamily.put(PokemonFamilyId.FAMILY_PIKACHU, PokemonId.RAICHU);
		highestForFamily.put(PokemonFamilyId.FAMILY_SANDSHREW, PokemonId.SANDSLASH);
		highestForFamily.put(PokemonFamilyId.FAMILY_NIDORAN_FEMALE, PokemonId.NIDOQUEEN);
		highestForFamily.put(PokemonFamilyId.FAMILY_NIDORAN_MALE, PokemonId.NIDOKING);
		highestForFamily.put(PokemonFamilyId.FAMILY_CLEFAIRY, PokemonId.CLEFABLE);
		highestForFamily.put(PokemonFamilyId.FAMILY_VULPIX, PokemonId.NINETALES);
		highestForFamily.put(PokemonFamilyId.FAMILY_JIGGLYPUFF, PokemonId.WIGGLYTUFF);
		highestForFamily.put(PokemonFamilyId.FAMILY_ZUBAT, PokemonId.GOLBAT);
		highestForFamily.put(PokemonFamilyId.FAMILY_ODDISH, PokemonId.VILEPLUME);
		highestForFamily.put(PokemonFamilyId.FAMILY_PARAS, PokemonId.PARASECT);
		highestForFamily.put(PokemonFamilyId.FAMILY_VENONAT, PokemonId.VENOMOTH);
		highestForFamily.put(PokemonFamilyId.FAMILY_DIGLETT, PokemonId.DUGTRIO);
		highestForFamily.put(PokemonFamilyId.FAMILY_MEOWTH, PokemonId.PERSIAN);
		highestForFamily.put(PokemonFamilyId.FAMILY_PSYDUCK, PokemonId.GOLDUCK);
		highestForFamily.put(PokemonFamilyId.FAMILY_MANKEY, PokemonId.PRIMEAPE);
		highestForFamily.put(PokemonFamilyId.FAMILY_GROWLITHE, PokemonId.ARCANINE);
		highestForFamily.put(PokemonFamilyId.FAMILY_POLIWAG, PokemonId.POLIWRATH);
		highestForFamily.put(PokemonFamilyId.FAMILY_ABRA, PokemonId.ALAKAZAM);
		highestForFamily.put(PokemonFamilyId.FAMILY_MACHOP, PokemonId.MACHAMP);
		highestForFamily.put(PokemonFamilyId.FAMILY_BELLSPROUT, PokemonId.VICTREEBEL);
		highestForFamily.put(PokemonFamilyId.FAMILY_TENTACOOL, PokemonId.TENTACRUEL);
		highestForFamily.put(PokemonFamilyId.FAMILY_GEODUDE, PokemonId.GOLEM);
		highestForFamily.put(PokemonFamilyId.FAMILY_PONYTA, PokemonId.RAPIDASH);
		highestForFamily.put(PokemonFamilyId.FAMILY_SLOWPOKE, PokemonId.SLOWBRO);
		highestForFamily.put(PokemonFamilyId.FAMILY_MAGNEMITE, PokemonId.MAGNETON);
		highestForFamily.put(PokemonFamilyId.FAMILY_FARFETCHD, PokemonId.FARFETCHD);
		highestForFamily.put(PokemonFamilyId.FAMILY_DODUO, PokemonId.DODRIO);
		highestForFamily.put(PokemonFamilyId.FAMILY_SEEL, PokemonId.DEWGONG);
		highestForFamily.put(PokemonFamilyId.FAMILY_GRIMER, PokemonId.MUK);
		highestForFamily.put(PokemonFamilyId.FAMILY_SHELLDER, PokemonId.CLOYSTER);
		highestForFamily.put(PokemonFamilyId.FAMILY_GASTLY, PokemonId.GENGAR);
		highestForFamily.put(PokemonFamilyId.FAMILY_ONIX, PokemonId.ONIX);
		highestForFamily.put(PokemonFamilyId.FAMILY_DROWZEE, PokemonId.HYPNO);
		highestForFamily.put(PokemonFamilyId.FAMILY_KRABBY, PokemonId.KINGLER);
		highestForFamily.put(PokemonFamilyId.FAMILY_VOLTORB, PokemonId.ELECTRODE);
		highestForFamily.put(PokemonFamilyId.FAMILY_EXEGGCUTE, PokemonId.EXEGGUTOR);
		highestForFamily.put(PokemonFamilyId.FAMILY_CUBONE, PokemonId.MAROWAK);
		highestForFamily.put(PokemonFamilyId.FAMILY_HITMONLEE, PokemonId.HITMONLEE);
		highestForFamily.put(PokemonFamilyId.FAMILY_HITMONCHAN, PokemonId.HITMONCHAN);
		highestForFamily.put(PokemonFamilyId.FAMILY_LICKITUNG, PokemonId.LICKITUNG);
		highestForFamily.put(PokemonFamilyId.FAMILY_KOFFING, PokemonId.WEEZING);
		highestForFamily.put(PokemonFamilyId.FAMILY_RHYHORN, PokemonId.RHYDON);
		highestForFamily.put(PokemonFamilyId.FAMILY_CHANSEY, PokemonId.CHANSEY);
		highestForFamily.put(PokemonFamilyId.FAMILY_TANGELA, PokemonId.TANGELA);
		highestForFamily.put(PokemonFamilyId.FAMILY_KANGASKHAN, PokemonId.KANGASKHAN);
		highestForFamily.put(PokemonFamilyId.FAMILY_HORSEA, PokemonId.SEADRA);
		highestForFamily.put(PokemonFamilyId.FAMILY_GOLDEEN, PokemonId.SEAKING);
		highestForFamily.put(PokemonFamilyId.FAMILY_STARYU, PokemonId.STARMIE);
		highestForFamily.put(PokemonFamilyId.FAMILY_MR_MIME, PokemonId.MR_MIME);
		highestForFamily.put(PokemonFamilyId.FAMILY_SCYTHER, PokemonId.SCYTHER);
		highestForFamily.put(PokemonFamilyId.FAMILY_JYNX, PokemonId.JYNX);
		highestForFamily.put(PokemonFamilyId.FAMILY_ELECTABUZZ, PokemonId.ELECTABUZZ);
		highestForFamily.put(PokemonFamilyId.FAMILY_MAGMAR, PokemonId.MAGMAR);
		highestForFamily.put(PokemonFamilyId.FAMILY_PINSIR, PokemonId.PINSIR);
		highestForFamily.put(PokemonFamilyId.FAMILY_TAUROS, PokemonId.TAUROS);
		highestForFamily.put(PokemonFamilyId.FAMILY_MAGIKARP, PokemonId.GYARADOS);
		highestForFamily.put(PokemonFamilyId.FAMILY_LAPRAS, PokemonId.LAPRAS);
		highestForFamily.put(PokemonFamilyId.FAMILY_DITTO, PokemonId.DITTO);
		highestForFamily.put(PokemonFamilyId.FAMILY_EEVEE, PokemonId.EEVEE);
		highestForFamily.put(PokemonFamilyId.FAMILY_PORYGON, PokemonId.PORYGON);
		highestForFamily.put(PokemonFamilyId.FAMILY_OMANYTE, PokemonId.OMASTAR);
		highestForFamily.put(PokemonFamilyId.FAMILY_KABUTO, PokemonId.KABUTOPS);
		highestForFamily.put(PokemonFamilyId.FAMILY_AERODACTYL, PokemonId.AERODACTYL);
		highestForFamily.put(PokemonFamilyId.FAMILY_SNORLAX, PokemonId.SNORLAX);
		highestForFamily.put(PokemonFamilyId.FAMILY_ARTICUNO, PokemonId.ARTICUNO);
		highestForFamily.put(PokemonFamilyId.FAMILY_ZAPDOS, PokemonId.ZAPDOS);
		highestForFamily.put(PokemonFamilyId.FAMILY_MOLTRES, PokemonId.MOLTRES);
		highestForFamily.put(PokemonFamilyId.FAMILY_DRATINI, PokemonId.DRAGONITE);
		highestForFamily.put(PokemonFamilyId.FAMILY_MEWTWO, PokemonId.MEWTWO);
		highestForFamily.put(PokemonFamilyId.FAMILY_MEW, PokemonId.MEW);

		PokemonMeta metap;
		metap = new PokemonMeta();
		metap.setTemplateId(" V0001_POKEMON_BULBASAUR");
		metap.setFamily(PokemonFamilyId.FAMILY_BULBASAUR);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(0.7);
		metap.setHeightStdDev(0.0875);
		metap.setBaseStamina(90);
		metap.setCylRadiusM(0.3815);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(126);
		metap.setDiskRadiusM(0.5723);
		metap.setCollisionRadiusM(0.3815);
		metap.setPokedexWeightKg(6.9);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.2725);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1.15);
		metap.setModelScale(1.09);
		metap.setUniqueId("V0001_POKEMON_BULBASAUR");
		metap.setBaseDefense(126);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.8625);
		metap.setCylHeightM(0.763);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.654);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.VINE_WHIP_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.SEED_BOMB,
				PokemonMove.POWER_WHIP
		});
		metap.setNumber(1);
		meta.put(PokemonId.BULBASAUR, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0002_POKEMON_IVYSAUR");
		metap.setFamily(PokemonFamilyId.FAMILY_BULBASAUR);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.51);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(156);
		metap.setDiskRadiusM(0.765);
		metap.setCollisionRadiusM(0.31875);
		metap.setPokedexWeightKg(13);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.255);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1.5);
		metap.setModelScale(0.85);
		metap.setUniqueId("V0002_POKEMON_IVYSAUR");
		metap.setBaseDefense(158);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(1.625);
		metap.setCylHeightM(1.0625);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(0.6375);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.08);
		metap.setParentId(PokemonId.BULBASAUR);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.VINE_WHIP_FAST,
				PokemonMove.RAZOR_LEAF_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.POWER_WHIP,
				PokemonMove.SOLAR_BEAM
		});
		metap.setNumber(2);
		meta.put(PokemonId.IVYSAUR, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0003_POKEMON_VENUSAUR");
		metap.setFamily(PokemonFamilyId.FAMILY_BULBASAUR);
		metap.setPokemonClass(PokemonClass.EPIC);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(2);
		metap.setHeightStdDev(0.25);
		metap.setBaseStamina(160);
		metap.setCylRadiusM(0.759);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(198);
		metap.setDiskRadiusM(1.1385);
		metap.setCollisionRadiusM(0.759);
		metap.setPokedexWeightKg(100);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.3795);
		metap.setMovementTimerS(11);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.69);
		metap.setUniqueId("V0003_POKEMON_VENUSAUR");
		metap.setBaseDefense(200);
		metap.setAttackTimerS(4);
		metap.setWeightStdDev(12.5);
		metap.setCylHeightM(1.2075);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.035);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.04);
		metap.setParentId(PokemonId.IVYSAUR);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.VINE_WHIP_FAST,
				PokemonMove.RAZOR_LEAF_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.PETAL_BLIZZARD,
				PokemonMove.SOLAR_BEAM
		});
		metap.setNumber(3);
		meta.put(PokemonId.VENUSAUR, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0004_POKEMON_CHARMANDER");
		metap.setFamily(PokemonFamilyId.FAMILY_CHARMANDER);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.6);
		metap.setHeightStdDev(0.075);
		metap.setBaseStamina(78);
		metap.setCylRadiusM(0.3125);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(128);
		metap.setDiskRadiusM(0.4688);
		metap.setCollisionRadiusM(0.15625);
		metap.setPokedexWeightKg(8.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.15625);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1.25);
		metap.setUniqueId("V0004_POKEMON_CHARMANDER");
		metap.setBaseDefense(108);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(1.0625);
		metap.setCylHeightM(0.75);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.46875);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SCRATCH_FAST,
				PokemonMove.EMBER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.FLAME_CHARGE,
				PokemonMove.FLAME_BURST,
				PokemonMove.FLAMETHROWER
		});
		metap.setNumber(4);
		meta.put(PokemonId.CHARMANDER, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0005_POKEMON_CHARMELEON");
		metap.setFamily(PokemonFamilyId.FAMILY_CHARMANDER);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.1);
		metap.setHeightStdDev(0.1375);
		metap.setBaseStamina(116);
		metap.setCylRadiusM(0.4635);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(160);
		metap.setDiskRadiusM(0.6953);
		metap.setCollisionRadiusM(0.2575);
		metap.setPokedexWeightKg(19);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.23175);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.03);
		metap.setUniqueId("V0005_POKEMON_CHARMELEON");
		metap.setBaseDefense(140);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(2.375);
		metap.setCylHeightM(1.133);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(0.7725);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.08);
		metap.setParentId(PokemonId.CHARMANDER);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SCRATCH_FAST,
				PokemonMove.EMBER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.FIRE_PUNCH,
				PokemonMove.FLAME_BURST,
				PokemonMove.FLAMETHROWER
		});
		metap.setNumber(5);
		meta.put(PokemonId.CHARMELEON, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0006_POKEMON_CHARIZARD");
		metap.setFamily(PokemonFamilyId.FAMILY_CHARMANDER);
		metap.setPokemonClass(PokemonClass.EPIC);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.7);
		metap.setHeightStdDev(0.2125);
		metap.setBaseStamina(156);
		metap.setCylRadiusM(0.81);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(212);
		metap.setDiskRadiusM(1.215);
		metap.setCollisionRadiusM(0.405);
		metap.setPokedexWeightKg(90.5);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.2025);
		metap.setMovementTimerS(11);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.81);
		metap.setUniqueId("V0006_POKEMON_CHARIZARD");
		metap.setBaseDefense(182);
		metap.setAttackTimerS(4);
		metap.setWeightStdDev(11.3125);
		metap.setCylHeightM(1.377);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.0125);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.04);
		metap.setParentId(PokemonId.CHARMELEON);
		metap.setCylGroundM(0.405);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.WING_ATTACK_FAST,
				PokemonMove.EMBER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DRAGON_CLAW,
				PokemonMove.FLAMETHROWER,
				PokemonMove.FIRE_BLAST
		});
		metap.setNumber(6);
		meta.put(PokemonId.CHARIZARD, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0007_POKEMON_SQUIRTLE");
		metap.setFamily(PokemonFamilyId.FAMILY_SQUIRTLE);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.5);
		metap.setHeightStdDev(0.0625);
		metap.setBaseStamina(88);
		metap.setCylRadiusM(0.3825);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(112);
		metap.setDiskRadiusM(0.5738);
		metap.setCollisionRadiusM(0.2295);
		metap.setPokedexWeightKg(9);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.19125);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.53);
		metap.setUniqueId("V0007_POKEMON_SQUIRTLE");
		metap.setBaseDefense(142);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(1.125);
		metap.setCylHeightM(0.64259988);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.3825);
		metap.setShoulderModeScale(0.1);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.TACKLE_FAST,
				PokemonMove.BUBBLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.AQUA_TAIL,
				PokemonMove.WATER_PULSE,
				PokemonMove.AQUA_JET
		});
		metap.setNumber(7);
		meta.put(PokemonId.SQUIRTLE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0008_POKEMON_WARTORTLE");
		metap.setFamily(PokemonFamilyId.FAMILY_SQUIRTLE);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(118);
		metap.setCylRadiusM(0.375);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(144);
		metap.setDiskRadiusM(0.5625);
		metap.setCollisionRadiusM(0.25);
		metap.setPokedexWeightKg(22.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.1875);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1);
		metap.setUniqueId("V0008_POKEMON_WARTORTLE");
		metap.setBaseDefense(176);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(2.8125);
		metap.setCylHeightM(1);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(0.625);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.08);
		metap.setParentId(PokemonId.SQUIRTLE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.WATER_GUN_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.ICE_BEAM,
				PokemonMove.HYDRO_PUMP,
				PokemonMove.AQUA_JET
		});
		metap.setNumber(8);
		meta.put(PokemonId.WARTORTLE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0009_POKEMON_BLASTOISE");
		metap.setFamily(PokemonFamilyId.FAMILY_SQUIRTLE);
		metap.setPokemonClass(PokemonClass.EPIC);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.6);
		metap.setHeightStdDev(0.2);
		metap.setBaseStamina(158);
		metap.setCylRadiusM(0.564);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(186);
		metap.setDiskRadiusM(0.846);
		metap.setCollisionRadiusM(0.564);
		metap.setPokedexWeightKg(85.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.282);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.94);
		metap.setUniqueId("V0009_POKEMON_BLASTOISE");
		metap.setBaseDefense(222);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(10.6875);
		metap.setCylHeightM(1.2925);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.175);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.04);
		metap.setParentId(PokemonId.WARTORTLE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.WATER_GUN_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.ICE_BEAM,
				PokemonMove.FLASH_CANNON,
				PokemonMove.HYDRO_PUMP
		});
		metap.setNumber(9);
		meta.put(PokemonId.BLASTOISE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0010_POKEMON_CATERPIE");
		metap.setFamily(PokemonFamilyId.FAMILY_CATERPIE);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.3);
		metap.setHeightStdDev(0.0375);
		metap.setBaseStamina(90);
		metap.setCylRadiusM(0.306);
		metap.setBaseFleeRate(0.2);
		metap.setBaseAttack(62);
		metap.setDiskRadiusM(0.459);
		metap.setCollisionRadiusM(0.102);
		metap.setPokedexWeightKg(2.9);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.153);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(0);
		metap.setModelScale(2.04);
		metap.setUniqueId("V0010_POKEMON_CATERPIE");
		metap.setBaseDefense(66);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.3625);
		metap.setCylHeightM(0.408);
		metap.setCandyToEvolve(12);
		metap.setCollisionHeightM(0.306);
		metap.setShoulderModeScale(0);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BUG_BITE_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STRUGGLE
		});
		metap.setNumber(10);
		meta.put(PokemonId.CATERPIE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0011_POKEMON_METAPOD");
		metap.setFamily(PokemonFamilyId.FAMILY_CATERPIE);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.7);
		metap.setHeightStdDev(0.0875);
		metap.setBaseStamina(100);
		metap.setCylRadiusM(0.351);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(56);
		metap.setDiskRadiusM(0.5265);
		metap.setCollisionRadiusM(0.117);
		metap.setPokedexWeightKg(9.9);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.1755);
		metap.setMovementTimerS(3600);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.17);
		metap.setUniqueId("V0011_POKEMON_METAPOD");
		metap.setBaseDefense(86);
		metap.setAttackTimerS(3600);
		metap.setWeightStdDev(1.2375);
		metap.setCylHeightM(0.6435);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.6435);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.2);
		metap.setParentId(PokemonId.CATERPIE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BUG_BITE_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STRUGGLE
		});
		metap.setNumber(11);
		meta.put(PokemonId.METAPOD, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0012_POKEMON_BUTTERFREE");
		metap.setFamily(PokemonFamilyId.FAMILY_CATERPIE);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.1);
		metap.setHeightStdDev(0.1375);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.666);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(144);
		metap.setDiskRadiusM(0.999);
		metap.setCollisionRadiusM(0.1665);
		metap.setPokedexWeightKg(32);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.1776);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.11);
		metap.setUniqueId("V0012_POKEMON_BUTTERFREE");
		metap.setBaseDefense(144);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(4);
		metap.setCylHeightM(1.11);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.555);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.1);
		metap.setParentId(PokemonId.METAPOD);
		metap.setCylGroundM(0.555);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.BUG_BITE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.BUG_BUZZ,
				PokemonMove.PSYCHIC,
				PokemonMove.SIGNAL_BEAM
		});
		metap.setNumber(12);
		meta.put(PokemonId.BUTTERFREE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0013_POKEMON_WEEDLE");
		metap.setFamily(PokemonFamilyId.FAMILY_WEEDLE);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(0.3);
		metap.setHeightStdDev(0.0375);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.209);
		metap.setBaseFleeRate(0.2);
		metap.setBaseAttack(68);
		metap.setDiskRadiusM(0.3135);
		metap.setCollisionRadiusM(0.1045);
		metap.setPokedexWeightKg(3.2);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.15675);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(2.09);
		metap.setUniqueId("V0013_POKEMON_WEEDLE");
		metap.setBaseDefense(64);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.4);
		metap.setCylHeightM(0.418);
		metap.setCandyToEvolve(12);
		metap.setCollisionHeightM(0.209);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POISON_STING_FAST,
				PokemonMove.BUG_BITE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STRUGGLE
		});
		metap.setNumber(13);
		meta.put(PokemonId.WEEDLE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0014_POKEMON_KAKUNA");
		metap.setFamily(PokemonFamilyId.FAMILY_WEEDLE);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(0.6);
		metap.setHeightStdDev(0.075);
		metap.setBaseStamina(90);
		metap.setCylRadiusM(0.25);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(62);
		metap.setDiskRadiusM(0.375);
		metap.setCollisionRadiusM(0.25);
		metap.setPokedexWeightKg(10);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.125);
		metap.setMovementTimerS(3600);
		metap.setJumpTimeS(0);
		metap.setModelScale(1.25);
		metap.setUniqueId("V0014_POKEMON_KAKUNA");
		metap.setBaseDefense(82);
		metap.setAttackTimerS(3600);
		metap.setWeightStdDev(1.25);
		metap.setCylHeightM(0.75);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.75);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.2);
		metap.setParentId(PokemonId.WEEDLE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POISON_STING_FAST,
				PokemonMove.BUG_BITE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STRUGGLE
		});
		metap.setNumber(14);
		meta.put(PokemonId.KAKUNA, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0015_POKEMON_BEEDRILL");
		metap.setFamily(PokemonFamilyId.FAMILY_WEEDLE);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.462);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(144);
		metap.setDiskRadiusM(0.693);
		metap.setCollisionRadiusM(0.308);
		metap.setPokedexWeightKg(29.5);
		metap.setMovementType(MovementType.ELECTRIC);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.231);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.77);
		metap.setUniqueId("V0015_POKEMON_BEEDRILL");
		metap.setBaseDefense(130);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(3.6875);
		metap.setCylHeightM(0.77);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.5775);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.1);
		metap.setParentId(PokemonId.KAKUNA);
		metap.setCylGroundM(0.385);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BUG_BITE_FAST,
				PokemonMove.POISON_JAB_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.AERIAL_ACE,
				PokemonMove.X_SCISSOR
		});
		metap.setNumber(15);
		meta.put(PokemonId.BEEDRILL, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0016_POKEMON_PIDGEY");
		metap.setFamily(PokemonFamilyId.FAMILY_PIDGEY);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(0.3);
		metap.setHeightStdDev(0.0375);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.252);
		metap.setBaseFleeRate(0.2);
		metap.setBaseAttack(94);
		metap.setDiskRadiusM(0.378);
		metap.setCollisionRadiusM(0.1344);
		metap.setPokedexWeightKg(1.8);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.126);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1.4);
		metap.setModelScale(1.68);
		metap.setUniqueId("V0016_POKEMON_PIDGEY");
		metap.setBaseDefense(90);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.225);
		metap.setCylHeightM(0.504);
		metap.setCandyToEvolve(12);
		metap.setCollisionHeightM(0.252);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.TACKLE_FAST,
				PokemonMove.QUICK_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.TWISTER,
				PokemonMove.AERIAL_ACE,
				PokemonMove.AIR_CUTTER
		});
		metap.setNumber(16);
		meta.put(PokemonId.PIDGEY, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0017_POKEMON_PIDGEOTTO");
		metap.setFamily(PokemonFamilyId.FAMILY_PIDGEY);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.1);
		metap.setHeightStdDev(0.1375);
		metap.setBaseStamina(126);
		metap.setCylRadiusM(0.474);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(126);
		metap.setDiskRadiusM(0.711);
		metap.setCollisionRadiusM(0.316);
		metap.setPokedexWeightKg(30);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.237);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.79);
		metap.setUniqueId("V0017_POKEMON_PIDGEOTTO");
		metap.setBaseDefense(122);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(3.75);
		metap.setCylHeightM(0.9875);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.69125);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.2);
		metap.setParentId(PokemonId.PIDGEY);
		metap.setCylGroundM(0.395);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.STEEL_WING_FAST,
				PokemonMove.WING_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.TWISTER,
				PokemonMove.AERIAL_ACE,
				PokemonMove.AIR_CUTTER
		});
		metap.setNumber(17);
		meta.put(PokemonId.PIDGEOTTO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0018_POKEMON_PIDGEOT");
		metap.setFamily(PokemonFamilyId.FAMILY_PIDGEY);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.5);
		metap.setHeightStdDev(0.1875);
		metap.setBaseStamina(166);
		metap.setCylRadiusM(0.864);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(170);
		metap.setDiskRadiusM(1.296);
		metap.setCollisionRadiusM(0.36);
		metap.setPokedexWeightKg(39.5);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.216);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.72);
		metap.setUniqueId("V0018_POKEMON_PIDGEOT");
		metap.setBaseDefense(166);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(4.9375);
		metap.setCylHeightM(1.44);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.008);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.1);
		metap.setParentId(PokemonId.PIDGEOTTO);
		metap.setCylGroundM(0.36);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.STEEL_WING_FAST,
				PokemonMove.WING_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.HURRICANE,
				PokemonMove.AERIAL_ACE,
				PokemonMove.AIR_CUTTER
		});
		metap.setNumber(18);
		meta.put(PokemonId.PIDGEOT, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0019_POKEMON_RATTATA");
		metap.setFamily(PokemonFamilyId.FAMILY_RATTATA);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.3);
		metap.setHeightStdDev(0.0375);
		metap.setBaseStamina(60);
		metap.setCylRadiusM(0.252);
		metap.setBaseFleeRate(0.2);
		metap.setBaseAttack(92);
		metap.setDiskRadiusM(0.378);
		metap.setCollisionRadiusM(0.189);
		metap.setPokedexWeightKg(3.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.126);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(0.9);
		metap.setModelScale(1.26);
		metap.setUniqueId("V0019_POKEMON_RATTATA");
		metap.setBaseDefense(86);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.4375);
		metap.setCylHeightM(0.378);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.252);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.QUICK_ATTACK_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DIG,
				PokemonMove.BODY_SLAM,
				PokemonMove.HYPER_FANG
		});
		metap.setNumber(19);
		meta.put(PokemonId.RATTATA, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0020_POKEMON_RATICATE");
		metap.setFamily(PokemonFamilyId.FAMILY_RATTATA);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.7);
		metap.setHeightStdDev(0.0875);
		metap.setBaseStamina(110);
		metap.setCylRadiusM(0.5265);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(146);
		metap.setDiskRadiusM(0.7898);
		metap.setCollisionRadiusM(0.2925);
		metap.setPokedexWeightKg(18.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.26325);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.17);
		metap.setUniqueId("V0020_POKEMON_RATICATE");
		metap.setBaseDefense(150);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(2.3125);
		metap.setCylHeightM(0.936);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.585);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.RATTATA);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.QUICK_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DIG,
				PokemonMove.HYPER_BEAM,
				PokemonMove.HYPER_FANG
		});
		metap.setNumber(20);
		meta.put(PokemonId.RATICATE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0021_POKEMON_SPEAROW");
		metap.setFamily(PokemonFamilyId.FAMILY_SPEAROW);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(0.3);
		metap.setHeightStdDev(0.0375);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.296);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(102);
		metap.setDiskRadiusM(0.444);
		metap.setCollisionRadiusM(0.148);
		metap.setPokedexWeightKg(2);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.148);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1.48);
		metap.setUniqueId("V0021_POKEMON_SPEAROW");
		metap.setBaseDefense(78);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.25);
		metap.setCylHeightM(0.518);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.2664);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.QUICK_ATTACK_FAST,
				PokemonMove.PECK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.TWISTER,
				PokemonMove.AERIAL_ACE,
				PokemonMove.DRILL_PECK
		});
		metap.setNumber(21);
		meta.put(PokemonId.SPEAROW, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0022_POKEMON_FEAROW");
		metap.setFamily(PokemonFamilyId.FAMILY_SPEAROW);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.2);
		metap.setHeightStdDev(0.15);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.504);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(168);
		metap.setDiskRadiusM(1.26);
		metap.setCollisionRadiusM(0.252);
		metap.setPokedexWeightKg(38);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.126);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.84);
		metap.setUniqueId("V0022_POKEMON_FEAROW");
		metap.setBaseDefense(146);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(4.75);
		metap.setCylHeightM(1.05);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.63);
		metap.setShoulderModeScale(0.375);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.SPEAROW);
		metap.setCylGroundM(0.42);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.STEEL_WING_FAST,
				PokemonMove.PECK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.TWISTER,
				PokemonMove.AERIAL_ACE,
				PokemonMove.DRILL_RUN
		});
		metap.setNumber(22);
		meta.put(PokemonId.FEAROW, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0023_POKEMON_EKANS");
		metap.setFamily(PokemonFamilyId.FAMILY_EKANS);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(2);
		metap.setHeightStdDev(0.25);
		metap.setBaseStamina(70);
		metap.setCylRadiusM(0.4325);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(112);
		metap.setDiskRadiusM(0.6488);
		metap.setCollisionRadiusM(0.2595);
		metap.setPokedexWeightKg(6.9);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.1384);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1.73);
		metap.setUniqueId("V0023_POKEMON_EKANS");
		metap.setBaseDefense(112);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(0.8625);
		metap.setCylHeightM(0.6055);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.346);
		metap.setShoulderModeScale(0.375);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POISON_STING_FAST,
				PokemonMove.ACID_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.WRAP,
				PokemonMove.GUNK_SHOT
		});
		metap.setNumber(23);
		meta.put(PokemonId.EKANS, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0024_POKEMON_ARBOK");
		metap.setFamily(PokemonFamilyId.FAMILY_EKANS);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(3.5);
		metap.setHeightStdDev(0.4375);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.615);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(166);
		metap.setDiskRadiusM(0.9225);
		metap.setCollisionRadiusM(0.41);
		metap.setPokedexWeightKg(65);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.164);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.82);
		metap.setUniqueId("V0024_POKEMON_ARBOK");
		metap.setBaseDefense(166);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(8.125);
		metap.setCylHeightM(1.353);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.353);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.EKANS);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.ACID_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DARK_PULSE,
				PokemonMove.GUNK_SHOT,
				PokemonMove.SLUDGE_WAVE
		});
		metap.setNumber(24);
		meta.put(PokemonId.ARBOK, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0025_POKEMON_PIKACHU");
		metap.setFamily(PokemonFamilyId.FAMILY_PIKACHU);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.4);
		metap.setHeightStdDev(0.05);
		metap.setBaseStamina(70);
		metap.setCylRadiusM(0.37);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(124);
		metap.setDiskRadiusM(0.555);
		metap.setCollisionRadiusM(0.185);
		metap.setPokedexWeightKg(6);
		metap.setMovementType(MovementType.NORMAL);
		metap.setType1(PokemonType.ELECTRIC);
		metap.setCollisionHeadRadiusM(0.185);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.48);
		metap.setUniqueId("V0025_POKEMON_PIKACHU");
		metap.setBaseDefense(108);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.75);
		metap.setCylHeightM(0.74);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.518);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.QUICK_ATTACK_FAST,
				PokemonMove.THUNDER_SHOCK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.THUNDER,
				PokemonMove.THUNDERBOLT,
				PokemonMove.DISCHARGE
		});
		metap.setNumber(25);
		meta.put(PokemonId.PIKACHU, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0026_POKEMON_RAICHU");
		metap.setFamily(PokemonFamilyId.FAMILY_PIKACHU);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.8);
		metap.setHeightStdDev(0.1);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.486);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(200);
		metap.setDiskRadiusM(0.729);
		metap.setCollisionRadiusM(0.27);
		metap.setPokedexWeightKg(30);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ELECTRIC);
		metap.setCollisionHeadRadiusM(0.216);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1.08);
		metap.setUniqueId("V0026_POKEMON_RAICHU");
		metap.setBaseDefense(154);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(3.75);
		metap.setCylHeightM(1.35);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.54);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.08);
		metap.setParentId(PokemonId.PIKACHU);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SPARK_FAST,
				PokemonMove.THUNDER_SHOCK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.THUNDER_PUNCH,
				PokemonMove.THUNDER,
				PokemonMove.BRICK_BREAK
		});
		metap.setNumber(26);
		meta.put(PokemonId.RAICHU, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0027_POKEMON_SANDSHREW");
		metap.setFamily(PokemonFamilyId.FAMILY_SANDSHREW);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.6);
		metap.setHeightStdDev(0.075);
		metap.setBaseStamina(100);
		metap.setCylRadiusM(0.3225);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(90);
		metap.setDiskRadiusM(0.4838);
		metap.setCollisionRadiusM(0.258);
		metap.setPokedexWeightKg(12);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GROUND);
		metap.setCollisionHeadRadiusM(0.1935);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.29);
		metap.setUniqueId("V0027_POKEMON_SANDSHREW");
		metap.setBaseDefense(114);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(1.5);
		metap.setCylHeightM(0.774);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.48375);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.SCRATCH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DIG,
				PokemonMove.ROCK_SLIDE,
				PokemonMove.ROCK_TOMB
		});
		metap.setNumber(27);
		meta.put(PokemonId.SANDSHREW, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0028_POKEMON_SANDSLASH");
		metap.setFamily(PokemonFamilyId.FAMILY_SANDSHREW);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(150);
		metap.setCylRadiusM(0.4);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(150);
		metap.setDiskRadiusM(0.6);
		metap.setCollisionRadiusM(0.35);
		metap.setPokedexWeightKg(29.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GROUND);
		metap.setCollisionHeadRadiusM(0.35);
		metap.setMovementTimerS(11);
		metap.setJumpTimeS(1);
		metap.setModelScale(1);
		metap.setUniqueId("V0028_POKEMON_SANDSLASH");
		metap.setBaseDefense(172);
		metap.setAttackTimerS(4);
		metap.setWeightStdDev(3.6875);
		metap.setCylHeightM(1);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.9);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.SANDSHREW);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.METAL_CLAW_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.BULLDOZE,
				PokemonMove.EARTHQUAKE,
				PokemonMove.ROCK_TOMB
		});
		metap.setNumber(28);
		meta.put(PokemonId.SANDSLASH, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0029_POKEMON_NIDORAN");
		metap.setFamily(PokemonFamilyId.FAMILY_NIDORAN_FEMALE);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.4);
		metap.setHeightStdDev(0.05);
		metap.setBaseStamina(110);
		metap.setCylRadiusM(0.37);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(100);
		metap.setDiskRadiusM(0.555);
		metap.setCollisionRadiusM(0.185);
		metap.setPokedexWeightKg(7);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.185);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1.48);
		metap.setUniqueId("V0029_POKEMON_NIDORAN");
		metap.setBaseDefense(104);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(0.875);
		metap.setCylHeightM(0.666);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.37);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.POISON_STING_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.POISON_FANG,
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.BODY_SLAM
		});
		metap.setNumber(29);
		meta.put(PokemonId.NIDORAN_FEMALE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0030_POKEMON_NIDORINA");
		metap.setFamily(PokemonFamilyId.FAMILY_NIDORAN_FEMALE);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.8);
		metap.setHeightStdDev(0.1);
		metap.setBaseStamina(140);
		metap.setCylRadiusM(0.4388);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(132);
		metap.setDiskRadiusM(0.6581);
		metap.setCollisionRadiusM(0.2925);
		metap.setPokedexWeightKg(20);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.1755);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.17);
		metap.setUniqueId("V0030_POKEMON_NIDORINA");
		metap.setBaseDefense(136);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(2.5);
		metap.setCylHeightM(0.87749988);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(0.585);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.2);
		metap.setParentId(PokemonId.NIDORAN_FEMALE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.POISON_STING_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.POISON_FANG,
				PokemonMove.DIG,
				PokemonMove.SLUDGE_BOMB
		});
		metap.setNumber(30);
		meta.put(PokemonId.NIDORINA, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0031_POKEMON_NIDOQUEEN");
		metap.setFamily(PokemonFamilyId.FAMILY_NIDORAN_FEMALE);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.GROUND);
		metap.setPokedexHeightM(1.3);
		metap.setHeightStdDev(0.1625);
		metap.setBaseStamina(180);
		metap.setCylRadiusM(0.4095);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(184);
		metap.setDiskRadiusM(0.6143);
		metap.setCollisionRadiusM(0.455);
		metap.setPokedexWeightKg(60);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.2275);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.91);
		metap.setUniqueId("V0031_POKEMON_NIDOQUEEN");
		metap.setBaseDefense(190);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(7.5);
		metap.setCylHeightM(1.183);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.79625);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.1);
		metap.setParentId(PokemonId.NIDORINA);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.POISON_JAB_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STONE_EDGE,
				PokemonMove.EARTHQUAKE,
				PokemonMove.SLUDGE_WAVE
		});
		metap.setNumber(31);
		meta.put(PokemonId.NIDOQUEEN, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0032_POKEMON_NIDORAN");
		metap.setFamily(PokemonFamilyId.FAMILY_NIDORAN_MALE);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.5);
		metap.setHeightStdDev(0.0625);
		metap.setBaseStamina(92);
		metap.setCylRadiusM(0.4725);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(110);
		metap.setDiskRadiusM(0.7088);
		metap.setCollisionRadiusM(0.252);
		metap.setPokedexWeightKg(9);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.1575);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.26);
		metap.setUniqueId("V0032_POKEMON_NIDORAN");
		metap.setBaseDefense(94);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(1.125);
		metap.setCylHeightM(0.756);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.315);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POISON_STING_FAST,
				PokemonMove.PECK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.HORN_ATTACK,
				PokemonMove.BODY_SLAM
		});
		metap.setNumber(32);
		meta.put(PokemonId.NIDORAN_MALE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0033_POKEMON_NIDORINO");
		metap.setFamily(PokemonFamilyId.FAMILY_NIDORAN_MALE);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.9);
		metap.setHeightStdDev(0.1125);
		metap.setBaseStamina(122);
		metap.setCylRadiusM(0.495);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(142);
		metap.setDiskRadiusM(0.7425);
		metap.setCollisionRadiusM(0.297);
		metap.setPokedexWeightKg(19.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.2475);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.99);
		metap.setUniqueId("V0033_POKEMON_NIDORINO");
		metap.setBaseDefense(128);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(2.4375);
		metap.setCylHeightM(0.792);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(0.594);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.2);
		metap.setParentId(PokemonId.NIDORAN_MALE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POISON_STING_FAST,
				PokemonMove.POISON_JAB_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.DIG,
				PokemonMove.HORN_ATTACK
		});
		metap.setNumber(33);
		meta.put(PokemonId.NIDORINO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0034_POKEMON_NIDOKING");
		metap.setFamily(PokemonFamilyId.FAMILY_NIDORAN_MALE);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.GROUND);
		metap.setPokedexHeightM(1.4);
		metap.setHeightStdDev(0.175);
		metap.setBaseStamina(162);
		metap.setCylRadiusM(0.5481);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(204);
		metap.setDiskRadiusM(0.8222);
		metap.setCollisionRadiusM(0.5481);
		metap.setPokedexWeightKg(62);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.27405);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.87);
		metap.setUniqueId("V0034_POKEMON_NIDOKING");
		metap.setBaseDefense(170);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(7.75);
		metap.setCylHeightM(1.305);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.87);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.1);
		metap.setParentId(PokemonId.NIDORINO);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.FURY_CUTTER_FAST,
				PokemonMove.POISON_JAB_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.MEGAHORN,
				PokemonMove.EARTHQUAKE,
				PokemonMove.SLUDGE_WAVE
		});
		metap.setNumber(34);
		meta.put(PokemonId.NIDOKING, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0035_POKEMON_CLEFAIRY");
		metap.setFamily(PokemonFamilyId.FAMILY_CLEFAIRY);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.6);
		metap.setHeightStdDev(0.075);
		metap.setBaseStamina(140);
		metap.setCylRadiusM(0.45);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(116);
		metap.setDiskRadiusM(0.675);
		metap.setCollisionRadiusM(0.3125);
		metap.setPokedexWeightKg(7.5);
		metap.setMovementType(MovementType.NORMAL);
		metap.setType1(PokemonType.FAIRY);
		metap.setCollisionHeadRadiusM(0.225);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1.25);
		metap.setUniqueId("V0035_POKEMON_CLEFAIRY");
		metap.setBaseDefense(124);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(0.9375);
		metap.setCylHeightM(0.75);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.75);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POUND_FAST,
				PokemonMove.ZEN_HEADBUTT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DISARMING_VOICE,
				PokemonMove.MOONBLAST,
				PokemonMove.BODY_SLAM
		});
		metap.setNumber(35);
		meta.put(PokemonId.CLEFAIRY, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0036_POKEMON_CLEFABLE");
		metap.setFamily(PokemonFamilyId.FAMILY_CLEFAIRY);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.3);
		metap.setHeightStdDev(0.1625);
		metap.setBaseStamina(190);
		metap.setCylRadiusM(0.712);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(178);
		metap.setDiskRadiusM(1.1681);
		metap.setCollisionRadiusM(0.445);
		metap.setPokedexWeightKg(40);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FAIRY);
		metap.setCollisionHeadRadiusM(0.445);
		metap.setMovementTimerS(4);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.89);
		metap.setUniqueId("V0036_POKEMON_CLEFABLE");
		metap.setBaseDefense(178);
		metap.setAttackTimerS(11);
		metap.setWeightStdDev(5);
		metap.setCylHeightM(1.44625);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.1125);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.08);
		metap.setParentId(PokemonId.CLEFAIRY);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POUND_FAST,
				PokemonMove.ZEN_HEADBUTT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DAZZLING_GLEAM,
				PokemonMove.PSYCHIC,
				PokemonMove.MOONBLAST
		});
		metap.setNumber(36);
		meta.put(PokemonId.CLEFABLE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0037_POKEMON_VULPIX");
		metap.setFamily(PokemonFamilyId.FAMILY_VULPIX);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.6);
		metap.setHeightStdDev(0.075);
		metap.setBaseStamina(76);
		metap.setCylRadiusM(0.567);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(106);
		metap.setDiskRadiusM(0.8505);
		metap.setCollisionRadiusM(0.315);
		metap.setPokedexWeightKg(9.9);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.252);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.26);
		metap.setUniqueId("V0037_POKEMON_VULPIX");
		metap.setBaseDefense(118);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(1.2375);
		metap.setCylHeightM(0.756);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.63);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.EMBER_FAST,
				PokemonMove.QUICK_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.FLAME_CHARGE,
				PokemonMove.FLAMETHROWER,
				PokemonMove.BODY_SLAM
		});
		metap.setNumber(37);
		meta.put(PokemonId.VULPIX, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0038_POKEMON_NINETALES");
		metap.setFamily(PokemonFamilyId.FAMILY_VULPIX);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.1);
		metap.setHeightStdDev(0.1375);
		metap.setBaseStamina(146);
		metap.setCylRadiusM(0.864);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(176);
		metap.setDiskRadiusM(1.296);
		metap.setCollisionRadiusM(0.36);
		metap.setPokedexWeightKg(19.9);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.24);
		metap.setMovementTimerS(5);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.96);
		metap.setUniqueId("V0038_POKEMON_NINETALES");
		metap.setBaseDefense(194);
		metap.setAttackTimerS(14);
		metap.setWeightStdDev(2.4875);
		metap.setCylHeightM(1.2);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.96);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.08);
		metap.setParentId(PokemonId.VULPIX);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.FEINT_ATTACK_FAST,
				PokemonMove.EMBER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.FLAMETHROWER,
				PokemonMove.HEAT_WAVE,
				PokemonMove.FIRE_BLAST
		});
		metap.setNumber(38);
		meta.put(PokemonId.NINETALES, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0039_POKEMON_JIGGLYPUFF");
		metap.setFamily(PokemonFamilyId.FAMILY_JIGGLYPUFF);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.FAIRY);
		metap.setPokedexHeightM(0.5);
		metap.setHeightStdDev(0.0625);
		metap.setBaseStamina(230);
		metap.setCylRadiusM(0.512);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(98);
		metap.setDiskRadiusM(0.768);
		metap.setCollisionRadiusM(0.32);
		metap.setPokedexWeightKg(5.5);
		metap.setMovementType(MovementType.NORMAL);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.256);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(3);
		metap.setModelScale(1.28);
		metap.setUniqueId("V0039_POKEMON_JIGGLYPUFF");
		metap.setBaseDefense(54);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.6875);
		metap.setCylHeightM(0.96);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.64);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POUND_FAST,
				PokemonMove.FEINT_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DISARMING_VOICE,
				PokemonMove.PLAY_ROUGH,
				PokemonMove.BODY_SLAM
		});
		metap.setNumber(39);
		meta.put(PokemonId.JIGGLYPUFF, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0040_POKEMON_WIGGLYTUFF");
		metap.setFamily(PokemonFamilyId.FAMILY_JIGGLYPUFF);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.FAIRY);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(280);
		metap.setCylRadiusM(0.445);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(168);
		metap.setDiskRadiusM(1.0013);
		metap.setCollisionRadiusM(0.356);
		metap.setPokedexWeightKg(12);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.2225);
		metap.setMovementTimerS(4);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.89);
		metap.setUniqueId("V0040_POKEMON_WIGGLYTUFF");
		metap.setBaseDefense(108);
		metap.setAttackTimerS(11);
		metap.setWeightStdDev(1.5);
		metap.setCylHeightM(1.22375);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.89);
		metap.setShoulderModeScale(0.4);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.JIGGLYPUFF);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POUND_FAST,
				PokemonMove.FEINT_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DAZZLING_GLEAM,
				PokemonMove.PLAY_ROUGH,
				PokemonMove.HYPER_BEAM
		});
		metap.setNumber(40);
		meta.put(PokemonId.WIGGLYTUFF, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0041_POKEMON_ZUBAT");
		metap.setFamily(PokemonFamilyId.FAMILY_ZUBAT);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(0.8);
		metap.setHeightStdDev(0.1);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.642);
		metap.setBaseFleeRate(0.2);
		metap.setBaseAttack(88);
		metap.setDiskRadiusM(0.963);
		metap.setCollisionRadiusM(0.0535);
		metap.setPokedexWeightKg(7.5);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.1605);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.07);
		metap.setUniqueId("V0041_POKEMON_ZUBAT");
		metap.setBaseDefense(90);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.9375);
		metap.setCylHeightM(0.6955);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.0535);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.535);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.QUICK_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.POISON_FANG,
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.AIR_CUTTER
		});
		metap.setNumber(41);
		meta.put(PokemonId.ZUBAT, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0042_POKEMON_GOLBAT");
		metap.setFamily(PokemonFamilyId.FAMILY_ZUBAT);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.6);
		metap.setHeightStdDev(0.2);
		metap.setBaseStamina(150);
		metap.setCylRadiusM(0.75);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(164);
		metap.setDiskRadiusM(1.5975);
		metap.setCollisionRadiusM(0.0355);
		metap.setPokedexWeightKg(55);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.355);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.71);
		metap.setUniqueId("V0042_POKEMON_GOLBAT");
		metap.setBaseDefense(164);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(6.875);
		metap.setCylHeightM(1.2425);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.0355);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.ZUBAT);
		metap.setCylGroundM(1.065);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.WING_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.POISON_FANG,
				PokemonMove.AIR_CUTTER,
				PokemonMove.OMINOUS_WIND
		});
		metap.setNumber(42);
		meta.put(PokemonId.GOLBAT, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0043_POKEMON_ODDISH");
		metap.setFamily(PokemonFamilyId.FAMILY_ODDISH);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(0.5);
		metap.setHeightStdDev(0.0625);
		metap.setBaseStamina(90);
		metap.setCylRadiusM(0.405);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(134);
		metap.setDiskRadiusM(0.6075);
		metap.setCollisionRadiusM(0.2025);
		metap.setPokedexWeightKg(5.4);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.2025);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.35);
		metap.setUniqueId("V0043_POKEMON_ODDISH");
		metap.setBaseDefense(130);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.675);
		metap.setCylHeightM(0.81000012);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.50625);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.48);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ACID_FAST,
				PokemonMove.RAZOR_LEAF_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.SEED_BOMB,
				PokemonMove.MOONBLAST
		});
		metap.setNumber(43);
		meta.put(PokemonId.ODDISH, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0044_POKEMON_GLOOM");
		metap.setFamily(PokemonFamilyId.FAMILY_ODDISH);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(0.8);
		metap.setHeightStdDev(0.1);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.495);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(162);
		metap.setDiskRadiusM(0.7425);
		metap.setCollisionRadiusM(0.4125);
		metap.setPokedexWeightKg(8.6);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.2475);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.1);
		metap.setUniqueId("V0044_POKEMON_GLOOM");
		metap.setBaseDefense(158);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(1.075);
		metap.setCylHeightM(0.88000011);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(0.88000011);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.ODDISH);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ACID_FAST,
				PokemonMove.RAZOR_LEAF_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.PETAL_BLIZZARD,
				PokemonMove.MOONBLAST
		});
		metap.setNumber(44);
		meta.put(PokemonId.GLOOM, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0045_POKEMON_VILEPLUME");
		metap.setFamily(PokemonFamilyId.FAMILY_ODDISH);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1.2);
		metap.setHeightStdDev(0.15);
		metap.setBaseStamina(150);
		metap.setCylRadiusM(0.828);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(202);
		metap.setDiskRadiusM(1.242);
		metap.setCollisionRadiusM(1.012);
		metap.setPokedexWeightKg(18.6);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.552);
		metap.setMovementTimerS(11);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.92);
		metap.setUniqueId("V0045_POKEMON_VILEPLUME");
		metap.setBaseDefense(190);
		metap.setAttackTimerS(4);
		metap.setWeightStdDev(2.325);
		metap.setCylHeightM(1.196);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.196);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.12);
		metap.setParentId(PokemonId.GLOOM);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ACID_FAST,
				PokemonMove.RAZOR_LEAF_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.MOONBLAST,
				PokemonMove.PETAL_BLIZZARD,
				PokemonMove.SOLAR_BEAM
		});
		metap.setNumber(45);
		meta.put(PokemonId.VILEPLUME, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0046_POKEMON_PARAS");
		metap.setFamily(PokemonFamilyId.FAMILY_PARAS);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.GRASS);
		metap.setPokedexHeightM(0.3);
		metap.setHeightStdDev(0.0375);
		metap.setBaseStamina(70);
		metap.setCylRadiusM(0.384);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(122);
		metap.setDiskRadiusM(0.576);
		metap.setCollisionRadiusM(0.192);
		metap.setPokedexWeightKg(5.4);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.192);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(1.1);
		metap.setModelScale(1.28);
		metap.setUniqueId("V0046_POKEMON_PARAS");
		metap.setBaseDefense(120);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(0.675);
		metap.setCylHeightM(0.448);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.32);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.32);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BUG_BITE_FAST,
				PokemonMove.SCRATCH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.CROSS_POISON,
				PokemonMove.X_SCISSOR,
				PokemonMove.SEED_BOMB
		});
		metap.setNumber(46);
		meta.put(PokemonId.PARAS, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0047_POKEMON_PARASECT");
		metap.setFamily(PokemonFamilyId.FAMILY_PARAS);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.GRASS);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.6313);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(162);
		metap.setDiskRadiusM(0.9469);
		metap.setCollisionRadiusM(0.4545);
		metap.setPokedexWeightKg(29.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.505);
		metap.setMovementTimerS(17);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1.01);
		metap.setUniqueId("V0047_POKEMON_PARASECT");
		metap.setBaseDefense(170);
		metap.setAttackTimerS(6);
		metap.setWeightStdDev(3.6875);
		metap.setCylHeightM(1.01);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.01);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.PARAS);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BUG_BITE_FAST,
				PokemonMove.FURY_CUTTER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.CROSS_POISON,
				PokemonMove.X_SCISSOR,
				PokemonMove.SOLAR_BEAM
		});
		metap.setNumber(47);
		meta.put(PokemonId.PARASECT, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0048_POKEMON_VENONAT");
		metap.setFamily(PokemonFamilyId.FAMILY_VENONAT);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.5325);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(108);
		metap.setDiskRadiusM(0.7988);
		metap.setCollisionRadiusM(0.355);
		metap.setPokedexWeightKg(30);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.26625);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.71);
		metap.setUniqueId("V0048_POKEMON_VENONAT");
		metap.setBaseDefense(118);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(3.75);
		metap.setCylHeightM(1.1715);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.71);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.BUG_BITE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DAZZLING_GLEAM,
				PokemonMove.SHADOW_BALL,
				PokemonMove.PSYBEAM
		});
		metap.setNumber(48);
		meta.put(PokemonId.VENONAT, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0049_POKEMON_VENOMOTH");
		metap.setFamily(PokemonFamilyId.FAMILY_VENONAT);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1.5);
		metap.setHeightStdDev(0.1875);
		metap.setBaseStamina(140);
		metap.setCylRadiusM(0.576);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(172);
		metap.setDiskRadiusM(0.864);
		metap.setCollisionRadiusM(0.36);
		metap.setPokedexWeightKg(12.5);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.288);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.72);
		metap.setUniqueId("V0049_POKEMON_VENOMOTH");
		metap.setBaseDefense(154);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(1.5625);
		metap.setCylHeightM(1.08);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.72);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.VENONAT);
		metap.setCylGroundM(0.36);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.BUG_BITE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.POISON_FANG,
				PokemonMove.PSYCHIC,
				PokemonMove.BUG_BUZZ
		});
		metap.setNumber(49);
		meta.put(PokemonId.VENOMOTH, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0050_POKEMON_DIGLETT");
		metap.setFamily(PokemonFamilyId.FAMILY_DIGLETT);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.2);
		metap.setHeightStdDev(0.025);
		metap.setBaseStamina(20);
		metap.setCylRadiusM(0.3);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(108);
		metap.setDiskRadiusM(0.45);
		metap.setCollisionRadiusM(0.16);
		metap.setPokedexWeightKg(0.8);
		metap.setMovementType(MovementType.NORMAL);
		metap.setType1(PokemonType.GROUND);
		metap.setCollisionHeadRadiusM(0.18);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(0);
		metap.setModelScale(2);
		metap.setUniqueId("V0050_POKEMON_DIGLETT");
		metap.setBaseDefense(86);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(0.1);
		metap.setCylHeightM(0.4);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.4);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.SCRATCH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DIG,
				PokemonMove.MUD_BOMB,
				PokemonMove.ROCK_TOMB
		});
		metap.setNumber(50);
		meta.put(PokemonId.DIGLETT, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0051_POKEMON_DUGTRIO");
		metap.setFamily(PokemonFamilyId.FAMILY_DIGLETT);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.7);
		metap.setHeightStdDev(0.0875);
		metap.setBaseStamina(70);
		metap.setCylRadiusM(0.672);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(148);
		metap.setDiskRadiusM(1.008);
		metap.setCollisionRadiusM(0.448);
		metap.setPokedexWeightKg(33.3);
		metap.setMovementType(MovementType.NORMAL);
		metap.setType1(PokemonType.GROUND);
		metap.setCollisionHeadRadiusM(0.336);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(0);
		metap.setModelScale(1.12);
		metap.setUniqueId("V0051_POKEMON_DUGTRIO");
		metap.setBaseDefense(140);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(4.1625);
		metap.setCylHeightM(0.84);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.84);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.DIGLETT);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SUCKER_PUNCH_FAST,
				PokemonMove.MUD_SHOT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STONE_EDGE,
				PokemonMove.EARTHQUAKE,
				PokemonMove.MUD_BOMB
		});
		metap.setNumber(51);
		meta.put(PokemonId.DUGTRIO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0052_POKEMON_MEOWTH");
		metap.setFamily(PokemonFamilyId.FAMILY_MEOWTH);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.4);
		metap.setHeightStdDev(0.05);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.4);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(104);
		metap.setDiskRadiusM(0.6);
		metap.setCollisionRadiusM(0.128);
		metap.setPokedexWeightKg(4.2);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.2);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.6);
		metap.setUniqueId("V0052_POKEMON_MEOWTH");
		metap.setBaseDefense(94);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(0.525);
		metap.setCylHeightM(0.64);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.4);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.SCRATCH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DARK_PULSE,
				PokemonMove.NIGHT_SLASH,
				PokemonMove.BODY_SLAM
		});
		metap.setNumber(52);
		meta.put(PokemonId.MEOWTH, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0053_POKEMON_PERSIAN");
		metap.setFamily(PokemonFamilyId.FAMILY_MEOWTH);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.533);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(156);
		metap.setDiskRadiusM(0.7995);
		metap.setCollisionRadiusM(0.328);
		metap.setPokedexWeightKg(32);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.164);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.82);
		metap.setUniqueId("V0053_POKEMON_PERSIAN");
		metap.setBaseDefense(146);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(4);
		metap.setCylHeightM(0.902);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.615);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.MEOWTH);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SCRATCH_FAST,
				PokemonMove.FEINT_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PLAY_ROUGH,
				PokemonMove.POWER_GEM,
				PokemonMove.NIGHT_SLASH
		});
		metap.setNumber(53);
		meta.put(PokemonId.PERSIAN, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0054_POKEMON_PSYDUCK");
		metap.setFamily(PokemonFamilyId.FAMILY_PSYDUCK);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.8);
		metap.setHeightStdDev(0.1);
		metap.setBaseStamina(100);
		metap.setCylRadiusM(0.3638);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(132);
		metap.setDiskRadiusM(0.5456);
		metap.setCollisionRadiusM(0.291);
		metap.setPokedexWeightKg(19.6);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.3395);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.97);
		metap.setUniqueId("V0054_POKEMON_PSYDUCK");
		metap.setBaseDefense(112);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(2.45);
		metap.setCylHeightM(0.97);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.60625);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.WATER_GUN_FAST,
				PokemonMove.ZEN_HEADBUTT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.AQUA_TAIL,
				PokemonMove.PSYBEAM,
				PokemonMove.CROSS_CHOP
		});
		metap.setNumber(54);
		meta.put(PokemonId.PSYDUCK, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0055_POKEMON_GOLDUCK");
		metap.setFamily(PokemonFamilyId.FAMILY_PSYDUCK);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.7);
		metap.setHeightStdDev(0.2125);
		metap.setBaseStamina(160);
		metap.setCylRadiusM(0.465);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(194);
		metap.setDiskRadiusM(0.9765);
		metap.setCollisionRadiusM(0.2325);
		metap.setPokedexWeightKg(76.6);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.2325);
		metap.setMovementTimerS(5);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.93);
		metap.setUniqueId("V0055_POKEMON_GOLDUCK");
		metap.setBaseDefense(176);
		metap.setAttackTimerS(14);
		metap.setWeightStdDev(9.575);
		metap.setCylHeightM(1.3485);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.81375);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.PSYDUCK);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.WATER_GUN_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.HYDRO_PUMP,
				PokemonMove.ICE_BEAM
		});
		metap.setNumber(55);
		meta.put(PokemonId.GOLDUCK, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0056_POKEMON_MANKEY");
		metap.setFamily(PokemonFamilyId.FAMILY_MANKEY);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.5);
		metap.setHeightStdDev(0.0625);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.4838);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(122);
		metap.setDiskRadiusM(0.7256);
		metap.setCollisionRadiusM(0.1935);
		metap.setPokedexWeightKg(28);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIGHTING);
		metap.setCollisionHeadRadiusM(0.129);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.29);
		metap.setUniqueId("V0056_POKEMON_MANKEY");
		metap.setBaseDefense(96);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(3.5);
		metap.setCylHeightM(0.80625);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.645);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.KARATE_CHOP_FAST,
				PokemonMove.SCRATCH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.LOW_SWEEP,
				PokemonMove.BRICK_BREAK,
				PokemonMove.CROSS_CHOP
		});
		metap.setNumber(56);
		meta.put(PokemonId.MANKEY, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0057_POKEMON_PRIMEAPE");
		metap.setFamily(PokemonFamilyId.FAMILY_MANKEY);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.46);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(178);
		metap.setDiskRadiusM(0.69);
		metap.setCollisionRadiusM(0.46);
		metap.setPokedexWeightKg(32);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIGHTING);
		metap.setCollisionHeadRadiusM(0.23);
		metap.setMovementTimerS(17);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.92);
		metap.setUniqueId("V0057_POKEMON_PRIMEAPE");
		metap.setBaseDefense(150);
		metap.setAttackTimerS(6);
		metap.setWeightStdDev(4);
		metap.setCylHeightM(1.15);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.104);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.MANKEY);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.KARATE_CHOP_FAST,
				PokemonMove.LOW_KICK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.LOW_SWEEP,
				PokemonMove.NIGHT_SLASH,
				PokemonMove.CROSS_CHOP
		});
		metap.setNumber(57);
		meta.put(PokemonId.PRIMEAPE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0058_POKEMON_GROWLITHE");
		metap.setFamily(PokemonFamilyId.FAMILY_GROWLITHE);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.7);
		metap.setHeightStdDev(0.0875);
		metap.setBaseStamina(110);
		metap.setCylRadiusM(0.585);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(156);
		metap.setDiskRadiusM(0.8775);
		metap.setCollisionRadiusM(0.234);
		metap.setPokedexWeightKg(19);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.1755);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.17);
		metap.setUniqueId("V0058_POKEMON_GROWLITHE");
		metap.setBaseDefense(110);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(2.375);
		metap.setCylHeightM(1.02375);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.585);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.EMBER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.FLAME_WHEEL,
				PokemonMove.FLAMETHROWER,
				PokemonMove.BODY_SLAM
		});
		metap.setNumber(58);
		meta.put(PokemonId.GROWLITHE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0059_POKEMON_ARCANINE");
		metap.setFamily(PokemonFamilyId.FAMILY_GROWLITHE);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.9);
		metap.setHeightStdDev(0.2375);
		metap.setBaseStamina(180);
		metap.setCylRadiusM(0.666);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(230);
		metap.setDiskRadiusM(0.999);
		metap.setCollisionRadiusM(0.37);
		metap.setPokedexWeightKg(155);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.333);
		metap.setMovementTimerS(4);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.74);
		metap.setUniqueId("V0059_POKEMON_ARCANINE");
		metap.setBaseDefense(180);
		metap.setAttackTimerS(11);
		metap.setWeightStdDev(19.375);
		metap.setCylHeightM(1.48);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.74);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.08);
		metap.setParentId(PokemonId.GROWLITHE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.FIRE_FANG_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.BULLDOZE,
				PokemonMove.FLAMETHROWER,
				PokemonMove.FIRE_BLAST
		});
		metap.setNumber(59);
		meta.put(PokemonId.ARCANINE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0060_POKEMON_POLIWAG");
		metap.setFamily(PokemonFamilyId.FAMILY_POLIWAG);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.6);
		metap.setHeightStdDev(0.075);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.5);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(108);
		metap.setDiskRadiusM(0.75);
		metap.setCollisionRadiusM(0.3125);
		metap.setPokedexWeightKg(12.4);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.3125);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.25);
		metap.setUniqueId("V0060_POKEMON_POLIWAG");
		metap.setBaseDefense(98);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(1.55);
		metap.setCylHeightM(0.875);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.75);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.BUBBLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.MUD_BOMB,
				PokemonMove.BUBBLE_BEAM,
				PokemonMove.BODY_SLAM
		});
		metap.setNumber(60);
		meta.put(PokemonId.POLIWAG, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0061_POKEMON_POLIWHIRL");
		metap.setFamily(PokemonFamilyId.FAMILY_POLIWAG);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.735);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(132);
		metap.setDiskRadiusM(1.1025);
		metap.setCollisionRadiusM(0.49);
		metap.setPokedexWeightKg(20);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.3675);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(0.8);
		metap.setModelScale(0.98);
		metap.setUniqueId("V0061_POKEMON_POLIWHIRL");
		metap.setBaseDefense(132);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(2.5);
		metap.setCylHeightM(1.078);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(0.882);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.2);
		metap.setParentId(PokemonId.POLIWAG);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.BUBBLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SCALD,
				PokemonMove.MUD_BOMB,
				PokemonMove.BUBBLE_BEAM
		});
		metap.setNumber(61);
		meta.put(PokemonId.POLIWHIRL, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0062_POKEMON_POLIWRATH");
		metap.setFamily(PokemonFamilyId.FAMILY_POLIWAG);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.FIGHTING);
		metap.setPokedexHeightM(1.3);
		metap.setHeightStdDev(0.1625);
		metap.setBaseStamina(180);
		metap.setCylRadiusM(0.817);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(180);
		metap.setDiskRadiusM(1.2255);
		metap.setCollisionRadiusM(0.645);
		metap.setPokedexWeightKg(54);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.344);
		metap.setMovementTimerS(11);
		metap.setJumpTimeS(1.05);
		metap.setModelScale(0.86);
		metap.setUniqueId("V0062_POKEMON_POLIWRATH");
		metap.setBaseDefense(202);
		metap.setAttackTimerS(4);
		metap.setWeightStdDev(6.75);
		metap.setCylHeightM(1.204);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.118);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.1);
		metap.setParentId(PokemonId.POLIWHIRL);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.BUBBLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.HYDRO_PUMP,
				PokemonMove.SUBMISSION,
				PokemonMove.ICE_PUNCH
		});
		metap.setNumber(62);
		meta.put(PokemonId.POLIWRATH, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0063_POKEMON_ABRA");
		metap.setFamily(PokemonFamilyId.FAMILY_ABRA);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.9);
		metap.setHeightStdDev(0.1125);
		metap.setBaseStamina(50);
		metap.setCylRadiusM(0.448);
		metap.setBaseFleeRate(0.99);
		metap.setBaseAttack(110);
		metap.setDiskRadiusM(0.672);
		metap.setCollisionRadiusM(0.28);
		metap.setPokedexWeightKg(19.5);
		metap.setMovementType(MovementType.PSYCHIC);
		metap.setType1(PokemonType.PSYCHIC);
		metap.setCollisionHeadRadiusM(0.28);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.12);
		metap.setUniqueId("V0063_POKEMON_ABRA");
		metap.setBaseDefense(76);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(2.4375);
		metap.setCylHeightM(0.784);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.56);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.168);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ZEN_HEADBUTT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SHADOW_BALL,
				PokemonMove.PSYSHOCK,
				PokemonMove.SIGNAL_BEAM
		});
		metap.setNumber(63);
		meta.put(PokemonId.ABRA, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0064_POKEMON_KADABRA");
		metap.setFamily(PokemonFamilyId.FAMILY_ABRA);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.3);
		metap.setHeightStdDev(0.1625);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.6675);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(150);
		metap.setDiskRadiusM(1.0013);
		metap.setCollisionRadiusM(0.445);
		metap.setPokedexWeightKg(56.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.PSYCHIC);
		metap.setCollisionHeadRadiusM(0.33375);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.89);
		metap.setUniqueId("V0064_POKEMON_KADABRA");
		metap.setBaseDefense(112);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(7.0625);
		metap.setCylHeightM(1.157);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(0.89);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.2);
		metap.setParentId(PokemonId.ABRA);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.PSYCHO_CUT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DAZZLING_GLEAM,
				PokemonMove.SHADOW_BALL,
				PokemonMove.PSYBEAM
		});
		metap.setNumber(64);
		meta.put(PokemonId.KADABRA, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0065_POKEMON_ALAKAZAM");
		metap.setFamily(PokemonFamilyId.FAMILY_ABRA);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.5);
		metap.setHeightStdDev(0.1875);
		metap.setBaseStamina(110);
		metap.setCylRadiusM(0.51);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(186);
		metap.setDiskRadiusM(0.765);
		metap.setCollisionRadiusM(0.425);
		metap.setPokedexWeightKg(48);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.PSYCHIC);
		metap.setCollisionHeadRadiusM(0.255);
		metap.setMovementTimerS(4);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.85);
		metap.setUniqueId("V0065_POKEMON_ALAKAZAM");
		metap.setBaseDefense(152);
		metap.setAttackTimerS(11);
		metap.setWeightStdDev(6);
		metap.setCylHeightM(1.275);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.93500012);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.1);
		metap.setParentId(PokemonId.KADABRA);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.PSYCHO_CUT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.DAZZLING_GLEAM,
				PokemonMove.SHADOW_BALL
		});
		metap.setNumber(65);
		meta.put(PokemonId.ALAKAZAM, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0066_POKEMON_MACHOP");
		metap.setFamily(PokemonFamilyId.FAMILY_MACHOP);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.8);
		metap.setHeightStdDev(0.1);
		metap.setBaseStamina(140);
		metap.setCylRadiusM(0.4125);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(118);
		metap.setDiskRadiusM(0.6188);
		metap.setCollisionRadiusM(0.22);
		metap.setPokedexWeightKg(19.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIGHTING);
		metap.setCollisionHeadRadiusM(0.20625);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.1);
		metap.setUniqueId("V0066_POKEMON_MACHOP");
		metap.setBaseDefense(96);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(2.4375);
		metap.setCylHeightM(0.88000011);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.55);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.KARATE_CHOP_FAST,
				PokemonMove.LOW_KICK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.LOW_SWEEP,
				PokemonMove.BRICK_BREAK,
				PokemonMove.CROSS_CHOP
		});
		metap.setNumber(66);
		meta.put(PokemonId.MACHOP, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0067_POKEMON_MACHOKE");
		metap.setFamily(PokemonFamilyId.FAMILY_MACHOP);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.5);
		metap.setHeightStdDev(0.1875);
		metap.setBaseStamina(160);
		metap.setCylRadiusM(0.546);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(154);
		metap.setDiskRadiusM(0.819);
		metap.setCollisionRadiusM(0.54600012);
		metap.setPokedexWeightKg(70.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIGHTING);
		metap.setCollisionHeadRadiusM(0.1365);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.91);
		metap.setUniqueId("V0067_POKEMON_MACHOKE");
		metap.setBaseDefense(144);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(8.8125);
		metap.setCylHeightM(1.274);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(1.092);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.2);
		metap.setParentId(PokemonId.MACHOP);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.KARATE_CHOP_FAST,
				PokemonMove.LOW_KICK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SUBMISSION,
				PokemonMove.BRICK_BREAK,
				PokemonMove.CROSS_CHOP
		});
		metap.setNumber(67);
		meta.put(PokemonId.MACHOKE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0068_POKEMON_MACHAMP");
		metap.setFamily(PokemonFamilyId.FAMILY_MACHOP);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.6);
		metap.setHeightStdDev(0.2);
		metap.setBaseStamina(180);
		metap.setCylRadiusM(0.5785);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(198);
		metap.setDiskRadiusM(0.8678);
		metap.setCollisionRadiusM(0.5785);
		metap.setPokedexWeightKg(130);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIGHTING);
		metap.setCollisionHeadRadiusM(0.1335);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.89);
		metap.setUniqueId("V0068_POKEMON_MACHAMP");
		metap.setBaseDefense(180);
		metap.setAttackTimerS(3);
		metap.setWeightStdDev(16.25);
		metap.setCylHeightM(1.424);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.246);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.1);
		metap.setParentId(PokemonId.MACHOKE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.KARATE_CHOP_FAST,
				PokemonMove.BULLET_PUNCH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STONE_EDGE,
				PokemonMove.SUBMISSION,
				PokemonMove.CROSS_CHOP
		});
		metap.setNumber(68);
		meta.put(PokemonId.MACHAMP, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0069_POKEMON_BELLSPROUT");
		metap.setFamily(PokemonFamilyId.FAMILY_BELLSPROUT);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(0.7);
		metap.setHeightStdDev(0.0875);
		metap.setBaseStamina(100);
		metap.setCylRadiusM(0.4515);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(158);
		metap.setDiskRadiusM(0.6773);
		metap.setCollisionRadiusM(0.1935);
		metap.setPokedexWeightKg(4);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.22575);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1.2);
		metap.setModelScale(1.29);
		metap.setUniqueId("V0069_POKEMON_BELLSPROUT");
		metap.setBaseDefense(78);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.5);
		metap.setCylHeightM(0.90299988);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.4515);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.VINE_WHIP_FAST,
				PokemonMove.ACID_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.POWER_WHIP,
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.WRAP
		});
		metap.setNumber(69);
		meta.put(PokemonId.BELLSPROUT, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0070_POKEMON_WEEPINBELL");
		metap.setFamily(PokemonFamilyId.FAMILY_BELLSPROUT);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.65);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(190);
		metap.setDiskRadiusM(0.975);
		metap.setCollisionRadiusM(0.25);
		metap.setPokedexWeightKg(6.4);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.25);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(1);
		metap.setUniqueId("V0070_POKEMON_WEEPINBELL");
		metap.setBaseDefense(110);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(0.8);
		metap.setCylHeightM(1);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(0.95);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.2);
		metap.setParentId(PokemonId.BELLSPROUT);
		metap.setCylGroundM(0.375);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ACID_FAST,
				PokemonMove.RAZOR_LEAF_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.POWER_WHIP,
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.SEED_BOMB
		});
		metap.setNumber(70);
		meta.put(PokemonId.WEEPINBELL, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0071_POKEMON_VICTREEBEL");
		metap.setFamily(PokemonFamilyId.FAMILY_BELLSPROUT);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1.7);
		metap.setHeightStdDev(0.2125);
		metap.setBaseStamina(160);
		metap.setCylRadiusM(0.546);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(222);
		metap.setDiskRadiusM(0.819);
		metap.setCollisionRadiusM(0.336);
		metap.setPokedexWeightKg(15.5);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.273);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.84);
		metap.setUniqueId("V0071_POKEMON_VICTREEBEL");
		metap.setBaseDefense(152);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(1.9375);
		metap.setCylHeightM(1.428);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.428);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.1);
		metap.setParentId(PokemonId.WEEPINBELL);
		metap.setCylGroundM(0.42);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ACID_FAST,
				PokemonMove.RAZOR_LEAF_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.LEAF_BLADE,
				PokemonMove.SOLAR_BEAM
		});
		metap.setNumber(71);
		meta.put(PokemonId.VICTREEBEL, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0072_POKEMON_TENTACOOL");
		metap.setFamily(PokemonFamilyId.FAMILY_TENTACOOL);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(0.9);
		metap.setHeightStdDev(0.1125);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.315);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(106);
		metap.setDiskRadiusM(0.4725);
		metap.setCollisionRadiusM(0.21);
		metap.setPokedexWeightKg(45.5);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.1575);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.05);
		metap.setUniqueId("V0072_POKEMON_TENTACOOL");
		metap.setBaseDefense(136);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(5.6875);
		metap.setCylHeightM(0.91874993);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.91874993);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.2625);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POISON_STING_FAST,
				PokemonMove.BUBBLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.WATER_PULSE,
				PokemonMove.BUBBLE_BEAM,
				PokemonMove.WRAP
		});
		metap.setNumber(72);
		meta.put(PokemonId.TENTACOOL, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0073_POKEMON_TENTACRUEL");
		metap.setFamily(PokemonFamilyId.FAMILY_TENTACOOL);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1.6);
		metap.setHeightStdDev(0.2);
		metap.setBaseStamina(160);
		metap.setCylRadiusM(0.492);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(170);
		metap.setDiskRadiusM(0.738);
		metap.setCollisionRadiusM(0.492);
		metap.setPokedexWeightKg(55);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.246);
		metap.setMovementTimerS(11);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.82);
		metap.setUniqueId("V0073_POKEMON_TENTACRUEL");
		metap.setBaseDefense(196);
		metap.setAttackTimerS(4);
		metap.setWeightStdDev(6.875);
		metap.setCylHeightM(1.312);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.23);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.TENTACOOL);
		metap.setCylGroundM(0.205);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ACID_FAST,
				PokemonMove.POISON_JAB_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.BLIZZARD,
				PokemonMove.HYDRO_PUMP,
				PokemonMove.SLUDGE_WAVE
		});
		metap.setNumber(73);
		meta.put(PokemonId.TENTACRUEL, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0074_POKEMON_GEODUDE");
		metap.setFamily(PokemonFamilyId.FAMILY_GEODUDE);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.GROUND);
		metap.setPokedexHeightM(0.4);
		metap.setHeightStdDev(0.05);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.3915);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(106);
		metap.setDiskRadiusM(0.5873);
		metap.setCollisionRadiusM(0.3915);
		metap.setPokedexWeightKg(20);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.ROCK);
		metap.setCollisionHeadRadiusM(0.19575);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.87);
		metap.setUniqueId("V0074_POKEMON_GEODUDE");
		metap.setBaseDefense(118);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(2.5);
		metap.setCylHeightM(0.348);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.1305);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.261);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ROCK_THROW_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DIG,
				PokemonMove.ROCK_SLIDE,
				PokemonMove.ROCK_TOMB
		});
		metap.setNumber(74);
		meta.put(PokemonId.GEODUDE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0075_POKEMON_GRAVELER");
		metap.setFamily(PokemonFamilyId.FAMILY_GEODUDE);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.GROUND);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(110);
		metap.setCylRadiusM(0.697);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(142);
		metap.setDiskRadiusM(1.0455);
		metap.setCollisionRadiusM(0.492);
		metap.setPokedexWeightKg(105);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ROCK);
		metap.setCollisionHeadRadiusM(0.369);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1.2);
		metap.setModelScale(0.82);
		metap.setUniqueId("V0075_POKEMON_GRAVELER");
		metap.setBaseDefense(156);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(13.125);
		metap.setCylHeightM(0.82);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(0.697);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.2);
		metap.setParentId(PokemonId.GEODUDE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.ROCK_THROW_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DIG,
				PokemonMove.ROCK_SLIDE,
				PokemonMove.STONE_EDGE
		});
		metap.setNumber(75);
		meta.put(PokemonId.GRAVELER, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0076_POKEMON_GOLEM");
		metap.setFamily(PokemonFamilyId.FAMILY_GEODUDE);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.GROUND);
		metap.setPokedexHeightM(1.4);
		metap.setHeightStdDev(0.175);
		metap.setBaseStamina(160);
		metap.setCylRadiusM(0.63);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(176);
		metap.setDiskRadiusM(0.945);
		metap.setCollisionRadiusM(0.63);
		metap.setPokedexWeightKg(300);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ROCK);
		metap.setCollisionHeadRadiusM(0.315);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1.2);
		metap.setModelScale(0.84);
		metap.setUniqueId("V0076_POKEMON_GOLEM");
		metap.setBaseDefense(198);
		metap.setAttackTimerS(3);
		metap.setWeightStdDev(37.5);
		metap.setCylHeightM(1.092);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.092);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.1);
		metap.setParentId(PokemonId.GRAVELER);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.ROCK_THROW_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STONE_EDGE,
				PokemonMove.EARTHQUAKE,
				PokemonMove.ANCIENT_POWER
		});
		metap.setNumber(76);
		meta.put(PokemonId.GOLEM, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0077_POKEMON_PONYTA");
		metap.setFamily(PokemonFamilyId.FAMILY_PONYTA);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(100);
		metap.setCylRadiusM(0.3788);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(168);
		metap.setDiskRadiusM(0.5681);
		metap.setCollisionRadiusM(0.2525);
		metap.setPokedexWeightKg(30);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.202);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(0.95);
		metap.setModelScale(1.01);
		metap.setUniqueId("V0077_POKEMON_PONYTA");
		metap.setBaseDefense(138);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(3.75);
		metap.setCylHeightM(1.2625);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.63125);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.32);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.EMBER_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.FLAME_WHEEL,
				PokemonMove.FLAME_CHARGE,
				PokemonMove.FIRE_BLAST
		});
		metap.setNumber(77);
		meta.put(PokemonId.PONYTA, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0078_POKEMON_RAPIDASH");
		metap.setFamily(PokemonFamilyId.FAMILY_PONYTA);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.7);
		metap.setHeightStdDev(0.2125);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.405);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(200);
		metap.setDiskRadiusM(0.6075);
		metap.setCollisionRadiusM(0.324);
		metap.setPokedexWeightKg(95);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.243);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.81);
		metap.setUniqueId("V0078_POKEMON_RAPIDASH");
		metap.setBaseDefense(170);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(11.875);
		metap.setCylHeightM(1.701);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.891);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.12);
		metap.setParentId(PokemonId.PONYTA);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.LOW_KICK_FAST,
				PokemonMove.EMBER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.HEAT_WAVE,
				PokemonMove.DRILL_RUN,
				PokemonMove.FIRE_BLAST
		});
		metap.setNumber(78);
		meta.put(PokemonId.RAPIDASH, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0079_POKEMON_SLOWPOKE");
		metap.setFamily(PokemonFamilyId.FAMILY_SLOWPOKE);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.PSYCHIC);
		metap.setPokedexHeightM(1.2);
		metap.setHeightStdDev(0.15);
		metap.setBaseStamina(180);
		metap.setCylRadiusM(0.5925);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(110);
		metap.setDiskRadiusM(1.185);
		metap.setCollisionRadiusM(0.316);
		metap.setPokedexWeightKg(36);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.29625);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.79);
		metap.setUniqueId("V0079_POKEMON_SLOWPOKE");
		metap.setBaseDefense(110);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(4.5);
		metap.setCylHeightM(0.94800007);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.5135);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.WATER_GUN_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.WATER_PULSE,
				PokemonMove.PSYSHOCK
		});
		metap.setNumber(79);
		meta.put(PokemonId.SLOWPOKE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0080_POKEMON_SLOWBRO");
		metap.setFamily(PokemonFamilyId.FAMILY_SLOWPOKE);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.PSYCHIC);
		metap.setPokedexHeightM(1.6);
		metap.setHeightStdDev(0.2);
		metap.setBaseStamina(190);
		metap.setCylRadiusM(0.4675);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(184);
		metap.setDiskRadiusM(0.7013);
		metap.setCollisionRadiusM(0.425);
		metap.setPokedexWeightKg(78.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.255);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.85);
		metap.setUniqueId("V0080_POKEMON_SLOWBRO");
		metap.setBaseDefense(198);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(9.8125);
		metap.setCylHeightM(1.275);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.85);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.SLOWPOKE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.WATER_GUN_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.WATER_PULSE,
				PokemonMove.ICE_BEAM
		});
		metap.setNumber(80);
		meta.put(PokemonId.SLOWBRO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0081_POKEMON_MAGNEMITE");
		metap.setFamily(PokemonFamilyId.FAMILY_MAGNEMITE);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.STEEL);
		metap.setPokedexHeightM(0.3);
		metap.setHeightStdDev(0.0375);
		metap.setBaseStamina(50);
		metap.setCylRadiusM(0.456);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(128);
		metap.setDiskRadiusM(0.684);
		metap.setCollisionRadiusM(0.456);
		metap.setPokedexWeightKg(6);
		metap.setMovementType(MovementType.ELECTRIC);
		metap.setType1(PokemonType.ELECTRIC);
		metap.setCollisionHeadRadiusM(0.228);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.52);
		metap.setUniqueId("V0081_POKEMON_MAGNEMITE");
		metap.setBaseDefense(138);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(0.75);
		metap.setCylHeightM(0.456);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.456);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.912);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SPARK_FAST,
				PokemonMove.THUNDER_SHOCK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.MAGNET_BOMB,
				PokemonMove.THUNDERBOLT,
				PokemonMove.DISCHARGE
		});
		metap.setNumber(81);
		meta.put(PokemonId.MAGNEMITE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0082_POKEMON_MAGNETON");
		metap.setFamily(PokemonFamilyId.FAMILY_MAGNEMITE);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.STEEL);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(100);
		metap.setCylRadiusM(0.44);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(186);
		metap.setDiskRadiusM(0.66);
		metap.setCollisionRadiusM(0.44);
		metap.setPokedexWeightKg(60);
		metap.setMovementType(MovementType.ELECTRIC);
		metap.setType1(PokemonType.ELECTRIC);
		metap.setCollisionHeadRadiusM(0.22);
		metap.setMovementTimerS(5);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.1);
		metap.setUniqueId("V0082_POKEMON_MAGNETON");
		metap.setBaseDefense(180);
		metap.setAttackTimerS(14);
		metap.setWeightStdDev(7.5);
		metap.setCylHeightM(1.1);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.825);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.MAGNEMITE);
		metap.setCylGroundM(0.44);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SPARK_FAST,
				PokemonMove.THUNDER_SHOCK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.MAGNET_BOMB,
				PokemonMove.FLASH_CANNON,
				PokemonMove.DISCHARGE
		});
		metap.setNumber(82);
		meta.put(PokemonId.MAGNETON, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0083_POKEMON_FARFETCHD");
		metap.setFamily(PokemonFamilyId.FAMILY_FARFETCHD);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(0.8);
		metap.setHeightStdDev(0.1);
		metap.setBaseStamina(104);
		metap.setCylRadiusM(0.452);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(138);
		metap.setDiskRadiusM(0.678);
		metap.setCollisionRadiusM(0.2825);
		metap.setPokedexWeightKg(15);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.2825);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1.13);
		metap.setUniqueId("V0083_POKEMON_FARFETCHD");
		metap.setBaseDefense(132);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(1.875);
		metap.setCylHeightM(0.8475);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.42375);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.FURY_CUTTER_FAST,
				PokemonMove.CUT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.AERIAL_ACE,
				PokemonMove.LEAF_BLADE,
				PokemonMove.AIR_CUTTER
		});
		metap.setNumber(83);
		meta.put(PokemonId.FARFETCHD, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0084_POKEMON_DODUO");
		metap.setFamily(PokemonFamilyId.FAMILY_DODUO);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.4);
		metap.setHeightStdDev(0.175);
		metap.setBaseStamina(70);
		metap.setCylRadiusM(0.396);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(126);
		metap.setDiskRadiusM(0.594);
		metap.setCollisionRadiusM(0.352);
		metap.setPokedexWeightKg(39.2);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.198);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.88);
		metap.setUniqueId("V0084_POKEMON_DODUO");
		metap.setBaseDefense(96);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(4.9);
		metap.setCylHeightM(1.232);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(1.232);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.QUICK_ATTACK_FAST,
				PokemonMove.PECK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.AERIAL_ACE,
				PokemonMove.DRILL_PECK,
				PokemonMove.SWIFT
		});
		metap.setNumber(84);
		meta.put(PokemonId.DODUO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0085_POKEMON_DODRIO");
		metap.setFamily(PokemonFamilyId.FAMILY_DODUO);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.8);
		metap.setHeightStdDev(0.225);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.5148);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(182);
		metap.setDiskRadiusM(0.7722);
		metap.setCollisionRadiusM(0.39);
		metap.setPokedexWeightKg(85.2);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.2574);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.78);
		metap.setUniqueId("V0085_POKEMON_DODRIO");
		metap.setBaseDefense(150);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(10.65);
		metap.setCylHeightM(1.287);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.287);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.DODUO);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.STEEL_WING_FAST,
				PokemonMove.FEINT_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.AERIAL_ACE,
				PokemonMove.DRILL_PECK,
				PokemonMove.AIR_CUTTER
		});
		metap.setNumber(85);
		meta.put(PokemonId.DODRIO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0086_POKEMON_SEEL");
		metap.setFamily(PokemonFamilyId.FAMILY_SEEL);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.1);
		metap.setHeightStdDev(0.1375);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.275);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(104);
		metap.setDiskRadiusM(0.4125);
		metap.setCollisionRadiusM(0.275);
		metap.setPokedexWeightKg(90);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.22);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(0.9);
		metap.setModelScale(1.1);
		metap.setUniqueId("V0086_POKEMON_SEEL");
		metap.setBaseDefense(138);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(11.25);
		metap.setCylHeightM(0.55);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.4125);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.WATER_GUN_FAST,
				PokemonMove.ICE_SHARD_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.ICY_WIND,
				PokemonMove.AQUA_TAIL,
				PokemonMove.AQUA_JET
		});
		metap.setNumber(86);
		meta.put(PokemonId.SEEL, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0087_POKEMON_DEWGONG");
		metap.setFamily(PokemonFamilyId.FAMILY_SEEL);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.ICE);
		metap.setPokedexHeightM(1.7);
		metap.setHeightStdDev(0.2125);
		metap.setBaseStamina(180);
		metap.setCylRadiusM(0.525);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(156);
		metap.setDiskRadiusM(0.7875);
		metap.setCollisionRadiusM(0.315);
		metap.setPokedexWeightKg(120);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.13125);
		metap.setMovementTimerS(5);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.05);
		metap.setUniqueId("V0087_POKEMON_DEWGONG");
		metap.setBaseDefense(192);
		metap.setAttackTimerS(14);
		metap.setWeightStdDev(15);
		metap.setCylHeightM(0.84);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.63);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.SEEL);
		metap.setCylGroundM(0.39375);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ICE_SHARD_FAST,
				PokemonMove.FROST_BREATH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.ICY_WIND,
				PokemonMove.BLIZZARD,
				PokemonMove.AQUA_JET
		});
		metap.setNumber(87);
		meta.put(PokemonId.DEWGONG, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0088_POKEMON_GRIMER");
		metap.setFamily(PokemonFamilyId.FAMILY_GRIMER);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.9);
		metap.setHeightStdDev(0.1125);
		metap.setBaseStamina(160);
		metap.setCylRadiusM(0.588);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(124);
		metap.setDiskRadiusM(0.882);
		metap.setCollisionRadiusM(0.49);
		metap.setPokedexWeightKg(30);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.294);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.98);
		metap.setUniqueId("V0088_POKEMON_GRIMER");
		metap.setBaseDefense(110);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(3.75);
		metap.setCylHeightM(0.98);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.83300012);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SLAP_FAST,
				PokemonMove.ACID_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.MUD_BOMB,
				PokemonMove.SLUDGE
		});
		metap.setNumber(88);
		meta.put(PokemonId.GRIMER, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0089_POKEMON_MUK");
		metap.setFamily(PokemonFamilyId.FAMILY_GRIMER);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.2);
		metap.setHeightStdDev(0.15);
		metap.setBaseStamina(210);
		metap.setCylRadiusM(0.86);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(180);
		metap.setDiskRadiusM(1.14);
		metap.setCollisionRadiusM(0.76);
		metap.setPokedexWeightKg(30);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.38);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.76);
		metap.setUniqueId("V0089_POKEMON_MUK");
		metap.setBaseDefense(188);
		metap.setAttackTimerS(3);
		metap.setWeightStdDev(3.75);
		metap.setCylHeightM(0.912);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.57);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.GRIMER);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ACID_FAST,
				PokemonMove.POISON_JAB_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DARK_PULSE,
				PokemonMove.GUNK_SHOT,
				PokemonMove.SLUDGE_WAVE
		});
		metap.setNumber(89);
		meta.put(PokemonId.MUK, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0090_POKEMON_SHELLDER");
		metap.setFamily(PokemonFamilyId.FAMILY_SHELLDER);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.3);
		metap.setHeightStdDev(0.0375);
		metap.setBaseStamina(60);
		metap.setCylRadiusM(0.3864);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(120);
		metap.setDiskRadiusM(0.5796);
		metap.setCollisionRadiusM(0.336);
		metap.setPokedexWeightKg(4);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.294);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1.2);
		metap.setModelScale(1.68);
		metap.setUniqueId("V0090_POKEMON_SHELLDER");
		metap.setBaseDefense(112);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(0.5);
		metap.setCylHeightM(0.504);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.504);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ICE_SHARD_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.ICY_WIND,
				PokemonMove.WATER_PULSE,
				PokemonMove.BUBBLE_BEAM
		});
		metap.setNumber(90);
		meta.put(PokemonId.SHELLDER, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0091_POKEMON_CLOYSTER");
		metap.setFamily(PokemonFamilyId.FAMILY_SHELLDER);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.ICE);
		metap.setPokedexHeightM(1.5);
		metap.setHeightStdDev(0.1875);
		metap.setBaseStamina(100);
		metap.setCylRadiusM(0.63);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(196);
		metap.setDiskRadiusM(0.945);
		metap.setCollisionRadiusM(0.42);
		metap.setPokedexWeightKg(132.5);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.54599988);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.84);
		metap.setUniqueId("V0091_POKEMON_CLOYSTER");
		metap.setBaseDefense(196);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(16.5625);
		metap.setCylHeightM(1.05);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.05);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.SHELLDER);
		metap.setCylGroundM(0.42);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ICE_SHARD_FAST,
				PokemonMove.FROST_BREATH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.ICY_WIND,
				PokemonMove.BLIZZARD,
				PokemonMove.HYDRO_PUMP
		});
		metap.setNumber(91);
		meta.put(PokemonId.CLOYSTER, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0092_POKEMON_GASTLY");
		metap.setFamily(PokemonFamilyId.FAMILY_GASTLY);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1.3);
		metap.setHeightStdDev(0.1625);
		metap.setBaseStamina(60);
		metap.setCylRadiusM(0.45);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(136);
		metap.setDiskRadiusM(0.675);
		metap.setCollisionRadiusM(0.25);
		metap.setPokedexWeightKg(0.1);
		metap.setMovementType(MovementType.PSYCHIC);
		metap.setType1(PokemonType.GHOST);
		metap.setCollisionHeadRadiusM(0.3);
		metap.setMovementTimerS(29);
		metap.setJumpTimeS(1);
		metap.setModelScale(1);
		metap.setUniqueId("V0092_POKEMON_GASTLY");
		metap.setBaseDefense(82);
		metap.setAttackTimerS(10);
		metap.setWeightStdDev(0.0125);
		metap.setCylHeightM(0.8);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.6);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.32);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.6);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SUCKER_PUNCH_FAST,
				PokemonMove.LICK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.DARK_PULSE,
				PokemonMove.OMINOUS_WIND
		});
		metap.setNumber(92);
		meta.put(PokemonId.GASTLY, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0093_POKEMON_HAUNTER");
		metap.setFamily(PokemonFamilyId.FAMILY_GASTLY);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1.6);
		metap.setHeightStdDev(0.2);
		metap.setBaseStamina(90);
		metap.setCylRadiusM(0.51);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(172);
		metap.setDiskRadiusM(0.765);
		metap.setCollisionRadiusM(0.442);
		metap.setPokedexWeightKg(0.1);
		metap.setMovementType(MovementType.PSYCHIC);
		metap.setType1(PokemonType.GHOST);
		metap.setCollisionHeadRadiusM(0.442);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.68);
		metap.setUniqueId("V0093_POKEMON_HAUNTER");
		metap.setBaseDefense(118);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(0.0125);
		metap.setCylHeightM(1.088);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(1.156);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.GASTLY);
		metap.setCylGroundM(0.34);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SHADOW_CLAW_FAST,
				PokemonMove.LICK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.SHADOW_BALL,
				PokemonMove.DARK_PULSE
		});
		metap.setNumber(93);
		meta.put(PokemonId.HAUNTER, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0094_POKEMON_GENGAR");
		metap.setFamily(PokemonFamilyId.FAMILY_GASTLY);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.POISON);
		metap.setPokedexHeightM(1.5);
		metap.setHeightStdDev(0.1875);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.462);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(204);
		metap.setDiskRadiusM(0.693);
		metap.setCollisionRadiusM(0.462);
		metap.setPokedexWeightKg(40.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GHOST);
		metap.setCollisionHeadRadiusM(0.504);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1.3);
		metap.setModelScale(0.84);
		metap.setUniqueId("V0094_POKEMON_GENGAR");
		metap.setBaseDefense(156);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(5.0625);
		metap.setCylHeightM(1.176);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.092);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.08);
		metap.setParentId(PokemonId.HAUNTER);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SUCKER_PUNCH_FAST,
				PokemonMove.SHADOW_CLAW_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SHADOW_BALL,
				PokemonMove.DARK_PULSE,
				PokemonMove.SLUDGE_WAVE
		});
		metap.setNumber(94);
		meta.put(PokemonId.GENGAR, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0095_POKEMON_ONIX");
		metap.setFamily(PokemonFamilyId.FAMILY_ONIX);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.GROUND);
		metap.setPokedexHeightM(8.8);
		metap.setHeightStdDev(1.1);
		metap.setBaseStamina(70);
		metap.setCylRadiusM(0.658);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(90);
		metap.setDiskRadiusM(0.987);
		metap.setCollisionRadiusM(0.658);
		metap.setPokedexWeightKg(210);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ROCK);
		metap.setCollisionHeadRadiusM(0.376);
		metap.setMovementTimerS(17);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.47);
		metap.setUniqueId("V0095_POKEMON_ONIX");
		metap.setBaseDefense(186);
		metap.setAttackTimerS(6);
		metap.setWeightStdDev(26.25);
		metap.setCylHeightM(1.41);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.175);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ROCK_THROW_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.IRON_HEAD,
				PokemonMove.STONE_EDGE,
				PokemonMove.ROCK_SLIDE
		});
		metap.setNumber(95);
		meta.put(PokemonId.ONIX, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0096_POKEMON_DROWZEE");
		metap.setFamily(PokemonFamilyId.FAMILY_DROWZEE);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.42);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(104);
		metap.setDiskRadiusM(0.63);
		metap.setCollisionRadiusM(0.3675);
		metap.setPokedexWeightKg(32.4);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.PSYCHIC);
		metap.setCollisionHeadRadiusM(0.2625);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1.05);
		metap.setUniqueId("V0096_POKEMON_DROWZEE");
		metap.setBaseDefense(140);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(4.05);
		metap.setCylHeightM(1.05);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.63);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.POUND_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.PSYSHOCK,
				PokemonMove.PSYBEAM
		});
		metap.setNumber(96);
		meta.put(PokemonId.DROWZEE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0097_POKEMON_HYPNO");
		metap.setFamily(PokemonFamilyId.FAMILY_DROWZEE);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.6);
		metap.setHeightStdDev(0.2);
		metap.setBaseStamina(170);
		metap.setCylRadiusM(0.6225);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(162);
		metap.setDiskRadiusM(0.9338);
		metap.setCollisionRadiusM(0.332);
		metap.setPokedexWeightKg(75.6);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.PSYCHIC);
		metap.setCollisionHeadRadiusM(0.332);
		metap.setMovementTimerS(11);
		metap.setJumpTimeS(0.8);
		metap.setModelScale(0.83);
		metap.setUniqueId("V0097_POKEMON_HYPNO");
		metap.setBaseDefense(196);
		metap.setAttackTimerS(4);
		metap.setWeightStdDev(9.45);
		metap.setCylHeightM(1.328);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.83);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.DROWZEE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.ZEN_HEADBUTT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.SHADOW_BALL,
				PokemonMove.PSYSHOCK
		});
		metap.setNumber(97);
		meta.put(PokemonId.HYPNO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0098_POKEMON_KRABBY");
		metap.setFamily(PokemonFamilyId.FAMILY_KRABBY);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.4);
		metap.setHeightStdDev(0.05);
		metap.setBaseStamina(60);
		metap.setCylRadiusM(0.522);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(116);
		metap.setDiskRadiusM(0.783);
		metap.setCollisionRadiusM(0.522);
		metap.setPokedexWeightKg(6.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.261);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.16);
		metap.setUniqueId("V0098_POKEMON_KRABBY");
		metap.setBaseDefense(110);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(0.8125);
		metap.setCylHeightM(0.87);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.87);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.BUBBLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.WATER_PULSE,
				PokemonMove.VICE_GRIP,
				PokemonMove.BUBBLE_BEAM
		});
		metap.setNumber(98);
		meta.put(PokemonId.KRABBY, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0099_POKEMON_KINGLER");
		metap.setFamily(PokemonFamilyId.FAMILY_KRABBY);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.3);
		metap.setHeightStdDev(0.1625);
		metap.setBaseStamina(110);
		metap.setCylRadiusM(0.6525);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(178);
		metap.setDiskRadiusM(0.9788);
		metap.setCollisionRadiusM(0.6525);
		metap.setPokedexWeightKg(60);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.32625);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(0.8);
		metap.setModelScale(0.87);
		metap.setUniqueId("V0099_POKEMON_KINGLER");
		metap.setBaseDefense(168);
		metap.setAttackTimerS(3);
		metap.setWeightStdDev(7.5);
		metap.setCylHeightM(1.0005);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.0005);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.KRABBY);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.METAL_CLAW_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.WATER_PULSE,
				PokemonMove.X_SCISSOR,
				PokemonMove.VICE_GRIP
		});
		metap.setNumber(99);
		meta.put(PokemonId.KINGLER, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0100_POKEMON_VOLTORB");
		metap.setFamily(PokemonFamilyId.FAMILY_VOLTORB);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.5);
		metap.setHeightStdDev(0.0625);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.3375);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(102);
		metap.setDiskRadiusM(0.5063);
		metap.setCollisionRadiusM(0.3375);
		metap.setPokedexWeightKg(10.4);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ELECTRIC);
		metap.setCollisionHeadRadiusM(0.16875);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1.2);
		metap.setModelScale(1.35);
		metap.setUniqueId("V0100_POKEMON_VOLTORB");
		metap.setBaseDefense(124);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(1.3);
		metap.setCylHeightM(0.675);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.675);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SPARK_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SIGNAL_BEAM,
				PokemonMove.THUNDERBOLT,
				PokemonMove.DISCHARGE
		});
		metap.setNumber(100);
		meta.put(PokemonId.VOLTORB, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0101_POKEMON_ELECTRODE");
		metap.setFamily(PokemonFamilyId.FAMILY_VOLTORB);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.2);
		metap.setHeightStdDev(0.15);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.552);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(150);
		metap.setDiskRadiusM(0.828);
		metap.setCollisionRadiusM(0.552);
		metap.setPokedexWeightKg(66.6);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ELECTRIC);
		metap.setCollisionHeadRadiusM(0.276);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1.2);
		metap.setModelScale(0.92);
		metap.setUniqueId("V0101_POKEMON_ELECTRODE");
		metap.setBaseDefense(174);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(8.325);
		metap.setCylHeightM(1.104);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.104);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.VOLTORB);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SPARK_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.HYPER_BEAM,
				PokemonMove.THUNDERBOLT,
				PokemonMove.DISCHARGE
		});
		metap.setNumber(101);
		meta.put(PokemonId.ELECTRODE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0102_POKEMON_EXEGGCUTE");
		metap.setFamily(PokemonFamilyId.FAMILY_EXEGGCUTE);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.PSYCHIC);
		metap.setPokedexHeightM(0.4);
		metap.setHeightStdDev(0.05);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.515);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(110);
		metap.setDiskRadiusM(0.7725);
		metap.setCollisionRadiusM(0.515);
		metap.setPokedexWeightKg(2.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.2575);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.03);
		metap.setUniqueId("V0102_POKEMON_EXEGGCUTE");
		metap.setBaseDefense(132);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(0.3125);
		metap.setCylHeightM(0.412);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.412);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.SEED_BOMB,
				PokemonMove.ANCIENT_POWER
		});
		metap.setNumber(102);
		meta.put(PokemonId.EXEGGCUTE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0103_POKEMON_EXEGGUTOR");
		metap.setFamily(PokemonFamilyId.FAMILY_EXEGGCUTE);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.PSYCHIC);
		metap.setPokedexHeightM(2);
		metap.setHeightStdDev(0.25);
		metap.setBaseStamina(190);
		metap.setCylRadiusM(0.507);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(232);
		metap.setDiskRadiusM(0.7605);
		metap.setCollisionRadiusM(0.507);
		metap.setPokedexWeightKg(120);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.2535);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.78);
		metap.setUniqueId("V0103_POKEMON_EXEGGUTOR");
		metap.setBaseDefense(164);
		metap.setAttackTimerS(3);
		metap.setWeightStdDev(15);
		metap.setCylHeightM(1.365);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.365);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.EXEGGCUTE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.ZEN_HEADBUTT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.SEED_BOMB,
				PokemonMove.SOLAR_BEAM
		});
		metap.setNumber(103);
		meta.put(PokemonId.EXEGGUTOR, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0104_POKEMON_CUBONE");
		metap.setFamily(PokemonFamilyId.FAMILY_CUBONE);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.4);
		metap.setHeightStdDev(0.05);
		metap.setBaseStamina(100);
		metap.setCylRadiusM(0.296);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(102);
		metap.setDiskRadiusM(0.444);
		metap.setCollisionRadiusM(0.222);
		metap.setPokedexWeightKg(6.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GROUND);
		metap.setCollisionHeadRadiusM(0.222);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.48);
		metap.setUniqueId("V0104_POKEMON_CUBONE");
		metap.setBaseDefense(150);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(0.8125);
		metap.setCylHeightM(0.592);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.37);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.32);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SLAP_FAST,
				PokemonMove.ROCK_SMASH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DIG,
				PokemonMove.BONE_CLUB,
				PokemonMove.BULLDOZE
		});
		metap.setNumber(104);
		meta.put(PokemonId.CUBONE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0105_POKEMON_MAROWAK");
		metap.setFamily(PokemonFamilyId.FAMILY_CUBONE);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.35);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(140);
		metap.setDiskRadiusM(0.525);
		metap.setCollisionRadiusM(0.25);
		metap.setPokedexWeightKg(45);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GROUND);
		metap.setCollisionHeadRadiusM(0.25);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(0.85);
		metap.setModelScale(1);
		metap.setUniqueId("V0105_POKEMON_MAROWAK");
		metap.setBaseDefense(202);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(5.625);
		metap.setCylHeightM(1);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.75);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.12);
		metap.setParentId(PokemonId.CUBONE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SLAP_FAST,
				PokemonMove.ROCK_SMASH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DIG,
				PokemonMove.EARTHQUAKE,
				PokemonMove.BONE_CLUB
		});
		metap.setNumber(105);
		meta.put(PokemonId.MAROWAK, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0106_POKEMON_HITMONLEE");
		metap.setFamily(PokemonFamilyId.FAMILY_HITMONLEE);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.5);
		metap.setHeightStdDev(0.1875);
		metap.setBaseStamina(100);
		metap.setCylRadiusM(0.415);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(148);
		metap.setDiskRadiusM(0.6225);
		metap.setCollisionRadiusM(0.415);
		metap.setPokedexWeightKg(49.8);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIGHTING);
		metap.setCollisionHeadRadiusM(0.2075);
		metap.setMovementTimerS(11);
		metap.setJumpTimeS(0.8);
		metap.setModelScale(0.83);
		metap.setUniqueId("V0106_POKEMON_HITMONLEE");
		metap.setBaseDefense(172);
		metap.setAttackTimerS(4);
		metap.setWeightStdDev(6.225);
		metap.setCylHeightM(1.245);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.245);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.LOW_KICK_FAST,
				PokemonMove.ROCK_SMASH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STOMP,
				PokemonMove.STONE_EDGE,
				PokemonMove.LOW_SWEEP
		});
		metap.setNumber(106);
		meta.put(PokemonId.HITMONLEE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0107_POKEMON_HITMONCHAN");
		metap.setFamily(PokemonFamilyId.FAMILY_HITMONCHAN);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.4);
		metap.setHeightStdDev(0.175);
		metap.setBaseStamina(100);
		metap.setCylRadiusM(0.459);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(138);
		metap.setDiskRadiusM(0.6885);
		metap.setCollisionRadiusM(0.3315);
		metap.setPokedexWeightKg(50.2);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIGHTING);
		metap.setCollisionHeadRadiusM(0.255);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1.1);
		metap.setModelScale(1.02);
		metap.setUniqueId("V0107_POKEMON_HITMONCHAN");
		metap.setBaseDefense(204);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(6.275);
		metap.setCylHeightM(1.428);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.02);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BULLET_PUNCH_FAST,
				PokemonMove.ROCK_SMASH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.THUNDER_PUNCH,
				PokemonMove.FIRE_PUNCH,
				PokemonMove.BRICK_BREAK,
				PokemonMove.ICE_PUNCH
		});
		metap.setNumber(107);
		meta.put(PokemonId.HITMONCHAN, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0108_POKEMON_LICKITUNG");
		metap.setFamily(PokemonFamilyId.FAMILY_LICKITUNG);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.2);
		metap.setHeightStdDev(0.15);
		metap.setBaseStamina(180);
		metap.setCylRadiusM(0.46);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(126);
		metap.setDiskRadiusM(0.69);
		metap.setCollisionRadiusM(0.46);
		metap.setPokedexWeightKg(65.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.253);
		metap.setMovementTimerS(23);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.92);
		metap.setUniqueId("V0108_POKEMON_LICKITUNG");
		metap.setBaseDefense(160);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(8.1875);
		metap.setCylHeightM(1.104);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.92);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ZEN_HEADBUTT_FAST,
				PokemonMove.LICK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STOMP,
				PokemonMove.POWER_WHIP,
				PokemonMove.HYPER_BEAM
		});
		metap.setNumber(108);
		meta.put(PokemonId.LICKITUNG, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0109_POKEMON_KOFFING");
		metap.setFamily(PokemonFamilyId.FAMILY_KOFFING);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.6);
		metap.setHeightStdDev(0.075);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.48);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(136);
		metap.setDiskRadiusM(0.72);
		metap.setCollisionRadiusM(0.36);
		metap.setPokedexWeightKg(1);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.6);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.2);
		metap.setUniqueId("V0109_POKEMON_KOFFING");
		metap.setBaseDefense(142);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(0.125);
		metap.setCylHeightM(0.72);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.66);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.6);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ACID_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.DARK_PULSE,
				PokemonMove.SLUDGE
		});
		metap.setNumber(109);
		meta.put(PokemonId.KOFFING, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0110_POKEMON_WEEZING");
		metap.setFamily(PokemonFamilyId.FAMILY_KOFFING);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.2);
		metap.setHeightStdDev(0.15);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.62);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(190);
		metap.setDiskRadiusM(0.93);
		metap.setCollisionRadiusM(0.682);
		metap.setPokedexWeightKg(9.5);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.POISON);
		metap.setCollisionHeadRadiusM(0.465);
		metap.setMovementTimerS(4);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.24);
		metap.setUniqueId("V0110_POKEMON_WEEZING");
		metap.setBaseDefense(198);
		metap.setAttackTimerS(11);
		metap.setWeightStdDev(1.1875);
		metap.setCylHeightM(0.744);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.744);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.KOFFING);
		metap.setCylGroundM(0.62);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ACID_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.SHADOW_BALL,
				PokemonMove.DARK_PULSE
		});
		metap.setNumber(110);
		meta.put(PokemonId.WEEZING, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0111_POKEMON_RHYHORN");
		metap.setFamily(PokemonFamilyId.FAMILY_RHYHORN);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.ROCK);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(160);
		metap.setCylRadiusM(0.5);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(110);
		metap.setDiskRadiusM(0.75);
		metap.setCollisionRadiusM(0.5);
		metap.setPokedexWeightKg(115);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GROUND);
		metap.setCollisionHeadRadiusM(0.3);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1);
		metap.setUniqueId("V0111_POKEMON_RHYHORN");
		metap.setBaseDefense(116);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(14.375);
		metap.setCylHeightM(0.85);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.85);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SLAP_FAST,
				PokemonMove.ROCK_SMASH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STOMP,
				PokemonMove.BULLDOZE,
				PokemonMove.HORN_ATTACK
		});
		metap.setNumber(111);
		meta.put(PokemonId.RHYHORN, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0112_POKEMON_RHYDON");
		metap.setFamily(PokemonFamilyId.FAMILY_RHYHORN);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.ROCK);
		metap.setPokedexHeightM(1.9);
		metap.setHeightStdDev(0.2375);
		metap.setBaseStamina(210);
		metap.setCylRadiusM(0.79);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(166);
		metap.setDiskRadiusM(1.185);
		metap.setCollisionRadiusM(0.5925);
		metap.setPokedexWeightKg(120);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GROUND);
		metap.setCollisionHeadRadiusM(0.395);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.79);
		metap.setUniqueId("V0112_POKEMON_RHYDON");
		metap.setBaseDefense(160);
		metap.setAttackTimerS(3);
		metap.setWeightStdDev(15);
		metap.setCylHeightM(1.343);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.185);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.RHYHORN);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SLAP_FAST,
				PokemonMove.ROCK_SMASH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STONE_EDGE,
				PokemonMove.EARTHQUAKE,
				PokemonMove.MEGAHORN
		});
		metap.setNumber(112);
		meta.put(PokemonId.RHYDON, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0113_POKEMON_CHANSEY");
		metap.setFamily(PokemonFamilyId.FAMILY_CHANSEY);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.1);
		metap.setHeightStdDev(0.1375);
		metap.setBaseStamina(500);
		metap.setCylRadiusM(0.48);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(40);
		metap.setDiskRadiusM(0.72);
		metap.setCollisionRadiusM(0.48);
		metap.setPokedexWeightKg(34.6);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.24);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.96);
		metap.setUniqueId("V0113_POKEMON_CHANSEY");
		metap.setBaseDefense(60);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(4.325);
		metap.setCylHeightM(1.056);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.056);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POUND_FAST,
				PokemonMove.ZEN_HEADBUTT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.DAZZLING_GLEAM,
				PokemonMove.PSYBEAM
		});
		metap.setNumber(113);
		meta.put(PokemonId.CHANSEY, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0114_POKEMON_TANGELA");
		metap.setFamily(PokemonFamilyId.FAMILY_TANGELA);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.73);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(164);
		metap.setDiskRadiusM(1.095);
		metap.setCollisionRadiusM(0.5);
		metap.setPokedexWeightKg(35);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.GRASS);
		metap.setCollisionHeadRadiusM(0.365);
		metap.setMovementTimerS(4);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1);
		metap.setUniqueId("V0114_POKEMON_TANGELA");
		metap.setBaseDefense(152);
		metap.setAttackTimerS(11);
		metap.setWeightStdDev(4.375);
		metap.setCylHeightM(1);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.9);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.32);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.VINE_WHIP_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.POWER_WHIP,
				PokemonMove.SLUDGE_BOMB,
				PokemonMove.SOLAR_BEAM
		});
		metap.setNumber(114);
		meta.put(PokemonId.TANGELA, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0115_POKEMON_KANGASKHAN");
		metap.setFamily(PokemonFamilyId.FAMILY_KANGASKHAN);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(2.2);
		metap.setHeightStdDev(0.275);
		metap.setBaseStamina(210);
		metap.setCylRadiusM(0.576);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(142);
		metap.setDiskRadiusM(0.864);
		metap.setCollisionRadiusM(0.504);
		metap.setPokedexWeightKg(80);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.36);
		metap.setMovementTimerS(11);
		metap.setJumpTimeS(0.7);
		metap.setModelScale(0.72);
		metap.setUniqueId("V0115_POKEMON_KANGASKHAN");
		metap.setBaseDefense(178);
		metap.setAttackTimerS(4);
		metap.setWeightStdDev(10);
		metap.setCylHeightM(1.584);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.26);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SLAP_FAST,
				PokemonMove.LOW_KICK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STOMP,
				PokemonMove.EARTHQUAKE,
				PokemonMove.BRICK_BREAK
		});
		metap.setNumber(115);
		meta.put(PokemonId.KANGASKHAN, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0116_POKEMON_HORSEA");
		metap.setFamily(PokemonFamilyId.FAMILY_HORSEA);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.4);
		metap.setHeightStdDev(0.05);
		metap.setBaseStamina(60);
		metap.setCylRadiusM(0.25);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(122);
		metap.setDiskRadiusM(0.2775);
		metap.setCollisionRadiusM(0.148);
		metap.setPokedexWeightKg(8);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.185);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.48);
		metap.setUniqueId("V0116_POKEMON_HORSEA");
		metap.setBaseDefense(100);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(1);
		metap.setCylHeightM(0.74);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.444);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.185);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.WATER_GUN_FAST,
				PokemonMove.BUBBLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.FLASH_CANNON,
				PokemonMove.BUBBLE_BEAM,
				PokemonMove.DRAGON_PULSE
		});
		metap.setNumber(116);
		meta.put(PokemonId.HORSEA, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0117_POKEMON_SEADRA");
		metap.setFamily(PokemonFamilyId.FAMILY_HORSEA);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.2);
		metap.setHeightStdDev(0.15);
		metap.setBaseStamina(110);
		metap.setCylRadiusM(0.46);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(176);
		metap.setDiskRadiusM(0.69);
		metap.setCollisionRadiusM(0.322);
		metap.setPokedexWeightKg(25);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.414);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.92);
		metap.setUniqueId("V0117_POKEMON_SEADRA");
		metap.setBaseDefense(150);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(3.125);
		metap.setCylHeightM(1.15);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.46);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.HORSEA);
		metap.setCylGroundM(0.46);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.DRAGON_BREATH_FAST,
				PokemonMove.WATER_GUN_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.BLIZZARD,
				PokemonMove.HYDRO_PUMP,
				PokemonMove.DRAGON_PULSE
		});
		metap.setNumber(117);
		meta.put(PokemonId.SEADRA, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0118_POKEMON_GOLDEEN");
		metap.setFamily(PokemonFamilyId.FAMILY_GOLDEEN);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.6);
		metap.setHeightStdDev(0.075);
		metap.setBaseStamina(90);
		metap.setCylRadiusM(0.27);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(112);
		metap.setDiskRadiusM(0.405);
		metap.setCollisionRadiusM(0.135);
		metap.setPokedexWeightKg(15);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.16875);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.35);
		metap.setUniqueId("V0118_POKEMON_GOLDEEN");
		metap.setBaseDefense(126);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(1.875);
		metap.setCylHeightM(0.3375);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.16875);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.3375);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.PECK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.WATER_PULSE,
				PokemonMove.HORN_ATTACK,
				PokemonMove.AQUA_TAIL
		});
		metap.setNumber(118);
		meta.put(PokemonId.GOLDEEN, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0119_POKEMON_SEAKING");
		metap.setFamily(PokemonFamilyId.FAMILY_GOLDEEN);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.3);
		metap.setHeightStdDev(0.1625);
		metap.setBaseStamina(160);
		metap.setCylRadiusM(0.396);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(172);
		metap.setDiskRadiusM(0.594);
		metap.setCollisionRadiusM(0.044);
		metap.setPokedexWeightKg(39);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.242);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.88);
		metap.setUniqueId("V0119_POKEMON_SEAKING");
		metap.setBaseDefense(160);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(4.875);
		metap.setCylHeightM(0.748);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.044);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.GOLDEEN);
		metap.setCylGroundM(0.33);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POISON_JAB_FAST,
				PokemonMove.PECK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.ICY_WIND,
				PokemonMove.MEGAHORN,
				PokemonMove.DRILL_RUN
		});
		metap.setNumber(119);
		meta.put(PokemonId.SEAKING, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0120_POKEMON_STARYU");
		metap.setFamily(PokemonFamilyId.FAMILY_STARYU);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.8);
		metap.setHeightStdDev(0.1);
		metap.setBaseStamina(60);
		metap.setCylRadiusM(0.4125);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(130);
		metap.setDiskRadiusM(0.6188);
		metap.setCollisionRadiusM(0.4125);
		metap.setPokedexWeightKg(34.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.20625);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1.35);
		metap.setModelScale(1.1);
		metap.setUniqueId("V0120_POKEMON_STARYU");
		metap.setBaseDefense(128);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(4.3125);
		metap.setCylHeightM(0.88000011);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.88000011);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.4);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.WATER_GUN_FAST,
				PokemonMove.QUICK_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.POWER_GEM,
				PokemonMove.BUBBLE_BEAM,
				PokemonMove.SWIFT
		});
		metap.setNumber(120);
		meta.put(PokemonId.STARYU, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0121_POKEMON_STARMIE");
		metap.setFamily(PokemonFamilyId.FAMILY_STARYU);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.PSYCHIC);
		metap.setPokedexHeightM(1.1);
		metap.setHeightStdDev(0.1375);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.485);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(194);
		metap.setDiskRadiusM(0.7275);
		metap.setCollisionRadiusM(0.485);
		metap.setPokedexWeightKg(80);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.2425);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1.6);
		metap.setModelScale(0.97);
		metap.setUniqueId("V0121_POKEMON_STARMIE");
		metap.setBaseDefense(192);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(10);
		metap.setCylHeightM(1.067);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.067);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.STARYU);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.WATER_GUN_FAST,
				PokemonMove.QUICK_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYBEAM,
				PokemonMove.HYDRO_PUMP,
				PokemonMove.POWER_GEM
		});
		metap.setNumber(121);
		meta.put(PokemonId.STARMIE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0122_POKEMON_MR_MIME");
		metap.setFamily(PokemonFamilyId.FAMILY_MR_MIME);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.FAIRY);
		metap.setPokedexHeightM(1.3);
		metap.setHeightStdDev(0.1625);
		metap.setBaseStamina(80);
		metap.setCylRadiusM(0.445);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(154);
		metap.setDiskRadiusM(0.6675);
		metap.setCollisionRadiusM(0.267);
		metap.setPokedexWeightKg(54.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.PSYCHIC);
		metap.setCollisionHeadRadiusM(0.267);
		metap.setMovementTimerS(5);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.89);
		metap.setUniqueId("V0122_POKEMON_MR_MIME");
		metap.setBaseDefense(196);
		metap.setAttackTimerS(14);
		metap.setWeightStdDev(6.8125);
		metap.setCylHeightM(1.157);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.6675);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.ZEN_HEADBUTT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.SHADOW_BALL,
				PokemonMove.PSYBEAM
		});
		metap.setNumber(122);
		meta.put(PokemonId.MR_MIME, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0123_POKEMON_SCYTHER");
		metap.setFamily(PokemonFamilyId.FAMILY_SCYTHER);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.5);
		metap.setHeightStdDev(0.1875);
		metap.setBaseStamina(140);
		metap.setCylRadiusM(0.76);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(176);
		metap.setDiskRadiusM(1.14);
		metap.setCollisionRadiusM(0.4);
		metap.setPokedexWeightKg(56);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.2);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.8);
		metap.setUniqueId("V0123_POKEMON_SCYTHER");
		metap.setBaseDefense(180);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(7);
		metap.setCylHeightM(1.2);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.4);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.STEEL_WING_FAST,
				PokemonMove.FURY_CUTTER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.BUG_BUZZ,
				PokemonMove.X_SCISSOR,
				PokemonMove.NIGHT_SLASH
		});
		metap.setNumber(123);
		meta.put(PokemonId.SCYTHER, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0124_POKEMON_JYNX");
		metap.setFamily(PokemonFamilyId.FAMILY_JYNX);
		metap.setPokemonClass(PokemonClass.COMMON);
		metap.setType2(PokemonType.PSYCHIC);
		metap.setPokedexHeightM(1.4);
		metap.setHeightStdDev(0.175);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.6525);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(172);
		metap.setDiskRadiusM(0.9788);
		metap.setCollisionRadiusM(0.435);
		metap.setPokedexWeightKg(40.6);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ICE);
		metap.setCollisionHeadRadiusM(0.522);
		metap.setMovementTimerS(4);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.87);
		metap.setUniqueId("V0124_POKEMON_JYNX");
		metap.setBaseDefense(134);
		metap.setAttackTimerS(11);
		metap.setWeightStdDev(5.075);
		metap.setCylHeightM(1.218);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.87);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POUND_FAST,
				PokemonMove.FROST_BREATH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYSHOCK,
				PokemonMove.DRAINING_KISS,
				PokemonMove.ICE_PUNCH
		});
		metap.setNumber(124);
		meta.put(PokemonId.JYNX, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0125_POKEMON_ELECTABUZZ");
		metap.setFamily(PokemonFamilyId.FAMILY_ELECTABUZZ);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.1);
		metap.setHeightStdDev(0.1375);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.5635);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(198);
		metap.setDiskRadiusM(0.8453);
		metap.setCollisionRadiusM(0.392);
		metap.setPokedexWeightKg(30);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ELECTRIC);
		metap.setCollisionHeadRadiusM(0.28175);
		metap.setMovementTimerS(6);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.98);
		metap.setUniqueId("V0125_POKEMON_ELECTABUZZ");
		metap.setBaseDefense(160);
		metap.setAttackTimerS(17);
		metap.setWeightStdDev(3.75);
		metap.setCylHeightM(0.98);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.735);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.LOW_KICK_FAST,
				PokemonMove.THUNDER_SHOCK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.THUNDER_PUNCH,
				PokemonMove.THUNDER,
				PokemonMove.THUNDERBOLT
		});
		metap.setNumber(125);
		meta.put(PokemonId.ELECTABUZZ, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0126_POKEMON_MAGMAR");
		metap.setFamily(PokemonFamilyId.FAMILY_MAGMAR);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.3);
		metap.setHeightStdDev(0.1625);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.66);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(214);
		metap.setDiskRadiusM(0.99);
		metap.setCollisionRadiusM(0.44);
		metap.setPokedexWeightKg(44.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.33);
		metap.setMovementTimerS(14);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.88);
		metap.setUniqueId("V0126_POKEMON_MAGMAR");
		metap.setBaseDefense(158);
		metap.setAttackTimerS(5);
		metap.setWeightStdDev(5.5625);
		metap.setCylHeightM(1.144);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.88);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.KARATE_CHOP_FAST,
				PokemonMove.EMBER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.FIRE_PUNCH,
				PokemonMove.FLAMETHROWER,
				PokemonMove.FIRE_BLAST
		});
		metap.setNumber(126);
		meta.put(PokemonId.MAGMAR, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0127_POKEMON_PINSIR");
		metap.setFamily(PokemonFamilyId.FAMILY_PINSIR);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.5);
		metap.setHeightStdDev(0.1875);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.348);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(184);
		metap.setDiskRadiusM(0.522);
		metap.setCollisionRadiusM(0.348);
		metap.setPokedexWeightKg(55);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.BUG);
		metap.setCollisionHeadRadiusM(0.348);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.87);
		metap.setUniqueId("V0127_POKEMON_PINSIR");
		metap.setBaseDefense(186);
		metap.setAttackTimerS(3);
		metap.setWeightStdDev(6.875);
		metap.setCylHeightM(1.131);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.87);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.FURY_CUTTER_FAST,
				PokemonMove.ROCK_SMASH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.SUBMISSION,
				PokemonMove.X_SCISSOR,
				PokemonMove.VICE_GRIP
		});
		metap.setNumber(127);
		meta.put(PokemonId.PINSIR, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0128_POKEMON_TAUROS");
		metap.setFamily(PokemonFamilyId.FAMILY_TAUROS);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.4);
		metap.setHeightStdDev(0.175);
		metap.setBaseStamina(150);
		metap.setCylRadiusM(0.5742);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(148);
		metap.setDiskRadiusM(0.8613);
		metap.setCollisionRadiusM(0.435);
		metap.setPokedexWeightKg(88.4);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.2871);
		metap.setMovementTimerS(4);
		metap.setJumpTimeS(1.2);
		metap.setModelScale(0.87);
		metap.setUniqueId("V0128_POKEMON_TAUROS");
		metap.setBaseDefense(184);
		metap.setAttackTimerS(11);
		metap.setWeightStdDev(11.05);
		metap.setCylHeightM(1.19625);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.19625);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.24);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ZEN_HEADBUTT_FAST,
				PokemonMove.TACKLE_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.IRON_HEAD,
				PokemonMove.EARTHQUAKE,
				PokemonMove.HORN_ATTACK
		});
		metap.setNumber(128);
		meta.put(PokemonId.TAUROS, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0129_POKEMON_MAGIKARP");
		metap.setFamily(PokemonFamilyId.FAMILY_MAGIKARP);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.9);
		metap.setHeightStdDev(0.1125);
		metap.setBaseStamina(40);
		metap.setCylRadiusM(0.428);
		metap.setBaseFleeRate(0.15);
		metap.setBaseAttack(42);
		metap.setDiskRadiusM(0.642);
		metap.setCollisionRadiusM(0.2675);
		metap.setPokedexWeightKg(10);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.321);
		metap.setMovementTimerS(3600);
		metap.setJumpTimeS(1.3);
		metap.setModelScale(1.07);
		metap.setUniqueId("V0129_POKEMON_MAGIKARP");
		metap.setBaseDefense(84);
		metap.setAttackTimerS(3600);
		metap.setWeightStdDev(1.25);
		metap.setCylHeightM(0.535);
		metap.setCandyToEvolve(400);
		metap.setCollisionHeightM(0.4815);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.56);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.SPLASH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STRUGGLE
		});
		metap.setNumber(129);
		meta.put(PokemonId.MAGIKARP, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0130_POKEMON_GYARADOS");
		metap.setFamily(PokemonFamilyId.FAMILY_MAGIKARP);
		metap.setPokemonClass(PokemonClass.EPIC);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(6.5);
		metap.setHeightStdDev(0.8125);
		metap.setBaseStamina(190);
		metap.setCylRadiusM(0.48);
		metap.setBaseFleeRate(0.07);
		metap.setBaseAttack(192);
		metap.setDiskRadiusM(0.72);
		metap.setCollisionRadiusM(0.24);
		metap.setPokedexWeightKg(235);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.36);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.48);
		metap.setUniqueId("V0130_POKEMON_GYARADOS");
		metap.setBaseDefense(196);
		metap.setAttackTimerS(3);
		metap.setWeightStdDev(29.375);
		metap.setCylHeightM(1.2);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.48);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.08);
		metap.setParentId(PokemonId.MAGIKARP);
		metap.setCylGroundM(0.48);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.DRAGON_BREATH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.TWISTER,
				PokemonMove.HYDRO_PUMP,
				PokemonMove.DRAGON_PULSE
		});
		metap.setNumber(130);
		meta.put(PokemonId.GYARADOS, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0131_POKEMON_LAPRAS");
		metap.setFamily(PokemonFamilyId.FAMILY_LAPRAS);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.ICE);
		metap.setPokedexHeightM(2.5);
		metap.setHeightStdDev(0.3125);
		metap.setBaseStamina(260);
		metap.setCylRadiusM(0.7);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(186);
		metap.setDiskRadiusM(1.05);
		metap.setCollisionRadiusM(0.525);
		metap.setPokedexWeightKg(220);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.35);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1.2);
		metap.setModelScale(0.7);
		metap.setUniqueId("V0131_POKEMON_LAPRAS");
		metap.setBaseDefense(190);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(27.5);
		metap.setCylHeightM(1.75);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.7);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ICE_SHARD_FAST,
				PokemonMove.FROST_BREATH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.BLIZZARD,
				PokemonMove.ICE_BEAM,
				PokemonMove.DRAGON_PULSE
		});
		metap.setNumber(131);
		meta.put(PokemonId.LAPRAS, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0132_POKEMON_DITTO");
		metap.setFamily(PokemonFamilyId.FAMILY_DITTO);
		metap.setPokemonClass(PokemonClass.EPIC);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.3);
		metap.setHeightStdDev(0.0375);
		metap.setBaseStamina(96);
		metap.setCylRadiusM(0.4025);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(110);
		metap.setDiskRadiusM(0.6038);
		metap.setCollisionRadiusM(0.4025);
		metap.setPokedexWeightKg(4);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.20125);
		metap.setMovementTimerS(3600);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.61);
		metap.setUniqueId("V0132_POKEMON_DITTO");
		metap.setBaseDefense(110);
		metap.setAttackTimerS(3600);
		metap.setWeightStdDev(0.5);
		metap.setCylHeightM(0.52325);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.52325);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POUND_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STRUGGLE
		});
		metap.setNumber(132);
		meta.put(PokemonId.DITTO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0133_POKEMON_EEVEE");
		metap.setFamily(PokemonFamilyId.FAMILY_EEVEE);
		metap.setPokemonClass(PokemonClass.VERY_COMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.3);
		metap.setHeightStdDev(0.0375);
		metap.setBaseStamina(110);
		metap.setCylRadiusM(0.42);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(114);
		metap.setDiskRadiusM(0.63);
		metap.setCollisionRadiusM(0.252);
		metap.setPokedexWeightKg(6.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.252);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(1.35);
		metap.setModelScale(1.68);
		metap.setUniqueId("V0133_POKEMON_EEVEE");
		metap.setBaseDefense(128);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.8125);
		metap.setCylHeightM(0.504);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.336);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.32);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.TACKLE_FAST,
				PokemonMove.QUICK_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DIG,
				PokemonMove.SWIFT,
				PokemonMove.BODY_SLAM
		});
		metap.setNumber(133);
		meta.put(PokemonId.EEVEE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0134_POKEMON_VAPOREON");
		metap.setFamily(PokemonFamilyId.FAMILY_EEVEE);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(260);
		metap.setCylRadiusM(0.3465);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(186);
		metap.setDiskRadiusM(0.5198);
		metap.setCollisionRadiusM(0.21);
		metap.setPokedexWeightKg(29);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.WATER);
		metap.setCollisionHeadRadiusM(0.2625);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.05);
		metap.setUniqueId("V0134_POKEMON_VAPOREON");
		metap.setBaseDefense(168);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(3.625);
		metap.setCylHeightM(0.94499987);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.525);
		metap.setShoulderModeScale(0.4);
		metap.setBaseCaptureRate(0.12);
		metap.setParentId(PokemonId.EEVEE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.WATER_GUN_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.WATER_PULSE,
				PokemonMove.HYDRO_PUMP,
				PokemonMove.AQUA_TAIL
		});
		metap.setNumber(134);
		meta.put(PokemonId.VAPOREON, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0135_POKEMON_JOLTEON");
		metap.setFamily(PokemonFamilyId.FAMILY_EEVEE);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.8);
		metap.setHeightStdDev(0.1);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.33);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(192);
		metap.setDiskRadiusM(0.495);
		metap.setCollisionRadiusM(0.22);
		metap.setPokedexWeightKg(24.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ELECTRIC);
		metap.setCollisionHeadRadiusM(0.22);
		metap.setMovementTimerS(4);
		metap.setJumpTimeS(1.3);
		metap.setModelScale(1.1);
		metap.setUniqueId("V0135_POKEMON_JOLTEON");
		metap.setBaseDefense(174);
		metap.setAttackTimerS(11);
		metap.setWeightStdDev(3.0625);
		metap.setCylHeightM(0.88000011);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.55);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.12);
		metap.setParentId(PokemonId.EEVEE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.THUNDER_SHOCK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.THUNDER,
				PokemonMove.THUNDERBOLT,
				PokemonMove.DISCHARGE
		});
		metap.setNumber(135);
		meta.put(PokemonId.JOLTEON, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0136_POKEMON_FLAREON");
		metap.setFamily(PokemonFamilyId.FAMILY_EEVEE);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.9);
		metap.setHeightStdDev(0.1125);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.3045);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(238);
		metap.setDiskRadiusM(0.4568);
		metap.setCollisionRadiusM(0.2175);
		metap.setPokedexWeightKg(25);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.19575);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1.35);
		metap.setModelScale(0.87);
		metap.setUniqueId("V0136_POKEMON_FLAREON");
		metap.setBaseDefense(178);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(3.125);
		metap.setCylHeightM(0.783);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.522);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.12);
		metap.setParentId(PokemonId.EEVEE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.EMBER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.FLAMETHROWER,
				PokemonMove.HEAT_WAVE,
				PokemonMove.FIRE_BLAST
		});
		metap.setNumber(136);
		meta.put(PokemonId.FLAREON, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0137_POKEMON_PORYGON");
		metap.setFamily(PokemonFamilyId.FAMILY_PORYGON);
		metap.setPokemonClass(PokemonClass.EPIC);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.8);
		metap.setHeightStdDev(0.1);
		metap.setBaseStamina(130);
		metap.setCylRadiusM(0.55);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(156);
		metap.setDiskRadiusM(0.825);
		metap.setCollisionRadiusM(0.385);
		metap.setPokedexWeightKg(36.5);
		metap.setMovementType(MovementType.HOVERING);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.33);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.1);
		metap.setUniqueId("V0137_POKEMON_PORYGON");
		metap.setBaseDefense(158);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(4.5625);
		metap.setCylHeightM(0.93500012);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.55);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.32);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.55);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.TACKLE_FAST,
				PokemonMove.QUICK_ATTACK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DISCHARGE,
				PokemonMove.PSYBEAM,
				PokemonMove.SIGNAL_BEAM
		});
		metap.setNumber(137);
		meta.put(PokemonId.PORYGON, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0138_POKEMON_OMANYTE");
		metap.setFamily(PokemonFamilyId.FAMILY_OMANYTE);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.WATER);
		metap.setPokedexHeightM(0.4);
		metap.setHeightStdDev(0.05);
		metap.setBaseStamina(70);
		metap.setCylRadiusM(0.222);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(132);
		metap.setDiskRadiusM(0.333);
		metap.setCollisionRadiusM(0.222);
		metap.setPokedexWeightKg(7.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ROCK);
		metap.setCollisionHeadRadiusM(0.111);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1.3);
		metap.setModelScale(1.48);
		metap.setUniqueId("V0138_POKEMON_OMANYTE");
		metap.setBaseDefense(160);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(0.9375);
		metap.setCylHeightM(0.592);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.592);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.32);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.WATER_GUN_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.ROCK_TOMB,
				PokemonMove.ANCIENT_POWER,
				PokemonMove.BRINE
		});
		metap.setNumber(138);
		meta.put(PokemonId.OMANYTE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0139_POKEMON_OMASTAR");
		metap.setFamily(PokemonFamilyId.FAMILY_OMANYTE);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.WATER);
		metap.setPokedexHeightM(1);
		metap.setHeightStdDev(0.125);
		metap.setBaseStamina(140);
		metap.setCylRadiusM(0.375);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(180);
		metap.setDiskRadiusM(0.5625);
		metap.setCollisionRadiusM(0.25);
		metap.setPokedexWeightKg(35);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ROCK);
		metap.setCollisionHeadRadiusM(0.1875);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(1);
		metap.setUniqueId("V0139_POKEMON_OMASTAR");
		metap.setBaseDefense(202);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(4.375);
		metap.setCylHeightM(1);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.9);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.12);
		metap.setParentId(PokemonId.OMANYTE);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ROCK_THROW_FAST,
				PokemonMove.WATER_GUN_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.HYDRO_PUMP,
				PokemonMove.ANCIENT_POWER,
				PokemonMove.ROCK_SLIDE
		});
		metap.setNumber(139);
		meta.put(PokemonId.OMASTAR, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0140_POKEMON_KABUTO");
		metap.setFamily(PokemonFamilyId.FAMILY_KABUTO);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.WATER);
		metap.setPokedexHeightM(0.5);
		metap.setHeightStdDev(0.0625);
		metap.setBaseStamina(60);
		metap.setCylRadiusM(0.3375);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(148);
		metap.setDiskRadiusM(0.5063);
		metap.setCollisionRadiusM(0.3375);
		metap.setPokedexWeightKg(11.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ROCK);
		metap.setCollisionHeadRadiusM(0.16875);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(0.9);
		metap.setModelScale(1.35);
		metap.setUniqueId("V0140_POKEMON_KABUTO");
		metap.setBaseDefense(142);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(1.4375);
		metap.setCylHeightM(0.50625);
		metap.setCandyToEvolve(50);
		metap.setCollisionHeightM(0.50625);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.32);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.SCRATCH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.ANCIENT_POWER,
				PokemonMove.AQUA_JET,
				PokemonMove.ROCK_TOMB
		});
		metap.setNumber(140);
		meta.put(PokemonId.KABUTO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0141_POKEMON_KABUTOPS");
		metap.setFamily(PokemonFamilyId.FAMILY_KABUTO);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.WATER);
		metap.setPokedexHeightM(1.3);
		metap.setHeightStdDev(0.1625);
		metap.setBaseStamina(120);
		metap.setCylRadiusM(0.455);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(190);
		metap.setDiskRadiusM(0.6825);
		metap.setCollisionRadiusM(0.364);
		metap.setPokedexWeightKg(40.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.ROCK);
		metap.setCollisionHeadRadiusM(0.3185);
		metap.setMovementTimerS(11);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.91);
		metap.setUniqueId("V0141_POKEMON_KABUTOPS");
		metap.setBaseDefense(190);
		metap.setAttackTimerS(4);
		metap.setWeightStdDev(5.0625);
		metap.setCylHeightM(1.1375);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.91);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.12);
		metap.setParentId(PokemonId.KABUTO);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.MUD_SHOT_FAST,
				PokemonMove.FURY_CUTTER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.STONE_EDGE,
				PokemonMove.WATER_PULSE,
				PokemonMove.ANCIENT_POWER
		});
		metap.setNumber(141);
		meta.put(PokemonId.KABUTOPS, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0142_POKEMON_AERODACTYL");
		metap.setFamily(PokemonFamilyId.FAMILY_AERODACTYL);
		metap.setPokemonClass(PokemonClass.VERY_RARE);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.8);
		metap.setHeightStdDev(0.225);
		metap.setBaseStamina(160);
		metap.setCylRadiusM(0.399);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(182);
		metap.setDiskRadiusM(0.5985);
		metap.setCollisionRadiusM(0.285);
		metap.setPokedexWeightKg(59);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.ROCK);
		metap.setCollisionHeadRadiusM(0.285);
		metap.setMovementTimerS(5);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.57);
		metap.setUniqueId("V0142_POKEMON_AERODACTYL");
		metap.setBaseDefense(162);
		metap.setAttackTimerS(14);
		metap.setWeightStdDev(7.375);
		metap.setCylHeightM(0.9975);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.9975);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.855);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.BITE_FAST,
				PokemonMove.STEEL_WING_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.IRON_HEAD,
				PokemonMove.HYPER_BEAM,
				PokemonMove.ANCIENT_POWER
		});
		metap.setNumber(142);
		meta.put(PokemonId.AERODACTYL, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0143_POKEMON_SNORLAX");
		metap.setFamily(PokemonFamilyId.FAMILY_SNORLAX);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(2.1);
		metap.setHeightStdDev(0.2625);
		metap.setBaseStamina(320);
		metap.setCylRadiusM(0.74);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(180);
		metap.setDiskRadiusM(1.11);
		metap.setCollisionRadiusM(0.74);
		metap.setPokedexWeightKg(460);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.NORMAL);
		metap.setCollisionHeadRadiusM(0.481);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.74);
		metap.setUniqueId("V0143_POKEMON_SNORLAX");
		metap.setBaseDefense(180);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(57.5);
		metap.setCylHeightM(1.48);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.11);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.16);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.ZEN_HEADBUTT_FAST,
				PokemonMove.LICK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.EARTHQUAKE,
				PokemonMove.HYPER_BEAM,
				PokemonMove.BODY_SLAM
		});
		metap.setNumber(143);
		meta.put(PokemonId.SNORLAX, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0144_POKEMON_ARTICUNO");
		metap.setFamily(PokemonFamilyId.FAMILY_ARTICUNO);
		metap.setPokemonClass(PokemonClass.LEGENDARY);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.7);
		metap.setHeightStdDev(0.2125);
		metap.setBaseStamina(180);
		metap.setCylRadiusM(0.396);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(198);
		metap.setDiskRadiusM(0.594);
		metap.setCollisionRadiusM(0.231);
		metap.setPokedexWeightKg(55.4);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.ICE);
		metap.setCollisionHeadRadiusM(0.231);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.66);
		metap.setUniqueId("V0144_POKEMON_ARTICUNO");
		metap.setBaseDefense(242);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(6.925);
		metap.setCylHeightM(0.99);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.66);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.66);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.FROST_BREATH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.ICY_WIND,
				PokemonMove.BLIZZARD,
				PokemonMove.ICE_BEAM
		});
		metap.setNumber(144);
		meta.put(PokemonId.ARTICUNO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0145_POKEMON_ZAPDOS");
		metap.setFamily(PokemonFamilyId.FAMILY_ZAPDOS);
		metap.setPokemonClass(PokemonClass.LEGENDARY);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(1.6);
		metap.setHeightStdDev(0.2);
		metap.setBaseStamina(180);
		metap.setCylRadiusM(0.5175);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(232);
		metap.setDiskRadiusM(0.7763);
		metap.setCollisionRadiusM(0.4485);
		metap.setPokedexWeightKg(52.6);
		metap.setMovementType(MovementType.ELECTRIC);
		metap.setType1(PokemonType.ELECTRIC);
		metap.setCollisionHeadRadiusM(0.276);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.69);
		metap.setUniqueId("V0145_POKEMON_ZAPDOS");
		metap.setBaseDefense(194);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(6.575);
		metap.setCylHeightM(1.035);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.759);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.8625);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.THUNDER_SHOCK_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.THUNDER,
				PokemonMove.THUNDERBOLT,
				PokemonMove.DISCHARGE
		});
		metap.setNumber(145);
		meta.put(PokemonId.ZAPDOS, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0146_POKEMON_MOLTRES");
		metap.setFamily(PokemonFamilyId.FAMILY_MOLTRES);
		metap.setPokemonClass(PokemonClass.LEGENDARY);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(2);
		metap.setHeightStdDev(0.25);
		metap.setBaseStamina(180);
		metap.setCylRadiusM(0.62);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(242);
		metap.setDiskRadiusM(0.93);
		metap.setCollisionRadiusM(0.403);
		metap.setPokedexWeightKg(60);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.FIRE);
		metap.setCollisionHeadRadiusM(0.217);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.62);
		metap.setUniqueId("V0146_POKEMON_MOLTRES");
		metap.setBaseDefense(194);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(7.5);
		metap.setCylHeightM(1.395);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.93);
		metap.setShoulderModeScale(0.25);
		metap.setBaseCaptureRate(0.00);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.93);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.EMBER_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.FLAMETHROWER,
				PokemonMove.HEAT_WAVE,
				PokemonMove.FIRE_BLAST
		});
		metap.setNumber(146);
		meta.put(PokemonId.MOLTRES, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0147_POKEMON_DRATINI");
		metap.setFamily(PokemonFamilyId.FAMILY_DRATINI);
		metap.setPokemonClass(PokemonClass.UNCOMMON);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(1.8);
		metap.setHeightStdDev(0.225);
		metap.setBaseStamina(82);
		metap.setCylRadiusM(0.2775);
		metap.setBaseFleeRate(0.09);
		metap.setBaseAttack(128);
		metap.setDiskRadiusM(0.4163);
		metap.setCollisionRadiusM(0.2775);
		metap.setPokedexWeightKg(3.3);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.DRAGON);
		metap.setCollisionHeadRadiusM(0.19425);
		metap.setMovementTimerS(10);
		metap.setJumpTimeS(0.85);
		metap.setModelScale(1.11);
		metap.setUniqueId("V0147_POKEMON_DRATINI");
		metap.setBaseDefense(110);
		metap.setAttackTimerS(29);
		metap.setWeightStdDev(0.4125);
		metap.setCylHeightM(0.8325);
		metap.setCandyToEvolve(25);
		metap.setCollisionHeightM(0.555);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.32);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.DRAGON_BREATH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.TWISTER,
				PokemonMove.WRAP,
				PokemonMove.AQUA_TAIL
		});
		metap.setNumber(147);
		meta.put(PokemonId.DRATINI, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0148_POKEMON_DRAGONAIR");
		metap.setFamily(PokemonFamilyId.FAMILY_DRATINI);
		metap.setPokemonClass(PokemonClass.RARE);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(4);
		metap.setHeightStdDev(0.5);
		metap.setBaseStamina(122);
		metap.setCylRadiusM(0.5625);
		metap.setBaseFleeRate(0.06);
		metap.setBaseAttack(170);
		metap.setDiskRadiusM(0.8438);
		metap.setCollisionRadiusM(0.375);
		metap.setPokedexWeightKg(16.5);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.DRAGON);
		metap.setCollisionHeadRadiusM(0.28125);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1.25);
		metap.setModelScale(0.75);
		metap.setUniqueId("V0148_POKEMON_DRAGONAIR");
		metap.setBaseDefense(152);
		metap.setAttackTimerS(23);
		metap.setWeightStdDev(2.0625);
		metap.setCylHeightM(1.5);
		metap.setCandyToEvolve(100);
		metap.setCollisionHeightM(1.125);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.08);
		metap.setParentId(PokemonId.DRATINI);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.DRAGON_BREATH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.WRAP,
				PokemonMove.AQUA_TAIL,
				PokemonMove.DRAGON_PULSE
		});
		metap.setNumber(148);
		meta.put(PokemonId.DRAGONAIR, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0149_POKEMON_DRAGONITE");
		metap.setFamily(PokemonFamilyId.FAMILY_DRATINI);
		metap.setPokemonClass(PokemonClass.EPIC);
		metap.setType2(PokemonType.FLYING);
		metap.setPokedexHeightM(2.2);
		metap.setHeightStdDev(0.275);
		metap.setBaseStamina(182);
		metap.setCylRadiusM(0.42);
		metap.setBaseFleeRate(0.05);
		metap.setBaseAttack(250);
		metap.setDiskRadiusM(0.63);
		metap.setCollisionRadiusM(0.42);
		metap.setPokedexWeightKg(210);
		metap.setMovementType(MovementType.FLYING);
		metap.setType1(PokemonType.DRAGON);
		metap.setCollisionHeadRadiusM(0.245);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1);
		metap.setModelScale(0.7);
		metap.setUniqueId("V0149_POKEMON_DRAGONITE");
		metap.setBaseDefense(212);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(26.25);
		metap.setCylHeightM(1.47);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.05);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0.04);
		metap.setParentId(PokemonId.DRAGONAIR);
		metap.setCylGroundM(0.595);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.STEEL_WING_FAST,
				PokemonMove.DRAGON_BREATH_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.DRAGON_CLAW,
				PokemonMove.HYPER_BEAM,
				PokemonMove.DRAGON_PULSE
		});
		metap.setNumber(149);
		meta.put(PokemonId.DRAGONITE, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0150_POKEMON_MEWTWO");
		metap.setFamily(PokemonFamilyId.FAMILY_MEWTWO);
		metap.setPokemonClass(PokemonClass.LEGENDARY);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(2);
		metap.setHeightStdDev(0.25);
		metap.setBaseStamina(212);
		metap.setCylRadiusM(0.37);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(284);
		metap.setDiskRadiusM(0.555);
		metap.setCollisionRadiusM(0.37);
		metap.setPokedexWeightKg(122);
		metap.setMovementType(MovementType.JUMP);
		metap.setType1(PokemonType.PSYCHIC);
		metap.setCollisionHeadRadiusM(0.185);
		metap.setMovementTimerS(8);
		metap.setJumpTimeS(1.2);
		metap.setModelScale(0.74);
		metap.setUniqueId("V0150_POKEMON_MEWTWO");
		metap.setBaseDefense(202);
		metap.setAttackTimerS(3);
		metap.setWeightStdDev(15.25);
		metap.setCylHeightM(1.48);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(1.184);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.CONFUSION_FAST,
				PokemonMove.PSYCHO_CUT_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.PSYCHIC,
				PokemonMove.SHADOW_BALL,
				PokemonMove.HYPER_BEAM
		});
		metap.setNumber(150);
		meta.put(PokemonId.MEWTWO, metap);

		metap = new PokemonMeta();
		metap.setTemplateId(" V0151_POKEMON_MEW");
		metap.setFamily(PokemonFamilyId.FAMILY_MEWTWO);
		metap.setPokemonClass(PokemonClass.MYTHIC);
		metap.setType2(PokemonType.NONE);
		metap.setPokedexHeightM(0.4);
		metap.setHeightStdDev(0.05);
		metap.setBaseStamina(200);
		metap.setCylRadiusM(0.282);
		metap.setBaseFleeRate(0.1);
		metap.setBaseAttack(220);
		metap.setDiskRadiusM(0.423);
		metap.setCollisionRadiusM(0.141);
		metap.setPokedexWeightKg(4);
		metap.setMovementType(MovementType.PSYCHIC);
		metap.setType1(PokemonType.PSYCHIC);
		metap.setCollisionHeadRadiusM(0.17625);
		metap.setMovementTimerS(3);
		metap.setJumpTimeS(1);
		metap.setModelScale(1.41);
		metap.setUniqueId("V0151_POKEMON_MEW");
		metap.setBaseDefense(220);
		metap.setAttackTimerS(8);
		metap.setWeightStdDev(0.5);
		metap.setCylHeightM(0.7755);
		metap.setCandyToEvolve(0);
		metap.setCollisionHeightM(0.564);
		metap.setShoulderModeScale(0.5);
		metap.setBaseCaptureRate(0);
		metap.setParentId(PokemonId.UNRECOGNIZED);
		metap.setCylGroundM(0.0705);
		metap.setQuickMoves(new PokemonMove[]{
				PokemonMove.POUND_FAST
		});
		metap.setCinematicMoves(new PokemonMove[]{
				PokemonMove.MOONBLAST,
				PokemonMove.FIRE_BLAST,
				PokemonMove.SOLAR_BEAM,
				PokemonMove.HYPER_BEAM,
				PokemonMove.PSYCHIC,
				PokemonMove.HURRICANE,
				PokemonMove.EARTHQUAKE,
				PokemonMove.DRAGON_PULSE,
				PokemonMove.THUNDER
		});
		metap.setNumber(151);
		meta.put(PokemonId.MEW, metap);

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
