/*
 * Copyright (c) 1997, 1998 Systemics Ltd on behalf of
 * the Cryptix Development Team. All rights reserved.
 */

package com.pokegoapi.util.hash.crypto;

import java.security.InvalidKeyException;

/**
 * Twofish is an AES candidate algorithm. It is a balanced 128-bit Feistel
 * cipher, consisting of 16 rounds. In each round, a 64-bit S-box value is
 * computed from 64 bits of the block, and this value is xored into the other
 * half of the block. The two half-blocks are then exchanged, and the next
 * round begins. Before the first round, all input bits are xored with key-
 * dependent "whitening" subkeys, and after the final round the output bits
 * are xored with other key-dependent whitening subkeys; these subkeys are
 * not used anywhere else in the algorithm.
 * Twofish was submitted by Bruce Schneier, Doug Whiting, John Kelsey, Chris Hall and David Wagner.
 * <b>Copyright</b> &copy; 1998
 * <a href="http://www.systemics.com/">Systemics Ltd</a> on behalf of the
 * <a href="http://www.systemics.com/docs/cryptix/">Cryptix Development Team</a>.
 * All rights reserved.
 *
 * @author Raif S. Naffah
 */
public final class TwoFish {
	public static final int BLOCK_SIZE = 16;
	private static final int ROUNDS = 16;
	private static final int INPUT_WHITEN = 0;
	private static final int OUTPUT_WHITEN = INPUT_WHITEN + BLOCK_SIZE / 4;
	private static final int ROUND_SUBKEYS = OUTPUT_WHITEN + BLOCK_SIZE / 4;
	private static final int SK_STEP = 0x02020202;
	private static final int SK_BUMP = 0x01010101;
	private static final int SK_ROTL = 9;

