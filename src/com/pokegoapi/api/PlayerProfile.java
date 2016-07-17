package com.pokegoapi.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PlayerProfile 
{
	private long creationTime;
	private String username;
	private Team team;
	private int pokemonStorage;
	private int itemStorage;
	private String badge;
	
	private PlayerAvatar avatar;
	private DailyBonus dailyBonus;
	private ContactSettings contactSettings;
	private HashMap<String, Integer> currencies;
	
	
	public PlayerProfile()
	{
		currencies = new HashMap<String, Integer>();
	}
	
	public long getCreationTime()
	{
		return creationTime;
	}
	public void setCreationTime(long creationTime)
	{
		this.creationTime = creationTime;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team)
	{
		this.team = team;
	}
	
	
	public int getPokemonStorage()
	{
		return pokemonStorage;
	}
	
	
	public void setPokemonStorage(int pokemonStorage)
	{
		this.pokemonStorage = pokemonStorage;
	}
	
	
	public int getItemStorage()
	{
		return itemStorage;
	}
	
	
	public void setItemStorage(int itemStorage)
	{
		this.itemStorage = itemStorage;
	}
	
	
	public String getBadge()
	{
		return badge;
	}
	
	
	public void setBadge(String badge) 
	{
		this.badge = badge;
	}
	
	
	public PlayerAvatar getAvatar()
	{
		return avatar;
	}
	
	
	public void setAvatar(PlayerAvatar avatar)
	{
		this.avatar = avatar;
	}
	
	
	public DailyBonus getDailyBonus()
	{
		return dailyBonus;
	}
	
	
	public void setDailyBonus(DailyBonus dailyBonus)
	{
		this.dailyBonus = dailyBonus;
	}
	
	
	public ContactSettings getContactSettings()
	{
		return contactSettings;
	}
	
	
	public void setContactSettings(ContactSettings contactSettings)
	{
		this.contactSettings = contactSettings;
	}

	public void addCurrency(String name, int amount)
	{
		this.currencies.put(name, amount);
	}
	
	public int getCurrency(String name)
	{
		
		// may throw exception here instead of returning 0
		int amount = 0;
		if (this.currencies.containsKey(name))
		{
			amount = currencies.get(name);
		}
		
		return amount;
	}
	
	
	public int getStardust()
	{
		return getCurrency("STARDUST");
	}
	
	public int getPokecoins()
	{
		return getCurrency("POKECOIN");
	}
	
	
	@Override
	public String toString()
	{
		return this.getUsername() + " " + this.getTeam();
	}
	
}
