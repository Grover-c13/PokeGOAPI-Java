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
import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Enums.PokemonMoveOuterClass;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.EvolvePokemonMessageOuterClass.EvolvePokemonMessage;
import POGOProtos.Networking.Requests.Messages.NicknamePokemonMessageOuterClass.NicknamePokemonMessage;
import POGOProtos.Networking.Requests.Messages.ReleasePokemonMessageOuterClass.ReleasePokemonMessage;
import POGOProtos.Networking.Requests.Messages.SetFavoritePokemonMessageOuterClass.SetFavoritePokemonMessage;
import POGOProtos.Networking.Requests.Messages.UpgradePokemonMessageOuterClass.UpgradePokemonMessage;
import POGOProtos.Networking.Requests.Messages.UseItemPotionMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.UseItemReviveMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass.EvolvePokemonResponse;
import POGOProtos.Networking.Responses.NicknamePokemonResponseOuterClass.NicknamePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result;
import POGOProtos.Networking.Responses.SetFavoritePokemonResponseOuterClass.SetFavoritePokemonResponse;
import POGOProtos.Networking.Responses.UpgradePokemonResponseOuterClass.UpgradePokemonResponse;
import POGOProtos.Networking.Responses.UseItemPotionResponseOuterClass;
import POGOProtos.Networking.Responses.UseItemPotionResponseOuterClass.UseItemPotionResponse;
import POGOProtos.Networking.Responses.UseItemReviveResponseOuterClass;
import POGOProtos.Networking.Responses.UseItemReviveResponseOuterClass.UseItemReviveResponse;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.map.pokemon.EvolutionResult;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.Log;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Pokemon.
 */
public class Pokemon extends PokemonDetails {

	private static final String TAG = Pokemon.class.getSimpleName();
	private final PokemonGo pgo;
	@Getter
	@Setter
	private int stamina;


