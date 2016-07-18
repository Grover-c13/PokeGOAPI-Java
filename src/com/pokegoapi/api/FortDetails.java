package com.pokegoapi.api;


import POGOProtos.Enums.TeamColorOuterClass;
import POGOProtos.Map.Fort.FortModifierOuterClass;
import POGOProtos.Map.Fort.FortTypeOuterClass;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;
import com.google.protobuf.ProtocolStringList;

import java.util.List;

public class FortDetails
{
	private FortDetailsResponseOuterClass.FortDetailsResponse proto;
	public FortDetails(FortDetailsResponseOuterClass.FortDetailsResponse proto)
	{
		this.proto = proto;
	}
	public String getId() {
		return proto.getFortId();
	}
	public TeamColorOuterClass.TeamColor getTeam() {
		return proto.getTeamColor();
	}
	public String getName() {
		return proto.getName();
	}
	public ProtocolStringList getImageUrl() {
		return proto.getImageUrlsList();
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
	public FortTypeOuterClass.FortType getFortType() {
		return proto.getType();
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
	public List<FortModifierOuterClass.FortModifier> getModifier() {
		return proto.getModifiersList();
	}
}
