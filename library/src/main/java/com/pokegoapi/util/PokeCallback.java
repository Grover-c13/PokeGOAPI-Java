package com.pokegoapi.util;

/**
 * Created by rama on 31/08/16.
 */
public abstract class PokeCallback<T> {

	public void onError(Throwable e) {
		Log.e("PokeCallback", "exception", e);
	}

	public abstract void onResponse(T result);


}
