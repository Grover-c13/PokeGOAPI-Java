package mx.may.courtney.pgo.api;

public class PlayerProfile 
{
	private long creationTime;
	private String username;
	private Team team;
	private int pokemonStorage;
	private int itemStorage;
	private String badge;
	
	public long getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	public int getPokemonStorage() {
		return pokemonStorage;
	}
	public void setPokemonStorage(int pokemonStorage) {
		this.pokemonStorage = pokemonStorage;
	}
	public int getItemStorage() {
		return itemStorage;
	}
	public void setItemStorage(int itemStorage) {
		this.itemStorage = itemStorage;
	}
	public String getBadge() {
		return badge;
	}
	public void setBadge(String badge) {
		this.badge = badge;
	}
	
	@Override
	public String toString()
	{
		return this.getUsername() + " " + this.getTeam();
	}
	
}
