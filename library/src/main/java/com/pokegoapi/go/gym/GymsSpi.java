package com.pokegoapi.go.gym;

import com.github.aeonlucid.pogoprotos.networking.Responses.FortDeployPokemonResponse;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.gym.spec.Battle;
import com.pokegoapi.go.gym.spec.BattleListener;
import com.pokegoapi.go.gym.spec.Gym;
import com.pokegoapi.go.pokemon.spec.Pokemon;
import com.pokegoapi.network.exception.RequestFailedException;

@SuppressWarnings("WeakerAccess")
public abstract class GymsSpi {

    /**
     * Initializes the Gyms Provider with a PokemonGoClient. Any calls to the server should go through
     * this PokemonGoClient
     * @param client the client for handling the server calls
     */
    protected abstract void engineInitialize(PokemonGoClient client);

    /**
     * Gets this gym's details. The gym's details will be set in this method
     * @return the updated Gym
     */
    protected abstract Gym engineGetGymDetails(Gym gym) throws RequestFailedException;

    /**
     * Deploys the pokemon to the gym if there is enough room
     * @param gym the gym for the pokemon to be deployed to
     * @param pokemon the pokemon to be deployed
     * @return the result of deploying to the gym
     */
    protected abstract FortDeployPokemonResponse.Result engineDeployPokemon(Gym gym, Pokemon pokemon)
            throws RequestFailedException;

    /**
     * Starts a battle at the gym
     * @param gym the gym to battle
     * @param attackingTeam the array of attacking pokemon, max 6
     * @param listener the battle listener for the battle
     * @return the battle to be fought
     */
    protected abstract Battle engineBattleGym(Gym gym, Pokemon[] attackingTeam, BattleListener listener)
            throws RequestFailedException;
}
