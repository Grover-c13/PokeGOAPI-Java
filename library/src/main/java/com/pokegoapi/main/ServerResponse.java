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

import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import com.google.protobuf.ByteString;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;

public class ServerResponse {
	private final EnumMap<RequestType, ByteString> responses = new EnumMap<>(RequestType.class);
	@Getter
	@Setter
	private Exception exception;

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
	 * Gets the response data for this request type
	 *
	 * @param type the type to check
	 * @return response data for the given type, null if none available
	 */
	public ByteString get(RequestType type) {
		return responses.get(type);
	}
}
