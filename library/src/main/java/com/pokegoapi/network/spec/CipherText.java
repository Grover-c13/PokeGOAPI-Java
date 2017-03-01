package com.pokegoapi.network.spec;

import java.nio.ByteBuffer;

/**
 * Created by chris on 2/28/2017.
 */
public interface CipherText {

    byte[] intBytes(long value);

    /**
     * Convert this Ciptext to a ByteBuffer
     *
     * @return contents as bytebuffer
     */
    public ByteBuffer toByteBuffer();
}
