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

package com.pokegoapi.api.listener;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.main.ServerRequestEnvelope;
import com.pokegoapi.main.ServerResponse;

/**
 * Listener that allows interception of
 */
public interface RequestInterceptor extends Listener {
	/**
	 * Allows removal of individual requests before they are sent
	 *
	 * @param api the current api
	 * @param request the request in question
	 * @param envelope the envelope containing this request
	 * @return true to remove and false to keep
	 */
	boolean shouldRemove(PokemonGo api, ServerRequest request, ServerRequestEnvelope envelope);

	/**
	 * Allows modification of individual requests before they are sent
	 *
	 * @param api the current api
	 * @param request the request to possibly be modified
	 * @param envelope the envelope containing the request
	 * @return a new request to send, or null to keep the original
	 */
	ServerRequest adaptRequest(PokemonGo api, ServerRequest request, ServerRequestEnvelope envelope);

	/**
	 * Called when a response is received from the server
	 *
	 * @param api the current api
	 * @param response the response from the server
	 * @param request the request sent to get this response
	 */
	void handleResponse(PokemonGo api, ServerResponse response, ServerRequestEnvelope request);
}
