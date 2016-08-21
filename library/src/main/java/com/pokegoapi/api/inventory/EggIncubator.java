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

package com.pokegoapi.api.inventory;

import POGOProtos.Inventory.EggIncubatorOuterClass;
import POGOProtos.Inventory.EggIncubatorTypeOuterClass.EggIncubatorType;
import POGOProtos.Networking.Requests.Messages.UseItemEggIncubatorMessageOuterClass.UseItemEggIncubatorMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.UseItemEggIncubatorResponseOuterClass;
import POGOProtos.Networking.Responses.UseItemEggIncubatorResponseOuterClass.UseItemEggIncubatorResponse;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import rx.Observable;
import rx.functions.Func1;

public class EggIncubator {
	private final Networking networking;
	private final Inventories inventories;
	private final EggIncubatorOuterClass.EggIncubator proto;

	/**
	 * Create new EggIncubator with given proto.
	 *
	 * @param proto the proto
	 */
	EggIncubator(Networking networking, Inventories inventories, EggIncubatorOuterClass.EggIncubator proto) {
		this.proto = proto;
		this.networking = networking;
		this.inventories = inventories;
	}

	/**
	 * Returns the remaining uses.
	 *
	 * @return uses remaining
	 */
	public int getUsesRemaining() {
		return proto.getUsesRemaining();
	}

	/**
	 * Hatch an egg.
	 *
	 * @param egg the egg
	 * @return status of putting egg in incubator
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	public Observable<UseItemEggIncubatorResponse.Result> hatchEgg(EggPokemon egg) {
		return networking.queueRequest(RequestType.USE_ITEM_EGG_INCUBATOR,
				UseItemEggIncubatorMessage.
						newBuilder()
						.setItemId(proto.getId())
						.setPokemonId(egg.getId())
						.build(),
				UseItemEggIncubatorResponse.class).map(new Func1<UseItemEggIncubatorResponse, UseItemEggIncubatorResponse.Result>() {
			@Override
			public UseItemEggIncubatorResponse.Result call(UseItemEggIncubatorResponse response) {
				return response.getResult();
			}
		});
	}

	/**
	 * Get incubator id.
	 *
	 * @return the id
	 */
	public String getId() {
		return proto.getId();
	}

	/**
	 * Get incubator type.
	 *
	 * @return EggIncubatorType
	 */
	public EggIncubatorType getType() {
		return proto.getIncubatorType();
	}

	/**
	 * Get the total distance you need to walk to hatch the current egg.
	 *
	 * @return total distance to walk to hatch the egg (km)
	 */
	public double getKmTarget() {
		return proto.getTargetKmWalked();
	}

	/**
	 * Get the distance walked before the current egg was incubated.
	 *
	 * @return distance to walked before incubating egg
	 * @deprecated Wrong method name, use {@link #getKmStart()}
	 */
	public double getKmWalked() {
		return getKmStart();
	}

	/**
	 * Get the distance walked before the current egg was incubated.
	 *
	 * @return distance walked before incubating egg (km)
	 */
	public double getKmStart() {
		return proto.getStartKmWalked();
	}

	/**
	 * Gets the total distance to walk with the current egg before hatching.
	 *
	 * @return total km between incubation and hatching
	 */
	public double getHatchDistance() {
		return getKmTarget() - getKmStart();
	}

	/**
	 * Get the distance walked with the current incubated egg.
	 *
	 * @return distance walked with the current incubated egg (km)
	 */
	public double getKmCurrentlyWalked() {
		return inventories.getStats().getKmWalked() - getKmStart();
	}

	/**
	 * Get the distance left to walk before this incubated egg will hatch.
	 *
	 * @return distance to walk before hatch (km)
	 */
	public double getKmLeftToWalk() throws LoginFailedException, RemoteServerException {
		return getKmTarget() - inventories.getStats().getKmWalked();
	}

	/**
	 * Is the incubator currently being used
	 *
	 * @return currently used or not
	 */
	public boolean isInUse() throws LoginFailedException, RemoteServerException {
		return getKmTarget() > inventories.getStats().getKmWalked();
	}
}
