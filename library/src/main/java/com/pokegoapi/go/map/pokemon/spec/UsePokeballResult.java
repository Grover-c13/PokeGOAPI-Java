package com.pokegoapi.go.map.pokemon.spec;

import POGOProtos.Data.Capture.CaptureAwardOuterClass.CaptureAward;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CaptureReason;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;
import com.pokegoapi.go.map.pokemon.Encounter;

/**
 * The result from using a pokeball on an {@link Encounter}
 */
public interface UsePokeballResult {

	/**
	 * @return true if this pokemon was captured
	 */
	boolean wasCaptured();

	/**
	 * @return true if this pokemon has fled
	 */
	boolean hasFled();

	/**
	 * @return true if this pokemon has escaped this pokeball
	 */
	boolean hasEscaped();

	/**
	 * @return the status of this request
	 */
	CatchStatus getStatus();

	/**
	 * @return the encounter in which this pokeball was thrown
	 */
	Encounter getEncounter();

	/**
	 * @return the captured pokemon id
	 */
	long getCapturedPokemonId();

	/**
	 * @return the reason for this capture
	 */
	CaptureReason getCaptureReason();

	/**
	 * @return the awards from this capture
	 */
	CaptureAward getCaptureAwards();

	/**
	 * @return the amount of awarded candy from this capture
	 */
	int getAwardedCandy();

	/**
	 * @return the amount of awarded stardust from this capture
	 */
	int getAwardedStardust();

}
