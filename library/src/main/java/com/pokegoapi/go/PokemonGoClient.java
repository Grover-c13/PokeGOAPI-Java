package com.pokegoapi.go;

import com.github.aeonlucid.pogoprotos.networking.Requests.RequestType;
import com.google.protobuf.Message;
import com.pokegoapi.go.spec.Credentials;
import com.pokegoapi.network.spec.ServerRequest;
import com.pokegoapi.provider.*;
import com.pokegoapi.provider.GetInstance.Instance;

/**
 * Created by chris on 1/22/2017.
 */
public final class PokemonGoClient extends ProviderInterface {

    private Provider provider;
    private PokemonGoClientSpi spi;

    private boolean called;

    private PokemonGoClient(PokemonGoClientSpi spi, Provider provider){
        this.spi = spi;
        this.provider = provider;
    }

    public static PokemonGoClient getInstance(){
        //TODO: Implement getting the default provider and get the instance from it
        return null;
    }

    public static PokemonGoClient getInstance(Provider provider) throws NoSuchTypeException {
        Instance instance = GetInstance.getInstance("PokemonGoClient",
                PokemonGoClientSpi.class, provider);
        return new PokemonGoClient((PokemonGoClientSpi) instance.impl, instance.provider);
    }

    /**
     * Returns the provider of this Pokemon Go Client object.
     *
     * @return the provider of this Pokemon Go Client object
     */
    public final Provider getProvider() {
        return provider;
    }

    public void login(Credentials credentials){
        ProviderInterfaces.requireInitialized(this);
        spi.engineLogin(credentials);
    }

    public ServerRequest createRequest(Message request, RequestType type) {
        ProviderInterfaces.requireInitialized(this);
        return spi.engineCreateRequest(request, type);
    }
}
