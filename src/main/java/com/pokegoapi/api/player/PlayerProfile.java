/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.player;

import POGOProtos.Data.Player.EquippedBadgeOuterClass;
import POGOProtos.Data.Player.PlayerStatsOuterClass;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
	@Getter
	@Setter
	private PlayerStatsOuterClass.PlayerStats stats;


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
