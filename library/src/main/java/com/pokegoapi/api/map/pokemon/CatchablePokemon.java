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
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse;
import POGOProtos.Networking.Responses.DiskEncounterResponseOuterClass.DiskEncounterResponse;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import POGOProtos.Networking.Responses.UseItemCaptureResponseOuterClass.UseItemCaptureResponse;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.api.internal.networking.Networking;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.pokemon.encounter.DiskEncounterResult;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.api.map.pokemon.encounter.NormalEncounterResult;
import com.pokegoapi.exceptions.EncounterFailedException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.api.map.MapPoint;
import lombok.Getter;
import lombok.ToString;
import rx.Observable;
import rx.functions.Func1;


/**
 * The type Catchable pokemon.
 */
@ToString
public class CatchablePokemon implements MapPoint {

	private static final String TAG = CatchablePokemon.class.getSimpleName();
	private final Networking networking;
	private final Location location;
	private final Inventories inventories;
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
	 * @param proto the proto
	 */
	public CatchablePokemon(Networking networking, Location location, Inventories inventories, MapPokemon proto) {
		this.networking = networking;
		this.location = location;
		this.inventories = inventories;
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
	 * @param proto the proto
	 */
	public CatchablePokemon(Networking networking, Location location, Inventories inventories, WildPokemon proto) {
		this.networking = networking;
		this.location = location;
		this.inventories = inventories;
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
	 * @param networking Networking, for all actions on pokemon
	 * @param location Current location of the user
	 * @param inventories Inventories are needed to use an item
	 * @param proto the proto
	 */
	public CatchablePokemon(Networking networking, Location location, Inventories inventories, FortData proto) {
		if (!proto.hasLureInfo()) {
			throw new IllegalArgumentException("Fort does not have lure");
		}
		this.networking = networking;
		this.location = location;
		this.inventories = inventories;
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
	 * Encounter pokemon encounter result.
	 *
	 * @return the encounter result
	 */
	public Observable<EncounterResult> encounterPokemon() {
		if (encounterKind == EncounterKind.NORMAL) {
			return encounterNormalPokemon();
		} else if (encounterKind == EncounterKind.DISK) {
			return encounterDiskPokemon();
		}

		throw new IllegalStateException("Catchable pokemon missing encounter type");
	}

	/**
	 * Encounter pokemon encounter result.
	 *
	 * @return the encounter result
	 */
	private Observable<EncounterResult> encounterNormalPokemon() {
		return networking.queueRequest(RequestType.ENCOUNTER, EncounterMessage
				.newBuilder().setEncounterId(getEncounterId())
				.setPlayerLatitude(location.getLatitude())
				.setPlayerLongitude(location.getLongitude())
				.setSpawnPointId(getSpawnPointId()).build(), EncounterResponse.class)
				.map(new Func1<EncounterResponse, EncounterResult>() {
					@Override
					public EncounterResult call(EncounterResponse response) {
						encountered = response.getStatus() == EncounterResponse.Status.ENCOUNTER_SUCCESS;
						return new NormalEncounterResult(response, inventories);
					}
				});
	}

	/**
	 * Encounter pokemon
	 *
	 * @return the encounter result
	 */
	private Observable<EncounterResult> encounterDiskPokemon() {
		return networking.queueRequest(RequestType.DISK_ENCOUNTER,
				DiskEncounterMessage
						.newBuilder().setEncounterId(getEncounterId())
						.setPlayerLatitude(location.getLatitude())
						.setPlayerLongitude(location.getLongitude())
						.setFortId(getSpawnPointId()).build(), DiskEncounterResponse.class)
				.map(new Func1<DiskEncounterResponse, EncounterResult>() {
					@Override
					public EncounterResult call(DiskEncounterResponse response) {
						encountered = response.getResult() == DiskEncounterResponse.Result.SUCCESS;
						return new DiskEncounterResult(response, inventories);
					}
				});
	}

	/**
	 * Tries to catch a pokemon (using defined {@link CatchOptions}).
	 *
	 * @param options the CatchOptions object
	 * @return Observable CatchResult
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 * @throws NoSuchItemException   the no such item exception
	 */
	public Observable<CatchResult> catchPokemon(CatchOptions options) {
		if (options != null) {
			if (options.getUseRazzBerry() != 0) {
				final CatchOptions catchOptions = options;
				final Pokeball pokeball = catchOptions.getItemBall();
				return useItemAsync(ItemId.ITEM_RAZZ_BERRY).flatMap(
						new Func1<CatchItemResult, Observable<CatchResult>>() {
							@Override
							public Observable<CatchResult> call(CatchItemResult result) {
								if (!result.getSuccess()) {
									return Observable.just(new CatchResult());
								}
								return catchPokemon(catchOptions.getNormalizedHitPosition(),
										catchOptions.getNormalizedReticleSize(),
										catchOptions.getSpinModifier(),
										pokeball,
										catchOptions.isHitPokemon());
							}
						});
			}
		} else {
			options = new CatchOptions(inventories);
		}
		return catchPokemon(options.getNormalizedHitPosition(),
				options.getNormalizedReticleSize(),
				options.getSpinModifier(),
				options.getItemBall(),
				options.isHitPokemon());
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball if the capture probability greater than 50%, if you have
	 * none will use greatball etc).
	 *
	 * @param encounter the encounter to compare
	 * @param options   the CatchOptions object
	 * @return the catch result
	 * @throws LoginFailedException     the login failed exception
	 * @throws RemoteServerException    the remote server exception
	 * @throws NoSuchItemException      the no such item exception
	 * @throws EncounterFailedException the encounter failed exception
	 */
	public Observable<CatchResult> catchPokemon(EncounterResult encounter,
												CatchOptions options) {

		if (!encounter.wasSuccessful()) {
			throw new EncounterFailedException();
		}
		double probability = encounter.getCaptureProbability().getCaptureProbability(0);

		if (options != null) {
			if (options.getUseRazzBerry() != 0) {
				final CatchOptions catchOptions = options;
				final Pokeball asyncPokeball = catchOptions.getItemBall(probability);
				return useItemAsync(ItemId.ITEM_RAZZ_BERRY).flatMap(
						new Func1<CatchItemResult, Observable<CatchResult>>() {
							@Override
							public Observable<CatchResult> call(CatchItemResult result) {
								if (!result.getSuccess()) {
									return Observable.just(new CatchResult());
								}
								return catchPokemon(catchOptions.getNormalizedHitPosition(),
										catchOptions.getNormalizedReticleSize(),
										catchOptions.getSpinModifier(),
										asyncPokeball,
										catchOptions.isHitPokemon());
							}
						});
			}
		} else {
			options = new CatchOptions(inventories);
		}
		return catchPokemon(options.getNormalizedHitPosition(),
				options.getNormalizedReticleSize(),
				options.getSpinModifier(),
				options.getItemBall(probability),
				options.isHitPokemon());
	}

	/**
	 * Tries to catch a pokemon.
	 *
	 * @param normalizedHitPosition the normalized hit position
	 * @param normalizedReticleSize the normalized hit reticle
	 * @param spinModifier          the spin modifier
	 * @param type                  Type of pokeball to throw
	 * @return CatchResult of resulted try to catch pokemon
	 */
	public Observable<CatchResult> catchPokemon(
			double normalizedHitPosition, double normalizedReticleSize, double spinModifier, Pokeball type, boolean hitPokemon) {
		if (!isEncountered()) {
			return Observable.just(new CatchResult());
		}

		CatchPokemonMessage reqMsg = CatchPokemonMessage.newBuilder()
				.setEncounterId(getEncounterId()).setHitPokemon(true)
				.setNormalizedHitPosition(normalizedHitPosition)
				.setNormalizedReticleSize(normalizedReticleSize)
				.setSpawnPointId(getSpawnPointId())
				.setSpinModifier(spinModifier)
				.setHitPokemon(hitPokemon)
				.setPokeball(type.getBallType())
				.build();
		return catchPokemon(reqMsg);
	}

	private Observable<CatchResult> catchPokemon(CatchPokemonMessage catchPokemonMessage) {
		return networking.queueRequest(RequestType.CATCH_POKEMON, catchPokemonMessage, CatchPokemonResponse.class)
				.map(new Func1<CatchPokemonResponse, CatchResult>() {
					@Override
					public CatchResult call(CatchPokemonResponse response) {
						return new CatchResult(response);
					}
				});
	}

	/**
	 * Tries to use an item on a catchable pokemon (ie razzberry).
	 *
	 * @param item the item ID
	 * @return CatchItemResult info about the new modifiers about the pokemon (can move, item capture multi) eg
	 */
	public Observable<CatchItemResult> useItemAsync(ItemId item) {
		return networking.queueRequest(RequestType.USE_ITEM_CAPTURE,
		UseItemCaptureMessage
				.newBuilder()
				.setEncounterId(this.getEncounterId())
				.setSpawnPointId(this.getSpawnPointId())
				.setItemId(item)
				.build(), UseItemCaptureResponse.class).map(new Func1<UseItemCaptureResponse, CatchItemResult>() {
			@Override
			public CatchItemResult call(UseItemCaptureResponse response) {
				return new CatchItemResult(response);
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

	private enum EncounterKind {
		NORMAL,
		DISK;
	}

	public CatchOptions newCatchOptions() {
		return new CatchOptions(inventories);
	}
}
