package com.pokegoapi.go.pokemon.spec;

import com.github.aeonlucid.pogoprotos.Data.PokemonDisplay;
import com.github.aeonlucid.pogoprotos.Enums.PokemonFamilyId;
import com.github.aeonlucid.pogoprotos.Enums.PokemonId;
import com.github.aeonlucid.pogoprotos.Enums.PokemonMove;
import com.github.aeonlucid.pogoprotos.inventory.Item;

public interface Pokemon {

    /**
     * @return the amount of candy available for this pokemon
     */
    int getCandy();

    PokemonFamilyId getPokemonFamily();

    long getId();

    PokemonId getPokemonId();

    int getCp();

    int getMaxStamina();

    int getStamina();

    PokemonMove getMove1();

    PokemonMove getMove2();

    String getDeployedFortId();

    boolean isDeployed();

    String getOwnerName();

    boolean isEgg();

    double getEggKmWalkedTarget();

    double getEggKmWalkedStart();

    int getOrigin();

    float getHeight();

    float getWeight();

    int getIndividualAttack();

    int getIndividualDefense();

    int getIndividualStamina();

    /**
     * Calculates the pokemons IV ratio.
     *
     * @return the pokemons IV ratio as a double between 0 and 1.0, 1.0 being perfect IVs
     */
    double getIvRatio();

    float getCpMultiplier();

    float getAdditionalCpMultiplier();

    float getCombinedCpMultiplier();

    Item.ItemId getPokeball();

    long getCapturedS2CellId();

    int getBattlesAttacked();

    int getBattlesDefended();

    String getEggIncubatorId();

    long getCreationTimeMs();

    /**
     * Checks whether the Pokémon is set as favorite.
     *
     * @return true if the Pokémon is set as favorite
     */
    boolean isFavorite();

    String getNickname();

    boolean isFromFort();

    int getBaseStamina();

    double getBaseCaptureRate();

    int getCandiesToEvolve();

    double getBaseFleeRate();

    float getLevel();

    /**
     * Calculate the maximum CP for this individual pokemon when the player is at level 40
     *
     * @return The maximum CP for this pokemon
     */
    int getMaxCp();

    /**
     * Calculate the maximum CP for this individual pokemon and this player's level
     *
     * @return The maximum CP for this pokemon
     */
    int getMaxCpForPlayer();

    /**
     * Calculates the absolute maximum CP for all pokemons with this PokemonId
     *
     * @return The absolute maximum CP
     */
    int getAbsoluteMaxCp();

    /**
     * Calculated the max cp of this pokemon, if you upgrade it fully and the player is at level 40
     *
     * @param highestEvolution the full evolution path
     * @return Max cp of this pokemon
     */
    int getCpFullEvolveAndPowerup(PokemonId highestEvolution);
    /**
     * Calculated the max cp of this pokemon, if you upgrade it fully with your current player level
     *
     * @param highestEvolution the full evolution path
     * @return Max cp of this pokemon
     */
    int getMaxCpFullEvolveAndPowerupForPlayer(PokemonId highestEvolution);

    /**
     * Calculated the max cp of this pokemon, if you upgrade it fully with your current player level
     *
     * @param playerLevel the current player level
     * @param highestEvolution the full evolution path
     * @return Max cp of this pokemon
     */
    int getMaxCpFullEvolveAndPowerup(int playerLevel, PokemonId highestEvolution);

    /**
     * Calculate the CP after evolving this Pokemon
     *
     * @param evolution the pokemon evolving into
     * @return New CP after evolve
     */
    int getCpAfterEvolve(PokemonId evolution);

    /**
     * Calculate the CP after fully evolving this Pokemon
     *
     * @param highestEvolution the pokemon at the top of the evolution chain being evolved into
     * @return New CP after evolve
     */
    int getCpAfterFullEvolve(PokemonId highestEvolution);

    /**
     * @return The number of powerups already done
     */
    int getNumerOfPowerupsDone();

    /**
     * @return The CP for this pokemon after powerup
     */
    int getCpAfterPowerup();

    /**
     * @return Cost of candy for a powerup
     */
    int getCandyCostsForPowerup();

    /**
     * @return Cost of stardust for a powerup
     */
    int getStardustCostsForPowerup();

    /**
     * @return Information about Costumes, Shiny and Gender
     */
    PokemonDisplay getPokemonDisplay();

    /**
     * @return The amount of candy awarded by Buddy
     */
    int getBuddyCandyAwarded();

    /**
     * @return The amount of km walked by Buddy
     */
    float getBuddyTotalKmWalked();

    /**
     * Check if pokemon its injured but not fainted. need potions to heal
     *
     * @return true if pokemon is injured
     */
    boolean isInjured();

    /**
     * check if a pokemon it's died (fainted). need a revive to resurrect
     *
     * @return true if a pokemon is fainted
     */
    boolean hasFainted();

    /**
     * Check if can powers up this pokemon
     *
     * @return the boolean
     */
    boolean canPowerUp();

    /**
     * Check if can powers up this pokemon, you can choose whether or not to consider the max cp limit for current
     * player level passing true to consider and false to not consider.
     *
     * @param considerMaxCPLimitForPlayerLevel Consider max cp limit for actual player level
     * @return the boolean
     */
    boolean canPowerUp(boolean considerMaxCPLimitForPlayerLevel);

    /**
     * Check if can evolve this pokemon
     *
     * @return the boolean
     */
    boolean canEvolve();
    
    Evolution getEvolution();

    /**
     * @return Actual stamina in percentage relative to the current maximum stamina (useful in ProgressBars)
     */
    int getStaminaInPercentage();

    /**
     * Actual cp in percentage relative to the maximum cp that this pokemon can reach
     * at the actual player level (useful in ProgressBars)
     *
     * @return Actual cp in percentage
     */
    int getCPInPercentageActualPlayerLevel();

    /**
     * Actual cp in percentage relative to the maximum cp that this pokemon can reach at player-level 40
     * (useful in ProgressBars)
     *
     * @return Actual cp in percentage
     */
    int getCPInPercentageMaxPlayerLevel();

    /**
     * @return IV in percentage
     */
    double getIvInPercentage();

    /**
     * Returns true if this pokemon is your current buddy
     * @return true if this pokemon is your current buddy
     */
    boolean isBuddy();
}