	/**
	 * Fixed 8x8 permutation S-boxes
	 */
	private static final byte[][] P = new byte[][]{
			{
					(byte) 0xA9, (byte) 0x67, (byte) 0xB3, (byte) 0xE8,
					(byte) 0x04, (byte) 0xFD, (byte) 0xA3, (byte) 0x76,
					(byte) 0x9A, (byte) 0x92, (byte) 0x80, (byte) 0x78,
					(byte) 0xE4, (byte) 0xDD, (byte) 0xD1, (byte) 0x38,
					(byte) 0x0D, (byte) 0xC6, (byte) 0x35, (byte) 0x98,
					(byte) 0x18, (byte) 0xF7, (byte) 0xEC, (byte) 0x6C,
					(byte) 0x43, (byte) 0x75, (byte) 0x37, (byte) 0x26,
					(byte) 0xFA, (byte) 0x13, (byte) 0x94, (byte) 0x48,
					(byte) 0xF2, (byte) 0xD0, (byte) 0x8B, (byte) 0x30,
					(byte) 0x84, (byte) 0x54, (byte) 0xDF, (byte) 0x23,
					(byte) 0x19, (byte) 0x5B, (byte) 0x3D, (byte) 0x59,
					(byte) 0xF3, (byte) 0xAE, (byte) 0xA2, (byte) 0x82,
					(byte) 0x63, (byte) 0x01, (byte) 0x83, (byte) 0x2E,
					(byte) 0xD9, (byte) 0x51, (byte) 0x9B, (byte) 0x7C,
					(byte) 0xA6, (byte) 0xEB, (byte) 0xA5, (byte) 0xBE,
					(byte) 0x16, (byte) 0x0C, (byte) 0xE3, (byte) 0x61,
					(byte) 0xC0, (byte) 0x8C, (byte) 0x3A, (byte) 0xF5,
					(byte) 0x73, (byte) 0x2C, (byte) 0x25, (byte) 0x0B,
					(byte) 0xBB, (byte) 0x4E, (byte) 0x89, (byte) 0x6B,
					(byte) 0x53, (byte) 0x6A, (byte) 0xB4, (byte) 0xF1,
					(byte) 0xE1, (byte) 0xE6, (byte) 0xBD, (byte) 0x45,
					(byte) 0xE2, (byte) 0xF4, (byte) 0xB6, (byte) 0x66,
					(byte) 0xCC, (byte) 0x95, (byte) 0x03, (byte) 0x56,
					(byte) 0xD4, (byte) 0x1C, (byte) 0x1E, (byte) 0xD7,
					(byte) 0xFB, (byte) 0xC3, (byte) 0x8E, (byte) 0xB5,
					(byte) 0xE9, (byte) 0xCF, (byte) 0xBF, (byte) 0xBA,
					(byte) 0xEA, (byte) 0x77, (byte) 0x39, (byte) 0xAF,
					(byte) 0x33, (byte) 0xC9, (byte) 0x62, (byte) 0x71,
					(byte) 0x81, (byte) 0x79, (byte) 0x09, (byte) 0xAD,
					(byte) 0x24, (byte) 0xCD, (byte) 0xF9, (byte) 0xD8,
					(byte) 0xE5, (byte) 0xC5, (byte) 0xB9, (byte) 0x4D,
					(byte) 0x44, (byte) 0x08, (byte) 0x86, (byte) 0xE7,
					(byte) 0xA1, (byte) 0x1D, (byte) 0xAA, (byte) 0xED,
					(byte) 0x06, (byte) 0x70, (byte) 0xB2, (byte) 0xD2,
					(byte) 0x41, (byte) 0x7B, (byte) 0xA0, (byte) 0x11,
					(byte) 0x31, (byte) 0xC2, (byte) 0x27, (byte) 0x90,
					(byte) 0x20, (byte) 0xF6, (byte) 0x60, (byte) 0xFF,
					(byte) 0x96, (byte) 0x5C, (byte) 0xB1, (byte) 0xAB,
					(byte) 0x9E, (byte) 0x9C, (byte) 0x52, (byte) 0x1B,
					(byte) 0x5F, (byte) 0x93, (byte) 0x0A, (byte) 0xEF,
					(byte) 0x91, (byte) 0x85, (byte) 0x49, (byte) 0xEE,
					(byte) 0x2D, (byte) 0x4F, (byte) 0x8F, (byte) 0x3B,
					(byte) 0x47, (byte) 0x87, (byte) 0x6D, (byte) 0x46,
					(byte) 0xD6, (byte) 0x3E, (byte) 0x69, (byte) 0x64,
					(byte) 0x2A, (byte) 0xCE, (byte) 0xCB, (byte) 0x2F,
					(byte) 0xFC, (byte) 0x97, (byte) 0x05, (byte) 0x7A,
					(byte) 0xAC, (byte) 0x7F, (byte) 0xD5, (byte) 0x1A,
					(byte) 0x4B, (byte) 0x0E, (byte) 0xA7, (byte) 0x5A,
					(byte) 0x28, (byte) 0x14, (byte) 0x3F, (byte) 0x29,
					(byte) 0x88, (byte) 0x3C, (byte) 0x4C, (byte) 0x02,
					(byte) 0xB8, (byte) 0xDA, (byte) 0xB0, (byte) 0x17,
					(byte) 0x55, (byte) 0x1F, (byte) 0x8A, (byte) 0x7D,
					(byte) 0x57, (byte) 0xC7, (byte) 0x8D, (byte) 0x74,
					(byte) 0xB7, (byte) 0xC4, (byte) 0x9F, (byte) 0x72,
					(byte) 0x7E, (byte) 0x15, (byte) 0x22, (byte) 0x12,
					(byte) 0x58, (byte) 0x07, (byte) 0x99, (byte) 0x34,
					(byte) 0x6E, (byte) 0x50, (byte) 0xDE, (byte) 0x68,
					(byte) 0x65, (byte) 0xBC, (byte) 0xDB, (byte) 0xF8,
					(byte) 0xC8, (byte) 0xA8, (byte) 0x2B, (byte) 0x40,
					(byte) 0xDC, (byte) 0xFE, (byte) 0x32, (byte) 0xA4,
					(byte) 0xCA, (byte) 0x10, (byte) 0x21, (byte) 0xF0,
					(byte) 0xD3, (byte) 0x5D, (byte) 0x0F, (byte) 0x00,
					(byte) 0x6F, (byte) 0x9D, (byte) 0x36, (byte) 0x42,
					(byte) 0x4A, (byte) 0x5E, (byte) 0xC1, (byte) 0xE0
			},
			{
					(byte) 0x75, (byte) 0xF3, (byte) 0xC6, (byte) 0xF4,
					(byte) 0xDB, (byte) 0x7B, (byte) 0xFB, (byte) 0xC8,
					(byte) 0x4A, (byte) 0xD3, (byte) 0xE6, (byte) 0x6B,
					(byte) 0x45, (byte) 0x7D, (byte) 0xE8, (byte) 0x4B,
					(byte) 0xD6, (byte) 0x32, (byte) 0xD8, (byte) 0xFD,
					(byte) 0x37, (byte) 0x71, (byte) 0xF1, (byte) 0xE1,
					(byte) 0x30, (byte) 0x0F, (byte) 0xF8, (byte) 0x1B,
					(byte) 0x87, (byte) 0xFA, (byte) 0x06, (byte) 0x3F,
					(byte) 0x5E, (byte) 0xBA, (byte) 0xAE, (byte) 0x5B,
					(byte) 0x8A, (byte) 0x00, (byte) 0xBC, (byte) 0x9D,
					(byte) 0x6D, (byte) 0xC1, (byte) 0xB1, (byte) 0x0E,
					(byte) 0x80, (byte) 0x5D, (byte) 0xD2, (byte) 0xD5,
					(byte) 0xA0, (byte) 0x84, (byte) 0x07, (byte) 0x14,
					(byte) 0xB5, (byte) 0x90, (byte) 0x2C, (byte) 0xA3,
					(byte) 0xB2, (byte) 0x73, (byte) 0x4C, (byte) 0x54,
					(byte) 0x92, (byte) 0x74, (byte) 0x36, (byte) 0x51,
					(byte) 0x38, (byte) 0xB0, (byte) 0xBD, (byte) 0x5A,
					(byte) 0xFC, (byte) 0x60, (byte) 0x62, (byte) 0x96,
					(byte) 0x6C, (byte) 0x42, (byte) 0xF7, (byte) 0x10,
					(byte) 0x7C, (byte) 0x28, (byte) 0x27, (byte) 0x8C,
					(byte) 0x13, (byte) 0x95, (byte) 0x9C, (byte) 0xC7,
					(byte) 0x24, (byte) 0x46, (byte) 0x3B, (byte) 0x70,
					(byte) 0xCA, (byte) 0xE3, (byte) 0x85, (byte) 0xCB,
					(byte) 0x11, (byte) 0xD0, (byte) 0x93, (byte) 0xB8,
					(byte) 0xA6, (byte) 0x83, (byte) 0x20, (byte) 0xFF,
					(byte) 0x9F, (byte) 0x77, (byte) 0xC3, (byte) 0xCC,
					(byte) 0x03, (byte) 0x6F, (byte) 0x08, (byte) 0xBF,
					(byte) 0x40, (byte) 0xE7, (byte) 0x2B, (byte) 0xE2,
					(byte) 0x79, (byte) 0x0C, (byte) 0xAA, (byte) 0x82,
					(byte) 0x41, (byte) 0x3A, (byte) 0xEA, (byte) 0xB9,
					(byte) 0xE4, (byte) 0x9A, (byte) 0xA4, (byte) 0x97,
					(byte) 0x7E, (byte) 0xDA, (byte) 0x7A, (byte) 0x17,
					(byte) 0x66, (byte) 0x94, (byte) 0xA1, (byte) 0x1D,
					(byte) 0x3D, (byte) 0xF0, (byte) 0xDE, (byte) 0xB3,
					(byte) 0x0B, (byte) 0x72, (byte) 0xA7, (byte) 0x1C,
					(byte) 0xEF, (byte) 0xD1, (byte) 0x53, (byte) 0x3E,
					(byte) 0x8F, (byte) 0x33, (byte) 0x26, (byte) 0x5F,
					(byte) 0xEC, (byte) 0x76, (byte) 0x2A, (byte) 0x49,
					(byte) 0x81, (byte) 0x88, (byte) 0xEE, (byte) 0x21,
					(byte) 0xC4, (byte) 0x1A, (byte) 0xEB, (byte) 0xD9,
					(byte) 0xC5, (byte) 0x39, (byte) 0x99, (byte) 0xCD,
					(byte) 0xAD, (byte) 0x31, (byte) 0x8B, (byte) 0x01,
					(byte) 0x18, (byte) 0x23, (byte) 0xDD, (byte) 0x1F,
					(byte) 0x4E, (byte) 0x2D, (byte) 0xF9, (byte) 0x48,
					(byte) 0x4F, (byte) 0xF2, (byte) 0x65, (byte) 0x8E,
					(byte) 0x78, (byte) 0x5C, (byte) 0x58, (byte) 0x19,
					(byte) 0x8D, (byte) 0xE5, (byte) 0x98, (byte) 0x57,
					(byte) 0x67, (byte) 0x7F, (byte) 0x05, (byte) 0x64,
					(byte) 0xAF, (byte) 0x63, (byte) 0xB6, (byte) 0xFE,
					(byte) 0xF5, (byte) 0xB7, (byte) 0x3C, (byte) 0xA5,
					(byte) 0xCE, (byte) 0xE9, (byte) 0x68, (byte) 0x44,
					(byte) 0xE0, (byte) 0x4D, (byte) 0x43, (byte) 0x69,
					(byte) 0x29, (byte) 0x2E, (byte) 0xAC, (byte) 0x15,
					(byte) 0x59, (byte) 0xA8, (byte) 0x0A, (byte) 0x9E,
					(byte) 0x6E, (byte) 0x47, (byte) 0xDF, (byte) 0x34,
					(byte) 0x35, (byte) 0x6A, (byte) 0xCF, (byte) 0xDC,
					(byte) 0x22, (byte) 0xC9, (byte) 0xC0, (byte) 0x9B,
					(byte) 0x89, (byte) 0xD4, (byte) 0xED, (byte) 0xAB,
					(byte) 0x12, (byte) 0xA2, (byte) 0x0D, (byte) 0x52,
					(byte) 0xBB, (byte) 0x02, (byte) 0x2F, (byte) 0xA9,
					(byte) 0xD7, (byte) 0x61, (byte) 0x1E, (byte) 0xB4,
					(byte) 0x50, (byte) 0x04, (byte) 0xF6, (byte) 0xC2,
					(byte) 0x16, (byte) 0x25, (byte) 0x86, (byte) 0x56,
					(byte) 0x55, (byte) 0x09, (byte) 0xBE, (byte) 0x91
			}
	};

