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
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus;
import POGOProtos.Networking.Responses.DiskEncounterResponseOuterClass.DiskEncounterResponse;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import POGOProtos.Networking.Responses.UseItemCaptureResponseOuterClass.UseItemCaptureResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.pokemon.encounter.DiskEncounterResult;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.api.map.pokemon.encounter.NormalEncounterResult;
import com.pokegoapi.exceptions.AsyncLoginFailedException;
import com.pokegoapi.exceptions.AsyncRemoteServerException;
import com.pokegoapi.exceptions.EncounterFailedException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.util.AsyncHelper;
import com.pokegoapi.util.Log;
import com.pokegoapi.util.MapPoint;
import lombok.Getter;
import lombok.ToString;
import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;

import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_GREAT_BALL;
import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_MASTER_BALL;
import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_POKE_BALL;
import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_ULTRA_BALL;
import static com.pokegoapi.api.inventory.Pokeball.GREATBALL;
import static com.pokegoapi.api.inventory.Pokeball.MASTERBALL;
import static com.pokegoapi.api.inventory.Pokeball.POKEBALL;
import static com.pokegoapi.api.inventory.Pokeball.ULTRABALL;


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
		this.expirationTimestampMs = proto.getTimeTillHiddenMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
	}

	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api   the api
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
		this.expirationTimestampMs = proto.getLureInfo()
				.getLureExpiresTimestampMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
		this.encounterKind = EncounterKind.DISK;
	}

	/**
	 * Encounter pokemon
	 *
	 * @return the encounter result
	 */
	public EncounterResult encounterPokemon() throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(encounterPokemonAsync());
	}

	/**
	 * Encounter pokemon encounter result.
	 *
	 * @return the encounter result
	 */
	public Observable<EncounterResult> encounterPokemonAsync() {
		if (encounterKind == EncounterKind.NORMAL) {
			return encounterNormalPokemonAsync();
		} else if (encounterKind == EncounterKind.DISK) {
			return encounterDiskPokemonAsync();
		}

		throw new IllegalStateException("Catchable pokemon missing encounter type");
	}

	/**
	 * Encounter pokemon encounter result.
	 *
	 * @return the encounter result
	 */
	public Observable<EncounterResult> encounterNormalPokemonAsync() {
		EncounterMessage reqMsg = EncounterMessage
				.newBuilder().setEncounterId(getEncounterId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setSpawnPointId(getSpawnPointId()).build();
		AsyncServerRequest serverRequest = new AsyncServerRequest(
				RequestType.ENCOUNTER, reqMsg);
		return api.getRequestHandler()
				.sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, EncounterResult>() {
					@Override
					public EncounterResult call(ByteString result) {
						EncounterResponse response;
						try {
							response = EncounterResponse
									.parseFrom(result);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}
						encountered = response.getStatus() == EncounterResponse.Status.ENCOUNTER_SUCCESS;
						return new NormalEncounterResult(api, response);
					}
				});
	}

	/**
	 * Encounter pokemon encounter result.
	 *
	 * @return the encounter result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public EncounterResult encounterNormalPokemon() throws LoginFailedException,
			RemoteServerException {
		return AsyncHelper.toBlocking(encounterNormalPokemonAsync());
	}

	/**
	 * Encounter pokemon
	 *
	 * @return the encounter result
	 */
	public Observable<EncounterResult> encounterDiskPokemonAsync() {
		DiskEncounterMessage reqMsg = DiskEncounterMessage
				.newBuilder().setEncounterId(getEncounterId())
				.setPlayerLatitude(api.getLatitude())
				.setPlayerLongitude(api.getLongitude())
				.setFortId(getSpawnPointId()).build();
		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.DISK_ENCOUNTER, reqMsg);
		return api.getRequestHandler()
				.sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, EncounterResult>() {
					@Override
					public EncounterResult call(ByteString result) {
						DiskEncounterResponse response;
						try {
							response = DiskEncounterResponse.parseFrom(result);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}
						encountered = response.getResult() == DiskEncounterResponse.Result.SUCCESS;
						return new DiskEncounterResult(api, response);
					}
				});
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball, if you have
	 * none will use greatball etc) and uwill use a single razz berry if available.
	 *
	 * @return CatchResult
	 */
	public Observable<CatchResult> catchPokemonWithRazzBerryAsync()
			throws LoginFailedException, RemoteServerException, NoSuchItemException {
		final Pokeball pokeball = getItemBall();
		return useItemAsync(ItemId.ITEM_RAZZ_BERRY).flatMap(new Func1<CatchItemResult, Observable<CatchResult>>() {
			@Override
			public Observable<CatchResult> call(CatchItemResult result) {
				if (!result.getSuccess()) {
					return Observable.just(new CatchResult());
				}
				return catchPokemonAsync(pokeball);
			}
		});
	}

	/**
	 * Gets item ball to catch a pokemon
	 *
	 * @return the item ball
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 * @throws NoSuchItemException   the no such item exception
	 */
	public Pokeball getItemBall() throws LoginFailedException,
			RemoteServerException, NoSuchItemException {
		ItemBag bag = api.getInventories().getItemBag();
		if (bag.getItem(ITEM_POKE_BALL).getCount() > 0) {
			return POKEBALL;
		} else if (bag.getItem(ITEM_GREAT_BALL).getCount() > 0) {
			return GREATBALL;
		} else if (bag.getItem(ITEM_ULTRA_BALL).getCount() > 0) {
			return ULTRABALL;
		} else if (bag.getItem(ITEM_MASTER_BALL).getCount() > 0) {
			return MASTERBALL;
		} else {
			throw new NoSuchItemException();
		}
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball, if you have
	 * none will use greatball etc) and uwill use a single razz berry if available.
	 *
	 * @return CatchResult
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemonWithRazzBerry() throws LoginFailedException,
			RemoteServerException, NoSuchItemException {
		Pokeball pokeball = getItemBall();

		useItem(ItemId.ITEM_RAZZ_BERRY);
		return catchPokemon(pokeball, -1, -1);
	}
	
	/**
	 * Tries to catch a pokemon with the given type of pokeball.
	 *
	 * @param pokeball Type of pokeball
	 * @return CatchResult
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemonWithRazzBerry(Pokeball pokeball) throws LoginFailedException, RemoteServerException {
		useItem(ItemId.ITEM_RAZZ_BERRY);
		return catchPokemon(pokeball, -1, -1);
	}

	/**
	 * Tries to catch a pokemon with you best pokeball first
	 * (start by the masterball if you have none then use the ultraball etc.)
	 *
	 * @return CatchResult catch result
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 * @throws NoSuchItemException   the no such item exception
	 */
	public CatchResult catchPokemonWithBestBall() throws LoginFailedException,
			RemoteServerException, NoSuchItemException {
		return catchPokemonWithBestBall(false, -1);
	}

	/**
	 * Tries to catch a pokemon with you best pokeball first
	 * (start by the masterball if you have none then use the ultraball etc.)
	 *
	 * @param noMasterBall the no master ball
	 * @return CatchResult catch result
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 * @throws NoSuchItemException   the no such item exception
	 */
	public CatchResult catchPokemonWithBestBall(boolean noMasterBall) throws LoginFailedException,
			RemoteServerException, NoSuchItemException {
		return catchPokemonWithBestBall(noMasterBall, -1);
	}

	/**
	 * Tries to catch a pokemon with you best pokeball first
	 * (start by the masterball if you have none then use the ultraball etc.)
	 *
	 * @param noMasterBall the no master ball
	 * @param amount       the amount
	 * @return CatchResult catch result
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 * @throws NoSuchItemException   the no such item exception
	 */
	public CatchResult catchPokemonWithBestBall(boolean noMasterBall, int amount) throws LoginFailedException,
			RemoteServerException, NoSuchItemException {
		return catchPokemonWithBestBall(noMasterBall, amount, -1);
	}

	/**
	 * Tries to catch a pokemon with you best pokeball first
	 * (start by the masterball if you have none then use the ultraball etc.)
	 *
	 * @param noMasterBall  the no master ball
	 * @param amount        the amount
	 * @param razberryLimit the razberry limit
	 * @return CatchResult catch result
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 * @throws NoSuchItemException   the no such item exception
	 */
	public CatchResult catchPokemonWithBestBall(boolean noMasterBall, int amount, int razberryLimit)
			throws LoginFailedException, RemoteServerException, NoSuchItemException {
		ItemBag bag = api.getInventories().getItemBag();
		Pokeball pokeball;
		if (bag.getItem(ITEM_MASTER_BALL).getCount() > 0 && !noMasterBall) {
			pokeball = MASTERBALL;
		} else if (bag.getItem(ITEM_ULTRA_BALL).getCount() > 0) {
			pokeball = ULTRABALL;
		} else if (bag.getItem(ITEM_GREAT_BALL).getCount() > 0) {
			pokeball = GREATBALL;
		} else if (bag.getItem(ITEM_POKE_BALL).getCount() > 0) {
			pokeball = POKEBALL;
		} else {
			throw new NoSuchItemException();
		}
		return catchPokemon(pokeball, amount, razberryLimit);
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball if the capture probability greater than 50%, if you have
	 * none will use greatball etc).
	 *
	 * @return the catch result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 * @throws NoSuchItemException   the no such item exception
	 */
	public CatchResult catchPokemonBestBallToUse()
			throws LoginFailedException, RemoteServerException, NoSuchItemException,
			EncounterFailedException {
		EncounterResult encounter = encounterPokemon();
		if (!encounter.wasSuccessful()) throw new EncounterFailedException();

		return catchPokemonBestBallToUse(encounter, new ArrayList<ItemId>());
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball if the capture probability greater than 50%, if you have
	 * none will use greatball etc).
	 *
	 * @param encounter the encounter
	 * @return the catch result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 * @throws NoSuchItemException   the no such item exception
	 */
	public CatchResult catchPokemonBestBallToUse(EncounterResult encounter)
			throws LoginFailedException, RemoteServerException, NoSuchItemException {

		return catchPokemonBestBallToUse(encounter, new ArrayList<ItemId>(), -1);
	}


	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball if the capture probability greater than 50%, if you have
	 * none will use greatball etc).
	 *
	 * @param encounter the encounter
	 * @param amount    the amount
	 * @return the catch result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 * @throws NoSuchItemException   the no such item exception
	 */
	public CatchResult catchPokemonBestBallToUse(EncounterResult encounter, int amount)
			throws LoginFailedException, RemoteServerException, NoSuchItemException {

		return catchPokemonBestBallToUse(encounter, new ArrayList<ItemId>(), amount);
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball if the capture probability greater than 50%, if you have
	 * none will use greatball etc).
	 *
	 * @param encounter the encounter
	 * @param notUse    the not use
	 * @return the catch result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 * @throws NoSuchItemException   the no such item exception
	 */
	public CatchResult catchPokemonBestBallToUse(EncounterResult encounter, List<ItemId> notUse)
			throws LoginFailedException, RemoteServerException, NoSuchItemException {
		return catchPokemonBestBallToUse(encounter, notUse, -1, -1);
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball if the capture probability greater than 50%, if you have
	 * none will use greatball etc).
	 *
	 * @param encounter the encounter
	 * @param notUse    the not use
	 * @param amount    the amount
	 * @return the catch result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 * @throws NoSuchItemException   the no such item exception
	 */
	public CatchResult catchPokemonBestBallToUse(EncounterResult encounter, List<ItemId> notUse, int amount)
			throws LoginFailedException, RemoteServerException, NoSuchItemException {
		return catchPokemonBestBallToUse(encounter, notUse, amount, -1);
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball if the capture probability greater than 50%, if you have
	 * none will use greatball etc).
	 *
	 * @param encounter     the encounter
	 * @param notUse        the not use
	 * @param amount        the amount
	 * @param razberryLimit the razberry limit
	 * @return the catch result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 * @throws NoSuchItemException   the no such item exception
	 */
	public CatchResult catchPokemonBestBallToUse(
			EncounterResult encounter, List<ItemId> notUse, int amount, int razberryLimit)
			throws LoginFailedException, RemoteServerException, NoSuchItemException {
		int razberries = 0;
		int numThrows = 0;
		CatchResult result;
		do {

			if (razberries < razberryLimit || razberryLimit == -1) {
				useItem(ItemId.ITEM_RAZZ_BERRY);
				razberries++;
			}
			result = AsyncHelper.toBlocking(catchPokemonBestBallToUseAsync(encounter, notUse, 1.0,
					1.95 + Math.random() * 0.05, 0.85 + Math.random() * 0.15));
			if (result == null) {
				Log.wtf(TAG, "Got a null result after catch attempt");
				break;
			}
			// continue for the following cases:
			// CatchStatus.CATCH_ESCAPE
			// CatchStatus.CATCH_MISSED
			// covers all cases

			// if its caught of has fleed, end the loop
			// FLEE OR SUCCESS
			if (result.getStatus() == CatchStatus.CATCH_FLEE
					|| result.getStatus() == CatchStatus.CATCH_SUCCESS) {
				Log.v(TAG, "Pokemon caught/or flee");
				break;
			}
			// if error or unrecognized end the loop
			// ERROR OR UNRECOGNIZED
			if (result.getStatus() == CatchStatus.CATCH_ERROR
					|| result.getStatus() == CatchStatus.UNRECOGNIZED) {
				Log.wtf(TAG, "Got an error or unrecognized catch attempt");
				Log.wtf(TAG, "Proto:" + result);
				break;
			}
			numThrows++;
		}
		while (amount < 0 || numThrows < amount);

		return result;
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball if the capture probability greater than 50%, if you have
	 * none will use greatball etc).
	 *
	 * @param encounter             the encounter
	 * @param notUse                the not use
	 * @param normalizedHitPosition the normalized hit position
	 * @param normalizedReticleSize the normalized hit reticle
	 * @param spinModifier          the spin modifier
	 * @return CatchResult of resulted try to catch pokemon
	 */
	public Observable<CatchResult> catchPokemonBestBallToUseAsync(
			EncounterResult encounter, List<ItemId> notUse, double normalizedHitPosition,
			double normalizedReticleSize, double spinModifier)
			throws NoSuchItemException, LoginFailedException, RemoteServerException {
		if (!isEncountered()) {
			return Observable.just(new CatchResult());
		}

		CatchPokemonMessage reqMsg = CatchPokemonMessage.newBuilder()
				.setEncounterId(getEncounterId()).setHitPokemon(true)
				.setNormalizedHitPosition(normalizedHitPosition)
				.setNormalizedReticleSize(normalizedReticleSize)
				.setSpawnPointId(getSpawnPointId())
				.setSpinModifier(spinModifier)
				.setPokeball(getBestBallToUse(encounter, notUse).getBallType()).build();
		AsyncServerRequest serverRequest = new AsyncServerRequest(
				RequestType.CATCH_POKEMON, reqMsg);
		return catchPokemonAsync(serverRequest);
	}


	private Pokeball getBestBallToUse(EncounterResult encounter, List<ItemId> notUse)
			throws LoginFailedException, RemoteServerException, NoSuchItemException {
		ItemBag bag = api.getInventories().getItemBag();
		Pokeball pokeball;
		if (!notUse.contains(ITEM_POKE_BALL)
				&& bag.getItem(ITEM_POKE_BALL).getCount() > 0
				&& (encounter.getCaptureProbability().getCaptureProbability(0) >= 0.50
				|| ((notUse.contains(ITEM_GREAT_BALL) || bag.getItem(ITEM_GREAT_BALL).getCount() <= 0)
				&& (notUse.contains(ITEM_ULTRA_BALL) || bag.getItem(ITEM_ULTRA_BALL).getCount() <= 0)))) {
			pokeball = POKEBALL;
		} else if (!notUse.contains(ITEM_GREAT_BALL) && bag.getItem(ITEM_GREAT_BALL).getCount() > 0
				&& (encounter.getCaptureProbability().getCaptureProbability(1) >= 0.50
				|| notUse.contains(ITEM_ULTRA_BALL)
				|| (!notUse.contains(ITEM_ULTRA_BALL)
				&& bag.getItem(ITEM_ULTRA_BALL).getCount() <= 0))) {
			pokeball = GREATBALL;
		} else if (!notUse.contains(ITEM_ULTRA_BALL) && bag.getItem(ITEM_ULTRA_BALL).getCount() > 0) {
			pokeball = ULTRABALL;
		} else {
			//master ball in the moment not exist
			throw new NoSuchItemException();
		}
		return pokeball;
	}

	/**
	 * Tries to catch a pokemon (will attempt to use a pokeball, if you have
	 * none will use greatball etc).
	 *
	 * @return CatchResult
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemon() throws LoginFailedException,
			RemoteServerException, NoSuchItemException {

		return catchPokemon(getItemBall());
	}

	/**
	 * Tries to catch a pokemon with the given type of pokeball.
	 *
	 * @param pokeball Type of pokeball
	 * @return CatchResult
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemon(Pokeball pokeball)
			throws LoginFailedException, RemoteServerException {
		return catchPokemon(pokeball, -1);
	}

	/**
	 * Tried to catch a pokemon with given pokeball and max number of pokeballs.
	 *
	 * @param pokeball Type of pokeball
	 * @param amount   Max number of pokeballs to use
	 * @return CatchResult
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemon(Pokeball pokeball, int amount)
			throws LoginFailedException, RemoteServerException {
		return catchPokemon(1.0, 1.95 + Math.random() * 0.05,
				0.85 + Math.random() * 0.15, pokeball, amount);
	}

	/**
	 * Tried to catch a pokemon with given pokeball and max number of pokeballs.
	 *
	 * @param pokeball      Type of pokeball
	 * @param amount        Max number of pokeballs to use
	 * @param razberryLimit Max number of razberrys to use
	 * @return CatchResult
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemon(Pokeball pokeball, int amount, int razberryLimit)
			throws LoginFailedException, RemoteServerException {
		return catchPokemon(1.0, 1.95 + Math.random() * 0.05,
				0.85 + Math.random() * 0.15, pokeball, amount, razberryLimit);
	}

	/**
	 * Tries to catch a pokemon.
	 *
	 * @param normalizedHitPosition the normalized hit position
	 * @param normalizedReticleSize the normalized hit reticle
	 * @param spinModifier          the spin modifier
	 * @param type                  Type of pokeball to throw
	 * @param amount                Max number of Pokeballs to throw, negative number for
	 *                              unlimited
	 * @return CatchResult of resulted try to catch pokemon
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemon(double normalizedHitPosition,
									double normalizedReticleSize, double spinModifier, Pokeball type,
									int amount) throws LoginFailedException, RemoteServerException {

		return catchPokemon(normalizedHitPosition, normalizedReticleSize, spinModifier, type, amount, 0);
	}

	/**
	 * Tries to catch a pokemon.
	 *
	 * @param normalizedHitPosition the normalized hit position
	 * @param normalizedReticleSize the normalized hit reticle
	 * @param spinModifier          the spin modifier
	 * @param type                  Type of pokeball to throw
	 * @param amount                Max number of Pokeballs to throw, negative number for
	 *                              unlimited
	 * @param razberriesLimit       The maximum amount of razberries to use, -1 for unlimited
	 * @return CatchResult of resulted try to catch pokemon
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchResult catchPokemon(double normalizedHitPosition,
									double normalizedReticleSize, double spinModifier, Pokeball type,
									int amount, int razberriesLimit)
			throws LoginFailedException, RemoteServerException {
		int razberries = 0;
		int numThrows = 0;
		CatchResult result;
		do {

			if (razberries < razberriesLimit || razberriesLimit == -1) {
				useItem(ItemId.ITEM_RAZZ_BERRY);
				razberries++;
			}
			result = AsyncHelper.toBlocking(catchPokemonAsync(normalizedHitPosition,
					normalizedReticleSize, spinModifier, type));
			if (result == null) {
				Log.wtf(TAG, "Got a null result after catch attempt");
				break;
			}

			// continue for the following cases:
			// CatchStatus.CATCH_ESCAPE
			// CatchStatus.CATCH_MISSED
			// covers all cases

			// if its caught of has fleed, end the loop
			// FLEE OR SUCCESS
			if (result.getStatus() == CatchStatus.CATCH_FLEE
					|| result.getStatus() == CatchStatus.CATCH_SUCCESS) {
				Log.v(TAG, "Pokemon caught/or flee");
				break;
			}
			// if error or unrecognized end the loop
			// ERROR OR UNRECOGNIZED
			if (result.getStatus() == CatchStatus.CATCH_ERROR
					|| result.getStatus() == CatchStatus.UNRECOGNIZED) {
				Log.wtf(TAG, "Got an error or unrecognized catch attempt");
				Log.wtf(TAG, "Proto:" + result);
				break;
			}

			numThrows++;
		}
		while (amount < 0 || numThrows < amount);

		return result;
	}

	/**
	 * Tries to catch a pokemon.
	 *
	 * @param type Type of pokeball to throw
	 * @return CatchResult of resulted try to catch pokemon
	 */
	public Observable<CatchResult> catchPokemonAsync(Pokeball type) {
		return catchPokemonAsync(1.0, 1.95 + Math.random() * 0.05,
				0.85 + Math.random() * 0.15, type);
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
	public Observable<CatchResult> catchPokemonAsync(
			double normalizedHitPosition, double normalizedReticleSize, double spinModifier, Pokeball type) {
		if (!isEncountered()) {
			return Observable.just(new CatchResult());
		}

		CatchPokemonMessage reqMsg = CatchPokemonMessage.newBuilder()
				.setEncounterId(getEncounterId()).setHitPokemon(true)
				.setNormalizedHitPosition(normalizedHitPosition)
				.setNormalizedReticleSize(normalizedReticleSize)
				.setSpawnPointId(getSpawnPointId())
				.setSpinModifier(spinModifier)
				.setPokeball(type.getBallType()).build();
		AsyncServerRequest serverRequest = new AsyncServerRequest(
				RequestType.CATCH_POKEMON, reqMsg);
		return catchPokemonAsync(serverRequest);
	}

	private Observable<CatchResult> catchPokemonAsync(AsyncServerRequest serverRequest) {
		final CatchablePokemon instance = this;
		return api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, CatchResult>() {
			@Override
			public CatchResult call(ByteString result) {
				CatchPokemonResponse response;

				try {
					response = CatchPokemonResponse.parseFrom(result);
				} catch (InvalidProtocolBufferException e) {
					throw new AsyncRemoteServerException(e);
				}
				try {

					// pokemon is caught of flees
					if (response.getStatus() == CatchStatus.CATCH_FLEE
							|| response.getStatus() == CatchStatus.CATCH_SUCCESS) {
						api.getMap().removeCatchable(instance);
					}

					// escapes
					if (response.getStatus() == CatchStatus.CATCH_ESCAPE) {
						api.getInventories().updateInventories();
					}
					CatchResult res = new CatchResult(response);
					return res;
				} catch (RemoteServerException e) {
					throw new AsyncRemoteServerException(e);
				} catch (LoginFailedException e) {
					throw new AsyncLoginFailedException(e);
				}
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

		UseItemCaptureMessage reqMsg = UseItemCaptureMessage
				.newBuilder()
				.setEncounterId(this.getEncounterId())
				.setSpawnPointId(this.getSpawnPointId())
				.setItemId(item)
				.build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(
				RequestType.USE_ITEM_CAPTURE, reqMsg);
		return api.getRequestHandler()
				.sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, CatchItemResult>() {
					@Override
					public CatchItemResult call(ByteString result) {
						UseItemCaptureResponse response;
						try {
							response = UseItemCaptureResponse.parseFrom(result);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}
						return new CatchItemResult(response);
					}
				});
	}

	/**
	 * Tries to use an item on a catchable pokemon (ie razzberry).
	 *
	 * @param item the item ID
	 * @return CatchItemResult info about the new modifiers about the pokemon (can move, item capture multi) eg
	 * @throws LoginFailedException  if failed to login
	 * @throws RemoteServerException if the server failed to respond
	 */
	public CatchItemResult useItem(ItemId item) throws LoginFailedException, RemoteServerException {
		return AsyncHelper.toBlocking(useItemAsync(item));
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
}
