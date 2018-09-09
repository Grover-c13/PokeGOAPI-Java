package com.pokegoapi.api.device;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope;
import POGOProtos.Networking.Envelopes.SignatureOuterClass.Signature.LocationFix;
import POGOProtos.Networking.Requests.RequestOuterClass.Request;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import com.pokegoapi.api.PokemonGo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by fabianterhorst on 23.08.16.
 */

public class LocationFixes extends ArrayList<LocationFix> {

	@Setter
	@Getter
	private long timestampCreate;

	public LocationFixes generate(PokemonGo api, RequestEnvelope.Builder builder, long currentTime, Random rand) {
		return getDefault(api, builder, currentTime, rand);
	}
	
	public LocationFixes() {
	}

	/**
	 * Gets the default device info for the given api
	 *
	 * @param api the api
	 * @param builder the request builder
	 * @param currentTime the current time
	 * @param random random object
	 * @return the default device info for the given api
	 */
	public static LocationFixes getDefault(PokemonGo api, RequestEnvelopeOuterClass.RequestEnvelope.Builder builder,
			long currentTime, Random random) {
		if (Double.isNaN(api.latitude) || Double.isNaN(api.longitude)) {
			return new LocationFixes();
		}

		boolean hasMapUpdate = false;
		boolean empty = builder.getRequestsCount() == 0 || builder.getRequests(0) == null;

		for (Request request : builder.getRequestsList()) {
			if (request.getRequestType() == RequestType.GET_MAP_OBJECTS) {
				hasMapUpdate = true;
				break;
			}
		}

		int pn = random.nextInt(100);
		int providerCount;
		int[] negativeSnapshotProviders = new int[0];

		int chance = random.nextInt(100);
		LocationFixes locationFixes;
		if (api.locationFixes == null) {
			locationFixes = new LocationFixes();
			api.locationFixes = locationFixes;
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
			locationFixes = api.locationFixes;
			locationFixes.clear();

			boolean expired = currentTime - locationFixes.timestampCreate < (random.nextInt(10000) + 5000);
			if (empty || (!hasMapUpdate && expired)) {
				locationFixes.timestampCreate = currentTime;
				return locationFixes;
			} else if (hasMapUpdate) {
				providerCount = chance >= 90 ? 2 : 1;
			} else {
				providerCount = pn < 60 ? 1 : pn < 90 ? 2 : 3;
			}
		}

		locationFixes.timestampCreate = currentTime;

		for (int i = 0; i < providerCount; i++) {
			float latitude = (float) api.latitude;
			float longitude = (float) api.longitude;
			if (i < providerCount - 1) {
				latitude = offsetOnLatLong(api.latitude, random.nextInt(100) + 10);
				longitude = offsetOnLatLong(api.longitude, random.nextInt(100) + 10);
			}

			float altitude = (float) api.altitude;
			float verticalAccuracy = (float) (15 + (23 - 15) * random.nextDouble());

			// Fake errors
			if (!hasMapUpdate) {
				if (random.nextInt(100) > 90) {
					latitude = 360;
					longitude = -360;
				}
				if (random.nextInt(100) > 90) {
					altitude = (float) (66 + 94 * random.nextDouble());
				}
			}

			LocationFix.Builder locationFixBuilder = LocationFix.newBuilder();

			locationFixBuilder.setProvider("fused")
					.setTimestampSnapshot(
							contains(negativeSnapshotProviders, i)
									? random.nextInt(1000) - 3000
									: currentTime - api.startTime
											+ (150 * (i + 1) + random.nextInt(250 * (i + 1) - (150 * (i + 1)))))
					.setLatitude(latitude)
					.setLongitude(longitude)
					.setHorizontalAccuracy((float) api.accuracy)
					.setAltitude(altitude)
					.setVerticalAccuracy(verticalAccuracy)
					.setProviderStatus(3L)
					.setLocationType(1L)
					// When emulating IOS (which is now mendatory) we must set those values too
					.setSpeed(-1.0f)
					.setCourse(-1.0f);
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
