package com.pokegoapi.api.inventory;

import POGOProtos.Data.PokemonDataOuterClass;
import POGOProtos.Enums.PokemonMoveOuterClass;
import POGOProtos.Networking.Requests.Messages.EvolvePokemonMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.ReleasePokemonMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.main.ServerRequest;
import lombok.Setter;

public class Pokemon {
	@Setter
	PokemonGo pgo;
	private PokemonDataOuterClass.PokemonData proto;

	// API METHODS //

	// DELEGATE METHODS BELOW //
	public Pokemon(PokemonDataOuterClass.PokemonData proto) {
		this.proto = proto;
	}

	public ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result transferPokemon() {
		try
		{
			ReleasePokemonMessageOuterClass.ReleasePokemonMessage reqMsg = ReleasePokemonMessageOuterClass.ReleasePokemonMessage.newBuilder()
					.setPokemonId(this.getPokemonId())
					.build();
			ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.RELEASE_POKEMON, reqMsg);
			pgo.getRequestHandler().request(serverRequest);
			pgo.getRequestHandler().sendServerRequests();
			ReleasePokemonResponseOuterClass.ReleasePokemonResponse response = ReleasePokemonResponseOuterClass.ReleasePokemonResponse.parseFrom(serverRequest.getData());


			if (response.getResult().equals(ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result.SUCCESS)) {
				pgo.getPokebank().removePokemon(this);
			}

			return response.getResult();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result.FAILED;
	}

	public EvolutionResult evolve() {
		try
		{
			EvolvePokemonMessageOuterClass.EvolvePokemonMessage reqMsg = EvolvePokemonMessageOuterClass.EvolvePokemonMessage.newBuilder()
					.setPokemonId(this.getPokemonId())
					.build();
			ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.EVOLVE_POKEMON, reqMsg);
			pgo.getRequestHandler().request(serverRequest);
			pgo.getRequestHandler().sendServerRequests();
			EvolvePokemonResponseOuterClass.EvolvePokemonResponse response = EvolvePokemonResponseOuterClass.EvolvePokemonResponse.parseFrom(serverRequest.getData());


			EvolutionResult result = new EvolutionResult(response);

			if (result.isSuccessful()) {
				this.pgo.getPokebank().removePokemon(this);
				this.pgo.getPokebank().addPokemon(result.getEvolvedPokemon());
			}


		}
		catch(Exception e)
		{
			e.printStackTrace();
		}



		return null;
	}

	public boolean equals(Pokemon other) {
		return (other.getId() == getId());
	}

	public PokemonDataOuterClass.PokemonData getDefaultInstanceForType() {
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
