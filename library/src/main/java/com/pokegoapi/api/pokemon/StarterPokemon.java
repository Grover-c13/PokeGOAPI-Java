package com.pokegoapi.api.pokemon;

import POGOProtos.Enums.PokemonIdOuterClass;
import lombok.Getter;

import java.util.Random;

public enum StarterPokemon {
	BULBASAUR(PokemonIdOuterClass.PokemonId.BULBASAUR),
	SQUIRTLE(PokemonIdOuterClass.PokemonId.SQUIRTLE),
	CHARMANDER(PokemonIdOuterClass.PokemonId.CHARMANDER);

	@Getter
	private PokemonIdOuterClass.PokemonId pokemon;

	StarterPokemon(PokemonIdOuterClass.PokemonId pokemon) {
		this.pokemon = pokemon;
	}

	public static StarterPokemon random() {
		Random random = new Random();
		return StarterPokemon.values()[random.nextInt(StarterPokemon.values().length)];
	}
}
