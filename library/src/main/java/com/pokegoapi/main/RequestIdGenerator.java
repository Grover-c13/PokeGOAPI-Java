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

package com.pokegoapi.main;

public class RequestIdGenerator {
	private static final long MULTIPLIER = 16807;
	private static final long MODULUS = 0x7FFFFFFF;

	private long rpcIdHigh = 1;
	private long rpcId = 2;

	/**
	 * Generates next request id and increments count
	 * @return the next request id
	 */
	public long next() {
		rpcIdHigh = MULTIPLIER * rpcIdHigh % MODULUS;
		return rpcId++ | (rpcIdHigh << 32);
	}
}
