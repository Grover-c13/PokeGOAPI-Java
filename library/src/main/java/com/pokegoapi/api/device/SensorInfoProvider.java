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
 *     aprivate long  with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.device;

import lombok.Data;

/**
 * Created by fabianterhorst on 08.08.16.
 */

public interface SensorInfoProvider {
	Info getInfo();

	@Data
	class Info {
		/**
		 * timestamp snapshot in ms since start
		 */
		private long timestampSnapshot;

		/**
		 * accelerometer axes, always 3
		 */
		private long accelerometerAxes;

		/**
		 * accel normalized x
		 */
		private double accelNormalizedX;

		/**
		 * accel normalized y
		 */
		private double accelNormalizedY;

		/**
		 * accel normalized z
		 */
		private double accelNormalizedZ;

		/**
		 * accel raw x
		 */
		private double accelRawX;

		/**
		 * accel raw y
		 */
		private double accelRawY;

		/**
		 * accel raw z
		 */
		private double accelRawZ;

		/**
		 * angel normalized x
		 */
		private double angleNormalizedX;

		/**
		 * angel normalized y
		 */
		private double angleNormalizedY;

		/**
		 * angel normalized z
		 */
		private double angleNormalizedZ;

		/**
		 * gyroscope raw x
		 */
		private double gyroscopeRawX;

		/**
		 * gyroscope raw y
		 */
		private double gyroscopeRawY;

		/**
		 * gyroscope raw z
		 */
		private double gyroscopeRawZ;
	}

}
