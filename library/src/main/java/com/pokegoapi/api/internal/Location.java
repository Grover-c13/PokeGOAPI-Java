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
	private float altitude;


	public Location(double latitiude, double longitude, double altitude) {
		this.latitude = (float)latitiude;
		this.longitude = (float)longitude;
		this.altitude = (float)altitude;
	}

	public void setLatitude(double value) {
		latitude = (float)value;
	}

	public void setLongitude(double value) {
		longitude = (float)value;
	}

	public void setAltitude(double value) {
		altitude = (float)value;
	}
}
