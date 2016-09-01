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
import POGOProtos.Networking.Requests.Messages.UseItemReviveMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.NicknamePokemonResponseOuterClass.NicknamePokemonResponse;
import POGOProtos.Networking.Responses.UseItemPotionResponseOuterClass.UseItemPotionResponse;
import POGOProtos.Networking.Responses.UseItemReviveResponseOuterClass.UseItemReviveResponse;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.util.PokeCallback;
import lombok.Getter;
import lombok.Setter;


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
	 * @param api   the api to use
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
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void transferPokemon() throws LoginFailedException, RemoteServerException {
		ReleasePokemonMessage reqMsg = ReleasePokemonMessage.newBuilder().setPokemonId(getId()).build();

		final Pokemon me = this;
		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.RELEASE_POKEMON, reqMsg, api);
		return AsyncHelper.toBlocking(
				api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, Result>() {

					@Override
					public Result call(ByteString bytes) {
						ReleasePokemonResponse response;
						try {
							response = ReleasePokemonResponse.parseFrom(bytes);
						} catch (InvalidProtocolBufferException e) {
							return ReleasePokemonResponse.Result.FAILED;
						}

						api.getInventories().getPokebank().removePokemon(me);
						return response.getResult();
					}
				})
		);

	}

	/**
	 * Rename pokemon nickname pokemon response . result.
	 *
	 * @param nickname the nickname
	 * @return the nickname pokemon response . result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void renamePokemon(String nickname)
			throws LoginFailedException, RemoteServerException {
		NicknamePokemonMessage reqMsg = NicknamePokemonMessage.newBuilder()
				.setPokemonId(getId())
				.setNickname(nickname)
				.build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.NICKNAME_POKEMON, reqMsg, api);
		return AsyncHelper.toBlocking(
				api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, NicknamePokemonResponse.Result>() {

					@Override
					public NicknamePokemonResponse.Result call(ByteString bytes) {
						NicknamePokemonResponse response;
						try {
							response = NicknamePokemonResponse.parseFrom(bytes);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}


						return response.getResult();
					}
				})
		);
	}

	/**
	 * Function to mark the pokemon as favorite or not.
	 *
	 * @param markFavorite Mark Pokemon as Favorite?
	 * @return the SetFavoritePokemonResponse.Result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void setFavoritePokemon(boolean markFavorite)
			throws LoginFailedException, RemoteServerException {
		SetFavoritePokemonMessage reqMsg = SetFavoritePokemonMessage.newBuilder()
				.setPokemonId(getId())
				.setIsFavorite(markFavorite)
				.build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.SET_FAVORITE_POKEMON, reqMsg, api);
		return AsyncHelper.toBlocking(
				api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, SetFavoritePokemonResponse.Result>() {

					@Override
					public SetFavoritePokemonResponse.Result call(ByteString bytes) {
						SetFavoritePokemonResponse response;
						try {
							response = SetFavoritePokemonResponse.parseFrom(bytes);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}


						return response.getResult();
					}
				})
		);

	}

	/**
	 * Check if can powers up this pokemon
	 *
	 * @return the boolean
	 */
	public boolean canPowerUp() {
		return getCandy() >= getCandyCostsForPowerup() && api.getPlayerProfile()
				.getCurrency(PlayerProfile.Currency.STARDUST) >= getStardustCostsForPowerup();
	}

	/**
	 * Check if can powers up this pokemon, you can choose whether or not to consider the max cp limit for current
	 * player level passing true to consider and false to not consider.
	 *
	 * @param considerMaxCPLimitForPlayerLevel Consider max cp limit for actual player level
	 * @return the boolean
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link PokemonMetaRegistry}.
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
		return !EvolutionInfo.isFullyEvolved(getPokemonId()) && (getCandy() >= getCandiesToEvolve());
	}

	/**
	 * Powers up a pokemon with candy and stardust.
	 * After powering up this pokemon object will reflect the new changes.
	 *
	 * @return The result
	 */
	public void powerUp() {
		UpgradePokemonMessage reqMsg = UpgradePokemonMessage.newBuilder().setPokemonId(getId()).build();
		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.UPGRADE_POKEMON, reqMsg);

		return api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(
				new Func1<ByteString, UpgradePokemonResponse.Result>() {
					@Override
					public UpgradePokemonResponse.Result call(ByteString result) {
						UpgradePokemonResponse response;
						try {
							response = UpgradePokemonResponse.parseFrom(result);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}
						//set new pokemon details
						setProto(response.getUpgradedPokemon());
						return response.getResult();
					}
				});
	}

	/**
	 * dus
	 * Evolve evolution result.
	 *
	 * @return the evolution result
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void evolve() throws LoginFailedException, RemoteServerException {
		EvolvePokemonMessage reqMsg = EvolvePokemonMessage.newBuilder().setPokemonId(getId()).build();

		final Pokemon me = this;
		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.EVOLVE_POKEMON, reqMsg, api);
		return AsyncHelper.toBlocking(
				api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, EvolutionResult>() {
					@Override
					public EvolutionResult call(ByteString bytes) {
						EvolvePokemonResponse response;
						try {
							response = EvolvePokemonResponse.parseFrom(bytes);
						} catch (InvalidProtocolBufferException e) {
							return null;
						}
						EvolutionResult result = new EvolutionResult(api, response);
						api.getInventories().getPokebank().removePokemon(me);
						return result;
					}
				})
		);

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
	 * @throws LoginFailedException  If login failed.
	 * @throws RemoteServerException If server communication issues occurred.
	 */
	public void heal()
			throws LoginFailedException, RemoteServerException {

		if (!isInjured()) {
			return UseItemPotionResponse.Result.ERROR_CANNOT_USE;
		}

		if (api.getInventories().getItemBag().getItem(ItemId.ITEM_POTION).getCount() > 0) {
			usePotion(ItemId.ITEM_POTION);
			return;
		}

		if (api.getInventories().getItemBag().getItem(ItemId.ITEM_SUPER_POTION).getCount() > 0) {
			usePotion(ItemId.ITEM_SUPER_POTION);
			return;
		}

		if (api.getInventories().getItemBag().getItem(ItemId.ITEM_HYPER_POTION).getCount() > 0) {
			usePotion(ItemId.ITEM_HYPER_POTION);
			return;
		}

		if (api.getInventories().getItemBag().getItem(ItemId.ITEM_MAX_POTION).getCount() > 0) {
			usePotion(ItemId.ITEM_MAX_POTION);
			return;
		}

		return UseItemPotionResponse.Result.ERROR_CANNOT_USE;
	}

	/**
	 * use a potion on that pokemon. Will check if there is enough potions and if the pokemon need
	 * to be healed.
	 *
	 * @param itemId {@link ItemId} of the potion to use.
	 * @return Result, ERROR_CANNOT_USE if the requirements aren't met
	 * @throws LoginFailedException  If login failed.
	 * @throws RemoteServerException If server communications failed.
	 */
	public void usePotion(ItemId itemId)
			throws LoginFailedException, RemoteServerException {

		Item potion = api.getInventories().getItemBag().getItem(itemId);
		//some sanity check, to prevent wrong use of this call
		if (!potion.isPotion() || potion.getCount() < 1 || !isInjured())
			return UseItemPotionResponse.Result.ERROR_CANNOT_USE;

		UseItemPotionMessageOuterClass.UseItemPotionMessage reqMsg = UseItemPotionMessageOuterClass.UseItemPotionMessage
				.newBuilder()
				.setItemId(itemId)
				.setPokemonId(getId())
				.build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.USE_ITEM_POTION, reqMsg, api);
		return AsyncHelper.toBlocking(
				api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, UseItemPotionResponse.Result>() {

					@Override
					public UseItemPotionResponse.Result call(ByteString bytes) {
						UseItemPotionResponse response;
						try {
							response = UseItemPotionResponse.parseFrom(bytes);
							if (response.getResult() == UseItemPotionResponse.Result.SUCCESS) {
								setStamina(response.getStamina());
							}
							return response.getResult();
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}
					}
				})
		);
	}

	/**
	 * Revive a pokemon, using various fallbacks for revive items
	 *
	 * @return Result, ERROR_CANNOT_USE if the requirements arent met
	 * @throws LoginFailedException  If login failed.
	 * @throws RemoteServerException If server communications failed.
	 */
	public void revive(PokeCallback<UseItemReviveResponse.Result> callback)
			throws LoginFailedException, RemoteServerException {

		if (!isFainted()) {
			callback.onResponse(UseItemReviveResponse.Result.ERROR_CANNOT_USE);
			return;
		}

		if (api.getInventories().getItemBag().getItem(ItemId.ITEM_REVIVE).getCount() > 0) {
			useRevive(ItemId.ITEM_REVIVE, callback);
			return;
		}

		if (api.getInventories().getItemBag().getItem(ItemId.ITEM_MAX_REVIVE).getCount() > 0) {
			useRevive(ItemId.ITEM_MAX_REVIVE, callback);
			return;
		}

		callback.onResponse(UseItemReviveResponse.Result.ERROR_CANNOT_USE);
	}

	/**
	 * Use a revive item on the pokemon. Will check if there is enough revive &amp; if the pokemon need
	 * to be revived.
	 *
	 * @param itemId {@link ItemId} of the Revive to use.
	 * @return Result, ERROR_CANNOT_USE if the requirements arent met
	 * @throws LoginFailedException  If login failed.
	 * @throws RemoteServerException If server communications failed.
	 */
	public void useRevive(ItemId itemId, PokeCallback<UseItemReviveResponse.Result> callback)
			throws LoginFailedException, RemoteServerException {

		Item item = api.getInventories().getItemBag().getItem(itemId);
		if (!item.isRevive() || item.getCount() < 1 || !isFainted()) {
			callback.onResponse(UseItemReviveResponse.Result.ERROR_CANNOT_USE);
		}

		UseItemReviveMessageOuterClass.UseItemReviveMessage reqMsg = UseItemReviveMessageOuterClass.UseItemReviveMessage
				.newBuilder()
				.setItemId(itemId)
				.setPokemonId(getId())
				.build();

		AsyncServerRequest serverRequest = new AsyncServerRequest(RequestType.USE_ITEM_REVIVE, reqMsg, api);
		return AsyncHelper.toBlocking(
				api.getRequestHandler().sendAsyncServerRequests(serverRequest).map(new Func1<ByteString, UseItemReviveResponse.Result>() {

					@Override
					public UseItemReviveResponse.Result call(ByteString bytes) {
						UseItemReviveResponse response;
						try {
							response = UseItemReviveResponse.parseFrom(bytes);
							if (response.getResult() == UseItemReviveResponse.Result.SUCCESS) {
								setStamina(response.getStamina());
							}
							return response.getResult();
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}
					}
				})
		);
	}

	public EvolutionForm getEvolutionForm() {
		return new EvolutionForm(getPokemonId());
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
}