package com.pokegoapi.api.pokemon;

import java.util.List;

import POGOProtos.Enums.PokemonIdOuterClass;

public class EvolutionForm {
	private PokemonIdOuterClass.PokemonId pokemonId;

	EvolutionForm(PokemonIdOuterClass.PokemonId pokemonId) {
		this.pokemonId = pokemonId;
	}

	public boolean isFullyEvolved() {
		return EvolutionInfo.isFullyEvolved(pokemonId);
	}

	public List<EvolutionForm> getEvolutionForms() {
		return EvolutionInfo.getEvolutionForms(pokemonId);
	}

	public int getEvolutionStage() {
		return EvolutionInfo.getEvolutionStage(pokemonId);
	}

	public PokemonIdOuterClass.PokemonId getPokemonId() {
		return pokemonId;
	}
}
