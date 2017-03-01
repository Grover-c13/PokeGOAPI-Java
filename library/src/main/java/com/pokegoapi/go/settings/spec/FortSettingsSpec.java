package com.pokegoapi.go.settings.spec;

public interface FortSettingsSpec {
    /**
     * @return the distance in meters at which a fort can be interacted with at
     */
    double getInteractionRange();

    /**
     * @return a far distance in meters at which this fort can be interacted with at
     */
    double getFarInteractionRange();

    /**
     * @return the maximum amount of pokemon that can be deployed to a fort
     */
    int getMaxDeployedPokemon();

    /**
     * @return the maximum amount of players that can deploy pokemon to a fort
     */
    int getMaxPlayersDeployedPokemon();

    /**
     * @return the stamina multiplier of a pokemon when deployed to a fort
     */
    double getDeployStaminaMultiplier();

    /**
     * @return the attack multiplier of a pokemon when deployed to a fort
     */
    double getDeployAttackMultiplier();
}
