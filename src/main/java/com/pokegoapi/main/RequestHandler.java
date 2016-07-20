package com.pokegoapi.main;

import POGOProtos.Networking.Envelopes.AuthTicketOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.ResponseEnvelopeOuterClass;
import com.google.protobuf.ByteString;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

	public void request(ServerRequest requestIn) {
		hasRequests = true;
		serverRequests.add(requestIn);
		builder.addRequests(requestIn.getRequest());
	}

	public String debugRequestResponse(final RequestEnvelopeOuterClass.RequestEnvelope requestEnvelope,
									   final ResponseEnvelopeOuterClass.ResponseEnvelope responseEnvelope) {
		String requestStr = requestEnvelope == null ? "null" : requestEnvelope.toString();
		String responseStr = responseEnvelope == null ? "null" : responseEnvelope.toString();
		return String.format("Request:\n%s\nResponse:\n%s", requestStr, responseStr);
	}

	public void sendServerRequests() throws RemoteServerException, LoginFailedException {
		setLatitude(api.getLatitude());
		setLongitude(api.getLongitude());
		setAltitude(api.getAltitude());

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		RequestEnvelopeOuterClass.RequestEnvelope request = builder.build();
		try {
			request.writeTo(stream);
		} catch (IOException e) {
			log.error(String.format("Error while sending server request %s", ExceptionUtils.getStackTrace(e)));
		}

		RequestBody body = RequestBody.create(null, stream.toByteArray());
		okhttp3.Request httpRequest = new okhttp3.Request.Builder()
				.url(api_endpoint)
				.post(body)
				.build();
		Response response = null;

		try {
			response = client.newCall(httpRequest).execute();
		} catch (IOException e) {
			throw new RemoteServerException(e);
		}

		ResponseEnvelopeOuterClass.ResponseEnvelope responseEnvelop = null;
		try (InputStream content = response.body().byteStream()) {
			responseEnvelop = ResponseEnvelopeOuterClass.ResponseEnvelope.parseFrom(content);
		} catch (IOException e) {
			throw new RemoteServerException(String.format("Received malformed response: %s", ExceptionUtils.getStackTrace(e)));
		}

		//log.info(debugRequestResponse(request, responseEnvelop));

		if (responseEnvelop.getApiUrl() != null && responseEnvelop.getApiUrl().length() > 0) {
			api_endpoint = "https://" + responseEnvelop.getApiUrl() + "/rpc";
		}

		if (responseEnvelop.hasAuthTicket()) {
			lastAuth = responseEnvelop.getAuthTicket();
		}

		if (responseEnvelop.getStatusCode() == 102) {
			throw new LoginFailedException();
		} else if (responseEnvelop.getStatusCode() == 53) {
			// 53 means that the api_endpoint was not correctly set, should be at this point, though, so redo the request
			sendServerRequests();
			return;
		}

		// map each reply to the numeric response, ie first response = first request and send back to the requests to handle.
		int count = 0;
		for (ByteString payload : responseEnvelop.getReturnsList()) {
			ServerRequest serverReq = serverRequests.get(count);
			// TODO: Probably all other payloads are garbage as well in this case, so might as well throw an exception and leave this loop
			if (payload != null) {
				serverReq.handleData(payload);
			}
			count++;
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
