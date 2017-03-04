package com.pokegoapi.network.spec;

import com.github.aeonlucid.pogoprotos.networking.Requests;
import com.google.protobuf.ByteString;

public class ServerResponse {

    private ServerRequest request;
    private ByteString response;
    private Requests.RequestType type;

    public ServerResponse(ServerRequest request, ByteString response, Requests.RequestType type) {
        this.request = request;
        this.response = response;
        this.type = type;
    }

    public ServerRequest getRequest() {
        return request;
    }

    public ByteString getResponse() {
        return response;
    }

    public Requests.RequestType getType() {
        return type;
    }

    public String getName(){
        return request.getName();
    }
}
