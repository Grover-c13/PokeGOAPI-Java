package com.pokegoapi.go.spec;

/**
 * Created by chris on 1/23/2017.
 */
public interface Location {

    double getLatitude();
    double getLongitude();
    double getAltitude();
    double getAccuracy();
    long getTimestamp();

    double getSpeed(Location location);
}
