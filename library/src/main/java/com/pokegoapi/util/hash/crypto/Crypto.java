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

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Crypto {
	public static final Crypto LEGACY = new Crypto();

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
		byte[] iv = new byte[256];
		for (int i = 0; i < 256; i++) {
			iv[i] = rand.next();
		}
		return iv;
	}

	protected byte makeIntegrityByte(Rand rand) {
		byte lastbyte = rand.next();

		byte v74 = (byte) ((lastbyte ^ 0x0C) & lastbyte);
		byte v75 = (byte) (((~v74 & 0x67) | (v74 & 0x98)) ^ 0x6F | (v74 & 8));
		return v75;
	}

	/**
	 * Shuffles bytes.
	 *
	 * @param input input data
	 * @param msSinceStart time since start
	 * @return shuffled bytes
	 */
	public CipherText encrypt(byte[] input, long msSinceStart) {
		Rand rand = new Rand(msSinceStart);

		byte[] arr3;
		CipherText output;

		byte[] iv = makeIv(rand);
		output = new CipherText(this, input, msSinceStart, rand);

		for (int i = 0; i < output.content.size(); ++i) {
			byte[] current = output.content.get(i);

			for (int j = 0; j < 256; j++) {
				current[j] ^= iv[j];
			}

			int[] temp2 = new int[0x100 / 4];
			// only use 256 bytes from input.
			IntBuffer intBuf = ByteBuffer.wrap(Arrays.copyOf(current, 0x100))//
					.order(ByteOrder.BIG_ENDIAN)//
					.asIntBuffer();
			intBuf.get(temp2);
			arr3 = Shuffle.shuffle2(temp2);

			System.arraycopy(arr3, 0, iv, 0, 256);
			System.arraycopy(arr3, 0, current, 0, 256);
		}

		return output;
	}

	public static class CipherText {
		Crypto crypto;
		Rand rand;
		byte[] prefix;
		public ArrayList<byte[]> content;

		int totalsize;
		int inputLen;

		byte[] intBytes(long value) {
			ByteBuffer buffer = ByteBuffer.allocate(4);
			buffer.putInt(new BigInteger(String.valueOf(value)).intValue());
			return buffer.array();
		}

		/**
		 * Create new CipherText with contents and IV.
		 *
		 * @param crypto the crypto instance to use
		 * @param input the contents
		 * @param ms the time
		 * @param rand the rand object to use
		 */
		public CipherText(Crypto crypto, byte[] input, long ms, Rand rand) {
			this.crypto = crypto;
			this.inputLen = input.length;
			this.rand = rand;
			prefix = new byte[32];
			content = new ArrayList<>();
			int roundedsize = input.length + (256 - (input.length % 256));
			for (int i = 0; i < roundedsize / 256; ++i) {
				content.add(new byte[256]);
			}
			totalsize = roundedsize + 5;

			prefix = intBytes(ms);

			for (int i = 0; i < input.length; ++i)
				content.get(i / 256)[i % 256] = input[i];
			byte[] last = content.get(content.size() - 1);
			last[last.length - 1] = (byte) (256 - (input.length % 256));

		}

		/**
		 * Convert this Ciptext to a ByteBuffer
		 *
		 * @return contents as bytebuffer
		 */
		public ByteBuffer toByteBuffer() {
			ByteBuffer buff = ByteBuffer.allocate(totalsize).put(prefix);
			for (int i = 0; i < content.size(); ++i)
				buff.put(content.get(i));

			buff.put(totalsize - 1, crypto.makeIntegrityByte(rand));
			return buff;
		}
	}
}