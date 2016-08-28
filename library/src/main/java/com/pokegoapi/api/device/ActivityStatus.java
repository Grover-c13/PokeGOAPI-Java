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
import com.pokegoapi.api.device.ActivityStatusProvider.Status.Activity;
import lombok.Data;

import java.util.Random;

/**
 * Created by fabianterhorst on 22.08.16.
 */

public class ActivityStatus {
	private final ActivityStatusProvider activityStatusProvider;

	public ActivityStatus(ActivityStatusProvider activityStatusProvider) {
		this.activityStatusProvider = activityStatusProvider;
	}

	/**
	 * Gets the default activity status for the given api
	 *
	 * @param random random object
	 * @return the default activity status for the given api
	 */
	public static ActivityStatus getDefault(Random random) {
		return new ActivityStatus(new DefaultActivityStatusProvider(random));
	}

	public SignatureOuterClass.Signature.ActivityStatus getActivityStatus() {
		Builder builder = SignatureOuterClass.Signature.ActivityStatus.newBuilder();
		builder.setTilting(activityStatusProvider.getActivity().isTilting());
		builder.setStationary(activityStatusProvider.getActivity().getActivity() == Activity.STATIONARY);
		builder.setAutomotive(activityStatusProvider.getActivity().getActivity() == Activity.AUTOMOTIVE);
		builder.setCycling(activityStatusProvider.getActivity().getActivity() == Activity.CYCLING);
		builder.setRunning(activityStatusProvider.getActivity().getActivity() == Activity.RUNNING);
		builder.setWalking(activityStatusProvider.getActivity().getActivity() == Activity.WALKING);
		return builder.build();
	}

	@Data
	private static class DefaultActivityStatusProvider implements ActivityStatusProvider {
		private final Random random;
		@Override
		public Status getActivity() {
			boolean tilting = random.nextInt() % 2 == 0;
			return new Status(Activity.STATIONARY, tilting);
		}
	}
}
