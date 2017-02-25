package com.pokegoapi.go.pokestop.spec;

import com.github.aeonlucid.pogoprotos.map.Fort;
import com.github.aeonlucid.pogoprotos.networking.Responses.FortDetailsResponse;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.map.spec.MapPoint;

import java.util.List;

/**
 * An object that represents a Pokestop on the map in Pokemon Go
 */
public abstract class Pokestop implements MapPoint{

    public Pokestop(PokemonGoClient client, Fort.FortData fort) {
        if(fort.getType() != Fort.FortType.CHECKPOINT) {
            throw new IllegalArgumentException("Fort " + fort.getId() + " is not a Pokestop. " +
                    "Unable to create Pokestop object");
        }
    }

    @Override
    public abstract String getMapId();

    @Override
    public abstract double getLatitude();

    @Override
    public abstract double getLongitude();

    /**
     * Returns the distance from the user to the pokestop.
     *
     * @return the calculated distance
     */
    public abstract double getDistanceTo();

    /**
     * Returns whether or not a pokestop is in range.
     *
     * @return true when in range of player
     */
    public abstract boolean isInRange();

    /**
     * Returns whether or not the lured pokemon is in range.
     *
     * @return true when the lured pokemon is in range of player
     */
    public abstract boolean isInRangeForLuredPokemon();

    /**
     * can user loot this from current position.
     *
     * @return true when lootable
     */
    public abstract boolean canLoot();

    /**
     * @return the name of the landmark that is the Pokestop
     */
    public abstract String getName();

    /**
     * @return the url to the pokestop images to display
     */
    public abstract ProtocolStringList getImageUrl();

    public abstract int getFp();

    /**
     * @return the description of the landmark that is the Pokestop
     */
    public abstract String getDescription();

    /**
     * @return a list of all of the modifiers attached to the Pokestop
     */
    public abstract List<Fort.FortModifier> getModifiers();

    /**
     * Returns whether the pokestop has an active lure when detected on map.
     *
     * @return lure status
     */
    public abstract boolean hasLure();

    /**
     * Check to see if the Pokestop has the detailed information about itself
     * @return true if th
     */
    public abstract boolean hasDetails();

    /**
     * Updates the Pokestop with the Fort Details
     * @param detailsResponse the details of the Pokestop
     */
    public abstract void updateDetails(FortDetailsResponse detailsResponse);

}
