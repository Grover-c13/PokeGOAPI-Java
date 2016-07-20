package com.pokegoapi.api.inventory;

public enum Pokeball {
	POKEBALL (1),
	GREATBALL (2),
	ULTRABALL (3),
	MASTERBALL (4);

	private final int balltype;

	Pokeball(int i) {
		balltype = i;
	}
}
