package com.pokegoapi.google.common.geometry;

import java.util.Arrays;

public class Utils
{
	// had nullable annotation
	public static int hashCode(Object ... objects) {
		return Arrays.hashCode(objects);
	}
}
