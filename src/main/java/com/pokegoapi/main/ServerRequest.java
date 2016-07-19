package com.pokegoapi.main;

import POGOProtos.Networking.Requests.RequestOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.Getter;

public class ServerRequest {

	@Getter
	RequestOuterClass.Request request;
	private RequestTypeOuterClass.RequestType type;
	private ByteString data;

	public ServerRequest(RequestTypeOuterClass.RequestType type, GeneratedMessage req) {
		RequestOuterClass.Request.Builder reqBuilder = RequestOuterClass.Request.newBuilder();
		reqBuilder.setRequestMessage(req.toByteString());
		reqBuilder.setRequestType(type);
		this.request = reqBuilder.build();
		this.type = type;
	}

	public void handleData(ByteString bytes) {
		this.data = bytes;
	}

	public ByteString getData() throws InvalidProtocolBufferException {
		if (data == null) {
			throw new InvalidProtocolBufferException("Contents of buffer are null");
		}
		return data;
	}
}
