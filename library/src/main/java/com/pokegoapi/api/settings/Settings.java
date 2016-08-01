package com.pokegoapi.api.settings;

import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;

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


	/**
	 * Settings object that hold different configuration aspect of the game.
	 * Can be used to simulate the real app behaviour.
     *
     * @param api api instance
     * @throws LoginFailedException If login failed.
     * @throws RemoteServerException If server communications failed.
	 */
	public Settings(PokemonGo api) throws LoginFailedException, RemoteServerException {
		this.api = api;
		this.mapSettings = new MapSettings();
		this.levelUpSettings = new LevelUpSettings();
		this.fortSettings = new FortSettings();
		this.inventorySettings = new InventorySettings();
		updateSettings();
	}

	/**
	 * Updates settings latest data.
	 *
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public void updateSettings() throws RemoteServerException, LoginFailedException {
		DownloadSettingsMessageOuterClass.DownloadSettingsMessage msg =
				DownloadSettingsMessageOuterClass.DownloadSettingsMessage.newBuilder().build();
		ServerRequest serverRequest = new ServerRequest(RequestTypeOuterClass.RequestType.DOWNLOAD_SETTINGS, msg);
		api.getRequestHandler().sendServerRequests(serverRequest); //here you marked everything as read
		DownloadSettingsResponseOuterClass.DownloadSettingsResponse response;
		try {
			response = DownloadSettingsResponseOuterClass.DownloadSettingsResponse.parseFrom(serverRequest.getData());
		} catch (InvalidProtocolBufferException e) {
			throw new RemoteServerException(e);
		}

		mapSettings.update(response.getSettings().getMapSettings());
		levelUpSettings.update(response.getSettings().getInventorySettings());
		fortSettings.update(response.getSettings().getFortSettings());
		inventorySettings.update(response.getSettings().getInventorySettings());

	}


}
