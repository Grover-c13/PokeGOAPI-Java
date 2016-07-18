package com.pokegoapi.api;

import lombok.Getter;

public enum Team 
{
	// VALUES UNCONFIRMED
	TEAM_NONE (0),
	TEAM_MYSTIC (1),	
	TEAM_INSTINCT (2),
	TEAM_VALOR (3);
	
	@Getter private int value;
	
	private Team(int value)
	{
		this.value = value;
	}
}
