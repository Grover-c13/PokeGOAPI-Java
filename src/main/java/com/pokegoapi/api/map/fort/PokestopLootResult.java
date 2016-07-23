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

import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass.FortSearchResponse.Result;

import java.util.List;

/**
 * Created by mjmfighter on 7/20/2016.
 */
public class PokestopLootResult {

	private FortSearchResponseOuterClass.FortSearchResponse response;

	public PokestopLootResult(FortSearchResponseOuterClass.FortSearchResponse response) {
		this.response = response;
	}

	public boolean wasSuccessful() {
		return response.getResult() == Result.SUCCESS || response.getResult() == Result.INVENTORY_FULL;
	}

	public Result getResult() {
		return response.getResult();
	}

	public List<ItemAward> getItemsAwarded() {
		return response.getItemsAwardedList();
	}

	public int getExperience() {
		return response.getExperienceAwarded();
	}

	public FortSearchResponseOuterClass.FortSearchResponse toPrimitive() {
		return response;
	}
}
