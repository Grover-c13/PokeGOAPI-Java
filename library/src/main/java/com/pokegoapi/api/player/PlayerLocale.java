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

import java.util.Locale;

import POGOProtos.Networking.Requests.Messages.GetPlayerMessageOuterClass;

/**
 * Created by iGio90 on 25/08/16.
 */
public class PlayerLocale {

	private GetPlayerMessageOuterClass.GetPlayerMessage.PlayerLocale playerLocale;

	/**
	 * Contructor to use the default Locale
	 */
	public PlayerLocale() {
		GetPlayerMessageOuterClass.GetPlayerMessage.PlayerLocale.Builder builder =
				GetPlayerMessageOuterClass.GetPlayerMessage.PlayerLocale.newBuilder();
		builder.setCountry(Locale.getDefault().getCountry())
				.setLanguage(Locale.getDefault().getLanguage());

		playerLocale = builder.build();
	}

	public GetPlayerMessageOuterClass.GetPlayerMessage.PlayerLocale getPlayerLocale() {
		return playerLocale;
	}

	public String getCountry() {
		return playerLocale.getCountry();
	}

	public String getLanguage() {
		return playerLocale.getLanguage();
	}
}
