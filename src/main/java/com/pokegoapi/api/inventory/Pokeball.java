package com.pokegoapi.api.inventory;

import lombok.Getter;

public enum Pokeball {
	POKEBALL(1),
	GREATBALL(2),
	ULTRABALL(3),
	MASTERBALL(4);

	@Getter
	private final int balltype;

	Pokeball(int i) {
		balltype = i;
	}
}
