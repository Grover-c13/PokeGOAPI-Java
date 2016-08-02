package com.pokegoapi.api.settings;

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

	protected void update(POGOProtos.Settings.InventorySettingsOuterClass.InventorySettings inventorySettings) {
		baseBagItems = inventorySettings.getBaseBagItems();
		maxBagItems = inventorySettings.getMaxBagItems();
		baseEggs = inventorySettings.getBaseEggs();
		maxPokemon = inventorySettings.getMaxPokemon();
		basePokemon = inventorySettings.getBasePokemon();
	}
}
