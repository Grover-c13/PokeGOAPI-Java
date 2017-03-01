package com.pokegoapi.go.settings;

import com.pokegoapi.go.settings.spec.EventSettingsSpec;
import com.pokegoapi.go.settings.spec.FestivalSettingsSpec;
import com.pokegoapi.go.settings.spec.FortSettingsSpec;
import com.pokegoapi.go.settings.spec.GpsSettingsSpec;
import com.pokegoapi.go.settings.spec.InventorySettingsSpec;
import com.pokegoapi.go.settings.spec.LevelSettingsSpec;
import com.pokegoapi.go.settings.spec.MapSettingsSpec;
import com.pokegoapi.go.settings.spec.NewsSettingsSpec;
import com.pokegoapi.go.settings.spec.SfidaSettingsSpec;

/**
 * Created by chris on 1/23/2017.
 */
public abstract class SettingsSpi {
    public abstract EventSettingsSpec getEventSettings();

    public abstract FestivalSettingsSpec getFestivalSettings();

    public abstract FortSettingsSpec getFortSettings();

    public abstract GpsSettingsSpec getGpsSettings();

    public abstract InventorySettingsSpec getInventorySettings();

    public abstract LevelSettingsSpec getLevelSettings();

    public abstract MapSettingsSpec getMapSettings();

    public abstract NewsSettingsSpec getNewsSettings();

    public abstract SfidaSettingsSpec getSfidaSettings();

    public abstract String getMinimumClientVersion();

    public abstract int getMaximumPokemonTypes();
}
