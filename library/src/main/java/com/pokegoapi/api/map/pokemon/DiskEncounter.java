package com.pokegoapi.api.map.pokemon;

import POGOProtos.Networking.Requests.Messages.DiskEncounterMessageOuterClass.DiskEncounterMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.DiskEncounterResponseOuterClass.DiskEncounterResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;

public class DiskEncounter extends Encounter {
	/**
	 * Creates a DiskEncounter object
	 *
	 * @param api the current api
	 * @param pokemon the pokemon of this encounter
	 */
	protected DiskEncounter(PokemonGo api, CatchablePokemon pokemon) {
		super(api, pokemon);
	}

	@Override
	public EncounterResult encounter() throws RequestFailedException {
		DiskEncounterMessage message = DiskEncounterMessage.newBuilder()
				.setEncounterId(pokemon.encounterId)
				.setFortId(pokemon.spawnPointId)
				.setPlayerLatitude(api.latitude)
				.setPlayerLongitude(api.longitude)
				.build();

		ServerRequest request = new ServerRequest(RequestType.DISK_ENCOUNTER, message);
		ByteString responseData = api.requestHandler.sendServerRequests(request, true);

		try {
			DiskEncounterResponse response = DiskEncounterResponse.parseFrom(responseData);
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
