package com.pokegoapi.main;

import POGOProtos.Networking.Requests.RequestOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ServerRequest  {
	private RequestTypeOuterClass.RequestType type;
	private RequestOuterClass.Request req;
	private ByteString data;

	public ServerRequest(RequestTypeOuterClass.RequestType type, GeneratedMessage req)  {
		RequestOuterClass.Request.Builder reqBuilder = RequestOuterClass.Request.newBuilder();
		reqBuilder.setRequestMessage(req.toByteString());
		reqBuilder.setRequestType(type);
		this.req = reqBuilder.build();
		this.type = type;
	}



	public void handleData(ByteString bytes) throws InvalidProtocolBufferException {
		this.data = bytes;
	}

	public ByteString getData() {
		return data;
	}

	public RequestOuterClass.Request getRequest() {
		return req;
	}
}
