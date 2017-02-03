package com.pokegoapi.old.api.pokemon;

import POGOProtos.Enums.PokemonIdOuterClass;


import java.util.Random;

public enum StarterPokemon {
	BULBASAUR(PokemonIdOuterClass.PokemonId.BULBASAUR),
	SQUIRTLE(PokemonIdOuterClass.PokemonId.SQUIRTLE),
	CHARMANDER(PokemonIdOuterClass.PokemonId.CHARMANDER);


	private PokemonIdOuterClass.PokemonId pokemon;

	StarterPokemon(PokemonIdOuterClass.PokemonId pokemon) {
		this.pokemon = pokemon;
	}

	public static StarterPokemon random() {
		Random random = new Random();
		return StarterPokemon.values()[random.nextInt(StarterPokemon.values().length)];
	}
}
