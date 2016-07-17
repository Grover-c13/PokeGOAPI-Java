package com.pokegoapi.api;

import com.pokegoapi.main.Stops.FortDetailsOutProto;

public class FortDetails 
{
	private FortDetailsOutProto proto;
	public FortDetails(FortDetailsOutProto proto)
	{
		this.proto = proto;
		
	}
	public String getId() {
		return proto.getId();
	}
	public int getTeam() {
		return proto.getTeam();
	}
	public String getName() {
		return proto.getName();
	}
	public String getImageUrl() {
		return proto.getImageUrl();
	}
	public int getFp() {
		return proto.getFp();
	}
	public int getStamina() {
		return proto.getStamina();
	}
	public int getMaxStamina() {
		return proto.getMaxStamina();
	}
	public int getFortType() {
		return proto.getFortType();
	}
	public double getLatitude() {
		return proto.getLatitude();
	}
	public double getLongitude() {
		return proto.getLongitude();
	}
	public String getDescription() {
		return proto.getDescription();
	}
	public double getModifier() {
		return proto.getModifier();
	}
	
	
	
	
}
