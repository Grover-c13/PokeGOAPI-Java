package com.pokegoapi.api;

import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.internal.Location;
import com.pokegoapi.auth.CredentialProvider;
import okhttp3.OkHttpClient;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by paul on 21-8-2016.
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
	private Locale locale;

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

	public PokemonApiBuilder locale(Locale locale) {
		this.locale = locale;
		return this;
	}

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
			client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).build();
		}
		if (locale == null) {
			locale = Locale.getDefault();
		}
		if (server == null) {
			try {
				server = URI.create("https://pgorelease.nianticlabs.com/plfe/rpc").toURL();
			}
			catch (MalformedURLException e) {
				throw new RuntimeException("The predefined url is not valid", e);
			}
		}
		if (deviceInfo == null) {
			deviceInfo = DeviceInfo.DEFAULT;
		}
		if (sensorInfo == null) {
			sensorInfo = new SensorInfo();
		}
		return new PokemonApi(executorService, credentialProvider, client, server, new Location(latitude, longitude, altitude), deviceInfo, sensorInfo, locale);
	}

	private static class PokemonApiThreadFactory implements ThreadFactory {
		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final String baseName;
		private final AtomicInteger counter = new AtomicInteger(1);
		PokemonApiThreadFactory() {
			baseName = "pokemonapi-" + poolNumber.addAndGet(1) + "-thread-";
		}
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r,baseName.concat(Integer.toString(counter.getAndAdd(1))));
			thread.setDaemon(true);
			return thread;
		}
	}
}
