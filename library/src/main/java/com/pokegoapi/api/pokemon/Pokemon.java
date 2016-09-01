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
import POGOProtos.Networking.Responses.EvolvePokemonResponseOuterClass.EvolvePokemonResponse;
import POGOProtos.Networking.Responses.NicknamePokemonResponseOuterClass.NicknamePokemonResponse;
import POGOProtos.Networking.Responses.ReleasePokemonResponseOuterClass.ReleasePokemonResponse;
import POGOProtos.Networking.Responses.SetFavoritePokemonResponseOuterClass.SetFavoritePokemonResponse;
import POGOProtos.Networking.Responses.UpgradePokemonResponseOuterClass.UpgradePokemonResponse;
import POGOProtos.Networking.Responses.UseItemPotionResponseOuterClass.UseItemPotionResponse;
import POGOProtos.Networking.Responses.UseItemReviveResponseOuterClass.UseItemReviveResponse;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.map.pokemon.EvolutionResult;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.util.PokeAFunc;
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
	 * @param callback an optional callback to handle results
	 */
	public void transferPokemon(PokeCallback<ReleasePokemonResponse.Result> callback) {
		ReleasePokemonMessage reqMsg = ReleasePokemonMessage.newBuilder().setPokemonId(getId()).build();

		new AsyncServerRequest(RequestType.RELEASE_POKEMON, reqMsg,
				new PokeAFunc<ReleasePokemonResponse, ReleasePokemonResponse.Result>() {
					@Override
					public ReleasePokemonResponse.Result exec(ReleasePokemonResponse response) {
						return response.getResult();
					}
				}, callback, api);
	}

	/**
	 * Rename pokemon nickname pokemon response . result.
	 *
	 * @param nickname the nickname
	 * @param callback an optional callback to handle results
	 */
	public void renamePokemon(String nickname, PokeCallback<NicknamePokemonResponse.Result> callback) {
		NicknamePokemonMessage reqMsg = NicknamePokemonMessage.newBuilder()
				.setPokemonId(getId())
				.setNickname(nickname)
				.build();

		new AsyncServerRequest(RequestType.NICKNAME_POKEMON, reqMsg,
				new PokeAFunc<NicknamePokemonResponse, NicknamePokemonResponse.Result>() {
					@Override
					public NicknamePokemonResponse.Result exec(NicknamePokemonResponse response) {
						return response.getResult();
					}
				}, callback, api);
	}

	/**
	 * Function to mark the pokemon as favorite or not.
	 *
	 * @param markFavorite Mark Pokemon as Favorite?
	 * @param callback     an optional callback to handle results
	 */
	public void setFavoritePokemon(boolean markFavorite, PokeCallback<SetFavoritePokemonResponse.Result> callback) {
		SetFavoritePokemonMessage reqMsg = SetFavoritePokemonMessage.newBuilder()
				.setPokemonId(getId())
				.setIsFavorite(markFavorite)
				.build();

		new AsyncServerRequest(RequestType.SET_FAVORITE_POKEMON, reqMsg,
				new PokeAFunc<SetFavoritePokemonResponse, SetFavoritePokemonResponse.Result>() {
					@Override
					public SetFavoritePokemonResponse.Result exec(SetFavoritePokemonResponse response) {
						return response.getResult();
					}
				}, callback, api);
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
	 * @param callback an optional callback to handle results
	 */
	public void powerUp(PokeCallback<UpgradePokemonResponse.Result> callback) {
		UpgradePokemonMessage reqMsg = UpgradePokemonMessage.newBuilder().setPokemonId(getId()).build();
		new AsyncServerRequest(RequestType.UPGRADE_POKEMON, reqMsg,
				new PokeAFunc<UpgradePokemonResponse, UpgradePokemonResponse.Result>() {
					@Override
					public UpgradePokemonResponse.Result exec(UpgradePokemonResponse response) {
						//set new pokemon details
						setProto(response.getUpgradedPokemon());
						return response.getResult();
					}
				}, callback, api);
	}

	/**
	 * Evolve evolution result.
	 *
	 * @param callback an optional callback to handle results
	 */
	public void evolve(PokeCallback<EvolutionResult> callback) {
		EvolvePokemonMessage reqMsg = EvolvePokemonMessage.newBuilder().setPokemonId(getId()).build();

		new AsyncServerRequest(RequestType.EVOLVE_POKEMON, reqMsg,
				new PokeAFunc<EvolvePokemonResponse, EvolutionResult>() {
					@Override
					public EvolutionResult exec(EvolvePokemonResponse response) {
						EvolutionResult result = new EvolutionResult(api, response);
						return result;
					}
				}, callback, api);
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
	 * @param callback an optional callback to handle results
	 */
	public void heal(PokeCallback<UseItemPotionResponse.Result> callback) {
		if (!isInjured()) {
			callback.onResponse(UseItemPotionResponse.Result.ERROR_CANNOT_USE);
			return;
		}

		if (api.getInventories().getItemBag().getItem(ItemId.ITEM_POTION).getCount() > 0) {
			usePotion(ItemId.ITEM_POTION, callback);
			return;
		}

		if (api.getInventories().getItemBag().getItem(ItemId.ITEM_SUPER_POTION).getCount() > 0) {
			usePotion(ItemId.ITEM_SUPER_POTION, callback);
			return;
		}

		if (api.getInventories().getItemBag().getItem(ItemId.ITEM_HYPER_POTION).getCount() > 0) {
			usePotion(ItemId.ITEM_HYPER_POTION, callback);
			return;
		}

		if (api.getInventories().getItemBag().getItem(ItemId.ITEM_MAX_POTION).getCount() > 0) {
			usePotion(ItemId.ITEM_MAX_POTION, callback);
			return;
		}

		callback.onResponse(UseItemPotionResponse.Result.ERROR_CANNOT_USE);
	}

	/**
	 * use a potion on that pokemon. Will check if there is enough potions and if the pokemon need
	 * to be healed.
	 *
	 * @param itemId   {@link ItemId} of the potion to use.
	 * @param callback an optional callback to handle results
	 */
	public void usePotion(ItemId itemId, PokeCallback<UseItemPotionResponse.Result> callback) {
		Item potion = api.getInventories().getItemBag().getItem(itemId);
		//some sanity check, to prevent wrong use of this call
		if (!potion.isPotion() || potion.getCount() < 1 || !isInjured()) {
			callback.onResponse(UseItemPotionResponse.Result.ERROR_CANNOT_USE);
			return;
		}

		UseItemPotionMessageOuterClass.UseItemPotionMessage reqMsg = UseItemPotionMessageOuterClass.UseItemPotionMessage
				.newBuilder()
				.setItemId(itemId)
				.setPokemonId(getId())
				.build();

		new AsyncServerRequest(RequestType.USE_ITEM_POTION, reqMsg,
				new PokeAFunc<UseItemPotionResponse, UseItemPotionResponse.Result>() {
					@Override
					public UseItemPotionResponse.Result exec(UseItemPotionResponse response) {
						if (response.getResult() == UseItemPotionResponse.Result.SUCCESS) {
							setStamina(response.getStamina());
						}
						return response.getResult();
					}
				}, callback, api);
	}

	/**
	 * Revive a pokemon, using various fallbacks for revive items
	 *
	 * @param callback an optional callback to handle results
	 */
	public void revive(PokeCallback<UseItemReviveResponse.Result> callback) {
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
	 * @param itemId   {@link ItemId} of the Revive to use.
	 * @param callback an optional callback to handle results
	 */
	public void useRevive(ItemId itemId, PokeCallback<UseItemReviveResponse.Result> callback) {
		Item item = api.getInventories().getItemBag().getItem(itemId);
		if (!item.isRevive() || item.getCount() < 1 || !isFainted()) {
			callback.onResponse(UseItemReviveResponse.Result.ERROR_CANNOT_USE);
			return;
		}

		UseItemReviveMessageOuterClass.UseItemReviveMessage reqMsg = UseItemReviveMessageOuterClass.UseItemReviveMessage
				.newBuilder()
				.setItemId(itemId)
				.setPokemonId(getId())
				.build();

		new AsyncServerRequest(RequestType.USE_ITEM_REVIVE, reqMsg,
				new PokeAFunc<UseItemReviveResponse, UseItemReviveResponse.Result>() {
					public UseItemReviveResponse.Result exec(UseItemReviveResponse response) {
						if (response.getResult() == UseItemReviveResponse.Result.SUCCESS) {
							setStamina(response.getStamina());
						}
						return response.getResult();
					}
				}, callback, api);
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

	@Override
	public int hashCode() {
		return getProto().getPokemonId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EggPokemon) {
			EggPokemon other = (EggPokemon) obj;
			return (this.getId() == other.getId());
		}

		return false;
	}
}