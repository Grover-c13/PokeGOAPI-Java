package com.pokegoapi.util;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.Point;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author Olaf Braun - Software Development
 * @version 1.0
 */
public class MapUtil<K extends MapPoint> {
    /**
     * Random step to a coordinate object
     *
     * @param point the coordinate
     * @return the coordinate
     */
    public static Point randomStep(Point point) {
        point.setLongitude(point.getLongitude() + randomStep());
        point.setLatitude(point.getLatitude() + randomStep());

        return point;
    }

    /**
     * Random step double.
     *
     * @return the double
     */
    public static double randomStep() {
        Random random = new Random();
        return random.nextDouble() / 100000.0;
    }

    /**
     * Dist between coordinates
     *
     * @param start the start coordinate
     * @param end   the end coordinate
     * @return the double
     */
    public static double distFrom(Point start, Point end) {
        return distFrom(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
    }

    /**
     * Dist between coordinates
     *
     * @param lat1 the start latitude coordinate
     * @param lng1 the start longitude coordinate
     * @param lat2 the end latitude coordinate
     * @param lng2 the end longitude coordinate
     * @return the double
     */
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    /**
     * Sort items map by distance
     *
     * @param items the items
     * @param api   the api
     * @return the map
     */
    public Map<Double, K> sortItems(List<K> items, PokemonGo api){
        Map<Double, K> result = new TreeMap<>();
        for (K point : items) {
            result.put(distFrom(api.getLatitude(), api.getLongitude(), point.getLatitude(), point.getLongitude()), point);
        }
        return result;
    }
}
