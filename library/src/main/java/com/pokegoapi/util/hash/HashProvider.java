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

import com.pokegoapi.exceptions.request.HashException;

public interface HashProvider {
	/**
	 * Provides a hash for the given input
	 *
	 * @param timestamp timestamp to hash
	 * @param latitude latitude to hash
	 * @param longitude longitude to hash
	 * @param altitude altitude to hash
	 * @param authTicket auth ticket to hash
	 * @param sessionData session data to hash
	 * @param requests request data to hash
	 * @return the hash for the given input
	 * @throws HashException if an exception occurs while hashing the given inputs
	 */
	Hash provide(long timestamp, double latitude, double longitude, double altitude,
			byte[] authTicket, byte[] sessionData, byte[][] requests)
			throws HashException;

	/**
	 * @return the version this hash supports, for example 4500 = 0.45.0 and 5300 = 0.53.0
	 */
	int getHashVersion();

	/**
	 * @return the unknown 25 value used with this hash
	 */
	long getUNK25();

}
