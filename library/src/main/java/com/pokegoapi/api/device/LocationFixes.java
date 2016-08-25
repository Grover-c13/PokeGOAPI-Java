package com.pokegoapi.api.device;

import com.pokegoapi.api.PokemonGo;

import java.util.ArrayList;
import java.util.Random;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by fabianterhorst on 23.08.16.
 */

public class LocationFixes extends ArrayList<SignatureOuterClass.Signature.LocationFix> {

	@Setter
	@Getter
	private long timestampCreate;

	public LocationFixes() {
	}

	/**
	 * Gets the default device info for the given api
	 *
	 * @param api         the api
	 * @param builder     the request builder
	 * @param currentTime the current time
	 * @param random random object
	 * @return the default device info for the given api
	 */
	public static LocationFixes getDefault(PokemonGo api, RequestEnvelopeOuterClass.RequestEnvelope.Builder builder,
											long currentTime, Random random) {
		int pn = random.nextInt(100);
		int providerCount;
		int[] negativeSnapshotProviders = new int[0];

		int chance = random.nextInt(100);
		LocationFixes locationFixes;
		if (api.getLocationFixes() == null) {
			locationFixes = new LocationFixes();
			api.setLocationFixes(locationFixes);
			providerCount = pn < 75 ? 6 : pn < 95 ? 5 : 8;
			if (providerCount != 8) {
				// a 5% chance that the second provider got a negative value else it should be the first only
				negativeSnapshotProviders = new int[1];
				negativeSnapshotProviders[0] = chance < 95 ? 0 : 1;
			} else {
				negativeSnapshotProviders = new int[chance >= 50 ? 3 : 2];
				negativeSnapshotProviders[0] = 0;
				negativeSnapshotProviders[1] = 1;
				if (chance >= 50) {
					negativeSnapshotProviders[2] = 2;
				}
			}
		} else {
			locationFixes = api.getLocationFixes();
			locationFixes.clear();

			if (builder.getRequestsCount() == 0 || builder.getRequests(0) == null
					|| (builder.getRequests(0).getRequestType() != RequestTypeOuterClass.RequestType.GET_MAP_OBJECTS
					&& (currentTime - locationFixes.getTimestampCreate() < (random.nextInt(10 * 1000) + 5000)))) {
				locationFixes.setTimestampCreate(currentTime);
				return locationFixes;
			} else if (builder.getRequests(0).getRequestType() == RequestTypeOuterClass.RequestType.GET_MAP_OBJECTS) {
				providerCount = chance >= 90 ? 2 : 1;
			} else {
				providerCount = pn < 60 ? 1 : pn < 90 ? 2 : 3;
			}
		}

		locationFixes.setTimestampCreate(api.currentTimeMillis());

		for (int i = 0; i < providerCount; i++) {
			float latitude = offsetOnLatLong(api.getLatitude(), random.nextInt(100) + 10);
			float longitude = offsetOnLatLong(api.getLongitude(), random.nextInt(100) + 10);
			float altitude = 65;
			float verticalAccuracy = (float) (15 + (23 - 15) * random.nextDouble());

			// Fake errors
			if (builder.getRequests(0).getRequestType() != RequestTypeOuterClass.RequestType.GET_MAP_OBJECTS) {
				if (random.nextInt(100) > 90) {
					latitude = 360;
					longitude = -360;
				}
				if (random.nextInt(100) > 90) {
					altitude = (float) (66 + (160 - 66) * random.nextDouble());
				}
			}

			SignatureOuterClass.Signature.LocationFix.Builder locationFixBuilder =
					SignatureOuterClass.Signature.LocationFix.newBuilder();

			locationFixBuilder.setProvider("fused")
					.setTimestampSnapshot(
							contains(negativeSnapshotProviders, i)
									? random.nextInt(1000) - 3000
									: api.currentTimeMillis() - api.getStartTime()
									+ (150 * (i + 1) + random.nextInt(250 * (i + 1) - (150 * (i + 1)))))
					.setLatitude(latitude)
					.setLongitude(longitude)
					.setHorizontalAccuracy(-1)
					.setAltitude(altitude)
					.setVerticalAccuracy(verticalAccuracy)
					.setProviderStatus(3)
					.setLocationType(1);
			locationFixes.add(locationFixBuilder.build());
		}
		return locationFixes;
	}

	private static boolean contains(int[] array, int value) {
		for (final int i : array) {
			if (i == value) {
				return true;
			}
		}
		return false;
	}

	private static float offsetOnLatLong(double lat, double ran) {
		double round = 6378137;
		double dl = ran / (round * Math.cos(Math.PI * lat / 180));
		return (float) (lat + dl * 180 / Math.PI);
	}
}
