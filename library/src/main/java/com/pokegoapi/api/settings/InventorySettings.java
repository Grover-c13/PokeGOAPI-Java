package com.pokegoapi.api.settings;

import POGOProtos.Settings.InventorySettingsOuterClass;
import lombok.Getter;

/**
 * Created by rama on 27/07/16.
 */
public class InventorySettings {
	@Getter
	private int baseBagItems;
	@Getter
	private int maxBagItems;
	@Getter
	private int baseEggs;
	@Getter
	private int basePokemon;
	@Getter
	private int maxPokemon;

	InventorySettings() {
	}

	void update(InventorySettingsOuterClass.InventorySettings inventorySettings) {
		baseBagItems = inventorySettings.getBaseBagItems();
		maxBagItems = inventorySettings.getMaxBagItems();
		baseEggs = inventorySettings.getBaseEggs();
		maxPokemon = inventorySettings.getMaxPokemon();
		basePokemon = inventorySettings.getBasePokemon();
	}
}
