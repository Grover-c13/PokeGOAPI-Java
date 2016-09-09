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

package com.pokegoapi.util.hash;

import lombok.Getter;

import java.util.List;

public class Hash {
	@Getter
	public final int locationAuthHash;
	@Getter
	public final int locationHash;
	@Getter
	public final List<Long> requestHashes;

	/**
	 * Creates a hash object
	 *
	 * @param locationAuthHash the hash of the location and auth ticket
	 * @param locationHash the hash of the location
	 * @param requestHashes the hash of each request
	 */
	public Hash(int locationAuthHash, int locationHash, List<Long> requestHashes) {
		this.locationAuthHash = locationAuthHash;
		this.locationHash = locationHash;
		this.requestHashes = requestHashes;
	}
}
