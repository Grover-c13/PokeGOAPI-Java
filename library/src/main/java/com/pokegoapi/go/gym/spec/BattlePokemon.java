package com.pokegoapi.go.gym.spec;

import com.github.aeonlucid.pogoprotos.Data;
import com.github.aeonlucid.pogoprotos.data.Battle;

/**
 * Created by chris on 2/2/2017.
 */
public class BattlePokemon {

    private final Data.PokemonData pokemon;
    private int health;
    private int maxHealth;
    private int energy;

    BattlePokemon(Battle.BattlePokemonInfo activePokemon) {
        this.health = activePokemon.getCurrentHealth();
        this.energy = activePokemon.getCurrentEnergy();
        this.pokemon = activePokemon.getPokemonData();
        this.maxHealth = pokemon.getStaminaMax();
    }

    public Data.PokemonData getPokemon() {
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