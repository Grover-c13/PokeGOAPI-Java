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

import POGOProtos.Data.Player.CurrencyOuterClass;
import POGOProtos.Data.PlayerDataOuterClass.PlayerData;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass.GetPlayerResponse;
import com.pokegoapi.api.internal.networking.Networking;
import lombok.Data;

import java.util.EnumMap;
import java.util.Map;

@Data
public class PlayerProfile {
	private static final String TAG = PlayerProfile.class.getSimpleName();
	private PlayerData playerData;
	private final PlayerAvatar avatar = new PlayerAvatar();
	private final DailyBonus dailyBonus;
	private final ContactSettings contactSettings = new ContactSettings();
	private final Map<Currency, Integer> currencies = new EnumMap<>(Currency.class);
	private final TutorialState tutorialState = new TutorialState();

	/**
	 */
	public PlayerProfile(GetPlayerResponse getPlayerResponse, Networking networking) {
		dailyBonus = new DailyBonus(networking);
		update(getPlayerResponse);
	}

	public final void update(GetPlayerResponse getPlayerResponse) {
		playerData = getPlayerResponse.getPlayerData();
		avatar.update(playerData.getAvatar());
		dailyBonus.update(playerData.getDailyBonus());
		contactSettings.update(playerData.getContactSettings());

		for (CurrencyOuterClass.Currency currency : getPlayerResponse.getPlayerData().getCurrenciesList()) {
			Currency currencyEnum = Currency.valueOf(currency.getName());
			currencies.put(currencyEnum, currency.getAmount());
		}

		tutorialState.update(playerData.getTutorialStateList());
	}

	/**
	 * Gets currency.
	 *
	 * @param currency the currency
	 * @return the currency
	 */
	public int getCurrency(Currency currency) {
		return currencies.get(currency);
	}

	public enum Currency {
		STARDUST, POKECOIN;
	}
}
