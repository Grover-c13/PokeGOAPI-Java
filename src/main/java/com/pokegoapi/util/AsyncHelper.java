package com.pokegoapi.util;

import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by paul on 30-7-2016.
 */
public class AsyncHelper {
    public static <T> T toBlocking(Future<T> future) throws LoginFailedException, RemoteServerException {
        try {
            return future.get();
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
