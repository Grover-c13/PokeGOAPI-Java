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
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Networking.Requests.Messages.EvolvePokemonMessageOuterClass.EvolvePokemonMessage;
import POGOProtos.Networking.Requests.Messages.NicknamePokemonMessageOuterClass.NicknamePokemonMessage;
import POGOProtos.Networking.Requests.Messages.ReleasePokemonMessageOuterClass.ReleasePokemonMessage;
import POGOProtos.Networking.Requests.Messages.SetFavoritePokemonMessageOuterClass.SetFavoritePokemonMessage;
import POGOProtos.Networking.Requests.Messages.UpgradePokemonMessageOuterClass.UpgradePokemonMessage;
import POGOProtos.Networking.Requests.Messages.UseItemPotionMessageOuterClass;
import POGOProtos.Networking.Requests.Messages.UseItemReviveMessageOuterClass.UseItemReviveMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass.EvolvePokemonResponse;
import POGOProtos.Networking.Responses.NicknamePokemonResponseOuterClass.NicknamePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse.Result;
import POGOProtos.Networking.Responses.SetFavoritePokemonResponseOuterClass.SetFavoritePokemonResponse;
import POGOProtos.Networking.Responses.UpgradePokemonResponseOuterClass.UpgradePokemonResponse;
import POGOProtos.Networking.Responses.UseItemPotionResponseOuterClass.UseItemPotionResponse;
import POGOProtos.Networking.Responses.UseItemReviveResponseOuterClass.UseItemReviveResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.map.pokemon.EvolutionResult;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.api.settings.templates.ItemTemplates;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.util.AsyncHelper;
import lombok.Getter;
import lombok.Setter;
import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/**
 * The type Pokemon.
 */
public class Pokemon extends PokemonDetails {

	private static final String TAG = Pokemon.class.getSimpleName();
	@Getter
	@Setter
	private int stamina;

	/**
	 * Creates a Pokemon object with helper functions around the proto.
	 *
	 * @param api the api to use
	 * @param proto the proto from the server
	 */
	public Pokemon(PokemonGo api, PokemonData proto) {
		super(api, proto);
		this.stamina = proto.getStamina();
	}

	/**
	 * Transfers the pokemon.
	 *
	 * @return the result
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public Result transferPokemon() throws RequestFailedException {
		if (this.isFavorite() || this.isDeployed()) {
			return Result.FAILED;
		}

		ReleasePokemonMessage reqMsg = ReleasePokemonMessage.newBuilder().setPokemonId(getId()).build();

		ServerRequest serverRequest = new ServerRequest(RequestType.RELEASE_POKEMON, reqMsg);
		api.requestHandler.sendServerRequests(serverRequest, true);

		ReleasePokemonResponse response;
		try {
			response = ReleasePokemonResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			return ReleasePokemonResponse.Result.FAILED;
		}

		if (response.getResult() == Result.SUCCESS) {
			api.inventories.pokebank.removePokemon(this);
		}

		return response.getResult();
	}

	/**
	 * Rename pokemon nickname pokemon response . result.
	 *
	 * @param nickname the nickname
	 * @return the nickname pokemon response . result
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public NicknamePokemonResponse.Result renamePokemon(String nickname)
			throws RequestFailedException {
		NicknamePokemonMessage reqMsg = NicknamePokemonMessage.newBuilder()
				.setPokemonId(getId())
				.setNickname(nickname)
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.NICKNAME_POKEMON, reqMsg);
		api.requestHandler.sendServerRequests(serverRequest, true);

		NicknamePokemonResponse response;
		try {
			response = NicknamePokemonResponse.parseFrom(serverRequest.getData());
			if (response.getResult() == NicknamePokemonResponse.Result.SUCCESS) {
				this.nickname = nickname;
			}
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}

		api.inventories.pokebank.removePokemon(this);

		return response.getResult();
	}

	/**
	 * Function to mark the pokemon as favorite or not.
	 *
	 * @param markFavorite Mark Pokemon as Favorite?
	 * @return the SetFavoritePokemonResponse.Result
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public SetFavoritePokemonResponse.Result setFavoritePokemon(boolean markFavorite)
			throws RequestFailedException {
		SetFavoritePokemonMessage reqMsg = SetFavoritePokemonMessage.newBuilder()
				.setPokemonId(getId())
				.setIsFavorite(markFavorite)
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.SET_FAVORITE_POKEMON, reqMsg);
		api.requestHandler.sendServerRequests(serverRequest, true);

		SetFavoritePokemonResponse response;
		try {
			response = SetFavoritePokemonResponse.parseFrom(serverRequest.getData());
			if (response.getResult() == SetFavoritePokemonResponse.Result.SUCCESS) {
				favorite = markFavorite ? 1 : 0;
			}
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}

		api.inventories.pokebank.removePokemon(this);

		return response.getResult();
	}

	/**
	 * Check if can powers up this pokemon
	 *
	 * @return the boolean
	 */
	public boolean canPowerUp() {
		return getCandy() >= getCandyCostsForPowerup() && api.playerProfile
				.getCurrency(PlayerProfile.Currency.STARDUST) >= getStardustCostsForPowerup();
	}

