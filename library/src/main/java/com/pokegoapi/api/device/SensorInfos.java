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

/**
 * Created by fabianterhorst on 08.08.16.
 */

public interface SensorInfos {
	/**
	 *
	 * @return timestamp snapshot in ms since start
	 */
	long getTimestampSnapshot();

	/**
	 *
	 * @return linear acceleration x
	 */
	double getLinearAccelerationX();

	/**
	 *
	 * @return linear acceleration y
	 */
	double getLinearAccelerationY();

	/**
	 *
	 * @return linear acceleration z
	 */
	double getLinearAccelerationZ();

	/**
	 *
	 * @return accelerometer axes, always 3
	 */
	long getAccelerometerAxes();

	/**
	 *
	 * @return magnetic field x
	 */
	double getMagneticFieldX();

	/**
	 *
	 * @return magnetic field y
	 */
	double getMagneticFieldY();

	/**
	 *
	 * @return magnetic field z
	 */
	double getMagneticFieldZ();

	/**
	 *
	 * @return rotation vector x
	 */
	double getRotationVectorX();

	/**
	 *
	 * @return rotation vector y
	 */
	double getRotationVectorY();

	/**
	 *
	 * @return rotation vector z
	 */
	double getRotationVectorZ();

	/**
	 *
	 * @return gravity x
	 */
	double getGravityX();

	/**
	 *
	 * @return gravity y
	 */
	double getGravityY();

	/**
	 *
	 * @return gravity z
	 */
	double getGravityZ();

	/**
	 *
	 * @return gyroscope raw x
	 */
	double getGyroscopeRawX();

	/**
	 *
	 * @return gyroscope raw y
	 */
	double getGyroscopeRawY();

	/**
	 *
	 * @return gyroscope raw z
	 */
	double getGyroscopeRawZ();
}
