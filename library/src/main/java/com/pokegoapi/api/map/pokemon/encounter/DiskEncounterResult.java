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
import POGOProtos.Networking.Responses.DiskEncounterResponseOuterClass.DiskEncounterResponse;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.pokemon.PokemonDetails;
import lombok.Getter;

public class DiskEncounterResult extends PokemonDetails implements EncounterResult {
	@Getter
	private DiskEncounterResponse response;

	public DiskEncounterResult(PokemonGo api, DiskEncounterResponse response) {
		super(api, response.getPokemonData());
		this.response = response;
	}

	@Override
	public boolean wasSuccessful() {
		return response != null
				&& response.getResult() == DiskEncounterResponse.Result.SUCCESS;
	}


	//TODO: i have conveted the DiskEncounter response to maintain compatibility, if not required
	//i think will be better to remove this method

	/**
	 * Return the status of the encounter
	 *
	 * @return status of results
	 */
	public EncounterResponse.Status getStatus() {
		if (response == null)
			return null;
		switch (response.getResult()) {
			case UNKNOWN:
				return EncounterResponse.Status.ENCOUNTER_ERROR;
			case SUCCESS:
				return EncounterResponse.Status.ENCOUNTER_SUCCESS;
			case NOT_AVAILABLE:
				return EncounterResponse.Status.ENCOUNTER_NOT_FOUND;
			case NOT_IN_RANGE:
				return EncounterResponse.Status.ENCOUNTER_NOT_IN_RANGE;
			case ENCOUNTER_ALREADY_FINISHED:
				return EncounterResponse.Status.ENCOUNTER_ALREADY_HAPPENED;
			case POKEMON_INVENTORY_FULL:
				return EncounterResponse.Status.POKEMON_INVENTORY_FULL;
			case UNRECOGNIZED:
				return EncounterResponse.Status.UNRECOGNIZED;
			default:
				return EncounterResponse.Status.UNRECOGNIZED;
		}
	}

	public CaptureProbabilityOuterClass.CaptureProbability getCaptureProbability() {
		return response.getCaptureProbability();
	}

	public PokemonDataOuterClass.PokemonData getPokemonData() {
		return response.getPokemonData();
	}

	public DiskEncounterResponse toPrimitive() {
		return response;
	}
}
