package com.pokegoapi.api.map.pokemon;

import POGOProtos.Networking.Requests.Messages.IncenseEncounterMessageOuterClass.IncenseEncounterMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.IncenseEncounterResponseOuterClass.IncenseEncounterResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;

public class IncenseEncounter extends Encounter {
	/**
	 * Creates a DiskEncounter object
	 *
	 * @param api the current api
	 * @param pokemon the pokemon of this encounter
	 */
	protected IncenseEncounter(PokemonGo api, CatchablePokemon pokemon) {
		super(api, pokemon);
	}

	@Override
	public EncounterResult encounter() throws RequestFailedException {
		IncenseEncounterMessage message = IncenseEncounterMessage.newBuilder()
				.setEncounterId(pokemon.encounterId)
				.setEncounterLocation(pokemon.spawnPointId)
				.build();

		ServerRequest request = new ServerRequest(RequestType.INCENSE_ENCOUNTER, message);
		ByteString responseData = api.requestHandler.sendServerRequests(request, true);

		try {
			IncenseEncounterResponse response = IncenseEncounterResponse.parseFrom(responseData);
			encounterResult = EncounterResult.from(response.getResult());
			activeItem = response.getActiveItem();
			captureProbabilities = response.getCaptureProbability();
			encounteredPokemon = response.getPokemonData();
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}

		return encounterResult;
	}
}
