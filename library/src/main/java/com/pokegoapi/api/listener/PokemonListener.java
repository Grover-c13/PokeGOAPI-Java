package com.pokegoapi.api.listener;

import POGOProtos.Enums.EncounterTypeOuterClass.EncounterType;
import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.pokemon.HatchedEgg;

/**
 * Listener for all pokemon related events.
 */
public interface PokemonListener extends Listener {
	/**
	 * Called when an egg is hatched.
	 * @param api the current api
	 * @param hatchedEgg the hatched egg
	 * @return true if this egg should be removed, if not required for later access via Hatchery#getHatchedEggs
	 */
	boolean onEggHatch(PokemonGo api, HatchedEgg hatchedEgg);

	/**
	 * Called when a pokemon is encountered
	 * @param api the current api
	 * @param encounterId the current encounter id
	 * @param pokemon the pokemon encountered
	 * @param encounterType the type of encounter made
	 */
	void onEncounter(PokemonGo api, long encounterId, CatchablePokemon pokemon, EncounterType encounterType);

	/**
	 * Called after a miss or pokeball escape when capturing a pokemon.
	 * @param api the current api
	 * @param pokemon the pokemon being caught
	 * @param pokeball the pokeball being used
	 * @param throwCount the current amount of times a pokeball has been thrown
	 * @return true to abort the capture and false to retry
	 */
	boolean onCatchEscape(PokemonGo api, CatchablePokemon pokemon, Pokeball pokeball, int throwCount);

	/**
	 * Called when your buddy pokemon finds candies
	 * @param api the current api
	 * @param family the candy family type
	 * @param candyCount the amount of candies found
	 */
	void onBuddyFindCandy(PokemonGo api, PokemonFamilyId family, int candyCount);
}
