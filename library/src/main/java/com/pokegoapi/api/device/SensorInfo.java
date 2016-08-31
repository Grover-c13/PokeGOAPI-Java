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

	public SensorInfo(SensorInfoProvider sensorInfoProvider) {
		this.sensorInfoProvider = sensorInfoProvider;
	}

	public static SensorInfo getDefault(final Random random) {
		return new SensorInfo(new DefaultSensorInfoProvider(random));
	}

	public SignatureOuterClass.Signature.SensorInfo getSensorInfo() {
		SensorInfoProvider.Info info = sensorInfoProvider.getInfo();
		return SignatureOuterClass.Signature.SensorInfo.newBuilder()
				.setTimestampSnapshot(info.getTimestampSnapshot())
				.setLinearAccelerationX(info.getLinearAccelerationX())
				.setLinearAccelerationY(info.getLinearAccelerationY())
				.setLinearAccelerationZ(info.getLinearAccelerationZ())
				.setAccelerometerAxes(info.getAccelerometerAxes())
				.setMagneticFieldX(info.getMagneticFieldX())
				.setMagneticFieldY(info.getMagneticFieldY())
				.setMagneticFieldZ(info.getMagneticFieldZ())
				.setRotationVectorX(info.getRotationVectorX())
				.setRotationVectorY(info.getRotationVectorY())
				.setRotationVectorZ(info.getRotationVectorZ())
				.setGyroscopeRawX(info.getGyroscopeRawX())
				.setGyroscopeRawY(info.getGyroscopeRawY())
				.setGyroscopeRawZ(info.getGyroscopeRawZ())
				.setGravityX(info.getGravityX())
				.setGravityY(info.getGravityX())
				.setGravityZ(info.getGravityZ())
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
			info.setMagneticFieldX(0.1 + (0.7 - 0.1) * random.nextDouble());
			info.setMagneticFieldY(0.1 + (0.8 - 0.1) * random.nextDouble());
			info.setMagneticFieldZ(0.1 + (0.8 - 0.1) * random.nextDouble());
			info.setGyroscopeRawX(-1.0 + random.nextDouble() * 2.0);
			info.setGyroscopeRawY(-1.0 + random.nextDouble() * 2.0);
			info.setGyroscopeRawZ(-1.0 + random.nextDouble() * 2.0);
			info.setLinearAccelerationX(-1.0 + random.nextDouble() * 2.0);
			info.setLinearAccelerationY(6.0 + (9.0 - 6.0) * random.nextDouble());
			info.setLinearAccelerationZ(-1.0 + (8.0 - (-1.0)) * random.nextDouble());
			info.setRotationVectorX(-1.0 + random.nextDouble() * 2.0);
			info.setRotationVectorY(-1.0 + random.nextDouble() * 2.0);
			info.setRotationVectorZ(-1.0 + random.nextDouble() * 2.0);
			info.setGyroscopeRawX(-1.0 + random.nextDouble() * 2.0);
			info.setGyroscopeRawY(-1.0 + random.nextDouble() * 2.0);
			info.setGyroscopeRawZ(-1.0 + random.nextDouble() * 2.0);
			info.setAccelerometerAxes(3);
			return info;
		}
	}
}
