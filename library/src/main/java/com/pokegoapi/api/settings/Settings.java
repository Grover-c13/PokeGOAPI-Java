package com.pokegoapi.api.settings;

import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass.DownloadSettingsMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass.DownloadSettingsResponse;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.AsyncRemoteServerException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.AsyncServerRequest;
import com.pokegoapi.main.ServerRequest;

import lombok.Getter;

import rx.functions.Func1;

/**
 * Created by rama on 27/07/16.
 */
public class Settings {

	private final PokemonGo api;


	@Getter
	/**
	 * Settings for various parameters on map
	 *
	 * @return MapSettings instance.
	 */
	private final MapSettings mapSettings;

	@Getter
	/**
	 * Settings for various parameters during levelup
	 *
	 * @return LevelUpSettings instance.
	 */
	private final LevelUpSettings levelUpSettings;

	@Getter
	/**
	 * Settings for various parameters during levelup
	 *
	 * @return LevelUpSettings instance.
	 */
	private final FortSettings fortSettings;


	@Getter
	/**
	 * Settings for various parameters during levelup
	 *
	 * @return LevelUpSettings instance.
	 */
	private final InventorySettings inventorySettings;

	@Getter
	/**
	 * Settings for showing speed warnings
	 *
	 * @return GpsSettings instance.
	 */
	private final GpsSettings gpsSettings;


	/**
	 * Settings object that hold different configuration aspect of the game.
	 * Can be used to simulate the real app behaviour.
	 *
	 * @param api api instance
	 * @throws LoginFailedException  If login failed.
	 * @throws RemoteServerException If server communications failed.
	 */
	public Settings(PokemonGo api) throws LoginFailedException, RemoteServerException {
		this.api = api;
		this.mapSettings = new MapSettings();
		this.levelUpSettings = new LevelUpSettings();
		this.fortSettings = new FortSettings();
		this.inventorySettings = new InventorySettings();
		this.gpsSettings = new GpsSettings();
		updateSettings();
	}

	/**
	 * Updates settings latest data.
	 *
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void updateSettings() throws RemoteServerException, LoginFailedException {
		DownloadSettingsMessage msg = DownloadSettingsMessage.newBuilder().build();
		ServerRequest serverRequest = new ServerRequest(RequestType.DOWNLOAD_SETTINGS, msg);
		api.getRequestHandler().sendServerRequests(serverRequest); //here you marked everything as read
		try {
			DownloadSettingsResponse response = DownloadSettingsResponse.parseFrom(serverRequest.getData());

			mapSettings.update(response.getSettings().getMapSettings());
			levelUpSettings.update(response.getSettings().getInventorySettings());
			fortSettings.update(response.getSettings().getFortSettings());
			inventorySettings.update(response.getSettings().getInventorySettings());
			gpsSettings.update(response.getSettings().getGpsSettings());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}
	}

	/**
	 * Updates settings latest data.
	 *
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void updateSettingsAsync() throws RemoteServerException, LoginFailedException {
		DownloadSettingsMessage msg = DownloadSettingsMessage.newBuilder().build();
		AsyncServerRequest asyncServerRequest = new AsyncServerRequest(RequestType.DOWNLOAD_SETTINGS, msg);
		DownloadSettingsResponse response = api.getRequestHandler()
				.sendAsyncServerRequests(asyncServerRequest)
				.map(new Func1<ByteString, DownloadSettingsResponse>() {

					@Override
					public DownloadSettingsResponse call(ByteString response) {
						try {
							return DownloadSettingsResponse.parseFrom(response);
						} catch (InvalidProtocolBufferException e) {
							throw new AsyncRemoteServerException(e);
						}
					}

				}).toBlocking().first();

		mapSettings.update(response.getSettings().getMapSettings());
		levelUpSettings.update(response.getSettings().getInventorySettings());
		fortSettings.update(response.getSettings().getFortSettings());
		inventorySettings.update(response.getSettings().getInventorySettings());
		gpsSettings.update(response.getSettings().getGpsSettings());
	}

}
