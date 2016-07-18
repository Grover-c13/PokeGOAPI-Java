package com.pokegoapi.api.inventory;

import com.pokegoapi.api.PokemonGo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PokeBank {
	@Getter
	List<Pokemon> pokemons = new ArrayList<Pokemon>();
	@Setter
	PokemonGo instance;

	public PokeBank(PokemonGo instance) {
		this.instance = instance;
	}

	public void addPokemon(Pokemon pokemon) {
		pokemon.setPgo(instance);
		pokemons.add(pokemon);
	}

	public List<Pokemon> getPokemonByPokemonId(int id) {
		return pokemons.stream().filter(pokemon -> pokemon.getId() == id).collect(Collectors.toCollection(ArrayList::new));
	}

	public void removePokemon(Pokemon pokemon) {
		pokemons = pokemons.stream().filter(pokmon -> pokmon.getId() != pokemon.getId()).collect(Collectors.toCollection(ArrayList::new));
	}

}
