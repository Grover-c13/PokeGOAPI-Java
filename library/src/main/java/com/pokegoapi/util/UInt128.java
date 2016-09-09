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

package com.pokegoapi.util;

public class UInt128 {
	long low;
	long high;

	UInt128(long low, long high) {
		this.low = low;
		this.high = high;
	}

	UInt128(UInt128 value) {
		this.low = value.low;
		this.high = value.high;
	}

	UInt128 add(UInt128 value) {
		boolean sx = this.low < 0;
		boolean sy = value.low < 0;
		this.low += value.low;
		this.high += value.high;
		// intuitively I believe there should be something more beautiful than this,
		// maybe: if (this.lo < value.lo) then carry...
		// Please let me know, if you find out
		if (sx && sy || (this.low > 0 && (sx || sy))) {
			this.high++;
		}
		return this;
	}

	/**
	 * @param shift Number of bits to shift (0..31)
	 */
	UInt128 clearHighBits(int shift) {
		this.high <<= shift;
		this.high >>>= shift;
		return this;
	}

	static UInt128 multiply(long first, long second) {
		long secondLow = second & 0xFFFFFFFFL;
		long secondHigh = second >>> 32;
		long firstLow = first & 0xFFFFFFFFL;
		long firstHigh = first >>> 32;

		// The upper 64 bits of the output is a combination of several factors
		long high = secondHigh * firstHigh;

		long p01 = firstLow * secondHigh;
		long p10 = firstHigh * secondLow;
		long p00 = firstLow * secondLow;

		// Add the high parts directly in.
		high += (p01 >>> 32);
		high += (p10 >>> 32);

		// Account for the possible carry from the low parts.
		long p2 = (p00 >>> 32) + (p01 & 0xFFFFFFFFL) + (p10 & 0xFFFFFFFFL);
		high += (p2 >>> 32);

		return new UInt128(first * second, high);
	}

	void multiply(long value) {
		UInt128 result = UInt128.multiply(value, this.high).add(new UInt128(this.low, 0));
		this.high = result.high;
		this.low = result.low;
	}
}