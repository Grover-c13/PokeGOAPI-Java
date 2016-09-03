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
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass;
import POGOProtos.Networking.Responses.DiskEncounterResponseOuterClass;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import POGOProtos.Networking.Responses.UseItemCaptureResponseOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.encounter.DiskEncounterResult;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.api.map.pokemon.encounter.NormalEncounterResult;
import com.pokegoapi.api.settings.AsyncCatchOptions;
import com.pokegoapi.exceptions.EncounterFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.MapPoint;
import com.pokegoapi.util.PokeAFunc;
import com.pokegoapi.util.PokeCallback;

import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.ThreadLocalRandom;


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

	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api   the api
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
	 * @param api   the api
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
	 * @param api   the api
	 * @param proto the proto
	 *
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
		this.expirationTimestampMs = proto.getLureInfo().getLureExpiresTimestampMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
		this.encounterKind = EncounterKind.DISK;
	}


	/**
	 * Encounter pokemon encounter result.
	 *
	 * @param callback an optional callback to handle results
	 *
	 * @return callback passed as argument
	 */
	public PokeCallback<EncounterResult> encounterPokemon(PokeCallback<EncounterResult> callback) {
		if (encounterKind == EncounterKind.NORMAL) {
			return encounterNormalPokemon(callback);
		} else if (encounterKind == EncounterKind.DISK) {
			return encounterDiskPokemon(callback);
		}

		throw new IllegalStateException("Catchable pokemon missing encounter type");
	}

	private PokeCallback<EncounterResult> encounterNormalPokemon(PokeCallback<EncounterResult> callback) {
		EncounterMessage reqMsg = EncounterMessage.newBuilder().setEncounterId(getEncounterId()).setPlayerLatitude(
				api.getLatitude()).setPlayerLongitude(api.getLongitude()).setSpawnPointId(getSpawnPointId()).build();
		new AsyncServerRequest(RequestType.ENCOUNTER, reqMsg,
				new PokeAFunc<EncounterResponse, NormalEncounterResult>() {
					@Override
					public NormalEncounterResult exec(EncounterResponse response) {
						encountered = response.getStatus() == EncounterResponse.Status.ENCOUNTER_SUCCESS;
						return new NormalEncounterResult(api, response);
					}
				}, callback, api);
		return callback;
	}

	private PokeCallback<EncounterResult> encounterDiskPokemon(PokeCallback<EncounterResult> callback) {
		DiskEncounterMessage reqMsg = DiskEncounterMessage.newBuilder().setEncounterId(
				getEncounterId()).setPlayerLatitude(api.getLatitude()).setPlayerLongitude(api.getLongitude()).setFortId(
				getSpawnPointId()).build();
		new AsyncServerRequest(RequestType.DISK_ENCOUNTER, reqMsg,
				new PokeAFunc<DiskEncounterResponseOuterClass.DiskEncounterResponse, DiskEncounterResult>() {
					@Override
					public DiskEncounterResult exec(DiskEncounterResponseOuterClass.DiskEncounterResponse response) {
						encountered = response.getResult() == DiskEncounterResponseOuterClass.DiskEncounterResponse.Result.SUCCESS;
						return new DiskEncounterResult(api, response);
					}
				}, callback, api);
		return callback;
	}

	/**
	 * Catch a pokemon with standard options
	 *
	 * @param callback an optional callback to handle result
	 *
	 * @return callback passed as argument
	 */
	public PokeCallback<CatchResult> catchPokemon(final PokeCallback<CatchResult> callback) {
		return catchPokemon(new AsyncCatchOptions(api), callback);
	}

	/**
	 * /**
	 * Tries to catch a pokemon (using defined {@link AsyncCatchOptions}).
	 *
	 * @param options  the AsyncCatchOptions object
	 * @param callback an optional callback to handle results
	 *
	 * @return callback passed as argument
	 */
	public PokeCallback<CatchResult> catchPokemon(AsyncCatchOptions options, final PokeCallback<CatchResult> callback) {
		if (options == null) options = new AsyncCatchOptions(api);

		return catchPokemon(options, 1, callback);
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball if the capture probability greater than 50%, if you have
	 * none will use greatball etc).
	 *
	 * @param encounter the encounter to compare
	 * @param options   the CatchOptions object
	 * @param callback  an optional callback to handle results
	 *
	 * @return callback passed as argument
	 */
	public PokeCallback<CatchResult> catchPokemon(EncounterResult encounter, AsyncCatchOptions options,
			final PokeCallback<CatchResult> callback) {

		if (!encounter.wasSuccessful()) {
			callback.fire(new EncounterFailedException());
			return callback;
		}

		if (options == null) {
			options = new AsyncCatchOptions(api);
		}

		options.checkProbability(encounter.getCaptureProbability().getCaptureProbability(0));
		return catchPokemon(options, callback);
	}

	private PokeCallback<CatchResult> catchPokemon(final AsyncCatchOptions options, final int trialNum,
			final PokeCallback<CatchResult> callback) {
		if (!isEncountered()) {
			callback.fire(new EncounterFailedException());
			return callback;
		}
		//we have to do a retry
		if (options.getRazzBerries() >= trialNum && api.getInventories().getItemBag().getItem(
				ItemId.ITEM_RAZZ_BERRY).getCount() > 0) useBerry(options, trialNum, callback);
		else catchPokemonPostBerry(options, trialNum, callback);
		//our last try
		return callback;
	}

	private PokeCallback<CatchResult> useBerry(final AsyncCatchOptions options, final int trialNum,
			final PokeCallback<CatchResult> callback) {
		Log.d("useBerry", "trialNum:" + trialNum + " max:" + options.getRazzBerries());
		useItem(ItemId.ITEM_RAZZ_BERRY, new PokeCallback<CatchItemResult>() {
			@Override
			public void onResponse(CatchItemResult result) {
				if (!result.getSuccess()) {
					return;
				}
				catchPokemonPostBerry(options, trialNum, callback);
			}
		});
		return callback;
	}


	private PokeCallback<CatchResult> catchPokemonPostBerry(final AsyncCatchOptions options, final int trialNum,
			final PokeCallback<CatchResult> callback) {
		if (!isEncountered()) {
			callback.fire(new EncounterFailedException());
			return callback;
		}
		Log.d("catchPokemonPostBerry", "trialNum:" + trialNum + " max:" + options.getNumThrows());

		PokeCallback<CatchResult> wrappedCallback = callback;

		if (options.getNumThrows() > trialNum) {

			wrappedCallback = new PokeCallback<CatchResult>() {
				@Override
				public void onError(Throwable throwable) {
					//fire the original callback error
					callback.fire(throwable);
				}

				@Override
				public void onResponse(CatchResult result) {
					if (result.getStatus() == CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus.CATCH_FLEE
							|| result.getStatus() == CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus.CATCH_SUCCESS
							|| result.getStatus() == CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus.CATCH_ERROR
							|| result.getStatus() == CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus.UNRECOGNIZED) {
						//we can't retry, so we fire the original callback
						callback.fire(result);
						return;
					}
					//we can retry, but to prevent flagging it's necessary to sleep for a while between each retry
					try {
						Thread.sleep(ThreadLocalRandom.current().nextInt(1352, 1976));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					catchPokemon(options, trialNum + 1, callback);
				}
			};
		}

		try {
			CatchPokemonMessage reqMsg = CatchPokemonMessage.newBuilder()
					.setEncounterId(getEncounterId())
					.setHitPokemon(true).setNormalizedHitPosition(options.getNormalizedHitPosition())
					.setNormalizedReticleSize(options.getNormalizedReticleSize())
					.setSpawnPointId(getSpawnPointId())
					.setSpinModifier(options.getSpinModifier())
					.setPokeball(options.getItemBall().getBallType())
					.build();

			new AsyncServerRequest(RequestType.CATCH_POKEMON, reqMsg,
					new PokeAFunc<CatchPokemonResponseOuterClass.CatchPokemonResponse, CatchResult>() {
						@Override
						public CatchResult exec(CatchPokemonResponseOuterClass.CatchPokemonResponse response) {
							return new CatchResult(response);
						}
					}, wrappedCallback, api);
		} catch (NoSuchItemException e) {
			callback.fire(e);
		}

		return callback;
	}


	/**
	 * Tries to use an item on a catchable pokemon (ie razzberry).
	 *
	 * @param item     the item ID
	 * @param callback an optional callback to handle results
	 * @return callback passed as argument
	 */
	public PokeCallback<CatchItemResult> useItem(ItemId item, PokeCallback<CatchItemResult> callback) {
		UseItemCaptureMessage reqMsg = UseItemCaptureMessage.newBuilder().setEncounterId(
				this.getEncounterId()).setSpawnPointId(this.getSpawnPointId()).setItemId(item).build();

		new AsyncServerRequest(RequestType.USE_ITEM_CAPTURE, reqMsg,
				new PokeAFunc<UseItemCaptureResponseOuterClass.UseItemCaptureResponse, CatchItemResult>() {
					@Override
					public CatchItemResult exec(UseItemCaptureResponseOuterClass.UseItemCaptureResponse response) {
						return new CatchItemResult(response);
					}
				}, callback, api);
		return callback;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof CatchablePokemon) {
			return this.getEncounterId() == ((CatchablePokemon) obj).getEncounterId();
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
		DISK
	}
}
