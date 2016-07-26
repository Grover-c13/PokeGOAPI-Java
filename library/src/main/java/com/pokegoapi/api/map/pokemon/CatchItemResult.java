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

package com.pokegoapi.api.map.pokemon;

import POGOProtos.Networking.Responses.UseItemCaptureResponseOuterClass;
import POGOProtos.Networking.Responses.UseItemCaptureResponseOuterClass.UseItemCaptureResponse;

public class CatchItemResult {
	private UseItemCaptureResponse proto;

	public CatchItemResult(UseItemCaptureResponse proto) {
		this.proto = proto;
	}

	public boolean getSuccess() {
		return proto.getSuccess();
	}

	public double getItemCaptureMult() {
		return proto.getItemCaptureMult();
	}

	public double getItemFleeMult() {
		return proto.getItemFleeMult();
	}

	public boolean getStopMovement() {
		return proto.getStopMovement();
	}

	public boolean getStopAttack() {
		return proto.getStopAttack();
	}

	public boolean getTargetMax() {
		return proto.getTargetMax();
	}

	public boolean getTargetSlow() {
		return proto.getTargetSlow();
	}
}
