package com.pokegoapi.go;

import com.pokegoapi.go.spec.Credentials;

/**
 * Created by chris on 1/23/2017.
 */
public abstract class PokemonGoClientSpi {

    public abstract void engineLogin(Credentials credentials);
}
