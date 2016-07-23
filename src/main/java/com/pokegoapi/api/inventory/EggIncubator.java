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
import POGOProtos.Networking.Requests.Messages.UseItemEggIncubatorMessageOuterClass.UseItemEggIncubatorMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.UseItemEggIncubatorResponseOuterClass.UseItemEggIncubatorResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

public class EggIncubator {
	private final EggIncubatorOuterClass.EggIncubator proto;
	private final PokemonGo pgo;
	@Getter
	private boolean inUse = false;

	public EggIncubator(PokemonGo pgo, EggIncubatorOuterClass.EggIncubator proto) {
		this.pgo = pgo;
		this.proto = proto;
		this.inUse = proto.getPokemonId() != 0;
	}

	public int getUsesRemaining() {
		return proto.getUsesRemaining();
	}

	public UseItemEggIncubatorResponse.Result hatchEgg(Pokemon pokemon) throws LoginFailedException, RemoteServerException {
		if (!pokemon.getIsEgg()) {
			return null;
		}
		UseItemEggIncubatorMessage reqMsg = UseItemEggIncubatorMessage.newBuilder()
				.setItemId(proto.getId())
				.setPokemonId(pokemon.getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.USE_ITEM_EGG_INCUBATOR, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		UseItemEggIncubatorResponse response;
		try {
			response = UseItemEggIncubatorResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		pgo.getInventories().getEggs().remove(pokemon);
		pgo.getInventories().updateInventories(true);

		this.inUse = true;

		return response.getResult();
	}
}
