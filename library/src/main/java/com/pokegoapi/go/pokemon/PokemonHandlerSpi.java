package com.pokegoapi.go.pokemon;

import com.github.aeonlucid.pogoprotos.inventory.Item.ItemId;
import com.github.aeonlucid.pogoprotos.networking.Responses.*;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.pokemon.spec.Pokemon;
import com.pokegoapi.network.exception.RequestFailedException;


@SuppressWarnings("WeakerAccess")
public abstract class PokemonHandlerSpi {

    public abstract void engineInitialize(PokemonGoClient client);

    /**
     * Transfers the pokemon.
     *
     * @return the result
     * @throws RequestFailedException if the request failed
     */
    public abstract ReleasePokemonResponse.Result engineTransfer(Pokemon pokemon) throws RequestFailedException;

    /**
     * Rename pokemon nickname pokemon response . result.
     *
     * @param nickname the nickname
     * @return the nickname pokemon response . result
     * @throws RequestFailedException if the request failed
     */
    public abstract NicknamePokemonResponse.Result engineRename(Pokemon pokemon, String nickname) throws RequestFailedException;

    /**
     * Function to mark the pokemon as favorite or not.
     *
     * @param markFavorite Mark Pokemon as Favorite?
     * @return the SetFavoritePokemonResponse.Result
     * @throws RequestFailedException If the Request failed
     */
    public abstract SetFavoritePokemonResponse.Result engineSetAsFavorite(Pokemon pokemon, boolean markFavorite)
            throws RequestFailedException;

    /**
     * Powers up a pokemon with candy and stardust.
     * After powering up this pokemon object will reflect the new changes.
     *
     * @return The result
     * @throws RequestFailedException If the Request failed
     */
    public abstract UpgradePokemonResponse.Result enginePowerUp(Pokemon pokemon) throws RequestFailedException;

    /**
     * dus
     * Evolve evolution result.
     *
     * @return the evolution result
     * @throws RequestFailedException If the Request Failed
     */
    public abstract EvolvePokemonResponse engineEvolve(Pokemon pokemon) throws RequestFailedException;

    /**
     * Heal a pokemon, using various fallbacks for potions
     *
     * @return Result, ERROR_CANNOT_USE if the requirements arent met
     * @throws RequestFailedException If the Request Failed
     */
    public abstract UseItemPotionResponse.Result engineHeal(Pokemon pokemon) throws RequestFailedException;

    /**
     * use a potion on that pokemon. Will check if there is enough potions and if the pokemon need
     * to be healed.
     *
     * @param itemId {@link ItemId} of the potion to use.
     * @return Result, ERROR_CANNOT_USE if the requirements aren't met
     * @throws RequestFailedException If the Request Failed
     */
    public abstract UseItemPotionResponse.Result engineUsePotion(Pokemon pokemon, ItemId itemId) throws RequestFailedException;

    /**
     * Revive a pokemon, using various fallbacks for revive items
     *
     * @return Result, ERROR_CANNOT_USE if the requirements aren't met
     * @throws RequestFailedException If the request failed
     */
    public abstract UseItemReviveResponse.Result engineRevive(Pokemon pokemon) throws RequestFailedException;

    /**
     * Use a revive item on the pokemon. Will check if there is enough revive &amp; if the pokemon need
     * to be revived.
     *
     * @param itemId {@link ItemId} of the Revive to use.
     * @return Result ERROR_CANNOT_USE if the requirements aren't met
     * @throws RequestFailedException If the request failed.
     */
    public abstract UseItemReviveResponse.Result engineRevive(Pokemon pokemon, ItemId itemId) throws RequestFailedException;

}
