package com.pokegoapi.api.map.pokemon.encounter;

import POGOProtos.Data.Capture.CaptureProbabilityOuterClass.CaptureProbability;
import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.PokemonDetails;


public abstract class Encounter extends PokemonDetails implements EncounterResult {

	public Encounter(PokemonGo api, PokemonData proto) {
		super(api, proto);
	}

	/**
	 * Gets the IV percentage for this encounter
	 *
	 * @return the IV percentage for this encounter
	 */
	public double getPercentageIV() {
		double ivStamina = getPokemonData().getIndividualStamina();
		double ivAttack = getPokemonData().getIndividualAttack();
		double ivDefense = getPokemonData().getIndividualDefense();
		return (ivAttack + ivDefense + ivStamina) * 100 / 45.0;
	}

	/**
	 * Return the status of the encounter
	 *
	 * @return status of results
	 */
	@Override
	public abstract EncounterResponse.Status getStatus();

	@Override
	public abstract boolean wasSuccessful();

	@Override
	public abstract CaptureProbability getCaptureProbability();

	@Override
	public abstract PokemonData getPokemonData();
}
