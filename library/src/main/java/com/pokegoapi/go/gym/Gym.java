package com.pokegoapi.go.gym;

import com.github.aeonlucid.pogoprotos.Data;
import com.github.aeonlucid.pogoprotos.Enums;
import com.github.aeonlucid.pogoprotos.data.Gym.GymMembership;
import com.github.aeonlucid.pogoprotos.data.Gym.GymState;
import com.github.aeonlucid.pogoprotos.map.Fort;
import com.github.aeonlucid.pogoprotos.networking.Responses;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.provider.GetInstance;
import com.pokegoapi.provider.NoSuchTypeException;
import com.pokegoapi.provider.Provider;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.gym.spec.Battle;
import com.pokegoapi.go.map.spec.MapPoint;
import com.pokegoapi.network.exception.RequestFailedException;
import com.pokegoapi.old.api.pokemon.Pokemon;

import java.util.List;

@SuppressWarnings("unused")
public final class Gym implements MapPoint {

    private final GymSpi spi;
    private final Provider provider;

    private Gym(GymSpi spi, Provider provider){
        this.spi = spi;
        this.provider = provider;
    }

    public static Gym getInstance(){
        //TODO: Implement getting the default provider and get the instance from it
        return null;
    }

    public static Gym getInstance(Provider provider) throws NoSuchTypeException {
        GetInstance.Instance instance = GetInstance.getInstance("Gyms",
                GymSpi.class, provider);
        return new Gym((GymSpi) instance.impl, instance.provider);
    }

    public void initialize(PokemonGoClient client, Fort.FortData fort) {
        if(fort.getType() != Fort.FortType.GYM) {
            throw new IllegalArgumentException("Fort " + fort.getId() + " is not a Gym. " +
                    "Unable to initialize Gym object");
        }
        spi.engineInitialize(client, fort);
    }

    public void initialize(PokemonGoClient client, Responses.GetGymDetailsResponse details) {
        spi.engineInitialize(client, details);
    }

    public void initialize(PokemonGoClient client, GymState state) {
        spi.engineInitialize(client, state);
    }

    @Override
    public String getMapId() {
        return spi.getMapId();
    }

    @Override
    public double getLatitude() {
        return spi.getLatitude();
    }

    @Override
    public double getLongitude() {
        return spi.getLongitude();
    }

    public boolean isEnabled() {
        return spi.engineIsEnabled();
    }

    public Enums.TeamColor getTeamOwner() {
        return spi.engineGetOwnedByTeam();
    }

    public Enums.PokemonId getGuardPokemonId() {
        return spi.engineGetGuardPokemonId();
    }

    public int getGuardPokemonCp() {
        return spi.engineGetGuardPokemonCp();
    }

    public long getPoints() {
        return spi.engineGetPoints();
    }

    public boolean isInBattle() {
        return spi.engineIsInBattle();
    }

    public boolean isAttackable() {
        return spi.engineIsAttackable();
    }

    public String getName() {
        return spi.engineGetName();
    }

    public ProtocolStringList getUrlsList() {
        return spi.engineGetUrlsList();
    }

    public boolean isInRange() {
        return spi.engineInRange();
    }

    public String getDescription() {
        return spi.engineGetDescription();
    }

    public List<GymMembership> getGymMembers() {
        return spi.engineGetGymMembers();
    }

    /**
     * Get a list of pokemon defending this gym.
     *
     * @return List of pokemon
     */
    public List<Data.PokemonData> getDefendingPokemon() {
        return spi.engineGetDefendingPokemon();
    }

    public GymState getGymState() {
        return spi.engineGetGymState();
    }

    public boolean hasGymDetails() {
        return spi.engineHasGymDetails();
    }

    public boolean hasGymState() {
        return spi.engineHasGymState();
    }

//    public GetGymDetailsResponse getGymDetails() throws RequestFailedException {
//        return spi.engineGetGymDetails();
//    }
//
//    public Result deployPokemon(Pokemon pokemon) throws RequestFailedException {
//        return spi.engineDeployPokemon(pokemon);
//    }

    public Battle battle() {
        return spi.engineBattleGym(this);
    }

    public Provider getProvider() {
        return provider;
    }
}
