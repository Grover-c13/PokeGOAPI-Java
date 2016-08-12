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

package com.pokegoapi.api.map.pokemon;

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Map.Pokemon.NearbyPokemonOuterClass;

public class NearbyPokemon {
	private NearbyPokemonOuterClass.NearbyPokemon proto;

	public NearbyPokemon(NearbyPokemonOuterClass.NearbyPokemon proto) {
		this.proto = proto;
	}

	public PokemonIdOuterClass.PokemonId getPokemonId() {
		return proto.getPokemonId();
	}

	public float getDistanceInMeters() {
		return proto.getDistanceInMeters();
	}

	public long getEncounterId() {
		return proto.getEncounterId();
	}

	public String getFortId() {
		return proto.getFortId();
	}

	public String getFortImageUrl() {
		return proto.getFortImageUrl();
	}
}
