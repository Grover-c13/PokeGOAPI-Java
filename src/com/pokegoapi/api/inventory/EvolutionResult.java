package com.pokegoapi.api.inventory;

import com.pokegoapi.main.Inventory.EvolvePokemonOutProto;


public class EvolutionResult 
{
	private EvolvePokemonOutProto proto;
	private Pokemon pokemon;
	
	public EvolutionResult(EvolvePokemonOutProto proto)
	{
		this.proto = proto;
		this.pokemon = new Pokemon(proto.getEvolvedPokemon());
	}

	public int getResult() {
		return proto.getResult();
	}

	public Pokemon getEvolvedPokemon() {
		return pokemon;
	} 

	public int getExpAwarded() {
		return proto.getExpAwarded();
	}

	public int getCandyAwarded() {
		return proto.getCandyAwarded();
	}
	
	
	public boolean isSuccessful()
	{
		return (this.getResult() == 1);
	}
	
	
	
}
