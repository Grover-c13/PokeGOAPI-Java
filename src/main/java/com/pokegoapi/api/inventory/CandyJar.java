package com.pokegoapi.api.inventory;

import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import POGOProtos.Inventory.PokemonFamilyOuterClass.PokemonFamily;
import com.pokegoapi.api.PokemonGo;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ToString
public class CandyJar
{
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
