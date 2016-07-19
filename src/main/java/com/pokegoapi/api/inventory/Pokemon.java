package com.pokegoapi.api.inventory;

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Enums.PokemonMoveOuterClass;
import POGOProtos.Networking.Requests.Messages.EvolvePokemonMessageOuterClass.EvolvePokemonMessage;
import POGOProtos.Networking.Requests.Messages.ReleasePokemonMessageOuterClass.ReleasePokemonMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass;
import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass.EvolvePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result;
import lombok.Setter;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.main.ServerRequest;

public class Pokemon {

	@Setter PokemonGo pgo;
	private PokemonData proto;

	// API METHODS //

	// DELEGATE METHODS BELOW //
	public Pokemon(PokemonData proto) {
		this.proto = proto;
	}

	public Result transferPokemon() {
		try
		{
			ReleasePokemonMessage reqMsg = ReleasePokemonMessage.newBuilder().setPokemonId(getId()).build();
			
			ServerRequest serverRequest = new ServerRequest(RequestType.RELEASE_POKEMON, reqMsg);
			pgo.getRequestHandler().request(serverRequest);
			pgo.getRequestHandler().sendServerRequests();
			
			ReleasePokemonResponse response = ReleasePokemonResponse.parseFrom(serverRequest.getData());
			
			if (response.getResult().equals(Result.SUCCESS)) {
				pgo.getPokebank().removePokemon(this);
				pgo.getPokebank().removePokemon(this);
			}

			return response.getResult();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return ReleasePokemonResponse.Result.FAILED;
	}

	public EvolutionResult evolve() {
		try
		{
			EvolvePokemonMessage reqMsg = EvolvePokemonMessage.newBuilder().setPokemonId(getId()).build();
			
			ServerRequest serverRequest = new ServerRequest(RequestType.EVOLVE_POKEMON, reqMsg);
			pgo.getRequestHandler().request(serverRequest);
			pgo.getRequestHandler().sendServerRequests();
			
			EvolvePokemonResponse response = EvolvePokemonResponseOuterClass.EvolvePokemonResponse.parseFrom(serverRequest.getData());

			EvolutionResult result = new EvolutionResult(response);

			if (result.isSuccessful()) {
				pgo.getPokebank().removePokemon(this);
				pgo.getPokebank().addPokemon(result.getEvolvedPokemon());
			}
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean equals(Pokemon other) {
		return (other.getId() == getId());
	}

	public PokemonData getDefaultInstanceForType() {
		return proto.getDefaultInstanceForType();
	}

	public long getId() {
		return proto.getId();
	}

	public PokemonIdOuterClass.PokemonId getPokemonId() { 
		return proto.getPokemonId();	
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

	public void debug()	{
		System.out.println(proto);
	}
}
