package com.pokegoapi.requests;

import POGOProtos.LocalPlayerOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.GetPlayerResponseOuterClass;
import POGOProtos.Player.CurrencyOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.*;
import com.pokegoapi.exceptions.InvalidCurrencyException;
import com.pokegoapi.main.Request;
import lombok.Getter;

public class ProfileRequest extends Request {

	@Getter
	PlayerProfile profile = new PlayerProfile();

	public RequestTypeOuterClass.RequestType getRpcId() {
		return RequestTypeOuterClass.RequestType.GET_PLAYER;
	}

	public void handleResponse(ByteString payload) {
		try {
			GetPlayerResponseOuterClass.GetPlayerResponse details = GetPlayerResponseOuterClass.GetPlayerResponse.parseFrom(payload);
			LocalPlayerOuterClass.LocalPlayer localPlayer = details.getLocalPlayer();

			profile.setBadge(localPlayer.getEquippedBadge());
			profile.setCreationTime(localPlayer.getCreationTimestampMs());
			profile.setItemStorage(localPlayer.getMaxItemStorage());
			profile.setPokemonStorage(localPlayer.getMaxPokemonStorage());
			profile.setTeam(Team.values()[localPlayer.getTeam()]);
			profile.setUsername(localPlayer.getUsername());

			PlayerAvatar avatarAPI = new PlayerAvatar();
			DailyBonus bonusAPI = new DailyBonus();
			ContactSettings contactAPI = new ContactSettings();

			for (CurrencyOuterClass.Currency currency : localPlayer.getCurrenciesList()) {
				profile.addCurrency(currency.getName(), currency.getAmount());
			}

			avatarAPI.setGender(localPlayer.getAvatarDetails().getGender());
			avatarAPI.setBackpack(localPlayer.getAvatarDetails().getBackpack());
			avatarAPI.setEyes(localPlayer.getAvatarDetails().getEyes());
			avatarAPI.setHair(localPlayer.getAvatarDetails().getHair());
			avatarAPI.setHat(localPlayer.getAvatarDetails().getHat());
			avatarAPI.setPants(localPlayer.getAvatarDetails().getPants());
			avatarAPI.setShirt(localPlayer.getAvatarDetails().getShirt());
			avatarAPI.setShoes(localPlayer.getAvatarDetails().getShoes());
			avatarAPI.setSkin(localPlayer.getAvatarDetails().getSkin());

			bonusAPI.setNextCollectionTimestamp(localPlayer.getDailyBonus().getNextCollectedTimestampMs());
			bonusAPI.setNextDefenderBonusCollectTimestamp(localPlayer.getDailyBonus().getNextDefenderBonusCollectTimestampMs());

			profile.setAvatar(avatarAPI);
			profile.setDailyBonus(bonusAPI);

		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (InvalidCurrencyException e) {
			e.printStackTrace();
		}
	}

	public byte[] getInput() {
		return null;
	}

}
