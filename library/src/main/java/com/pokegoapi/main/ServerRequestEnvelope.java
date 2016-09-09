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
import com.google.protobuf.Message;
import com.pokegoapi.api.PokemonGo;
import lombok.Getter;
import lombok.Setter;
import rx.Observable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ServerRequestEnvelope {
	@Getter
	@Setter
	public ServerRequest request;
	@Getter
	public List<ServerPlatformRequest> platformRequests = new ArrayList<>();
	@Getter
	public List<ServerRequest> commons;
	private Observable<ServerResponse> observable;
	private ServerResponse response;
	private final Object responseLock = new Object();

	private ServerRequestEnvelope(ServerRequest request, List<ServerRequest> commons) {
		this.request = request;
		this.commons = commons;
		this.observable = Observable.from(new Future<ServerResponse>() {
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				return false;
			}

			@Override
			public boolean isCancelled() {
				return false;
			}

			@Override
			public boolean isDone() {
				synchronized (responseLock) {
					return response != null;
				}
			}

			@Override
			public ServerResponse get() throws InterruptedException, ExecutionException {
				if (!isDone()) {
					synchronized (responseLock) {
						responseLock.wait();
					}
				}
				if (response != null && response.exception != null) {
					throw new RuntimeException(response.exception);
				}
				return response;
			}

			@Override
			public ServerResponse get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				return get();
			}
		});
	}

	/**
	 * Creates an empty request envelope without commons
	 *
	 * @return the envelope created
	 */
	public static ServerRequestEnvelope create() {
		return new ServerRequestEnvelope(null, new ArrayList<ServerRequest>());
	}

	/**
	 * Creates a request envelope without commons
	 *
	 * @param request the request to add to this envelope
	 * @return the envelope created
	 */
	public static ServerRequestEnvelope create(ServerRequest request) {
		return new ServerRequestEnvelope(request, new ArrayList<ServerRequest>());
	}

	/**
	 * Creates a request envelope with optional default commons
	 *
	 * @param request the request to add to this envelope
	 * @param api the current api
	 * @param commons true if the default common requests should be added to this envelope
	 * @return the envelope created
	 */
	public static ServerRequestEnvelope create(ServerRequest request, PokemonGo api, boolean commons) {
		List<ServerRequest> commonRequests = new ArrayList<>();
		if (commons) {
			commonRequests.addAll(CommonRequests.getDefaultCommons(api, request.type));
		}
		return new ServerRequestEnvelope(request, commonRequests);
	}

	/**
	 * Creates a request envelope with the default common requests
	 *
	 * @param request the request to add to this envelope
	 * @param api the current api
	 * @return the envelope created
	 */
	public static ServerRequestEnvelope createCommons(ServerRequest request, PokemonGo api) {
		return new ServerRequestEnvelope(request, CommonRequests.getDefaultCommons(api, request.type));
	}

	/**
	 * Includes the given commons from this request
	 *
	 * @param commons the requests to include
	 */
	public void includeCommons(ServerRequest... commons) {
		Collections.addAll(this.commons, commons);
	}

	/**
	 * Removes the given commons from this request
	 *
	 * @param commons the commons to remove
	 */
	public void removeCommons(ServerRequest... commons) {
		for (ServerRequest common : commons) {
			this.commons.remove(common);
		}
	}

	/**
	 * Adds a platform request to this envelope
	 *
	 * @param request the request to add
	 * @return the added request
	 */
	public ServerPlatformRequest addPlatform(ServerPlatformRequest request) {
		this.platformRequests.add(request);
		return request;
	}

	/**
	 * Adds a platform request to this envelope
	 *
	 * @param requestType the type of request being added
	 * @param request the request to be added
	 * @return the added request
	 */
	public ServerPlatformRequest addPlatform(PlatformRequestType requestType, ByteString request) {
		return this.addPlatform(new ServerPlatformRequest(requestType, request));
	}

	/**
	 * Handles the response for this request
	 *
	 * @param response the response
	 */
	public void handleResponse(ServerResponse response) {
		if (request != null && response.has(request.type)) {
			request.handleResponse(response.get(request.type));
		}
		for (ServerRequest request : commons) {
			RequestType type = request.type;
			if (response.has(type)) {
				request.handleResponse(response.get(type));
			}
		}
		for (ServerPlatformRequest request : platformRequests) {
			PlatformRequestType type = request.type;
			if (response.has(type)) {
				request.handleResponse(response.get(type));
			}
		}
	}

	/**
	 * Notifies all listeners of this envelope that the response has been received
	 *
	 * @param response the response that has been received
	 */
	public void notifyResponse(ServerResponse response) {
		synchronized (responseLock) {
			this.response = response;
			this.responseLock.notifyAll();
		}
	}

	/**
	 * Gets the observable for this envelope response
	 *
	 * @return the observable
	 */
	public Observable<ServerResponse> observable() {
		return observable;
	}
}