	/**
	 * Define the fixed p0/p1 permutations used in keyed S-box lookup.
	 * By changing the following constant definitions, the S-boxes will
	 * automatically get changed in the Twofish engine.
	 */
	private static final int P_00 = 1;
	private static final int P_01 = 0;
	private static final int P_02 = 0;
	private static final int P_03 = P_01 ^ 1;
	private static final int P_04 = 1;
	private static final int P_10 = 0;
	private static final int P_11 = 0;
	private static final int P_12 = 1;
	private static final int P_13 = P_11 ^ 1;
	private static final int P_14 = 0;
	private static final int P_20 = 1;
	private static final int P_21 = 1;
	private static final int P_22 = 0;
	private static final int P_23 = P_21 ^ 1;
	private static final int P_24 = 0;
	private static final int P_30 = 0;
	private static final int P_31 = 1;
	private static final int P_32 = 1;
	private static final int P_33 = P_31 ^ 1;
	private static final int P_34 = 1;

	/**
	 * Primitive polynomial for GF(256)
	 */
	private static final int GF256_FDBK_2 = 0x169 / 2;
	private static final int GF256_FDBK_4 = 0x169 / 4;

	/**
	 * MDS matrix
	 */
	private static final int[][] MDS = new int[4][256];

	private static final int RS_GF_FDBK = 0x14D;

