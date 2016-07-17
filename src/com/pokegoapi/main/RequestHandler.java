package com.pokegoapi.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;

import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.main.Communication.Payload;
import com.pokegoapi.main.Communication.RequestEnvelop;
import com.pokegoapi.main.Communication.RequestEnvelop.AuthInfo;
import com.pokegoapi.main.Communication.RequestEnvelop.Builder;
import com.pokegoapi.main.Communication.ResponseEnvelop;



public class RequestHandler 
{
	private Builder builder;
	private boolean hasRequests;
	private AuthInfo auth;
	private List<Request> requests;
	private String api_endpoint;
	private HttpClient client;
	
	public RequestHandler(AuthInfo auth)
	{
		client = HttpClients.createDefault();
		api_endpoint = APISettings.API_ENDPOINT;
		this.auth = auth;
		requests = new ArrayList<Request>();
		resetBuilder();
	}
	
	public void doRequest(Request request)
	{
		addRequest(request);
		sendRequests();
		
	}
	
	public void sendRequests()
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try 
		{
			
			RequestEnvelop request = builder.build();
			System.out.println(request);
			request.writeTo(stream);
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		byte[] array = stream.toByteArray();
		
		try 
		{
			
			HttpPost post = new HttpPost(api_endpoint);
			HttpEntity entity = new ByteArrayEntity(array);
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			InputStream content = response.getEntity().getContent();

			ResponseEnvelop responseEnvelop = ResponseEnvelop.parseFrom(content);
			
			if (responseEnvelop.getUnknown1() == 102)
			{
				throw new LoginFailedException();
			}
	
			
			System.out.println(responseEnvelop);
			if (responseEnvelop.hasApiUrl())
			{
				api_endpoint = "https://" + responseEnvelop.getApiUrl() + "/rpc";
			}
			
			// map each reply to the numeric response, ie first response = first request and send back to the requests to handle.
			int count = 0;
			for (Payload payload: responseEnvelop.getPayloadList())
			{
				requests.get(count).handleResponse(payload);
				count++;
			}
			
			content.close();
			
			// 53 seems to mean handshak'n so need to resend request
			if (responseEnvelop.getUnknown1() == 53)
			{
				sendRequests();
			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		resetBuilder();
		
	}
	


	
	private void resetBuilder()
	{
		builder =  RequestEnvelop.newBuilder();
		builder.setUnknown1(2);
		builder.setRpcId(8145806132888207460l);
		builder.setAuth(auth);
		builder.setUnknown12(989);
		hasRequests = false;
		requests.clear();
	}
	
	public void addRequest(Request request)
	{
		hasRequests = true;
		requests.add(request);
		builder.addRequests(request.getRequest());
	}
	
	public RequestEnvelop build()
	{
		if (!hasRequests)
		{
			throw new IllegalStateException("Attempting to send request envelop with no requests");
		}
		return builder.build();
	}
	

}
