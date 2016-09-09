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

package com.pokegoapi.api.map.pokemon;

import POGOProtos.Enums.PokemonIdOuterClass.PokemonId;
import POGOProtos.Map.Fort.FortDataOuterClass.FortData;
import POGOProtos.Map.Fort.FortLureInfoOuterClass.FortLureInfo;
import POGOProtos.Map.Pokemon.MapPokemonOuterClass.MapPokemon;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass.WildPokemon;
import POGOProtos.Networking.Responses.GetIncensePokemonResponseOuterClass.GetIncensePokemonResponse;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.util.MapPoint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Catchable pokemon.
 */
@ToString
public class CatchablePokemon implements MapPoint {
	private final PokemonGo api;
	@Getter
	public final String spawnPointId;
	@Getter
	public final long encounterId;
	@Getter
	public final PokemonId pokemonId;
	@Getter
	private final int pokemonIdValue;
	@Getter
	public final long expirationTimestampMs;
	private final double latitude;
	private final double longitude;
	private final EncounterKind encounterKind;
	private Encounter encounter = null;
	@Getter
	@Setter
	public boolean despawned = false;

	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api the api
	 * @param proto the proto
	 */
	public CatchablePokemon(PokemonGo api, MapPokemon proto) {
		this.api = api;
		this.encounterKind = EncounterKind.NORMAL;
		this.spawnPointId = proto.getSpawnPointId();
		this.encounterId = proto.getEncounterId();
		this.pokemonId = proto.getPokemonId();
		this.pokemonIdValue = proto.getPokemonIdValue();
		this.expirationTimestampMs = proto.getExpirationTimestampMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
	}

	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api the api
	 * @param proto the proto
	 */
	public CatchablePokemon(PokemonGo api, WildPokemon proto) {
		this.api = api;
		this.encounterKind = EncounterKind.NORMAL;
		this.spawnPointId = proto.getSpawnPointId();
		this.encounterId = proto.getEncounterId();
		this.pokemonId = proto.getPokemonData().getPokemonId();
		this.pokemonIdValue = proto.getPokemonData().getPokemonIdValue();
		this.expirationTimestampMs = proto.getTimeTillHiddenMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
	}

	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api the api
	 * @param proto the proto
	 */
	public CatchablePokemon(PokemonGo api, FortData proto) {
		if (!proto.hasLureInfo()) {
			throw new IllegalArgumentException("Fort does not have lure");
		}
		FortLureInfo lureInfo = proto.getLureInfo();
		this.api = api;
		this.spawnPointId = lureInfo.getFortId();
		this.encounterId = lureInfo.getEncounterId();
		this.pokemonId = lureInfo.getActivePokemonId();
		this.pokemonIdValue = lureInfo.getActivePokemonIdValue();
		this.expirationTimestampMs = lureInfo.getLureExpiresTimestampMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
		this.encounterKind = EncounterKind.DISK;
	}

	/**
	 * Instantiates a new Catchable pokemon.
	 *
	 * @param api the api
	 * @param proto the proto
	 */
	public CatchablePokemon(PokemonGo api, GetIncensePokemonResponse proto) {
		this.api = api;
		this.spawnPointId = proto.getEncounterLocation();
		this.encounterId = proto.getEncounterId();
		this.pokemonId = proto.getPokemonId();
		this.pokemonIdValue = proto.getPokemonIdValue();
		this.expirationTimestampMs = proto.getDisappearTimestampMs();
		this.latitude = proto.getLatitude();
		this.longitude = proto.getLongitude();
		this.encounterKind = EncounterKind.INCENSE;
	}

	/**
	 * Encounters this pokemon
	 *
	 * @return the encounter for this pokemon
	 * @throws RequestFailedException if the request fails
	 */
	public Encounter encounter() throws RequestFailedException {
		if (encounter == null) {
			encounter = createEncounter();
			encounter.encounter();
		}
		return encounter;
	}

	/**
	 * @return creates the appropriate encounter for this pokemon
	 */
	protected Encounter createEncounter() {
		switch (encounterKind) {
			case DISK:
				return new DiskEncounter(api, this);
			case INCENSE:
				return new IncenseEncounter(api, this);
			default:
				return new Encounter(api, this);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof CatchablePokemon) {
			return this.encounterId == ((CatchablePokemon) obj).encounterId;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (int) this.encounterId;
	}

	/**
	 * Encounter check
	 *
	 * @return Checks if encounter has happened
	 */
	public boolean hasEncountered() {
		return encounter != null;
	}

	/**
	 * Return true when the catchable pokemon is a lured pokemon
	 *
	 * @return true for lured pokemon
	 */
	public boolean isLured() {
		return encounterKind == EncounterKind.DISK;
	}

	/**
	 * Return true when the catchable pokemon is a lured pokemon from incense
	 *
	 * @return true for pokemon lured by incense
	 */
	public boolean isFromIncense() {
		return encounterKind == EncounterKind.INCENSE;
	}

	private enum EncounterKind {
		NORMAL,
		DISK,
		INCENSE
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}
}
