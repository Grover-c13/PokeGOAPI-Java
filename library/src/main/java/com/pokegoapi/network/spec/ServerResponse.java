package com.pokegoapi.network.spec;

import com.github.aeonlucid.pogoprotos.networking.Requests;
import com.google.protobuf.ByteString;

public class ServerResponse {

    private ServerRequest request;
    private ByteString response;

    public ServerResponse(ServerRequest request, ByteString response) {
        this.request = request;
        this.response = response;
    }

    public ServerRequest getRequest() {
        return request;
    }

    public ByteString getResponse() {
        return response;
    }

    public Requests.RequestType getType() {
        return request.getType();
    }

    public String getName(){
        return request.getName();
    }
}
