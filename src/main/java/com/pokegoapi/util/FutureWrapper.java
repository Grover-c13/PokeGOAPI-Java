package com.pokegoapi.util;

import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class FutureWrapper<T,R> implements PokemonFuture<R> {
    private final Future<T> result;
    public FutureWrapper(Future<T> result) {
        this.result = result;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return result.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return result.isCancelled();
    }

    @Override
    public boolean isDone() {
        return result.isDone();
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

    private R getResult(long timeouut, TimeUnit timeUnit) throws InterruptedException, ExecutionException {
        long wait = System.currentTimeMillis() + timeUnit.toMillis(timeouut);
        while (!isDone()) {
            Thread.sleep(10);
            if (wait < System.currentTimeMillis()) {
                return null;
            }
        }
        try {
            return handle(result.get());
        }
        catch (RemoteServerException e) {
            throw new ExecutionException(e);
        }
    }

    protected abstract R handle(T result) throws RemoteServerException;

    public R toBlocking() throws LoginFailedException, RemoteServerException {
        try {
            return get();
        }
        catch (InterruptedException e) {
            throw new AsyncPokemonGoException("Shutdown received", e);
        }
        catch (ExecutionException e) {
            if (e.getCause() instanceof LoginFailedException) {
                throw (LoginFailedException) e.getCause();
            }
            if (e.getCause() instanceof RemoteServerException) {
                throw (RemoteServerException) e.getCause();
            }
            throw new AsyncPokemonGoException("Unknown exception occurred. ", e);
        }
    }
}