	static {
		int[] m1 = new int[2];
		int[] mxArray = new int[2];
		int[] myArray = new int[2];
		int first;
		int second;
		for (first = 0; first < 256; first++) {
			second = P[0][first] & 0xFF;
			m1[0] = second;
			mxArray[0] = mxX(second) & 0xFF;
			myArray[0] = mxY(second) & 0xFF;

			second = P[1][first] & 0xFF;
			m1[1] = second;
			mxArray[1] = mxX(second) & 0xFF;
			myArray[1] = mxY(second) & 0xFF;

			MDS[0][first] = m1[P_00]
					| mxArray[P_00] << 8
					| myArray[P_00] << 16
					| myArray[P_00] << 24;
			MDS[1][first] = myArray[P_10]
					| myArray[P_10] << 8
					| mxArray[P_10] << 16
					| m1[P_10] << 24;
			MDS[2][first] = mxArray[P_20]
					| myArray[P_20] << 8
					| m1[P_20] << 16
					| myArray[P_20] << 24;
			MDS[3][first] = mxArray[P_30]
					| m1[P_30] << 8
					| myArray[P_30] << 16
					| mxArray[P_30] << 24;
		}
	}

	private static final int lfsr1(int input) {
		return (input >> 1) ^ ((input & 0x01) != 0 ? GF256_FDBK_2 : 0);
	}

