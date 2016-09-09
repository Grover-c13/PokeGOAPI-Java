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

package com.pokegoapi.api.map.fort;

import POGOProtos.Inventory.Item.ItemIdOuterClass;
import POGOProtos.Map.Fort.FortDataOuterClass;
import POGOProtos.Map.Fort.FortModifierOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.request.RequestFailedException;
import java.util.List;

/**
 * Created by mjmfighter on 7/20/2016.
 */
public class Pokestop extends Fort {

	/**
	 * Instantiates a new Pokestop.
	 *
	 * @param api the api
	 * @param fortData the fort data
	 */
	public Pokestop(PokemonGo api, FortDataOuterClass.FortData fortData) {
		super(api, fortData);
	}

	/**
	 * Returns whether this pokestop has an active lure.
	 *
	 * @return lure status
	 */
	@Deprecated
	public boolean hasLurePokemon() {
		return fortData.hasLureInfo() && fortData.getLureInfo().getLureExpiresTimestampMs() > api.startTime;
	}

	/**
	 * Returns whether or not the lured pokemon is in range.
	 *
	 * @return true when the lured pokemon is in range of player
	 */
	public boolean inRangeForLuredPokemon() {
		return getDistance() <= api.settings.mapSettings.pokemonVisibilityRange;
	}

	/**
	 * Returns whether this pokestop has an active lure when detected on map.
	 *
	 * @return true if this pokestop currently has a lure active
	 */
	public boolean hasLure() {
		try {
			return hasLure(false);
		} catch (RequestFailedException e) {
			return false;
		}
	}

	/**
	 * Returns whether this pokestop has an active lure.
	 *
	 * @param updateFortDetails to make a new request and get updated lured status
	 * @return lure status
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public boolean hasLure(boolean updateFortDetails) throws RequestFailedException {
		if (updateFortDetails) {
			List<FortModifierOuterClass.FortModifier> modifiers = getDetails().getModifier();
			for (FortModifierOuterClass.FortModifier modifier : modifiers) {
				if (modifier.getItemId() == ItemIdOuterClass.ItemId.ITEM_TROY_DISK) {
					return true;
				}
			}
			return false;
		}

		return fortData.getActiveFortModifierList().contains(ItemIdOuterClass.ItemId.ITEM_TROY_DISK);
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Pokestop && ((Pokestop) obj).getId().equals(getId());
	}
}
