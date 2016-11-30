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
import POGOProtos.Networking.Requests.Messages.UseItemPotionMessageOuterClass.UseItemPotionMessage;
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
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.map.pokemon.EvolutionResult;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncReturn;
import com.pokegoapi.main.PokemonCallback;
import com.pokegoapi.main.PokemonRequest;
import com.pokegoapi.main.PokemonResponse;
import com.pokegoapi.main.RequestCallback;
import com.pokegoapi.main.Utils;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Pokemon.
 */
public class Pokemon extends PokemonDetails {
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
	 * @param result callback for the transfer result
	 */
	public void transferPokemon(final AsyncReturn<Result> result) {
		final ReleasePokemonMessage message = ReleasePokemonMessage.newBuilder().setPokemonId(getId()).build();
		PokemonRequest request = new PokemonRequest(RequestType.RELEASE_POKEMON, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, Result.FAILED)) {
					return;
				}
				try {
					ReleasePokemonResponse messageResponse
							= ReleasePokemonResponse.parseFrom(response.getResponseData());
					if (messageResponse.getResult() == Result.SUCCESS) {
						api.getInventories().getPokebank().removePokemon(Pokemon.this);
					}

					api.getInventories().updateInventories(PokemonCallback.NULL_CALLBACK);

					result.onReceive(messageResponse.getResult(), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(Result.FAILED, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Rename pokemon nickname pokemon response . result.
	 *
	 * @param nickname the nickname
	 * @param result callback to return the nickname pokemon response
	 */
	public void renamePokemon(String nickname, final AsyncReturn<NicknamePokemonResponse.Result> result) {
		final NicknamePokemonMessage message = NicknamePokemonMessage.newBuilder()
				.setPokemonId(getId())
				.setNickname(nickname)
				.build();

		PokemonRequest request = new PokemonRequest(RequestType.NICKNAME_POKEMON, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, NicknamePokemonResponse.Result.UNSET)) {
					return;
				}
				try {
					NicknamePokemonResponse messageResponse
							= NicknamePokemonResponse.parseFrom(response.getResponseData());
					api.getInventories().getPokebank().removePokemon(Pokemon.this);
					api.getInventories().updateInventories(PokemonCallback.NULL_CALLBACK);
					result.onReceive(messageResponse.getResult(), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Function to mark the pokemon as favorite or not.
	 *
	 * @param markFavorite Mark Pokemon as Favorite?
	 * @param result callback to return the SetFavoritePokemonResponse.Result
	 */
	public void setFavoritePokemon(boolean markFavorite, final AsyncReturn<SetFavoritePokemonResponse.Result> result) {
		final SetFavoritePokemonMessage message = SetFavoritePokemonMessage.newBuilder()
				.setPokemonId(getId())
				.setIsFavorite(markFavorite)
				.build();

		PokemonRequest request = new PokemonRequest(RequestType.SET_FAVORITE_POKEMON, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, SetFavoritePokemonResponse.Result.UNSET)) {
					return;
				}
				try {
					SetFavoritePokemonResponse messageResponse
							= SetFavoritePokemonResponse.parseFrom(response.getResponseData());
					api.getInventories().getPokebank().removePokemon(Pokemon.this);
					api.getInventories().updateInventories(PokemonCallback.NULL_CALLBACK);
					result.onReceive(messageResponse.getResult(), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(SetFavoritePokemonResponse.Result.UNSET, new RemoteServerException(e));
				}
			}
		});
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
	 * @param result callback to return power up result
	 */
	public void powerUp(final AsyncReturn<UpgradePokemonResponse.Result> result) {
		UpgradePokemonMessage message = UpgradePokemonMessage.newBuilder().setPokemonId(getId()).build();
		PokemonRequest request = new PokemonRequest(RequestType.UPGRADE_POKEMON, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, UpgradePokemonResponse.Result.UNSET)) {
					return;
				}
				try {
					UpgradePokemonResponse messageResponse = UpgradePokemonResponse.parseFrom(response.getResponseData());
					setProto(messageResponse.getUpgradedPokemon());
					result.onReceive(messageResponse.getResult(), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(UpgradePokemonResponse.Result.UNSET, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Evolves this pokemon.
	 *
	 * @param result callback to return the result from this evolution
	 */
	public void evolve(final AsyncReturn<EvolutionResult> result) {
		EvolvePokemonMessage message = EvolvePokemonMessage.newBuilder().setPokemonId(getId()).build();
		PokemonRequest request = new PokemonRequest(RequestType.EVOLVE_POKEMON, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, null)) {
					return;
				}
				try {
					EvolvePokemonResponse messageResponse = EvolvePokemonResponse.parseFrom(response.getResponseData());
					api.getInventories().getPokebank().removePokemon(Pokemon.this);
					api.getInventories().updateInventories(PokemonCallback.NULL_CALLBACK);
					result.onReceive(new EvolutionResult(api, messageResponse), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
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
	 * Heals a pokemon using the lowest potion possible.
	 * @param result callback to return the heal result
	 * @return if the request was sent
	 */
	public boolean heal(AsyncReturn<UseItemPotionResponse.Result> result) {
		if (isInjured()) {
			ItemBag bag = api.getInventories().getItemBag();
			if (bag.getItem(ItemId.ITEM_POTION).getCount() > 0) {
				usePotion(ItemId.ITEM_POTION, result);
				return true;
			} else if (bag.getItem(ItemId.ITEM_SUPER_POTION).getCount() > 0) {
				usePotion(ItemId.ITEM_SUPER_POTION, result);
				return true;
			} else if (bag.getItem(ItemId.ITEM_HYPER_POTION).getCount() > 0) {
				usePotion(ItemId.ITEM_HYPER_POTION, result);
				return true;
			} else if (bag.getItem(ItemId.ITEM_MAX_POTION).getCount() > 0) {
				usePotion(ItemId.ITEM_MAX_POTION, result);
				return true;
			}
		}
		result.onReceive(UseItemPotionResponse.Result.ERROR_CANNOT_USE, null);
		return false;
	}

	/**
	 * Uses potion on this pokemon.
	 *
	 * @param item potion item to use
	 * @param result callback to return result of the potion use
	 */
	public void usePotion(ItemId item, final AsyncReturn<UseItemPotionResponse.Result> result) {
		Item potion = api.getInventories().getItemBag().getItem(item);
		if (!potion.isPotion() || potion.getCount() == 0 || !isInjured()) {
			result.onReceive(UseItemPotionResponse.Result.ERROR_CANNOT_USE, null);
		}
		final UseItemPotionMessage message = UseItemPotionMessage.newBuilder()
				.setPokemonId(getId())
				.setItemId(item)
				.build();
		PokemonRequest request = new PokemonRequest(RequestType.USE_ITEM_POTION, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, UseItemPotionResponse.Result.ERROR_CANNOT_USE)) {
					return;
				}
				try {
					UseItemPotionResponse messageResponse = UseItemPotionResponse.parseFrom(response.getResponseData());
					if (messageResponse.getResult() == UseItemPotionResponse.Result.SUCCESS) {
						Pokemon.this.setStamina(messageResponse.getStamina());
					}
					result.onReceive(messageResponse.getResult(), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
	}

	/**
	 * Revive a pokemon, using various fallbacks for revive items
	 *
	 * @param result callback to return the result of the revive request
	 */
	public boolean revive(AsyncReturn<UseItemReviveResponse.Result> result) {
		if (isFainted()) {
			ItemBag bag = api.getInventories().getItemBag();
			if (bag.getItem(ItemId.ITEM_REVIVE).getCount() > 0) {
				useRevive(ItemId.ITEM_REVIVE, result);
				return true;
			} else if (bag.getItem(ItemId.ITEM_MAX_REVIVE).getCount() > 0) {
				useRevive(ItemId.ITEM_MAX_REVIVE, result);
				return true;
			}
		}

		result.onReceive(UseItemReviveResponse.Result.ERROR_CANNOT_USE, null);
		return false;
	}

	/**
	 * Revives this pokemon with the given item.
	 *
	 * @param item revive item to use
	 * @param result callback to return the result of the revive request
	 */
	public void useRevive(ItemId item, final AsyncReturn<UseItemReviveResponse.Result> result) {
		Item revive = api.getInventories().getItemBag().getItem(item);
		if (!revive.isRevive() || revive.getCount() == 0 || !isFainted()) {
			result.onReceive(UseItemReviveResponse.Result.ERROR_CANNOT_USE, null);
		}
		final UseItemReviveMessage message = UseItemReviveMessage.newBuilder()
				.setItemId(item)
				.setPokemonId(getId())
				.build();
		PokemonRequest request = new PokemonRequest(RequestType.USE_ITEM_REVIVE, message);
		api.getRequestHandler().sendRequest(request, new RequestCallback() {
			@Override
			public void handleResponse(PokemonResponse response) throws InvalidProtocolBufferException {
				if (Utils.callbackException(response, result, UseItemReviveResponse.Result.ERROR_CANNOT_USE)) {
					return;
				}
				try {
					UseItemReviveResponse messageResponse = UseItemReviveResponse.parseFrom(response.getResponseData());
					if (messageResponse.getResult() == UseItemReviveResponse.Result.SUCCESS) {
						setStamina(messageResponse.getStamina());
					}
					result.onReceive(messageResponse.getResult(), null);
				} catch (InvalidProtocolBufferException e) {
					result.onReceive(null, new RemoteServerException(e));
				}
			}
		});
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