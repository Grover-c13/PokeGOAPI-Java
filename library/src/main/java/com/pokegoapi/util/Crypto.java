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

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Crypto {
	private static class Rand {
		public long state;
	}

	private static byte[] makeIv(Rand rand) {
		byte[] iv = new byte[256];
		for (int i = 0; i < 256; i++) {
			rand.state = (0x41C64E6D * rand.state) + 0x3039;
			long shiftedRand = rand.state >> 16;
			iv[i] = Long.valueOf(shiftedRand).byteValue();
		}
		return iv;
	}

	private static byte makeIntegrityByte(Rand rand) {
		rand.state = (0x41C64E6D * rand.state) + 0x3039;
		long shiftedRand = rand.state >> 16;
		byte lastbyte = Long.valueOf(shiftedRand).byteValue();

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
	public static CipherText encrypt(byte[] input, long msSinceStart) {
		Rand rand = new Rand();

		byte[] arr3;
		CipherText output;

		rand.state = msSinceStart;

		byte[] iv = makeIv(rand);
		output = new CipherText(input, msSinceStart, rand);

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
			arr3 = shuffle2(temp2);

			for (int k = 0; k < 256; ++k)
				iv[k] = arr3[k];

			for (int k = 0; k < 256; ++k)
				current[k] = arr3[k];
		}

		return output;
	}

	private static byte[] shuffle2(int[] vector) {
		int[] tmp = new int[193];
		tmp[0] = vector[7] ^ vector[15];
		tmp[1] = ~vector[7];
		tmp[2] = ~vector[1];
		tmp[3] = vector[17] & tmp[2];
		tmp[4] = ~vector[15];
		tmp[5] = vector[7] & tmp[4];
		tmp[6] = vector[15] & tmp[1];
		tmp[7] = vector[15] & ~tmp[6];
		tmp[8] = vector[7] & vector[39];
		tmp[9] = tmp[4] & tmp[8];
		tmp[10] = vector[15] ^ tmp[9];
		tmp[11] = vector[1] & vector[39];
		tmp[12] = vector[39] & tmp[2];
		tmp[13] = ~tmp[5];
		tmp[14] = vector[39] & tmp[1];
		tmp[15] = vector[39] & tmp[13];
		tmp[16] = vector[39] & ~tmp[7];
		tmp[17] = vector[7] ^ tmp[14];
		tmp[18] = vector[37] & tmp[1];
		tmp[19] = vector[7] & vector[37];
		tmp[20] = ~vector[37];
		tmp[21] = ~vector[5];
		tmp[22] = vector[37] & tmp[21];
		tmp[23] = vector[5] | vector[37];
		tmp[24] = tmp[21] & tmp[23];
		tmp[21] &= vector[27];
		tmp[25] = ~tmp[21];
		tmp[26] = vector[5] & vector[37];
		tmp[27] = ~tmp[26];
		tmp[28] = vector[27] & tmp[25];
		tmp[29] = vector[29] & tmp[20];
		tmp[30] = ~vector[13];
		tmp[31] = vector[5] ^ vector[29] & ~tmp[23];
		tmp[32] = vector[5] & vector[29];
		tmp[33] = vector[5] | vector[27];
		tmp[34] = ~tmp[33];
		tmp[35] = vector[5] ^ vector[27];
		tmp[36] = ~tmp[35];
		tmp[37] = vector[35] & tmp[34];
		tmp[38] = vector[5] & ~vector[27];
		tmp[39] = vector[5] & vector[27];
		tmp[40] = ~vector[3];
		tmp[41] = ~vector[25];
		tmp[42] = vector[25] & tmp[40];
		tmp[43] = ~tmp[42];
		tmp[44] = vector[3] & tmp[41];
		tmp[45] = vector[25] | tmp[44];
		tmp[46] = vector[25] & tmp[43];
		tmp[47] = vector[3] ^ vector[25];
		tmp[48] = vector[25] ^ tmp[3];
		tmp[49] = vector[25] & tmp[2];
		tmp[50] = ~vector[33];
		tmp[51] = vector[1] | vector[25];
		tmp[52] = vector[17] | vector[25];
		tmp[53] = tmp[49] & ~vector[17];
		tmp[54] = vector[1] | tmp[52];
		tmp[55] = tmp[49] ^ (vector[17] ^ tmp[50] & tmp[54]);
		tmp[56] = tmp[51] ^ tmp[52];
		tmp[57] = vector[1] ^ tmp[52];
		tmp[52] &= tmp[41];
		tmp[58] = vector[17] & vector[25];
		tmp[59] = vector[25] ^ tmp[51];
		tmp[60] = tmp[59] ^ tmp[49] & tmp[50];
		tmp[61] = vector[25] ^ tmp[49];
		tmp[62] = vector[3] & vector[25];
		tmp[63] = ~vector[31];
		tmp[64] = vector[23] & tmp[8];
		tmp[65] = vector[23] & ~tmp[8];
		tmp[66] = vector[3] | vector[25];
		tmp[27] = vector[37] ^ (vector[21] & ~(tmp[24] ^ tmp[30] & (vector[29] ^ tmp[23]) ^ vector[29] & tmp[27]) ^ vector[29] & ~(vector[5] & tmp[27])) ^ tmp[30] & (vector[37] ^ tmp[32]);
		tmp[23] = vector[37] ^ (vector[29] ^ (vector[21] & ~(tmp[24] ^ tmp[30] & (tmp[22] ^ vector[29] & vector[37]) ^ vector[29] & tmp[23]) ^ tmp[30] & tmp[31]));
		tmp[20] = vector[37] ^ (vector[5] ^ (tmp[29] ^ (vector[21] & ~(vector[29] & tmp[22] ^ tmp[30] & (tmp[29] ^ vector[5] & tmp[20])) ^ (vector[13] | tmp[26] ^ vector[29] & ~tmp[24]))));
		tmp[26] = vector[29] ^ (vector[5] ^ (tmp[30] & (tmp[22] ^ tmp[32]) ^ vector[21] & ~(tmp[31] ^ (vector[13] | vector[29] ^ tmp[26]))));
		tmp[31] = ~vector[19];
		tmp[32] = vector[23] & ~vector[39];
		tmp[32] = vector[28] ^ (vector[23] ^ (tmp[7] ^ (tmp[15] ^ (vector[61] & (tmp[64] ^ (vector[31] | tmp[16] ^ tmp[32]) ^ vector[15] & tmp[8]) ^ (vector[31] | tmp[9] ^ tmp[32] ^ (vector[7]
				| vector[15]))))));
		tmp[64] = vector[15] ^ (vector[7] ^ vector[39]) ^ (vector[32] ^ (vector[61] & (tmp[5] ^ (vector[31] | tmp[10] ^ tmp[64]) ^ vector[23] & tmp[13]) ^ tmp[63] & (tmp[10] ^ tmp[65])));
		tmp[10] = vector[59] & ~tmp[38];
		tmp[13] = vector[59] & tmp[39];
		tmp[9] = tmp[35] ^ tmp[13];
		tmp[7] = vector[34] ^ (tmp[23] ^ (vector[59] | tmp[26]));
		tmp[22] = vector[62] ^ (tmp[20] ^ tmp[27] & ~vector[59]);
		tmp[30] = tmp[64] & tmp[22];
		tmp[29] = tmp[64] ^ tmp[22];
		tmp[24] = ~tmp[64];
		tmp[67] = tmp[64] | tmp[22];
		tmp[68] = tmp[24] & tmp[67];
		tmp[69] = vector[59] & (vector[27] | tmp[38]);
		tmp[70] = vector[5] ^ vector[5] & vector[59];
		tmp[34] &= vector[59];
		tmp[71] = vector[27] ^ tmp[34];
		tmp[72] = vector[59] & ~tmp[28];
		tmp[73] = tmp[38] ^ tmp[72];
		tmp[74] = vector[11] & vector[57];
		tmp[75] = tmp[21] ^ tmp[34];
		tmp[76] = ~vector[57];
		tmp[77] = vector[19] | vector[57];
		tmp[78] = tmp[76] & tmp[77];
		tmp[79] = vector[11] & ~tmp[77];
		tmp[80] = ~vector[35];
		tmp[81] = vector[19] & tmp[76];
		tmp[82] = vector[19] & vector[57];
		tmp[83] = ~tmp[82];
		tmp[84] = vector[57] & tmp[83];
		tmp[85] = tmp[77] ^ vector[11] & ~tmp[84];
		tmp[86] = vector[19] ^ vector[57];
		tmp[83] = tmp[86] ^ vector[11] & tmp[83];
		tmp[87] = vector[11] & tmp[76];
		tmp[88] = vector[57] ^ (vector[11] ^ vector[19]);
		tmp[89] = vector[27] & ~(vector[57] ^ tmp[87]);
		tmp[90] = vector[11] & tmp[86];
		tmp[90] =
				vector[57] ^ (vector[19] ^ (vector[24] ^ ((vector[3] | tmp[89] ^ (tmp[90] ^ (vector[57] ^ (vector[35] | vector[57] ^ tmp[90] ^ vector[27] & ~(vector[11] ^ tmp[84]))))) ^ tmp[80] & (tmp[79] ^ (
						tmp[77] ^ vector[27] & ~(vector[19] ^ tmp[79])))))) ^ vector[11] & vector[27];
		tmp[81] =
				vector[38] ^ tmp[83] ^ (tmp[40] & (vector[19] ^ (vector[35] | tmp[86] ^ vector[27] & (tmp[81] ^ tmp[87])) ^ vector[11] & tmp[82]) ^ (vector[35] | tmp[79] ^ (tmp[82] ^ vector[27] & ~(vector[57]
						^ vector[11] & tmp[81]))) ^ (vector[27] | tmp[79] ^ tmp[84]));
		tmp[4] = vector[54] ^ tmp[0] ^ (tmp[15] ^ (vector[61] & ~(tmp[8] ^ (vector[31] | tmp[65] ^ (tmp[6] ^ vector[39] & (vector[15] | tmp[5]))) ^ vector[23] & ~(vector[15] ^ vector[39] & tmp[4]))
				^ vector[23] & (vector[39] | ~tmp[6]) ^ (vector[31] | vector[39] & tmp[0] & ~vector[23])));
		tmp[6] = tmp[81] | tmp[4];
		tmp[65] = tmp[81] & ~tmp[4];
		tmp[8] = tmp[81] & tmp[4];
		tmp[15] = tmp[81] & ~tmp[8];
		tmp[86] = ~tmp[81];
		tmp[82] = tmp[4] & tmp[86];
		tmp[79] = tmp[81] ^ tmp[4];
		tmp[1] &= vector[53];
		tmp[91] = vector[7] ^ vector[37] & tmp[1];
		tmp[92] = vector[7] | tmp[1];
		tmp[1] = vector[37] & ~tmp[1];
		tmp[93] = vector[37] & vector[53];
		tmp[94] = ~vector[53];
		tmp[95] = vector[7] & tmp[94];
		tmp[94] &= tmp[19];
		tmp[96] = ~tmp[95];
		tmp[97] = vector[7] & tmp[96];
		tmp[98] = vector[7] & vector[53];
		tmp[99] = vector[7] | vector[53];
		tmp[100] = vector[37] & ~tmp[99];
		tmp[101] = tmp[99] ^ tmp[100];
		tmp[102] = vector[7] ^ vector[53];
		tmp[27] = tmp[20] ^ (vector[52] ^ vector[59] & ~tmp[27]);
		tmp[20] = tmp[32] ^ tmp[27];
		tmp[103] = tmp[32] & tmp[27];
		tmp[104] = ~tmp[27];
		tmp[105] = tmp[32] & tmp[104];
		tmp[106] = tmp[27] ^ tmp[105];
		tmp[0] = vector[39] ^ (vector[15] ^ (vector[50] ^ (tmp[63] & (tmp[5] ^ (tmp[14] ^ vector[7] & vector[23])) ^ vector[23] & ~tmp[17]))) ^ vector[61] & ~(tmp[5] ^ (tmp[16] ^ (vector[31]
				| tmp[5] ^ vector[23] & tmp[17] ^ vector[39] & ~tmp[0])) ^ vector[23] & ~tmp[14]);
		tmp[26] = vector[48] ^ tmp[23] ^ vector[59] & tmp[26];
		tmp[23] = vector[1] ^ vector[47];
		tmp[17] = tmp[23] ^ vector[39] & tmp[23];
		tmp[5] = tmp[11] ^ tmp[23];
		tmp[2] &= vector[47];
		tmp[14] = vector[39] & ~tmp[2];
		tmp[16] = vector[1] | tmp[2];
		tmp[63] = vector[39] & tmp[16];
		tmp[107] = vector[1] & ~vector[47];
		tmp[108] = ~tmp[107];
		tmp[109] = vector[1] & tmp[108];
		tmp[110] = ~vector[55];
		tmp[111] = vector[47] & tmp[11];
		tmp[112] = tmp[111] ^ (vector[1] | vector[47]);
		tmp[113] = ~vector[29];
		tmp[100] =
				vector[37] ^ (vector[16] ^ (tmp[102] ^ ((vector[29] | tmp[100] ^ vector[45] & ~tmp[1]) ^ vector[45] & ~(tmp[94] ^ tmp[97])))) ^ vector[61] & ~(vector[7] ^ tmp[113] & (tmp[18] ^ (tmp[102]
						^ vector[45] & (tmp[19] ^ tmp[92]))) ^ vector[45] & ~tmp[18]);
		tmp[24] &= tmp[100] & tmp[22];
		tmp[114] = tmp[30] ^ tmp[24];
		tmp[115] = tmp[100] & ~tmp[67];
		tmp[116] = tmp[100] & ~tmp[22];
		tmp[117] = tmp[22] & tmp[100];
		tmp[118] = tmp[117] ^ tmp[64] & ~tmp[30];
		tmp[119] = tmp[29] ^ tmp[115];
		tmp[120] = tmp[67] ^ tmp[117];
		tmp[121] = vector[45] & tmp[98];
		tmp[121] = tmp[98] ^ (vector[22] ^ (vector[61] & ~(tmp[121] ^ tmp[113] & (tmp[95] ^ (tmp[19] ^ tmp[121])) ^ vector[37] & tmp[98]) ^ (vector[29] | tmp[19] ^ tmp[97] ^ vector[45] & tmp[95])
				^ vector[45] & ~(tmp[18] ^ tmp[95]) ^ vector[37] & tmp[102]));
		tmp[18] = vector[53] ^ (vector[2] ^ (tmp[1] ^ (vector[61] & ~(tmp[95] ^ tmp[113] & (tmp[93] ^ vector[45] & tmp[18]) ^ vector[45] & ~tmp[92]) ^ tmp[113] & (vector[45] | tmp[91])
				^ vector[45] & tmp[91])));
		tmp[96] =
				tmp[93] ^ (vector[4] ^ tmp[102]) ^ (vector[61] & (vector[45] & tmp[92] ^ (vector[29] | vector[45] ^ tmp[101])) ^ tmp[113] & (tmp[101] ^ vector[45] & (vector[37] & tmp[96])) ^ vector[45] & ~(
						tmp[94] ^ tmp[99]));
		tmp[101] = tmp[0] & tmp[96];
		tmp[78] = tmp[88] ^ (vector[44] ^ ((vector[35] | tmp[78] ^ vector[19] & vector[27]) ^ tmp[40] & (tmp[74] ^ (vector[35] | vector[19] ^ (vector[11] ^ vector[27] & ~(tmp[74] ^ tmp[78])))
				^ vector[27] & ~tmp[88]))) ^ vector[27] & tmp[31];
		tmp[88] = ~tmp[78];
		tmp[92] = tmp[32] & tmp[88];
		tmp[99] = tmp[32] & tmp[78];
		tmp[88] &= tmp[27];
		tmp[94] = tmp[27] | tmp[78];
		tmp[113] = tmp[32] & ~tmp[94];
		tmp[102] = tmp[27] & tmp[99];
		tmp[93] = tmp[78] ^ tmp[92];
		tmp[95] = tmp[32] & tmp[88];
		tmp[88] |= tmp[78];
		tmp[104] = ~(tmp[104] & tmp[78]);
		tmp[91] = tmp[78] & tmp[104];
		tmp[1] = tmp[27] ^ tmp[78];
		tmp[105] ^= tmp[1];
		tmp[19] = ~vector[43];
		tmp[28] = tmp[9] ^ (vector[20] ^ ((vector[35] | tmp[28]) ^ ((vector[43] | tmp[73] ^ vector[35] & tmp[38]) ^ vector[51] & ~(tmp[69] ^ tmp[19] & (vector[5] ^ tmp[69] ^ vector[35] & tmp[35])
				^ vector[27] & vector[35]))));
		tmp[98] = tmp[28] & ~tmp[32];
		tmp[97] = tmp[32] | tmp[28];
		tmp[122] = tmp[32] & tmp[28];
		tmp[123] = tmp[32] ^ tmp[28];
		tmp[124] = tmp[32] & ~tmp[122];
		tmp[38] =
				vector[59] ^ (vector[46] ^ tmp[21]) ^ (vector[35] & tmp[71] ^ (vector[51] & ~(tmp[38] ^ (vector[59] ^ (vector[35] & ~(vector[27] ^ tmp[10]) ^ (vector[43] | tmp[37] ^ tmp[70])))) ^ (vector[43]
						| vector[59] ^ vector[35] & ~(vector[5] ^ vector[59] & tmp[35]))));
		tmp[25] =
				tmp[35] ^ (vector[56] ^ (tmp[69] ^ (tmp[19] & (tmp[71] ^ vector[35] & ~(vector[5] ^ tmp[72])) ^ vector[51] & ~(tmp[75] ^ vector[35] & ~(vector[5] ^ vector[59] & tmp[25]) ^ tmp[19] & (tmp[37]
						^ tmp[34])) ^ vector[35] & ~(tmp[21] ^ vector[59] & tmp[36]))));
		tmp[34] = ~tmp[7];
		tmp[37] = tmp[25] & tmp[34];
		tmp[72] = ~tmp[90];
		tmp[21] = ~tmp[26];
		tmp[71] = tmp[25] & tmp[72];
		tmp[69] = tmp[7] | tmp[25];
		tmp[36] = vector[6] ^ (tmp[73] ^ (vector[51] & (tmp[9] ^ tmp[19] & (tmp[70] ^ vector[35] & tmp[36]) ^ vector[35] & (tmp[39] ^ vector[27] & vector[59])) ^ (vector[43] | tmp[75] ^ (vector[35]
				| tmp[10])))) ^ vector[35] & ~(tmp[33] ^ tmp[13]);
		tmp[70] = ~vector[49];
		tmp[89] = vector[11] ^ vector[42] ^ ((vector[3] | tmp[84] ^ (tmp[89] ^ tmp[80] & (tmp[85] ^ tmp[89])) ^ tmp[31] & tmp[74]) ^ (vector[35] | tmp[83] ^ vector[27] & ~(tmp[77] ^ tmp[87]))
				^ vector[27] & tmp[85]);
		tmp[85] = vector[41] & tmp[44];
		tmp[87] = tmp[62] ^ vector[41] & ~tmp[44];
		tmp[77] = vector[41] & tmp[45];
		tmp[41] = tmp[87] ^ (vector[14] ^ ((vector[33] | vector[57] & ~(tmp[66] ^ vector[41] & tmp[41]) ^ tmp[45] & (vector[41] & tmp[70])) ^ (vector[49] | tmp[77] ^ vector[57] & tmp[45]))) ^ (vector[57]
				| tmp[66] ^ tmp[85]);
		tmp[66] = vector[41] & tmp[40];
		tmp[80] = vector[41] & tmp[62];
		tmp[74] = vector[41] & ~tmp[47];
		tmp[31] = vector[41] & tmp[47];
		tmp[42] = vector[18] ^ (tmp[87] ^ (vector[49] | tmp[47] ^ tmp[85] ^ vector[57] & (tmp[42] ^ tmp[85])) ^ vector[57] & ~(tmp[42] ^ vector[41] & ~tmp[46]) ^ tmp[50] & (tmp[77] ^ (vector[25]
				^ vector[57] & (tmp[42] ^ tmp[74])) ^ tmp[70] & (tmp[85] ^ tmp[42] & tmp[76])));
		tmp[80] =
				vector[0] ^ (vector[25] ^ (tmp[66] ^ (tmp[70] & (tmp[45] ^ vector[57] & (tmp[62] ^ tmp[80])) ^ vector[57] & ~(tmp[44] ^ tmp[85])))) ^ (vector[33] | tmp[45] ^ tmp[74] ^ vector[57] & ~(vector[3]
						^ tmp[66]) ^ (vector[49] | vector[3] ^ tmp[80] ^ vector[57] & tmp[40]));
		tmp[66] = tmp[45] ^ vector[3] & vector[41] ^ vector[57] & ~(tmp[46] ^ vector[41] & tmp[43]) ^ (vector[36] ^ (vector[49] | vector[3] ^ vector[57] & (tmp[45] ^ tmp[66])) ^ (vector[33]
				| vector[57] & tmp[85] ^ (tmp[31] ^ tmp[70] & tmp[31])));
		tmp[45] =
				vector[39] ^ vector[58] ^ (tmp[23] ^ (vector[31] & ~(tmp[109] ^ vector[63] & ~(tmp[112] ^ vector[55] & ~(tmp[11] ^ tmp[109])) ^ vector[39] & ~tmp[109] ^ vector[55] & (vector[1] ^ tmp[14]))
						^ vector[63] & (tmp[16] ^ (vector[55] ^ tmp[63])) ^ vector[55] & tmp[63]));
		tmp[31] =
				tmp[48] ^ vector[33] & tmp[57] ^ (vector[33] | tmp[58] ^ (vector[1] | vector[17])) & ~vector[63] ^ (vector[40] ^ ~vector[9] & ((vector[63] | (vector[33] | tmp[52])) ^ vector[33] & tmp[49]));
		tmp[70] = ~tmp[31];
		tmp[43] = tmp[31] ^ tmp[72] & (tmp[25] & tmp[70]);
		tmp[85] = tmp[25] | tmp[31];
		tmp[46] = tmp[70] & tmp[85];
		tmp[62] = tmp[90] | tmp[46];
		tmp[44] = tmp[90] | tmp[85];
		tmp[40] = tmp[25] ^ tmp[31];
		tmp[74] = tmp[90] | tmp[40];
		tmp[76] = tmp[90] | tmp[31];
		tmp[47] = tmp[26] | tmp[76];
		tmp[77] = ~tmp[18];
		tmp[70] = vector[15] ^ (tmp[31] ^ (tmp[62] ^ tmp[64] & (tmp[43] ^ tmp[26] & tmp[70])) ^ tmp[21] & tmp[40]) ^ tmp[77] & (tmp[44] ^ tmp[26] & ~tmp[74] ^ tmp[64] & ~(tmp[71] ^ tmp[47]));
		tmp[87] = tmp[25] & tmp[31];
		tmp[84] = tmp[90] | tmp[87];
		tmp[83] = tmp[31] & ~tmp[87];
		tmp[39] = tmp[90] ^ tmp[83];
		tmp[76] =
				vector[1] ^ (tmp[25] ^ (tmp[84] ^ (tmp[64] & (tmp[21] | ~(tmp[31] ^ tmp[76])) ^ (tmp[26] | tmp[87] ^ tmp[72] & tmp[87])))) ^ tmp[77] & (tmp[39] ^ tmp[26] & ~(tmp[87] ^ tmp[84]) ^ tmp[64] & ~(
						tmp[39] ^ (tmp[26] | tmp[40] ^ tmp[76])));
		tmp[40] ^=
				vector[19] ^ ((tmp[18] | tmp[87] ^ (tmp[21] & (tmp[25] ^ (tmp[90] | tmp[83])) ^ tmp[64] & (tmp[87] ^ (tmp[74] ^ (tmp[26] | tmp[25] ^ tmp[72] & tmp[40])))) ^ tmp[72] & (tmp[31] & ~tmp[25]))
						^ tmp[64] & (tmp[85] ^ tmp[62] ^ tmp[47]) ^ tmp[26] & ~(tmp[72] & tmp[85]));
		tmp[43] = tmp[71] ^ (vector[37] ^ tmp[85]) ^ tmp[25] & tmp[21] ^ (tmp[64] & ~(tmp[44] ^ tmp[47]) ^ (tmp[18] | tmp[83] ^ (tmp[84] ^ ((tmp[26] | tmp[71] ^ tmp[46]) ^ tmp[64] & (tmp[43] ^ (tmp[26]
				| tmp[31]))))));
		tmp[57] = tmp[55] ^ (vector[26] ^ ((vector[9] | tmp[60] ^ vector[63] & ~(tmp[3] ^ tmp[50] & tmp[57])) ^ vector[63] & (tmp[56] ^ (vector[33] | tmp[54]))));
		tmp[54] = tmp[57] & ~tmp[96];
		tmp[46] = tmp[34] & tmp[54];
		tmp[71] = tmp[96] & tmp[57];
		tmp[34] &= tmp[71];
		tmp[84] = ~tmp[45];
		tmp[47] = tmp[7] | tmp[71];
		tmp[44] = tmp[96] & ~tmp[71];
		tmp[83] = tmp[0] & ~tmp[54] ^ tmp[44];
		tmp[85] = tmp[7] | tmp[44];
		tmp[21] = ~tmp[89];
		tmp[72] = tmp[7] | tmp[57];
		tmp[74] = ~tmp[57];
		tmp[87] = ~tmp[7];
		tmp[62] = tmp[96] & tmp[74];
		tmp[39] = tmp[62] ^ tmp[71] & tmp[87];
		tmp[77] = tmp[96] ^ tmp[57];
		tmp[19] = tmp[87] & tmp[77];
		tmp[10] = tmp[7] | tmp[77];
		tmp[9] = ~tmp[45];
		tmp[75] = tmp[96] | tmp[57];
		tmp[83] = tmp[85] ^ tmp[84] & tmp[83] ^ (vector[21] ^ tmp[77]) ^ tmp[0] & ~(tmp[54] ^ tmp[72]) ^ (tmp[89] | tmp[96] ^ tmp[34] ^ (tmp[45] | tmp[83]) ^ tmp[0] & (tmp[10] ^ tmp[75]));
		tmp[77] = ~tmp[96];
		tmp[13] = tmp[75] & tmp[77];
		tmp[33] = tmp[7] | tmp[13];
		tmp[62] = vector[7] ^ (tmp[89] | tmp[84] & (tmp[47] ^ (tmp[101] ^ tmp[57])) ^ (tmp[39] ^ tmp[0] & tmp[19])) ^ (tmp[96] ^ tmp[19] ^ tmp[0] & (tmp[13] ^ (tmp[7] | tmp[75])) ^ (tmp[45]
				| tmp[87] & tmp[62] ^ (tmp[44] ^ tmp[0] & ~(tmp[72] ^ tmp[62]))));
		tmp[10] = tmp[47] ^ tmp[75] ^ (vector[47] ^ ((tmp[45] | tmp[46] ^ tmp[0] & tmp[47]) ^ (tmp[21] & (tmp[0] & ~tmp[46] ^ tmp[57] & tmp[87] ^ tmp[9] & (tmp[101] ^ tmp[10])) ^ tmp[0] & (tmp[71]
				^ tmp[33]))));
		tmp[101] = ~tmp[40];
		tmp[75] = vector[57] ^ tmp[21] & (tmp[34] ^ (tmp[71] ^ tmp[0] & (tmp[54] ^ tmp[46])) ^ (tmp[45] | tmp[85] ^ (tmp[54] ^ tmp[0] & tmp[54]))) ^ (tmp[39] ^ (
				(tmp[45] | tmp[13] ^ (tmp[19] ^ tmp[0] & (tmp[54] ^ tmp[87] & tmp[75]))) ^ tmp[0] & ~(tmp[96] ^ tmp[33])));
		tmp[52] = vector[30] ^ (tmp[55] ^ (vector[63] | tmp[56] ^ (vector[33] | tmp[53] ^ tmp[58]))) ^ (tmp[60] ^ (vector[63] | tmp[61] ^ vector[33] & (tmp[51] ^ tmp[52]))) & ~vector[9];
		tmp[56] = tmp[82] & tmp[52];
		tmp[60] = tmp[52] & ~tmp[4];
		tmp[55] = tmp[8] & tmp[52];
		tmp[54] = ~tmp[38];
		tmp[46] = tmp[52] & ~tmp[8];
		tmp[33] = tmp[15] ^ tmp[65] & tmp[52];
		tmp[19] = tmp[4] & tmp[52];
		tmp[85] = tmp[65] ^ tmp[55];
		tmp[71] = tmp[81] ^ tmp[52] & ~tmp[15];
		tmp[13] = tmp[79] & tmp[52];
		tmp[79] = tmp[52] & ~tmp[79];
		tmp[34] = ~tmp[80];
		tmp[21] = ~tmp[81];
		tmp[39] = tmp[6] & tmp[52];
		tmp[47] = tmp[81] ^ tmp[52];
		tmp[44] = tmp[38] | tmp[47];
		tmp[46] ^= tmp[81] ^ vector[59] ^ (tmp[22] & (tmp[19] ^ (tmp[4] ^ tmp[44])) ^ (tmp[38] | tmp[33])) ^ (tmp[80] | tmp[60] ^ (tmp[15] ^ tmp[54] & (tmp[65] ^ tmp[46])) ^ tmp[22] & (tmp[71] ^ (tmp[38]
				| tmp[81] ^ tmp[13])));
		tmp[79] =
				tmp[4] ^ (vector[11] ^ tmp[54] & tmp[47]) ^ (tmp[22] & (tmp[6] & tmp[86] ^ tmp[19] ^ (tmp[38] | tmp[81] ^ tmp[79])) ^ tmp[34] & (tmp[71] ^ tmp[54] & tmp[85] ^ tmp[22] & ~(tmp[55] ^ (tmp[81]
						^ tmp[54] & tmp[79]))));
		tmp[86] = ~tmp[75];
		tmp[6] = tmp[75] ^ tmp[79];
		tmp[71] = ~tmp[79];
		tmp[15] = tmp[75] & tmp[71];
		tmp[84] = ~tmp[15];
		tmp[73] = tmp[79] & tmp[86];
		tmp[35] = ~tmp[73];
		tmp[125] = tmp[75] & tmp[79];
		tmp[19] =
				vector[61] ^ tmp[22] & ~(tmp[4] ^ (tmp[4] | tmp[38])) ^ (tmp[47] ^ (tmp[38] | tmp[8] ^ tmp[13])) ^ tmp[34] & (tmp[56] ^ (tmp[4] ^ tmp[82] & ~tmp[38]) ^ tmp[22] & (tmp[85] ^ tmp[38] & ~(tmp[81]
						^ tmp[19])));
		tmp[82] = tmp[70] & tmp[19];
		tmp[85] = tmp[70] | tmp[19];
		tmp[13] = ~tmp[70] & tmp[19];
		tmp[8] = tmp[70] & ~tmp[19];
		tmp[54] &= tmp[39];
		tmp[60] = tmp[55] ^ (vector[9] ^ tmp[4]) ^ (tmp[44] ^ tmp[22] & ~(tmp[33] ^ tmp[38] & tmp[47])) ^ (tmp[80] | tmp[54] ^ (tmp[39] ^ tmp[22] & ~(tmp[65] ^ tmp[56] ^ (tmp[38] | tmp[81] ^ tmp[60]))));
		tmp[56] = tmp[76] & tmp[60];
		tmp[65] = tmp[76] & ~tmp[60];
		tmp[47] = tmp[76] & ~tmp[65];
		tmp[33] = tmp[76] ^ tmp[60];
		tmp[39] = ~tmp[76];
		tmp[54] = tmp[60] & tmp[39];
		tmp[44] = tmp[76] | tmp[54];
		tmp[59] = (vector[63] | (vector[33] | tmp[53] ^ vector[25] & ~tmp[58])) ^ (vector[12] ^ (tmp[48] ^ (vector[33] | vector[17] ^ tmp[3]))) ^ (vector[9] | tmp[58] ^ (tmp[51] ^ tmp[50] & tmp[61]) ^ (
				vector[63] | tmp[49] ^ (tmp[58] ^ (vector[33] | tmp[59]))));
		tmp[58] = ~tmp[59];
		tmp[61] = tmp[32] & ~tmp[28] ^ tmp[98] & tmp[58];
		tmp[50] = tmp[28] | tmp[59];
		tmp[3] = tmp[98] ^ tmp[50];
		tmp[50] ^= tmp[28];
		tmp[49] = tmp[28] & tmp[58];
		tmp[51] = tmp[124] ^ tmp[49];
		tmp[53] = tmp[123] | tmp[59];
		tmp[48] = tmp[98] ^ tmp[59];
		tmp[55] = tmp[32] ^ tmp[53];
		tmp[34] = tmp[97] ^ tmp[122] & tmp[58];
		tmp[97] ^= tmp[97] | tmp[59];
		tmp[123] = tmp[98] ^ tmp[123] & tmp[58];
		tmp[58] = tmp[98] ^ tmp[32] & tmp[58];
		tmp[98] = tmp[122] ^ (tmp[32] | tmp[59]);
		tmp[126] = vector[53] ^ (tmp[58] ^ tmp[45] & tmp[61]) ^ (tmp[96] | tmp[49] ^ tmp[9] & tmp[55]) ^ (tmp[97] ^ (tmp[96] | tmp[123] ^ (tmp[45] | tmp[51])) ^ tmp[9] & tmp[50]) & ~tmp[66];
		tmp[127] = ~tmp[126];
		tmp[51] = vector[63] ^ (tmp[58] ^ (tmp[45] | tmp[61])) ^ (tmp[96] | tmp[49] ^ tmp[45] & tmp[55]) ^ (tmp[66] | tmp[97] ^ tmp[45] & tmp[50] ^ tmp[77] & (tmp[123] ^ tmp[45] & ~tmp[51]));
		tmp[123] = tmp[47] | tmp[51];
		tmp[50] = ~tmp[51];
		tmp[55] = tmp[33] & tmp[50];
		tmp[61] = tmp[76] ^ tmp[55];
		tmp[97] = tmp[60] | tmp[51];
		tmp[49] = tmp[60] ^ tmp[97];
		tmp[128] = tmp[76] ^ (tmp[76] | tmp[51]);
		tmp[129] = tmp[76] & tmp[50];
		tmp[130] = tmp[54] & tmp[50];
		tmp[53] ^= tmp[122];
		tmp[124] = tmp[122] ^ (tmp[124] | tmp[59]);
		tmp[122] = vector[35] ^ tmp[124] ^ tmp[9] & tmp[34] ^ tmp[77] & (tmp[58] ^ tmp[45] & tmp[98]) ^ (tmp[66] | tmp[124] ^ (tmp[96] | tmp[53] ^ tmp[45] & ~tmp[3]) ^ tmp[45] & ~tmp[48]);
		tmp[131] = ~tmp[42];
		tmp[3] = vector[49] ^ (tmp[124] ^ tmp[45] & ~tmp[34]) ^ tmp[77] & (tmp[58] ^ tmp[9] & tmp[98]) ^ (tmp[66] | tmp[77] & (tmp[53] ^ (tmp[45] | tmp[3])) ^ (tmp[124] ^ (tmp[45] | tmp[48])));
		tmp[63] ^=
				vector[31] & (tmp[5] ^ vector[63] & ~(tmp[63] ^ vector[55] & ~(tmp[11] ^ tmp[2])) ^ tmp[110] & (vector[39] & tmp[107])) ^ ((vector[55] | tmp[111]) ^ (vector[10] ^ vector[63] & (tmp[17] ^ (
						vector[39] | vector[55]))));
		tmp[111] = tmp[63] & ~tmp[25];
		tmp[34] = tmp[87] & tmp[111];
		tmp[48] = tmp[69] ^ (tmp[25] | tmp[111]);
		tmp[53] = ~tmp[63];
		tmp[98] = tmp[25] & tmp[53];
		tmp[53] &= tmp[25] & tmp[87];
		tmp[9] = tmp[25] & ~tmp[98];
		tmp[124] = tmp[25] & tmp[63];
		tmp[77] = tmp[53] ^ tmp[9];
		tmp[58] = tmp[87] & tmp[124];
		tmp[48] = tmp[77] ^ (vector[39] ^ (tmp[57] | tmp[58])) ^ (tmp[18] | tmp[57] & tmp[58] ^ (tmp[48] ^ (tmp[42] | tmp[37] & tmp[74] ^ tmp[48]))) ^ tmp[131] & (tmp[48] ^ (tmp[57] | tmp[77]));
		tmp[77] = tmp[76] & tmp[48];
		tmp[39] &= tmp[48];
		tmp[132] = tmp[76] | tmp[48];
		tmp[133] = tmp[76] ^ tmp[48];
		tmp[134] = ~tmp[48];
		tmp[135] = tmp[132] & tmp[134];
		tmp[136] = tmp[76] & tmp[134];
		tmp[58] ^= tmp[124];
		tmp[137] = ~tmp[18];
		tmp[111] = tmp[69] ^ (vector[43] ^ tmp[131] & (tmp[111] ^ tmp[34] ^ (tmp[57] | tmp[37] ^ tmp[111]))) ^ tmp[74] & (tmp[63] ^ tmp[53]) ^ tmp[137] & (tmp[9] ^ (tmp[34] ^ tmp[74] & tmp[58]) ^ (tmp[42]
				| tmp[111] ^ (tmp[7] | tmp[111])));
		tmp[53] = tmp[25] ^ tmp[63];
		tmp[138] = tmp[46] & ~tmp[111];
		tmp[34] =
				vector[45] ^ (tmp[72] ^ (tmp[37] ^ tmp[53]) ^ (tmp[42] | tmp[37] ^ tmp[7] & tmp[57])) ^ tmp[137] & ((tmp[42] | tmp[69] ^ (tmp[37] | tmp[57])) ^ (tmp[124] ^ (tmp[7] | tmp[9]) ^ tmp[74] & (
						tmp[34] ^ tmp[53])));
		tmp[53] ^=
				tmp[7] ^ (vector[25] ^ (tmp[57] | tmp[63])) ^ (tmp[42] | tmp[69] ^ (tmp[72] | tmp[98])) ^ (tmp[18] | tmp[58] ^ tmp[57] & (tmp[124] ^ tmp[87] & tmp[63]) ^ tmp[131] & ((tmp[69] | tmp[57]) ^ (
						tmp[63] ^ tmp[87] & tmp[53])));
		tmp[87] = ~tmp[53];
		tmp[69] = tmp[50] & tmp[53];
		tmp[11] = vector[31] & ~(tmp[17] ^ tmp[110] & (vector[63] & (tmp[11] ^ tmp[107])) ^ vector[55] & tmp[5]) ^ (vector[60] ^ (tmp[14] ^ (tmp[16] ^ vector[55] & (tmp[107] ^ vector[39] & tmp[2]))))
				^ vector[63] & ~(tmp[112] ^ (vector[55] | vector[47] ^ tmp[11]));
		tmp[2] = ~tmp[11];
		tmp[110] = tmp[52] & tmp[2];
		tmp[17] = tmp[52] & ~tmp[110];
		tmp[112] = tmp[81] | tmp[17];
		tmp[124] = ~tmp[36];
		tmp[98] = tmp[21] & tmp[11];
		tmp[72] = tmp[52] ^ tmp[11];
		tmp[131] = tmp[21] & tmp[72];
		tmp[58] = tmp[11] ^ tmp[131];
		tmp[9] = tmp[81] | tmp[11];
		tmp[37] = tmp[52] | tmp[9];
		tmp[74] = tmp[36] | tmp[37];
		tmp[113] =
				tmp[78] ^ tmp[32] & tmp[88] ^ (vector[5] ^ (tmp[92] | tmp[11])) ^ tmp[124] & (tmp[113] ^ (tmp[94] | tmp[11])) ^ tmp[66] & (tmp[95] ^ tmp[1] ^ (tmp[36] | tmp[113] ^ (tmp[106] | tmp[11])));
		tmp[104] =
				vector[17] ^ tmp[20] ^ ((tmp[36] | tmp[106] ^ (tmp[95] ^ tmp[88]) & tmp[2]) ^ tmp[105] & tmp[11]) ^ tmp[66] & ~(tmp[94] ^ (tmp[93] | tmp[11]) ^ tmp[124] & (tmp[32] & tmp[104] ^ tmp[1] ^ (
						tmp[99] | tmp[11])));
		tmp[93] = vector[3] ^ tmp[105] ^ tmp[103] & tmp[2] ^ ((tmp[36] | tmp[27] & tmp[78] ^ tmp[32] & ~tmp[91] ^ (tmp[92] ^ tmp[91] | tmp[11])) ^ tmp[66] & ~(tmp[124] & (tmp[102] ^ tmp[11] & ~tmp[93])
				^ (tmp[27] ^ tmp[103]) & tmp[11]));
		tmp[103] = tmp[15] ^ tmp[15] & tmp[93];
		tmp[91] = tmp[86] & tmp[93];
		tmp[105] = tmp[84] & tmp[93];
		tmp[88] = tmp[91] ^ tmp[79] & tmp[35];
		tmp[95] = tmp[73] ^ tmp[73] & tmp[93];
		tmp[94] = tmp[75] ^ tmp[93];
		tmp[106] = tmp[93] & (tmp[79] | tmp[15]);
		tmp[137] = tmp[40] | tmp[73] ^ tmp[106];
		tmp[139] = tmp[75] & tmp[93];
		tmp[140] = tmp[125] ^ tmp[139];
		tmp[71] &= tmp[93];
		tmp[141] = ~tmp[93];
		tmp[142] = tmp[3] & tmp[141];
		tmp[143] = tmp[11] & ~tmp[52];
		tmp[144] = tmp[21] & tmp[143];
		tmp[145] = ~tmp[41];
		tmp[9] = tmp[36] & tmp[9] ^ (vector[55] ^ tmp[72]) ^ tmp[121] & ~((tmp[41] | tmp[81] & ~tmp[36] ^ tmp[58]) ^ (tmp[17] ^ tmp[98] ^ tmp[74])) ^ tmp[145] & (tmp[112] ^ (tmp[36] | tmp[143] ^ tmp[144])
				^ tmp[52] & tmp[11]);
		tmp[146] = tmp[48] | tmp[9];
		tmp[147] = ~tmp[9];
		tmp[148] = tmp[77] & tmp[147];
		tmp[149] = tmp[76] | tmp[9];
		tmp[150] = tmp[136] ^ tmp[9];
		tmp[151] = tmp[39] & tmp[147];
		tmp[152] = tmp[136] ^ tmp[151];
		tmp[153] = tmp[135] | tmp[9];
		tmp[154] = tmp[76] & tmp[147];
		tmp[155] = tmp[133] ^ tmp[149];
		tmp[143] |= tmp[52];
		tmp[21] = vector[29] ^ (tmp[11] ^ (tmp[52] & tmp[21] ^ ((tmp[36] | tmp[52] ^ tmp[131]) ^ (tmp[41] | tmp[52] ^ tmp[124] & (tmp[52] ^ tmp[21] & tmp[110]))))) ^ tmp[121] & ~(tmp[37] ^ tmp[143]
				^ tmp[124] & tmp[98]);
		tmp[131] = tmp[127] & tmp[21];
		tmp[156] = tmp[21] ^ tmp[131];
		tmp[157] = tmp[19] | tmp[21];
		tmp[158] = ~tmp[21];
		tmp[159] = tmp[157] & tmp[158];
		tmp[160] = tmp[19] ^ tmp[21];
		tmp[161] = ~tmp[34];
		tmp[162] = tmp[126] | tmp[160];
		tmp[163] = tmp[43] & tmp[158];
		tmp[164] = ~tmp[62];
		tmp[165] = tmp[19] & tmp[21];
		tmp[166] = tmp[21] & ~tmp[165];
		tmp[162] = tmp[34] ^ (tmp[165] ^ (tmp[126] | tmp[166])) ^ tmp[164] & (tmp[162] ^ tmp[161] & (tmp[157] ^ tmp[162])) ^ (tmp[18] ^ ~tmp[43] & (tmp[157] ^ (tmp[62] | tmp[156] ^ (tmp[34]
				| tmp[131] ^ tmp[159])) ^ (tmp[126] | tmp[157])));
		tmp[18] = tmp[34] | tmp[131] ^ tmp[165];
		tmp[166] ^= tmp[127] & tmp[165];
		tmp[167] = tmp[126] | tmp[165];
		tmp[168] = tmp[126] | tmp[21];
		tmp[166] = tmp[167] ^ (tmp[159] ^ (tmp[121] ^ (tmp[34] | tmp[166]))) ^ (tmp[43] | tmp[165] ^ tmp[164] & (tmp[34] ^ tmp[165] ^ (tmp[19] | tmp[126])) ^ tmp[19] & tmp[127]) ^ (tmp[62]
				| tmp[34] & ~tmp[166]);
		tmp[158] &= tmp[19];
		tmp[159] = tmp[127] & tmp[158];
		tmp[131] = tmp[96] ^ (tmp[160] ^ tmp[18] ^ (tmp[62] | tmp[160] & (tmp[127] & tmp[161]))) ^ (tmp[43] | tmp[126] ^ (tmp[34] | tmp[156]) ^ tmp[164] & (tmp[159] ^ tmp[131] & tmp[161]));
		tmp[18] =
				tmp[100] ^ (tmp[21] ^ tmp[127] & tmp[157] ^ (tmp[19] | tmp[34]) ^ (tmp[62] | tmp[19] ^ tmp[168] ^ tmp[161] & (tmp[165] ^ tmp[159])) ^ (tmp[43] | (tmp[34] | tmp[126] ^ tmp[158]) ^ (tmp[62]
						| tmp[168] ^ (tmp[157] ^ tmp[18]))));
		tmp[112] = vector[51] ^ (tmp[17] ^ (tmp[81] | tmp[52]) ^ (tmp[36] | tmp[58]) ^ tmp[145] & (tmp[144] ^ (tmp[36] | tmp[110] ^ tmp[98])) ^ tmp[121] & ~(tmp[11] ^ tmp[112] ^ (tmp[36]
				| tmp[112] ^ tmp[143])));
		tmp[145] = vector[33] ^ ((tmp[52] | tmp[11]) ^ (tmp[81] ^ tmp[36]) ^ (tmp[41] | tmp[74] ^ tmp[143]) ^ tmp[121] & (tmp[72] ^ tmp[11] & (tmp[124] & tmp[145]) ^ (tmp[36] | tmp[11] ^ tmp[37])));
		tmp[55] = tmp[31] ^ (tmp[60] ^ tmp[51] ^ tmp[53] & ~tmp[56] ^ tmp[145] & (tmp[76] ^ tmp[53] & (tmp[54] ^ tmp[97])) ^ (tmp[104] | tmp[76] ^ tmp[53] ^ tmp[145] & ~(tmp[55] ^ (tmp[33]
				^ tmp[53] & ~tmp[49]))));
		tmp[97] = ~tmp[104];
		tmp[130] =
				tmp[44] ^ tmp[53] & ~tmp[130] ^ tmp[145] & (tmp[123] ^ (tmp[65] ^ tmp[130] & tmp[53])) ^ (tmp[59] ^ tmp[97] & (tmp[76] ^ tmp[60] & tmp[69] ^ tmp[145] & ~(tmp[76] ^ tmp[128] & tmp[53])));
		tmp[141] &= tmp[145];
		tmp[33] ^=
				tmp[52] ^ (tmp[44] & tmp[50] ^ tmp[53] & (tmp[54] ^ tmp[129]) ^ tmp[145] & ~(tmp[56] ^ tmp[123] ^ tmp[53] & (tmp[60] ^ (tmp[54] | tmp[51]))) ^ (tmp[104] | tmp[49] & tmp[87] ^ tmp[145] & (
						tmp[128] ^ (tmp[60] ^ (tmp[33] | tmp[51])) & tmp[87])));
		tmp[129] =
				tmp[145] & ~(tmp[56] & (tmp[50] ^ tmp[53])) ^ (tmp[57] ^ ((tmp[76] | tmp[60] | tmp[51]) ^ (tmp[47] ^ tmp[53] & (tmp[44] ^ tmp[129])))) ^ tmp[97] & (tmp[145] & (tmp[61] ^ tmp[61] & tmp[53]) ^ (
						tmp[123] ^ tmp[69]));
		tmp[20] = vector[23] ^ (tmp[92] ^ tmp[1] ^ (tmp[102] | tmp[11]) ^ (tmp[36] | tmp[78] ^ tmp[99] ^ tmp[102] & tmp[2]) ^ tmp[66] & ~((tmp[36] | tmp[1] ^ (tmp[20] | tmp[11])) ^ (tmp[20]
				^ tmp[11] & ~tmp[20])));
		tmp[107] = vector[55] ^ tmp[23] ^ vector[39] & vector[47] ^ vector[63] & ~(tmp[14] ^ (tmp[109] ^ vector[55] & tmp[12])) ^ (vector[8] ^ vector[31] & (tmp[16] ^ (vector[39] ^ vector[55] & (tmp[107]
				^ vector[39] & tmp[108])) ^ vector[63] & ~(tmp[5] ^ vector[55] & (tmp[12] ^ tmp[107]))));
		tmp[12] = ~tmp[107];
		tmp[108] = tmp[29] & tmp[12];
		tmp[5] = ~tmp[90];
		tmp[109] = tmp[68] | tmp[107];
		tmp[109] ^= tmp[67] ^ tmp[100] ^ vector[31] ^ (tmp[90] | tmp[114] ^ (tmp[64] ^ tmp[64] & tmp[116] | tmp[107])) ^ tmp[80] & ~(tmp[119] ^ tmp[108] ^ tmp[5] & (tmp[114] ^ tmp[109]));
		tmp[114] = ~tmp[109];
		tmp[16] = tmp[70] & ~tmp[8] | tmp[109];
		tmp[14] = tmp[70] & tmp[114];
		tmp[152] = tmp[107] ^ (tmp[39] ^ ((tmp[48] & ~tmp[77] | tmp[9]) ^ (tmp[51] & ~(tmp[10] & tmp[132] ^ tmp[152]) ^ tmp[109] & ~(tmp[148] ^ (tmp[132] ^ (tmp[51] & (tmp[152] ^ tmp[10] & ~tmp[154])
				^ tmp[10] & (tmp[76] ^ tmp[148]))))) ^ tmp[10] & ~tmp[135]));
		tmp[23] = tmp[19] | tmp[109];
		tmp[45] ^= tmp[76] ^ tmp[146] ^ tmp[51] & (tmp[9] ^ (tmp[48] ^ tmp[10] & ~tmp[132])) ^ (
				tmp[109] & (tmp[9] ^ (tmp[133] ^ tmp[10] & ~(tmp[132] & tmp[147])) ^ tmp[51] & ~(tmp[77] ^ tmp[153] ^ tmp[10] & ~(tmp[135] ^ tmp[154]))) ^ tmp[10] & (tmp[132] ^ tmp[151]));
		tmp[1] = tmp[48] & tmp[109];
		tmp[32] ^=
				tmp[1] ^ (tmp[19] ^ (tmp[14] ^ (tmp[20] & (tmp[48] & (tmp[62] & tmp[19]) ^ (tmp[48] | tmp[23])) ^ tmp[62] & ~(tmp[70] ^ (tmp[70] | tmp[109]) ^ tmp[48] & (tmp[85] ^ (tmp[8] | tmp[109]))))));
		tmp[1] = tmp[82] & tmp[114];
		tmp[154] =
				tmp[77] ^ tmp[11] ^ (tmp[51] & ~(tmp[148] ^ tmp[10] & ~tmp[150]) ^ tmp[10] & (tmp[48] ^ (tmp[132] | tmp[9])) ^ tmp[48] & tmp[147]) ^ tmp[109] & ~(tmp[155] ^ tmp[10] & tmp[155] ^ tmp[51] & (
						tmp[148] ^ (tmp[77] ^ tmp[10] & (tmp[76] ^ tmp[154]))));
		tmp[77] = tmp[166] & tmp[154];
		tmp[132] = tmp[48] & (tmp[114] | ~tmp[19]);
		tmp[155] = ~tmp[109];
		tmp[149] = tmp[63] ^ (tmp[151] ^ (tmp[133] ^ (tmp[10] & ~tmp[148] ^ tmp[51] & (tmp[135] ^ tmp[146] ^ tmp[10] & tmp[134])))
				^ (tmp[150] ^ tmp[51] & ~(tmp[48] ^ tmp[10] & ~(tmp[39] ^ tmp[149]) ^ tmp[136] & tmp[147]) ^ ~tmp[10] & (tmp[48] ^ tmp[153])) & tmp[109]);
		tmp[39] = tmp[19] & tmp[155];
		tmp[114] = tmp[20] & ~(tmp[19] ^ (tmp[132] ^ tmp[62] & (tmp[23] ^ tmp[132]))) ^ (tmp[70] ^ tmp[19] ^ tmp[48] & ~tmp[16]) ^ (tmp[0] ^ (
				tmp[62] & ~(tmp[48] & (tmp[13] ^ tmp[109]) ^ (tmp[13] ^ tmp[8] & tmp[114])) ^ tmp[39]));
		tmp[132] = ~tmp[129];
		tmp[0] = tmp[114] & tmp[132];
		tmp[147] = tmp[114] ^ tmp[0];
		tmp[136] = tmp[129] ^ tmp[114];
		tmp[8] = tmp[64] ^ (tmp[70] ^ tmp[48] ^ (tmp[85] | tmp[109]) ^ tmp[20] & ~(tmp[19] ^ (tmp[109] ^ tmp[62] & (tmp[13] ^ tmp[48] & ~tmp[23])))) ^ tmp[62] & ~(tmp[48] & (tmp[8] & tmp[155]));
		tmp[134] = ~tmp[8];
		tmp[146] = tmp[162] & tmp[134];
		tmp[135] = tmp[162] | tmp[8];
		tmp[148] = tmp[162] ^ tmp[146];
		tmp[153] = tmp[162] ^ tmp[135];
		tmp[150] = ~tmp[107];
		tmp[133] = tmp[64] & tmp[150];
		tmp[1] = tmp[4] ^ (tmp[13] ^ tmp[62] & ~(tmp[1] ^ (tmp[70] ^ tmp[48] & ~(tmp[19] ^ tmp[1])))) ^ (tmp[16] ^ tmp[48] & ~(tmp[85] ^ tmp[82] & tmp[155])) ^ tmp[20] & ~(tmp[39] ^ (tmp[85] ^ (
				tmp[62] & (tmp[23] ^ (tmp[48] | tmp[70] ^ tmp[23])) ^ tmp[48] & ((tmp[70] | tmp[13]) ^ tmp[14]))));
		tmp[29] =
				tmp[80] & (tmp[118] ^ (tmp[90] | tmp[119] ^ tmp[64] & tmp[12]) ^ (tmp[118] | tmp[107])) ^ (vector[27] ^ (tmp[30] ^ tmp[116] ^ tmp[120] & tmp[12] ^ (tmp[90] | tmp[29] ^ tmp[100] & ~tmp[29] ^ (
						tmp[64] ^ tmp[24] | tmp[107]))));
		tmp[24] = ~tmp[1];
		tmp[119] = ~tmp[111];
		tmp[118] = tmp[46] ^ tmp[29];
		tmp[23] = tmp[29] & tmp[119];
		tmp[13] = tmp[111] | tmp[29];
		tmp[14] = tmp[138] ^ tmp[118];
		tmp[155] = tmp[46] & ~tmp[29];
		tmp[82] = tmp[138] ^ tmp[155];
		tmp[85] = tmp[23] ^ tmp[155];
		tmp[39] = ~tmp[122];
		tmp[140] = tmp[81] ^ (tmp[79] ^ (tmp[91] ^ (tmp[40] | tmp[95]))) ^ tmp[29] & ~(tmp[103] ^ tmp[40] & ~(tmp[75] ^ tmp[139])) ^ tmp[39] & (tmp[140] ^ (tmp[40] | tmp[15] ^ tmp[93])
				^ (tmp[103] ^ tmp[40] & tmp[140]) & tmp[29]);
		tmp[81] = ~tmp[140];
		tmp[16] = tmp[166] & tmp[81];
		tmp[4] = tmp[166] ^ tmp[16];
		tmp[151] = ~tmp[154];
		tmp[35] = tmp[78] ^ (tmp[6] ^ tmp[125] & tmp[93] ^ (tmp[40] | tmp[94])) ^ ((tmp[122] | tmp[101] & tmp[94] ^ tmp[71] ^ tmp[29] & ~((tmp[40] | tmp[139]) ^ tmp[35] & tmp[93])) ^ tmp[29] & ~(tmp[105]
				^ tmp[101] & tmp[91]));
		tmp[139] = ~tmp[35];
		tmp[73] = (tmp[122] | tmp[6] ^ tmp[105] ^ tmp[137] ^ (tmp[103] ^ tmp[101] & tmp[75]) & tmp[29]) ^ (tmp[89] ^ (tmp[75] ^ tmp[71] ^ (tmp[40] | tmp[73] ^ tmp[93]) ^ tmp[29] & ~(tmp[101] & tmp[73])));
		tmp[103] = ~tmp[46];
		tmp[105] = tmp[23] & tmp[103];
		tmp[88] = tmp[90] ^ (tmp[94] ^ tmp[40] & tmp[84]) ^ ((tmp[40] | tmp[88]) ^ tmp[95]) & tmp[29] ^ (tmp[122] | tmp[15] ^ tmp[106] ^ tmp[101] & (tmp[15] ^ tmp[79] & tmp[93])
				^ (tmp[71] ^ tmp[40] & ~tmp[88]) & tmp[29]);
		tmp[15] = tmp[46] & tmp[29];
		tmp[71] = tmp[23] ^ tmp[15];
		tmp[101] = tmp[119] & tmp[15];
		tmp[106] = tmp[111] | tmp[15];
		tmp[84] = tmp[113] & ~((tmp[46] | tmp[111]) ^ tmp[15]);
		tmp[95] = tmp[46] | tmp[29];
		tmp[155] =
				tmp[14] ^ (tmp[28] ^ tmp[113] & (tmp[111] | tmp[95])) ^ (tmp[122] | tmp[113] & ~tmp[23]) ^ tmp[112] & ((tmp[122] | tmp[111] & tmp[113] ^ tmp[13]) ^ tmp[119] & tmp[155] ^ tmp[113] & tmp[105]);
		tmp[28] = tmp[119] & tmp[95];
		tmp[94] = ~tmp[155];
		tmp[6] = tmp[32] & tmp[94];
		tmp[137] = tmp[15] ^ tmp[28];
		tmp[101] = tmp[25] ^ (tmp[15] ^ tmp[113] & ~(tmp[138] ^ tmp[15])) ^ (tmp[111] | tmp[46] & ~tmp[15]) ^ tmp[39] & (tmp[82] ^ tmp[113] & tmp[137]) ^ tmp[112] & (tmp[101] ^ tmp[103] & tmp[95] ^ (
				(tmp[122] | tmp[71] ^ tmp[113] & tmp[85]) ^ tmp[113] & ~(tmp[101] ^ tmp[95])));
		tmp[25] = ~tmp[101];
		tmp[89] = tmp[162] & tmp[25];
		tmp[91] = tmp[162] ^ tmp[101];
		tmp[125] = tmp[132] & tmp[101];
		tmp[78] = tmp[162] & tmp[101];
		tmp[63] = tmp[134] & tmp[78];
		tmp[11] = ~tmp[78];
		tmp[2] = tmp[162] & tmp[11];
		tmp[102] = tmp[162] | tmp[101];
		tmp[99] = tmp[8] | tmp[102];
		tmp[92] = ~tmp[162];
		tmp[44] = tmp[102] & tmp[92];
		tmp[61] = tmp[101] & tmp[92];
		tmp[50] = tmp[44] ^ tmp[101] & (tmp[134] & tmp[92]);
		tmp[85] =
				tmp[112] & ~(tmp[23] ^ (tmp[95] ^ (tmp[84] ^ (tmp[122] | tmp[85] ^ tmp[113] & ~(tmp[111] ^ tmp[29]))))) ^ (tmp[106] ^ (tmp[36] ^ tmp[95]) ^ tmp[113] & (tmp[15] ^ tmp[106]) ^ tmp[39] & (tmp[14]
						^ tmp[113] & ~(tmp[118] ^ tmp[105])));
		tmp[71] = (tmp[113] | tmp[82]) ^ (tmp[38] ^ tmp[137]) ^ (tmp[122] | tmp[113] & (tmp[138] ^ tmp[95]) ^ (tmp[118] ^ tmp[28])) ^ tmp[112] & ~(tmp[13] ^ tmp[113] & (tmp[119] & tmp[118] ^ tmp[95]) ^ (
				tmp[122] | tmp[23] ^ (tmp[46] ^ tmp[113] & tmp[71])));
		tmp[118] = tmp[1] & tmp[71];
		tmp[119] = tmp[24] & tmp[71];
		tmp[95] = tmp[33] & ~tmp[118];
		tmp[23] = tmp[1] | tmp[71];
		tmp[138] = tmp[33] & tmp[71];
		tmp[13] = tmp[33] & tmp[23];
		tmp[28] = tmp[71] ^ tmp[13];
		tmp[137] = ~tmp[71];
		tmp[38] = tmp[1] ^ tmp[71];
		tmp[82] = tmp[33] & ~(tmp[24] & tmp[23]);
		tmp[105] = tmp[1] & tmp[137];
		tmp[84] = tmp[95] ^ tmp[105];
		tmp[106] = tmp[33] & tmp[1];
		tmp[15] = tmp[13] ^ tmp[38];
		tmp[36] = tmp[33] & tmp[38];
		tmp[14] = tmp[38] ^ tmp[106];
		tmp[39] = ~tmp[90];
		tmp[133] =
				vector[41] ^ (tmp[22] ^ tmp[100] ^ tmp[68] & tmp[12]) ^ (tmp[64] ^ tmp[64] & tmp[100] ^ (tmp[68] ^ tmp[115] | tmp[107])) & tmp[39] ^ tmp[80] & ~(tmp[67] & tmp[100] ^ tmp[133] ^ tmp[39] & (
						tmp[64] ^ tmp[133]));
		tmp[67] = ~tmp[133];
		tmp[39] = tmp[145] & tmp[67];
		tmp[47] = ~tmp[93] & tmp[133];
		tmp[56] = tmp[67] & (tmp[93] & tmp[145]);
		tmp[69] = tmp[93] | tmp[133];
		tmp[123] = tmp[145] & ~tmp[69];
		tmp[67] &= tmp[69];
		tmp[57] = tmp[145] & tmp[69];
		tmp[97] = tmp[141] ^ tmp[67];
		tmp[54] = tmp[93] & tmp[133];
		tmp[87] = ~tmp[54];
		tmp[128] = tmp[145] & tmp[87];
		tmp[49] = ~tmp[75];
		tmp[52] = tmp[133] ^ tmp[39];
		tmp[65] = tmp[93] ^ tmp[133];
		tmp[66] ^=
				tmp[133] ^ (tmp[57] ^ (tmp[3] & (tmp[47] ^ tmp[123]) ^ (tmp[75] | tmp[54] ^ tmp[3] & tmp[69]))) ^ (tmp[53] | (tmp[3] | tmp[52]) ^ (tmp[93] ^ (tmp[75] | tmp[65] ^ (tmp[142] ^ tmp[56]))));
		tmp[59] = ~tmp[66];
		tmp[31] = tmp[32] ^ tmp[66];
		tmp[124] = tmp[35] & tmp[59];
		tmp[37] = tmp[35] ^ tmp[66];
		tmp[72] = tmp[139] & tmp[66];
		tmp[143] = tmp[32] & tmp[66];
		tmp[74] = tmp[6] & tmp[59];
		tmp[121] = tmp[66] & ~tmp[143];
		tmp[98] = tmp[66] & ~tmp[32];
		tmp[110] = tmp[74] ^ tmp[98];
		tmp[144] = tmp[35] | tmp[66];
		tmp[58] = tmp[32] ^ tmp[155] ^ tmp[130] & ~(tmp[66] ^ tmp[94] & tmp[31]) ^ (tmp[131] | tmp[110] ^ (tmp[130] | tmp[32] ^ tmp[94] & tmp[66]));
		tmp[17] = ~tmp[45];
		tmp[157] = tmp[155] | tmp[66];
		tmp[159] = tmp[121] ^ tmp[157];
		tmp[74] = (tmp[32] | tmp[155]) ^ (tmp[31] ^ tmp[130] & (tmp[143] ^ tmp[74])) ^ (tmp[131] | tmp[159] ^ (tmp[130] | tmp[143]));
		tmp[126] ^= tmp[74] ^ tmp[58] & tmp[17];
		tmp[58] = tmp[51] ^ tmp[74] ^ tmp[45] & ~tmp[58];
		tmp[74] = tmp[32] | tmp[66];
		tmp[98] = tmp[155] ^ tmp[59] & tmp[74] ^ tmp[130] & tmp[110] ^ (tmp[131] | tmp[121] ^ tmp[94] & tmp[98] ^ tmp[130] & tmp[159]);
		tmp[157] = tmp[130] & ~(tmp[6] ^ tmp[66]) ^ (tmp[31] ^ tmp[94] & tmp[74] ^ (tmp[131] | tmp[31] ^ tmp[94] & tmp[143] ^ tmp[130] & ~(tmp[31] ^ tmp[157])));
		tmp[17] = tmp[122] ^ (tmp[98] ^ tmp[17] & tmp[157]);
		tmp[157] = tmp[3] ^ tmp[98] ^ tmp[45] & ~tmp[157];
		tmp[98] = tmp[145] & tmp[65];
		tmp[54] = tmp[80] ^ tmp[133] ^ ((tmp[75] | tmp[133] & tmp[87] ^ tmp[145] & tmp[47] ^ tmp[3] & ~tmp[97]) ^ (tmp[53] | tmp[65] ^ tmp[98] ^ (tmp[3] & ~(tmp[56] ^ tmp[54]) ^ tmp[49] & (tmp[54]
				^ tmp[3] & tmp[54]))) ^ tmp[3] & (tmp[54] ^ tmp[128]) ^ tmp[145] & ~tmp[67]);
		tmp[56] = ~tmp[54];
		tmp[47] = tmp[134] & tmp[54];
		tmp[98] = (tmp[3] & tmp[123] ^ (tmp[75] | tmp[69] ^ (tmp[142] ^ tmp[98]))) & ~tmp[53] ^ (tmp[145] ^ (tmp[86] & tmp[3] ^ (tmp[41] ^ tmp[65])) ^ tmp[3] & ~tmp[39]);
		tmp[142] = tmp[166] ^ tmp[98];
		tmp[41] = tmp[166] | tmp[98];
		tmp[86] = tmp[140] | tmp[142];
		tmp[69] = tmp[16] ^ tmp[98];
		tmp[39] = tmp[81] & tmp[142];
		tmp[123] = tmp[16] ^ tmp[142];
		tmp[87] = tmp[140] ^ tmp[98];
		tmp[67] = tmp[81] & tmp[98];
		tmp[77] ^= tmp[87] ^ tmp[145] ^ tmp[85] & (tmp[98] | ~tmp[77]) ^ tmp[33] & ~(tmp[166] ^ tmp[85] & (tmp[151] & tmp[39]) ^ (tmp[154] | tmp[98]));
		tmp[87] = tmp[21] ^ (tmp[154] ^ (tmp[166] ^ tmp[140])) ^ tmp[85] & ~(tmp[69] ^ (tmp[154] | tmp[86])) ^ tmp[33] & ~(tmp[4] & tmp[151] ^ tmp[87] ^ tmp[85] & (tmp[87] ^ tmp[151] & tmp[67]));
		tmp[122] = ~tmp[87];
		tmp[31] = tmp[126] | tmp[87];
		tmp[143] = tmp[142] ^ tmp[67];
		tmp[86] = tmp[33] & ~(tmp[86] ^ (tmp[154] | tmp[39]) ^ tmp[85] & ~(tmp[151] & tmp[123] ^ (tmp[166] | tmp[140]))) ^ (tmp[112] ^ (tmp[123] ^ tmp[85] & (tmp[143] ^ tmp[151] & (tmp[98] ^ tmp[86])))
				^ tmp[154] & ~tmp[4]);
		tmp[123] = tmp[98] & ~tmp[166];
		tmp[39] = tmp[9] ^ tmp[69] ^ tmp[151] & (tmp[41] ^ (tmp[140] | tmp[41])) ^ tmp[85] & (tmp[123] ^ (tmp[154] | tmp[140]) ^ tmp[16] & tmp[98]) ^ tmp[33] & ~(tmp[142] ^ tmp[81] & tmp[123] ^ (
				tmp[151] & tmp[143] ^ tmp[85] & (tmp[98] ^ tmp[67] ^ tmp[151] & (tmp[98] ^ tmp[39]))));
		tmp[3] =
				tmp[49] & (tmp[3] & tmp[141] ^ (tmp[133] ^ tmp[128])) ^ (tmp[145] & tmp[133] ^ (tmp[93] ^ (tmp[42] ^ tmp[3] & ~tmp[141]))) ^ (tmp[53] | tmp[57] ^ (tmp[65] ^ tmp[3] & (tmp[93] ^ tmp[128])) ^ (
						tmp[75] | tmp[52] ^ tmp[97] & ~tmp[3]));
		tmp[68] =
				(tmp[90] | tmp[117] & ~tmp[150]) ^ (tmp[80] & (tmp[22] ^ tmp[100] & ~tmp[68] ^ tmp[108] ^ tmp[5] & (tmp[30] ^ tmp[115] ^ tmp[30] & tmp[12])) ^ (vector[13] ^ (tmp[64] ^ tmp[116] ^ (tmp[120]
						| tmp[107]))));
		tmp[12] = ~tmp[68];
		tmp[30] = tmp[43] & tmp[12];
		tmp[115] = tmp[21] ^ tmp[68];
		tmp[100] = tmp[43] & tmp[115];
		tmp[115] = tmp[43] & ~tmp[115];
		tmp[12] &= tmp[21];
		tmp[107] = ~tmp[83];
		tmp[120] = tmp[68] & ~tmp[21];
		tmp[116] = tmp[43] & tmp[12];
		tmp[64] = ~tmp[113];
		tmp[5] = tmp[68] | tmp[12];
		tmp[30] = tmp[120] ^ (tmp[83] | tmp[30] ^ tmp[12]) ^ tmp[64] & (tmp[30] ^ tmp[5] ^ tmp[107] & (tmp[21] ^ tmp[100]));
		tmp[108] = tmp[43] ^ tmp[12] ^ (tmp[83] | tmp[116] ^ tmp[68] & ~tmp[120]) ^ (tmp[113] | tmp[68] ^ ((tmp[43] | tmp[83]) ^ tmp[116]));
		tmp[7] ^= tmp[108] ^ tmp[46] & ~tmp[30];
		tmp[150] = tmp[132] & tmp[7];
		tmp[80] = tmp[129] | tmp[7];
		tmp[117] = tmp[114] & tmp[7];
		tmp[90] = tmp[129] | tmp[117];
		tmp[128] = tmp[114] & ~tmp[117];
		tmp[141] = tmp[7] ^ tmp[90];
		tmp[97] = tmp[117] ^ tmp[132] & tmp[117];
		tmp[52] = ~tmp[114];
		tmp[65] = tmp[114] | tmp[7];
		tmp[42] = tmp[52] & tmp[65];
		tmp[52] &= tmp[7];
		tmp[57] = tmp[132] & tmp[52];
		tmp[145] = ~tmp[7];
		tmp[0] &= tmp[145];
		tmp[49] = tmp[128] ^ tmp[0];
		tmp[25] &= tmp[7];
		tmp[67] = tmp[132] & tmp[25];
		tmp[25] ^= tmp[67];
		tmp[143] = tmp[149] & ~tmp[25];
		tmp[123] = tmp[101] & tmp[7];
		tmp[41] = tmp[101] ^ tmp[7];
		tmp[142] = ~tmp[123];
		tmp[16] = tmp[132] & tmp[41];
		tmp[69] = tmp[7] & tmp[142];
		tmp[9] = tmp[67] ^ tmp[123];
		tmp[9] =
				tmp[111] ^ (tmp[129] ^ (tmp[41] ^ (tmp[162] & ~(tmp[143] ^ tmp[9] ^ tmp[3] & ~(tmp[69] ^ tmp[149] & tmp[123])) ^ tmp[3] & (tmp[9] ^ tmp[149] & (tmp[101] ^ tmp[125])) ^ tmp[149] & tmp[142])));
		tmp[125] &= tmp[145];
		tmp[142] = tmp[114] ^ tmp[7];
		tmp[111] = tmp[132] & tmp[142];
		tmp[4] = tmp[150] ^ tmp[142];
		tmp[112] = tmp[73] & ~tmp[4];
		tmp[90] =
				tmp[75] ^ (tmp[80] ^ (tmp[128] ^ (tmp[45] & ~(tmp[42] ^ (tmp[150] ^ tmp[73] & (tmp[7] ^ tmp[150]))) ^ tmp[73] & ~(tmp[7] ^ tmp[80])))) ^ tmp[131] & (tmp[128] ^ (tmp[90] ^ tmp[73] & tmp[49])
						^ tmp[45] & (tmp[142] ^ tmp[132] & tmp[65] ^ tmp[73] & (tmp[114] ^ tmp[111])));
		tmp[128] = tmp[129] | tmp[142];
		tmp[147] = tmp[83] ^ (tmp[141] ^ (tmp[131] & ~(tmp[73] & (tmp[117] ^ tmp[128]) ^ (tmp[114] ^ tmp[128]) ^ tmp[45] & ~(tmp[147] ^ tmp[73] & ~tmp[147])) ^ tmp[73] & ~tmp[97])) ^ tmp[45] & (tmp[136]
				^ tmp[73] & ~tmp[136]);
		tmp[57] = tmp[10] ^ (tmp[42] ^ (tmp[112] ^ tmp[128]) ^ tmp[45] & (tmp[57] ^ (tmp[114] ^ tmp[73] & tmp[4])) ^ tmp[131] & ~(tmp[49] ^ tmp[73] & (tmp[52] ^ tmp[57]) ^ tmp[45] & (tmp[97]
				^ tmp[73] & ~tmp[142])));
		tmp[117] =
				tmp[62] ^ tmp[114] ^ (tmp[150] ^ (tmp[45] & ~(tmp[0] ^ tmp[73] & (tmp[114] ^ tmp[80])) ^ tmp[73] & tmp[141])) ^ tmp[131] & ~(tmp[142] ^ tmp[73] & ~(tmp[80] ^ tmp[117]) ^ (tmp[129] | tmp[65])
						^ tmp[45] & (tmp[111] ^ tmp[112]));
		tmp[80] = tmp[101] | tmp[7];
		tmp[145] &= tmp[80];
		tmp[0] = tmp[41] ^ (tmp[129] | tmp[145]);
		tmp[112] = tmp[129] | tmp[80];
		tmp[123] =
				tmp[7] ^ (tmp[129] | tmp[123]) ^ (tmp[53] ^ tmp[149] & ~(tmp[41] ^ tmp[132] & tmp[123])) ^ (tmp[162] & ~(tmp[25] ^ (tmp[3] & (tmp[125] ^ tmp[149] & tmp[7]) ^ tmp[149] & (tmp[123] ^ tmp[16])))
						^ tmp[3] & ~(tmp[7] ^ (tmp[145] ^ tmp[112]) & ~tmp[149]));
		tmp[53] = tmp[58] & ~tmp[123];
		tmp[111] = tmp[58] & tmp[123];
		tmp[65] = tmp[58] & ~tmp[111];
		tmp[142] = ~tmp[58];
		tmp[141] = tmp[58] ^ tmp[123];
		tmp[150] = tmp[58] | tmp[123];
		tmp[62] = tmp[142] & tmp[150];
		tmp[112] = tmp[48] ^ (tmp[67] ^ (tmp[143] ^ tmp[145]) ^ tmp[3] & (tmp[69] ^ (tmp[149] & tmp[25] ^ (tmp[129] | tmp[69]))) ^ tmp[162] & (tmp[80] ^ tmp[149] & ~tmp[112] ^ tmp[3] & ~(tmp[7] ^ tmp[112]
				^ tmp[149] & tmp[101])));
		tmp[80] = tmp[34] ^ (tmp[101] ^ (tmp[149] & ~tmp[0] ^ tmp[3] & ~(tmp[125] ^ tmp[145])) ^ (tmp[129] | tmp[41]) ^ tmp[162] & (tmp[16] ^ (tmp[7] ^ tmp[149] & tmp[0]) ^ tmp[3] & ~(tmp[7]
				^ tmp[132] & tmp[80])));
		tmp[108] ^= tmp[103] & tmp[30] ^ tmp[26];
		tmp[26] = (tmp[146] ^ tmp[89]) & tmp[108];
		tmp[30] = tmp[61] & ~tmp[108];
		tmp[132] = tmp[108] & ~tmp[2];
		tmp[26] = tmp[40] ^ (tmp[91] ^ tmp[92] & tmp[108]) ^ tmp[55] & ~(tmp[50] ^ tmp[26]) ^ (tmp[88] | tmp[148] ^ tmp[108] & (tmp[99] ^ tmp[44]) ^ tmp[55] & ~(tmp[26] ^ (tmp[8] | tmp[101])));
		tmp[91] ^=
				tmp[70] ^ ((tmp[8] | tmp[44]) ^ tmp[11] & tmp[108] ^ tmp[55] & ~(tmp[2] ^ tmp[134] & tmp[91] ^ tmp[148] & tmp[108]) ^ (tmp[88] | tmp[50] ^ tmp[30] ^ tmp[55] & (tmp[2] ^ (tmp[8] | tmp[91])
						^ tmp[153] & tmp[108])));
		tmp[78] =
				tmp[8] ^ tmp[102] ^ (tmp[76] ^ tmp[55] & (tmp[2] ^ tmp[99])) ^ tmp[108] & ~(tmp[162] ^ tmp[99]) ^ (tmp[88] | tmp[153] ^ (tmp[132] ^ tmp[55] & ~(tmp[135] ^ tmp[108] & (tmp[146] ^ tmp[78]))));
		tmp[134] = tmp[8] ^ tmp[44] ^ (tmp[43] ^ tmp[132]) ^ ((tmp[88] | tmp[30] ^ (tmp[2] ^ tmp[55] & (tmp[63] ^ tmp[61] ^ tmp[134] & tmp[108]))) ^ tmp[55] & (tmp[89] ^ tmp[63] ^ tmp[108] & (tmp[146]
				^ tmp[61])));
		tmp[61] = ~tmp[134];
		tmp[63] = tmp[122] & tmp[134];
		tmp[21] &= tmp[68];
		tmp[120] = tmp[5] ^ (tmp[115] ^ ((tmp[113] | tmp[163] ^ tmp[120] ^ (tmp[83] | tmp[68] ^ tmp[115])) ^ tmp[107] & (tmp[163] ^ tmp[21])));
		tmp[100] = tmp[21] ^ (tmp[115] ^ (tmp[83] | tmp[12] ^ tmp[116])) ^ tmp[64] & (tmp[116] ^ (tmp[83] | tmp[68] ^ tmp[100]));
		tmp[103] = tmp[100] ^ (tmp[22] ^ tmp[103] & tmp[120]);
		tmp[106] = tmp[19] ^ (tmp[140] | tmp[118] ^ tmp[138]) ^ (tmp[84] ^ ((tmp[23] ^ tmp[81] & tmp[23] ^ tmp[1] & tmp[138] | tmp[54]) ^ (tmp[103] | tmp[23] ^ (
				tmp[71] ^ (tmp[137] & tmp[106] ^ tmp[81] & tmp[14]) | tmp[54]))));
		tmp[19] = ~tmp[106];
		tmp[22] = tmp[126] & tmp[19];
		tmp[116] = tmp[126] | tmp[106];
		tmp[12] = tmp[122] & tmp[106];
		tmp[83] = tmp[87] ^ tmp[22];
		tmp[115] = ~tmp[117];
		tmp[64] = tmp[126] & tmp[106];
		tmp[21] = tmp[106] & ~tmp[64];
		tmp[163] = tmp[87] | tmp[21];
		tmp[107] = tmp[122] & tmp[64];
		tmp[163] = tmp[166] ^ (tmp[116] ^ tmp[107] ^ (tmp[134] | tmp[12] ^ tmp[64]) ^ (tmp[117] | tmp[163] ^ tmp[134] & (tmp[22] ^ tmp[12]))) ^ ~tmp[80] & (tmp[163] ^ tmp[115] & (tmp[134] ^ tmp[163]));
		tmp[166] = tmp[126] ^ tmp[106];
		tmp[5] = tmp[122] & tmp[166];
		tmp[83] = tmp[162] ^ tmp[87] ^ (tmp[21] ^ ((tmp[80] | tmp[115] & (tmp[83] ^ (tmp[134] | tmp[106] ^ tmp[5]))) ^ (tmp[134] | tmp[116] ^ (tmp[87] | tmp[116])) ^ (tmp[117] | tmp[83] ^ (tmp[31]
				| tmp[134]))));
		tmp[21] = ~tmp[134];
		tmp[162] = ~tmp[117];
		tmp[107] = tmp[131] ^ ((tmp[117] | tmp[134] & (tmp[64] ^ tmp[107])) ^ (tmp[87] ^ ((tmp[126] | tmp[134]) ^ tmp[166]))) ^ (tmp[80] | tmp[115] & (tmp[64] ^ tmp[122] & (tmp[126] & tmp[61])) ^ (tmp[5]
				^ tmp[31] & tmp[134]));
		tmp[22] = tmp[18] ^ (tmp[134] ^ (tmp[64] ^ (tmp[87] | tmp[19] & tmp[116])) ^ (tmp[80] | tmp[126] ^ tmp[12] ^ tmp[61] & (tmp[12] ^ tmp[166]) ^ tmp[115] & (tmp[64] ^ tmp[122] & tmp[22] ^ (tmp[134]
				| tmp[5])))) ^ tmp[162] & ((tmp[87] | tmp[106]) ^ tmp[21] & (tmp[166] ^ (tmp[87] | tmp[166])));
		tmp[119] = tmp[79] ^ (tmp[15] ^ (tmp[54] | tmp[82] ^ tmp[81] & (tmp[119] ^ tmp[82])) ^ tmp[140] & (tmp[1] ^ tmp[138]) ^ (
				tmp[15] ^ tmp[56] & ((tmp[140] | tmp[36]) ^ tmp[33] & tmp[119]) ^ tmp[81] & (tmp[138] ^ tmp[38]) | tmp[103]));
		tmp[15] = tmp[54] ^ tmp[103];
		tmp[79] = tmp[54] & tmp[103];
		tmp[122] = ~tmp[8];
		tmp[5] = tmp[15] & tmp[122];
		tmp[64] = tmp[54] & ~tmp[79];
		tmp[166] = tmp[54] | tmp[103];
		tmp[12] = ~tmp[54];
		tmp[116] = tmp[166] & tmp[12];
		tmp[19] = tmp[8] | tmp[116];
		tmp[12] &= tmp[103];
		tmp[115] = tmp[166] ^ tmp[19];
		tmp[31] = tmp[8] | tmp[166];
		tmp[131] = tmp[79] ^ tmp[31];
		tmp[146] = ~tmp[103];
		tmp[89] = tmp[54] & tmp[146];
		tmp[2] = tmp[122] & tmp[89];
		tmp[30] = tmp[122] & tmp[12];
		tmp[132] = tmp[103] & tmp[122];
		tmp[43] = tmp[103] ^ tmp[132];
		tmp[44] = tmp[18] & tmp[43];
		tmp[44] = tmp[88] & (tmp[47] ^ (tmp[18] | tmp[8])) ^ (tmp[133] ^ (tmp[131] ^ tmp[18] & ~tmp[115])) ^ (tmp[152] | tmp[43] ^ tmp[44] ^ tmp[88] & (tmp[47] ^ tmp[44]));
		tmp[43] = tmp[77] | tmp[44];
		tmp[23] = tmp[46] ^ (tmp[84] ^ tmp[56] & (tmp[28] ^ tmp[81] & (tmp[105] ^ tmp[33] & ~tmp[23])) ^ tmp[140] & ~(tmp[118] ^ tmp[13])) ^ (
				tmp[38] ^ (tmp[36] ^ tmp[56] & (tmp[71] ^ tmp[95] ^ (tmp[140] | tmp[71] ^ tmp[33] & ~tmp[38]))) ^ tmp[81] & tmp[71] | tmp[103]);
		tmp[95] = tmp[8] | tmp[103];
		tmp[122] = tmp[115] ^ (tmp[18] & (tmp[8] | tmp[54]) ^ tmp[68]) ^ (tmp[152] | tmp[64] ^ tmp[79] & tmp[122] ^ tmp[88] & (tmp[31] ^ (tmp[18] | tmp[103]))) ^ tmp[88] & ~(tmp[31] ^ (tmp[18]
				| tmp[79] ^ tmp[30]));
		tmp[68] = tmp[134] & tmp[122];
		tmp[115] = ~tmp[87];
		tmp[105] = tmp[87] | tmp[122];
		tmp[13] = tmp[134] | tmp[122];
		tmp[118] = ~tmp[122];
		tmp[36] = tmp[13] & tmp[118];
		tmp[84] = tmp[134] ^ tmp[122];
		tmp[133] = tmp[87] | tmp[84];
		tmp[118] &= tmp[134];
		tmp[95] = tmp[5] ^ (tmp[29] ^ tmp[64]) ^ tmp[18] & (tmp[54] ^ tmp[19]) ^ tmp[88] & (tmp[31] ^ tmp[18] & ~(tmp[79] ^ tmp[19])) ^ (tmp[152] | tmp[131] ^ tmp[88] & ~(tmp[47]
				^ (tmp[103] ^ tmp[95]) & ~tmp[18]) ^ tmp[18] & (tmp[166] ^ tmp[95]));
		tmp[47] = tmp[26] | tmp[95];
		tmp[19] = tmp[90] | tmp[47];
		tmp[166] = ~tmp[90];
		tmp[29] = tmp[47] & tmp[166];
		tmp[131] = ~tmp[95];
		tmp[31] = tmp[47] & tmp[131];
		tmp[135] = tmp[90] | tmp[31];
		tmp[99] = tmp[95] & tmp[166];
		tmp[76] = tmp[26] & tmp[95];
		tmp[102] = tmp[90] | tmp[95];
		tmp[153] = tmp[95] ^ tmp[99];
		tmp[148] = tmp[86] ^ tmp[95];
		tmp[50] = tmp[86] & tmp[95];
		tmp[11] = ~tmp[86];
		tmp[70] = tmp[95] & tmp[11];
		tmp[92] = tmp[86] & tmp[131];
		tmp[40] = tmp[26] & tmp[131];
		tmp[145] = ~tmp[92];
		tmp[125] = tmp[86] & tmp[145];
		tmp[0] = tmp[166] & tmp[40];
		tmp[16] = tmp[26] ^ tmp[95];
		tmp[41] = tmp[102] ^ tmp[16];
		tmp[34] = tmp[90] | tmp[16];
		tmp[69] = tmp[26] ^ tmp[34];
		tmp[132] &= tmp[18];
		tmp[2] = tmp[109] ^ tmp[12] ^ (tmp[8] | tmp[64]) ^ tmp[18] & ~(tmp[15] ^ tmp[5]) ^ tmp[88] & ~(tmp[79] ^ tmp[18] & (tmp[89] ^ tmp[2]))
				^ (tmp[30] ^ (tmp[15] ^ tmp[132]) ^ tmp[88] & ~(tmp[132] ^ (tmp[116] ^ tmp[2]))) & ~tmp[152];
		tmp[116] = tmp[58] | tmp[2];
		tmp[89] = ~tmp[39];
		tmp[132] = tmp[58] ^ tmp[2];
		tmp[18] = ~tmp[2];
		tmp[5] = tmp[116] & tmp[18];
		tmp[15] = tmp[39] | tmp[116];
		tmp[79] = tmp[39] | tmp[5];
		tmp[64] = tmp[116] & tmp[89];
		tmp[12] = tmp[132] ^ tmp[79];
		tmp[109] = tmp[58] & tmp[2];
		tmp[30] = tmp[39] | tmp[109];
		tmp[25] = tmp[89] & tmp[109];
		tmp[143] = tmp[2] & ~tmp[109];
		tmp[67] = tmp[116] ^ tmp[30];
		tmp[79] = tmp[116] ^ (tmp[154] ^ (tmp[25] ^ tmp[78] & ~(tmp[79] ^ tmp[109]))) ^ (tmp[57] & ~(tmp[25] ^ (tmp[109] ^ tmp[78] & ~(tmp[64] ^ tmp[143]))) ^ (tmp[112]
				| tmp[57] & (tmp[78] & ~(tmp[58] ^ tmp[39]) ^ tmp[132]) ^ (tmp[30] ^ (tmp[109] ^ tmp[78] & (tmp[109] ^ tmp[30])))));
		tmp[25] = tmp[2] & tmp[89];
		tmp[116] = tmp[163] & tmp[79];
		tmp[48] = tmp[58] & tmp[18];
		tmp[89] &= tmp[48];
		tmp[52] = tmp[143] ^ tmp[89];
		tmp[149] ^=
				tmp[109] ^ tmp[78] & tmp[132] ^ (tmp[39] | tmp[143]) ^ tmp[57] & (tmp[132] ^ tmp[78] & ~tmp[15]) ^ (tmp[112] | tmp[67] ^ tmp[78] & ~tmp[52] ^ tmp[57] & ~(tmp[132] ^ tmp[25] ^ tmp[78] & (
						tmp[25] ^ tmp[48])));
		tmp[97] = tmp[39] | tmp[2];
		tmp[89] = tmp[152] ^ tmp[12] ^ tmp[78] & ~(tmp[5] ^ tmp[89]) ^ ((tmp[112] | tmp[109] ^ tmp[78] & tmp[67] ^ tmp[57] & (tmp[97] ^ tmp[78] & (tmp[58] ^ tmp[97]))) ^ tmp[57] & ~(tmp[52] ^ tmp[78] & (
				tmp[132] ^ tmp[89])));
		tmp[67] = ~tmp[22];
		tmp[52] = tmp[89] & tmp[67];
		tmp[5] = tmp[91] ^ tmp[2];
		tmp[152] = ~tmp[91];
		tmp[4] = ~tmp[112];
		tmp[49] = tmp[91] | tmp[2];
		tmp[128] = tmp[91] & tmp[18];
		tmp[42] = tmp[152] & tmp[49];
		tmp[25] = tmp[45] ^ tmp[132] ^ (tmp[30] ^ tmp[78] & (tmp[143] ^ tmp[97])) ^ tmp[57] & ~(tmp[15] ^ (tmp[2] ^ tmp[78] & (tmp[15] ^ tmp[109]))) ^ tmp[4] & (tmp[12] ^ tmp[57] & (tmp[48] ^ tmp[78] & ~(
				tmp[109] ^ tmp[25])) ^ tmp[78] & ~(tmp[58] ^ tmp[64]));
		tmp[109] = tmp[107] & tmp[25];
		tmp[15] = ~tmp[107];
		tmp[64] = tmp[25] & tmp[15];
		tmp[82] =
				((tmp[54] | tmp[28] ^ (tmp[140] | tmp[14])) ^ tmp[33] & tmp[137] ^ tmp[81] & (tmp[38] ^ tmp[33] & tmp[24])) & tmp[146] ^ (tmp[60] ^ (tmp[33] ^ (tmp[38] ^ tmp[56] & (tmp[71] ^ tmp[138] ^ (
						tmp[140] | tmp[38] ^ tmp[82]))) ^ tmp[81] & (tmp[33] ^ tmp[1])));
		tmp[38] = tmp[111] | tmp[82];
		tmp[138] = tmp[141] | tmp[82];
		tmp[14] = tmp[123] | tmp[82];
		tmp[120] = tmp[46] & ~tmp[120] ^ (tmp[27] ^ tmp[100]);
		tmp[100] = tmp[124] & tmp[120];
		tmp[27] = tmp[120] & ~tmp[124];
		tmp[46] = ~tmp[32];
		tmp[56] = tmp[35] & tmp[120];
		tmp[24] = tmp[66] & tmp[120];
		tmp[28] = tmp[24] ^ tmp[46] & (tmp[37] ^ tmp[56]);
		tmp[137] = tmp[66] ^ tmp[56];
		tmp[137] = tmp[35] ^ (tmp[20] ^ tmp[120] & ~tmp[37]) ^ tmp[32] & ~tmp[137] ^ ((tmp[154] | tmp[28] ^ tmp[85] & tmp[28]) ^ tmp[85] & ~(tmp[144] ^ tmp[46] & tmp[137]));
		tmp[18] &= tmp[137];
		tmp[46] = tmp[49] ^ tmp[18];
		tmp[28] = ~tmp[106];
		tmp[20] = tmp[106] & ~(tmp[42] ^ tmp[137]);
		tmp[81] = tmp[128] ^ tmp[137];
		tmp[8] ^= tmp[91] & tmp[2] ^ (tmp[18] ^ tmp[28] & tmp[81]) ^ (tmp[112] | tmp[162] & (tmp[46] ^ (tmp[106] | tmp[46]))) ^ (tmp[117] | tmp[46] ^ (tmp[106] | tmp[49] ^ tmp[137] & ~tmp[5]));
		tmp[60] = ~tmp[8];
		tmp[146] = tmp[89] & tmp[60];
		tmp[48] = tmp[8] ^ tmp[146];
		tmp[97] = tmp[2] & tmp[137];
		tmp[143] = tmp[2] ^ tmp[137];
		tmp[128] ^= tmp[18];
		tmp[128] =
				tmp[5] ^ (tmp[114] ^ (tmp[106] | tmp[128])) ^ (tmp[117] | tmp[128] ^ (tmp[106] | tmp[49] ^ tmp[137])) ^ tmp[4] & (tmp[143] ^ (tmp[106] | tmp[49] ^ tmp[97]) ^ tmp[162] & (tmp[143] ^ tmp[28] & (
						tmp[91] ^ tmp[97])));
		tmp[114] = tmp[18] & tmp[28];
		tmp[143] = tmp[32] ^ (tmp[106] ^ (tmp[162] & (tmp[42] ^ (tmp[137] ^ tmp[20])) ^ tmp[81])) ^ (tmp[112] | tmp[2] ^ (tmp[18] ^ (tmp[114] ^ (tmp[117] | tmp[20] ^ tmp[143]))));
		tmp[20] = ~tmp[143];
		tmp[28] =
				tmp[114] ^ (tmp[1] ^ tmp[5] ^ tmp[49] & tmp[137]) ^ tmp[162] & (tmp[91] ^ (tmp[18] ^ (tmp[106] | tmp[18]))) ^ tmp[4] & (tmp[91] ^ tmp[152] & tmp[97] ^ (tmp[106] | tmp[42] ^ tmp[91] & tmp[137])
						^ (tmp[117] | tmp[5] & tmp[137] ^ tmp[46] & tmp[28]));
		tmp[46] = tmp[120] & ~tmp[35];
		tmp[5] = tmp[72] & tmp[120];
		tmp[24] =
				tmp[32] & ((tmp[35] | tmp[72]) ^ tmp[120] & ~tmp[144]) ^ (tmp[66] ^ (tmp[113] ^ tmp[27])) ^ tmp[85] & ~(tmp[5] ^ tmp[32] & tmp[27]) ^ tmp[151] & (tmp[35] ^ tmp[32] & tmp[139] ^ tmp[85] & (
						tmp[46] ^ tmp[32] & tmp[24]));
		tmp[27] = tmp[24] & ~tmp[148];
		tmp[21] = tmp[122] ^ tmp[122] & tmp[115] ^ ((tmp[36] ^ (tmp[105] ^ tmp[147] & (tmp[63] ^ tmp[21] & tmp[122]))) & tmp[24] ^ tmp[147] & ~(tmp[105] ^ tmp[13]));
		tmp[105] = ~tmp[23];
		tmp[13] = tmp[24] & ~(tmp[13] ^ tmp[115] & tmp[118] ^ tmp[147] & (tmp[63] ^ tmp[13])) ^ (tmp[147] & (tmp[115] | ~tmp[13]) ^ (tmp[87] ^ tmp[84]));
		tmp[63] = tmp[21] & tmp[105] ^ (tmp[120] ^ tmp[13]);
		tmp[21] = tmp[103] ^ (tmp[13] ^ tmp[23] & ~tmp[21]);
		tmp[13] = tmp[115] & tmp[84];
		tmp[118] ^= (tmp[133] ^ (tmp[84] ^ tmp[147] & ~((tmp[87] | tmp[134]) ^ tmp[118]))) & tmp[24] ^ (tmp[13] ^ tmp[147] & (tmp[134] ^ tmp[133]));
		tmp[68] = tmp[122] ^ (tmp[87] ^ ((tmp[84] ^ (tmp[147] & tmp[61] ^ tmp[68] & tmp[115])) & tmp[24] ^ tmp[147] & ~(tmp[134] ^ (tmp[87] | tmp[122] & ~tmp[68]))));
		tmp[108] ^= tmp[68] ^ tmp[23] & tmp[118];
		tmp[118] = tmp[68] ^ (tmp[7] ^ (tmp[23] | tmp[118]));
		tmp[7] = tmp[24] & ~tmp[125];
		tmp[68] = tmp[70] & tmp[24];
		tmp[115] = tmp[92] ^ tmp[68];
		tmp[61] = tmp[148] & tmp[24];
		tmp[84] = tmp[24] & ~tmp[70];
		tmp[131] &= tmp[24];
		tmp[133] = tmp[148] ^ tmp[84];
		tmp[145] &= tmp[24];
		tmp[13] = tmp[131] ^ (tmp[86] | tmp[95]);
		tmp[103] = ~tmp[17];
		tmp[50] = tmp[85] ^ tmp[148] ^ ((tmp[23] | tmp[92] & tmp[24]) ^ tmp[50] & tmp[24]) ^ tmp[9] & (tmp[131] ^ (tmp[50] ^ tmp[105] & (tmp[86] ^ tmp[61]))) ^ (tmp[17] | tmp[92] ^ ((tmp[23] | tmp[13])
				^ tmp[9] & (tmp[92] ^ (tmp[23] | tmp[95] ^ tmp[95] & tmp[24]) ^ tmp[86] & tmp[24])) ^ tmp[24] & (tmp[86] | tmp[70]));
		tmp[36] = tmp[20] & tmp[50];
		tmp[139] = tmp[143] | tmp[50];
		tmp[113] = tmp[79] & tmp[50];
		tmp[151] = tmp[79] & (tmp[50] ^ tmp[139]);
		tmp[61] = tmp[155] ^ tmp[115] ^ tmp[23] & ~tmp[133] ^ tmp[103] & (tmp[13] ^ tmp[9] & (tmp[70] ^ tmp[61])) ^ tmp[9] & ~(tmp[92] ^ tmp[105] & tmp[133]);
		tmp[115] &= tmp[105];
		tmp[11] = tmp[95] ^ (tmp[71] ^ tmp[84]) ^ (tmp[9] & (tmp[145] ^ (tmp[23] | tmp[148] ^ tmp[11] & tmp[24])) ^ (tmp[23] | tmp[125] ^ tmp[68])) ^ (tmp[17] | tmp[115] ^ (tmp[70] ^ tmp[9] & (tmp[7]
				^ tmp[105] & (tmp[95] ^ tmp[24]))));
		tmp[68] = ~tmp[21];
		tmp[131] =
				tmp[101] ^ tmp[145] ^ tmp[23] & (tmp[148] ^ tmp[27]) ^ (tmp[103] & (tmp[27] ^ tmp[9] & (tmp[7] ^ tmp[105] & (tmp[95] ^ tmp[131])) ^ (tmp[23] | tmp[148] ^ tmp[145])) ^ tmp[9] & (tmp[105] | ~(
						tmp[95] ^ tmp[7])));
		tmp[105] = tmp[118] ^ tmp[131];
		tmp[7] = tmp[131] & ~tmp[118];
		tmp[145] = tmp[118] & tmp[131];
		tmp[148] = tmp[118] ^ tmp[7];
		tmp[56] =
				tmp[32] ^ tmp[37] ^ (tmp[93] ^ (tmp[120] ^ tmp[85] & (tmp[32] | ~(tmp[35] & tmp[66] ^ tmp[100])))) ^ (tmp[154] | tmp[120] ^ tmp[85] & (tmp[32] & ~tmp[144] ^ tmp[56]) ^ tmp[32] & ~(tmp[66]
						^ tmp[120] & ~tmp[66]));
		tmp[93] = tmp[77] & tmp[56];
		tmp[37] = ~tmp[56];
		tmp[27] = tmp[44] | tmp[93];
		tmp[103] = ~tmp[44];
		tmp[101] = tmp[93] & tmp[103];
		tmp[125] = tmp[77] & ~tmp[93];
		tmp[70] = tmp[44] | tmp[125];
		tmp[84] = tmp[157] | tmp[56];
		tmp[71] = tmp[56] & ~tmp[69];
		tmp[115] = tmp[56] & tmp[103];
		tmp[133] = tmp[125] ^ tmp[115];
		tmp[92] = ~tmp[157];
		tmp[102] = tmp[26] ^ tmp[99] ^ (tmp[88] ^ tmp[102] & tmp[56]) ^ (
				tmp[17] & ~(tmp[95] ^ tmp[29] ^ tmp[56] & ~(tmp[76] ^ (tmp[90] | tmp[95] & ~tmp[76])) ^ tmp[119] & ~(tmp[19] ^ tmp[37] & (tmp[90] ^ tmp[16]))) ^ tmp[119] & (tmp[41] ^ tmp[56] & (tmp[26]
						^ tmp[102])));
		tmp[88] = ~tmp[102];
		tmp[153] =
				tmp[41] ^ (tmp[35] ^ tmp[56] & ~(tmp[99] ^ tmp[76])) ^ tmp[119] & (tmp[56] | ~(tmp[90] ^ tmp[95])) ^ tmp[17] & (tmp[0] ^ (tmp[47] ^ (tmp[119] & ~(tmp[153] ^ tmp[56] & ~tmp[153]) ^ tmp[56] & ~(
						tmp[16] ^ tmp[166] & tmp[76]))));
		tmp[166] = tmp[77] | tmp[56];
		tmp[47] = tmp[166] & ~tmp[77];
		tmp[103] &= tmp[166];
		tmp[103] ^= tmp[93] ^ (tmp[157] | tmp[56] ^ tmp[101]);
		tmp[34] &= tmp[56];
		tmp[71] ^= tmp[73] ^ (tmp[135] ^ tmp[16]) ^ tmp[119] & ~(tmp[69] & tmp[56] ^ (tmp[135] ^ tmp[40])) ^ tmp[17] & ~(tmp[34] ^ (tmp[0] ^ (tmp[26] ^ tmp[119] & (tmp[90] ^ tmp[71]))));
		tmp[40] = tmp[107] & tmp[71];
		tmp[135] = ~tmp[40];
		tmp[69] = ~tmp[128];
		tmp[0] = tmp[107] & tmp[135];
		tmp[16] = tmp[107] ^ tmp[71];
		tmp[34] = tmp[25] & tmp[40];
		tmp[73] = tmp[25] & tmp[16];
		tmp[41] = tmp[25] & ~tmp[16];
		tmp[13] = tmp[71] ^ (tmp[107] ^ tmp[25]);
		tmp[155] = tmp[15] & tmp[71];
		tmp[42] = tmp[64] & tmp[71];
		tmp[97] = tmp[107] | tmp[71];
		tmp[152] = tmp[25] & ~tmp[97];
		tmp[18] = tmp[44] | tmp[56];
		tmp[49] = tmp[92] & (tmp[43] ^ tmp[56]);
		tmp[115] ^= tmp[56];
		tmp[125] =
				tmp[103] ^ (tmp[54] ^ (tmp[123] | (tmp[157] | tmp[101]) ^ (tmp[125] ^ (tmp[44] | tmp[166])))) ^ tmp[90] & ~(tmp[56] ^ tmp[92] & tmp[115] & ~tmp[123] ^ (tmp[70] ^ (tmp[157] | tmp[133])));
		tmp[101] = tmp[11] | tmp[125];
		tmp[54] = ~tmp[125];
		tmp[1] = tmp[21] | tmp[125];
		tmp[162] = tmp[11] | tmp[1];
		tmp[114] = tmp[11] & tmp[54];
		tmp[4] = tmp[11] & ~tmp[114];
		tmp[81] = tmp[21] | tmp[4];
		tmp[4] ^= tmp[1];
		tmp[12] = tmp[11] & tmp[125];
		tmp[30] = tmp[68] & tmp[12];
		tmp[132] = tmp[11] ^ tmp[125];
		tmp[45] = tmp[21] | tmp[132];
		tmp[10] = tmp[81] ^ tmp[132];
		tmp[136] = tmp[125] & ~tmp[11];
		tmp[75] = tmp[11] | tmp[136];
		tmp[94] = tmp[68] & tmp[125];
		tmp[74] = tmp[125] ^ tmp[94];
		tmp[54] &= tmp[8];
		tmp[6] = ~tmp[54];
		tmp[159] = tmp[8] & tmp[6];
		tmp[6] &= tmp[89];
		tmp[60] &= tmp[125];
		tmp[121] = tmp[89] & ~tmp[60];
		tmp[59] = tmp[8] | tmp[60];
		tmp[110] = tmp[8] | tmp[125];
		tmp[51] = tmp[8] & tmp[125];
		tmp[165] = tmp[22] | tmp[89] ^ tmp[110];
		tmp[168] = tmp[51] ^ tmp[89] & ~tmp[159];
		tmp[48] = tmp[44] ^ (tmp[102] | tmp[67] & tmp[8] ^ (tmp[8] ^ tmp[89] & tmp[51])) ^ (tmp[168] ^ tmp[67] & tmp[125] ^ tmp[21] & ~(tmp[48] ^ (tmp[48] | (tmp[22] | tmp[102])) ^ tmp[22] & ~tmp[110]));
		tmp[51] = tmp[8] ^ tmp[125];
		tmp[168] = tmp[22] ^ (tmp[8] ^ (tmp[125] ^ tmp[121])) ^ tmp[88] & (tmp[168] ^ tmp[67] & tmp[60]);
		tmp[146] = tmp[95] ^ tmp[168] ^ (tmp[21] | tmp[121] ^ tmp[52] & tmp[125] ^ tmp[88] & (tmp[146] ^ tmp[159] ^ (tmp[22] | tmp[146] ^ tmp[60])));
		tmp[6] =
				tmp[122] ^ (tmp[8] ^ ((tmp[102] | tmp[52] ^ tmp[6]) ^ (tmp[22] | tmp[89] ^ tmp[54])) ^ tmp[89] & tmp[59] ^ tmp[21] & ~(tmp[165] ^ (tmp[60] ^ tmp[89] & ~tmp[51]) ^ tmp[88] & (tmp[165] ^ (tmp[6]
						^ tmp[110]))));
		tmp[59] = tmp[2] ^ (tmp[168] ^ tmp[21] & ~(tmp[110] ^ tmp[89] & tmp[51] ^ (tmp[22] | tmp[110]) ^ (tmp[102] | tmp[121] ^ (tmp[159] ^ tmp[67] & (tmp[59] ^ tmp[89] & tmp[8])))));
		tmp[37] &= tmp[77];
		tmp[67] = ~tmp[44];
		tmp[159] = tmp[56] ^ tmp[37] & tmp[67];
		tmp[51] = ~tmp[157];
		tmp[18] ^= (tmp[123] | tmp[49] ^ (tmp[56] ^ tmp[27])) ^ (tmp[77] ^ (tmp[90] & (tmp[115] ^ tmp[157] & tmp[18] ^ (tmp[123] | tmp[27] ^ tmp[84])) ^ (tmp[66] ^ tmp[159] & tmp[51])));
		tmp[115] = tmp[143] | tmp[18];
		tmp[49] = ~tmp[18];
		tmp[121] = tmp[61] & tmp[49];
		tmp[110] = tmp[50] ^ tmp[18];
		tmp[168] = ~tmp[143];
		tmp[2] = tmp[50] & tmp[18];
		tmp[165] = tmp[143] ^ tmp[110];
		tmp[60] = tmp[61] & tmp[18];
		tmp[54] = tmp[168] & tmp[2];
		tmp[52] = ~tmp[79];
		tmp[122] = tmp[50] & ~tmp[2];
		tmp[95] = tmp[18] & ~tmp[50];
		tmp[158] = tmp[50] | tmp[18];
		tmp[161] = tmp[143] | tmp[158];
		tmp[24] ^= tmp[79] ^ (tmp[165] ^ tmp[153] & ~(tmp[79] & tmp[143] ^ tmp[18] & tmp[168])) ^ tmp[63] & (tmp[18] ^ tmp[79] & tmp[54] ^ tmp[153] & (tmp[161] ^ (tmp[113] ^ tmp[158])));
		tmp[127] = tmp[6] | tmp[24];
		tmp[156] = ~tmp[146];
		tmp[160] = tmp[24] & tmp[156];
		tmp[164] = tmp[146] | tmp[24];
		tmp[96] = ~tmp[6];
		tmp[167] = tmp[24] & tmp[96];
		tmp[139] = tmp[110] ^ (tmp[36] ^ tmp[153] & ~(tmp[2] ^ (tmp[139] ^ tmp[151]))) ^ tmp[79] & ~(tmp[158] ^ tmp[161]) ^ (tmp[137] ^ tmp[63] & ~(tmp[151] & tmp[153] ^ (tmp[2] ^ tmp[79] & (tmp[139]
				^ tmp[158]))));
		tmp[151] = tmp[49] & (tmp[50] & tmp[168]);
		tmp[115] = tmp[56] ^ tmp[158] ^ tmp[79] & tmp[115] ^ tmp[168] & tmp[95] ^ tmp[153] & ~(tmp[36] ^ tmp[2] ^ tmp[79] & tmp[151]) ^ tmp[63] & ~(tmp[79] & (tmp[50] ^ tmp[115]) ^ (tmp[2] ^ tmp[161])
				^ tmp[153] & (tmp[151] ^ (tmp[113] ^ tmp[2])));
		tmp[44] = tmp[123] & (tmp[166] ^ (tmp[44] | tmp[47]) ^ (tmp[157] | tmp[159])) ^ (tmp[98] ^ tmp[103]) ^ tmp[90] & ~(tmp[157] ^ (tmp[123] | tmp[84] ^ tmp[166] ^ (tmp[44] | tmp[77] ^ tmp[56])));
		tmp[166] = ~tmp[118];
		tmp[92] = tmp[3] ^ (tmp[70] ^ (tmp[157] ^ tmp[47]) ^ tmp[90] & ~((tmp[123] | tmp[43] ^ tmp[133] & tmp[92]) ^ (tmp[27] ^ tmp[92] & tmp[37]))) ^ (tmp[123] | tmp[93] ^ tmp[51] & (tmp[56]
				^ tmp[56] & tmp[67] & ~tmp[77]));
		tmp[133] = tmp[131] & tmp[166] & tmp[92];
		tmp[37] = ~tmp[83];
		tmp[43] = tmp[118] | tmp[92];
		tmp[67] = tmp[118] & ~tmp[92];
		tmp[27] = tmp[131] & tmp[67];
		tmp[47] = tmp[43] ^ tmp[27];
		tmp[51] = tmp[145] ^ tmp[43];
		tmp[70] = tmp[118] ^ tmp[92];
		tmp[93] = tmp[118] & tmp[92];
		tmp[3] = tmp[131] & tmp[92];
		tmp[84] = tmp[118] & ~tmp[93];
		tmp[159] = tmp[131] & ~tmp[84];
		tmp[56] =
				tmp[140] ^ (tmp[29] ^ (tmp[31] ^ tmp[56] & ~tmp[26]) ^ tmp[17] & (tmp[19] ^ (tmp[56] & ~(tmp[26] ^ (tmp[90] | tmp[76])) ^ tmp[119] & ~(tmp[19] ^ tmp[99] & tmp[56])))) ^ tmp[119] & (tmp[19]
						^ tmp[90] & tmp[26] & tmp[56]);
		tmp[46] =
				tmp[104] ^ (tmp[66] ^ tmp[32] & ~tmp[72] ^ tmp[120] & ~tmp[72] ^ (tmp[154] | tmp[35] ^ tmp[32] & (tmp[72] ^ tmp[120]) ^ tmp[85] & (tmp[144] ^ tmp[32] & ~(tmp[124] ^ tmp[46])))) ^ tmp[85] & (
						tmp[32] & ~(tmp[144] ^ tmp[100]) ^ (tmp[35] ^ tmp[5]));
		tmp[124] = tmp[46] & ~tmp[65];
		tmp[32] = ~tmp[82];
		tmp[142] &= tmp[123] & tmp[46];
		tmp[141] =
				tmp[129] ^ (tmp[53] ^ tmp[138]) ^ tmp[46] & ~tmp[111] ^ tmp[77] & ~(tmp[32] & (tmp[111] ^ tmp[111] & tmp[46])) ^ tmp[78] & ~(tmp[32] & (tmp[123] ^ tmp[58] & tmp[46]) ^ (tmp[62] ^ tmp[77] & ~(
						tmp[142] ^ (tmp[53] ^ tmp[82] & ~tmp[141]))));
		tmp[90] ^= tmp[16] ^ (tmp[42] ^ tmp[118] & ~(tmp[152] ^ tmp[128] & ~tmp[109])) ^ (tmp[128] | tmp[107] ^ tmp[109]) ^ (
				tmp[41] ^ tmp[118] & (tmp[25] & ~tmp[0] ^ (tmp[40] ^ tmp[69] & (tmp[64] ^ tmp[155]))) ^ (tmp[128] | tmp[107] ^ tmp[152]) | tmp[141]);
		tmp[93] ^= tmp[131] ^ (tmp[123] ^ tmp[37] & tmp[47]) ^ (tmp[149] | tmp[83] & ~tmp[51]) ^ ((tmp[149] | tmp[159] ^ ((tmp[83] | tmp[105]) ^ tmp[93])) ^ tmp[37] & tmp[27]) & tmp[141];
		tmp[129] = ~tmp[93];
		tmp[120] = tmp[115] & tmp[129];
		tmp[72] = ~tmp[90];
		tmp[144] = tmp[115] | tmp[93];
		tmp[100] = tmp[115] & tmp[93];
		tmp[85] = tmp[93] & ~tmp[100];
		tmp[35] = tmp[115] ^ tmp[93];
		tmp[3] =
				tmp[9] ^ tmp[51] ^ (~tmp[149] & (tmp[7] ^ (tmp[83] | tmp[133])) ^ ((tmp[83] | tmp[84]) ^ (tmp[118] & tmp[3] ^ tmp[166] & tmp[43] ^ (tmp[149] | (tmp[83] | tmp[148]) ^ tmp[3])) & tmp[141]));
		tmp[166] = tmp[156] & tmp[3];
		tmp[84] = tmp[24] & ~tmp[3];
		tmp[51] = tmp[3] | tmp[84];
		tmp[9] = tmp[24] & tmp[3];
		tmp[66] = tmp[24] & tmp[166];
		tmp[5] = ~tmp[24];
		tmp[154] = tmp[3] & tmp[5];
		tmp[104] = tmp[156] & tmp[154];
		tmp[99] = tmp[146] ^ tmp[154];
		tmp[76] = tmp[3] & ~tmp[154];
		tmp[19] = tmp[24] | tmp[3];
		tmp[31] = tmp[24] ^ tmp[3];
		tmp[29] = tmp[156] & tmp[31];
		tmp[159] =
				tmp[7] ^ (tmp[149] | tmp[83] & ~tmp[148]) ^ (tmp[80] ^ (tmp[92] ^ (tmp[83] | tmp[27] ^ tmp[70]))) ^ tmp[141] & ~(tmp[118] ^ (tmp[83] & ~tmp[149] & ~tmp[145] ^ (tmp[159] ^ tmp[37] & (tmp[43]
						^ tmp[159]))));
		tmp[148] = ~tmp[141];
		tmp[41] = tmp[117] ^ (tmp[97] ^ (tmp[25] ^ (tmp[118] & ~(tmp[107] ^ tmp[41] ^ (tmp[128] | tmp[25] ^ tmp[40])) ^ tmp[69] & (tmp[40] ^ tmp[25] & tmp[135])))
				^ (tmp[152] ^ tmp[118] & ~(tmp[0] ^ tmp[34] ^ tmp[69] & tmp[13]) ^ (tmp[128] | tmp[64] ^ tmp[71])) & tmp[148]);
		tmp[135] = ~tmp[41];
		tmp[64] = tmp[159] & tmp[135];
		tmp[117] = tmp[139] | tmp[41];
		tmp[80] = tmp[59] ^ tmp[41];
		tmp[7] = tmp[159] | tmp[41];
		tmp[140] = tmp[135] & tmp[7];
		tmp[103] = tmp[159] & tmp[41];
		tmp[98] = tmp[41] & ~tmp[103];
		tmp[151] = tmp[41] & ~tmp[159];
		tmp[161] = tmp[159] ^ tmp[41];
		tmp[155] =
				(tmp[73] ^ (tmp[71] ^ (tmp[118] & ~(tmp[73] ^ (tmp[16] ^ tmp[69] & (tmp[109] ^ tmp[155]))) ^ tmp[69] & (tmp[109] ^ tmp[97])))) & tmp[148] ^ (tmp[57] ^ (tmp[13] ^ tmp[118] & (tmp[152] ^ (
						tmp[25] | tmp[128])) ^ tmp[128] & ~(tmp[71] ^ tmp[109] & ~tmp[71])));
		tmp[97] = tmp[147] ^ (tmp[0] ^ (tmp[118] & ~(tmp[107] ^ tmp[69] & tmp[97]) ^ tmp[25] & ~(tmp[15] & tmp[97])) ^ (tmp[128] | tmp[25] & tmp[71])) ^ (
				tmp[34] ^ (tmp[40] ^ (tmp[69] & (tmp[40] ^ tmp[42]) ^ tmp[118] & (tmp[40] ^ tmp[71] & tmp[69]))) | tmp[141]);
		tmp[43] = tmp[112] ^ tmp[105] ^ (tmp[83] | tmp[92] ^ tmp[133]) ^ ((tmp[149] | tmp[37] & (tmp[118] ^ tmp[131] & tmp[70])) ^ tmp[141] & ~(tmp[47] ^ (tmp[83] & (tmp[67] ^ tmp[27]) ^ (tmp[149]
				| (tmp[118] ^ tmp[145]) & ~tmp[83] ^ (tmp[43] ^ tmp[131] & ~tmp[43])))));
		tmp[145] = tmp[155] & tmp[43];
		tmp[27] = tmp[43] & ~tmp[145];
		tmp[67] = ~tmp[139];
		tmp[70] = tmp[59] ^ tmp[43];
		tmp[47] = tmp[41] | tmp[43];
		tmp[133] = tmp[43] & ~tmp[59];
		tmp[105] = tmp[59] | tmp[43];
		tmp[112] = tmp[43] & ~tmp[133];
		tmp[69] = ~tmp[43];
		tmp[15] = tmp[41] | tmp[112];
		tmp[40] = tmp[155] ^ tmp[43];
		tmp[42] = tmp[155] | tmp[43];
		tmp[0] = tmp[155] & tmp[69];
		tmp[34] = tmp[69] & tmp[42];
		tmp[69] &= tmp[59];
		tmp[147] = tmp[15] ^ tmp[69];
		tmp[109] = tmp[43] | tmp[69];
		tmp[16] = tmp[109] ^ tmp[135] & tmp[43];
		tmp[109] &= tmp[135];
		tmp[69] &= tmp[135];
		tmp[73] = tmp[59] ^ tmp[109];
		tmp[152] = tmp[82] | tmp[58] ^ tmp[142];
		tmp[13] = ~tmp[131];
		tmp[14] =
				tmp[55] ^ (tmp[58] ^ tmp[82]) ^ (tmp[123] & tmp[46] ^ tmp[77] & ~(tmp[124] ^ (tmp[123] ^ tmp[152]))) ^ tmp[78] & (tmp[14] ^ tmp[46] ^ tmp[77] & ~(tmp[58] ^ (tmp[14] ^ tmp[46] & ~tmp[58])));
		tmp[55] = tmp[8] ^ tmp[14];
		tmp[57] = tmp[13] & tmp[14];
		tmp[148] = tmp[131] | tmp[55];
		tmp[110] = tmp[131] | tmp[14];
		tmp[137] = tmp[8] & ~tmp[14];
		tmp[169] = tmp[8] & tmp[14];
		tmp[170] = tmp[14] ^ tmp[13] & tmp[169];
		tmp[171] = ~tmp[102];
		tmp[172] = tmp[14] & ~tmp[8];
		tmp[173] = tmp[110] ^ tmp[172];
		tmp[174] = tmp[102] | tmp[173];
		tmp[13] &= tmp[172];
		tmp[175] = tmp[137] ^ tmp[13];
		tmp[176] = tmp[131] | tmp[172];
		tmp[177] = tmp[14] & ~tmp[172];
		tmp[178] = tmp[57] ^ tmp[177];
		tmp[137] =
				tmp[26] ^ tmp[173] ^ ((tmp[102] | tmp[175]) ^ (tmp[83] | tmp[170] ^ (tmp[102] | tmp[148] ^ tmp[137]))) ^ ~tmp[108] & (tmp[178] ^ (tmp[83] | tmp[178] ^ tmp[171] & (tmp[148] ^ tmp[177])) ^ (
						tmp[102] | tmp[178]));
		tmp[178] = tmp[72] & tmp[137];
		tmp[26] = tmp[131] ^ tmp[172];
		tmp[26] = tmp[91] ^ (tmp[175] ^ (tmp[102] | tmp[172] ^ tmp[13]) ^ (tmp[83] | tmp[102] & (tmp[55] ^ tmp[148])) ^ (tmp[108] | tmp[26] ^ tmp[37] & (tmp[26] ^ tmp[171] & tmp[26])));
		tmp[88] =
				tmp[134] ^ (tmp[8] ^ tmp[176]) ^ ((tmp[108] | tmp[131] ^ tmp[102] & tmp[169] ^ (tmp[83] | tmp[131] & tmp[88] ^ tmp[173])) ^ tmp[171] & (tmp[131] | tmp[177])) ^ tmp[37] & (tmp[170] ^ (tmp[102]
						| tmp[172] ^ tmp[176]));
		tmp[110] =
				(tmp[108] | tmp[173] ^ tmp[37] & (tmp[8] & ~tmp[131] ^ tmp[174]) ^ tmp[102] & tmp[173]) ^ (tmp[55] ^ (tmp[57] ^ (tmp[78] ^ tmp[174])) ^ tmp[37] & (tmp[171] & (tmp[55] ^ tmp[110]) ^ (tmp[14]
						| (tmp[8] | tmp[131]))));
		tmp[55] = tmp[150] & tmp[46];
		tmp[158] =
				tmp[165] ^ (tmp[46] ^ tmp[52] & (tmp[50] ^ tmp[54])) ^ (tmp[63] & ((tmp[143] | tmp[122]) ^ (tmp[79] & tmp[20] ^ tmp[153] & (tmp[95] ^ (tmp[79] & tmp[36] ^ tmp[54])))) ^ tmp[153] & ~(tmp[2]
						^ tmp[79] & ~(tmp[122] ^ tmp[168] & tmp[158])));
		tmp[55] = tmp[130] ^ (tmp[123] ^ (tmp[82] | tmp[53] & tmp[46])) ^ tmp[77] & ~(tmp[55] ^ (tmp[62] ^ tmp[65] & ~tmp[82])) ^ tmp[78] & ~((tmp[65] | tmp[82]) ^ tmp[46] ^ tmp[77] & (tmp[150] ^ (tmp[38]
				^ tmp[55])));
		tmp[123] = tmp[121] & tmp[55];
		tmp[130] = ~tmp[107];
		tmp[168] = tmp[18] ^ tmp[55];
		tmp[36] = tmp[55] ^ (tmp[18] ^ tmp[123]);
		tmp[122] = tmp[18] & tmp[55];
		tmp[54] = tmp[18] | tmp[55];
		tmp[49] &= tmp[54];
		tmp[95] = tmp[61] & ~tmp[49];
		tmp[49] = tmp[36] ^ (tmp[107] | tmp[121] ^ tmp[49]) ^ tmp[143] & (tmp[130] & tmp[122] ^ (tmp[55] ^ tmp[95]));
		tmp[36] = tmp[54] ^ (tmp[107] | tmp[123] ^ tmp[122]) ^ (tmp[95] ^ tmp[143] & ~(tmp[36] ^ tmp[130] & (tmp[60] ^ tmp[122])));
		tmp[121] = tmp[95] ^ (tmp[143] & ~(tmp[55] ^ tmp[61] & tmp[55] ^ tmp[130] & (tmp[121] ^ tmp[55])) ^ (tmp[107] | tmp[122] ^ tmp[61] & ~tmp[122]));
		tmp[17] ^= tmp[49] ^ tmp[25] & ~tmp[121];
		tmp[122] = ~tmp[17];
		tmp[95] = tmp[137] & tmp[122];
		tmp[123] = tmp[90] | tmp[17];
		tmp[20] = tmp[90] & tmp[17];
		tmp[2] = tmp[17] & (tmp[90] & tmp[137]);
		tmp[165] = tmp[20] ^ tmp[2];
		tmp[174] = tmp[90] ^ tmp[17];
		tmp[171] = tmp[137] & tmp[17];
		tmp[37] = tmp[72] & tmp[17];
		tmp[57] = tmp[72] & tmp[171];
		tmp[173] = tmp[37] ^ tmp[57];
		tmp[122] &= tmp[90];
		tmp[169] = tmp[90] & ~tmp[122];
		tmp[176] = tmp[137] & ~tmp[169];
		tmp[172] = tmp[122] ^ tmp[176];
		tmp[177] = tmp[20] ^ tmp[137] & ~tmp[169];
		tmp[170] = ~tmp[25];
		tmp[121] = tmp[157] ^ tmp[49] ^ tmp[121] & tmp[170];
		tmp[168] = ~tmp[55] & (tmp[61] ^ tmp[18]) ^ tmp[130] & (tmp[61] & tmp[54]) ^ tmp[143] & ~(tmp[168] ^ ((tmp[107] | tmp[60] ^ tmp[168]) ^ tmp[61] & ~tmp[168]));
		tmp[126] ^= tmp[36] ^ tmp[25] & ~tmp[168];
		tmp[60] = tmp[41] & tmp[126];
		tmp[168] = tmp[58] ^ tmp[36] ^ tmp[170] & tmp[168];
		tmp[170] = ~tmp[158];
		tmp[36] = tmp[168] & tmp[170];
		tmp[38] = tmp[33] ^ tmp[62] ^ tmp[46] & ~tmp[62] ^ (tmp[78] & (tmp[111] ^ tmp[138] ^ tmp[77] & (tmp[65] ^ tmp[38] ^ tmp[124])) ^ tmp[32] & (tmp[53] ^ tmp[142])) ^ tmp[77] & ~(tmp[58] ^ tmp[152]
				^ tmp[46] & ~tmp[150]);
		tmp[65] = ~tmp[38];
		return shuffle2_2(tmp, vector);
	}

	private static byte[] shuffle2_2(int[] tmp, int vector[]) {
		tmp[124] = tmp[79] | tmp[38];
		tmp[12] =
				tmp[23] ^ tmp[45] ^ (tmp[132] ^ tmp[45]) & tmp[38] ^ (tmp[28] & ~(tmp[21] ^ tmp[12] ^ (tmp[11] ^ (tmp[21] | tmp[114])) & tmp[65] ^ tmp[56] & (tmp[1] ^ tmp[38] & ~tmp[4])) ^ tmp[56] & ~(tmp[81]
						^ tmp[12] ^ tmp[162] & tmp[65]));
		tmp[94] = tmp[82] ^ (tmp[132] ^ (tmp[21] | tmp[11]) ^ ((tmp[114] ^ tmp[11] & tmp[68]) & tmp[65] ^ (tmp[56] & (tmp[75] ^ (tmp[21] | tmp[136]) ^ (tmp[125] ^ tmp[1] | tmp[38])) ^ tmp[28] & (tmp[74]
				^ tmp[56] & ~(tmp[74] ^ (tmp[101] ^ tmp[94] | tmp[38])) ^ (tmp[101] ^ tmp[68] & tmp[75] | tmp[38])))));
		tmp[74] = tmp[168] & tmp[94];
		tmp[82] = tmp[168] ^ tmp[94];
		tmp[81] = tmp[36] ^ tmp[94];
		tmp[23] = tmp[158] ^ tmp[94];
		tmp[150] = tmp[168] & ~tmp[23];
		tmp[138] = tmp[168] & tmp[23];
		tmp[111] = tmp[94] ^ tmp[150];
		tmp[46] = tmp[129] & tmp[111];
		tmp[36] ^= tmp[23];
		tmp[152] = ~tmp[94];
		tmp[58] = tmp[158] & tmp[168];
		tmp[142] = tmp[152] & tmp[58];
		tmp[58] &= tmp[94];
		tmp[53] = tmp[58] ^ tmp[158] & ~(tmp[158] & tmp[152]);
		tmp[62] = tmp[129] & tmp[94];
		tmp[170] &= tmp[94];
		tmp[32] = tmp[79] ^ tmp[38];
		tmp[78] = tmp[163] & tmp[32];
		tmp[101] = tmp[10] ^ (tmp[119] ^ tmp[38] & ~tmp[30]) ^ tmp[56] & ~(tmp[45] ^ (tmp[10] | tmp[38])) ^ tmp[28] & (tmp[75] ^ tmp[56] & ~(tmp[21] ^ tmp[75] ^ (tmp[101] ^ tmp[1]) & tmp[65])
				^ (tmp[11] ^ tmp[30]) & tmp[65]);
		tmp[2] ^= tmp[37] ^ tmp[71] ^ tmp[115] & ~(tmp[17] ^ tmp[171]) ^ (tmp[146] | tmp[176]) ^ (tmp[101] | tmp[17] ^ tmp[57] ^ (tmp[115] & ~(tmp[122] ^ tmp[156] & tmp[122] ^ tmp[137] & ~tmp[123])
				^ tmp[156] & (tmp[178] ^ tmp[122])));
		tmp[20] = tmp[56] ^ tmp[123] ^ (tmp[115] & ~(tmp[169] ^ tmp[137] & tmp[174] ^ tmp[156] & tmp[165]) ^ (
				(tmp[101] | tmp[172] ^ tmp[115] & (tmp[178] ^ tmp[156] & tmp[20]) ^ tmp[156] & (tmp[90] ^ tmp[137] & tmp[122])) ^ tmp[156] & tmp[177]) ^ tmp[137] & ~tmp[174]);
		tmp[37] = tmp[153] ^ tmp[174] ^ (tmp[146] | tmp[95]) ^ (tmp[176] ^ (tmp[101] | tmp[115] & (tmp[174] ^ (tmp[146] | tmp[37])))) ^ tmp[115] & ~(tmp[171] ^ (tmp[90] ^ (tmp[146] | tmp[172])));
		tmp[174] ^=
				(tmp[146] | tmp[173]) ^ (tmp[137] ^ tmp[102]) ^ (tmp[101] | tmp[146] & ~tmp[165] ^ tmp[115] & ~(tmp[95] ^ tmp[123] ^ tmp[156] & tmp[174])) ^ tmp[115] & (tmp[177] ^ tmp[146] & tmp[173]);
		tmp[123] = tmp[79] & tmp[65];
		tmp[95] = tmp[116] ^ tmp[123];
		tmp[165] = ~tmp[123];
		tmp[102] = tmp[79] & tmp[165];
		tmp[52] &= tmp[38];
		tmp[173] = tmp[163] & tmp[52];
		tmp[177] = tmp[79] | tmp[52];
		tmp[172] = tmp[163] & tmp[177];
		tmp[95] = tmp[163] & ~tmp[79] ^ (tmp[87] ^ tmp[102]) ^ tmp[50] & ~tmp[95] ^ (tmp[44] | tmp[50] & tmp[123] ^ (tmp[124] ^ (tmp[56] | (tmp[50] | tmp[124])))) ^ (tmp[56]
				| tmp[79] ^ tmp[172] ^ tmp[50] & tmp[95]);
		tmp[87] = tmp[96] & tmp[95];
		tmp[171] = tmp[64] & tmp[95];
		tmp[5] &= tmp[95];
		tmp[153] = tmp[103] & tmp[95];
		tmp[176] = tmp[87] ^ tmp[5];
		tmp[122] = tmp[135] & tmp[95];
		tmp[178] = tmp[24] & ~tmp[95];
		tmp[169] = tmp[24] & ~tmp[178];
		tmp[71] = tmp[127] ^ tmp[169];
		tmp[57] = ~tmp[12];
		tmp[10] = tmp[95] ^ (tmp[6] | tmp[178]);
		tmp[178] ^= tmp[6];
		tmp[30] = tmp[7] ^ tmp[122];
		tmp[119] = tmp[41] & tmp[95];
		tmp[33] = tmp[24] | tmp[95];
		tmp[54] = tmp[167] ^ tmp[95];
		tmp[5] = tmp[33] ^ tmp[96] & tmp[5];
		tmp[130] = tmp[167] ^ tmp[33];
		tmp[33] |= tmp[6];
		tmp[49] = tmp[24] ^ tmp[87];
		tmp[157] = tmp[95] & ~tmp[7];
		tmp[134] = tmp[24] ^ tmp[95];
		tmp[96] &= tmp[134];
		tmp[87] ^= tmp[134];
		tmp[148] = tmp[134] ^ tmp[96];
		tmp[169] = tmp[134] ^ (tmp[6] | tmp[169]);
		tmp[96] ^= tmp[24];
		tmp[134] ^= tmp[167];
		tmp[118] ^= tmp[54] ^ (tmp[12] | tmp[169]) ^ tmp[88] & ~(tmp[33] ^ (tmp[127] | tmp[12])) ^ (tmp[97] | tmp[148] ^ tmp[57] & tmp[96] ^ tmp[88] & ~(tmp[49] ^ tmp[12] & ~tmp[130]));
		tmp[130] = tmp[108] ^ (tmp[54] ^ tmp[12] & tmp[169] ^ tmp[88] & (tmp[33] ^ tmp[12] & ~tmp[127]) ^ (tmp[97] | tmp[148] ^ tmp[12] & tmp[96] ^ tmp[88] & (tmp[49] ^ (tmp[12] | tmp[130]))));
		tmp[57] = tmp[134] ^ tmp[57] & tmp[10] ^ tmp[88] & ~(tmp[5] ^ tmp[57] & tmp[178]) ^ (tmp[63] ^ ~tmp[97] & (tmp[49] ^ (tmp[12] | tmp[176]) ^ tmp[88] & (tmp[87] ^ tmp[71] & tmp[57])));
		tmp[63] = tmp[37] | tmp[57];
		tmp[178] = tmp[21] ^ ((tmp[97] | tmp[49] ^ tmp[12] & ~tmp[176] ^ tmp[88] & (tmp[87] ^ tmp[12] & tmp[71])) ^ (tmp[88] & ~(tmp[5] ^ tmp[12] & tmp[178]) ^ (tmp[12] & ~tmp[10] ^ tmp[134])));
		tmp[71] = ~tmp[178];
		tmp[176] = tmp[20] & tmp[71];
		tmp[10] = tmp[20] & tmp[178];
		tmp[103] = tmp[7] ^ tmp[95] & ~tmp[103];
		tmp[5] = ~tmp[50];
		tmp[165] = tmp[172] ^ tmp[50] & (tmp[78] ^ tmp[177]) ^ (tmp[56] | tmp[102] ^ tmp[50] & (tmp[163] & tmp[165] ^ tmp[177])) ^ (tmp[86] ^ (tmp[44] | ~tmp[56] & (tmp[113] ^ tmp[123]) ^ (tmp[38]
				^ tmp[50] & tmp[52])));
		tmp[113] = tmp[165] & ~tmp[160];
		tmp[172] = tmp[51] & tmp[165];
		tmp[61] ^= tmp[9] ^ (tmp[146] | tmp[154]) ^ tmp[172] ^ (tmp[12] | tmp[3] ^ tmp[104] ^ (tmp[66] ^ tmp[154]) & tmp[165]) ^ tmp[17] & (tmp[51] ^ tmp[104] ^ tmp[165] & ~(tmp[166] ^ tmp[19]));
		tmp[86] = ~tmp[57];
		tmp[29] =
				tmp[50] ^ (tmp[31] ^ (tmp[146] | tmp[76])) ^ tmp[165] & ~tmp[99] ^ ((tmp[12] | tmp[29] ^ (tmp[164] ^ tmp[19]) & tmp[165] ^ tmp[17] & (tmp[84] ^ tmp[104] ^ tmp[165] & ~tmp[146])) ^ tmp[17] & ~(
						tmp[160] ^ tmp[31] ^ (tmp[84] ^ tmp[29]) & tmp[165]));
		tmp[164] =
				tmp[11] ^ tmp[99] ^ (tmp[164] ^ tmp[76]) & ~tmp[165] ^ ((tmp[3] ^ tmp[172] ^ tmp[17] & (tmp[154] ^ (tmp[146] | tmp[31]) ^ tmp[165] & ~(tmp[24] ^ tmp[164]))) & ~tmp[12] ^ tmp[17] & ~(tmp[146]
						^ tmp[84] ^ (tmp[84] ^ (tmp[146] | tmp[84])) & tmp[165]));
		tmp[51] = tmp[131] ^ (tmp[24] ^ tmp[166] ^ (tmp[113] ^ ((tmp[12] | (tmp[9] ^ tmp[66]) & ~tmp[165] ^ tmp[17] & ~(tmp[31] ^ tmp[156] & tmp[51] ^ tmp[113])) ^ tmp[17] & ~(tmp[160] ^ tmp[84]
				^ (tmp[9] ^ tmp[156] & tmp[84]) & tmp[165]))));
		tmp[156] = tmp[118] & tmp[51];
		tmp[84] = tmp[51] & ~tmp[118];
		tmp[31] = ~tmp[174];
		tmp[9] = tmp[118] | tmp[84];
		tmp[113] = tmp[174] | tmp[51];
		tmp[160] = tmp[51] & tmp[31];
		tmp[66] = tmp[51] & ~tmp[160];
		tmp[166] = tmp[174] ^ tmp[51];
		tmp[131] = tmp[174] & tmp[51];
		tmp[154] = ~tmp[51];
		tmp[172] = tmp[174] & tmp[154];
		tmp[76] = tmp[118] | tmp[51];
		tmp[99] = tmp[118] ^ tmp[51];
		tmp[104] = tmp[118] & tmp[154];
		tmp[102] =
				tmp[39] ^ ((tmp[44] | (tmp[56] | tmp[38] ^ tmp[78] ^ tmp[50] & ~(tmp[163] ^ tmp[102])) ^ (tmp[50] ^ (tmp[38] ^ tmp[163] & tmp[38]))) ^ (tmp[163] ^ tmp[32] ^ tmp[50] & ~(tmp[124] ^ tmp[173])))
						^ (tmp[56] | tmp[123] ^ tmp[116] & tmp[38] ^ tmp[50] & (tmp[32] ^ tmp[163] & ~tmp[52]));
		tmp[78] = tmp[102] & ~tmp[40];
		tmp[124] = tmp[102] & ~tmp[43];
		tmp[39] = tmp[43] & tmp[102];
		tmp[19] = tmp[155] & tmp[102];
		tmp[87] = tmp[40] & tmp[102];
		tmp[49] = ~tmp[59];
		tmp[134] = tmp[145] ^ tmp[0] & tmp[102];
		tmp[127] = tmp[42] ^ tmp[19];
		tmp[96] = tmp[0] ^ tmp[102] & ~tmp[42];
		tmp[148] = ~tmp[110];
		tmp[79] ^=
				tmp[96] ^ (tmp[59] | tmp[27] ^ tmp[102] & ~tmp[155]) ^ tmp[168] & ~(tmp[155] ^ tmp[19]) ^ tmp[148] & (tmp[102] & ~tmp[34] ^ tmp[168] & tmp[127] ^ tmp[49] & (tmp[124] ^ tmp[168] & tmp[87]));
		tmp[33] = ~tmp[29];
		tmp[42] ^=
				tmp[149] ^ (tmp[102] ^ tmp[168] & ~tmp[78]) ^ (tmp[59] | tmp[155] ^ tmp[168] & ~tmp[127]) ^ tmp[148] & (tmp[27] ^ tmp[39] ^ tmp[168] & (tmp[0] ^ tmp[78]) ^ (tmp[59] | tmp[96] ^ (tmp[168]
						| tmp[42] ^ tmp[78])));
		tmp[40] ^= tmp[39];
		tmp[78] =
				tmp[25] ^ (tmp[168] & ~tmp[27] ^ tmp[40] ^ (tmp[59] | tmp[27] ^ tmp[145] & tmp[102] ^ tmp[168] & ~(tmp[155] ^ tmp[102]))) ^ (tmp[110] | tmp[34] ^ tmp[124] ^ tmp[168] & ~(tmp[155] ^ tmp[78])
						^ tmp[49] & (tmp[155] ^ tmp[168] & ~(tmp[145] ^ tmp[78]) ^ tmp[102] & ~tmp[27]));
		tmp[87] =
				tmp[89] ^ (tmp[27] ^ (tmp[168] & ~tmp[34] ^ tmp[124])) ^ (tmp[59] | tmp[39] ^ (tmp[155] ^ tmp[168] & tmp[40])) ^ (tmp[110] | tmp[134] ^ tmp[49] & (tmp[134] ^ tmp[168] & ~(tmp[155] ^ tmp[87]))
						^ tmp[168] & (tmp[145] ^ tmp[19]));
		tmp[177] = tmp[77] ^ (tmp[32] ^ tmp[163] & tmp[123]) ^ tmp[50] & ~tmp[173] ^ ((tmp[44] | tmp[52] ^ (tmp[116] ^ tmp[177]) & tmp[5] ^ tmp[52] & tmp[5] & ~tmp[56]) ^ (tmp[56] | tmp[173] & tmp[5]));
		tmp[116] = tmp[120] & tmp[177];
		tmp[5] = tmp[177] & ~tmp[115];
		tmp[52] = tmp[115] ^ tmp[5];
		tmp[123] = ~tmp[90];
		tmp[173] = tmp[177] & ~(tmp[129] & tmp[144]);
		tmp[32] = tmp[100] & tmp[177];
		tmp[50] = ~tmp[121];
		tmp[77] = tmp[144] & tmp[177];
		tmp[34] = tmp[93] & ~tmp[115] & tmp[177];
		tmp[62] = tmp[14] ^ (tmp[82] ^ tmp[129] & tmp[158]) ^ ((tmp[110] | tmp[23] ^ tmp[62] ^ tmp[177] & ~(tmp[81] ^ tmp[62])) ^ tmp[177] & ~(tmp[138] ^ (tmp[93] | tmp[158] ^ tmp[142])));
		tmp[14] = tmp[174] & tmp[62];
		tmp[134] = tmp[131] ^ tmp[14];
		tmp[40] = tmp[174] ^ tmp[166] & tmp[62];
		tmp[19] = tmp[160] & tmp[62];
		tmp[145] = tmp[172] & tmp[62];
		tmp[49] = tmp[31] & tmp[62];
		tmp[124] = tmp[51] ^ tmp[49];
		tmp[39] = tmp[51] & tmp[62];
		tmp[27] = tmp[62] & ~tmp[172];
		tmp[89] = tmp[93] ^ tmp[177] & ~tmp[35];
		tmp[25] = tmp[177] & ~tmp[93];
		tmp[111] =
				tmp[141] ^ (tmp[158] ^ tmp[168] & tmp[152] ^ (tmp[93] | tmp[36])) ^ ((tmp[110] | tmp[53] ^ tmp[129] & tmp[82] ^ (tmp[111] ^ tmp[93] & tmp[81]) & tmp[177]) ^ tmp[177] & ~(tmp[129] & (tmp[170]
						^ tmp[168] & tmp[170])));
		tmp[152] = ~tmp[111];
		tmp[141] = tmp[84] & tmp[152];
		tmp[0] = tmp[84] | tmp[111];
		tmp[127] = tmp[156] ^ (tmp[99] | tmp[111]);
		tmp[96] = tmp[156] & tmp[152];
		tmp[149] = tmp[2] | tmp[111];
		tmp[169] = tmp[51] | tmp[111];
		tmp[54] = tmp[118] ^ tmp[96];
		tmp[108] = tmp[84] ^ tmp[141];
		tmp[167] = tmp[76] ^ tmp[169];
		tmp[13] = ~tmp[48];
		tmp[100] ^=
				tmp[125] ^ (tmp[177] ^ tmp[52] & tmp[123]) ^ ((tmp[121] | tmp[35] ^ tmp[32] ^ (tmp[90] | tmp[52])) ^ tmp[13] & (tmp[50] & (tmp[72] & tmp[100] ^ tmp[32]) ^ (tmp[85] ^ tmp[34]) ^ (tmp[90]
						| tmp[5])));
		tmp[32] = tmp[87] & tmp[100];
		tmp[52] = tmp[93] ^ tmp[35] & tmp[177];
		tmp[85] ^=
				(tmp[121] | tmp[90] & ~tmp[85] ^ tmp[52]) ^ (tmp[18] ^ (tmp[173] ^ (tmp[90] | tmp[115] ^ tmp[116]))) ^ tmp[13] & (tmp[115] ^ tmp[90] & (tmp[93] ^ tmp[116]) ^ tmp[50] & ((tmp[115] | tmp[90])
						^ tmp[52]));
		tmp[72] =
				tmp[35] ^ (tmp[92] ^ tmp[173]) ^ (tmp[90] | tmp[89]) ^ (tmp[121] | tmp[116] ^ tmp[123] & (tmp[120] ^ tmp[34])) ^ (tmp[48] | tmp[5] ^ (tmp[50] & (tmp[116] ^ (tmp[120] ^ tmp[120] & tmp[72]))
						^ tmp[123] & tmp[89]));
		tmp[50] &= tmp[77];
		tmp[25] ^= tmp[44] ^ tmp[35] ^ ((tmp[121] | tmp[90] & tmp[144] ^ tmp[77]) ^ (tmp[90] | tmp[116])) ^ (tmp[48] | tmp[50] ^ (tmp[77] ^ tmp[90] & ~(tmp[144] ^ tmp[25])));
		tmp[144] = tmp[29] | tmp[25];
		tmp[77] = ~tmp[25];
		tmp[116] = tmp[29] & tmp[77];
		tmp[150] = tmp[38] ^ (tmp[74] ^ (tmp[94] ^ (tmp[158] ^ tmp[129] & (tmp[94] ^ tmp[168] & ~(tmp[158] | tmp[94]))))) ^ (
				tmp[148] & (tmp[74] ^ (tmp[93] | tmp[82]) ^ tmp[177] & ~(tmp[81] ^ (tmp[93] | tmp[23] ^ tmp[150]))) ^ tmp[177] & ~(tmp[150] ^ tmp[129] & tmp[142]));
		tmp[74] =
				tmp[148] & (tmp[74] ^ (tmp[158] ^ tmp[129] & (tmp[94] ^ tmp[74])) ^ (tmp[46] ^ tmp[36]) & tmp[177]) ^ (tmp[55] ^ (tmp[58] ^ tmp[170] ^ (tmp[93] | tmp[53])) ^ tmp[177] & ~(tmp[138] ^ (tmp[23]
						^ tmp[46])));
		tmp[38] = tmp[21] ^ tmp[125] ^ tmp[56] & ~(tmp[132] ^ tmp[68] & tmp[114] ^ (tmp[11] ^ tmp[1]) & tmp[65]) ^ (tmp[106] ^ tmp[28] & ~(tmp[45] ^ tmp[56] & ~(tmp[4] ^ (tmp[1] | tmp[38])) ^ (
				tmp[162] ^ tmp[136] | tmp[38]))) ^ (tmp[162] ^ tmp[75] | tmp[38]);
		tmp[15] =
				tmp[28] ^ (tmp[80] ^ tmp[26] & (tmp[15] ^ (tmp[59] ^ (tmp[139] | tmp[16]))) ^ tmp[139] & tmp[70]) ^ tmp[38] & ~(tmp[69] ^ (tmp[59] ^ tmp[67] & (tmp[70] ^ (tmp[41] | tmp[105]))) ^ tmp[26] & (
						tmp[133] ^ tmp[43] & tmp[67] ^ tmp[135] & tmp[133]));
		tmp[28] = ~tmp[15];
		tmp[1] = tmp[20] & tmp[28];
		tmp[4] = tmp[178] ^ tmp[15];
		tmp[136] = tmp[1] ^ tmp[4];
		tmp[162] = tmp[20] & tmp[4];
		tmp[56] = tmp[178] | tmp[15];
		tmp[11] = tmp[71] & tmp[15];
		tmp[114] = ~tmp[11];
		tmp[68] = tmp[15] & tmp[114];
		tmp[45] = tmp[20] & tmp[11];
		tmp[65] = tmp[178] ^ tmp[45];
		tmp[132] = tmp[178] & tmp[15];
		tmp[125] = tmp[20] ^ tmp[132];
		tmp[28] &= tmp[178];
		tmp[21] = ~tmp[150];
		tmp[75] = ~tmp[164];
		tmp[106] = tmp[164] & ~(tmp[10] ^ tmp[68]);
		tmp[11] =
				tmp[101] ^ tmp[4] ^ (tmp[164] & tmp[125] ^ tmp[20] & ~tmp[68]) ^ tmp[100] & (tmp[15] ^ tmp[164] & ~(tmp[176] ^ tmp[68]) ^ tmp[20] & tmp[114]) ^ (tmp[150] | tmp[106] ^ (tmp[10] ^ tmp[100] & (
						tmp[164] & (tmp[162] ^ tmp[11]) ^ tmp[20] & ~tmp[28])));
		tmp[68] = tmp[20] ^ tmp[28];
		tmp[65] = tmp[136] ^ (tmp[12] ^ ((tmp[164] | tmp[178] ^ tmp[10]) ^ (tmp[21] & (tmp[68] ^ tmp[100] & (tmp[65] ^ tmp[164] & ~(tmp[162] ^ tmp[56])) ^ tmp[164] & ~tmp[136]) ^ tmp[100] & ~(tmp[65]
				^ tmp[164] & tmp[68]))));
		tmp[68] = tmp[1] ^ tmp[28];
		tmp[28] = tmp[178] ^ (tmp[94] ^ (tmp[164] & ~tmp[176] ^ tmp[1])) ^ tmp[100] & (tmp[28] ^ tmp[20] & ~tmp[4] | tmp[75]) ^ (tmp[150] | tmp[56] ^ (tmp[71] & tmp[164] ^ tmp[100] & (tmp[28]
				^ tmp[20] & tmp[132] ^ tmp[164] & ~tmp[68])) ^ tmp[20] & (tmp[15] | tmp[28]));
		tmp[132] =
				tmp[125] ^ (tmp[38] ^ (tmp[75] & tmp[68] ^ tmp[100] & (tmp[4] ^ (tmp[10] ^ tmp[164] & (tmp[162] ^ tmp[132]))))) ^ tmp[21] & ((tmp[164] | tmp[20] ^ tmp[15]) ^ tmp[100] & (tmp[45] ^ tmp[132]
						^ tmp[164] & tmp[132]));
		tmp[105] =
				tmp[128] ^ (tmp[80] ^ (tmp[139] | tmp[105] ^ tmp[109]) ^ tmp[16] & tmp[26] ^ (tmp[112] ^ (tmp[67] & tmp[147] ^ tmp[26] & (tmp[105] ^ (tmp[41] | tmp[70]))) ^ (tmp[41] | tmp[133])) & tmp[38]);
		tmp[117] = tmp[8] ^ (tmp[70] ^ tmp[26] & ~(tmp[41] ^ tmp[117]) ^ (tmp[139] | tmp[80])) ^ tmp[38] & ~(tmp[73] ^ (tmp[139] | tmp[109]) ^ tmp[26] & (tmp[117] ^ tmp[73]));
		tmp[73] = tmp[49] & tmp[117];
		tmp[109] = ~tmp[130];
		tmp[80] = ~tmp[117];
		tmp[70] = tmp[100] & tmp[80];
		tmp[8] = ~tmp[70];
		tmp[112] = tmp[100] & tmp[8];
		tmp[16] = tmp[87] & ~tmp[112];
		tmp[128] = tmp[62] & tmp[117];
		tmp[162] = tmp[87] & tmp[117];
		tmp[10] = tmp[100] ^ tmp[117];
		tmp[45] = tmp[87] & tmp[10];
		tmp[80] &= tmp[87];
		tmp[4] = tmp[117] ^ tmp[80];
		tmp[68] = tmp[100] | tmp[117];
		tmp[75] = ~tmp[68];
		tmp[21] = tmp[87] & tmp[75];
		tmp[125] = tmp[117] & ~tmp[100];
		tmp[176] = tmp[87] & tmp[125];
		tmp[69] = tmp[143] ^ (tmp[147] ^ tmp[67] & tmp[47] ^ tmp[26] & (tmp[135] | ~tmp[59]) ^ ((tmp[139] | tmp[47] ^ tmp[133]) ^ (tmp[133] ^ tmp[69] & tmp[26])) & tmp[38]);
		tmp[133] = ~tmp[85];
		tmp[47] = tmp[37] & tmp[69];
		tmp[67] = ~tmp[47];
		tmp[147] = tmp[57] ^ tmp[69];
		tmp[143] = tmp[29] & tmp[67];
		tmp[67] &= tmp[37];
		tmp[71] = tmp[86] & tmp[47];
		tmp[1] = tmp[57] | tmp[47];
		tmp[56] = tmp[37] | tmp[69];
		tmp[94] = tmp[37] & ~tmp[69];
		tmp[136] = tmp[37] ^ tmp[69];
		tmp[12] = tmp[86] & tmp[94];
		tmp[147] =
				tmp[139] ^ tmp[136] ^ ((tmp[79] | tmp[29] & (tmp[69] ^ tmp[1]) ^ tmp[147] & (tmp[29] & tmp[133])) ^ tmp[133] & (tmp[147] ^ (tmp[29] | tmp[136])) ^ tmp[29] & (tmp[71] ^ tmp[69] & ~tmp[37]));
		tmp[139] = ~tmp[79];
		tmp[114] = tmp[86] & tmp[136];
		tmp[101] = tmp[136] ^ tmp[114];
		tmp[12] = tmp[57] ^ (tmp[158] ^ (tmp[63] & tmp[29] ^ tmp[47])) ^ (tmp[85] | tmp[12] ^ (tmp[67] ^ tmp[29] & ~(tmp[1] ^ tmp[136]))) ^ tmp[139] & (tmp[12] ^ (tmp[37] ^ tmp[29] & (tmp[67] ^ (tmp[57]
				| tmp[56]))) ^ tmp[133] & (tmp[143] ^ tmp[136]));
		tmp[158] = tmp[57] | tmp[69];
		tmp[106] = tmp[69] ^ tmp[158];
		tmp[1] = tmp[24] ^ tmp[37] ^ ((tmp[57] | tmp[136]) ^ (tmp[133] & (tmp[94] ^ tmp[29] & (tmp[47] ^ tmp[1]) ^ tmp[86] & tmp[69]) ^ tmp[29] & ~tmp[101])) ^ (tmp[79] | tmp[67] ^ (tmp[114] ^ (
				tmp[133] & (tmp[106] ^ tmp[29] & tmp[106]) ^ tmp[29] & tmp[101])));
		tmp[56] ^=
				tmp[143] ^ (tmp[115] ^ tmp[57]) ^ ((tmp[85] | tmp[33] & tmp[71]) ^ (tmp[79] | tmp[37] ^ tmp[63] ^ tmp[29] & (tmp[37] & tmp[57]) ^ tmp[133] & (tmp[158] ^ (tmp[56] ^ tmp[29] & ~tmp[56]))));
		tmp[158] = tmp[11] & ~tmp[56];
		tmp[63] = ~tmp[38];
		tmp[119] = tmp[163] ^ (tmp[60] ^ tmp[103]) ^ (tmp[119] ^ tmp[126] & (tmp[151] ^ tmp[153]) | tmp[38]) ^ tmp[88] & ~(tmp[140] ^ tmp[135] & tmp[126] ^ (tmp[60] ^ tmp[119]) & tmp[63]);
		tmp[60] = tmp[29] & ~tmp[119];
		tmp[135] = tmp[119] | tmp[60];
		tmp[163] = tmp[29] ^ tmp[119];
		tmp[133] = tmp[29] | tmp[119];
		tmp[71] = tmp[116] ^ tmp[133];
		tmp[115] = tmp[133] ^ (tmp[25] | tmp[119]);
		tmp[77] =
				tmp[79] & tmp[33] ^ (tmp[102] ^ (tmp[116] ^ (tmp[119] ^ ((tmp[20] | tmp[29] ^ (tmp[150] & (tmp[115] ^ tmp[79] & ~tmp[71]) ^ tmp[79] & ~(tmp[163] ^ tmp[77] & tmp[119])) ^ (tmp[25] | tmp[60]))
						^ tmp[150] & ~(tmp[115] ^ tmp[79] & tmp[60])))));
		tmp[115] = tmp[144] ^ tmp[133];
		tmp[133] = tmp[29] ^ (tmp[177] ^ (tmp[25] ^ ((tmp[20] | tmp[150] & (tmp[115] ^ tmp[79] & tmp[115])) ^ tmp[79] & ~(tmp[60] ^ (tmp[25] | tmp[133]))))) ^ tmp[150] & ~(tmp[116] ^ (tmp[60]
				^ tmp[79] & tmp[71]));
		tmp[33] &= tmp[119];
		tmp[115] = tmp[56] & tmp[133];
		tmp[71] = tmp[119] & ~tmp[33];
		tmp[33] ^= tmp[144];
		tmp[60] =
				tmp[165] ^ (tmp[25] ^ ((tmp[20] | tmp[71] ^ tmp[79] & (tmp[144] ^ tmp[71]) ^ tmp[150] & (tmp[29] ^ tmp[79] & ~(tmp[25] ^ tmp[60]))) ^ tmp[150] & (tmp[33] ^ tmp[79] & (tmp[116] ^ tmp[119]))
						^ tmp[79] & tmp[135]));
		tmp[71] = tmp[1] | tmp[60];
		tmp[116] = ~tmp[60];
		tmp[165] = tmp[1] & tmp[116];
		tmp[144] =
				tmp[163] ^ (tmp[95] ^ (tmp[150] & (tmp[25] ^ tmp[79] & ~tmp[144]) ^ tmp[79] & tmp[33])) ^ (tmp[20] | tmp[135] ^ tmp[150] & (tmp[29] ^ tmp[139] & (tmp[144] ^ tmp[119])) ^ (tmp[79] | tmp[33]));
		tmp[140] = tmp[83] ^ (tmp[161] ^ tmp[95] & ~tmp[140] ^ tmp[140] & tmp[126] ^ (tmp[140] | tmp[126]) & tmp[63]) ^ tmp[88] & ~(tmp[157] ^ tmp[126] & (tmp[64] ^ tmp[95] & ~tmp[140])
				^ (tmp[157] ^ tmp[126] & tmp[95]) & tmp[63]);
		tmp[64] = ~tmp[140];
		tmp[83] =
				tmp[159] ^ tmp[104] ^ (tmp[118] | tmp[111]) ^ ((tmp[140] | tmp[104] & tmp[152]) ^ (tmp[42] | tmp[64] & (tmp[99] ^ tmp[9] & tmp[152]))) ^ (tmp[72] | tmp[84] ^ tmp[118] & tmp[152] ^ tmp[64] & (
						tmp[118] ^ tmp[0]));
		tmp[139] = tmp[132] | tmp[83];
		tmp[33] = tmp[132] & ~tmp[83];
		tmp[135] = ~tmp[72];
		tmp[108] =
				tmp[93] ^ tmp[99] ^ (~tmp[42] & (tmp[111] ^ tmp[135] & (tmp[108] ^ tmp[118] & tmp[64]) ^ (tmp[127] | tmp[140])) ^ (tmp[72] | tmp[108] ^ tmp[140] & ~(tmp[76] | tmp[111])) ^ tmp[127] & tmp[64]);
		tmp[127] = tmp[28] & tmp[108];
		tmp[99] = ~tmp[108];
		tmp[93] = tmp[28] & tmp[99];
		tmp[163] = tmp[28] ^ tmp[108];
		tmp[177] = tmp[108] & ~tmp[28];
		tmp[113] =
				tmp[166] ^ (tmp[137] ^ tmp[154] & tmp[62]) ^ (tmp[109] & (tmp[113] | tmp[117]) ^ tmp[27] & tmp[117]) ^ (tmp[140] | (tmp[130] | tmp[160] ^ tmp[145] ^ tmp[117] & ~(tmp[113] ^ tmp[14])) ^ (
						tmp[117] | tmp[174] ^ tmp[39]));
		tmp[124] =
				tmp[110] ^ tmp[62] ^ (tmp[51] ^ ((tmp[130] | tmp[174] ^ tmp[154] & tmp[117]) ^ tmp[40] & tmp[117])) ^ tmp[64] & (tmp[124] ^ tmp[109] & ((tmp[51] | tmp[172]) ^ (tmp[19] ^ tmp[117] & ~tmp[124]))
						^ (tmp[40] | tmp[117]));
		tmp[154] = tmp[28] & tmp[124];
		tmp[40] = tmp[127] & tmp[124];
		tmp[110] = tmp[93] & tmp[124];
		tmp[137] = tmp[177] ^ tmp[124];
		tmp[102] = tmp[124] & ~tmp[93];
		tmp[143] = tmp[127] ^ tmp[102];
		tmp[99] &= tmp[124];
		tmp[47] = tmp[28] ^ tmp[163] & tmp[124];
		tmp[39] =
				tmp[88] ^ (tmp[117] ^ (tmp[172] ^ (tmp[19] ^ (tmp[130] | tmp[73] ^ (tmp[160] ^ tmp[39]))))) ^ tmp[64] & (tmp[166] ^ tmp[62] & ~tmp[66] ^ tmp[131] & tmp[128] ^ (tmp[130] | tmp[39] ^ (tmp[66]
						^ tmp[128])));
		tmp[128] = tmp[1] | tmp[39];
		tmp[131] = ~tmp[1];
		tmp[19] = tmp[128] & tmp[131];
		tmp[172] = tmp[1] ^ tmp[39];
		tmp[106] = ~tmp[39];
		tmp[86] = tmp[1] & tmp[39];
		tmp[94] = tmp[1] & tmp[106];
		tmp[14] ^= tmp[117] & ~(tmp[66] ^ tmp[27]) ^ (tmp[26] ^ (tmp[166] ^ (tmp[130] | tmp[73] ^ (tmp[160] ^ tmp[49]))) ^ (tmp[140] | tmp[145] ^ tmp[109] & (tmp[134] ^ tmp[14] & tmp[117])
				^ tmp[134] & tmp[117]));
		tmp[141] = tmp[43] ^ tmp[156] ^ (tmp[0] ^ ((tmp[76] ^ tmp[141] | tmp[140]) ^ tmp[135] & (tmp[104] ^ (tmp[169] ^ tmp[64] & (tmp[118] ^ tmp[141]))))) ^ (tmp[42]
				| tmp[140] & ~(tmp[51] ^ tmp[141]) ^ tmp[135] & (tmp[84] ^ tmp[96] ^ (tmp[51] | tmp[140])));
		tmp[169] = tmp[141] & ~tmp[14];
		tmp[96] = tmp[14] ^ tmp[141];
		tmp[64] = tmp[3] ^ (tmp[9] ^ tmp[111]) ^ (tmp[135] & (tmp[54] ^ (tmp[118] & ~tmp[104] | tmp[140])) ^ (tmp[42] | tmp[167] ^ tmp[140] & ~tmp[54] ^ tmp[135] & (tmp[167] ^ tmp[104] & tmp[64]))
				^ tmp[54] & tmp[64]);
		tmp[104] = tmp[116] & tmp[64];
		tmp[131] &= tmp[64];
		tmp[54] = tmp[1] | tmp[131];
		tmp[167] = tmp[116] & tmp[54];
		tmp[135] = tmp[60] ^ tmp[54];
		tmp[9] = tmp[1] & ~tmp[64];
		tmp[3] = tmp[1] & ~tmp[9];
		tmp[9] = tmp[54] ^ (tmp[60] | tmp[9]);
		tmp[84] = tmp[1] ^ tmp[64];
		tmp[76] = tmp[1] | tmp[64];
		tmp[0] = tmp[1] & tmp[64];
		tmp[156] = tmp[60] | tmp[76];
		tmp[43] = tmp[3] ^ tmp[156];
		tmp[49] = tmp[116] & tmp[0];
		tmp[160] = tmp[165] ^ tmp[64];
		tmp[171] ^=
				tmp[126] & ~(tmp[98] ^ tmp[7] & tmp[95]) ^ (tmp[22] ^ (tmp[161] ^ tmp[153] ^ tmp[95] & (tmp[7] & tmp[126])) & tmp[63]) ^ tmp[88] & ~(tmp[30] ^ (tmp[151] ^ tmp[171]) & ~tmp[126] ^ (tmp[103]
						| tmp[38]));
		tmp[7] = tmp[171] & ~(tmp[87] ^ tmp[68]);
		tmp[153] = ~tmp[174];
		tmp[68] = tmp[10] ^ tmp[87] & tmp[8] ^ (tmp[59] ^ tmp[171]) ^ tmp[153] & (tmp[100] ^ tmp[16] ^ tmp[70] & tmp[171]) ^ (tmp[178] | tmp[45] ^ tmp[171] & ~tmp[68] ^ (tmp[174]
				| tmp[10] ^ tmp[21] ^ (tmp[162] ^ (tmp[100] | tmp[125])) & tmp[171]));
		tmp[8] = ~tmp[68];
		tmp[59] = tmp[14] & tmp[8];
		tmp[103] = tmp[14] ^ tmp[68];
		tmp[63] = ~tmp[132];
		tmp[22] = tmp[68] ^ tmp[141] & tmp[103];
		tmp[134] = tmp[141] & tmp[68];
		tmp[73] = tmp[132] & ~tmp[134];
		tmp[109] = tmp[141] & tmp[8];
		tmp[145] = tmp[103] ^ tmp[109];
		tmp[166] = tmp[68] ^ tmp[109];
		tmp[26] = tmp[14] & tmp[68];
		tmp[27] = tmp[68] & ~tmp[26];
		tmp[66] = tmp[141] & ~tmp[27];
		tmp[101] = tmp[27] ^ tmp[66];
		tmp[114] = tmp[14] | tmp[68];
		tmp[136] = tmp[141] & ~tmp[114];
		tmp[4] = (tmp[178] | tmp[4] ^ tmp[75] & tmp[171] ^ (tmp[174] | tmp[4] ^ tmp[171] & ~tmp[4])) ^ (tmp[48] ^ (tmp[70] ^ tmp[16])) ^ ((tmp[174] | tmp[100] & tmp[162] ^ tmp[117] & tmp[171])
				^ tmp[100] & tmp[171]);
		tmp[75] = ~tmp[56];
		tmp[162] = tmp[4] & tmp[75];
		tmp[48] = tmp[133] & tmp[4];
		tmp[67] = tmp[75] & tmp[48];
		tmp[24] = tmp[56] ^ tmp[67];
		tmp[46] = tmp[67] ^ (tmp[56] | tmp[162]);
		tmp[67] ^= tmp[4];
		tmp[23] = tmp[56] ^ tmp[4];
		tmp[129] = tmp[56] & ~tmp[4];
		tmp[138] = tmp[56] & ~tmp[129];
		tmp[53] = tmp[129] ^ tmp[133] & tmp[23];
		tmp[170] = tmp[133] & (tmp[56] & tmp[4]);
		tmp[58] = tmp[23] ^ tmp[170];
		tmp[48] ^= tmp[4];
		tmp[7] =
				tmp[171] & ~(tmp[87] ^ tmp[125]) ^ (tmp[6] ^ (tmp[112] ^ tmp[21])) ^ ((tmp[174] | tmp[176] ^ tmp[87] & tmp[171]) ^ (tmp[178] | tmp[117] ^ tmp[45] ^ tmp[7] ^ tmp[153] & (tmp[176] ^ tmp[7])));
		tmp[45] = tmp[7] & ~tmp[86];
		tmp[125] = tmp[7] & ~(tmp[1] & ~tmp[86]);
		tmp[21] = tmp[7] & ~tmp[19];
		tmp[112] = tmp[7] & ~tmp[1];
		tmp[6] = ~tmp[39];
		tmp[36] = tmp[7] & tmp[6];
		tmp[80] =
				tmp[146] ^ (tmp[32] ^ tmp[10]) ^ (tmp[171] & ~tmp[32] ^ ((tmp[178] | tmp[31] & (tmp[100] ^ tmp[171] & ~(tmp[70] ^ tmp[80])) ^ (tmp[16] ^ tmp[32] & tmp[171])) ^ tmp[153] & (tmp[176] ^ (tmp[80]
						| tmp[171]))));
		tmp[95] = tmp[107] ^ (tmp[161] ^ tmp[126] ^ tmp[159] & tmp[95] ^ (tmp[98] ^ tmp[126] & ~tmp[30] ^ tmp[161] & tmp[95] | tmp[38])) ^ tmp[88] & (tmp[151] ^ tmp[157] ^ tmp[126] & ~(tmp[98]
				^ tmp[151] & tmp[95]) ^ (tmp[122] ^ tmp[126] & tmp[122]) & ~tmp[38]);
		tmp[151] = tmp[74] | tmp[95];
		tmp[30] = tmp[74] ^ tmp[61] & ~tmp[151];
		tmp[98] = tmp[74] & tmp[95];
		tmp[122] = ~tmp[98];
		tmp[161] = tmp[61] & tmp[122];
		tmp[38] = ~tmp[95];
		tmp[157] = tmp[61] & tmp[95];
		tmp[159] = ~tmp[111];
		tmp[88] = tmp[2] | tmp[95];
		tmp[122] = tmp[161] ^ tmp[69] & ~(tmp[61] & ~tmp[74] ^ tmp[95] & tmp[122]) ^ tmp[85] & ~(tmp[30] ^ tmp[69] & (tmp[95] ^ tmp[157]));
		tmp[107] = tmp[111] | tmp[88];
		tmp[70] = ~tmp[2];
		tmp[32] = tmp[88] & tmp[70];
		tmp[16] = ~tmp[78];
		tmp[70] &= tmp[95];
		tmp[31] = tmp[159] & tmp[70];
		tmp[176] = tmp[2] ^ tmp[95];
		tmp[153] = tmp[111] | tmp[176];
		tmp[10] = tmp[159] & tmp[176];
		tmp[146] = tmp[2] & tmp[95];
		tmp[55] = tmp[2] & ~tmp[146];
		tmp[148] = tmp[111] | tmp[55];
		tmp[81] = tmp[159] & tmp[146];
		tmp[97] ^=
				tmp[16] & (tmp[107] ^ tmp[32]) ^ (tmp[55] ^ tmp[159] & tmp[88]) ^ tmp[118] & ~(tmp[95] ^ tmp[107]) ^ tmp[105] & ~(tmp[107] ^ (tmp[78] | tmp[2] ^ tmp[31]) ^ tmp[118] & (tmp[149] ^ tmp[146]));
		tmp[142] = tmp[172] ^ tmp[21] ^ (tmp[128] | ~tmp[7]) & tmp[97] ^ (tmp[144] | tmp[39] ^ tmp[45] ^ tmp[97] & (tmp[86] ^ tmp[36]));
		tmp[82] = ~tmp[144];
		tmp[19] = tmp[39] ^ tmp[39] & tmp[7] ^ tmp[82] & (tmp[128] ^ tmp[112] ^ tmp[97] & ~(tmp[19] ^ tmp[7])) ^ tmp[97] & ~(tmp[86] ^ tmp[125]);
		tmp[128] = ~tmp[65];
		tmp[130] ^= tmp[19] & tmp[128] ^ tmp[142];
		tmp[19] = tmp[65] & ~tmp[19] ^ (tmp[118] ^ tmp[142]);
		tmp[36] = tmp[172] ^ tmp[125] ^ tmp[82] & (tmp[45] ^ tmp[97] & ~tmp[36]) ^ tmp[97] & (tmp[94] ^ tmp[36]);
		tmp[94] = tmp[97] & ~(tmp[1] ^ tmp[21]) ^ (tmp[1] ^ tmp[86] & tmp[7] ^ (tmp[144] | tmp[112] ^ tmp[97] & (tmp[94] ^ tmp[7])));
		tmp[178] ^= tmp[36] ^ tmp[128] & tmp[94];
		tmp[94] = tmp[65] & ~tmp[94] ^ (tmp[57] ^ tmp[36]);
		tmp[36] = tmp[74] ^ tmp[95];
		tmp[57] = tmp[61] & ~tmp[36];
		tmp[112] = tmp[74] & tmp[38];
		tmp[86] = tmp[112] ^ tmp[61] & tmp[98];
		tmp[112] ^= tmp[57];
		tmp[151] = tmp[98] ^ (tmp[61] ^ tmp[69] & ~(tmp[157] ^ tmp[151] & tmp[38])) ^ tmp[85] & ~(tmp[86] ^ tmp[112] & ~tmp[69]);
		tmp[112] = tmp[161] ^ (tmp[69] & (tmp[61] | ~tmp[74]) ^ tmp[36]) ^ tmp[85] & ~(tmp[30] ^ (tmp[69] | tmp[112]));
		tmp[126] ^= (tmp[78] | tmp[122]) ^ tmp[112];
		tmp[30] = ~tmp[83];
		tmp[161] = tmp[126] & tmp[30];
		tmp[157] = tmp[132] ^ tmp[126];
		tmp[21] = tmp[30] & tmp[157];
		tmp[45] = tmp[83] | tmp[157];
		tmp[82] = tmp[132] & ~tmp[126];
		tmp[125] = tmp[83] | tmp[82];
		tmp[172] = tmp[83] | tmp[126];
		tmp[142] = tmp[63] & tmp[126];
		tmp[50] = tmp[126] & ~tmp[142] ^ (tmp[132] & tmp[106] ^ tmp[125]);
		tmp[35] = tmp[132] & tmp[126];
		tmp[122] = tmp[168] ^ tmp[112] ^ tmp[78] & tmp[122];
		tmp[112] = tmp[68] | tmp[122];
		tmp[168] = tmp[122] ^ tmp[112];
		tmp[44] = ~tmp[122];
		tmp[74] ^=
				tmp[177] ^ tmp[124] & (tmp[177] | ~tmp[108]) ^ (tmp[133] & (tmp[93] ^ tmp[102] ^ tmp[122] & ~(tmp[93] ^ tmp[40])) ^ (tmp[93] ^ tmp[110]) & tmp[122]) ^ tmp[12] & (tmp[124] ^ tmp[133] & ~(
						tmp[124] ^ tmp[122] & ~(tmp[108] ^ tmp[99])) ^ tmp[93] & tmp[44]);
		tmp[102] =
				tmp[122] ^ (tmp[62] ^ (tmp[163] ^ tmp[110])) ^ (tmp[12] & (tmp[108] ^ tmp[124] ^ tmp[133] & (tmp[108] ^ tmp[122] & ~tmp[137])) ^ tmp[133] & (tmp[102] ^ tmp[122] & ~(tmp[28] | tmp[108])));
		tmp[93] = tmp[12] & ~(tmp[177] & tmp[122] ^ tmp[133] & (tmp[137] ^ (tmp[177] ^ tmp[99] | tmp[122]))) ^ (tmp[150] ^ tmp[143] ^ (tmp[133] & ~(tmp[110] ^ (tmp[40] ^ (tmp[108] | tmp[93])) & tmp[122])
				^ tmp[47] & tmp[44]));
		tmp[127] = tmp[111] ^ tmp[143] ^ tmp[122] & ~tmp[47] ^ (tmp[133] & ~(tmp[40] ^ tmp[154] & tmp[122]) ^ tmp[12] & ~((tmp[108] ^ tmp[154]) & tmp[44] ^ tmp[133] & (tmp[127]
				^ (tmp[127] ^ tmp[108] & tmp[124]) & tmp[122])));
		tmp[57] = tmp[95] ^ tmp[61] & tmp[36] ^ tmp[85] & (tmp[86] ^ tmp[69] & (tmp[61] & tmp[38])) ^ tmp[69] & ~(tmp[98] ^ tmp[57]);
		tmp[121] ^= tmp[16] & tmp[57] ^ tmp[151];
		tmp[57] = tmp[17] ^ (tmp[151] ^ tmp[78] & ~tmp[57]);
		tmp[164] ^= tmp[43] ^ tmp[54] & tmp[57] ^ (tmp[80] & ~(tmp[165] ^ tmp[57] & ~(tmp[167] ^ tmp[76])) ^ tmp[128] & (tmp[9] ^ tmp[57] & (~tmp[135] ^ tmp[160] & tmp[80])));
		tmp[151] = ~tmp[57];
		tmp[17] = tmp[56] ^ tmp[57];
		tmp[98] = tmp[56] & tmp[151];
		tmp[86] = tmp[11] ^ tmp[17];
		tmp[36] = ~tmp[80];
		tmp[154] = tmp[57] | tmp[98];
		tmp[44] = tmp[154] ^ tmp[11] & ~tmp[98];
		tmp[40] = tmp[11] & tmp[98];
		tmp[0] =
				tmp[51] ^ tmp[160] ^ (~(tmp[71] ^ tmp[54]) & tmp[57] ^ (tmp[80] & (tmp[71] ^ tmp[84] ^ tmp[57] & ~(tmp[131] ^ tmp[156])) ^ (tmp[65] | tmp[57] & ~(tmp[165] ^ tmp[76]) ^ tmp[80] & (tmp[43] ^ (
						tmp[71] ^ tmp[0] | tmp[57])))));
		tmp[71] = tmp[19] & tmp[0];
		tmp[76] = tmp[19] ^ tmp[0];
		tmp[165] = tmp[130] ^ tmp[0];
		tmp[156] = tmp[130] & tmp[0];
		tmp[43] = tmp[130] & ~tmp[156];
		tmp[51] = tmp[130] | tmp[0];
		tmp[47] = ~tmp[130];
		tmp[143] = tmp[51] & tmp[47];
		tmp[47] &= tmp[0];
		tmp[99] = tmp[19] & ~tmp[0];
		tmp[177] = ~tmp[19];
		tmp[110] = tmp[0] & tmp[177];
		tmp[137] = tmp[0] & ~tmp[110];
		tmp[84] = tmp[128] & (tmp[54] ^ tmp[49] ^ tmp[57] & ~(tmp[60] ^ tmp[84]) ^ tmp[80] & ~(tmp[160] ^ (tmp[60] ^ tmp[3]) & tmp[57])) ^ (tmp[135] ^ (tmp[29] ^ (
				tmp[80] & ~(tmp[60] ^ tmp[131] ^ tmp[57] & ~(tmp[64] ^ (tmp[60] | tmp[84]))) ^ (tmp[131] ^ tmp[49] | tmp[57]))));
		tmp[49] = ~tmp[84];
		tmp[160] = tmp[93] | tmp[84];
		tmp[54] = tmp[93] ^ tmp[84];
		tmp[29] = tmp[93] & tmp[49];
		tmp[135] = tmp[84] | tmp[29];
		tmp[128] = tmp[56] | tmp[57];
		tmp[150] = ~tmp[93];
		tmp[163] = tmp[84] & tmp[150];
		tmp[62] = ~tmp[113];
		tmp[120] = tmp[56] & tmp[57];
		tmp[34] = tmp[11] & tmp[120];
		tmp[131] =
				tmp[57] & ~(tmp[131] ^ tmp[167]) ^ ((tmp[65] | tmp[104] ^ tmp[3] ^ (tmp[64] ^ tmp[116] & tmp[131]) & tmp[80]) ^ (tmp[61] ^ tmp[9])) ^ tmp[80] & ~(tmp[3] ^ (tmp[104] ^ tmp[131]) & tmp[57]);
		tmp[116] = ~tmp[74];
		tmp[75] &= tmp[57];
		tmp[104] = tmp[11] & tmp[75];
		tmp[3] = tmp[11] & tmp[57];
		tmp[167] = tmp[128] ^ tmp[3];
		tmp[9] = tmp[111] | tmp[95];
		tmp[159] &= tmp[95];
		tmp[32] =
				tmp[90] ^ tmp[176] ^ (tmp[78] | tmp[70] ^ tmp[153]) ^ tmp[118] & ~(tmp[107] ^ tmp[16] & (tmp[32] ^ tmp[148])) ^ tmp[105] & ~(tmp[31] ^ tmp[118] & (tmp[31] ^ (tmp[78] | tmp[95] ^ tmp[159]))
						^ tmp[78] & ~(tmp[95] ^ tmp[81]));
		tmp[70] = ~tmp[32];
		tmp[120] =
				(tmp[113] | tmp[98] ^ tmp[80] & tmp[17]) ^ (tmp[37] ^ tmp[86]) ^ (tmp[80] | tmp[56] ^ tmp[34]) ^ tmp[70] & (tmp[34] ^ (tmp[98] ^ (tmp[113] | tmp[128] ^ tmp[98] & tmp[36])) ^ tmp[36] & (tmp[40]
						^ tmp[120]));
		tmp[37] = ~tmp[120];
		tmp[90] = tmp[84] & tmp[37];
		tmp[61] = tmp[84] | tmp[120];
		tmp[151] =
				tmp[174] ^ tmp[158] ^ (tmp[36] & tmp[44] ^ (tmp[113] | tmp[17] ^ tmp[34] ^ (tmp[80] | tmp[128]))) ^ (tmp[32] | tmp[40] ^ (tmp[154] ^ (tmp[80] | tmp[128] ^ tmp[11] & ~tmp[17])) ^ tmp[62] & (
						tmp[128] ^ (tmp[80] | tmp[128] ^ tmp[11] & tmp[151])));
		tmp[34] = tmp[178] | tmp[151];
		tmp[154] = ~tmp[121];
		tmp[40] = tmp[56] & tmp[32];
		tmp[104] =
				tmp[80] ^ (tmp[2] ^ tmp[75]) ^ tmp[11] & tmp[17] ^ tmp[62] & (tmp[98] ^ (tmp[104] ^ (tmp[11] | tmp[80]))) ^ tmp[70] & (tmp[3] ^ (tmp[57] ^ (tmp[36] & tmp[104] ^ (tmp[113] | tmp[104] ^ (tmp[80]
						| tmp[11] ^ tmp[57])))));
		tmp[67] = tmp[133] ^ tmp[162] ^ (tmp[85] ^ (tmp[154] & (tmp[67] ^ (tmp[129] | tmp[32])) ^ tmp[24] & tmp[32])) ^ tmp[108] & (tmp[32] & ~tmp[67] ^ (tmp[56] ^ tmp[154] & (tmp[48] ^ tmp[40])));
		tmp[85] = tmp[74] ^ tmp[67];
		tmp[36] = tmp[74] | tmp[67];
		tmp[98] = tmp[116] & tmp[36];
		tmp[17] = tmp[131] & tmp[36];
		tmp[3] = tmp[84] | tmp[67];
		tmp[174] = tmp[131] & tmp[67];
		tmp[49] &= tmp[67];
		tmp[89] = tmp[67] & ~tmp[49];
		tmp[123] = tmp[84] & tmp[67];
		tmp[173] = tmp[37] & tmp[123];
		tmp[92] = ~tmp[67];
		tmp[5] = tmp[84] & tmp[92];
		tmp[52] = tmp[173] ^ tmp[5];
		tmp[18] = (tmp[67] | tmp[5]) ^ tmp[37] & tmp[67];
		tmp[13] = tmp[5] ^ (tmp[120] | tmp[5]);
		tmp[175] = tmp[37] & tmp[5];
		tmp[91] = tmp[90] ^ tmp[5];
		tmp[92] = tmp[85] ^ tmp[131] & tmp[92];
		tmp[179] = tmp[74] & tmp[67];
		tmp[180] = tmp[179] ^ tmp[131] & ~tmp[36];
		tmp[181] = tmp[74] & ~tmp[179];
		tmp[182] = tmp[181] ^ tmp[131] & ~tmp[181];
		tmp[183] = tmp[84] ^ tmp[67];
		tmp[184] = tmp[90] ^ tmp[67];
		tmp[40] =
				tmp[25] ^ (tmp[23] ^ tmp[133] & ~tmp[162]) ^ ((tmp[121] | tmp[24] ^ (tmp[56] | tmp[32])) ^ tmp[32] & ~tmp[53]) ^ tmp[108] & ~(tmp[170] ^ tmp[154] & (tmp[170] ^ tmp[40]) ^ tmp[53] & tmp[32]);
		tmp[53] = ~tmp[40];
		tmp[162] =
				tmp[58] ^ (tmp[72] ^ (tmp[170] | tmp[32])) ^ (tmp[108] & (tmp[4] ^ tmp[133] & ~tmp[23] ^ (tmp[121] | tmp[46] ^ (tmp[115] ^ tmp[162]) & tmp[70]) ^ tmp[48] & tmp[32]) ^ tmp[154] & (tmp[46]
						^ tmp[162] & tmp[70]));
		tmp[23] = ~tmp[162];
		tmp[48] = tmp[137] | tmp[162];
		tmp[167] = tmp[86] ^ (tmp[20] ^ tmp[62] & (tmp[128] ^ (tmp[158] ^ tmp[80]))) ^ (tmp[167] ^ (tmp[113] | tmp[158] ^ tmp[75] ^ (tmp[80] | tmp[167])) ^ tmp[80] & ~tmp[44] | tmp[32]);
		tmp[75] = ~tmp[167];
		tmp[158] = tmp[93] | tmp[167];
		tmp[44] = tmp[29] & tmp[75];
		tmp[128] = tmp[54] & tmp[75];
		tmp[62] = tmp[160] | tmp[167];
		tmp[20] = tmp[163] ^ tmp[62];
		tmp[86] = tmp[84] & tmp[75];
		tmp[170] = tmp[93] & tmp[86];
		tmp[62] ^= tmp[135];
		tmp[72] = tmp[167] | tmp[84] & ~tmp[163];
		tmp[138] =
				tmp[58] & tmp[32] ^ (tmp[100] ^ (tmp[56] ^ tmp[133] & ~tmp[138])) ^ (tmp[121] | tmp[56] ^ (tmp[56] ^ tmp[115]) & tmp[70]) ^ tmp[108] & ~(tmp[138] ^ tmp[133] & tmp[129] ^ tmp[154] & (tmp[46]
						^ tmp[32] & ~(tmp[56] | tmp[4])));
		tmp[46] = tmp[167] & tmp[138];
		tmp[129] = tmp[167] ^ tmp[138];
		tmp[115] = tmp[138] & ~tmp[46];
		tmp[154] = ~tmp[138];
		tmp[70] = tmp[167] & tmp[154];
		tmp[100] = tmp[167] | tmp[138];
		tmp[154] &= tmp[100];
		tmp[88] =
				tmp[41] ^ (tmp[111] ^ tmp[146] ^ (tmp[78] | tmp[81]) ^ tmp[118] & (tmp[95] ^ tmp[148] ^ tmp[16] & (tmp[107] ^ tmp[176]))) ^ tmp[105] & ~(tmp[2] & tmp[152] ^ tmp[88] ^ tmp[118] & (tmp[10] ^ (
						tmp[111] & ~tmp[78] ^ tmp[88])) ^ tmp[16] & (tmp[88] ^ tmp[159]));
		tmp[145] =
				tmp[96] ^ (tmp[147] & (tmp[109] ^ tmp[63] & tmp[145]) ^ tmp[132] & ~(tmp[68] ^ tmp[136])) ^ (tmp[117] ^ (tmp[132] & ~(tmp[114] ^ tmp[136]) ^ tmp[147] & (tmp[166] ^ (tmp[132] | tmp[145]))
						| tmp[88]));
		tmp[136] = ~tmp[145];
		tmp[109] = tmp[165] & tmp[136];
		tmp[117] = tmp[143] | tmp[145];
		tmp[152] = tmp[47] & tmp[136];
		tmp[176] = tmp[0] | tmp[145];
		tmp[107] = tmp[130] | tmp[145];
		tmp[159] = ~tmp[151];
		tmp[148] = tmp[51] ^ tmp[176];
		tmp[81] = tmp[0] & tmp[136];
		tmp[41] = ~tmp[102];
		tmp[58] = tmp[130] & tmp[136];
		tmp[24] = tmp[0] ^ tmp[176];
		tmp[25] = tmp[148] ^ (tmp[102] | tmp[165] ^ tmp[58]);
		tmp[103] = (tmp[132] | tmp[169]) ^ (tmp[169] ^ tmp[68]) ^ tmp[147] & ~(tmp[8] & tmp[114] ^ (tmp[141] & tmp[59] ^ tmp[141] & (tmp[63] & ~tmp[103]))) ^ (tmp[15]
				^ (tmp[96] ^ (tmp[132] | tmp[141] & tmp[114]) ^ tmp[147] & (tmp[22] ^ (tmp[132] | tmp[166]))) & ~tmp[88]);
		tmp[96] = tmp[100] | tmp[103];
		tmp[169] = ~tmp[103];
		tmp[15] = tmp[138] & tmp[169];
		tmp[185] = tmp[75] & tmp[15];
		tmp[186] = tmp[138] | tmp[103];
		tmp[187] = tmp[167] ^ tmp[186];
		tmp[188] = tmp[46] | tmp[103];
		tmp[189] = ~tmp[103];
		tmp[190] = tmp[46] ^ tmp[186];
		tmp[187] =
				tmp[132] ^ ((tmp[93] | tmp[187] ^ tmp[164] & tmp[187] ^ tmp[178] & (tmp[185] ^ tmp[164] & tmp[15])) ^ (tmp[115] ^ tmp[164] & (tmp[154] ^ tmp[96]) ^ tmp[129] & tmp[169] ^ tmp[178] & ~(tmp[115]
						^ tmp[164] & tmp[190])));
		tmp[191] = tmp[46] & tmp[189];
		tmp[192] = tmp[138] & tmp[189];
		tmp[100] = tmp[164] & (tmp[100] ^ tmp[192]);
		tmp[115] = (tmp[129] ^ tmp[103] ^ tmp[178] & (tmp[164] & ~(tmp[115] ^ tmp[15]) ^ (tmp[138] ^ tmp[188])) ^ tmp[164] & ~(tmp[70] ^ (tmp[154] | tmp[103]))) & ~tmp[93] ^ (tmp[65] ^ (
				tmp[178] & (tmp[129] ^ tmp[46] & tmp[169] ^ tmp[164] & ~(tmp[129] ^ tmp[192])) ^ ((tmp[167] | tmp[103]) ^ tmp[100])));
		tmp[15] = tmp[28] ^ tmp[150] & (tmp[185] ^ (tmp[129] ^ tmp[164] & (tmp[46] ^ tmp[185])) ^ tmp[178] & (tmp[129] ^ tmp[96] ^ tmp[164] & ~(tmp[138] ^ tmp[15]))) ^ (tmp[129] ^ tmp[167] & tmp[169]
				^ tmp[164] & ~tmp[185] ^ tmp[178] & ~tmp[100]);
		tmp[186] =
				tmp[11] ^ (tmp[154] ^ (tmp[129] | tmp[103])) ^ (tmp[164] & ~(tmp[129] ^ tmp[186]) ^ tmp[178] & ~(tmp[164] & ~tmp[190] ^ tmp[70] & tmp[189])) ^ (tmp[93] | tmp[191] ^ (tmp[167] ^ tmp[164] & (
						tmp[154] ^ tmp[188])) ^ tmp[178] & (tmp[70] ^ tmp[191] ^ tmp[164] & ~(tmp[70] ^ tmp[186])));
		tmp[114] =
				tmp[105] ^ (tmp[27] ^ (tmp[141] ^ tmp[63] & (tmp[68] ^ tmp[66])) ^ tmp[147] & ~(tmp[132] & tmp[68])) ^ (tmp[22] ^ tmp[132] & ~(tmp[14] & tmp[141] ^ tmp[114]) ^ tmp[147] & (tmp[73] ^ tmp[166])
						| tmp[88]);
		tmp[106] = tmp[171] ^ (tmp[45] ^ (tmp[39] | tmp[161]) ^ (tmp[126] ^ tmp[144] & ~(tmp[142] ^ (tmp[33] ^ tmp[33] & tmp[106]))))
				^ (tmp[172] ^ (tmp[132] ^ (tmp[39] | tmp[45])) ^ tmp[144] & (tmp[132] ^ tmp[30] & tmp[142] ^ (tmp[39] | tmp[157] ^ tmp[21]))) & tmp[88];
		tmp[33] = ~(tmp[151] ^ tmp[34]) & tmp[106];
		tmp[45] = tmp[178] & tmp[106];
		tmp[171] = ~tmp[178];
		tmp[66] = tmp[34] & tmp[106];
		tmp[50] = tmp[119] ^ (tmp[157] ^ ((tmp[39] | tmp[82]) ^ tmp[30] & tmp[82]) ^ tmp[144] & ~tmp[50]) ^ tmp[88] & ~(tmp[125] ^ (tmp[126] ^ (tmp[144] & tmp[50] ^ (tmp[39] | tmp[83] ^ (tmp[126]
				| tmp[82])))));
		tmp[125] = ~tmp[50];
		tmp[119] = ~tmp[40];
		tmp[166] = tmp[20] | tmp[50];
		tmp[73] = tmp[69] ^ (tmp[59] ^ tmp[134] & ~tmp[14] ^ (tmp[147] & (tmp[141] ^ tmp[73]) ^ tmp[63] & tmp[101]))
				^ (tmp[141] & tmp[26] ^ (tmp[147] & ~(tmp[132] | tmp[141]) ^ (tmp[132] | tmp[101]))) & ~tmp[88];
		tmp[101] = tmp[180] ^ (tmp[98] ^ tmp[17] | tmp[73]);
		tmp[63] = tmp[92] ^ tmp[116] & tmp[67] & tmp[73];
		tmp[134] = ~tmp[73];
		tmp[26] = tmp[182] ^ (tmp[74] ^ tmp[174]) & tmp[134];
		tmp[98] = tmp[131] & ~tmp[98] ^ tmp[92] & tmp[134];
		tmp[179] = tmp[182] ^ (tmp[174] ^ tmp[179]) & tmp[73];
		tmp[116] = tmp[36] ^ tmp[17] ^ (tmp[131] & tmp[116] | tmp[73]);
		tmp[180] = tmp[67] ^ tmp[17] ^ (tmp[180] | tmp[73]);
		tmp[85] = tmp[181] ^ tmp[74] & tmp[131] ^ (tmp[131] & tmp[85] | tmp[73]);
		tmp[35] =
				tmp[95] ^ (tmp[139] ^ tmp[157] ^ (tmp[39] | tmp[126] ^ tmp[161]) ^ tmp[144] & ~(tmp[139] ^ (tmp[39] | tmp[172])) ^ tmp[88] & ~(tmp[6] & tmp[161] ^ tmp[30] & tmp[35] ^ tmp[144] & (tmp[161]
						^ tmp[39] & (tmp[161] ^ tmp[35]))));
		tmp[161] = tmp[104] & tmp[35];
		tmp[30] = ~tmp[104];
		tmp[139] = tmp[35] & tmp[30];
		tmp[157] = ~tmp[19];
		tmp[181] = tmp[35] & tmp[157];
		tmp[30] &= tmp[181];
		tmp[17] = tmp[19] | tmp[35];
		tmp[36] = ~tmp[35];
		tmp[174] = tmp[104] & tmp[36];
		tmp[182] = tmp[36] & (tmp[104] & tmp[157]);
		tmp[134] = tmp[104] ^ tmp[35];
		tmp[92] = tmp[157] & tmp[134];
		tmp[59] = tmp[35] ^ (tmp[104] ^ tmp[17]);
		tmp[181] = tmp[114] & (tmp[35] ^ tmp[181]);
		tmp[69] = tmp[19] ^ tmp[35];
		tmp[22] = tmp[104] | tmp[35];
		tmp[27] = tmp[157] & tmp[22];
		tmp[36] &= tmp[22];
		tmp[70] = tmp[19] | tmp[36];
		tmp[190] = tmp[17] ^ tmp[36];
		tmp[21] =
				tmp[140] ^ (tmp[83] ^ (tmp[39] ^ tmp[82]) ^ tmp[144] & (tmp[126] | (tmp[132] | tmp[39]))) ^ tmp[88] & ~(tmp[142] ^ tmp[6] & (tmp[126] ^ tmp[172]) ^ (tmp[83] | tmp[142]) ^ tmp[144] & ~(tmp[83]
						^ (tmp[39] | tmp[126] ^ tmp[21])));
		tmp[148] = tmp[14] ^ (tmp[145] ^ (tmp[165] ^ tmp[159] & (tmp[148] ^ (tmp[102] | tmp[130] ^ (tmp[165] | tmp[145]))) ^ tmp[102] & tmp[176])) ^ (tmp[25] ^ (tmp[151] | tmp[25]) | tmp[21]);
		tmp[176] = ~tmp[21];
		tmp[58] = tmp[124] ^ (tmp[0] ^ (tmp[117] ^ (tmp[151] | tmp[130] ^ tmp[102] & tmp[136])) ^ (tmp[102] | tmp[81]))
				^ (tmp[152] ^ (tmp[165] ^ (tmp[151] | tmp[143] ^ tmp[145] ^ (tmp[102] | tmp[51] ^ tmp[58]))) ^ tmp[41] & (tmp[0] ^ tmp[81])) & tmp[176];
		tmp[47] = tmp[113] ^ (tmp[165] ^ (tmp[151] | tmp[143] ^ tmp[152] ^ tmp[81] & tmp[41]) ^ (tmp[102] | tmp[24]) ^ (
				tmp[130] ^ tmp[159] & (tmp[117] ^ (tmp[51] ^ (tmp[102] | tmp[47] ^ tmp[107]))) ^ (tmp[102] | tmp[156] ^ tmp[109]) | tmp[21]));
		tmp[117] = tmp[186] | tmp[47];
		tmp[81] = ~tmp[186];
		tmp[152] = tmp[186] & tmp[47];
		tmp[107] = tmp[39] ^ ((tmp[102] | tmp[51]) ^ (tmp[165] ^ tmp[145] ^ (tmp[151] | tmp[43] ^ tmp[41] & (tmp[0] ^ tmp[107]))))
				^ (tmp[156] ^ tmp[159] & (tmp[51] ^ tmp[41] & tmp[24]) ^ (tmp[102] | tmp[43] ^ tmp[109])) & tmp[176];
		tmp[38] =
				tmp[155] ^ (tmp[95] ^ (tmp[149] ^ (tmp[78] | tmp[10] ^ tmp[55])) ^ tmp[118] & (tmp[153] ^ tmp[16] & (tmp[10] ^ tmp[146]))) ^ tmp[105] & (tmp[2] ^ (tmp[78] | tmp[2] & tmp[38] ^ tmp[9]) ^ (
						tmp[111] | tmp[146]) ^ tmp[118] & ~(tmp[31] ^ tmp[9] & ~tmp[78]));
		tmp[2] = ~tmp[68];
		tmp[9] = tmp[38] & tmp[2];
		tmp[146] = tmp[38] & ~tmp[122];
		tmp[10] = tmp[68] | tmp[146];
		tmp[55] = ~tmp[124];
		tmp[31] = tmp[2] & tmp[146];
		tmp[16] = tmp[122] | tmp[146];
		tmp[111] = tmp[122] ^ tmp[38];
		tmp[153] = ~tmp[141];
		tmp[149] = tmp[2] & tmp[111];
		tmp[118] = ~tmp[77];
		tmp[95] = tmp[146] & (tmp[124] & tmp[2]);
		tmp[105] = tmp[122] & ~tmp[38];
		tmp[155] = tmp[122] & tmp[38];
		tmp[24] = tmp[68] | tmp[38];
		tmp[41] = tmp[122] | tmp[38];
		tmp[8] = tmp[79] ^ tmp[68] ^ (tmp[155] ^ (tmp[124] | tmp[41])) ^ (tmp[141] | tmp[9] ^ tmp[146] & tmp[55]) ^ (tmp[77] | tmp[41] ^ (tmp[9] ^ (
				(tmp[141] | tmp[38] ^ (tmp[124] | tmp[8] & tmp[122] ^ tmp[111])) ^ (tmp[124] | tmp[38] ^ tmp[2] & tmp[16]))));
		tmp[86] =
				tmp[135] & tmp[53] ^ (tmp[93] ^ tmp[72]) ^ (tmp[60] ^ ((tmp[160] ^ tmp[86] | tmp[50]) ^ ~(tmp[53] & (tmp[163] ^ tmp[128]) ^ (tmp[135] ^ tmp[158] ^ (tmp[54] ^ tmp[170]) & tmp[125])) & tmp[8]));
		tmp[44] = (tmp[40] | tmp[93] ^ tmp[44]) ^ (tmp[84] ^ tmp[93] & tmp[75] ^ (tmp[144] ^ (tmp[158] & tmp[125] ^ (tmp[62] ^ tmp[53] & (tmp[29] ^ tmp[44]) ^ (tmp[84] ^ tmp[128] | tmp[50])) & tmp[8])));
		tmp[128] = ~tmp[8];
		tmp[62] = tmp[166] ^ (tmp[29] ^ tmp[135] & tmp[75] ^ tmp[53] & tmp[20]) ^ (tmp[133] ^ ~((tmp[20] ^ (tmp[84] | tmp[50])) & tmp[119] ^ tmp[50] & ~tmp[62]) & tmp[8]);
		tmp[90] = tmp[84] ^ tmp[90] | tmp[8];
		tmp[61] = tmp[73] & (tmp[61] ^ tmp[49] & tmp[128]) ^ (tmp[13] ^ (tmp[147] ^ tmp[8] & ~tmp[49])) ^ (tmp[94] | tmp[67] ^ tmp[73] & ~(tmp[61] | tmp[8]) ^ tmp[49] & tmp[8]);
		tmp[175] =
				tmp[56] ^ (tmp[49] ^ (tmp[120] | tmp[89])) ^ (tmp[73] & ~(tmp[52] ^ (tmp[5] ^ tmp[175]) & tmp[128]) ^ tmp[52] & tmp[128]) ^ (tmp[94] | tmp[184] ^ tmp[73] & (tmp[67] ^ tmp[175] ^ (tmp[67]
						| tmp[8])) ^ tmp[184] & tmp[128]);
		tmp[52] = tmp[186] | tmp[175];
		tmp[184] = tmp[81] & tmp[175];
		tmp[56] = ~tmp[47];
		tmp[147] = tmp[175] & tmp[56];
		tmp[20] = tmp[184] ^ tmp[147];
		tmp[75] = tmp[47] & tmp[175];
		tmp[135] = ~tmp[175];
		tmp[53] = tmp[52] ^ tmp[47] & tmp[135];
		tmp[29] = tmp[47] | tmp[175];
		tmp[56] &= tmp[29];
		tmp[133] = tmp[29] ^ (tmp[186] | tmp[75]);
		tmp[158] = tmp[47] ^ tmp[175];
		tmp[144] = tmp[186] | tmp[158];
		tmp[160] = tmp[81] & tmp[158];
		tmp[163] = tmp[54] ^ (tmp[84] | tmp[167]) ^ (tmp[163] ^ tmp[170] | tmp[50]) ^ (tmp[40] | tmp[84] ^ tmp[167] ^ (tmp[93] ^ (tmp[54] | tmp[167])) & tmp[125]) ^ (tmp[77] ^ tmp[8] & ~(tmp[84] ^ tmp[72]
				^ tmp[166] ^ tmp[119] & ((tmp[163] | tmp[167]) ^ (tmp[167] | tmp[50]))));
		tmp[3] =
				tmp[1] ^ (tmp[183] ^ (tmp[120] | tmp[67]) ^ (tmp[91] | tmp[8])) ^ ((tmp[67] ^ tmp[91] & tmp[128] ^ tmp[73] & (tmp[3] ^ (tmp[120] | tmp[3]) ^ tmp[123] & tmp[128])) & ~tmp[94] ^ tmp[73] & ~(
						tmp[13] ^ (tmp[120] ^ tmp[89]) & tmp[128]));
		tmp[89] = ~tmp[115];
		tmp[49] = tmp[5] ^ (tmp[120] | tmp[49]) ^ (tmp[12] ^ (tmp[49] ^ (tmp[120] | tmp[183]) | tmp[8])) ^ (tmp[73] & ~(tmp[18] ^ tmp[8] & ~(tmp[67] ^ tmp[37] & tmp[49])) ^ (tmp[94] | tmp[90] ^ (tmp[18]
				^ tmp[73] & (tmp[84] ^ tmp[173] ^ tmp[90]))));
		tmp[42] ^=
				tmp[118] & (tmp[95] ^ (tmp[168] ^ (tmp[141] | tmp[122] ^ tmp[55] & (tmp[38] ^ tmp[149])))) ^ (tmp[41] ^ (tmp[55] & (tmp[122] ^ tmp[10]) ^ tmp[2] & tmp[155]) ^ (tmp[141] | tmp[31] ^ (tmp[155]
						^ (tmp[124] | tmp[10] ^ tmp[111]))));
		tmp[95] = tmp[71] & tmp[42];
		tmp[37] = tmp[19] ^ tmp[42] & ~(tmp[19] | tmp[0]);
		tmp[173] = ~tmp[162];
		tmp[110] &= tmp[42];
		tmp[48] ^= tmp[83] ^ ((tmp[0] | tmp[99]) ^ tmp[127] & ~(tmp[177] & tmp[162])) ^ (tmp[42] ^ tmp[21] & (tmp[76] ^ tmp[19] & tmp[23] ^ tmp[42] & ~tmp[76] ^ tmp[127] & ~(tmp[0] ^ tmp[48]
				^ tmp[42] & ~tmp[99])));
		tmp[83] = ~tmp[107];
		tmp[90] = tmp[48] & tmp[83];
		tmp[183] = tmp[42] & ~tmp[0];
		tmp[18] = tmp[42] & ~tmp[137];
		tmp[12] = tmp[141] ^ tmp[71] ^ tmp[0] & tmp[42] ^ (tmp[162] | tmp[76] ^ tmp[18]) ^ tmp[127] & (tmp[95] ^ (tmp[162] | tmp[137] ^ tmp[110])) ^ tmp[21] & ~(tmp[162] & (tmp[76] ^ tmp[183]) ^ (tmp[137]
				^ tmp[127] & (tmp[137] & tmp[23] ^ tmp[95])));
		tmp[5] = tmp[58] & ~tmp[12];
		tmp[128] = tmp[12] ^ tmp[5];
		tmp[183] =
				tmp[64] ^ tmp[19] ^ (tmp[18] ^ tmp[173] & (tmp[71] ^ tmp[99] & tmp[42])) ^ tmp[21] & ~(tmp[37] & tmp[173] ^ tmp[19] & tmp[42] ^ tmp[127] & tmp[110]) ^ tmp[127] & ~(tmp[110] ^ tmp[162] & ~(
						tmp[137] ^ tmp[183]));
		tmp[157] = tmp[76] ^ (tmp[108] ^ (tmp[162] | tmp[95])) ^ tmp[127] & ~(tmp[42] ^ tmp[173] & (tmp[99] ^ tmp[95])) ^ tmp[21] & (tmp[37] ^ tmp[162] & ~(tmp[19] ^ tmp[157] & tmp[42]) ^ tmp[127] & (
				tmp[99] & tmp[23] ^ tmp[76] & tmp[42]));
		tmp[95] = tmp[135] & tmp[157];
		tmp[99] = ~tmp[62];
		tmp[31] = tmp[111] ^ (tmp[87] ^ ((tmp[124] | tmp[146] ^ tmp[31]) ^ (tmp[68] | tmp[105]))) ^ (tmp[141] | tmp[112] ^ tmp[155] ^ tmp[55] & (tmp[24] ^ tmp[41])) ^ (tmp[77]
				| tmp[55] & (tmp[2] & tmp[105]) ^ tmp[153] & (tmp[9] ^ tmp[124] & ~(tmp[122] ^ tmp[149])));
		tmp[146] = tmp[178] | tmp[31];
		tmp[2] = tmp[171] & tmp[31];
		tmp[155] = tmp[151] & ~tmp[31];
		tmp[112] = tmp[178] | tmp[155];
		tmp[87] = tmp[31] | tmp[155];
		tmp[77] = tmp[171] & tmp[87];
		tmp[76] = tmp[106] & ~tmp[112];
		tmp[23] = tmp[151] | tmp[146];
		tmp[173] = tmp[151] & tmp[31];
		tmp[37] = tmp[173] ^ tmp[151] & tmp[2];
		tmp[108] = tmp[151] ^ tmp[31];
		tmp[137] = tmp[171] & tmp[108];
		tmp[137] = tmp[151] ^ (tmp[80] ^ tmp[23]) ^ tmp[106] & (tmp[171] | ~tmp[155]) ^ ((tmp[145] | tmp[106] & tmp[37] ^ (tmp[37] ^ tmp[138] & (tmp[151] & ~tmp[178] & tmp[106] ^ (tmp[173] ^ tmp[137]))))
				^ tmp[138] & ~(tmp[2] ^ tmp[106] & (tmp[31] ^ tmp[137])));
		tmp[173] = tmp[3] | tmp[137];
		tmp[37] = tmp[115] & tmp[173];
		tmp[80] = tmp[3] & tmp[137];
		tmp[110] = tmp[31] & ~tmp[151];
		tmp[71] = ~tmp[145];
		tmp[171] =
				tmp[76] ^ (tmp[4] ^ tmp[138] & ~(tmp[178] ^ tmp[45]) ^ (tmp[146] ^ tmp[108])) ^ tmp[71] & (tmp[151] ^ tmp[2] ^ (tmp[138] & (tmp[110] ^ (tmp[178] ^ tmp[106] & tmp[171])) ^ tmp[106] & ~(tmp[151]
						^ tmp[112])));
		tmp[2] = tmp[175] ^ tmp[171];
		tmp[4] = tmp[157] & ~tmp[2];
		tmp[18] = tmp[2] ^ tmp[4];
		tmp[64] = tmp[175] | tmp[171];
		tmp[123] = ~tmp[171];
		tmp[91] = tmp[175] & tmp[123];
		tmp[13] = tmp[175] & tmp[157];
		tmp[123] &= tmp[13];
		tmp[1] = tmp[157] & ~tmp[91];
		tmp[72] = tmp[171] | tmp[91];
		tmp[54] = tmp[135] & tmp[171];
		tmp[119] = ~tmp[54];
		tmp[166] = tmp[171] & tmp[119];
		tmp[125] = tmp[175] & tmp[171];
		tmp[13] &= tmp[171];
		tmp[170] = tmp[125] ^ tmp[13];
		tmp[66] = tmp[155] ^ (tmp[7] ^ (tmp[77] ^ tmp[106] & (tmp[23] ^ tmp[110]))) ^ tmp[138] & ~(tmp[106] & ~tmp[34] ^ (tmp[31] ^ (tmp[178] | tmp[108]))) ^ (tmp[145] | tmp[66] ^ (tmp[110] ^ (tmp[77]
				^ tmp[138] & ~(tmp[112] ^ (tmp[66] ^ tmp[110])))));
		tmp[108] = tmp[3] & tmp[66];
		tmp[87] = tmp[68] ^ tmp[45] ^ (tmp[112] ^ tmp[110] ^ tmp[138] & (tmp[33] ^ tmp[31] ^ (tmp[178] | tmp[110]))) ^ tmp[71] & (tmp[76] ^ (tmp[146] ^ tmp[138] & ~(tmp[112] ^ (tmp[33] ^ tmp[87]))));
		tmp[33] = tmp[148] & ~tmp[87];
		tmp[112] = tmp[87] & ~tmp[148];
		tmp[110] = tmp[148] | tmp[112];
		tmp[146] = tmp[148] ^ tmp[87];
		tmp[76] = tmp[124] | tmp[10];
		tmp[111] =
				tmp[55] & (tmp[16] ^ tmp[149]) ^ (tmp[10] ^ (tmp[78] ^ tmp[105])) ^ tmp[153] & (tmp[9] ^ (tmp[124] | tmp[105] ^ (tmp[68] | tmp[41])) ^ tmp[122] & ~tmp[105]) ^ tmp[118] & (tmp[76] ^ (tmp[168]
						^ (tmp[141] | tmp[24] ^ (tmp[38] ^ tmp[55] & (tmp[38] ^ (tmp[68] | tmp[111]))))));
		tmp[68] = ~tmp[111];
		tmp[126] ^= tmp[101] ^ ((tmp[116] | tmp[111]) ^ tmp[35] & ~(tmp[63] ^ tmp[98] & tmp[68]));
		tmp[55] = ~tmp[126];
		tmp[41] = tmp[107] & tmp[55];
		tmp[105] = tmp[107] | tmp[126];
		tmp[24] = tmp[55] & tmp[105];
		tmp[124] = tmp[48] & ~tmp[105];
		tmp[141] = tmp[48] & tmp[55];
		tmp[9] = tmp[48] & tmp[126];
		tmp[78] = tmp[105] ^ tmp[141];
		tmp[149] = tmp[107] ^ tmp[9];
		tmp[16] = tmp[107] ^ tmp[126];
		tmp[168] = tmp[48] & tmp[16];
		tmp[10] = tmp[126] ^ tmp[168];
		tmp[76] = tmp[107] & tmp[126];
		tmp[98] = tmp[101] ^ (tmp[122] ^ tmp[116] & tmp[111]) ^ tmp[35] & ~(tmp[63] ^ tmp[98] & tmp[111]);
		tmp[63] = ~tmp[98];
		tmp[116] = tmp[62] | tmp[98];
		tmp[122] = tmp[157] | tmp[116];
		tmp[101] = tmp[99] & tmp[98];
		tmp[153] = tmp[99] & (tmp[157] & tmp[98]);
		tmp[118] = tmp[98] & ~tmp[157];
		tmp[45] = tmp[99] & tmp[118];
		tmp[71] = ~tmp[15];
		tmp[34] = tmp[118] ^ tmp[45];
		tmp[23] = tmp[157] ^ tmp[98];
		tmp[77] = tmp[157] & tmp[63];
		tmp[7] = tmp[62] | tmp[77];
		tmp[155] = tmp[101] ^ tmp[77];
		tmp[101] =
				tmp[102] ^ (tmp[98] ^ (tmp[58] & tmp[116] ^ ((tmp[15] | tmp[101] ^ tmp[58] & (tmp[157] ^ tmp[157] & tmp[99])) ^ (tmp[62] | tmp[157])))) ^ ~tmp[49] & (tmp[157] ^ tmp[7] ^ tmp[58] & ~tmp[101]
						^ tmp[45] & tmp[71]);
		tmp[102] = tmp[98] | tmp[77];
		tmp[60] = tmp[99] & tmp[102];
		tmp[74] ^= tmp[157] ^ (tmp[62] | tmp[118]) ^ tmp[58] & tmp[153] ^ (tmp[15] | tmp[155] ^ (tmp[58] | tmp[77] ^ tmp[60])) ^ (tmp[49] | tmp[71] & tmp[77] ^ (tmp[62] | tmp[98] & ~tmp[118]) & ~tmp[58]);
		tmp[122] = tmp[127] ^ tmp[71] & (tmp[23] ^ (tmp[122] ^ tmp[58] & (tmp[157] ^ (tmp[62] | tmp[23])))) ^ (tmp[62] ^ tmp[77] ^ tmp[58] & ~tmp[34] ^ (tmp[49] | tmp[102] ^ tmp[58] & tmp[45] ^ (tmp[15]
				| tmp[122] ^ tmp[58] & tmp[63])));
		tmp[60] = tmp[93] ^ (tmp[116] ^ tmp[118] ^ tmp[58] & ~(tmp[7] ^ tmp[102]) ^ tmp[71] & (tmp[155] ^ tmp[58] & ~(tmp[23] ^ tmp[60])) ^ (tmp[49] | tmp[71] & tmp[34] ^ (tmp[153] ^ tmp[118]
				^ tmp[58] & ~tmp[7])));
		tmp[121] ^= tmp[35] & (tmp[179] ^ tmp[111] & ~tmp[26]) ^ (tmp[180] ^ tmp[111] & ~tmp[85]);
		tmp[23] = ~tmp[121];
		tmp[97] ^= tmp[59] ^ tmp[114] & ~(tmp[104] ^ tmp[70]) ^ (tmp[139] ^ (tmp[104] | tmp[114])) & tmp[68] ^ (tmp[127]
				| (tmp[104] ^ (tmp[19] | tmp[104])) & tmp[114] ^ tmp[30] ^ (tmp[139] ^ tmp[114] & ~tmp[22]) & tmp[68]);
		tmp[7] = tmp[66] | tmp[97];
		tmp[102] = ~tmp[97];
		tmp[118] = tmp[7] & tmp[102];
		tmp[153] = tmp[66] ^ tmp[3] & tmp[7] ^ (tmp[44] | tmp[108] ^ tmp[118]);
		tmp[34] = ~tmp[44];
		tmp[71] = tmp[66] & tmp[97];
		tmp[155] = tmp[97] & ~tmp[71];
		tmp[116] = tmp[3] & ~tmp[155];
		tmp[93] = tmp[97] ^ tmp[116] ^ tmp[7] & tmp[34];
		tmp[45] = tmp[3] & tmp[97];
		tmp[77] = tmp[66] ^ tmp[97];
		tmp[79] = tmp[3] & ~tmp[77];
		tmp[108] ^= tmp[66] ^ (tmp[97] ^ tmp[34] & (tmp[7] ^ tmp[79]));
		tmp[51] = tmp[155] ^ tmp[79];
		tmp[71] = tmp[66] ^ tmp[3] & tmp[71] ^ (tmp[44] | tmp[51]);
		tmp[7] = tmp[155] ^ tmp[3] & ~tmp[7] ^ tmp[34] & tmp[51];
		tmp[51] = tmp[66] ^ tmp[45];
		tmp[79] = tmp[44] | tmp[97] ^ tmp[79];
		tmp[155] = tmp[51] ^ tmp[79];
		tmp[43] = tmp[115] & tmp[108];
		tmp[102] = tmp[116] ^ tmp[77] ^ (tmp[44] | tmp[97] ^ tmp[3] & tmp[102]);
		tmp[178] ^= tmp[93] ^ tmp[115] & tmp[153] ^ tmp[107] & (tmp[71] ^ tmp[115] & tmp[102]);
		tmp[77] = tmp[60] & ~tmp[178];
		tmp[116] = tmp[60] ^ tmp[77];
		tmp[153] = tmp[107] & (tmp[71] ^ tmp[89] & tmp[102]) ^ (tmp[94] ^ (tmp[93] ^ (tmp[115] | tmp[153])));
		tmp[118] = tmp[34] & tmp[45] ^ (tmp[97] & ~tmp[66] ^ tmp[3] & ~tmp[118]);
		tmp[108] = tmp[130] ^ (tmp[155] ^ (tmp[115] | tmp[108]) ^ tmp[107] & ~(tmp[7] ^ tmp[89] & tmp[118]));
		tmp[70] ^= tmp[114] & tmp[174] ^ tmp[139] ^ (tmp[182] ^ (tmp[161] ^ tmp[114] & ~(tmp[19] ^ tmp[161])) | tmp[111]) ^ (tmp[38] ^ (tmp[127] | tmp[69] ^ tmp[114] & (tmp[22] ^ tmp[70])
				^ ((tmp[19] | tmp[134]) ^ (tmp[35] ^ (tmp[104] ^ tmp[181]))) & tmp[68]));
		tmp[118] = tmp[19] ^ (tmp[79] ^ (tmp[51] ^ tmp[43]) ^ tmp[107] & ~(tmp[7] ^ tmp[115] & tmp[118]));
		tmp[7] = tmp[58] & (tmp[12] & tmp[70]);
		tmp[43] = tmp[12] ^ tmp[70];
		tmp[51] = tmp[58] & tmp[43];
		tmp[79] = ~tmp[12];
		tmp[139] = tmp[58] & ~tmp[43];
		tmp[38] = tmp[70] & tmp[79];
		tmp[155] = ~tmp[38];
		tmp[130] = ~tmp[70];
		tmp[45] = tmp[58] & ~(tmp[70] & tmp[155]);
		tmp[34] = tmp[12] & tmp[130];
		tmp[102] = tmp[70] | tmp[34];
		tmp[93] = tmp[58] & tmp[34];
		tmp[71] = tmp[7] ^ tmp[34];
		tmp[94] = tmp[58] & ~tmp[34];
		tmp[109] = tmp[12] | tmp[70];
		tmp[159] = tmp[58] & ~tmp[109];
		tmp[93] = tmp[42] ^ tmp[70] ^ (tmp[58] & tmp[102] ^ tmp[98] & ~(tmp[58] & tmp[12] ^ tmp[34])) ^ tmp[163] & ~(tmp[12] & tmp[63] ^ tmp[159]) ^ (tmp[87] | tmp[109] ^ (tmp[7] ^ (
				tmp[163] & (tmp[70] ^ tmp[93] ^ tmp[98] & (tmp[70] ^ tmp[51])) ^ tmp[98] & (tmp[34] ^ tmp[93]))));
		tmp[63] = tmp[118] | tmp[93];
		tmp[42] = tmp[118] ^ tmp[93];
		tmp[109] = tmp[118] & ~tmp[93];
		tmp[165] = ~tmp[118];
		tmp[156] = tmp[93] & tmp[165];
		tmp[176] = ~tmp[87];
		tmp[130] &= tmp[58];
		tmp[128] =
				tmp[163] & (tmp[128] ^ tmp[98] & ~tmp[128]) ^ tmp[70] ^ (tmp[31] ^ tmp[7]) ^ tmp[98] & (tmp[45] ^ tmp[102]) ^ tmp[176] & (tmp[98] & ~(tmp[43] ^ tmp[130]) ^ (tmp[12] ^ (tmp[45] ^ tmp[163] & (
						tmp[159] ^ tmp[98] & ~(tmp[12] ^ tmp[139])))));
		tmp[51] = tmp[111] ^ (tmp[12] ^ tmp[130]) ^ tmp[98] & ~tmp[71] ^ tmp[163] & (tmp[58] & tmp[38] ^ (tmp[98] ^ tmp[102])) ^ (tmp[87] | tmp[45] ^ (tmp[34] ^ tmp[163] & ~(tmp[7] ^ tmp[38]
				^ tmp[98] & ~tmp[51])) ^ tmp[98] & ~(tmp[70] ^ tmp[58] & tmp[155]));
		tmp[5] = tmp[8] ^ tmp[12] ^ (tmp[94] ^ tmp[163] & ~(tmp[71] ^ tmp[98] & (tmp[5] ^ tmp[70]))) ^ tmp[98] & ~(tmp[70] ^ tmp[94]) ^ tmp[176] & (tmp[102] ^ tmp[163] & ~(tmp[70] ^ tmp[130]
				^ tmp[98] & tmp[139]));
		tmp[127] = ~tmp[127];
		tmp[69] =
				tmp[88] ^ (tmp[36] ^ (tmp[177] & tmp[104] ^ tmp[114] & ~tmp[30]) ^ (tmp[92] ^ tmp[114] & (tmp[19] ^ tmp[35] & ~tmp[161]) | tmp[111])) ^ tmp[127] & (tmp[190] ^ tmp[114] & ~(tmp[134] ^ tmp[92])
						^ (tmp[174] ^ (tmp[30] ^ tmp[114] & tmp[69])) & ~tmp[111]);
		tmp[92] = ~tmp[69];
		tmp[30] = tmp[148] & tmp[92];
		tmp[177] = tmp[112] & tmp[92];
		tmp[174] = tmp[110] & tmp[92];
		tmp[36] = tmp[112] | tmp[69];
		tmp[88] = tmp[87] ^ (tmp[87] | tmp[69]);
		tmp[139] = tmp[87] & tmp[92];
		tmp[130] = tmp[79] & tmp[139];
		tmp[88] =
				tmp[114] ^ tmp[148] ^ (tmp[36] ^ (tmp[12] | tmp[88])) ^ (tmp[187] & ~(tmp[87] ^ tmp[174] ^ tmp[79] & (tmp[112] ^ tmp[30]) ^ tmp[61] & ~(tmp[139] ^ tmp[79] & tmp[88])) ^ tmp[61] & (tmp[139]
						^ tmp[12] & tmp[69]));
		tmp[130] = tmp[145] ^ tmp[33] ^ (tmp[69] ^ (tmp[12] | tmp[177])) ^ tmp[61] & (tmp[87] ^ tmp[130]) ^ tmp[187] & (tmp[148] & tmp[87] ^ tmp[79] & tmp[177] ^ tmp[61] & ~(tmp[30] ^ tmp[130]));
		tmp[177] = tmp[69] & ~(tmp[107] ^ tmp[90]);
		tmp[174] = tmp[73] ^ (tmp[112] ^ tmp[61] & ~(tmp[12] & tmp[87])) ^ (tmp[33] | tmp[69]) ^ (tmp[187] & ~(tmp[79] & tmp[174] ^ tmp[61] & ~(tmp[69] ^ tmp[12] & ~(tmp[87] ^ tmp[69]))) ^ (tmp[12]
				| tmp[87] ^ tmp[33] & tmp[92]));
		tmp[33] = ~tmp[5];
		tmp[112] = tmp[174] & tmp[33];
		tmp[73] = ~tmp[44];
		tmp[145] = tmp[5] | tmp[174];
		tmp[78] =
				tmp[35] ^ tmp[10] ^ tmp[78] & tmp[69] ^ tmp[187] & (tmp[78] | tmp[69]) ^ tmp[73] & (tmp[90] ^ tmp[76] ^ tmp[187] & ~(tmp[48] ^ tmp[24] ^ tmp[41] & tmp[69]) ^ tmp[69] & ~(tmp[126] ^ tmp[124]));
		tmp[71] = tmp[88] | tmp[78];
		tmp[94] = tmp[88] ^ tmp[78];
		tmp[8] = ~tmp[88];
		tmp[102] = ~tmp[78];
		tmp[176] = ~tmp[51];
		tmp[38] = tmp[102] & (tmp[51] & tmp[8]);
		tmp[7] = tmp[51] | tmp[78];
		tmp[155] = tmp[78] & tmp[8];
		tmp[34] = tmp[176] & tmp[155];
		tmp[45] = tmp[8] & tmp[7];
		tmp[159] = tmp[176] & tmp[7];
		tmp[43] = tmp[88] | tmp[159];
		tmp[31] = tmp[51] & tmp[78];
		tmp[39] = tmp[51] ^ tmp[43];
		tmp[143] = tmp[51] & ~tmp[31];
		tmp[113] = tmp[71] ^ tmp[159];
		tmp[136] = tmp[51] ^ tmp[71];
		tmp[25] = tmp[51] ^ tmp[78];
		tmp[146] =
				tmp[103] ^ ((tmp[12] | tmp[148] ^ tmp[69]) ^ (tmp[87] ^ tmp[30]) ^ tmp[61] & ~(tmp[148] ^ tmp[79] & ((tmp[148] | tmp[87]) ^ (tmp[146] | tmp[69])) ^ (tmp[148] | tmp[69]))) ^ tmp[187] & ~(
						tmp[69] ^ tmp[12] & ~(tmp[110] ^ tmp[36]) ^ tmp[61] & (tmp[146] ^ tmp[79] & (tmp[148] ^ tmp[139])));
		tmp[9] =
				tmp[187] & (tmp[168] ^ (tmp[141] ^ tmp[16]) & tmp[69]) ^ (tmp[106] ^ (tmp[124] ^ tmp[16]) ^ tmp[69] & ~(tmp[76] ^ tmp[48] & ~tmp[16])) ^ tmp[73] & (tmp[187] & ~(tmp[149] ^ tmp[10] & tmp[69])
						^ (tmp[48] & tmp[105] ^ tmp[69] & ~(tmp[107] ^ tmp[83] & tmp[9])));
		tmp[83] = ~tmp[130];
		tmp[10] = tmp[9] & tmp[83];
		tmp[124] = tmp[130] ^ tmp[9];
		tmp[106] = ~tmp[10];
		tmp[73] = tmp[9] & tmp[106];
		tmp[139] = ~tmp[178];
		tmp[36] = tmp[130] & ~tmp[9];
		tmp[110] = tmp[130] & tmp[9];
		tmp[79] = ~tmp[36];
		tmp[168] =
				tmp[187] & ~(tmp[24] ^ tmp[168] ^ tmp[69] & ~(tmp[107] ^ tmp[168])) ^ (tmp[48] ^ tmp[16] ^ (tmp[21] ^ tmp[69])) ^ (tmp[44] | tmp[187] & ~(tmp[90] ^ tmp[105] ^ tmp[177]) ^ tmp[48] & (tmp[55]
						| tmp[76]) & ~tmp[69]);
		tmp[149] = tmp[105] ^ tmp[107] & tmp[48] ^ (tmp[50] ^ ((tmp[44] | tmp[69] & (tmp[107] ^ tmp[141] ^ tmp[187] & tmp[149])) ^ tmp[177])) ^ tmp[187] & (tmp[92] | ~tmp[41]);
		tmp[141] = ~tmp[60];
		tmp[41] = tmp[149] & tmp[141];
		tmp[177] = tmp[60] | tmp[149];
		tmp[190] = tmp[27] ^ (tmp[161] ^ tmp[114] & tmp[17]) ^ (tmp[32] ^ (tmp[27] ^ (tmp[181] ^ tmp[22]) | tmp[111])) ^ tmp[127] & (tmp[59] ^ tmp[114] & ~(tmp[182] ^ tmp[22]) ^ (
				tmp[134] ^ tmp[114] & ~tmp[190] ^ (tmp[19] | tmp[161]) | tmp[111]));
		tmp[114] = ~tmp[190];
		tmp[13] = tmp[138] ^ (tmp[2] ^ tmp[1] ^ tmp[121] & (tmp[175] ^ tmp[157] & ~tmp[166]) ^ (tmp[62] | tmp[4] ^ tmp[13] & tmp[121]))
				^ (tmp[123] ^ (tmp[91] ^ tmp[123] & tmp[121]) ^ (tmp[62] | tmp[64] ^ tmp[135] & tmp[121])) & tmp[114];
		tmp[2] = ~tmp[146];
		tmp[138] = tmp[13] & tmp[2];
		tmp[22] = tmp[141] & tmp[13];
		tmp[182] = tmp[139] & tmp[22];
		tmp[161] = tmp[60] | tmp[13];
		tmp[19] = tmp[178] | tmp[161];
		tmp[134] = tmp[60] ^ tmp[13];
		tmp[181] = tmp[161] ^ tmp[19];
		tmp[27] = tmp[19] ^ tmp[134];
		tmp[17] = ~tmp[13];
		tmp[59] = tmp[60] & tmp[17];
		tmp[32] = tmp[60] & tmp[13];
		tmp[127] = tmp[59] ^ tmp[139] & tmp[32];
		tmp[92] = tmp[13] | tmp[59];
		tmp[50] = tmp[139] & tmp[92];
		tmp[105] = tmp[139] & tmp[59];
		tmp[72] = tmp[67] ^ (tmp[64] ^ tmp[157] & tmp[119] ^ tmp[121] & ~(tmp[95] ^ tmp[54]) ^ (tmp[62] | tmp[170] ^ tmp[23] & (tmp[125] ^ tmp[157] & tmp[171]))) ^ (
				tmp[171] ^ tmp[157] & tmp[72] ^ tmp[121] & (tmp[95] ^ tmp[72]) ^ tmp[99] & tmp[170] | tmp[190]);
		tmp[119] = tmp[78] | tmp[72];
		tmp[67] = ~tmp[72];
		tmp[90] = tmp[119] & tmp[67];
		tmp[76] = tmp[78] & tmp[72];
		tmp[55] = tmp[72] & ~tmp[76];
		tmp[102] &= tmp[72];
		tmp[64] = ((tmp[62] | tmp[18]) ^ (tmp[170] ^ (tmp[175] ^ tmp[95]) & tmp[23])) & tmp[114] ^ (tmp[40] ^ (tmp[123] ^ tmp[54] ^ tmp[99] & (tmp[18] ^ tmp[121] & (tmp[125] ^ tmp[157] & tmp[54]))
				^ tmp[121] & ~(tmp[91] ^ tmp[157] & ~tmp[64])));
		tmp[91] = ~tmp[3];
		tmp[170] = (tmp[171] ^ tmp[1] ^ tmp[121] & (tmp[1] ^ tmp[125]) ^ tmp[99] & (tmp[4] ^ tmp[54] ^ tmp[95] & tmp[121]) | tmp[190]) ^ (tmp[162] ^ (tmp[157] ^ tmp[166] ^ tmp[121] & (tmp[135] | tmp[157])
				^ tmp[99] & (tmp[171] ^ tmp[123] ^ tmp[170] & tmp[121])));
		tmp[111] = tmp[57] ^ (tmp[180] ^ tmp[85] & tmp[68]) ^ tmp[35] & ~(tmp[179] ^ (tmp[26] | tmp[111]));
		tmp[26] = tmp[3] | tmp[111];
		tmp[179] = tmp[91] & tmp[26];
		tmp[68] = tmp[3] & tmp[111];
		tmp[85] = tmp[3] & ~tmp[68];
		tmp[180] = ~tmp[137];
		tmp[35] = tmp[137] | tmp[68];
		tmp[57] = tmp[3] & ~tmp[111];
		tmp[123] = tmp[180] & tmp[57];
		tmp[160] =
				(tmp[137] | tmp[20] ^ tmp[111] & ~(tmp[52] ^ tmp[147]) ^ tmp[190] & (tmp[147] ^ tmp[160] ^ tmp[111] & ~(tmp[117] ^ tmp[147]))) ^ (tmp[190] & (tmp[117] ^ tmp[160] & tmp[111]) ^ (tmp[151] ^ (
						tmp[158] ^ tmp[111] & ~(tmp[29] ^ (tmp[186] | tmp[29])))));
		tmp[151] = tmp[160] & ~tmp[73];
		tmp[73] = tmp[178] & ~(tmp[73] ^ tmp[79] & tmp[160]);
		tmp[135] = tmp[168] & tmp[160];
		tmp[95] = tmp[168] | tmp[160];
		tmp[54] = tmp[124] ^ tmp[160];
		tmp[4] = tmp[101] & tmp[95];
		tmp[83] &= tmp[160];
		tmp[125] = tmp[130] ^ tmp[83];
		tmp[1] = tmp[130] ^ tmp[36] & tmp[160];
		tmp[166] = tmp[10] ^ tmp[83];
		tmp[106] = tmp[171] ^ ((tmp[178] | tmp[110]) ^ (tmp[110] ^ tmp[106] & tmp[160]) ^ tmp[128] & ~(tmp[178] & tmp[1]) ^ (tmp[13] | tmp[166] ^ tmp[139] & tmp[110] ^ tmp[128] & ~tmp[125]));
		tmp[36] =
				tmp[87] ^ (tmp[54] ^ tmp[178] & tmp[79] ^ tmp[128] & ~(tmp[160] ^ tmp[178] & ~(tmp[130] ^ tmp[160] & (tmp[9] | tmp[36])))) ^ (tmp[13] | tmp[73] ^ tmp[1] ^ tmp[128] & (tmp[125] ^ tmp[178] & ~(
						tmp[130] & tmp[160])));
		tmp[83] = tmp[66] ^ tmp[130] ^ (tmp[178] | tmp[10] ^ tmp[160] & ~(tmp[130] | tmp[9])) ^ (tmp[151] ^ tmp[128] & (tmp[178] | tmp[124] ^ tmp[160] & ~tmp[124])) ^ (tmp[13]
				| tmp[73] ^ tmp[166] ^ tmp[128] & (tmp[125] ^ tmp[178] & ~tmp[83]));
		tmp[166] = tmp[168] ^ tmp[160];
		tmp[124] =
				tmp[137] ^ (tmp[54] ^ tmp[139] & (tmp[10] & tmp[160]) ^ tmp[128] & (tmp[160] ^ (tmp[178] | tmp[110] & tmp[160]))) ^ tmp[17] & (tmp[1] ^ tmp[128] & ~(tmp[125] ^ (tmp[178] | tmp[124])) ^ (
						tmp[178] | tmp[124] ^ tmp[151]));
		tmp[147] =
				tmp[167] ^ (tmp[133] ^ tmp[111] & ~tmp[75] ^ tmp[190] & ~(tmp[56] ^ tmp[81] & tmp[75] ^ tmp[111] & ~(tmp[117] ^ tmp[175])) ^ tmp[180] & (tmp[47] ^ tmp[190] & (tmp[20] ^ tmp[111] & ~(tmp[147]
						^ tmp[144]))));
		tmp[117] = ~tmp[147];
		tmp[29] =
				tmp[186] ^ tmp[75] ^ tmp[53] & tmp[111] ^ (tmp[120] ^ (tmp[180] & (tmp[29] ^ tmp[111] & ~(tmp[47] ^ tmp[52]) ^ tmp[190] & ~(tmp[53] ^ (tmp[52] ^ tmp[29]) & tmp[111])) ^ tmp[190] & ~(tmp[52]
						^ tmp[158] ^ tmp[47] & tmp[111])));
		tmp[91] &= tmp[111];
		tmp[52] = tmp[180] & tmp[91];
		tmp[91] ^= tmp[137] | tmp[85];
		tmp[56] = tmp[104] ^ (tmp[111] & ~tmp[133] ^ (tmp[144] ^ tmp[47] & ~tmp[75] ^ tmp[190] & ~(tmp[152] ^ (tmp[47] ^ (tmp[186] | tmp[56])) & tmp[111]))) ^ (tmp[137] | tmp[152] ^ tmp[190] & (tmp[152]
				^ (tmp[47] ^ tmp[184]) & tmp[111]));
		tmp[184] = tmp[56] & ~(tmp[71] ^ tmp[25]);
		tmp[184] =
				tmp[70] ^ (tmp[136] ^ (tmp[34] ^ tmp[25] | tmp[56])) ^ tmp[122] & ~(tmp[51] ^ (tmp[88] | tmp[31]) ^ tmp[184]) ^ tmp[118] & ~(tmp[45] ^ tmp[122] & (tmp[39] ^ tmp[184]) ^ tmp[56] & ~tmp[113]);
		tmp[70] = tmp[36] & ~tmp[184];
		tmp[152] = tmp[36] & ~tmp[70];
		tmp[75] = tmp[184] & ~tmp[36];
		tmp[144] = tmp[36] | tmp[75];
		tmp[133] = tmp[36] | tmp[184];
		tmp[104] = tmp[36] ^ tmp[184];
		tmp[159] =
				tmp[69] ^ (tmp[78] ^ (tmp[51] ^ tmp[155]) ^ tmp[56] & ~tmp[39] ^ tmp[122] & ~(tmp[38] ^ tmp[143] ^ (tmp[88] ^ tmp[143]) & tmp[56]) ^ tmp[118] & ~(tmp[78] ^ (tmp[71] ^ tmp[143]) & tmp[56]
						^ tmp[122] & ~(tmp[7] ^ (tmp[88] | tmp[25]) ^ (tmp[88] ^ tmp[159]) & tmp[56])));
		tmp[94] = tmp[97] ^ (tmp[43] ^ tmp[31] ^ (tmp[7] ^ tmp[34]) & tmp[56] ^ tmp[122] & (tmp[56] | ~(tmp[51] ^ tmp[45])) ^ tmp[118] & ~(tmp[155] & tmp[56] ^ tmp[122] & (tmp[94] ^ tmp[94] & tmp[56])));
		tmp[45] = tmp[111] & tmp[180];
		tmp[31] =
				tmp[7] ^ tmp[8] & tmp[31] ^ tmp[190] ^ (tmp[56] & ~(tmp[51] ^ tmp[38]) ^ tmp[122] & (tmp[113] ^ (tmp[38] ^ tmp[31]) & tmp[56])) ^ tmp[118] & (tmp[31] ^ tmp[8] & tmp[25] ^ tmp[136] & tmp[56]
						^ tmp[122] & (tmp[31] ^ (tmp[88] | tmp[7]) ^ (tmp[71] ^ tmp[31]) & tmp[56]));
		tmp[57] ^= tmp[45];
		tmp[37] = tmp[183] & ~(tmp[115] & ~tmp[80] ^ tmp[123]) ^ (tmp[115] & tmp[80] ^ tmp[91]) ^ (tmp[131] ^ tmp[86] & ~(tmp[37] ^ (tmp[111] ^ tmp[45]) ^ tmp[183] & (tmp[45] ^ (tmp[37] ^ tmp[26]))));
		tmp[80] = ~tmp[37];
		tmp[131] = tmp[78] & tmp[67] & tmp[80];
		tmp[71] = tmp[90] ^ tmp[131];
		tmp[7] = tmp[102] ^ (tmp[72] | tmp[37]);
		tmp[38] = ~tmp[74];
		tmp[25] = tmp[78] ^ tmp[37];
		tmp[8] = tmp[38] & (tmp[7] ^ (tmp[51] | tmp[25]));
		tmp[136] = tmp[55] ^ tmp[131];
		tmp[131] ^= tmp[119];
		tmp[25] = tmp[74] | tmp[7] ^ tmp[51] & ~tmp[25];
		tmp[7] = tmp[78] ^ (tmp[55] | tmp[37]);
		tmp[113] = tmp[90] | tmp[37];
		tmp[102] = tmp[78] ^ tmp[72] ^ tmp[102] & tmp[80];
		tmp[119] ^= tmp[72] & tmp[80];
		tmp[90] ^= tmp[76] | tmp[37];
		tmp[190] = tmp[113] ^ (tmp[55] ^ (tmp[111] ^ (tmp[51] | tmp[90]))) ^ ((tmp[174] | tmp[37] ^ tmp[8] ^ (tmp[51] | tmp[71])) ^ (tmp[74] | tmp[78] ^ (tmp[51] | tmp[102])));
		tmp[102] = tmp[113] ^ (tmp[121] ^ tmp[55]) ^ ((tmp[174] | tmp[37] ^ tmp[25] ^ tmp[51] & ~tmp[71]) ^ tmp[38] & (tmp[78] ^ tmp[51] & ~tmp[102]) ^ tmp[51] & tmp[90]);
		tmp[80] = tmp[55] ^ tmp[76] & tmp[80];
		tmp[176] = tmp[126] ^ (tmp[90] ^ (tmp[51] | tmp[119])) ^ ((tmp[74] | tmp[131] ^ (tmp[51] | tmp[136])) ^ (tmp[174] | tmp[8] ^ tmp[80] ^ tmp[176] & tmp[7]));
		tmp[8] = ~tmp[174];
		tmp[136] = tmp[98] ^ tmp[90] ^ (tmp[38] & (tmp[131] ^ tmp[51] & ~tmp[136]) ^ tmp[51] & tmp[119]) ^ tmp[8] & (tmp[25] ^ tmp[80] ^ tmp[51] & tmp[7]);
		tmp[131] = tmp[3] ^ tmp[111];
		tmp[7] = tmp[180] & tmp[131];
		tmp[35] =
				tmp[57] ^ (tmp[84] ^ ((tmp[115] | tmp[85] ^ tmp[123]) ^ tmp[183] & ~(tmp[173] ^ tmp[131] ^ tmp[115] & (tmp[111] ^ tmp[35])))) ^ tmp[86] & (tmp[85] ^ tmp[35] ^ tmp[183] & (tmp[115] & tmp[3]
						^ tmp[57]) ^ tmp[115] & (tmp[131] ^ tmp[7]));
		tmp[57] = tmp[149] ^ tmp[35];
		tmp[84] = tmp[149] & tmp[35];
		tmp[8] &= tmp[35];
		tmp[80] = tmp[33] & tmp[8];
		tmp[25] = tmp[174] | tmp[8];
		tmp[119] = tmp[141] & tmp[35];
		tmp[38] = ~tmp[35];
		tmp[90] = tmp[149] & tmp[38];
		tmp[98] = tmp[90] ^ (tmp[60] | tmp[57]);
		tmp[126] = tmp[149] & ~tmp[90];
		tmp[38] &= tmp[174];
		tmp[76] = tmp[33] & tmp[38];
		tmp[55] = tmp[5] | tmp[35];
		tmp[71] = tmp[8] ^ tmp[55];
		tmp[121] = tmp[33] & tmp[35];
		tmp[113] = tmp[174] & tmp[121] ^ tmp[174] & ~tmp[38];
		tmp[155] = tmp[60] | tmp[35];
		tmp[34] = tmp[174] ^ tmp[35];
		tmp[71] = (tmp[174] | tmp[72]) ^ (tmp[112] ^ (tmp[3] ^ tmp[34])) ^ tmp[153] & (tmp[71] ^ (tmp[72] | tmp[8] ^ tmp[76])) ^ ~tmp[29] & (tmp[145] ^ tmp[174] & tmp[67] ^ tmp[153] & ~(tmp[71]
				^ tmp[67] & tmp[8]));
		tmp[8] = tmp[174] ^ (tmp[49] ^ ((tmp[72] | tmp[25] ^ (tmp[5] | tmp[34])) ^ (tmp[5] | tmp[38]))) ^ tmp[153] & ~(tmp[113] ^ tmp[67] & tmp[35]) ^ (tmp[29]
				| tmp[153] & (tmp[76] ^ tmp[67] & tmp[25]) ^ tmp[33] & tmp[25] ^ tmp[67] & (tmp[35] ^ (tmp[5] | tmp[8])));
		tmp[49] = tmp[136] & ~tmp[8];
		tmp[43] = tmp[8] & ~tmp[136];
		tmp[97] = tmp[136] | tmp[43];
		tmp[143] = tmp[136] & ~tmp[49];
		tmp[34] ^= tmp[5];
		tmp[80] = tmp[61] ^ tmp[34] ^ (tmp[67] & (tmp[35] ^ tmp[80]) ^ tmp[153] & (tmp[72] | tmp[35] ^ tmp[121])) ^ (tmp[29] | tmp[174] & tmp[35] ^ (tmp[80] ^ tmp[153] & tmp[80]) ^ (tmp[72] | tmp[35]));
		tmp[121] = tmp[35] & ~tmp[149];
		tmp[61] = tmp[141] & (tmp[149] | tmp[121]);
		tmp[39] = tmp[5] | tmp[149] ^ tmp[119];
		tmp[69] = tmp[141] & tmp[84];
		tmp[39] = tmp[163] ^ tmp[98] ^ tmp[5] & (tmp[141] & tmp[57]) ^ (tmp[117] & (tmp[69] ^ (tmp[121] ^ (tmp[5] | tmp[35] ^ tmp[61]))) ^ tmp[64] & (tmp[121] ^ (tmp[39] ^ tmp[117] & (tmp[39] ^ (tmp[41]
				^ tmp[126])))));
		tmp[69] = tmp[60] | (tmp[149] | tmp[35]);
		tmp[90] = tmp[33] & tmp[149] ^ tmp[41] ^ (tmp[62] ^ tmp[35]) ^ (tmp[64] & ~((tmp[5] | tmp[60] ^ tmp[90]) ^ (tmp[60] ^ tmp[117] & (tmp[155] ^ (tmp[5] | tmp[155])))) ^ (tmp[147] | tmp[69] ^ (tmp[5]
				| tmp[69])));
		tmp[62] = tmp[31] | tmp[90];
		tmp[141] = ~tmp[90];
		tmp[163] = tmp[62] & tmp[141];
		tmp[53] = tmp[31] ^ tmp[90];
		tmp[158] = tmp[97] & tmp[141];
		tmp[120] = tmp[43] & tmp[141];
		tmp[81] = tmp[136] | tmp[90];
		tmp[20] = tmp[136] ^ tmp[90];
		tmp[167] = tmp[31] & tmp[90];
		tmp[141] &= tmp[49];
		tmp[125] = tmp[8] ^ (tmp[143] | tmp[90]);
		tmp[110] = ~tmp[90];
		tmp[49] = tmp[143] ^ tmp[49] & tmp[110];
		tmp[143] = tmp[136] & tmp[110];
		tmp[10] = tmp[136] & tmp[8] & tmp[110];
		tmp[151] = ~tmp[31];
		tmp[1] = tmp[90] & tmp[151];
		tmp[112] =
				tmp[175] ^ (tmp[153] & ~(tmp[145] ^ (tmp[174] ^ tmp[67] & (tmp[174] ^ tmp[112]))) ^ (tmp[34] ^ tmp[72] & ~tmp[113])) ^ ~tmp[29] & (tmp[25] ^ tmp[72] & ~(tmp[35] ^ tmp[76]) ^ tmp[153] & (
						(tmp[72] | tmp[25]) ^ (tmp[38] ^ tmp[55])));
		tmp[67] = tmp[90] | tmp[112];
		tmp[76] = tmp[90] ^ tmp[67];
		tmp[55] = ~tmp[112];
		tmp[38] = tmp[53] & tmp[55];
		tmp[25] = tmp[62] | tmp[112];
		tmp[113] = tmp[31] | tmp[112];
		tmp[145] = tmp[53] ^ tmp[38];
		tmp[34] = tmp[53] | tmp[112];
		tmp[175] = tmp[31] ^ tmp[112];
		tmp[54] = tmp[31] & tmp[55];
		tmp[17] = tmp[31] ^ tmp[113];
		tmp[73] = tmp[35] ^ tmp[155];
		tmp[66] = tmp[73] & ~tmp[5];
		tmp[98] = tmp[44] ^ ((tmp[5] | tmp[41]) ^ (tmp[177] ^ (tmp[57] ^ (tmp[147] | tmp[177] ^ (tmp[5] | tmp[98]))))) ^ tmp[64] & ~(tmp[35] ^ tmp[119] ^ (tmp[73] ^ tmp[66]) & ~tmp[147]);
		tmp[177] = ~tmp[83];
		tmp[73] = tmp[83] ^ tmp[98];
		tmp[57] = tmp[98] & tmp[177];
		tmp[41] = tmp[83] | tmp[57];
		tmp[44] = ~tmp[98];
		tmp[79] = tmp[83] & tmp[98];
		tmp[87] = tmp[83] & tmp[44];
		tmp[171] = ~tmp[87];
		tmp[99] = tmp[83] & tmp[171];
		tmp[162] = ~tmp[159];
		tmp[126] = tmp[69] ^ (tmp[86] ^ (tmp[117] & (tmp[119] ^ tmp[121] ^ (tmp[5] | tmp[84] ^ (tmp[60] | tmp[126]))) ^ (tmp[121] ^ (tmp[5] | tmp[149] ^ tmp[61])))) ^ tmp[64] & (tmp[66] ^ (tmp[155]
				^ tmp[33] & tmp[117] & (tmp[60] ^ tmp[35])));
		tmp[84] = ~tmp[71];
		tmp[61] = tmp[126] & tmp[84];
		tmp[121] = tmp[71] & tmp[126];
		tmp[119] = tmp[71] ^ tmp[126];
		tmp[33] = tmp[71] & ~tmp[126];
		tmp[155] = tmp[71] | tmp[126];
		tmp[66] = tmp[84] & tmp[155];
		tmp[69] = tmp[137] | tmp[131];
		tmp[89] = tmp[131] ^ (tmp[115] & ~tmp[52] ^ (tmp[183] & ~(tmp[179] ^ tmp[115] & ~(tmp[179] ^ tmp[45])) ^ (
				tmp[86] & (tmp[115] & ~(tmp[111] ^ tmp[68] & tmp[180]) ^ (tmp[183] & ~(tmp[3] & tmp[89] ^ tmp[52]) ^ tmp[7])) ^ (tmp[164] ^ tmp[69]))));
		tmp[134] ^=
				tmp[186] ^ (tmp[138] ^ tmp[178] ^ tmp[117] & (tmp[22] ^ (tmp[146] | tmp[59] ^ tmp[50]) ^ tmp[139] & tmp[134]) ^ tmp[89] & ~(tmp[182] ^ tmp[2] & (tmp[182] ^ tmp[134]) ^ tmp[117] & (tmp[60]
						^ tmp[2] & tmp[59] ^ (tmp[178] | tmp[22]))));
		tmp[182] = tmp[190] | tmp[134];
		tmp[186] = tmp[134] ^ tmp[182];
		tmp[3] = ~tmp[124];
		tmp[138] =
				tmp[187] ^ (tmp[50] ^ (tmp[146] | tmp[22] ^ tmp[105]) ^ tmp[13] & ~tmp[22] ^ tmp[117] & (tmp[27] ^ (tmp[146] | tmp[105])) ^ tmp[89] & ~(tmp[181] ^ (tmp[147] | tmp[60] ^ tmp[138] ^ (tmp[178]
						| tmp[13])) ^ tmp[2] & (tmp[22] ^ tmp[50])));
		tmp[22] = tmp[162] & tmp[138];
		tmp[105] = tmp[159] & tmp[138];
		tmp[187] = ~tmp[80];
		tmp[180] = ~tmp[138];
		tmp[111] = tmp[159] & tmp[180];
		tmp[45] = tmp[138] | tmp[111];
		tmp[7] = tmp[98] & tmp[22];
		tmp[164] = tmp[138] & ~tmp[22];
		tmp[127] = (tmp[77] ^ tmp[92] ^ tmp[2] & tmp[181] ^ tmp[117] & (tmp[13] ^ (tmp[146] | tmp[161]) ^ tmp[139] & tmp[13])) & tmp[89] ^ (tmp[15] ^ (tmp[178] ^ tmp[13] ^ tmp[2] & tmp[127] ^ (tmp[147]
				| tmp[116] ^ tmp[146] & ~tmp[127])));
		tmp[32] = tmp[115] ^ (tmp[19] ^ tmp[59] ^ (tmp[116] | tmp[146]) ^ (tmp[147] | tmp[50] ^ (tmp[60] ^ tmp[2] & tmp[19]))
				^ (tmp[60] ^ (tmp[60] | tmp[178]) ^ tmp[2] & tmp[27] ^ (tmp[147] | tmp[13] ^ tmp[2] & tmp[32] ^ (tmp[178] | tmp[59]))) & tmp[89]);
		tmp[177] &= tmp[32];
		tmp[2] = tmp[177] ^ (tmp[83] | tmp[98]);
		tmp[59] = tmp[98] & tmp[32];
		tmp[44] &= tmp[32];
		tmp[19] = tmp[99] ^ tmp[44];
		tmp[27] = tmp[87] ^ tmp[44];
		tmp[50] = tmp[57] ^ tmp[177];
		tmp[44] ^= tmp[57];
		tmp[116] = tmp[71] | tmp[32];
		tmp[161] = ~tmp[32];
		tmp[139] = tmp[33] & tmp[161];
		tmp[181] = tmp[126] | tmp[32];
		tmp[92] = tmp[73] & tmp[32];
		tmp[77] = ~tmp[94];
		tmp[117] = tmp[32] & ~tmp[99];
		tmp[15] = tmp[41] & tmp[32];
		tmp[131] = tmp[61] & tmp[161];
		tmp[18] = tmp[83] & tmp[32];
		tmp[23] = tmp[32] & ~tmp[73];
		tmp[57] = tmp[84] & (tmp[57] ^ tmp[15]);
		tmp[40] = tmp[87] & tmp[32];
		tmp[114] = tmp[73] ^ tmp[23];
		tmp[24] = tmp[66] | tmp[32];
		tmp[21] = tmp[155] ^ tmp[24];
		tmp[16] = tmp[190] & ~(tmp[155] ^ (tmp[119] | tmp[32]));
		tmp[171] &= tmp[32];
		tmp[173] =
				tmp[115] & (tmp[68] ^ tmp[69]) ^ (tmp[0] ^ (tmp[86] & ~(tmp[173] ^ tmp[26] ^ tmp[183] & (tmp[115] & ~tmp[173] ^ tmp[52]) ^ tmp[115] & ~(tmp[68] ^ (tmp[137] | tmp[26]))) ^ (tmp[85] ^ (tmp[137]
						| tmp[179]) ^ tmp[183] & (tmp[123] ^ (tmp[115] | tmp[91])))));
		tmp[26] = tmp[118] & tmp[173];
		tmp[137] = tmp[118] ^ tmp[26];
		tmp[115] = tmp[173] & ~tmp[109];
		tmp[68] = tmp[42] ^ tmp[115];
		tmp[52] = ~tmp[122];
		tmp[91] = tmp[173] & ~tmp[63];
		tmp[123] = tmp[63] ^ tmp[91];
		tmp[179] = tmp[173] & ~tmp[93];
		tmp[85] = tmp[173] & ~tmp[160];
		tmp[86] = ~tmp[168];
		tmp[69] = tmp[168] | tmp[173] & ~tmp[85];
		tmp[0] = tmp[160] ^ tmp[69];
		tmp[30] = tmp[109] & tmp[173];
		tmp[103] = tmp[109] ^ tmp[30];
		tmp[12] ^= tmp[173] ^ tmp[42] ^ (tmp[122] | tmp[170] & tmp[123]) ^ tmp[86] & (tmp[103] ^ (tmp[170] | tmp[42] & tmp[173]) ^ tmp[52] & (tmp[103] ^ tmp[170] & tmp[137]));
		tmp[14] = tmp[187] & tmp[12];
		tmp[172] = tmp[80] ^ tmp[14];
		tmp[6] = tmp[168] | tmp[160] ^ tmp[173];
		tmp[142] = tmp[160] ^ tmp[6];
		tmp[132] = tmp[160] | tmp[173];
		tmp[82] = tmp[168] | tmp[132];
		tmp[140] = ~tmp[101];
		tmp[188] = tmp[85] ^ tmp[82];
		tmp[154] = tmp[173] & tmp[86];
		tmp[189] = tmp[160] & ~tmp[173];
		tmp[4] = tmp[148] ^ tmp[189] ^ (tmp[108] & ~(tmp[4] ^ (tmp[173] ^ tmp[82])) ^ tmp[86] & (tmp[173] | tmp[189])) ^ tmp[101] & ~tmp[188] ^ tmp[130] & ~(tmp[108] & (tmp[95] ^ tmp[4]) ^ (
				tmp[101] & (tmp[95] ^ tmp[85]) ^ tmp[188]));
		tmp[188] = ~tmp[4];
		tmp[148] = tmp[12] & tmp[188];
		tmp[191] = tmp[80] ^ tmp[4];
		tmp[129] = tmp[12] & tmp[191];
		tmp[11] = tmp[12] & ~tmp[191];
		tmp[185] = tmp[14] ^ tmp[191];
		tmp[46] = tmp[12] ^ tmp[191];
		tmp[187] = tmp[159] & ~(tmp[180] & (tmp[187] & tmp[4])) ^ (tmp[130] ^ (tmp[46] ^ tmp[138] & tmp[187])) ^ (tmp[36] | tmp[80] ^ tmp[138] & tmp[4] ^ tmp[105] & tmp[129]);
		tmp[14] ^= tmp[4];
		tmp[96] = tmp[148] ^ tmp[191];
		tmp[185] ^= tmp[88] ^ (tmp[180] & tmp[172] ^ tmp[159] & (tmp[96] ^ tmp[138] & (tmp[4] ^ tmp[11])) ^ (tmp[36] | tmp[12] ^ (tmp[138] | tmp[129]) ^ tmp[159] & (tmp[46] ^ tmp[180] & tmp[185])));
		tmp[180] = tmp[12] ^ tmp[4];
		tmp[11] = tmp[174] ^ (tmp[138] ^ (tmp[80] ^ tmp[12]) ^ tmp[159] & ~(tmp[14] ^ tmp[138] & tmp[11])) ^ (tmp[36] | tmp[180] ^ tmp[138] & ~tmp[172] ^ tmp[159] & (tmp[180] ^ tmp[138] & tmp[148]));
		tmp[172] = tmp[80] | tmp[4];
		tmp[180] = tmp[80] & tmp[4];
		tmp[129] =
				tmp[146] ^ (tmp[14] ^ tmp[138] & (tmp[180] ^ tmp[12] & tmp[180]) ^ tmp[159] & ~(tmp[12] & ~tmp[172] ^ tmp[138] & tmp[12] ^ tmp[188] & tmp[172])) ^ (tmp[36] | tmp[191] ^ tmp[12] & (tmp[80]
						& tmp[188]) ^ tmp[138] & tmp[96] ^ tmp[159] & ~(tmp[148] ^ (tmp[4] ^ tmp[138] & ~(tmp[4] ^ tmp[129]))));
		tmp[172] = tmp[168] | tmp[173];
		tmp[42] = tmp[93] ^ (tmp[165] & tmp[170] ^ (tmp[183] ^ tmp[173] & ~tmp[156])) ^ tmp[52] & (tmp[170] | tmp[118] ^ tmp[173] & ~tmp[42]) ^ (tmp[168] | tmp[93] ^ tmp[91] ^ (tmp[122] | tmp[123])
				^ tmp[170] & (tmp[118] ^ tmp[91]));
		tmp[123] = tmp[155] | tmp[32];
		tmp[16] =
				tmp[35] ^ tmp[190] & tmp[71] ^ (tmp[155] ^ tmp[32]) ^ (tmp[42] | tmp[131] ^ (tmp[66] ^ tmp[190] & ~(tmp[121] ^ tmp[71] & tmp[161]))) ^ (tmp[124] | tmp[123] ^ (tmp[119] ^ (tmp[16] ^ (tmp[42]
						| tmp[71] ^ tmp[32] ^ tmp[190] & ~(tmp[126] ^ tmp[116])))));
		tmp[66] = tmp[11] | tmp[16];
		tmp[35] = tmp[11] ^ tmp[16];
		tmp[123] = tmp[11] & ~tmp[16];
		tmp[183] = tmp[16] & ~tmp[11];
		tmp[165] = tmp[11] & tmp[16];
		tmp[188] = ~tmp[42];
		tmp[131] ^= tmp[37] ^ (tmp[190] ^ tmp[119]) ^ (tmp[42] | tmp[190] & ~tmp[155] ^ tmp[21]) ^ (tmp[124] | (tmp[71] ^ tmp[190] & ~tmp[119] ^ (tmp[32] | tmp[71] & ~tmp[121])) & tmp[42]);
		tmp[155] = tmp[89] ^ (tmp[181] ^ (tmp[61] ^ tmp[190] & (tmp[126] ^ tmp[139]))) ^ tmp[188] & (tmp[24] ^ (tmp[121] ^ tmp[190] & ~(tmp[33] ^ tmp[116]))) ^ tmp[3] & (tmp[155] ^ tmp[190] & ~(tmp[126]
				^ tmp[155] & tmp[161]) ^ ~(tmp[121] ^ tmp[32]) & (tmp[190] & tmp[188]));
		tmp[121] = tmp[21] ^ (tmp[173] ^ (tmp[190] | tmp[121] ^ tmp[119] & tmp[161])) ^ ((tmp[139] ^ tmp[190] & ~(tmp[33] ^ tmp[32])) & tmp[188] ^ tmp[3] & (tmp[139] ^ (tmp[121] ^ tmp[190] & ~(tmp[71] ^ (
				tmp[121] | tmp[32]))) ^ (tmp[42] | tmp[139] ^ tmp[190] & ~(tmp[121] ^ tmp[116]))));
		tmp[116] = tmp[170] & tmp[26];
		tmp[179] =
				tmp[157] ^ (tmp[170] & ~tmp[156] ^ tmp[68]) ^ (tmp[122] | tmp[170] & tmp[103] ^ (tmp[109] ^ tmp[179])) ^ tmp[86] & ((tmp[122] | tmp[116] ^ (tmp[63] ^ tmp[179])) ^ (tmp[30] ^ tmp[170] & (
						tmp[156] ^ tmp[26])));
		tmp[63] = ~tmp[106];
		tmp[103] = ~tmp[179];
		tmp[30] = tmp[113] | tmp[179];
		tmp[38] =
				tmp[53] ^ (tmp[90] & ~tmp[167] | tmp[112]) ^ (tmp[64] ^ ((tmp[1] & tmp[55] | tmp[179]) ^ tmp[63] & (tmp[163] ^ tmp[38] ^ (tmp[62] ^ tmp[38] | tmp[179])))) ^ (tmp[102] | tmp[30] ^ (tmp[17] ^ (
						tmp[106] | tmp[76] ^ (tmp[67] | tmp[179]))));
		tmp[145] = (tmp[76] | tmp[179]) ^ (tmp[170] ^ tmp[175]) ^ tmp[63] & (tmp[25] ^ tmp[167] & tmp[103]) ^ (tmp[102] | tmp[112] ^ tmp[63] & (tmp[145] ^ tmp[145] & tmp[103])
				^ (tmp[163] | tmp[112]) & tmp[103]);
		tmp[163] = tmp[90] & tmp[103];
		tmp[25] = tmp[72] ^ (tmp[90] ^ tmp[113]) ^ ((tmp[106] | tmp[175] ^ tmp[179] & ~(tmp[1] ^ tmp[25])) ^ (tmp[102] | tmp[175] ^ (tmp[106] | tmp[90] ^ tmp[34] ^ tmp[163]) ^ tmp[179] & ~tmp[113])
				^ tmp[175] & tmp[103]);
		tmp[1] = tmp[11] | tmp[25];
		tmp[110] =
				tmp[13] ^ (tmp[90] ^ tmp[54]) ^ tmp[67] & tmp[103] ^ (tmp[106] | tmp[31] ^ tmp[55] & (tmp[31] & tmp[110]) ^ tmp[55] & tmp[163]) ^ (tmp[102] | (tmp[167] | tmp[112]) ^ tmp[63] & tmp[103] & (
						tmp[53] ^ tmp[34]) ^ (tmp[179] | tmp[31] ^ tmp[54]));
		tmp[34] = tmp[110] & ~tmp[187];
		tmp[53] = tmp[110] ^ tmp[34];
		tmp[103] = tmp[187] | tmp[110];
		tmp[26] = tmp[48] ^ (tmp[109] ^ tmp[170]) ^ (tmp[52] & (tmp[68] ^ tmp[116]) ^ tmp[156] & tmp[173]) ^ (tmp[168] | tmp[52] & (tmp[93] ^ tmp[115] ^ tmp[170] & ~tmp[137]) ^ (tmp[91] ^ (tmp[156]
				^ tmp[170] & ~tmp[26])));
		tmp[82] =
				tmp[47] ^ (tmp[173] ^ tmp[6]) ^ tmp[101] & ~tmp[154] ^ (tmp[130] & (tmp[108] & (tmp[101] & (tmp[160] ^ tmp[95]) ^ tmp[0]) ^ (tmp[135] ^ tmp[140] & (tmp[154] ^ tmp[189]))) ^ tmp[108] & ~(tmp[0]
						^ tmp[140] & (tmp[132] ^ tmp[82])));
		tmp[132] = tmp[134] & tmp[82];
		tmp[140] = ~tmp[82];
		tmp[0] = ~tmp[190];
		tmp[47] = tmp[134] | tmp[82];
		tmp[137] = tmp[190] | tmp[82];
		tmp[170] = tmp[47] ^ tmp[137];
		tmp[115] = tmp[134] ^ tmp[82];
		tmp[156] = ~tmp[134];
		tmp[116] = tmp[82] & tmp[156];
		tmp[68] = tmp[0] & tmp[115];
		tmp[91] = tmp[190] | tmp[115];
		tmp[52] = tmp[82] & tmp[0];
		tmp[156] &= tmp[52];
		tmp[109] = tmp[31] & tmp[91];
		tmp[48] = tmp[134] ^ tmp[68];
		tmp[63] = tmp[116] ^ tmp[156];
		tmp[147] ^= tmp[115] ^ tmp[190] ^ tmp[31] & ~tmp[63] ^ (tmp[124] | tmp[82] ^ tmp[31] & tmp[63]) ^ tmp[112] & ~(tmp[170] ^ tmp[3] & (tmp[91] ^ (tmp[31] | tmp[91])) ^ tmp[31] & ~(tmp[190]
				^ tmp[134] & tmp[140]));
		tmp[55] = tmp[16] | tmp[147];
		tmp[54] = ~tmp[147];
		tmp[167] = tmp[16] ^ tmp[147];
		tmp[163] = tmp[147] & ~tmp[110];
		tmp[67] = tmp[110] & tmp[147];
		tmp[13] = tmp[147] & ~tmp[67];
		tmp[113] = tmp[110] | tmp[147];
		tmp[175] = tmp[54] & tmp[113];
		tmp[72] = tmp[110] & tmp[54];
		tmp[76] = ~tmp[155];
		tmp[62] = tmp[110] ^ tmp[147];
		tmp[151] =
				tmp[47] ^ (tmp[56] ^ tmp[68]) ^ ((tmp[124] | tmp[140] & tmp[47] ^ tmp[31] & tmp[170]) ^ (tmp[112] & (tmp[91] ^ tmp[151] & tmp[63] ^ tmp[3] & (tmp[186] ^ tmp[151] & tmp[186])) ^ tmp[31] & ~(
						tmp[116] ^ tmp[91])));
		tmp[52] = tmp[160] ^ tmp[82] & ~tmp[132] ^ (tmp[190] | tmp[132]) ^ tmp[31] & ~(tmp[134] ^ (tmp[190] | tmp[47])) ^ (tmp[124] | tmp[186] ^ tmp[31] & ~(tmp[134] ^ tmp[156])) ^ tmp[112] & (
				tmp[31] & tmp[186] ^ (tmp[132] ^ ((tmp[124] | tmp[91] ^ tmp[31] & ~(tmp[134] ^ tmp[52])) ^ tmp[132] & tmp[0])));
		tmp[156] = ~tmp[52];
		tmp[47] = tmp[121] & tmp[156];
		tmp[132] = tmp[52] ^ tmp[47];
		tmp[0] = tmp[121] ^ tmp[52];
		tmp[109] =
				tmp[82] ^ (tmp[29] ^ tmp[91]) ^ tmp[31] & (tmp[134] ^ tmp[137]) ^ (tmp[124] | tmp[109] ^ tmp[48]) ^ tmp[112] & ~(tmp[31] & tmp[134] ^ (tmp[48] ^ tmp[3] & (tmp[115] ^ (tmp[182] ^ tmp[109]))));
		tmp[182] = tmp[16] | tmp[109];
		tmp[115] = tmp[16] ^ tmp[182];
		tmp[3] = ~tmp[109];
		tmp[48] = tmp[123] ^ tmp[35] & tmp[3];
		tmp[137] = tmp[123] & tmp[3];
		tmp[91] = tmp[123] ^ tmp[182];
		tmp[29] = tmp[3] & (tmp[16] | tmp[123]);
		tmp[182] ^= tmp[183];
		tmp[186] = tmp[183] & tmp[3];
		tmp[63] = tmp[123] ^ tmp[29];
		tmp[116] = tmp[183] | tmp[109];
		tmp[85] ^= tmp[107] ^ (tmp[168] | tmp[189]) ^ tmp[101] & (tmp[173] ^ (tmp[168] | tmp[85])) ^ (
				tmp[130] & ~(tmp[108] & (tmp[166] ^ tmp[101] & tmp[166]) ^ (tmp[168] & (tmp[101] & tmp[85]) ^ tmp[168] & tmp[85])) ^ tmp[108] & ~(tmp[173] ^ tmp[69] ^ tmp[101] & tmp[154]));
		tmp[166] = ~tmp[85];
		tmp[154] = tmp[45] & tmp[166];
		tmp[69] = tmp[159] & tmp[166];
		tmp[189] = tmp[111] ^ tmp[69];
		tmp[107] = tmp[105] ^ tmp[85];
		tmp[170] = tmp[105] & tmp[166];
		tmp[140] = tmp[45] ^ tmp[170];
		tmp[68] = tmp[159] | tmp[85];
		tmp[111] =
				(tmp[159] | tmp[98]) ^ (tmp[168] ^ tmp[107]) ^ tmp[176] & (tmp[69] ^ (tmp[45] ^ (tmp[98] & ~(tmp[159] ^ tmp[138] | tmp[85]) ^ tmp[26] & (tmp[189] ^ tmp[98] & tmp[69])))) ^ tmp[26] & ~(tmp[164]
						^ tmp[98] & (tmp[159] ^ tmp[111] & tmp[166]));
		tmp[23] = tmp[178] ^ ((tmp[71] | tmp[87] ^ tmp[40]) ^ (tmp[44] ^ (tmp[94] | tmp[98] ^ tmp[171] ^ tmp[84] & tmp[177]))) ^ tmp[85] & ~(tmp[23] ^ (tmp[94] | tmp[18] ^ (tmp[79] ^ (tmp[71] | tmp[23])))
				^ tmp[71] & ~tmp[19]);
		tmp[18] = tmp[113] | tmp[23];
		tmp[87] = ~tmp[23];
		tmp[178] = tmp[110] & tmp[87];
		tmp[56] = tmp[163] ^ tmp[178];
		tmp[17] = tmp[67] & tmp[87];
		tmp[30] = tmp[113] & tmp[87];
		tmp[64] = tmp[110] | tmp[23];
		tmp[157] = tmp[113] ^ tmp[64];
		tmp[86] = ~tmp[129];
		tmp[33] = tmp[13] | tmp[23];
		tmp[139] = tmp[62] ^ tmp[33];
		tmp[79] =
				tmp[85] & ~(tmp[84] & tmp[50] ^ (tmp[50] ^ tmp[77] & (tmp[44] ^ (tmp[71] | tmp[92])))) ^ (tmp[118] ^ (tmp[41] ^ (tmp[59] ^ ((tmp[94] | tmp[79] ^ tmp[117] ^ (tmp[71] | tmp[59])) ^ tmp[84] & (
						tmp[83] ^ tmp[15])))));
		tmp[15] = tmp[121] & tmp[79];
		tmp[84] = tmp[121] & ~tmp[79];
		tmp[92] = tmp[57] ^ (tmp[27] ^ tmp[77] & (tmp[117] ^ (tmp[71] | tmp[98] ^ tmp[59]))) ^ (tmp[108] ^ (tmp[2] ^ (tmp[71] | tmp[2]) ^ (tmp[94] | tmp[19] ^ (tmp[71] | tmp[73] ^ tmp[92]))) & tmp[85]);
		tmp[73] = ~tmp[92];
		tmp[19] = tmp[121] & tmp[73];
		tmp[2] = tmp[52] ^ tmp[19];
		tmp[117] = tmp[52] | tmp[92];
		tmp[27] = tmp[121] & tmp[117];
		tmp[57] = tmp[117] ^ tmp[121] & ~tmp[117];
		tmp[73] &= tmp[52];
		tmp[50] = tmp[121] & tmp[73];
		tmp[118] = tmp[52] ^ tmp[50];
		tmp[161] = tmp[52] ^ tmp[92];
		tmp[119] = tmp[52] & tmp[92];
		tmp[188] = ~tmp[119];
		tmp[69] ^= tmp[138];
		tmp[170] ^= tmp[105];
		tmp[170] = tmp[78] ^ (tmp[26] & (tmp[107] ^ (tmp[98] | tmp[69])) ^ (tmp[140] ^ (tmp[98] | tmp[170])) ^ tmp[176] & (tmp[85] ^ tmp[98] & tmp[170] ^ tmp[26] & ~(tmp[164] ^ (tmp[98] | tmp[68]))));
		tmp[78] = tmp[25] | tmp[170];
		tmp[105] = ~tmp[25];
		tmp[21] = tmp[25] | (tmp[11] | tmp[170]);
		tmp[61] = tmp[170] & ~tmp[11];
		tmp[24] = tmp[11] ^ tmp[170];
		tmp[181] = tmp[11] ^ tmp[78];
		tmp[89] = tmp[11] & ~tmp[170];
		tmp[37] = tmp[11] & ~tmp[89];
		tmp[189] =
				tmp[9] ^ (tmp[107] ^ tmp[98] & tmp[140] ^ tmp[176] & ~(tmp[98] & ~tmp[69] ^ tmp[26] & (tmp[68] ^ (tmp[159] ^ tmp[98] & tmp[189])))) ^ tmp[26] & ~(tmp[98] & (tmp[45] ^ tmp[154]) ^ (tmp[138] ^ (
						tmp[164] | tmp[85])));
		tmp[7] =
				tmp[176] & ~(tmp[154] ^ (tmp[7] ^ (tmp[159] ^ tmp[26] & (tmp[22] ^ tmp[7])))) ^ (tmp[68] ^ (tmp[138] ^ tmp[98] & (tmp[45] ^ tmp[22] & tmp[166]))) ^ (tmp[149] ^ tmp[26] & (tmp[98] & tmp[162]
						^ (tmp[159] ^ tmp[159] & ~tmp[85])));
		tmp[22] = ~tmp[7];
		tmp[114] = tmp[153] ^ (tmp[99] ^ tmp[59] ^ (tmp[71] | tmp[40]) ^ (tmp[94] | tmp[41] ^ (tmp[71] | tmp[83] ^ tmp[177]) ^ tmp[171])) ^ tmp[85] & ~(tmp[114] ^ tmp[77] & (tmp[44] ^ tmp[71] & tmp[114])
				^ tmp[71] & tmp[44]);
		tmp[135] =
				tmp[58] ^ (tmp[130] & (tmp[108] & ~(tmp[101] & tmp[135] ^ tmp[142]) ^ tmp[172] ^ tmp[101] & ~tmp[6]) ^ (tmp[108] & (tmp[95] ^ tmp[160] & (tmp[173] & (tmp[101] & tmp[168]))) ^ tmp[142])) ^ (
						tmp[101] | tmp[173] ^ tmp[172]);
		tmp[168] = ~tmp[135];
		tmp[142] = tmp[70] & tmp[168];
		tmp[173] = tmp[133] ^ (tmp[70] | tmp[135]);
		tmp[160] = tmp[104] & tmp[168];
		tmp[6] = tmp[184] & tmp[168];
		tmp[108] = tmp[36] | tmp[135];
		tmp[95] = tmp[36] & tmp[184] & tmp[168];
		tmp[172] = tmp[184] ^ tmp[95];
		tmp[130] = tmp[36] & tmp[168];
		tmp[58] = tmp[39] & tmp[130];
		tmp[177] = ~tmp[12];
		tmp[5] ^= tmp[104] ^ tmp[39] & (tmp[70] ^ tmp[160]) ^ (tmp[12] | tmp[70] ^ tmp[39] & (tmp[184] ^ (tmp[104] | tmp[135]))) ^ tmp[136] & (tmp[39] & tmp[172] ^ (tmp[184] ^ tmp[6]) ^ (tmp[12]
				| (tmp[184] | tmp[135]) ^ tmp[39] & ~tmp[108]));
		tmp[44] = ~tmp[5];
		tmp[66] = tmp[165] ^ tmp[29] ^ tmp[115] & tmp[5] ^ tmp[114] & (tmp[115] | tmp[44]) ^ (tmp[112] ^ (tmp[25] | tmp[137] & tmp[5] ^ tmp[114] & (tmp[66] ^ (tmp[66] | tmp[109]) ^ tmp[11] & tmp[5])));
		tmp[183] = (tmp[25] | tmp[48] ^ tmp[5] & ~(tmp[123] ^ tmp[116]) ^ tmp[63] & tmp[114]) ^ (tmp[8] ^ (tmp[114] & ~(tmp[16] & tmp[3] ^ (tmp[11] ^ tmp[137]) & tmp[5]) ^ (tmp[35] ^ (tmp[11] | tmp[109])
				^ (tmp[183] ^ (tmp[109] | tmp[16] & ~tmp[183])) & tmp[5])));
		tmp[137] = tmp[123] & tmp[5];
		tmp[165] =
				tmp[80] ^ (tmp[11] ^ tmp[116]) ^ (tmp[114] & (tmp[16] ^ (tmp[165] ^ tmp[186]) & tmp[44]) ^ tmp[5] & ~tmp[165]) ^ (tmp[25] | tmp[182] ^ tmp[165] & tmp[5] ^ tmp[114] & ~(tmp[16] & tmp[5]));
		tmp[186] = tmp[71] ^ (tmp[182] ^ tmp[5] & ~tmp[48]) ^ tmp[114] & (tmp[91] ^ tmp[5] & ~tmp[91]) ^ tmp[105] & (tmp[114] & ~(tmp[123] ^ tmp[186] ^ tmp[137]) ^ (tmp[63] ^ tmp[137]));
		tmp[108] =
				tmp[39] & ~tmp[95] ^ (tmp[135] ^ (tmp[128] ^ tmp[144])) ^ (tmp[12] | tmp[172] ^ tmp[39] & (tmp[36] ^ tmp[95])) ^ tmp[136] & ~(tmp[152] ^ (tmp[133] | tmp[135]) ^ tmp[39] & tmp[95] ^ (tmp[12]
						| tmp[104] ^ tmp[6] ^ tmp[39] & (tmp[36] ^ tmp[108])));
		tmp[6] = ~tmp[187];
		tmp[133] = tmp[110] | tmp[108];
		tmp[95] = tmp[108] & tmp[6];
		tmp[128] = ~tmp[189];
		tmp[172] = tmp[110] ^ tmp[108];
		tmp[123] = tmp[6] & tmp[172];
		tmp[137] = tmp[108] & ~tmp[110];
		tmp[91] = tmp[110] | tmp[137];
		tmp[48] = tmp[123] ^ tmp[91];
		tmp[63] = tmp[6] & tmp[137];
		tmp[182] = tmp[187] | tmp[108];
		tmp[95] ^= tmp[106] ^ tmp[172] ^ ((tmp[23] | tmp[123] ^ tmp[128] & (tmp[34] ^ tmp[52] & tmp[95])) ^ (tmp[52] | tmp[133] ^ tmp[6] & tmp[91]) ^ (tmp[189] | tmp[53] ^ tmp[52] & tmp[182]));
		tmp[91] = tmp[110] & ~tmp[108];
		tmp[106] = tmp[110] & ~tmp[91];
		tmp[103] = (tmp[103] ^ (tmp[103] | tmp[52]) | tmp[189]) ^ (tmp[34] ^ tmp[133]) ^ (tmp[52] | tmp[63] ^ tmp[106]);
		tmp[63] = tmp[36] ^ tmp[103] ^ tmp[23] & ~((tmp[187] | tmp[91]) ^ (tmp[137] ^ (tmp[52] | tmp[172] ^ tmp[63])) ^ tmp[128] & (tmp[182] ^ (tmp[108] ^ (tmp[52] | tmp[108] ^ (tmp[187] | tmp[172])))));
		tmp[182] = ~tmp[23];
		tmp[123] = tmp[124] ^ tmp[103] ^ tmp[182] & (tmp[172] ^ (tmp[189] | tmp[48] ^ (tmp[52] | tmp[123])) ^ ~tmp[52] & (tmp[110] ^ tmp[123]) ^ (tmp[187] | tmp[106]));
		tmp[172] = tmp[66] | tmp[123];
		tmp[106] = tmp[66] & tmp[123];
		tmp[103] = ~tmp[123];
		tmp[124] = tmp[172] & tmp[103];
		tmp[137] = tmp[186] ^ tmp[123];
		tmp[128] = tmp[66] ^ tmp[123];
		tmp[71] = tmp[186] & tmp[103];
		tmp[44] = tmp[186] & tmp[123];
		tmp[116] = tmp[66] & tmp[103];
		tmp[80] = ~tmp[66];
		tmp[3] = tmp[123] & tmp[80];
		tmp[34] = (tmp[53] ^ tmp[34] & tmp[156] | tmp[189]) ^ ((tmp[34] | tmp[52]) ^ (tmp[83] ^ tmp[91] ^ (tmp[187] | tmp[133]))) ^ tmp[182] & ((tmp[189] | (tmp[52] | tmp[34] ^ tmp[108])) ^ tmp[48]
				^ tmp[52] & ~(tmp[133] ^ tmp[108] & (tmp[110] & tmp[6])));
		tmp[133] = tmp[90] & tmp[168];
		tmp[141] ^= tmp[127] & ~(tmp[179] & (tmp[141] ^ tmp[133]) ^ (tmp[143] ^ tmp[133])) ^ ((tmp[125] | tmp[135]) ^ (tmp[179] & ~(tmp[8] ^ tmp[158]) ^ (tmp[101] ^ tmp[136])));
		tmp[101] = ~tmp[141];
		tmp[132] = tmp[4] ^ (tmp[117] ^ tmp[121] & tmp[119] ^ tmp[132] & tmp[101] ^ (tmp[187] | tmp[132] ^ (tmp[47] ^ tmp[52] & tmp[188] | tmp[141]))) ^ tmp[111] & ~(
				(tmp[187] | tmp[132] ^ tmp[156] & tmp[92] & tmp[101]) ^ (tmp[57] ^ tmp[141] & ~tmp[132]));
		tmp[57] =
				tmp[82] ^ (tmp[121] ^ tmp[161]) ^ (tmp[121] & tmp[52] | tmp[141]) ^ (tmp[187] | tmp[50] ^ (tmp[47] | tmp[141])) ^ tmp[111] & ~(tmp[117] ^ tmp[6] & (tmp[118] ^ (tmp[121] ^ tmp[117]) & tmp[101])
						^ (tmp[57] | tmp[141]));
		tmp[2] = tmp[135] ^ (tmp[119] ^ tmp[121] & tmp[92] ^ (tmp[0] | tmp[141])) ^ (tmp[187] | tmp[2] ^ tmp[0] & tmp[101]) ^ tmp[111] & ~(tmp[92] ^ tmp[27] ^ tmp[6] & (tmp[2]
				^ (tmp[121] ^ tmp[119]) & tmp[101]) ^ tmp[118] & tmp[101]);
		tmp[118] = ~tmp[2];
		tmp[0] = tmp[63] & tmp[118];
		tmp[50] = tmp[183] & tmp[118];
		tmp[82] = tmp[63] | tmp[2];
		tmp[4] = tmp[183] | tmp[2];
		tmp[161] = tmp[85] ^ (tmp[117] ^ tmp[121] & tmp[188]) ^ ((tmp[156] & tmp[117] ^ tmp[121] & ~tmp[161] | tmp[141]) ^ (
				tmp[111] & (tmp[27] ^ tmp[119] ^ (tmp[19] | tmp[141]) ^ tmp[6] & ((tmp[47] ^ tmp[161]) & tmp[101])) ^ (tmp[187] | tmp[73] & tmp[101])));
		tmp[97] = tmp[74] ^ (tmp[158] ^ (tmp[8] ^ (tmp[43] | tmp[90])) & tmp[168] ^ tmp[179] & ~(tmp[10] & tmp[135])) ^ tmp[127] & (tmp[97] ^ tmp[81] ^ (tmp[20] | tmp[135]) ^ tmp[179] & (tmp[43]
				^ (tmp[136] ^ tmp[81]) & tmp[135]));
		tmp[158] = ~tmp[131];
		tmp[105] = tmp[24] ^ tmp[158] & (tmp[170] ^ tmp[105] & tmp[89] ^ ~(tmp[24] ^ tmp[170] & tmp[105]) & tmp[97]) ^ (tmp[78] ^ tmp[61]) & tmp[97];
		tmp[61] = tmp[11] ^ (tmp[25] | tmp[37]) ^ (tmp[25] ^ tmp[37]) & tmp[97] ^ (tmp[131] | (tmp[11] | tmp[61]) ^ (tmp[25] | tmp[61]) ^ tmp[181] & tmp[97]);
		tmp[24] = (tmp[170] ^ tmp[21] | tmp[97]) ^ (tmp[181] ^ tmp[158] & (tmp[170] ^ (tmp[25] | tmp[24]) ^ (tmp[11] ^ tmp[1]) & tmp[97]));
		tmp[1] = tmp[25] ^ tmp[170] ^ (tmp[21] ^ tmp[37]) & tmp[97] ^ (tmp[131] | tmp[181] ^ tmp[97] & ~tmp[1]);
		tmp[133] = tmp[122] ^ (tmp[136] ^ tmp[8] ^ tmp[90] ^ tmp[135]) ^ tmp[179] & (tmp[43] ^ (tmp[136] ^ tmp[10]) & tmp[168]) ^ tmp[127] & ~(tmp[136] ^ tmp[179] & ~(tmp[120] ^ tmp[133]) ^ (
				(tmp[136] | tmp[8]) ^ tmp[143] | tmp[135]));
		tmp[8] = tmp[133] & ~tmp[185];
		tmp[143] = ~tmp[8];
		tmp[43] = tmp[185] ^ tmp[133];
		tmp[122] = ~tmp[79];
		tmp[37] = tmp[79] ^ tmp[133];
		tmp[21] = tmp[121] & tmp[37];
		tmp[181] = ~tmp[133];
		tmp[158] = tmp[185] & tmp[181];
		tmp[89] = tmp[121] & (tmp[79] & tmp[133]);
		tmp[78] = tmp[133] | tmp[158];
		tmp[181] &= tmp[79];
		tmp[74] = tmp[133] & tmp[122];
		tmp[47] = tmp[89] ^ tmp[181];
		tmp[101] = tmp[133] | tmp[181];
		tmp[19] = tmp[79] | tmp[133];
		tmp[119] = tmp[121] & ~tmp[19];
		tmp[27] = tmp[121] & ~tmp[74];
		tmp[6] = tmp[74] ^ tmp[27];
		tmp[73] = tmp[121] & tmp[74];
		tmp[130] ^=
				tmp[39] ^ (tmp[93] ^ tmp[70]) ^ (tmp[136] & ~(tmp[39] & ~(tmp[152] ^ tmp[142]) ^ tmp[173] ^ tmp[177] & (tmp[173] ^ tmp[39] & ~tmp[130])) ^ (tmp[12] | tmp[184] ^ tmp[160] ^ tmp[39] & ~(tmp[75]
						| tmp[135])));
		tmp[173] = tmp[121] & tmp[130];
		tmp[47] =
				tmp[84] ^ (tmp[42] ^ tmp[133]) ^ (tmp[145] & ~(tmp[47] ^ tmp[173] & ~tmp[37] ^ tmp[111] & ~(tmp[79] ^ tmp[15] ^ tmp[15] & tmp[130])) ^ tmp[111] & (tmp[47] ^ tmp[130] & ~tmp[47]) ^ tmp[130] & (
						tmp[181] ^ tmp[119]));
		tmp[173] =
				tmp[12] ^ tmp[181] ^ tmp[121] & tmp[101] ^ (tmp[111] & (tmp[15] ^ tmp[6] & ~tmp[130]) ^ tmp[130] & ~tmp[6]) ^ tmp[145] & ~(tmp[21] ^ tmp[111] & (tmp[101] ^ (tmp[73] ^ tmp[173])) ^ tmp[130] & (
						tmp[181] ^ tmp[121] & ~tmp[181]));
		tmp[27] = tmp[111] & ~(tmp[89] ^ ~(tmp[37] ^ tmp[27]) & tmp[130]) ^ (tmp[74] ^ (tmp[26] ^ tmp[15]) ^ (tmp[130] ^ tmp[145] & (tmp[84] ^ tmp[181] ^ tmp[111] & ~(tmp[181] ^ tmp[73]))));
		tmp[73] = tmp[179] ^ (tmp[121] ^ tmp[79]) ^ tmp[133] & tmp[130] ^ (tmp[111] & (tmp[130] | ~(tmp[21] ^ tmp[181])) ^ tmp[145] & (tmp[73] ^ tmp[111] & (tmp[73] ^ tmp[79] & tmp[130]) ^ (tmp[130]
				| tmp[19] ^ tmp[119])));
		tmp[119] = ~tmp[183];
		tmp[19] = tmp[73] & tmp[119];
		tmp[181] = tmp[2] ^ tmp[19];
		tmp[21] = tmp[118] & tmp[73];
		tmp[119] &= tmp[21];
		tmp[37] = tmp[73] & ~tmp[19];
		tmp[84] = tmp[2] | tmp[73];
		tmp[15] = tmp[73] ^ tmp[21];
		tmp[26] = ~tmp[73];
		tmp[89] = tmp[183] & tmp[26];
		tmp[74] = tmp[73] | tmp[89];
		tmp[101] = tmp[4] ^ tmp[89];
		tmp[6] = tmp[2] ^ tmp[74];
		tmp[42] = tmp[74] ^ (tmp[183] | tmp[84]);
		tmp[70] = tmp[50] ^ (tmp[183] ^ tmp[73]);
		tmp[81] = tmp[125] ^ (tmp[10] | tmp[135]) ^ (tmp[60] ^ tmp[179] & ~(tmp[49] ^ (tmp[136] ^ tmp[120] | tmp[135]))) ^ tmp[127] & (tmp[81] ^ tmp[179] & ~(tmp[49] ^ tmp[81] & tmp[168])
				^ tmp[135] & ~tmp[20]);
		tmp[120] = tmp[16] ^ tmp[81];
		tmp[49] = ~tmp[147];
		tmp[20] = tmp[81] & tmp[49];
		tmp[179] = tmp[120] & tmp[49];
		tmp[10] = ~tmp[81];
		tmp[60] = tmp[16] & tmp[10];
		tmp[125] = ~tmp[38];
		tmp[93] = tmp[49] & tmp[60];
		tmp[117] = tmp[16] & ~tmp[60];
		tmp[13] = tmp[134] ^ (tmp[30] ^ (tmp[110] ^ ((tmp[129] | tmp[13] ^ tmp[17] ^ tmp[87] & (tmp[110] & tmp[76])) ^ tmp[76] & (tmp[67] ^ tmp[18])))) ^ (
				tmp[113] ^ (tmp[129] | tmp[163] ^ tmp[33] ^ tmp[76] & tmp[56]) ^ (tmp[155] | tmp[72] & tmp[87]) | tmp[81]);
		tmp[157] = tmp[127] ^ (tmp[17] ^ (tmp[113] ^ ((tmp[155] | tmp[67] ^ (tmp[67] | tmp[23])) ^ (tmp[129] | tmp[147] ^ tmp[155] & tmp[157]))))
				^ (tmp[163] ^ (tmp[147] | tmp[23]) ^ (tmp[86] & (tmp[175] ^ (tmp[175] | tmp[23]) ^ tmp[76] & tmp[157]) ^ (tmp[155] | tmp[56]))) & tmp[10];
		tmp[67] ^= tmp[32] ^ (tmp[23] ^ (tmp[86] & (tmp[175] ^ (tmp[155] | tmp[64]) ^ tmp[163] & tmp[87]) ^ (tmp[155] | tmp[113] ^ (tmp[62] | tmp[23])))
				^ (tmp[18] ^ (tmp[110] ^ ((tmp[129] | tmp[139] ^ (tmp[155] | tmp[67] ^ tmp[64])) ^ tmp[76] & tmp[139]))) & tmp[10]);
		tmp[139] = ~tmp[67];
		tmp[62] = tmp[186] & tmp[139];
		tmp[87] = tmp[103] & tmp[67];
		tmp[163] = tmp[123] & tmp[67];
		tmp[175] = tmp[123] & ~tmp[163];
		tmp[10] = tmp[186] & ~tmp[175];
		tmp[32] = tmp[186] & tmp[163];
		tmp[17] = tmp[123] & tmp[139];
		tmp[127] = tmp[123] & tmp[62];
		tmp[33] = tmp[123] ^ tmp[67];
		tmp[134] = tmp[186] & ~tmp[33];
		tmp[156] = tmp[123] | tmp[67];
		tmp[188] = tmp[103] & tmp[156];
		tmp[85] = tmp[186] & ~tmp[188];
		tmp[91] = tmp[123] ^ tmp[85];
		tmp[83] = tmp[32] ^ tmp[156];
		tmp[48] = ~tmp[161];
		tmp[53] = tmp[186] & tmp[156];
		tmp[182] = tmp[67] ^ tmp[53];
		tmp[35] = tmp[161] ^ tmp[67];
		tmp[115] = tmp[161] | tmp[67];
		tmp[29] = tmp[48] & tmp[115];
		tmp[112] = tmp[161] & tmp[67];
		tmp[77] = tmp[161] & ~tmp[112];
		tmp[139] &= tmp[161];
		tmp[41] = tmp[16] & tmp[81];
		tmp[178] = tmp[138] ^ (tmp[64] ^ (tmp[147] ^ (tmp[86] & (tmp[56] ^ tmp[76] & (tmp[72] ^ tmp[178])) ^ (tmp[155] | tmp[147] ^ tmp[30])))) ^ (
				tmp[147] & tmp[76] ^ tmp[86] & (tmp[113] ^ tmp[18] ^ tmp[76] & tmp[64]) | tmp[81]);
		tmp[72] = tmp[49] & tmp[41];
		tmp[76] = tmp[117] ^ tmp[72];
		tmp[93] = tmp[98] ^ tmp[167] ^ (tmp[5] & ~(tmp[76] ^ tmp[22] & (tmp[16] ^ tmp[179]) ^ tmp[125] & (tmp[60] ^ tmp[93])) ^ tmp[125] & (tmp[81] ^ tmp[93]) ^ tmp[7] & ~tmp[20]);
		tmp[98] = tmp[35] | tmp[93];
		tmp[30] = tmp[35] & ~tmp[93];
		tmp[56] = tmp[55] | tmp[81];
		tmp[64] = tmp[81] & ~tmp[16];
		tmp[72] ^= tmp[64];
		tmp[18] = tmp[22] & tmp[72];
		tmp[72] = tmp[90] ^ tmp[60] ^ (tmp[18] ^ (tmp[38] | tmp[72])) ^ ((tmp[147] | tmp[117]) ^ tmp[5] & ~(tmp[76] ^ (tmp[7] | tmp[76]) ^ tmp[125] & (tmp[16] & tmp[22] ^ tmp[72])));
		tmp[76] = tmp[37] ^ (tmp[2] | tmp[89]) ^ (tmp[72] & ~tmp[42] ^ tmp[157] & (tmp[84] ^ tmp[15] & ~tmp[72]));
		tmp[60] = tmp[72] & ~tmp[6];
		tmp[90] = ~tmp[95];
		tmp[113] = tmp[72] & tmp[90];
		tmp[49] &= tmp[16] | tmp[64];
		tmp[86] = tmp[95] & tmp[72];
		tmp[179] =
				tmp[126] ^ tmp[81] ^ ((tmp[7] | tmp[55] ^ tmp[41]) ^ (tmp[49] ^ (tmp[5] & (tmp[20] ^ (tmp[117] ^ tmp[125] & (tmp[179] ^ tmp[64])) ^ (tmp[7] | tmp[120] ^ tmp[56])) ^ (tmp[38] | tmp[117]))));
		tmp[64] =
				tmp[22] & (tmp[56] ^ tmp[64]) ^ (tmp[39] ^ (tmp[16] & tmp[54] ^ tmp[120])) ^ (tmp[5] & (tmp[18] ^ (tmp[16] ^ tmp[49] ^ tmp[125] & ((tmp[147] | tmp[7]) ^ (tmp[147] | tmp[64])))) ^ (tmp[38]
						| tmp[167] ^ (tmp[7] | tmp[81] ^ (tmp[147] | tmp[120]))));
		tmp[142] ^= tmp[36];
		tmp[58] ^= tmp[51] ^ (tmp[104] ^ (tmp[152] | tmp[135])) ^ tmp[177] & (tmp[142] ^ (tmp[39] | tmp[75] ^ tmp[144] & tmp[168])) ^ tmp[136] & ~(tmp[144] ^ tmp[160] ^ tmp[39] & ~tmp[142] ^ (tmp[12]
				| tmp[75] & tmp[168] ^ (tmp[184] ^ tmp[58])));
		tmp[102] ^= tmp[1] ^ tmp[105] & tmp[58];
		tmp[168] = tmp[73] & ~(tmp[86] ^ tmp[102]);
		tmp[75] = tmp[95] & tmp[102];
		tmp[142] = tmp[72] & ~(tmp[95] | tmp[102]);
		tmp[144] = ~tmp[102];
		tmp[90] &= tmp[102];
		tmp[39] = ~tmp[90];
		tmp[160] = tmp[72] & tmp[39];
		tmp[12] = tmp[72] & tmp[144];
		tmp[135] = tmp[95] ^ tmp[102];
		tmp[152] = tmp[72] & tmp[135];
		tmp[104] = tmp[113] ^ tmp[135];
		tmp[177] = tmp[95] & tmp[144];
		tmp[51] = tmp[102] | tmp[177];
		tmp[36] = tmp[72] & tmp[51];
		tmp[120] = tmp[160] ^ tmp[177];
		tmp[105] = tmp[1] ^ (tmp[190] ^ (tmp[105] | tmp[58]));
		tmp[190] = tmp[105] & ~tmp[124];
		tmp[1] = ~tmp[105];
		tmp[125] = tmp[128] & tmp[105];
		tmp[49] = tmp[123] & tmp[105];
		tmp[54] = ~tmp[179];
		tmp[167] = tmp[106] & tmp[105];
		tmp[18] = tmp[3] ^ tmp[167];
		tmp[56] = tmp[66] & tmp[105];
		tmp[3] &= tmp[105];
		tmp[22] = tmp[124] ^ tmp[190];
		tmp[117] = tmp[105] & ~tmp[172];
		tmp[83] = tmp[91] ^ (tmp[71] | tmp[105]) ^ (tmp[131] ^ (tmp[179] | tmp[83] ^ (tmp[123] | tmp[105]))) ^ tmp[47] & ~(tmp[182] ^ tmp[44] & tmp[105] ^ tmp[54] & (tmp[83] ^ (tmp[137] | tmp[105])));
		tmp[44] = (tmp[123] ^ tmp[44]) & tmp[1];
		tmp[131] = tmp[172] ^ tmp[3];
		tmp[188] =
				tmp[47] & (tmp[163] ^ (tmp[62] ^ (tmp[105] | tmp[62] ^ tmp[188])) ^ (tmp[179] | tmp[123] ^ tmp[134] ^ tmp[44])) ^ (tmp[155] ^ (tmp[87] ^ tmp[10] ^ (tmp[105] | tmp[175] ^ tmp[32]) ^ tmp[54] & (
						tmp[1] & (tmp[32] ^ tmp[33]) ^ (tmp[123] ^ tmp[127]))));
		tmp[44] = tmp[121] ^ (tmp[182] ^ tmp[1] & (tmp[17] ^ tmp[134])) ^ tmp[54] & (tmp[123] ^ tmp[186] & tmp[33] ^ (tmp[105] | tmp[163] ^ tmp[85])) ^ tmp[47] & ~(tmp[62] ^ tmp[156] ^ (tmp[105]
				| tmp[67] ^ tmp[85]) ^ tmp[54] & (tmp[127] ^ (tmp[17] ^ tmp[44])));
		tmp[172] &= tmp[105];
		tmp[1] = tmp[16] ^ tmp[182] ^ (tmp[47] & (tmp[175] ^ tmp[10] ^ (tmp[105] | tmp[67] ^ tmp[32]) ^ tmp[54] & (tmp[123] ^ tmp[186] & tmp[67] ^ tmp[137] & tmp[1])) ^ (tmp[179] | tmp[91] ^ tmp[105] & (
				tmp[71] ^ tmp[87])) ^ tmp[105] & (tmp[123] ^ tmp[53]));
		tmp[137] = tmp[8] & tmp[58];
		tmp[32] = tmp[133] & tmp[58];
		tmp[87] = tmp[151] & (tmp[78] ^ tmp[32]);
		tmp[71] = tmp[158] & tmp[58];
		tmp[10] = ~tmp[79];
		tmp[78] ^= tmp[58];
		tmp[175] = tmp[151] & tmp[78];
		tmp[176] ^= tmp[24] ^ tmp[61] & tmp[58];
		tmp[54] = ~tmp[161];
		tmp[91] = ~tmp[176];
		tmp[61] = tmp[136] ^ tmp[24] ^ (tmp[61] | tmp[58]);
		tmp[24] = tmp[178] & tmp[91];
		tmp[136] = ~tmp[178];
		tmp[53] = ~(tmp[176] & tmp[136]);
		tmp[182] = tmp[176] & tmp[53];
		tmp[16] = tmp[178] ^ tmp[176];
		tmp[17] = tmp[178] & tmp[176];
		tmp[85] = ~tmp[63];
		tmp[127] = ~tmp[2];
		tmp[156] = tmp[61] & tmp[85];
		tmp[62] = tmp[127] & tmp[156];
		tmp[163] = tmp[2] | tmp[156];
		tmp[33] = tmp[63] | tmp[156];
		tmp[134] = tmp[127] & tmp[33];
		tmp[121] = tmp[63] ^ tmp[163];
		tmp[155] = ~tmp[61];
		tmp[20] = tmp[63] & tmp[155];
		tmp[41] = tmp[63] | tmp[61];
		tmp[55] = tmp[2] | tmp[41];
		tmp[126] = tmp[63] & ~tmp[20];
		tmp[138] = tmp[2] | tmp[126];
		tmp[171] = tmp[2] | tmp[61];
		tmp[127] &= tmp[61];
		tmp[40] = tmp[63] ^ tmp[61];
		tmp[59] = tmp[55] ^ tmp[40];
		tmp[15] = tmp[141] ^ (tmp[70] ^ tmp[60] ^ tmp[157] & ~(tmp[15] & tmp[72])) ^ tmp[61] & ~(tmp[101] ^ tmp[157] & ~(tmp[89] ^ tmp[72] & ~tmp[89]) ^ tmp[101] & tmp[72]);
		tmp[60] = tmp[133] ^ tmp[76] ^ (tmp[73] ^ (tmp[157] & (tmp[19] ^ (tmp[4] ^ tmp[60])) ^ tmp[42] & tmp[72]) ^ tmp[118] & tmp[89]) & tmp[155];
		tmp[74] = tmp[81] ^ tmp[76] ^ ((tmp[183] | tmp[73]) ^ (tmp[157] & ~(tmp[6] ^ (tmp[72] | tmp[84] ^ tmp[74])) ^ tmp[72] & ~(tmp[73] ^ tmp[119])) ^ (tmp[2] | tmp[37])) & tmp[61];
		tmp[21] = tmp[97] ^ (tmp[70] ^ tmp[157] & ~(tmp[181] ^ tmp[21] & tmp[72]) ^ (tmp[50] | tmp[72])) ^ tmp[61] & ~((tmp[119] | tmp[72]) ^ tmp[157] & (tmp[181] ^ tmp[84] & tmp[72]));
		tmp[84] = ~tmp[21];
		tmp[122] =
				tmp[94] ^ (tmp[43] ^ tmp[137] ^ (tmp[79] | tmp[8] ^ tmp[151] & tmp[8]) ^ tmp[151] & ~(tmp[143] & tmp[58])) ^ tmp[170] & ~(tmp[58] & ~tmp[158] ^ ((tmp[133] ^ tmp[151] & tmp[43]) & tmp[122] ^ (
						tmp[133] ^ tmp[151] & (tmp[43] ^ tmp[58] & ~tmp[158]))));
		tmp[94] = ~tmp[122];
		tmp[181] = tmp[112] & tmp[94];
		tmp[119] = tmp[115] | tmp[122];
		tmp[50] = tmp[139] & tmp[94];
		tmp[48] &= tmp[67] & tmp[94];
		tmp[70] = ~tmp[93];
		tmp[97] = tmp[67] ^ tmp[35] & tmp[94];
		tmp[6] = tmp[67] ^ tmp[122];
		tmp[37] = tmp[161] ^ tmp[67] & tmp[94];
		tmp[76] = ~tmp[34];
		tmp[35] = tmp[93] | tmp[67] ^ (tmp[35] | tmp[122]);
		tmp[81] = tmp[161] | tmp[122];
		tmp[139] =
				tmp[92] ^ tmp[115] & tmp[94] ^ (tmp[93] | tmp[29] ^ tmp[50]) ^ tmp[76] & (tmp[112] ^ tmp[35]) ^ (tmp[186] | tmp[77] ^ tmp[81] ^ (tmp[34] | tmp[181] ^ (tmp[67] ^ tmp[30])) ^ tmp[70] & (tmp[139]
						^ tmp[48]));
		tmp[29] = tmp[67] ^ (tmp[29] | tmp[122]);
		tmp[94] = ~tmp[139];
		tmp[81] =
				tmp[79] ^ (tmp[29] ^ tmp[70] & (tmp[115] ^ tmp[181])) ^ ((tmp[112] ^ (tmp[81] ^ (tmp[93] | tmp[112] ^ tmp[119])) ^ (tmp[34] | tmp[67] ^ (tmp[98] ^ tmp[50]))) & ~tmp[186] ^ tmp[76] & (tmp[77]
						^ (tmp[93] | tmp[97])));
		tmp[115] = ~tmp[81];
		tmp[92] = tmp[44] & tmp[115];
		tmp[4] = tmp[44] | tmp[81];
		tmp[19] = tmp[60] & tmp[115];
		tmp[42] = tmp[60] | tmp[81];
		tmp[48] = tmp[93] & ~tmp[6] ^ (tmp[114] ^ tmp[37]) ^ ((tmp[186] | tmp[97] ^ tmp[93] & tmp[181] ^ tmp[76] & (tmp[112] ^ (tmp[98] ^ tmp[48]))) ^ (tmp[34] | tmp[50] ^ tmp[93] & ~tmp[97]));
		tmp[30] = tmp[23] ^ tmp[37] ^ tmp[70] & tmp[6] ^ (tmp[34] | tmp[119] ^ (tmp[67] ^ tmp[35])) ^ (tmp[186] | tmp[76] & (tmp[119] ^ (tmp[77] ^ tmp[30])) ^ (tmp[29] ^ tmp[50] & tmp[70]));
		tmp[77] = ~tmp[30];
		tmp[70] = tmp[58] & ~tmp[185];
		tmp[143] = tmp[159] ^ (tmp[8] ^ tmp[58] ^ tmp[151] & ~(tmp[133] ^ tmp[32]) ^ tmp[170] & ~(tmp[43] ^ tmp[185] & tmp[133] & tmp[58] ^ (tmp[79] | tmp[133] & tmp[143] ^ tmp[32] ^ tmp[151] & ~(tmp[185]
				^ tmp[58] & ~tmp[133])) ^ tmp[151] & ~(tmp[43] ^ tmp[70]))) ^ (tmp[79] | tmp[137] ^ (tmp[133] ^ tmp[175]));
		tmp[32] = tmp[24] ^ tmp[24] & tmp[143];
		tmp[137] = tmp[178] & tmp[143];
		tmp[159] = tmp[143] & ~tmp[16];
		tmp[50] = ~tmp[173];
		tmp[119] = ~tmp[143];
		tmp[35] = tmp[16] & tmp[143];
		tmp[29] = tmp[173] & tmp[143];
		tmp[76] = tmp[16] ^ tmp[35];
		tmp[6] = tmp[132] & tmp[119];
		tmp[37] = tmp[143] & tmp[50];
		tmp[23] = tmp[173] | tmp[143];
		tmp[98] = tmp[136] & tmp[23];
		tmp[97] = ~tmp[23];
		tmp[112] = tmp[132] & tmp[97];
		tmp[181] = tmp[132] & ~(tmp[119] & tmp[23]);
		tmp[11] ^= tmp[132] ^ (tmp[143] ^ tmp[98]) ^ tmp[165] & (tmp[23] ^ tmp[178] & (tmp[119] | tmp[29])) ^ (tmp[63] | tmp[173] ^ (tmp[178] & (tmp[165] & tmp[173]) ^ (tmp[181] ^ (tmp[178]
				| tmp[23] ^ tmp[112]))));
		tmp[114] = tmp[21] | tmp[11];
		tmp[89] = tmp[23] ^ tmp[132] & tmp[23];
		tmp[97] = tmp[112] ^ (tmp[187] ^ tmp[29]) ^ (tmp[178] | tmp[89]) ^ tmp[165] & (tmp[23] ^ tmp[136] & (tmp[6] ^ tmp[23])) ^ tmp[85] & (tmp[165] & ~(tmp[178] & tmp[97]) ^ tmp[178] & ~tmp[89]);
		tmp[89] = tmp[176] & tmp[143];
		tmp[187] = tmp[143] & ~tmp[182];
		tmp[112] = tmp[176] ^ tmp[89];
		tmp[118] = tmp[54] & tmp[112];
		tmp[155] = tmp[173] ^ tmp[143];
		tmp[101] = tmp[132] & tmp[155];
		tmp[6] = tmp[185] ^ tmp[173] ^ tmp[132] & tmp[143] ^ tmp[165] & ~(tmp[137] ^ tmp[29]) ^ tmp[136] & (tmp[155] ^ tmp[132] & tmp[37]) ^ tmp[85] & (tmp[29] ^ tmp[6] ^ tmp[165] & (tmp[143] ^ (tmp[178]
				| tmp[29])) ^ (tmp[178] | tmp[155] ^ tmp[101]));
		tmp[23] ^=
				tmp[129] ^ (tmp[101] ^ ((tmp[63] | tmp[165] & (tmp[132] & ~tmp[173] ^ tmp[37] ^ tmp[98]) ^ tmp[136] & (tmp[29] ^ tmp[132] & tmp[29])) ^ tmp[165] & ~(tmp[136] & (tmp[132] & tmp[173] ^ tmp[23])
						^ (tmp[143] ^ tmp[181])) ^ (tmp[178] | tmp[155] ^ tmp[132] & (tmp[173] & tmp[119]))));
		tmp[53] =
				tmp[7] ^ tmp[16] ^ (tmp[187] ^ tmp[54] & (tmp[176] ^ tmp[53] & tmp[143])) ^ tmp[93] & ~(tmp[187] ^ tmp[54] & tmp[137]) ^ tmp[27] & ~(tmp[187] ^ (tmp[161] & tmp[119] ^ tmp[93] & tmp[187]));
		tmp[91] &= tmp[143];
		tmp[187] = tmp[17] & tmp[143];
		tmp[187] =
				tmp[189] ^ tmp[161] ^ (tmp[16] ^ tmp[137]) ^ tmp[93] & ~(tmp[24] ^ (tmp[91] ^ tmp[54] & tmp[76])) ^ tmp[27] & (tmp[137] ^ (tmp[161] | tmp[178] ^ tmp[35]) ^ tmp[93] & ~(tmp[187] ^ (tmp[17] ^ (
						tmp[161] | tmp[176] ^ tmp[159]))));
		tmp[182] =
				tmp[27] & ~(tmp[182] ^ tmp[54] & tmp[91] ^ tmp[143] & ~(tmp[178] | tmp[176]) ^ tmp[93] & ~(tmp[178] ^ (tmp[91] ^ (tmp[161] | tmp[91])))) ^ (tmp[176] & tmp[54] ^ (tmp[170] ^ tmp[178]) ^ (
						tmp[143] ^ tmp[93] & (tmp[161] | ~tmp[32])));
		tmp[35] = tmp[182] & ~tmp[11];
		tmp[137] = tmp[21] | tmp[182];
		tmp[189] = tmp[11] | tmp[137];
		tmp[119] = tmp[84] & tmp[182];
		tmp[7] = tmp[11] & tmp[119];
		tmp[29] = tmp[11] & ~tmp[182];
		tmp[37] = tmp[11] ^ tmp[182];
		tmp[181] = tmp[84] & tmp[37];
		tmp[159] =
				tmp[27] & ~(tmp[118] ^ (tmp[32] ^ tmp[93] & ~(tmp[76] ^ (tmp[161] | tmp[16] ^ tmp[159])))) ^ (tmp[111] ^ tmp[178] ^ (tmp[143] & ~tmp[24] ^ (tmp[161] | tmp[112])) ^ tmp[93] & (tmp[17] ^ tmp[91]
						^ tmp[54] & (tmp[17] ^ tmp[89])));
		tmp[16] = tmp[44] & tmp[159];
		tmp[76] = ~tmp[159];
		tmp[8] = ~tmp[151] & (tmp[133] ^ tmp[71]) ^ tmp[78] ^ (tmp[31] ^ tmp[10] & (tmp[175] ^ (tmp[43] ^ tmp[71]))) ^ tmp[170] & (tmp[151] & tmp[71] ^ (tmp[8] ^ tmp[71]) ^ (tmp[79]
				| tmp[43] ^ tmp[87] ^ tmp[58] & ~tmp[8]));
		tmp[31] = ~tmp[8];
		tmp[117] =
				tmp[147] ^ (tmp[123] ^ tmp[172] ^ tmp[13] & ~tmp[117]) ^ (tmp[57] & ~(tmp[167] ^ (tmp[66] ^ tmp[13] & (tmp[123] ^ tmp[49])) ^ (tmp[123] ^ tmp[190] ^ tmp[13] & ~(tmp[128] ^ tmp[49])) & tmp[31])
						^ (tmp[3] ^ tmp[13] & ~(tmp[66] ^ tmp[117]) | tmp[8]));
		tmp[167] = tmp[74] & tmp[117];
		tmp[3] = ~tmp[74];
		tmp[147] = tmp[117] & tmp[3];
		tmp[80] = tmp[151] ^ (tmp[18] ^ tmp[13] & tmp[131]) ^ (tmp[57] & (tmp[123] ^ tmp[125] ^ (tmp[128] ^ tmp[13] & ~(tmp[128] ^ tmp[80] & tmp[105]) ^ tmp[105] & ~tmp[128] | tmp[8]))
				^ (tmp[22] ^ tmp[13] & (tmp[116] ^ tmp[172])) & tmp[31]);
		tmp[128] = tmp[80] & ~tmp[60];
		tmp[172] = tmp[80] & ~tmp[128];
		tmp[78] = tmp[81] | tmp[172];
		tmp[89] = tmp[81] | tmp[128];
		tmp[17] = tmp[115] & tmp[128];
		tmp[24] = tmp[80] ^ tmp[89];
		tmp[54] = ~tmp[80];
		tmp[91] = tmp[60] & tmp[54];
		tmp[112] = tmp[80] | tmp[91];
		tmp[32] = tmp[81] | tmp[80];
		tmp[111] = tmp[60] | tmp[80];
		tmp[118] = tmp[60] ^ tmp[80];
		tmp[136] = tmp[81] | tmp[118];
		tmp[98] = tmp[60] & tmp[80];
		tmp[155] = tmp[115] & tmp[98];
		tmp[144] = tmp[168] ^ tmp[104] ^ (tmp[110] ^ (tmp[152] | tmp[8])) ^ (tmp[66] | tmp[120] ^ tmp[73] & (tmp[160] ^ tmp[51]) ^ (tmp[75] ^ tmp[152] ^ tmp[95] & (tmp[73] & tmp[144])) & tmp[31]);
		tmp[160] = tmp[30] & tmp[144];
		tmp[110] = ~tmp[160];
		tmp[101] = tmp[74] & tmp[110];
		tmp[129] = ~tmp[144];
		tmp[110] &= tmp[144];
		tmp[85] = tmp[74] & ~tmp[110];
		tmp[141] = tmp[30] & tmp[129];
		tmp[99] = tmp[74] & tmp[141];
		tmp[153] = tmp[187] | tmp[144];
		tmp[166] = tmp[144] & ~(tmp[187] & tmp[144]);
		tmp[45] = tmp[129] & tmp[153];
		tmp[162] = ~tmp[117];
		tmp[154] = ~tmp[187];
		tmp[68] = tmp[187] ^ tmp[144];
		tmp[149] = tmp[144] & tmp[154];
		tmp[69] = tmp[30] | tmp[144];
		tmp[164] = tmp[74] & tmp[69];
		tmp[140] = tmp[77] & tmp[144];
		tmp[107] = tmp[74] & ~tmp[69];
		tmp[9] = tmp[129] & tmp[69];
		tmp[180] = tmp[101] ^ tmp[9];
		tmp[148] = tmp[99] ^ tmp[9];
		tmp[96] = tmp[74] & ~tmp[9];
		tmp[69] ^= tmp[96];
		tmp[191] = tmp[144] ^ tmp[164];
		tmp[14] = tmp[144] & tmp[162];
		tmp[146] = tmp[30] ^ tmp[144];
		tmp[142] = tmp[25] ^ (tmp[36] ^ (tmp[90] ^ tmp[73] & (tmp[142] ^ tmp[135])) ^ (tmp[51] ^ (tmp[73] | tmp[135]) | tmp[8])) ^ (tmp[66]
				| tmp[142] ^ tmp[73] & ~tmp[104] ^ tmp[102] & tmp[39] ^ (tmp[51] ^ tmp[73] & tmp[104]) & tmp[31]);
		tmp[51] = ~tmp[142];
		tmp[39] = tmp[7] ^ tmp[29] ^ ((tmp[83] | tmp[181] ^ (tmp[11] ^ tmp[51] & (tmp[35] ^ tmp[137]))) ^ tmp[51] & (tmp[21] ^ tmp[11] & ~tmp[29]));
		tmp[137] |= tmp[142];
		tmp[119] = tmp[137] ^ (tmp[7] ^ (tmp[37] ^ (tmp[83] | tmp[114] ^ tmp[51] & (tmp[119] ^ tmp[29]) ^ (tmp[11] | tmp[35]))));
		tmp[7] = ~tmp[83];
		tmp[181] ^= tmp[182] ^ tmp[7] & (tmp[11] ^ tmp[142] & (tmp[114] ^ tmp[182])) ^ tmp[189] & tmp[51];
		tmp[37] = (tmp[21] | tmp[29]) ^ (tmp[35] ^ ((tmp[189] ^ tmp[29] | tmp[142]) ^ tmp[7] & (tmp[11] ^ tmp[51] & (tmp[114] ^ tmp[37]))));
		tmp[120] = tmp[104] ^ tmp[26] & (tmp[75] ^ tmp[36]) ^ (tmp[38] ^ (tmp[102] ^ tmp[168]) & tmp[31]) ^ (tmp[66] | tmp[120] ^ tmp[26] & tmp[120] ^ tmp[73] & (tmp[72] ^ tmp[135]) & tmp[31]);
		tmp[26] = ~tmp[120];
		tmp[168] = tmp[74] & tmp[120];
		tmp[75] = tmp[74] & ~tmp[168];
		tmp[38] = tmp[117] & ~tmp[75];
		tmp[104] = tmp[74] ^ tmp[120];
		tmp[114] = tmp[74] | tmp[120];
		tmp[51] = tmp[117] & ~tmp[114];
		tmp[29] = tmp[167] ^ tmp[114];
		tmp[189] = tmp[117] & tmp[120];
		tmp[7] = tmp[147] ^ tmp[120];
		tmp[116] =
				tmp[66] ^ tmp[13] & ~(tmp[124] ^ tmp[56]) ^ tmp[103] & tmp[105] ^ (tmp[109] ^ (tmp[22] ^ tmp[13] & (tmp[66] ^ tmp[116] & tmp[105]) | tmp[8])) ^ tmp[57] & (tmp[131] ^ tmp[190] & tmp[31]);
		tmp[22] = tmp[48] | tmp[116];
		tmp[103] = ~tmp[116];
		tmp[131] = ~tmp[1];
		tmp[109] = tmp[48] & tmp[103];
		tmp[152] = tmp[73] ^ (tmp[113] ^ tmp[102]) ^ (tmp[145] ^ (tmp[135] ^ (tmp[12] ^ tmp[73] & ~(tmp[102] ^ tmp[152])) | tmp[8])) ^ (tmp[66] | tmp[86] ^ (tmp[95] ^ tmp[73] & (tmp[102] ^ tmp[36]))
				^ (tmp[152] ^ (tmp[135] ^ tmp[73] & (tmp[12] ^ tmp[177]))) & tmp[31]);
		tmp[177] = tmp[4] | tmp[152];
		tmp[12] = ~tmp[152];
		tmp[49] = tmp[124] ^ tmp[105] & ~tmp[106] ^ tmp[13] & ~tmp[18] ^ (tmp[52] ^ (~tmp[13] & (tmp[106] ^ tmp[56]) | tmp[8])) ^ tmp[57] & ~(tmp[49] ^ tmp[13] & (tmp[66] ^ tmp[125])
				^ (tmp[190] ^ tmp[13] & tmp[49]) & tmp[31]);
		tmp[125] = ~tmp[49];
		tmp[190] = tmp[159] & tmp[125];
		tmp[56] = ~tmp[190];
		tmp[106] = tmp[44] & tmp[56];
		tmp[31] = tmp[44] & tmp[190];
		tmp[18] = tmp[76] & tmp[49];
		tmp[124] = ~tmp[18];
		tmp[52] = tmp[44] & tmp[124];
		tmp[36] = tmp[159] & tmp[49];
		tmp[135] = tmp[187] & tmp[49];
		tmp[154] &= tmp[49];
		tmp[86] = tmp[144] ^ tmp[154];
		tmp[113] = tmp[166] ^ tmp[135];
		tmp[145] = tmp[49] & ~tmp[68];
		tmp[35] = tmp[68] & tmp[49];
		tmp[21] = tmp[159] | tmp[49];
		tmp[137] = ~tmp[21];
		tmp[90] = tmp[44] & tmp[137];
		tmp[25] = tmp[144] & tmp[49];
		tmp[174] = tmp[149] & tmp[49];
		tmp[46] = tmp[159] ^ tmp[49];
		tmp[88] = tmp[144] ^ tmp[145];
		tmp[169] = tmp[16] ^ tmp[46];
		tmp[100] = tmp[44] & tmp[46];
		tmp[70] = tmp[184] ^ (tmp[133] ^ (tmp[79] | tmp[158] ^ tmp[43] & tmp[58] ^ tmp[87]) ^ tmp[185] & tmp[58] ^ tmp[151] & (tmp[185] ^ tmp[58]) ^ tmp[170] & ~(tmp[10] & (tmp[71] ^ tmp[175]) ^ (tmp[158]
				^ tmp[151] & ~(tmp[185] ^ tmp[70]))));
		tmp[185] = ~tmp[70];
		tmp[55] |= tmp[70];
		tmp[82] =
				tmp[108] ^ (tmp[64] & ~(tmp[171] ^ tmp[63] & tmp[61] ^ tmp[55] ^ (tmp[173] | tmp[126] ^ tmp[138] ^ (tmp[82] | tmp[70]))) ^ (tmp[0] ^ tmp[156] ^ (tmp[82] ^ tmp[126]) & tmp[185] ^ tmp[50] & (
						tmp[59] ^ (tmp[61] ^ tmp[171] | tmp[70]))));
		tmp[108] = ~tmp[82];
		tmp[43] = ~tmp[97];
		tmp[127] =
				tmp[5] ^ (tmp[2] ^ tmp[40] ^ tmp[55] ^ tmp[50] & (tmp[121] ^ (tmp[63] ^ tmp[127]) & tmp[70]) ^ tmp[64] & (tmp[61] ^ tmp[127] ^ tmp[59] & tmp[185] ^ (tmp[173] | tmp[138] ^ tmp[2] & tmp[185])));
		tmp[59] = tmp[116] | tmp[127];
		tmp[40] = ~tmp[48];
		tmp[50] = tmp[127] & tmp[40];
		tmp[55] = ~tmp[1];
		tmp[5] = ~tmp[116];
		tmp[158] = tmp[127] & tmp[5];
		tmp[151] = tmp[48] | tmp[127];
		tmp[175] = ~tmp[127];
		tmp[71] = tmp[48] & tmp[127];
		tmp[87] = tmp[116] | tmp[71];
		tmp[10] = tmp[48] ^ tmp[127];
		tmp[79] = tmp[116] | tmp[10];
		tmp[133] = tmp[5] & tmp[10];
		tmp[170] = ~tmp[70];
		tmp[134] ^= tmp[64] & (tmp[163] ^ tmp[126] ^ (tmp[121] | tmp[70]) ^ (tmp[173] | tmp[63] ^ tmp[134] ^ (tmp[63] ^ tmp[0]) & tmp[185])) ^ (tmp[58] ^ (tmp[41] ^ tmp[33] & tmp[185])) ^ (tmp[173]
				| tmp[63] ^ tmp[171] ^ (tmp[61] ^ tmp[138]) & tmp[170]);
		tmp[121] = ~tmp[134];
		tmp[126] = ~tmp[182];
		tmp[138] = ~tmp[81];
		tmp[41] = tmp[130] ^ (tmp[163] ^ tmp[20] ^ (tmp[156] ^ tmp[62] | tmp[70]) ^ (tmp[173] | tmp[61] ^ (tmp[2] | tmp[20]) ^ (tmp[62] ^ tmp[20] | tmp[70]))) ^ tmp[64] & ~(
				(tmp[173] | tmp[61] ^ tmp[62] ^ tmp[0] & tmp[185]) ^ (tmp[62] ^ tmp[33] ^ (tmp[163] ^ tmp[41]) & tmp[170]));
		tmp[163] = tmp[44] & tmp[41];
		tmp[185] = tmp[138] & tmp[163];
		tmp[0] = tmp[163] ^ tmp[185];
		tmp[62] = tmp[41] & ~tmp[163];
		tmp[20] = tmp[44] ^ tmp[41];
		tmp[170] = tmp[92] ^ tmp[20];
		tmp[33] = ~tmp[41];
		tmp[156] = tmp[44] | tmp[41];
		tmp[130] = tmp[4] ^ tmp[156];
		tmp[171] = tmp[41] & ~tmp[44];
		tmp[58] = tmp[138] & tmp[171];
		tmp[184] = ~tmp[159];
		tmp[150] = tmp[163] ^ tmp[138] & (tmp[44] & tmp[33]);
		vector[0] = tmp[2] ^ (tmp[169] ^ (tmp[139] | tmp[49] & tmp[124] ^ tmp[44] & (tmp[49] | tmp[190])) ^ tmp[15] & (tmp[52] ^ (tmp[49] ^ tmp[139] & (tmp[18] ^ tmp[44] & tmp[18]))) ^ tmp[97] & ~(tmp[16]
				^ tmp[190] ^ tmp[139] & (tmp[159] ^ tmp[16]) ^ tmp[15] & (tmp[169] ^ tmp[139] & tmp[18])));
		vector[1] = tmp[144];
		vector[2] =
				tmp[66] ^ (tmp[71] ^ (tmp[116] ^ tmp[11] & ~(tmp[116] & tmp[131])) ^ (tmp[1] | tmp[151] ^ tmp[5] & tmp[151]) ^ tmp[142] & ~(tmp[131] & (tmp[48] ^ tmp[109]) ^ tmp[11] & ~(tmp[48] & tmp[175] ^ (
						tmp[1] | tmp[48] ^ tmp[79]))));
		vector[3] = tmp[76];
		vector[4] =
				tmp[142] & (tmp[48] ^ tmp[22] ^ (tmp[1] | tmp[22]) ^ tmp[11] & ~(tmp[109] ^ (tmp[1] | tmp[22] ^ tmp[50]))) ^ (tmp[186] ^ ((tmp[1] | tmp[50] & tmp[5]) ^ (tmp[59] ^ tmp[10]) ^ tmp[11] & (tmp[59]
						^ tmp[50] & tmp[55])));
		vector[5] = tmp[182];
		vector[6] =
				(tmp[182] | tmp[78] ^ tmp[98] ^ tmp[24] & tmp[121] ^ tmp[6] & ~(tmp[60] ^ tmp[136] ^ (tmp[118] | tmp[134]))) ^ (tmp[118] ^ tmp[115] & tmp[112] ^ (tmp[91] ^ tmp[136]) & tmp[121] ^ (tmp[143]
						^ tmp[6] & ~(tmp[60] ^ tmp[89] ^ tmp[19] & tmp[121])));
		vector[7] = ~tmp[1];
		vector[8] = tmp[146] ^ (tmp[157] ^ tmp[162] & tmp[164]) ^ tmp[188] & ~(tmp[144] ^ (tmp[117] | tmp[110]) ^ tmp[74] & ~tmp[146]) ^ tmp[23] & ~(tmp[85] ^ ((tmp[117] | tmp[85]) ^ tmp[188] & (tmp[148]
				^ tmp[117] & ~(tmp[144] ^ tmp[74] & tmp[129]))));
		vector[9] = tmp[82];
		vector[10] = tmp[74] ^ (tmp[13] ^ tmp[146]) ^ (tmp[188] & (tmp[160] ^ (tmp[117] | tmp[180]) ^ tmp[74] & tmp[160]) ^ (tmp[117] | tmp[110] ^ tmp[74] & tmp[146])) ^ tmp[23] & ~(
				(tmp[117] | tmp[160] ^ tmp[107]) ^ (tmp[144] ^ tmp[188] & (tmp[101] ^ tmp[140] ^ tmp[129] & (tmp[74] & tmp[162]))));
		vector[11] = tmp[33];
		vector[12] = tmp[34] ^ (tmp[45] ^ tmp[49] ^ tmp[108] & (tmp[144] ^ tmp[30] & ~(tmp[68] ^ tmp[145])) ^ (tmp[97] | tmp[144] ^ tmp[174] ^ tmp[30] & ~(tmp[166] ^ tmp[174]) ^ (tmp[82]
				| tmp[144] ^ tmp[30] & ~tmp[25])));
		vector[13] = tmp[84];
		vector[14] = ~(tmp[132] ^ (tmp[46] ^ tmp[139] & ~tmp[106] ^ tmp[15] & ~tmp[31]) ^ tmp[97] & ~(tmp[31] ^ tmp[15] & (tmp[139] & tmp[56] ^ tmp[44] & tmp[125])));
		vector[15] = tmp[26];
		vector[16] = ~(tmp[183] ^ (tmp[55] & (tmp[109] ^ tmp[127]) ^ (tmp[48] ^ tmp[133]) ^ tmp[11] & (tmp[158] ^ tmp[151] ^ tmp[55] & (tmp[127] ^ (tmp[116] | tmp[151]))) ^ tmp[142] & (tmp[59]
				^ tmp[1] & tmp[133] ^ tmp[11] & ~(tmp[158] ^ (tmp[1] | tmp[10] ^ tmp[79])))));
		vector[17] = tmp[187];
		vector[18] =
				tmp[57] ^ (tmp[44] ^ tmp[36] ^ tmp[139] & tmp[21] ^ tmp[15] & ~(tmp[106] ^ (tmp[49] ^ tmp[139] & tmp[90])) ^ tmp[97] & (tmp[18] ^ tmp[94] & (tmp[36] ^ tmp[44] & tmp[36]) ^ tmp[15] & ~(tmp[190]
						^ tmp[100] ^ tmp[139] & tmp[137])));
		vector[19] = tmp[12];
		vector[20] =
				tmp[122] ^ (tmp[6] & ~(tmp[172] ^ tmp[155] ^ tmp[98] & tmp[121]) ^ (tmp[78] ^ tmp[91] ^ (tmp[128] | tmp[134])) ^ (tmp[182] | tmp[6] & (tmp[172] ^ tmp[32] ^ tmp[128] & tmp[121]) ^ (tmp[128]
						^ tmp[155] ^ tmp[80] & tmp[121])));
		vector[21] = ~tmp[83];
		vector[22] =
				tmp[165] ^ (tmp[10] ^ tmp[5] & tmp[71] ^ (tmp[1] | tmp[48] ^ tmp[87]) ^ tmp[11] & (tmp[55] | ~(tmp[87] ^ tmp[40] & tmp[151])) ^ tmp[142] & ~(tmp[71] ^ tmp[11] & (tmp[127] ^ tmp[127] & tmp[55])
						^ tmp[55] & (tmp[116] ^ tmp[48] & ~tmp[71])));
		vector[23] = ~tmp[53];
		vector[24] = ~(tmp[73] ^ (tmp[4] & tmp[152] ^ tmp[170] ^ tmp[60] & (tmp[41] ^ (tmp[81] | tmp[152] | tmp[33] & tmp[156])) ^ tmp[184] & (tmp[58] ^ (tmp[152] | tmp[41] ^ tmp[138] & tmp[20])
				^ tmp[60] & ~(tmp[177] ^ (tmp[163] ^ tmp[138] & tmp[41])))));
		vector[25] = tmp[125];
		vector[26] =
				tmp[123] ^ (tmp[68] ^ tmp[154] ^ (tmp[30] | tmp[35]) ^ (tmp[82] | tmp[25] ^ tmp[30] & ~tmp[45]) ^ tmp[43] & (tmp[77] & (tmp[49] & ~tmp[153]) ^ (tmp[82] | tmp[35] ^ (tmp[30] | tmp[86]))));
		vector[27] = tmp[60];
		vector[28] = ~(tmp[93] ^ (tmp[29] ^ (tmp[127] ^ tmp[53] & (tmp[74] ^ tmp[147] | tmp[127])) ^ tmp[1] & ~(tmp[168] ^ tmp[117] & tmp[168] ^ tmp[38] & tmp[127] ^ tmp[53] & (tmp[117] & tmp[127]))));
		vector[29] = tmp[11];
		vector[30] =
				tmp[63] ^ (tmp[154] ^ (tmp[68] ^ tmp[30] & tmp[35]) ^ (tmp[82] | tmp[88] ^ tmp[30] & tmp[86]) ^ (tmp[97] | tmp[113] ^ tmp[30] & (tmp[149] ^ tmp[49] & ~tmp[166]) ^ tmp[108] & (tmp[135] ^ (
						tmp[144] ^ tmp[30] & ~tmp[145]))));
		vector[31] = tmp[74];
		vector[32] = ~(tmp[72] ^ (tmp[53] & ~(tmp[74] ^ tmp[127]) ^ (tmp[117] ^ tmp[168] ^ tmp[3] & tmp[120] & tmp[127]) ^ tmp[1] & ~(tmp[51] & tmp[175] ^ tmp[53] & (tmp[147] ^ tmp[7] & tmp[127]))));
		vector[33] = tmp[97];
		vector[34] = tmp[37] ^ (tmp[105] ^ tmp[39] & tmp[134]);
		vector[35] = tmp[115];
		vector[36] = tmp[161] ^ (tmp[52] ^ (tmp[190] ^ tmp[139] & (tmp[159] ^ tmp[106])) ^ tmp[15] & (tmp[49] ^ tmp[100] ^ tmp[139] & ~tmp[169]) ^ tmp[97] & (tmp[90] ^ (tmp[139] | tmp[46]) | ~tmp[15]));
		vector[37] = tmp[142];
		vector[38] = ~(tmp[173] ^ ((tmp[152] | tmp[130]) ^ tmp[150] ^ tmp[60] & (tmp[185] ^ (tmp[152] | tmp[150])) ^ (tmp[159] | tmp[60] & (tmp[92] & tmp[12] ^ tmp[185]) ^ (tmp[185]
				^ tmp[152] & ~tmp[130]))));
		vector[39] = tmp[117];
		vector[40] = ~(tmp[95] ^ (tmp[30] ^ (tmp[153] ^ tmp[135]) ^ (tmp[144] ^ tmp[129] & tmp[49]) & tmp[108] ^ tmp[43] & (tmp[86] ^ (tmp[30] | tmp[113]) ^ tmp[108] & (tmp[88] ^ tmp[30] & ~tmp[135]))));
		vector[41] = tmp[15];
		vector[42] =
				tmp[47] ^ (tmp[60] & ~(tmp[0] ^ tmp[152] & ~(tmp[92] ^ tmp[62])) ^ (tmp[4] ^ (tmp[152] | tmp[62] ^ tmp[58])) ^ tmp[184] & (tmp[163] ^ tmp[58] ^ tmp[60] & tmp[0] ^ (tmp[152] | tmp[171] ^ (
						tmp[81] | tmp[62]))));
		vector[43] = tmp[54];
		vector[44] =
				tmp[27] ^ (tmp[60] & ~(tmp[81] & tmp[152]) ^ (tmp[177] ^ tmp[170])) ^ (tmp[159] | tmp[4] & tmp[12] ^ tmp[163] ^ (tmp[81] | tmp[163]) ^ tmp[60] & ~((tmp[92] | tmp[152]) ^ (tmp[20] ^ tmp[58])));
		vector[45] = tmp[103];
		vector[46] = ~(tmp[70] ^ (tmp[42] ^ tmp[118] ^ (tmp[80] ^ tmp[155] | tmp[134]) ^ tmp[6] & ~(tmp[24] ^ tmp[81] & tmp[121])) ^ tmp[126] & ((tmp[81] | tmp[111]) ^ (tmp[80] ^ tmp[32]) & tmp[121]
				^ tmp[6] & ~(tmp[60] ^ tmp[17] ^ (tmp[42] ^ tmp[80]) & tmp[121])));
		vector[47] = ~tmp[188];
		vector[48] = ~(tmp[102] ^ (tmp[37] ^ (tmp[39] | tmp[134])));
		vector[49] = tmp[94];
		vector[50] = ~(tmp[179] ^ (tmp[7] ^ tmp[53] & ~(tmp[117] ^ tmp[127] & ~tmp[167]) ^ (tmp[74] ^ tmp[51]) & tmp[127] ^ tmp[1] & (tmp[53] & (tmp[74] ^ tmp[117] ^ tmp[127] & ~(tmp[74] ^ tmp[167])) ^ (
				tmp[29] ^ tmp[127] & ~(tmp[75] ^ tmp[189])))));
		vector[51] = tmp[6];
		vector[52] = ~(tmp[176] ^ (tmp[181] ^ (tmp[119] | tmp[134])));
		vector[53] = tmp[48];
		vector[54] =
				tmp[1] & (tmp[117] ^ tmp[120] ^ tmp[127] & (tmp[117] | ~tmp[114]) ^ tmp[53] & ~(tmp[120] ^ tmp[117] & tmp[104] ^ tmp[147] & tmp[127])) ^ (tmp[64] ^ (tmp[74] ^ tmp[189] ^ tmp[127] & ~(tmp[75]
						^ tmp[38])) ^ tmp[53] & (tmp[120] ^ tmp[117] & (tmp[74] & tmp[26]) ^ tmp[127] & ~(tmp[104] ^ tmp[189])));
		vector[55] = tmp[23];
		vector[56] =
				tmp[8] ^ (tmp[126] & (tmp[60] ^ (tmp[78] | tmp[134]) ^ tmp[6] & (tmp[17] ^ tmp[112] ^ (tmp[19] ^ tmp[128]) & tmp[121])) ^ (tmp[6] & ~(tmp[89] ^ tmp[111] ^ (tmp[42] ^ tmp[128] | tmp[134])) ^ (
						tmp[89] ^ tmp[91] ^ tmp[32] & tmp[121])));
		vector[57] = ~tmp[44];
		vector[58] =
				tmp[9] ^ (tmp[67] ^ tmp[107]) ^ (tmp[188] & (tmp[30] ^ tmp[74] & tmp[77] ^ tmp[162] & (tmp[99] ^ tmp[146])) ^ (tmp[117] | tmp[160] ^ tmp[96])) ^ tmp[23] & (tmp[117] & ~tmp[148] ^ (tmp[180]
						^ tmp[188] & ~(tmp[69] ^ tmp[74] & tmp[14])));
		vector[59] = tmp[121];
		vector[60] = tmp[160] ^ (tmp[178] ^ (tmp[164] ^ tmp[162] & tmp[69])) ^ tmp[188] & ~(tmp[191] ^ tmp[117] & (tmp[74] ^ tmp[141])) ^ tmp[23] & (tmp[191] ^ (tmp[117] | tmp[99]) ^ tmp[188] & (tmp[164]
				^ tmp[140] ^ tmp[14]));
		vector[61] = tmp[127];
		vector[62] = ~(tmp[61] ^ (tmp[181] ^ tmp[119] & tmp[134]));
		vector[63] = tmp[30];

		ByteBuffer byteBuf_out = ByteBuffer.allocate(0x100).order(ByteOrder.BIG_ENDIAN);
		IntBuffer intBuf_out = byteBuf_out.asIntBuffer();
		intBuf_out.put(vector);

		return byteBuf_out.array();
	}


	public static class CipherText {
		Rand rand;
		byte[] prefix;
		public ArrayList<byte[]> content;

		int totalsize;
		int inputLen;

		byte[] intToBytes(long x) {
			ByteBuffer buffer = ByteBuffer.allocate(4);
			buffer.putInt(new BigInteger(String.valueOf(x)).intValue());
			return buffer.array();
		}

		/**
		 * Create new CipherText with contents and IV.
		 *
		 * @param input the contents
		 * @param ms the time
		 */
		public CipherText(byte[] input, long ms, Rand rand) {
			this.inputLen = input.length;
			this.rand = rand;
			prefix = new byte[32];
			content = new ArrayList<>();
			int roundedsize = input.length + (256 - (input.length % 256));
			for (int i = 0; i < roundedsize / 256; ++i) {
				content.add(new byte[256]);
			}
			totalsize = roundedsize + 5;

			prefix = intToBytes(ms);

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

			buff.put(totalsize - 1, makeIntegrityByte(rand));
			return buff;
		}
	}
}