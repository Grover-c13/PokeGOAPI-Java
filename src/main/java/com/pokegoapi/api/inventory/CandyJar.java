package com.pokegoapi.api.inventory;

import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import com.pokegoapi.api.PokemonGo;
import lombok.ToString;

import java.util.HashMap;

@ToString
public class CandyJar {
	private PokemonGo pgo;
	private HashMap<PokemonFamilyId, Integer> candies;

	public CandyJar(PokemonGo pgo) {
		this.pgo = pgo;
		candies = new HashMap<PokemonFamilyId, Integer>();
	}

	public void setCandy(PokemonFamilyId family, int candies) {
		this.candies.put(family, candies);
	}

	public int getCandies(PokemonFamilyId family) {
		return this.candies.get(family);
	}
}
