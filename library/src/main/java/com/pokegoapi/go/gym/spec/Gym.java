package com.pokegoapi.go.gym.spec;

import com.github.aeonlucid.pogoprotos.Data;
import com.github.aeonlucid.pogoprotos.Enums.PokemonId;
import com.github.aeonlucid.pogoprotos.Enums.TeamColor;
import com.github.aeonlucid.pogoprotos.data.Gym.GymMembership;
import com.github.aeonlucid.pogoprotos.map.Fort;
import com.github.aeonlucid.pogoprotos.map.Fort.FortData;
import com.github.aeonlucid.pogoprotos.networking.Responses.GetGymDetailsResponse;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.go.map.spec.MapPoint;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class Gym implements MapPoint {

    public Gym(FortData fort){
        if(fort.getType() != Fort.FortType.GYM){
            throw new IllegalArgumentException("Fort " + fort.getId() + " is not a Pokestop. " +
                    "Unable to initialize Pokestop object");
        }
    }

    public abstract boolean isEnabled();

    public abstract TeamColor getTeamOwner();

    public abstract PokemonId getGuardPokemonId();

    public abstract int getGuardPokemonCp();

    public abstract long getPoints();

    public abstract boolean isInBattle();

    public abstract boolean canBeAttacked();

    public abstract String getName();

    public abstract ProtocolStringList getUrlsList();

    public abstract boolean isInRange();

    public abstract String getDescription();

    public abstract List<GymMembership> getGymMembers();

    /**
     * Get a list of pokemon defending this gym.
     *
     * @return List of pokemon
     */
    public abstract List<Data.PokemonData> getDefendingPokemon();

    public abstract boolean hasGymDetails();

    public abstract void updateGymDetails(GetGymDetailsResponse response);

    @Override
    public int hashCode() {
        return Objects.hashCode(getMapId());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Gym && Objects.equals(((Gym) obj).getMapId(), getMapId());
    }
}
