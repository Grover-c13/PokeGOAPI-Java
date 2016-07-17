package com.pokegoapi.api;

import lombok.Getter;

public enum Team 
{
	// VALUES UNCONFIRMED (except for Team Mystic, the best team)
	TEAM_NONE (-1),
	TEAM_INSTINCT (0), 
	TEAM_MYSTIC (1),	
	TEAM_VALOR (2);
	
	@Getter private int value;
	
	private Team(int value)
	{
		this.value = value;
	}
}
