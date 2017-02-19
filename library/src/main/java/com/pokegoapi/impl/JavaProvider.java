package com.pokegoapi.impl;

import com.pokegoapi.Provider;

/**
 * Created by chris on 2/3/2017.
 */
public class JavaProvider extends Provider {

    /**
     * Constructs a provider with the specified name, version number,
     * and information.
     *
     * @param name    the provider name.
     * @param version the provider version number.
     * @param info    a description of the provider and its services.
     */
    protected JavaProvider(String name, double version, String info) {
        super(name, version, info);
    }
}
