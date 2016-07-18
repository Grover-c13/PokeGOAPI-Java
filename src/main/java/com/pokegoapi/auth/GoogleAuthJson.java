package com.pokegoapi.auth;

import lombok.Data;

@Data
public class GoogleAuthJson {
	String device_code;
	String user_code;
	String verification_url;
	int expires_in;
	int interval;

}
