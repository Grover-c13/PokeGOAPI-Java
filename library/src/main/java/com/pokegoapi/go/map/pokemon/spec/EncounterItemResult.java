package com.pokegoapi.go.map.pokemon.spec;

import com.github.aeonlucid.pogoprotos.data.Capture.CaptureProbability;
import com.github.aeonlucid.pogoprotos.inventory.Item.ItemId;
import com.github.aeonlucid.pogoprotos.networking.Responses.UseItemEncounterResponse;
import com.pokegoapi.go.map.pokemon.Encounter;

/**
 * The result from using an item on an {@link Encounter}
 */
public interface EncounterItemResult {

	/**
	 * @return true if this request was successful
	 */
	boolean wasSuccessful();

	/**
	 * @return the status of this request
	 */
	UseItemEncounterResponse.Status getStatus();

	/**
	 * @return the encounter this item was used on
	 */
	Encounter getEncounter();

	/**
	 * @return the item used for this encounter
	 */
	ItemId getUsedItem();

	/**
	 * @return the capture probability after using this item
	 */
	CaptureProbability getCaptureProbability();

}
