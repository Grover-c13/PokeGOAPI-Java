package com.pokegoapi.util;

import okhttp3.*;
import com.pokegoapi.api.PokemonGo;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.lang.IllegalAccessException;
import java.lang.IllegalStateException;
import java.lang.NullPointerException;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

public class BosslandHash {
    private static final String BASE = "http://pokehash.buddyauth.com/";
    private static final String VERSION_END = "api/hash/versions";

    private static String version;

    public static class HashResponseContent {
        @Getter private long locationAuthHash;
        @Getter private long locationHash;
        @Getter private List<Long> requestHashes;

        public HashResponseContent(String response) {
            response = response.replaceAll("\\D+", "|").substring(1);
            String[] split = response.split("\\|");

            locationAuthHash = Long.parseLong(split[0]);
            locationHash = Long.parseLong(split[1]);

            requestHashes = new ArrayList<>();
            for (int i = 2; i < split.length; i++)
                requestHashes.add(Long.parseLong(split[i]));
        }

    }

    public static class HashRequestContent {
        @Setter private long timestamp;
        @Setter private double latitude;
        @Setter private double longitude;
        @Setter private double altitude;
        @Setter private byte[] authTicket;
        @Setter private byte[] sessionData;
        @Getter private List<byte[]> requests;

        public HashRequestContent() {
            requests = new ArrayList<>();
        }

        public String json() {
            Base64.Encoder encoder = Base64.getEncoder();
            String auth = encoder.encodeToString(authTicket);
            String session = encoder.encodeToString(sessionData);
            String request = "";
            for (byte[] array : requests)
                request += "\"" + encoder.encodeToString(array) + "\",";
            request = request.substring(0, request.length() - 1);

            return "{"
                    + "\"Timestamp\":\"" + timestamp + "\","
                    + "\"Latitude\":" + latitude + ","
                    + "\"Longitude\":" + longitude + ","
                    + "\"Altitude\":" + altitude + ","
                    + "\"AuthTicket\":\"" + auth + "\","
                    + "\"SessionData\":\"" + session + "\","
                    + "\"Requests\":[" + request +  "]"
                    + "}";
        }
    }

    private static String version(OkHttpClient client) throws IOException {
        Response response = client.newCall(new Request.Builder()
                .url(BASE + VERSION_END)
                .build())
                .execute();

        if (response.code() == 200) { // OK
            String version = response.body().string();
            response.close();
            return version.substring(version.lastIndexOf(":") + 2, version.length() - 2);
        } else return null;
    }

    public static HashResponseContent request(PokemonGo api, OkHttpClient client, HashRequestContent content) throws IOException {
        if (api.getHashKey() == null)
            throw new NullPointerException("PokemonGo#getHashKey() returned null");

        if (version == null) {
            version = version(client);

            if (version == null)
                throw new IllegalStateException("Failed to load latest hashing version");
        }

        Call call = client.newCall(new Request.Builder()
                .url(BASE + version)
                .addHeader("Accept", "application/json")
                .addHeader("X-AuthToken", api.getHashKey())
                .post(RequestBody.create(MediaType.parse("application/json"), content.json()))
                .build());
        Response response = call.execute();

        int code = response.code();
        switch (code) {
            case 200: // OK
                String result = response.body().string();
                response.close();
                return new HashResponseContent(result);
            case 400: // Bad request
                throw new IllegalStateException("Bad request to hashing server, request=" + content.json());
            case 401: // Unauthorized
                throw new IllegalStateException("Unauthorized to request hash values");
            case 429: // Limit exceeded
                throw new IllegalStateException("Exceeded hashing limit");
            default:
                throw new IllegalStateException("Recieved unhandled response from hashing server. code=" + code);
        }
    }

}
