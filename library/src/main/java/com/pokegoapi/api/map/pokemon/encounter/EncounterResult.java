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
import POGOProtos.Map.Pokemon.WildPokemonOuterClass;
import POGOProtos.Networking.Responses.EncounterResponseOuterClass.EncounterResponse;



public interface EncounterResult {
	
	boolean wasSuccessful();

	EncounterResponse.Status getStatus();

	PokemonDataOuterClass.PokemonData getPokemonData();

	CaptureProbabilityOuterClass.CaptureProbability getCaptureProbability();
}