package com.pokegoapi.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.main.Communication.RequestEnvelop.AuthInfo;
import com.pokegoapi.main.Communication.RequestEnvelop.AuthInfo.Builder;



public class PTCLogin extends Login {
  public static final String CLIENT_SECRET = "w8ScCUXJQc6kXKw8FiOhd8Fixzht18Dq3PEVkUCP5ZPxtgyWsbTvWHFLm2wNY0JR";
  public static final String REDIRECT_URI = "https://www.nianticlabs.com/pokemongo/error";
  public static final String CLIENT_ID = "mobile-app_pokemon-go";

  public static final String API_URL = "https://pgorelease.nianticlabs.com/plfe/rpc";
  public static final String LOGIN_URL = "https://sso.pokemon.com/sso/login?service=https%3A%2F%2Fsso.pokemon.com%2Fsso%2Foauth2.0%2FcallbackAuthorize";
  public static final String LOGIN_OAUTH = "https://sso.pokemon.com/sso/oauth2.0/accessToken";

  public static final String USER_AGENT = "niantic";


  /**
   * Returns an AuthInfo object given a token, this should not be an access token but rather an id_token
   *
   * @param String the id_token stored from a previous oauth attempt.
   * @return AuthInfo a AuthInfo proto structure to be encapsulated in server requests
   */
  public AuthInfo login(String token) {
    Builder builder = AuthInfo.newBuilder();
    builder.setProvider("ptc");
    builder.setToken(AuthInfo.JWT.newBuilder().setContents(token).setUnknown13(59).build());
    return builder.build();
  }

  /**
   * Starts a login flow for pokemon.com (PTC) using a username and password, this uses pokemon.com's oauth endpoint and returns a usable AuthInfo without user interaction
   *
   * @param String PTC username
   * @param String PTC password
   * @return AuthInfo a AuthInfo proto structure to be encapsulated in server requests
   */
  public AuthInfo login(String username, String password) throws LoginFailedException {
    HttpClient client = HttpClients.createDefault();
    URIBuilder builder;
    try {
      builder = new URIBuilder(LOGIN_URL);

      HttpGet get = new HttpGet(builder.build());
      get.setHeader("User-Agent", USER_AGENT);

      HttpResponse response = client.execute(get);

      Gson gson = new GsonBuilder().create();

      PTCAuthJson ptcAuth = gson.fromJson(getResponseBody(response), PTCAuthJson.class);

      builder = new URIBuilder(LOGIN_URL);
      builder.addParameter("lt", ptcAuth.getLt());
      builder.addParameter("execution", ptcAuth.getExecution());
      builder.addParameter("_eventId", "submit");
      builder.addParameter("username", username);
      builder.addParameter("password", password);
      HttpPost post = new HttpPost(builder.build());
      post.setHeader("User-Agent", USER_AGENT);

      response = client.execute(post);
      String body = getResponseBody(response);
      if (body.length() > 0) {
        PTCError ptcError = gson.fromJson(body, PTCError.class);
        if (ptcError.getError() != null && ptcError.getError().length() > 0) {
          throw new LoginFailedException();
        }
      }

      String ticket = null;
      for (Header location : response.getHeaders("location")) {
        ticket = location.getValue().split("ticket=")[1];
      }

      builder = new URIBuilder(LOGIN_OAUTH);
      builder.addParameter("client_id", CLIENT_ID);
      builder.addParameter("redirect_uri", REDIRECT_URI);
      builder.addParameter("client_secret", CLIENT_SECRET);
      builder.addParameter("grant_type", "refresh_token");
      builder.addParameter("code", ticket);

      post = new HttpPost(builder.build());
      post.setHeader("User-Agent", USER_AGENT);

      response = client.execute(post);
      body = getResponseBody(response);

      String token;
      try {
        token = body.split("token=")[1];
        token = token.split("&")[0];
      } catch (Exception e) {
        throw new LoginFailedException();
      }

      Builder authbuilder = AuthInfo.newBuilder();
      authbuilder.setProvider("ptc");
      authbuilder.setToken(AuthInfo.JWT.newBuilder().setContents(token).setUnknown13(59).build());

      return authbuilder.build();
    } catch (Exception e) {
      e.printStackTrace();
      throw new LoginFailedException();
    }

  }

  private String getResponseBody(HttpResponse response) throws UnsupportedOperationException, IOException {
    BufferedReader buff = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    String body = "";
    String line;

    while ((line = buff.readLine()) != null) {
      body += line;
    }

    return body;
  }

}
