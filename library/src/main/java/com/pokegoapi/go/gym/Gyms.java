package com.pokegoapi.go.gym;

import com.github.aeonlucid.pogoprotos.networking.Responses.FortDeployPokemonResponse;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.gym.spec.Battle;
import com.pokegoapi.go.gym.spec.BattleListener;
import com.pokegoapi.go.gym.spec.Gym;
import com.pokegoapi.go.pokemon.spec.Pokemon;
import com.pokegoapi.network.exception.RequestFailedException;
import com.pokegoapi.provider.GetInstance;
import com.pokegoapi.provider.NoSuchTypeException;
import com.pokegoapi.provider.Provider;

import java.util.Objects;

@SuppressWarnings("unused")
public final class Gyms {

    private final GymsSpi spi;
    private final Provider provider;

    private Gyms(GymsSpi spi, Provider provider){
        this.spi = spi;
        this.provider = provider;
    }

    public static Gyms getInstance(){
        //TODO: Implement getting the default provider and get the instance from it
        return null;
    }

    public static Gyms getInstance(Provider provider) throws NoSuchTypeException {
        GetInstance.Instance instance = GetInstance.getInstance("Gyms",
                GymsSpi.class, provider);
        return new Gyms((GymsSpi) instance.impl, instance.provider);
    }

    public void initialize(PokemonGoClient client) {
        spi.engineInitialize(client);
    }

    /**
     * Gets this gym's details
     * @return the updated Gym
     */
    public Gym getGymDetails(Gym gym) throws RequestFailedException {
        gymNullCheck(gym);
        return spi.engineGetGymDetails(gym);
    }

    /**
     * Deploys the pokemon to the gym if there is enough room
     * @param gym the gym for the pokemon to be deployed to
     * @param pokemon the pokemon to be deployed
     * @return the result of deploying to the gym
     */
    public FortDeployPokemonResponse.Result deployPokemon(Gym gym, Pokemon pokemon) throws RequestFailedException {
        gymNullCheck(gym);
        Objects.requireNonNull(pokemon, "Pokemon is null");
        return spi.engineDeployPokemon(gym, pokemon);
    }

    /**
     * Starts a battle at the gym
     * @param gym the gym to battle
     * @param attackingTeam the array of attacking pokemon with a max of 6 pokemon
     * @param listener the battle listener for the battle
     * @return the battle to be fought
     */
    public Battle battle(Gym gym, Pokemon[] attackingTeam, BattleListener listener) throws RequestFailedException {
        gymNullCheck(gym);
        Objects.requireNonNull(attackingTeam,"Attacking Team is null");
        Objects.requireNonNull(listener, "Battle Listener is null");

        if(attackingTeam.length == 0 || attackingTeam.length < 6){
            throw new IllegalArgumentException("Attacking Team must be between 1 and 6 Pokemon");
        }

        return spi.engineBattleGym(gym, attackingTeam, listener);
    }

    private void gymNullCheck(Gym gym){
        Objects.requireNonNull(gym, "Gym is null");
    }

    public Provider getProvider() {
        return provider;
    }
}
