package com.pokegoapi.api.inventory;

import java.util.ArrayList;
import java.util.List;

import com.pokegoapi.api.PokemonGo;

import rx.Observable;
import rx.functions.Func1;


public class PokeBank {
	List<Pokemon> pokemons = new ArrayList<Pokemon>();
	PokemonGo instance;
	
	public PokeBank(PokemonGo instance)
	{
		this.instance = instance;
	}
	
	public void addPokemon(Pokemon pokemon)
	{
		pokemon.setPgo(instance);
		pokemons.add(pokemon);
	}
	
	public List<Pokemon> getPokemonByPokemonId(final int id) {
		return Observable.from(pokemons).filter(new Func1<Pokemon, Boolean>() {
			public Boolean call(Pokemon pokemon) {
				return pokemon.getId() == id;
			}
		}).toList().toBlocking().first();
	}
	
	public void removePokemon(final Pokemon pokemon) {
		pokemons = Observable.from(pokemons)
				.filter(new Func1<Pokemon, Boolean>() {
					public Boolean call(Pokemon pokemn) {
						return pokemn.getId() != pokemon.getId();
					}
				}).toList().toBlocking().first();
	}

	public List<Pokemon> getPokemons() {
		return pokemons;
	}

	public void setInstance(PokemonGo instance) {
		this.instance = instance;
	}
}
