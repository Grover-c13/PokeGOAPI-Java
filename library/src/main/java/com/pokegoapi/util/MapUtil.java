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
		point.longitude = (point.getLongitude() + randomStep());
		point.latitude = (point.getLatitude() + randomStep());

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
	 * @param end the end coordinate
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
		double lat = Math.toRadians(lat2 - lat1);
		double lng = Math.toRadians(lng2 - lng1);
		double haversine = Math.sin(lat / 2) * Math.sin(lat / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(lng / 2) * Math.sin(lng / 2);


		return earthRadius * (2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine)));
	}

	/**
	 * Sort items map by distance
	 *
	 * @param items the items
	 * @param api the api
	 * @return the map
	 */
	public Map<Double, K> sortItems(List<K> items, PokemonGo api) {
		Map<Double, K> result = new TreeMap<>();
		for (K point : items) {
			result.put(distFrom(api.latitude, api.longitude, point.getLatitude(), point.getLongitude()),
					point);
		}
		return result;
	}
}
