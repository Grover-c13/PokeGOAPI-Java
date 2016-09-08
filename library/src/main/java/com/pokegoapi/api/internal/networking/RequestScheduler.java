package com.pokegoapi.api.internal.networking;

import POGOProtos.Networking.Envelopes.AuthTicketOuterClass.AuthTicket;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass.ResponseEnvelope;
import com.google.protobuf.ByteString;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Paul van Assen
 */
class RequestScheduler {
	private static final int REQUESTS_PER_SECOND = 3;
	private static final int MINIMUM_WAIT_TIME = Math.round(1 / (float) REQUESTS_PER_SECOND);
	private final RequestExecutor requestExecutor;
	private final BlockingQueue<RequestWrap> requestQueue = new LinkedBlockingQueue<>();

	RequestScheduler(ExecutorService executorService, OkHttpClient client, URL currentServer) {
		this.requestExecutor = new RequestExecutor(requestQueue, client, currentServer);
		executorService.submit(requestExecutor);
	}

	Observable<ResponseEnvelope> queueRequest(final RequestEnvelope requestEnvelope) {
		return Observable
				.create(new Observable.OnSubscribe<ResponseEnvelope>() {
					@Override
					public void call(Subscriber<? super ResponseEnvelope> subscriber) {
						requestQueue.add(new RequestWrap(requestEnvelope, subscriber));
					}
				});
	}

	void setCurrentServer(URL currentServer) {
		this.requestExecutor.currentServer = currentServer;
	}

	AuthTicket getAuthTicket() {
		return requestExecutor.authTicket;
	}

	@Slf4j
	private static class RequestExecutor implements Runnable {
		private final BlockingQueue<RequestWrap> requestQueue;
		private final OkHttpClient client;
		private long lastRequest = 0;
		private URL currentServer;
		private AuthTicket authTicket;

		RequestExecutor(BlockingQueue<RequestWrap> requestQueue, OkHttpClient client, URL currentServer) {
			this.requestQueue = requestQueue;
			this.client = client;
			this.currentServer = currentServer;
		}

		@Override
		public void run() {
			boolean run = true;
			RequestWrap request = null;
			while (run) {
				try {
					try {
						request = requestQueue.poll(1, TimeUnit.HOURS);
						while (System.currentTimeMillis() < lastRequest + MINIMUM_WAIT_TIME) {
							Thread.sleep(10);
						}
					} catch (InterruptedException e) {
						run = false;
					}
					if (request == null) {
						continue;
					}
					request.getSubscriber().onStart();
					// Do request
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					try {
						request.getRequestEnvelope().writeTo(stream);
					} catch (IOException e) {
						request.getSubscriber().onError(e);
						continue;
					}
					RequestBody body = RequestBody.create(null, stream.toByteArray());
					okhttp3.Request httpRequest = new okhttp3.Request.Builder()
							.url(currentServer)
							.post(body)
							.addHeader("Content-Type", "application/x-www-form-urlencoded")
							.build();
					try (Response response = client.newCall(httpRequest).execute()) {
						if (response.code() != 200) {
							request.getSubscriber().onError(
									new RemoteServerException("Got a unexpected http code : " + response.code()));
							continue;
						}

						ResponseEnvelope responseEnvelop;
						try (InputStream content = response.body().byteStream()) {
							responseEnvelop = ResponseEnvelope.parseFrom(content);
						} catch (IOException e) {
							// retrieved garbage from the server
							request.getSubscriber()
									.onError(new RemoteServerException("Received malformed response : " + e));
							continue;
						}
						if (responseEnvelop.getAuthTicket().getExpireTimestampMs() > 0L
								&& responseEnvelop.getAuthTicket().getStart() != ByteString.EMPTY) {
							authTicket = responseEnvelop.getAuthTicket();
						}
						request.getSubscriber().onNext(responseEnvelop);
						if (!request.getSubscriber().isUnsubscribed()) {
							request.getSubscriber().onCompleted();
						}
					} catch (IOException e) {
						request.getSubscriber().onError(new RemoteServerException(e));
					}
				} catch (RuntimeException e) {
					log.error("Dramatic crash!", e);
					if (request != null) {
						request.getSubscriber().onError(new RemoteServerException(e));
					}
				}
			}
		}
	}

	@Data
	private static class RequestWrap {
		private final RequestEnvelope requestEnvelope;
		private final Subscriber<? super ResponseEnvelope> subscriber;
	}
}
