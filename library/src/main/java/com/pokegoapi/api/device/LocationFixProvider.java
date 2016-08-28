package com.pokegoapi.api.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author Paul van Assen
 */
public interface LocationFixProvider {
	Collection<LocationFix> getLocationFixes(double latitude, double longitude, double altitude, boolean getMapObjectRequest);

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
