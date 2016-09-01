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

package com.pokegoapi.main;

import POGOProtos.Networking.Requests.RequestOuterClass.Request;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.util.PokeAFunc;
import com.pokegoapi.util.PokeCallback;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The type Server request.
 */
public class AsyncServerRequest<T extends GeneratedMessage, K> {
	@Getter
	private final RequestType type;
	@Getter
	private final Request request;
	@Getter
	final ArrayList<InternalServerRequest> boundedRequests = new ArrayList<>();

	private final PokeCallback<K> callback;
	private final PokeAFunc<T, K> func;

	/**
	 * Instantiates a new Server request.
	 *
	 * @param type the type
	 * @param req  the req
	 * @param func internal func to handle data
	 * @param callback an optional callback to handle results
	 * @param api the current instance of PokemonGo used to bound common requests
	 * @param requests requests to bound in the same request envelope
	 */
	public AsyncServerRequest(RequestType type, GeneratedMessage req, PokeAFunc<T, K> func,
								PokeCallback<K> callback, PokemonGo api,
								InternalServerRequest... requests) {
		Request.Builder reqBuilder = Request.newBuilder();
		reqBuilder.setRequestMessage(req.toByteString());
		reqBuilder.setRequestType(type);
		this.type = type;
		this.request = reqBuilder.build();
		this.callback = callback;
		this.func = func;

		if (requests.length > 0) {
			Collections.addAll(boundedRequests, requests);
		} else {
			Collections.addAll(boundedRequests, CommonRequest.getCommonRequests(api));
		}

		api.getRequestHandler().sendRequest(this);
	}

	/**
	 * Fire both, the internal callback and the outgoing callback
	 *
	 * @param data the data
	 */
	public void fire(ByteString data) {
		K response = null;

		if (func != null) {
			try {
				response = func.exec(data);
			} catch (Throwable e) {
				if (callback != null) {
					callback.onError(e);
				}
			}
		}

		if (callback != null) {
			callback.onResponse(response);
		}
	}

	/**
	 * Fire the error
	 *
	 * @param error the throwable exception
	 */
	public void fire(Throwable error) {
		if (callback != null) {
			callback.onError(error);
		}
	}
}
