package com.pokegoapi.go.pokestop.spec;

import com.github.aeonlucid.pogoprotos.Data;
import com.github.aeonlucid.pogoprotos.inventory.Item;
import com.github.aeonlucid.pogoprotos.networking.Responses;
import com.pokegoapi.go.pokestop.Pokestop;

import java.util.List;

/**
 *  The Loot Results of trying to looting a {@link Pokestop}
 */
public interface LootResult {

    /**
     * @return true if the pokestop was successfully looted
     */
    boolean wasSuccessful();

    /**
     * @return the pokestop that was looted
     */
    Pokestop getPokestop();

    /**
     * @return the result of looting the pokestop
     */
    Responses.FortSearchResponse.Result getResult();

    /**
     * @return the items gained from looting the pokestop
     */
    List<Item.ItemAward> getItemsAwarded();

    /**
     * @return the experience gained from looting the pokestop
     */
    int getExperience();

    /**
     * @return true if an egg was looted from the pokestop
     */
    boolean hasEgg();

    /**
     * @return the egg that was looted from the pokestop
     */
    Data.PokemonData getEgg();
}
