package com.pokegoapi.go.gym.spec;

import POGOProtos.Data.Battle.BattlePokemonInfoOuterClass.BattlePokemonInfo;
import POGOProtos.Data.PokemonDataOuterClass.PokemonData;

/**
 * Created by chris on 2/2/2017.
 */
public class BattlePokemon {

    private final PokemonData pokemon;
    private int health;
    private int maxHealth;
    private int energy;

    BattlePokemon(BattlePokemonInfo activePokemon) {
        this.health = activePokemon.getCurrentHealth();
        this.energy = activePokemon.getCurrentEnergy();
        this.pokemon = activePokemon.getPokemonData();
        this.maxHealth = pokemon.getStaminaMax();
    }

    public PokemonData getPokemon() {
        return pokemon;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getEnergy() {
        return energy;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}