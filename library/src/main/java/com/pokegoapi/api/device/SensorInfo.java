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
import lombok.Getter;
import lombok.Setter;

/**
 * Created by fabianterhorst on 08.08.16.
 */

public class SensorInfo {

	private SignatureOuterClass.Signature.SensorInfo.Builder sensorInfoBuilder;

	@Setter
	@Getter
	private long timestampCreate;

	public SensorInfo() {
		sensorInfoBuilder = SignatureOuterClass.Signature.SensorInfo.newBuilder();
	}

	/**
	 * Create a sensor info with already existing sensor infos
	 *
	 * @param sensorInfos the sensor infos interface
	 */
	public SensorInfo(SensorInfos sensorInfos) {
		this();
	}

	/**
	 * Gets the default sensor info for the given api
	 *
	 * @param api         the api
	 * @param currentTime the current time
	 * @param random      random object
	 * @return the default sensor info for the given api
	 */
	public static SignatureOuterClass.Signature.SensorInfo getDefault(PokemonGo api, long currentTime, Random random) {
		SensorInfo sensorInfo;
		if (api.getSensorInfo() == null) {
			sensorInfo = new SensorInfo();
			sensorInfo.getBuilder().setTimestampSnapshot(currentTime - api.getStartTime() + random.nextInt(500))
					.setRotationVectorX(0.1 + (0.7 - 0.1) * random.nextDouble())
					.setRotationVectorY(0.1 + (0.8 - 0.1) * random.nextDouble())
					.setRotationVectorZ(0.1 + (0.8 - 0.1) * random.nextDouble())
					.setGyroscopeRawX(-1.0 + random.nextDouble() * 2.0)
					.setGyroscopeRawY(-1.0 + random.nextDouble() * 2.0)
					.setGyroscopeRawZ(-1.0 + random.nextDouble() * 2.0)
					.setMagneticFieldX(-1.0 + random.nextDouble() * 2.0)
					.setMagneticFieldY(6.0 + (9.0 - 6.0) * random.nextDouble())
					.setMagneticFieldZ(-1.0 + (8.0 - (-1.0)) * random.nextDouble())
					.setAccelerometerAxes(3);
			api.setSensorInfo(sensorInfo);
		} else {
			sensorInfo = api.getSensorInfo();
			sensorInfo.getBuilder().setTimestampSnapshot(currentTime - api.getStartTime() + random.nextInt(500))
					.setLinearAccelerationX(-0.7 + random.nextDouble() * 1.4)
					.setLinearAccelerationY(-0.7 + random.nextDouble() * 1.4)
					.setLinearAccelerationZ(-0.7 + random.nextDouble() * 1.4)
					.setMagneticFieldX(-55.0 + random.nextDouble() * 110.0)
					.setMagneticFieldY(-55.0 + random.nextDouble() * 110.0)
					.setMagneticFieldZ(-55.0 + random.nextDouble() * 110.0)
					.setRotationVectorX(0.1 + (0.7 - 0.1) * random.nextDouble())
					.setRotationVectorY(0.1 + (0.8 - 0.1) * random.nextDouble())
					.setRotationVectorZ(0.1 + (0.8 - 0.1) * random.nextDouble())
					.setGyroscopeRawX(-1.0 + random.nextDouble() * 2.0)
					.setGyroscopeRawY(-1.0 + random.nextDouble() * 2.0)
					.setGyroscopeRawZ(-1.0 + random.nextDouble() * 2.0)
					.setGravityX(-1.0 + random.nextDouble() * 2.0)
					.setGravityY(6.0 + (9.0 - 6.0) * random.nextDouble())
					.setGravityZ(-1.0 + (8.0 - (-1.0)) * random.nextDouble())
					.setAccelerometerAxes(3);
		}
		if (currentTime - sensorInfo.getTimestampCreate() > (random.nextInt(10 * 1000) + 5 * 1000)) {
			sensorInfo.setTimestampCreate(currentTime);
			return sensorInfo.getSensorInfo();
		}
		return null;
	}

	public SignatureOuterClass.Signature.SensorInfo.Builder getBuilder() {
		return sensorInfoBuilder;
	}

	/**
	 * Gets SensorInfo.
	 *
	 * @return SensorInfo
	 */
	public SignatureOuterClass.Signature.SensorInfo getSensorInfo() {
		return sensorInfoBuilder.build();
	}
}
