package com.pokegoapi.go.pokestop;

import com.github.aeonlucid.pogoprotos.inventory.Item.ItemId;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.pokestop.spec.LootResult;
import com.pokegoapi.go.pokestop.spec.Pokestop;
import com.pokegoapi.network.exception.RequestFailedException;
import com.pokegoapi.provider.GetInstance;
import com.pokegoapi.provider.NoSuchTypeException;
import com.pokegoapi.provider.Provider;

/**
 * An object that handles interactions of Pokestops on the map
 */
@SuppressWarnings("unused")
public final class Pokestops{

    private final PokestopsSpi spi;
    private final Provider provider;

    private Pokestops(PokestopsSpi spi, Provider provider){
        this.spi = spi;
        this.provider = provider;
    }

    public static Pokestops getInstance(){
        //TODO: Implement getting the default provider and get the instance from it
        return null;
    }

    public static Pokestops getInstance(Provider provider) throws NoSuchTypeException {
        GetInstance.Instance instance = GetInstance.getInstance("Pokestops",
                PokestopsSpi.class, provider);
        return new Pokestops((PokestopsSpi) instance.impl, instance.provider);
    }

    public void initialize(PokemonGoClient client) {
        spi.engineInitialize(client);
    }

    /**
     * Loots the pokestop for pokeballs and other items.
     *
     * @return the Pokestop's Loot Result
     * @throws RequestFailedException if an exception occurs while sending the request
     */
    public LootResult loot(Pokestop pokestop) throws RequestFailedException {
        return spi.engineLoot(pokestop);
    }

    /**
     * Adds a modifier to the pokestop. (i.e. add a lure module)
     *
     * @param item the modifier to add to this pokestop
     * @throws RequestFailedException if an exception occurs while sending the request
     */
    public boolean addModifier(Pokestop pokestop, ItemId item) throws RequestFailedException {
        return spi.engineAddModifier(pokestop, item);
    }

    /**
     * Get more detailed information about the pokestop.
     *
     * @return FortDetails
     * @throws RequestFailedException if an exception occurs while sending the request
     */
    public boolean getDetails(Pokestop pokestop) throws RequestFailedException {
        return spi.engineGetDetails(pokestop);
    }

    public Provider getProvider() {
        return provider;
    }
}
