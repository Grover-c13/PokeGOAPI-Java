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

import POGOProtos.Enums.TutorialStateOuterClass.TutorialState;
import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo;
import POGOProtos.Networking.Envelopes.SignatureOuterClass;
import POGOProtos.Networking.Requests.Messages.CheckChallengeMessageOuterClass.CheckChallengeMessage;
import POGOProtos.Networking.Requests.Messages.DownloadItemTemplatesMessageOuterClass.DownloadItemTemplatesMessage;
import POGOProtos.Networking.Requests.Messages.LevelUpRewardsMessageOuterClass.LevelUpRewardsMessage;
import POGOProtos.Networking.Requests.Messages.VerifyChallengeMessageOuterClass.VerifyChallengeMessage;
import POGOProtos.Networking.Requests.RequestTypeOuterClass.RequestType;
import POGOProtos.Networking.Responses.CheckChallengeResponseOuterClass.CheckChallengeResponse;
import POGOProtos.Networking.Responses.DownloadRemoteConfigVersionResponseOuterClass.DownloadRemoteConfigVersionResponse;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse;
import POGOProtos.Networking.Responses.LevelUpRewardsResponseOuterClass.LevelUpRewardsResponse.Result;
import POGOProtos.Networking.Responses.VerifyChallengeResponseOuterClass.VerifyChallengeResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pokegoapi.api.device.ActivityStatus;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.device.LocationFixes;
import com.pokegoapi.api.device.SensorInfo;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.listener.Listener;
import com.pokegoapi.api.listener.LocationListener;
import com.pokegoapi.api.listener.LoginListener;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.map.Point;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.settings.Settings;
import com.pokegoapi.auth.CredentialProvider;
import com.pokegoapi.exceptions.request.RequestFailedException;
import com.pokegoapi.main.CommonRequests;
import com.pokegoapi.main.Heartbeat;
import com.pokegoapi.main.PokemonMeta;
import com.pokegoapi.main.RequestHandler;
import com.pokegoapi.main.ServerRequest;
import com.pokegoapi.main.ServerRequestEnvelope;
import com.pokegoapi.util.ClientInterceptor;
import com.pokegoapi.util.SystemTimeImpl;
import com.pokegoapi.util.Time;
import com.pokegoapi.util.hash.HashProvider;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class PokemonGo {

	private static final java.lang.String TAG = PokemonGo.class.getSimpleName();
	private final Time time;
	@Getter
	private long startTime;
	@Getter
	private final byte[] sessionHash;
	@Getter
	RequestHandler requestHandler;
	@Getter
	private PlayerProfile playerProfile;
	@Getter
	private Inventories inventories;
	@Getter
	private double latitude;
	@Getter
	private double longitude;
	@Getter
	@Setter
	private double altitude;
	@Getter
	@Setter
	private double accuracy = 65;
	private CredentialProvider credentialProvider;
	@Getter
	private Settings settings;
	private Map map;
	@Setter
	private DeviceInfo deviceInfo;
	@Getter
	@Setter
	public SensorInfo sensorInfo;
	@Getter
	@Setter
	public ActivityStatus activityStatus;
	@Setter
	@Getter
	private long seed;
	@Getter
	@Setter
	public LocationFixes locationFixes;

	@Setter
	private boolean hasChallenge;
	@Getter
	private String challengeURL;
	private final Object challengeLock = new Object();

	@Getter
	private List<Listener> listeners = Collections.synchronizedList(new ArrayList<Listener>());

	private final Object lock = new Object();

	@Getter
	private boolean loggingIn;
	@Getter
	private boolean active;

	@Getter
	private Heartbeat heartbeat = new Heartbeat(this);

	@Getter
	private HashProvider hashProvider;

	private OkHttpClient client;

	/**
	 * Ptr8 is only sent with the first Get Map Object,
	 * we need a flag to tell us if it has already been sent.
	 * After that, GET_MAP_OBJECTS is sent with common requests.
	 */
	@Getter
	@Setter
	private boolean firstGMO = true;
	/**
	 * Ptr8 is only sent with the first Get Player request,
	 * we need a flag to tell us if it has already been sent.
	 * after that, GET_PLAYER  is sent with common requests.
	 */
	@Getter
	@Setter
	private boolean firstGP = true;

	/**
	 * Instantiates a new Pokemon go.
	 *
	 * @param client the http client
	 * @param time a time implementation
	 * @param seed the seed to generate same device
	 */
	public PokemonGo(OkHttpClient client, Time time, long seed) {
		this.time = time;
		this.seed = seed;
		sessionHash = new byte[32];
		new Random().nextBytes(sessionHash);
		inventories = new Inventories(this);
		settings = new Settings(this);
		playerProfile = new PlayerProfile(this);
		map = new Map(this);
		longitude = Double.NaN;
		latitude = Double.NaN;
		this.client = client.newBuilder()
				.addNetworkInterceptor(new ClientInterceptor())
				.build();
	}

	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param client the http client
	 * @param seed the seed to generate same device
	 */
	public PokemonGo(OkHttpClient client, long seed) {
		this(client, new SystemTimeImpl(), seed);
	}

	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param client the http client
	 * @param time a time implementation
	 */
	public PokemonGo(OkHttpClient client, Time time) {
		this(client, time, hash(UUID.randomUUID().toString()));
	}

	/**
	 * Instantiates a new Pokemon go.
	 * Deprecated: specify a time implementation
	 *
	 * @param client the http client
	 */
	public PokemonGo(OkHttpClient client) {
		this(client, new SystemTimeImpl(), hash(UUID.randomUUID().toString()));
	}

	/**
	 * Login user with the provided provider
	 *
	 * @param credentialProvider the credential provider
	 * @param hashProvider to provide hashes
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void login(CredentialProvider credentialProvider, HashProvider hashProvider)
			throws RequestFailedException {
		this.loggingIn = true;
		if (credentialProvider == null) {
			throw new NullPointerException("Credential Provider can not be null!");
		} else if (hashProvider == null) {
			throw new NullPointerException("Hash Provider can not be null!");
		}
		this.credentialProvider = credentialProvider;
		this.hashProvider = hashProvider;

		startTime = currentTimeMillis();
		initialize();
	}

	private void initialize() throws RequestFailedException {
		if (getRequestHandler() != null) {
			getRequestHandler().exit();
		}

		requestHandler = new RequestHandler(this, client);

		getRequestHandler().sendServerRequests(ServerRequestEnvelope.create());

		playerProfile.updateProfile();

		ServerRequest downloadConfigRequest = new ServerRequest(RequestType.DOWNLOAD_REMOTE_CONFIG_VERSION,
				CommonRequests.getDownloadRemoteConfigVersionMessageRequest(this));
		getRequestHandler().sendServerRequests(downloadConfigRequest, true, RequestType.GET_BUDDY_WALKED,
				RequestType.GET_INCENSE_POKEMON);
		getAssetDigest();

		try {
			ByteString configVersionData = downloadConfigRequest.getData();
			if (PokemonMeta.checkVersion(DownloadRemoteConfigVersionResponse.parseFrom(configVersionData))) {
				DownloadItemTemplatesMessage message = CommonRequests.getDownloadItemTemplatesRequest();
				ServerRequest request = new ServerRequest(RequestType.DOWNLOAD_ITEM_TEMPLATES, message);
				PokemonMeta.update(getRequestHandler().sendServerRequests(request, true), true);
			}
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		playerProfile.getProfile();

		try {
			LevelUpRewardsMessage rewardsMessage = LevelUpRewardsMessage.newBuilder()
					.setLevel(playerProfile.getLevel())
					.build();
			ServerRequestEnvelope envelope = ServerRequestEnvelope.createCommons();
			ServerRequest request = envelope.add(RequestType.LEVEL_UP_REWARDS, rewardsMessage);
			getRequestHandler().sendServerRequests(envelope);
			LevelUpRewardsResponse levelUpRewardsResponse = LevelUpRewardsResponse.parseFrom(request.getData());
			if (levelUpRewardsResponse.getResult() == Result.SUCCESS) {
				inventories.getItemBag().addAwardedItems(levelUpRewardsResponse);
			}
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}

		List<LoginListener> loginListeners = getListeners(LoginListener.class);

		for (LoginListener listener : loginListeners) {
			listener.onLogin(this);
		}

		loggingIn = false;
		active = true;

		// From now one we will start to check our accounts is ready to fire requests.
		// Actually, we can receive valid responses even with this first check,
		// that mark the tutorial state into LEGAL_SCREEN.
		// Following, we are going to check if the account binded to this session
		// have an avatar, a nickname, and all the other things that are usually filled
		// on the official client BEFORE sending any requests such as the getMapObject etc.
		ArrayList<TutorialState> tutorialStates = playerProfile.getTutorialState().getTutorialStates();
		if (tutorialStates.isEmpty()) {
			playerProfile.activateAccount();
		}

		if (!tutorialStates.contains(TutorialState.AVATAR_SELECTION)) {
			playerProfile.setupAvatar();
		}

		heartbeat.start();

		if (!tutorialStates.contains(TutorialState.POKEMON_CAPTURE)) {
			playerProfile.encounterTutorialComplete();
		}

		int remainingCodenameClaims = getPlayerProfile().getPlayerData().getRemainingCodenameClaims();
		if (!tutorialStates.contains(TutorialState.NAME_SELECTION) && remainingCodenameClaims > 0) {
			playerProfile.claimCodeName();
		}

		if (!tutorialStates.contains(TutorialState.FIRST_TIME_EXPERIENCE_COMPLETE)) {
			playerProfile.firstTimeExperienceComplete();
		}
	}

	/**
	 * Second requests block. Public since it could be re-fired at any time
	 *
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public void getAssetDigest() throws RequestFailedException {
		ServerRequestEnvelope envelope = ServerRequestEnvelope.createCommons(RequestType.GET_BUDDY_WALKED,
				RequestType.GET_INCENSE_POKEMON);
		envelope.add(RequestType.GET_ASSET_DIGEST, CommonRequests.getGetAssetDigestMessageRequest(this));
		getRequestHandler().sendServerRequests(envelope);
	}

	/**
	 * Hash the given string
	 *
	 * @param string string to hash
	 * @return the hashed long
	 */
	private static long hash(String string) {
		long upper = ((long) string.hashCode()) << 32;
		int len = string.length();
		StringBuilder dest = new StringBuilder(len);

		for (int index = (len - 1); index >= 0; index--) {
			dest.append(string.charAt(index));
		}
		long lower = ((long) dest.toString().hashCode()) - ((long) Integer.MIN_VALUE);
		return upper + lower;
	}

	/**
	 * Fetches valid AuthInfo
	 *
	 * @param refresh if the AuthInfo object should be refreshed
	 * @return AuthInfo object
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public AuthInfo getAuthInfo(boolean refresh)
			throws RequestFailedException {
		return credentialProvider.getAuthInfo(refresh);
	}

	/**
	 * Sets location.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param altitude the altitude
	 */
	public void setLocation(double latitude, double longitude, double altitude) {
		setLocation(latitude, longitude, altitude, accuracy);
	}

	/**
	 * Sets location with accuracy.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param altitude the altitude
	 * @param accuracy the accuracy of this location
	 */
	public void setLocation(double latitude, double longitude, double altitude, double accuracy) {
		setLatitude(latitude);
		setLongitude(longitude);
		setAltitude(altitude);
		setAccuracy(accuracy);
	}

	public long currentTimeMillis() {
		return time.currentTimeMillis();
	}

	/**
	 * Validates and sets a given latitude value
	 *
	 * @param value the latitude
	 * @throws IllegalArgumentException if value exceeds +-90
	 */
	public void setLatitude(double value) {
		if (value > 90 || value < -90) {
			throw new IllegalArgumentException("latittude can not exceed +/- 90");
		}
		latitude = value;

		if (active && !Double.isNaN(latitude) && !Double.isNaN(longitude)) {
			if (!heartbeat.active()) {
				heartbeat.start();
			} else {
				heartbeat.beat();
			}
		}

		for (LocationListener listener : this.getListeners(LocationListener.class)) {
			listener.onLocationUpdate(this, getPoint());
		}
	}

	/**
	 * Validates and sets a given longitude value
	 *
	 * @param value the longitude
	 * @throws IllegalArgumentException if value exceeds +-180
	 */
	public void setLongitude(double value) {
		if (value > 180 || value < -180) {
			throw new IllegalArgumentException("longitude can not exceed +/- 180");
		}
		longitude = value;

		if (active && !Double.isNaN(latitude) && !Double.isNaN(longitude)) {
			if (!heartbeat.active()) {
				heartbeat.start();
			} else {
				heartbeat.beat();
			}
		}

		for (LocationListener listener : this.getListeners(LocationListener.class)) {
			listener.onLocationUpdate(this, getPoint());
		}
	}

	/**
	 * Gets the map API
	 *
	 * @return the map
	 * @throws IllegalStateException if location has not been set
	 */
	public Map getMap() {
		if (this.latitude == Double.NaN || this.longitude == Double.NaN) {
			throw new IllegalStateException("Attempt to get map without setting location first");
		}
		return map;
	}

	/**
	 * Gets the device info
	 *
	 * @return the device info
	 */
	public SignatureOuterClass.Signature.DeviceInfo getDeviceInfo() {
		if (deviceInfo == null) {
			deviceInfo = DeviceInfo.getDefault(this);
		}
		return deviceInfo.getDeviceInfo();
	}

	/**
	 * Gets the sensor info
	 *
	 * @param currentTime the current time
	 * @param random the random object
	 * @return the sensor info
	 */
	public SignatureOuterClass.Signature.SensorInfo getSensorSignature(long currentTime, Random random) {
		if (this.sensorInfo == null || sensorInfo.getTimestampCreate() != 0L) {
			return SensorInfo.getDefault(this, currentTime, random);
		}
		return sensorInfo.getSensorInfo();
	}

	/**
	 * Gets the activity status
	 *
	 * @param random the random object
	 * @return the activity status
	 */
	public SignatureOuterClass.Signature.ActivityStatus getActivitySignature(Random random) {
		if (this.activityStatus == null) {
			return ActivityStatus.getDefault(this, random);
		}
		return activityStatus.getActivityStatus();
	}

	/**
	 * Updates the current challenge
	 *
	 * @param url the challenge url, if any
	 * @param hasChallenge whether the challenge solve is required
	 */
	public void updateChallenge(String url, boolean hasChallenge) {
		this.hasChallenge = hasChallenge;
		this.challengeURL = url;
		if (hasChallenge) {
			List<LoginListener> listeners = getListeners(LoginListener.class);
			for (LoginListener listener : listeners) {
				listener.onChallenge(this, url);
			}
		} else {
			synchronized (challengeLock) {
				challengeLock.notifyAll();
			}
		}
	}

	/**
	 * Registers the given listener to this api.
	 *
	 * @param listener the listener to register
	 */
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the given listener from this api.
	 *
	 * @param listener the listener to remove
	 */
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	/**
	 * Returns all listeners for the given type.
	 *
	 * @param listenerType the type of listeners to return
	 * @param <T> the listener type
	 * @return all listeners for the given type
	 */
	public <T extends Listener> List<T> getListeners(Class<T> listenerType) {
		List<T> listeners = new ArrayList<T>();
		synchronized (this.lock) {
			for (Listener listener : this.listeners) {
				if (listenerType.isAssignableFrom(listener.getClass())) {
					listeners.add((T) listener);
				}
			}
		}
		return listeners;
	}

	/**
	 * Invokes a method in all listeners of the given type
	 *
	 * @param listenerType the listener to call to
	 * @param name the method name to call
	 * @param parameters the parameters to pass to the method
	 * @param <T> the listener type
	 * @throws ReflectiveOperationException if an exception occurred while invoking the listener
	 */
	public <T extends Listener> void callListener(Class<T> listenerType, String name, Object... parameters)
			throws ReflectiveOperationException {
		Class[] parameterTypes = new Class[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			Object parameter = parameters[i];
			parameterTypes[i] = parameter.getClass();
		}
		Method method = listenerType.getMethod(name, parameterTypes);
		if (method != null) {
			List<T> listeners = getListeners(listenerType);
			for (T listener : listeners) {
				method.invoke(listener, parameters);
			}
		} else {
			throw new NoSuchMethodException("Method \"" + name + "\" does not exist");
		}
	}

	/**
	 * @return if there is an active challenge required. Challenge accessible via getChallengeURL.
	 */
	public boolean hasChallenge() {
		return this.hasChallenge;
	}

	/**
	 * Verifies the current challenge with the given token.
	 *
	 * @param token the challenge response token
	 * @return if the token was valid or not
	 * @throws RequestFailedException if an exception occurred while sending requests
	 */
	public boolean verifyChallenge(String token) throws RequestFailedException {
		hasChallenge = false;
		VerifyChallengeMessage message = VerifyChallengeMessage.newBuilder().setToken(token).build();
		ServerRequest request = new ServerRequest(RequestType.VERIFY_CHALLENGE, message);
		ByteString responseData = getRequestHandler().sendServerRequests(request, true);
		try {
			VerifyChallengeResponse response = VerifyChallengeResponse.parseFrom(responseData);
			hasChallenge = !response.getSuccess();
			if (!hasChallenge) {
				challengeURL = null;
				synchronized (challengeLock) {
					challengeLock.notifyAll();
				}
			}
			return response.getSuccess();
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
	}

	/**
	 * Checks for a challenge / captcha
	 *
	 * @return the new challenge URL, if any
	 * @throws RequestFailedException if an exception occurred while sending requests
	 * @deprecated CHECK_CHALLENGE is sent as a common request, should not be needed
	 */
	@Deprecated
	public String checkChallenge() throws RequestFailedException {
		CheckChallengeMessage message = CheckChallengeMessage.newBuilder().build();
		try {
			ServerRequest request = new ServerRequest(RequestType.CHECK_CHALLENGE, message);
			ByteString responseData = getRequestHandler().sendServerRequests(request, false);
			CheckChallengeResponse response = CheckChallengeResponse.parseFrom(responseData);
			String newChallenge = response.getChallengeUrl();
			if (response.getShowChallenge() && newChallenge != null && newChallenge.length() > 0) {
				updateChallenge(newChallenge, true);
				return newChallenge;
			}
		} catch (InvalidProtocolBufferException e) {
			throw new RequestFailedException(e);
		}
		return null;
	}

	/**
	 * @return the current player position in Point form
	 */
	public Point getPoint() {
		return new Point(this.getLatitude(), this.getLongitude());
	}

	/**
	 * Blocks this thread until the current challenge is solved
	 *
	 * @throws InterruptedException if this thread is interrupted while blocking
	 */
	public void awaitChallenge() throws InterruptedException {
		if (hasChallenge()) {
			synchronized (challengeLock) {
				challengeLock.wait();
			}
		}
	}

	/**
	 * Enqueues the given task
	 *
	 * @param task the task to enqueue
	 */
	public void enqueueTask(Runnable task) {
		heartbeat.enqueueTask(task);
	}

	/**
	 * @return the version of the API being used
	 */
	public int getVersion() {
		return hashProvider.getHashVersion();
	}

	/**
	 * Exits this API
	 */
	public void exit() {
		if (active) {
			heartbeat.exit();
			requestHandler.exit();
			active = false;
		}
	}
}
