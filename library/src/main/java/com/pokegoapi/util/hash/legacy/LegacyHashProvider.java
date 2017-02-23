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

import com.pokegoapi.util.NiaHash;
import com.pokegoapi.util.hash.Hash;
import com.pokegoapi.util.hash.HashProvider;
import com.pokegoapi.util.hash.crypto.Crypto;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 0.45.0 local hash provider, no key required
 */
public class LegacyHashProvider implements HashProvider {
	private static final int VERSION = 4500;
	private static final long UNK25 = -816976800928766045L;

	@Override
	public Hash provide(long timestamp, double latitude, double longitude, double altitude, byte[] authTicket,
			byte[] sessionData, byte[][] requests) {
		int locationHash = getLocationHash(latitude, longitude, altitude);
		int locationAuthHash = getLocationAuthHash(latitude, longitude, altitude, authTicket);
		List<Long> requestHashes = new ArrayList<>();
		for (byte[] request : requests) {
			requestHashes.add(getRequestHash(request, authTicket));
		}
		return new Hash(locationAuthHash, locationHash, requestHashes);
	}

	@Override
	public int getHashVersion() {
		return VERSION;
	}

	@Override
	public Crypto getCrypto() {
		return Crypto.LEGACY;
	}

	@Override
	public long getUNK25() {
		return UNK25;
	}

	private int getLocationHash(double latitude, double longitude, double altitude) {
		byte[] bytes = new byte[24];
		System.arraycopy(toBytes(latitude), 0, bytes, 0, 8);
		System.arraycopy(toBytes(longitude), 0, bytes, 8, 8);
		System.arraycopy(toBytes(altitude), 0, bytes, 16, 8);

		return NiaHash.hash32(bytes);
	}

	private int getLocationAuthHash(double latitude, double longitude, double altitude, byte[] authTicket) {
		byte[] bytes = new byte[24];
		System.arraycopy(toBytes(latitude), 0, bytes, 0, 8);
		System.arraycopy(toBytes(longitude), 0, bytes, 8, 8);
		System.arraycopy(toBytes(altitude), 0, bytes, 16, 8);
		int seed = NiaHash.hash32(authTicket);
		return NiaHash.hash32Salt(bytes, NiaHash.toBytes(seed));
	}

	private long getRequestHash(byte[] request, byte[] authTicket) {
		byte[] seed = ByteBuffer.allocate(8).putLong(NiaHash.hash64(authTicket)).array();
		return NiaHash.hash64Salt(request, seed);
	}

	private byte[] toBytes(double input) {
		long rawDouble = Double.doubleToRawLongBits(input);
		return new byte[]{
				(byte) (rawDouble >>> 56),
				(byte) (rawDouble >>> 48),
				(byte) (rawDouble >>> 40),
				(byte) (rawDouble >>> 32),
				(byte) (rawDouble >>> 24),
				(byte) (rawDouble >>> 16),
				(byte) (rawDouble >>> 8),
				(byte) rawDouble
		};
	}
}
