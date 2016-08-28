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

package com.pokegoapi.api.settings;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.ItemBag;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;

import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId;
import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_GREAT_BALL;
import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_MASTER_BALL;
import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_POKE_BALL;
import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_ULTRA_BALL;
import static com.pokegoapi.api.inventory.Pokeball.GREATBALL;
import static com.pokegoapi.api.inventory.Pokeball.MASTERBALL;
import static com.pokegoapi.api.inventory.Pokeball.POKEBALL;
import static com.pokegoapi.api.inventory.Pokeball.ULTRABALL;

import java.util.Arrays;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by LoungeKatt on 8/16/16.
 */

@ToString
public class CatchOptions {
	
	private final PokemonGo api;
	private boolean useBestPokeball;
	private boolean skipMasterBall;
	private boolean useRazzBerries;
	private int maxRazzBerries;
	private Pokeball pokeBall;
	private boolean strictBallType;
	private boolean smartSelect;
	@Getter
	private int maxPokeballs;
	private double probability;
	@Getter
	private double normalizedHitPosition;
	@Getter
	private double normalizedReticleSize;
	@Getter
	private double spinModifier;
	
	/**
	 * Instantiates a new CatchOptions object.
	 *
	 * @param api   the api
	 */
	public CatchOptions(PokemonGo api) {
		this.api = api;
		this.useRazzBerries = false;
		this.maxRazzBerries = 0;
		this.useBestPokeball = false;
		this.skipMasterBall = false;
		this.pokeBall = POKEBALL;
		this.strictBallType = false;
		this.smartSelect = false;
		this.maxPokeballs = 1;
		this.probability = 0.50;
		this.normalizedHitPosition = 1.0;
		this.normalizedReticleSize = 1.95 + Math.random() * 0.05;
		this.spinModifier = 0.85 + Math.random() * 0.15;
	}
	
	/**
	 * Gets item ball to catch a pokemon
	 *
	 * @return the item ball
	 * @throws NoSuchItemException   the no such item exception
	 */
	public Pokeball getItemBall() throws NoSuchItemException {
		ItemBag bag = api.getInventories().getItemBag();
		if (strictBallType) {
			if (bag.getItem(pokeBall.getBallType()).getCount() > 0) {
				return pokeBall;
			} else if (useBestPokeball) {
				if (!skipMasterBall && bag.getItem(ITEM_MASTER_BALL).getCount() > 0) {
					return MASTERBALL;
				} else if (bag.getItem(ITEM_ULTRA_BALL).getCount() > 0) {
					return ULTRABALL;
				} else if (bag.getItem(ITEM_GREAT_BALL).getCount() > 0) {
					return GREATBALL;
				}
			}
			if (bag.getItem(ITEM_POKE_BALL).getCount() > 0) {
				return POKEBALL;
			}
			throw new NoSuchItemException();
		} else {
			int index = Arrays.asList(new ItemId[] { ITEM_MASTER_BALL, ITEM_ULTRA_BALL,
					ITEM_GREAT_BALL, ITEM_POKE_BALL }).indexOf(pokeBall.getBallType());
			
			if (useBestPokeball) {
				if (!skipMasterBall && index >= 0 && bag.getItem(ITEM_MASTER_BALL).getCount() > 0) {
					return MASTERBALL;
				} else if (index >= 1 && bag.getItem(ITEM_ULTRA_BALL).getCount() > 0) {
					return ULTRABALL;
				} else if (index >= 2 && bag.getItem(ITEM_GREAT_BALL).getCount() > 0) {
					return GREATBALL;
				} else if (bag.getItem(ITEM_POKE_BALL).getCount() > 0) {
					return POKEBALL;
				}
			} else {
				if (index <= 3 && bag.getItem(ITEM_POKE_BALL).getCount() > 0) {
					return POKEBALL;
				} else if (index <= 2 && bag.getItem(ITEM_GREAT_BALL).getCount() > 0) {
					return GREATBALL;
				} else if (index <= 1 && bag.getItem(ITEM_ULTRA_BALL).getCount() > 0) {
					return ULTRABALL;
				} else if (!skipMasterBall && bag.getItem(ITEM_MASTER_BALL).getCount() > 0) {
					return MASTERBALL;
				}
			}
		}
		if (smartSelect) {
			useBestPokeball = false;
			skipMasterBall = false;
			smartSelect = false;
			return getItemBall();
		}
		throw new NoSuchItemException();
	}
	
