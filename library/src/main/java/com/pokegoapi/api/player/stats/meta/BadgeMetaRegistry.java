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

package com.pokegoapi.api.player.stats.meta;

import java.util.EnumMap;

import POGOProtos.Enums.BadgeTypeOuterClass.BadgeType;

/**
 * The Badge/Medal/Achievement registry.
 * 
 * @author gionata-bisciari
 *
 */
public class BadgeMetaRegistry {

	private static EnumMap<BadgeType, BadgeMeta> meta = new EnumMap<>(BadgeType.class);

	static {

		meta.put(BadgeType.BADGE_TRAVEL_KM,
				new BadgeMeta(BadgeType.BADGE_TRAVEL_KM, "Jogger", "Amount of kilometers walked", 10, 100, 1000));

		meta.put(BadgeType.BADGE_POKEDEX_ENTRIES, new BadgeMeta(BadgeType.BADGE_POKEDEX_ENTRIES, "Kanto",
				"Amount of pokemons registered in the pokedex", 5, 50, 100));

		meta.put(BadgeType.BADGE_CAPTURE_TOTAL,
				new BadgeMeta(BadgeType.BADGE_CAPTURE_TOTAL, "Collector", "Amount of pokemons caught", 30, 500, 2000));

		meta.put(BadgeType.BADGE_EVOLVED_TOTAL,
				new BadgeMeta(BadgeType.BADGE_EVOLVED_TOTAL, "Scientist", "Amount of pokemons evolved", 3, 20, 200));

		meta.put(BadgeType.BADGE_HATCHED_TOTAL,
				new BadgeMeta(BadgeType.BADGE_HATCHED_TOTAL, "Breeder", "Amount of eggs hatched", 10, 100, 500));

		meta.put(BadgeType.BADGE_POKESTOPS_VISITED, new BadgeMeta(BadgeType.BADGE_POKESTOPS_VISITED, "Backpacker",
				"Amount of pokestops visited", 100, 1000, 2000));

		meta.put(BadgeType.BADGE_BIG_MAGIKARP,
				new BadgeMeta(BadgeType.BADGE_BIG_MAGIKARP, "Fisherman", "Amount of big magikarp caught", 3, 50, 300));

		meta.put(BadgeType.BADGE_BATTLE_ATTACK_WON, new BadgeMeta(BadgeType.BADGE_BATTLE_ATTACK_WON, "Battle Girl",
				"Total attacks won VS enemy team", 10, 100, 1000));

		meta.put(BadgeType.BADGE_BATTLE_TRAINING_WON, new BadgeMeta(BadgeType.BADGE_BATTLE_TRAINING_WON, "Ace Trainer",
				"Total attacks won VS your team", 10, 100, 1000));

		meta.put(BadgeType.BADGE_TYPE_NORMAL, new BadgeMeta(BadgeType.BADGE_TYPE_NORMAL, "Schoolkid",
				"Amount of normal-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_FIGHTING, new BadgeMeta(BadgeType.BADGE_TYPE_FIGHTING, "Black Belt",
				"Amount of fighting-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_FLYING, new BadgeMeta(BadgeType.BADGE_TYPE_FLYING, "Bird Keeper",
				"Amount of flying-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_POISON, new BadgeMeta(BadgeType.BADGE_TYPE_POISON, "Punk Girl",
				"Amount of poison-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_GROUND, new BadgeMeta(BadgeType.BADGE_TYPE_GROUND, "Ruin Maniac",
				"Amount of ground-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_ROCK,
				new BadgeMeta(BadgeType.BADGE_TYPE_ROCK, "Hiker", "Amount of rock-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_BUG, new BadgeMeta(BadgeType.BADGE_TYPE_BUG, "Bug Catcher",
				"Amount of bug-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_GHOST, new BadgeMeta(BadgeType.BADGE_TYPE_GHOST, "Hex Maniac",
				"Amount of ghost-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_STEEL, new BadgeMeta(BadgeType.BADGE_TYPE_STEEL, "Depot Agent",
				"Amount of steel-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_FIRE, new BadgeMeta(BadgeType.BADGE_TYPE_FIRE, "Kindler",
				"Amount of fire-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_WATER, new BadgeMeta(BadgeType.BADGE_TYPE_WATER, "Swimmer",
				"Amount of water-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_GRASS, new BadgeMeta(BadgeType.BADGE_TYPE_GRASS, "Gardener",
				"Amount of grass-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_ELECTRIC, new BadgeMeta(BadgeType.BADGE_TYPE_ELECTRIC, "Rocker",
				"Amount of electric-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_PSYCHIC, new BadgeMeta(BadgeType.BADGE_TYPE_PSYCHIC, "Psychic",
				"Amount of psychic-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_DRAGON, new BadgeMeta(BadgeType.BADGE_TYPE_DRAGON, "Dragon Tamer",
				"Amount of dragon-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_FAIRY, new BadgeMeta(BadgeType.BADGE_TYPE_FAIRY, "Fairy Tale Girl",
				"Amount of fairy-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_TYPE_DARK, new BadgeMeta(BadgeType.BADGE_TYPE_DARK, "Delinquent",
				"Amount of dark-type pokemons caught", 10, 50, 200));

		meta.put(BadgeType.BADGE_SMALL_RATTATA, new BadgeMeta(BadgeType.BADGE_SMALL_RATTATA, "Youngster",
				"Amount of XS-weight rattatas caught", 3, 50, 300));

		meta.put(BadgeType.BADGE_PIKACHU,
				new BadgeMeta(BadgeType.BADGE_PIKACHU, "Pikachu Fan", "Amount of pikachu caught", 3, 50, 300));
		// ?
		meta.put(BadgeType.BADGE_DEFEATED_FORT,
				new BadgeMeta(BadgeType.BADGE_DEFEATED_FORT, "Defeated Forts", "Amount of forts defeated"));

		meta.put(BadgeType.BADGE_ENCOUNTERED_TOTAL,
				new BadgeMeta(BadgeType.BADGE_ENCOUNTERED_TOTAL, "Encountered", "Amount of pokemons encountered"));

		meta.put(BadgeType.BADGE_UNIQUE_POKESTOPS,
				new BadgeMeta(BadgeType.BADGE_UNIQUE_POKESTOPS, "Unique Pokestops", "Unique pokestop visits"));

		meta.put(BadgeType.BADGE_DEPLOYED_TOTAL,
				new BadgeMeta(BadgeType.BADGE_DEPLOYED_TOTAL, "Deployed Pokemons", "Total pokemons deployed"));

		meta.put(BadgeType.BADGE_BATTLE_DEFEND_WON, new BadgeMeta(BadgeType.BADGE_BATTLE_DEFEND_WON, "Gyms Defended",
				"Amount of battles won as a defender"));

		meta.put(BadgeType.BADGE_PRESTIGE_RAISED,
				new BadgeMeta(BadgeType.BADGE_PRESTIGE_RAISED, "Gym Prestige Raised", "Total prestige raised in gyms"));

		meta.put(BadgeType.BADGE_PRESTIGE_DROPPED, new BadgeMeta(BadgeType.BADGE_PRESTIGE_DROPPED,
				"Gym Prestige Dropped", "Total prestige taken down in gyms"));

	}

	/**
	 * Return BadgeMeta object containing meta info about a badge.
	 *
	 * @param badgeType
	 *            The badge type.
	 * @return The badge meta.
	 */
	public static BadgeMeta getMeta(BadgeType badgeType) {
		return meta.get(badgeType);
	}
}