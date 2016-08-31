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

import POGOProtos.Networking.Requests.Messages.GetHatchedEggsMessageOuterClass.GetHatchedEggsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.api.pokemon.HatchedEgg;
import com.pokegoapi.exceptions.AsyncRemoteServerException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.util.AsyncHelper;
import lombok.Getter;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Hatchery {
	@Getter
	private final Set<EggPokemon> eggs = Collections.newSetFromMap(new ConcurrentHashMap<EggPokemon, Boolean>());

	@Getter
	private PokemonGo api;

	public Hatchery(PokemonGo api) {
		this.api = api;
	}

	public void addEgg(EggPokemon egg) {
		egg.setApi(api);
		eggs.add(egg);
	}

	/**
	 * Get if eggs has hatched.
	 *
	 * @return list of hatched eggs
	 * @throws RemoteServerException e
	 * @throws LoginFailedException  e
	 */
	public List<HatchedEgg> queryHatchedEggs() throws RemoteServerException, LoginFailedException {
		GetHatchedEggsMessage msg = GetHatchedEggsMessage.newBuilder().build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.GET_HATCHED_EGGS, msg, api);
		return AsyncHelper.toBlocking(
				api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, List<HatchedEgg>>() {

					@Override
					public List<HatchedEgg> call(ByteString bytes) {
						GetHatchedEggsResponse response;
						try {
							response = GetHatchedEggsResponse.parseFrom(bytes);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);

						}
						List<HatchedEgg> eggs = new ArrayList<HatchedEgg>();
						for (int i = 0; i < response.getPokemonIdCount(); i++) {
							eggs.add(new HatchedEgg(response.getPokemonId(i),
									response.getExperienceAwarded(i),
									response.getCandyAwarded(i),
									response.getStardustAwarded(i)));
						}
						return eggs;
					}
				})
		);

	}

	public void setEggs(List<EggPokemon> eggs) {
		synchronized (eggs) {
			eggs.clear();
			eggs.addAll(eggs);
		}
	}
}
