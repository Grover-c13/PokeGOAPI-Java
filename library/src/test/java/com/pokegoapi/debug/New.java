package com.pokegoapi.debug;

import com.pokegoapi.api.PokemonApi;
import com.pokegoapi.api.device.ActivityStatus;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.auth.CredentialProvider;
import com.pokegoapi.auth.GoogleUserCredentialProvider;
import okhttp3.OkHttpClient;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by paul on 28-8-2016.
 */
public class New {
    static void testNew(String token, double latitude, double longitude) {
        OkHttpClient http = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
//                .addInterceptor(new MitmInterceptor())
                .build();
        CredentialProvider auth;
        auth = new GoogleUserCredentialProvider(http, token);
        PokemonApi api = PokemonApi.newBuilder()
                .withHttpClient(http)
                .credentialProvider(auth)
                .latitude(latitude)
                .longitude(longitude)
                .altitude(65d)
                .deviceInfo(DeviceInfo.getDefault(new Random()))
//                .sensorInfo(SensorInfo.getDefault(new Random()))
                .locationFixes(LocationFixes.getDefault(new Random()))
                .locale(new Locale("nl", "NL"))
                .activityStatus(ActivityStatus.getDefault(new Random()))
                .random(new Random())
                .build();
        api.getMap().getPokestops();
    }
}
