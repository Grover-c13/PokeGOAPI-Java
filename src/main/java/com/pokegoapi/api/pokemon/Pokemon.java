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

package com.pokegoapi.api.pokemon;

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Enums.PokemonFamilyIdOuterClass.PokemonFamilyId;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Enums.PokemonMoveOuterClass;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.EvolvePokemonMessageOuterClass.EvolvePokemonMessage;
import POGOProtos.Networking.Requests.Messages.NicknamePokemonMessageOuterClass.NicknamePokemonMessage;
import POGOProtos.Networking.Requests.Messages.ReleasePokemonMessageOuterClass.ReleasePokemonMessage;
import POGOProtos.Networking.Requests.Messages.SetFavoritePokemonMessageOuterClass.SetFavoritePokemonMessage;
import POGOProtos.Networking.Requests.Messages.UpgradePokemonMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.UpgradePokemonMessageOuterClass.UpgradePokemonMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass.EvolvePokemonResponse;
import POGOProtos.Networking.Responses.NicknamePokemonResponseOuterClass.NicknamePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result;
import POGOProtos.Networking.Responses.SetFavoritePokemonResponseOuterClass.SetFavoritePokemonResponse;
import POGOProtos.Networking.Responses.UpgradePokemonResponseOuterClass;
import POGOProtos.Networking.Responses.UpgradePokemonResponseOuterClass.UpgradePokemonResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.EvolutionResult;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;
import lombok.Setter;

/**
 * The type Pokemon.
 */
public class Pokemon {

	private static final String TAG = Pokemon.class.getSimpleName();
	private final PokemonGo pgo;
	private PokemonData proto;
	private PokemonMeta meta;

	// API METHODS //

	// DELEGATE METHODS BELOW //
	public Pokemon(PokemonGo api, PokemonData proto) {
		this.pgo = api;
		this.proto = proto;
	}

