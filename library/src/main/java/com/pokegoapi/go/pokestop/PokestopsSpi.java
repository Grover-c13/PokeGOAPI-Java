package com.pokegoapi.go.pokestop;

import com.github.aeonlucid.pogoprotos.inventory.Item.ItemId;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.pokestop.spec.LootResult;
import com.pokegoapi.go.pokestop.spec.Pokestop;
import com.pokegoapi.network.exception.RequestFailedException;

@SuppressWarnings("WeakerAccess")
public abstract class PokestopsSpi{

    protected abstract void engineInitialize(PokemonGoClient client);

    /**
     * Loots a pokestop for pokeballs and other items.
     *
     * @return PokestopLootResult
     * @throws RequestFailedException if an exception occurs while sending the request
     */
    protected abstract LootResult engineLoot(Pokestop pokestop) throws RequestFailedException;

    /**
     * Adds a modifier to this pokestop. (i.e. add a lure module)
     *
     * @param item the modifier to add to this pokestop
     * @throws RequestFailedException if a problem occurs while sending this request
     */
    protected abstract boolean engineAddModifier(Pokestop pokestop, ItemId item) throws RequestFailedException;

    /**
     * Get more detailed information about a pokestop.
     *
     * @return FortDetails
     * @throws RequestFailedException if an exception occurs while sending the request
     */
    protected abstract boolean engineGetDetails(Pokestop pokestop) throws RequestFailedException;

}
