package com.pokegoapi.auth;

public class GoogleAuthJson 
{
	private String device_code;
	private String user_code;
	private String verification_url;
	private int expires_in;
	private int interval;
	
	public String getDevice_code() {
		return device_code;
	}
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}
	public String getUser_code() {
		return user_code;
	}
	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}
	public String getVerification_url() {
		return verification_url;
	}
	public void setVerification_url(String verification_url) {
		this.verification_url = verification_url;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	
	

}
