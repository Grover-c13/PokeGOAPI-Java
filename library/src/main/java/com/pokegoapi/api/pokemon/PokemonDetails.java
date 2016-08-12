package com.pokegoapi.api.pokemon;

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.util.Log;
import lombok.Getter;
import lombok.Setter;

import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.EEVEE;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.FLAREON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.JOLTEON;
import static POGOProtos.Enums.PokemonIdOuterClass.PokemonId.VAPOREON;
import static java.util.Arrays.asList;

public class PokemonDetails {
	private static final String TAG = Pokemon.class.getSimpleName();
	private PokemonGo api;
	@Getter
	@Setter
	private PokemonData proto;
	private PokemonMeta meta;

	public PokemonDetails(PokemonGo api, PokemonData proto) {
		this.api = api;
		this.proto = proto;
	}



	public int getCandy() throws LoginFailedException, RemoteServerException {
		return api.getInventories().getCandyjar().getCandies(getPokemonFamily());
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
		Log.d(TAG, proto.toString());
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

	/**
	 * Calculate the maximum CP for this individual pokemon when the player is at level 40
	 *
	 * @return The maximum CP for this pokemon
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link PokemonMetaRegistry}.
	 * @throws LoginFailedException If login failed
	 * @throws RemoteServerException If the server is causing issues
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
	 * Calculate the maximum CP for this individual pokemon and this player's level
	 *
	 * @return The maximum CP for this pokemon
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link PokemonMetaRegistry}.
	 * @throws LoginFailedException If login failed
	 * @throws RemoteServerException If the server is causing issues
	 */
	public int getMaxCpForPlayer() throws NoSuchItemException, LoginFailedException, RemoteServerException {
		PokemonMeta pokemonMeta = PokemonMetaRegistry.getMeta(proto.getPokemonId());
		if (pokemonMeta == null) {
			throw new NoSuchItemException("Cannot find meta data for " + proto.getPokemonId().name());
		}
		int attack = proto.getIndividualAttack() + pokemonMeta.getBaseAttack();
		int defense = proto.getIndividualDefense() + pokemonMeta.getBaseDefense();
		int stamina = proto.getIndividualStamina() + pokemonMeta.getBaseStamina();
		int playerLevel = api.getPlayerProfile().getStats().getLevel();
		return PokemonCpUtils.getMaxCpForPlayer(attack, defense, stamina, playerLevel);
	}

	/**
	 * Calculates the absolute maximum CP for all pokemons with this PokemonId
	 *
	 * @return The absolute maximum CP
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link PokemonMetaRegistry}.
	 */
	public int getAbsoluteMaxCp() throws NoSuchItemException {
		return getAbsoluteMaxCp(proto.getPokemonId());
	}

	/**
	 * Static helper to get the absolute maximum CP for pokemons with their PokemonId.
	 * @param id The {@link PokemonIdOuterClass.PokemonId} of the Pokemon to get CP for.
	 * @return The absolute maximum CP
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link PokemonMetaRegistry}.
	 */
	public static int getAbsoluteMaxCp(PokemonIdOuterClass.PokemonId id) throws NoSuchItemException {
		PokemonMeta pokemonMeta = PokemonMetaRegistry.getMeta(id);
		if (pokemonMeta == null) {
			throw new NoSuchItemException("Cannot find meta data for " + id);
		}
		int attack = 15 + pokemonMeta.getBaseAttack();
		int defense = 15 + pokemonMeta.getBaseDefense();
		int stamina = 15 + pokemonMeta.getBaseStamina();
		return PokemonCpUtils.getMaxCpForPlayer(attack, defense, stamina, 40);
	}

	/**
	 * Calculated the max cp of this pokemon, if you upgrade it fully and the player is at level 40
	 * @return Max cp of this pokemon
	 */
	public int getCpFullEvolveAndPowerup() throws LoginFailedException, RemoteServerException {
		return getMaxCpFullEvolveAndPowerup(40);
	}

	/**
	 * Calculated the max cp of this pokemon, if you upgrade it fully with your current player level
	 * @return Max cp of this pokemon
	 */
	public int getMaxCpFullEvolveAndPowerupForPlayer() throws LoginFailedException, RemoteServerException {
		return getMaxCpFullEvolveAndPowerup(api.getPlayerProfile().getStats().getLevel());
	}

	/**
	 * Calculated the max cp of this pokemon, if you upgrade it fully with your current player level
	 * @return Max cp of this pokemon
	 */
	private int getMaxCpFullEvolveAndPowerup(int playerLevel) {
		PokemonIdOuterClass.PokemonId highestUpgradedFamily;
		if (getPokemonId() == EEVEE) {
			highestUpgradedFamily = FLAREON;
		} else {
			highestUpgradedFamily = PokemonMetaRegistry.getHightestForFamily(getPokemonFamily());
		}
		PokemonMeta pokemonMeta = PokemonMetaRegistry.getMeta(highestUpgradedFamily);
		int attack = getProto().getIndividualAttack() + pokemonMeta.getBaseAttack();
		int defense = getProto().getIndividualDefense() + pokemonMeta.getBaseDefense();
		int stamina = getProto().getIndividualStamina() + pokemonMeta.getBaseStamina();
		return PokemonCpUtils.getMaxCpForPlayer(attack, defense, stamina, playerLevel);
	}

	/**
	 * Calculate the CP after evolving this Pokemon
	 * @return New CP after evolve
	 */
	public int getCpAfterEvolve() {
		if (asList(VAPOREON, JOLTEON, FLAREON).contains(getPokemonId())) {
			return getCp();
		}
		PokemonIdOuterClass.PokemonId highestUpgradedFamily = PokemonMetaRegistry.getHightestForFamily(getPokemonFamily());
		if (getPokemonId() == highestUpgradedFamily) {
			return getCp();
		}
		PokemonMeta pokemonMeta = PokemonMetaRegistry.getMeta(highestUpgradedFamily);
		PokemonIdOuterClass.PokemonId secondHighest = pokemonMeta.getParentId();
		float level = PokemonCpUtils.getLevelFromCpMultiplier(getCpMultiplier() + getAdditionalCpMultiplier());
		if (getPokemonId() == secondHighest) {
			int attack = getProto().getIndividualAttack() + pokemonMeta.getBaseAttack();
			int defense = getProto().getIndividualDefense() + pokemonMeta.getBaseDefense();
			int stamina = getProto().getIndividualStamina() + pokemonMeta.getBaseStamina();
			return PokemonCpUtils.getCp(attack, defense, stamina, level);
		}
		pokemonMeta = PokemonMetaRegistry.getMeta(secondHighest);
		int attack = getProto().getIndividualAttack() + pokemonMeta.getBaseAttack();
		int defense = getProto().getIndividualDefense() + pokemonMeta.getBaseDefense();
		int stamina = getProto().getIndividualStamina() + pokemonMeta.getBaseStamina();
		return PokemonCpUtils.getCp(attack, defense, stamina, level);
	}

	/**
	 * @return The number of powerups already done
	 */
	public int getNumerOfPowerupsDone() {
		return getProto().getNumUpgrades();
	}

}