	/**
	 * Creates a Pokemon object with helper functions around the proto.
	 *
	 * @param api   the api to use
	 * @param proto the proto from the server
	 */
	public Pokemon(PokemonGo api, PokemonData proto) {
		super(api, proto);
		this.pgo = api;
		this.stamina = proto.getStamina();
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
			setProto(response.getUpgradedPokemon());
			return response.getResult();
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	/**
	 * dus
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

	/**
	 * @return The CP for this pokemon after powerup
	 */
	public int getCpAfterPowerup() {
		return PokemonCpUtils.getCpAfterPowerup(getProto().getCp(),
				getProto().getCpMultiplier() + getProto().getAdditionalCpMultiplier());
	}

	/**
	 * @return Cost of candy for a powerup
	 */
	public int getCandyCostsForPowerup() {
		return PokemonCpUtils.getCandyCostsForPowerup(getProto().getCpMultiplier() + getProto().getAdditionalCpMultiplier(),
				getProto().getNumUpgrades());
	}

	/**
	 * @return Cost of stardust for a powerup
	 */
	public int getStardustCostsForPowerup() {
		return PokemonCpUtils.getStartdustCostsForPowerup(getProto().getCpMultiplier() + getProto().getAdditionalCpMultiplier(),
				getProto().getNumUpgrades());
	}



	/**
	 * Check if pokemon its injured but not fainted. need potions to heal
	 *
	 * @return true if pokemon is injured
	 */
	public boolean isInjured() {
		return !isFainted() && getStamina() < getMaxStamina();
	}

	/**
	 * check if a pokemon it's died (fainted). need a revive to resurrect
	 *
	 * @return true if a pokemon is fainted
	 */
	public boolean isFainted() {
		return getStamina() == 0;
	}

	/**
	 * Heal a pokemon, using various fallbacks for potions
	 *
	 * @return Result, ERROR_CANNOT_USE if the requirements arent met
     * @throws LoginFailedException If login failed.
     * @throws RemoteServerException If server communication issues occurred.
	 */
	public UseItemPotionResponse.Result heal()
			throws LoginFailedException, RemoteServerException {

		if (!isInjured())
			return UseItemPotionResponse.Result.ERROR_CANNOT_USE;

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_POTION).getCount() > 0)
			return usePotion(ItemId.ITEM_POTION);

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_SUPER_POTION).getCount() > 0)
			return usePotion(ItemId.ITEM_SUPER_POTION);

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_HYPER_POTION).getCount() > 0)
			return usePotion(ItemId.ITEM_HYPER_POTION);

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_MAX_POTION).getCount() > 0)
			return usePotion(ItemId.ITEM_MAX_POTION);

		return UseItemPotionResponse.Result.ERROR_CANNOT_USE;
	}

	/**
	 * use a potion on that pokemon. Will check if there is enough potions and if the pokemon need
	 * to be healed.
	 * @param itemId {@link ItemId} of the potion to use.
	 * @return Result, ERROR_CANNOT_USE if the requirements aren't met
     * @throws LoginFailedException If login failed.
     * @throws RemoteServerException If server communications failed.
	 */
	public UseItemPotionResponse.Result usePotion(ItemId itemId)
			throws LoginFailedException, RemoteServerException {

		Item potion = pgo.getInventories().getItemBag().getItem(itemId);
		//some sanity check, to prevent wrong use of this call
		if (!potion.isPotion() || potion.getCount() < 1 || !isInjured())
			return UseItemPotionResponse.Result.ERROR_CANNOT_USE;

		UseItemPotionMessageOuterClass.UseItemPotionMessage reqMsg = UseItemPotionMessageOuterClass.UseItemPotionMessage
				.newBuilder()
				.setItemId(itemId)
				.setPokemonId(getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.USE_ITEM_POTION, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		UseItemPotionResponse response;
		try {
			response = UseItemPotionResponse.parseFrom(serverRequest.getData());
			if (response.getResult() == UseItemPotionResponse.Result.SUCCESS) {
				setStamina(response.getStamina());
			}
			return response.getResult();
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	/**
	 * Revive a pokemon, using various fallbacks for revive items
	 *
	 * @return Result, ERROR_CANNOT_USE if the requirements arent met
     * @throws LoginFailedException If login failed.
     * @throws RemoteServerException If server communications failed.
	 */
	public UseItemReviveResponse.Result revive()
			throws LoginFailedException, RemoteServerException {

		if (!isFainted())
			return UseItemReviveResponse.Result.ERROR_CANNOT_USE;

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_REVIVE).getCount() > 0)
			return useRevive(ItemId.ITEM_REVIVE);

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_MAX_REVIVE).getCount() > 0)
			return useRevive(ItemId.ITEM_MAX_REVIVE);

		return UseItemReviveResponse.Result.ERROR_CANNOT_USE;
	}

	/**
	 * Use a revive item on the pokemon. Will check if there is enough revive &amp; if the pokemon need
	 * to be revived.
	 * @param itemId {@link ItemId} of the Revive to use.
	 * @return Result, ERROR_CANNOT_USE if the requirements arent met
     * @throws LoginFailedException If login failed.
     * @throws RemoteServerException If server communications failed.
	 */
	public UseItemReviveResponse.Result useRevive(ItemId itemId)
			throws LoginFailedException, RemoteServerException {

		Item item = pgo.getInventories().getItemBag().getItem(itemId);
		if (!item.isRevive() || item.getCount() < 1 || !isFainted())
			return UseItemReviveResponse.Result.ERROR_CANNOT_USE;

		UseItemReviveMessageOuterClass.UseItemReviveMessage reqMsg = UseItemReviveMessageOuterClass.UseItemReviveMessage
				.newBuilder()
				.setItemId(itemId)
				.setPokemonId(getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.USE_ITEM_REVIVE, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		UseItemReviveResponse response;
		try {
			response = UseItemReviveResponse.parseFrom(serverRequest.getData());
			if (response.getResult() == UseItemReviveResponse.Result.SUCCESS) {
				setStamina(response.getStamina());
			}
			return response.getResult();
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	public EvolutionForm getEvolutionForm() {
		return new EvolutionForm(getPokemonId());
	}

}
