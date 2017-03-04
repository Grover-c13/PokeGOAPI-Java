package com.pokegoapi.go.map.pokemon;

import com.github.aeonlucid.pogoprotos.Enums;
import com.github.aeonlucid.pogoprotos.map.Fort;
import com.github.aeonlucid.pogoprotos.map.Pokemon;
import com.github.aeonlucid.pogoprotos.networking.Responses;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.map.spec.MapPoint;
import com.pokegoapi.network.exception.RequestFailedException;
import com.pokegoapi.provider.GetInstance;
import com.pokegoapi.provider.NoSuchTypeException;
import com.pokegoapi.provider.Provider;

/**
 * An object that represents a catchable pokemon on the map in Pokemon GO
 */
public final class CatchablePokemon implements MapPoint {
	private final CatchablePokemonSpi spi;
	private final Provider provider;

	private CatchablePokemon(CatchablePokemonSpi spi, Provider provider) {
		this.spi = spi;
		this.provider = provider;
	}

	public static CatchablePokemon getInstance() {
		//TODO: Implement getting the default provider and get the instance from it
		return null;
	}

	public static CatchablePokemon getInstance(Provider provider) throws NoSuchTypeException {
		GetInstance.Instance instance = GetInstance.getInstance("CatchablePokemon", CatchablePokemonSpi.class,
				provider);
		return new CatchablePokemon((CatchablePokemonSpi) instance.impl, instance.provider);
	}

	public void initialize(PokemonGoClient client, Pokemon.WildPokemon pokemon) {
		spi.engineInitialize(client, pokemon);
	}

	public void initialize(PokemonGoClient client, Pokemon.MapPokemon pokemon) {
		spi.engineInitialize(client, pokemon);
	}

	public void initialize(PokemonGoClient client, Fort.FortData luredFort) {
		spi.engineInitialize(client, luredFort);
	}

	public void initialize(PokemonGoClient client, Responses.GetIncensePokemonResponse pokemon) {
		spi.engineInitialize(client, pokemon);
	}

	@Override
	public String getMapId() {
		return spi.getMapId();
	}

	@Override
	public double getLatitude() {
		return spi.getLatitude();
	}

	@Override
	public double getLongitude() {
		return spi.getLongitude();
	}

	public Enums.PokemonId getPokemonId() {
		return spi.engineGetPokemonId();
	}

	/**
	 * Calculates the distance from this pokemon to the player
	 *
	 * @return the calculated distance
	 */
	public double getDistance() {
		return spi.engineGetDistance();
	}

	public long getEncounterId() {
		return spi.engineGetEncounterId();
	}

	public String getSpawnPointId() {
		return spi.engineGetSpawnPointId();
	}

	/**
	 * @return the expiration timestamp for this pokemon, -1 if the server has sent none
	 */
	public long getExpirationTimestamp() {
		return spi.engineGetExpirationTimestamp();
	}

	/**
	 * Encounters this pokemon.
	 *
	 * @return the encounter of this pokemon
	 * @throws RequestFailedException if an exception occurs while sending the encounter request
	 */
	public Encounter encounterPokemon() throws RequestFailedException {
		return spi.engineEncounterPokemon();
	}
}
