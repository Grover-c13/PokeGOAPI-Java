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

package com.pokegoapi.api.player;

import POGOProtos.Data.PlayerBadgeOuterClass.PlayerBadge;
import POGOProtos.Enums.BadgeTypeOuterClass.BadgeType;
import lombok.Getter;
import lombok.Setter;

public class Medal {
	@Getter
	@Setter
	private int rank;
	@Getter
	private BadgeType type;

	@Getter
	private final int startValue;
	@Getter
	private final double currentValue;
	@Getter
	private final int endValue;

	/**
	 * Creates a Medal with a PlayerBadge proto
	 * @param badge the proto to inititialize with
	 */
	public Medal(PlayerBadge badge) {
		this.type = badge.getBadgeType();
		this.rank = badge.getRank();
		this.startValue = badge.getStartValue();
		this.currentValue = badge.getCurrentValue();
		this.endValue = badge.getEndValue();
	}

	@Override
	public int hashCode() {
		return type.getNumber();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Medal && ((Medal) obj).type.equals(type);
	}
}
