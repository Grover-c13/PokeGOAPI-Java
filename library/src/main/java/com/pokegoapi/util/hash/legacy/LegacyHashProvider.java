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

package com.pokegoapi.util.hash.legacy;

import com.pokegoapi.exceptions.request.HashException;
import com.pokegoapi.util.hash.Hash;
import com.pokegoapi.util.hash.HashProvider;

/**
 * 0.45.0 local hash provider, no key required
 *
 * @deprecated Niantic have disabled use of invalid hashes,
 * {@link com.pokegoapi.util.hash.pokehash.PokeHashProvider} must be used now
 */
@Deprecated
public class LegacyHashProvider implements HashProvider {
	@Override
	public Hash provide(long timestamp, double latitude, double longitude, double altitude, byte[] authTicket,
			byte[] sessionData, byte[][] requests) throws HashException {
		throw new HashException(this.getClass().getName() + " is no longer supported");
	}

	@Override
	public int getHashVersion() {
		return 4500;
	}

	@Override
	public long getUNK25() {
		return -816976800928766045L;
	}
}
