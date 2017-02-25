package com.pokegoapi.go.gym.spec;

import com.pokegoapi.go.pokemon.spec.Pokemon;

public interface BattlePokemon {

    Pokemon getPokemon();

    int getHealth();

    int getMaxHealth();

    int getEnergy();

    void setHealth(int health);

    void setEnergy(int energy);
}