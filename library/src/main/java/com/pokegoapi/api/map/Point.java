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

package com.pokegoapi.api.map;

import POGOProtos.Map.SpawnPointOuterClass;
import lombok.Getter;
import lombok.Setter;

public class Point {
	@Getter
	@Setter
	private static double longitude;
	@Getter
	@Setter
	private static double latitude;

	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Point(SpawnPointOuterClass.SpawnPoint spawnpoint) {
		this.latitude = spawnpoint.getLatitude();
		this.longitude = spawnpoint.getLongitude();
	}
}
