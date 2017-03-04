package com.pokegoapi.go.settings.spec;

import com.github.aeonlucid.pogoprotos.Settings;

import java.util.List;

public interface NewsSettingsSpec {
    /**
     * @return a list of all currently displayed news entries
     */
    List<Settings.NewsSettings.News> getNews();
}
