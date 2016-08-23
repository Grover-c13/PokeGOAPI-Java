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

package com.pokegoapi.api.map.fort;

import POGOProtos.Enums.TeamColorOuterClass;
import POGOProtos.Map.Fort.FortModifierOuterClass;
import POGOProtos.Map.Fort.FortTypeOuterClass;
import POGOProtos.Networking.Responses.FortDetailsResponseOuterClass;
import com.google.protobuf.ProtocolStringList;

import java.util.List;

public class FortDetails {
	private FortDetailsResponseOuterClass.FortDetailsResponse proto;

	public FortDetails(FortDetailsResponseOuterClass.FortDetailsResponse proto) {
		this.proto = proto;
	}

	public String getId() {
		return proto.getFortId();
	}

	public TeamColorOuterClass.TeamColor getTeam() {
		return proto.getTeamColor();
	}

	public String getName() {
		return proto.getName();
	}

	public ProtocolStringList getImageUrl() {
		return proto.getImageUrlsList();
	}

	public int getFp() {
		return proto.getFp();
	}

	public int getStamina() {
		return proto.getStamina();
	}

	public int getMaxStamina() {
		return proto.getMaxStamina();
	}

	public FortTypeOuterClass.FortType getFortType() {
		return proto.getType();
	}

	public double getLatitude() {
		return proto.getLatitude();
	}

	public double getLongitude() {
		return proto.getLongitude();
	}

	public String getDescription() {
		return proto.getDescription();
	}

	public List<FortModifierOuterClass.FortModifier> getModifier() {
		return proto.getModifiersList();
	}
}
