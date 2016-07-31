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

package com.pokegoapi.util;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DummyFuture<R> implements PokemonFuture<R> {


	protected final R result;

	public DummyFuture(R result) {
		this.result = result;
	}

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
		return true;
	}

	@Override
	public R get() throws InterruptedException, ExecutionException {
		R result = getResult(1, TimeUnit.MINUTES);
		while (result == null) {
			result = getResult(1, TimeUnit.MINUTES);
		}
		return result;
	}

	@Override
	public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		R result = getResult(timeout, unit);
		if (result == null) {
			throw new TimeoutException("No result found");
		}
		return result;
	}

	protected R getResult(long timeouut, TimeUnit timeUnit) throws InterruptedException, ExecutionException {
		return result;
	}


	/**
	 * Convert a future to its result
	 *
	 * @return The result or an unwrapped exception
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	public R toBlocking() throws LoginFailedException, RemoteServerException {
		return DummyFuture.toBlocking(this);
	}

	/**
	 * Convert a future to its result
	 *
	 * @param future The future
	 * @param <N>    Result type
	 * @return The result or an unwrapped exception
	 * @throws RemoteServerException the remote server exception
	 * @throws LoginFailedException  the login failed exception
	 */
	public static <N> N toBlocking(Future<N> future) throws LoginFailedException, RemoteServerException {
		try {
			return future.get();
		} catch (InterruptedException e) {
			throw new AsyncPokemonGoException("Shutdown received", e);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof LoginFailedException) {
				throw (LoginFailedException) e.getCause();
			}
			if (e.getCause() instanceof RemoteServerException) {
				throw (RemoteServerException) e.getCause();
			}
			if (e.getCause() instanceof InvalidProtocolBufferException) {
				throw new RemoteServerException(e.getCause().getMessage(), e.getCause());
			}
			throw new AsyncPokemonGoException("Unknown exception occurred. ", e);
		}
	}


}
