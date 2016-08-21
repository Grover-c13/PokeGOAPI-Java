package com.pokegoapi.api.internal;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by paul on 21-8-2016.
 */
@Data
@AllArgsConstructor
public class Location {
	private double latitude;
	private double longitude;
	private double altitude;
}
