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

package com.pokegoapi.api.device;

import lombok.Data;

/**
 * Provider for activity status
 */
public interface ActivityStatusProvider {

	/**
	 * Status
	 */
	@Data
	class Status {
		public enum Activity {
			AUTOMOTIVE, CYCLING, RUNNING, STATIONARY, WALKING;
		}

		private final Activity activity;
		private final boolean tilting;
	}

	/**
	 * @return Status
	 */
	Status getActivity();
}
