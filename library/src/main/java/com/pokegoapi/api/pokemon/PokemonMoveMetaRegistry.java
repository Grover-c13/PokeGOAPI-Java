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

import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import lombok.Getter;

import java.util.EnumMap;

public class PokemonMoveMetaRegistry {

	@Getter
	private static EnumMap<PokemonMove, PokemonMoveMeta> meta = new EnumMap<>(PokemonMove.class);

	static {

		PokemonMoveMeta metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BODY_SLAM);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(40);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1560);
		metam.setEnergy(-50);
		meta.put(PokemonMove.BODY_SLAM, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.CROSS_CHOP);
		metam.setType(PokemonType.FIGHTING);
		metam.setPower(60);
		metam.setAccuracy(1);
		metam.setCritChance(0.25);
		metam.setTime(2000);
		metam.setEnergy(-100);
		meta.put(PokemonMove.CROSS_CHOP, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DRAGON_CLAW);
		metam.setType(PokemonType.DRAGON);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.25);
		metam.setTime(1500);
		metam.setEnergy(-50);
		meta.put(PokemonMove.DRAGON_CLAW, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.PSYCHO_CUT_FAST);
		metam.setType(PokemonType.PSYCHIC);
		metam.setPower(7);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(570);
		metam.setEnergy(7);
		meta.put(PokemonMove.PSYCHO_CUT_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.MUD_SHOT_FAST);
		metam.setType(PokemonType.GROUND);
		metam.setPower(6);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(550);
		metam.setEnergy(7);
		meta.put(PokemonMove.MUD_SHOT_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.POWER_WHIP);
		metam.setType(PokemonType.GRASS);
		metam.setPower(70);
		metam.setAccuracy(1);
		metam.setCritChance(0.25);
		metam.setTime(2800);
		metam.setEnergy(-100);
		meta.put(PokemonMove.POWER_WHIP, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.AQUA_TAIL);
		metam.setType(PokemonType.WATER);
		metam.setPower(45);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2350);
		metam.setEnergy(-50);
		meta.put(PokemonMove.AQUA_TAIL, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.IRON_HEAD);
		metam.setType(PokemonType.STEEL);
		metam.setPower(30);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2000);
		metam.setEnergy(-33);
		meta.put(PokemonMove.IRON_HEAD, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.GUNK_SHOT);
		metam.setType(PokemonType.POISON);
		metam.setPower(65);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3000);
		metam.setEnergy(-100);
		meta.put(PokemonMove.GUNK_SHOT, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.LICK_FAST);
		metam.setType(PokemonType.GHOST);
		metam.setPower(5);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(500);
		metam.setEnergy(6);
		meta.put(PokemonMove.LICK_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SCRATCH_FAST);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(6);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(500);
		metam.setEnergy(7);
		meta.put(PokemonMove.SCRATCH_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.WATER_GUN_FAST);
		metam.setType(PokemonType.WATER);
		metam.setPower(6);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(500);
		metam.setEnergy(7);
		meta.put(PokemonMove.WATER_GUN_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.WATER_GUN_FAST_BLASTOISE);
		metam.setType(PokemonType.WATER);
		metam.setPower(6);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(500);
		metam.setEnergy(7);
		meta.put(PokemonMove.WATER_GUN_FAST_BLASTOISE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SLUDGE_BOMB);
		metam.setType(PokemonType.POISON);
		metam.setPower(55);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2600);
		metam.setEnergy(-50);
		meta.put(PokemonMove.SLUDGE_BOMB, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.METAL_CLAW_FAST);
		metam.setType(PokemonType.STEEL);
		metam.setPower(8);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(630);
		metam.setEnergy(7);
		meta.put(PokemonMove.METAL_CLAW_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.HURRICANE);
		metam.setType(PokemonType.FLYING);
		metam.setPower(80);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3200);
		metam.setEnergy(-100);
		meta.put(PokemonMove.HURRICANE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BRICK_BREAK);
		metam.setType(PokemonType.FIGHTING);
		metam.setPower(30);
		metam.setAccuracy(1);
		metam.setCritChance(0.25);
		metam.setTime(1600);
		metam.setEnergy(-33);
		meta.put(PokemonMove.BRICK_BREAK, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.THUNDERBOLT);
		metam.setType(PokemonType.ELECTRIC);
		metam.setPower(55);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2700);
		metam.setEnergy(-50);
		meta.put(PokemonMove.THUNDERBOLT, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.PSYCHIC);
		metam.setType(PokemonType.PSYCHIC);
		metam.setPower(55);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2800);
		metam.setEnergy(-50);
		meta.put(PokemonMove.PSYCHIC, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.STONE_EDGE);
		metam.setType(PokemonType.ROCK);
		metam.setPower(80);
		metam.setAccuracy(1);
		metam.setCritChance(0.5);
		metam.setTime(3100);
		metam.setEnergy(-100);
		meta.put(PokemonMove.STONE_EDGE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SLUDGE_WAVE);
		metam.setType(PokemonType.POISON);
		metam.setPower(70);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3400);
		metam.setEnergy(-100);
		meta.put(PokemonMove.SLUDGE_WAVE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FLAMETHROWER);
		metam.setType(PokemonType.FIRE);
		metam.setPower(55);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2900);
		metam.setEnergy(-50);
		meta.put(PokemonMove.FLAMETHROWER, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.PLAY_ROUGH);
		metam.setType(PokemonType.FAIRY);
		metam.setPower(55);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2900);
		metam.setEnergy(-50);
		meta.put(PokemonMove.PLAY_ROUGH, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.MEGAHORN);
		metam.setType(PokemonType.BUG);
		metam.setPower(80);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3200);
		metam.setEnergy(-100);
		meta.put(PokemonMove.MEGAHORN, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SHADOW_CLAW_FAST);
		metam.setType(PokemonType.GHOST);
		metam.setPower(11);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(950);
		metam.setEnergy(8);
		meta.put(PokemonMove.SHADOW_CLAW_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.THUNDER_PUNCH);
		metam.setType(PokemonType.ELECTRIC);
		metam.setPower(40);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2400);
		metam.setEnergy(-33);
		meta.put(PokemonMove.THUNDER_PUNCH, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.HYPER_FANG);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2100);
		metam.setEnergy(-33);
		meta.put(PokemonMove.HYPER_FANG, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.LEAF_BLADE);
		metam.setType(PokemonType.GRASS);
		metam.setPower(55);
		metam.setAccuracy(1);
		metam.setCritChance(0.25);
		metam.setTime(2800);
		metam.setEnergy(-50);
		meta.put(PokemonMove.LEAF_BLADE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DISCHARGE);
		metam.setType(PokemonType.ELECTRIC);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2500);
		metam.setEnergy(-33);
		meta.put(PokemonMove.DISCHARGE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.WING_ATTACK_FAST);
		metam.setType(PokemonType.FLYING);
		metam.setPower(9);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(750);
		metam.setEnergy(7);
		meta.put(PokemonMove.WING_ATTACK_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.HEAT_WAVE);
		metam.setType(PokemonType.FIRE);
		metam.setPower(80);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3800);
		metam.setEnergy(-100);
		meta.put(PokemonMove.HEAT_WAVE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.HYDRO_PUMP);
		metam.setType(PokemonType.WATER);
		metam.setPower(90);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3800);
		metam.setEnergy(-100);
		meta.put(PokemonMove.HYDRO_PUMP, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.HYDRO_PUMP_BLASTOISE);
		metam.setType(PokemonType.WATER);
		metam.setPower(90);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3800);
		metam.setEnergy(-100);
		meta.put(PokemonMove.HYDRO_PUMP_BLASTOISE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.PETAL_BLIZZARD);
		metam.setType(PokemonType.GRASS);
		metam.setPower(65);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3200);
		metam.setEnergy(-50);
		meta.put(PokemonMove.PETAL_BLIZZARD, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BLIZZARD);
		metam.setType(PokemonType.ICE);
		metam.setPower(100);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3900);
		metam.setEnergy(-100);
		meta.put(PokemonMove.BLIZZARD, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.VINE_WHIP_FAST);
		metam.setType(PokemonType.GRASS);
		metam.setPower(7);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(650);
		metam.setEnergy(7);
		meta.put(PokemonMove.VINE_WHIP_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.THUNDER);
		metam.setType(PokemonType.ELECTRIC);
		metam.setPower(100);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4300);
		metam.setEnergy(-100);
		meta.put(PokemonMove.THUNDER, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.PSYSHOCK);
		metam.setType(PokemonType.PSYCHIC);
		metam.setPower(40);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2700);
		metam.setEnergy(-33);
		meta.put(PokemonMove.PSYSHOCK, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FROST_BREATH_FAST);
		metam.setType(PokemonType.ICE);
		metam.setPower(9);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(810);
		metam.setEnergy(7);
		meta.put(PokemonMove.FROST_BREATH_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.POUND_FAST);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(7);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(540);
		metam.setEnergy(7);
		meta.put(PokemonMove.POUND_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.MOONBLAST);
		metam.setType(PokemonType.FAIRY);
		metam.setPower(85);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4100);
		metam.setEnergy(-100);
		meta.put(PokemonMove.MOONBLAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FIRE_BLAST);
		metam.setType(PokemonType.FIRE);
		metam.setPower(100);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4100);
		metam.setEnergy(-100);
		meta.put(PokemonMove.FIRE_BLAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.EARTHQUAKE);
		metam.setType(PokemonType.GROUND);
		metam.setPower(100);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4200);
		metam.setEnergy(-100);
		meta.put(PokemonMove.EARTHQUAKE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SUBMISSION);
		metam.setType(PokemonType.FIGHTING);
		metam.setPower(30);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2100);
		metam.setEnergy(-33);
		meta.put(PokemonMove.SUBMISSION, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.X_SCISSOR);
		metam.setType(PokemonType.BUG);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2100);
		metam.setEnergy(-33);
		meta.put(PokemonMove.X_SCISSOR, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.POISON_JAB_FAST);
		metam.setType(PokemonType.POISON);
		metam.setPower(12);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1050);
		metam.setEnergy(10);
		meta.put(PokemonMove.POISON_JAB_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ZEN_HEADBUTT_FAST);
		metam.setType(PokemonType.PSYCHIC);
		metam.setPower(12);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1050);
		metam.setEnergy(9);
		meta.put(PokemonMove.ZEN_HEADBUTT_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FLASH_CANNON);
		metam.setType(PokemonType.STEEL);
		metam.setPower(60);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3900);
		metam.setEnergy(-33);
		meta.put(PokemonMove.FLASH_CANNON, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.HYPER_BEAM);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(120);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(5000);
		metam.setEnergy(-100);
		meta.put(PokemonMove.HYPER_BEAM, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DRAGON_PULSE);
		metam.setType(PokemonType.DRAGON);
		metam.setPower(65);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3600);
		metam.setEnergy(-50);
		meta.put(PokemonMove.DRAGON_PULSE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.POWER_GEM);
		metam.setType(PokemonType.ROCK);
		metam.setPower(40);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2900);
		metam.setEnergy(-33);
		meta.put(PokemonMove.POWER_GEM, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.PSYSTRIKE);
		metam.setType(PokemonType.PSYCHIC);
		metam.setPower(70);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(5100);
		metam.setEnergy(-100);
		meta.put(PokemonMove.PSYSTRIKE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ICE_BEAM);
		metam.setType(PokemonType.ICE);
		metam.setPower(65);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3650);
		metam.setEnergy(-50);
		meta.put(PokemonMove.ICE_BEAM, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.CROSS_POISON);
		metam.setType(PokemonType.POISON);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.25);
		metam.setTime(1500);
		metam.setEnergy(-25);
		meta.put(PokemonMove.CROSS_POISON, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BUG_BITE_FAST);
		metam.setType(PokemonType.BUG);
		metam.setPower(5);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(450);
		metam.setEnergy(7);
		meta.put(PokemonMove.BUG_BITE_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SOLAR_BEAM);
		metam.setType(PokemonType.GRASS);
		metam.setPower(120);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4900);
		metam.setEnergy(-100);
		meta.put(PokemonMove.SOLAR_BEAM, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SHADOW_BALL);
		metam.setType(PokemonType.GHOST);
		metam.setPower(45);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3080);
		metam.setEnergy(-33);
		meta.put(PokemonMove.SHADOW_BALL, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DARK_PULSE);
		metam.setType(PokemonType.DARK);
		metam.setPower(45);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3500);
		metam.setEnergy(-33);
		meta.put(PokemonMove.DARK_PULSE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ICE_PUNCH);
		metam.setType(PokemonType.ICE);
		metam.setPower(45);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3500);
		metam.setEnergy(-33);
		meta.put(PokemonMove.ICE_PUNCH, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SEED_BOMB);
		metam.setType(PokemonType.GRASS);
		metam.setPower(40);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2400);
		metam.setEnergy(-33);
		meta.put(PokemonMove.SEED_BOMB, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ROCK_SLIDE);
		metam.setType(PokemonType.ROCK);
		metam.setPower(40);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3200);
		metam.setEnergy(-33);
		meta.put(PokemonMove.ROCK_SLIDE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BONE_CLUB);
		metam.setType(PokemonType.GROUND);
		metam.setPower(20);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1600);
		metam.setEnergy(-25);
		meta.put(PokemonMove.BONE_CLUB, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FIRE_PUNCH);
		metam.setType(PokemonType.FIRE);
		metam.setPower(40);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2800);
		metam.setEnergy(-33);
		meta.put(PokemonMove.FIRE_PUNCH, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BITE_FAST);
		metam.setType(PokemonType.DARK);
		metam.setPower(6);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(500);
		metam.setEnergy(7);
		meta.put(PokemonMove.BITE_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DRAGON_BREATH_FAST);
		metam.setType(PokemonType.DRAGON);
		metam.setPower(6);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(500);
		metam.setEnergy(7);
		meta.put(PokemonMove.DRAGON_BREATH_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FLAME_BURST);
		metam.setType(PokemonType.FIRE);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2100);
		metam.setEnergy(-25);
		meta.put(PokemonMove.FLAME_BURST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.STOMP);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(30);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2100);
		metam.setEnergy(-25);
		meta.put(PokemonMove.STOMP, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DRILL_RUN);
		metam.setType(PokemonType.GROUND);
		metam.setPower(50);
		metam.setAccuracy(1);
		metam.setCritChance(0.25);
		metam.setTime(3400);
		metam.setEnergy(-33);
		meta.put(PokemonMove.DRILL_RUN, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BUG_BUZZ);
		metam.setType(PokemonType.BUG);
		metam.setPower(75);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4250);
		metam.setEnergy(-50);
		meta.put(PokemonMove.BUG_BUZZ, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FEINT_ATTACK_FAST);
		metam.setType(PokemonType.DARK);
		metam.setPower(12);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1040);
		metam.setEnergy(10);
		meta.put(PokemonMove.FEINT_ATTACK_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SIGNAL_BEAM);
		metam.setType(PokemonType.BUG);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3100);
		metam.setEnergy(-33);
		meta.put(PokemonMove.SIGNAL_BEAM, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.REST);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3100);
		metam.setEnergy(-33);
		meta.put(PokemonMove.REST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.STEEL_WING_FAST);
		metam.setType(PokemonType.STEEL);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1330);
		metam.setEnergy(12);
		meta.put(PokemonMove.STEEL_WING_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DRILL_PECK);
		metam.setType(PokemonType.FLYING);
		metam.setPower(40);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2700);
		metam.setEnergy(-33);
		meta.put(PokemonMove.DRILL_PECK, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.LOW_SWEEP);
		metam.setType(PokemonType.FIGHTING);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2250);
		metam.setEnergy(-25);
		meta.put(PokemonMove.LOW_SWEEP, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.TACKLE_FAST);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(12);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1100);
		metam.setEnergy(10);
		meta.put(PokemonMove.TACKLE_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DAZZLING_GLEAM);
		metam.setType(PokemonType.FAIRY);
		metam.setPower(55);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4200);
		metam.setEnergy(-33);
		meta.put(PokemonMove.DAZZLING_GLEAM, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.CUT_FAST);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(12);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1130);
		metam.setEnergy(10);
		meta.put(PokemonMove.CUT_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.POISON_STING_FAST);
		metam.setType(PokemonType.POISON);
		metam.setPower(6);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(575);
		metam.setEnergy(8);
		meta.put(PokemonMove.POISON_STING_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.RAZOR_LEAF_FAST);
		metam.setType(PokemonType.GRASS);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1450);
		metam.setEnergy(12);
		meta.put(PokemonMove.RAZOR_LEAF_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SUCKER_PUNCH_FAST);
		metam.setType(PokemonType.DARK);
		metam.setPower(7);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(700);
		metam.setEnergy(9);
		meta.put(PokemonMove.SUCKER_PUNCH_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SPARK_FAST);
		metam.setType(PokemonType.ELECTRIC);
		metam.setPower(7);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(700);
		metam.setEnergy(8);
		meta.put(PokemonMove.SPARK_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.GIGA_DRAIN);
		metam.setType(PokemonType.GRASS);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3600);
		metam.setEnergy(-33);
		meta.put(PokemonMove.GIGA_DRAIN, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SLUDGE);
		metam.setType(PokemonType.POISON);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2600);
		metam.setEnergy(-25);
		meta.put(PokemonMove.SLUDGE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.MUD_BOMB);
		metam.setType(PokemonType.GROUND);
		metam.setPower(30);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2600);
		metam.setEnergy(-25);
		meta.put(PokemonMove.MUD_BOMB, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SHADOW_PUNCH);
		metam.setType(PokemonType.GHOST);
		metam.setPower(20);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2100);
		metam.setEnergy(-25);
		meta.put(PokemonMove.SHADOW_PUNCH, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.EMBER_FAST);
		metam.setType(PokemonType.FIRE);
		metam.setPower(10);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1050);
		metam.setEnergy(10);
		meta.put(PokemonMove.EMBER_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ACID_FAST);
		metam.setType(PokemonType.POISON);
		metam.setPower(10);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1050);
		metam.setEnergy(10);
		meta.put(PokemonMove.ACID_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.NIGHT_SLASH);
		metam.setType(PokemonType.DARK);
		metam.setPower(30);
		metam.setAccuracy(1);
		metam.setCritChance(0.25);
		metam.setTime(2700);
		metam.setEnergy(-25);
		meta.put(PokemonMove.NIGHT_SLASH, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.PSYBEAM);
		metam.setType(PokemonType.PSYCHIC);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3800);
		metam.setEnergy(-25);
		meta.put(PokemonMove.PSYBEAM, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.WATER_PULSE);
		metam.setType(PokemonType.WATER);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3300);
		metam.setEnergy(-25);
		meta.put(PokemonMove.WATER_PULSE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.HORN_ATTACK);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(20);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2200);
		metam.setEnergy(-25);
		meta.put(PokemonMove.HORN_ATTACK, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.MAGNET_BOMB);
		metam.setType(PokemonType.STEEL);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2800);
		metam.setEnergy(-25);
		meta.put(PokemonMove.MAGNET_BOMB, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.STRUGGLE);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1695);
		metam.setEnergy(-20);
		meta.put(PokemonMove.STRUGGLE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BULLDOZE);
		metam.setType(PokemonType.GROUND);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3400);
		metam.setEnergy(-25);
		meta.put(PokemonMove.BULLDOZE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ROCK_THROW_FAST);
		metam.setType(PokemonType.ROCK);
		metam.setPower(12);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1360);
		metam.setEnergy(15);
		meta.put(PokemonMove.ROCK_THROW_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SCALD);
		metam.setType(PokemonType.WATER);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4000);
		metam.setEnergy(-33);
		meta.put(PokemonMove.SCALD, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SCALD_BLASTOISE);
		metam.setType(PokemonType.WATER);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4000);
		metam.setEnergy(-33);
		meta.put(PokemonMove.SCALD_BLASTOISE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.PECK_FAST);
		metam.setType(PokemonType.FLYING);
		metam.setPower(10);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1150);
		metam.setEnergy(10);
		meta.put(PokemonMove.PECK_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.AERIAL_ACE);
		metam.setType(PokemonType.FLYING);
		metam.setPower(30);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2900);
		metam.setEnergy(-25);
		meta.put(PokemonMove.AERIAL_ACE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BUBBLE_BEAM);
		metam.setType(PokemonType.WATER);
		metam.setPower(30);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2900);
		metam.setEnergy(-25);
		meta.put(PokemonMove.BUBBLE_BEAM, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ANCIENT_POWER);
		metam.setType(PokemonType.ROCK);
		metam.setPower(35);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3600);
		metam.setEnergy(-25);
		meta.put(PokemonMove.ANCIENT_POWER, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BRINE);
		metam.setType(PokemonType.WATER);
		metam.setPower(20);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2400);
		metam.setEnergy(-25);
		meta.put(PokemonMove.BRINE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SWIFT);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3000);
		metam.setEnergy(-25);
		meta.put(PokemonMove.SWIFT, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.THUNDER_SHOCK_FAST);
		metam.setType(PokemonType.ELECTRIC);
		metam.setPower(5);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(600);
		metam.setEnergy(8);
		meta.put(PokemonMove.THUNDER_SHOCK_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.LOW_KICK_FAST);
		metam.setType(PokemonType.FIGHTING);
		metam.setPower(5);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(600);
		metam.setEnergy(7);
		meta.put(PokemonMove.LOW_KICK_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BULLET_PUNCH_FAST);
		metam.setType(PokemonType.STEEL);
		metam.setPower(10);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1200);
		metam.setEnergy(10);
		meta.put(PokemonMove.BULLET_PUNCH_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FIRE_FANG_FAST);
		metam.setType(PokemonType.FIRE);
		metam.setPower(10);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(840);
		metam.setEnergy(8);
		meta.put(PokemonMove.FIRE_FANG_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SPLASH_FAST);
		metam.setType(PokemonType.WATER);
		metam.setPower(10);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1230);
		metam.setEnergy(10);
		meta.put(PokemonMove.SPLASH_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.OMINOUS_WIND);
		metam.setType(PokemonType.GHOST);
		metam.setPower(30);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3100);
		metam.setEnergy(-25);
		meta.put(PokemonMove.OMINOUS_WIND, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.CONFUSION_FAST);
		metam.setType(PokemonType.PSYCHIC);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1510);
		metam.setEnergy(14);
		meta.put(PokemonMove.CONFUSION_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.HEART_STAMP);
		metam.setType(PokemonType.PSYCHIC);
		metam.setPower(20);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2550);
		metam.setEnergy(-25);
		meta.put(PokemonMove.HEART_STAMP, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DIG);
		metam.setType(PokemonType.GROUND);
		metam.setPower(70);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(5800);
		metam.setEnergy(-33);
		meta.put(PokemonMove.DIG, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FLAME_WHEEL);
		metam.setType(PokemonType.FIRE);
		metam.setPower(40);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4600);
		metam.setEnergy(-25);
		meta.put(PokemonMove.FLAME_WHEEL, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.AIR_CUTTER);
		metam.setType(PokemonType.FLYING);
		metam.setPower(30);
		metam.setAccuracy(1);
		metam.setCritChance(0.25);
		metam.setTime(3300);
		metam.setEnergy(-25);
		meta.put(PokemonMove.AIR_CUTTER, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.QUICK_ATTACK_FAST);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(10);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1330);
		metam.setEnergy(12);
		meta.put(PokemonMove.QUICK_ATTACK_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FURY_CUTTER_FAST);
		metam.setType(PokemonType.BUG);
		metam.setPower(3);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(400);
		metam.setEnergy(6);
		meta.put(PokemonMove.FURY_CUTTER_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.KARATE_CHOP_FAST);
		metam.setType(PokemonType.FIGHTING);
		metam.setPower(6);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(800);
		metam.setEnergy(8);
		meta.put(PokemonMove.KARATE_CHOP_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ROCK_TOMB);
		metam.setType(PokemonType.ROCK);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.25);
		metam.setTime(3400);
		metam.setEnergy(-25);
		meta.put(PokemonMove.ROCK_TOMB, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ICE_SHARD_FAST);
		metam.setType(PokemonType.ICE);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1400);
		metam.setEnergy(12);
		meta.put(PokemonMove.ICE_SHARD_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.VICE_GRIP);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2100);
		metam.setEnergy(-20);
		meta.put(PokemonMove.VICE_GRIP, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.PARABOLIC_CHARGE);
		metam.setType(PokemonType.ELECTRIC);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2100);
		metam.setEnergy(-20);
		meta.put(PokemonMove.PARABOLIC_CHARGE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.BUBBLE_FAST);
		metam.setType(PokemonType.WATER);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2300);
		metam.setEnergy(25);
		meta.put(PokemonMove.BUBBLE_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.FLAME_CHARGE);
		metam.setType(PokemonType.FIRE);
		metam.setPower(20);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3100);
		metam.setEnergy(-20);
		meta.put(PokemonMove.FLAME_CHARGE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.AQUA_JET);
		metam.setType(PokemonType.WATER);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2350);
		metam.setEnergy(-20);
		meta.put(PokemonMove.AQUA_JET, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.POISON_FANG);
		metam.setType(PokemonType.POISON);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2400);
		metam.setEnergy(-20);
		meta.put(PokemonMove.POISON_FANG, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.TWISTER);
		metam.setType(PokemonType.DRAGON);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2700);
		metam.setEnergy(-20);
		meta.put(PokemonMove.TWISTER, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DRAINING_KISS);
		metam.setType(PokemonType.FAIRY);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(2800);
		metam.setEnergy(-20);
		meta.put(PokemonMove.DRAINING_KISS, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.DISARMING_VOICE);
		metam.setType(PokemonType.FAIRY);
		metam.setPower(20);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3900);
		metam.setEnergy(-20);
		meta.put(PokemonMove.DISARMING_VOICE, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.SHADOW_SNEAK);
		metam.setType(PokemonType.GHOST);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3100);
		metam.setEnergy(-20);
		meta.put(PokemonMove.SHADOW_SNEAK, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.MEGA_DRAIN);
		metam.setType(PokemonType.GRASS);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3200);
		metam.setEnergy(-20);
		meta.put(PokemonMove.MEGA_DRAIN, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.MUD_SLAP_FAST);
		metam.setType(PokemonType.GROUND);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1350);
		metam.setEnergy(12);
		meta.put(PokemonMove.MUD_SLAP_FAST, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.WRAP_GREEN);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3700);
		metam.setEnergy(-20);
		meta.put(PokemonMove.WRAP_GREEN, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.WRAP_PINK);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3700);
		metam.setEnergy(-20);
		meta.put(PokemonMove.WRAP_PINK, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ICY_WIND);
		metam.setType(PokemonType.ICE);
		metam.setPower(25);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(3800);
		metam.setEnergy(-20);
		meta.put(PokemonMove.ICY_WIND, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.WRAP);
		metam.setType(PokemonType.NORMAL);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(4000);
		metam.setEnergy(-20);
		meta.put(PokemonMove.WRAP, metam);

		metam = new PokemonMoveMeta();
		metam.setMove(PokemonMove.ROCK_SMASH_FAST);
		metam.setType(PokemonType.FIGHTING);
		metam.setPower(15);
		metam.setAccuracy(1);
		metam.setCritChance(0.05);
		metam.setTime(1410);
		metam.setEnergy(12);
		meta.put(PokemonMove.ROCK_SMASH_FAST, metam);

	}

	/**
	 * Return PokemonMoveMeta object containing meta info about a pokemon move.
	 *
	 * @param id
	 *            the id of the pokemon move
	 * @return PokemonMoveMeta
	 */
	public static PokemonMoveMeta getMeta(PokemonMove id) {
		return meta.get(id);
	}

}
