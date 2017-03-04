package com.pokegoapi.network.spec;

import java.util.List;

/**
 * Created by chris on 2/28/2017.
 */
public interface Hash {

    int getLocationHash();

    int getLocationAuthHash();

    List<Long> getRequestHashes();
}
