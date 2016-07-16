package api;

public enum Team 
{
	// VALUES UNCONFIRMED (exception for team mystic, the best team)
	TEAM_NONE (-1),
	TEAM_INSTINCT (0), 
	TEAM_MYSTIC (1),	
	TEAM_VALOR (2);
	
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
