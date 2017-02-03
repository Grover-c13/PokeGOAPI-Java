package com.pokegoapi.old.api.settings;

import POGOProtos.Settings.GpsSettingsOuterClass;


/**
 * Created by fabianterhorst on 16.08.16.
 */

public class GpsSettings {


	/**
	 *
	 * @return meters per seconds.
	 */
	private double drivingWarningSpeedMetersPerSecond;


	/**
	 *
	 * @return minutes.
	 */
	private float drivingWarningCooldownMinutes;


	/**
	 *
	 * @return seconds.
	 */
	private float drivingSpeedSampleIntervalSeconds;


	/**
	 *
	 * @return count.
	 */
	private double drivingSpeedSampleCount;

	/**
	 * Update the gps settings from the network response.
	 *
	 * @param gpsSettings the new gps settings
	 */
	public void update(GpsSettingsOuterClass.GpsSettings gpsSettings) {
		drivingWarningSpeedMetersPerSecond = gpsSettings.getDrivingWarningSpeedMetersPerSecond();
		drivingWarningCooldownMinutes = gpsSettings.getDrivingWarningCooldownMinutes();
		drivingSpeedSampleIntervalSeconds = gpsSettings.getDrivingSpeedSampleIntervalSeconds();
		drivingSpeedSampleCount = gpsSettings.getDrivingSpeedSampleCount();
	}
}