	/**
	 * Check if can powers up this pokemon, you can choose whether or not to consider the max cp limit for current
	 * player level passing true to consider and false to not consider.
	 *
	 * @param considerMaxCPLimitForPlayerLevel Consider max cp limit for actual player level
	 * @return the boolean
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the
	 * {@link ItemTemplates}.
	 */
	public boolean canPowerUp(boolean considerMaxCPLimitForPlayerLevel)
			throws NoSuchItemException {
		return considerMaxCPLimitForPlayerLevel
				? this.canPowerUp() && (this.getCp() < this.getMaxCpForPlayer())
				: canPowerUp();
	}

	/**
	 * Check if can evolve this pokemon
	 *
	 * @return the boolean
	 */
	public boolean canEvolve() {
		Evolutions evolutions = api.itemTemplates.evolutions;
		return evolutions.canEvolve(getPokemonId()) && (getCandy() >= getCandiesToEvolve());
	}

	/**
	 * Powers up a pokemon with candy and stardust.
	 * After powering up this pokemon object will reflect the new changes.
	 *
	 * @return The result
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public UpgradePokemonResponse.Result powerUp() throws RequestFailedException {
		return AsyncHelper.toBlocking(powerUpAsync());
	}

	/**
	 * Powers up a pokemon with candy and stardust.
	 * After powering up this pokemon object will reflect the new changes.
	 *
	 * @return The result
	 */
	public Observable<UpgradePokemonResponse.Result> powerUpAsync() {
		UpgradePokemonMessage reqMsg = UpgradePokemonMessage.newBuilder().setPokemonId(getId()).build();
		ServerRequest serverRequest = new ServerRequest(RequestType.UPGRADE_POKEMON, reqMsg);

		return api.requestHandler.sendAsyncServerRequests(serverRequest, true).map(
				new Func1<ByteString, UpgradePokemonResponse.Result>() {
					@Override
					public UpgradePokemonResponse.Result call(ByteString result) {
						UpgradePokemonResponse response;
						try {
							response = UpgradePokemonResponse.parseFrom(result);
						} catch (InvalidProtocolBufferException e) {
							throw Exceptions.propagate(e);
						}
						//set new pokemon details
						applyProto(response.getUpgradedPokemon());
						return response.getResult();
					}
				});
	}

