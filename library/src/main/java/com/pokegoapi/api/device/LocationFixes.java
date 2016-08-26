package com.pokegoapi.api.device;

import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.pokegoapi.api.internal.Location;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by fabianterhorst on 23.08.16.
 */
@RequiredArgsConstructor
public class LocationFixes  {
	private final LocationFixProvider locationFixProvider;

	public Collection<SignatureOuterClass.Signature.LocationFix> getLocationFixes(final Location location,
																				  final boolean getMapObjectRequest) {
		return Stream.of(locationFixProvider.getLocationFixes(location, getMapObjectRequest)).map(new Function<LocationFixProvider.LocationFix, SignatureOuterClass.Signature.LocationFix>() {
			@Override
			public SignatureOuterClass.Signature.LocationFix apply(LocationFixProvider.LocationFix locationFix) {
				SignatureOuterClass.Signature.LocationFix.Builder locationFixBuilder =
						SignatureOuterClass.Signature.LocationFix.newBuilder();

				return locationFixBuilder.setProvider("fused")
						.setAltitude(locationFix.getAltitude())
						.setHorizontalAccuracy(locationFix.getHorizontalAccuracy())
						.setVerticalAccuracy(locationFix.getVerticalAccurary())
						.setLatitude(locationFix.getLatitude())
						.setLongitude(locationFix.getLongitude())
						.setLocationType(locationFix.getLocationType())
						.setTimestampSnapshot(locationFix.getTimestampSnapshot())
						.build();
			}
		}).collect(Collectors.<SignatureOuterClass.Signature.LocationFix>toList());
	}

	/**
	 * Gets the default device info for the given api
	 *
	 * @param random random object
	 * @return the default device info for the given api
	 */
	public static LocationFixes getDefault(Random random) {
		return new LocationFixes(new DefaultLocationFixProvider(random));
	}

	@RequiredArgsConstructor
	private static class DefaultLocationFixProvider implements LocationFixProvider {
		private final long startTime = System.currentTimeMillis();
		private final Random random;

		@Override
		public Collection<LocationFix> getLocationFixes(Location location, boolean getMapObjectRequest) {
			int pn = random.nextInt(100);
			int chance = random.nextInt(100);
			int providerCount = pn < 75 ? 6 : pn < 95 ? 5 : 8;
			int[] negativeSnapshotProviders;
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
			List<LocationFix> locationFixes = new ArrayList<>(providerCount);
			for (int i = 0; i < providerCount; i++) {
				float latitude = offsetOnLatLong(location.getLatitude(), random.nextInt(100) + 10);
				float longitude = offsetOnLatLong(location.getLongitude(), random.nextInt(100) + 10);
				float altitude = 65;
				float verticalAccuracy = (float) (15 + (23 - 15) * random.nextDouble());

				// Fake errors
				if (getMapObjectRequest) {
					if (random.nextInt(100) > 90) {
						latitude = 360;
						longitude = -360;
					}
					if (random.nextInt(100) > 90) {
						altitude = (float) (66 + (160 - 66) * random.nextDouble());
					}
				}
				long timestampSnapshot = contains(negativeSnapshotProviders, i)
						? random.nextInt(1000) - 3000
						: System.currentTimeMillis() - startTime
						+ (150 * (i + 1) + random.nextInt(250 * (i + 1) - (150 * (i + 1))));
				locationFixes.add(new LocationFixProvider.LocationFix(
						timestampSnapshot, latitude, longitude, altitude, -1, verticalAccuracy, 3, 1));
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

}
