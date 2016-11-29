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
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.exceptions.NoSuchItemException;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Created by LoungeKatt on 8/16/16.
 */

@ToString
public class CatchOptions {
	private final PokemonGo api;
	@Getter
	private boolean useRazzberry;
	private double probability;
	@Getter
	private double normalizedHitPosition;
	@Getter
	private double normalizedReticleSize;
	@Getter
	private double spinModifier;
	@Getter
	private PokeballSelector pokeballSelector;

	/**
	 * Instantiates a new CatchOptions object.
	 *
	 * @param api the api
	 */
	public CatchOptions(PokemonGo api) {
		this.api = api;
		this.useRazzberry = false;
		this.probability = 0;
		this.normalizedHitPosition = 1.0;
		this.normalizedReticleSize = 1.95 + Math.random() * 0.05;
		this.spinModifier = 0.85 + Math.random() * 0.15;
	}

	/**
	 * Sets this CatchOptions' pokeball selector
	 *
	 * @param selector the new selector
	 * @return the CatchOptions object
	 */
	public CatchOptions withPokeballSelector(PokeballSelector selector) {
		this.pokeballSelector = selector;
		return this;
	}

	/**
	 * Enable or disable the use of razzberries
	 *
	 * @param useRazzberry true or false
	 * @return the CatchOptions object
	 */
	public CatchOptions useRazzberry(boolean useRazzberry) {
		this.useRazzberry = useRazzberry;
		return this;
	}

	/**
	 * Set a capture probability before switching balls
	 * or the minimum probability for a specific ball
	 *
	 * @param probability the probability
	 * @return the CatchOptions object
	 */
	public CatchOptions withProbability(double probability) {
		this.probability = probability;
		return this;
	}

	/**
	 * Set the normalized hit position of a pokeball throw
	 *
	 * @param normalizedHitPosition the normalized position
	 * @return the AsynCatchOptions object
	 */
	public CatchOptions setNormalizedHitPosition(double normalizedHitPosition) {
		this.normalizedHitPosition = normalizedHitPosition;
		return this;
	}

	/**
	 * Set the normalized reticle for a pokeball throw
	 *
	 * @param normalizedReticleSize the normalized size
	 * @return the AsynCatchOptions object
	 */
	public CatchOptions setNormalizedReticleSize(double normalizedReticleSize) {
		this.normalizedReticleSize = normalizedReticleSize;
		return this;
	}

	/**
	 * Set the spin modifier of a pokeball throw
	 *
	 * @param spinModifier the spin modifier
	 * @return the AsynCatchOptions object
	 */
	public CatchOptions setSpinModifier(double spinModifier) {
		this.spinModifier = spinModifier;
		return this;
	}

	/**
	 * Selects a pokeball to use based on
	 * @param pokeballs the pokeballs contained in your inventory
	 * @param probability encounter probability
	 * @return the pokeball to use
	 * @throws NoSuchItemException if there are no pokeballs to use
	 */
	public Pokeball selectPokeball(List<Pokeball> pokeballs, double probability) throws NoSuchItemException {
		if (pokeballs.size() == 0) {
			throw new NoSuchItemException("Player has no pokeballs");
		}
		if (pokeballSelector != null) {
			Pokeball selected = pokeballSelector.select(pokeballs, probability);
			if (selected != null) {
				boolean hasPokeball = pokeballs.contains(selected);
				if (hasPokeball) {
					return selected;
				} else {
					throw new NoSuchItemException("Player does not have pokeball: " + selected.name());
				}
			}
		}
		return pokeballs.get(0);
	}

	public interface PokeballSelector {
		Pokeball select(List<Pokeball> pokeballs, double probability);
	}
}
