package com.pokegoapi.api.inventory;

import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass;

public class EvolutionResult {
	
	private EvolvePokemonResponseOuterClass.EvolvePokemonResponse proto;
	private Pokemon pokemon;

	public EvolutionResult(EvolvePokemonResponseOuterClass.EvolvePokemonResponse proto) {
		this.proto = proto;
		this.pokemon = new Pokemon(proto.getEvolvedPokemonData());
	}

	public EvolvePokemonResponseOuterClass.EvolvePokemonResponse.Result getResult() {
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

	public boolean isSuccessful() {
		return (getResult().equals(EvolvePokemonResponseOuterClass.EvolvePokemonResponse.Result.SUCCESS));
	}
}
