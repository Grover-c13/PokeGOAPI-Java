package requests;

import java.util.LinkedList;
import java.util.List;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import api.ContactSettings;
import api.DailyBonus;
import api.PlayerAvatar;
import api.PlayerProfile;
import api.PokemonDetails;
import api.Team;
import main.Pokemon.ClientPlayerDetails;
import main.Pokemon.InventoryRequestProto.Builder;
import main.Pokemon.InventoryResponseProto;
import main.Pokemon.InventoryResponseProto.InventoryItemResponseProto;
import main.Pokemon.Payload;
import main.Pokemon;
import main.Request;

public class InventoryRequest extends Request {

	private Builder builder;
	private List<PokemonDetails> pokemon;
	

	
	public int getRpcId()
	{
		return 4;
	}

	public InventoryRequest()
	{
		builder = Pokemon.InventoryRequestProto.newBuilder();
		pokemon = new LinkedList<PokemonDetails>();
	}

	public void setTimestamp(long timestamp)
	{
		builder.setTimestamp(timestamp);
	}
	
	public List<PokemonDetails> getPokemon()
	{
		return pokemon;
	}

	
	public void handleResponse(Payload payload)
	{
		try
		{
			InventoryResponseProto response = InventoryResponseProto.parseFrom(payload.getData());
			for(InventoryItemResponseProto item : response.getItemsList())
			{
				if(item.getItem().hasPokemon())
				{
					pokemon.add(new PokemonDetails(item.getItem().getPokemon()));
				}
			}
			System.out.println(response);
		} 
		catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
	}


	public byte[] getInput() 
	{
		return builder.build().toByteArray();
	}




}
