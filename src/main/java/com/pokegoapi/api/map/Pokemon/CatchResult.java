/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.map.pokemon;

import POGOProtos.Data.Capture.CaptureAwardOuterClass.CaptureAward;
import POGOProtos.Enums.ActivityTypeOuterClass;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;
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
		setFailed(true);
	}

	public CatchResult(CatchPokemonResponse response) {
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

	/**
	 * Returns whether the catch failed.
	 *
	 * @return the boolean
	 */
	public boolean isFailed() {
		if (response == null) {
			return failed;
		}
		return (this.getStatus() != CatchStatus.CATCH_SUCCESS || failed);
	}
}
