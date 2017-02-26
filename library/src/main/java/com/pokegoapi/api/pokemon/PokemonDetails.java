package com.pokegoapi.api.pokemon;

import POGOProtos.Data.PokemonDataOuterClass.PokemonData;
import POGOProtos.Data.PokemonDisplayOuterClass.PokemonDisplay;
import POGOProtos.Enums.PokemonFamilyIdOuterClass;
import POGOProtos.Enums.PokemonIdOuterClass;
import POGOProtos.Enums.PokemonMoveOuterClass.PokemonMove;
import POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import POGOProtos.Settings.Master.Pokemon.EvolutionBranchOuterClass.EvolutionBranch;
import POGOProtos.Settings.Master.Pokemon.StatsAttributesOuterClass;
import POGOProtos.Settings.Master.PokemonSettingsOuterClass;

import java.util.List;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.main.PokemonMeta;
import com.pokegoapi.util.Log;

public class PokemonDetails {
	private static final String TAG = Pokemon.class.getSimpleName();
	protected final PokemonGo api;
	private PokemonSettingsOuterClass.PokemonSettings settings;
	private long id;
	private PokemonIdOuterClass.PokemonId pokemonId;
	private int cp;
	private int maxStamina;
	private int stamina;
	private PokemonMove move1;
	private PokemonMove move2;
	private String deployedFortId;
	private String ownerName;
	private boolean isEgg;
	private double eggKmWalkedTarget;
	private double eggKmWalkedStart;
	private int origin;
	private float height;
	private float weight;
	private int individualAttack;
	private int individualDefense;
	private int individualStamina;
	private float cpMultiplier;
	private float additionalCpMultiplier;
	private ItemId pokeball;
	private long capturedCellId;
	private int battlesAttacked;
	private int battlesDefended;
	private String eggIncubatorId;
	private long creationTimeMs;
	private int favorite;
	private String nickname;
	private int fromFort;
	private String protoData;
	private int numUpgrades;
	private PokemonDisplay pokemonDisplay;
	private int buddyCandyAwarded;
	private float buddyTotalKmWalked;

	public PokemonDetails(PokemonGo api, PokemonData proto) {
		this.api = api;
		this.applyProto(proto);
	}

	/**
	 * Applies the given PokemonData proto to these PokemonDetails
	 * @param proto the proto to apply
	 */
	public void applyProto(PokemonData proto) {
		id = proto.getId();
		pokemonId = proto.getPokemonId();
		cp = proto.getCp();
		maxStamina = proto.getStaminaMax();
		stamina = proto.getStamina();
		move1 = proto.getMove1();
		move2 = proto.getMove2();
		deployedFortId = proto.getDeployedFortId();
		ownerName = proto.getOwnerName();
		isEgg = proto.getIsEgg();
		eggKmWalkedTarget = proto.getEggKmWalkedTarget();
		eggKmWalkedStart = proto.getEggKmWalkedStart();
		origin = proto.getOrigin();
		height = proto.getHeightM();
		weight = proto.getWeightKg();
		individualAttack = proto.getIndividualAttack();
		individualDefense = proto.getIndividualDefense();
		individualStamina = proto.getIndividualStamina();
		cpMultiplier = proto.getCpMultiplier();
		additionalCpMultiplier = proto.getAdditionalCpMultiplier();
		pokeball = proto.getPokeball();
		capturedCellId = proto.getCapturedCellId();
		battlesAttacked = proto.getBattlesAttacked();
		battlesDefended = proto.getBattlesDefended();
		eggIncubatorId = proto.getEggIncubatorId();
		creationTimeMs = proto.getCreationTimeMs();
		favorite = proto.getFavorite();
		nickname = proto.getNickname();
		fromFort = proto.getFromFort();
		numUpgrades = proto.getNumUpgrades();
		pokemonDisplay = proto.getPokemonDisplay();
		buddyCandyAwarded = proto.getBuddyCandyAwarded();
		buddyTotalKmWalked = proto.getBuddyTotalKmWalked();
		protoData = proto.toString();
	}

	/**
	 * @return the amount of candy available for this pokemon
	 */
	public int getCandy() {
		return api.getInventories().getCandyjar().getCandies(getPokemonFamily());
	}

	public PokemonFamilyIdOuterClass.PokemonFamilyId getPokemonFamily() {
		return getSettings().getFamilyId();
	}

	public PokemonData getDefaultInstanceForType() {
		return PokemonData.getDefaultInstance();
	}

	public long getId() {
		return id;
	}

	public PokemonIdOuterClass.PokemonId getPokemonId() {
		return pokemonId;
	}

	public int getCp() {
		return cp;
	}

	public int getMaxStamina() {
		return maxStamina;
	}

	public int getStamina() {
		return stamina;
	}

