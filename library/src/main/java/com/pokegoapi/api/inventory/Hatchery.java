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
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.listener.PokemonListener;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.api.pokemon.HatchedEgg;
import com.pokegoapi.exceptions.CaptchaActiveException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hatchery {
	@Getter
	private final Set<EggPokemon> eggs = Collections.synchronizedSet(new HashSet<EggPokemon>());
	@Getter
	private final Set<HatchedEgg> hatchedEggs = Collections.synchronizedSet(new HashSet<HatchedEgg>());
	@Getter
	private PokemonGo api;

	private final Object lock = new Object();

	public Hatchery(PokemonGo api) {
		this.api = api;
	}

	public void reset() {
		synchronized (this.lock) {
			eggs.clear();
			hatchedEggs.clear();
		}
	}

	public void addEgg(EggPokemon egg) {
		egg.setApi(api);
		synchronized (this.lock) {
			eggs.add(egg);
		}
	}

	/**
	 * Adds the given hatched egg to the hatchedEggs set.
	 * @param egg the egg to add
	 */
	public void addHatchedEgg(HatchedEgg egg) {
		synchronized (this.lock) {
			hatchedEggs.add(egg);
		}
		boolean remove = false;
		List<PokemonListener> listeners = api.getListeners(PokemonListener.class);
		for (PokemonListener listener : listeners) {
			remove |= listener.onEggHatch(api, egg);
		}
		if (remove) {
			removeHatchedEgg(egg);
		}
	}

	/**
	 * Removes the given egg from the hatchedEggs set.
	 * @param egg the egg to remove
	 */
	public void removeHatchedEgg(HatchedEgg egg) {
		synchronized (this.lock) {
			hatchedEggs.remove(egg);
		}
	}

	/**
	 * Adds the hatched eggs obtained from the given GetHatchedEggs response
	 * @param response the GetHatchedEggs response
	 * @return the hatched eggs contained in the response
	 */
	public List<HatchedEgg> updateHatchedEggs(GetHatchedEggsResponse response) {
		List<HatchedEgg> eggs = new ArrayList<HatchedEgg>();
		for (int i = 0; i < response.getPokemonIdCount(); i++) {
			HatchedEgg egg = new HatchedEgg(response.getPokemonId(i),
					response.getExperienceAwarded(i),
					response.getCandyAwarded(i),
					response.getStardustAwarded(i));
			eggs.add(egg);
			addHatchedEgg(egg);
		}
		return eggs;
	}

	/**
	 * Get if eggs has hatched.
	 *
	 * @return list of hatched eggs
	 * @throws RemoteServerException e
	 * @throws LoginFailedException  e
	 * @throws CaptchaActiveException if a captcha is active and the message can't be sent
	 *
	 * @deprecated Use getHatchedEggs()
	 */
	@Deprecated
	public List<HatchedEgg> queryHatchedEggs()
			throws RemoteServerException, CaptchaActiveException, LoginFailedException {
		GetHatchedEggsMessage msg = GetHatchedEggsMessage.newBuilder().build();
		ServerRequest serverRequest = new ServerRequest(RequestType.GET_HATCHED_EGGS, msg);
		api.getRequestHandler().sendServerRequests(serverRequest);

		GetHatchedEggsResponse response;
		try {
			response = GetHatchedEggsResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
		api.getInventories().updateInventories();
		return updateHatchedEggs(response);
	}

}
