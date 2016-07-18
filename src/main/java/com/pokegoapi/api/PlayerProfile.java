package com.pokegoapi.api;

import POGOProtos.Player.EquippedBadgeOuterClass;
import com.pokegoapi.exceptions.InvalidCurrencyException;

import java.util.HashMap;
import java.util.Map;

public class PlayerProfile {
	private long creationTime;
	private String username;
	private Team team;
	private int pokemonStorage;
	private int itemStorage;
	private EquippedBadgeOuterClass.EquippedBadge badge;

	private PlayerAvatar avatar;
	private DailyBonus dailyBonus;
	private ContactSettings contactSettings;
	private Map<Currency, Integer> currencies = new HashMap<Currency, Integer>();


	public void addCurrency(String name, int amount) throws InvalidCurrencyException {
		try {
			currencies.put(Currency.valueOf(name), amount);
		} catch (Exception e) {
			;
		}
	}

	public int getCurrency(Currency currency) throws InvalidCurrencyException {
		if (currencies.containsKey(currency))
			return currencies.get(currency);
		else
			throw new InvalidCurrencyException();
	}

	public enum Currency {
		STARDUST, POKECOIN;
	}

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

	public EquippedBadgeOuterClass.EquippedBadge getBadge() {
		return badge;
	}

	public void setBadge(EquippedBadgeOuterClass.EquippedBadge badge) {
		this.badge = badge;
	}

	public PlayerAvatar getAvatar() {
		return avatar;
	}

	public void setAvatar(PlayerAvatar avatar) {
		this.avatar = avatar;
	}

	public DailyBonus getDailyBonus() {
		return dailyBonus;
	}

	public void setDailyBonus(DailyBonus dailyBonus) {
		this.dailyBonus = dailyBonus;
	}

	public ContactSettings getContactSettings() {
		return contactSettings;
	}

	public void setContactSettings(ContactSettings contactSettings) {
		this.contactSettings = contactSettings;
	}

	public Map<Currency, Integer> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<Currency, Integer> currencies) {
		this.currencies = currencies;
	}
}
