package com.pokegoapi.util;

import java.nio.ByteBuffer;

public class NiaHash {
	private static final int HASH_SEED = 0x46E945F8;
	private static final long[] MAGIC_TABLE = new long[]{
			0x2DD7CAAEFCF073EBL, 0xA9209937349CFE9CL,
			0xB84BFC934B0E60EFL, 0xFF709C157B26E477L,
			0x3936FD8735455112L, 0xCA141BF22338D331L,
			0xDD40E749CB64FD02L, 0x5E268F564B0DEB26L,
			0x658239596BDEA9ECL, 0x31CEDF33AC38C624L,
			0x12F56816481B0CFDL, 0x94E9DE155F40F095L,
			0x5089C907844C6325L, 0xDF887E97D73C50E3L,
			0xAE8870787CE3C11DL, 0xA6767D18C58D2117L,
	};
	private static final UInt128 ROUND_MAGIC = new UInt128(0x081570AFDD535EC3L, 0xE3F0D44988BCDFABL);
	private static final long FINAL_MAGIC_0 = 0xCE7C4801D683E824L;
	private static final long FINAL_MAGIC_1 = 0x6823775B1DAAD522L;

	public static int hash32(byte[] buffer) {
		return hash32Salt(buffer, toBytes(HASH_SEED));
	}

	/**
	 * Computes 32-bit hash with salt
	 *
	 * @param buffer input to the hash function
	 * @param salt salt for the hash function
	 * @return hash for given inputs
	 */
	public static int hash32Salt(byte[] buffer, byte[] salt) {
		long result = hash64Salt(buffer, salt);
		return (int) ((result & 0xFFFFFFFFL) ^ (result >>> 32));
	}

	public static long hash64(byte[] buffer) {
		return hash64Salt(buffer, toBytes(HASH_SEED));
	}

	/**
	 * Computes 64-bit hash with salt
	 *
	 * @param buffer input to the hash function
	 * @param salt salt for the hash function
	 * @return hash for given inputs
	 */
	public static long hash64Salt(byte[] buffer, byte[] salt) {
		byte[] newBuffer = new byte[buffer.length + salt.length];
		System.arraycopy(salt, 0, newBuffer, 0, salt.length);
		System.arraycopy(buffer, 0, newBuffer, salt.length, buffer.length);
		return computeHash(newBuffer, newBuffer.length);
	}

	public static long hash64Salt64(byte[] buffer, long salt) {
		byte[] saltBytes = ByteBuffer.allocate(8).putLong(salt).array();
		return hash64Salt(buffer, saltBytes);
	}

	/**
	 * Computes hash for given input
	 *
	 * @param in input to hash function
	 * @param length length of input
	 * @return hash for given input
	 */
	public static long computeHash(byte[] in, int length) {
		int chunkCount = length >> 7;

		// copy tail, pad with zeroes
		// TODO: try to avoid memcopy (work in place)
		byte[] tail = new byte[128];
		int tailSize = length & 0x7F;
		System.arraycopy(in, length - tailSize, tail, 0, tailSize);

		UInt128 hash;
		if (chunkCount != 0) {
			hash = hashChunk(in, 128, 0); // Hash the first 128 bytes
		} else {
			hash = hashChunk(tail, tailSize, 0); // Hash the tail
		}

		hash = hash.add(ROUND_MAGIC);
		int offset = 0;
		if (chunkCount != 0) {
			while (--chunkCount != 0) {
				offset += 128;
				hash = hashMulAdd(hash, ROUND_MAGIC, hashChunk(in, 128, offset));
			}
			if (tailSize != 0) {
				hash = hashMulAdd(hash, ROUND_MAGIC, hashChunk(tail, tailSize, 0));
			}
		}

		// Finalize the hash
		hash.add(new UInt128(0, tailSize * 8));
		UInt128 temporary = new UInt128(hash);
		temporary.add(new UInt128(1L, 0L));
		if (temporary.high < 0) {
			hash = temporary;
		}
		hash.clearHighBits(1);

		long hashHigh = hash.high;
		long hashLow = hash.low;

		long hash1 = hashHigh + (hashLow >>> 32);
		hash1 = ((hash1 + (hash1 >>> 32) + 1L) >>> 32) + hashHigh;
		long hash2 = (hash1 << 32) + hashLow;

		long magicHash1 = hash1 + FINAL_MAGIC_0;
		if (unsignedCompare(magicHash1, hash1)) {
			magicHash1 += 0x101L;
		}

		long magicHash2 = hash2 + FINAL_MAGIC_1;
		if (unsignedCompare(magicHash2, hash2)) {
			magicHash2 += 0x101L;
		}

		UInt128 unsignedHash = UInt128.multiply(magicHash1, magicHash2);
		unsignedHash.multiply(0x101L);
		unsignedHash.multiply(0x101L);

		if (unsignedHash.high != 0L) {
			unsignedHash.add(new UInt128(0x101L, 0));
		}
		if (unsignedCompare(0xFFFFFFFFFFFFFEFEL, unsignedHash.low)) {
			unsignedHash.add(new UInt128(0x101L, 0));
		}

		return unsignedHash.low;
	}

