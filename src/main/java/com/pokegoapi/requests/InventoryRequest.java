package com.pokegoapi.requests;

import POGOProtos.Inventory.InventoryItemOuterClass;
import POGOProtos.Networking.Requests.Messages.GetInventoryMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.GetInventoryResponseOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.inventory.Pokemon;
import com.pokegoapi.main.Request;

import java.util.LinkedList;
import java.util.List;

public class InventoryRequest extends Request {

	List<Pokemon> pokemon = new LinkedList<Pokemon>();
	private GetInventoryMessageOuterClass.GetInventoryMessage.Builder builder;

	public InventoryRequest() {
		builder = GetInventoryMessageOuterClass.GetInventoryMessage.newBuilder();
	}

	public RequestTypeOuterClass.RequestType getRpcId() {
		return RequestTypeOuterClass.RequestType.GET_INVENTORY;
	}

	public void setTimestamp(long timestamp) {
		builder.setLastTimestampMs(timestamp);
	}

	public void handleResponse(ByteString payload) {
		try {
			GetInventoryResponseOuterClass.GetInventoryResponse response = GetInventoryResponseOuterClass.GetInventoryResponse.parseFrom(payload);
			for (InventoryItemOuterClass.InventoryItem item : response.getInventoryDelta().getInventoryItemsList()) {
				if (item.getInventoryItemData().hasPokemonData()) {
					pokemon.add(new Pokemon(item.getInventoryItemData().getPokemonData()));
				}
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}


	public byte[] getInput() {
		return builder.build().toByteArray();
	}

	public List<Pokemon> getPokemon() {
		return pokemon;
	}
}
