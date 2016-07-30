package com.pokegoapi.util;

import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by paul on 30-7-2016.
 */
public interface PokemonFuture<T> extends Future<T> {
    T toBlocking() throws LoginFailedException, RemoteServerException;
}
