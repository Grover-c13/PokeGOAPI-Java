package com.pokegoapi.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.main.Communication.RequestEnvelop.AuthInfo;
import com.pokegoapi.main.Communication.RequestEnvelop.AuthInfo.Builder;

public class GoogleLogin extends Login 
{
	public static final String SECRET = "NCjF1TLi2CcY6t5mt0ZveuL7";
	public static final String CLIENT_ID = "848232511240-73ri3t7plvk96pj4f85uj8otdat2alem.apps.googleusercontent.com";
	public static final String OAUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/device/code";
	public static final String OAUTH_TOKEN_ENDPOINT = "https://www.googleapis.com/oauth2/v4/token";

	/**
	 * Returns an AuthInfo object given a token, this should not be an access token but rather an id_token
	 * 
	 * @param  		String the id_token stored from a previous oauth attempt.
	 * @return      AuthInfo a AuthInfo proto structure to be encapsulated in server requests
	 */
	public AuthInfo login(String token)
	{
		Builder builder = AuthInfo.newBuilder();
		builder.setProvider("google");
		builder.setToken(AuthInfo.JWT.newBuilder().setContents(token).setUnknown13(59).build());
		return builder.build();
	}
	
	/**
	 * Starts a login flow for google using a username and password, this uses googles device oauth endpoint, a URL and code is display
	 * not really ideal right now.
	 * 
	 * @param  		String Google username
	 * @param  		String Google password
	 * @return      AuthInfo a AuthInfo proto structure to be encapsulated in server requests
	 */
	public AuthInfo login(String username, String password) throws LoginFailedException 
	{
		HttpClient client = HttpClients.createDefault();
		URIBuilder builder;
		try {
			builder = new URIBuilder(OAUTH_ENDPOINT);
		
			builder.addParameter("client_id", CLIENT_ID);
			builder.addParameter("scope", "openid email https://www.googleapis.com/auth/userinfo.email");
			HttpPost post = new HttpPost(builder.build());	
			
			HttpResponse response = client.execute(post);

            Gson gson = new GsonBuilder().create();
      
            GoogleAuthJson googleAuth = gson.fromJson(getJsonFromPost(response), GoogleAuthJson.class);
            System.out.println("Get user to go to:" + googleAuth.getVerification_url() + " and enter code:" + googleAuth.getUser_code());
            
            GoogleAuthTokenJson token;
            while( (token = poll(googleAuth)) == null)
            {
            	Thread.sleep(googleAuth.getInterval()*1000);
            }
            
            System.out.println("Got token:" + token.getId_token());
			
    		Builder authbuilder = AuthInfo.newBuilder();
    		authbuilder.setProvider("google");
    		authbuilder.setToken( AuthInfo.JWT.newBuilder().setContents( token.getId_token() ).setUnknown13(59).build());
    		
    		return authbuilder.build();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			throw new LoginFailedException();
		}

	}
	
	
	private GoogleAuthTokenJson poll(GoogleAuthJson json) throws URISyntaxException, ClientProtocolException, IOException
	{
		HttpClient client = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(OAUTH_TOKEN_ENDPOINT);
		
		builder.addParameter("client_id", CLIENT_ID);
		builder.addParameter("client_secret", SECRET);
		builder.addParameter("code", json.getDevice_code());
		builder.addParameter("grant_type", "http://oauth.net/grant_type/device/1.0");
		builder.addParameter("scope", "openid email https://www.googleapis.com/auth/userinfo.email");
		HttpPost post = new HttpPost(builder.build());	
		
		HttpResponse response = client.execute(post);


        Gson gson = new GsonBuilder().create();
        GoogleAuthTokenJson token = gson.fromJson(getJsonFromPost(response), GoogleAuthTokenJson.class);
        
        if (token.getError() == null)
        {
        	return token;
        }
        else
        {
        	return null;
        }
        
	}
	
	private String getJsonFromPost(HttpResponse response) throws UnsupportedOperationException, IOException
	{
		BufferedReader buff = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String json = "";
	    String line = null;
	    
        while ( (line = buff.readLine()) != null)
        {

            json += line;
        }
        
        
        return json;
  
	}

}