	private static final int lfsr2(int input) {
		return (input >> 2) ^ ((input & 0x02) != 0 ? GF256_FDBK_2 : 0) ^ ((input & 0x01) != 0 ? GF256_FDBK_4 : 0);
	}

	private static final int mxX(int input) {
		return input ^ lfsr2(input);
	}

	private static final int mxY(int input) {
		return input ^ lfsr1(input) ^ lfsr2(input);
	}

	/**
	 * Expand a user-supplied key material into a session key.
	 *
	 * @param bytes The 64/128/192/256-bit user-key to use.
	 * @return This cipher's round keys.
	 * @throws InvalidKeyException If the key is invalid.
	 */
	public static synchronized Object makeKey(byte[] bytes) throws InvalidKeyException {
		if (bytes == null)
			throw new InvalidKeyException("Empty key");
		int length = bytes.length;
		if (!(length == 8 || length == 16 || length == 24 || length == 32))
			throw new InvalidKeyException("Incorrect key length");

		int k64Cnt = length / 8;
		int subkeyCnt = ROUND_SUBKEYS + 2 * ROUNDS;
		int[] k32e = new int[4];
		int[] k32o = new int[4];
		int[] sboxkey = new int[4];
		int input = 0;
		int input2 = 0;
		int offset = 0;
		for (input = 0, input2 = k64Cnt - 1; input < 4 && offset < length; input++, input2--) {
			k32e[input] = (bytes[offset++] & 0xFF)
					| (bytes[offset++] & 0xFF) << 8
					| (bytes[offset++] & 0xFF) << 16
					| (bytes[offset++] & 0xFF) << 24;
			k32o[input] = (bytes[offset++] & 0xFF)
					| (bytes[offset++] & 0xFF) << 8
					| (bytes[offset++] & 0xFF) << 16
					| (bytes[offset++] & 0xFF) << 24;
			sboxkey[input2] = rsMdsEncode(k32e[input], k32o[input]);
		}
		int input3;
		int input4;
		int input5;
		int[] subkeys = new int[subkeyCnt];
		for (input = input3 = 0; input < subkeyCnt / 2; input++, input3 += SK_STEP) {
			input4 = f32(k64Cnt, input3, k32e);
			input5 = f32(k64Cnt, input3 + SK_BUMP, k32o);
			input5 = input5 << 8 | input5 >>> 24;
			input4 += input5;
			subkeys[2 * input] = input4;
			input4 += input5;
			subkeys[2 * input + 1] = input4 << SK_ROTL | input4 >>> (32 - SK_ROTL);
		}
		int k0 = sboxkey[0];
		int k1 = sboxkey[1];
		int k2 = sboxkey[2];
		int k3 = sboxkey[3];
		int b0;
		int b1;
		int b2;
		int b3;
		int[] sbox = new int[4 * 256];
		for (input = 0; input < 256; input++) {
			b0 = b1 = b2 = b3 = input;
			switch (k64Cnt & 3) {
				case 1:
					sbox[2 * input] = MDS[0][(P[P_01][b0] & 0xFF) ^ b0(k0)];
					sbox[2 * input + 1] = MDS[1][(P[P_11][b1] & 0xFF) ^ b1(k0)];
					sbox[0x200 + 2 * input] = MDS[2][(P[P_21][b2] & 0xFF) ^ b2(k0)];
					sbox[0x200 + 2 * input + 1] = MDS[3][(P[P_31][b3] & 0xFF) ^ b3(k0)];
					break;
				case 0:
					b0 = (P[P_04][b0] & 0xFF) ^ b0(k3);
					b1 = (P[P_14][b1] & 0xFF) ^ b1(k3);
					b2 = (P[P_24][b2] & 0xFF) ^ b2(k3);
					b3 = (P[P_34][b3] & 0xFF) ^ b3(k3);
					break;
				case 3:
					b0 = (P[P_03][b0] & 0xFF) ^ b0(k2);
					b1 = (P[P_13][b1] & 0xFF) ^ b1(k2);
					b2 = (P[P_23][b2] & 0xFF) ^ b2(k2);
					b3 = (P[P_33][b3] & 0xFF) ^ b3(k2);
					break;
				case 2:
					sbox[2 * input] = MDS[0][(P[P_01][(P[P_02][b0] & 0xFF) ^ b0(k1)] & 0xFF) ^ b0(k0)];
					sbox[2 * input + 1] = MDS[1][(P[P_11][(P[P_12][b1] & 0xFF) ^ b1(k1)] & 0xFF) ^ b1(k0)];
					sbox[0x200 + 2 * input] = MDS[2][(P[P_21][(P[P_22][b2] & 0xFF) ^ b2(k1)] & 0xFF) ^ b2(k0)];
					sbox[0x200 + 2 * input + 1] = MDS[3][(P[P_31][(P[P_32][b3] & 0xFF) ^ b3(k1)] & 0xFF) ^ b3(k0)];
					break;
				default:
					break;
			}
		}
		return new Object[]{sbox, subkeys};
	}

