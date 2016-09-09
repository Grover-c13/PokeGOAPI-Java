package com.pokegoapi.api.device;

import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.pokegoapi.api.internal.Location;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Location fixes class
 */
@RequiredArgsConstructor
public class LocationFixes {
	private final LocationFixProvider locationFixProvider;
	private final List<LocationFixProvider.LocationFix> fixList = new CopyOnWriteArrayList<>();

	/**
	 * Gets the default device info for the given api
	 *
	 * @param random random object
	 * @return the default device info for the given api
	 */
	public static LocationFixes getDefault(Random random) {
		return new LocationFixes(new DefaultLocationFixProvider(random));
	}

	/**
	 * For internal use
	 *
	 * @param location            Location containing the current position
	 * @param getMapObjectRequest Is this a map objects request
	 * @return Collection of LocationFix's
	 */
	public Collection<SignatureOuterClass.Signature.LocationFix> getLocationFixes(final Location location,
			final boolean getMapObjectRequest) {
		List<LocationFixProvider.LocationFix> fixes = new ArrayList<>(fixList);
		fixList.clear();
		return Stream.of(fixes)
				.map(new Function<LocationFixProvider.LocationFix, SignatureOuterClass.Signature.LocationFix>() {
					@Override
					public SignatureOuterClass.Signature.LocationFix apply(
							LocationFixProvider.LocationFix locationFix) {
						SignatureOuterClass.Signature.LocationFix.Builder locationFixBuilder =
								SignatureOuterClass.Signature.LocationFix.newBuilder();

						return locationFixBuilder.setProvider("fused")
								.setAltitude(locationFix.getAltitude())
								.setHorizontalAccuracy(locationFix.getHorizontalAccuracy())
								.setVerticalAccuracy(locationFix.getVerticalAccuracy())
								.setLatitude(locationFix.getLatitude())
								.setLongitude(locationFix.getLongitude())
								.setLocationType(locationFix.getLocationType())
								.setTimestampSnapshot(locationFix.getTimestampSnapshot())
								.setProviderStatus(3)
								.build();
					}
				}).collect(Collectors.<SignatureOuterClass.Signature.LocationFix>toList());
	}

	/**
	 * Trigger location update
	 * @param lat Latitude
	 * @param lng longitude
	 * @param accuracy Accuracy
	 */
	public void locationUpdated(double lat, double lng, double accuracy) {
		fixList.addAll(locationFixProvider.getLocationFixes(lat, lng, accuracy));
	}

	@RequiredArgsConstructor
	private static class DefaultLocationFixProvider implements LocationFixProvider {
		private final long startTime = System.currentTimeMillis();
		private final Random random;
		private long previousTimestamp;
		private List<LocationFix> previousLocationFixes;

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

		@Override
		public Collection<LocationFix> getLocationFixes(final double lat, final double lng, final double accuracy) {
			long currentTime = System.currentTimeMillis();
			int pn = random.nextInt(100);
			int providerCount;
			int[] negativeSnapshotProviders = new int[0];
			List<LocationFix> locationFixes = new LinkedList<>();
			int chance = random.nextInt(100);
			if (previousLocationFixes == null) {
				previousLocationFixes = locationFixes;
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
				locationFixes = previousLocationFixes;
				locationFixes.clear();

				if ((currentTime - previousTimestamp < (random.nextInt(10 * 1000) + 5000))) {
					previousTimestamp = currentTime;
					return locationFixes;
				} else {
					providerCount = pn < 60 ? 1 : pn < 90 ? 2 : 3;
				}
			}

			// locationFixes.setTimestampCreate(api.currentTimeMillis());

			for (int i = 0; i < providerCount; i++) {
				float latitude = offsetOnLatLong(lat, random.nextInt(100) + 10);
				float longitude = offsetOnLatLong(lng, random.nextInt(100) + 10);
				float altitude = 65;
				final float verticalAccuracy = (float) (accuracy + (8 * random.nextDouble()));

				// Fake errors
				if (random.nextInt(100) < 20) {
					if (random.nextInt(100) > 90) {
						latitude = 360;
						longitude = -360;
					}
					if (random.nextInt(100) > 90) {
						altitude = (float) (66 + (160 - 66) * random.nextDouble());
					}
				}

				LocationFix locationFix = new LocationFix();
				locationFix.setTimestampSnapshot(
						contains(negativeSnapshotProviders, i)
								? random.nextInt(1000) - 3000
								: currentTime - startTime
								+ (150 * (i + 1) + random.nextInt(250 * (i + 1) - (150 * (i + 1)))));
				locationFix.setLatitude(latitude);
				locationFix.setLongitude(longitude);
				locationFix.setHorizontalAccuracy(-1);
				locationFix.setAltitude(altitude);
				locationFix.setVerticalAccuracy(verticalAccuracy);
				locationFix.setProviderStatus(3);
				locationFix.setLocationType(1);
				locationFixes.add(locationFix);
			}
			return locationFixes;
		}
	}

}
