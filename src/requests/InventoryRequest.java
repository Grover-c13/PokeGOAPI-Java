package requests;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import api.ContactSettings;
import api.DailyBonus;
import api.PlayerAvatar;
import api.PlayerProfile;
import api.Team;
import main.Pokemon.ClientPlayerDetails;
import main.Pokemon.InventoryRequestProto.Builder;
import main.Pokemon.InventoryResponseProto;
import main.Pokemon.Payload;
import main.Pokemon;
import main.Request;

public class InventoryRequest extends Request {

	private Builder builder;
	public int getRpcId()
	{
		return 4;
	}

	public InventoryRequest()
	{
		builder = Pokemon.InventoryRequestProto.newBuilder();
	}

	public void setTimestamp(long timestamp)
	{
		builder.setTimestamp(timestamp);
	}


	public void handleResponse(Payload payload)
	{
		try
		{
			InventoryResponseProto response = InventoryResponseProto.parseFrom(payload.getData());
			System.out.println(response);
		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
	}


	public ByteString getInput() 
	{
		return builder.build().toByteString();
	}




}
