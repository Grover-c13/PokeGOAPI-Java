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

import POGOProtos.Data.BuddyPokemonOuterClass.BuddyPokemon;
import com.pokegoapi.api.PokemonGo;

public class Buddy {
	private final PokemonGo api;
	private long id;
	private double lastKMAwarded;
	private double startKM;
	private Pokemon pokemon;
	private double buddyDistance;

	/**
	 * Creates a buddy object
	 * @param api the current api
	 * @param proto the buddy proto
	 */
	public Buddy(PokemonGo api, BuddyPokemon proto) {
		this.api = api;
		this.id = proto.getId();
		this.lastKMAwarded = proto.getLastKmAwarded();
		this.startKM = proto.getStartKmWalked();
	}

	/**
	 * @return the pokemon object of this buddy
	 */
	public Pokemon getPokemon() {
		if (pokemon == null) {
			pokemon = api.getInventories().getPokebank().getPokemonById(this.id);
			buddyDistance = PokemonMetaRegistry.getMeta(pokemon.getPokemonId()).getBuddyDistance();
		}
		return pokemon;
	}

	/**
	 * @return the total distance this type of buddy pokemon needs to walk per candy
	 */
	public double getBuddyDistance() {
		if (pokemon == null) {
			getPokemon();
		}
		return buddyDistance;
	}

	/**
	 * @return the last walk distance when a candy was received
	 */
	public double getLastReceiveKM() {
		return lastKMAwarded;
	}

	/**
	 * @return the distance when the distance started progressing
	 */
	public double getStartKM() {
		return startKM;
	}

	/**
	 * @return the target distance walked for this buddy's next candy
	 */
	public double getTargetKM() {
		return getStartKM() + buddyDistance;
	}

	/**
	 * @return the current buddy walk progress, from 0-buddyDistance
	 */
	public double getProgressKM() {
		return getTargetKM() - api.getPlayerProfile().getStats().getKmWalked();
	}
}
