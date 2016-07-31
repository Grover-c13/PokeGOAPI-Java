package com.pokegoapi.util;

import com.pokegoapi.exceptions.AsyncLoginFailedException;
import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.AsyncRemoteServerException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import rx.Observable;

/**
 * Created by paul on 30-7-2016.
 */
public class AsyncHelper {
    public static <T> T toBlocking(Observable<T> observable) throws LoginFailedException, RemoteServerException {
        try {
            return observable.toBlocking().first();
        }
        catch (RuntimeException e) {
            if (e.getCause() instanceof AsyncLoginFailedException) {
                throw new LoginFailedException(e.getMessage(), e.getCause());
            }
            if (e.getCause() instanceof AsyncRemoteServerException) {
                throw new RemoteServerException(e.getMessage(), e.getCause());
            }
            throw new AsyncPokemonGoException("Unknown exception occurred. ", e);
        }
    }
}
