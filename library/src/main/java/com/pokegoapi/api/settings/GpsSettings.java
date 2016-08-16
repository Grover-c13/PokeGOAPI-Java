package com.pokegoapi.api.settings;

import POGOProtos.Settings.GpsSettingsOuterClass;
import lombok.Getter;

/**
 * Created by fabianterhorst on 16.08.16.
 */

public class GpsSettings {

	@Getter
	/**
	 *
	 * @return meters per seconds.
	 */
	private double drivingWarningSpeedMetersPerSecond;

	@Getter
	/**
	 *
	 * @return minutes.
	 */
	private float drivingWarningCooldownMinutes;

	@Getter
	/**
	 *
	 * @return seconds.
	 */
	private float drivingSpeedSampleIntervalSeconds;

	@Getter
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
