package com.pokegoapi.api.inventory;

import POGOProtos.Data.PokemonOuterClass;
import POGOProtos.Enums.PokemonMoveOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.requests.PokemonEvolveRequest;
import com.pokegoapi.requests.PokemonTransferRequest;

import lombok.Setter;

public class Pokemon
{
	private PokemonOuterClass.Pokemon proto;
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
	public Pokemon(PokemonOuterClass.Pokemon proto)
	{
		this.proto = proto;
	}

	public PokemonOuterClass.Pokemon getDefaultInstanceForType() {
		return proto.getDefaultInstanceForType();
	}

	public long getId() {
		return proto.getId();
	}

	public int getPokemonId() {
		return proto.getId();
	}

	public int getCp() {
		return proto.getCp();
	}

	public int getStamina() {
		return proto.getStamina();
	}

	public int getMaxStamina() {
		return proto.getStaminaMax();
	}

	public PokemonMoveOuterClass.PokemonMove getMove1() {
		return proto.getMove1();
	}

	public PokemonMoveOuterClass.PokemonMove getMove2() {
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
		return proto.getCapturedCellId();
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
		return proto.getFavorite() > 0;
	}

	public String getNickname() {
		return proto.getNickname();
	}

	public boolean getFromFort() {
		return proto.getFromFort() > 0;
	}
	
	
}
