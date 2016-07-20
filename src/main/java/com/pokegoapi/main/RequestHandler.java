package com.pokegoapi.main;

import POGOProtos.Networking.Envelopes.AuthTicketOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass;
import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler {
	private final PokemonGo api;
	private RequestEnvelopeOuterClass.RequestEnvelope.Builder builder;
	private boolean hasRequests;
	private RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth;
	private List<ServerRequest> serverRequests;
	private String api_endpoint;
	private OkHttpClient client;

	private AuthTicketOuterClass.AuthTicket lastAuth;

	public RequestHandler(PokemonGo api, RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth, OkHttpClient client) {
		this.api = api;
		this.client = client;
		api_endpoint = APISettings.API_ENDPOINT;
		this.auth = auth;
		serverRequests = new ArrayList<ServerRequest>();
		resetBuilder();
	}

	public void request(ServerRequest requestIn)
	{
		hasRequests = true;
		serverRequests.add(requestIn);
		builder.addRequests(requestIn.getRequest());
	}

	public void sendServerRequests()
	{
		setLatitude(api.getLatitude());
		setLongitude(api.getLongitude());
		setAltitude(api.getAltitude());
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			RequestEnvelopeOuterClass.RequestEnvelope request = builder.build();
			request.writeTo(stream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {

			RequestBody body = RequestBody.create(null, stream.toByteArray());
			okhttp3.Request request = new okhttp3.Request.Builder()
					.url(api_endpoint)
					.post(body)
					.build();
			Response response = client.newCall(request).execute();
			InputStream content = response.body().byteStream();

			ResponseEnvelopeOuterClass.ResponseEnvelope responseEnvelop = ResponseEnvelopeOuterClass.ResponseEnvelope.parseFrom(content);

			if (responseEnvelop.getStatusCode() == 102) {
				throw new LoginFailedException();
			}

			if (responseEnvelop.getApiUrl() != null && responseEnvelop.getApiUrl().length() > 0) {
				api_endpoint = "https://" + responseEnvelop.getApiUrl() + "/rpc";
			}

			if (responseEnvelop.hasAuthTicket()) {
				lastAuth = responseEnvelop.getAuthTicket();
			}

			// map each reply to the numeric response, ie first response = first request and send back to the requests to handle.
			int count = 0;
			for (ByteString payload : responseEnvelop.getReturnsList()) {
				ServerRequest serverReq = serverRequests.get(count);
				serverReq.handleData(payload);
				count++;

			}

			content.close();

			// 53 seems to mean handshak'n so need to resend request
			if (responseEnvelop.getStatusCode() == 53) {
				sendServerRequests();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resetBuilder();
	}



	private void resetBuilder() {
		builder = RequestEnvelopeOuterClass.RequestEnvelope.newBuilder();
		builder.setStatusCode(2);
		builder.setRequestId(8145806132888207460l);
		if (lastAuth != null && lastAuth.getExpireTimestampMs() > 0) 
			builder.setAuthTicket(lastAuth);
		 else 
			builder.setAuthInfo(auth);
		builder.setUnknown12(989);
		hasRequests = false;
		serverRequests.clear();
	}


	public RequestEnvelopeOuterClass.RequestEnvelope build() {
		if (!hasRequests)
			throw new IllegalStateException("Attempting to send request envelop with no requests");
		return builder.build();
	}

	public void setLatitude(double latitude) {
		builder.setLatitude(latitude);
	}

	public void setLongitude(double longitude) {
		builder.setLongitude(longitude);
	}

	public void setAltitude(double altitude) {
		builder.setAltitude(altitude);
	}

}
