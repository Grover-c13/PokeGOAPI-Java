package com.pokegoapi.network.spec;

/**
 * Created by chris on 2/28/2017.
 */
public interface Cipher {

    class Rand {
        private long state;

        private Rand(long state) {
            this.state = state;
        }

        private byte next() {
            state = (state * 0x41C64E6D) + 0x3039;
            return (byte) ((state >> 16) & 0xFF);
        }
    }

    byte[] makeIv(Rand rand);

    byte makeIntegrityByte(Rand rand);

    /**
     * Shuffles bytes.
     *
     * @param input input data
     * @param msSinceStart time since start
     * @return shuffled bytes
     */
    CipherText encrypt(byte[] input, long msSinceStart);
}
