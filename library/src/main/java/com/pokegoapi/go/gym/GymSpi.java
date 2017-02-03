package com.pokegoapi.go.gym;

import POGOProtos.Data.Gym.GymMembershipOuterClass.GymMembership;
import POGOProtos.Data.Gym.GymStateOuterClass.GymState;
import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Enums.TeamColorOuterClass.TeamColor;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Networking.Responses.FortDeployPokemonResponseOuterClass.FortDeployPokemonResponse.Result;
import POGOProtos.Networking.Responses.GetGymDetailsResponseOuterClass.GetGymDetailsResponse;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.gym.spec.Battle;
import com.pokegoapi.go.map.spec.MapPoint;
import com.pokegoapi.old.api.pokemon.Pokemon;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class GymSpi implements MapPoint {

    /**
     * Initializes the Gyms Provider with a PokemonGoClient. Any calls to the server should go through
     * this PokemonGoClient
     * @param client the client for handling the server calls
     */
    protected abstract void engineInitialize(PokemonGoClient client, FortData fortData);

    /**
     * Initializes the Gyms Provider with a PokemonGoClient. Any calls to the server should go through
     * this PokemonGoClient
     * @param client the client for handling the server calls
     */
    protected abstract void engineInitialize(PokemonGoClient client, GetGymDetailsResponse gymDetails);

    /**
     * Initializes the Gyms Provider with a PokemonGoClient. Any calls to the server should go through
     * this PokemonGoClient
     * @param client the client for handling the server calls
     */
    protected abstract void engineInitialize(PokemonGoClient client, GymState gymState);

    protected abstract boolean engineIsEnabled();

    protected abstract TeamColor engineGetOwnedByTeam();

    protected abstract PokemonId engineGetGuardPokemonId();

    protected abstract int engineGetGuardPokemonCp();

    protected abstract long engineGetPoints();

    protected abstract boolean engineIsInBattle();

    protected abstract boolean engineIsAttackable();

    protected abstract String engineGetName();

    protected abstract ProtocolStringList engineGetUrlsList();

    protected abstract boolean engineInRange();

    protected abstract String engineGetDescription();

    protected abstract List<GymMembership> engineGetGymMembers();

    /**
     * Get a list of pokemon defending this gym.
     *
     * @return List of pokemon
     */
    protected abstract List<PokemonData> engineGetDefendingPokemon();

    protected abstract GymState engineGetGymState();

    /**
     * Gets this gym's details. The gym's details will be set in this method
     * @return the GymDetailsResponse from the request to the server
     */
    protected abstract GetGymDetailsResponse engineGetGymDetails();

    /**
     * Deploys the pokemon to the gym if there is enough room
     * @param pokemon the pokemon to be deployed
     * @return the result of deploying to the gym
     */
    protected abstract Result engineDeployPokemon(Pokemon pokemon);

    /**
     * Starts a battle at the gym
     * @return the battle to be fought
     */
    protected abstract Battle engineBattleGym();
}
