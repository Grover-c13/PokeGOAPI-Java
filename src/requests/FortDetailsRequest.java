package requests;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import main.Pokemon;
import main.Request;
import main.Pokemon.FortDetailsOutProto;
import main.Pokemon.FortDetailsProto;
import main.Pokemon.FortDetailsProto.Builder;
import main.Pokemon.Payload;

public class FortDetailsRequest extends Request
{
	private String id; // input for the proto
	private long latitude; // input for the proto
	private long longitude;  // input for the proto
	private Builder builder;
	
	private FortDetailsOutProto output;
	
	public FortDetailsRequest(String id)
	{
		builder = Pokemon.FortDetailsProto.newBuilder();
		builder.setId(id);
	}
	
	
	
	public long getLatitude() {
		return latitude;
	}



	public void setLatitude(long latitude) {
		this.latitude = latitude;
		builder.setLatitude(latitude); // need to update the proto with this value
	}



	public double getLongitude() {
		return longitude;
	}



	public void setLongitude(long longitude) {
		this.longitude = longitude;
		builder.setLongitude(longitude); // need to update the proto with this value
	}

	public FortDetailsOutProto getOutput()
	{
		return output;
	}

	@Override
	public int getRpcId() 
	{
		return 104;
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
		FortDetailsProto in = builder.build();
		System.out.println(in);
		return in.toByteArray();
	}

}
