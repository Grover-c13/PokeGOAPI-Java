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
import POGOProtos.Networking.Responses.UseItemReviveResponseOuterClass;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.map.pokemon.EvolutionResult;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Pokemon.
 */
@Slf4j
public class Pokemon {
	private final PokemonGo pgo;
	private PokemonData proto;
	private PokemonMeta meta;
	@Getter
	@Setter
	private int stamina;

	// API METHODS //

	// DELEGATE METHODS BELOW //

	/**
	 * Creates a Pokemon object with helper functions around the proto.
	 *
	 * @param api   the api to use
	 * @param proto the proto from the server
	 */
	public Pokemon(PokemonGo api, PokemonData proto) {
		this.pgo = api;
		this.proto = proto;
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
			this.proto = response.getUpgradedPokemon();
			return response.getResult();
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	/**dus
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
	 * Get the meta info for a pokemon.
	 *
	 * @return PokemonMeta
	 */
	public PokemonMeta getMeta() {
		if (meta == null) {
			meta = PokemonMetaRegistry.getMeta(this.getPokemonId());
		}

		return meta;
	}

	public int getCandy() {
		return pgo.getInventories().getCandyjar().getCandies(getPokemonFamily());
	}

	public PokemonFamilyId getPokemonFamily() {
		return getMeta().getFamily();
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
	 *
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

	/**
	 * Checks whether the Pokémon is set as favorite.
	 *
	 * @return true if the Pokémon is set as favorite
	 */
	public boolean isFavorite() {
		return proto.getFavorite() > 0;
	}

	@Deprecated
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
		log.debug(proto.toString());
	}


	public int getBaseStam() {
		return getMeta().getBaseStamina();
	}

	public double getBaseCaptureRate() {
		return getMeta().getBaseCaptureRate();
	}

	public int getCandiesToEvolve() {
		return getMeta().getCandyToEvolve();
	}

	public double getBaseFleeRate() {
		return getMeta().getBaseFleeRate();
	}

	public float getLevel() {
		return PokemonCpUtils.getLevelFromCpMultiplier(proto.getCpMultiplier() + proto.getAdditionalCpMultiplier());
	}

	/**
	 * @return The maximum CP for this pokemon
	 */
	public int getMaxCp() throws NoSuchItemException {
		PokemonMeta pokemonMeta = PokemonMetaRegistry.getMeta(proto.getPokemonId());
		if (pokemonMeta == null) {
			throw new NoSuchItemException("Cannot find meta data for " + proto.getPokemonId().name());
		}
		int attack = proto.getIndividualAttack() + pokemonMeta.getBaseAttack();
		int defense = proto.getIndividualDefense() + pokemonMeta.getBaseDefense();
		int stamina = proto.getIndividualStamina() + pokemonMeta.getBaseStamina();
		return PokemonCpUtils.getMaxCp(attack, defense, stamina);
	}

	/**
	 * @return The CP for this pokemon after powerup
	 */
	public int getCpAfterPowerup() {
		return PokemonCpUtils.getCpAfterPowerup(proto.getCp(),
				proto.getCpMultiplier() + proto.getAdditionalCpMultiplier());
	}

	/**
	 * @return Cost of candy for a powerup
	 */
	public int getCandyCostsForPowerup() {
		return PokemonCpUtils.getCandyCostsForPowerup(proto.getCpMultiplier() + proto.getAdditionalCpMultiplier(),
				proto.getNumUpgrades());
	}

	/**
	 * @return Cost of stardust for a powerup
	 */
	public int getStardustCostsForPowerup() {
		return PokemonCpUtils.getStartdustCostsForPowerup(proto.getCpMultiplier() + proto.getAdditionalCpMultiplier(),
				proto.getNumUpgrades());
	}

	public PokemonIdOuterClass.PokemonId getParent() {
		return getMeta().getParentId();
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
	 */
	public UseItemPotionResponseOuterClass.UseItemPotionResponse.Result heal()
			throws LoginFailedException, RemoteServerException {

		if (!isInjured())
			return UseItemPotionResponseOuterClass.UseItemPotionResponse.Result.ERROR_CANNOT_USE;

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_POTION).getCount() > 0)
			return usePotion(ItemId.ITEM_POTION);

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_SUPER_POTION).getCount() > 0)
			return usePotion(ItemId.ITEM_SUPER_POTION);

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_HYPER_POTION).getCount() > 0)
			return usePotion(ItemId.ITEM_HYPER_POTION);

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_MAX_POTION).getCount() > 0)
			return usePotion(ItemId.ITEM_MAX_POTION);

		return UseItemPotionResponseOuterClass.UseItemPotionResponse.Result.ERROR_CANNOT_USE;
	}

	/**
	 * use a potion on that pokemon. Will check if there is enough potions & if the pokemon need
	 * to be healed.
	 *
	 * @return Result, ERROR_CANNOT_USE if the requirements arent met
	 */
	public UseItemPotionResponseOuterClass.UseItemPotionResponse.Result usePotion(ItemId itemId)
			throws LoginFailedException, RemoteServerException {

		Item potion = pgo.getInventories().getItemBag().getItem(itemId);
		//some sanity check, to prevent wrong use of this call
		if (!potion.isPotion() || potion.getCount() < 1 || !isInjured())
			return UseItemPotionResponseOuterClass.UseItemPotionResponse.Result.ERROR_CANNOT_USE;

		UseItemPotionMessageOuterClass.UseItemPotionMessage reqMsg = UseItemPotionMessageOuterClass.UseItemPotionMessage
				.newBuilder()
				.setItemId(itemId)
				.setPokemonId(getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.USE_ITEM_POTION, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		UseItemPotionResponseOuterClass.UseItemPotionResponse response;
		try {
			response = UseItemPotionResponseOuterClass.UseItemPotionResponse.parseFrom(serverRequest.getData());
			if (response.getResult() == UseItemPotionResponseOuterClass.UseItemPotionResponse.Result.SUCCESS) {
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
	 */
	public UseItemReviveResponseOuterClass.UseItemReviveResponse.Result revive()
			throws LoginFailedException, RemoteServerException {

		if (!isFainted())
			return UseItemReviveResponseOuterClass.UseItemReviveResponse.Result.ERROR_CANNOT_USE;

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_REVIVE).getCount() > 0)
			return useRevive(ItemId.ITEM_REVIVE);

		if (pgo.getInventories().getItemBag().getItem(ItemId.ITEM_MAX_REVIVE).getCount() > 0)
			return useRevive(ItemId.ITEM_MAX_REVIVE);

		return UseItemReviveResponseOuterClass.UseItemReviveResponse.Result.ERROR_CANNOT_USE;
	}

	/**
	 * Use a revive item on the pokemon. Will check if there is enough revive & if the pokemon need
	 * to be revived.
	 *
	 * @return Result, ERROR_CANNOT_USE if the requirements arent met
	 */
	public UseItemReviveResponseOuterClass.UseItemReviveResponse.Result useRevive(ItemId itemId)
			throws LoginFailedException, RemoteServerException {

		Item item = pgo.getInventories().getItemBag().getItem(itemId);
		if (!item.isRevive() || item.getCount() < 1 || !isFainted())
			return UseItemReviveResponseOuterClass.UseItemReviveResponse.Result.ERROR_CANNOT_USE;

		UseItemReviveMessageOuterClass.UseItemReviveMessage reqMsg = UseItemReviveMessageOuterClass.UseItemReviveMessage
				.newBuilder()
				.setItemId(itemId)
				.setPokemonId(getId())
				.build();

		ServerRequest serverRequest = new ServerRequest(RequestType.USE_ITEM_REVIVE, reqMsg);
		pgo.getRequestHandler().sendServerRequests(serverRequest);

		UseItemReviveResponseOuterClass.UseItemReviveResponse response;
		try {
			response = UseItemReviveResponseOuterClass.UseItemReviveResponse.parseFrom(serverRequest.getData());
			if (response.getResult() == UseItemReviveResponseOuterClass.UseItemReviveResponse.Result.SUCCESS) {
				setStamina(response.getStamina());
			}
			return response.getResult();
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

}
