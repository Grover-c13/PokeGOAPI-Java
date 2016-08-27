package com.pokegoapi.api.device;

import com.pokegoapi.api.internal.Location;
import lombok.Data;

import java.util.Collection;

/**
 * @author Paul van Assen
 */
public interface LocationFixProvider {
	Collection<LocationFix> getLocationFixes(double latitude, double longitude, double altitude, boolean getMapObjectRequest);

	@Data
	class LocationFix {
		private final long timestampSnapshot;
		private final float latitude;
		private final float longitude;
		private final float altitude;
		private final int horizontalAccuracy;
		private final float verticalAccurary;
		private final int providerStatus;
		private final int locationType;
	}
}
