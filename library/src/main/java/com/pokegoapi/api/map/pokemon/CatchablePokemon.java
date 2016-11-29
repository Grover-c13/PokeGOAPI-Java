/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api.map.pokemon;


import POGOProtos.Enums.EncounterTypeOuterClass.EncounterType;
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass.WildPokemon;
import POGOProtos.Networking.Requests.Messages.CatchPokemonMessageOuterClass.CatchPokemonMessage;
import POGOProtos.Networking.Requests.Messages.DiskEncounterMessageOuterClass.DiskEncounterMessage;
import POGOProtos.Networking.Requests.Messages.EncounterMessageOuterClass.EncounterMessage;
import POGOProtos.Networking.Requests.Messages.UseItemCaptureMessageOuterClass.UseItemCaptureMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;
import POGOProtos.Networking.Responses.DiskEncounterResponseOuterClass.DiskEncounterResponse;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import POGOProtos.Networking.Responses.UseItemCaptureResponseOuterClass.UseItemCaptureResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.listener.PokemonListener;
import com.pokegoapi.api.map.pokemon.encounter.DiskEncounterResult;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.api.map.pokemon.encounter.NormalEncounterResult;
import com.pokegoapi.api.settings.CatchOptions;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonCallback;
import com.pokegoapi.main.PokemonRequest;
import com.pokegoapi.main.PokemonResponse;
import com.pokegoapi.main.RequestCallback;
import com.pokegoapi.main.Utils;
import com.pokegoapi.util.MapPoint;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Catchable pokemon.
 */
@ToString
public class CatchablePokemon implements MapPoint {

	private static final String TAG = CatchablePokemon.class.getSimpleName();
	private final PokemonGo api;
	@Getter
	private final String spawnPointId;
	@Getter
	private final long encounterId;
	@Getter
	private final PokemonId pokemonId;
	@Getter
	private final int pokemonIdValue;
	@Getter
	private final long expirationTimestampMs;
	@Getter
	private final double latitude;
	@Getter
	private final double longitude;
	private final EncounterKind encounterKind;
	private Boolean encountered = null;
	@Getter
	private EncounterResult encounterResult;

	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api the api
	 * @param proto the proto
	 */
	public CatchablePokemon(PokemonGo api, MapPokemon proto) {
		this.api = api;
		this.encounterKind = EncounterKind.NORMAL;
		this.spawnPointId = proto.getSpawnPointId();
		this.encounterId = proto.getEncounterId();
		this.pokemonId = proto.getPokemonId();
		this.pokemonIdValue = proto.getPokemonIdValue();
		this.expirationTimestampMs = proto.getExpirationTimestampMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
	}


	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api the api
	 * @param proto the proto
	 */
	public CatchablePokemon(PokemonGo api, WildPokemon proto) {
		this.api = api;
		this.encounterKind = EncounterKind.NORMAL;
		this.spawnPointId = proto.getSpawnPointId();
		this.encounterId = proto.getEncounterId();
		this.pokemonId = proto.getPokemonData().getPokemonId();
		this.pokemonIdValue = proto.getPokemonData().getPokemonIdValue();
		this.expirationTimestampMs = proto.getTimeTillHiddenMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
	}

	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api the api
	 * @param proto the proto
	 */
	public CatchablePokemon(PokemonGo api, FortData proto) {
		if (!proto.hasLureInfo()) {
			throw new IllegalArgumentException("Fort does not have lure");
		}
		this.api = api;
		// TODO: does this work?
		// seems that spawnPoint it's fortId in catchAPI so it should be safe to just set it in that way
		this.spawnPointId = proto.getLureInfo().getFortId();
		this.encounterId = proto.getLureInfo().getEncounterId();
		this.pokemonId = proto.getLureInfo().getActivePokemonId();
		this.pokemonIdValue = proto.getLureInfo().getActivePokemonIdValue();
		this.expirationTimestampMs = proto.getLureInfo()
				.getLureExpiresTimestampMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
		this.encounterKind = EncounterKind.DISK;
	}

	/**
	 * Encounters this pokemon
	 *
	 * @param result callback to receive the encounter result
	 */
	public void encounterPokemon(AsyncReturn<EncounterResult> result) {
		if (encounterKind == EncounterKind.NORMAL) {
			encounterNormalPokemon(result);
		} else if (encounterKind == EncounterKind.DISK) {
			encounterDiskPokemon(result);
		} else {
			throw new IllegalStateException("Catchable pokemon missing encounter type");
		}
	}

