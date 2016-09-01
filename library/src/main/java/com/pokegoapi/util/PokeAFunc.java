package com.pokegoapi.util;

import rx.Subscriber;

/**
 * Created by rama on 31/08/16.
 */
public abstract class PokeAFunc<T> {

	public abstract void onError(Throwable e);

	public abstract void onResponse(T data);

	public Subscriber<T> getSubscriber() {
		return new Subscriber<T>() {
			@Override
			public void onCompleted() {}

			@Override
			public void onError(Throwable e) {
				this.onError(e);
			}

			@Override
			public void onNext(T object) {
				onResponse(object);
			}
		};
	}
}
