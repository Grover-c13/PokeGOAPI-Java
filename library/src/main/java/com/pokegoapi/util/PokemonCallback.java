package com.pokegoapi.util;

/**
 * Created by rama on 31/08/16.
 */
public abstract class PokemonCallback<T> {

	public abstract void onError(Exception e);

	public abstract void onResponse(T data);
}
