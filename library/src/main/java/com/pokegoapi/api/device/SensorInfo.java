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

import java.util.Random;

/**
 * Created by fabianterhorst on 08.08.16.
 */

public class SensorInfo {
	private final SensorInfoProvider sensorInfoProvider;

	SensorInfo(SensorInfoProvider sensorInfoProvider) {
		this.sensorInfoProvider = sensorInfoProvider;
	}

	public static SensorInfo getDefault(final Random random) {
		return new SensorInfo(new DefaultSensorInfoProvider(random));
	}

	public SignatureOuterClass.Signature.SensorInfo getSensorInfo() {
		SensorInfoProvider.Info info = sensorInfoProvider.getInfo();
		return SignatureOuterClass.Signature.SensorInfo.newBuilder()
				.setTimestampSnapshot(info.getTimestampSnapshot())
				.setAccelerometerAxes(info.getAccelerometerAxes())
				.setAccelNormalizedX(info.getAccelNormalizedX())
				.setAccelNormalizedY(info.getAccelNormalizedY())
				.setAccelNormalizedZ(info.getAccelNormalizedZ())
				.setAccelRawX(info.getAccelRawX())
				.setAccelRawY(info.getAccelRawY())
				.setAccelRawZ(info.getAccelRawZ())
				.setAngleNormalizedX(info.getAngleNormalizedX())
				.setAngleNormalizedY(info.getAngleNormalizedY())
				.setAngleNormalizedZ(info.getAngleNormalizedZ())
				.setGyroscopeRawX(info.getGyroscopeRawX())
				.setGyroscopeRawY(info.getGyroscopeRawY())
				.setGyroscopeRawZ(info.getGyroscopeRawZ())
				.build();
	}

	private static class DefaultSensorInfoProvider implements SensorInfoProvider {
		private final long timestampCreate = System.currentTimeMillis();
		private final Random random;

		DefaultSensorInfoProvider(Random random) {
			this.random = random;
		}

		@Override
		public Info getInfo() {
			Info info = new Info();
			info.setTimestampSnapshot(System.currentTimeMillis() - timestampCreate + random.nextInt(500));
			info.setAccelRawX(0.1 + (0.7 - 0.1) * random.nextDouble());
			info.setAccelRawY(0.1 + (0.8 - 0.1) * random.nextDouble());
			info.setAccelRawZ(0.1 + (0.8 - 0.1) * random.nextDouble());
			info.setGyroscopeRawX(-1.0 + random.nextDouble() * 2.0);
			info.setGyroscopeRawY(-1.0 + random.nextDouble() * 2.0);
			info.setGyroscopeRawZ(-1.0 + random.nextDouble() * 2.0);
			info.setAccelNormalizedX(-1.0 + random.nextDouble() * 2.0);
			info.setAccelNormalizedY(6.0 + (9.0 - 6.0) * random.nextDouble());
			info.setAccelNormalizedZ(-1.0 + (8.0 - (-1.0)) * random.nextDouble());
			info.setAccelerometerAxes(3);
			return info;
		}
	}
}
