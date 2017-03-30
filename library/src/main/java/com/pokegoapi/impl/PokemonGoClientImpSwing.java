package com.pokegoapi.impl;

import com.github.aeonlucid.pogoprotos.networking.Requests.RequestType;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.pokegoapi.go.PokemonGoClientSpi;
import com.pokegoapi.go.spec.Credentials;
import com.pokegoapi.go.spec.Location;
import com.pokegoapi.network.spec.ServerRequest;
import com.pokegoapi.network.spec.ServerResponse;

import javax.swing.*;
import java.util.concurrent.*;

/**
 * Created by chris on 3/29/2017.
 */
public class PokemonGoClientImpSwing extends PokemonGoClientSpi{

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void engineLogin(Credentials credentials) {

    }

    @Override
    public ServerRequest engineCreateRequest(Message request, RequestType type) {
        return new SwingServerRequest(request, type);
    }

    @Override
    public void engineMoveTo(double lat, double lng, double altitude) {

    }

    @Override
    public Location engineGetLocation() {
        return null;
    }

    @Override
    public Credentials engineGetCredentials() {
        return null;
    }

    @Override
    public long engineGetClientTime() {
        return 0;
    }

    /**
     * Created by chris on 3/29/2017.
     */
    private static class SwingServerRequest implements ServerRequest, Callable<ByteString> {

        private Message message;
        private RequestType type;

        private FutureTask<ByteString> future;

        private AsyncListener listener;

        private SwingServerRequest(Message message, RequestType type) {
            this.message = message;
            this.type = type;
            future = new FutureTask<ByteString>(this) {
                @Override
                protected void done() {
                    try {
                        postResult(get());
                    } catch (InterruptedException | ExecutionException e) {
                        postException(e.getCause());
                    } catch (CancellationException e) {
                        postCancel();
                    }
                }
            };
        }

        @Override
        public String getName() {
            return type.name();
        }

        @Override
        public Message getMessage() {
            return message;
        }

        @Override
        public RequestType getType() {
            return type;
        }

        @Override
        public ByteString send() {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void send(AsyncListener listener) {
            this.listener = listener;
            executorService.execute(future);
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return future.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isDone() {
            return future.isDone();
        }

        @Override
        public boolean isCancelled() {
            return future.isCancelled();
        }

        @Override
        public ByteString call() throws Exception {
            postStart();
            //TODO:finish
            return null;
        }

        private void postStart(){
            if(listener == null){
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    listener.start(SwingServerRequest.this);
                }
            });
        }

        private void postException(final Throwable t){
            if(listener == null){
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    listener.error(SwingServerRequest.this, t);
                }
            });
        }

        private void postCancel(){
            if(listener == null){
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    listener.cancelled(SwingServerRequest.this);
                }
            });
        }

        private void postResult(ByteString byteString) {
            if(listener == null){
                return;
            }
            final ServerResponse response = new ServerResponse(this, byteString);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    listener.done(response);
                }
            });
        }
    }
}
