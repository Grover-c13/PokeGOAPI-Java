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
import com.google.protobuf.GeneratedMessage;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The type Server request.
 */
public class AsyncServerRequest {
	private static final AtomicLong CURRENT_ID = new AtomicLong(System.currentTimeMillis());

	@Getter
	private final long id = CURRENT_ID.getAndIncrement();
	@Getter
	private final RequestType type;
	@Getter
	private final Request request;
	@Getter
	private boolean requireCommonRequest;

	/**
	 * Instantiates a new Server request.
	 *
	 * @param type the type
	 * @param req  the req
	 * @param requireCommonRequest indicate if this request require common requests
	 */
	public AsyncServerRequest(RequestType type, GeneratedMessage req, boolean requireCommonRequest) {
		Request.Builder reqBuilder = Request.newBuilder();
		reqBuilder.setRequestMessage(req.toByteString());
		reqBuilder.setRequestType(type);
		this.type = type;
		this.request = reqBuilder.build();
		this.requireCommonRequest = requireCommonRequest;
	}

	/**
	 * Instantiates a new Server request.
	 *
	 * @param type the type
	 * @param req  the req
	 */
	public AsyncServerRequest(RequestType type, GeneratedMessage req) {
		this(type, req, false);
	}

	/**
	 * Instantiates a new Server request.
	 *
	 * @param type the type
	 * @param req  the req
	 */
	AsyncServerRequest(RequestType type, Request req) {
		this.type = type;
		this.request = req;
		this.requireCommonRequest = false;
	}

	/**
	 * Adds a common request to this request if the given parameter is true
	 * @param requireCommon if this request should add commons
	 * @return this object
	 */
	public AsyncServerRequest withCommons(boolean requireCommon) {
		this.requireCommonRequest = requireCommon;
		return this;
	}
}
