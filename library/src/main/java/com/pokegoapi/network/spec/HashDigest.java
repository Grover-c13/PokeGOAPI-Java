package com.pokegoapi.network.spec;

import com.github.aeonlucid.pogoprotos.networking.Envelopes.AuthTicket;
import com.github.aeonlucid.pogoprotos.networking.Envelopes.RequestEnvelope.AuthInfo;
import com.pokegoapi.go.spec.Location;

/**
 * Created by chris on 1/23/2017.
 */
public interface HashDigest {

    void update(Location location);

    void update(AuthInfo authInfo);

    void update(AuthTicket authTicket);

    /**
     * Provides a hash for the given input
     *
     * @param timestamp timestamp to hash
     * @param requests request data to hash
     * @return the hash for the given input
     */
    Hash digest(long timestamp, byte[][] requests);

    /**
     * @return the version this hash supports, for example 4500 = 0.45.0 and 5300 = 0.53.0
     */
    int getHashVersion();

    /**
     * @return the cipher this hash should use
     */
    Cipher getCrypto();

    /**
     * @return the unknown 25 value used with this hash
     */
    long getUNK25();
}
