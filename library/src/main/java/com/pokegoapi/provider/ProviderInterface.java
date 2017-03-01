package com.pokegoapi.provider;

/**
 * Created by chris on 2/27/2017.
 */
public abstract class ProviderInterface {

    protected boolean called = false;

    protected boolean hasBeenInitialized() {
        return called;
    }
}
