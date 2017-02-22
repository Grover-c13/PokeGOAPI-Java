package com.pokegoapi.go.auth;

import com.pokegoapi.provider.GetInstance;
import com.pokegoapi.provider.NoSuchTypeException;
import com.pokegoapi.provider.Provider;
import com.pokegoapi.go.spec.Credentials;
import com.pokegoapi.go.spec.LoginParameterSpec;
import com.pokegoapi.network.exception.LoginFailedException;

/**
 * Created by chris on 1/23/2017.
 */
public final class CredentialProvider {

    private Provider provider;
    private CredentialProviderSpi spi;

    private CredentialProvider(CredentialProviderSpi spi, Provider provider) {
        this.provider = provider;
        this.spi = spi;
    }

    public static CredentialProvider getInstance(){
        //TODO: Implement getting the default provider and get the instance from it
        return null;
    }

    public static CredentialProvider getInstance(Provider provider) throws NoSuchTypeException {
        GetInstance.Instance instance = GetInstance.getInstance("CredentialProvider",
                CredentialProviderSpi.class, provider);
        return new CredentialProvider((CredentialProviderSpi) instance.impl, instance.provider);
    }

    /**
     * Returns the provider of this Credential Provider object.
     *
     * @return the provider of this Credential Provider object
     */
    public final Provider getProvider() {
        return provider;
    }

    public Credentials createCredentials(LoginParameterSpec spec){
        return spi.engineCreateCredential(spec, this);
    }

    public Credentials refreshCredential(Credentials credential) throws LoginFailedException {
        return spi.engineRefreshCredential(credential, this);
    }
}
