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
import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import lombok.Getter;
import lombok.Setter;

import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.EEVEE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.FLAREON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.JOLTEON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VAPOREON;
import static java.util.Arrays.asList;

/**
 * All pokemon details
 */
public class PokemonDetails {
	private static final String TAG = Pokemon.class.getSimpleName();
	@Getter
	@Setter
	private PokemonData proto;
	@Getter
	private final PokemonMeta meta;
	private final Inventories inventories;

	/**
	 * Constructor for details
	 * @param proto Pokemon data
	 * @param inventories Inventories
	 */
	protected PokemonDetails(PokemonData proto, Inventories inventories) {
		this.proto = proto;
		this.meta = PokemonMetaRegistry.getMeta(this.getPokemonId());
		this.inventories = inventories;
	}

	public int getCandy() throws LoginFailedException, RemoteServerException {
		return inventories.getCandyjar().getCandies(getPokemonFamily());
	}

	public PokemonFamilyIdOuterClass.PokemonFamilyId getPokemonFamily() {
		return getMeta().getFamily();
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

	public PokemonMove getMove1() {
		return proto.getMove1();
	}

	public PokemonMove getMove2() {
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

	public float getWeightKg() {
		return proto.getWeightKg();
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

	public float getAdditionalCpMultiplier() {
		return proto.getAdditionalCpMultiplier();
	}

	public float getCombinedCpMultiplier() {
		return getCpMultiplier() + getAdditionalCpMultiplier();
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

	public String getNickname() {
		return proto.getNickname();
	}

	public boolean getFromFort() {
		return proto.getFromFort() > 0;
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
		return PokemonCpUtils.getLevelFromCpMultiplier(getCombinedCpMultiplier());
	}

	/**
	 * Calculate the maximum CP for this individual pokemon when the player is at level 40
	 *
	 * @return The maximum CP for this pokemon
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link PokemonMetaRegistry}.
	 */
	public int getMaxCp() throws NoSuchItemException {
		PokemonMeta pokemonMeta = PokemonMetaRegistry.getMeta(proto.getPokemonId());
		if (pokemonMeta == null) {
			throw new NoSuchItemException("Cannot find meta data for " + proto.getPokemonId().name());
		}
		int attack = getIndividualAttack() + pokemonMeta.getBaseAttack();
		int defense = getIndividualDefense() + pokemonMeta.getBaseDefense();
		int stamina = getIndividualStamina() + pokemonMeta.getBaseStamina();
		return PokemonCpUtils.getMaxCp(attack, defense, stamina);
	}

	/**
	 * Calculate the maximum CP for this individual pokemon and this player's level
	 *
	 * @param playerLevel Player level to use in calculations
	 * @return The maximum CP for this pokemon
	 * @throws NoSuchItemException   If the PokemonId value cannot be found in the {@link PokemonMetaRegistry}.
	 */
	public int getMaxCpForPlayer(int playerLevel) throws NoSuchItemException {
		PokemonMeta pokemonMeta = PokemonMetaRegistry.getMeta(proto.getPokemonId());
		if (pokemonMeta == null) {
			throw new NoSuchItemException("Cannot find meta data for " + proto.getPokemonId().name());
		}
		int attack = getIndividualAttack() + pokemonMeta.getBaseAttack();
		int defense = getIndividualDefense() + pokemonMeta.getBaseDefense();
		int stamina = getIndividualStamina() + pokemonMeta.getBaseStamina();
		return PokemonCpUtils.getMaxCpForPlayer(attack, defense, stamina, playerLevel);
	}

	/**
	 * Calculates the absolute maximum CP for all pokemons with this PokemonId
	 *
	 * @return The absolute maximum CP
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link PokemonMetaRegistry}.
	 */
	public int getAbsoluteMaxCp() throws NoSuchItemException {
		return PokemonCpUtils.getAbsoluteMaxCp(getPokemonId());
	}

	/**
	 * Calculated the max cp of this pokemon, if you upgrade it fully and the player is at level 40
	 *
	 * @return Max cp of this pokemon
	 */
	public int getCpFullEvolveAndPowerup()  {
		return getMaxCpFullEvolveAndPowerup(40);
	}

	/**
	 * Calculated the max cp of this pokemon, if you upgrade it fully with your current player level
	 *
	 * @param playerLevel Player level to use in calculations
	 * @return Max cp of this pokemon
	 */
	public int getMaxCpFullEvolveAndPowerupForPlayer(int playerLevel) {
		return getMaxCpFullEvolveAndPowerup(playerLevel);
	}

	/**
	 * Calculated the max cp of this pokemon, if you upgrade it fully with your current player level
	 *
	 * @param playerLevel Player level to use in calculations
	 * @return Max cp of this pokemon
	 */
	private int getMaxCpFullEvolveAndPowerup(int playerLevel) {
		PokemonIdOuterClass.PokemonId highestUpgradedFamily;
		if (asList(VAPOREON, JOLTEON, FLAREON).contains(getPokemonId())) {
			highestUpgradedFamily = getPokemonId();
		} else if (getPokemonId() == EEVEE) {
			highestUpgradedFamily = FLAREON;
		} else {
			highestUpgradedFamily = PokemonMetaRegistry.getHighestForFamily(getPokemonFamily());
		}
		PokemonMeta pokemonMeta = PokemonMetaRegistry.getMeta(highestUpgradedFamily);
		int attack = getIndividualAttack() + pokemonMeta.getBaseAttack();
		int defense = getIndividualDefense() + pokemonMeta.getBaseDefense();
		int stamina = getIndividualStamina() + pokemonMeta.getBaseStamina();
		return PokemonCpUtils.getMaxCpForPlayer(attack, defense, stamina, playerLevel);
	}

	/**
	 * Calculate the CP after evolving this Pokemon
	 *
	 * @return New CP after evolve
	 */
	public int getCpAfterEvolve() {
		if (asList(VAPOREON, JOLTEON, FLAREON).contains(getPokemonId())) {
			return getCp();
		}
		PokemonIdOuterClass.PokemonId highestUpgradedFamily = PokemonMetaRegistry.getHighestForFamily(getPokemonFamily());
		if (getPokemonId() == highestUpgradedFamily) {
			return getCp();
		}
		PokemonMeta pokemonMeta = PokemonMetaRegistry.getMeta(highestUpgradedFamily);
		PokemonIdOuterClass.PokemonId secondHighest = pokemonMeta.getParentId();
		if (getPokemonId() == secondHighest) {
			int attack = getIndividualAttack() + pokemonMeta.getBaseAttack();
			int defense = getIndividualDefense() + pokemonMeta.getBaseDefense();
			int stamina = getIndividualStamina() + pokemonMeta.getBaseStamina();
			return PokemonCpUtils.getCp(attack, defense, stamina, getCombinedCpMultiplier());
		}
		pokemonMeta = PokemonMetaRegistry.getMeta(secondHighest);
		int attack = getIndividualAttack() + pokemonMeta.getBaseAttack();
		int defense = getIndividualDefense() + pokemonMeta.getBaseDefense();
		int stamina = getIndividualStamina() + pokemonMeta.getBaseStamina();
		return PokemonCpUtils.getCp(attack, defense, stamina, getCombinedCpMultiplier());
	}

	/**
	 * Calculate the CP after fully evolving this Pokemon
	 *
	 * @return New CP after evolve
	 */
	public int getCpAfterFullEvolve() {
		if (asList(VAPOREON, JOLTEON, FLAREON).contains(getPokemonId())) {
			return getCp();
		}
		PokemonIdOuterClass.PokemonId highestUpgradedFamily = PokemonMetaRegistry.getHighestForFamily(getPokemonFamily());
		if (getPokemonId() == highestUpgradedFamily) {
			return getCp();
		}
		PokemonMeta pokemonMeta = PokemonMetaRegistry.getMeta(highestUpgradedFamily);
		int attack = getProto().getIndividualAttack() + pokemonMeta.getBaseAttack();
		int defense = getProto().getIndividualDefense() + pokemonMeta.getBaseDefense();
		int stamina = getProto().getIndividualStamina() + pokemonMeta.getBaseStamina();
		return PokemonCpUtils.getCp(attack, defense, stamina, getCombinedCpMultiplier());
	}

	/**
	 * @return The number of powerups already done
	 */
	public int getNumerOfPowerupsDone() {
		return getProto().getNumUpgrades();
	}

	/**
	 * @return The CP for this pokemon after powerup
	 */
	public int getCpAfterPowerup() {
		return PokemonCpUtils.getCpAfterPowerup(getCp(), getCombinedCpMultiplier());
	}

	/**
	 * @return Cost of candy for a powerup
	 */
	public int getCandyCostsForPowerup() {
		return PokemonCpUtils.getCandyCostsForPowerup(getCombinedCpMultiplier(), getNumerOfPowerupsDone());
	}

	/**
	 * @return Cost of stardust for a powerup
	 */
	public int getStardustCostsForPowerup() {
		return PokemonCpUtils.getStartdustCostsForPowerup(getCombinedCpMultiplier(), getNumerOfPowerupsDone());
	}
}
