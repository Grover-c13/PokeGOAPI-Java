package com.pokegoapi.go.auth;

import com.pokegoapi.go.spec.Credentials;
import com.pokegoapi.go.spec.LoginParameterSpec;
import com.pokegoapi.network.exception.LoginFailedException;

public abstract class CredentialProviderSpi {

    public abstract Credentials engineCreateCredential(LoginParameterSpec spec, CredentialProvider provider);
    public abstract Credentials engineRefreshCredential(Credentials credential, CredentialProvider provider)
            throws LoginFailedException;
}
