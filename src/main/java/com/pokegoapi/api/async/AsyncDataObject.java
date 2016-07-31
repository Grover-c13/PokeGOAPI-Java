package com.pokegoapi.api.async;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import rx.Observable;
import rx.functions.Func1;

/**
 * Author: Mustafa Ashurex
 * Created: 7/31/16
 */
public abstract class AsyncDataObject<T> {
	private final PokemonGo api;

	protected AsyncDataObject(final PokemonGo api) {
		this.api = api;
	}

	protected synchronized PokemonGo getApi(){ return api; }

	public abstract T refreshDataSync() throws LoginFailedException, RemoteServerException;
	public abstract Observable<T> refreshData();
	public abstract T getInstance();

	/**
	 * Update instance data with the returned ServerRequest data.
	 *
	 * @param responses
	 * @return an updated instance.
	 * @throws LoginFailedException
	 * @throws RemoteServerException
	 */
	protected abstract T updateInstanceData(ServerRequest... responses)
			throws LoginFailedException, RemoteServerException;

	protected Observable<?> sendAsyncServerRequests(final ServerRequest... requests) {
		return getApi().getRequestHandler().sendAsyncServerRequests(requests)
				.flatMap(new Func1<ServerRequest[], Observable<?>>() {
					@Override
					public Observable<?> call(ServerRequest[] requests) {
						if(requests == null || requests.length == 0){ return Observable.empty(); }
						try {
							return Observable.just(updateInstanceData(requests[0]));
						}
						catch (Exception e) {
							return Observable.error(e);
						}
					}
				});
	}
}
