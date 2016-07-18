package com.pokegoapi.requests;


import POGOProtos.Networking.Requests.Messages.FortDetailsMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import com.pokegoapi.main.Request;
import lombok.Getter;

public class FortDetailsRequest extends Request
{
	@Getter String id; // input for the proto
	@Getter long latitude; // input for the proto
	@Getter long longitude;  // input for the proto
	private FortDetailsMessageOuterClass.FortDetailsMessage.Builder builder;
	
	@Getter
	FortDetailsResponseOuterClass.FortDetailsResponse output;
	
	public FortDetailsRequest(String id) {
		this(id, 0, 0);
	}
	
	public FortDetailsRequest(String id, long lati, long longi)
	{
		builder = FortDetailsMessageOuterClass.FortDetailsMessage.newBuilder();
		builder.setFortId(id);
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
	public RequestTypeOuterClass.RequestType getRpcId()
	{
		return RequestTypeOuterClass.RequestType.FORT_DETAILS;
	}

	@Override
	public void handleResponse(ByteString payload)
	{
		try
		{
			FortDetailsResponseOuterClass.FortDetailsResponse response = FortDetailsResponseOuterClass.FortDetailsResponse.parseFrom(payload);
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
