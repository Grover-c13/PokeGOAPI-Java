package com.pokegoapi.go.settings.spec;

public interface GpsSettingsSpec {
    /**
     * @return the amount of samples to take for driving check
     */
    int getDrivingSpeedSampleCount();

    /**
     * @return the interval in seconds between driving speed samples
     */
    float getDrivingSpeedSampleInterval();

    /**
     * @return the cooldown in minutes between driving warnings
     */
    float getDrivingWarningCooldown();

    /**
     * @return the speed in meters per second at which you will begin to receive driving warnings
     */
    float getDrivingWarningSpeed();
}
