package com.pokegoapi.api.map.Pokemon;

public enum CatchPokemonResult
{
	SUCCESS (1),
	ESCAPE (2),
	FLEE (3),
	MISSED (4);

	private final int status;

	CatchPokemonResult(int i) {
		status = i;
	}
}
