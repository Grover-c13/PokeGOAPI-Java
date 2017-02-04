package com.pokegoapi.go.pokestop.spec;

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Inventory.Item.ItemAwardOuterClass.ItemAward;
import POGOProtos.Networking.Responses.FortSearchResponseOuterClass.FortSearchResponse.Result;
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
    Result getResult();

    /**
     * @return the items gained from looting the pokestop
     */
    List<ItemAward> getItemsAwarded();

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
    PokemonData getEgg();
}
