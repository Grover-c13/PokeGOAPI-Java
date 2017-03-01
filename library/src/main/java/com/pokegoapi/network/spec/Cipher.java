package com.pokegoapi.network.spec;

/**
 * Created by chris on 2/28/2017.
 */
public interface Cipher {

    class Rand {
        public long state;
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
