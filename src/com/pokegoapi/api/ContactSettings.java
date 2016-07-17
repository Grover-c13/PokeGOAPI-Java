package com.pokegoapi.api;

public class ContactSettings 
{
	private boolean sendMarketingEmails;
	private boolean sendPushNotifications;
	public boolean isSendMarketingEmails() {
		return sendMarketingEmails;
	}
	public void setSendMarketingEmails(boolean sendMarketingEmails) {
		this.sendMarketingEmails = sendMarketingEmails;
	}
	public boolean isSendPushNotifications() {
		return sendPushNotifications;
	}
	public void setSendPushNotifications(boolean sendPushNotifications) {
		this.sendPushNotifications = sendPushNotifications;
	}
	
	
	
}
