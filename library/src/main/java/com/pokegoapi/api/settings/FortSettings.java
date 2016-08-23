package com.pokegoapi.api.settings;

import POGOProtos.Settings.FortSettingsOuterClass;
import lombok.Getter;

/**
 * Created by rama on 27/07/16.
 */
public class FortSettings {

	@Getter
	/**
	 * Min distance to interact with the fort
	 *
	 * @return distance in meters.
	 */
	private double interactionRangeInMeters;

	@Getter
	/**
	 * NOT SURE: max number of pokemons in the fort
	 *
	 * @return number of pokemons.
	 */
	private int maxTotalDeployedPokemon;

	@Getter
	/**
	 * NOT SURE: max number of players who can add pokemons to the fort
	 *
	 * @return number of players.
	 */
	private int maxPlayerDeployedPokemon;

	@Getter
	/**
	 * Stamina multiplier
	 *
	 * @return multiplier.
	 */
	private double deployStaminaMultiplier;

	@Getter
	/**
	 * Attack multiplier
	 *
	 * @return multiplier.
	 */
	private double deployAttackMultiplier;

	@Getter
	/**
	 * NO IDEA
	 *
	 * @return distance in meters.
	 */
	private double farInteractionRangeMeters;

	FortSettings() {
	}

	/**
	 * Update the fort settings from the network response.
	 *
	 * @param fortSettings the new fort settings
	 */
	void update(FortSettingsOuterClass.FortSettings fortSettings) {
		interactionRangeInMeters = fortSettings.getInteractionRangeMeters();
		maxTotalDeployedPokemon = fortSettings.getMaxTotalDeployedPokemon();
		maxPlayerDeployedPokemon = fortSettings.getMaxPlayerDeployedPokemon();
		deployStaminaMultiplier = fortSettings.getDeployStaminaMultiplier();
		deployAttackMultiplier = fortSettings.getDeployAttackMultiplier();
		farInteractionRangeMeters = fortSettings.getFarInteractionRangeMeters();
	}
}
