package com.pokegoapi.go.map.spec;

/**
 * Created by chris on 1/23/2017.
 */
public interface MapPoint {

    /**
     * Gets the id for this map point. For example, the gym id, pokestop id or even the catchable pokemon id
     * @return the map point's id
     */
    String getMapId();

    /**
     * Gets latitude of the point on the globe.
     *
     * @return the latitude
     */
    double getLatitude();

    /**
     * Gets longitude of the point on the globe.
     *
     * @return the longitude
     */
    double getLongitude();
}
