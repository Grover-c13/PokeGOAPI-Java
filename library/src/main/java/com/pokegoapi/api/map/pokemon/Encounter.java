package com.pokegoapi.api.map.pokemon;

import POGOProtos.Data.Capture.CaptureAwardOuterClass.CaptureAward;
import POGOProtos.Data.Capture.CaptureProbabilityOuterClass.CaptureProbability;
import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.CatchPokemonMessageOuterClass.CatchPokemonMessage;
import POGOProtos.Networking.Requests.Messages.EncounterMessageOuterClass.EncounterMessage;
import POGOProtos.Networking.Requests.Messages.UseItemEncounterMessageOuterClass.UseItemEncounterMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CaptureReason;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import POGOProtos.Networking.Responses.UseItemEncounterResponseOuterClass.UseItemEncounterResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.settings.PokeballSelector;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Encounter {
	protected final PokemonGo api;

	@Getter
	protected final CatchablePokemon pokemon;

	@Getter
	protected CatchPokemonResponse.CatchStatus status = CatchStatus.UNRECOGNIZED;

	@Getter
	protected EncounterResult encounterResult;

	protected ItemId activeItem;

	@Getter
	protected CaptureProbability captureProbabilities;

	@Getter
	protected CaptureAward captureAward;

	@Getter
	protected CaptureReason captureReason;

	@Getter
	protected long capturedPokemon;

	@Getter
	protected PokemonData encounteredPokemon;

	/**
	 * Creates an Encounter object
	 *
	 * @param api the current api
	 * @param pokemon the pokemon of this encounter
	 */
	protected Encounter(PokemonGo api, CatchablePokemon pokemon) {
		this.api = api;
		this.pokemon = pokemon;
	}

	/**
	 * Encounters this pokemon
	 *
	 * @return the result from the attempted encounter
	 * @throws RequestFailedException if the encounter request fails
	 */
	protected EncounterResult encounter() throws RequestFailedException {
		EncounterMessage message = EncounterMessage.newBuilder()
				.setEncounterId(pokemon.getEncounterId())
				.setSpawnPointId(pokemon.getSpawnPointId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.build();

		ServerRequest request = new ServerRequest(RequestType.ENCOUNTER, message);
		ByteString responseData = api.getRequestHandler().sendServerRequests(request, true);

		try {
			EncounterResponse response = EncounterResponse.parseFrom(responseData);
			encounterResult = EncounterResult.from(response.getStatus());
			activeItem = response.getActiveItem();
			captureProbabilities = response.getCaptureProbability();
			encounteredPokemon = response.getWildPokemon().getPokemonData();
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}

		return encounterResult;
	}

	/**
	 * Throws a pokeball in this encounter using a PokeballSelector
	 *
	 * @param selector the selector for the pokeball to throw
	 * @param throwProperties the throw properties for this throw
	 * @return the result from the pokeball throw
	 * @throws RequestFailedException if the throw request fails
	 * @throws NoSuchItemException if the requested pokeball does not exist
	 */
	public CatchPokemonResponse.CatchStatus throwPokeball(PokeballSelector selector, ThrowProperties throwProperties)
			throws RequestFailedException, NoSuchItemException {
		List<Pokeball> pokeballs = api.getInventories().getItemBag().getUsablePokeballs();
		if (pokeballs.size() > 0) {
			Pokeball pokeball = selector.select(pokeballs, getCaptureProbability());
			return throwPokeball(pokeball.getBallType(), throwProperties);
		} else {
			throw new NoSuchItemException();
		}
	}

	/**
	 * Throws a pokeball in this encounter
	 *
	 * @param pokeball the pokeball to throw
	 * @param throwProperties the throw properties for this throw
	 * @return the result from the pokeball throw
	 * @throws RequestFailedException if the throw request fails
	 * @throws NoSuchItemException if the requested pokeball does not exist
	 */
	public CatchPokemonResponse.CatchStatus throwPokeball(ItemId pokeball, ThrowProperties throwProperties)
			throws RequestFailedException, NoSuchItemException {
		if (isActive()) {
			ItemBag bag = api.getInventories().getItemBag();
			if (bag.getItem(pokeball).getCount() > 0) {
				CatchPokemonMessage message = CatchPokemonMessage.newBuilder()
						.setEncounterId(pokemon.getEncounterId())
						.setSpawnPointId(pokemon.getSpawnPointId())
						.setPokeball(pokeball)
						.setNormalizedHitPosition(throwProperties.getNormalizedHitPosition())
						.setNormalizedReticleSize(throwProperties.getNormalizedReticleSize())
						.setSpinModifier(throwProperties.getSpinModifier())
						.setHitPokemon(throwProperties.shouldHitPokemon())
						.build();

				ServerRequest request = new ServerRequest(RequestType.CATCH_POKEMON, message);
				ByteString responseData = api.getRequestHandler().sendServerRequests(request, true);

				try {
					CatchPokemonResponse response = CatchPokemonResponse.parseFrom(responseData);
					status = response.getStatus();
					if (hasCaptured()) {
						captureAward = response.getCaptureAward();
						capturedPokemon = response.getCapturedPokemonId();
						captureReason = response.getCaptureReason();
					}
					if (status == CatchStatus.CATCH_SUCCESS || status == CatchStatus.CATCH_FLEE) {
						pokemon.setDespawned(true);
					}
				} catch (InvalidProtocolBufferException e) {
					throw new RequestFailedException(e);
				}
			} else {
				throw new NoSuchItemException();
			}
		}
		return status;
	}

	/**
	 * Uses an item in this encounter
	 *
	 * @param item the item to use
	 * @return the result from this action
	 * @throws RequestFailedException if the use request fails
	 */
	public UseItemEncounterResponse.Status useItem(ItemId item) throws RequestFailedException {
		if (isActive()) {
			ItemBag bag = api.getInventories().getItemBag();
			if (bag.getItem(item).getCount() > 0) {
				if (activeItem == null) {
					UseItemEncounterMessage message = UseItemEncounterMessage.newBuilder()
							.setEncounterId(pokemon.getEncounterId())
							.setSpawnPointGuid(pokemon.getSpawnPointId())
							.setItem(item)
							.build();

					ServerRequest request = new ServerRequest(RequestType.USE_ITEM_ENCOUNTER, message);
					ByteString responseData = api.getRequestHandler().sendServerRequests(request, true);

					try {
						UseItemEncounterResponse response = UseItemEncounterResponse.parseFrom(responseData);
						activeItem = response.getActiveItem();
						captureProbabilities = response.getCaptureProbability();
						return response.getStatus();
					} catch (InvalidProtocolBufferException e) {
						throw new RequestFailedException(e);
					}
				} else {
					return UseItemEncounterResponse.Status.ACTIVE_ITEM_EXISTS;
				}
			} else {
				return UseItemEncounterResponse.Status.NO_ITEM_IN_INVENTORY;
			}
		}
		return UseItemEncounterResponse.Status.ALREADY_COMPLETED;
	}

	/**
	 * @return true if this encounter is still active, and this pokemon has not yet been caught or fled
	 */
	public boolean isActive() {
		return encounterResult == EncounterResult.SUCCESS
				&& (status != CatchStatus.CATCH_FLEE && status != CatchStatus.CATCH_SUCCESS);
	}

	/**
	 * @return true if this pokemon has been captured
	 */
	public boolean hasCaptured() {
		return status == CatchStatus.CATCH_SUCCESS;
	}

	/**
	 * @return true if this pokemon was successfully encountered
	 */
	public boolean isSuccessful() {
		return encounterResult == EncounterResult.SUCCESS;
	}

	/**
	 * @return the capture probability for this pokemon
	 */
	public double getCaptureProbability() {
		return captureProbabilities.getCaptureProbability(0);
	}

	/**
	 * @return the current reticle difficulty scale
	 */
	public double getReticleDifficultyScale() {
		if (captureProbabilities != null) {
			return captureProbabilities.getReticleDifficultyScale();
		}
		return 0.0;
	}

	/**
	 * @return the currently active item
	 */
	public ItemId getActiveItem() {
		if (activeItem == ItemId.UNRECOGNIZED || activeItem == ItemId.ITEM_UNKNOWN) {
			return null;
		}
		return activeItem;
	}

	/**
	 * @return the awarded candies from this capture
	 */
	public List<Integer> getAwardedCandies() {
		if (hasCaptured()) {
			return captureAward.getCandyList();
		}
		return new ArrayList<>();
	}

	/**
	 * @return the awarded stardust from this capture
	 */
	public List<Integer> getAwardedStardust() {
		if (hasCaptured()) {
			return captureAward.getStardustList();
		}
		return new ArrayList<>();
	}

	/**
	 * @return the awarded experience from this capture
	 */
	public List<Integer> getAwardedExperience() {
		if (hasCaptured()) {
			return captureAward.getXpList();
		}
		return new ArrayList<>();
	}
}
