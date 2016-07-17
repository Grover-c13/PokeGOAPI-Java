package com.pokegoapi.requests;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.main.Communication;
import com.pokegoapi.main.Map;
import com.pokegoapi.main.Request;

import java.util.List;

public class GetMapObjectsRequest extends Request {
  private Map.GetMapObjectsProto.Builder builder;

  public GetMapObjectsRequest(List<Long> cellIds, double latitude, double longitude) {
    builder = Map.GetMapObjectsProto.newBuilder();
    int i = 0;
    for (Long cellId : cellIds) {
      builder.addCellId(cellId);
      builder.addSinceTimeMs(0);
      i++;
    }
    builder.setPlayerLat(latitude);
    builder.setPlayerLng(longitude);
  }

  public Communication.Method getRpcId() {
    return Communication.Method.GET_MAP_OBJECTS;
  }

  public void handleResponse(Communication.Payload payload) {
    try {
      Map.GetMapObjectsOutProto response = Map.GetMapObjectsOutProto.parseFrom(payload.getData());

    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  public byte[] getInput() {
    return builder.build().toByteArray();
  }
}