	/**
	 * Gets item ball to catch a pokemon
	 *
	 * @param  encounterProbability  the capture probability to compare
	 * @return the item ball
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 * @throws NoSuchItemException   the no such item exception
	 */
	public Pokeball getItemBall(double encounterProbability) throws LoginFailedException,
						RemoteServerException, NoSuchItemException {
		if (encounterProbability >= probability) {
			useBestPokeball = false;
		} else {
			useBestPokeball = true;
		}
		return getItemBall();
	}
	
	/**
	 * Gets razzberries to catch a pokemon
	 *
	 * @return the number to use
	 */
	public int getRazzberries() {
		return useRazzBerries && maxRazzBerries == 0 ? 1 : maxRazzBerries;
	}
	
	/**
	 * Enable or disable the use of razzberries
	 *
	 * @param useRazzBerries true or false
	 * @return               the CatchOptions object
	 */
	public CatchOptions useRazzberries(boolean useRazzBerries) {
		this.useRazzBerries = useRazzBerries;
		return this;
	}
	
	/**
	 * Set a maximum number of razzberries
	 *
	 * @param maxRazzBerries maximum allowed
	 * @return               the CatchOptions object
	 */
	public CatchOptions maxRazzberries(int maxRazzBerries) {
		this.maxRazzBerries = maxRazzBerries;
		return this;
	}
	
	/**
	 * Set a specific Pokeball to use
	 *
	 * @param pokeBall the pokeball to use
	 * @return         the CatchOptions object
	 */
	public CatchOptions usePokeball(Pokeball pokeBall) {
		this.pokeBall = pokeBall;
		return this;
	}
	
	/**
	 * Set using the best available ball
	 *
	 * @param useBestPokeball true or false
	 * @return                the CatchOptions object
	 */
	public CatchOptions useBestBall(boolean useBestPokeball) {
		this.useBestPokeball = useBestPokeball;
		return this;
	}
	
	/**
	 * <pre>
	 * Set using only the defined ball type
	 *   combined with useBestBall: Sets the minimum
	 *   combined with usePokeball: Sets the maximum
	 *
	 *   without either will attempt the ball specified
	 *       or throw an error
	 * </pre>
	 * @param strictBallType  true or false
	 * @return                the CatchOptions object
	 */
	public CatchOptions noFallback(boolean strictBallType) {
		this.strictBallType = strictBallType;
		return this;
	}
	
	/**
	 * Set whether or not Master balls can be used
	 *
	 * @param skipMasterBall true or false
	 * @return               the CatchOptions object
	 */
	public CatchOptions noMasterBall(boolean skipMasterBall) {
		this.skipMasterBall = skipMasterBall;
		return this;
	}
	
	/**
	 * Set whether or not to use adaptive ball selection
	 *
	 * @param smartSelect    true or false
	 * @return               the CatchOptions object
	 */
	public CatchOptions useSmartSelect(boolean smartSelect) {
		this.smartSelect = smartSelect;
		return this;
	}
	
	/**
	 * Set a maximum number of pokeballs
	 *
	 * @param maxPokeballs maximum allowed
	 * @return             the CatchOptions object
	 */
	public CatchOptions maxPokeballs(int maxPokeballs) {
		if (maxPokeballs <= 1)
			maxPokeballs = -1;
		this.maxPokeballs = maxPokeballs;
		return this;
	}
	
	/**
	 * Set a capture probability before switching balls
	 *		or the minimum probability for a specific ball
	 *
	 * @param probability    the probability
	 * @return               the AsyncCatchOptions object
	 */
	public CatchOptions withProbability(double probability) {
		this.probability = probability;
		return this;
	}
	
	/**
	 * Set the normalized hit position of a pokeball throw
	 *
	 * @param normalizedHitPosition the normalized position
	 * @return                      the CatchOptions object
	 */
	public CatchOptions setNormalizedHitPosition(double normalizedHitPosition) {
		this.normalizedHitPosition = normalizedHitPosition;
		return this;
	}
	
	/**
	 * Set the normalized reticle for a pokeball throw
	 *
	 * @param normalizedReticleSize the normalized size
	 * @return                      the CatchOptions object
	 */
	public CatchOptions setNormalizedReticleSize(double normalizedReticleSize) {
		this.normalizedReticleSize = normalizedReticleSize;
		return this;
	}
	
	/**
	 * Set the spin modifier of a pokeball throw
	 *
	 * @param spinModifier the spin modifier
	 * @return             the CatchOptions object
	 */
	public CatchOptions setSpinModifier(double spinModifier) {
		this.spinModifier = spinModifier;
		return this;
	}

}
