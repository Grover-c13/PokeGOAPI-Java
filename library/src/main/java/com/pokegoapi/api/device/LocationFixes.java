package com.pokegoapi.api.device;

import com.pokegoapi.api.PokemonGo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	 * @return the default device info for the given api
	 */
	public static LocationFixes getDefault(PokemonGo api, RequestEnvelopeOuterClass.RequestEnvelope.Builder builder,
											long currentTime) {
		Random random = new Random();
		int pn = random.nextInt(100);
		int providerCount;
		HashSet<String> negativeSnapshotProviders = new HashSet<>();

		LocationFixes locationFixes;
		if (api.getLocationFixes() == null) {
			locationFixes = new LocationFixes();
			providerCount = pn < 75 ? 6 : pn < 95 ? 5 : 8;

			if (providerCount != 8) {
				// a 5% chance that the second provider got a negative value else it should be the first only
				int chance = random.nextInt(100);
				negativeSnapshotProviders.add(chance < 95 ? "0" : "1");
			} else {
				int chance = random.nextInt(100);
				negativeSnapshotProviders.add("0");
				negativeSnapshotProviders.add("1");
				if (chance >= 50) {
					negativeSnapshotProviders.add("2");
				}
			}
		} else {
			locationFixes = api.getLocationFixes();
			locationFixes.clear();

			if (builder.getRequests(0) == null
					|| builder.getRequests(0).getRequestType() != RequestTypeOuterClass.RequestType.GET_MAP_OBJECTS
					|| (currentTime - locationFixes.getTimestampCreate() > (random.nextInt(10 * 1000) + 5000))) {
				return locationFixes;
			}

			providerCount = pn < 60 ? 1 : pn < 90 ? 2 : 3;
		}

		locationFixes.setTimestampCreate(currentTime);

		for (int i = 0; i < providerCount; i++) {
			float latitude = offsetOnLatLong(api.getLatitude(), random.nextInt(100) + 10);
			float longitude = offsetOnLatLong(api.getLongitude(), random.nextInt(100) + 10);
			float altitude = 65;
			float verticalAccuracy = (float) (15 + (23 - 15) * random.nextDouble());

			// Fake errors xD
			if (random.nextInt(100) > 90) {
				latitude = 360;
				longitude = -360;
			}

			// Another fake error
			if (random.nextInt(100) > 90) {
				altitude = (float) (66 + (160 - 66) * random.nextDouble());
			}

			SignatureOuterClass.Signature.LocationFix.Builder locationFixBuilder =
					SignatureOuterClass.Signature.LocationFix.newBuilder();

			locationFixBuilder.setProvider("fused")
					.setTimestampSnapshot(
							negativeSnapshotProviders.contains(String.valueOf(i))
									? random.nextInt(1000) - 3000
									: currentTime - api.getStartTime())
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

	private static float offsetOnLatLong(double lat, double ran) {
		double round = 6378137;
		double dl = ran / (round * Math.cos(Math.PI * lat / 180));
		return (float) (lat + dl * 180 / Math.PI);
	}
}
