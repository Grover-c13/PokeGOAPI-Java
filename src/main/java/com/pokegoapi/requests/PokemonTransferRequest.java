package com.pokegoapi.requests;

import POGOProtos.Networking.Requests.Messages.ReleasePokemonMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.main.Request;

public class PokemonTransferRequest extends Request {
	private ReleasePokemonMessageOuterClass.ReleasePokemonMessage.Builder builder;
	private int candies;
	private ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result result;

	public PokemonTransferRequest(long entid) {
		candies = 0;
		result = ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result.UNSET;
		builder = ReleasePokemonMessageOuterClass.ReleasePokemonMessage.newBuilder();
		builder.setPokemonId(entid);
	}

	@Override
	public RequestTypeOuterClass.RequestType getRpcId() {
		return RequestTypeOuterClass.RequestType.RELEASE_POKEMON;
	}

	public int getCandies() {
		return candies;
	}

	public ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result getResult() {
		return result;
	}


	@Override
	public void handleResponse(ByteString payload) {
		try {
			ReleasePokemonResponseOuterClass.ReleasePokemonResponse out = ReleasePokemonResponseOuterClass.ReleasePokemonResponse.parseFrom(payload);
			candies = out.getCandyAwarded();
			result = out.getResult();
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

	}

	@Override
	public byte[] getInput() {
		return builder.build().toByteArray();
	}

}
