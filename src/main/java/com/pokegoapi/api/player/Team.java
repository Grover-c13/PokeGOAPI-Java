package com.pokegoapi.api.player;

public enum Team {
	// VALUES CONFIRMED
	TEAM_NONE(0),
	TEAM_MYSTIC(1),
	TEAM_VALOR(2),
	TEAM_INSTINCT(3);

	private int value;

	private Team(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
