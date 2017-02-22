package com.pokegoapi.go.pokestop;

import com.github.aeonlucid.pogoprotos.inventory.Item;
import com.github.aeonlucid.pogoprotos.map.Fort;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.map.spec.MapPoint;
import com.pokegoapi.go.pokestop.spec.LootResult;
import com.pokegoapi.network.CaptchaActiveException;
import com.pokegoapi.network.LoginFailedException;
import com.pokegoapi.network.RemoteServerException;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class PokestopSpi implements MapPoint {

    protected abstract void engineInitialize(PokemonGoClient client, Fort.FortData fort);

    /**
     * Returns the distance to a pokestop.
     *
     * @return the calculated distance
     */
    protected abstract double engineGetDistance();

    /**
     * Returns whether or not a pokestop is in range.
     *
     * @return true when in range of player
     */
    protected abstract boolean engineIsInRange();

    /**
     * Returns whether or not the lured pokemon is in range.
     *
     * @return true when the lured pokemon is in range of player
     */
    protected abstract boolean engineIsInRangeForLuredPokemon();

    /**
     * can user loot this from current position.
     *
     * @return true when lootable
     */
    protected abstract boolean engineCanLoot();

    protected abstract String engineGetName();

    protected abstract ProtocolStringList engineGetImageUrl();

    protected abstract int engineGetFp();

    protected abstract String engineGetDescription();

    protected abstract List<Fort.FortModifier> engineGetModifiers();

    public abstract boolean engineHasDetails();

    /**
     * Returns whether this pokestop has an active lure when detected on map.
     *
     * @return lure status
     */
    protected abstract boolean engineHasLure();

    /**
     * Loots a pokestop for pokeballs and other items.
     *
     * @return PokestopLootResult
     * @throws LoginFailedException if login failed
     * @throws RemoteServerException if the server failed to respond
     * @throws CaptchaActiveException if a captcha is active and the message can't be sent
     */
    protected abstract LootResult engineLoot(Pokestop pokestop) throws LoginFailedException, CaptchaActiveException, RemoteServerException;

    /**
     * Adds a modifier to this pokestop. (i.e. add a lure module)
     *
     * @param item the modifier to add to this pokestop
     * @throws LoginFailedException if login failed
     * @throws RemoteServerException if the server failed to respond or the modifier could not be added to this pokestop
     * @throws CaptchaActiveException if a captcha is active and the message can't be sent
     */
    protected abstract boolean engineAddModifier(Item.ItemId item) throws LoginFailedException, CaptchaActiveException,
            RemoteServerException;

    /**
     * Get more detailed information about a pokestop.
     *
     * @return FortDetails
     * @throws LoginFailedException if login failed
     * @throws RemoteServerException if the server failed to respond
     * @throws CaptchaActiveException if a captcha is active and the message can't be sent
     */
    protected abstract boolean engineGetDetails() throws LoginFailedException, CaptchaActiveException, RemoteServerException;

}
