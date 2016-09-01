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

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.EggIncubator;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Networking.Responses.UseItemEggIncubatorResponseOuterClass.UseItemEggIncubatorResponse;
import com.pokegoapi.util.PokeCallback;
import lombok.Setter;

/**
 * The egg pokemon.
 */
public class EggPokemon {

	private static final String TAG = EggPokemon.class.getSimpleName();
	@Setter
	PokemonGo api;
	private PokemonData proto;

	// API METHODS //

	/**
	 * Incubate this egg.
	 *
	 * @param incubator : the incubator
	 * @param callback an optional callback to handle results
	 */
	public void incubate(EggIncubator incubator, PokeCallback<UseItemEggIncubatorResponse> callback) {
		if (incubator.isInUse()) {
			throw new IllegalArgumentException("Incubator already used");
		}
		incubator.hatchEgg(this, callback);
	}

	/**
	 * Get the current distance that has been done with this egg
	 *
	 * @return get distance already walked
	 */
	public double getEggKmWalked() {
		if (!isIncubate())
			return 0;
		EggIncubator incubator = api.getInventories().getIncubators().get(proto.getEggIncubatorId());
		// incubator should not be null but why not eh
		if (incubator == null)
			return 0;
		else
			return proto.getEggKmWalkedTarget()
					- (incubator.getKmTarget() - api.getPlayerProfile().getStats().getKmWalked());
	}

	/**
	 * Build a EggPokemon wrapper from the proto.
	 *
	 * @param proto : the prototype
	 */
	public EggPokemon(PokemonData proto) {
		if (!proto.getIsEgg()) {
			throw new IllegalArgumentException("You cant build a EggPokemon without a valid PokemonData.");
		}
		this.proto = proto;
	}

	public long getId() {
		return proto.getId();
	}

	public double getEggKmWalkedTarget() {
		return proto.getEggKmWalkedTarget();
	}

	public long getCapturedCellId() {
		return proto.getCapturedCellId();
	}

	public long getCreationTimeMs() {
		return proto.getCreationTimeMs();
	}

	public String getEggIncubatorId() {
		return proto.getEggIncubatorId();
	}

	public boolean isIncubate() {
		return proto.getEggIncubatorId().length() > 0;
	}

	@Override
	public int hashCode() {
		return proto.getPokemonId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EggPokemon) {
			EggPokemon other = (EggPokemon) obj;
			return (this.getId() == other.getId());
		}

		return false;
	}
}
