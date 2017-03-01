package com.pokegoapi.go.settings.spec;

public interface LevelSettingsSpec {
    /**
     * @return the pokemon CP multiplier for this trainer
     */
    double getTrainerCpMultiplier();

    /**
     * @return the game difficulty modifier for this trainer
     */
    double getTrainerDifficultyModifier();
}
