package com.pokegoapi.go.spec;

import com.github.aeonlucid.pogoprotos.networking.Envelopes.AuthTicket;
import com.github.aeonlucid.pogoprotos.networking.Envelopes.RequestEnvelope.AuthInfo;
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
