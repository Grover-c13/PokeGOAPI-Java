package com.pokegoapi.main;

import POGOProtos.Networking.EnvelopesOuterClass;
import com.google.protobuf.ByteString;
import com.pokegoapi.exceptions.LoginFailedException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler {
	private EnvelopesOuterClass.Envelopes.RequestEnvelope.Builder builder;
	private boolean hasRequests;
	private EnvelopesOuterClass.Envelopes.RequestEnvelope.AuthInfo auth;
	private List<ServerRequest> serverRequests;
	private String api_endpoint;
	private HttpClient client;

	private EnvelopesOuterClass.Envelopes.AuthTicket lastAuth;

	public RequestHandler(EnvelopesOuterClass.Envelopes.RequestEnvelope.AuthInfo auth) {
		client = HttpClients.createDefault();
		api_endpoint = APISettings.API_ENDPOINT;
		this.auth = auth;
		serverRequests = new ArrayList<>();
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
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			EnvelopesOuterClass.Envelopes.RequestEnvelope request = builder.build();
			request.writeTo(stream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		try {
			HttpPost post = new HttpPost(api_endpoint);
			post.setEntity(new ByteArrayEntity(stream.toByteArray()));

			HttpResponse response = client.execute(post);
			InputStream content = response.getEntity().getContent();

			EnvelopesOuterClass.Envelopes.ResponseEnvelope responseEnvelop = EnvelopesOuterClass.Envelopes.ResponseEnvelope.parseFrom(content);

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
			System.out.println(responseEnvelop);
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
		builder = EnvelopesOuterClass.Envelopes.RequestEnvelope.newBuilder();
		builder.setStatusCode(2);
		builder.setRequestId(8145806132888207460l);
		if (lastAuth != null && lastAuth.getExpireTimestampMs() > 0) {
			builder.setAuthTicket(lastAuth);
		} else {
			builder.setAuthInfo(auth);
		}
		builder.setUnknown12(989);
		hasRequests = false;
		serverRequests.clear();
	}


	public EnvelopesOuterClass.Envelopes.RequestEnvelope build() {
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
