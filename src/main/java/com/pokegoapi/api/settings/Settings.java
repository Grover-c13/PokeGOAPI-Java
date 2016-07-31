package com.pokegoapi.api.settings;

import POGOProtos.Networking.Requests.Messages.DownloadSettingsMessageOuterClass;
import POGOProtos.Networking.Requests.RequestTypeOuterClass;
import POGOProtos.Networking.Responses.DownloadSettingsResponseOuterClass;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.async.AsyncDataObject;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.pokegoapi.main.ServerRequest;
import lombok.Getter;
import rx.Observable;

/**
 * Created by rama on 27/07/16.
 */
public class Settings extends AsyncDataObject<Settings> {

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
	public Settings(final PokemonGo api) {
		super(api);
		this.mapSettings = new MapSettings();
		this.levelUpSettings = new LevelUpSettings();
		this.fortSettings = new FortSettings();
		this.inventorySettings = new InventorySettings();
	}

	/**
	 * Updates settings latest data.
	 *
	 * @throws LoginFailedException  the login failed exception
	 * @throws RemoteServerException the remote server exception
	 */
	public Settings refreshDataSync() throws RemoteServerException, LoginFailedException {

		final ServerRequest serverRequest = makeServerRequest();
		getApi().getRequestHandler().sendServerRequests(serverRequest);
		return updateInstanceData(serverRequest);
	}

	@Override
	protected synchronized Settings updateInstanceData(final ServerRequest... serverRequests)
			throws LoginFailedException, RemoteServerException
	{
		DownloadSettingsResponseOuterClass.DownloadSettingsResponse response;
		try {
			response = DownloadSettingsResponseOuterClass.DownloadSettingsResponse.parseFrom(serverRequests[0].getData());
		} catch (InvalidProtocolBufferException e) {
			return refreshDataSync();
		}

		mapSettings.update(response.getSettings().getMapSettings());
		levelUpSettings.update(response.getSettings().getInventorySettings());
		fortSettings.update(response.getSettings().getFortSettings());
		inventorySettings.update(response.getSettings().getInventorySettings());

		return this;
	}

	private ServerRequest makeServerRequest() {
		DownloadSettingsMessageOuterClass.DownloadSettingsMessage msg =
				DownloadSettingsMessageOuterClass.DownloadSettingsMessage.newBuilder().build();
		return new ServerRequest(RequestTypeOuterClass.RequestType.DOWNLOAD_SETTINGS, msg);
	}

	@Override
	public Observable<Settings> refreshData() {
		return sendAsyncServerRequests(makeServerRequest()).cast(Settings.class);
	}

	@Override
	public Settings getInstance() {
		return this;
	}
}
