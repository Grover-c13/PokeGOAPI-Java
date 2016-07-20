package com.pokegoapi.api.map.Pokemon;

import POGOProtos.Data.Capture.CaptureAwardOuterClass.CaptureAward;
import POGOProtos.Enums.ActivityTypeOuterClass;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
public class CatchResult {
	private CaptureAward captureAward;
	private CatchPokemonResponse response;

	@Setter
	private boolean failed;

	public CatchResult() {

	}

	public CatchResult(CatchPokemonResponse response)
	{
		this.captureAward = response.getCaptureAward();
		this.response = response;
	}

	public CatchStatus getStatus() {
		return response.getStatus();
	}

	public double getMissPercent() {
		return response.getMissPercent();
	}

	public long getCapturedPokemonId() {
		return response.getCapturedPokemonId();
	}

	public List<ActivityTypeOuterClass.ActivityType> getActivityTypeList() {
		return captureAward.getActivityTypeList();
	}

	public List<Integer> getXpList() {
		return captureAward.getXpList();
	}

	public List<Integer> getCandyList() {
		return captureAward.getCandyList();
	}

	public List<Integer> getStardustList() {
		return captureAward.getStardustList();
	}

	public boolean isFailed() {
		if (response == null) return failed;
		return (this.getStatus() != CatchStatus.CATCH_SUCCESS || failed);
	}
}
