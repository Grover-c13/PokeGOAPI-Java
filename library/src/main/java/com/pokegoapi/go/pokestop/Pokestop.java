package com.pokegoapi.go.pokestop;

import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Fort.FortModifierOuterClass.FortModifier;
import POGOProtos.Map.Fort.FortTypeOuterClass;
import com.google.protobuf.ProtocolStringList;
import com.pokegoapi.GetInstance;
import com.pokegoapi.NoSuchTypeException;
import com.pokegoapi.Provider;
import com.pokegoapi.go.PokemonGoClient;
import com.pokegoapi.go.map.spec.MapPoint;
import com.pokegoapi.go.pokestop.spec.LootResult;
import com.pokegoapi.network.exception.RequestFailedException;

import java.util.List;

/**
 * An object that represents a Pokestop on the map in Pokemon Go
 */
@SuppressWarnings("unused")
public final class Pokestop implements MapPoint{

    private final PokestopSpi spi;
    private final Provider provider;

    private Pokestop(PokestopSpi spi, Provider provider){
        this.spi = spi;
        this.provider = provider;
    }

    public static Pokestop getInstance(){
        //TODO: Implement getting the default provider and get the instance from it
        return null;
    }

    public static Pokestop getInstance(Provider provider) throws NoSuchTypeException {
        GetInstance.Instance instance = GetInstance.getInstance("Pokestops",
                PokestopSpi.class, provider);
        return new Pokestop((PokestopSpi) instance.impl, instance.provider);
    }

    public void initialize(PokemonGoClient client, FortData fort) {
        if(fort.getType() != FortTypeOuterClass.FortType.CHECKPOINT) {
            throw new IllegalArgumentException("Fort " + fort.getId() + " is not a Pokestop. " +
                    "Unable to initialize Pokestop object");
        }
        spi.engineInitialize(client, fort);
    }

    @Override
    public String getMapId() {
        return spi.getMapId();
    }

    @Override
    public double getLatitude() {
        return spi.getLongitude();
    }

    @Override
    public double getLongitude() {
        return spi.getLongitude();
    }

    /**
     * Returns the distance from the user to the pokestop.
     *
     * @return the calculated distance
     */
    public double getDistanceTo(){
        return spi.engineGetDistance();
    }

    /**
     * Returns whether or not a pokestop is in range.
     *
     * @return true when in range of player
     */
    public boolean isInRange(){
        return spi.engineIsInRange();
    }

    /**
     * Returns whether or not the lured pokemon is in range.
     *
     * @return true when the lured pokemon is in range of player
     */
    public boolean isInRangeForLuredPokemon(){
        return spi.engineIsInRangeForLuredPokemon();
    }

    /**
     * can user loot this from current position.
     *
     * @return true when lootable
     */
    public boolean canLoot(){
        return spi.engineCanLoot();
    }

    /**
     * @return the name of the landmark that is the Pokestop
     */
    public String getName(){
        return spi.engineGetName();
    }

    /**
     * @return the url to the pokestop images to display
     */
    public ProtocolStringList getImageUrl(){
        return spi.engineGetImageUrl();
    }

    public int getFp(){
        return spi.engineGetFp();
    }

    /**
     * @return the description of the landmark that is the Pokestop
     */
    public String getDescription(){
        return spi.engineGetDescription();
    }

    /**
     * @return a list of all of the modifiers attached to the Pokestop
     */
    public List<FortModifier> getModifiers(){
        return spi.engineGetModifiers();
    }

    /**
     * Loots the pokestop for pokeballs and other items.
     *
     * @return the Pokestop's Loot Result
     * @throws RequestFailedException if an exception occurs while sending the request
     */
    public LootResult loot() throws RequestFailedException {
        return spi.engineLoot(this);
    }

    /**
     * Adds a modifier to the pokestop. (i.e. add a lure module)
     *
     * @param item the modifier to add to this pokestop
     * @throws RequestFailedException if an exception occurs while sending the request
     */
    public boolean addModifier(ItemId item) throws RequestFailedException {
        return spi.engineAddModifier(item);
    }

    /**
     * Get more detailed information about the pokestop.
     *
     * @return FortDetails
     * @throws RequestFailedException if an exception occurs while sending the request
     */
    public boolean getDetails() throws RequestFailedException {
        return spi.engineGetDetails();
    }

    /**
     * Check to see if the Pokestop has the detailed information about itself
     * @return true if th
     */
    public boolean hasDetails() {
        return spi.engineHasDetails();
    }

    /**
     * Returns whether the pokestop has an active lure when detected on map.
     *
     * @return lure status
     */
    public boolean hasLure(){
        return spi.engineHasLure();
    }

    public Provider getProvider() {
        return provider;
    }
}
