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
		SignatureOuterClass.Signature.ActivityStatus.Builder builder = activityStatus.getBuilder();
		builder.setStationary(true);
		if (tilting) {
			builder.setTilting(true);
		}
		return builder.build();
	}

	/**
	 * Gets the activity status builder
	 *
	 * @return the activity status builder
	 */
	public SignatureOuterClass.Signature.ActivityStatus.Builder getBuilder() {
		return activityStatusBuilder;
	}
}
