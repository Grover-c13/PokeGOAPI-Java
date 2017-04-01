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

package com.pokegoapi.util.hash.crypto;

import java.security.InvalidKeyException;

public class Crypto {
	private static final byte[] KEY = new byte[]{
			(byte) 0x4F, (byte) 0xEB, (byte) 0x1C, (byte) 0xA5, (byte) 0xF6, (byte) 0x1A, (byte) 0x67, (byte) 0xCE,
			(byte) 0x43, (byte) 0xF3, (byte) 0xF0, (byte) 0x0C, (byte) 0xB1, (byte) 0x23, (byte) 0x88, (byte) 0x35,
			(byte) 0xE9, (byte) 0x8B, (byte) 0xE8, (byte) 0x39, (byte) 0xD8, (byte) 0x89, (byte) 0x8F, (byte) 0x5A,
			(byte) 0x3B, (byte) 0x51, (byte) 0x2E, (byte) 0xA9, (byte) 0x47, (byte) 0x38, (byte) 0xC4, (byte) 0x14
	};

	protected static class Rand {
		private long state;

		private Rand(long state) {
			this.state = state;
		}

		public byte next() {
			state = (state * 0x41C64E6D) + 0x3039;
			return (byte) ((state >> 16) & 0xFF);
		}
	}

	protected byte[] makeIv(Rand rand) {
		byte[] iv = new byte[TwoFish.BLOCK_SIZE];
		for (int i = 0; i < iv.length; i++) {
			iv[i] = rand.next();
		}
		return iv;
	}

	protected byte makeIntegrityByte(Rand rand) {
		return 0x21;
	}

	/**
	 * Encrypts the given signature
	 *
	 * @param input input data
	 * @param msSinceStart time since start
	 * @return encrypted signature
	 */
	public byte[] encrypt(byte[] input, long msSinceStart) {
		try {
			Object key = TwoFish.makeKey(KEY);

			Rand rand = new Rand(msSinceStart);
			byte[] iv = this.makeIv(rand);
			int blockCount = (input.length + 256) / 256;
			int outputSize = (blockCount * 256) + 5;
			byte[] output = new byte[outputSize];

			output[0] = (byte) (msSinceStart >> 24);
			output[1] = (byte) (msSinceStart >> 16);
			output[2] = (byte) (msSinceStart >> 8);
			output[3] = (byte) msSinceStart;

			System.arraycopy(input, 0, output, 4, input.length);
			output[outputSize - 2] = (byte) (256 - input.length % 256);

			for (int offset = 0; offset < blockCount * 256; offset += TwoFish.BLOCK_SIZE) {
				for (int i = 0; i < TwoFish.BLOCK_SIZE; i++) {
					output[4 + offset + i] ^= iv[i];
				}

				byte[] block = TwoFish.blockEncrypt(output, offset + 4, key);
				System.arraycopy(block, 0, output, offset + 4, block.length);
				System.arraycopy(output, 4 + offset, iv, 0, TwoFish.BLOCK_SIZE);
			}

			output[outputSize - 1] = this.makeIntegrityByte(rand);
			return output;
		} catch (InvalidKeyException e) {
			return null;
		}
	}
}