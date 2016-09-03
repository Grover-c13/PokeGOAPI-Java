package com.pokegoapi.util;

import lombok.Getter;

/**
 * Created by rama on 31/08/16.
 */
public abstract class PokeCallback<T> {

	@Getter
	private Throwable error;
	@Getter
	private T result;

	public final void fire(T result) {
		this.result = result;
		onResponse(result);
	}

	public final void fire(Throwable error) {
		this.error = error;
		onError(error);
	}

	public void onError(Throwable error) {
		Log.e("PokeCallback", "exception", error);
	}

	public abstract void onResponse(T result);

}