	/**
	 * dus
	 * Evolve evolution result.
	 *
	 * @return the evolution result
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public EvolutionResult evolve() throws RequestFailedException {
		return evolve(null);
	}

	/**
	 * Evolves pokemon with evolution item
	 *
	 * @param evolutionItem the evolution item to evolve with
	 * @return the evolution result
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public EvolutionResult evolve(ItemId evolutionItem) throws
			RequestFailedException {
		EvolvePokemonMessage.Builder messageBuilder = EvolvePokemonMessage.newBuilder().setPokemonId(getId());

		if (evolutionItem != null) {
			messageBuilder.setEvolutionItemRequirement(evolutionItem);
		}

		ServerRequest serverRequest = new ServerRequest(RequestType.EVOLVE_POKEMON, messageBuilder.build());
		api.requestHandler.sendServerRequests(serverRequest, true);

		EvolvePokemonResponse response;
		try {
			response = EvolvePokemonResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			return null;
		}

		return new EvolutionResult(api, response);
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
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public UseItemPotionResponse.Result heal()
			throws RequestFailedException {

		if (!isInjured() || isFainted())
			return UseItemPotionResponse.Result.ERROR_CANNOT_USE;

		if (api.inventories.itemBag.getItem(ItemId.ITEM_POTION).count > 0)
			return usePotion(ItemId.ITEM_POTION);

		if (api.inventories.itemBag.getItem(ItemId.ITEM_SUPER_POTION).count > 0)
			return usePotion(ItemId.ITEM_SUPER_POTION);

		if (api.inventories.itemBag.getItem(ItemId.ITEM_HYPER_POTION).count > 0)
			return usePotion(ItemId.ITEM_HYPER_POTION);

		if (api.inventories.itemBag.getItem(ItemId.ITEM_MAX_POTION).count > 0)
			return usePotion(ItemId.ITEM_MAX_POTION);

		return UseItemPotionResponse.Result.ERROR_CANNOT_USE;
	}

	/**
	 * use a potion on that pokemon. Will check if there is enough potions and if the pokemon need
	 * to be healed.
	 *
	 * @param itemId {@link ItemId} of the potion to use.
	 * @return Result, ERROR_CANNOT_USE if the requirements aren't met
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public UseItemPotionResponse.Result usePotion(ItemId itemId)
			throws RequestFailedException {

		Item potion = api.inventories.itemBag.getItem(itemId);
		//some sanity check, to prevent wrong use of this call
		if (!potion.isPotion() || potion.count < 1 || !isInjured())
			return UseItemPotionResponse.Result.ERROR_CANNOT_USE;

		UseItemPotionMessageOuterClass.UseItemPotionMessage reqMsg = UseItemPotionMessageOuterClass
				.UseItemPotionMessage
				.newBuilder()
				.setItemId(itemId)
				.setPokemonId(getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.USE_ITEM_POTION, reqMsg);
		api.requestHandler.sendServerRequests(serverRequest, true);

		UseItemPotionResponse response;
		try {
			response = UseItemPotionResponse.parseFrom(serverRequest.getData());
			if (response.getResult() == UseItemPotionResponse.Result.SUCCESS) {
				potion.setCount(potion.count - 1);
				this.stamina = response.getStamina();
			}
			return response.getResult();
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * Revive a pokemon, using various fallbacks for revive items
	 *
	 * @return Result, ERROR_CANNOT_USE if the requirements aren't met
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public UseItemReviveResponse.Result revive()
			throws RequestFailedException {

		if (!isFainted())
			return UseItemReviveResponse.Result.ERROR_CANNOT_USE;

		if (api.inventories.itemBag.getItem(ItemId.ITEM_REVIVE).count > 0)
			return useRevive(ItemId.ITEM_REVIVE);

		if (api.inventories.itemBag.getItem(ItemId.ITEM_MAX_REVIVE).count > 0)
			return useRevive(ItemId.ITEM_MAX_REVIVE);

		return UseItemReviveResponse.Result.ERROR_CANNOT_USE;
	}

	/**
	 * Use a revive item on the pokemon. Will check if there is enough revive &amp; if the pokemon need
	 * to be revived.
	 *
	 * @param itemId {@link ItemId} of the Revive to use.
	 * @return Result, ERROR_CANNOT_USE if the requirements aren't met
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public UseItemReviveResponse.Result useRevive(ItemId itemId)
			throws RequestFailedException {

		Item item = api.inventories.itemBag.getItem(itemId);
		if (!item.isRevive() || item.count < 1 || !isFainted())
			return UseItemReviveResponse.Result.ERROR_CANNOT_USE;

		UseItemReviveMessage reqMsg = UseItemReviveMessage
				.newBuilder()
				.setItemId(itemId)
				.setPokemonId(getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.USE_ITEM_REVIVE, reqMsg);
		api.requestHandler.sendServerRequests(serverRequest, true);

		UseItemReviveResponse response;
		try {
			response = UseItemReviveResponse.parseFrom(serverRequest.getData());
			if (response.getResult() == UseItemReviveResponse.Result.SUCCESS) {
				item.setCount(item.count - 1);
				this.stamina = response.getStamina();
			}
			return response.getResult();
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * @return the evolution metadata for this pokemon, or null if it doesn't exist
	 */
	public Evolution getEvolution() {
		return api.itemTemplates.evolutions.getEvolution(this.getPokemonId());
	}

	/**
	 * @return Actual stamina in percentage relative to the current maximum stamina (useful in ProgressBars)
	 */
	public int getStaminaInPercentage() {
		return (getStamina() * 100) / getMaxStamina();
	}

	/**
	 * Actual cp in percentage relative to the maximum cp that this pokemon can reach
	 * at the actual player level (useful in ProgressBars)
	 *
	 * @return Actual cp in percentage
	 * @throws NoSuchItemException if threw from {@link #getMaxCpForPlayer()}
	 */
	public int getCPInPercentageActualPlayerLevel()
			throws NoSuchItemException {
		return ((getCp() * 100) / getMaxCpForPlayer());
	}

	/**
	 * Actual cp in percentage relative to the maximum cp that this pokemon can reach at player-level 40
	 * (useful in ProgressBars)
	 *
	 * @return Actual cp in percentage
	 * @throws NoSuchItemException if threw from {@link #getMaxCp()}
	 */
	public int getCPInPercentageMaxPlayerLevel() throws NoSuchItemException {
		return ((getCp() * 100) / getMaxCp());
	}

	/**
	 * @return IV in percentage
	 */
	public double getIvInPercentage() {
		return ((Math.floor((this.getIvRatio() * 100) * 100)) / 100);
	}

	@Override
	public int hashCode() {
		return (int) getId();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Pokemon && ((Pokemon) obj).getId() == getId();
	}

	/**
	 * Returns true if this pokemon is your current buddy
	 * @return true if this pokemon is your current buddy
	 */
	public boolean isBuddy() {
		PlayerProfile profile = api.playerProfile;
		return profile.hasBuddy() && profile.buddy.getPokemon().getId() == this.getId();
	}
}
