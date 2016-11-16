package com.pokegoapi.api.listener;

import com.pokegoapi.api.PokemonGo;

public interface Listener {
	default void remove(PokemonGo api) {
		api.removeListener(this);
	}
}
