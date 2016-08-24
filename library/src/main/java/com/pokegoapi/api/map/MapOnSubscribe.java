package com.pokegoapi.api.map;

import rx.Observable;
import rx.Subscriber;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Paul van Assen
 */
public class MapOnSubscribe<T>  {
	final List<Subscriber<? super T>> subscribers = new LinkedList<>();

	Observable<T> create() {
		Observable.OnSubscribe<T> onSubscribe = new Observable.OnSubscribe<T>() {
			@Override
			public void call(Subscriber<? super T> child) {
				subscribers.add(child);
			}
		};
		return new Observable<T>(onSubscribe) {};
	}

	void onNext(T nearbyPokemon) {
		Iterator<Subscriber<? super T>> iterator = subscribers.iterator();
		while (iterator.hasNext()) {
			Subscriber<? super T> subscriber = iterator.next();
			if (subscriber.isUnsubscribed()) {
				iterator.remove();
				continue;
			}
			subscriber.onNext(nearbyPokemon);
		}
	}
}