	/**
	 * Encounters this pokemon
	 *
	 * @param result callback to receive the encounter result
	 */
	private void encounterNormalPokemon(final AsyncReturn<EncounterResult> result) {
		encounterResult = null;
		EncounterMessage message = EncounterMessage
				.newBuilder().setEncounterId(getEncounterId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setSpawnPointId(getSpawnPointId()).build();
		PokemonRequest request = new PokemonRequest(RequestType.ENCOUNTER, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, null)) {
					return;
				}
				try {
					EncounterResponse messageResponse = EncounterResponse.parseFrom(response.getResponseData());
					encountered = messageResponse.getStatus() == EncounterResponse.Status.ENCOUNTER_SUCCESS;
					encounterResult = new NormalEncounterResult(api, messageResponse);
					if (encountered) {
						List<PokemonListener> listeners = api.getListeners(PokemonListener.class);
						for (PokemonListener listener : listeners) {
							listener.onEncounter(api, getEncounterId(), CatchablePokemon.this,
									EncounterType.SPAWN_POINT);
						}
					}
					result.onReceive(encounterResult, null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Encounters this pokemon
	 *
	 * @param result callback to receive the encounter result
	 */
	private void encounterDiskPokemon(final AsyncReturn<EncounterResult> result) {
		encounterResult = null;
		DiskEncounterMessage message = DiskEncounterMessage
				.newBuilder().setEncounterId(getEncounterId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setFortId(getSpawnPointId()).build();
		PokemonRequest request = new PokemonRequest(RequestType.DISK_ENCOUNTER, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, null)) {
					return;
				}
				try {
					DiskEncounterResponse messageResponse = DiskEncounterResponse.parseFrom(response.getResponseData());
					encountered = messageResponse.getResult() == DiskEncounterResponse.Result.SUCCESS;
					encounterResult = new DiskEncounterResult(api, messageResponse);
					if (encountered) {
						List<PokemonListener> listeners = api.getListeners(PokemonListener.class);
						for (PokemonListener listener : listeners) {
							listener.onEncounter(api, getEncounterId(), CatchablePokemon.this,
									EncounterType.DISK);
						}
					}
					result.onReceive(encounterResult, null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Attempts to fully capture this pokemon.
	 *
	 * @param options CatchOptions to catch this with
	 * @param result callback to return the result of this catch
	 * @param maxPokeballs the maximum amount of pokeballs to use before aborting the catch, -1 for no limit
	 * @param maxRazzberries the maximum amount of razzberries to use, -1 for no limit
	 */
	public void capture(final CatchOptions options, final AsyncReturn<CatchResult> result,
						int maxPokeballs, int maxRazzberries) {
		this.captureRecursive(options, result, maxPokeballs, maxRazzberries, 0, 0);
	}

	private void captureRecursive(final CatchOptions options, final AsyncReturn<CatchResult> result,
									final int maxPokeballs, final int maxRazzberries,
									final int pokeballsUsed, final int razzberriesUsed) {
		if (maxPokeballs >= 0 && pokeballsUsed >= maxPokeballs) {
			result.onReceive(new CatchResult(), new RuntimeException("Reached Pokeball limit"));
			return;
		}
		if (maxRazzberries >= 0 && razzberriesUsed >= maxRazzberries) {
			options.useRazzberry(false);
		}
		this.attemptCapture(options, new AsyncReturn<CatchResult>() {
			@Override
			public void onReceive(CatchResult catchResult, Exception exception) {
				if (Utils.callbackException(exception, result, new CatchResult())) {
					return;
				}
				if (catchResult.isFailed()) {
					int newPokeballsUsed = pokeballsUsed + 1;
					int newRazzberriesUsed = razzberriesUsed;
					if (options.isUseRazzberry()) {
						newRazzberriesUsed += 1;
					}
					boolean abort = false;
					List<PokemonListener> listeners = api.getListeners(PokemonListener.class);
					for (PokemonListener listener : listeners) {
						if (listener.onCatchEscape(api, CatchablePokemon.this, pokeballsUsed)) {
							abort |= true;
						}
					}
					if (!abort) {
						captureRecursive(options, result, maxPokeballs, maxRazzberries, newPokeballsUsed, newRazzberriesUsed);
					} else {
						result.onReceive(new CatchResult(), null);
					}
				} else {
					result.onReceive(catchResult, null);
				}
			}
		});
	}

	/**
	 * Attempts a single throw of a Pokeball at this pokemon.
	 *
	 * @param options CatchOptions to catch with
	 * @param result callback to return the result of this capture
	 */
	public void attemptCapture(final CatchOptions options, final AsyncReturn<CatchResult> result) {
		if (encountered && encounterResult != null) {
			double encounterProbability = encounterResult.getCaptureProbability().getCaptureProbability(0);
			List<Pokeball> bagPokeballs = new ArrayList<>();
			ItemBag bag = api.getInventories().getItemBag();
			for (Pokeball pokeball : Pokeball.values()) {
				if (bag.getItem(pokeball.getBallType()).getCount() > 0) {
					bagPokeballs.add(pokeball);
				}
			}
			if (bagPokeballs.size() > 0) {
				try {
					final Pokeball pokeball = options.selectPokeball(bagPokeballs, encounterProbability);
					if (options.isUseRazzberry()) {
						useItem(ItemId.ITEM_RAZZ_BERRY, new AsyncReturn<CatchItemResult>() {
							@Override
							public void onReceive(CatchItemResult itemResult, Exception exception) {
								if (Utils.callbackException(exception, result, new CatchResult())) {
									return;
								}
								if (itemResult.getSuccess()) {
									attemptCapture(
											options.getNormalizedHitPosition(), options.getNormalizedReticleSize(),
											options.getSpinModifier(), pokeball, result);
								}
							}
						});
					} else {
						attemptCapture(options.getNormalizedHitPosition(), options.getNormalizedReticleSize(),
								options.getSpinModifier(), pokeball, result);
					}
				} catch (NoSuchItemException e) {
					result.onReceive(new CatchResult(), e);
				}
			}
		} else {
			result.onReceive(new CatchResult(), new IllegalArgumentException("Pokemon not yet encountered!"));
		}
	}

	private void attemptCapture(double normalizedHitPosition, double normalizedReticleSize, double spinModifier,
								Pokeball pokeball, final AsyncReturn<CatchResult> result) {
		CatchPokemonMessage message = CatchPokemonMessage.newBuilder()
				.setEncounterId(getEncounterId()).setHitPokemon(true)
				.setNormalizedHitPosition(normalizedHitPosition)
				.setNormalizedReticleSize(normalizedReticleSize)
				.setSpawnPointId(getSpawnPointId())
				.setSpinModifier(spinModifier)
				.setPokeball(pokeball.getBallType()).build();
		PokemonRequest request = new PokemonRequest(RequestType.CATCH_POKEMON, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, new CatchResult())) {
					return;
				}
				try {
					CatchPokemonResponse messageResponse = CatchPokemonResponse.parseFrom(response.getResponseData());
					CatchStatus status = messageResponse.getStatus();
					if (status == CatchStatus.CATCH_FLEE || status == CatchStatus.CATCH_SUCCESS) {
						api.getMap().removeCatchable(CatchablePokemon.this);
					} else if (status == CatchStatus.CATCH_ESCAPE) {
						api.getInventories().updateInventories(PokemonCallback.NULL_CALLBACK);
					}
					result.onReceive(new CatchResult(messageResponse), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(new CatchResult(), new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Tries to use an item on a catchable pokemon (ie razzberry).
	 *
	 * @param item the item ID
	 * @param result callback to return CatchItemResult
	 */
	public void useItem(ItemId item, final AsyncReturn<CatchItemResult> result) {
		final UseItemCaptureMessage message = UseItemCaptureMessage
				.newBuilder()
				.setEncounterId(this.getEncounterId())
				.setSpawnPointId(this.getSpawnPointId())
				.setItemId(item)
				.build();

		PokemonRequest request = new PokemonRequest(RequestType.USE_ITEM_CAPTURE, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, null)) {
					return;
				}
				try {
					UseItemCaptureResponse messageResponse = UseItemCaptureResponse.parseFrom(response.getResponseData());
					result.onReceive(new CatchItemResult(messageResponse), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof CatchablePokemon) {
			return this.getEncounterId() == ((CatchablePokemon) obj)
					.getEncounterId();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (int) this.getEncounterId();
	}

	/**
	 * Encounter check
	 *
	 * @return Checks if encounter has happened
	 */
	public boolean isEncountered() {
		if (encountered == null) {
			return false;
		}
		return encountered;
	}

	/**
	 * Return true when the catchable pokemon is a lured pokemon
	 *
	 * @return true for lured pokemon
	 */
	public boolean isLured() {
		return encounterKind == EncounterKind.DISK;
	}

	private enum EncounterKind {
		NORMAL,
		DISK;
	}
}
