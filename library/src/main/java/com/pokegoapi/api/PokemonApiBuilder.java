/*
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pokegoapi.api;

import com.pokegoapi.api.device.ActivityStatus;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.auth.CredentialProvider;
import okhttp3.OkHttpClient;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Builder class for setting up API
 */
public class PokemonApiBuilder {
	private ExecutorService executorService;
	private CredentialProvider credentialProvider;
	private OkHttpClient client;
	private Double latitude;
	private Double longitude;
	private Double altitude;
	private URL server;
	private DeviceInfo deviceInfo;
	private SensorInfo sensorInfo;
	private ActivityStatus activityStatus;
	private LocationFixes locationFixes;
	private Locale locale;
	private Random random;

	PokemonApiBuilder() {
		super();
	}

	public PokemonApiBuilder executorService(ExecutorService executorService) {
		this.executorService = executorService;
		return this;
	}

	public PokemonApiBuilder credentialProvider(CredentialProvider credentialProvider) {
		this.credentialProvider = credentialProvider;
		return this;
	}

	public PokemonApiBuilder withHttpClient(OkHttpClient client) {
		this.client = client;
		return this;
	}

	public PokemonApiBuilder latitude(double latitude) {
		this.latitude = latitude;
		return this;
	}

	public PokemonApiBuilder longitude(double longitude) {
		this.longitude = longitude;
		return this;
	}

	public PokemonApiBuilder altitude(double altitude) {
		this.altitude = altitude;
		return this;
	}

	public PokemonApiBuilder deviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
		return this;
	}

	public PokemonApiBuilder sensorInfo(SensorInfo sensorInfo) {
		this.sensorInfo = sensorInfo;
		return this;
	}

	public PokemonApiBuilder activityStatus(ActivityStatus activityStatus) {
		this.activityStatus = activityStatus;
		return this;
	}

	public PokemonApiBuilder locationFixes(LocationFixes locationFixes) {
		this.locationFixes = locationFixes;
		return this;
	}

	public PokemonApiBuilder locale(Locale locale) {
		this.locale = locale;
		return this;
	}

	public PokemonApiBuilder random(Random random) {
		this.random = random;
		return this;
	}

	/**
	 * @return Builds the pokemon api
	 */
	public PokemonApi build() {
		if (latitude == null) {
			throw new IllegalArgumentException("Latitude must be set");
		}
		if (longitude == null) {
			throw new IllegalArgumentException("Longitude must be set");
		}
		if (altitude == null) {
			throw new IllegalArgumentException("Altitude must be set");
		}
		if (credentialProvider == null) {
			throw new IllegalArgumentException("Credential provider must be set");
		}
		if (executorService == null) {
			executorService = Executors.newCachedThreadPool(new PokemonApiThreadFactory());
		}
		if (client == null) {
			client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS)
					.build();
		}
		if (locale == null) {
			locale = Locale.getDefault();
		}
		if (random == null) {
			random = new Random();
		}
		if (server == null) {
			try {
				server = URI.create("https://pgorelease.nianticlabs.com/plfe/rpc").toURL();
			} catch (MalformedURLException e) {
				throw new RuntimeException("The predefined url is not valid", e);
			}
		}
		if (deviceInfo == null) {
			deviceInfo = DeviceInfo.getDefault(random);
		}
		if (sensorInfo == null) {
			sensorInfo = SensorInfo.getDefault(random);
		}
		if (activityStatus == null) {
			activityStatus = ActivityStatus.getDefault(random);
		}
		if (locationFixes == null) {
			locationFixes = LocationFixes.getDefault(random);
		}
		return new PokemonApi(executorService, credentialProvider, client, server,
				new Location(latitude, longitude, altitude),
				deviceInfo, sensorInfo, activityStatus, locationFixes, locale);
	}

	private static class PokemonApiThreadFactory implements ThreadFactory {
		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final String baseName;
		private final AtomicInteger counter = new AtomicInteger(1);

		PokemonApiThreadFactory() {
			baseName = "pokemonapi-" + poolNumber.addAndGet(1) + "-thread-";
		}

		@Override
		public Thread newThread(Runnable runnable) {
			Thread thread = new Thread(runnable, baseName.concat(Integer.toString(counter.getAndAdd(1))));
			thread.setDaemon(true);
			return thread;
		}
	}
}
