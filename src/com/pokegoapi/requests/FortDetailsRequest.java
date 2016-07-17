package com.pokegoapi.requests;


import com.google.protobuf.InvalidProtocolBufferException;

import com.pokegoapi.main.Communication;
import com.pokegoapi.main.Request;
import com.pokegoapi.main.Stops.FortDetailsOutProto;
import com.pokegoapi.main.Stops.FortDetailsProto;
import com.pokegoapi.main.Stops.FortDetailsProto.Builder;
import com.pokegoapi.main.Communication.Payload;

import lombok.Getter;

public class FortDetailsRequest extends Request
{
	@Getter String id; // input for the proto
	@Getter long latitude; // input for the proto
	@Getter long longitude;  // input for the proto
	private Builder builder;
	
	@Getter FortDetailsOutProto output;
	
	public FortDetailsRequest(String id) {
		this(id, 0, 0);
	}
	
	public FortDetailsRequest(String id, long lati, long longi)
	{
		builder = FortDetailsProto.newBuilder();
		builder.setId(id);
		setLatitude(lati);
		setLongitude(longi);
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
		builder.setLatitude(latitude); // need to update the proto with this value
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
		builder.setLongitude(longitude); // need to update the proto with this value
	}

	@Override
	public Communication.Method getRpcId()
	{
		return Communication.Method.FORT_DETAILS;
	}

	@Override
	public void handleResponse(Payload payload)
	{
		try
		{
			FortDetailsOutProto response = FortDetailsOutProto.parseFrom(payload.toByteArray());
			output = response;
		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getInput() 
	{
		return builder.build().toByteArray();
	}

}
