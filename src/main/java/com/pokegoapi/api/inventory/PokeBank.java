package com.pokegoapi.api.inventory;

import POGOProtos.Enums.PokemonIdOuterClass;
import com.pokegoapi.api.PokemonGo;
import java8.util.function.Predicate;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class PokeBank {
	@Getter List<Pokemon> pokemons = new ArrayList<Pokemon>();
	@Getter PokemonGo instance;

	public PokeBank(PokemonGo instance) {
		this.instance = instance;
	}

	public void addPokemon(Pokemon pokemon) {
		pokemon.setPgo(instance);
		pokemons.add(pokemon);
	}

	public List<Pokemon> getPokemonByPokemonId(final PokemonIdOuterClass.PokemonId id) {
		return StreamSupport.stream(pokemons).filter(new Predicate<Pokemon>() {
			@Override
			public boolean test(Pokemon pokemon) {
				return pokemon.getPokemonId().equals(id);
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
}