	/**
	 * Transfers the pokemon.
	 *
	 * @return the result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public Result transferPokemon() throws LoginFailedException, RemoteServerException {
		ReleasePokemonMessage reqMsg = ReleasePokemonMessage.newBuilder().setPokemonId(getId()).build();

		ServerRequest serverRequest = new ServerRequest(RequestType.RELEASE_POKEMON, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		ReleasePokemonResponse response;
		try {
			response = ReleasePokemonResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			return ReleasePokemonResponse.Result.FAILED;
		}

		if (response.getResult() == Result.SUCCESS) {
			pgo.getInventories().getPokebank().removePokemon(this);
		}

		pgo.getInventories().getPokebank().removePokemon(this);

		pgo.getInventories().updateInventories();

		return response.getResult();
	}

	/**
	 * Rename pokemon nickname pokemon response . result.
	 *
	 * @param nickname the nickname
	 * @return the nickname pokemon response . result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public NicknamePokemonResponse.Result renamePokemon(String nickname)
			throws LoginFailedException, RemoteServerException {
		NicknamePokemonMessage reqMsg = NicknamePokemonMessage.newBuilder()
				.setPokemonId(getId())
				.setNickname(nickname)
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.NICKNAME_POKEMON, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		NicknamePokemonResponse response;
		try {
			response = NicknamePokemonResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		pgo.getInventories().getPokebank().removePokemon(this);
		pgo.getInventories().updateInventories();

		return response.getResult();
	}

	/**
	* Function to mark the pokemon as favorite or not.
	*
	* @param markFavorite Mark Pokemon as Favorite?
	* @return the SetFavoritePokemonResponse.Result
	* @throws LoginFailedException  the login failed exception
	* @throws RemoteServerException the remote server exception
	*/
	public SetFavoritePokemonResponse.Result setFavoritePokemon(boolean markFavorite) 
			throws LoginFailedException, RemoteServerException {
		SetFavoritePokemonMessage reqMsg = SetFavoritePokemonMessage.newBuilder()
				.setPokemonId(getId())
				.setIsFavorite(markFavorite)
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.SET_FAVORITE_POKEMON, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		SetFavoritePokemonResponse response;
		try {
			response = SetFavoritePokemonResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		pgo.getInventories().getPokebank().removePokemon(this);
		pgo.getInventories().updateInventories();

		return response.getResult();
	}

	/**
	 * Powers up a pokemon with candy and stardust.
	 * After powering up this pokemon object will reflect the new changes.
	 *
	 * @return The result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public UpgradePokemonResponse.Result powerUp() throws LoginFailedException, RemoteServerException {
		UpgradePokemonMessage reqMsg = UpgradePokemonMessage.newBuilder()
				.setPokemonId(this.getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.UPGRADE_POKEMON, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		UpgradePokemonResponse response;
		try {
			response = UpgradePokemonResponse.parseFrom(serverRequest.getData());
			this.proto = response.getUpgradedPokemon();
			return response.getResult();
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	/**
	 * Evolve evolution result.
	 *
	 * @return the evolution result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public EvolutionResult evolve() throws LoginFailedException, RemoteServerException {
		EvolvePokemonMessage reqMsg = EvolvePokemonMessage.newBuilder().setPokemonId(getId()).build();

		ServerRequest serverRequest = new ServerRequest(RequestType.EVOLVE_POKEMON, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		EvolvePokemonResponse response;
		try {
			response = EvolvePokemonResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			return null;
		}

		EvolutionResult result = new EvolutionResult(pgo, response);

		pgo.getInventories().getPokebank().removePokemon(this);

		pgo.getInventories().updateInventories();

		return result;
	}

	private PokemonMeta getMeta() {
		if (meta == null) {
			meta = PokemonMetaRegistry.getMeta(this.getPokemonId());
		}

		return meta;
	}

	public int getCandy() {
		return pgo.getInventories().getCandyjar().getCandies(getPokemonFamily());
	}

	public PokemonFamilyId getPokemonFamily() {
		return PokemonMetaRegistry.getFamily(this.getPokemonId());
	}

	public boolean equals(Pokemon other) {
		return (other.getId() == getId());
	}

	public PokemonData getDefaultInstanceForType() {
		return proto.getDefaultInstanceForType();
	}

	public long getId() {
		return proto.getId();
	}

	public PokemonIdOuterClass.PokemonId getPokemonId() {
		return proto.getPokemonId();
	}

	public int getCp() {
		return proto.getCp();
	}

	public int getStamina() {
		return proto.getStamina();
	}

	public int getMaxStamina() {
		return proto.getStaminaMax();
	}

	public PokemonMoveOuterClass.PokemonMove getMove1() {
		return proto.getMove1();
	}

	public PokemonMoveOuterClass.PokemonMove getMove2() {
		return proto.getMove2();
	}

	public String getDeployedFortId() {
		return proto.getDeployedFortId();
	}

	public String getOwnerName() {
		return proto.getOwnerName();
	}

	public boolean getIsEgg() {
		return proto.getIsEgg();
	}

	public double getEggKmWalkedTarget() {
		return proto.getEggKmWalkedTarget();
	}

	public double getEggKmWalkedStart() {
		return proto.getEggKmWalkedStart();
	}

	public int getOrigin() {
		return proto.getOrigin();
	}

	public float getHeightM() {
		return proto.getHeightM();
	}

	public int getIndividualAttack() {
		return proto.getIndividualAttack();
	}

	public int getIndividualDefense() {
		return proto.getIndividualDefense();
	}

	public int getIndividualStamina() {
		return proto.getIndividualStamina();
	}
	
	/**
	 * Calculates the pokemons IV ratio.
	 * @return the pokemons IV ratio as a double between 0 and 1.0, 1.0 being perfect IVs
	 */
	public double getIvRatio() {
		return (this.getIndividualAttack() + this.getIndividualDefense() + this.getIndividualStamina()) / 45.0;
	}

	public float getCpMultiplier() {
		return proto.getCpMultiplier();
	}

	public ItemId getPokeball() {
		return proto.getPokeball();
	}

	public long getCapturedS2CellId() {
		return proto.getCapturedCellId();
	}

	public int getBattlesAttacked() {
		return proto.getBattlesAttacked();
	}

	public int getBattlesDefended() {
		return proto.getBattlesDefended();
	}

	public String getEggIncubatorId() {
		return proto.getEggIncubatorId();
	}

	public long getCreationTimeMs() {
		return proto.getCreationTimeMs();
	}

	public boolean getFavorite() {
		return proto.getFavorite() > 0;
	}

	public String getNickname() {
		return proto.getNickname();
	}

	public boolean getFromFort() {
		return proto.getFromFort() > 0;
	}

	public void debug() {
		Log.d(TAG, proto.toString());
	}


	public int getBaseStam() {
		return getMeta().getBaseStam();
	}

	public double getBaseCaptureRate() {
		return getMeta().getBaseCaptureRate();
	}

	public int getCandiesToEvolve() {
		return getMeta().getCandiesToEvolve();
	}

	public double getBaseFleeRate() {
		return getMeta().getBaseFleeRate();
	}

	public PokemonIdOuterClass.PokemonId getParent() {
		return getMeta().getParent();
	}
}
