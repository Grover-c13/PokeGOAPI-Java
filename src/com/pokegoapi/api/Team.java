package com.pokegoapi.api;

public enum Team 
{
	// VALUES UNCONFIRMED for the two inferior teams (Valor and Instinct)
	TEAM_NONE (0),
	TEAM_MYSTIC (1),	
	TEAM_VALOR (2),
	TEAM_INSTINCT (3);
	
	private int value;
	private Team(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return this.value;
	}
	

}
