package com.pokegoapi.go.map.pokemon.spec;

import POGOProtos.Data.Capture.CaptureProbabilityOuterClass.CaptureProbability;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Responses.UseItemEncounterResponseOuterClass.UseItemEncounterResponse.Status;
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
	Status getStatus();

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
