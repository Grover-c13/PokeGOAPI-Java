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
import POGOProtos.Enums.GenderOuterClass;
import lombok.Data;

@Data
public class PlayerAvatar {
	private PlayerAvatarOuterClass.PlayerAvatar proto;

	public PlayerAvatar(PlayerAvatarOuterClass.PlayerAvatar proto) {
		this.proto = proto;
	}

	public int getSkin() {
		return proto.getSkin();
	}

	public int getHair() {
		return proto.getHair();
	}

	public int getShirt() {
		return proto.getShirt();
	}

	public int getPants() {
		return proto.getPants();
	}

	public int getHat() {
		return proto.getHat();
	}

	public int getShoes() {
		return proto.getShoes();
	}

	public GenderOuterClass.Gender getGender() {
		return proto.getGender();
	}

	public int getEyes() {
		return proto.getEyes();
	}

	public int getBackpack() {
		return proto.getBackpack();
	}
}
