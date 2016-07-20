package com.pokegoapi.examples;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.auth.GoogleLogin;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PokeBankList {
    public static void main(String[] args) {
        OkHttpClient http = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build();
        RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth = null;

        try {
            String token = "";
            auth = new GoogleLogin(http).login(token); // currently uses oauth flow so no user or pass needed
            PokemonGo go = new PokemonGo(auth, http);

            PokeBank pokeBank = go.getPokebank();

            if (pokeBank == null)
                throw new RemoteServerException("pokeBank is null");

            log.debug(pokeBank.toString());
        } catch (RemoteServerException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
