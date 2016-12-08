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

package com.pokegoapi.util.path;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.Point;
import com.pokegoapi.util.MapUtil;
import lombok.Getter;

public class Path {
	private final Point source;
	private final Point destination;
	private final Point intermediate;
	private final double speed;
	private long startTime;
	private long endTime;
	private long totalTime;
	@Getter
	private boolean complete;

	/**
	 * Creates a Path with the given positions
	 * @param source the source of this path
	 * @param destination the destination for this path
	 * @param speed the speed to move at in kmph
	 */
	public Path(Point source, Point destination, double speed) {
		this.source = source;
		this.destination = destination;
		double metersPerHour = speed * 1000;
		this.speed = metersPerHour / 60 / 60 / 1000;
		this.intermediate = new Point(source.getLatitude(), source.getLongitude());
	}

	/**
	 * Sets the start and end time for this Path, ready to begin moving
	 * @param api the current API
	 * @return the total time it will take for this path to complete
	 */
	public long start(PokemonGo api) {
		startTime = api.currentTimeMillis();
		totalTime = (long) (MapUtil.distFrom(source, destination) / speed);
		endTime = startTime + totalTime;
		complete = false;
		return totalTime;
	}

	/**
	 * Calculates the desired intermediate point for this path, based on the current time
	 * @param api the current API
	 * @return the intermediate point for the given time
	 */
	public Point calculateIntermediate(PokemonGo api) {
		long time = Math.min(api.currentTimeMillis(), endTime) - startTime;
		if (time >= totalTime) {
			this.complete = true;
		}
		double intermediate = (double) time / totalTime;
		double latitude = source.getLatitude() + (destination.getLatitude() - source.getLatitude()) * intermediate;
		double longitude = source.getLongitude() + (destination.getLongitude() - source.getLongitude()) * intermediate;
		this.intermediate.setLatitude(latitude);
		this.intermediate.setLongitude(longitude);
		return this.intermediate;
	}
}
