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

import POGOProtos.Networking.Requests.RequestOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import com.google.protobuf.GeneratedMessage;
import com.pokegoapi.util.Signature;
import lombok.Getter;

/**
 * The type Server request.
 */
public class AsyncServerRequest {
	@Getter
	private final long id = System.nanoTime();
	@Getter
	private final RequestTypeOuterClass.RequestType type;
	@Getter
	private final RequestOuterClass.Request request;

	/**
	 * Instantiates a new Server request.
	 *
	 * @param type the type
	 * @param req  the req
	 */
	public AsyncServerRequest(RequestTypeOuterClass.RequestType type, GeneratedMessage req) {
		RequestOuterClass.Request.Builder reqBuilder = RequestOuterClass.Request.newBuilder();
		reqBuilder.setRequestMessage(req.toByteString());
		reqBuilder.setRequestType(type);
		this.type = type;
		this.request = reqBuilder.build();
	}

	/**
	 * Instantiates a new Server request.
	 *
	 * @param type the type
	 * @param req  the req
	 */
	AsyncServerRequest(RequestTypeOuterClass.RequestType type, RequestOuterClass.Request req) {
		this.type = type;
		this.request = req;
	}
}
