package com.pokegoapi.examples;

import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.GoogleLogin;
import com.pokegoapi.auth.UserReader;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PokeBankList {
    public static void main(String[] args) {
        OkHttpClient http = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build();
        RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth = null;
        UserReader userReader = null;

        try {
            userReader = UserReader.getUserReader(UserReader.GOOGLE_USERS);
            String[] user = userReader.getSingle();
            if (user == null)
                throw new IOException("Cannot get token from google users list");

            String token = user[0];
            auth = new GoogleLogin(http).login(token); // currently uses oauth flow so no user or pass needed
            PokemonGo go = new PokemonGo(auth, http);

            PokeBank pokeBank = go.getPokebank();

            if (pokeBank == null)
                throw new RemoteServerException("pokeBank is null");

            log.debug(pokeBank.toString());
        } catch (IOException | RemoteServerException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
