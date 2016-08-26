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

import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Envelopes.SignatureOuterClass.Signature.ActivityStatus.Builder;

import java.util.Random;

/**
 * Created by fabianterhorst on 22.08.16.
 */

public class ActivityStatus {
	private final ActivityStatusProvider activityStatusProvider;
	private final Random random;
	public ActivityStatus(ActivityStatusProvider activityStatusProvider) {
		this.activityStatusProvider = activityStatusProvider;
		this.random = null;
	}

	private ActivityStatus(Random random) {
		this.activityStatusProvider = null;
		this.random = random;
	}

	/**
	 * Gets the default activity status for the given api
	 *
	 * @param random random object
	 * @return the default activity status for the given api
	 */
	public static ActivityStatus getDefault(Random random) {
		return new ActivityStatus(random);
	}

	public SignatureOuterClass.Signature.ActivityStatus getActivityStatus() {
		Builder builder = SignatureOuterClass.Signature.ActivityStatus.newBuilder();
		builder.setStartTimeMs(1);
		if (activityStatusProvider != null) {
			builder.setTilting(activityStatusProvider.isTilting());
			builder.setStationary(activityStatusProvider.isStationary());
			builder.setAutomotive(activityStatusProvider.isAutomotive());
			builder.setCycling(activityStatusProvider.isCycling());
			builder.setRunning(activityStatusProvider.isRunning());
			builder.setWalking(activityStatusProvider.isWalking());
			return builder.build();
		}
		if (random == null) {
			throw new IllegalArgumentException("How did you instantiate this object?");
		}
		boolean tilting = random.nextInt() % 2 == 0;
			builder.setStationary(true);

		if (tilting) {
			builder.setTilting(true);
		}
		return builder.build();
	}
}
