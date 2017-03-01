package com.pokegoapi.go.settings.spec;

import com.github.aeonlucid.pogoprotos.Settings;

public interface FestivalSettingsSpec {
    /**
     * @return the type of currently active festival
     */
    Settings.FestivalSettings.FestivalType getFestivalType();

    String getKey();

    String getVector();
}
