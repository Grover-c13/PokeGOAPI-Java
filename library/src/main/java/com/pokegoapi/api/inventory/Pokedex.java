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

import POGOProtos.Data.PokedexEntryOuterClass.PokedexEntry;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Inventory.InventoryItemDataOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass;

import java.util.EnumMap;
import java.util.Map;

/**
 * Pokemon pokedex containing information about pokemons, which have been seen and caught.
 */
public class Pokedex {
	private Map<PokemonId, PokedexEntry> pokedexMap = new EnumMap<>(PokemonId.class);

	/**
	 * Constructor for internal use
	 *
	 * @param getInventoryResponse Initial inventory response
	 */
	public Pokedex(GetInventoryResponseOuterClass.GetInventoryResponse getInventoryResponse) {
		update(getInventoryResponse);
	}

	/**
	 * Update call
	 *
	 * @param getInventoryResponse Update the class with information from the response
	 */
	public final void update(GetInventoryResponseOuterClass.GetInventoryResponse getInventoryResponse) {
		for (InventoryItemOuterClass.InventoryItem inventoryItem : getInventoryResponse.getInventoryDelta()
				.getInventoryItemsList()) {
			InventoryItemDataOuterClass.InventoryItemData itemData = inventoryItem.getInventoryItemData();
			if (itemData.hasPokedexEntry()) {
				pokedexMap.put(itemData.getPokedexEntry().getPokemonId(), itemData.getPokedexEntry());
			}
		}
	}

	/**
	 * Get a pokedex entry value.
	 *
	 * @param pokemonId the ID of the pokemon to get
	 * @return Entry if in pokedex or null if it doesn't
	 */
	public PokedexEntry getPokedexEntry(PokemonId pokemonId) {
		return pokedexMap.get(pokemonId);
	}
}
