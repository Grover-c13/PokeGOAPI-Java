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

package com.pokegoapi.api.inventory;

import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import POGOProtos.Inventory.InventoryItemDataOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import lombok.ToString;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ToString
public class CandyJar {
	private final Map<PokemonFamilyId, Integer> candies = new EnumMap<>(PokemonFamilyId.class);

	CandyJar(GetInventoryResponse getInventoryResponse) {
		update(getInventoryResponse);
	}

	final void update(GetInventoryResponse getInventoryResponse) {
		List<PokemonFamilyId> currentItems = new LinkedList<>();
		for (InventoryItemOuterClass.InventoryItem inventoryItem : getInventoryResponse.getInventoryDelta().getInventoryItemsList()) {
			InventoryItemDataOuterClass.InventoryItemData itemData = inventoryItem.getInventoryItemData();
			if (itemData.getCandy().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.UNRECOGNIZED
					&& itemData.getCandy().getFamilyId() != PokemonFamilyIdOuterClass.PokemonFamilyId.FAMILY_UNSET) {
				candies.put(itemData.getCandy().getFamilyId(), itemData.getCandy().getCandy());
				currentItems.add(itemData.getCandy().getFamilyId());
			}
		}
		for (PokemonFamilyId pokemonFamilyId : PokemonFamilyId.values()) {
			if (currentItems.contains(pokemonFamilyId)) {
				continue;
			}
			candies.put(pokemonFamilyId, 0);
		}
	}

	/**
	 * Get number of candies from the candyjar.
	 *
	 * @param family Pokemon family id
	 * @return number of candies in jar
	 */
	public int getCandies(PokemonFamilyId family) {
		return candies.get(family);
	}
}
