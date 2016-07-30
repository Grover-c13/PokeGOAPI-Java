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
