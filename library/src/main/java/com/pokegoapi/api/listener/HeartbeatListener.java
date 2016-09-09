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

package com.pokegoapi.api.listener;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.MapObjects;

/**
 * Listener to handle all heartbeat related events such as map updates
 */
public interface HeartbeatListener extends Listener {
	/**
	 * Called when the map is updated
	 *
	 * @param api the current API
	 * @param mapObjects the updated map objects
	 */
	void onMapUpdate(PokemonGo api, MapObjects mapObjects);

	/**
	 * Called when an exception occurs while the map is being updated.
	 *
	 * @param api the current API
	 * @param exception the exception that occurred while updating the map
	 */
	void onMapUpdateException(PokemonGo api, Exception exception);
}
