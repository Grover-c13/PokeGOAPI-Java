package com.pokegoapi.go.map.pokemon;

import com.github.aeonlucid.pogoprotos.Enums.PokemonId;
import com.github.aeonlucid.pogoprotos.map.Fort.FortData;
import com.github.aeonlucid.pogoprotos.map.Pokemon.MapPokemon;
import com.github.aeonlucid.pogoprotos.map.Pokemon.WildPokemon;
import com.github.aeonlucid.pogoprotos.networking.Responses.GetIncensePokemonResponse;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.map.spec.MapPoint;
import com.pokegoapi.network.exception.RequestFailedException;

@SuppressWarnings("WeakerAccess")
public abstract class CatchablePokemonSpi implements MapPoint {

	protected abstract void engineInitialize(PokemonGoClient client, MapPokemon mapPokemon);

	protected abstract void engineInitialize(PokemonGoClient client, WildPokemon wildPokemon);

	protected abstract void engineInitialize(PokemonGoClient client, GetIncensePokemonResponse incense);

	protected abstract void engineInitialize(PokemonGoClient client, FortData luredFort);

	/**
	 * Returns the distance to this pokemon.
	 *
	 * @return the calculated distance
	 */
	protected abstract double engineGetDistance();

	protected abstract PokemonId engineGetPokemonId();

	protected abstract String engineGetSpawnPointId();

	protected abstract long engineGetEncounterId();

	/**
	 * @return the expiration timestamp for this pokemon, -1 if the server has sent none
	 */
	protected abstract long engineGetExpirationTimestamp();

	/**
	 * Encounters this pokemon.
	 *
	 * @return the encounter of this pokemon
	 * @throws RequestFailedException if an exception occurs while sending the encounter request
	 */
	protected abstract Encounter engineEncounterPokemon() throws RequestFailedException;
}
