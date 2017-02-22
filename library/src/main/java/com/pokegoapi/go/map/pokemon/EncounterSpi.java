package com.pokegoapi.go.map.pokemon;

import com.github.aeonlucid.pogoprotos.inventory.Item.ItemId;
import com.github.aeonlucid.pogoprotos.networking.Responses.EncounterResponse;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.map.pokemon.spec.EncounterItemResult;
import com.pokegoapi.go.map.pokemon.spec.UsePokeballResult;
import com.pokegoapi.network.exception.RequestFailedException;

public abstract class EncounterSpi {

	protected abstract void engineInitialize(PokemonGoClient client, CatchablePokemon pokemon,
			EncounterResponse encounter);

	protected abstract CatchablePokemon engineGetPokemon();

	protected abstract EncounterResponse.Status engineGetEncounterStatus();

	/**
	 * @return the currently active item, or null if no items are active
	 */
	protected abstract ItemId engineGetActiveItem();

	/**
	 * Uses an item in this encounter
	 *
	 * @param item the item to use
	 * @return the result from this request {@link EncounterItemResult}
	 * @throws RequestFailedException if an exception occurs while sending the request
	 */
	protected abstract EncounterItemResult engineUseItem(ItemId item) throws RequestFailedException;

	/**
	 * Throws a pokeball at the encountered pokemon
	 *
	 * @param item the pokeball item to use
	 * @param hitPosition the normalized hit position
	 * @param reticleSize the normalized hit reticle size
	 * @param spinModifier the spin modifier
	 * @return the result from this request {@link UsePokeballResult}
	 * @throws RequestFailedException if an exception occurs while sending the request
	 */
	protected abstract UsePokeballResult engineUsePokeball(ItemId item, double hitPosition, double reticleSize,
														   double spinModifier) throws RequestFailedException;

}
