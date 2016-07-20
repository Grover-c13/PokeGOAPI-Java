package com.pokegoapi.api.player;

import POGOProtos.Data.Player.EquippedBadgeOuterClass;
import lombok.Data;

import com.pokegoapi.exceptions.InvalidCurrencyException;

import java.util.HashMap;
import java.util.Map;

@Data
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
			throw new InvalidCurrencyException();
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
}
