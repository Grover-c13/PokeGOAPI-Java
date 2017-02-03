package com.pokegoapi.go.spec;

import POGOProtos.Networking.Envelopes.AuthTicketOuterClass.AuthTicket;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import com.pokegoapi.go.auth.CredentialProvider;

public interface Credentials {

    CredentialProvider getCredentialProvider();
    String getTokenId();
    boolean isTokenIdExpired();
    void login();
    boolean hasLoggedIn();
    AuthInfo getAuthInfo();
    void setAuthTicket(AuthTicket ticket);
    AuthTicket getAuthTicket();

}
