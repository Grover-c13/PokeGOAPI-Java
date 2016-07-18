package com.pokegoapi.auth;

import lombok.Data;

@Data
public class GoogleAuthTokenJson {
	private String error;
	private String access_token;
	private String token_type;
	private int expires_in;
	private String refresh_token;
	private String id_token;
}
