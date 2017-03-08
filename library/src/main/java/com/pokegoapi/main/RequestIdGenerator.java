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
	private static final int MULTIPLIER = 16807;
	private static final int MODULUS = 0x7FFFFFFF;
	private static final int MQ = (int) Math.floor(MODULUS / MULTIPLIER);
	private static final int MR = MODULUS % MULTIPLIER;
	private int seed;
	private int count = 2;

	/**
	 * Creates request id generator with an inital seed
	 * @param seed the initial seed
	 */
	public RequestIdGenerator(int seed) {
		this.seed = seed;
	}

	/**
	 * Generates next request id and increments count
	 * @return the next request id
	 */
	public long next() {
		int temp = MULTIPLIER * (this.seed % MQ) - (MR * (int) Math.floor(this.seed / MQ));
		if (temp > 0) {
			this.seed = temp;
		} else {
			this.seed = temp + MODULUS;
		}
		return this.count++ | (long) this.seed << 32;
	}
}
