package com.pokegoapi.go.settings.spec;

public interface SfidaSettingsSpec {
    /**
     * @return the threshold on a scale from 0-1 at which this is considered 'low battery'
     */
    float getLowBatteryThreshold();
}
