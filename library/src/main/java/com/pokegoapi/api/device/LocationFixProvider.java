package com.pokegoapi.api.device;

/**
 * @author Paul van Assen
 */
public interface LocationFixProvider {
	long getTimestampSnapshot();
	double getLatitude();
	double getLongitude();
	double getAltitude();
	int getHorizontalAccuracy();
	float getVerticalAccurary();
	int getProviderStatus();
	int getLocationType();
}
