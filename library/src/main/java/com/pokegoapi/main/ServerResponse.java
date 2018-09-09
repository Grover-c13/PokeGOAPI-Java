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

import POGOProtos.Networking.Platform.PlatformRequestTypeOuterClass.PlatformRequestType;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import com.google.protobuf.ByteString;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;

public class ServerResponse {
	private final EnumMap<RequestType, ByteString> responses = new EnumMap<>(RequestType.class);
	private final EnumMap<PlatformRequestType, ByteString> platformResponses = new EnumMap<>(PlatformRequestType.class);
	@Getter
	@Setter
	public Exception exception;

	/**
	 * Creates a blank {@link ServerResponse}
	 */
	public ServerResponse() {
	}

	/**
	 * Creates a {@link ServerResponse} with an exception
	 *
	 * @param exception the exception in this response
	 */
	public ServerResponse(Exception exception) {
		this.exception = exception;
	}

	/**
	 * Adds a response to this envelope
	 *
	 * @param type the type of request
	 * @param data the response data for this request
	 */
	public void addResponse(RequestType type, ByteString data) {
		responses.put(type, data);
	}

	/**
	 * Adds a response to this envelope
	 *
	 * @param type the type of request
	 * @param data the response data for this request
	 */
	public void addResponse(PlatformRequestType type, ByteString data) {
		platformResponses.put(type, data);
	}

	/**
	 * Gets the response data for this request type
	 *
	 * @param type the type to check
	 * @return response data for the given type, null if none available
	 */
	public ByteString get(RequestType type) {
		return responses.get(type);
	}

	/**
	 * Gets the response data for this request type
	 *
	 * @param type the type to check
	 * @return response data for the given type, null if none available
	 */
	public ByteString get(PlatformRequestType type) {
		return platformResponses.get(type);
	}

	/**
	 * Checks if this response contains the given request type
	 *
	 * @param type the request type to check for
	 * @return true if this response contains the given request type
	 */
	public boolean has(RequestType type) {
		return responses.containsKey(type);
	}

	/**
	 * Checks if this response contains the given platform request type
	 *
	 * @param type the platform request type to check for
	 * @return true if this response contains the given platform request type
	 */
	public boolean has(PlatformRequestType type) {
		return platformResponses.containsKey(type);
	}
}
