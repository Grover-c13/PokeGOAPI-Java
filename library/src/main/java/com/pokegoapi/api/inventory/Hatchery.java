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

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Inventory.InventoryItemDataOuterClass;
import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Networking.Responses.GetHatchedEggsResponseOuterClass.GetHatchedEggsResponse;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass.GetInventoryResponse;
import com.pokegoapi.api.pokemon.EggPokemon;
import com.pokegoapi.api.pokemon.HatchedEgg;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class Hatchery {
	private final Map<Long, EggPokemon> eggMap = new ConcurrentHashMap<>();
	private final Inventories inventories;
	private final ExecutorService executorService;
	@Setter
	private Callback callback;

	Hatchery(GetInventoryResponse getInventoryResponse, GetHatchedEggsResponse getHatchedEggsResponse,
			 Inventories inventories, ExecutorService executorService) {
		this.executorService = executorService;
		this.inventories = inventories;
		update(getInventoryResponse);
		update(getHatchedEggsResponse);
	}

	final void update(GetInventoryResponse getInventoryResponse) {
		List<Long> currentItems = new LinkedList<>();
		for (InventoryItemOuterClass.InventoryItem inventoryItem : getInventoryResponse.getInventoryDelta().getInventoryItemsList()) {
			InventoryItemDataOuterClass.InventoryItemData itemData = inventoryItem.getInventoryItemData();
			if (itemData.getPokemonData().getPokemonId() == PokemonIdOuterClass.PokemonId.MISSINGNO && itemData.getPokemonData().getIsEgg()) {
				eggMap.put(itemData.getPokemonData().getId(), new EggPokemon(itemData.getPokemonData(), inventories));
				currentItems.add(itemData.getPokemonData().getId());
			}
		}
		eggMap.keySet().retainAll(currentItems);
	}

	final void update(GetHatchedEggsResponse response) {
		if (callback == null) {
			return;
		}
		for (int i = 0; i < response.getPokemonIdCount(); i++) {
			final HatchedEgg hatchedEgg = new HatchedEgg(response.getPokemonId(i),
					response.getExperienceAwarded(i),
					response.getCandyAwarded(i),
					response.getStardustAwarded(i));
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					callback.hatchedEgg(hatchedEgg);
				}
			});
		}
	}

	public Collection<EggPokemon> getEggs() {
		return eggMap.values();
	}

	public interface Callback {
		void hatchedEgg(HatchedEgg hatchedEgg);
	}

}