	public PokemonMove getMove1() {
		return move1;
	}

	public PokemonMove getMove2() {
		return move2;
	}

	public String getDeployedFortId() {
		return deployedFortId;
	}

	public boolean isDeployed() {
		return deployedFortId != null && deployedFortId.trim().length() > 0;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public boolean isEgg() {
		return isEgg;
	}

	public double getEggKmWalkedTarget() {
		return eggKmWalkedTarget;
	}

	public double getEggKmWalkedStart() {
		return eggKmWalkedStart;
	}

	public int getOrigin() {
		return origin;
	}

	public float getHeightM() {
		return height;
	}

	public float getWeightKg() {
		return weight;
	}

	public int getIndividualAttack() {
		return individualAttack;
	}

	public int getIndividualDefense() {
		return individualDefense;
	}

	public int getIndividualStamina() {
		return individualStamina;
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
		return cpMultiplier;
	}

	public float getAdditionalCpMultiplier() {
		return additionalCpMultiplier;
	}

	public float getCombinedCpMultiplier() {
		return getCpMultiplier() + getAdditionalCpMultiplier();
	}

	public ItemId getPokeball() {
		return pokeball;
	}

	public long getCapturedS2CellId() {
		return capturedCellId;
	}

	public int getBattlesAttacked() {
		return battlesAttacked;
	}

	public int getBattlesDefended() {
		return battlesDefended;
	}

	public String getEggIncubatorId() {
		return eggIncubatorId;
	}

	public long getCreationTimeMs() {
		return creationTimeMs;
	}

	/**
	 * Checks whether the Pokemon is set as favorite.
	 *
	 * @return true if the Pokemon is set as favorite
	 */
	public boolean isFavorite() {
		return favorite > 0;
	}

	@Deprecated
	public boolean getFavorite() {
		return favorite > 0;
	}

	public String getNickname() {
		return nickname;
	}

	public boolean getFromFort() {
		return fromFort > 0;
	}

	public void debug() {
		Log.d(TAG, protoData);
	}

	public int getBaseStamina() {
		return getSettings().getStats().getBaseStamina();
	}

	public double getBaseCaptureRate() {
		return getSettings().getEncounter().getBaseCaptureRate();
	}

	/**
	 * Return the amount of candies necessary to evolve this pokemon
	 * @return candy needed to evolve
	 */
	public int getCandiesToEvolve() {
		Evolution evolution = Evolutions.getEvolution(pokemonId);
		if (evolution.getEvolutionBranch() != null && evolution.getEvolutionBranch().size() > 0) {
			return evolution.getEvolutionBranch().get(0).getCandyCost();
		}
		return 0;
	}
	
	public List<EvolutionBranch> getEvolutionBranch() {
		Evolution evolution = Evolutions.getEvolution(pokemonId);
		return evolution.getEvolutionBranch();
	}

	public double getBaseFleeRate() {
		return getSettings().getEncounter().getBaseFleeRate();
	}

	public float getLevel() {
		return PokemonCpUtils.getLevelFromCpMultiplier(getCombinedCpMultiplier());
	}

	/**
	 * Get the settings for a pokemon.
	 *
	 * @return PokemonSettings
	 */
	public PokemonSettingsOuterClass.PokemonSettings getSettings() {
		if (settings == null) {
			settings = PokemonMeta.getPokemonSettings(pokemonId);
		}

		return settings;
	}

	/**
	 * Calculate the maximum CP for this individual pokemon when the player is at level 40
	 *
	 * @return The maximum CP for this pokemon
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link PokemonMeta}.
	 */
	public int getMaxCp() throws NoSuchItemException {
		if (settings == null) {
			throw new NoSuchItemException("Cannot find meta data for " + pokemonId.name());
		}
		int attack = getIndividualAttack() + settings.getStats().getBaseAttack();
		int defense = getIndividualDefense() + settings.getStats().getBaseDefense();
		int stamina = getIndividualStamina() + settings.getStats().getBaseStamina();
		return PokemonCpUtils.getMaxCp(attack, defense, stamina);
	}

	/**
	 * Calculate the maximum CP for this individual pokemon and this player's level
	 *
	 * @return The maximum CP for this pokemon
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link PokemonMeta}.
	 */
	public int getMaxCpForPlayer() throws NoSuchItemException {
		if (settings == null) {
			throw new NoSuchItemException("Cannot find meta data for " + pokemonId.name());
		}
		int attack = getIndividualAttack() + settings.getStats().getBaseAttack();
		int defense = getIndividualDefense() + settings.getStats().getBaseDefense();
		int stamina = getIndividualStamina() + settings.getStats().getBaseStamina();
		int playerLevel = api.getPlayerProfile().getStats().getLevel();
		return PokemonCpUtils.getMaxCpForPlayer(attack, defense, stamina, playerLevel);
	}

	/**
	 * Calculates the absolute maximum CP for all pokemons with this PokemonId
	 *
	 * @return The absolute maximum CP
	 * @throws NoSuchItemException If the PokemonId value cannot be found in the {@link PokemonMeta}.
	 */
	public int getAbsoluteMaxCp() throws NoSuchItemException {
		return PokemonCpUtils.getAbsoluteMaxCp(getPokemonId());
	}

	/**
	 * Calculated the max cp of this pokemon, if you upgrade it fully and the player is at level 40
	 *
	 * @param highestEvolution the full evolution path
	 * @return Max cp of this pokemon
	 */
	public int getCpFullEvolveAndPowerup(PokemonIdOuterClass.PokemonId highestEvolution) {
		return getMaxCpFullEvolveAndPowerup(40, highestEvolution);
	}

	/**
	 * Calculated the max cp of this pokemon, if you upgrade it fully with your current player level
	 *
	 * @param highestEvolution the full evolution path
	 * @return Max cp of this pokemon
	 */
	public int getMaxCpFullEvolveAndPowerupForPlayer(PokemonIdOuterClass.PokemonId highestEvolution) {
		return getMaxCpFullEvolveAndPowerup(api.getPlayerProfile().getStats().getLevel(), highestEvolution);
	}

	/**
	 * Calculated the max cp of this pokemon, if you upgrade it fully with your current player level
	 *
	 * @param playerLevel the current player level
	 * @param highestEvolution the full evolution path
	 * @return Max cp of this pokemon
	 */
	private int getMaxCpFullEvolveAndPowerup(int playerLevel, PokemonIdOuterClass.PokemonId highestEvolution) {
		PokemonSettingsOuterClass.PokemonSettings settings = PokemonMeta.getPokemonSettings(highestEvolution);
		StatsAttributesOuterClass.StatsAttributes stats = settings.getStats();
		int attack = getIndividualAttack() + stats.getBaseAttack();
		int defense = getIndividualDefense() + stats.getBaseDefense();
		int stamina = getIndividualStamina() + stats.getBaseStamina();
		return PokemonCpUtils.getMaxCpForPlayer(attack, defense, stamina, playerLevel);
	}

	/**
	 * Calculate the CP after evolving this Pokemon
	 *
	 * @param evolution the pokemon evolving into
	 * @return New CP after evolve
	 */
	public int getCpAfterEvolve(PokemonIdOuterClass.PokemonId evolution) {
		PokemonSettingsOuterClass.PokemonSettings settings = PokemonMeta.getPokemonSettings(evolution);
		StatsAttributesOuterClass.StatsAttributes stats = settings.getStats();
		int attack = getIndividualAttack() + stats.getBaseAttack();
		int defense = getIndividualDefense() + stats.getBaseDefense();
		int stamina = getIndividualStamina() + stats.getBaseStamina();
		return PokemonCpUtils.getCp(attack, defense, stamina, getCombinedCpMultiplier());
	}

	/**
	 * Calculate the CP after fully evolving this Pokemon
	 *
	 * @param highestEvolution the pokemon at the top of the evolution chain being evolved into
	 * @return New CP after evolve
	 */
	public int getCpAfterFullEvolve(PokemonIdOuterClass.PokemonId highestEvolution) {
		PokemonSettingsOuterClass.PokemonSettings settings = PokemonMeta.getPokemonSettings(highestEvolution);
		StatsAttributesOuterClass.StatsAttributes stats = settings.getStats();
		int attack = getIndividualAttack() + stats.getBaseAttack();
		int defense = getIndividualDefense() + stats.getBaseDefense();
		int stamina = getIndividualStamina() + stats.getBaseStamina();
		return PokemonCpUtils.getCp(attack, defense, stamina, getCombinedCpMultiplier());
	}

	/**
	 * @return The number of powerups already done
	 */
	public int getNumerOfPowerupsDone() {
		return numUpgrades;
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
		return PokemonCpUtils.getCandyCostsForPowerup(getCombinedCpMultiplier());
	}

	/**
	 * @return Cost of stardust for a powerup
	 */
	public int getStardustCostsForPowerup() {
		return PokemonCpUtils.getStartdustCostsForPowerup(getCombinedCpMultiplier());
	}

	/**
	 * @return Information about Costumes, Shiny and Gender
	 */
	public PokemonDisplay getPokemonDisplay() {
		return pokemonDisplay;
	}

	/**
	 * @return The amount of candy awarded by Buddy
	 */
	public int getBuddyCandyAwarded() {
		return buddyCandyAwarded;
	}

	/**
	 * @return The amount of km walked by Buddy
	 */
	public float getBuddyTotalKmWalked() {
		return buddyTotalKmWalked;
	}
}
