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

import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_GREAT_BALL;
import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_MASTER_BALL;
import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_POKE_BALL;
import static POGOProtos.Inventory.Item.ItemIdOuterClass.ItemId.ITEM_ULTRA_BALL;
import static com.pokegoapi.api.inventory.Pokeball.GREATBALL;
import static com.pokegoapi.api.inventory.Pokeball.MASTERBALL;
import static com.pokegoapi.api.inventory.Pokeball.POKEBALL;
import static com.pokegoapi.api.inventory.Pokeball.ULTRABALL;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by LoungeKatt on 8/16/16.
 */

@ToString
public class AsyncCatchOptions {
	
	private final PokemonGo api;
	private boolean useBestPokeball;
	private boolean skipMasterBall;
	@Getter
	private int useRazzBerry;
	private Pokeball pokeBall;
	private boolean strictBallType;
	
	/**
	 * Instantiates a new CatchOptions object.
	 *
	 * @param api   the api
	 */
	public AsyncCatchOptions(PokemonGo api) {
		this.api = api;
		this.useRazzBerry = 0;
		this.useBestPokeball = false;
		this.skipMasterBall = false;
		this.pokeBall = POKEBALL;
		this.strictBallType = false;
	}
	
	/**
	 * Gets item ball to catch a pokemon
	 *
	 * @return the item ball
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 * @throws NoSuchItemException   the no such item exception
	 */
	public Pokeball getItemBall() throws LoginFailedException,
						RemoteServerException, NoSuchItemException {
		ItemBag bag = api.getInventories().getItemBag();
		if (strictBallType) {
			if (bag.getItem(pokeBall.getBallType()).getCount() > 0) {
				return pokeBall;
			} else if (useBestPokeball) {
				if (!skipMasterBall) {
					if (bag.getItem(ITEM_MASTER_BALL).getCount() > 0) {
						return MASTERBALL;
					}
				} else if (bag.getItem(ITEM_ULTRA_BALL).getCount() > 0) {
					return ULTRABALL;
				}
			} else if (bag.getItem(ITEM_POKE_BALL).getCount() > 0) {
				return POKEBALL;
			}
			throw new NoSuchItemException();
		} else {
			int index = 3;
			if (pokeBall.getBallType() == ITEM_MASTER_BALL) index = 3;
			if (pokeBall.getBallType() == ITEM_ULTRA_BALL) index = 2;
			if (pokeBall.getBallType() == ITEM_GREAT_BALL) index = 1;
			if (pokeBall.getBallType() == ITEM_POKE_BALL) index = 0;
			
			if (useBestPokeball) {
				if (!skipMasterBall && index <= 3 && bag.getItem(ITEM_MASTER_BALL).getCount() > 0) {
					return MASTERBALL;
				} else if (index <= 2 && bag.getItem(ITEM_ULTRA_BALL).getCount() > 0) {
					return ULTRABALL;
				} else if (index <= 1 && bag.getItem(ITEM_GREAT_BALL).getCount() > 0) {
					return GREATBALL;
				} else if (bag.getItem(ITEM_POKE_BALL).getCount() > 0) {
					return POKEBALL;
				}
			} else {
				if (index >= 0 && bag.getItem(ITEM_POKE_BALL).getCount() > 0) {
					return POKEBALL;
				} else if (index >= 1 && bag.getItem(ITEM_GREAT_BALL).getCount() > 0) {
					return GREATBALL;
				} else if (index >= 2 && bag.getItem(ITEM_ULTRA_BALL).getCount() > 0) {
					return ULTRABALL;
				} else if (!skipMasterBall && index <= 0 && bag.getItem(ITEM_MASTER_BALL).getCount() > 0) {
					return MASTERBALL;
				}
			}
			throw new NoSuchItemException();
		}
	}
	
	/**
	 * Enable or disable the use of razzberries
	 *
	 * @param useRazzBerries true or false
	 * @return               the AsyncCatchOptions object
	 */
	public AsyncCatchOptions useRazzberries(boolean useRazzBerries) {
		this.useRazzBerry = useRazzBerries ? 1 : 0;
		return this;
	}
	
	/**
	 * Set a specific Pokeball to use
	 *
	 * @param pokeBall the pokeball to use
	 * @return         the AsyncCatchOptions object
	 */
	public AsyncCatchOptions usePokeball(Pokeball pokeBall) {
		this.pokeBall = pokeBall;
		return this;
	}
	
	/**
	 * Set using the best available ball
	 *
	 * @param useBestPokeball true or false
	 * @return                the AsyncCatchOptions object
	 */
	public AsyncCatchOptions useBestBall(boolean useBestPokeball) {
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
	 * @return                the AsyncCatchOptions object
	 */
	public AsyncCatchOptions noFallback(boolean strictBallType) {
		this.strictBallType = strictBallType;
		return this;
	}
	
	/**
	 * Set whether or not Master balls can be used
	 *
	 * @param skipMasterBall true or false
	 * @return               the AsyncCatchOptions object
	 */
	public AsyncCatchOptions noMasterBall(boolean skipMasterBall) {
		this.skipMasterBall = skipMasterBall;
		return this;
	}

}
