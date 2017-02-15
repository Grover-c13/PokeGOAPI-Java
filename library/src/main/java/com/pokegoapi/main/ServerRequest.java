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
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import lombok.Getter;
import rx.Observable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ServerRequest {
	@Getter
	private final RequestType type;
	@Getter
	private final Message request;

	private Observable<ByteString> observable;

	private final Object responseLock = new Object();

	private ByteString response;
	private boolean received;

	public ServerRequest(RequestType type, Message request) {
		this.type = type;
		this.request = request;
		this.observable = Observable.from(new Future<ByteString>() {
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				synchronized (responseLock) {
					responseLock.notifyAll();
				}
				return true;
			}

			@Override
			public boolean isCancelled() {
				return false;
			}

			@Override
			public boolean isDone() {
				synchronized (responseLock) {
					return received;
				}
			}

			@Override
			public ByteString get() throws InterruptedException, ExecutionException {
				return get(TimeUnit.MINUTES.toMillis(1));
			}

			@Override
			public ByteString get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				return get(unit.toMillis(timeout));
			}

			private ByteString get(long timeout) throws ExecutionException, InterruptedException {
				if (!isDone()) {
					responseLock.wait(timeout);
				}
				return response;
			}
		});
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
			this.received = true;
		}
	}

	/**
	 * Gets the observable for this request
	 *
	 * @return the observable for this request
	 */
	public Observable<ByteString> observable() {
		return observable;
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
