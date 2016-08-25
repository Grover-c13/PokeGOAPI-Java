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

package com.pokegoapi.api.player.stats.meta;

import POGOProtos.Enums.BadgeTypeOuterClass.BadgeType;

/**
 * The Badge/Medal/Achievement class type.
 * 
 * @author gionata-bisciari
 *
 */
public class BadgeMeta {

	/**
	 * The badge type.
	 */
	private BadgeType badgeType;

	/**
	 * The badge title.
	 */
	private String title;

	/**
	 * The badge description.
	 */
	private String description;

	/**
	 * The bronze achievement value.
	 */
	private Integer bronzeAchievementValue;

	/**
	 * The silver achievement value.
	 */
	private Integer silverAchievementValue;

	/**
	 * The gold achievement value.
	 */
	private Integer goldAchievementValue;

	/**
	 * Creates a new BadgeMeta.
	 * 
	 * @param badgeType
	 *            The badge type.
	 * @param title
	 *            The title of the the badge.
	 * @param description
	 *            The description of the badge.
	 */
	protected BadgeMeta(BadgeType badgeType, String title, String description) {
		this.badgeType = badgeType;
		this.title = new String(title);
		this.description = new String(description);
	}

	/**
	 * Creates a new BadgeMeta.
	 * 
	 * @param badgeType
	 *            The badge type.
	 * @param title
	 *            The title of the badge.
	 * @param description
	 *            The description of the badge.
	 * @param bronzeAchievementValue
	 *            The bronze achievement value.
	 * @param silverAchievementValue
	 *            The silver achievement value.
	 * @param goldAchievementValue
	 *            The gold achievement value.
	 */
	protected BadgeMeta(BadgeType badgeType, String title, String description, Integer bronzeAchievementValue,
			Integer silverAchievementValue, Integer goldAchievementValue) {
		this.badgeType = badgeType;
		this.title = new String(title);
		this.description = new String(description);
		this.bronzeAchievementValue = new Integer(bronzeAchievementValue);
		this.silverAchievementValue = new Integer(silverAchievementValue);
		this.goldAchievementValue = new Integer(goldAchievementValue);
	}

	/**
	 * Get badge type.
	 * 
	 * @return Badge type.
	 */
	public BadgeType getBadgeType() {
		return badgeType;
	}

	/**
	 * Get badge title.
	 * 
	 * @return Badge title.
	 */
	public String getTitle() {
		return new String(title);
	}

	/**
	 * Get badge description.
	 * 
	 * @return Badge description;
	 */
	public String getDescription() {
		return new String(description);
	}

	/**
	 * Get bronze achievement value.
	 * 
	 * @return Bronze achievement value.
	 */
	public Integer getBronzeAchievementValue() {
		return new Integer(bronzeAchievementValue);
	}

	/**
	 * Get silver achievement value.
	 * 
	 * @return Silver achievement value.
	 */
	public Integer getSilverAchievementValue() {
		return new Integer(silverAchievementValue);
	}

	/**
	 * Get gold achievement value;
	 * 
	 * @return Gold achievement value.
	 */
	public Integer getGoldAchievementValue() {
		return new Integer(goldAchievementValue);
	}
}