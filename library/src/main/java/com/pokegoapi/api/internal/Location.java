package com.pokegoapi.api.internal;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Location object representing the position of the user, together with the accuracy
 */
@Data
@AllArgsConstructor
public class Location {
	private float latitude;
	private float longitude;
	private float accuracy;

	/**
	 * Constructor, for internal use
	 * @param latitude Latitude
	 * @param longitude Longitude
	 * @param accuracy Accuracy
	 */
	public Location(double latitude, double longitude, double accuracy) {
		this.latitude = (float)latitude;
		this.longitude = (float)longitude;
		this.accuracy = (float) accuracy;
	}

	public void setLatitude(double value) {
		latitude = (float)value;
	}

	public void setLongitude(double value) {
		longitude = (float)value;
	}

	public void setAccuracy(double value) {
		accuracy = (float)value;
	}
}
