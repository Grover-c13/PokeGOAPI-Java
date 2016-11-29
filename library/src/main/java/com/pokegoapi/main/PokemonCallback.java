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

package com.pokegoapi.main;

public interface PokemonCallback {
	PokemonCallback NULL_CALLBACK = new PokemonCallback() {
		@Override
		public void onCompleted(Exception e) {
		}
	};

	/**
	 * Called when a task completes.
	 * @param e exception thrown while doing this task, null if no exception was thrown.
	 */
	void onCompleted(Exception e);
}
