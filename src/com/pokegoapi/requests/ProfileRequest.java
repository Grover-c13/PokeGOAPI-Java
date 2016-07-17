package com.pokegoapi.requests;

import com.google.protobuf.InvalidProtocolBufferException;

import com.pokegoapi.api.ContactSettings;
import com.pokegoapi.api.DailyBonus;
import com.pokegoapi.api.PlayerAvatar;
import com.pokegoapi.api.PlayerProfile;
import com.pokegoapi.api.Team;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.main.Communication;
import com.pokegoapi.main.Player.ClientPlayerDetails;
import com.pokegoapi.main.Player.Currency;
import com.pokegoapi.main.Communication.Payload;
import com.pokegoapi.main.Request;

import lombok.Getter;
public class ProfileRequest extends Request {
	
	@Getter PlayerProfile profile = new PlayerProfile();
	
	public Communication.Method getRpcId() {
		return Communication.Method.GET_PLAYER_PROFILE;
	}

	public void handleResponse(Payload payload)
	{
		try {
			ClientPlayerDetails details = ClientPlayerDetails.parseFrom(payload.getData());

			profile.setBadge(details.getBadge());
			profile.setCreationTime(details.getCreationTime());
			profile.setItemStorage(details.getItemStorage());
			profile.setPokemonStorage(details.getPokeStorage());
			profile.setTeam(Team.values()[details.getTeam()]);
			profile.setUsername(details.getUsername());
			

			PlayerAvatar avatarAPI = new PlayerAvatar();
			DailyBonus bonusAPI = new DailyBonus();
			ContactSettings contactAPI = new ContactSettings();
			
			for(Currency currency : details.getCurrencyList() )
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
			
			profile.setAvatar(avatarAPI);
			profile.setDailyBonus(bonusAPI);
			
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (InvalidCurrencyException e) {
			e.printStackTrace();
		}
	}

	public byte[] getInput() 
	{
		return null;
	}

}
