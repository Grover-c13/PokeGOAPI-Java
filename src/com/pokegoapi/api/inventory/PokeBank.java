package com.pokegoapi.api.inventory;

import java.util.ArrayList;
import java.util.List;
import com.pokegoapi.api.PokemonGo;

public class PokeBank {
	List<PokemonDetails> pokemons;
	private PokemonGo instance;
	
	public PokeBank(PokemonGo instance)
	{
		this.instance = instance;
		pokemons = new ArrayList<PokemonDetails>();
	}
	
	public void addPokemon(PokemonDetails pokemon)
	{
		pokemon.setAPIInstance(instance);
		pokemons.add(pokemon);
	}
	
	
	public List<PokemonDetails> getPokemonByPokemonId(int id)
	{
		List<PokemonDetails> list = new ArrayList<PokemonDetails>();
		for (PokemonDetails details : pokemons)
		{
			if (details.getPokemonId() == id)
			{
				list.add(details);
			}
		}
		
		return list;
	}
	
	

	
	public List<PokemonDetails> getPokemon()
	{
		return pokemons;
	}
	
	
}
