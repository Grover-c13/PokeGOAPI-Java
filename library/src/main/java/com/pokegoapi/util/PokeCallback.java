package com.pokegoapi.util;

import com.pokegoapi.api.map.MapObjects;
import rx.Subscriber;

/**
 * Created by rama on 31/08/16.
 */
public abstract class PokeCallback<T> {

	public abstract void onError(Throwable e);

	public abstract void onResponse(T data);

	public Subscriber<T> getSubscriber() {
		return new Subscriber<T>() {
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				this.onError(e);
			}

			@Override
			public void onNext(T mapObjects) {
				onResponse(mapObjects);
			}
		};
	}
}
