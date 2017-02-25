package com.pokegoapi.network.spec;

import com.github.aeonlucid.pogoprotos.networking.Requests;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

public interface ServerRequest {

    String getName();

    Message getMessage();

    Requests.RequestType getType();

    ByteString send();

    void send(AsyncListener listener);

    boolean cancel(boolean mayInterruptIfRunning);

    boolean isDone();

    boolean isCancelled();

    interface AsyncListener {

        void start(ServerRequest request);

        void done(ServerResponse response);

        void cancelled(ServerRequest request);

        void error(ServerRequest request, Throwable throwable);
    }
}
