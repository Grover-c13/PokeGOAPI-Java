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
import lombok.Getter;
import lombok.Setter;
import rx.Observable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ServerRequestEnvelope {
	@Getter
	private List<ServerRequest> requests = new ArrayList<>();
	@Getter
	private List<ServerPlatformRequest> platformRequests = new ArrayList<>();
	@Getter
	private Set<RequestType> commonExclusions = new HashSet<>();
	@Setter
	@Getter
	private boolean commons;

	private Observable<ServerResponse> observable;
	private ServerResponse response;
	private final Object responseLock = new Object();

	private ServerRequestEnvelope(boolean commons, Set<RequestType> commonExclusions) {
		this.commons = commons;
		this.commonExclusions = commonExclusions;
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
				if (response != null && response.getException() != null) {
					throw new RuntimeException(response.getException());
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
	 * Creates a request envelope without commons
	 *
	 * @return the envelope created
	 */
	public static ServerRequestEnvelope create() {
		return new ServerRequestEnvelope(false, new HashSet<RequestType>());
	}

	/**
	 * Creates a request envelope with commons
	 *
	 * @param commonExclusions the common requests to exclude
	 * @return the envelope created
	 */
	public static ServerRequestEnvelope createCommons(RequestType... commonExclusions) {
		Set<RequestType> exclusions = new HashSet<>();
		Collections.addAll(exclusions, commonExclusions);
		return new ServerRequestEnvelope(true, exclusions);
	}

	/**
	 * Excludes the given commons from this request
	 *
	 * @param requestTypes the requests to exclude
	 */
	public void excludeCommons(RequestType... requestTypes) {
		Collections.addAll(this.commonExclusions, requestTypes);
	}

	/**
	 * Adds a request to this envelope
	 *
	 * @param request the request to add
	 * @return the added request
	 */
	public ServerRequest add(ServerRequest request) {
		this.requests.add(request);
		return request;
	}

	/**
	 * Adds a request to this envelope
	 *
	 * @param requestType the type of request being added
	 * @param request the request to be added
	 * @return the added request
	 */
	public ServerRequest add(RequestType requestType, Message request) {
		return this.add(new ServerRequest(requestType, request));
	}

	/**
	 * Adds a platform request to this envelope
	 *
	 * @param request the request to add
	 * @return the added request
	 */
	public ServerPlatformRequest add(ServerPlatformRequest request) {
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
	public ServerPlatformRequest add(PlatformRequestType requestType, ByteString request) {
		return this.add(new ServerPlatformRequest(requestType, request));
	}

	/**
	 * Handles the response for this request
	 *
	 * @param response the response
	 */
	public void handleResponse(ServerResponse response) {
		for (ServerRequest request : requests) {
			request.handleResponse(response.get(request.getType()));
		}
		for (ServerPlatformRequest request : platformRequests) {
			request.handleResponse(response.get(request.getType()));
		}
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
