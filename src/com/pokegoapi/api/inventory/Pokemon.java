package com.pokegoapi.api.inventory;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.main.Inventory.PokemonProto;
import com.pokegoapi.requests.PokemonEvolveRequest;
import com.pokegoapi.requests.PokemonTransferRequest;

import lombok.Setter;

public class Pokemon
{
	private PokemonProto proto;
	@Setter PokemonGo pgo;
	
	// API METHODS //
	
	public int transferPokemon()
	{
		PokemonTransferRequest req = new PokemonTransferRequest(getId());
		pgo.getRequestHandler().doRequest(req);
		
		if (req.getStatus() == 1)
		{
			pgo.getPokebank().removePokemon(this);
		}
		
		return req.getCandies();
	}
	
	public EvolutionResult evolve()
	{

		PokemonEvolveRequest req = new PokemonEvolveRequest(getId());
		pgo.getRequestHandler().doRequest(req);
		EvolutionResult result = new EvolutionResult(req.getOutput());
		
		if (result.isSuccessful()) 
		{
			this.pgo.getPokebank().removePokemon(this);
			this.pgo.getPokebank().addPokemon(result.getEvolvedPokemon());
		}
		
		return result;
	}
	
	
	public boolean equals(Pokemon other)
	{
		return (other.getId() == getId());
	}
	
	// DELEGATE METHODS BELOW //
	public Pokemon(PokemonProto proto)
	{
		this.proto = proto;
	}

	public PokemonProto getDefaultInstanceForType() {
		return proto.getDefaultInstanceForType();
	}

	public long getId() {
		return proto.getEntId();
	}

	public int getPokemonId() {
		return proto.getPokemonId();
	}

	public int getCp() {
		return proto.getCp();
	}

	public int getStamina() {
		return proto.getStamina();
	}

	public int getMaxStamina() {
		return proto.getMaxStamina();
	}

	public int getMove1() {
		return proto.getMove1();
	}

	public int getMove2() {
		return proto.getMove2();
	}

	public int getDeployedFortId() {
		return proto.getDeployedFortId();
	}

	public String getOwnerName() {
		return proto.getOwnerName();
	}

	public boolean getIsEgg() {
		return proto.getIsEgg();
	}

	public double getEggKmWalkedTarget() {
		return proto.getEggKmWalkedTarget();
	}

	public double getEggKmWalkedStart() {
		return proto.getEggKmWalkedStart();
	}

	public int getOrigin() {
		return proto.getOrigin();
	}

	public float getHeightM() {
		return proto.getHeightM();
	}

	public int getIndividualAttack() {
		return proto.getIndividualAttack();
	}

	public int getIndividualDefense() {
		return proto.getIndividualDefense();
	}

	public int getIndividualStamina() {
		return proto.getIndividualStamina();
	}

	public float getCpMultiplier() {
		return proto.getCpMultiplier();
	}

	public int getPokeball() {
		return proto.getPokeball();
	}

	public long getCapturedS2CellId() {
		return proto.getCapturedS2CellId();
	}

	public int getBattlesAttacked() {
		return proto.getBattlesAttacked();
	}

	public int getBattlesDefended() {
		return proto.getBattlesDefended();
	}

	public int getEggIncubatorId() {
		return proto.getEggIncubatorId();
	}

	public long getCreationTimeMs() {
		return proto.getCreationTimeMs();
	}

	public boolean getFavorite() {
		return proto.getFavorite();
	}

	public String getNickname() {
		return proto.getNickname();
	}

	public boolean getFromFort() {
		return proto.getFromFort();
	}
	
	
}
