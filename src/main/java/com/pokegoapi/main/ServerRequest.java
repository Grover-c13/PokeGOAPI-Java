package com.pokegoapi.main;

import POGOProtos.Networking.Requests.RequestOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import lombok.Getter;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

public class ServerRequest  {
	
	private RequestTypeOuterClass.RequestType type;
	@Getter RequestOuterClass.Request request;
	@Getter ByteString data;

	public ServerRequest(RequestTypeOuterClass.RequestType type, GeneratedMessage req)  {
		RequestOuterClass.Request.Builder reqBuilder = RequestOuterClass.Request.newBuilder();
		reqBuilder.setRequestMessage(req.toByteString());
		reqBuilder.setRequestType(type);
		this.request = reqBuilder.build();
		this.type = type;
	}

	public void handleData(ByteString bytes) throws InvalidProtocolBufferException {
		this.data = bytes;
	}
}
