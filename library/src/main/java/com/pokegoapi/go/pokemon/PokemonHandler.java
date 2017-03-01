package com.pokegoapi.go.pokemon;

import com.github.aeonlucid.pogoprotos.inventory.Item.ItemId;
import com.github.aeonlucid.pogoprotos.networking.Responses;
import com.github.aeonlucid.pogoprotos.networking.Responses.NicknamePokemonResponse;
import com.github.aeonlucid.pogoprotos.networking.Responses.SetFavoritePokemonResponse;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.pokemon.spec.Pokemon;
import com.pokegoapi.network.exception.RequestFailedException;
import com.pokegoapi.provider.*;

@SuppressWarnings("unused")
public final class PokemonHandler extends ProviderInterface {

    private final PokemonHandlerSpi spi;
    private final Provider provider;

    private PokemonHandler(PokemonHandlerSpi spi, Provider provider){
        this.spi = spi;
        this.provider = provider;
    }

    public static PokemonHandler getInstance(){
        //TODO: Implement getting the default provider and get the instance from it
        return null;
    }

    public static PokemonHandler getInstance(Provider provider) throws NoSuchTypeException {
        GetInstance.Instance instance = GetInstance.getInstance("Pokemon",
                PokemonHandlerSpi.class, provider);
        return new PokemonHandler((PokemonHandlerSpi) instance.impl, instance.provider);
    }

    public void initialize(PokemonGoClient client) {
        spi.engineInitialize(client);
        called = true;
    }

    /**
     * Transfers the pokemon.
     *
     * @return the result
     * @throws RequestFailedException if the request failed
     */
    public Responses.ReleasePokemonResponse.Result transfer(Pokemon pokemon) throws RequestFailedException {
        ProviderInterfaces.requireInitializedAndNonNull(this, pokemon, Pokemon.class);
        return spi.engineTransfer(pokemon);
    }

    /**
     * Rename pokemon nickname pokemon response . result.
     *
     * @param nickname the nickname
     * @return the nickname pokemon response . result
     * @throws RequestFailedException if the request failed
     */
    public NicknamePokemonResponse.Result rename(Pokemon pokemon, String nickname) throws RequestFailedException {
        ProviderInterfaces.requireInitializedAndNonNull(this, pokemon, Pokemon.class);
        return spi.engineRename(pokemon, nickname);
    }

    /**
     * Function to mark the pokemon as favorite or not.
     *
     * @param markFavorite Mark Pokemon as Favorite?
     * @return the SetFavoritePokemonResponse.Result
     * @throws RequestFailedException If the Request failed
     */
    public SetFavoritePokemonResponse.Result setAsFavorite(Pokemon pokemon, boolean markFavorite)
            throws RequestFailedException {
        ProviderInterfaces.requireInitializedAndNonNull(this, pokemon, Pokemon.class);
        return spi.engineSetAsFavorite(pokemon, markFavorite);
    }

    /**
     * Powers up a pokemon with candy and stardust.
     * After powering up this pokemon object will reflect the new changes.
     *
     * @return The result
     * @throws RequestFailedException If the Request failed
     */
    public Responses.UpgradePokemonResponse.Result powerUp(Pokemon pokemon) throws RequestFailedException {
        ProviderInterfaces.requireInitializedAndNonNull(this, pokemon, Pokemon.class);
        return spi.enginePowerUp(pokemon);
    }

    /**
     * dus
     * Evolve evolution result.
     *
     * @return the evolution result
     * @throws RequestFailedException If the Request Failed
     */
    public Responses.EvolvePokemonResponse evolve(Pokemon pokemon) throws RequestFailedException {
        ProviderInterfaces.requireInitializedAndNonNull(this, pokemon, Pokemon.class);
        return spi.engineEvolve(pokemon);
    }

    /**
     * Heal a pokemon, using various fallbacks for potions
     *
     * @return Result, ERROR_CANNOT_USE if the requirements arent met
     * @throws RequestFailedException If the Request Failed
     */
    public Responses.UseItemPotionResponse.Result heal(Pokemon pokemon) throws RequestFailedException {
        ProviderInterfaces.requireInitializedAndNonNull(this, pokemon, Pokemon.class);
        return spi.engineHeal(pokemon);
    }

    /**
     * use a potion on that pokemon. Will check if there is enough potions and if the pokemon need
     * to be healed.
     *
     * @param itemId {@link ItemId} of the potion to use.
     * @return Result, ERROR_CANNOT_USE if the requirements aren't met
     * @throws RequestFailedException If the Request Failed
     */
    public Responses.UseItemPotionResponse.Result usePotion(Pokemon pokemon, ItemId itemId)
            throws RequestFailedException {
        ProviderInterfaces.requireInitializedAndNonNull(this, pokemon, Pokemon.class);
        return spi.engineUsePotion(pokemon, itemId);
    }

    /**
     * Revive a pokemon, using various fallbacks for revive items
     *
     * @return Result, ERROR_CANNOT_USE if the requirements aren't met
     * @throws RequestFailedException If the request failed
     */
    public Responses.UseItemReviveResponse.Result revive(Pokemon pokemon) throws RequestFailedException {
        ProviderInterfaces.requireInitializedAndNonNull(this, pokemon, Pokemon.class);
        return spi.engineRevive(pokemon);
    }

    /**
     * Use a revive item on the pokemon. Will check if there is enough revive &amp; if the pokemon need
     * to be revived.
     *
     * @param itemId {@link ItemId} of the Revive to use.
     * @return Result ERROR_CANNOT_USE if the requirements aren't met
     * @throws RequestFailedException If the request failed.
     */
    public Responses.UseItemReviveResponse.Result revive(Pokemon pokemon, ItemId itemId)
            throws RequestFailedException {
        ProviderInterfaces.requireInitializedAndNonNull(this, pokemon, Pokemon.class);
        return spi.engineRevive(pokemon, itemId);
    }

    public Provider getProvider() {
        return provider;
    }
}
