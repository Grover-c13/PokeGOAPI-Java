package com.pokegoapi.api.map.pokemon;

import lombok.Getter;

public class ThrowProperties {
	@Getter
	public double normalizedHitPosition;
	@Getter
	public double normalizedReticleSize;
	@Getter
	public double spinModifier;
	private boolean hitPokemon = true;

	private ThrowProperties() {
		this.normalizedHitPosition = 1.0;
		this.normalizedReticleSize = 1.95 + Math.random() * 0.05;
		double spinType = Math.random();
		if (spinType > 0.5) {
			this.spinModifier = 1.0;
		} else if (spinType > 0.8) {
			this.spinModifier = 0.0;
		} else {
			this.spinModifier = 0.85 + Math.random() * 0.15;
		}
	}

	/**
	 * Creates a ThrowProperties object
	 * @param normalizedHitPosition the normalized hit position of this throw
	 * @param normalizedReticleSize the normalized reticle size of this throw
	 * @param spinModifier the spin modifier of this throw
	 * @param hitPokemon true if this throw hit the pokemon
	 */
	public ThrowProperties(double normalizedHitPosition, double normalizedReticleSize, double spinModifier,
			boolean hitPokemon) {
		this.normalizedHitPosition = normalizedHitPosition;
		this.normalizedReticleSize = normalizedReticleSize;
		this.spinModifier = spinModifier;
		this.hitPokemon = hitPokemon;
	}

	/**
	 * @return a randomly populated ThrowProperties object
	 */
	public static ThrowProperties random() {
		return new ThrowProperties();
	}

	/**
	 * Applies a hit position to this throw
	 * @param normalizedHitPosition the normalized hit position of this throw
	 * @return these properties
	 */
	public ThrowProperties withHitPosition(double normalizedHitPosition) {
		this.normalizedHitPosition = normalizedHitPosition;
		return this;
	}

	/**
	 * Applies a reticle size to this throw
	 * @param normalizedReticleSize the normalized reticle size for this throw
	 * @return these properties
	 */
	public ThrowProperties withReticleSize(double normalizedReticleSize) {
		this.normalizedReticleSize = normalizedReticleSize;
		return this;
	}

	/**
	 * Applies a spin modifier to this throw
	 * @param spinModifier the spin modifier for this throw
	 * @return these properties
	 */
	public ThrowProperties withSpinModifier(double spinModifier) {
		this.spinModifier = spinModifier;
		return this;
	}

	/**
	 * Sets whether this throw hit the pokemon or not
	 * @param hitPokemon true if this throw hit the pokemon
	 * @return these properties
	 */
	public ThrowProperties withHit(boolean hitPokemon) {
		this.hitPokemon = hitPokemon;
		return this;
	}

	/**
	 * @return true if this throw should hit the current pokemon
	 */
	public boolean shouldHitPokemon() {
		return hitPokemon;
	}
}
