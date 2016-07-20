package com.pokegoapi.api.inventory;

import POGOProtos.Inventory.PokemonFamilyOuterClass.PokemonFamily;
import com.pokegoapi.api.PokemonGo;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class CandyJar
{
	private PokemonGo pgo;
	private List<Integer> candies;

	public CandyJar(PokemonGo pgo) {
		this.pgo = pgo;
		candies = new ArrayList<Integer>();
	}

	public void setCandy(PokemonFamily family, int candies) {
		this.candies.set(family.getFamilyId().getNumber(), candies);
	}

	public int getCandies(PokemonFamily family) {
		return this.candies.get(family.getFamilyId().getNumber());
	}
}
