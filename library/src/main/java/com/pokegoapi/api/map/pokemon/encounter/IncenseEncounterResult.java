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

package com.pokegoapi.api.map.pokemon.encounter;

import POGOProtos.Data.Capture.CaptureProbabilityOuterClass;
import POGOProtos.Data.PokemonDataOuterClass;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import POGOProtos.Networking.Responses.IncenseEncounterResponseOuterClass.IncenseEncounterResponse;
import POGOProtos.Networking.Responses.IncenseEncounterResponseOuterClass.IncenseEncounterResponse.Result;
import com.pokegoapi.api.PokemonGo;
import lombok.Getter;

public class IncenseEncounterResult extends Encounter implements EncounterResult {
	@Getter
	private IncenseEncounterResponse response;

	public IncenseEncounterResult(PokemonGo api, IncenseEncounterResponse response) {
		super(api, response.getPokemonData());
		this.response = response;
	}

	@Override
	public boolean wasSuccessful() {
		return response != null && response.getResult() == Result.INCENSE_ENCOUNTER_SUCCESS;
	}

	/**
	 * Return the status of the encounter
	 *
	 * @return status of results
	 */
	@Override
	public EncounterResponse.Status getStatus() {
		if (response == null) {
			return null;
		}
		switch (response.getResult()) {
			case INCENSE_ENCOUNTER_UNKNOWN:
				return EncounterResponse.Status.ENCOUNTER_ERROR;
			case INCENSE_ENCOUNTER_SUCCESS:
				return EncounterResponse.Status.ENCOUNTER_SUCCESS;
			case INCENSE_ENCOUNTER_NOT_AVAILABLE:
				return EncounterResponse.Status.ENCOUNTER_NOT_FOUND;
			case POKEMON_INVENTORY_FULL:
				return EncounterResponse.Status.POKEMON_INVENTORY_FULL;
			default:
				return EncounterResponse.Status.UNRECOGNIZED;
		}
	}

	@Override
	public CaptureProbabilityOuterClass.CaptureProbability getCaptureProbability() {
		return response.getCaptureProbability();
	}

	@Override
	public PokemonDataOuterClass.PokemonData getPokemonData() {
		return response.getPokemonData();
	}

	public IncenseEncounterResponse toPrimitive() {
		return response;
	}
}
