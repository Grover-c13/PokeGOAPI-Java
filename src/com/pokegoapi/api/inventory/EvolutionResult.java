package com.pokegoapi.api.inventory;

import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass;
import com.pokegoapi.requests.PokemonEvolveRequest;


public class EvolutionResult 
{
	private EvolvePokemonResponseOuterClass.EvolvePokemonResponse proto;
	private Pokemon pokemon;

	public EvolutionResult(EvolvePokemonResponseOuterClass.EvolvePokemonResponse proto)
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
		return proto.getExperienceAwarded();
	}

	public int getCandyAwarded() {
		return proto.getCandyAwarded();
	}
	
	
	public boolean isSuccessful()
	{
		return (this.getResult() == 1);
	}
	
	
	
}
