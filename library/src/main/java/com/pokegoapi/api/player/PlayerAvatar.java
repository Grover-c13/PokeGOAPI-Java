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
import lombok.Data;
import lombok.Getter;

import java.security.SecureRandom;

@Data
public class PlayerAvatar {
	@Getter
	public PlayerAvatarOuterClass.PlayerAvatar avatar;

	public PlayerAvatar(PlayerAvatarOuterClass.PlayerAvatar data) {
		avatar = data;
	}

	/**
	 * Constructs an avatar with individual parameters
	 *
	 * @param gender the gender of this avatar
	 * @param skin the skin index of this avatar
	 * @param hair the hair index of this avatar
	 * @param shirt the shirt index of this avatar
	 * @param pants the pants index of this avatar
	 * @param hat the hat index of this avatar
	 * @param shoes the shoe index of this avatar
	 * @param eyes the eye index of this avatar
	 * @param backpack the backpack index of this avatar
	 */
	public PlayerAvatar(PlayerGender gender, int skin, int hair, int shirt, int pants,
						int hat, int shoes, int eyes, int backpack) {
		avatar = PlayerAvatarOuterClass.PlayerAvatar.newBuilder()
				.setAvatar(gender.ordinal())
				.setSkin(skin)
				.setHair(hair)
				.setShirt(shirt)
				.setPants(pants)
				.setHat(hat)
				.setShoes(shoes)
				.setEyes(eyes)
				.setBackpack(backpack)
				.build();
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
		return avatar.getAvatar();
	}

	public PlayerGender getGender() {
		return PlayerGender.get(avatar.getAvatar());
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

	public static int getAvailableShirts(PlayerGender gender) {
		return gender == PlayerGender.MALE ? 4 : 9;
	}

	public static int getAvailablePants(PlayerGender gender) {
		return gender == PlayerGender.MALE ? 3 : 6;
	}

	public static int getAvailableShoes() {
		return 7;
	}

	public static int getAvailableBags(PlayerGender gender) {
		return gender == PlayerGender.MALE ? 6 : 3;
	}

	/**
	 * Creates a random avatar based on the given gender
	 *
	 * @param gender the gender to generate based on
	 * @return a randomly generated avatar
	 */
	public static PlayerAvatar random(PlayerGender gender) {
		SecureRandom random = new SecureRandom();
		return new PlayerAvatar(gender,
				random.nextInt(PlayerAvatar.getAvailableSkins()),
				random.nextInt(PlayerAvatar.getAvailableHair()),
				random.nextInt(PlayerAvatar.getAvailableShirts(gender)),
				random.nextInt(PlayerAvatar.getAvailablePants(gender)),
				random.nextInt(PlayerAvatar.getAvailableHats()),
				random.nextInt(PlayerAvatar.getAvailableShoes()),
				random.nextInt(PlayerAvatar.getAvailableEyes()),
				random.nextInt(PlayerAvatar.getAvailableBags(gender)));
	}
}