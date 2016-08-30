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

import com.google.protobuf.GeneratedMessage;

import java.util.ArrayList;
import java.util.Collections;

import POGOProtos.Networking.Requests.RequestOuterClass.Request;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import lombok.Getter;

/**
 * The type Server request.
 */
public class AsyncServerRequest {
	@Getter
	private final long id = System.nanoTime();
	@Getter
	private final RequestType type;
	@Getter
	private final Request request;
	@Getter
	private final ArrayList<ServerRequest> commonRequests;

	/**
	 * Instantiates a new Server request.
	 *
	 * @param type the type
	 * @param req  the req
	 */
	public AsyncServerRequest(RequestType type, GeneratedMessage req) {
		Request.Builder reqBuilder = Request.newBuilder();
		reqBuilder.setRequestMessage(req.toByteString());
		reqBuilder.setRequestType(type);
		this.type = type;
		this.request = reqBuilder.build();
		this.commonRequests = new ArrayList<ServerRequest>();
	}

	public AsyncServerRequest addCommonRequest(ServerRequest... requests) {
		Collections.addAll(commonRequests, requests);
		return this;
	}
}
