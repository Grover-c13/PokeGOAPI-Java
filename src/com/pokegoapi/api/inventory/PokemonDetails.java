package com.pokegoapi.api.inventory;




import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.main.Inventory.PokemonProto;
import com.pokegoapi.requests.PokemonTransferRequest;

public class PokemonDetails
{
	private PokemonProto proto;
	private PokemonGo pgo;
	
	protected void setAPIInstance(PokemonGo pgo)
	{
		this.pgo = pgo;
	}
	
	// API METHODS //
	
	public int transferPokemon()
	{
		PokemonTransferRequest req = new PokemonTransferRequest(this.getId());
		pgo.getRequestHandler().addRequest(req);
		pgo.getRequestHandler().sendRequests();
		
		return req.getCandies();
	}
	
	
	
	// DELEGATE METHODS BELOW //
	public PokemonDetails(PokemonProto proto)
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
