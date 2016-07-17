package com.pokegoapi.api.inventory;

import java.util.ArrayList;
import java.util.List;
import com.pokegoapi.api.PokemonGo;

public class PokeBank {
	List<Pokemon> pokemons;
	private PokemonGo instance;
	
	public PokeBank(PokemonGo instance)
	{
		this.instance = instance;
		pokemons = new ArrayList<Pokemon>();
	}
	
	public void addPokemon(Pokemon pokemon)
	{
		pokemon.setAPIInstance(instance);
		pokemons.add(pokemon);
	}
	
	
	public List<Pokemon> getPokemonByPokemonId(int id)
	{
		List<Pokemon> list = new ArrayList<Pokemon>();
		for (Pokemon details : pokemons)
		{
			if (details.getPokemonId() == id)
			{
				list.add(details);
			}
		}
		
		return list;
	}
	
	protected void removePokemon(Pokemon pokemon)
	{
		List<Pokemon> list = new ArrayList<Pokemon>();
		for (Pokemon details : pokemons)
		{
			if (!details.equals(pokemon))
			{
				list.add(details);
			}
		}
		
		pokemons = list;
	}

	
	public List<Pokemon> getPokemon()
	{
		return pokemons;
	}
	
	
}
