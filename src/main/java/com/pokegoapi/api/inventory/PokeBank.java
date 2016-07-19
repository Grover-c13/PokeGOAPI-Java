package com.pokegoapi.api.inventory;

import com.pokegoapi.api.PokemonGo;
import java8.util.function.Predicate;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import java.util.ArrayList;
import java.util.List;


public class PokeBank {
	List<Pokemon> pokemons = new ArrayList<Pokemon>();
	PokemonGo instance;

	public PokeBank(PokemonGo instance) {
		this.instance = instance;
	}

	public void addPokemon(Pokemon pokemon) {
		pokemon.setPgo(instance);
		pokemons.add(pokemon);
	}

	public List<Pokemon> getPokemonByPokemonId(final int id) {
		return StreamSupport.stream(pokemons).filter(new Predicate<Pokemon>() {
			@Override
			public boolean test(Pokemon pokemon) {
				return pokemon.getId() == id;
			}
		}).collect(Collectors.<Pokemon>toList());
	}

	public void removePokemon(final Pokemon pokemon) {
		pokemons = StreamSupport.stream(pokemons).filter(new Predicate<Pokemon>() {
			@Override
			public boolean test(Pokemon pokemn) {
				return pokemn.getId() != pokemon.getId();
			}
		}).collect(Collectors.<Pokemon>toList());
	}

	public List<Pokemon> getPokemons() {
		return pokemons;
	}

	public void setInstance(PokemonGo instance) {
		this.instance = instance;
	}
}
