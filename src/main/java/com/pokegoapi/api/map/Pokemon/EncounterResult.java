package com.pokegoapi.api.map.Pokemon;

import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;

/**
 * Created by mjmfighter on 7/20/2016.
 */
public class EncounterResult {

    private EncounterResponse response;

    public EncounterResult(EncounterResponse response){
        this.response = response;
    }

    public EncounterResponse.Status getStatus(){
        return response == null ? null : response.getStatus();
    }

    public boolean wasSuccessful(){
        return response != null && getStatus() != null && getStatus().equals(EncounterResponse.Status.ENCOUNTER_SUCCESS);
    }
}
