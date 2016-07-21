package com.pokegoapi.api.map.fort;

import POGOProtos.Inventory.ItemAwardOuterClass;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass.FortSearchResponse.Result;

import java.util.List;

/**
 * Created by mjmfighter on 7/20/2016.
 */
public class PokestopLootResult {

    private FortSearchResponseOuterClass.FortSearchResponse response;

    public PokestopLootResult(FortSearchResponseOuterClass.FortSearchResponse response) {
        this.response = response;
    }

    public boolean wasSuccessful(){
        return response.getResult() == Result.SUCCESS || response.getResult() == Result.INVENTORY_FULL;
    }

    public Result getResult(){
        return response.getResult();
    }

    public List<ItemAwardOuterClass.ItemAward> getItemsAwarded(){
        return response.getItemsAwardedList();
    }

    public int getExperience(){
        return response.getExperienceAwarded();
    }

    public FortSearchResponseOuterClass.FortSearchResponse toPrimitive(){
        return response;
    }
}