	private static UInt128 hashChunk(byte[] chunk, int size, int masterOffset) {
		UInt128 hash = new UInt128(0L, 0L);
		for (int i = 0; i < 8; i++) {
			int offset = i * 16;
			if (offset >= size) {
				break;
			}
			long first = readInt64(chunk, masterOffset + offset);
			long second = readInt64(chunk, masterOffset + offset + 8);
			long even = first + (MAGIC_TABLE[i * 2]);
			long odd = second + (MAGIC_TABLE[i * 2 + 1]);
			UInt128 mul = UInt128.multiply(even, odd);
			hash.add(mul);
		}
		return hash.clearHighBits(2);
	}

	private static UInt128 hashMulAdd(UInt128 hash, UInt128 mul, UInt128 add) {
		long a0 = add.low & 0xFFFFFFFFL;
		long a1 = add.low >>> 32;
		long a23 = add.high;
		long m0 = mul.low & 0xFFFFFFFFL;
		long m1 = mul.low >>> 32;
		long m2 = mul.high & 0xFFFFFFFFL;
		long m3 = mul.high >>> 32;
		long h0 = hash.low & 0xFFFFFFFFL;
		long h1 = hash.low >>> 32;
		long h2 = hash.high & 0xFFFFFFFFL;
		long h3 = hash.high >>> 32;

		/* Column sums, before carry */
		long c0 = (h0 * m0);
		long c1 = (h0 * m1) + (h1 * m0);
		long c2 = (h0 * m2) + (h1 * m1) + (h2 * m0);
		long c3 = (h0 * m3) + (h1 * m2) + (h2 * m1) + (h3 * m0);
		long c4 = (h1 * m3) + (h2 * m2) + (h3 * m1);
		long c5 = (h2 * m3) + (h3 * m2);
		long c6 = (h3 * m3);

		/* Combine, add, and carry (bugs included) */
		long r2 = c2 + (c6 << 1) + a23;
		long r3 = c3 + (r2 >>> 32);

		long r0 = c0 + (c4 << 1) + a0 + (r3 >>> 31);
		long r1 = c1 + (c5 << 1) + a1 + (r0 >>> 32);

		/* Return as uint128_t */
		// no carry during addition as bit63 = 0
		return new UInt128((r1 << 32) | (r0 & 0xFFFFFFFFL), ((r3 << 33 >>> 1) | (r2 & 0xFFFFFFFFL)) + (r1 >>> 32));
	}

	private static long readInt64(byte[] bytes, int offset) { // 01, 02, 03, 04, 05, 06, 07, 08 -> 0x0807060504030201
		// endian-safe read 64-bit integer
		long value = 0;
		for (int i = 7; i >= 0; i--) {
			value = (value << 8) | (bytes[offset + i] & 0xFF);
		}
		return value;
	}

	private static boolean unsignedCompare(long first, long second) {
		return (first < second) ^ (first < 0) ^ (second < 0);
	}

	/**
	 * Converts given integer to an array of 4 bytes.
	 *
	 * @param value value to convert
	 * @return an array of 4 bytes containing the given integer
	 */
	public static byte[] toBytes(int value) {
		byte[] ret = new byte[4];
		ret[3] = (byte) (value & 0xFF);
		ret[2] = (byte) ((value >> 8) & 0xFF);
		ret[1] = (byte) ((value >> 16) & 0xFF);
		ret[0] = (byte) ((value >> 24) & 0xFF);
		return ret;
	}
}