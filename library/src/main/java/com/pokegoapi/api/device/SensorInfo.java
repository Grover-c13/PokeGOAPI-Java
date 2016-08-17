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

/**
 * Created by fabianterhorst on 08.08.16.
 */

public class SensorInfo {

	private SignatureOuterClass.Signature.SensorInfo.Builder sensorInfoBuilder;

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
				.setAccelerometerAxes(sensorInfos.getAccelerometerAxes())
				.setAccelNormalizedX(sensorInfos.getAccelNormalizedX())
				.setAccelNormalizedY(sensorInfos.getAccelNormalizedY())
				.setAccelNormalizedZ(sensorInfos.getAccelNormalizedZ())
				.setAccelRawX(sensorInfos.getAccelRawX())
				.setAccelRawY(sensorInfos.getAccelRawY())
				.setAccelRawZ(sensorInfos.getAccelRawZ())
				.setAngleNormalizedX(sensorInfos.getAngleNormalizedX())
				.setAngleNormalizedY(sensorInfos.getAngleNormalizedY())
				.setAngleNormalizedZ(sensorInfos.getAngleNormalizedZ())
				.setGyroscopeRawX(sensorInfos.getGyroscopeRawX())
				.setGyroscopeRawY(sensorInfos.getGyroscopeRawY())
				.setGyroscopeRawZ(sensorInfos.getGyroscopeRawZ())
				.build();
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
	 * Sets accelerometer axes
	 *
	 * @param accelerometerAxes accelerometer axes (always 3)
	 */
	public void setAccelerometerAxes(long accelerometerAxes) {
		sensorInfoBuilder.setAccelerometerAxes(accelerometerAxes);
	}

	/**
	 * Sets accel normalized x
	 *
	 * @param accelNormalizedX accel normalized x
	 */
	public void setAccelNormalizedX(double accelNormalizedX) {
		sensorInfoBuilder.setAccelNormalizedX(accelNormalizedX);
	}

	/**
	 * Sets accel normalized y
	 *
	 * @param accelNormalizedY accel normalized y
	 */
	public void setAccelNormalizedY(double accelNormalizedY) {
		sensorInfoBuilder.setAngleNormalizedY(accelNormalizedY);
	}

	/**
	 * Sets accel normalized z
	 *
	 * @param accelNormalizedZ accel normalized z
	 */
	public void setAccelNormalizedZ(double accelNormalizedZ) {
		sensorInfoBuilder.setAccelNormalizedZ(accelNormalizedZ);
	}

	/**
	 * Sets accel raw x
	 *
	 * @param accelRawX accel raw x
	 */
	public void setAccelRawX(double accelRawX) {
		sensorInfoBuilder.setAccelRawX(accelRawX);
	}

	/**
	 * Sets accel raw y
	 *
	 * @param accelRawY accel raw y
	 */
	public void setAccelRawY(double accelRawY) {
		sensorInfoBuilder.setAccelRawY(accelRawY);
	}

	/**
	 * Sets accel raw z
	 *
	 * @param accelRawZ accel raw z
	 */
	public void setAccelRawZ(double accelRawZ) {
		sensorInfoBuilder.setAccelRawZ(accelRawZ);
	}

	/**
	 * Sets angel normalized x
	 *
	 * @param angleNormalizedX angel normalized x
	 */
	public void setAngleNormalizedX(double angleNormalizedX) {
		sensorInfoBuilder.setAngleNormalizedX(angleNormalizedX);
	}

	/**
	 * Sets angel normalized y
	 *
	 * @param angleNormalizedY angel normalized y
	 */
	public void setAngleNormalizedY(double angleNormalizedY) {
		sensorInfoBuilder.setAngleNormalizedY(angleNormalizedY);
	}

	/**
	 * Sets angel normalized z
	 *
	 * @param angleNormalizedZ angel normalized z
	 */
	public void setAngleNormalizedZ(double angleNormalizedZ) {
		sensorInfoBuilder.setAngleNormalizedZ(angleNormalizedZ);
	}

	/**
	 * Sets gyroscope raw x
	 *
	 * @param gyroscopeRawX gyroscope raw x
	 */
	public void setGyroscopeRawX(double gyroscopeRawX) {
		sensorInfoBuilder.setGyroscopeRawX(gyroscopeRawX);
	}

	/**
	 * Sets gyroscope raw y
	 *
	 * @param gyroscopeRawY gyroscope raw y
	 */
	public void setGyroscopeRawY(double gyroscopeRawY) {
		sensorInfoBuilder.setGyroscopeRawY(gyroscopeRawY);
	}

	/**
	 * Sets gyroscope raw z
	 *
	 * @param gyroscopeRawZ gyroscope raw z
	 */
	public void setGyroscopeRawZ(double gyroscopeRawZ) {
		sensorInfoBuilder.setGyroscopeRawZ(gyroscopeRawZ);
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
