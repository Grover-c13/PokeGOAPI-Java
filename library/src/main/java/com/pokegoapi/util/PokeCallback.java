package com.pokegoapi.util;

import com.pokegoapi.api.map.MapObjects;
import rx.Subscriber;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by rama on 31/08/16.
 */
public abstract class PokeCallback<T> {

	private AtomicLong barrier = new AtomicLong(0);

	public abstract void onError(Throwable e);

	public abstract void onResponse(T data);

	public Subscriber<T> getSubscriber() {
		barrier.incrementAndGet();
		return new Subscriber<T>() {
			public void onCompleted() {
				barrier.set(0);
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

	public boolean completed() {
		return barrier.compareAndSet(0, 0);
	}

	public void waitComplete() {
		while (true) {
			if (barrier.compareAndSet(0, 0))
				return;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

			}
		}
	}
}
