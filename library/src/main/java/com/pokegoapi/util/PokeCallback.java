package com.pokegoapi.util;

/**
 * Created by rama on 31/08/16.
 */
public abstract class PokeCallback<T> {

	public abstract void onError(Throwable e);

	public abstract void onResponse(T data);
}