	/**
	 * Encrypt exactly one block of plaintext.
	 *
	 * @param in         The plaintext.
	 * @param inOffset   Index of in from which to start considering data.
	 * @param sessionKey The session key to use for encryption.
	 * @return The ciphertext generated from a plaintext using the session key.
	 */
	public static byte[] blockEncrypt(byte[] in, int inOffset, Object sessionKey) {
		int x0 = 0;
		x0 = (in[inOffset++] & 0xFF)
				| (in[inOffset++] & 0xFF) << 8
				| (in[inOffset++] & 0xFF) << 16
				| (in[inOffset++] & 0xFF) << 24;
		int x1 = 0;
		x1 = (in[inOffset++] & 0xFF)
				| (in[inOffset++] & 0xFF) << 8
				| (in[inOffset++] & 0xFF) << 16
				| (in[inOffset++] & 0xFF) << 24;
		int x2 = 0;
		x2 = (in[inOffset++] & 0xFF)
				| (in[inOffset++] & 0xFF) << 8
				| (in[inOffset++] & 0xFF) << 16
				| (in[inOffset++] & 0xFF) << 24;
		int x3 = 0;
		x3 = (in[inOffset++] & 0xFF)
				| (in[inOffset++] & 0xFF) << 8
				| (in[inOffset++] & 0xFF) << 16
				| (in[inOffset++] & 0xFF) << 24;

		Object[] sk = (Object[]) sessionKey;
		int[] skey = (int[]) sk[1];
		x0 ^= skey[INPUT_WHITEN];
		x1 ^= skey[INPUT_WHITEN + 1];
		x2 ^= skey[INPUT_WHITEN + 2];
		x3 ^= skey[INPUT_WHITEN + 3];
		int t0;
		int t1;
		int[] sbox = (int[]) sk[0];
		int roundSubkeys = ROUND_SUBKEYS;
		for (int rounds = 0; rounds < ROUNDS; rounds += 2) {
			t0 = fe32(sbox, x0, 0);
			t1 = fe32(sbox, x1, 3);
			x2 ^= t0 + t1 + skey[roundSubkeys++];
			x2 = x2 >>> 1 | x2 << 31;
			x3 = x3 << 1 | x3 >>> 31;
			x3 ^= t0 + 2 * t1 + skey[roundSubkeys++];

			t0 = fe32(sbox, x2, 0);
			t1 = fe32(sbox, x3, 3);
			x0 ^= t0 + t1 + skey[roundSubkeys++];
			x0 = x0 >>> 1 | x0 << 31;
			x1 = x1 << 1 | x1 >>> 31;
			x1 ^= t0 + 2 * t1 + skey[roundSubkeys++];
		}
		x2 ^= skey[OUTPUT_WHITEN];
		x3 ^= skey[OUTPUT_WHITEN + 1];
		x0 ^= skey[OUTPUT_WHITEN + 2];
		x1 ^= skey[OUTPUT_WHITEN + 3];

		return new byte[]{
				(byte) x2, (byte) (x2 >>> 8), (byte) (x2 >>> 16), (byte) (x2 >>> 24),
				(byte) x3, (byte) (x3 >>> 8), (byte) (x3 >>> 16), (byte) (x3 >>> 24),
				(byte) x0, (byte) (x0 >>> 8), (byte) (x0 >>> 16), (byte) (x0 >>> 24),
				(byte) x1, (byte) (x1 >>> 8), (byte) (x1 >>> 16), (byte) (x1 >>> 24),
		};
	}

	private static final int b0(int input) {
		return input & 0xFF;
	}

