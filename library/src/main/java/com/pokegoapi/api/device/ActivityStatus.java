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

package com.pokegoapi.api.device;

import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;

import java.util.Random;

import POGOProtos.Networking.Envelopes.SignatureOuterClass;

/**
 * Created by fabianterhorst on 22.08.16.
 */

public class ActivityStatus {

	private SignatureOuterClass.Signature.ActivityStatus.Builder activityStatusBuilder;

	public ActivityStatus() {
		activityStatusBuilder = SignatureOuterClass.Signature.ActivityStatus.newBuilder();
	}

	/**
	 * Gets the default activity status for the given api
	 *
	 * @param api the api
	 * @param random random object
	 * @return the default activity status for the given api
	 */
	public static SignatureOuterClass.Signature.ActivityStatus getDefault(PokemonGo api, Random random) {
		boolean tilting = random.nextInt() % 2 == 0;
		ActivityStatus activityStatus = api.getActivityStatus();
		if (activityStatus == null) {
			activityStatus = new ActivityStatus();
			api.setActivityStatus(activityStatus);
		}
		activityStatus.setStationary(true);
		if (tilting) {
			activityStatus.setTilting(true);
		}
		return activityStatus.getActivityStatus();
	}

	public void setAutomotive(boolean automotive) {
		activityStatusBuilder.setAutomotive(automotive);
	}

	public void setCycling(boolean cycling) {
		activityStatusBuilder.setCycling(cycling);
	}

	public void setTilting(boolean tilting) {
		activityStatusBuilder.setTilting(tilting);
	}

	public void setRunning(boolean running) {
		activityStatusBuilder.setRunning(running);
	}

	public void setStationary(boolean stationary) {
		activityStatusBuilder.setStationary(stationary);
	}

	public void setWalking(boolean walking) {
		activityStatusBuilder.setWalking(walking);
	}

	public void setStartTimeMs(long startTimeMs) {
		activityStatusBuilder.setStartTimeMs(startTimeMs);
	}

	public void setStatus(ByteString status) {
		activityStatusBuilder.setStatus(status);
	}

	public void setUnknownStatus(boolean unknownStatus) {
		activityStatusBuilder.setUnknownStatus(unknownStatus);
	}

	/**
	 * Gets the activity status builder
	 *
	 * @return the activity status builder
	 */
	public SignatureOuterClass.Signature.ActivityStatus.Builder getBuilder() {
		return activityStatusBuilder;
	}

	public SignatureOuterClass.Signature.ActivityStatus getActivityStatus() {
		return activityStatusBuilder.build();
	}
}
