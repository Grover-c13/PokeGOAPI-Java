package com.pokegoapi.requests;

import POGOProtos.Networking.Requests.Messages.GetMapObjectsMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.GetMapObjectsResponseOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.main.Request;

import java.util.List;

public class GetMapObjectsRequest extends Request {
  private GetMapObjectsMessageOuterClass.GetMapObjectsMessage.Builder builder;

  public GetMapObjectsRequest(List<Long> cellIds, double latitude, double longitude) {
    builder = GetMapObjectsMessageOuterClass.GetMapObjectsMessage.newBuilder();
    int i = 0;
    for (Long cellId : cellIds) {
      builder.addCellId(cellId);
      builder.addSinceTimestampMs(0);
      i++;
    }
    builder.setLatitude(latitude);
    builder.setLongitude(longitude);
  }

  public RequestTypeOuterClass.RequestType getRpcId() {
    return RequestTypeOuterClass.RequestType.GET_MAP_OBJECTS;
  }

  public void handleResponse(ByteString payload) {
    try {
      GetMapObjectsResponseOuterClass.GetMapObjectsResponse response = GetMapObjectsResponseOuterClass.GetMapObjectsResponse.parseFrom(payload);

    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  public byte[] getInput() {
    return builder.build().toByteArray();
  }
}
