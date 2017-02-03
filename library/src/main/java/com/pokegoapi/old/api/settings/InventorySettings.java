package com.pokegoapi.old.api.settings;



/**
 * Created by rama on 27/07/16.
 */
public class InventorySettings {

	private int baseBagItems;

	private int maxBagItems;

	private int baseEggs;

	private int basePokemon;

	private int maxPokemon;

	protected void update(POGOProtos.Settings.InventorySettingsOuterClass.InventorySettings inventorySettings) {
		baseBagItems = inventorySettings.getBaseBagItems();
		maxBagItems = inventorySettings.getMaxBagItems();
		baseEggs = inventorySettings.getBaseEggs();
		maxPokemon = inventorySettings.getMaxPokemon();
		basePokemon = inventorySettings.getBasePokemon();
	}
}
