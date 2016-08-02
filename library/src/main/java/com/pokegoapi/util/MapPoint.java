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

<<<<<<< HEAD:library/src/main/java/com/pokegoapi/util/MapPoint.java
/**
 * @author Olaf Braun - Software Development
 * @version 1.0
 */
public interface MapPoint {
	/**
	 * Gets latitude.
	 *
	 * @return the latitude
	 */
	double getLatitude();

	/**
	 * Gets longitude.
	 *
	 * @return the longitude
	 */
	double getLongitude();
=======
import POGOProtos.Data.Player.DailyBonusOuterClass;
import lombok.Data;

@Data
public class DailyBonus {
	private final DailyBonusOuterClass.DailyBonus proto;

	public DailyBonus(DailyBonusOuterClass.DailyBonus proto ) {
		this.proto = proto;
	}

	public long getNextCollectedTimestampMs() {
		return proto.getNextCollectedTimestampMs();
	}

	public long getNextDefenderBonusCollectTimestampMs() {
		return proto.getNextDefenderBonusCollectTimestampMs();
	}
>>>>>>> d1cd3f98c5c1ecb22cb0f363a0b430d84a66919e:src/main/java/com/pokegoapi/api/player/DailyBonus.java
}
