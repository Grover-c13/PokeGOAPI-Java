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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Location fix provider, which is called with lat, lon and accuracy
 */
public interface LocationFixProvider {
	/**
	 * Get location fixes for all providers. A device usually has 2
	 *
	 * @param latitude            Latitude
	 * @param longitude           Longittude
	 * @param accuracy            Accuracy
	 * @return Collection of location fixes
	 */
	Collection<LocationFix> getLocationFixes(double latitude, double longitude, double accuracy);

	/**
	 * Location fix object
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	class LocationFix {
		private long timestampSnapshot;
		private float latitude;
		private float longitude;
		private float altitude;
		private int horizontalAccuracy;
		private float verticalAccuracy;
		private int providerStatus;
		private int locationType;
	}
}
