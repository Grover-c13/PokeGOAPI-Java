package com.pokegoapi.api.internal;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by paul on 21-8-2016.
 */
@Data
@AllArgsConstructor
public class Location {
	private float latitude;
	private float longitude;
	private float accuracy;


	public Location(double latitiude, double longitude, double accuracy) {
		this.latitude = (float)latitiude;
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
