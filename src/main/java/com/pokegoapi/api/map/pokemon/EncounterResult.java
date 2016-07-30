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

import POGOProtos.Data.Capture.CaptureProbabilityOuterClass;
import POGOProtos.Map.Pokemon.WildPokemonOuterClass;
import POGOProtos.Networking.Responses.DiskEncounterResponseOuterClass;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;
import org.omg.PortableInterceptor.SUCCESSFUL;


public abstract class EncounterResult {


	public abstract boolean wasSuccessful();

	/**
	 * Return the status of the encounter
	 *
	 * @return status of results
	 */
	public abstract EncounterResponse.Status getStatus();

	public static class DiskEncounterResult extends EncounterResult {

		private DiskEncounterResponseOuterClass.DiskEncounterResponse response;

		public DiskEncounterResult(DiskEncounterResponseOuterClass.DiskEncounterResponse response) {
			this.response = response;
		}

		@Override
		public boolean wasSuccessful() {
			return response != null
					&& response.getResult() == DiskEncounterResponseOuterClass.DiskEncounterResponse.Result.SUCCESS;
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
	}

	public static class NormalEncounterResult extends EncounterResult {
		private EncounterResponse response;

		public NormalEncounterResult(EncounterResponse response) {
			this.response = response;
		}

		/**
		 * Return the status of the encounter
		 *
		 * @return status of results
		 */
		public EncounterResponse.Status getStatus() {
			return response == null ? null : response.getStatus();
		}

		public boolean wasSuccessful() {
			return response != null
					&& getStatus() != null && getStatus().equals(EncounterResponse.Status.ENCOUNTER_SUCCESS);
		}

		public EncounterResponse.Background getBackground() {
			return response.getBackground();
		}

		public CaptureProbabilityOuterClass.CaptureProbability getCaptureProbability() {
			return response.getCaptureProbability();
		}

		public WildPokemonOuterClass.WildPokemon getWildPokemon() {
			return response.getWildPokemon();
		}

		public EncounterResponse toPrimitive() {
			return response;
		}


	}

}
