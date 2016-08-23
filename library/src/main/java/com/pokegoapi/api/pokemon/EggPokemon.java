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

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Networking.Responses.UseItemEggIncubatorResponseOuterClass.UseItemEggIncubatorResponse;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.pokegoapi.api.inventory.EggIncubator;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import rx.Observable;

/**
 * The egg pokemon.
 */
public class EggPokemon {

	private static final String TAG = EggPokemon.class.getSimpleName();
	private final PokemonData proto;
	private final Inventories inventories;

	public EggPokemon(PokemonData proto, Inventories inventories) {
		this.proto = proto;
		this.inventories = inventories;
	}

	/**
	 * Incubate this egg.
	 *
	 * @param incubator : the incubator
	 * @return status of putting egg in incubator
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public Observable<UseItemEggIncubatorResponse.Result> incubate(EggIncubator incubator) {
		if (incubator.isInUse()) {
			throw new IllegalArgumentException("Incubator already used");
		}
		return incubator.hatchEgg(this);
	}

	/**
	 * Get the current distance that has been done with this egg
	 *
	 * @return get distance already walked
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public double getEggKmWalked() throws LoginFailedException, RemoteServerException {
		if (!isIncubate())
			return 0;

		EggIncubator incubator = Stream.of(inventories.getIncubators())
				.filter(new Predicate<EggIncubator>() {
					@Override
					public boolean test(EggIncubator incub) {
						return incub.getId().equals(proto.getEggIncubatorId());
					}
				}).findFirst().orElse(null);
		// incubator should not be null but why not eh
		if (incubator == null) {
			return 0;
		}
		else
			return proto.getEggKmWalkedTarget()
					- (incubator.getKmTarget() - inventories.getStats().getKmWalked());
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
	// TODO: add wrapper objects for encubators and allow to be got.
}
