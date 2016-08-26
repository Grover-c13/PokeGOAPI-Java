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
	private final long timestampCreate = System.currentTimeMillis();
	private final SensorInfoProvider sensorInfoProvider;
	private final Random random;

	SensorInfo(SensorInfoProvider sensorInfoProvider) {
		this.sensorInfoProvider = sensorInfoProvider;
		random = null;
	}

	SensorInfo(Random random) {
		this.sensorInfoProvider = null;
		this.random = random;
	}


	SignatureOuterClass.Signature.SensorInfo getSensorInfo() {
		if (this.sensorInfoProvider == null) {
			return SignatureOuterClass.Signature.SensorInfo.newBuilder()
					.setTimestampSnapshot(System.currentTimeMillis() - timestampCreate + random.nextInt(500))
					.setAccelRawX(0.1 + (0.7 - 0.1) * random.nextDouble())
					.setAccelRawY(0.1 + (0.8 - 0.1) * random.nextDouble())
					.setAccelRawZ(0.1 + (0.8 - 0.1) * random.nextDouble())
					.setGyroscopeRawX(-1.0 + random.nextDouble() * 2.0)
					.setGyroscopeRawY(-1.0 + random.nextDouble() * 2.0)
					.setGyroscopeRawZ(-1.0 + random.nextDouble() * 2.0)
					.setAccelNormalizedX(-1.0 + random.nextDouble() * 2.0)
					.setAccelNormalizedY(6.0 + (9.0 - 6.0) * random.nextDouble())
					.setAccelNormalizedZ(-1.0 + (8.0 - (-1.0)) * random.nextDouble())
					.setAccelerometerAxes(3)
					.build();
		}
		return SignatureOuterClass.Signature.SensorInfo.newBuilder()
				.setTimestampSnapshot(sensorInfoProvider.getTimestampSnapshot())
				.setAccelerometerAxes(sensorInfoProvider.getAccelerometerAxes())
				.setAccelNormalizedX(sensorInfoProvider.getAccelNormalizedX())
				.setAccelNormalizedY(sensorInfoProvider.getAccelNormalizedY())
				.setAccelNormalizedZ(sensorInfoProvider.getAccelNormalizedZ())
				.setAccelRawX(sensorInfoProvider.getAccelRawX())
				.setAccelRawY(sensorInfoProvider.getAccelRawY())
				.setAccelRawZ(sensorInfoProvider.getAccelRawZ())
				.setAngleNormalizedX(sensorInfoProvider.getAngleNormalizedX())
				.setAngleNormalizedY(sensorInfoProvider.getAngleNormalizedY())
				.setAngleNormalizedZ(sensorInfoProvider.getAngleNormalizedZ())
				.setGyroscopeRawX(sensorInfoProvider.getGyroscopeRawX())
				.setGyroscopeRawY(sensorInfoProvider.getGyroscopeRawY())
				.setGyroscopeRawZ(sensorInfoProvider.getGyroscopeRawZ())
				.build();
	}
}
