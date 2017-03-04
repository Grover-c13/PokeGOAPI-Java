package com.pokegoapi.go.settings.spec;

public interface InventorySettingsSpec {
    /**
     * @return the capacity of the item bag before any upgrades have been applied to it
     */
    int getMinimumItemBagCapacity();

    /**
     * @return the capacity of the item bag after it has been fully upgraded
     */
    int getMaximumItemBagCapacity();

    /**
     * @return the capacity of the pokebank before any upgrades have been applied to it
     */
    int getMinimumPokemonCapacity();

    /**
     * @return the capacity of the pokebank after it has been fully upgraded
     */
    int getMaximumPokemonCapacity();

    /**
     * @return the maximum amount of eggs that can be held in the inventory
     */
    int getEggCapacity();
}
