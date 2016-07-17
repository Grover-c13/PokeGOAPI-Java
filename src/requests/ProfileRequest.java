package requests;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import api.ContactSettings;
import api.DailyBonus;
import api.PlayerAvatar;
import api.PlayerProfile;
import api.Team;
import main.Pokemon.ClientPlayerDetails;
import main.Pokemon.Payload;
import main.Request;

public class ProfileRequest extends Request {
	private PlayerProfile profile;
	public int getRpcId()
	{
		profile = new PlayerProfile();
		return 2;
	}

	public PlayerProfile getProfile()
	{
		return profile;
	}

	public void handleResponse(Payload payload)
	{
		try {
			ClientPlayerDetails details = ClientPlayerDetails.parseFrom(payload.getData());

			profile.setBadge(details.getBadge());
			profile.setCreationTime(details.getCreationTime());
			profile.setItemStorage(details.getItemStorage());
			profile.setPokemonStorage(details.getPokeStorage());
			profile.setTeam(valueOf(details.getTeam()));
			profile.setUsername(details.getUsername());
			

			PlayerAvatar avatarAPI = new PlayerAvatar();
			DailyBonus bonusAPI = new DailyBonus();
			ContactSettings contactAPI = new ContactSettings();
			
			for(main.Pokemon.Currency currency : details.getCurrencyList() )
			{
				profile.addCurrency(currency.getType(), currency.getAmount());
			}
			
			
			avatarAPI.setAvatar(details.getAvatar().getAvatar());
			avatarAPI.setBackpack(details.getAvatar().getBackpack());
			avatarAPI.setEyes(details.getAvatar().getEyes());
			avatarAPI.setHair(details.getAvatar().getHair());
			avatarAPI.setHat(details.getAvatar().getHat());
			avatarAPI.setPants(details.getAvatar().getPants());
			avatarAPI.setShirt(details.getAvatar().getShirt());
			avatarAPI.setShoes(details.getAvatar().getShoes());
			avatarAPI.setSkin(details.getAvatar().getSkin());
		
			bonusAPI.setNextCollectionTimestamp(details.getDailyBonus().getNextCollectTimestampMs());
			bonusAPI.setNextDefenderBonusCollectTimestamp(details.getDailyBonus().getNextDefenderBonusCollectTimestampMs());
			
			// TODO contact settings
			
			profile.setAvatar(avatarAPI);
			profile.setDailyBonus(bonusAPI);
			
			
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Team valueOf(int value)
	{
		if (value == 0) return Team.TEAM_INSTINCT;
		if (value == 1) return Team.TEAM_MYSTIC;
		if (value == 2) return Team.TEAM_VALOR;

		return Team.TEAM_NONE;
	}

	

	public byte[] getInput() 
	{
		return null;
	}



}
