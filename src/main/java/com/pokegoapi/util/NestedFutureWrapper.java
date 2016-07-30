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

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class NestedFutureWrapper<T, R> extends FutureWrapper<T, R> {
	public NestedFutureWrapper(Future<T> result) {
		super(result);
	}

	protected R getResult(long timeouut, TimeUnit timeUnit) throws InterruptedException, ExecutionException {
		long wait = System.currentTimeMillis() + timeUnit.toMillis(timeouut);
		while (!isDone()) {
			Thread.sleep(10);
			if (wait < System.currentTimeMillis()) {
				return null;
			}
		}
		Future<R> future = handleFuture(result.get());
		while (future.isDone()) {
			Thread.sleep(10);
			if (wait < System.currentTimeMillis()) {
				return null;
			}
		}
		return future.get();
	}

	protected abstract Future<R> handleFuture(T result);

	@Override
	protected final R handle(T result) throws RemoteServerException, LoginFailedException {
		// Because getResult is overiden, this is not called
		return null;
	}
}