	private static final int b1(int input) {
		return (input >>> 8) & 0xFF;
	}

	private static final int b2(int input) {
		return (input >>> 16) & 0xFF;
	}

	private static final int b3(int input) {
		return (input >>> 24) & 0xFF;
	}

	/**
	 * Use (12, 8) Reed-Solomon code over GF(256) to produce a key S-box
	 * 32-bit entity from two key material 32-bit entities.
	 *
	 * @param k0 1st 32-bit entity.
	 * @param k1 2nd 32-bit entity.
	 * @return Remainder polynomial generated using RS code
	 */
	private static final int rsMdsEncode(int k0, int k1) {
		int k11 = k1;
		for (int i = 0; i < 4; i++) {
			k11 = rsRem(k11);
		}
		k11 ^= k0;
		for (int i = 0; i < 4; i++) {
			k11 = rsRem(k11);
		}
		return k11;
	}

	private static final int rsRem(int input) {
		int in = (input >>> 24) & 0xFF;
		int g2 = ((in << 1) ^ ((in & 0x80) != 0 ? RS_GF_FDBK : 0)) & 0xFF;
		int g3 = (in >>> 1) ^ ((in & 0x01) != 0 ? (RS_GF_FDBK >>> 1) : 0) ^ g2;
		int result = (input << 8) ^ (g3 << 24) ^ (g2 << 16) ^ (g3 << 8) ^ in;
		return result;
	}

	private static final int f32(int k64Cnt, int input, int[] k32) {
		int b0 = b0(input);
		int b1 = b1(input);
		int b2 = b2(input);
		int b3 = b3(input);
		int k0 = k32[0];
		int k1 = k32[1];
		int k2 = k32[2];
		int k3 = k32[3];

		int result = 0;
		switch (k64Cnt & 3) {
			case 1:
				result =
						MDS[0][(P[P_01][b0] & 0xFF)
								^ b0(k0)]
								^ MDS[1][(P[P_11][b1] & 0xFF)
								^ b1(k0)]
								^ MDS[2][(P[P_21][b2] & 0xFF)
								^ b2(k0)]
								^ MDS[3][(P[P_31][b3] & 0xFF)
								^ b3(k0)];
				break;
			case 0:
				b0 = (P[P_04][b0] & 0xFF) ^ b0(k3);
				b1 = (P[P_14][b1] & 0xFF) ^ b1(k3);
				b2 = (P[P_24][b2] & 0xFF) ^ b2(k3);
				b3 = (P[P_34][b3] & 0xFF) ^ b3(k3);
				break;
			case 3:
				b0 = (P[P_03][b0] & 0xFF) ^ b0(k2);
				b1 = (P[P_13][b1] & 0xFF) ^ b1(k2);
				b2 = (P[P_23][b2] & 0xFF) ^ b2(k2);
				b3 = (P[P_33][b3] & 0xFF) ^ b3(k2);
				break;
			case 2:
				result =
						MDS[0][(P[P_01][(P[P_02][b0] & 0xFF)
								^ b0(k1)] & 0xFF)
								^ b0(k0)]
								^ MDS[1][(P[P_11][(P[P_12][b1] & 0xFF)
								^ b1(k1)] & 0xFF) ^ b1(k0)]
								^ MDS[2][(P[P_21][(P[P_22][b2] & 0xFF)
								^ b2(k1)] & 0xFF)
								^ b2(k0)]
								^ MDS[3][(P[P_31][(P[P_32][b3] & 0xFF)
								^ b3(k1)] & 0xFF)
								^ b3(k0)];
				break;
			default:
				result = 0;
				break;
		}
		return result;
	}

	private static final int fe32(int[] sbox, int in1, int in2) {
		return sbox[2 * base(in1, in2)]
				^ sbox[2 * base(in1, in2 + 1) + 1]
				^ sbox[0x200 + 2 * base(in1, in2 + 2)]
				^ sbox[0x200 + 2 * base(in1, in2 + 3) + 1];
	}

	private static final int base(int in1, int in2) {
		int result = 0;
		switch (in2 % 4) {
			case 0:
				result = b0(in1);
				break;
			case 1:
				result = b1(in1);
				break;
			case 2:
				result = b2(in1);
				break;
			case 3:
				result = b3(in1);
				break;
			default:
				result = 0;
				break;
		}
		return result;
	}
}
