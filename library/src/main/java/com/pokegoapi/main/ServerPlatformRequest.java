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
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.Getter;

public class ServerPlatformRequest {
	@Getter
	public final PlatformRequestType type;
	@Getter
	public final ByteString request;

	private final Object responseLock = new Object();

	private ByteString response;

	/**
	 * Creates a ServerPlatformRequest
	 * @param type the type of request
	 * @param request the request data
	 */
	public ServerPlatformRequest(PlatformRequestType type, ByteString request) {
		this.type = type;
		this.request = request;
	}

	/**
	 * Handles the response for this request
	 *
	 * @param response the response to handle
	 */
	public void handleResponse(ByteString response) {
		synchronized (responseLock) {
			this.response = response;
			this.responseLock.notifyAll();
		}
	}

	/**
	 * Gets the response data for this request, if received
	 *
	 * @return the response data for this request, if received
	 * @throws InvalidProtocolBufferException if the response data is null
	 */
	public ByteString getData() throws InvalidProtocolBufferException {
		synchronized (responseLock) {
			if (response != null) {
				return response;
			}
			throw new InvalidProtocolBufferException("Response data cannot be null");
		}
	}
}
