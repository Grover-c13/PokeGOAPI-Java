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
import com.pokegoapi.api.PokemonGo;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

/**
 * Created by fabianterhorst on 08.08.16.
 */

public class SensorInfo {

	private SignatureOuterClass.Signature.SensorInfo.Builder sensorInfoBuilder;

	@Setter
	@Getter
	public long timestampCreate;

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
		sensorInfoBuilder
				.setTimestampSnapshot(sensorInfos.getTimestampSnapshot())
				.setStatus((int) sensorInfos.getAccelerometerAxes())
				.setGravityX(sensorInfos.getAccelNormalizedX())
				.setGravityY(sensorInfos.getAccelNormalizedY())
				.setGravityZ(sensorInfos.getAccelNormalizedZ())
				.setAttitudePitch(sensorInfos.getAccelRawX())
				.setAttitudeYaw(sensorInfos.getAccelRawY())
				.setAttitudeRoll(sensorInfos.getAccelRawZ())
				.setRotationRateX(sensorInfos.getAngleNormalizedX())
				.setRotationRateY(sensorInfos.getAngleNormalizedY())
				.setRotationRateZ(sensorInfos.getAngleNormalizedZ())
				.setAttitudePitch(sensorInfos.getGyroscopeRawX())
				.setAttitudeYaw(sensorInfos.getGyroscopeRawY())
				.setAttitudeRoll(sensorInfos.getGyroscopeRawZ())
				.build();
	}

	/**
	 * Gets the default sensor info for the given api
	 *
	 * @param api the api
	 * @param currentTime the current time
	 * @param random random object
	 * @return the default sensor info for the given api
	 */
	public static SignatureOuterClass.Signature.SensorInfo getDefault(PokemonGo api, long currentTime, Random random) {
		SensorInfo sensorInfo;
		if (api.sensorInfo == null) {
			sensorInfo = new SensorInfo();
			sensorInfo.getBuilder()
				.setTimestampSnapshot(currentTime - api.startTime + random.nextInt(500))
				.setRotationRateX(0.1 + 0.6 * random.nextDouble())
				.setRotationRateY(0.1 + 0.7000000000000001 * random.nextDouble())
				.setRotationRateZ(0.1 + 0.7000000000000001 * random.nextDouble())
				.setAttitudePitch(-1.0 + random.nextDouble() * 2.0)
				.setAttitudeRoll(-1.0 + random.nextDouble() * 2.0)
				.setAttitudeYaw(-1.0 + random.nextDouble() * 2.0)
				.setGravityX(-1.0 + random.nextDouble() * 2.0)
				.setGravityY(-1.0 + random.nextDouble() * 2.0)
				.setGravityZ(-1.0 + random.nextDouble() * 2.0)
				.setMagneticFieldAccuracy(-1)
				.setStatus(3);
			api.sensorInfo = sensorInfo;
		} else {
			sensorInfo = api.sensorInfo;
			sensorInfo.getBuilder()
				.setTimestampSnapshot(currentTime - api.startTime + random.nextInt(500))
				.setLinearAccelerationX(-0.7 + random.nextDouble() * 1.4)
				.setLinearAccelerationY(-0.7 + random.nextDouble() * 1.4)
				.setLinearAccelerationZ(-0.7 + random.nextDouble() * 1.4)
				.setRotationRateX(0.1 + 0.6 * random.nextDouble())
				.setRotationRateY(0.1 + 0.7000000000000001 * random.nextDouble())
				.setRotationRateZ(0.1 + 0.7000000000000001 * random.nextDouble())
				.setAttitudePitch(-1.0 + random.nextDouble() * 2.0)
				.setAttitudeRoll(-1.0 + random.nextDouble() * 2.0)
				.setAttitudeYaw(-1.0 + random.nextDouble() * 2.0)
				.setGravityX(-1.0 + random.nextDouble() * 2.0)
				.setGravityY(-1.0 + random.nextDouble() * 2.0)
				.setGravityZ(-1.0 + random.nextDouble() * 2.0)
				.setMagneticFieldAccuracy(-1)
				.setStatus(3);
		}
		if (currentTime - sensorInfo.timestampCreate > (random.nextInt(10000) + 5000)) {
			sensorInfo.timestampCreate = currentTime;
			return sensorInfo.getSensorInfo();
		}
		return null;
	}

	/**
	 * Sets timestamp snapshot since start
	 *
	 * @param timestampSnapshot timestamp in ms since app start
	 */
	public void setTimestampSnapshot(long timestampSnapshot) {
		sensorInfoBuilder.setTimestampSnapshot(timestampSnapshot);
	}

	/**
	 * Sets sensor status
	 *
	 * @param status sensor info status (always 3)
	 */
	public void setStatus(int status) {
		sensorInfoBuilder.setStatus(status);
	}

	/**
	 * Sets linear acceleration x
	 *
	 * @param linearAccelerationX linear acceleration x
	 */
	public void setLinearAccelerationX(double linearAccelerationX) {
		sensorInfoBuilder.setLinearAccelerationX(linearAccelerationX);
	}

	/**
	 * Sets linear acceleration y
	 *
	 * @param linearAccelerationY linear acceleration y
	 */
	public void setLinearAccelerationY(double linearAccelerationY) {
		sensorInfoBuilder.setLinearAccelerationY(linearAccelerationY);
	}

	/**
	 * Sets linear acceleration z
	 *
	 * @param linearAccelerationZ linear acceleration z
	 */
	public void setLinearAccelerationZ(double linearAccelerationZ) {
		sensorInfoBuilder.setLinearAccelerationZ(linearAccelerationZ);
	}

	/**
	 * Sets gravity x
	 *
	 * @param gravityX gravity x
	 */
	public void setGravityX(double gravityX) {
		sensorInfoBuilder.setGravityX(gravityX);
	}

	/**
	 * Sets gravity y
	 *
	 * @param gravityY gravity y
	 */
	public void setGravityY(double gravityY) {
		sensorInfoBuilder.setGravityY(gravityY);
	}

	/**
	 * Sets gravity z
	 *
	 * @param gravityZ gravity z
	 */
	public void setGravityZ(double gravityZ) {
		sensorInfoBuilder.setGravityZ(gravityZ);
	}

	/**
	 * Sets rotation rate x
	 *
	 * @param rotationRateX rotation rate x
	 */
	public void setRotationRateX(double rotationRateX) {
		sensorInfoBuilder.setRotationRateX(rotationRateX);
	}

	/**
	 * Sets rotation rate y
	 *
	 * @param rotationRateY rotation rate y
	 */
	public void setRotationRateY(double rotationRateY) {
		sensorInfoBuilder.setRotationRateY(rotationRateY);
	}

	/**
	 * Setsrotation rate z
	 *
	 * @param rotationRateZ rotation rate z
	 */
	public void setRotationRateZ(double rotationRateZ) {
		sensorInfoBuilder.setRotationRateZ(rotationRateZ);
	}

	/**
	 * Sets attitude pitch (x axis)
	 *
	 * @param attitudePitch attitude pitch (x axis)
	 */
	public void setAttitudePitch(double attitudePitch) {
		sensorInfoBuilder.setAttitudePitch(attitudePitch);
	}

	/**
	 * Sets attitude yaw (y axis)
	 *
	 * @param attitudeYaw attitude yaw (y axis)
	 */
	public void setAttitudeYaw(double attitudeYaw) {
		sensorInfoBuilder.setAttitudeYaw(attitudeYaw);
	}

	/**
	 * Sets attitude roll (z axis)
	 *
	 * @param attitudeRoll attitude roll (z axis)
	 */
	public void setAttitudeRoll(double attitudeRoll) {
		sensorInfoBuilder.setAttitudeRoll(attitudeRoll);
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
