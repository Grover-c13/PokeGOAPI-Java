package com.pokegoapi.util;

/**
 * Created by rama on 31/08/16.
 */
public abstract class PokeCallback<T> {

	public void onError(Throwable error) {
		Log.e("PokeCallback", "exception", error);
	}

	public abstract void onResponse(T result);
}
