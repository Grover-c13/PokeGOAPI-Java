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

import POGOProtos.Data.Player.PlayerAvatarOuterClass;
import POGOProtos.Enums.GenderOuterClass.Gender;
import lombok.Data;

@Data
public class PlayerAvatar {
	private PlayerAvatarOuterClass.PlayerAvatar avatar;

	PlayerAvatar() {
	}

	final void update(PlayerAvatarOuterClass.PlayerAvatar data) {
		this.avatar = data;
	}

	public int getSkin() {
		return avatar.getSkin();
	}

	public int getHair() {
		return avatar.getHair();
	}

	public int getShirt() {
		return avatar.getShirt();
	}

	public int getPants() {
		return avatar.getPants();
	}

	public int getHat() {
		return avatar.getHat();
	}

	public int getShoes() {
		return avatar.getShoes();
	}

	public int getGenderValue() {
		return avatar.getGenderValue();
	}

	public Gender getGender() {
		return avatar.getGender();
	}

	public int getEyes() {
		return avatar.getEyes();
	}

	public int getBackpack() {
		return avatar.getBackpack();
	}

	public static int getAvailableSkins() {
		return 4;
	}

	public static int getAvailableHair() {
		return 6;
	}

	public static int getAvailableEyes() {
		return 5;
	}

	public static int getAvailableHats() {
		return 5;
	}

	public static int getAvailableShirts(Gender gender) {
		return gender.getNumber() == Gender.MALE_VALUE ? 4 : 9;
	}

	public static int getAvailablePants(Gender gender) {
		return gender.getNumber() == Gender.MALE_VALUE ? 3 : 6;
	}

	public static int getAvailableShoes() {
		return 7;
	}

	public static int getAvailableBags(Gender gender) {
		return gender.getNumber() == Gender.MALE_VALUE ? 6 : 3;
	}
}
